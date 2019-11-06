package com.palidinodh.osrsscript.incomingpacket.widget;

import com.palidinodh.osrscore.io.incomingpacket.WidgetHandler;
import com.palidinodh.osrscore.io.cache.id.VarpId;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.Skills;
import com.palidinodh.util.PNumber;

public class ChooseAdvanceSkillWidget implements WidgetHandler {
  @Override
  public int[] getIds() {
    return new int[] {WidgetId.CHOOSE_ADVANCE_SKILL};
  }

  @Override
  public void execute(Player player, int index, int widgetId, int childId, int slot, int itemId) {
    if (player.isLocked()) {
      return;
    }
    int[] skillOrder = new int[] {Skills.ATTACK, Skills.STRENGTH, Skills.RANGED, Skills.MAGIC,
        Skills.DEFENCE, Skills.HITPOINTS, Skills.PRAYER, Skills.AGILITY, Skills.HERBLORE,
        Skills.THIEVING, Skills.CRAFTING, Skills.RUNECRAFTING, Skills.MINING, Skills.SMITHING,
        Skills.FISHING, Skills.COOKING, Skills.FIREMAKING, Skills.WOODCUTTING, Skills.FLETCHING,
        Skills.SLAYER, Skills.FARMING, Skills.CONSTRUCTION, Skills.HUNTER};
    if (childId - 3 >= 0 && childId - 3 < skillOrder.length) {
      int skillId = skillOrder[childId - 3];
      if (skillId == Skills.CONSTRUCTION) {
        return;
      }
      player.putAttribute("choose_skill_id", skillId);
      player.getGameEncoder().setVarp(VarpId.CHOOSE_ADVANCE_SKILL, childId - 2);
      int xpMultipler = player.getSkills().getXPMultipler(skillId)
          * player.getAttributeInt("choose_xp_multiplier");
      if (xpMultipler == 0) {
        xpMultipler = 1;
      }
      int xp = player.getAttributeInt("choose_fixed_xp");
      if (player.getAttributeInt("choose_skill_level_multiplier_" + skillId) > 0) {
        xp = player.getController().getLevelForXP(skillId)
            * player.getAttributeInt("choose_skill_level_multiplier_" + skillId);
      } else if (player.getAttributeInt("choose_skill_level_multiplier") > 0) {
        xp = player.getController().getLevelForXP(skillId)
            * player.getAttributeInt("choose_skill_level_multiplier");
      }
      xp *= xpMultipler;
      player.getGameEncoder().sendWidgetText(WidgetId.CHOOSE_ADVANCE_SKILL, 2,
          "Choose the stat you wish to be advanced!<br>Experience that will be given: "
              + PNumber.formatNumber(xp));
    } else if (childId == 26) {
      int skillId = player.getAttributeInt("choose_skill_id");
      int minLevel = player.getAttributeInt("choose_min_level");
      if (minLevel > 1 && player.getController().getLevelForXP(skillId) < minLevel) {
        player.getGameEncoder()
            .sendMessage("You need a minimum level of " + minLevel + " to do this.");
        return;
      }
      int xpMultipler = player.getSkills().getXPMultipler(skillId)
          * player.getAttributeInt("choose_xp_multiplier");
      if (xpMultipler == 0) {
        xpMultipler = 1;
      }
      int xp = player.getAttributeInt("choose_fixed_xp");
      if (player.getAttributeInt("choose_skill_level_multiplier_" + skillId) > 0) {
        xp = player.getController().getLevelForXP(skillId)
            * player.getAttributeInt("choose_skill_level_multiplier_" + skillId);
      } else if (player.getAttributeInt("choose_skill_level_multiplier") > 0) {
        xp = player.getController().getLevelForXP(skillId)
            * player.getAttributeInt("choose_skill_level_multiplier");
      }
      xp *= xpMultipler;
      player.getSkills().addXp(skillId, xp, false);
      player.getInventory().deleteItem(player.getAttributeInt("choose_item_id"), 1);
      player.getWidgetManager().removeInteractiveWidgets();
    }
  }
}
