package com.palidinodh.osrsscript.packetdecoder.command;

import com.palidinodh.osrscore.io.Command;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.util.PEvent;
import lombok.var;

public class GfxLoopCommand implements Command {
  @Override
  public String getExample() {
    return "id";
  }

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
          player.setGraphic(-1);
          setTick(0);
        } else {
          reset = true;
          player.getGameEncoder().sendMessage("Graphic: " + id);
          player.setGraphic(id++, 20);
          setTick(1);
        }
      }
    };
    player.setAction(event);
  }
}
