package com.palidinodh.osrsscript.map.area;

import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.map.Area;
import com.palidinodh.osrscore.model.map.MapObject;
import lombok.var;

public class SmokeDevilDungeonArea extends Area {
  public SmokeDevilDungeonArea() {
    super(9363, 9619);
  }

  @Override
  public boolean mapObjectOptionHook(int index, MapObject mapObject) {
    var player = getPlayer();
    switch (mapObject.getId()) {
      case ObjectId.CREVICE:
        player.getMovement().ladderUpTeleport(new Tile(2413, 3059));
        return true;
      case ObjectId.CREVICE_536:
        player.getMovement().ladderDownTeleport(new Tile(2379, 9452));
        return true;
    }
    return false;
  }
}
