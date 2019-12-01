package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.model.Movement;
import com.palidinodh.osrscore.model.player.Player;

class InvisibleCommand implements CommandHandler {
  @Override
  public boolean canUse(Player player) {
    return player.getRights() == Player.RIGHTS_ADMIN;
  }

  @Override
  public void execute(Player player, String message) {
    if (player.getAppearance().getNpcId() == Movement.VIEWING_NPC_ID) {
      player.getAppearance().setNpcId(-1);
    } else {
      player.getAppearance().setNpcId(Movement.VIEWING_NPC_ID);
    }
  }
}
