package com.palidinodh.osrsscript.packetdecoder.command;

import com.palidinodh.osrscore.Main;
import com.palidinodh.osrscore.io.Command;
import com.palidinodh.osrscore.model.player.Player;

public class StoreCommand implements Command {

  @Override
  public String getExample() {
    return "- Opens up the store page.";
  }

  @Override
  public void execute(Player player, String message) {
    player.getGameEncoder().sendOpenURL(Main.getSettings().getStoreUrl());
  }
}
