package com.palidinodh.osrsscript.map.area.edgeville.mapobject;

import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.io.incomingpacket.MapObjectHandler;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;
import lombok.var;

@ReferenceId(ObjectId.LEVER_26761)
class WildernessLeverMapObject implements MapObjectHandler {
  @Override
  public void execute(Player player, int option, MapObject mapObject) {
    if (player.getMovement().getTeleportBlock() > 0) {
      player.getGameEncoder()
          .sendMessage("A teleport block has been cast on you. It should wear off in "
              + player.getMovement().getTeleportBlockRemaining() + ".");
      return;
    }
    var tile = new Tile(3153, 3923);
    if (player.getClientHeight() == tile.getHeight()) {
      tile.setHeight(player.getHeight());
    }
    if (!player.getController().canTeleport(tile, true)) {
      return;
    }
    player.getMagic().standardTeleport(tile);
    player.clearHits();
  }
}
