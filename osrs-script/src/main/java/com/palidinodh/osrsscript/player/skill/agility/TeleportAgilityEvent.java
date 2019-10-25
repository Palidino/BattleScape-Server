package com.palidinodh.osrsscript.player.skill.agility;

import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.player.Player;
import lombok.Builder;

@Builder
public class TeleportAgilityEvent extends AgilityEvent {
  private int completeDelay;
  private int visualDelay;
  private Tile tile;
  private int startAnimation;
  private int endAnimation;
  private Graphic graphic;

  public boolean complete(Player player) {
    if ((completeDelay > 0 || visualDelay > 0)
        && (player.getX() != tile.getX() || player.getY() != tile.getY())) {
      player.setFaceTile(tile);
    }
    if (visualDelay > 0) {
      visualDelay--;
      return false;
    }
    if (startAnimation > 0) {
      player.setAnimation(startAnimation);
      startAnimation = -1;
    }
    if (graphic != null) {
      player.setGraphic(graphic);
      graphic = null;
    }
    if (completeDelay > 0) {
      completeDelay--;
      return false;
    }
    if (endAnimation > 0) {
      player.setAnimation(endAnimation);
      endAnimation = -1;
    }
    player.getMovement().teleport(tile);
    return true;
  }
}
