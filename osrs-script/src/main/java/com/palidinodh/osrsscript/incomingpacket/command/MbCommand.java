package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.model.player.Player;

class MbCommand implements CommandHandler {
  @Override
  public boolean canUse(Player player) {
    return !player.getController().inWilderness() && !player.getController().inPvPWorld();
  }

  @Override
  public void execute(Player player, String message) {
    if (player.getController().canTeleport(true)) {
      player.getMagic().standardTeleport(2539, 4718, 0);
      player.getGameEncoder().sendMessage("You teleport to Mage bank..");
      player.getController().stopWithTeleport();
    } else {
      return;
    }
  }
}
