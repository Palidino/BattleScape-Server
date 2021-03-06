package com.palidinodh.osrsscript.incomingpacket.widget;

import com.palidinodh.osrscore.io.incomingpacket.WidgetHandler;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;

@ReferenceId(WidgetId.CUSTOM_INFORMATION)
class InformationWidget implements WidgetHandler {
  @Override
  public void execute(Player player, int option, int widgetId, int childId, int slot, int itemId) {
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
