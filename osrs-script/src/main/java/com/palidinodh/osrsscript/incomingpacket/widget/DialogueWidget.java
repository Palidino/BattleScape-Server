package com.palidinodh.osrsscript.incomingpacket.widget;

import com.palidinodh.osrscore.io.incomingpacket.WidgetHandler;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.dialogue.Dialogue;
import com.palidinodh.osrscore.model.dialogue.old.DialogueOld;
import com.palidinodh.osrscore.model.player.Player;

public class DialogueWidget implements WidgetHandler {
  @Override
  public int[] getIds() {
    return new int[] { WidgetId.CHATBOX_PLAYER, WidgetId.CHATBOX_NPC, WidgetId.CHATBOX_MESSAGE,
        WidgetId.CHATBOX_SELECTION, WidgetId.SCREEN_SELECTION, WidgetId.MAKE_X };
  }

  @Override
  public void execute(Player player, int option, int widgetId, int childId, int slot, int itemId) {
    if (widgetId == WidgetId.CHATBOX_PLAYER || widgetId == WidgetId.CHATBOX_NPC
        || widgetId == WidgetId.CHATBOX_MESSAGE) {
      slot = 0;
    } else if (widgetId == WidgetId.CHATBOX_SELECTION) {
      slot--;
    } else if (widgetId == WidgetId.MAKE_X) {
      childId -= 14;
    }
    if (player.getAttribute("dialogue") != null) {
      ((Dialogue) player.getAttribute("dialogue")).widgetSelected(player, childId, slot);
    } else {
      DialogueOld.handleWidget(player, childId, slot);
    }
  }
}
