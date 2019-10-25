package com.palidinodh.osrsscript.packetdecoder.command;

import com.palidinodh.osrscore.Main;
import com.palidinodh.osrscore.io.Command;
import com.palidinodh.osrscore.io.cache.ItemId;
import com.palidinodh.osrscore.model.player.Player;

public class TbCommand implements Command {
  @Override
  public boolean canUse(Player player) {
    return Main.isSpawn() && player.inEdgeville() && !player.getController().inPvPWorld();
  }

  @Override
  public void execute(Player player, String message) {
    player.getInventory().addItem(ItemId.LAW_RUNE, 5000);
    player.getInventory().addItem(ItemId.CHAOS_RUNE, 5000);
    player.getInventory().addItem(ItemId.DEATH_RUNE, 5000);
  }


}
