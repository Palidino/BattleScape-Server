package com.palidinodh.osrsscript.map.area.karuulm;

import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.model.Hit;
import com.palidinodh.osrscore.model.map.Area;
import com.palidinodh.rs.ReferenceId;
import lombok.var;

@ReferenceId({ 5022, 5023, 5179, 5279, 5280, 5535, 5536 })
public class KaruulmArea extends Area {
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
