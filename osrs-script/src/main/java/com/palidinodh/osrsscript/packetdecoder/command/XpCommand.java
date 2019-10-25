package com.palidinodh.osrsscript.packetdecoder.command;

import com.palidinodh.osrscore.io.Command;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.Skills;
import com.palidinodh.rs.setting.SqlUserRank;
import lombok.var;

public class XpCommand implements Command {
  @Override
  public String getExample() {
    return "skill_id skill_xp username_or_userid";
  }

  @Override
  public boolean canUse(Player player) {
    return player.getRights() == Player.RIGHTS_ADMIN
        || player.isUsergroup(SqlUserRank.COMMUNITY_MANAGER);
  }

  @Override
  public void execute(Player player, String command) {
    var split = command.split(" ");
    var skillID = Integer.parseInt(split[0]);
    if (skillID < 0 || skillID > Skills.SKILL_COUNT) {
      player.getGameEncoder().sendMessage("Max skill ID is " + Skills.SKILL_COUNT);
      return;
    }
    var skillXP = Integer.parseInt(split[1]);
    if (skillXP < 0 || skillXP > Skills.MAX_XP) {
      player.getGameEncoder().sendMessage("Skill level can be 0-" + Skills.MAX_XP);
      return;
    }
    var baseString = skillID + " " + skillXP;
    var username = command.substring(command.indexOf(baseString) + baseString.length() + 1);
    var player2 = player.getWorld().getPlayerByUsername(username);
    if (player2 == null) {
      var userID = -1;
      userID = Integer.parseInt(username);
      if (userID != -1) {
        player2 = player.getWorld().getPlayerById(userID);
      }
    }
    if (player2 == null) {
      player.getGameEncoder().sendMessage("Unable to find user " + username);
      return;
    }
    player2.getGameEncoder().sendMessage(player.getUsername() + " has adjusted your "
        + Skills.SKILL_NAMES[skillID] + " to " + Skills.getLevelSuppliedXP(skillXP));
    player.getGameEncoder().sendMessage("You have adjusted " + player2.getUsername() + "'s "
        + Skills.SKILL_NAMES[skillID] + " to " + Skills.getLevelSuppliedXP(skillXP));
    player2.getSkills().setXP(skillID, skillXP);
    player2.getSkills().setLevel(skillID, Skills.getLevelSuppliedXP(skillXP));
    player2.getSkills().setCombatLevel();
    player2.getGameEncoder().sendSkillLevel(skillID);
  }
}
