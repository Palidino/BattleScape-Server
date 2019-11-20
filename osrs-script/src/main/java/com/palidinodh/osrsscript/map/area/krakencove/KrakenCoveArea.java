package com.palidinodh.osrsscript.map.area.krakencove;

import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.map.Area;
import com.palidinodh.osrscore.model.map.MapObject;
import lombok.var;

public class KrakenCoveArea extends Area {
  public KrakenCoveArea() {
    super(9116);
  }

  @Override
  public boolean mapObjectOptionHook(int index, MapObject mapObject) {
    var player = getPlayer();
    switch (mapObject.getId()) {
      case ObjectId.CREVICE_537:
        player.openDialogue("bossinstance", 12);
        return true;
      case ObjectId.CREVICE_538:
        player.getMovement().ladderUpTeleport(new Tile(2280, 10016));
        player.getController().stopWithTeleport();
        return true;
    }
    return false;
  }
}
