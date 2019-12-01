package com.palidinodh.osrsscript.map.area.clanwars.mapobject;

import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.io.incomingpacket.MapObjectHandler;
import com.palidinodh.osrscore.model.dialogue.DialogueOption;
import com.palidinodh.osrscore.model.dialogue.OptionsDialogue;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrsscript.player.plugin.clanwars.ClanWarsPlugin;
import com.palidinodh.osrsscript.player.plugin.clanwars.ClanWarsPlayerState;
import com.palidinodh.rs.ReferenceId;
import lombok.var;

@ReferenceId({ ObjectId.EXIT_PORTAL, ObjectId.EXIT_PORTAL_7820, ObjectId.EXIT_PORTAL_26727,
    ObjectId.EXIT_PORTAL_26728, ObjectId.EXIT_PORTAL_26731, ObjectId.EXIT_PORTAL_26732,
    ObjectId.EXIT_PORTAL_26733, ObjectId.EXIT_PORTAL_26734, ObjectId.EXIT_PORTAL_26735,
    ObjectId.EXIT_PORTAL_26736, ObjectId.EXIT_PORTAL_26737, ObjectId.PORTAL_26738,
    ObjectId.PORTAL_26739, ObjectId.PORTAL_26740 })
class ExitPortalMapObject implements MapObjectHandler {
  @Override
  public void execute(Player player, int option, MapObject mapObject) {
    var plugin = player.getPlugin(ClanWarsPlugin.class);
    player.openDialogue(new OptionsDialogue(new DialogueOption("Leave the Clan War.", (c, s) -> {
      if (plugin.getState() == ClanWarsPlayerState.TOURNAMENT && player.getInCombatDelay() > 0) {
        player.getGameEncoder().sendMessage("You can't do this while in combat.");
        return;
      }
      if (!player.getController().getVariableBool("clan_wars")) {
        return;
      }
      player.getController().stop();
    }), new DialogueOption("Nevermind.")));
  }
}
