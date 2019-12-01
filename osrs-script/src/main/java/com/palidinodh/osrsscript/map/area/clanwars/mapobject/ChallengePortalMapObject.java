package com.palidinodh.osrsscript.map.area.clanwars.mapobject;

import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.io.incomingpacket.MapObjectHandler;
import com.palidinodh.osrscore.model.dialogue.DialogueOption;
import com.palidinodh.osrscore.model.dialogue.OptionsDialogue;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrsscript.player.plugin.clanwars.ClanWarsPlugin;
import com.palidinodh.osrsscript.player.plugin.clanwars.ClanWarsStages;
import com.palidinodh.rs.ReferenceId;
import lombok.var;

@ReferenceId({ ObjectId.CHALLENGE_PORTAL, ObjectId.CHALLENGE_PORTAL_26643,
    ObjectId.CHALLENGE_PORTAL_26644 })
class ChallengePortalMapObject implements MapObjectHandler {
  @Override
  public void execute(Player player, int option, MapObject mapObject) {
    var plugin = player.getPlugin(ClanWarsPlugin.class);
    player.openDialogue(new OptionsDialogue(
        new DialogueOption("Join your clan's battle.",
            (c, s) -> ClanWarsStages.openJoin(player, plugin)),
        new DialogueOption("Observe your clan's battle.",
            (c, s) -> ClanWarsStages.openView(player, plugin)),
        new DialogueOption("Cancel.")));
  }
}
