package com.palidinodh.osrsscript.incomingpacket.widget;

import com.palidinodh.osrscore.io.incomingpacket.WidgetHandler;
import com.palidinodh.osrscore.io.cache.WidgetId;
import com.palidinodh.osrscore.model.player.Player;

public class ItemsOnDeathWidget implements WidgetHandler {
  @Override
  public int[] getIds() {
    return new int[] {WidgetId.ITEMS_KEPT_ON_DEATH};
  }

  @Override
  public void execute(Player player, int index, int widgetId, int childId, int slot, int itemId) {}
}
