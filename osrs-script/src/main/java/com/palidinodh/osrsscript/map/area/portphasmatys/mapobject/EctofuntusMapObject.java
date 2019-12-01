package com.palidinodh.osrsscript.map.area.portphasmatys.mapobject;

import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.io.incomingpacket.MapObjectHandler;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;

@ReferenceId(ObjectId.ECTOFUNTUS)
class EctofuntusMapObject implements MapObjectHandler {
  @Override
  public void execute(Player player, int option, MapObject mapObject) {
    if (!player.getInventory().hasItem(ItemId.ECTOPHIAL_4252)) {
      player.getGameEncoder().sendMessage("Nothing interesting happens...");
      return;
    }
    player.getInventory().deleteItem(ItemId.ECTOPHIAL_4252);
    player.getInventory().addOrDropItem(ItemId.ECTOPHIAL);
    player.setAnimation(1651);
  }
}
