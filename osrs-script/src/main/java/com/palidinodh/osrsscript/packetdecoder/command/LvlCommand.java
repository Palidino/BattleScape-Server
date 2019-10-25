package com.palidinodh.osrsscript.packetdecoder.command;

import com.palidinodh.osrscore.io.Command;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.Skills;
import lombok.var;

public class LvlCommand implements Command {
  @Override
  public String getExample() {
    return "skill_id skill_level username_or_userid";
  }

  @Override
  public boolean canUse(Player player) {
    return player.getRights() == Player.RIGHTS_ADMIN;
  }

  @Override
  public void execute(Player player, String command) {
    var split = command.split(" ");
    var skillID = Integer.parseInt(split[0]);
    if (skillID < 0 || skillID > Skills.SKILL_COUNT) {
      player.getGameEncoder().sendMessage("Max skill ID is " + Skills.SKILL_COUNT);
      return;
    }
    var skillLevel = Integer.parseInt(split[1]);
    if (skillLevel < 1 || skillLevel > 99) {
      player.getGameEncoder().sendMessage("Skill level can be 1-99");
      return;
    }
    var baseString = skillID + " " + skillLevel;
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
        + Skills.SKILL_NAMES[skillID] + " to " + skillLevel);
    player.getGameEncoder().sendMessage("You have adjusted " + player2.getUsername() + "'s "
        + Skills.SKILL_NAMES[skillID] + " to " + skillLevel);
    if (player2.getSkills().getLevelForXP(skillID) < skillLevel) {
      player2.getSkills().addXp(skillID,
          Skills.XP_TABLE[skillLevel] - player2.getSkills().getXP(skillID), false);
    } else {
      player2.getSkills().setXP(skillID, Skills.XP_TABLE[skillLevel]);
      player2.getSkills().setLevel(skillID, skillLevel);
      player2.getSkills().setCombatLevel();
      player2.getGameEncoder().sendSkillLevel(skillID);
    }
  }
}
