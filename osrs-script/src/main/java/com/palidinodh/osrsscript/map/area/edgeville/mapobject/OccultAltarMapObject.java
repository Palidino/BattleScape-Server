package com.palidinodh.osrsscript.map.area.edgeville.mapobject;

import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.io.incomingpacket.MapObjectHandler;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.player.Magic;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;

@ReferenceId(ObjectId.ALTAR_OF_THE_OCCULT)
class OccultAltarMapObject implements MapObjectHandler {
  @Override
  public void execute(Player player, int option, MapObject mapObject) {
    if (player.getController().inPvPWorldCombat()) {
      player.getGameEncoder().sendMessage("You can't use this here.");
      return;
    }
    if (option == 0) {
      player.openDialogue("spellbooks", 0);
    } else if (option == 1) {
      if (player.getMagic().getSpellbook() == Magic.STANDARD_MAGIC) {
        player.getMagic().setSpellbook(Magic.ANCIENT_MAGIC);
        player.getGameEncoder().sendMessage("You switch your spellbook to Ancient magic.");
      } else if (player.getMagic().getSpellbook() == Magic.ANCIENT_MAGIC
          || player.getMagic().getSpellbook() == Magic.LUNAR_MAGIC) {
        player.getMagic().setSpellbook(Magic.STANDARD_MAGIC);
        player.getGameEncoder().sendMessage("You switch your spellbook to normal magic.");
      }
    } else if (option == 2) {
      if (player.getMagic().getSpellbook() == Magic.STANDARD_MAGIC
          || player.getMagic().getSpellbook() == Magic.ANCIENT_MAGIC) {
        player.getMagic().setSpellbook(Magic.LUNAR_MAGIC);
        player.getGameEncoder().sendMessage("You switch your spellbook to Lunar magic.");
      } else if (player.getMagic().getSpellbook() == Magic.LUNAR_MAGIC) {
        player.getMagic().setSpellbook(Magic.ANCIENT_MAGIC);
        player.getGameEncoder().sendMessage("You switch your spellbook to Ancient magic.");
      }
    }
  }
}
