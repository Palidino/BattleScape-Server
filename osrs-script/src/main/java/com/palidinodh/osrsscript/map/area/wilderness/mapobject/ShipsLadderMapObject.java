package com.palidinodh.osrsscript.map.area.wilderness.mapobject;

import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.io.incomingpacket.MapObjectHandler;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;

@ReferenceId({ ObjectId.SHIPS_LADDER, ObjectId.SHIPS_LADDER_246, ObjectId.SHIPS_LADDER_272,
    ObjectId.SHIPS_LADDER_273 })
class ShipsLadderMapObject implements MapObjectHandler {
  @Override
  public void execute(Player player, int option, MapObject mapObject) {
    switch (mapObject.getId()) {
      case ObjectId.SHIPS_LADDER:
        if (mapObject.getX() == 3017 && mapObject.getY() == 3959) {
          player.getMovement().teleport(new Tile(3017, 3961, player.getHeight() + 1));
        } else if (mapObject.getX() == 3019 && mapObject.getY() == 3959) {
          player.getMovement().teleport(new Tile(3019, 3961, player.getHeight() + 1));
        }
        break;
      case ObjectId.SHIPS_LADDER_246:
        if (mapObject.getX() == 3017 && mapObject.getY() == 3959) {
          player.getMovement().teleport(new Tile(3017, 3958, player.getHeight() - 1));
        } else if (mapObject.getX() == 3019 && mapObject.getY() == 3959) {
          player.getMovement().teleport(new Tile(3019, 3958, player.getHeight() - 1));
        }
        break;
      case ObjectId.SHIPS_LADDER_272:
        player.getMovement().ladderUpTeleport(new Tile(3018, 3956, player.getHeight() + 1));
        break;
      case ObjectId.SHIPS_LADDER_273:
        player.getMovement().ladderDownTeleport(new Tile(3018, 3958, player.getHeight() - 1));
        break;
    }
  }
}
