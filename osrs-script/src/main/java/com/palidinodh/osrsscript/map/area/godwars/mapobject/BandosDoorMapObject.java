package com.palidinodh.osrsscript.map.area.godwars.mapobject;

import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.io.incomingpacket.MapObjectHandler;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;

@ReferenceId(ObjectId.BIG_DOOR)
class BandosDoorMapObject implements MapObjectHandler {
  @Override
  public void execute(Player player, int option, MapObject mapObject) {
    if (player.getX() >= 2851) {
      player.getMovement().teleport(new Tile(2850, 5333, 2));
    } else {
      player.getMovement().teleport(new Tile(2851, 5333, 2));
    }
  }
}
