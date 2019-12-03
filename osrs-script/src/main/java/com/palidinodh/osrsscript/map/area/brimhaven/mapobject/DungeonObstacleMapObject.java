package com.palidinodh.osrsscript.map.area.brimhaven.mapobject;

import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.io.incomingpacket.MapObjectHandler;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.Skills;
import com.palidinodh.rs.ReferenceId;

@ReferenceId(ObjectId.STEPPING_STONE_19040)
class DungeonObstacleMapObject implements MapObjectHandler {
  @Override
  public ReachType canReach(Player player, MapObject mapObject) {
    switch (mapObject.getId()) {
      case ObjectId.STEPPING_STONE_19040:
        if (mapObject.getX() == 2684 && mapObject.getY() == 9548) {
          return player.getX() == 2682 && player.getY() == 9548 ? ReachType.TRUE : ReachType.FALSE;
        } else if (mapObject.getX() == 2688 && mapObject.getY() == 9547) {
          return player.getX() == 2690 && player.getY() == 9547 ? ReachType.TRUE : ReachType.FALSE;
        } else if (mapObject.getX() == 2695 && mapObject.getY() == 9531) {
          return player.getX() == 2695 && player.getY() == 9533 ? ReachType.TRUE : ReachType.FALSE;
        } else if (mapObject.getX() == 2696 && mapObject.getY() == 9527) {
          return player.getX() == 2697 && player.getY() == 9525 ? ReachType.TRUE : ReachType.FALSE;
        }
        return ReachType.FALSE;
    }
    return ReachType.DEFAULT;
  }

  @Override
  public void execute(Player player, int option, MapObject mapObject) {
    switch (mapObject.getId()) {
      case ObjectId.STEPPING_STONE_19040:
        if (player.getController().getLevelForXP(Skills.AGILITY) < 83) {
          player.getGameEncoder().sendMessage("You need an Agility level of 83 to do this.");
          break;
        }
        if (mapObject.getX() == 2684 && mapObject.getY() == 9548) {
          player.getMovement().teleport(new Tile(2690, 9547));
        } else if (mapObject.getX() == 2688 && mapObject.getY() == 9547) {
          player.getMovement().teleport(new Tile(2682, 9548));
        } else if (mapObject.getX() == 2695 && mapObject.getY() == 9531) {
          player.getMovement().teleport(new Tile(2697, 9525));
        } else if (mapObject.getX() == 2696 && mapObject.getY() == 9527) {
          player.getMovement().teleport(new Tile(2695, 9533));
        }
        break;
    }
  }
}
