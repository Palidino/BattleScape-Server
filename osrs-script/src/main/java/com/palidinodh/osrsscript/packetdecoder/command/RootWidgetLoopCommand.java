package com.palidinodh.osrsscript.packetdecoder.command;

import com.palidinodh.osrscore.io.Command;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.util.PEvent;
import lombok.var;

public class RootWidgetLoopCommand implements Command {
  @Override
  public String getExample() {
    return "widget_id";
  }

  @Override
  public boolean canUse(Player player) {
    return player.getRights() == Player.RIGHTS_ADMIN;
  }

  @Override
  public void execute(Player player, String message) {
    var widgetId = Integer.parseInt(message);
    var event = new PEvent(1) {
      int index = 0;

      @Override
      public void execute() {
        player.getGameEncoder().sendMessage("Index: " + index);
        player.getGameEncoder().sendWidget(player.getWidgetManager().getRootWidgetId(), index++,
            widgetId, false);
      }
    };
    player.setAction(event);
  }
}
