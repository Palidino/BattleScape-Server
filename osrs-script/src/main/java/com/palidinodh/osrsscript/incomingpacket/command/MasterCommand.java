package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.Skills;
import com.palidinodh.rs.setting.Settings;
import com.palidinodh.rs.setting.SqlUserRank;
import lombok.var;

public class MasterCommand implements CommandHandler {
  @Override
  public boolean canUse(Player player) {
    return Settings.isBeta() || player.getRights() == Player.RIGHTS_ADMIN
        || player.isUsergroup(SqlUserRank.COMMUNITY_MANAGER);
  }

  @Override
  public void execute(Player player, String message) {
    for (var i = 0; i < Skills.SKILL_COUNT; i++) {
      player.getSkills().setXP(i, Skills.XP_TABLE[99]);
      player.getGameEncoder().sendSkillLevel(i);
      player.restore();
    }
  }
}
