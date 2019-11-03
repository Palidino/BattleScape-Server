package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.io.JavaCord;
import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.setting.DiscordChannel;
import com.palidinodh.rs.setting.SqlUserRank;
import com.palidinodh.util.PLogger;
import lombok.var;

public class ShutdownCommand implements CommandHandler {
  @Override
  public String getExample() {
    return "minutes";
  }

  @Override
  public boolean canUse(Player player) {
    return player.isUsergroup(SqlUserRank.SENIOR_MODERATOR)
        || player.getRights() == Player.RIGHTS_ADMIN
        || player.isUsergroup(SqlUserRank.COMMUNITY_MANAGER);
  }

  @Override
  public void execute(Player player, String message) {
    var minutes = Integer.parseInt(message);
    player.getWorld().startShutdown(minutes);
    PLogger.println(player.getUsername() + " shut the server down with a countdown of " + minutes
        + " minutes.");
    JavaCord.sendMessage(DiscordChannel.MODERATION, player.getUsername()
        + " shut the server down with a countdown of " + minutes + " minutes.");
  }
}
