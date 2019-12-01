package com.palidinodh.osrsscript.map.area.slayertower.mapobject;

import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.io.incomingpacket.MapObjectHandler;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.controller.BossInstancePC;
import com.palidinodh.osrscore.model.player.slayer.SlayerUnlock;
import com.palidinodh.osrsscript.player.plugin.slayer.SlayerPlugin;
import com.palidinodh.rs.ReferenceId;
import com.palidinodh.rs.setting.Settings;
import lombok.var;

@ReferenceId(ObjectId.ROOF_ENTRANCE_31681)
class RoofEntranceMapObject implements MapObjectHandler {
  @Override
  public void execute(Player player, int option, MapObject mapObject) {
    var plugin = player.getPlugin(SlayerPlugin.class);
    if (!Settings.isBeta() && !Settings.getInstance().isSpawn()
        && !plugin.isUnlocked(SlayerUnlock.GROTESQUE_GUARDIANS)) {
      if (!player.getInventory().hasItem(ItemId.BRITTLE_KEY)) {
        player.getGameEncoder().sendMessage("You need a brittle key to unlock this.");
        return;
      }
      player.getInventory().deleteItem(ItemId.BRITTLE_KEY);
      plugin.unlock(SlayerUnlock.GROTESQUE_GUARDIANS);
      return;
    }
    if (!Settings.isBeta() && !Settings.getInstance().isLocal() && !Settings.getInstance().isSpawn()
        && !player.getSkills().isAnySlayerTask(NpcId.DUSK_248)) {
      player.getGameEncoder().sendMessage("You need an appropriate task to enter.");
      return;
    }
    player.setController(new BossInstancePC());
    player.getController().instance();
    player.getMovement().teleport(new Tile(1696, 4574));
    player.getController().getVariable("boss_instance_grotesque_guardians");
  }
}
