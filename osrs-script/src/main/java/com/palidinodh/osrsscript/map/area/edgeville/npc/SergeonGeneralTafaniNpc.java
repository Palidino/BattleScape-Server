package com.palidinodh.osrsscript.map.area.edgeville.npc;

import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.io.incomingpacket.NpcHandler;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;

@ReferenceId(NpcId.SURGEON_GENERAL_TAFANI)
class SergeonGeneralTafaniNpc implements NpcHandler {
  @Override
  public void execute(Player player, int option, Npc npc) {
    if (player.getX() != 3094 || player.getY() != 3498) {
      return;
    }
    player.setGraphic(436);
    player.getGameEncoder().sendMessage(npc.getDef().getName() + " restores you.");
    player.rejuvenate();
  }
}
