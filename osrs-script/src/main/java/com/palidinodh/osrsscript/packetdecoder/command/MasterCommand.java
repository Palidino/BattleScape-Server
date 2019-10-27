package com.palidinodh.osrsscript.packetdecoder.command;

import com.palidinodh.osrscore.io.Command;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.Skills;
import com.palidinodh.rs.setting.Settings;
import com.palidinodh.rs.setting.SqlUserRank;
import lombok.var;

public class MasterCommand implements Command {
  @Override
  public boolean canUse(Player player) {
    return Settings.getInstance().isBeta() && !Settings.getInstance().isBetaSaving()
        || player.getRights() == Player.RIGHTS_ADMIN
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
