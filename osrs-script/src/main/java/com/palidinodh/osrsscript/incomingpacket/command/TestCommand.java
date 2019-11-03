package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.model.player.Player;
import lombok.var;

/*
 * Freely edit this to quickly test features. For commands that need more than a single use/test,
 * consider a proper command for it.
 */
@SuppressWarnings("all")
public class TestCommand implements CommandHandler {
  @Override
  public boolean canUse(Player player) {
    return player.getRights() == Player.RIGHTS_ADMIN;
  }

  @Override
  public void execute(Player player, String message) {
    // lms fog
    var baseX = 2968;
    var baseY = 3915;
    player.getGameEncoder().setVarbit(5314, 1);
    player.getGameEncoder().setVarbit(5320, baseX);
    player.getGameEncoder().setVarbit(5316, baseY);
    player.getGameEncoder().setVarbit(5317, 8);
  }
}
