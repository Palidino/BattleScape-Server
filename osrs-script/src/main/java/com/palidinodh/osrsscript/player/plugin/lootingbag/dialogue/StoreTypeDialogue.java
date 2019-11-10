package com.palidinodh.osrsscript.player.plugin.lootingbag.dialogue;

import com.palidinodh.osrscore.model.dialogue.OptionsDialogue;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrsscript.player.plugin.lootingbag.LootingBagPlugin;
import com.palidinodh.osrsscript.player.plugin.lootingbag.StoreType;

public class StoreTypeDialogue extends OptionsDialogue {
  public StoreTypeDialogue(Player player, LootingBagPlugin plugin) {
    addOption("Always Ask", (childId, slot) -> {
      plugin.setStoreType(StoreType.ASK);
    });
    addOption("Always Store-1", (childId, slot) -> {
      plugin.setStoreType(StoreType.STORE_1);
    });
    addOption("Always Store-5", (childId, slot) -> {
      plugin.setStoreType(StoreType.STORE_5);
    });
    addOption("Always Store-All", (childId, slot) -> {
      plugin.setStoreType(StoreType.STORE_ALL);
    });
    addOption("Always Store-X", (childId, slot) -> {
      plugin.setStoreType(StoreType.STORE_X);
    });
  }
}
