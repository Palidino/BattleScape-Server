package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.model.player.Player;
import lombok.var;

class WidgetIndexCommand implements CommandHandler {
  @Override
  public String getExample() {
    return "id index";
  }

  @Override
  public boolean canUse(Player player) {
    return player.getRights() == Player.RIGHTS_ADMIN;
  }

  @Override
  public void execute(Player player, String message) {
    String[] messages = message.split(" ");
    var id = Integer.parseInt(messages[0]);
    var index = Integer.parseInt(messages[1]);
    player.getGameEncoder().sendWidget(player.getWidgetManager().getRootWidgetId(), index, id,
        true);
  }
}
