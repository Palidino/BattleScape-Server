package com.palidinodh.osrsscript.map.area.edgeville.mapobject;

import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.io.incomingpacket.MapObjectHandler;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;

@ReferenceId(ObjectId.WISHING_WELL)
class WishingWellMapObject implements MapObjectHandler {
  @Override
  public void execute(Player player, int option, MapObject mapObject) {
    player.openDialogue("wishingwell", 0);
  }
}
