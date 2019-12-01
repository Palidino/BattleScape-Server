package com.palidinodh.osrsscript.map.area.edgeville.mapobject;

import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.io.incomingpacket.MapObjectHandler;
import com.palidinodh.osrscore.model.item.MysteryBox;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;
import lombok.var;

@ReferenceId(ObjectId.DEADMAN_CHEST)
class DeadmanChestMapObject implements MapObjectHandler {
  @Override
  public void execute(Player player, int option, MapObject mapObject) {
    if (player.getInventory().hasItem(ItemId.SINISTER_KEY)) {
      player.getCombat().getBarrows()
          .openChest(mapObject.getX() != 3551 || mapObject.getY() != 9695);
      return;
    }
    var mysteryId = -1;
    if (player.getInventory().hasItem(ItemId.BLOODIER_KEY_32305)) {
      mysteryId = ItemId.BLOODIER_KEY_32305;
    } else if (player.getInventory().hasItem(ItemId.BLOODY_KEY_32304)) {
      mysteryId = ItemId.BLOODY_KEY_32304;
    } else if (player.getInventory().hasItem(ItemId.DIAMOND_KEY_32309)) {
      mysteryId = ItemId.DIAMOND_KEY_32309;
    } else if (player.getInventory().hasItem(ItemId.GOLD_KEY_32308)) {
      mysteryId = ItemId.GOLD_KEY_32308;
    } else if (player.getInventory().hasItem(ItemId.SILVER_KEY_32307)) {
      mysteryId = ItemId.SILVER_KEY_32307;
    } else if (player.getInventory().hasItem(ItemId.BRONZE_KEY_32306)) {
      mysteryId = ItemId.BRONZE_KEY_32306;
    }
    if (mysteryId != -1) {
      MysteryBox.open(player, mysteryId);
    } else {
      player.getGameEncoder().sendMessage("You need a key to open this chest.");
    }
  }
}
