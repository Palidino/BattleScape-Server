package com.palidinodh.osrsscript.packetdecoder.command;

import com.palidinodh.osrscore.io.Command;
import com.palidinodh.osrscore.io.cache.AnimationDef;
import com.palidinodh.osrscore.model.player.Player;
import lombok.var;

public class FindAnimMatchCommand implements Command {
  @Override
  public boolean canUse(Player player) {
    return player.getRights() == Player.RIGHTS_ADMIN;
  }

  @Override
  public void execute(Player player, String command) {
    var id = Integer.parseInt(command);
    AnimationDef.findMatches(id);
  }
}
