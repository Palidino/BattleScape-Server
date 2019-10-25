package com.palidinodh.osrsscript.packetdecoder.command;

import com.palidinodh.osrscore.Main;
import com.palidinodh.osrscore.io.Command;
import com.palidinodh.osrscore.model.player.Player;

public class RiskZoneCommand implements Command {

  @Override
  public boolean canUse(Player player) {
    return Main.isSpawn() && player.inEdgeville() && !player.getController().inPvPWorld()
        && player.getController().canTeleport(false);
  }

  @Override
  public void execute(Player player, String message) {
    player.getMagic().standardTeleport(2655, 5471, 0);
    player.getGameEncoder().sendMessage("You teleport to The risk zone..");
  }

}
