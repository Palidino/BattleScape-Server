package com.palidinodh.osrsscript.map.area.corporealbeastcave;

import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.map.Area;
import com.palidinodh.osrscore.model.map.MapObject;
import lombok.var;

public class CorporealBeastCaveArea extends Area {
  public CorporealBeastCaveArea() {
    super(11844);
  }

  @Override
  public boolean mapObjectOptionHook(int option, MapObject mapObject) {
    var player = getPlayer();
    switch (mapObject.getId()) {
      case ObjectId.PASSAGE:
        player.getCombat().setDamageInflicted(0);
        if (player.getX() <= 2970) {
          player.getMovement().teleport(2974, 4384, 2);
        } else {
          player.getMovement().teleport(2970, 4384, 2);
        }
        player.getController().stopWithTeleport();
        return true;
      case ObjectId.CAVE_EXIT:
        player.getMovement().ladderDownTeleport(new Tile(3206, 3681));
        return true;
    }
    return false;
  }
}
