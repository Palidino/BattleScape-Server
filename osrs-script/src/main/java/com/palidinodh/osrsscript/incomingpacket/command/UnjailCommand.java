package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.setting.SqlUserRank;
import lombok.var;

public class UnjailCommand implements CommandHandler {
  @Override
  public String getExample() {
    return "username";
  }

  @Override
  public boolean canUse(Player player) {
    return player.isUsergroup(SqlUserRank.TRIAL_MODERATOR)
        || player.getRights() == Player.RIGHTS_MOD || player.getRights() == Player.RIGHTS_ADMIN
        || player.isUsergroup(SqlUserRank.COMMUNITY_MANAGER);
  }

  @Override
  public void execute(Player player, String username) {
    var player2 = player.getWorld().getPlayerByUsername(username);
    if (player2 == null) {
      player.getGameEncoder().sendMessage("Unable to find user " + username + ".");
      return;
    } else if (player2.getController().isInstanced()) {
      player.getGameEncoder().sendMessage(username + " is in an instance located at: "
          + player.getX() + ", " + player.getY() + ", " + player.getHeight() + ".");
      return;
    }
    player2.getMovement().teleport(3093, 3495);
    player2.getGameEncoder().sendMessage("You have been unjailed.");
    player.getGameEncoder().sendMessage(username + " has been unjailed.");
    player.getWorld().sendStaffMessage("[<col=ff0000>Staff</col>] " + player.getUsername()
        + " has unjailed " + player2.getUsername() + ".");

  }
}