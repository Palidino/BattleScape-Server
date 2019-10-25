package com.palidinodh.osrsscript.packetdecoder.command;

import com.palidinodh.osrscore.Main;
import com.palidinodh.osrscore.io.Command;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.setting.SqlUserRank;
import lombok.var;

public class TeleCommand implements Command {
  @Override
  public String getExample() {
    return "region_id/x y (z)";
  }

  @Override
  public boolean canUse(Player player) {
    return player.getRights() == Player.RIGHTS_ADMIN
        || player.isUsergroup(SqlUserRank.COMMUNITY_MANAGER) || Main.eventPriviledges(player);
  }

  @Override
  public void execute(Player player, String message) {
    var values = message.split(" ");
    if (values.length == 1) {
      var id = Integer.parseInt(values[0]);
      player.getMovement().teleport(Tile.getAbsRegionX(id) + 32, Tile.getAbsRegionY(id) + 32);
      return;
    }
    var x = Integer.parseInt(values[0]);
    var y = Integer.parseInt(values[1]);
    var z = 0;
    if (values.length == 3) {
      z = Integer.parseInt(values[2]);
    }
    player.getMovement().teleport(x, y, z);
  }
}
