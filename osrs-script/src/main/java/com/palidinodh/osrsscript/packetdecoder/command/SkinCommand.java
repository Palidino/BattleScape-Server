

package com.palidinodh.osrsscript.packetdecoder.command;

import com.palidinodh.osrscore.Main;
import com.palidinodh.osrscore.io.Command;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.setting.SqlUserRank;
import lombok.var;

public class SkinCommand implements Command {
  @Override
  public String getExample() {
    return "id";
  }

  @Override
  public boolean canUse(Player player) {
    return player.getRights() == Player.RIGHTS_ADMIN
        || player.isUsergroup(SqlUserRank.COMMUNITY_MANAGER) || Main.eventPriviledges(player);
  }

  @Override
  public void execute(Player player, String message) {
    var id = Integer.parseInt(message);
    player.getAppearance().setColor(4, id);
  }
}
