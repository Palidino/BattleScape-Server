package com.palidinodh.osrsscript.packetdecoder.command;

import com.palidinodh.osrscore.io.Command;
import com.palidinodh.osrscore.model.player.Player;

public class PlayedCommand implements Command {

  @Override
  public boolean canUse(Player player) {
    return player.getRights() == Player.RIGHTS_MOD || player.getRights() == Player.RIGHTS_ADMIN;
  }

  @Override
  public void execute(Player player, String message) {
    player.getGameEncoder().sendMessage(player.getUsername() + " has played for: "
        + player.getPlayTimeText() + " in: " + player.getCreationTimeText());

  }
}
