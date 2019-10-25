package com.palidinodh.osrsscript.packetdecoder;

import com.palidinodh.osrscore.Main;
import com.palidinodh.osrscore.io.PacketDecoder;
import com.palidinodh.osrscore.model.map.route.Route;
import com.palidinodh.osrscore.model.player.Equipment;
import com.palidinodh.osrscore.model.player.Messaging;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.util.RequestManager;
import com.palidinodh.io.Stream;
import com.palidinodh.util.PLogger;
import lombok.var;

public class PlayerOptionDecoder extends PacketDecoder {
  public PlayerOptionDecoder() {
    super(47, 56, 62, 27, 83);
  }

  @Override
  public void execute(Player player, int index, int size, Stream stream) {
    var id = -1;
    var moveType = 0;
    if (index == 0) {
      id = stream.getUShort();
      moveType = stream.getUReversedByte();
    } else if (index == 1) {
      moveType = stream.getUByte128();
      id = stream.getUShort128();
    } else if (index == 2) {
      id = stream.getUShort128();
      moveType = stream.getUByte();
    } else if (index == 3) {
      id = stream.getUShortLE128();
      moveType = stream.getUReversedByte();
    } else if (index == 4) {
      moveType = stream.getUByte128();
      id = stream.getUShort128();
    }
    var player2 = player.getWorld().getPlayerByIndex(id);
    if (player2 == null) {
      return;
    }
    var message = "[PlayerOption(" + index + ")] index=" + id + "; moveType=" + moveType
        + "; username=" + player2.getUsername();
    if (Main.isLocal()) {
      PLogger.println(message);
    }
    if (player.getOptions().getPrintPackets()) {
      player.getGameEncoder().sendMessage(message);
    }
    RequestManager.addUserPacketLog(player, message);
    if (player.isLocked()) {
      return;
    }
    if (player.getMovement().isViewing()) {
      return;
    }
    player.clearIdleTime();
    player.clearAllActions(false, true);
    player.setFaceEntity(player2);
    player.getMovement().setFollowing(player2);
    if (index == 1) {
      if (player.getController().playerOptionHook(index, player2)) {
        return;
      }
      player.setAttacking(true);
      player.setInteractingEntity(player2);
      player.getCombat().setFollowing(player2);
      player.getMovement().follow();
      return;
    } else if (index == 2) {
      if (player.getController().playerOptionHook(index, player2)) {
        return;
      }
      player.setFaceEntity(player2);
      player.getMovement().setFollowBack(player2);
      player.getMovement().follow();
      return;
    }
    player.getMovement().follow();
    player.putAttribute("packet_decoder_index", index);
    player.putAttribute("packet_decoder_player_index", id);
    if (complete(player)) {
      stop(player);
      return;
    }
    player.putAttribute("packet_decoder", this);
  }

  @Override
  public boolean complete(Player player) {
    var index = player.getAttributeInt("packet_decoder_index");
    var id = player.getAttributeInt("packet_decoder_player_index");
    var player2 = player.getWorld().getPlayerByIndex(id);
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
    if (player.getController().playerOptionHook(index, player2)) {
      return true;
    }
    for (var plugin : player.getPlugins()) {
      if (plugin.playerOptionHook(index, player2)) {
        return true;
      }
    }
    doAction(player, index, player2);
    return true;
  }

  @Override
  public void stop(Player player) {
    player.removeAttribute("packet_decoder_index");
    player.removeAttribute("packet_decoder_player_index");
  }

  void doAction(Player player, int index, Player player2) {
    if (index == 0) {
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
    } else if (index == 3) {
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
    } else if (index == 4) {
      if (Equipment.isWhackId(player.getEquipment().getWeaponId())) {
        player.setAnimation(1833);
      }
    }
  }
}
