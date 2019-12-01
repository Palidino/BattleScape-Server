package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.osrscore.Main;
import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.model.player.Player;
import lombok.var;

class ItemCommand implements CommandHandler {
  @Override
  public String getExample() {
    return "id_or_name (quantity)";
  }

  @Override
  public boolean canUse(Player player) {
    return Main.ownerPrivledges(player);
  }

  @Override
  public void execute(Player player, String message) {
    var values = message.split(" ");
    var id = -1;
    if (values.length == 0) {
      return;
    }
    if (values[0].matches("[0-9]+")) {
      id = Integer.parseInt(values[0]);
    } else {
      id = ItemId.valueOf(values[0].toUpperCase());
    }
    var amount = 1;
    if (id == -1) {
      player.getGameEncoder().sendMessage("Couldn't find item.");
      return;
    }
    if (values.length == 2) {
      amount = Integer.parseInt(values[1]);
    }
    player.getInventory().addItem(id, amount);
  }
}
