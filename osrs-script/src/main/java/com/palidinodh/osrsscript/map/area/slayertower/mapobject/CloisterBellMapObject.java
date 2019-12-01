package com.palidinodh.osrsscript.map.area.slayertower.mapobject;

import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.io.incomingpacket.MapObjectHandler;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;
import com.palidinodh.rs.setting.Settings;
import lombok.var;

@ReferenceId(ObjectId.THE_CLOISTER_BELL)
class CloisterBellMapObject implements MapObjectHandler {
  @Override
  public void execute(Player player, int option, MapObject mapObject) {
    if (!Settings.isBeta() && !Settings.getInstance().isLocal() && !Settings.getInstance().isSpawn()
        && !player.getSkills().isAnySlayerTask(NpcId.DUSK_248)) {
      player.getGameEncoder().sendMessage("You need an appropriate task to do this.");
      return;
    }
    if (player.getController().getNpc(NpcId.DUSK_248) != null
        || player.getController().getNpc(NpcId.DUSK_248_7882) != null
        || player.getController().getNpc(NpcId.DUSK_328_7888) != null
        || player.getController().getNpc(NpcId.DAWN_228) != null
        || player.getController().getNpc(NpcId.DAWN_228_7885) != null
        || player.getController().getNpc(NpcId.DUSK_328_7889) != null) {
      player.getGameEncoder().sendMessage("Nothing interesting happens.");
      return;
    }
    player.getGameEncoder().setVarp(1667, 3);
    player.getGameEncoder().sendMapObjectAnimation(mapObject, 7748);
    var dusk = new Npc(player.getController(), NpcId.DUSK_248, new Tile(1685, 4573));
    dusk.setLargeVisibility();
    var dawn = new Npc(player.getController(), NpcId.DAWN_228, new Tile(1705, 4573));
    dawn.setLargeVisibility();
  }
}
