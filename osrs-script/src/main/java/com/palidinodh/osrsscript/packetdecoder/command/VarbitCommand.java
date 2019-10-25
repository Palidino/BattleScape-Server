package com.palidinodh.osrsscript.packetdecoder.command;

import com.palidinodh.osrscore.io.Command;
import com.palidinodh.osrscore.model.player.Player;
import lombok.var;

public class VarbitCommand implements Command {
  @Override
  public String getExample() {
    return "id value";
  }

  @Override
  public boolean canUse(Player player) {
    return player.getRights() == Player.RIGHTS_ADMIN;
  }

  @Override
  public void execute(Player player, String message) {
    var values = message.split(" ");
    var id = Integer.parseInt(values[0]);
    var value = Integer.parseInt(values[1]);
    player.getGameEncoder().setVarbit(id, value);
  }
}
