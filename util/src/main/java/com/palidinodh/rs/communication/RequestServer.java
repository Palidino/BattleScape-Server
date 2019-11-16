package com.palidinodh.rs.communication;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import com.palidinodh.io.Readers;
import com.palidinodh.io.Stream;
import com.palidinodh.io.Writers;
import com.palidinodh.rs.adaptive.Clan;
import com.palidinodh.rs.adaptive.GrandExchangeItem;
import com.palidinodh.rs.adaptive.GrandExchangeUser;
import com.palidinodh.rs.adaptive.RsClanActiveUser;
import com.palidinodh.rs.adaptive.RsClanRank;
import com.palidinodh.rs.adaptive.RsFriend;
import com.palidinodh.rs.adaptive.RsPlayer;
import com.palidinodh.rs.communication.request.ClanMessageRequest;
import com.palidinodh.rs.communication.request.ClanRankRequest;
import com.palidinodh.rs.communication.request.ClanSettingRequest;
import com.palidinodh.rs.communication.request.FriendStatusRequest;
import com.palidinodh.rs.communication.request.FriendStatusSingleRequest;
import com.palidinodh.rs.communication.request.GEAbortOfferRequest;
import com.palidinodh.rs.communication.request.GEBuyOfferRequest;
import com.palidinodh.rs.communication.request.GECollectOfferRequest;
import com.palidinodh.rs.communication.request.GEHistoryRequest;
import com.palidinodh.rs.communication.request.GEListRequest;
import com.palidinodh.rs.communication.request.GEPriceAverageRequest;
import com.palidinodh.rs.communication.request.GERefreshRequest;
import com.palidinodh.rs.communication.request.GESellOfferRequest;
import com.palidinodh.rs.communication.request.GEShopOfferRequest;
import com.palidinodh.rs.communication.request.GEShopRequest;
import com.palidinodh.rs.communication.request.JoinClanRequest;
import com.palidinodh.rs.communication.request.KickClanUserRequest;
import com.palidinodh.rs.communication.request.LeaveClanRequest;
import com.palidinodh.rs.communication.request.LoadClanRequest;
import com.palidinodh.rs.communication.request.LoadFriendRequest;
import com.palidinodh.rs.communication.request.LoadFriendsRequest;
import com.palidinodh.rs.communication.request.LoadIgnoreRequest;
import com.palidinodh.rs.communication.request.LogRequest;
import com.palidinodh.rs.communication.request.PingRequest;
import com.palidinodh.rs.communication.request.PlayerLoginRequest;
import com.palidinodh.rs.communication.request.PlayerLogoutRequest;
import com.palidinodh.rs.communication.request.PrivateMessageRequest;
import com.palidinodh.rs.communication.request.Request;
import com.palidinodh.rs.communication.request.SQLUpdateRequest;
import com.palidinodh.rs.communication.request.WorldsShutdownRequest;
import com.palidinodh.rs.communication.response.ClanMessageResponse;
import com.palidinodh.rs.communication.response.ClanUpdateResponse;
import com.palidinodh.rs.communication.response.FriendStatusResponse;
import com.palidinodh.rs.communication.response.FriendStatusSingleResponse;
import com.palidinodh.rs.communication.response.GEHistoryResponse;
import com.palidinodh.rs.communication.response.GEListResponse;
import com.palidinodh.rs.communication.response.GEPriceAverageResponse;
import com.palidinodh.rs.communication.response.GERefreshResponse;
import com.palidinodh.rs.communication.response.GEShopOfferResponse;
import com.palidinodh.rs.communication.response.JoinClanResponse;
import com.palidinodh.rs.communication.response.KickClanUserResponse;
import com.palidinodh.rs.communication.response.LoadClanResponse;
import com.palidinodh.rs.communication.response.PlayerLoginResponse;
import com.palidinodh.rs.communication.response.PrivateMessageResponse;
import com.palidinodh.rs.communication.response.Response;
import com.palidinodh.rs.setting.SecureSettings;
import com.palidinodh.rs.setting.Settings;
import com.palidinodh.util.PLogger;
import com.palidinodh.util.PString;
import com.palidinodh.util.PTime;
import com.palidinodh.util.PUtil;

public class RequestServer implements Runnable {
  public static final int CONNECT_DELAY = 10000;
  public static final int SHUTDOWN_REQUEST_DELAY = 2;
  public static final int REQUESTS_PER_CYCLE = 16;
  public static final String SHUTDOWN_ERROR =
      "Unable to send accounts to Response Server! Saving locally.";

  private static RequestServer instance;

  private String ip = "0.0.0.0";
  private int port = 43596;
  private int worldId;
  private SecureSettings secureSettings;
  private boolean running = true;
  private List<RsPlayer> players = new ArrayList<>();
  private SocketChannel socket;
  private ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
  private Stream inputStream = new Stream(8192);
  private Stream outputStream = new Stream(8192);
  private long shutdown;
  private long lastPing = PTime.currentTimeMillis(), lastConnect = PTime.currentTimeMillis();
  private List<Request> newRequests = new ArrayList<>();
  private List<Request> requests = new ArrayList<>();
  private Map<Integer, Request> requestsByKey = new HashMap<>();
  private Vector<Response> requestNotifications = new Vector<>();
  private Vector<String> usernameRequests = new Vector<>();
  private int uniqueKey = 1;
  private int playerCount;
  private int worldShutdown = -1;
  private int lastReadOpcode1 = -1, lastReadSize1 = -1, lastReadOpcode2 = -1, lastReadSize2 = -1,
      lastReadOpcode3 = -1, lastReadSize3 = -1;

  public RequestServer(SecureSettings secureSettings, int worldId) {
    this.secureSettings = secureSettings;
    this.ip = secureSettings.getCommunicationIp();
    this.port = secureSettings.getCommunicationPort();
    this.worldId = worldId;
    connect(ip, port);
  }

  @Override
  public void run() {
    while (shutdown == 0
        || !requests.isEmpty() && PTime.milliToMin(shutdown) < SHUTDOWN_REQUEST_DELAY) {
      try {
        boolean connect = false;
        if (!requests.isEmpty()) {
          try {
            boolean onlyProcessLogouts = false;
            if (shutdown != 0) {
              for (Request r : requests) {
                if (r instanceof PlayerLogoutRequest) {
                  onlyProcessLogouts = true;
                  break;
                }
              }
            }
            int encoded = 0;
            for (Iterator<Request> it = requests.iterator(); it.hasNext();) {
              if (socket == null) {
                break;
              }
              Request request = it.next();
              if (onlyProcessLogouts && !(request instanceof PlayerLogoutRequest)) {
                continue;
              }
              Request.State state = encodeRequest(request, encoded >= REQUESTS_PER_CYCLE);
              if (state == Request.State.COMPLETE || state == Request.State.ERROR) {
                it.remove();
                continue;
              } else if (state == Request.State.ENCODED) {
                encoded++;
                continue;
              }
              if (request.getSendAttempts() > 4 && !request.getLogged()) {
                request.setLogged(true);
                printStats(request);
                throw new IOException("Reached 4 send attempts");
              }
            }
          } catch (IOException ie) {
            for (Request request : requests) {
              request.resetSendAttempts();
            }
            connect = true;
          }
        }
        if (socket != null) {
          try {
            checkPing();
            decode();
          } catch (IOException ie) {
            connect = true;
          }
        }
        if (connect || socket == null) {
          connect(ip, port);
        }
        synchronized (this) {
          try {
            wait(50);
          } catch (InterruptedException ie) {
          }
          requests.addAll(newRequests);
          try {
            Collections.sort(requests, (r1, r2) -> {
              if (r1 instanceof PlayerLogoutRequest && r2 instanceof PlayerLogoutRequest) {
                return 0;
              } else if (r1 instanceof PlayerLogoutRequest) {
                return -1;
              } else if (r2 instanceof PlayerLogoutRequest) {
                return 1;
              }
              return 0;
            });
          } catch (Exception sortError) {
            sortError.printStackTrace();
          }
          newRequests.clear();
        }
        PUtil.gc();
      } catch (Exception e) {
        PLogger.error(e);
      }
    }
    if (!requests.isEmpty()) {
      int remaining = requests.size();
      boolean hasLogouts = false;
      for (Request request : requests) {
        PLogger.println(request.getClass().getName() + ": " + request.getSendAttempts() + ", "
            + request.getState());
        if (request instanceof PlayerLogoutRequest) {
          internalPlayerLogout((PlayerLogoutRequest) request);
          hasLogouts = true;
        }
      }
      requests.clear();
      requestsByKey.clear();
      PLogger.error(SHUTDOWN_ERROR + "; hasLogouts=" + hasLogouts + ", size=" + remaining);
    }
    try {
      outputStream.clear();
      outputStream.writeOpcodeVarInt(Opcodes.WORLD_SHUTDOWN);
      outputStream.writeInt(-1);
      outputStream.endOpcodeVarInt();
      socket.write(ByteBuffer.wrap(outputStream.toByteArray()));
    } catch (Exception e) {
    }
    running = false;
    PLogger.println("RequestServer stopped");
  }

  private void connect(String ip, int port) {
    int connectDelay = CONNECT_DELAY;
    if (PTime.currentTimeMillis() - lastConnect < connectDelay) {
      return;
    }
    lastConnect = PTime.currentTimeMillis();
    disconnect();
    PLogger.println("Connecting communications...");
    outputStream.clear();
    outputStream.writeOpcodeVarInt(Opcodes.VERIFY);
    outputStream.writeShort(worldId);
    outputStream.writeString(secureSettings.getPassword());
    synchronized (this) {
      outputStream.writeShort(players.size());
      for (RsPlayer player : players) {
        outputStream.writeInt(player.getId());
        outputStream.writeString(player.getUsername());
        outputStream.writeString(player.getPassword());
        outputStream.writeString(player.getIp());
        outputStream.writeShort(player.getWorldId());
        outputStream.writeByte(player.getRights());
      }
    }
    outputStream.endOpcodeVarInt();
    try {
      socket = SocketChannel.open(new InetSocketAddress(ip, port));
      socket.configureBlocking(false);
      socket.write(ByteBuffer.wrap(outputStream.toByteArray()));
    } catch (IOException ie) {
      socket = null;
      PLogger.error(ie.getMessage());
    }
    inputStream.clear();
  }

  private void disconnect() {
    if (socket != null) {
      PLogger.println("[" + PTime.getFullDate() + "-RequestServer] Disconnecting");
      printStats(null);
      try {
        socket.close();
      } catch (Exception e) {
      }
    }
    socket = null;
  }

  private Request.State encodeRequest(Request request, boolean onlyComplete) throws IOException {
    try {
      outputStream.clear();
      if (request.getState() == Request.State.COMPLETE) {
        if (request.getKey() > -1) {
          requestsByKey.remove(request.getKey());
        }
        return Request.State.COMPLETE;
      } else if (onlyComplete) {
        return Request.State.PENDING;
      } else if (request.getState() == Request.State.PENDING_RECEIVE && !request.needResend()) {
        return Request.State.PENDING_RECEIVE;
      }
      if (request.getKey() > -1) {
        requestsByKey.put(request.getKey(), request);
      }
      if (request instanceof PingRequest) {
        encodePing((PingRequest) request);
      } else if (request instanceof PlayerLoginRequest) {
        encodePlayerLogin((PlayerLoginRequest) request);
      } else if (request instanceof PlayerLogoutRequest) {
        encodePlayerLogout((PlayerLogoutRequest) request);
      } else if (request instanceof LoadFriendsRequest) {
        encodeLoadFriends((LoadFriendsRequest) request);
      } else if (request instanceof LoadFriendRequest) {
        encodeLoadFriend((LoadFriendRequest) request);
      } else if (request instanceof LoadIgnoreRequest) {
        encodeLoadIgnore((LoadIgnoreRequest) request);
      } else if (request instanceof LoadClanRequest) {
        encodeLoadClan((LoadClanRequest) request);
      } else if (request instanceof JoinClanRequest) {
        encodeJoinClan((JoinClanRequest) request);
      } else if (request instanceof ClanRankRequest) {
        encodeClanRank((ClanRankRequest) request);
      } else if (request instanceof FriendStatusRequest) {
        encodeFriendStatus((FriendStatusRequest) request);
      } else if (request instanceof FriendStatusSingleRequest) {
        encodeFriendStatusSingle((FriendStatusSingleRequest) request);
      } else if (request instanceof PrivateMessageRequest) {
        encodePrivateMessage((PrivateMessageRequest) request);
      } else if (request instanceof ClanSettingRequest) {
        encodeClanSetting((ClanSettingRequest) request);
      } else if (request instanceof ClanMessageRequest) {
        encodeClanMessage((ClanMessageRequest) request);
      } else if (request instanceof KickClanUserRequest) {
        encodeKickClanUser((KickClanUserRequest) request);
      } else if (request instanceof LeaveClanRequest) {
        encodeLeaveClan((LeaveClanRequest) request);
      } else if (request instanceof SQLUpdateRequest) {
        encodeSqlUpdate((SQLUpdateRequest) request);
      } else if (request instanceof GERefreshRequest) {
        encodeGERefresh((GERefreshRequest) request);
      } else if (request instanceof GEBuyOfferRequest) {
        encodeGEBuyOffer((GEBuyOfferRequest) request);
      } else if (request instanceof GESellOfferRequest) {
        encodeGESellOffer((GESellOfferRequest) request);
      } else if (request instanceof GEAbortOfferRequest) {
        encodeGEAbortOffer((GEAbortOfferRequest) request);
      } else if (request instanceof GECollectOfferRequest) {
        encodeGECollectOffer((GECollectOfferRequest) request);
      } else if (request instanceof GEPriceAverageRequest) {
        encodeGEPriceAverage((GEPriceAverageRequest) request);
      } else if (request instanceof GEHistoryRequest) {
        encodeGEHistory((GEHistoryRequest) request);
      } else if (request instanceof LogRequest) {
        encodeLog((LogRequest) request);
      } else if (request instanceof GEShopRequest) {
        encodeGEShop((GEShopRequest) request);
      } else if (request instanceof GEShopOfferRequest) {
        encodeGEShopOffer((GEShopOfferRequest) request);
      } else if (request instanceof GEListRequest) {
        encodeGEList((GEListRequest) request);
      } else if (request instanceof WorldsShutdownRequest) {
        encodeWorldsShutdown((WorldsShutdownRequest) request);
      }
      request.updateLastSendTime();
      return Request.State.ENCODED;
    } catch (IOException io) {
      throw io;
    } catch (Exception e) {
      PLogger.error(e);
      return Request.State.ERROR;
    }
  }

  private void checkPing() throws IOException {
    if (PTime.currentTimeMillis() - lastPing >= Opcodes.PING_DELAY) {
      lastPing = PTime.currentTimeMillis();
      synchronized (this) {
        newRequests.add(new PingRequest(null, getUniqueKey()));
        notify();
      }
    }
  }

  private void decode() throws IOException {
    int read = 0;
    do {
      byteBuffer.clear();
      read = socket.read(byteBuffer);
      if (read > 0) {
        byteBuffer.flip();
        inputStream.appendBytes(byteBuffer.array(), 0, read);
      }
    } while (read > 0 && inputStream.getLength() < 16000000);
    if (inputStream.getLength() >= 16000000) {
      PLogger.println("[" + PTime.getFullDate() + "-RequestServer] Data exceeded 16MB");
      printStats(null);
      inputStream.clear();
    }
    while (inputStream.available() >= 7) {
      inputStream.mark();
      int opcode = inputStream.readUnsignedByte();
      int length = inputStream.readInt();
      if (length < 0 || length >= 16000000) {
        PLogger.println("[" + PTime.getFullDate() + "-RequestServer] Invalid length: " + opcode
            + ", " + length);
        printStats(null);
        inputStream.clear();
        break;
      }
      if (inputStream.available() < length) {
        inputStream.reset();
        break;
      }
      inputStream.mark();
      int position = inputStream.getPosition();
      int key = inputStream.readInt();
      Request request = requestsByKey.get(key);
      if (key != -1 && request == null) {
        inputStream.skip(length);
        continue;
      }
      switch (opcode) {
        case Opcodes.PING:
          decodePing((PingRequest) request);
          break;
        case Opcodes.PLAYER_COUNT:
          decodePlayerCount();
          break;
        case Opcodes.WORLDS_SHUTDOWN:
          decodeWorldsShutdown();
          break;
        case Opcodes.PLAYER_LOGIN:
          decodePlayerLogin((PlayerLoginRequest) request);
          break;
        case Opcodes.PLAYER_LOGOUT:
          decodePlayerLogout((PlayerLogoutRequest) request);
          break;
        case Opcodes.LOAD_FRIENDS:
          decodeLoadFriends((LoadFriendsRequest) request);
          break;
        case Opcodes.LOAD_FRIEND:
          decodeLoadFriend((LoadFriendRequest) request);
          break;
        case Opcodes.LOAD_IGNORE:
          decodeLoadIgnore((LoadIgnoreRequest) request);
          break;
        case Opcodes.LOAD_CLAN:
          decodeLoadClan((LoadClanRequest) request);
          break;
        case Opcodes.JOIN_CLAN:
          decodeJoinClan((JoinClanRequest) request);
          break;
        case Opcodes.CLAN_RANK:
          decodeClanRank((ClanRankRequest) request);
          break;
        case Opcodes.FRIEND_STATUS:
          if (request != null) {
            decodeFriendStatus((FriendStatusRequest) request);
          } else {
            decodeFriendStatus();
          }
          break;
        case Opcodes.FRIEND_STATUS_SINGLE:
          if (request != null) {
            decodeFriendStatusSingle((FriendStatusSingleRequest) request);
          } else {
            decodeFriendStatusSingle();
          }
          break;
        case Opcodes.PRIVATE_MESSAGE:
          if (request != null) {
            decodePrivateMessage((PrivateMessageRequest) request);
          } else {
            decodePrivateMessage();
          }
          break;
        case Opcodes.CLAN_SETTING:
          decodeClanSetting((ClanSettingRequest) request);
          break;
        case Opcodes.CLAN_UPDATE:
          decodeClanUpdate();
          break;
        case Opcodes.CLAN_MESSAGE:
          if (request != null) {
            decodeClanMessage((ClanMessageRequest) request);
          } else {
            decodeClanMessage();
          }
          break;
        case Opcodes.KICK_CLAN_USER:
          if (request != null) {
            decodeKickClanUser((KickClanUserRequest) request);
          } else {
            decodeKickClanUser();
          }
          break;
        case Opcodes.LEAVE_CLAN:
          decodeLeaveClan((LeaveClanRequest) request);
          break;
        case Opcodes.SQL_UPDATE:
          decodeSqlUpdate((SQLUpdateRequest) request);
          break;
        case Opcodes.GE_REFRESH:
          if (request != null) {
            decodeGERefresh((GERefreshRequest) request);
          } else {
            decodeGERefresh();
          }
          break;
        case Opcodes.GE_BUY_OFFER:
          decodeGEBuyOffer((GEBuyOfferRequest) request);
          break;
        case Opcodes.GE_SELL_OFFER:
          decodeGESellOffer((GESellOfferRequest) request);
          break;
        case Opcodes.GE_ABORT_OFFER:
          decodeGEAbortOffer((GEAbortOfferRequest) request);
          break;
        case Opcodes.GE_COLLECT_OFFER:
          decodeGECollectOffer((GECollectOfferRequest) request);
          break;
        case Opcodes.GE_PRICE_AVERAGE:
          decodeGEPriceAverage((GEPriceAverageRequest) request);
          break;
        case Opcodes.GE_HISTORY:
          decodeGEHistory((GEHistoryRequest) request);
          break;
        case Opcodes.GE_SHOP:
          decodeGEShop((GEShopRequest) request);
          break;
        case Opcodes.GE_SHOP_OFFER:
          decodeGEShopOffer((GEShopOfferRequest) request);
          break;
        case Opcodes.GE_LIST:
          decodeGEList((GEListRequest) request);
          break;
        default:
          PLogger.println("[" + PTime.getFullDate() + "-RequestServer] Invalid opcode: " + opcode
              + ":" + length);
          printStats(request);
          inputStream.clear();
          return;
      }
      if (position + length != inputStream.getPosition()) {
        PLogger.println("[" + PTime.getFullDate() + "-RequestServer] Opcode " + opcode
            + " mismatched: " + (inputStream.getPosition() - position) + " but expected " + length);
        printStats(request);
        inputStream.clear();
        return;
      }
      inputStream.reset();
      inputStream.skip(length);
      lastReadOpcode3 = lastReadOpcode2;
      lastReadSize3 = lastReadSize2;
      lastReadOpcode2 = lastReadOpcode1;
      lastReadSize2 = lastReadSize1;
      lastReadOpcode1 = opcode;
      lastReadSize1 = length;
    }
    if (inputStream.available() == 0) {
      inputStream.clear();
    }
  }

  private void encodePing(PingRequest request) throws IOException {
    outputStream.writeOpcodeVarInt(Opcodes.PING);
    outputStream.writeInt(request.getKey());
    outputStream.writeString("ping");
    outputStream.endOpcodeVarInt();
    socket.write(ByteBuffer.wrap(outputStream.toByteArray()));
    request.setState(Request.State.PENDING_RECEIVE);
  }

  private void decodePing(PingRequest request) {
    String pong = inputStream.readString();
    if (!pong.equals("pong")) {
      PLogger.println("[" + PTime.getFullDate() + "-RequestServer] Pong actually " + pong);
      disconnect();
    }
    request.setState(Request.State.COMPLETE);
  }

  private void decodePlayerCount() {
    playerCount = inputStream.readInt();
  }

  private void encodeWorldsShutdown(WorldsShutdownRequest request) throws IOException {
    outputStream.writeOpcodeVarInt(Opcodes.WORLDS_SHUTDOWN);
    outputStream.writeInt(request.getKey());
    outputStream.writeInt(request.getTime());
    outputStream.endOpcodeVarInt();
    socket.write(ByteBuffer.wrap(outputStream.toByteArray()));
    request.setState(Request.State.COMPLETE);
  }

  private void decodeWorldsShutdown() {
    worldShutdown = inputStream.readInt();
  }

  private void decodePlayerLogin(PlayerLoginRequest request) {
    usernameRequests.remove(request.getUsername().toLowerCase());
    int state = inputStream.readUnsignedByte();
    int userId = -1;
    String username = "";
    int rights = 0;
    byte[] charFile = null;
    byte[] sqlFile = null;
    if (state != 0) {
      userId = -state;
    } else {
      userId = inputStream.readInt();
      username = inputStream.readString();
      rights = inputStream.readUnsignedByte();
      int charFileLength = inputStream.readUnsignedShort();
      if (charFileLength > 0) {
        charFile = new byte[charFileLength];
        inputStream.readBytes(charFile);
        charFile = Readers.gzDecompress(charFile);
      }
      int sqlFileLength = inputStream.readUnsignedShort();
      if (sqlFileLength > 0) {
        sqlFile = new byte[sqlFileLength];
        inputStream.readBytes(sqlFile);
        sqlFile = Readers.gzDecompress(sqlFile);
      }
    }
    PlayerLoginResponse response =
        new PlayerLoginResponse(request, userId, username, rights, charFile, sqlFile);
    request.setResponse(response);
  }

  private void encodePlayerLogin(PlayerLoginRequest request) throws IOException {
    outputStream.writeOpcodeVarInt(Opcodes.PLAYER_LOGIN);
    outputStream.writeInt(request.getKey());
    outputStream.writeString(request.getUsername());
    outputStream.writeString(request.getPassword());
    outputStream.writeString(request.getIP());
    outputStream.writeShort(request.getWorldId());
    outputStream.endOpcodeVarInt();
    socket.write(ByteBuffer.wrap(outputStream.toByteArray()));
    request.setState(Request.State.PENDING_RECEIVE);
  }

  private void decodePlayerLogout(PlayerLogoutRequest request) {
    String packetVerification = inputStream.readString();
    if (!packetVerification.equals(Opcodes.PACKET_END_VERIFICATION)) {
      request.setState(Request.State.PENDING_SEND);
      PLogger
          .println("[" + PTime.getFullDate() + "] Bad Logout Verification: " + packetVerification);
      return;
    }
    usernameRequests.remove(request.getUsername().toLowerCase());
    request.setState(Request.State.COMPLETE);
  }

  private void encodePlayerLogout(PlayerLogoutRequest request) throws IOException {
    outputStream.writeOpcodeVarInt(Opcodes.PLAYER_LOGOUT);
    outputStream.writeInt(request.getKey());
    outputStream.writeString(request.getUsername());
    outputStream.writeInt(request.getUserId());
    byte[] charFile = request.getCharFile();
    if (charFile != null) {
      charFile = Writers.gzCompress(charFile);
      outputStream.writeShort(charFile.length);
      outputStream.writeBytes(charFile);
    } else {
      outputStream.writeShort(0);
    }
    outputStream.writeString(Opcodes.PACKET_END_VERIFICATION);
    outputStream.endOpcodeVarInt();
    socket.write(ByteBuffer.wrap(outputStream.toByteArray()));
    request.setState(Request.State.PENDING_RECEIVE);
    if (request.getCharFile() != null) {
      Writers.writeFile(new File(Settings.getInstance().getPlayerMapBackupDirectory(),
          request.getUserId() + ".dat"), request.getCharFile());
    }
  }

  private void internalPlayerLogout(PlayerLogoutRequest request) {
    Writers.writeFile(
        new File(Settings.getInstance().getPlayerMapDirectory(), request.getUserId() + ".dat"),
        request.getCharFile());
  }

  private void decodeLoadFriends(LoadFriendsRequest request) {
    boolean hasData = inputStream.readUnsignedByte() == 1;
    if (!hasData) {
      request.setState(Request.State.COMPLETE);
      return;
    }
    int friendsSize = inputStream.readUnsignedShort();
    for (int i = 0; i < friendsSize; i++) {
      String username = inputStream.readString();
      int worldId = inputStream.readUnsignedShort();
      RsFriend friend = new RsFriend(username);
      int indexOf = request.getFriends().indexOf(friend);
      if (indexOf != -1) {
        request.getFriends().get(indexOf).setWorldId(worldId);
      }
    }
    request.setState(Request.State.COMPLETE);
  }

  private void encodeLoadFriends(LoadFriendsRequest request) throws IOException {
    outputStream.writeOpcodeVarInt(Opcodes.LOAD_FRIENDS);
    outputStream.writeInt(request.getKey());
    outputStream.writeString(request.getUsername());
    outputStream.writeByte(request.getIcon());
    outputStream.writeByte(request.getIcon2());
    outputStream.writeShort(request.getFriends().size());
    for (RsFriend friend : request.getFriends()) {
      outputStream.writeString(friend.getUsername());
      outputStream.writeByte(friend.getClanRank().ordinal());
    }
    outputStream.writeShort(request.getIgnores().size());
    for (String username : request.getIgnores()) {
      outputStream.writeString(username);
    }
    outputStream.writeByte(request.getPrivateChatStatus());
    outputStream.endOpcodeVarInt();
    socket.write(ByteBuffer.wrap(outputStream.toByteArray()));
    request.setState(Request.State.PENDING_RECEIVE);
  }

  private void decodeLoadFriend(LoadFriendRequest request) {
    request.getFriend().setWorldId(inputStream.readUnsignedShort());
    request.setState(Request.State.COMPLETE);
  }

  private void encodeLoadFriend(LoadFriendRequest request) throws IOException {
    outputStream.writeOpcodeVarInt(Opcodes.LOAD_FRIEND);
    outputStream.writeInt(request.getKey());
    outputStream.writeString(request.getUsername());
    outputStream.writeString(request.getFriend().getUsername());
    outputStream.writeByte(request.getFriend().getClanRank().ordinal());
    outputStream.endOpcodeVarInt();
    socket.write(ByteBuffer.wrap(outputStream.toByteArray()));
    request.setState(Request.State.PENDING_RECEIVE);
  }

  private void decodeLoadIgnore(LoadIgnoreRequest request) {
    request.setState(Request.State.COMPLETE);
  }

  private void encodeLoadIgnore(LoadIgnoreRequest request) throws IOException {
    outputStream.writeOpcodeVarInt(Opcodes.LOAD_IGNORE);
    outputStream.writeInt(request.getKey());
    outputStream.writeString(request.getUsername());
    outputStream.writeString(request.getIgnoreUsername());
    outputStream.endOpcodeVarInt();
    socket.write(ByteBuffer.wrap(outputStream.toByteArray()));
    request.setState(Request.State.PENDING_RECEIVE);
  }

  private void decodeLoadClan(LoadClanRequest request) {
    boolean hasData = inputStream.readUnsignedByte() == 1;
    if (!hasData) {
      request.setState(Request.State.COMPLETE);
      return;
    }
    String name = inputStream.readString();
    boolean disabled = inputStream.readUnsignedByte() == 1;
    int enterLimit = inputStream.readUnsignedByte();
    int talkLimit = inputStream.readUnsignedByte();
    int kickLimit = inputStream.readUnsignedByte();
    LoadClanResponse response =
        new LoadClanResponse(request, name, disabled, enterLimit, talkLimit, kickLimit);
    request.setResponse(response);
  }

  private void encodeLoadClan(LoadClanRequest request) throws IOException {
    outputStream.writeOpcodeVarInt(Opcodes.LOAD_CLAN);
    outputStream.writeInt(request.getKey());
    outputStream.writeString(request.getUsername());
    outputStream.endOpcodeVarInt();
    socket.write(ByteBuffer.wrap(outputStream.toByteArray()));
    request.setState(Request.State.PENDING_RECEIVE);
  }

  private void decodeJoinClan(JoinClanRequest request) {
    boolean hasData = inputStream.readUnsignedByte() == 1;
    if (!hasData) {
      request.setState(Request.State.COMPLETE);
      return;
    }
    String name = inputStream.readString();
    int userId = inputStream.readInt();
    int kickLimit = inputStream.readUnsignedByte();
    List<RsClanActiveUser> users = new ArrayList<>();
    int count = inputStream.readUnsignedByte();
    for (int i = 0; i < count; i++) {
      users.add(new RsClanActiveUser(inputStream.readString(), inputStream.readUnsignedShort(),
          RsClanRank.values()[inputStream.readUnsignedByte()]));
    }
    JoinClanResponse response = new JoinClanResponse(request, name, userId, kickLimit, users);
    request.setResponse(response);
  }

  private void encodeJoinClan(JoinClanRequest request) throws IOException {
    outputStream.writeOpcodeVarInt(Opcodes.JOIN_CLAN);
    outputStream.writeInt(request.getKey());
    outputStream.writeString(request.getUsername());
    outputStream.writeString(request.getJoinUsername());
    outputStream.endOpcodeVarInt();
    socket.write(ByteBuffer.wrap(outputStream.toByteArray()));
    request.setState(Request.State.PENDING_RECEIVE);
  }

  private void decodeFriendStatus(FriendStatusRequest request) {
    request.setState(Request.State.COMPLETE);
  }

  private void decodeFriendStatus() {
    List<RsFriend> friends = null;
    List<String> ignores = null;
    int privateChatStatus = 0;
    String username = inputStream.readString();
    int worldId = inputStream.readUnsignedShort();
    if (worldId != 0) {
      friends = new ArrayList<>();
      int friendsSize = inputStream.readUnsignedShort();
      for (int i = 0; i < friendsSize; i++) {
        friends.add(new RsFriend(inputStream.readString()));
      }
      ignores = new ArrayList<>();
      int ignoresSize = inputStream.readUnsignedShort();
      for (int i = 0; i < ignoresSize; i++) {
        ignores.add(inputStream.readString());
      }
      privateChatStatus = inputStream.readUnsignedByte();
    }
    FriendStatusResponse response = new FriendStatusResponse(null, new RsFriend(username, worldId),
        friends, ignores, privateChatStatus);
    synchronized (requestNotifications) {
      requestNotifications.add(response);
    }
  }

  private void encodeFriendStatus(FriendStatusRequest request) throws IOException {
    outputStream.writeOpcodeVarInt(Opcodes.FRIEND_STATUS);
    outputStream.writeInt(request.getKey());
    outputStream.writeString(request.getUsername());
    outputStream.writeByte(request.getPrivateChatStatus());
    outputStream.endOpcodeVarInt();
    socket.write(ByteBuffer.wrap(outputStream.toByteArray()));
    request.setState(Request.State.PENDING_RECEIVE);
  }

  private void decodeFriendStatusSingle(FriendStatusSingleRequest request) {
    request.setState(Request.State.COMPLETE);
  }

  private void decodeFriendStatusSingle() {
    String username = inputStream.readString();
    int worldId = inputStream.readUnsignedShort();
    List<RsFriend> friends = new ArrayList<>();
    int friendsSize = inputStream.readUnsignedShort();
    for (int i = 0; i < friendsSize; i++) {
      friends.add(new RsFriend(inputStream.readString()));
    }
    List<String> ignores = new ArrayList<>();
    int ignoresSize = inputStream.readUnsignedShort();
    for (int i = 0; i < ignoresSize; i++) {
      ignores.add(inputStream.readString());
    }
    int privateChatStatus = inputStream.readUnsignedByte();
    String usernameAffected = inputStream.readString();
    FriendStatusSingleResponse response = new FriendStatusSingleResponse(null,
        new RsFriend(username, worldId), friends, ignores, privateChatStatus, usernameAffected);
    synchronized (requestNotifications) {
      requestNotifications.add(response);
    }
  }

  private void encodeFriendStatusSingle(FriendStatusSingleRequest request) throws IOException {
    outputStream.writeOpcodeVarInt(Opcodes.FRIEND_STATUS_SINGLE);
    outputStream.writeInt(request.getKey());
    outputStream.writeString(request.getUsername());
    outputStream.writeString(request.getUsernameAffected());
    outputStream.endOpcodeVarInt();
    socket.write(ByteBuffer.wrap(outputStream.toByteArray()));
    request.setState(Request.State.PENDING_RECEIVE);
  }

  private void decodeClanRank(ClanRankRequest request) {
    request.getFriend().setWorldId(inputStream.readUnsignedShort());
    request.setState(Request.State.COMPLETE);
  }

  private void encodeClanRank(ClanRankRequest request) throws IOException {
    outputStream.writeOpcodeVarInt(Opcodes.CLAN_RANK);
    outputStream.writeInt(request.getKey());
    outputStream.writeString(request.getUsername());
    outputStream.writeString(request.getFriend().getUsername());
    outputStream.writeByte(request.getFriend().getClanRank().ordinal());
    outputStream.endOpcodeVarInt();
    socket.write(ByteBuffer.wrap(outputStream.toByteArray()));
    request.setState(Request.State.PENDING_RECEIVE);
  }

  private void decodePrivateMessage(PrivateMessageRequest request) {
    request.setState(Request.State.COMPLETE);
  }

  private void decodePrivateMessage() {
    String senderUsername = inputStream.readString();
    int icon = inputStream.readUnsignedByte();
    String receiverUsername = inputStream.readString();
    String message = inputStream.readString();
    PrivateMessageResponse response =
        new PrivateMessageResponse(null, senderUsername, icon, receiverUsername, message);
    synchronized (requestNotifications) {
      requestNotifications.add(response);
    }
  }

  private void encodePrivateMessage(PrivateMessageRequest request) throws IOException {
    outputStream.writeOpcodeVarInt(Opcodes.PRIVATE_MESSAGE);
    outputStream.writeInt(request.getKey());
    outputStream.writeString(request.getSenderUsername());
    outputStream.writeByte(request.getIcon());
    outputStream.writeString(request.getReceiverUsername());
    outputStream.writeString(request.getMessage());
    outputStream.endOpcodeVarInt();
    socket.write(ByteBuffer.wrap(outputStream.toByteArray()));
    request.setState(Request.State.PENDING_RECEIVE);
  }

  private void decodeClanSetting(ClanSettingRequest request) {
    request.setState(Request.State.COMPLETE);
  }

  private void encodeClanSetting(ClanSettingRequest request) throws IOException {
    outputStream.writeOpcodeVarInt(Opcodes.CLAN_SETTING);
    outputStream.writeInt(request.getKey());
    outputStream.writeString(request.getUsername());
    outputStream.writeByte(request.getType());
    if (request.getType() == Clan.NAME) {
      outputStream.writeString(request.readString());
    } else {
      outputStream.writeByte(request.getValue());
    }
    outputStream.endOpcodeVarInt();
    socket.write(ByteBuffer.wrap(outputStream.toByteArray()));
    request.setState(Request.State.PENDING_RECEIVE);
  }

  private void decodeClanUpdate() {
    String clanUsername = inputStream.readString();
    String name = inputStream.readString();
    int kickLimit = inputStream.readUnsignedByte();
    List<RsClanActiveUser> users = new ArrayList<>();
    int count = inputStream.readUnsignedByte();
    for (int i = 0; i < count; i++) {
      users.add(new RsClanActiveUser(inputStream.readString(), inputStream.readUnsignedShort(),
          RsClanRank.values()[inputStream.readUnsignedByte()]));
    }
    ClanUpdateResponse response =
        new ClanUpdateResponse(null, clanUsername, name, kickLimit, users);
    synchronized (requestNotifications) {
      requestNotifications.add(response);
    }
  }

  private void decodeClanMessage(ClanMessageRequest request) {
    request.setState(Request.State.COMPLETE);
  }

  private void decodeClanMessage() {
    String clanUsername = inputStream.readString();
    String username = inputStream.readString();
    int icon = inputStream.readUnsignedByte();
    String message = inputStream.readString();
    ClanMessageResponse response =
        new ClanMessageResponse(null, clanUsername, username, icon, message);
    synchronized (requestNotifications) {
      requestNotifications.add(response);
    }
  }

  private void encodeClanMessage(ClanMessageRequest request) throws IOException {
    outputStream.writeOpcodeVarInt(Opcodes.CLAN_MESSAGE);
    outputStream.writeInt(request.getKey());
    outputStream.writeString(request.getUsername());
    outputStream.writeByte(request.getIcon());
    outputStream.writeString(request.getClanUsername());
    outputStream.writeString(request.getMessage());
    outputStream.endOpcodeVarInt();
    socket.write(ByteBuffer.wrap(outputStream.toByteArray()));
    request.setState(Request.State.PENDING_RECEIVE);
  }

  private void decodeKickClanUser(KickClanUserRequest request) {
    request.setState(Request.State.COMPLETE);
  }

  private void decodeKickClanUser() {
    String username = inputStream.readString();
    String clanUsername = inputStream.readString();
    KickClanUserResponse response = new KickClanUserResponse(null, username, clanUsername);
    synchronized (requestNotifications) {
      requestNotifications.add(response);
    }
  }

  private void encodeKickClanUser(KickClanUserRequest request) throws IOException {
    outputStream.writeOpcodeVarInt(Opcodes.KICK_CLAN_USER);
    outputStream.writeInt(request.getKey());
    outputStream.writeString(request.getUsername());
    outputStream.writeString(request.getClanUsername());
    outputStream.writeString(request.getKickUsername());
    outputStream.endOpcodeVarInt();
    socket.write(ByteBuffer.wrap(outputStream.toByteArray()));
    request.setState(Request.State.PENDING_RECEIVE);
  }

  private void decodeLeaveClan(LeaveClanRequest request) {
    request.setState(Request.State.COMPLETE);
  }

  private void encodeLeaveClan(LeaveClanRequest request) throws IOException {
    outputStream.writeOpcodeVarInt(Opcodes.LEAVE_CLAN);
    outputStream.writeInt(request.getKey());
    outputStream.writeString(request.getUsername());
    outputStream.writeString(request.getClanUsername());
    outputStream.endOpcodeVarInt();
    socket.write(ByteBuffer.wrap(outputStream.toByteArray()));
    request.setState(Request.State.PENDING_RECEIVE);
  }

  private void decodeSqlUpdate(SQLUpdateRequest request) {
    request.setState(Request.State.COMPLETE);
  }

  private void encodeSqlUpdate(SQLUpdateRequest request) throws IOException {
    outputStream.writeOpcodeVarInt(Opcodes.SQL_UPDATE);
    outputStream.writeInt(request.getKey());
    outputStream.writeString(PString.cleanString(request.getSql()));
    outputStream.endOpcodeVarInt();
    socket.write(ByteBuffer.wrap(outputStream.toByteArray()));
    request.setState(Request.State.PENDING_RECEIVE);
  }

  private void decodeGERefresh(GERefreshRequest request) {
    GrandExchangeItem[] items = new GrandExchangeItem[GrandExchangeItem.MAX_P2P_ITEMS];
    for (int i = 0; i < items.length; i++) {
      int itemId = inputStream.readInt();
      if (itemId != -1) {
        int status = inputStream.readUnsignedByte();
        int amount = inputStream.readInt();
        int price = inputStream.readInt();
        GrandExchangeItem item = new GrandExchangeItem(status, itemId, amount, price);
        items[i] = item;
        item.setExchanged(inputStream.readInt(), inputStream.readInt());
        item.setCollectedAmount(inputStream.readInt());
        item.setCollectedPrice(inputStream.readInt());
        item.setAborted(inputStream.readUnsignedByte() == 1);
      }
    }
    request.setResponse(new GERefreshResponse(request, items));
    request.setState(Request.State.COMPLETE);
  }

  private void decodeGERefresh() {
    int userId = inputStream.readInt();
    int gameMode = inputStream.readUnsignedByte();
    GrandExchangeItem[] items = new GrandExchangeItem[GrandExchangeItem.MAX_P2P_ITEMS];
    for (int i = 0; i < items.length; i++) {
      int itemId = inputStream.readInt();
      if (itemId != -1) {
        int status = inputStream.readUnsignedByte();
        int amount = inputStream.readInt();
        int price = inputStream.readInt();
        GrandExchangeItem item = new GrandExchangeItem(status, itemId, amount, price);
        items[i] = item;
        item.setExchanged(inputStream.readInt(), inputStream.readInt());
        item.setCollectedAmount(inputStream.readInt());
        item.setCollectedPrice(inputStream.readInt());
        item.setAborted(inputStream.readUnsignedByte() == 1);
      }
    }
    GERefreshResponse response = new GERefreshResponse(null, userId, gameMode, items);
    synchronized (requestNotifications) {
      requestNotifications.add(response);
    }
  }

  private void encodeGERefresh(GERefreshRequest request) throws IOException {
    outputStream.writeOpcodeVarInt(Opcodes.GE_REFRESH);
    outputStream.writeInt(request.getKey());
    outputStream.writeInt(request.getUserId());
    outputStream.writeString(request.getUsername());
    outputStream.writeByte(request.getGameMode());
    outputStream.endOpcodeVarInt();
    socket.write(ByteBuffer.wrap(outputStream.toByteArray()));
    request.setState(Request.State.PENDING_RECEIVE);
  }

  private void decodeGEBuyOffer(GEBuyOfferRequest request) {
    request.setState(Request.State.COMPLETE);
  }

  private void encodeGEBuyOffer(GEBuyOfferRequest request) throws IOException {
    outputStream.writeOpcodeVarInt(Opcodes.GE_BUY_OFFER);
    outputStream.writeInt(request.getKey());
    outputStream.writeInt(request.getUserId());
    outputStream.writeString(request.getUsername());
    outputStream.writeString(request.getUserIP());
    outputStream.writeByte(request.getGameMode());
    outputStream.writeByte(request.getSlot());
    outputStream.writeInt(request.getId());
    outputStream.writeString(request.getName());
    outputStream.writeInt(request.getAmount());
    outputStream.writeInt(request.getPrice());
    outputStream.endOpcodeVarInt();
    socket.write(ByteBuffer.wrap(outputStream.toByteArray()));
    request.setState(Request.State.PENDING_RECEIVE);
  }

  private void decodeGESellOffer(GESellOfferRequest request) {
    request.setState(Request.State.COMPLETE);
  }

  private void encodeGESellOffer(GESellOfferRequest request) throws IOException {
    outputStream.writeOpcodeVarInt(Opcodes.GE_SELL_OFFER);
    outputStream.writeInt(request.getKey());
    outputStream.writeInt(request.getUserId());
    outputStream.writeString(request.getUsername());
    outputStream.writeString(request.getUserIP());
    outputStream.writeByte(request.getGameMode());
    outputStream.writeByte(request.getSlot());
    outputStream.writeInt(request.getId());
    outputStream.writeString(request.getName());
    outputStream.writeInt(request.getAmount());
    outputStream.writeInt(request.getPrice());
    outputStream.endOpcodeVarInt();
    socket.write(ByteBuffer.wrap(outputStream.toByteArray()));
    request.setState(Request.State.PENDING_RECEIVE);
  }

  private void decodeGEAbortOffer(GEAbortOfferRequest request) {
    request.setState(Request.State.COMPLETE);
  }

  private void encodeGEAbortOffer(GEAbortOfferRequest request) throws IOException {
    outputStream.writeOpcodeVarInt(Opcodes.GE_ABORT_OFFER);
    outputStream.writeInt(request.getKey());
    outputStream.writeInt(request.getUserId());
    outputStream.writeByte(request.getSlot());
    outputStream.endOpcodeVarInt();
    socket.write(ByteBuffer.wrap(outputStream.toByteArray()));
    request.setState(Request.State.PENDING_RECEIVE);
  }

  private void decodeGEPriceAverage(GEPriceAverageRequest request) {
    int totalBuy = inputStream.readInt();
    int averageBuy = inputStream.readInt();
    int totalSell = inputStream.readInt();
    int averageSell = inputStream.readInt();
    int cheapestSell = inputStream.readInt();
    GEPriceAverageResponse response = new GEPriceAverageResponse(request, totalBuy, averageBuy,
        totalSell, averageSell, cheapestSell);
    request.setResponse(response);
    request.setState(Request.State.COMPLETE);
  }

  private void encodeGEPriceAverage(GEPriceAverageRequest request) throws IOException {
    outputStream.writeOpcodeVarInt(Opcodes.GE_PRICE_AVERAGE);
    outputStream.writeInt(request.getKey());
    outputStream.writeInt(request.getUserId());
    outputStream.writeByte(request.getGameMode());
    outputStream.writeInt(request.getItemId());
    outputStream.endOpcodeVarInt();
    socket.write(ByteBuffer.wrap(outputStream.toByteArray()));
    request.setState(Request.State.PENDING_RECEIVE);
  }

  private void decodeGECollectOffer(GECollectOfferRequest request) {
    request.setState(Request.State.COMPLETE);
  }

  private void encodeGECollectOffer(GECollectOfferRequest request) throws IOException {
    outputStream.writeOpcodeVarInt(Opcodes.GE_COLLECT_OFFER);
    outputStream.writeInt(request.getKey());
    outputStream.writeInt(request.getUserId());
    outputStream.writeByte(request.getSlot());
    outputStream.writeInt(request.getCollectedAmount());
    outputStream.writeInt(request.getCollectedPrice());
    outputStream.endOpcodeVarInt();
    socket.write(ByteBuffer.wrap(outputStream.toByteArray()));
    request.setState(Request.State.PENDING_RECEIVE);
  }

  private void decodeGEHistory(GEHistoryRequest request) {
    GrandExchangeItem[] items = new GrandExchangeItem[GrandExchangeUser.HISTORY_SIZE];
    for (int i = 0; i < items.length; i++) {
      int id = inputStream.readInt();
      int state = inputStream.readUnsignedByte();
      int amount = inputStream.readInt();
      int price = inputStream.readInt();
      items[i] = new GrandExchangeItem(state, id, amount, price);
    }
    request.setResponse(new GEHistoryResponse(request, items));
    request.setState(Request.State.COMPLETE);
  }

  private void encodeGEHistory(GEHistoryRequest request) throws IOException {
    outputStream.writeOpcodeVarInt(Opcodes.GE_HISTORY);
    outputStream.writeInt(request.getKey());
    outputStream.writeInt(request.getUserId());
    outputStream.writeByte(request.getType());
    outputStream.endOpcodeVarInt();
    socket.write(ByteBuffer.wrap(outputStream.toByteArray()));
    request.setState(Request.State.PENDING_RECEIVE);
  }

  private void encodeLog(LogRequest request) throws IOException {
    String logDetails = PString.cleanString(request.getLine());
    String dir1 =
        request.getDirectory1() != null ? PString.cleanString(request.getDirectory1()) : "";
    String dir2 =
        request.getDirectory2() != null ? PString.cleanString(request.getDirectory2()) : "";
    outputStream.writeOpcodeVarInt(Opcodes.LOG);
    outputStream.writeInt(request.getKey());
    outputStream.writeString(dir1);
    outputStream.writeString(dir2);
    outputStream.writeString(logDetails);
    outputStream.endOpcodeVarInt();
    socket.write(ByteBuffer.wrap(outputStream.toByteArray()));
    request.setState(Request.State.COMPLETE);
  }

  private void decodeGEShop(GEShopRequest request) {
    int userId = inputStream.readInt();
    String username = inputStream.readString();
    int gameMode = inputStream.readUnsignedByte();
    request.setUsername(username);
    GrandExchangeItem[] items = new GrandExchangeItem[GrandExchangeItem.MAX_P2P_ITEMS];
    for (int i = 0; i < items.length; i++) {
      int itemId = inputStream.readInt();
      if (itemId != -1) {
        int status = inputStream.readUnsignedByte();
        int amount = inputStream.readInt();
        int price = inputStream.readInt();
        GrandExchangeItem item = new GrandExchangeItem(status, itemId, amount, price);
        items[i] = item;
        item.setExchanged(inputStream.readInt(), inputStream.readInt());
        item.setCollectedAmount(inputStream.readInt());
        item.setCollectedPrice(inputStream.readInt());
        item.setAborted(inputStream.readUnsignedByte() == 1);
      }
    }
    request.setResponse(new GERefreshResponse(request, userId, gameMode, items));
    request.setState(Request.State.COMPLETE);
  }

  private void encodeGEShop(GEShopRequest request) throws IOException {
    outputStream.writeOpcodeVarInt(Opcodes.GE_SHOP);
    outputStream.writeInt(request.getKey());
    outputStream.writeString(request.getUsername());
    outputStream.endOpcodeVarInt();
    socket.write(ByteBuffer.wrap(outputStream.toByteArray()));
    request.setState(Request.State.PENDING_RECEIVE);
  }

  private void decodeGEShopOffer(GEShopOfferRequest request) {
    boolean success = inputStream.readBoolean();
    request.setResponse(new GEShopOfferResponse(request, success));
    request.setState(Request.State.COMPLETE);
  }

  private void encodeGEShopOffer(GEShopOfferRequest request) throws IOException {
    outputStream.writeOpcodeVarInt(Opcodes.GE_SHOP_OFFER);
    outputStream.writeInt(request.getKey());
    outputStream.writeInt(request.getUserId());
    outputStream.writeString(request.getUsername());
    outputStream.writeString(request.getUserIP());
    outputStream.writeByte(request.getGameMode());
    outputStream.writeInt(request.getShopUserId());
    outputStream.writeByte(request.getSlot());
    outputStream.writeInt(request.getId());
    outputStream.writeInt(request.getAmount());
    outputStream.writeInt(request.getPrice());
    outputStream.writeByte(request.getGEState());
    outputStream.endOpcodeVarInt();
    socket.write(ByteBuffer.wrap(outputStream.toByteArray()));
    request.setState(Request.State.PENDING_RECEIVE);
  }

  private void decodeGEList(GEListRequest request) {
    String title = inputStream.readString();
    int size = inputStream.readUnsignedByte();
    String[] list = new String[size];
    for (int i = 0; i < size; i++) {
      list[i] = inputStream.readString();
    }
    request.setResponse(new GEListResponse(request, title, list));
    request.setState(Request.State.COMPLETE);
  }

  private void encodeGEList(GEListRequest request) throws IOException {
    outputStream.writeOpcodeVarInt(Opcodes.GE_LIST);
    outputStream.writeInt(request.getKey());
    outputStream.writeInt(request.getUserId());
    outputStream.writeByte(request.getType());
    outputStream.writeInt(request.getSearchId());
    outputStream.writeString(request.getSearchString());
    outputStream.endOpcodeVarInt();
    socket.write(ByteBuffer.wrap(outputStream.toByteArray()));
    request.setState(Request.State.PENDING_RECEIVE);
  }

  private int getUniqueKey() {
    if (++uniqueKey < 1) {
      uniqueKey = 1;
    }
    return uniqueKey;
  }

  public Request addPlayerLogin(String username, String password, String ip, int worldId) {
    Request request;
    synchronized (this) {
      newRequests.add(
          request = new PlayerLoginRequest(null, getUniqueKey(), username, password, ip, worldId));
      usernameRequests.add(username.toLowerCase());
      notify();
    }
    return request;
  }

  public Request addPlayerLogout(String username, int userId, byte[] file) {
    if (Settings.isBeta()) {
      file = null;
    }
    Request request;
    synchronized (this) {
      newRequests.add(request =
          new PlayerLogoutRequest(null, getUniqueKey(), username.toLowerCase(), userId, file));
      usernameRequests.add(username.toLowerCase());
      notify();
    }
    return request;
  }

  public Request addLoadFriends(String username, int icon, int icon2, List<RsFriend> friends,
      List<String> ignores, int privateChatStatus) {
    Request request;
    synchronized (this) {
      newRequests.add(request = new LoadFriendsRequest(null, getUniqueKey(), username.toLowerCase(),
          icon, icon2, friends, ignores, privateChatStatus));
      notify();
    }
    return request;
  }

  public Request addLoadFriend(String username, RsFriend friend) {
    Request request;
    synchronized (this) {
      newRequests.add(request = new LoadFriendRequest(null, getUniqueKey(), username.toLowerCase(),
          new RsFriend(friend)));
      notify();
    }
    return request;
  }

  public Request addLoadIgnore(String username, String ignoreUsername) {
    Request request;
    synchronized (this) {
      newRequests.add(request = new LoadIgnoreRequest(null, getUniqueKey(), username.toLowerCase(),
          ignoreUsername.toLowerCase()));
      notify();
    }
    return request;
  }

  public Request addLoadClan(String username) {
    Request request;
    synchronized (this) {
      newRequests.add(request = new LoadClanRequest(null, getUniqueKey(), username.toLowerCase()));
      notify();
    }
    return request;
  }

  public Request addJoinClan(String username, String joinUsername) {
    Request request;
    synchronized (this) {
      newRequests.add(request = new JoinClanRequest(null, getUniqueKey(), username.toLowerCase(),
          joinUsername.toLowerCase()));
      notify();
    }
    return request;
  }

  public Request addClanRank(String username, RsFriend friend) {
    Request request;
    synchronized (this) {
      newRequests.add(request =
          new ClanRankRequest(null, getUniqueKey(), username.toLowerCase(), new RsFriend(friend)));
      notify();
    }
    return request;
  }

  public Request addFriendStatus(String username, int privateChatStatus) {
    Request request;
    synchronized (this) {
      newRequests.add(request =
          new FriendStatusRequest(null, getUniqueKey(), username.toLowerCase(), privateChatStatus));
      notify();
    }
    return request;
  }

  public Request addFriendStatusSingle(String username, String usernameAffected) {
    Request request;
    synchronized (this) {
      newRequests.add(request = new FriendStatusSingleRequest(null, getUniqueKey(),
          username.toLowerCase(), usernameAffected.toLowerCase()));
      notify();
    }
    return request;
  }

  public Request addPrivateMessage(String senderUsername, int icon, String receiverUsername,
      String message) {
    Request request;
    synchronized (this) {
      newRequests.add(request = new PrivateMessageRequest(null, getUniqueKey(), senderUsername,
          icon, receiverUsername.toLowerCase(), message));
      notify();
    }
    return request;
  }

  public Request addClanSetting(String username, int type, String string) {
    Request request;
    synchronized (this) {
      newRequests.add(request =
          new ClanSettingRequest(null, getUniqueKey(), username.toLowerCase(), type, string));
      notify();
    }
    return request;
  }

  public Request addClanSetting(String username, int type, int value) {
    Request request;
    synchronized (this) {
      newRequests.add(request =
          new ClanSettingRequest(null, getUniqueKey(), username.toLowerCase(), type, value));
      notify();
    }
    return request;
  }

  public Request addClanMessage(String username, int icon, String clanUsername, String message) {
    Request request;
    synchronized (this) {
      newRequests.add(request = new ClanMessageRequest(null, getUniqueKey(), username, icon,
          clanUsername.toLowerCase(), message));
      notify();
    }
    return request;
  }

  public Request addKickClanUser(String username, String clanUsername, String kickUsername) {
    Request request;
    synchronized (this) {
      newRequests.add(request = new KickClanUserRequest(null, getUniqueKey(),
          username.toLowerCase(), clanUsername.toLowerCase(), kickUsername.toLowerCase()));
      notify();
    }
    return request;
  }

  public Request addLeaveClan(String username, String clanUsername) {
    Request request;
    synchronized (this) {
      newRequests.add(request = new LeaveClanRequest(null, getUniqueKey(), username.toLowerCase(),
          clanUsername.toLowerCase()));
      notify();
    }
    return request;
  }

  public Request addSqlUpdate(String sql) {
    if (Settings.isBeta()) {
      return null;
    }
    Request request;
    synchronized (this) {
      newRequests.add(request = new SQLUpdateRequest(null, getUniqueKey(), sql));
      notify();
    }
    return request;
  }

  public Request addGERefresh(int userId, String username, int gameMode, long time) {
    if (Settings.isBeta()) {
      return null;
    }
    GERefreshRequest request;
    synchronized (this) {
      newRequests.add(request = new GERefreshRequest(null, getUniqueKey(), userId,
          username.toLowerCase(), gameMode, time));
      notify();
    }
    return request;
  }

  public Request addGEBuyOffer(int userId, String username, String userIP, int gameMode, int slot,
      int id, String name, int amount, int price) {
    if (Settings.isBeta()) {
      return null;
    }
    GEBuyOfferRequest request;
    synchronized (this) {
      newRequests.add(request = new GEBuyOfferRequest(null, getUniqueKey(), userId,
          username.toLowerCase(), userIP, gameMode, slot, id, name, amount, price));
      notify();
    }
    return request;
  }

  public Request addGESellOffer(int userId, String username, String userIP, int gameMode, int slot,
      int id, String name, int amount, int price) {
    if (Settings.isBeta()) {
      return null;
    }
    GESellOfferRequest request;
    synchronized (this) {
      newRequests.add(request = new GESellOfferRequest(null, getUniqueKey(), userId,
          username.toLowerCase(), userIP, gameMode, slot, id, name, amount, price));
      notify();
    }
    return request;
  }

  public Request addGEAbortOffer(int userId, int slot) {
    if (Settings.isBeta()) {
      return null;
    }
    GEAbortOfferRequest request;
    synchronized (this) {
      newRequests.add(request = new GEAbortOfferRequest(null, getUniqueKey(), userId, slot));
      notify();
    }
    return request;
  }

  public Request addGECollectOffer(int userId, int slot, int collectedAmount, int collectedPrice) {
    if (Settings.isBeta()) {
      return null;
    }
    GECollectOfferRequest request;
    synchronized (this) {
      newRequests.add(request = new GECollectOfferRequest(null, getUniqueKey(), userId, slot,
          collectedAmount, collectedPrice));
      notify();
    }
    return request;
  }

  public Request addGEPriceAverage(int userId, int gameMode, int itemId) {
    if (Settings.isBeta()) {
      return null;
    }
    GEPriceAverageRequest request;
    synchronized (this) {
      newRequests
          .add(request = new GEPriceAverageRequest(null, getUniqueKey(), userId, gameMode, itemId));
      notify();
    }
    return request;
  }

  public Request addGEHistory(int userId, int type) {
    if (Settings.isBeta()) {
      return null;
    }
    GEHistoryRequest request;
    synchronized (this) {
      newRequests.add(request = new GEHistoryRequest(null, getUniqueKey(), userId, type));
      notify();
    }
    return request;
  }

  public Request addLog(File baseDirectory, String fileName, String line) {
    if (Settings.isBeta()) {
      return null;
    }
    Request request;
    synchronized (this) {
      newRequests.add(
          request = new LogRequest(null, getUniqueKey(), baseDirectory.getPath(), fileName, line));
      notify();
    }
    return request;
  }

  public Request addGEShop(String username) {
    if (Settings.isBeta()) {
      return null;
    }
    GEShopRequest request;
    synchronized (this) {
      newRequests.add(request = new GEShopRequest(null, getUniqueKey(), username.toLowerCase()));
      notify();
    }
    return request;
  }

  public Request addGEShopOffer(int userId, String username, String userIP, int gameMode,
      int shopUserId, int slot, int id, int amount, int price, int state) {
    if (Settings.isBeta()) {
      return null;
    }
    GEShopOfferRequest request;
    synchronized (this) {
      newRequests.add(request = new GEShopOfferRequest(null, getUniqueKey(), userId,
          username.toLowerCase(), userIP, gameMode, shopUserId, slot, id, amount, price, state));
      notify();
    }
    return request;
  }

  public Request addGEList(int userId, int type, int searchId, String searchString) {
    if (Settings.isBeta()) {
      return null;
    }
    GEListRequest request;
    synchronized (this) {
      newRequests.add(
          request = new GEListRequest(null, getUniqueKey(), userId, type, searchId, searchString));
      notify();
    }
    return request;
  }

  public Request addWorldsShutdown(int time) {
    if (Settings.isBeta()) {
      return null;
    }
    WorldsShutdownRequest request;
    synchronized (this) {
      newRequests.add(request = new WorldsShutdownRequest(null, getUniqueKey(), time));
      notify();
    }
    return request;
  }

  public Vector<Response> getRequestNotifications() {
    return requestNotifications;
  }

  public boolean hasUsernameRequest(String username) {
    return usernameRequests.contains(username.toLowerCase());
  }

  public void addPlayer(int id, String username, String password, String ip, int worldId,
      int rights) {
    synchronized (this) {
      RsPlayer player = new RsPlayer(id, username.toLowerCase(), password, ip, worldId);
      player.setRights(rights);
      players.add(player);
    }
  }

  public void removePlayer(int id) {
    synchronized (this) {
      for (Iterator<RsPlayer> it = players.iterator(); it.hasNext();) {
        if (it.next().getId() == id) {
          it.remove();
          return;
        }
      }
    }
  }

  public void shutdown() {
    shutdown = PTime.currentTimeMillis();
    synchronized (this) {
      notify();
    }
  }

  public void printStats(Request request) {
    PLogger.println("[" + PTime.getFullDate() + "-RequestServer] requests: " + requests.size()
        + "; new: " + newRequests.size() + "; byKey: " + requestsByKey.size() + "; notifications: "
        + requestNotifications.size() + "; names: " + usernameRequests.size() + "; players: "
        + players.size() + "; in: " + inputStream.getPosition() + "; out: "
        + outputStream.getPosition());
    PLogger.println("lastReadOpcode1: " + lastReadOpcode1 + "; lastReadSize1: " + lastReadSize1
        + "; lastReadOpcode2: " + lastReadOpcode2 + "; lastReadSize2: " + lastReadSize2
        + "; lastReadOpcode3: " + lastReadOpcode3 + "; lastReadSize3: " + lastReadSize3);
    if (request != null) {
      PLogger.println(request + "; state: " + request.getState());
    } else {
      for (Request r : requests) {
        PLogger.println(r + "; state: " + r.getState() + "; attempts: " + r.getSendAttempts());
      }
    }
  }

  public boolean isRunning() {
    return running;
  }

  public int getPlayerCount() {
    return playerCount;
  }

  public int getWorldShutdown() {
    return worldShutdown;
  }

  public static RequestServer getInstance() {
    return instance;
  }

  public static void init(SecureSettings secureSettings, int worldId) {
    if (instance != null) {
      return;
    }
    instance = new RequestServer(secureSettings, worldId);
  }
}
