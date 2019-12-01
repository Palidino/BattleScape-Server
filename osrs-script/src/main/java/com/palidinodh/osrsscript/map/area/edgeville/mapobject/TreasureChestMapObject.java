package com.palidinodh.osrsscript.map.area.edgeville.mapobject;

import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.io.incomingpacket.MapObjectHandler;
import com.palidinodh.osrscore.model.dialogue.DialogueOption;
import com.palidinodh.osrscore.model.dialogue.OptionsDialogue;
import com.palidinodh.osrscore.model.item.clue.ClueChestType;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrsscript.player.plugin.cluechest.ClueChestPlugin;
import com.palidinodh.rs.ReferenceId;
import lombok.var;

@ReferenceId(ObjectId.TREASURE_CHEST_18808)
class TreasureChestMapObject implements MapObjectHandler {
  @Override
  public void execute(Player player, int option, MapObject mapObject) {
    var plugin = player.getPlugin(ClueChestPlugin.class);
    player.openDialogue(
        new OptionsDialogue(new DialogueOption("Easy", (c, s) -> plugin.open(ClueChestType.EASY)),
            new DialogueOption("Medium", (c, s) -> plugin.open(ClueChestType.MEDIUM)),
            new DialogueOption("Hard", (c, s) -> plugin.open(ClueChestType.HARD)),
            new DialogueOption("Elite", (c, s) -> plugin.open(ClueChestType.ELITE)),
            new DialogueOption("Master", (c, s) -> plugin.open(ClueChestType.MASTER))));
  }
}
