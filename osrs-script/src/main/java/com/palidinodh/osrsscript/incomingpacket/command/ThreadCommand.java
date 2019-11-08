package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.setting.Settings;

public class ThreadCommand implements CommandHandler {
  @Override
  public String getExample() {
    return "- Open a thread by id.";
  }

  @Override
  public void execute(Player player, String id) {
    player.getGameEncoder().sendOpenUrl(Settings.getInstance().getThreadUrl() + id);
  }
}
