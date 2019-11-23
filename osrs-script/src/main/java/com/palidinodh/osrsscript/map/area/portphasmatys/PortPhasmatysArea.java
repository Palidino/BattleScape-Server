package com.palidinodh.osrsscript.map.area.portphasmatys;

import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.model.map.Area;
import com.palidinodh.osrscore.model.map.MapObject;
import lombok.var;

public class PortPhasmatysArea extends Area {
  public PortPhasmatysArea() {
    super(14647, 14646);
  }

  @Override
  public boolean mapObjectOptionHook(int option, MapObject mapObject) {
    var player = getPlayer();
    switch (mapObject.getId()) {
      case ObjectId.ECTOFUNTUS:
        if (!player.getInventory().hasItem(ItemId.ECTOPHIAL_4252)) {
          player.getGameEncoder().sendMessage("Nothing interesting happens...");
          return true;
        }
        player.getInventory().deleteItem(ItemId.ECTOPHIAL_4252);
        player.getInventory().addOrDropItem(ItemId.ECTOPHIAL);
        player.setAnimation(1651);
        return true;
    }
    return false;
  }
}
