package com.palidinodh.osrsscript.incomingpacket.widget;

import com.palidinodh.osrscore.io.ValueEnteredEvent;
import com.palidinodh.osrscore.io.incomingpacket.WidgetHandler;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.guide.Guide;
import com.palidinodh.osrscore.model.item.Item;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;
import com.palidinodh.rs.setting.Settings;

@ReferenceId(WidgetId.CUSTOM_BOND_POUCH)
class BondWidget implements WidgetHandler {
  @Override
  public void execute(Player player, int option, int widgetId, int childId, int slot, int itemId) {
    if (player.isLocked()) {
      return;
    }
    switch (childId) {
      case 28:
        int inventoryCount = player.getInventory().getCount(ItemId.BOND_32318);
        if (inventoryCount == 0) {
          player.getGameEncoder().sendMessage("You have no bonds in your inventory.");
          break;
        }
        if (option == 0) {
          player.getInventory().deleteItem(ItemId.BOND_32318, 1);
          player.getBonds().setPouch(player.getBonds().getPouch() + 1);
        } else if (option == 1) {
          player.getGameEncoder().sendEnterAmount(new ValueEnteredEvent.IntegerEvent() {
            @Override
            public void execute(int value) {
              int inventoryCount = player.getInventory().getCount(ItemId.BOND_32318);
              value = Math.min(value, inventoryCount);
              player.getInventory().deleteItem(ItemId.BOND_32318, value);
              player.getBonds().setPouch(player.getBonds().getPouch() + value);
              player.getBonds().sendPouchCounts();
            }
          });
        } else if (option == 2) {
          player.getInventory().deleteItem(ItemId.BOND_32318, inventoryCount);
          player.getBonds().setPouch(player.getBonds().getPouch() + inventoryCount);
        }
        break;
      case 29:
        if (player.getBonds().getPouch() == 0) {
          player.getGameEncoder().sendMessage("You have no bonds in your pouch.");
          break;
        }
        if (option == 0) {
          int maxWithdraw = (int) Math.min(player.getBonds().getPouch(), Item.MAX_AMOUNT);
          maxWithdraw = Math.min(1, maxWithdraw);
          maxWithdraw = player.getInventory().canAddAmount(ItemId.BOND_32318, maxWithdraw);
          if (maxWithdraw == 0) {
            player.getInventory().notEnoughSpace();
            break;
          }
          player.getBonds().setPouch(player.getBonds().getPouch() - maxWithdraw);
          player.getInventory().addItem(ItemId.BOND_32318, maxWithdraw);
        } else if (option == 1) {
          player.getGameEncoder().sendEnterAmount(new ValueEnteredEvent.IntegerEvent() {
            @Override
            public void execute(int value) {
              int maxWithdraw = (int) Math.min(player.getBonds().getPouch(), Item.MAX_AMOUNT);
              value = Math.min(value, maxWithdraw);
              maxWithdraw = player.getInventory().canAddAmount(ItemId.BOND_32318, maxWithdraw);
              if (maxWithdraw == 0) {
                player.getInventory().notEnoughSpace();
                return;
              }
              player.getBonds().setPouch(player.getBonds().getPouch() - value);
              player.getInventory().addItem(ItemId.BOND_32318, value);
              player.getBonds().sendPouchCounts();
            }
          });
        } else if (option == 2) {
          int maxWithdraw = (int) Math.min(player.getBonds().getPouch(), Item.MAX_AMOUNT);
          maxWithdraw = player.getInventory().canAddAmount(ItemId.BOND_32318, maxWithdraw);
          if (maxWithdraw == 0) {
            player.getInventory().notEnoughSpace();
            break;
          }
          player.getBonds().setPouch(player.getBonds().getPouch() - maxWithdraw);
          player.getInventory().addItem(ItemId.BOND_32318, maxWithdraw);
        }
        break;
      case 69:
        player.getGameEncoder().sendOpenUrl(Settings.getInstance().getStoreUrl());
        break;
      case 71:
        if (Settings.isBeta()) {
          player.getGameEncoder().sendMessage("You can't do this right now.");
          break;
        }
        player.openShop("bond");
        break;
      case 73:
        Guide.openEntry(player, "main", "bonds");
        break;
      case 75:
        player.openDialogue("bond", 2);
        break;
      case 77:
        player.getBonds().setHideRankIcon(!player.getBonds().isHideRankIcon());
        player.getGameEncoder().sendMessage("Hide rank: " + player.getBonds().isHideRankIcon());
        break;
    }
    player.getBonds().sendPouchCounts();
  }
}
