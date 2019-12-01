package com.palidinodh.osrsscript.player.plugin.lootingbag;

import java.util.Map;
import com.palidinodh.osrscore.io.ValueEnteredEvent;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.ScriptId;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.dialogue.DialogueOption;
import com.palidinodh.osrscore.model.dialogue.OptionsDialogue;
import com.palidinodh.osrscore.model.item.Item;
import com.palidinodh.osrscore.model.item.ItemDef;
import com.palidinodh.osrscore.model.item.ItemList;
import com.palidinodh.osrscore.model.map.MapItem;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.PlayerPlugin;
import com.palidinodh.osrsscript.incomingpacket.UseWidgetDecoder;
import lombok.Setter;
import lombok.var;

public class LootingBagPlugin extends PlayerPlugin {
  private transient Player player;

  private ItemList items;
  @Setter
  private LootingBagStoreType storeType = LootingBagStoreType.ASK;

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
      storeType = LootingBagStoreType.values()[(int) map.get("widgetManager.lootingBagStoreType")];
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
  public boolean widgetHook(int option, int widgetId, int childId, int slot, int itemId) {
    if (widgetId == WidgetId.INVENTORY) {
      switch (itemId) {
        case ItemId.LOOTING_BAG:
        case ItemId.LOOTING_BAG_22586:
          if (option == 0) {
            player.getInventory().deleteItem(itemId, 1, slot);
            if (itemId == ItemId.LOOTING_BAG) {
              player.getInventory().addItem(ItemId.LOOTING_BAG_22586, 1, slot);
            } else {
              player.getInventory().addItem(ItemId.LOOTING_BAG, 1, slot);
            }
          } else if (option == 1) {
            getItems().displayItemList();
          } else if (option == 2) {
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
          } else if (option == 3) {
            player.openDialogue(new OptionsDialogue(
                new DialogueOption("Always Ask", (c, s) -> setStoreType(LootingBagStoreType.ASK)),
                new DialogueOption("Always Store-1",
                    (c, s) -> setStoreType(LootingBagStoreType.STORE_1)),
                new DialogueOption("Always Store-5",
                    (c, s) -> setStoreType(LootingBagStoreType.STORE_5)),
                new DialogueOption("Always Store-All",
                    (c, s) -> setStoreType(LootingBagStoreType.STORE_ALL)),
                new DialogueOption("Always Store-X",
                    (c, s) -> setStoreType(LootingBagStoreType.STORE_X))));
          }
          return true;
      }
    } else if (widgetId == WidgetId.LOOTING_BAG_DEPOSIT) {
      if (player.isLocked()) {
        return false;
      }
      switch (childId) {
        case 5:
          if (option == 0) {
            storeItemFromInventory(slot, LootingBagStoreType.STORE_1);
          } else if (option == 1) {
            storeItemFromInventory(slot, LootingBagStoreType.STORE_5);
          } else if (option == 2) {
            storeItemFromInventory(slot, LootingBagStoreType.STORE_ALL);
          } else if (option == 3) {
            storeItemFromInventory(slot, LootingBagStoreType.STORE_X);
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
        if (storeType == LootingBagStoreType.ASK) {
          player.openDialogue(new OptionsDialogue(
              new DialogueOption("Store-1",
                  (c, s) -> storeItemFromInventory(itemSlot, LootingBagStoreType.STORE_1)),
              new DialogueOption("Store-5",
                  (c, s) -> storeItemFromInventory(itemSlot, LootingBagStoreType.STORE_5)),
              new DialogueOption("Store-All",
                  (c, s) -> storeItemFromInventory(itemSlot, LootingBagStoreType.STORE_ALL)),
              new DialogueOption("Store-X",
                  (c, s) -> storeItemFromInventory(itemSlot, LootingBagStoreType.STORE_X))));
          return true;
        } else if (storeType == LootingBagStoreType.STORE_1) {
          storeItemFromInventory(itemSlot, LootingBagStoreType.STORE_1);
        } else if (storeType == LootingBagStoreType.STORE_5) {
          storeItemFromInventory(itemSlot, LootingBagStoreType.STORE_5);
        } else if (storeType == LootingBagStoreType.STORE_ALL) {
          storeItemFromInventory(itemSlot, LootingBagStoreType.STORE_ALL);
        } else if (storeType == LootingBagStoreType.STORE_X) {
          storeItemFromInventory(itemSlot, LootingBagStoreType.STORE_X);
        }
        return true;
      }
    }
    return false;
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

  private ItemList getItems() {
    if (items == null) {
      items = (ItemList) new ItemList(28).setName("looting bag").setWidget(-1, 93);
    }
    items.setPlayer(player);
    return items;
  }

  private void storeItemFromInventory(int itemSlot, LootingBagStoreType storeType) {
    var itemId = player.getInventory().getId(itemSlot);
    var amount = 0;
    if (storeType == LootingBagStoreType.STORE_1) {
      amount = 1;
    } else if (storeType == LootingBagStoreType.STORE_5) {
      amount = 5;
    } else if (storeType == LootingBagStoreType.STORE_ALL) {
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
    if (storeType == LootingBagStoreType.ASK || storeType == LootingBagStoreType.STORE_X) {
      player.getGameEncoder().sendEnterAmount(valueEntered);
    } else {
      valueEntered.execute(amount);
    }
  }

  private boolean canStoreItem(int itemId, boolean sendMessage) {
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
}
