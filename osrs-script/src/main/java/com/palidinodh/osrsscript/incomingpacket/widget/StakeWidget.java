package com.palidinodh.osrsscript.incomingpacket.widget;

import com.palidinodh.osrscore.io.ValueEnteredEvent;
import com.palidinodh.osrscore.io.incomingpacket.WidgetHandler;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.item.Item;
import com.palidinodh.osrscore.model.player.Duel;
import com.palidinodh.osrscore.model.player.Player;

public class StakeWidget implements WidgetHandler {
  @Override
  public int[] getIds() {
    return new int[] {WidgetId.CUSTOM_STAKE, WidgetId.CUSTOM_STAKE_INVENTORY};
  }

  @Override
  public void execute(Player player, int index, int widgetId, int childId, int slot, int itemId) {
    if (widgetId == WidgetId.CUSTOM_STAKE) {
      if (player.getDuel().getState() == Duel.State.CUSTOM_STAKE
          || player.getDuel().getState() == Duel.State.ACCEPT_CUSTOM_STAKE) {
        switch (childId) {
          case 19:
            player.getDuel().acceptCustomStake();
            break;
          case 28:
            switch (index) {
              case 0:
                player.getDuel().removeOffer(slot, itemId, 1);
                break;
              case 1:
                player.getDuel().removeOffer(slot, itemId, 5);
                break;
              case 2:
                player.getDuel().removeOffer(slot, itemId, 10);
                break;
              case 3:
                player.getDuel().removeOffer(slot, itemId, Item.MAX_AMOUNT);
                break;
              case 4:
                player.getGameEncoder().sendEnterAmount(new ValueEnteredEvent.IntegerEvent() {
                  @Override
                  public void execute(int value) {
                    player.getDuel().removeOffer(slot, itemId, value);
                  }
                });
                break;
            }
            break;
        }
      }
      if (childId == 13 || childId == 21) {
        player.getWidgetManager().removeInteractiveWidgets();
      }
    } else if (widgetId == WidgetId.CUSTOM_STAKE_INVENTORY) {
      if (player.getDuel().getState() == Duel.State.CUSTOM_STAKE
          || player.getDuel().getState() == Duel.State.ACCEPT_CUSTOM_STAKE) {
        switch (index) {
          case 0:
            player.getDuel().addOffer(slot, itemId, 1);
            break;
          case 1:
            player.getDuel().addOffer(slot, itemId, 5);
            break;
          case 2:
            player.getDuel().addOffer(slot, itemId, 10);
            break;
          case 3:
            player.getDuel().addOffer(slot, itemId, Item.MAX_AMOUNT);
            break;
          case 4:
            player.getGameEncoder().sendEnterAmount(new ValueEnteredEvent.IntegerEvent() {
              @Override
              public void execute(int value) {
                player.getDuel().addOffer(slot, itemId, value);
              }
            });
            break;
        }
      }
    }
  }
}
