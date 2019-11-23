package com.palidinodh.osrsscript.incomingpacket.widget;

import com.palidinodh.osrscore.io.incomingpacket.WidgetHandler;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.player.Player;

public class BountyHunterWidget implements WidgetHandler {
  @Override
  public int[] getIds() {
    return new int[] {WidgetId.WILDERNESS};
  }

  @Override
  public void execute(Player player, int option, int widgetId, int childId, int slot, int itemId) {
    if (player.isLocked()) {
      return;
    }
    switch (childId) {
      case 57:
        player.openDialogue("bountyhunter", 0);
        break;
    }
  }
}
