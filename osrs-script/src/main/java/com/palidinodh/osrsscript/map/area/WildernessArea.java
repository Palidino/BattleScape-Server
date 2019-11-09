package com.palidinodh.osrsscript.map.area;

import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.map.Area;
import com.palidinodh.osrscore.model.map.MapObject;
import lombok.var;

public class WildernessArea extends Area {
  public WildernessArea() {
    super(11831, 11832, 11833, 11834, 11835, 11836, 11837, 12087, 12088, 12089, 12090, 12091, 12092,
        12093, 12343, 12344, 12345, 12346, 12347, 12348, 12349, 12599, 12600, 12601, 12602, 12603,
        12604, 12605, 12855, 12856, 12857, 12858, 12859, 12860, 12861, 13111, 13112, 13113, 13114,
        13115, 13116, 13117, 13367, 13368, 13369, 13370, 13371, 13372, 13373, 12190, 12192, 12193,
        12443, 12444, 12961, 12701, 12702, 12703, 12957, 12958, 12959);
  }

  @Override
  public boolean mapObjectOptionHook(int index, MapObject mapObject) {
    var player = getPlayer();
    switch (mapObject.getId()) {
      case ObjectId.SHIPS_LADDER:
        if (mapObject.getX() == 3017 && mapObject.getY() == 3959) {
          player.getMovement().teleport(3017, 3961, player.getHeight() + 1);
        } else if (mapObject.getX() == 3019 && mapObject.getY() == 3959) {
          player.getMovement().teleport(3019, 3961, player.getHeight() + 1);
        }
        return true;
      case ObjectId.SHIPS_LADDER_246:
        if (mapObject.getX() == 3017 && mapObject.getY() == 3959) {
          player.getMovement().teleport(3017, 3958, player.getHeight() - 1);
        } else if (mapObject.getX() == 3019 && mapObject.getY() == 3959) {
          player.getMovement().teleport(3019, 3958, player.getHeight() - 1);
        }
        return true;
      case ObjectId.SHIPS_LADDER_272:
        player.getMovement().ladderUpTeleport(new Tile(3018, 3956, player.getHeight() + 1));
        return true;
      case ObjectId.SHIPS_LADDER_273:
        player.getMovement().ladderDownTeleport(new Tile(3018, 3958, player.getHeight() - 1));
        return true;
      case ObjectId.CAVE:
        if (player.getInventory().hasItem(ItemId.BLOODY_KEY)
            || player.getInventory().hasItem(ItemId.BLOODIER_KEY)) {
          player.getGameEncoder().sendMessage("You can't use this right now.");
          return true;
        }
        if (player.getMovement().getTeleportBlock() > 0) {
          player.getGameEncoder()
              .sendMessage("A teleport block has been cast on you. It should wear off in "
                  + player.getMovement().getTeleportBlockRemaining() + ".");
          return true;
        }
        player.getMovement().ladderUpTeleport(new Tile(2964, 4382, 2));
        return true;
    }
    return false;
  }

  @Override
  public boolean inWilderness() {
    return getTile().getRegionId() != 12442 || getTile().getY() > 9919;
  }
}
