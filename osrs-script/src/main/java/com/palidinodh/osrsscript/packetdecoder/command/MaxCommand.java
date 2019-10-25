package com.palidinodh.osrsscript.packetdecoder.command;

import com.palidinodh.osrscore.Main;
import com.palidinodh.osrscore.io.Command;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.Skills;
import com.palidinodh.rs.setting.SqlUserRank;
import lombok.var;

public class MaxCommand implements Command {
  @Override
  public boolean canUse(Player player) {
    return player.getRights() == Player.RIGHTS_ADMIN
        || player.isUsergroup(SqlUserRank.COMMUNITY_MANAGER)
        || Main.isSpawn() && player.inEdgeville() && !player.getController().inPvPWorld();
  }

  @Override
  public void execute(Player player, String message) {
    var skills = new int[] {Skills.ATTACK, Skills.DEFENCE, Skills.STRENGTH, Skills.HITPOINTS,
        Skills.RANGED, Skills.PRAYER, Skills.MAGIC};
    for (var skillId : skills) {
      player.getSkills().setLevel(skillId, 99);
      player.getSkills().setXP(skillId, Skills.XP_TABLE[99]);
      player.getGameEncoder().sendSkillLevel(skillId);
    }
    player.getSkills().setCombatLevel();
  }
}
