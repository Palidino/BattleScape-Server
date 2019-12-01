package com.palidinodh.osrsscript.player.plugin.clanwars.widget;

import com.palidinodh.osrscore.io.incomingpacket.WidgetHandler;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;

@ReferenceId(WidgetId.CLAN_WARS_OPTIONS)
class ClanWarsOptionsWidget implements WidgetHandler {
  @Override
  public void execute(Player player, int option, int widgetId, int childId, int slot, int itemId) {
    switch (childId) {
      case 5:
        player.getMovement().stopViewing();
        player.getWidgetManager().removeInteractiveWidgets();
        player.getAppearance().setNpcId(-1);
        break;
    }
  }
}
