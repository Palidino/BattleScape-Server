package com.palidinodh.osrsscript.player.plugin.clanwars.dialogue;

import com.palidinodh.osrscore.model.dialogue.OptionsDialogue;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrsscript.player.plugin.clanwars.ClanWarsPlugin;

public class LeaveBattleDialogue extends OptionsDialogue {
  public LeaveBattleDialogue(Player player, ClanWarsPlugin plugin) {
    addOption("Leave the Clan War.", (childId, slot) -> {
      if (!player.getController().getVariableBool("clan_wars")) {
        return;
      }
      player.getController().stop();
    });
    addOption("Nevermind.");
    open(player);
  }
}
