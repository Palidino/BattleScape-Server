package com.palidinodh.osrsscript.player.plugin.slayer.dialogue;

import com.palidinodh.osrscore.model.dialogue.OptionsDialogue;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrsscript.player.plugin.slayer.SlayerPlugin;

public class ChooseMasterDialogue extends OptionsDialogue {
  public ChooseMasterDialogue(Player player, SlayerPlugin plugin) {
    addOption("Mazchna - level 20", (childId, slot) -> {
      plugin.getAssignment("Mazchna");
    });
    addOption("Chaeldar - level 70", (childId, slot) -> {
      plugin.getAssignment("Chaeldar");
    });
    addOption("Nieve - level 85", (childId, slot) -> {
      plugin.getAssignment("Nieve");
    });
    addOption("Duradel - level 100", (childId, slot) -> {
      plugin.getAssignment("Duradel");
    });
    addOption("Krystilia - wilderness", (childId, slot) -> {
      player.getGameEncoder().sendMessage("Please speak to krystilia for a wilderness task.");
    });
    open(player);
  }
}
