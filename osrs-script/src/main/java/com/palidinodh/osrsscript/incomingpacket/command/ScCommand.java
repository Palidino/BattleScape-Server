package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.io.JavaCord;
import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.setting.DiscordChannel;
import com.palidinodh.rs.setting.SqlUserRank;

class ScCommand implements CommandHandler {
  @Override
  public String getExample() {
    return "message to staff only";
  }

  @Override
  public boolean canUse(Player player) {
    return player.isUsergroup(SqlUserRank.TRIAL_MODERATOR)
        || player.getRights() == Player.RIGHTS_MOD || player.getRights() == Player.RIGHTS_ADMIN
        || player.isUsergroup(SqlUserRank.COMMUNITY_MANAGER);
  }

  @Override
  public void execute(Player player, String message) {
    player.getWorld().sendStaffMessage("<col=FF0000>[Staff] " + player.getMessaging().getIconImage()
        + player.getUsername() + ": " + message + "</col>");
    JavaCord.sendMessage(DiscordChannel.MODERATION, "[Staff chat] "
        + player.getMessaging().getIconImage() + player.getUsername() + ": " + message);
  }
}
