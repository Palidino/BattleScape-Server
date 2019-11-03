package com.palidinodh.osrsscript.incomingpacket.widget;

import com.palidinodh.osrscore.io.incomingpacket.WidgetHandler;
import com.palidinodh.osrscore.io.cache.WidgetId;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.setting.Settings;

public class ChatboxWidget implements WidgetHandler {
  @Override
  public int[] getIds() {
    return new int[] {WidgetId.CHATBOX};
  }

  @Override
  public void execute(Player player, int index, int widgetId, int childId, int slot, int itemId) {
    switch (childId) {
      case 27:
        player.getGameEncoder().sendOpenURL(Settings.getInstance().getWebsiteUrl());
        break;
    }
  }
}
