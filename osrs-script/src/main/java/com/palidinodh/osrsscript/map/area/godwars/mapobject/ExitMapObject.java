package com.palidinodh.osrsscript.map.area.godwars.mapobject;

import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.io.incomingpacket.MapObjectHandler;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;

@ReferenceId(ObjectId.ROPE_26370)
class ExitMapObject implements MapObjectHandler {
  @Override
  public void execute(Player player, int option, MapObject mapObject) {
    player.getMovement().ladderUpTeleport(new Tile(2916, 3746));
  }
}
