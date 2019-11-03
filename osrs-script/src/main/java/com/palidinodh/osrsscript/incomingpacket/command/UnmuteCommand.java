package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.util.RequestManager;
import com.palidinodh.rs.setting.SqlUserRank;
import lombok.var;

public class UnmuteCommand implements CommandHandler {
  @Override
  public String getExample() {
    return "hours username_or_userid";
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
    }
    player2.getGameEncoder().sendMessage(player.getUsername() + " has unmuted you.");
    player2.getMessaging().setMuteTime(0, null);
    player.getGameEncoder().sendMessage(username + " has been unmuted.");
    player.getWorld().sendStaffMessage("[<col=ff0000>Staff</col>] " + player.getUsername()
        + " has unmuted " + player2.getUsername() + ".");
    RequestManager.addPlayerLog("mute/0.txt",
        player.getLogName() + " unmuted " + player2.getLogName() + ".");
  }
}
