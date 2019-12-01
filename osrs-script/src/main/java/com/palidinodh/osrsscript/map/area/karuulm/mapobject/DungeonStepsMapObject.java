package com.palidinodh.osrsscript.map.area.karuulm.mapobject;

import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.io.incomingpacket.MapObjectHandler;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;

@ReferenceId({ ObjectId.STEPS_34530, ObjectId.STEPS_34531 })
class DungeonStepsMapObject implements MapObjectHandler {
  @Override
  public void execute(Player player, int option, MapObject mapObject) {
    switch (mapObject.getId()) {
      case ObjectId.STEPS_34530:
        player.getMovement().ladderUpTeleport(new Tile(1334, 10205, 1));
        break;
      case ObjectId.STEPS_34531:
        player.getMovement().ladderDownTeleport(new Tile(1329, 10205));
        break;
    }
  }
}
