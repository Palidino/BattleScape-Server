package com.palidinodh.osrsscript.map.area.godwars.mapobject;

import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.io.incomingpacket.MapObjectHandler;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;

@ReferenceId({ ObjectId.BIG_DOOR_26502, ObjectId.BIG_DOOR_26503, ObjectId.BIG_DOOR_26504,
    ObjectId.BIG_DOOR_26505 })
class BossDoorMapObject implements MapObjectHandler {
  @Override
  public void execute(Player player, int option, MapObject mapObject) {
    switch (mapObject.getId()) {
      case ObjectId.BIG_DOOR_26502:
        if (player.getY() > 5294) {
          player.getGameEncoder().sendMessage("You must use the altar to leave.");
          break;
        }
        player.openDialogue("bossinstance", 7);
        break;
      case ObjectId.BIG_DOOR_26503:
        if (player.getX() > 2862) {
          player.getGameEncoder().sendMessage("You must use the altar to leave.");
          break;
        }
        player.openDialogue("bossinstance", 8);
        break;
      case ObjectId.BIG_DOOR_26504:
        if (player.getX() < 2909) {
          player.getGameEncoder().sendMessage("You must use the altar to leave.");
          break;
        }
        player.openDialogue("bossinstance", 10);
        break;
      case ObjectId.BIG_DOOR_26505:
        if (player.getY() < 5333) {
          player.getGameEncoder().sendMessage("You must use the altar to leave.");
          break;
        }
        player.openDialogue("bossinstance", 9);
        break;
    }
  }
}
