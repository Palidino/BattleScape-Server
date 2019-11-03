package com.palidinodh.osrsscript.incomingpacket.widget;

import com.palidinodh.osrscore.io.incomingpacket.WidgetHandler;
import com.palidinodh.osrscore.io.cache.WidgetChild;
import com.palidinodh.osrscore.io.cache.WidgetId;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.setting.Settings;

public class FriendsWidget implements WidgetHandler {
  @Override
  public int[] getIds() {
    return new int[] {WidgetId.FRIENDS, WidgetId.IGNORES, WidgetId.ACCOUNT_MANAGEMENT};
  }

  @Override
  public void execute(Player player, int index, int widgetId, int childId, int slot, int itemId) {
    if (widgetId == WidgetId.FRIENDS) {
      if (childId == 1) {
        player.getWidgetManager().sendWidget(WidgetChild.ViewportContainer.FRIENDS,
            WidgetId.IGNORES);
      }
    } else if (widgetId == WidgetId.IGNORES) {
      if (childId == 1) {
        player.getWidgetManager().sendWidget(WidgetChild.ViewportContainer.FRIENDS,
            WidgetId.FRIENDS);
      }
    } else if (widgetId == WidgetId.ACCOUNT_MANAGEMENT) {
      if (childId == 3 || childId == 8) {
        player.getBonds().sendPouch();
      } else if (childId == 15) {
        player.getGameEncoder().sendOpenURL(Settings.getInstance().getWebsiteUrl());
      } else if (childId == 22) {
        player.getGameEncoder().sendOpenURL(Settings.getInstance().getSupportUrl());
      } else if (childId == 29) {
        player.getGameEncoder().sendOpenURL(Settings.getInstance().getWebsiteUrl());
      } else if (childId == 32) {
        player.getGameEncoder().sendOpenURL(Settings.getInstance().getSupportUrl());
      }
    }
  }
}
