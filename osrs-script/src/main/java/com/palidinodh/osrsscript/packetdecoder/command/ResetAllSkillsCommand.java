package com.palidinodh.osrsscript.packetdecoder.command;

import com.palidinodh.osrscore.io.Command;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.Skills;
import com.palidinodh.rs.setting.Settings;
import lombok.var;

public class ResetAllSkillsCommand implements Command {
  @Override
  public boolean canUse(Player player) {
    return Settings.getInstance().isLocal();
  }

  @Override
  public void execute(Player player, String message) {
    for (var i = 0; i < Skills.SKILL_COUNT; i++) {
      player.getSkills().setXP(i, 1);
      if (i == Skills.HITPOINTS) {
        player.getSkills().setXP(i, Skills.XP_TABLE[10]);
      }
      player.getGameEncoder().sendSkillLevel(i);
    }
    player.restore();
  }
}
