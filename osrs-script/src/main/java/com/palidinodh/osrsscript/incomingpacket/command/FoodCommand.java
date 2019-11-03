package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.io.cache.ItemId;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.setting.Settings;

public class FoodCommand implements CommandHandler {
  @Override
  public boolean canUse(Player player) {
    return Settings.getInstance().isSpawn() && player.inEdgeville()
        && !player.getController().inPvPWorld();
  }

  @Override
  public void execute(Player player, String message) {
    player.getInventory().addItem(ItemId.SHARK_NOTED, 5000);
    player.getInventory().addItem(ItemId.SWORDFISH_NOTED, 5000);
    player.getInventory().addItem(ItemId.ANCHOVY_PIZZA_NOTED, 5000);
  }
}
