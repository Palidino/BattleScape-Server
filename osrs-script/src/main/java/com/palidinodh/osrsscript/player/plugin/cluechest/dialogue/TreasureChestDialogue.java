package com.palidinodh.osrsscript.player.plugin.cluechest.dialogue;

import com.palidinodh.osrscore.model.dialogue.SelectionDialogue;
import com.palidinodh.osrscore.model.item.clue.ClueChestType;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrsscript.player.plugin.cluechest.ClueChestPlugin;

public class TreasureChestDialogue extends SelectionDialogue {
  public TreasureChestDialogue(Player player, ClueChestPlugin plugin) {
    addOption("Easy", (childId, slot) -> {
      plugin.open(ClueChestType.EASY);
    });
    addOption("Medium", (childId, slot) -> {
      plugin.open(ClueChestType.MEDIUM);
    });
    addOption("Hard", (childId, slot) -> {
      plugin.open(ClueChestType.HARD);
    });
    addOption("Elite", (childId, slot) -> {
      plugin.open(ClueChestType.ELITE);
    });
    addOption("Master", (childId, slot) -> {
      plugin.open(ClueChestType.MASTER);
    });
    open(player);
  }
}
