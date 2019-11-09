package com.palidinodh.osrsscript.incomingpacket;

import com.palidinodh.io.Stream;
import com.palidinodh.osrscore.io.cache.CacheManager;
import com.palidinodh.osrscore.io.incomingpacket.InStreamKey;
import com.palidinodh.osrscore.io.incomingpacket.IncomingPacketDecoder;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.util.RequestManager;
import com.palidinodh.osrsscript.world.event.pvptournament.PvpTournament;
import com.palidinodh.rs.adaptive.Clan;
import com.palidinodh.rs.adaptive.RsClanRank;
import lombok.var;

public class ChatDecoder {
  public static class PublicChatDecoder extends IncomingPacketDecoder {
    @Override
    public boolean execute(Player player, Stream stream) {
      var effects = getInt(InStreamKey.EFFECTS);
      var secondaryEffect = getInt(InStreamKey.SECONDARY_EFFECT);
      var length = getInt(InStreamKey.LENGTH);
      var message = CacheManager.getHuffman().readEncryptedMessage(stream, length);
      RequestManager.addUserPacketLog(player, "[Messaging-Public Chat] message=" + message
          + "; effects=" + effects + "; length=" + length);
      player.getMessaging().setMessage(message, effects, secondaryEffect);
      if (secondaryEffect != 1) {
        player.clearIdleTime();
      }
      return true;
    }
  }

  public static class PublicPrivateTradeDecoder extends IncomingPacketDecoder {
    @Override
    public boolean execute(Player player, Stream stream) {
      var publicChat = getInt(InStreamKey.PUBLIC);
      var privateChat = getInt(InStreamKey.PRIVATE);
      var trade = getInt(InStreamKey.TRADE);
      RequestManager.addUserPacketLog(player, "[Messaging-Chat State] publicChat=" + publicChat
          + "; privateChat=" + privateChat + "; trade=" + trade);
      player.getMessaging().setPublicChatStatus(publicChat);
      player.getMessaging().setPrivateChatStatus(privateChat);
      return true;
    }
  }

  public static class AddFriendDecoder extends IncomingPacketDecoder {
    @Override
    public boolean execute(Player player, Stream stream) {
      var username = getString(InStreamKey.STRING_INPUT);
      RequestManager.addUserPacketLog(player, "[Messaging-Add RsFriend] username=" + username);
      player.getMessaging().addFriend(username);
      return true;
    }
  }

  public static class RemoveFriendDecoder extends IncomingPacketDecoder {
    @Override
    public boolean execute(Player player, Stream stream) {
      var username = getString(InStreamKey.STRING_INPUT);
      RequestManager.addUserPacketLog(player, "[Messaging-Remove RsFriend] username=" + username);
      player.getMessaging().removeFriend(username);
      return true;
    }
  }

  public static class MessageFriendDecoder extends IncomingPacketDecoder {
    @Override
    public boolean execute(Player player, Stream stream) {
      var username = stream.readString();
      var length = stream.readUnsignedByte();
      var message = CacheManager.getHuffman().readEncryptedMessage(stream, length);
      RequestManager.addUserPacketLog(player, "[Messaging-Private Chat] message=" + message
          + "; username=" + username + "; length=" + length);
      player.getMessaging().setPrivateMessage(username, message);
      player.clearIdleTime();
      return true;
    }
  }

  public static class AddIgnoreDecoder extends IncomingPacketDecoder {
    @Override
    public boolean execute(Player player, Stream stream) {
      var username = getString(InStreamKey.STRING_INPUT);
      RequestManager.addUserPacketLog(player, "[Messaging-Add Ignore] username=" + username);
      player.getMessaging().addIgnore(username);
      return true;
    }
  }

  public static class RemoveIgnoreDecoder extends IncomingPacketDecoder {
    @Override
    public boolean execute(Player player, Stream stream) {
      var username = getString(InStreamKey.STRING_INPUT);
      RequestManager.addUserPacketLog(player, "[Messaging-Remove Ignore] username=" + username);
      player.getMessaging().removeIgnore(username);
      return true;
    }
  }

  public static class JoinClanDecoder extends IncomingPacketDecoder {
    @Override
    public boolean execute(Player player, Stream stream) {
      var username = getString(InStreamKey.STRING_INPUT);
      RequestManager.addUserPacketLog(player, "[Messaging-Join Clan] username=" + username);
      if (username.length() == 0 || username.equals(Clan.LEAVE_CLAN)) {
        player.getMessaging().leaveClan();
      } else {
        player.getMessaging().joinClan(username);
      }
      return true;
    }
  }

  public static class ClanRankDecoder extends IncomingPacketDecoder {
    @Override
    public boolean execute(Player player, Stream stream) {
      var username = getString(InStreamKey.STRING_INPUT);
      var rankId = getInt(InStreamKey.RANK);
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
      return true;
    }
  }

  public static class KickClanUserDecoder extends IncomingPacketDecoder {
    @Override
    public boolean execute(Player player, Stream stream) {
      var username = getString(InStreamKey.STRING_INPUT);
      RequestManager.addUserPacketLog(player, "[Messaging-Kick Clan User] username=" + username);
      player.getMessaging().kickClanUser(username);
      return true;
    }
  }

  public static class UsernameListDecoder extends IncomingPacketDecoder {
    @Override
    public boolean execute(Player player, Stream stream) {
      player.getWorld().getWorldEvent(PvpTournament.class).teleportViewing(player,
          getString(InStreamKey.STRING_INPUT));
      return true;
    }
  }
}
