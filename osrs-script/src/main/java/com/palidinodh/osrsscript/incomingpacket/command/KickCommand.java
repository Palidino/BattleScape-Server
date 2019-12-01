package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.setting.SqlUserRank;
import lombok.var;

class KickCommand implements CommandHandler {
  @Override
  public String getExample() {
    return "username";
  }

  @Override
  public boolean canUse(Player player) {
    return player.getRights() == Player.RIGHTS_MOD || player.getRights() == Player.RIGHTS_ADMIN
        || player.isUsergroup(SqlUserRank.COMMUNITY_MANAGER);
  }

  @Override
  public void execute(Player player, String username) {
    var player2 = player.getWorld().getPlayerByUsername(username);
    if (player2 == null) {
      player.getGameEncoder().sendMessage("Unable to find user " + username + ".");
      return;
    } else if (player2.getController().inWilderness()) {
      player.getGameEncoder().sendMessage("This player is in the wilderness.");
      return;
    } else if (player2.getController().inPvPWorld()) {
      player.getGameEncoder().sendMessage("This player is in the PvP world.");
      return;
    }
    player2.getGameEncoder().sendLogout();
    player2.setVisible(false);
    player2.getGameEncoder().sendMessage(player.getUsername() + " has kicked you.");
    player.getGameEncoder().sendMessage(username + " has been kicked.");
    player.getWorld().sendStaffMessage("[<col=ff0000>Staff</col>] " + player.getUsername()
        + " has kicked " + player2.getUsername() + ".");
  }
}
