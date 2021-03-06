package com.palidinodh.osrsscript.map.area.clanwars.mapobject;

import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.io.incomingpacket.MapObjectHandler;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrsscript.player.plugin.clanwars.ClanWarsPlugin;
import com.palidinodh.rs.ReferenceId;
import lombok.var;

@ReferenceId({ ObjectId.VIEWING_ORB_26741, ObjectId.VIEWING_ORB_26743, ObjectId.VIEWING_ORB_26745,
    ObjectId.VIEWING_ORB_26747, ObjectId.VIEWING_ORB_26749 })
class ViewOrbMapObject implements MapObjectHandler {
  @Override
  public void execute(Player player, int option, MapObject mapObject) {
    var plugin = player.getPlugin(ClanWarsPlugin.class);
    plugin.teleportViewing(2);
  }
}
