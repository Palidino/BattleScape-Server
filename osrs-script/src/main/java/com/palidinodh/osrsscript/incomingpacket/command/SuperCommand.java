package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.setting.SqlUserRank;
import lombok.var;

public class SuperCommand implements CommandHandler {
  @Override
  public boolean canUse(Player player) {
    return player.getRights() == Player.RIGHTS_ADMIN
        || player.isUsergroup(SqlUserRank.COMMUNITY_MANAGER);
  }

  @Override
  public void execute(Player player, String message) {
    player.getMovement().setEnergy(999999999);
    player.setHitpoints(999999999);
    player.getPrayer().setPoints(999999999);
    for (var i = 0; i <= 6; i++) {
      player.getSkills().setLevel(i, 255);
      player.getGameEncoder().sendSkillLevel(i);
    }
    player.getGameEncoder().sendMessage("You feel superior..");
  }
}
