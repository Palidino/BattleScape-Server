package com.palidinodh.osrsscript.map.area.karuulm.mapobject;

import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.io.incomingpacket.MapObjectHandler;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;

@ReferenceId({ ObjectId.ALCHEMICAL_DOOR, ObjectId.ALCHEMICAL_DOOR_34554 })
class AlchemicalDoorMapObject implements MapObjectHandler {
  @Override
  public void execute(Player player, int option, MapObject mapObject) {
    if (player.getX() <= 1355) {
      player.openDialogue("bossinstance", 14);
    } else {
      player.getController().stopWithTeleport();
      player.getMovement().teleport(new Tile(1355, 10258));
    }
  }
}
