package com.palidinodh.osrsscript.map.area;

import com.palidinodh.osrscore.io.cache.ItemId;
import com.palidinodh.osrscore.model.Hit;
import com.palidinodh.osrscore.model.map.Area;
import lombok.var;

public class KaruulmDungeonArea extends Area {
  public KaruulmDungeonArea() {
    super(5022, 5023, 5279, 5280, 5535, 5536);
  }

  @Override
  public void tickPlayer() {
    var player = getPlayer();
    if (player.getEquipment().getFootId() != ItemId.BOOTS_OF_STONE
        && player.getEquipment().getFootId() != ItemId.BOOTS_OF_BRIMSTONE
        && player.getEquipment().getFootId() != ItemId.GRANITE_BOOTS) {
      player.applyHit(new Hit(4));
    }
  }
}
