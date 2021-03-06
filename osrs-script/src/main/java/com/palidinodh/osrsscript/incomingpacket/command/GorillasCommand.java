package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.setting.Settings;

class GorillasCommand implements CommandHandler {

  @Override
  public boolean canUse(Player player) {
    return Settings.getInstance().isSpawn() && player.inEdgeville()
        && !player.getController().inPvPWorld() && player.getController().canTeleport(false);
  }

  @Override
  public void execute(Player player, String message) {
    player.getMagic().standardTeleport(2972, 3940, 0);
    player.getGameEncoder().sendMessage("You teleport to Gorillas..");
  }

}
