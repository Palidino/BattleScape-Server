package com.palidinodh.osrsscript.incomingpacket.misc;

import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.player.Player;

public class MapObject2 {
  // cave exit
  public static void mapObject34514(Player player, int index, MapObject mapObject) {
    player.getMovement().ladderUpTeleport(new Tile(1311, 3805));
  }

  // lava gap
  public static void mapObject34515(Player player, int index, MapObject mapObject) {
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
  }

  // tunnel
  public static void mapObject34516(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 1330 && mapObject.getY() == 10238) {
      if (player.getX() <= 1329) {
        player.getMovement().teleport(1336, 10239, 1);
      } else {
        player.getMovement().teleport(1329, 10239, 1);
      }
    }
  }

  // steps
  public static void mapObject34530(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 1330 && mapObject.getY() == 10205) {
      player.getMovement().ladderUpTeleport(new Tile(1334, 10205, 1));
    }
  }

  // steps
  public static void mapObject34531(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 1330 && mapObject.getY() == 10205) {
      player.getMovement().ladderDownTeleport(new Tile(1329, 10205));
    }
  }

  // rocks
  public static void mapObject34544(Player player, int index, MapObject mapObject) {
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
  }

  // rocks
  public static void mapObject34548(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 1351 && mapObject.getY() == 10251) {
      if (player.getY() <= 10250) {
        player.getMovement().teleport(1351, 10252);
      } else {
        player.getMovement().teleport(1351, 10250);
      }
    }
  }

  // alchemical door
  public static void mapObject34553(Player player, int index, MapObject mapObject) {
    if (player.getX() <= 1355) {
      player.openDialogue("bossinstance", 14);
    } else {
      player.getController().stopWithTeleport();
      player.getMovement().teleport(1355, 10258);
    }
  }

  // alchemical door
  public static void mapObject34554(Player player, int index, MapObject mapObject) {
    if (player.getX() <= 1355) {
      player.openDialogue("bossinstance", 14);
    } else {
      player.getController().stopWithTeleport();
      player.getMovement().teleport(1355, 10258);
    }
  }
}

