package com.palidinodh.osrsscript.incomingpacket.widget;

import com.palidinodh.osrscore.io.incomingpacket.WidgetHandler;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.Teleports;
import com.palidinodh.rs.ReferenceId;

@ReferenceId(WidgetId.CUSTOM_TELEPORTS)
class TeleportsWidget implements WidgetHandler {
  @Override
  public void execute(Player player, int option, int widgetId, int childId, int slot, int itemId) {
    if (player.isLocked()) {
      return;
    }
    if (!player.getController().canTeleport(true)) {
      return;
    }
    if (childId >= 21 && childId <= 53) {
      player.getWidgetManager().setLastTeleportViewingIndex((childId - 19) / 3);
      Teleports.open(player);
    } else if (childId >= 59 && childId <= 198) {
      Teleports.destinationWidgetPressed(player, childId);
    } else if (childId == 203) {
      Teleports.openHomeSelect(player);
    } else if (childId >= 207 && childId <= 209) {
      int[] teleportIndices = player.getWidgetManager().getLastTeleport(childId - 207);
      Teleports.destinationWidgetPressed(player, teleportIndices[0], teleportIndices[1]);
    }
  }
}
