package com.palidinodh.osrsscript.packetdecoder.command;

import com.palidinodh.osrscore.io.Command;
import com.palidinodh.osrscore.model.player.Player;

public class MbCommand implements Command {
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
