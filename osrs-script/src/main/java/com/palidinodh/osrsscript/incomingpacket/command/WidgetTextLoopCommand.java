package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.model.player.Player;
import lombok.var;

class WidgetTextLoopCommand implements CommandHandler {
  @Override
  public String getExample() {
    return "id start end";
  }

  @Override
  public boolean canUse(Player player) {
    return player.getRights() == Player.RIGHTS_ADMIN;
  }

  @Override
  public void execute(Player player, String message) {
    var values = message.split(" ");
    var id = Integer.parseInt(values[0]);
    var start = Integer.parseInt(values[1]);
    var end = Integer.parseInt(values[2]);
    for (var i = start; i < end; i++) {
      player.getGameEncoder().sendWidgetText(id, i, "(" + i + ")");
    }
  }
}
