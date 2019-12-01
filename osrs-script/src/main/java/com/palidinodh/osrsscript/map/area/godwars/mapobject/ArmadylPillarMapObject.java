package com.palidinodh.osrsscript.map.area.godwars.mapobject;

import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.io.incomingpacket.MapObjectHandler;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;

@ReferenceId(ObjectId.PILLAR_26380)
class ArmadylPillarMapObject implements MapObjectHandler {
  @Override
  public void execute(Player player, int option, MapObject mapObject) {
    if (player.getY() >= 5279) {
      player.getMovement().teleport(new Tile(2872, 5269, 2));
    } else {
      player.getMovement().teleport(new Tile(2872, 5279, 2));
    }
  }
}
