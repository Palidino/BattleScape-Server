package com.palidinodh.osrsscript.map.area.edgeville.npc;

import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.io.incomingpacket.NpcHandler;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;

@ReferenceId(NpcId.CAPN_IZZY_NO_BEARD)
class CapnIzzyNoBeardNpc implements NpcHandler {
  @Override
  public void execute(Player player, int option, Npc npc) {
    player.openShop("agility");
  }
}
