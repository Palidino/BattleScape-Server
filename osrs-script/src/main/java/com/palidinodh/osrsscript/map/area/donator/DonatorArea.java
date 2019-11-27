package com.palidinodh.osrsscript.map.area.donator;

import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.model.map.Area;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.rs.setting.SqlUserRank;
import lombok.var;

public class DonatorArea extends Area {
  public DonatorArea() {
    super(12806);
  }

  @Override
  public boolean mapObjectOptionHook(int option, MapObject mapObject) {
    var player = getPlayer();
    switch (mapObject.getId()) {
      case ObjectId.ENERGY_BARRIER_4470:
        if (!player.isUsergroup(SqlUserRank.DONATOR)) {
          player.getGameEncoder().sendMessage("Only donators may enter.");
          return true;
        }
        return true;
    }
    return false;
  }
}
