package com.palidinodh.osrsscript.incomingpacket.widget;

import com.palidinodh.osrscore.io.ValueEnteredEvent;
import com.palidinodh.osrscore.io.incomingpacket.WidgetHandler;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.Smithing;

public class SmithingWidget implements WidgetHandler {
  @Override
  public int[] getIds() {
    return new int[] { WidgetId.SMITHING };
  }

  @Override
  public void execute(Player player, int option, int widgetId, int childId, int slot, int itemId) {
    if (player.isLocked()) {
      return;
    }
    int selected = childId - 2;
    int[] itemArray = (int[]) player.getAttribute("smithing_ids");
    if (itemArray == null || selected < 0 || selected >= itemArray.length) {
      return;
    }
    int smithId = itemArray[selected];
    if (option == 0) {
      Smithing.start(player, smithId, 1);
    } else if (option == 1) {
      Smithing.start(player, smithId, 5);
    } else if (option == 2) {
      Smithing.start(player, smithId, 10);
    } else if (option == 3) {
      player.getGameEncoder().sendEnterAmount(new ValueEnteredEvent.IntegerEvent() {
        @Override
        public void execute(int value) {
          Smithing.start(player, smithId, value);
        }
      });
    } else if (option == 4) {
      Smithing.start(player, smithId, 28);
    }
  }
}
