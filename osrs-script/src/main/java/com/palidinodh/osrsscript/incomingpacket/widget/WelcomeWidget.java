package com.palidinodh.osrsscript.incomingpacket.widget;

import com.palidinodh.osrscore.io.incomingpacket.WidgetHandler;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.dialogue.MessageDialogue;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.random.PRandom;
import com.palidinodh.rs.ReferenceId;
import com.palidinodh.rs.setting.Settings;

@ReferenceId(WidgetId.WELCOME)
class WelcomeWidget implements WidgetHandler {
  @Override
  public void execute(Player player, int option, int widgetId, int childId, int slot, int itemId) {
    switch (childId) {
      case 81:
        player.getWidgetManager().sendGameViewport();
        if (player.isNewAccount()) {
          player.getWidgetManager().sendInteractiveOverlay(WidgetId.CHARACTER_DESIGN);
        } else if (!player.isPremiumMember() && PRandom.randomE(5) == 0
            && !Settings.getInstance().isSpawn()) {
          player.openDialogue(new MessageDialogue(
              "<col=ff0000>Bonds</col> can be purchased and used on: trading, membership, mystery boxes, blood money, and other items!"));
        }
        break;
    }
  }
}
