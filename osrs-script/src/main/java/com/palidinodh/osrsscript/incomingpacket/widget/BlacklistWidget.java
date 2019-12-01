package com.palidinodh.osrsscript.incomingpacket.widget;

import com.palidinodh.osrscore.io.ValueEnteredEvent;
import com.palidinodh.osrscore.io.incomingpacket.WidgetHandler;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;

@ReferenceId(WidgetId.CUSTOM_BLACKLIST)
class BlacklistWidget implements WidgetHandler {
  @Override
  public void execute(Player player, int option, int widgetId, int childId, int slot, int itemId) {
    if (player.isLocked()) {
      return;
    }
    if (player.getController().inWilderness()) {
      player.getGameEncoder().sendMessage("You can't use the blacklist while in the Wilderness.");
      return;
    }
    if (childId >= 18 && childId <= 27) {
      player.getCombat().getEdgevilleBlacklist().removeName(childId - 18);
    } else if (childId == 28) {
      player.getGameEncoder().sendEnterString("Enter Name:", new ValueEnteredEvent.StringEvent() {
        @Override
        public void execute(String value) {
          player.getCombat().getEdgevilleBlacklist().addName(value);
        }
      });
    }
  }
}
