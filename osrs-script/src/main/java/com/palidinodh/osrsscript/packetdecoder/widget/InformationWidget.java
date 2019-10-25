package com.palidinodh.osrsscript.packetdecoder.widget;

import com.palidinodh.osrscore.io.Widget;
import com.palidinodh.osrscore.io.cache.WidgetId;
import com.palidinodh.osrscore.model.player.Player;

public class InformationWidget implements Widget {
  @Override
  public int[] getIds() {
    return new int[] {WidgetId.CUSTOM_INFORMATION};
  }

  @Override
  public void execute(Player player, int index, int widgetId, int childId, int slot, int itemId) {
    if (childId == 2) {
      player.getWidgetManager().setQuestIndex(0);
      player.getWidgetManager().resetQuestText();
    } else if (childId == 5) {
      player.getWidgetManager().setQuestIndex(1);
      player.getWidgetManager().resetQuestText();
    } else if (childId == 8) {
      player.getWidgetManager().setQuestIndex(2);
      player.getWidgetManager().resetQuestText();
    } else if (childId == 11) {
      player.getWidgetManager().setQuestIndex(3);
      player.getWidgetManager().resetQuestText();
    } else if (childId == 14) {
      player.getBonds().sendPouch();
      // player.getWidgetManager().setQuestIndex(4);
      // player.getWidgetManager().resetQuestText();
    } else {
      player.getWidgetManager().questAction(childId - 33);
    }
  }
}
