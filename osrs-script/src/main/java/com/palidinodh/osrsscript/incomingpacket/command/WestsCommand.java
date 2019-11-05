package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.model.dialogue.old.DialogueEntry;
import com.palidinodh.osrscore.model.dialogue.old.DialogueOld;
import com.palidinodh.osrscore.model.dialogue.old.DialogueScript;
import com.palidinodh.osrscore.model.player.Player;

public class WestsCommand implements CommandHandler {
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
          player.getMagic().standardTeleport(2976, 3604, 0);
          player.getGameEncoder().sendMessage("You teleport to West dragons..");
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