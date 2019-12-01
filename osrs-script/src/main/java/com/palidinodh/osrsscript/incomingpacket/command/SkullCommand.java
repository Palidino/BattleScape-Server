package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.model.player.PCombat;
import com.palidinodh.osrscore.model.player.Player;

class SkullCommand implements CommandHandler {
  @Override
  public String getExample() {
    return "- Skulls you.";
  }

  @Override
  public void execute(Player player, String message) {
    player.getCombat().setPKSkullDelay(PCombat.SKULL_DELAY);
  }
}
