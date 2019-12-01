package com.palidinodh.osrsscript.map.area.godwars.mapobject;

import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.io.incomingpacket.MapObjectHandler;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.Prayer;
import com.palidinodh.osrscore.model.player.Skills;
import com.palidinodh.osrsscript.map.area.godwars.GodWarsArea;
import com.palidinodh.rs.ReferenceId;
import com.palidinodh.util.PTime;
import lombok.var;

@ReferenceId({ ObjectId.ZAMORAK_ALTAR, ObjectId.SARADOMIN_ALTAR, ObjectId.ARMADYL_ALTAR,
    ObjectId.BANDOS_ALTAR })
class BossAltarMapObject implements MapObjectHandler {
  @Override
  public void execute(Player player, int option, MapObject mapObject) {
    var area = player.getArea().cast(GodWarsArea.class);
    if (area == null) {
      return;
    }
    if (option == 0) {
      if (area.getAltarDelay() > 0) {
        var message = "The gods blessed you recently. They will ignore your prayers for another ";
        var seconds = PTime.tickToSec(area.getAltarDelay());
        if (seconds > 60) {
          message += seconds / 60 + " minutes.";
        } else {
          message += seconds + " seconds.";
        }
        player.getGameEncoder().sendMessage(message);
        return;
      }
      player.getPrayer().adjustPoints(player.getController().getLevelForXP(Skills.PRAYER));
      player.setAnimation(Prayer.PRAY_ANIMATION);
      player.getGameEncoder().sendMessage("You recharge your Prayer.");
      area.setAltarDelay((int) PTime.minToTick(10));
    } else if (option == 1) {
      switch (mapObject.getId()) {
        case ObjectId.ZAMORAK_ALTAR:
          player.getMagic().standardTeleport(new Tile(2925, 5333, 2));
          player.getController().stopWithTeleport();
          break;
        case ObjectId.SARADOMIN_ALTAR:
          player.getMagic().standardTeleport(new Tile(2909, 5265));
          player.getController().stopWithTeleport();
          break;
        case ObjectId.ARMADYL_ALTAR:
          player.getMagic().standardTeleport(new Tile(2839, 5294, 2));
          player.getController().stopWithTeleport();
          break;
        case ObjectId.BANDOS_ALTAR:
          player.getMagic().standardTeleport(new Tile(2862, 5354, 2));
          player.getController().stopWithTeleport();
          break;
      }
    }
  }
}
