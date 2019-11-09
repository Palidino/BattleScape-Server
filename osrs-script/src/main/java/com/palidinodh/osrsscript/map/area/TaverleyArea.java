package com.palidinodh.osrsscript.map.area;

import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.map.Area;
import com.palidinodh.osrscore.model.map.MapObject;
import lombok.var;

public class TaverleyArea extends Area {
  public TaverleyArea() {
    // 11929 is also the dwarvern mine dungeon.
    super(11573, 11416, 11417, 11671, 11672, 11673, 11928, 11929);
  }

  @Override
  public boolean mapObjectOptionHook(int index, MapObject mapObject) {
    var player = getPlayer();
    switch (mapObject.getId()) {
      case ObjectId.ROCKS:
        player.getMovement().ladderUpTeleport(new Tile(2888, 9823, 1));
        return true;
    }
    return false;
  }
}
