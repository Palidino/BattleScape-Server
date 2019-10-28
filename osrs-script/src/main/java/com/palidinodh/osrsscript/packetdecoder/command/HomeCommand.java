package com.palidinodh.osrsscript.packetdecoder.command;

import com.palidinodh.osrscore.io.Command;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.setting.Settings;

public class HomeCommand implements Command {
  @Override
  public boolean canUse(Player player) {
    return Settings.getInstance().isSpawn() && !player.getController().inWilderness()
        && !player.getController().inPvPWorld() && player.getController().canTeleport(false);
  }

  @Override
  public void execute(Player player, String message) {
    player.getMagic().standardTeleport(3087, 3493, 0);
    player.getGameEncoder().sendMessage("You teleport home..");
  }
}
