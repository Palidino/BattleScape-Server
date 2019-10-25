package com.palidinodh.osrsscript.packetdecoder.command;

import com.palidinodh.osrscore.io.Command;
import com.palidinodh.osrscore.model.player.Player;

public class ChatIconCommand implements Command {
  @Override
  public String getExample() {
    return "id";
  }

  @Override
  public boolean canUse(Player player) {
    return player.getRights() == Player.RIGHTS_ADMIN;
  }

  @Override
  public void execute(Player player, String message) {
    player.getMessaging().setOverrideIcon(Integer.parseInt(message));
  }
}
