package com.palidinodh.osrsscript.packetdecoder.widget;

import com.palidinodh.osrscore.io.ValueEnteredEvent;
import com.palidinodh.osrscore.io.Widget;
import com.palidinodh.osrscore.io.cache.WidgetId;
import com.palidinodh.osrscore.model.item.Item;
import com.palidinodh.osrscore.model.player.Player;

public class TradeWidget implements Widget {
  @Override
  public int[] getIds() {
    return new int[] {WidgetId.TRADE, WidgetId.TRADE_INVENTORY, WidgetId.ACCEPT_TRADE};
  }

  @Override
  public void execute(Player player, int index, int widgetId, int childId, int slot, int itemId) {
    if (player.isLocked()) {
      return;
    }
    if (widgetId == WidgetId.TRADE) {
      switch (childId) {
        case 10:
          player.getTrade().accept1();
          break;
        case 25:
          if (index == 0) {
            player.getTrade().removeOffer(itemId, slot, 1);
          } else if (index == 1) {
            player.getTrade().removeOffer(itemId, slot, 5);
          } else if (index == 2) {
            player.getTrade().removeOffer(itemId, slot, 10);
          } else if (index == 3) {
            player.getTrade().removeOffer(itemId, slot, Item.MAX_AMOUNT);
          } else if (index == 4) {
            player.getGameEncoder().sendEnterAmount(new ValueEnteredEvent.IntegerEvent() {
              @Override
              public void execute(int value) {
                player.getTrade().removeOffer(itemId, slot, value);
              }
            });
          }
          break;
      }
    } else if (widgetId == WidgetId.TRADE_INVENTORY) {
      switch (childId) {
        case 0:
          if (index == 0) {
            player.getTrade().offerItem(itemId, slot, 1);
          } else if (index == 1) {
            player.getTrade().offerItem(itemId, slot, 5);
          } else if (index == 2) {
            player.getTrade().offerItem(itemId, slot, 10);
          } else if (index == 3) {
            player.getTrade().offerItem(itemId, slot, Item.MAX_AMOUNT);
          } else if (index == 4) {
            player.getGameEncoder().sendEnterAmount(new ValueEnteredEvent.IntegerEvent() {
              @Override
              public void execute(int value) {
                player.getTrade().offerItem(itemId, slot, value);
              }
            });
          }
          break;
      }
    } else if (widgetId == WidgetId.ACCEPT_TRADE) {
      switch (childId) {
        case 13:
          player.getTrade().accept2();
          break;
        case 14:
          player.getWidgetManager().removeInteractiveWidgets();
          break;
      }
    }
  }
}
