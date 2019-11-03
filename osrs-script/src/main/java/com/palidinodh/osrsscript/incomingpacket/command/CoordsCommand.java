package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.setting.SqlUserRank;

public class CoordsCommand implements CommandHandler {
  @Override
  public boolean canUse(Player player) {
    return player.getRights() == Player.RIGHTS_ADMIN
        || player.isUsergroup(SqlUserRank.COMMUNITY_MANAGER);
  }

  @Override
  public void execute(Player player, String message) {
    player.getGameEncoder().sendMessage("mapClip="
        + player.getController().getMapClip(player.getX(), player.getY(), player.getHeight()));
    player.getGameEncoder()
        .sendMessage("solidMapObject=" + player.getController().getSolidMapObject(player));
    player.getGameEncoder()
        .sendMessage("x=" + player.getX() + ", y=" + player.getY() + ", z=" + player.getHeight()
            + ", client-z=" + player.getClientHeight() + ", region-id=" + player.getRegionId()
            + ", instanced=" + player.getController().isInstanced());
  }
}
