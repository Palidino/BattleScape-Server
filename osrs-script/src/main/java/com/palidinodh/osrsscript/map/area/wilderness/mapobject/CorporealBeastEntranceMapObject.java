package com.palidinodh.osrsscript.map.area.wilderness.mapobject;

import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.io.incomingpacket.MapObjectHandler;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;

@ReferenceId(ObjectId.CAVE)
class CorporealBeastEntranceMapObject implements MapObjectHandler {
  @Override
  public void execute(Player player, int option, MapObject mapObject) {
    if (player.getInventory().hasItem(ItemId.BLOODY_KEY)
        || player.getInventory().hasItem(ItemId.BLOODIER_KEY)) {
      player.getGameEncoder().sendMessage("You can't use this right now.");
      return;
    }
    if (player.getMovement().getTeleportBlock() > 0) {
      player.getGameEncoder()
          .sendMessage("A teleport block has been cast on you. It should wear off in "
              + player.getMovement().getTeleportBlockRemaining() + ".");
      return;
    }
    player.getMovement().ladderUpTeleport(new Tile(2964, 4382, 2));
  }
}
