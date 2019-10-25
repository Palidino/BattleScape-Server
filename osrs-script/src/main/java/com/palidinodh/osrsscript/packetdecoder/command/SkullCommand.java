package com.palidinodh.osrsscript.packetdecoder.command;

import com.palidinodh.osrscore.io.Command;
import com.palidinodh.osrscore.model.player.PCombat;
import com.palidinodh.osrscore.model.player.Player;

public class SkullCommand implements Command {
  @Override
  public String getExample() {
    return "- Skulls you.";
  }

  @Override
  public void execute(Player player, String message) {
    player.getCombat().setPKSkullDelay(PCombat.SKULL_DELAY);
  }
}
