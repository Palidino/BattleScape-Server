package com.palidinodh.osrsscript.packetdecoder.command;

import com.palidinodh.osrscore.io.Command;
import com.palidinodh.osrscore.model.player.Player;
import lombok.var;

public class TotalBondsCommand implements Command {
  @Override
  public String getExample() {
    return "total_purchased username_or_userid";
  }

  @Override
  public boolean canUse(Player player) {
    return player.getRights() == Player.RIGHTS_ADMIN;
  }

  @Override
  public void execute(Player player, String command) {
    var split = command.split(" ");
    var totalPurchased = Integer.parseInt(split[0]);
    var username = command.substring(command.indexOf(split[0]) + split[0].length() + 1);
    var player2 = player.getWorld().getPlayerByUsername(username);
    if (player2 == null) {
      var userID = -1;
      userID = Integer.parseInt(username);
      if (userID != -1) {
        player2 = player.getWorld().getPlayerById(userID);
      }
    }
    if (player2 == null) {
      player.getGameEncoder().sendMessage("Unable to find user " + username);
      return;
    }
    player2.getBonds().setTotalPurchased(totalPurchased);
    player.getGameEncoder()
        .sendMessage("Updated " + username + " to " + totalPurchased + " total purchased bonds.");
    player2.getGameEncoder()
        .sendMessage("Your total purchased bonds have been updated to " + totalPurchased + ".");
  }
}
