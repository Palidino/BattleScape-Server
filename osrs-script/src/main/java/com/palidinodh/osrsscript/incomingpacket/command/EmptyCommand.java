package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.model.dialogue.old.DialogueEntry;
import com.palidinodh.osrscore.model.dialogue.old.DialogueOld;
import com.palidinodh.osrscore.model.dialogue.old.DialogueScript;
import com.palidinodh.osrscore.model.item.Item;
import com.palidinodh.osrscore.model.player.Player;
import lombok.var;

public class EmptyCommand implements CommandHandler {

  @Override
  public String getExample() {
    return "- Empties your inventory.";
  }

  @Override
  public void execute(Player player, String message) {
    if (!player.getController().inWilderness() && !player.getController().inPvPWorld()) {
      DialogueEntry entry = new DialogueEntry();
      entry.setSelection("Are you sure you want to empty your inventory?",
          "Yes, empty my inventory!", "No!");
      DialogueScript script = (p, index, childId, slot) -> {
        for (var i = 0; i < player.getInventory().size(); i++) {
          var id = player.getInventory().getId(i);
          if (slot == 0) {
            player.getInventory().deleteItem(id, Item.MAX_AMOUNT);
          } else {
            return;
          }
        }
        player.getGameEncoder().sendMessage("You empty your inventory..");
      };
      DialogueOld.open(player, entry, script);
    } else {
      player.getGameEncoder().sendMessage("You can not use this command in the wilderness.");
      return;
    }
  }
}
