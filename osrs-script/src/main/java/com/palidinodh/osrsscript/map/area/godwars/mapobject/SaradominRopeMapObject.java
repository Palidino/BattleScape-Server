package com.palidinodh.osrsscript.map.area.godwars.mapobject;

import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.io.incomingpacket.MapObjectHandler;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;

@ReferenceId({ ObjectId.ROPE_26371, ObjectId.ROPE_26375 })
class SaradominRopeMapObject implements MapObjectHandler {
  @Override
  public void execute(Player player, int option, MapObject mapObject) {
    switch (mapObject.getId()) {
      case ObjectId.ROPE_26371:
        player.getMovement().ladderUpTeleport(new Tile(2912, 5300, 2));
        break;
      case ObjectId.ROPE_26375:
        player.getMovement().ladderUpTeleport(new Tile(2920, 5276, 1));
        break;
    }
  }
}
