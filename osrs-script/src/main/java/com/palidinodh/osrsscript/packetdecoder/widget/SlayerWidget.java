package com.palidinodh.osrsscript.packetdecoder.widget;

import java.util.Map;
import com.palidinodh.osrscore.io.Widget;
import com.palidinodh.osrscore.io.cache.WidgetId;
import com.palidinodh.osrscore.model.player.Player;

public class SlayerWidget implements Widget {
  @Override
  public int[] getIds() {
    return new int[] {WidgetId.SLAYER_REWARDS, WidgetId.SLAYER_PARTNER};
  }

  @Override
  public void execute(Player player, int index, int widgetId, int childId, int slot, int itemId) {
    if (player.isLocked()) {
      return;
    }
    if (widgetId == WidgetId.SLAYER_PARTNER) {
      if (childId != 7) {
        return;
      }
      if (player.getAttribute("clan_chat_usernames") != null) {
        Map<Integer, String> groupMap = player.canUpdateGroupIronman();
        if (groupMap == null) {
          return;
        }
        if (player.getGroupIronmanUsernames().contains("<col=ff0000>")) {
          player.openDialogue("clanchat", 1);
          return;
        }
        for (String username : groupMap.values()) {
          Player player2 = player.getWorld().getPlayerByUsername(username);
          player2.setGroupIronman(groupMap);
          player2.getGameEncoder().sendMessage("Your ironman group has been updated.");
        }
        player.getWidgetManager().removeInteractiveWidgets();
      } else {
        player.getGameEncoder()
            .sendMessage("No action associated with this click, please reload the interface.");
      }
    }
  }
}
