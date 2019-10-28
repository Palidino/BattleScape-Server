package com.palidinodh.osrsscript.packetdecoder.widget;

import com.palidinodh.osrscore.io.Widget;
import com.palidinodh.osrscore.io.cache.WidgetId;
import com.palidinodh.osrscore.model.dialogue.old.DialogueOld;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.random.PRandom;
import com.palidinodh.rs.setting.Settings;

public class WelcomeWidget implements Widget {
  @Override
  public int[] getIds() {
    return new int[] {WidgetId.WELCOME};
  }

  @Override
  public void execute(Player player, int index, int widgetId, int childId, int slot, int itemId) {
    switch (childId) {
      case 81:
        player.getWidgetManager().sendGameViewport();
        if (player.isNewAccount()) {
          player.getWidgetManager().sendInteractiveOverlay(WidgetId.CHARACTER_DESIGN);
        } else if (!player.isPremiumMember() && PRandom.randomE(5) == 0
            && !Settings.getInstance().isSpawn()) {
          DialogueOld.openChatboxMessage(player,
              "<col=ff0000>Bonds</col> can be purchased and used on: trading, membership, mystery boxes, blood money, and other items!");
        }
        break;
    }
  }
}
