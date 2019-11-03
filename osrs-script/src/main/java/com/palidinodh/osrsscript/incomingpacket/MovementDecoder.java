package com.palidinodh.osrsscript.incomingpacket;

import com.palidinodh.io.Stream;
import com.palidinodh.osrscore.io.incomingpacket.InStreamKey;
import com.palidinodh.osrscore.io.incomingpacket.IncomingPacketDecoder;
import com.palidinodh.osrscore.model.Movement;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.util.RequestManager;
import lombok.var;

public class MovementDecoder extends IncomingPacketDecoder {
  @Override
  public boolean execute(Player player, Stream stream) {
    var option = getInt(InStreamKey.PACKET_OPTION);
    var tileX = getInt(InStreamKey.TILE_X);
    var tileY = getInt(InStreamKey.TILE_Y);
    var ctrlRun = getInt(InStreamKey.CTRL_RUN);
    player.clearIdleTime();
    var message =
        "[Movement(" + option + ")] tileX=" + tileX + "; tileY=" + tileY + "; ctrlRun=" + ctrlRun;
    RequestManager.addUserPacketLog(player, message);
    if (player.getGameMode() == 0 || player.isDead()) {
      return false;
    }
    if (player.getMovement().isViewing()
        || player.getAppearance().getNpcId() == Movement.VIEWING_NPC_ID) {
      if (!player.getMovement().getTeleporting() && !player.getMovement().getTeleported()) {
        player.getMovement().stopViewing();
        player.getWidgetManager().removeInteractiveWidgets();
      }
      return false;
    }
    player.setInteractingEntity(null);
    return true;
  }

  @Override
  public boolean complete(Player player) {
    var option = getInt(InStreamKey.PACKET_OPTION);
    var tileX = getInt(InStreamKey.TILE_X);
    var tileY = getInt(InStreamKey.TILE_Y);
    var ctrlRun = getInt(InStreamKey.CTRL_RUN);
    if (player.isLocked()) {
      return false;
    }
    if (!player.getMovement().isRouting() && player.getX() == tileX && player.getY() == tileY) {
      player.getCombat().clear(false);
      return false;
    }
    if (player.getController().movementHook(option, tileX, tileY, ctrlRun)) {
      return true;
    }
    player.getMovement().fullRoute(tileX, tileY, ctrlRun);
    return true;
  }
}
