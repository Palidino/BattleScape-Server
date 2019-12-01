package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.setting.Settings;

class ShopsCommand implements CommandHandler {
  @Override
  public boolean canUse(Player player) {
    return Settings.getInstance().isSpawn() && !player.getController().inWilderness()
        && !player.getController().inPvPWorld() && player.getController().canTeleport(false);
  }

  @Override
  public void execute(Player player, String message) {
    player.getMovement().teleport(3096, 3503);
  }
}
