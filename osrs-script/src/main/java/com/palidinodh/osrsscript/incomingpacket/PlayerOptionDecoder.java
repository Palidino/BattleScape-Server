package com.palidinodh.osrsscript.incomingpacket;

import com.palidinodh.io.Stream;
import com.palidinodh.osrscore.io.incomingpacket.InStreamKey;
import com.palidinodh.osrscore.io.incomingpacket.IncomingPacketDecoder;
import com.palidinodh.osrscore.model.map.route.Route;
import com.palidinodh.osrscore.model.player.Equipment;
import com.palidinodh.osrscore.model.player.Messaging;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.util.RequestManager;
import com.palidinodh.rs.setting.Settings;
import com.palidinodh.util.PLogger;
import lombok.var;

class PlayerOptionDecoder extends IncomingPacketDecoder {
  @Override
  public boolean execute(Player player, Stream stream) {
    var option = getInt(InStreamKey.PACKET_OPTION);
    var playerIndex = getInt(InStreamKey.TARGET_INDEX);
    var ctrlRun = getInt(InStreamKey.CTRL_RUN);
    player.clearIdleTime();
    var player2 = player.getWorld().getPlayerByIndex(playerIndex);
    if (player2 == null) {
      return false;
    }
    var message = "[PlayerOption(" + option + ")] playerIndex=" + playerIndex + "; ctrlRun="
        + ctrlRun + "; username=" + player2.getUsername();
    if (Settings.getInstance().isLocal()) {
      PLogger.println(message);
    }
    if (player.getOptions().getPrintPackets()) {
      player.getGameEncoder().sendMessage(message);
    }
    RequestManager.addUserPacketLog(player, message);
    if (player.isLocked()) {
      return false;
    }
    if (player.getMovement().isViewing()) {
      return false;
    }
    player.clearAllActions(false, true);
    player.setFaceEntity(player2);
    player.getMovement().setFollowing(player2);
    if (option == 1) {
      if (player.getController().playerOptionHook(option, player2)) {
        return false;
      }
      player.setAttacking(true);
      player.setInteractingEntity(player2);
      player.getCombat().setFollowing(player2);
      player.getMovement().follow();
      return false;
    } else if (option == 2) {
      if (player.getController().playerOptionHook(option, player2)) {
        return false;
      }
      player.setFaceEntity(player2);
      player.getMovement().setFollowBack(player2);
      player.getMovement().follow();
      return false;
    }
    player.getMovement().follow();
    return true;
  }

  @Override
  public boolean complete(Player player) {
    var option = getInt(InStreamKey.PACKET_OPTION);
    var playerIndex = getInt(InStreamKey.TARGET_INDEX);
    var player2 = player.getWorld().getPlayerByIndex(playerIndex);
    if (player2 == null) {
      return true;
    }
    var range = 1;
    if (player.getMovement().isRouting() && player2.getMovement().isRouting()) {
      range++;
    }
    if (player.isLocked()) {
      return false;
    }
    if (!player.withinDistance(player2, range)) {
      return false;
    }
    if (!Route.canReachEntity(player, player2, false)) {
      return false;
    }
    player.setFaceEntity(null);
    player.getMovement().setFollowing(null);
    if (player.getX() == player2.getX() || player.getY() == player2.getY()) {
      player.getMovement().clear();
    }
    player.setFaceTile(player2);
    if (player.getController().playerOptionHook(option, player2)) {
      return true;
    }
    for (var plugin : player.getPlugins()) {
      if (plugin.playerOptionHook(option, player2)) {
        return true;
      }
    }
    doAction(player, option, player2);
    return true;
  }

  private void doAction(Player player, int option, Player player2) {
    if (option == 0) {
      if (player.inDuelArena() && player2.inDuelArena()) {
        if (!player.getDuel().isStateNone() || !player2.getDuel().isStateNone()) {
          return;
        }
        player.getDuel().setOpponent(player2);
        if (player == player2.getDuel().getOpponent()) {
          player.getDuel().sendOptions();
          player2.getDuel().sendOptions();
        } else {
          player.getGameEncoder().sendMessage("Challenging " + player2.getUsername() + "...");
          player2.getGameEncoder().sendMessage(player.getUsername() + " wishes to duel with you.",
              Messaging.CHAT_TYPE_DUEL, player.getUsername());
        }
      }
    } else if (option == 3) {
      if (!player2.getOptions().getAcceptAid()) {
        player.getGameEncoder().sendMessage("This player is not accepting aid.");
        return;
      } else if (player2.getWidgetManager().hasInteractiveWidgets()) {
        player.getGameEncoder().sendMessage("This player is currently busy.");
        return;
      } else if (!player.getController().canTradeHook(player2)
          || !player2.getController().canTradeHook(player)) {
        return;
      }
      player.getTrade().setTrading(player2);
      if (player == player2.getTrade().getTrading()) {
        player.getTrade().open1();
        player2.getTrade().open1();
      } else {
        player.getGameEncoder().sendMessage("Sending trade offer...");
        player2.getGameEncoder().sendMessage(player.getUsername() + " wishes to trade with you.",
            Messaging.CHAT_TYPE_TRADE, player.getUsername());
      }
    } else if (option == 4) {
      if (Equipment.isWhackId(player.getEquipment().getWeaponId())) {
        player.setAnimation(1833);
      }
    }
  }
}
