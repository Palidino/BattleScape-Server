package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.util.PString;
import lombok.var;

class FakeDropCommand implements CommandHandler {
  @Override
  public String getExample() {
    return "username,itemname,from. ex. ::fakedrop miika,blue partyhat,bloodier key";
  }

  @Override
  public boolean canUse(Player player) {
    return player.getRights() == Player.RIGHTS_ADMIN;
  }

  @Override
  public void execute(Player player, String message) {
    var split = message.split(",");
    var username = split[0];
    var player2 = player.getWorld().getPlayerByUsername(username);
    var itemName = split[1];
    var from = split[2];
    String msg = player2.getMessaging().getIconImage() + player2.getUsername() + " has received "
        + PString.aOrAn(itemName) + " " + itemName + " drop";
    if (from != null) {
      msg += " from " + from;
    }
    msg += "!";
    player.getWorld().sendNews(msg);
  }
}
