package com.palidinodh.osrsscript.incomingpacket.widget;

import com.palidinodh.osrscore.io.incomingpacket.WidgetHandler;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.player.Player;

public class MusicWidget implements WidgetHandler {
  @Override
  public int[] getIds() {
    return new int[] {WidgetId.MUSIC};
  }

  @Override
  public void execute(Player player, int option, int widgetId, int childId, int slot, int itemId) {}
}
