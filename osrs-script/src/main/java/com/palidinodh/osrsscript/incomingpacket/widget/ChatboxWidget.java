package com.palidinodh.osrsscript.incomingpacket.widget;

import com.palidinodh.osrscore.io.incomingpacket.WidgetHandler;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;
import com.palidinodh.rs.setting.Settings;

@ReferenceId(WidgetId.CHATBOX)
class ChatboxWidget implements WidgetHandler {
  @Override
  public void execute(Player player, int option, int widgetId, int childId, int slot, int itemId) {
    switch (childId) {
      case 27:
        player.getGameEncoder().sendOpenUrl(Settings.getInstance().getWebsiteUrl());
        break;
    }
  }
}
