package com.palidinodh.osrsscript.packetdecoder.command;

import static com.palidinodh.osrscore.model.player.Player.RIGHTS_ADMIN;
import com.palidinodh.osrscore.io.Command;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.setting.SqlUserRank;
import lombok.var;

public class PrivateCommand implements Command {
  @Override
  public String getExample() {
    return "username";
  }

  @Override
  public boolean canUse(Player player) {
    return player.isUsergroup(SqlUserRank.TRIAL_MODERATOR)
        || player.getRights() == Player.RIGHTS_MOD || player.getRights() == Player.RIGHTS_ADMIN
        || player.isUsergroup(SqlUserRank.COMMUNITY_MANAGER);
  }

  @Override
  public void execute(Player player, String username) {
    var player2 = player.getWorld().getPlayerByUsername(username);
    if (player2 == null) {
      player.getGameEncoder().sendMessage("Unable to find user " + username + ".");
      return;
    } else if (player2.getController().isInstanced()) {
      player.getGameEncoder().sendMessage(username + " is in an instance located at: "
          + player.getX() + ", " + player.getY() + ", " + player.getHeight() + ".");
      return;
    } else if (!player2.getController().canTeleport(30, false)
        && (player.getRights() != RIGHTS_ADMIN)) {
      player.getGameEncoder().sendMessage(username + " can't currently be moved.");
      return;
    }
    player2.getGameEncoder().sendMessage(player.getUsername() + " has moved you.");
    player.getGameEncoder().sendMessage(username + " has been moved.");
    player2.getMovement().teleport(2895, 2727);
    player2.getController().stopWithTeleport();
  }
}
