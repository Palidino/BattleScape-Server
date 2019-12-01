package com.palidinodh.osrsscript.player.plugin.combat;

import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.model.dialogue.LargeOptionsDialogue;
import com.palidinodh.osrscore.model.dialogue.OptionsDialogue;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.Skills;
import lombok.var;

class CombatLampLevel99Dialogue extends LargeOptionsDialogue {
  public CombatLampLevel99Dialogue(Player player) {
    class ConfirmationDialogue extends OptionsDialogue {
      ConfirmationDialogue(int skillId) {
        addOption("Confirm selection.", (c, s) -> {
          maxLevel(player, skillId);
        });
        addOption("Nevermind.");
      }
    }
    addOption("Attack", (c, s) -> {
      player.openDialogue(new ConfirmationDialogue(Skills.ATTACK));
    });
    addOption("Strength", (c, s) -> {
      player.openDialogue(new ConfirmationDialogue(Skills.STRENGTH));
    });
    addOption("Ranged", (c, s) -> {
      player.openDialogue(new ConfirmationDialogue(Skills.RANGED));
    });
    addOption("Magic", (c, s) -> {
      player.openDialogue(new ConfirmationDialogue(Skills.MAGIC));
    });
    addOption("Defence", (c, s) -> {
      player.openDialogue(new ConfirmationDialogue(Skills.DEFENCE));
    });
    addOption("Prayer", (c, s) -> {
      player.openDialogue(new ConfirmationDialogue(Skills.PRAYER));
    });
    addOption("Hitpoints", (c, s) -> {
      player.openDialogue(new ConfirmationDialogue(Skills.HITPOINTS));
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
