package com.palidinodh.osrsscript.packetdecoder.command;

import com.palidinodh.osrscore.io.Command;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.setting.Settings;

public class StoreCommand implements Command {

  @Override
  public String getExample() {
    return "- Opens up the store page.";
  }

  @Override
  public void execute(Player player, String message) {
    player.getGameEncoder().sendOpenURL(Settings.getInstance().getStoreUrl());
  }
}
