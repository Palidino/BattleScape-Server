package com.palidinodh.osrsscript.map.area.edgeville.npc;

import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.io.incomingpacket.NpcHandler;
import com.palidinodh.osrscore.model.dialogue.DialogueOption;
import com.palidinodh.osrscore.model.dialogue.OptionsDialogue;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.slayer.SlayerMaster;
import com.palidinodh.osrsscript.player.plugin.slayer.SlayerPlugin;
import com.palidinodh.rs.ReferenceId;
import lombok.var;

@ReferenceId(NpcId.KRYSTILIA)
class KrystiliaNpc implements NpcHandler {
  @Override
  public void execute(Player player, int option, Npc npc) {
    var plugin = player.getPlugin(SlayerPlugin.class);
    if (option == 0) {
      player.openDialogue(new OptionsDialogue(
          new DialogueOption("Get task",
              (c, s) -> plugin.getAssignment(SlayerMaster.WILDERNESS_MASTER)),
          new DialogueOption("Current task", (c, s) -> plugin.sendTask()),
          new DialogueOption("Cancel task (30 points)",
              (c, s) -> plugin.cancelWildernessTask())));
    } else if (option == 2) {
      plugin.getAssignment(SlayerMaster.WILDERNESS_MASTER);
    } else if (option == 3) {
      player.openShop("slayer");
    } else if (option == 4) {
      plugin.openRewards();
    }
  }
}
