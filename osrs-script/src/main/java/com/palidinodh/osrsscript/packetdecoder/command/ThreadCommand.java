package com.palidinodh.osrsscript.packetdecoder.command;

import com.palidinodh.osrscore.io.Command;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.setting.Settings;

public class ThreadCommand implements Command {
  @Override
  public String getExample() {
    return "- Open a thread by id.";
  }

  @Override
  public void execute(Player player, String id) {
    player.getGameEncoder().sendOpenURL(Settings.getInstance().getThreadUrl() + id);
  }
}
