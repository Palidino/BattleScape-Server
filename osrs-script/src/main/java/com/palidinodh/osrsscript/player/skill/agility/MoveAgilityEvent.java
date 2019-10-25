package com.palidinodh.osrsscript.player.skill.agility;

import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.player.Player;
import lombok.Builder;

@Builder
public class MoveAgilityEvent extends AgilityEvent {
  private Tile tile;
  private boolean running;
  private boolean noclip;
  private int animation;

  public boolean complete(Player player) {
    if (animation > 0) {
      player.getAppearance().setForceMoveAnimation(animation);
    }
    player.getMovement().setForceRunning(running);
    player.getMovement().clear();
    if (noclip) {
      player.getMovement().addMovement(tile);
    } else {
      player.getMovement().fullRoute(tile.getX(), tile.getY(), 0);
    }
    if (player.getX() != tile.getX() || player.getY() != tile.getY()) {
      return false;
    }
    player.getAppearance().setForceMoveAnimation(-1);
    player.getMovement().setForceRunning(null);
    return true;
  }
}
