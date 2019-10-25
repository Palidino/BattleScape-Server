package com.palidinodh.osrsscript.packetdecoder.command;

import com.palidinodh.osrscore.Main;
import com.palidinodh.osrscore.io.Command;
import com.palidinodh.osrscore.model.player.Player;

public class ShopsCommand implements Command {
  @Override
  public boolean canUse(Player player) {
    return Main.isSpawn() && !player.getController().inWilderness()
        && !player.getController().inPvPWorld() && player.getController().canTeleport(false);
  }

  @Override
  public void execute(Player player, String message) {
    player.getMovement().teleport(3096, 3503);
  }
}
