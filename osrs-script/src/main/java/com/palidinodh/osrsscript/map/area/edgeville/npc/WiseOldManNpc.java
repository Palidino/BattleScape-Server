package com.palidinodh.osrsscript.map.area.edgeville.npc;

import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.io.incomingpacket.NpcHandler;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;

@ReferenceId(NpcId.WISE_OLD_MAN)
class WiseOldManNpc implements NpcHandler {
  @Override
  public void execute(Player player, int option, Npc npc) {
    player.openShop("platinum_tokens");
  }
}
