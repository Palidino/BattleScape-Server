package com.palidinodh.osrsscript.map.area.edgeville.npc;

import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.io.incomingpacket.NpcHandler;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrsscript.player.plugin.slayer.SlayerPlugin;
import com.palidinodh.osrsscript.player.plugin.slayer.dialogue.MasterMenuDialogue;
import com.palidinodh.osrsscript.player.plugin.slayer.dialogue.RewardsDialogue;
import com.palidinodh.rs.ReferenceId;
import lombok.var;

@ReferenceId(NpcId.NIEVE)
class NieveNpc implements NpcHandler {
  @Override
  public void execute(Player player, int option, Npc npc) {
    var plugin = player.getPlugin(SlayerPlugin.class);
    if (option == 0) {
      player.openDialogue(new MasterMenuDialogue(player));
    } else if (option == 2) {
      plugin.getAssignment();
    } else if (option == 3) {
      player.openShop("slayer");
    } else if (option == 4) {
      player.openDialogue(new RewardsDialogue(player));
    }
  }
}
