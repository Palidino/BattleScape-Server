package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.model.dialogue.DialogueOption;
import com.palidinodh.osrscore.model.dialogue.OptionsDialogue;
import com.palidinodh.osrscore.model.item.Item;
import com.palidinodh.osrscore.model.player.Player;
import lombok.var;

class EmptyCommand implements CommandHandler {

  @Override
  public String getExample() {
    return "- Empties your inventory.";
  }

  @Override
  public void execute(Player player, String message) {
    if (player.getController().inWilderness() || player.getController().inPvPWorld()) {
      player.getGameEncoder().sendMessage("You can not use this command in the wilderness.");
      return;
    }
    player.openDialogue(new OptionsDialogue("Are you sure you want to empty your inventory?",
        new DialogueOption("Yes, empty my inventory!", (c, s) -> {
          for (var i = 0; i < player.getInventory().size(); i++) {
            var id = player.getInventory().getId(i);
            player.getInventory().deleteItem(id, Item.MAX_AMOUNT);
          }
          player.getGameEncoder().sendMessage("You empty your inventory..");
        }), new DialogueOption("No!")));
  }
}
