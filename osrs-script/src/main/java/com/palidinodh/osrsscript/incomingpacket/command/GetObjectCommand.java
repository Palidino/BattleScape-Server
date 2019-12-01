package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.model.player.Player;
import lombok.var;

class GetObjectCommand implements CommandHandler {
  @Override
  public String getExample() {
    return "type";
  }

  @Override
  public boolean canUse(Player player) {
    return player.getRights() == Player.RIGHTS_ADMIN;
  }

  @Override
  public void execute(Player player, String message) {
    var type = Integer.parseInt(message);
    var mapObject = player.getController().getMapObjectByType(type, player);
    if (mapObject == null) {
      return;
    }
    player.getGameEncoder().sendMessage("Id: " + mapObject.getId());
  }
}
