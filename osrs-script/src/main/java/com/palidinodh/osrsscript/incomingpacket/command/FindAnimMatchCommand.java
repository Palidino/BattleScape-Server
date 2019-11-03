package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.io.cache.AnimationDef;
import com.palidinodh.osrscore.model.player.Player;
import lombok.var;

public class FindAnimMatchCommand implements CommandHandler {
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
