package com.palidinodh.osrsscript.packetdecoder.command;

import com.palidinodh.osrscore.Main;
import com.palidinodh.osrscore.io.Command;
import com.palidinodh.osrscore.model.player.Player;

public class ThreadCommand implements Command {
  @Override
  public String getExample() {
    return "- Open a thread by id.";
  }

  @Override
  public void execute(Player player, String id) {
    player.getGameEncoder().sendOpenURL(Main.getSettings().getThreadUrl() + id);
  }
}
