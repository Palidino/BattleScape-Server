package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.setting.Settings;

public class ReportCommand implements CommandHandler {
  @Override
  public String getExample() {
    return "- Opens the report-page.";
  }

  @Override
  public void execute(Player player, String message) {
    player.getGameEncoder().sendOpenUrl(Settings.getInstance().getReportPlayerUrl());
  }
}
