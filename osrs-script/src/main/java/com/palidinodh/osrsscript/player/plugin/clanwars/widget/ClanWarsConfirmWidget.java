package com.palidinodh.osrsscript.player.plugin.clanwars.widget;

import com.palidinodh.osrscore.io.incomingpacket.WidgetHandler;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrsscript.player.plugin.clanwars.ClanWarsPlugin;
import com.palidinodh.osrsscript.player.plugin.clanwars.ClanWarsStages;
import com.palidinodh.rs.ReferenceId;

@ReferenceId(WidgetId.CLAN_WARS_CONFIRM)
class ClanWarsConfirmWidget implements WidgetHandler {
  @Override
  public void execute(Player player, int option, int widgetId, int childId, int slot, int itemId) {
    switch (childId) {
      case 6:
        ClanWarsStages.acceptRuleConfirmation(player, player.getPlugin(ClanWarsPlugin.class));
        break;
    }
  }
}
