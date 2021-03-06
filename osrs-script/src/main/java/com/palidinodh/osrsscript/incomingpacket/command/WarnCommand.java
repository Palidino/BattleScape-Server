package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.io.JavaCord;
import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.setting.DiscordChannel;
import com.palidinodh.rs.setting.Settings;
import com.palidinodh.rs.setting.SqlUserRank;
import lombok.var;

class WarnCommand implements CommandHandler {
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
    }
    player2.getGameEncoder()
        .sendMessage("<col=FF0000>[WARNING]: You have been given an official warning by "
            + player.getUsername() + ". Further instances will result in action being taken.");
    player.getGameEncoder().sendMessage(username + " has been warned.");
    if (!Settings.getInstance().isLocal()) {
      JavaCord.sendMessage(DiscordChannel.MODERATION,
          player.getUsername() + " has warned " + username);
    } else {
      JavaCord.sendMessage(DiscordChannel.LOCAL, player.getUsername() + "has warned " + username);
    }
  }
}
