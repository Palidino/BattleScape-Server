package com.palidinodh.osrsscript.player.plugin.combat.dialogue;

import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.model.dialogue.LargeSelectionDialogue;
import com.palidinodh.osrscore.model.dialogue.SelectionDialogue;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.Skills;
import lombok.var;

public class CombatLampLevel99Dialogue extends LargeSelectionDialogue {
  public CombatLampLevel99Dialogue(Player player) {
    class ConfirmationDialogue extends SelectionDialogue {
      ConfirmationDialogue(int skillId) {
        addOption("Confirm selection.", (childId, slot) -> {
          maxLevel(player, skillId);
        });
        addOption("Nevermind.");
      }
    }
    addOption("Attack", (childId, slot) -> {
      new ConfirmationDialogue(Skills.ATTACK);
    });
    addOption("Strength", (childId, slot) -> {
      new ConfirmationDialogue(Skills.STRENGTH);
    });
    addOption("Ranged", (childId, slot) -> {
      new ConfirmationDialogue(Skills.RANGED);
    });
    addOption("Magic", (childId, slot) -> {
      new ConfirmationDialogue(Skills.MAGIC);
    });
    addOption("Defence", (childId, slot) -> {
      new ConfirmationDialogue(Skills.DEFENCE);
    });
    addOption("Prayer", (childId, slot) -> {
      new ConfirmationDialogue(Skills.PRAYER);
    });
    addOption("Hitpoints", (childId, slot) -> {
      new ConfirmationDialogue(Skills.HITPOINTS);
    });
    open(player);
  }

  private void maxLevel(Player player, int skillId) {
    if (!player.getInventory().hasItem(ItemId.COMBAT_LAMP_LEVEL_99_32337)) {
      return;
    }
    if (player.getController().getLevelForXP(skillId) == 99) {
      player.getGameEncoder()
          .sendMessage("Your " + Skills.SKILL_NAMES[skillId] + " level is already 99.");
      return;
    }
    var xp = Skills.XP_TABLE[99] - player.getSkills().getXP(skillId);
    player.getInventory().deleteItem(ItemId.COMBAT_LAMP_LEVEL_99_32337);
    player.getSkills().addXp(skillId, xp, false);
  }
}
