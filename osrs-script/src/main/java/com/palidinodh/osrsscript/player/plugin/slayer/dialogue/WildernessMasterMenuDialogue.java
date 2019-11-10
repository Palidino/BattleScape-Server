package com.palidinodh.osrsscript.player.plugin.slayer.dialogue;

import com.palidinodh.osrscore.model.dialogue.OptionsDialogue;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.slayer.SlayerMaster;
import com.palidinodh.osrsscript.player.plugin.slayer.SlayerPlugin;

public class WildernessMasterMenuDialogue extends OptionsDialogue {
  public WildernessMasterMenuDialogue(Player player, SlayerPlugin plugin) {
    addOption("Get task", (childId, slot) -> {
      plugin.getAssignment(SlayerMaster.WILDERNESS_MASTER);
    });
    addOption("Current task", (childId, slot) -> {
      plugin.sendTask();
    });
    addOption("Cancel task (30 points)", (childId, slot) -> {
      plugin.cancelWildernessTask();
    });
  }
}
