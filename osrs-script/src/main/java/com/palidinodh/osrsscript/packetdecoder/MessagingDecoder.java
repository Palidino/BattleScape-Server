package com.palidinodh.osrsscript.packetdecoder;

import com.palidinodh.io.Stream;
import com.palidinodh.osrscore.io.PacketDecoder;
import com.palidinodh.osrscore.io.cache.CacheManager;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.util.RequestManager;
import com.palidinodh.osrsscript.world.event.pvptournament.PvpTournament;
import com.palidinodh.rs.adaptive.Clan;
import com.palidinodh.rs.adaptive.RsClanRank;
import lombok.var;

public class MessagingDecoder extends PacketDecoder {
  public MessagingDecoder() {
    super(97, 15, 88, 54, 25, 90, 28, 78, 77, 13, 95);
  }

  @Override
  public void execute(Player player, int index, int size, Stream stream) {
    if (index == 0) {
      var secondaryEffect = stream.getUByte();
      var effects = stream.getUByte() << 8 | stream.getUByte() & 255;
      var length = stream.getUByte();
      var message = CacheManager.getHuffman().readEncryptedMessage(stream, length);
      RequestManager.addUserPacketLog(player, "[Messaging-Public Chat] message=" + message
          + "; effects=" + effects + "; length=" + length);
      player.getMessaging().setMessage(message, effects, secondaryEffect);
      if (secondaryEffect != 1) {
        player.clearIdleTime();
      }
    } else if (index == 1) {
      var publicChat = stream.getUByte();
      var privateChat = stream.getUByte();
      var trade = stream.getUByte();
      RequestManager.addUserPacketLog(player, "[Messaging-Chat State] publicChat=" + publicChat
          + "; privateChat=" + privateChat + "; trade=" + trade);
      player.getMessaging().setPublicChatStatus(publicChat);
      player.getMessaging().setPrivateChatStatus(privateChat);
    } else if (index == 2) {
      var username = stream.getString();
      RequestManager.addUserPacketLog(player, "[Messaging-Add RsFriend] username=" + username);
      player.getMessaging().addFriend(username);
    } else if (index == 3) {
      var username = stream.getString();
      RequestManager.addUserPacketLog(player, "[Messaging-Remove RsFriend] username=" + username);
      player.getMessaging().removeFriend(username);
    } else if (index == 4) {
      var username = stream.getString();
      var length = stream.getUByte();
      var message = CacheManager.getHuffman().readEncryptedMessage(stream, length);
      RequestManager.addUserPacketLog(player, "[Messaging-Private Chat] message=" + message
          + "; username=" + username + "; length=" + length);
      player.getMessaging().setPrivateMessage(username, message);
      player.clearIdleTime();
    } else if (index == 5) {
      var username = stream.getString();
      RequestManager.addUserPacketLog(player, "[Messaging-Add Ignore] username=" + username);
      player.getMessaging().addIgnore(username);
    } else if (index == 6) {
      var username = stream.getString();
      RequestManager.addUserPacketLog(player, "[Messaging-Remove Ignore] username=" + username);
      player.getMessaging().removeIgnore(username);
    } else if (index == 7) {
      var username = stream.getString();
      RequestManager.addUserPacketLog(player, "[Messaging-Join Clan] username=" + username);
      if (username.length() == 0 || username.equals(Clan.LEAVE_CLAN)) {
        player.getMessaging().leaveClan();
      } else {
        player.getMessaging().joinClan(username);
      }
    } else if (index == 8) {
      var username = stream.getString();
      var rankId = stream.getUByte();
      RequestManager.addUserPacketLog(player,
          "[Messaging-Clan Rank] username=" + username + "; rankId=" + rankId);
      var rank = RsClanRank.NOT_IN_CLAN;
      switch (rankId) {
        case 0:
          rank = RsClanRank.NOT_IN_CLAN;
          break;
        case 1:
          rank = RsClanRank.RECRUIT;
          break;
        case 2:
          rank = RsClanRank.CORPORAL;
          break;
        case 3:
          rank = RsClanRank.SERGEANT;
          break;
        case 4:
          rank = RsClanRank.LIEUTENANT;
          break;
        case 5:
          rank = RsClanRank.CAPTAIN;
          break;
        case 6:
          rank = RsClanRank.GENERAL;
          break;
      }
      player.getMessaging().setFriendClanRank(username, rank);
    } else if (index == 9) {
      var username = stream.getString();
      RequestManager.addUserPacketLog(player, "[Messaging-Kick Clan User] username=" + username);
      player.getMessaging().kickClanUser(username);
    } else if (index == 10) {
      player.getWorld().getWorldEvent(PvpTournament.class).teleportViewing(player,
          stream.getString());
    }
  }
}
