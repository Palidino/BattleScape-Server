package com.palidinodh.osrsscript.packetdecoder.command;

import com.palidinodh.osrscore.io.Command;
import com.palidinodh.osrscore.model.player.Player;
import lombok.var;

public class WidgetIndexCommand implements Command {
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
