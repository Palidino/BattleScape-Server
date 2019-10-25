package com.palidinodh.osrsscript.packetdecoder.widget;

import com.palidinodh.osrscore.Main;
import com.palidinodh.osrscore.io.Widget;
import com.palidinodh.osrscore.io.cache.WidgetId;
import com.palidinodh.osrscore.model.player.Player;

public class ChatboxWidget implements Widget {
  @Override
  public int[] getIds() {
    return new int[] {WidgetId.CHATBOX};
  }

  @Override
  public void execute(Player player, int index, int widgetId, int childId, int slot, int itemId) {
    switch (childId) {
      case 27:
        player.getGameEncoder().sendOpenURL(Main.getSettings().getWebsiteUrl());
        break;
    }
  }
}
