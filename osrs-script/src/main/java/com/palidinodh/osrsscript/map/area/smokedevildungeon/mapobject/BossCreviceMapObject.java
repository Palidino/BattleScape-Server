package com.palidinodh.osrsscript.map.area.smokedevildungeon.mapobject;

import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.io.incomingpacket.MapObjectHandler;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;
import com.palidinodh.rs.setting.Settings;

@ReferenceId({ ObjectId.CREVICE_535, ObjectId.CREVICE_536 })
class BossCreviceMapObject implements MapObjectHandler {
  @Override
  public void execute(Player player, int option, MapObject mapObject) {
    switch (mapObject.getId()) {
      case ObjectId.CREVICE_535:
        if (!Settings.isBeta() && !Settings.getInstance().isSpawn()
            && !player.getSkills().isAnySlayerTask(NpcId.THERMONUCLEAR_SMOKE_DEVIL_301)) {
          player.getGameEncoder().sendMessage("You need an appropriate task to enter.");
          break;
        }
        player.openDialogue("bossinstance", 5);
        break;
      case ObjectId.CREVICE_536:
        player.getMovement().ladderDownTeleport(new Tile(2379, 9452));
        player.getController().stopWithTeleport();
        break;
    }
  }
}
