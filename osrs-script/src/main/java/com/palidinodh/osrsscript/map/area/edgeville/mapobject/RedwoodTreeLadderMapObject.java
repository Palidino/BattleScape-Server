package com.palidinodh.osrsscript.map.area.edgeville.mapobject;

import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.io.incomingpacket.MapObjectHandler;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;

@ReferenceId({ ObjectId.ROPE_LADDER_28857, ObjectId.ROPE_LADDER_28858 })
class RedwoodTreeLadderMapObject implements MapObjectHandler {
  @Override
  public void execute(Player player, int option, MapObject mapObject) {
    switch (mapObject.getId()) {
      case ObjectId.ROPE_LADDER_28857:
        player.getMovement().ladderUpTeleport(new Tile(3127, 3468, 3));
        break;
      case ObjectId.ROPE_LADDER_28858:
        player.getMovement().ladderDownTeleport(new Tile(3127, 3469));
        break;
    }
  }
}
