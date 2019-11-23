package com.palidinodh.osrsscript.incomingpacket.widget;

import com.palidinodh.osrscore.io.incomingpacket.WidgetHandler;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.player.Player;

public class UnmorphWidget implements WidgetHandler {
  @Override
  public int[] getIds() {
    return new int[] {WidgetId.UNMORPH};
  }

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
