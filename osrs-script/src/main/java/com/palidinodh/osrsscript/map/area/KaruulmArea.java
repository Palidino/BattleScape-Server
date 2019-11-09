package com.palidinodh.osrsscript.map.area;

import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.model.Hit;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.map.Area;
import com.palidinodh.osrscore.model.map.MapObject;
import lombok.var;

public class KaruulmArea extends Area {
  public KaruulmArea() {
    super(5022, 5023, 5179, 5279, 5280, 5535, 5536);
  }

  @Override
  public void tickPlayer() {
    var player = getPlayer();
    if (player.getEquipment().getFootId() != ItemId.BOOTS_OF_STONE
        && player.getEquipment().getFootId() != ItemId.BOOTS_OF_BRIMSTONE
        && player.getEquipment().getFootId() != ItemId.GRANITE_BOOTS) {
      player.applyHit(new Hit(4));
    }
  }

  @Override
  public boolean mapObjectOptionHook(int index, MapObject mapObject) {
    var player = getPlayer();
    switch (mapObject.getId()) {
      case ObjectId.CAVE_EXIT_34514:
        player.getMovement().ladderUpTeleport(new Tile(1311, 3805));
        return true;
      case ObjectId.LAVA_GAP:
        if (mapObject.getX() == 1269 && mapObject.getY() == 10171) {
          if (player.getY() >= 10175) {
            player.getMovement().teleport(1270, 10170);
          } else {
            player.getMovement().teleport(1270, 10175);
          }
        } else if (mapObject.getX() == 1307 && mapObject.getY() == 10253) {
          if (player.getY() <= 10252) {
            player.getMovement().teleport(1308, 10257);
          } else {
            player.getMovement().teleport(1308, 10252);
          }
        }
        return true;
      case ObjectId.TUNNEL_34516:
        if (player.getX() <= 1329) {
          player.getMovement().teleport(1336, 10239, 1);
        } else {
          player.getMovement().teleport(1329, 10239, 1);
        }
        return true;
      case ObjectId.STEPS_34530:
        player.getMovement().ladderUpTeleport(new Tile(1334, 10205, 1));
        return true;
      case ObjectId.STEPS_34531:
        player.getMovement().ladderDownTeleport(new Tile(1329, 10205));
        return true;
      case ObjectId.ROCKS_34544:
        if (mapObject.getX() == 1302 && mapObject.getY() == 10205) {
          if (player.getX() >= 1303) {
            player.getMovement().teleport(1301, 10205);
          } else {
            player.getMovement().teleport(1303, 10205);
          }
        } else if (mapObject.getX() == 1321 && mapObject.getY() == 10205) {
          if (player.getX() <= 1320) {
            player.getMovement().teleport(1322, 10205);
          } else {
            player.getMovement().teleport(1320, 10205);
          }
        } else if (mapObject.getX() == 1311 && mapObject.getY() == 10215) {
          if (player.getY() <= 10214) {
            player.getMovement().teleport(1311, 10216);
          } else {
            player.getMovement().teleport(1311, 10214);
          }
        }
        return true;
      case ObjectId.ROCKS_34548:
        if (player.getY() <= 10250) {
          player.getMovement().teleport(1351, 10252);
        } else {
          player.getMovement().teleport(1351, 10250);
        }
        return true;
      case ObjectId.ALCHEMICAL_DOOR:
      case ObjectId.ALCHEMICAL_DOOR_34554:
      if (player.getX() <= 1355) {
        player.openDialogue("bossinstance", 14);
      } else {
        player.getController().stopWithTeleport();
        player.getMovement().teleport(1355, 10258);
      }
        return true;

    }
    return false;
  }
}
