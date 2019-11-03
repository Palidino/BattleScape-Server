package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.osrscore.Main;
import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.model.player.Player;
import lombok.var;

public class OsrsKeyCommand implements CommandHandler {
  @Override
  public boolean canUse(Player player) {
    return player.getRights() == Player.RIGHTS_ADMIN;
  }

  @Override
  public void execute(Player player, String message) {
    if (message.length() == 0) {
      player.getGameEncoder().sendMessage("osrs key table: 1 in " + Main.osrsKeyTableChance);
      player.getGameEncoder().sendMessage("osrs key 50M: 1 in " + Main.osrsKey50MChance);
      player.getGameEncoder().sendMessage("osrs key 25M: 1 in " + Main.osrsKey25MChance);
      player.getGameEncoder().sendMessage("osrs key 10M: 1 in " + Main.osrsKey10MChance);
      player.getGameEncoder().sendMessage("osrs key 5M: 1 in " + Main.osrsKey5MChance);
      player.getGameEncoder()
          .sendMessage("50M: " + Main.getOsrsKeyCount("50m") + "; 25M: "
              + Main.getOsrsKeyCount("25m") + "; 10M: " + Main.getOsrsKeyCount("10M") + "; 5M: "
              + Main.getOsrsKeyCount("5m") + "; 1M: " + Main.getOsrsKeyCount("1m"));
      return;
    } else if (message.equals("disable")) {
      Main.osrsKeyTableChance = 0;
      player.getGameEncoder().sendMessage("osrs keys disabled");
      return;
    } else if (message.equals("enable")) {
      Main.osrsKeyTableChance = 128;
      player.getGameEncoder().sendMessage("osrs keys enabled at 1 in 128");
      return;
    }
    var messages = message.split(" ");
    var type = messages[0];
    var chance = Integer.parseInt(messages[1]);
    if (type.equals("table")) {
      Main.osrsKeyTableChance = chance;
      player.getGameEncoder()
          .sendMessage("Updated: osrs key table: 1 in " + Main.osrsKeyTableChance);
    } else if (type.equals("50m")) {
      Main.osrsKey50MChance = chance;
      player.getGameEncoder().sendMessage("Updated: osrs key 50M: 1 in " + Main.osrsKey50MChance);
    } else if (type.equals("25m")) {
      Main.osrsKey25MChance = chance;
      player.getGameEncoder().sendMessage("Updated: osrs key 25M: 1 in " + Main.osrsKey25MChance);
    } else if (type.equals("10m")) {
      Main.osrsKey10MChance = chance;
      player.getGameEncoder().sendMessage("Updated: osrs key 10M: 1 in " + Main.osrsKey10MChance);
    } else if (type.equals("5m")) {
      Main.osrsKey5MChance = chance;
      player.getGameEncoder().sendMessage("Updated: osrs key 5M: 1 in " + Main.osrsKey5MChance);
    }
  }
}
