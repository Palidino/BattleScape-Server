package com.palidinodh.osrsscript.map.area.edgeville.mapobject;

import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.io.incomingpacket.MapObjectHandler;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;

@ReferenceId(ObjectId.SPIRITUAL_FAIRY_TREE)
class SpiritualFairyTreeMapObject implements MapObjectHandler {
  @Override
  public void execute(Player player, int option, MapObject mapObject) {
    if (option == 0) {
      player.openDialogue("spirittree", 0);
    } else if (option == 1) {
      player.openDialogue("fairyring", 0);
    }
  }
}
