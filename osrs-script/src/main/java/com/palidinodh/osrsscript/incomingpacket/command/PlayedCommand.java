package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.model.player.Player;

class PlayedCommand implements CommandHandler {

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
