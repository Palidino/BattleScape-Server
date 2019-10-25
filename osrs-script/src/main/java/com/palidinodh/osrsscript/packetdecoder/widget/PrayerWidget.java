package com.palidinodh.osrsscript.packetdecoder.widget;

import com.palidinodh.osrscore.io.Widget;
import com.palidinodh.osrscore.io.cache.WidgetId;
import com.palidinodh.osrscore.model.player.Player;

public class PrayerWidget implements Widget {
  @Override
  public int[] getIds() {
    return new int[] {WidgetId.PRAYER, WidgetId.QUICK_PRAYER};
  }

  @Override
  public void execute(Player player, int index, int widgetId, int childId, int slot, int itemId) {
    if (widgetId == WidgetId.PRAYER) {
      player.getPrayer().activate(childId);
    } else if (widgetId == WidgetId.QUICK_PRAYER) {
      switch (childId) {
        case 4:
          int prayerOffset = 5;
          if (slot <= 23 || slot >= 27) {
            player.getPrayer().activate(slot + prayerOffset);
          } else if (slot == 24) {
            player.getPrayer().activate(slot + prayerOffset + 2);
          } else if (slot == 25 || slot == 26) {
            player.getPrayer().activate(slot + prayerOffset - 1);
          }
          break;
        case 5:
          player.getPrayer().switchQuick();
          break;
      }
    }
  }
}
