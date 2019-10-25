package com.palidinodh.osrsscript.packetdecoder.command;

import com.palidinodh.osrscore.io.Command;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.util.PEvent;
import lombok.var;

@SuppressWarnings("all")
public class PnpcLoopCommand implements Command {
  @Override
  public boolean canUse(Player player) {
    return player.getRights() == Player.RIGHTS_ADMIN;
  }

  @Override
  public void execute(Player player, String message) {
    var fromId = Integer.parseInt(message);
    var event = new PEvent(1) {
      int id = fromId;
      boolean reset = false;

      @Override
      public void execute() {
        if (reset) {
          reset = false;
          player.getAppearance().setNpcId(-1);
          setTick(0);
        } else {
          reset = true;
          player.getGameEncoder().sendMessage("Npc id: " + id);
          player.getAppearance().setNpcId(id++);
          setTick(1);
        }
      }
    };
    player.setAction(event);
  }
}
