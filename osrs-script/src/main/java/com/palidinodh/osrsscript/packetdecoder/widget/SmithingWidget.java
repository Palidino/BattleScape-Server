package com.palidinodh.osrsscript.packetdecoder.widget;

import com.palidinodh.osrscore.io.ValueEnteredEvent;
import com.palidinodh.osrscore.io.Widget;
import com.palidinodh.osrscore.io.cache.WidgetId;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.Smithing;

public class SmithingWidget implements Widget {
  @Override
  public int[] getIds() {
    return new int[] {WidgetId.SMITHING};
  }

  @Override
  public void execute(Player player, int index, int widgetId, int childId, int slot, int itemId) {
    if (player.isLocked()) {
      return;
    }
    int selected = childId - 2;
    int[] itemArray = (int[]) player.getAttribute("smithing_ids");
    if (itemArray == null || selected < 0 || selected >= itemArray.length) {
      return;
    }
    int smithId = itemArray[selected];
    if (index == 0) {
      Smithing.start(player, smithId, 1);
    } else if (index == 1) {
      Smithing.start(player, smithId, 5);
    } else if (index == 2) {
      Smithing.start(player, smithId, 10);
    } else if (index == 3) {
      player.getGameEncoder().sendEnterAmount(new ValueEnteredEvent.IntegerEvent() {
        @Override
        public void execute(int value) {
          Smithing.start(player, smithId, value);
        }
      });
    } else if (index == 4) {
      Smithing.start(player, smithId, 28);
    }
  }
}
