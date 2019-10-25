package com.palidinodh.osrsscript.packetdecoder.widget;

import com.palidinodh.osrscore.Main;
import com.palidinodh.osrscore.io.Widget;
import com.palidinodh.osrscore.io.cache.WidgetId;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.Skills;
import com.palidinodh.osrscore.model.player.WidgetManager;

public class SkillsWidget implements Widget {
  @Override
  public int[] getIds() {
    return new int[] {WidgetId.SKILLS, WidgetId.SKILL_GUIDE};
  }

  @Override
  public void execute(Player player, int index, int widgetId, int childId, int slot, int itemId) {
    if (player.isLocked()) {
      return;
    }
    if (widgetId == WidgetId.SKILLS) {
      int guideId = -1;
      switch (childId) {
        case 1:
          guideId = Skills.ATTACK;
          break;
        case 2:
          guideId = Skills.STRENGTH;
          break;
        case 3:
          guideId = Skills.DEFENCE;
          break;
        case 4:
          guideId = Skills.RANGED;
          break;
        case 5:
          guideId = Skills.PRAYER;
          break;
        case 6:
          guideId = Skills.MAGIC;
          break;
        case 7:
          guideId = Skills.RUNECRAFTING;
          break;
        case 8:
          guideId = Skills.CONSTRUCTION;
          break;
        case 9:
          guideId = Skills.HITPOINTS;
          break;
        case 10:
          guideId = Skills.AGILITY;
          break;
        case 11:
          guideId = Skills.HERBLORE;
          break;
        case 12:
          guideId = Skills.THIEVING;
          break;
        case 13:
          guideId = Skills.CRAFTING;
          break;
        case 14:
          guideId = Skills.FLETCHING;
          break;
        case 15:
          guideId = Skills.SLAYER;
          break;
        case 16:
          guideId = Skills.HUNTER;
          break;
        case 17:
          guideId = Skills.MINING;
          break;
        case 18:
          guideId = Skills.SMITHING;
          break;
        case 19:
          guideId = Skills.FISHING;
          break;
        case 20:
          guideId = Skills.COOKING;
          break;
        case 21:
          guideId = Skills.FIREMAKING;
          break;
        case 22:
          guideId = Skills.WOODCUTTING;
          break;
        case 23:
          guideId = Skills.FARMING;
          break;
      }
      if (guideId != -1) {
        player.getWidgetManager().removeInteractiveWidgets();
        player.getWidgetManager().sendInteractiveOverlay(WidgetId.SKILL_GUIDE,
            new WidgetManager.CloseEvent() {
              @Override
              public void execute() {
                player.removeAttribute("skill_guide_id");
              }
            });
        player.getGameEncoder().sendWidgetSettings(WidgetId.SKILL_GUIDE, 8, 0, 99, 0);
        player.getGameEncoder().setVarp(965, Skills.GUIDE_CONFIGS[guideId][0]);
        player.putAttribute("skill_guide_id", guideId);
        if (guideId < player.getSkills().getXPLocks().length) {
          if (Main.isSpawn()) {
            player.openDialogue("xplock", guideId + 7);
          } else {
            player.openDialogue("xplock", guideId);
          }
        }
      }
    } else if (widgetId == WidgetId.SKILL_GUIDE) {
      if (childId >= 11 && childId <= 24) {
        int guideId = player.getAttributeInt("skill_guide_id");
        int guideIndex = childId - 11;
        if (guideIndex >= Skills.GUIDE_CONFIGS[guideId].length) {
          return;
        }
        player.getGameEncoder().setVarp(965, Skills.GUIDE_CONFIGS[guideId][guideIndex]);
      }
    }
  }
}
