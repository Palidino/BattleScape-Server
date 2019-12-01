package com.palidinodh.osrsscript.player.plugin.slayer.dialogue;

import com.palidinodh.osrscore.model.dialogue.OptionsDialogue;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrsscript.player.plugin.slayer.SlayerPlugin;
import lombok.var;

public class ChooseMasterDialogue extends OptionsDialogue {
  public ChooseMasterDialogue(Player player) {
    var plugin = player.getPlugin(SlayerPlugin.class);
    addOption("Mazchna - level 20", (c, s) -> plugin.getAssignment("Mazchna"));
    addOption("Chaeldar - level 70", (c, s) -> plugin.getAssignment("Chaeldar"));
    addOption("Nieve - level 85", (c, s) -> plugin.getAssignment("Nieve"));
    addOption("Duradel - level 100", (c, s) -> plugin.getAssignment("Duradel"));
    addOption("Krystilia - wilderness", (c, s) -> player.getGameEncoder()
        .sendMessage("Please speak to krystilia for a wilderness task."));
  }
}
