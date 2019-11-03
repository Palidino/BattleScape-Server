package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.osrscore.Main;
import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.setting.SqlUserRank;

public class BroadcastCommand implements CommandHandler {
  @Override
  public String getExample() {
    return "message";
  }

  @Override
  public boolean canUse(Player player) {
    return player.getRights() == Player.RIGHTS_MOD
        || player.isUsergroup(SqlUserRank.COMMUNITY_MANAGER)
        || player.getRights() == Player.RIGHTS_ADMIN || Main.eventPriviledges(player);
  }

  @Override
  public void execute(Player player, String message) {
    player.getWorld().sendBroadcast(
        player.getMessaging().getIconImage() + player.getUsername() + ": " + message);
  }
}
