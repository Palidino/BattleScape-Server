package com.palidinodh.osrsscript.map.area.wilderness.mapobject;

import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.io.incomingpacket.MapObjectHandler;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.dialogue.DialogueOption;
import com.palidinodh.osrscore.model.dialogue.LargeOptionsDialogue;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.map.TempMapObject;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrsscript.map.area.wilderness.WildernessObelisk;
import com.palidinodh.rs.ReferenceId;
import lombok.var;

@ReferenceId({ ObjectId.OBELISK_14826, ObjectId.OBELISK_14827, ObjectId.OBELISK_14828,
    ObjectId.OBELISK_14829, ObjectId.OBELISK_14830, ObjectId.OBELISK_14831 })
class ObeliskMapObject implements MapObjectHandler {
  @Override
  public void execute(Player player, int option, MapObject mapObject) {
    if (option == 0) {
      activateObelisk(player, mapObject, null);
    } else if (option == 1) {
      if (player.hasAttribute("wilderness_obelisk")) {
        activateObelisk(player, mapObject,
            (WildernessObelisk) player.getAttribute("wilderness_obelisk"));
      }
    } else if (option == 2) {
      var options = new DialogueOption[WildernessObelisk.values().length];
      for (var i = 0; i < WildernessObelisk.values().length; i++) {
        options[i] = new DialogueOption(WildernessObelisk.get(i).getFormattedName());
      }
      player.openDialogue(new LargeOptionsDialogue(options).action((c, s) -> {
        var obelisk = WildernessObelisk.get(s);
        if (obelisk == null) {
          return;
        }
        player.putAttribute("wilderness_obelisk", obelisk);
        player.getGameEncoder()
            .sendMessage("Destination set to " + obelisk.getFormattedName() + ".");
      }));
    }
  }

  private void activateObelisk(Player player, MapObject mapObject, WildernessObelisk toObelisk) {
    var obelisk = WildernessObelisk.getByObjectId(mapObject.getId());
    if (obelisk == null) {
      return;
    }
    var tiles = obelisk.getTiles();
    var activatedMapObjects = new MapObject[tiles.length];
    for (var i = 0; i < activatedMapObjects.length; i++) {
      activatedMapObjects[i] = new MapObject(ObjectId.OBELISK_14825, tiles[i], 10, 0);
    }
    if (toObelisk == null) {
      toObelisk = WildernessObelisk.getRandom(obelisk);
    }
    var center = new Tile(toObelisk.getSouthWest()).moveTile(2, 2);
    var activateObelisksEvent = new TempMapObject(8, player.getController(), activatedMapObjects) {
      @Override
      public void executeScript() {
        for (var player2 : player.getWorld().getPlayers(tiles[0].getRegionIds())) {
          if (player2.isLocked() || player2.getMovement().getTeleportBlock() > 0) {
            continue;
          }
          if (!obelisk.inside(player2)) {
            continue;
          }
          if (player2.getInventory().hasItem(ItemId.BLOODY_KEY)
              || player2.getInventory().hasItem(ItemId.BLOODIER_KEY)) {
            continue;
          }
          player2.getMovement().animatedTeleport(new Tile(center).randomize(1), 1816, null, 2);
          player2.getGameEncoder()
              .sendMessage("Ancient magic teleports you to a place within the wilderness!");
          player2.removeAttribute("wilderness_obelisk");
        }
      }
    };
    player.getWorld().addEvent(activateObelisksEvent);
  }
}
