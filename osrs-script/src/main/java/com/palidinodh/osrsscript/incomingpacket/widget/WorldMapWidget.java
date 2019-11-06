package com.palidinodh.osrsscript.incomingpacket.widget;

import com.palidinodh.osrscore.io.incomingpacket.WidgetHandler;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.player.Player;

public class WorldMapWidget implements WidgetHandler {
  @Override
  public int[] getIds() {
    return new int[] {WidgetId.WORLD_MAP};
  }

  @Override
  public void execute(Player player, int index, int widgetId, int childId, int slot, int itemId) {
    switch (childId) {
      case 3:
      case 37:
        player.getWidgetManager().removeWorldMap();
        break;
    }
  }
}
