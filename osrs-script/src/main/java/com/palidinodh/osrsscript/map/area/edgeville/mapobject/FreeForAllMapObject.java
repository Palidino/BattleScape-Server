package com.palidinodh.osrsscript.map.area.edgeville.mapobject;

import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.io.incomingpacket.MapObjectHandler;
import com.palidinodh.osrscore.model.dialogue.DialogueOption;
import com.palidinodh.osrscore.model.dialogue.OptionsDialogue;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.controller.ClanWarsFreeForAllPC;
import com.palidinodh.rs.ReferenceId;

@ReferenceId(ObjectId.FREE_FOR_ALL_PORTAL)
class FreeForAllMapObject implements MapObjectHandler {
  @Override
  public void execute(Player player, int option, MapObject mapObject) {
    player.openDialogue(
        new OptionsDialogue(new DialogueOption("Safe Free-For-All", (c, s) -> {
          player.getMovement().teleport(3327, 4752);
          player.setController(new ClanWarsFreeForAllPC());
        }), new DialogueOption("Risk Zone", (c, s) -> {
          player.getMovement().teleport(2655, 5471);
        })));
  }
}
