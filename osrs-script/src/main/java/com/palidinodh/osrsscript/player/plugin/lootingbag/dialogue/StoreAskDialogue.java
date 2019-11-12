package com.palidinodh.osrsscript.player.plugin.lootingbag.dialogue;

import com.palidinodh.osrscore.model.dialogue.OptionsDialogue;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrsscript.player.plugin.lootingbag.LootingBagPlugin;
import com.palidinodh.osrsscript.player.plugin.lootingbag.StoreType;

public class StoreAskDialogue extends OptionsDialogue {
  public StoreAskDialogue(Player player, LootingBagPlugin plugin) {
    addOption("Store-1", (childId, slot) -> {
      plugin.storeItemFromInventory(player.getAttributeInt("looting_bag_item_slot"),
          StoreType.STORE_1);
    });
    addOption("Store-5", (childId, slot) -> {
      plugin.storeItemFromInventory(player.getAttributeInt("looting_bag_item_slot"),
          StoreType.STORE_5);
    });
    addOption("Store-All", (childId, slot) -> {
      plugin.storeItemFromInventory(player.getAttributeInt("looting_bag_item_slot"),
          StoreType.STORE_ALL);
    });
    addOption("Store-X", (childId, slot) -> {
      plugin.storeItemFromInventory(player.getAttributeInt("looting_bag_item_slot"),
          StoreType.STORE_X);
    });
  }
}
