package com.palidinodh.osrsscript.map.area.godwars.mapobject;

import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.io.incomingpacket.MapObjectHandler;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;

@ReferenceId({ ObjectId.ROCK_26561, ObjectId.ROCK_26562 })
class SaradominRockMapObject implements MapObjectHandler {
  @Override
  public void execute(Player player, int option, MapObject mapObject) {
    switch (mapObject.getId()) {
      case ObjectId.ROCK_26561:
        player.getMovement().ladderDownTeleport(new Tile(2915, 5300, 1));
        break;
      case ObjectId.ROCK_26562:
        player.getMovement().ladderDownTeleport(new Tile(2919, 5274));
        break;
    }
  }
}
