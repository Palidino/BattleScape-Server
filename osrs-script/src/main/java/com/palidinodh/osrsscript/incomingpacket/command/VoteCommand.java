package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.setting.Settings;

class VoteCommand implements CommandHandler {
  @Override
  public String getExample() {
    return "- Opens the voting page.";
  }

  @Override
  public void execute(Player player, String message) {
    player.getGameEncoder().sendOpenUrl(Settings.getInstance().getVoteUrl());
  }
}
