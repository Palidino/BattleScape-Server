package com.palidinodh.osrsscript.player.plugin.slayer.dialogue;

import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.model.dialogue.OptionsDialogue;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.slayer.SlayerMaster;
import com.palidinodh.osrsscript.player.plugin.slayer.SlayerPlugin;
import lombok.var;

public class MasterMenuDialogue extends OptionsDialogue {
  public MasterMenuDialogue(Player player) {
    var plugin = player.getPlugin(SlayerPlugin.class);
    addOption("Get task", (c, s) -> player.openDialogue(new ChooseMasterDialogue(player)));
    addOption("Current task", (c, s) -> plugin.sendTask());
    addOption("Get boss task", (c, s) -> plugin.getAssignment(SlayerMaster.BOSS_MASTER));
    addOption("Cancel boss task", (c, s) -> {
      if (player.getInventory().getCount(ItemId.COINS) < 500000) {
        player.getGameEncoder().sendMessage("You need 500K coins to do this.");
        return;
      }
      if (player.getInventory().getCount(ItemId.VOTE_TICKET) < 2) {
        player.getGameEncoder().sendMessage("You need 2 vote tickets to do this.");
        return;
      }
      if (plugin.getBossTask().isComplete()) {
        player.getGameEncoder().sendMessage("You don't have a boss task to cancel.");
        return;
      }
      plugin.getBossTask().cancel();
      player.getInventory().deleteItem(ItemId.COINS, 500000);
      player.getInventory().deleteItem(ItemId.VOTE_TICKET, 2);
      player.getGameEncoder().sendMessage("Your boss task has been cancelled.");
    });
  }
}
