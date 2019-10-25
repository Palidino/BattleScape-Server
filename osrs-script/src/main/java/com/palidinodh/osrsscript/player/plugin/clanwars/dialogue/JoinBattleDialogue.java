package com.palidinodh.osrsscript.player.plugin.clanwars.dialogue;

import com.palidinodh.osrscore.model.dialogue.SelectionDialogue;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrsscript.player.plugin.clanwars.ClanWarsPlugin;
import com.palidinodh.osrsscript.player.plugin.clanwars.Stages;

public class JoinBattleDialogue extends SelectionDialogue {
  public JoinBattleDialogue(Player player, ClanWarsPlugin plugin) {
    addOption("Join your clan's battle.", (childId, slot) -> {
      Stages.openJoin(player, plugin);
    });
    addOption("Observe your clan's battle.", (childId, slot) -> {
      Stages.openView(player, plugin);
    });
    addOption("Cancel.");
    open(player);
  }
}
