package com.palidinodh.osrsscript.map.area.donator.mapobject;

import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.io.incomingpacket.MapObjectHandler;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;
import com.palidinodh.rs.setting.SqlUserRank;

@ReferenceId(ObjectId.ENERGY_BARRIER_4470)
class EntranceMapObject implements MapObjectHandler {
  @Override
  public void execute(Player player, int option, MapObject mapObject) {
    if (!player.isUsergroup(SqlUserRank.DONATOR)) {
      player.getGameEncoder().sendMessage("Only donators may enter.");
      return;
    }
  }
}
