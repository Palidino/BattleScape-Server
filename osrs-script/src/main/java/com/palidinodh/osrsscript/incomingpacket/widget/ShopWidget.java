package com.palidinodh.osrsscript.incomingpacket.widget;

import com.palidinodh.osrscore.io.ValueEnteredEvent;
import com.palidinodh.osrscore.io.incomingpacket.WidgetHandler;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.item.ItemDef;
import com.palidinodh.osrscore.model.npc.NKillLog;
import com.palidinodh.osrscore.model.player.Loadout;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;

@ReferenceId({ WidgetId.TOURNAMENT_SUPPLIES, WidgetId.SHOP, WidgetId.SHOP_INVENTORY })
class ShopWidget implements WidgetHandler {
  @Override
  public void execute(Player player, int option, int widgetId, int childId, int slot, int itemId) {
    if (player.isLocked()) {
      return;
    }
    if (widgetId == WidgetId.TOURNAMENT_SUPPLIES) {
      switch (childId) {
        case 4:
          if (player.getShop() == null) {
            break;
          }
          if (option == 0) {
            player.getShop().sendShopPrice(player, itemId, slot);
          } else if (option == 1) {
            player.getShop().buyShopItem(player, itemId, slot, 1);
          } else if (option == 2) {
            player.getShop().buyShopItem(player, itemId, slot, 5);
          } else if (option == 3) {
            player.getShop().buyShopItem(player, itemId, slot, 10);
          } else if (option == 4) {
            player.getShop().buyShopItem(player, itemId, slot, 50);
          } else if (option == 5) {
            player.getGameEncoder().sendEnterAmount(new ValueEnteredEvent.IntegerEvent() {
              @Override
              public void execute(int value) {
                player.getShop().buyShopItem(player, itemId, slot, value);
              }
            });
          }
          break;
        case 31:
          if (option == 0) {
            player.getLoadout().preview(slot);
          } else if (option == 1) {
            player.getLoadout().setAsQuick(slot - Loadout.OFFSET);
          } else if (option == 2) {
            player.getLoadout().rename(slot - Loadout.OFFSET);
          } else if (option == 3) {
            player.getLoadout().replace(slot - Loadout.OFFSET);
          } else if (option == 4) {
            player.getLoadout().insertNew(slot - Loadout.OFFSET);
          } else if (option == 5) {
            player.getLoadout().remove(slot - Loadout.OFFSET);
          }
          break;
        case 33:
          player.getLoadout().apply();
          break;
      }
    } else if (widgetId == WidgetId.SHOP) {
      switch (childId) {
        case 16:
          if (player.getGrandExchange().getViewingUserId() != -1) {
            if (option == 0) {
              player.getGrandExchange().sendShopPrice(itemId, slot - 1);
            } else if (option == 1) {
              player.getGrandExchange().buyShopItem(itemId, slot - 1, 1);
            } else if (option == 2) {
              player.getGrandExchange().buyShopItem(itemId, slot - 1, 5);
            } else if (option == 3) {
              player.getGrandExchange().buyShopItem(itemId, slot - 1, 10);
            } else if (option == 4) {
              player.getGameEncoder().sendEnterAmount(new ValueEnteredEvent.IntegerEvent() {
                @Override
                public void execute(int value) {
                  player.getGrandExchange().buyShopItem(itemId, slot - 1, value);
                }
              });
            }
          } else if (player.getShop() == null) {
            NKillLog nKillLog = player.getCombat().getNPCKillLog();
            if (nKillLog == null || nKillLog.getItems() == null) {
              return;
            }
            player.getGameEncoder()
                .sendMessage(ItemDef.getName(itemId) + ": " + nKillLog.getItemOnCounts(itemId));
            return;
          }
          break;
      }
    } else if (widgetId == WidgetId.SHOP_INVENTORY) {
      switch (childId) {
        case 0:
          if (player.getShop() != null) {
            if (option == 0) {
              player.getShop().sendInventoryPrice(player, itemId, slot);
            } else if (option == 1) {
              player.getShop().sellInventoryItem(player, itemId, slot, 1);
            } else if (option == 2) {
              player.getShop().sellInventoryItem(player, itemId, slot, 5);
            } else if (option == 3) {
              player.getShop().sellInventoryItem(player, itemId, slot, 10);
            } else if (option == 4) {
              player.getShop().sellInventoryItem(player, itemId, slot, 50);
            } else if (option == 5) {
              player.getGameEncoder().sendEnterAmount(new ValueEnteredEvent.IntegerEvent() {
                @Override
                public void execute(int value) {
                  player.getShop().sellInventoryItem(player, itemId, slot, value);
                }
              });
            }
          } else if (player.getGrandExchange().getViewingUserId() != -1) {
            if (option == 0) {
              player.getGrandExchange().sendInventoryPrice(itemId, slot);
            } else if (option == 1) {
              player.getGrandExchange().sellInventoryItem(itemId, slot, 1);
            } else if (option == 2) {
              player.getGrandExchange().sellInventoryItem(itemId, slot, 5);
            } else if (option == 3) {
              player.getGrandExchange().sellInventoryItem(itemId, slot, 10);
            } else if (option == 4) {
              player.getGrandExchange().sellInventoryItem(itemId, slot, 50);
            } else if (option == 5) {
              player.getGameEncoder().sendEnterAmount(new ValueEnteredEvent.IntegerEvent() {
                @Override
                public void execute(int value) {
                  player.getGrandExchange().sellInventoryItem(itemId, slot, value);
                }
              });
            }
          }
          break;
      }
    }
  }
}
