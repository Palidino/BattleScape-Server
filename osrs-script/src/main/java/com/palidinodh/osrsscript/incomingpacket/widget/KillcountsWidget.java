package com.palidinodh.osrsscript.incomingpacket.widget;

import com.palidinodh.osrscore.io.incomingpacket.WidgetHandler;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;

@ReferenceId({ WidgetId.KILL_LOG, WidgetId.CUSTOM_LOOT_RATES })
class KillcountsWidget implements WidgetHandler {
  @Override
  public void execute(Player player, int option, int widgetId, int childId, int slot, int itemId) {
    if (widgetId == WidgetId.CUSTOM_LOOT_RATES) {
      switch (childId) {
        case 425:
          player.getCombat().openNPCRareLootList();
          break;
      }
    }
  }
}
