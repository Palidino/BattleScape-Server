package com.palidinodh.osrsscript.map.area.edgeville.mapobject;

import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.io.incomingpacket.MapObjectHandler;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;

@ReferenceId(ObjectId.BOX_OF_HEALTH)
class BoxOfHealthMapObject implements MapObjectHandler {
  @Override
  public void execute(Player player, int option, MapObject mapObject) {
    if (player.getController().inPvPWorldCombat()) {
      player.getGameEncoder().sendMessage("You can't use this here.");
      return;
    }
    player.setGraphic(436);
    player.getGameEncoder().sendMessage("The pool restores you.");
    player.rejuvenate();
  }
}
