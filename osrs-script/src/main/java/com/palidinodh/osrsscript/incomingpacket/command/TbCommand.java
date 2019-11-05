package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.io.cache.ItemId;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.setting.Settings;

public class TbCommand implements CommandHandler {
  @Override
  public boolean canUse(Player player) {
    return Settings.getInstance().isSpawn() && player.inEdgeville()
        && !player.getController().inPvPWorld();
  }

  @Override
  public void execute(Player player, String message) {
    player.getInventory().addItem(ItemId.LAW_RUNE, 5000);
    player.getInventory().addItem(ItemId.CHAOS_RUNE, 5000);
    player.getInventory().addItem(ItemId.DEATH_RUNE, 5000);
  }


}