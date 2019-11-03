package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.setting.Settings;

public class StaffOnlyCommand implements CommandHandler {
  @Override
  public String getExample() {
    return "true/false";
  }

  @Override
  public boolean canUse(Player player) {
    return player.getRights() == Player.RIGHTS_ADMIN;
  }

  @Override
  public void execute(Player player, String message) {
    Settings.getInstance().setStaffOnly(Boolean.parseBoolean(message));
    player.getGameEncoder().sendMessage("Staff only: " + message);
  }
}
