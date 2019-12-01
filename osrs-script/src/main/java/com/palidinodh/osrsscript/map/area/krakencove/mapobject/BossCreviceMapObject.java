package com.palidinodh.osrsscript.map.area.krakencove.mapobject;

import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.io.incomingpacket.MapObjectHandler;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;

@ReferenceId({ ObjectId.CREVICE_537, ObjectId.CREVICE_538 })
class BossCreviceMapObject implements MapObjectHandler {
  @Override
  public void execute(Player player, int option, MapObject mapObject) {
    switch (mapObject.getId()) {
      case ObjectId.CREVICE_537:
        player.openDialogue("bossinstance", 12);
        break;
      case ObjectId.CREVICE_538:
        player.getMovement().ladderUpTeleport(new Tile(2280, 10016));
        player.getController().stopWithTeleport();
        break;
    }
  }
}
