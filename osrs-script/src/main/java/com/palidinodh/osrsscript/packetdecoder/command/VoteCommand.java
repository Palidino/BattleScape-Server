package com.palidinodh.osrsscript.packetdecoder.command;

import com.palidinodh.osrscore.io.Command;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.setting.Settings;

public class VoteCommand implements Command {
  @Override
  public String getExample() {
    return "- Opens the voting page.";
  }

  @Override
  public void execute(Player player, String message) {
    player.getGameEncoder().sendOpenURL(Settings.getInstance().getVoteUrl());
  }
}
