package com.palidinodh.osrsscript.incomingpacket.widget;

import com.palidinodh.osrscore.io.incomingpacket.WidgetHandler;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;

@ReferenceId({ WidgetId.PRAYER, WidgetId.QUICK_PRAYER })
class PrayerWidget implements WidgetHandler {
  @Override
  public void execute(Player player, int option, int widgetId, int childId, int slot, int itemId) {
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
