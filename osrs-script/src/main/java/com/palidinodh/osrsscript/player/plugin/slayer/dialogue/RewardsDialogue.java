package com.palidinodh.osrsscript.player.plugin.slayer.dialogue;

import com.palidinodh.osrscore.model.dialogue.OptionsDialogue;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrsscript.player.plugin.slayer.SlayerPlugin;

public class RewardsDialogue extends OptionsDialogue {
  public RewardsDialogue(Player player, SlayerPlugin plugin) {
    addOption("Slayer rewards", (childId, slot) -> {
      plugin.openRewards();
    });
    addOption("Boss Slayer rewards", (childId, slot) -> {
      player.openShop("boss_slayer");
    });
  }
}
