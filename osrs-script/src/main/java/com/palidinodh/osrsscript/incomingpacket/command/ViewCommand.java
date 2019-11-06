package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.setting.SqlUserRank;
import lombok.var;

public class ViewCommand implements CommandHandler {
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
    } else if (player == player2) {
      player.getGameEncoder().sendMessage("You can't view yourself.");
      return;
    } else if (player.getController().isInstanced()) {
      player.getGameEncoder().sendMessage("You can't view while in an instance.");
      return;
    } else if (player2.getController().isInstanced()) {
      player.getGameEncoder().sendMessage(username + " is in an instance located at: "
          + player2.getX() + ", " + player2.getY() + ", " + player2.getHeight() + ".");
      return;
    }
    var viewTile = new Tile(player2);
    viewTile.randomize(1);
    player.getMovement().setViewing(viewTile);
    player.getWidgetManager().sendInventoryOverlay(WidgetId.UNMORPH);
  }
}
