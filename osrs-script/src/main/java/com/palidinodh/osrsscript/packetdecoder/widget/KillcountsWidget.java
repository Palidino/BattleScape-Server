package com.palidinodh.osrsscript.packetdecoder.widget;

import com.palidinodh.osrscore.io.Widget;
import com.palidinodh.osrscore.io.cache.WidgetId;
import com.palidinodh.osrscore.model.player.Player;

public class KillcountsWidget implements Widget {
  @Override
  public int[] getIds() {
    return new int[] {WidgetId.KILL_LOG, WidgetId.CUSTOM_LOOT_RATES};
  }

  @Override
  public void execute(Player player, int index, int widgetId, int childId, int slot, int itemId) {
    if (widgetId == WidgetId.CUSTOM_LOOT_RATES) {
      switch (childId) {
        case 425:
          player.getCombat().openNPCRareLootList();
          break;
      }
    }
  }
}
