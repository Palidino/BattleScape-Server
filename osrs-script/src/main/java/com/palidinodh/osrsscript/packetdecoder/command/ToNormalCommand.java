package com.palidinodh.osrsscript.packetdecoder.command;

import com.palidinodh.osrscore.io.Command;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.adaptive.RsGameMode;
import lombok.var;

public class ToNormalCommand implements Command {
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
    player2.setGameMode(RsGameMode.NORMAL.ordinal());
    player2.getGameEncoder()
        .sendMessage("Your gamemode has been set to Normal mode by " + player.getUsername());
    player.getGameEncoder().sendMessage("Success");
  }
}
