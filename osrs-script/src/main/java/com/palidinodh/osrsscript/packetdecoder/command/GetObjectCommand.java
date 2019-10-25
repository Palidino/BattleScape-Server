package com.palidinodh.osrsscript.packetdecoder.command;

import com.palidinodh.osrscore.io.Command;
import com.palidinodh.osrscore.model.player.Player;
import lombok.var;

public class GetObjectCommand implements Command {
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
