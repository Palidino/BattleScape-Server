package com.palidinodh.osrsscript.map.area.edgeville.npc;

import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.io.incomingpacket.NpcHandler;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;

@ReferenceId(NpcId.BOB_BARTER_HERBS)
class BobBarterNpc implements NpcHandler {
  @Override
  public void execute(Player player, int option, Npc npc) {
    if (option == 0) {
      player.openDialogue("bobbarter", 0);
    } else if (option == 2) {
      player.openShop("herb_exchange");
    } else if (option == 3) {
      player.getSkills().decantAllPotions();
    }
  }
}
