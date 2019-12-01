package com.palidinodh.osrsscript.player.plugin.slayer.dialogue;

import com.palidinodh.osrscore.model.dialogue.OptionsDialogue;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrsscript.player.plugin.slayer.SlayerPlugin;
import lombok.var;

public class RewardsDialogue extends OptionsDialogue {
  public RewardsDialogue(Player player) {
    var plugin = player.getPlugin(SlayerPlugin.class);
    addOption("Slayer rewards", (c, s) -> plugin.openRewards());
    addOption("Boss Slayer rewards", (c, s) -> player.openShop("boss_slayer"));
  }
}
