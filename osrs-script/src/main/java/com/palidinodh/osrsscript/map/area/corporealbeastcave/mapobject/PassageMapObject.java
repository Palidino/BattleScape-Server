package com.palidinodh.osrsscript.map.area.corporealbeastcave.mapobject;

import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.io.incomingpacket.MapObjectHandler;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;

@ReferenceId(ObjectId.PASSAGE)
class PassageMapObject implements MapObjectHandler {
  @Override
  public void execute(Player player, int option, MapObject mapObject) {
    player.getCombat().setDamageInflicted(0);
    if (player.getX() <= 2970) {
      player.getMovement().teleport(new Tile(2974, 4384, 2));
    } else {
      player.getMovement().teleport(new Tile(2970, 4384, 2));
    }
    player.getController().stopWithTeleport();
  }
}
