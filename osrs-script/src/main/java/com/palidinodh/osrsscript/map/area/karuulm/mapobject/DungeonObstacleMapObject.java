package com.palidinodh.osrsscript.map.area.karuulm.mapobject;

import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.io.incomingpacket.MapObjectHandler;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;

@ReferenceId({ ObjectId.LAVA_GAP, ObjectId.TUNNEL_34516, ObjectId.ROCKS_34544,
    ObjectId.ROCKS_34548 })
class DungeonObstacleMapObject implements MapObjectHandler {
  @Override
  public void execute(Player player, int option, MapObject mapObject) {
    switch (mapObject.getId()) {
      case ObjectId.LAVA_GAP:
        if (mapObject.getX() == 1269 && mapObject.getY() == 10171) {
          if (player.getY() >= 10175) {
            player.getMovement().teleport(new Tile(1270, 10170));
          } else {
            player.getMovement().teleport(new Tile(1270, 10175));
          }
        } else if (mapObject.getX() == 1307 && mapObject.getY() == 10253) {
          if (player.getY() <= 10252) {
            player.getMovement().teleport(new Tile(1308, 10257));
          } else {
            player.getMovement().teleport(new Tile(1308, 10252));
          }
        }
        break;
      case ObjectId.TUNNEL_34516:
        if (player.getX() <= 1329) {
          player.getMovement().teleport(new Tile(1336, 10239, 1));
        } else {
          player.getMovement().teleport(new Tile(1329, 10239, 1));
        }
        break;
      case ObjectId.ROCKS_34544:
        if (mapObject.getX() == 1302 && mapObject.getY() == 10205) {
          if (player.getX() >= 1303) {
            player.getMovement().teleport(new Tile(1301, 10205));
          } else {
            player.getMovement().teleport(new Tile(1303, 10205));
          }
        } else if (mapObject.getX() == 1321 && mapObject.getY() == 10205) {
          if (player.getX() <= 1320) {
            player.getMovement().teleport(new Tile(1322, 10205));
          } else {
            player.getMovement().teleport(new Tile(1320, 10205));
          }
        } else if (mapObject.getX() == 1311 && mapObject.getY() == 10215) {
          if (player.getY() <= 10214) {
            player.getMovement().teleport(new Tile(1311, 10216));
          } else {
            player.getMovement().teleport(new Tile(1311, 10214));
          }
        }
        break;
      case ObjectId.ROCKS_34548:
        if (player.getY() <= 10250) {
          player.getMovement().teleport(new Tile(1351, 10252));
        } else {
          player.getMovement().teleport(new Tile(1351, 10250));
        }
        break;
    }
  }
}
