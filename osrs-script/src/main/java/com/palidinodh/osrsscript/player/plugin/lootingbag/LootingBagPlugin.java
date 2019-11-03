package com.palidinodh.osrsscript.player.plugin.lootingbag;

import java.util.Map;
import com.palidinodh.osrscore.io.ValueEnteredEvent;
import com.palidinodh.osrscore.io.cache.ItemId;
import com.palidinodh.osrscore.io.cache.ScriptId;
import com.palidinodh.osrscore.io.cache.WidgetId;
import com.palidinodh.osrscore.model.item.Item;
import com.palidinodh.osrscore.model.item.ItemDef;
import com.palidinodh.osrscore.model.item.ItemList;
import com.palidinodh.osrscore.model.map.MapItem;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.PlayerPlugin;
import com.palidinodh.osrsscript.incomingpacket.UseWidgetDecoder;
import com.palidinodh.osrsscript.player.plugin.lootingbag.dialogue.StoreAskDialogue;
import com.palidinodh.osrsscript.player.plugin.lootingbag.dialogue.StoreTypeDialogue;
import lombok.Setter;
import lombok.var;

public class LootingBagPlugin extends PlayerPlugin {
  private transient Player player;

  private ItemList items;
  @Setter
  private StoreType storeType = StoreType.ASK;

  @Override
  public void login() {
    player = getPlayer();
  }

  @Override
  public void loadLegacy(Map<String, Object> map) {
    if (map.containsKey("combat.lootingBag")) {
      try {
        getItems().setItemsRaw(map.get("combat.lootingBag"));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    if (map.containsKey("widgetManager.lootingBagStoreType")) {
      storeType = StoreType.values()[(int) map.get("widgetManager.lootingBagStoreType")];
    }
  }

  @Override
  public Item[] getItemLostOnDeathHook(Item item) {
    if (item.getId() == ItemId.LOOTING_BAG || item.getId() == ItemId.LOOTING_BAG_22586) {
      Item[] lootingBagItems = getItems().toItemArray();
      getItems().clear();
      return lootingBagItems;
    }
    return null;
  }

  @Override
  public boolean pickupMapItemHook(MapItem mapItem) {
    var item = mapItem.getItem();
    if (!player.getInventory().hasItem(ItemId.LOOTING_BAG_22586)) {
      return false;
    }
    if (ItemDef.getStackable(item.getId()) && player.getInventory().hasItem(item.getId())) {
      return false;
    }
    if (!canStoreItem(item.getId(), false)) {
      return false;
    }
    if (!getItems().canAddItem(item)) {
      return false;
    }
    getItems().addItem(item);
    return true;
  }

  @Override
  public boolean widgetHook(int index, int widgetId, int childId, int slot, int itemId) {
    if (widgetId == WidgetId.INVENTORY) {
      switch (itemId) {
        case ItemId.LOOTING_BAG:
        case ItemId.LOOTING_BAG_22586:
          if (index == 0) {
            player.getInventory().deleteItem(itemId, 1, slot);
            if (itemId == ItemId.LOOTING_BAG) {
              player.getInventory().addItem(ItemId.LOOTING_BAG_22586, 1, slot);
            } else {
              player.getInventory().addItem(ItemId.LOOTING_BAG, 1, slot);
            }
          } else if (index == 1) {
            getItems().displayItemList();
          } else if (index == 2) {
            if (!player.getController().inWilderness() && !player.getController().inPvPWorld()) {
              player.getGameEncoder()
                  .sendMessage("You can't put items in the bag unless you're in the Wilderness.");
              return true;
            }
            player.getWidgetManager().sendInventoryOverlay(WidgetId.LOOTING_BAG_DEPOSIT);
            player.getGameEncoder().sendScript(ScriptId.LOOTING_BAG_DEPOSIT, 1, "Looting bag");
            player.getGameEncoder().sendWidgetSettings(WidgetId.LOOTING_BAG_DEPOSIT, 5, 0,
                player.getInventory().capacity() - 1, 1086);
            player.getInventory().setUpdate(true);
          } else if (index == 3) {
            new StoreTypeDialogue(player, this);
          }
          return true;
      }
    } else if (widgetId == WidgetId.LOOTING_BAG_DEPOSIT) {
      if (player.isLocked()) {
        return false;
      }
      switch (childId) {
        case 5:
          if (index == 0) {
            storeItemFromInventory(slot, StoreType.STORE_1);
          } else if (index == 1) {
            storeItemFromInventory(slot, StoreType.STORE_5);
          } else if (index == 2) {
            storeItemFromInventory(slot, StoreType.STORE_ALL);
          } else if (index == 3) {
            storeItemFromInventory(slot, StoreType.STORE_X);
          }
          return true;
      }
    }
    return false;
  }

  @Override
  public boolean widgetOnWidgetHook(int useWidgetId, int useChildId, int onWidgetId, int onChildId,
      int useSlot, int useItemId, int onSlot, int onItemId) {
    if (useWidgetId == WidgetId.INVENTORY && onWidgetId == WidgetId.INVENTORY) {
      if (UseWidgetDecoder.hasMatch(useItemId, onItemId, ItemId.LOOTING_BAG, -1)) {
        var itemSlot = useItemId != ItemId.LOOTING_BAG ? useSlot : onSlot;
        if (storeType == StoreType.ASK) {
          new StoreAskDialogue(player, this);
          player.putAttribute("looting_bag_item_slot", itemSlot);
          return true;
        } else if (storeType == StoreType.STORE_1) {
          storeItemFromInventory(itemSlot, StoreType.STORE_1);
        } else if (storeType == StoreType.STORE_5) {
          storeItemFromInventory(itemSlot, StoreType.STORE_5);
        } else if (storeType == StoreType.STORE_ALL) {
          storeItemFromInventory(itemSlot, StoreType.STORE_ALL);
        } else if (storeType == StoreType.STORE_X) {
          storeItemFromInventory(itemSlot, StoreType.STORE_X);
        }
        return true;
      }
    }
    return false;
  }

  public ItemList getItems() {
    if (items == null) {
      items = (ItemList) new ItemList(28).setName("looting bag").setWidget(-1, 93);
    }
    items.setPlayer(player);
    return items;
  }

  public void storeItemFromInventory(int itemSlot, StoreType storeType) {
    var itemId = player.getInventory().getId(itemSlot);
    var amount = 0;
    if (storeType == StoreType.STORE_1) {
      amount = 1;
    } else if (storeType == StoreType.STORE_5) {
      amount = 5;
    } else if (storeType == StoreType.STORE_ALL) {
      amount = ItemDef.getStackOrNote(itemId) ? player.getInventory().getAmount(itemSlot)
          : Item.MAX_AMOUNT;
    }
    var valueEntered = new ValueEnteredEvent.IntegerEvent() {
      @Override
      public void execute(int value) {
        if (!canStoreItem(itemId, true)) {
          return;
        }
        value = Math.min(value, player.getInventory().getCount(itemId));
        value = getItems().canAddAmount(itemId, value);
        if (value == 0) {
          getItems().notEnoughSpace();
          return;
        }
        player.getInventory().deleteItem(itemId, value, itemSlot);
        getItems().addItem(itemId, value);
      }
    };
    if (storeType == StoreType.ASK || storeType == StoreType.STORE_X) {
      player.getGameEncoder().sendEnterAmount(valueEntered);
    } else {
      valueEntered.execute(amount);
    }
  }

  public boolean canStoreItem(int itemId, boolean sendMessage) {
    if (!player.getController().inWilderness() && !player.getController().inPvPWorld()) {
      if (sendMessage) {
        player.getGameEncoder()
            .sendMessage("You can't put items in the bag unless you're in the Wilderness.");
      }
      return false;
    } else if (ItemDef.getUntradable(itemId)) {
      if (sendMessage) {
        player.getGameEncoder().sendMessage("You can only put tradeable items in the bag.");
      }
      return false;
    } else if (itemId == ItemId.BLOODY_KEY || itemId == ItemId.BLOODIER_KEY) {
      if (sendMessage) {
        player.getGameEncoder().sendMessage("This key can't be stored in the bag.");
      }
      return false;
    } else if (itemId == -1) {
      if (sendMessage) {
        player.getGameEncoder().sendMessage("Invalid item selected.");
      }
      return false;
    }
    return true;
  }

  public void bankItems() {
    while (!getItems().isEmpty()) {
      var slot = getItems().getFirstUsedSlot();
      if (!player.getBank().deposit(getItems(), getItems().getId(slot), slot,
          getItems().getAmount(slot))) {
        break;
      }
    }
  }
}
