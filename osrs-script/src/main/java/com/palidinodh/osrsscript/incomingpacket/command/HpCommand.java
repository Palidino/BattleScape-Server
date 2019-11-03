package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.osrscore.Main;
import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.setting.SqlUserRank;
import lombok.var;

public class HpCommand implements CommandHandler {
  @Override
  public String getExample() {
    return "amount";
  }

  @Override
  public boolean canUse(Player player) {
    return player.getRights() == Player.RIGHTS_ADMIN
        || player.isUsergroup(SqlUserRank.COMMUNITY_MANAGER) || Main.eventPriviledges(player);
  }

  @Override
  public void execute(Player player, String message) {
    var amount = Integer.parseInt(message);
    player.getMovement().setEnergy(amount);
    player.setHitpoints(amount);
    player.getSkills().setLevel(3, amount);
    player.getGameEncoder().sendSkillLevel(3);
    player.getGameEncoder().sendMessage("You set your hitpoints to " + amount + ".");
  }
}
