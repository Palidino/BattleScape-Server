package com.palidinodh.osrsscript.player.plugin.clanwars.widget;

import com.palidinodh.osrscore.io.incomingpacket.WidgetHandler;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrsscript.player.plugin.clanwars.ClanWarsPlugin;
import com.palidinodh.rs.ReferenceId;

@ReferenceId(WidgetId.TOURNAMENT_VIEWER)
class TournamentViewerWidget implements WidgetHandler {
  @Override
  public void execute(Player player, int option, int widgetId, int childId, int slot, int itemId) {
    if (player.getMovement().getTeleporting() || player.getMovement().getTeleported()
        || player.getMovement().isViewing()) {
      return;
    }
    switch (childId) {
      case 8:
        player.getPlugin(ClanWarsPlugin.class).teleportViewing(-1);
        break;
    }
  }
}
