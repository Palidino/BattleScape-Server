package com.palidinodh.osrsscript.map.area.edgeville.npc;

import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.io.incomingpacket.NpcHandler;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.player.PCombat;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;

@ReferenceId(NpcId.EMBLEM_TRADER_316)
class EmblemTraderNpc implements NpcHandler {
  @Override
  public void execute(Player player, int option, Npc npc) {
    if (option == 0) {
      player.openDialogue("emblemtrader", 0);
    } else if (option == 2) {
      player.openShop("blood_money");
    } else if (option == 3) {
      player.getCombat().setShowKDR(!player.getCombat().showKDR());
      player.getGameEncoder().sendMessage("Streaks: " + player.getCombat().showKDR());
    } else if (option == 4) {
      player.getCombat().setPKSkullDelay(PCombat.SKULL_DELAY);
    }
  }
}
