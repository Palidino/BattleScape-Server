package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.model.dialogue.DialogueOption;
import com.palidinodh.osrscore.model.dialogue.OptionsDialogue;
import com.palidinodh.osrscore.model.player.Player;

class WestsCommand implements CommandHandler {
  @Override
  public boolean canUse(Player player) {
    return !player.getController().inWilderness() && !player.getController().inPvPWorld();
  }

  @Override
  public void execute(Player player, String message) {
    if (!player.getController().canTeleport(true)) {
      return;
    }
    player.openDialogue(new OptionsDialogue("Are you sure you want to teleport to the wilderness?",
        new DialogueOption("Are you sure you want to teleport to the wilderness?",
            (c, s) -> {
              player.getMagic().standardTeleport(2976, 3604, 0);
              player.getGameEncoder().sendMessage("You teleport to West dragons..");
              player.getController().stopWithTeleport();
            }),
        new DialogueOption("No!")));
  }
}
