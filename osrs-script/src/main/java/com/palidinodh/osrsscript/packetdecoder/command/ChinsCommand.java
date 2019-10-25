package com.palidinodh.osrsscript.packetdecoder.command;

import com.palidinodh.osrscore.io.Command;
import com.palidinodh.osrscore.model.dialogue.old.DialogueEntry;
import com.palidinodh.osrscore.model.dialogue.old.DialogueOld;
import com.palidinodh.osrscore.model.dialogue.old.DialogueScript;
import com.palidinodh.osrscore.model.player.Player;

public class ChinsCommand implements Command {
  @Override
  public boolean canUse(Player player) {
    return !player.getController().inWilderness() && !player.getController().inPvPWorld();
  }

  @Override
  public void execute(Player player, String message) {
    if (player.getController().canTeleport(true)) {
      DialogueEntry entry = new DialogueEntry();
      entry.setSelection("Are you sure you want to teleport to the wilderness?",
          "Yes, teleport me to the wilderness!", "No!");
      DialogueScript script = (p, index, childId, slot) -> {
        if (slot == 0) {

          player.getMagic().standardTeleport(3137, 3784, 0);
          player.getGameEncoder().sendMessage("You teleport to Chinchompas..");
          player.getController().stopWithTeleport();

        } else {
          return;
        }
      };
      DialogueOld.open(player, entry, script);
    } else {
      return;
    }
  }
}
