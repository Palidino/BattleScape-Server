package com.palidinodh.osrsscript.packetdecoder.command;

import com.palidinodh.osrscore.io.Command;
import com.palidinodh.osrscore.model.player.Player;
import lombok.var;

public class RWildSlayerCommand implements Command {
  @Override
  public String getExample() {
    return "username";
  }

  @Override
  public boolean canUse(Player player) {
    return player.getRights() == Player.RIGHTS_ADMIN;
  }

  @Override
  public void execute(Player player, String username) {
    var player2 = player.getWorld().getPlayerByUsername(username);
    if (player2 == null) {
      player.getGameEncoder().sendMessage("Unable to find user " + username + ".");
      return;
    }
    player2.pluginScript("slayer_reset_wilderness_task");
    player2.getGameEncoder()
        .sendMessage("Your wilderness slayer task has been reset by " + player.getUsername());
    player.getGameEncoder().sendMessage("Success");
  }
}
