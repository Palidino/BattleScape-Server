package com.palidinodh.osrsscript.incomingpacket.widget;

import com.palidinodh.osrscore.io.ValueEnteredEvent;
import com.palidinodh.osrscore.io.incomingpacket.WidgetHandler;
import com.palidinodh.osrscore.io.cache.WidgetId;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.adaptive.GrandExchangeItem;
import com.palidinodh.rs.adaptive.GrandExchangeUser;

public class ExchangeWidget implements WidgetHandler {
  @Override
  public int[] getIds() {
    return new int[] {WidgetId.GRAND_EXCHANGE, WidgetId.GRAND_EXCHANGE_INVENTORY,
        WidgetId.GRAND_EXCHANGE_HISTORY};
  }

  @Override
  public void execute(Player player, int index, int widgetId, int childId, int slot, int itemId) {
    if (player.isLocked()) {
      return;
    }
    if (widgetId == WidgetId.GRAND_EXCHANGE) {
      switch (childId) {
        case 3:
          player.getGrandExchange().openHistory(GrandExchangeUser.HISTORY);
          break;
        case 4:
          player.getGrandExchange().reset(false);
          break;
        case 6:
          if (index == 0) {
            player.getGrandExchange().collectAll();
          }
          break;
        case 7:
        case 8:
        case 9:
        case 10:
        case 11:
        case 12:
        case 13:
        case 14:
          if (slot == 2) {
            if (index == 0) {
              player.getGrandExchange().viewOffer(childId - 7);
            } else if (index == 1) {
              player.getGrandExchange().abortOffer(childId - 7);
            }
          } else if (slot == 3) {
            player.getGrandExchange().configureBuyOffer(childId - 7);
          } else if (slot == 4) {
            player.getGrandExchange().configureSellOffer(childId - 7, -1);
          }
          break;
        case 22:
          player.getGrandExchange().abortOffer(player.getAttributeInt("ge_slot"));
          break;
        case 23:
          if (slot == 2 || slot == 3) {
            player.getGrandExchange().collectOffer(player.getAttributeInt("ge_slot"), itemId,
                index);
          }
          break;
        case 24:
          if (slot == 0) {
            player.getGrandExchange().openSearch("What would you like to buy?");
          } else if (slot == 1) {
            player.getGrandExchange().adjustAmount(-1);
          } else if (slot == 2 || slot == 3) {
            player.getGrandExchange().adjustAmount(1);
          } else if (slot == 4) {
            player.getGrandExchange().adjustAmount(10);
          } else if (slot == 5) {
            player.getGrandExchange().adjustAmount(100);
          } else if (slot == 6) {
            if (player.getAttributeInt("ge_state") == GrandExchangeItem.STATE_BUYING) {
              player.getGrandExchange().adjustAmount(1000);
            } else if (player.getAttributeInt("ge_state") == GrandExchangeItem.STATE_SELLING) {
              player.getGrandExchange()
                  .setAmount(player.getInventory().getCount(player.getAttributeInt("ge_id")));
            }
          } else if (slot == 7) {
            player.getGameEncoder().sendEnterAmount(new ValueEnteredEvent.IntegerEvent() {
              @Override
              public void execute(int value) {
                player.getGrandExchange().setAmount(value);
              }
            });
          } else if (slot == 8) {
            player.getGrandExchange().adjustPrice(-1);
          } else if (slot == 9) {
            player.getGrandExchange().adjustPrice(1);
          } else if (slot == 10) {
            player.getGrandExchange().adjustPricePercent(-0.05);
          } else if (slot == 11) {
            player.getGrandExchange().setPrice(-1);
          } else if (slot == 12) {
            player.getGameEncoder().sendEnterAmount(new ValueEnteredEvent.IntegerEvent() {
              @Override
              public void execute(int value) {
                player.getGrandExchange().setPrice(value);
              }
            });
          } else if (slot == 13) {
            player.getGrandExchange().adjustPricePercent(0.05);
          }
          break;
        case 27:
          player.getGrandExchange().confirmOffer();
          break;
      }
    } else if (widgetId == WidgetId.GRAND_EXCHANGE_INVENTORY) {
      switch (childId) {
        case 0:
          if (index == 0) {
            player.getGrandExchange().configureSellOffer(-1, slot);
          }
          break;
      }
    } else if (widgetId == WidgetId.GRAND_EXCHANGE_HISTORY) {
      switch (childId) {
        case 2:
          player.getGrandExchange().open();
          break;
        case 3:
          player.getGrandExchange().openFromHistory(slot / 6);
          break;
      }
    }
  }
}
