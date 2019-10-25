package com.palidinodh.osrsscript.packetdecoder.widget;

import com.palidinodh.osrscore.Main;
import com.palidinodh.osrscore.io.Widget;
import com.palidinodh.osrscore.io.cache.WidgetChild;
import com.palidinodh.osrscore.io.cache.WidgetId;
import com.palidinodh.osrscore.model.player.Player;

public class FriendsWidget implements Widget {
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
        player.getGameEncoder().sendOpenURL(Main.getSettings().getWebsiteUrl());
      } else if (childId == 22) {
        player.getGameEncoder().sendOpenURL(Main.getSettings().getSupportUrl());
      } else if (childId == 29) {
        player.getGameEncoder().sendOpenURL(Main.getSettings().getWebsiteUrl());
      } else if (childId == 32) {
        player.getGameEncoder().sendOpenURL(Main.getSettings().getSupportUrl());
      }
    }
  }
}
