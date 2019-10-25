package com.palidinodh.osrsscript.packetdecoder.command;

import com.palidinodh.osrscore.io.Command;
import com.palidinodh.osrscore.model.player.Player;
import lombok.var;

public class SetRedeemedCommand implements Command {
  @Override
  public String getExample() {
    return "username amount";
  }

  @Override
  public boolean canUse(Player player) {
    return player.getRights() == Player.RIGHTS_ADMIN;
  }

  @Override
  public void execute(Player player, String message) {
    var split = message.split(" ");
    var username = split[0].replace("_", " ");
    var amount = Integer.parseInt(split[1]);
    var player2 = player.getWorld().getPlayerByUsername(username);

    player2.getBonds().setTotalRedeemed(player2.getBonds().getTotalRedeemed() + amount);
    player.getGameEncoder()
        .sendMessage("Added " + amount + " redeemed bonds to " + player2.getUsername());
    player2.getGameEncoder().sendMessage(
        player.getUsername() + " added " + amount + " redeemed bonds to your account.");
  }
}
