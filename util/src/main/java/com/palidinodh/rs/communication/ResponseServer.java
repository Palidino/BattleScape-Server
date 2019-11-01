package com.palidinodh.rs.communication;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.palidinodh.encryption.MD5;
import com.palidinodh.io.FileManager;
import com.palidinodh.nio.NioServer;
import com.palidinodh.nio.Session;
import com.palidinodh.nio.SessionHandler;
import com.palidinodh.nio.WriteEventHandler;
import com.palidinodh.rs.adaptive.Clan;
import com.palidinodh.rs.adaptive.GrandExchangeItem;
import com.palidinodh.rs.adaptive.GrandExchangeUser;
import com.palidinodh.rs.adaptive.RsClanActiveUser;
import com.palidinodh.rs.adaptive.RsClanRank;
import com.palidinodh.rs.adaptive.RsFriend;
import com.palidinodh.rs.adaptive.RsGameMode;
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
import com.palidinodh.rs.setting.Forum;
import com.palidinodh.rs.setting.Settings;
import com.palidinodh.rs.setting.SqlUserField;
import com.palidinodh.rs.setting.SqlUserRank;
import com.palidinodh.util.PLogger;
import com.palidinodh.util.PNumber;
import com.palidinodh.util.PString;
import com.palidinodh.util.PTime;
import com.palidinodh.util.PUtil;

public class ResponseServer implements Runnable, SessionHandler {
  public static final boolean DEBUG = false;
  public static final int PRINT_STATS = 4 * 60 * 60 * 1000;
  public static final boolean COUNT_VOTED_BY_REGISTRATION_IP = true;

  private static ResponseServer instance;
  private static boolean isLocal;
  private NioServer nioServer;
  private boolean running;
  private Map<Integer, ServerSession> sessions = new HashMap<>();
  private List<Request> requests = new ArrayList<>();
  private List<Clan> clanUpdates = new ArrayList<>();
  private List<Request> sqlRequests = new ArrayList<>();
  private Map<String, RsPlayer> playersByUsername = new HashMap<>();
  private Map<Integer, RsPlayer> playersById = new HashMap<>();
  private Map<String, Clan> clans = new HashMap<>();
  private int lastLocalUserId = 10;
  public Map<Integer, GrandExchangeUser> grandExchange = new HashMap<>();
  private long lastPlayerCount = PTime.nanoTime();
  private int largestPacket;
  private int lastReadOpcode1 = -1, lastReadSize1 = -1, lastReadOpcode2 = -1, lastReadSize2 = -1,
      lastReadOpcode3 = -1, lastReadSize3 = -1;
  private String lastLog1 = "", lastLog2 = "", lastLog3 = "";
  private String lastSQL1 = "", lastSQL2 = "", lastSQL3 = "";

  public ResponseServer(String ip, int port) {
    try {
      startNIOServer(ip, port);
      FileManager.addXMLAlias("clan", Clan.class);
      FileManager.addXMLAlias("friend", RsFriend.class);
      FileManager.addXMLAlias("grandexchangeuser", GrandExchangeUser.class);
      FileManager.addXMLAlias("grandexchangeitem", GrandExchangeItem.class);
      loadGrandExchangeUsers();
      running = true;
      synchronized (this) {
        notify();
      }
      new Thread(this).start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    List<Request> activeRequests = new ArrayList<>();
    long lastStatPrint = PTime.currentTimeMillis();
    while (running) {
      try {
        PTime.update();
        activeRequests.clear();
        synchronized (this) {
          activeRequests.addAll(requests);
          requests.clear();
        }
        for (Request request : activeRequests) {
          try {
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
            } else if (request instanceof FriendStatusRequest) {
              encodeFriendStatus((FriendStatusRequest) request);
            } else if (request instanceof FriendStatusSingleRequest) {
              encodeFriendStatusSingle((FriendStatusSingleRequest) request);
            } else if (request instanceof PrivateMessageRequest) {
              encodePrivateMessage((PrivateMessageRequest) request);
            } else if (request instanceof LoadClanRequest) {
              encodeLoadClan((LoadClanRequest) request);
            } else if (request instanceof ClanSettingRequest) {
              encodeClanSetting((ClanSettingRequest) request);
            } else if (request instanceof JoinClanRequest) {
              encodeJoinClan((JoinClanRequest) request);
            } else if (request instanceof ClanMessageRequest) {
              encodeClanMessage((ClanMessageRequest) request);
            } else if (request instanceof ClanRankRequest) {
              encodeClanRank((ClanRankRequest) request);
            } else if (request instanceof KickClanUserRequest) {
              encodeKickClanUser((KickClanUserRequest) request);
            } else if (request instanceof LeaveClanRequest) {
              encodeLeaveClan((LeaveClanRequest) request);
            } else if (request instanceof SQLUpdateRequest) {
              encodeSQLUpdate((SQLUpdateRequest) request);
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
          } catch (Exception e2) {
            PLogger.error(e2);
          }
        }
        synchronized (this) {
          if (!clanUpdates.isEmpty()) {
            for (Iterator<Clan> it = clanUpdates.iterator(); it.hasNext();) {
              Clan clan = it.next();
              if (!clan.canUpdate()) {
                continue;
              }
              encodeClanUpdate(clan);
              it.remove();
            }
          }
          if (!clans.isEmpty()) {
            for (Iterator<Clan> it = clans.values().iterator(); it.hasNext();) {
              Clan clan = it.next();
              if (clan.getActiveUsers() != null && !clan.getActiveUsers().isEmpty()) {
                continue;
              }
              clanUpdates.remove(clan);
              it.remove();
            }
          }
          if (!sqlRequests.isEmpty()) {
            for (Iterator<Request> it = sqlRequests.iterator(); it.hasNext();) {
              Request request = it.next();
              if (isLocal || executeSQLUpdate((SQLUpdateRequest) request)) {
                it.remove();
              }
            }
          }
          if (!isLocal && PTime.nanoToMin(lastPlayerCount) >= 1) {
            lastPlayerCount = PTime.nanoTime();
            int playerCount = 0;
            Statement statement = null;
            try {
              Connection connection = FileManager.getSqlConnection();
              statement = connection.createStatement();
              boolean hasResults = statement
                  .execute("SELECT text, varname FROM phrase WHERE varname LIKE '%playercount'");
              while (hasResults) {
                ResultSet set = statement.getResultSet();
                while (set.next()) {
                  playerCount += set.getInt("text");
                }
                set.close();
                hasResults = statement.getMoreResults();
              }
              statement.close();
            } catch (Exception e) {
            } finally {
              try {
                if (statement != null) {
                  statement.close();
                }
              } catch (Exception e) {
              }
            }
            for (ServerSession session : sessions.values()) {
              session.getOutput().addOpcodeVarInt(Opcodes.PLAYER_COUNT);
              session.getOutput().addInt(-1);
              session.getOutput().addInt(playerCount);
              session.getOutput().endOpcodeVarInt();
              session.write();
            }
          }
          if (requests.isEmpty() && running && clanUpdates.isEmpty() && sqlRequests.isEmpty()) {
            try {
              this.wait(5000);
            } catch (InterruptedException ie) {
              break;
            }
          } else {
            try {
              this.wait(50);
            } catch (InterruptedException ie) {
              break;
            }
          }
        }
        PUtil.gc();
        if (PTime.currentTimeMillis() - lastStatPrint >= PRINT_STATS) {
          lastStatPrint = PTime.currentTimeMillis();
          // printStats();
        }
      } catch (Exception e) {
        PLogger.error(e);
      }
    }
    nioServer.stop();
  }

  @Override
  public void accept(Session session) {
    ServerSession serverSession = new ServerSession(session);
    session.setAttachment(serverSession);
  }

  @Override
  public void read(Session session, byte[] bytes) {
    ServerSession serverSession = (ServerSession) session.getAttachment();
    synchronized (serverSession.getInput()) {
      serverSession.getInput().appendBytes(bytes);
      decode(serverSession);
    }
    synchronized (this) {
      notify();
    }
  }

  @Override
  public void error(Exception exception, Session session) {
    session.close();
    exception.printStackTrace();
  }

  @Override
  public void closed(Session session) {}

  private void startNIOServer(String ip, int port) throws IOException {
    nioServer = new NioServer();
    nioServer.setSessionHandler(this);
    nioServer.setMaxConnectionsPerIPAddress(2);
    nioServer.setSessionIdleTimeout(Opcodes.PING_DELAY / 1000 * 2);
    nioServer.setSocketBufferSize(1_048_576);
    nioServer.start(ip, port);
  }

  private void decode(ServerSession session) {
    synchronized (session.getInput()) {
      while (session.getInput().available() > 0) {
        session.getInput().mark();
        int opcode = session.getInput().getOpcode();
        int length = session.getInput().getInt();
        if (length > largestPacket) {
          largestPacket = length;
        }
        if (length > 4194304) {
          PLogger.println("[" + PTime.getFullDate() + "] Long length: " + opcode + ":" + length
              + "; " + lastReadOpcode1 + ":" + lastReadSize1 + "; " + lastReadOpcode2 + ":"
              + lastReadSize2 + "; " + lastReadOpcode3 + ":" + lastReadSize3);
          session.getInput().clear();
          break;
        }
        if (length == 0 || session.getInput().available() < length) {
          session.getInput().reset();
          break;
        }
        if (DEBUG) {
          PLogger.println("[" + PTime.getFullDate() + "] Decode: " + opcode);
        }
        session.getInput().mark();
        int position = session.getInput().getPosition();
        if (opcode != Opcodes.VERIFY && !session.passwordMatches()) {
          session.close();
          return;
        }
        switch (opcode) {
          case Opcodes.VERIFY:
            decodeVerify(session);
            break;
          case Opcodes.WORLD_SHUTDOWN:
            PLogger.println(
                "[" + PTime.getFullDate() + "] World shutdown, last opcode: " + lastReadOpcode1);
            decodeWorldShutdown(session);
            break;
          case Opcodes.WORLDS_SHUTDOWN:
            decodeWorldsShutdown(session);
            break;
          case Opcodes.PING:
            decodePing(session);
            break;
          case Opcodes.SHUTDOWN:
            decodeShutdown(session);
            break;
          case Opcodes.PLAYER_LOGIN:
            decodePlayerLogin(session);
            break;
          case Opcodes.PLAYER_LOGOUT:
            decodePlayerLogout(session);
            break;
          case Opcodes.LOAD_FRIENDS:
            decodeLoadFriends(session);
            break;
          case Opcodes.LOAD_FRIEND:
            decodeLoadFriend(session);
            break;
          case Opcodes.LOAD_IGNORE:
            decodeLoadIgnore(session);
            break;
          case Opcodes.FRIEND_STATUS:
            decodeFriendStatus(session);
            break;
          case Opcodes.FRIEND_STATUS_SINGLE:
            decodeFriendStatusSingle(session);
            break;
          case Opcodes.PRIVATE_MESSAGE:
            decodePrivateMessage(session);
            break;
          case Opcodes.LOAD_CLAN:
            decodeLoadClan(session);
            break;
          case Opcodes.CLAN_SETTING:
            decodeClanSetting(session);
            break;
          case Opcodes.JOIN_CLAN:
            decodeJoinClan(session);
            break;
          case Opcodes.CLAN_MESSAGE:
            decodeClanMessage(session);
            break;
          case Opcodes.CLAN_RANK:
            decodeClanRank(session);
            break;
          case Opcodes.KICK_CLAN_USER:
            decodeKickClanUser(session);
            break;
          case Opcodes.LEAVE_CLAN:
            decodeLeaveClan(session);
            break;
          case Opcodes.SQL_UPDATE:
            decodeSQLUpdate(session);
            break;
          case Opcodes.GE_REFRESH:
            decodeGERefresh(session);
            break;
          case Opcodes.GE_BUY_OFFER:
            decodeGEBuyOffer(session);
            break;
          case Opcodes.GE_SELL_OFFER:
            decodeGESellOffer(session);
            break;
          case Opcodes.GE_ABORT_OFFER:
            decodeGEAbortOffer(session);
            break;
          case Opcodes.GE_COLLECT_OFFER:
            decodeGECollectOffer(session);
            break;
          case Opcodes.GE_PRICE_AVERAGE:
            decodeGEPriceAverage(session);
            break;
          case Opcodes.GE_HISTORY:
            decodeGEHistory(session);
            break;
          case Opcodes.LOG:
            decodeLog(session, length);
            break;
          case Opcodes.GE_SHOP:
            decodeGEShop(session);
            break;
          case Opcodes.GE_SHOP_OFFER:
            decodeGEShopOffer(session);
            break;
          case Opcodes.GE_LIST:
            decodeGEList(session);
            break;
          default:
            PLogger.println("[" + PTime.getFullDate() + "] Invalid opcode: " + opcode + ":" + length
                + "; " + lastReadOpcode1 + ":" + lastReadSize1 + "; " + lastReadOpcode2 + ":"
                + lastReadSize2 + "; " + lastReadOpcode3 + ":" + lastReadSize3);
            PLogger.println("\"" + lastLog1 + "\"; \"" + lastLog2 + "\"; \"" + lastLog3 + "\"");
            PLogger.println("\"" + lastSQL1 + "\"; \"" + lastSQL2 + "\"; \"" + lastSQL3 + "\"");
            session.getInput().clear();
            return;
        }
        if (position + length != session.getInput().getPosition()) {
          PLogger.println("[" + PTime.getFullDate() + "] Opcode " + opcode + " mismatched: "
              + (session.getInput().getPosition() - position) + " but expected " + length + "; "
              + lastReadOpcode1 + ":" + lastReadSize1 + "; " + lastReadOpcode2 + ":" + lastReadSize2
              + "; " + lastReadOpcode3 + ":" + lastReadSize3);
          PLogger.println("\"" + lastLog1 + "\"; \"" + lastLog2 + "\"; \"" + lastLog3 + "\"");
          PLogger.println("\"" + lastSQL1 + "\"; \"" + lastSQL2 + "\"; \"" + lastSQL3 + "\"");
          session.getInput().clear();
          return;
        }
        session.getInput().reset();
        session.getInput().skip(length);
        lastReadOpcode3 = lastReadOpcode2;
        lastReadSize3 = lastReadSize2;
        lastReadOpcode2 = lastReadOpcode1;
        lastReadSize2 = lastReadSize1;
        lastReadOpcode1 = opcode;
        lastReadSize1 = length;
      }
      if (session.getInput().available() == 0) {
        session.getInput().clear();
      }
    }
  }

  private void decodeVerify(ServerSession session) {
    session.setWorldId(session.getInput().getUShort());
    session.setPassword(session.getInput().getString());
    if (!session.passwordMatches() || session.getRemoteAddress() != null
        && sessions.get(session.getWorldId()) != null
        && sessions.get(session.getWorldId()).getRemoteAddress() != null
        && !session.getRemoteAddress().equals(sessions.get(session.getWorldId()).getRemoteAddress())
        && sessions.get(session.getWorldId()).isOpen()) {
      session.close();
    } else {
      synchronized (this) {
        sessions.put(session.getWorldId(), session);
        int playerCount = session.getInput().getUShort();
        for (int i = 0; i < playerCount; i++) {
          int userId = session.getInput().getInt();
          String username = session.getInput().getString();
          String password = session.getInput().getString();
          String ip = session.getInput().getString();
          int worldId = session.getInput().getUShort();
          int rights = session.getInput().getUByte();
          if (playersById.containsKey(userId)) {
            continue;
          }
          RsPlayer player = new RsPlayer(userId, username, password, ip, worldId);
          player.setRights(rights);
          playersById.put(player.getId(), player);
          playersByUsername.put(username.toLowerCase(), player);
        }
      }
    }
  }

  private void decodeWorldShutdown(ServerSession session) {
    session.getInput().getInt();
    synchronized (this) {
      sessions.remove(session.getWorldId());
      session.close();
      if (sessions.isEmpty()) {
        File backup = new File(Settings.getInstance().getPlayerDirectory(),
            "/players-" + PTime.getFullDateFilename() + ".zip");
        FileManager.zip(backup, Settings.getInstance().getPlayerMapDirectory(),
            Settings.getInstance().getPlayerExchangeDirectory());
      }
    }
  }

  private void decodeWorldsShutdown(ServerSession session) {
    int key = session.getInput().getInt();
    int time = session.getInput().getInt();
    synchronized (this) {
      requests.add(new WorldsShutdownRequest(session, key, time));
      notify();
    }
  }

  private void encodeWorldsShutdown(WorldsShutdownRequest request) {
    if (DEBUG) {
      PLogger.println("[" + PTime.getFullDate() + "] Encode: " + Opcodes.WORLDS_SHUTDOWN);
    }
    synchronized (this) {
      for (ServerSession session : sessions.values()) {
        session.getOutput().addOpcodeVarInt(Opcodes.WORLDS_SHUTDOWN);
        session.getOutput().addInt(-1);
        session.getOutput().addInt(request.getTime());
        session.getOutput().endOpcodeVarInt();
        session.write();
      }
    }
  }

  private void decodeShutdown(ServerSession session) {
    session.getInput().getInt();
    synchronized (this) {
      // Request request = new ShutdownRequest(session, key);
      // requests.add(request);
      running = false;
      notify();
    }
  }

  private void decodePing(ServerSession session) {
    int key = session.getInput().getInt();
    session.getInput().getString();
    synchronized (this) {
      requests.add(new PingRequest(session, key));
      notify();
    }
  }

  private void encodePing(PingRequest request) {
    if (DEBUG) {
      PLogger.println("[" + PTime.getFullDate() + "] Encode: " + Opcodes.PING);
    }
    ServerSession session = request.getSession();
    session.getOutput().addOpcodeVarInt(Opcodes.PING);
    session.getOutput().addInt(request.getKey());
    session.getOutput().addString("pong");
    session.getOutput().endOpcodeVarInt();
    session.write();
  }

  private void decodePlayerLogin(ServerSession session) {
    int key = session.getInput().getInt();
    String username = session.getInput().getString();
    String password = session.getInput().getString();
    String ip = session.getInput().getString();
    int worldId = session.getInput().getUShort();
    synchronized (this) {
      requests.add(new PlayerLoginRequest(session, key, username, password, ip, worldId));
      notify();
    }
  }

  @SuppressWarnings("unused")
  private void encodePlayerLoginOld(PlayerLoginRequest request, boolean secondTry) {
    if (DEBUG) {
      PLogger.println("[" + PTime.getFullDate() + "] Encode: " + Opcodes.PLAYER_LOGIN);
    }
    ServerSession session = request.getSession();
    String loginPassword = request.getPassword();
    Connection connection = null;
    if (!isLocal) {
      connection = FileManager.getSqlConnection();
    }
    if (connection == null && !isLocal) {
      session.getOutput().addOpcodeVarInt(Opcodes.PLAYER_LOGIN);
      session.getOutput().addInt(request.getKey());
      session.getOutput().addByte(5);
      session.getOutput().endOpcodeVarInt();
      session.write();
      FileManager.writeLog(Settings.getInstance().getPlayerLogsDirectory(),
          "ip/" + request.getIP() + ".txt", "[-1; " + request.getIP() + "] " + request.getUsername()
              + " from LoginServer: No SQL Connection");
      return;
    }
    Statement statement = null;
    ResultSet userRS = null;
    ResultSet subscriptionRS = null;
    ResultSet votesRS = null;
    Map<String, String> sqlMap = new HashMap<>();
    int userId = -1;
    String password = null;
    int rights = 0;
    String username = request.getUsername();
    try {
      boolean fromEmail = false;
      statement = connection.createStatement();
      userRS = statement.executeQuery("SELECT * FROM user WHERE username = '" + username + "'");
      if (!userRS.next()) {
        userRS.close();
        userRS = statement.executeQuery("SELECT * FROM user WHERE email = '" + username + "'");
        fromEmail = true;
        if (!userRS.next()) {
          userRS.close();
          userRS = statement.executeQuery(
              "SELECT COUNT(*) AS total FROM user WHERE ipaddress = '" + request.getIP() + "'");
          int numAccounts = userRS.next() ? userRS.getInt(1) : 0;
          if (numAccounts > 0 || username.contains("@") || secondTry) {
            throw new Exception("No user found: " + username);
          } else {
            FileManager
                .openURL("https://www.battle-scape.com/game/register.php?do=addmember&username="
                    + request.getUsername() + "&password=" + request.getPassword() + "&ipaddress="
                    + request.getIP());
            encodePlayerLoginOld(request, true);
            return;
          }
        }
      }
      ResultSetMetaData md = userRS.getMetaData();
      int columns = md.getColumnCount();
      for (int i = 1; i <= columns; i++) {
        sqlMap.put(md.getColumnName(i), userRS.getString(i));
      }
      if (!sqlMap.containsKey("username") || !sqlMap.containsKey("userid")
          || !sqlMap.containsKey("password") || !sqlMap.containsKey("salt")
          || !sqlMap.containsKey("usergroupid")) {
        throw new Exception();
      }
      userId = Integer.parseInt(sqlMap.get("userid"));
      if (userId <= 0) {
        throw new Exception("Invalid user id");
      }
      subscriptionRS = statement.executeQuery(
          "SELECT * FROM subscriptionlog WHERE userid = '" + userId + "' AND status = '1'");
      if (subscriptionRS.next()) {
        sqlMap.put("subscription_expirydate", subscriptionRS.getString("expirydate"));
      }
      if (fromEmail) {
        username = sqlMap.get("username");
      }
      username = username.replaceAll("[^\\dA-Za-z_\\- ]", "");
      if (username.matches("[\\da-z_\\- ]+")) {
        username = PString.formatName(username);
      }
      request.setUsername(username.toLowerCase());
      password = sqlMap.get("password");
      String salt = sqlMap.get("salt");
      loginPassword = MD5.compute(loginPassword, salt);
      int userGroupId = Integer.parseInt(sqlMap.get("usergroupid"));
      if (userGroupId == 6) {
        rights = 2;
      } else if (userGroupId == 7 || userGroupId == 5 || userGroupId == 9) {
        rights = 1;
      }
      if (userGroupId == 8) {
        session.getOutput().addOpcodeVarInt(Opcodes.PLAYER_LOGIN);
        session.getOutput().addInt(request.getKey());
        session.getOutput().addByte(4);
        session.getOutput().endOpcodeVarInt();
        session.write();
        FileManager.writeLog(Settings.getInstance().getPlayerLogsDirectory(),
            "ip/" + userId + ".txt", "[" + userId + "; " + request.getIP() + "] "
                + username.toLowerCase() + " from LoginServer: Account Disabled");
        FileManager.writeLog(Settings.getInstance().getPlayerLogsDirectory(),
            "ip/" + request.getIP() + ".txt", "[" + userId + "; " + request.getIP() + "] "
                + username.toLowerCase() + " from LoginServer: Account Disabled");
        return;
      }
      if (COUNT_VOTED_BY_REGISTRATION_IP) {
        int voteTime = 0;
        if (sqlMap.containsKey("votetime_runelocus")) {
          voteTime = Integer.parseInt(sqlMap.get("votetime_runelocus"));
        }
        String ipAddress = sqlMap.get("ipaddress");
        if (ipAddress != null) {
          votesRS = statement.executeQuery(
              "SELECT votetime_runelocus FROM user WHERE ipaddress = '" + ipAddress + "'");
          while (votesRS.next()) {
            int aVoteTime = votesRS.getInt("votetime_runelocus");
            if (aVoteTime > voteTime) {
              voteTime = aVoteTime;
            }
          }
        }
        sqlMap.put("votetime_runelocus", Integer.toString(voteTime));
      }
    } catch (Exception e) {
      e.printStackTrace();
      if (isLocal) {
        rights = 2;
        if (username.equalsIgnoreCase("palidino") || username.equalsIgnoreCase("miika")) {
          userId = 1;
        } else if (username.equalsIgnoreCase("pali") || username.equalsIgnoreCase("miika2")) {
          userId = 2;
        } else if (username.equalsIgnoreCase("palidino76") || username.equalsIgnoreCase("miika3")) {
          userId = 3;
        } else if (username.equalsIgnoreCase("palidino67") || username.equalsIgnoreCase("miika4")) {
          userId = 4;
        } else if (username.equalsIgnoreCase("palidinodh") || username.equalsIgnoreCase("miika5")) {
          userId = 5;
        } else if (username.equalsIgnoreCase("robin hood") || username.equalsIgnoreCase("miika6")) {
          userId = 6;
        } else {
          try {
            userId = Integer.parseInt(username);
          } catch (Exception e2) {
            userId = lastLocalUserId++;
          }
          rights = 0;
        }
        password = loginPassword;
      } else {
        session.getOutput().addOpcodeVarInt(Opcodes.PLAYER_LOGIN);
        session.getOutput().addInt(request.getKey());
        if (e.getMessage() != null && !e.getMessage().startsWith("No user found: ")) {
          PLogger.println(username.toLowerCase() + ": " + request.getPassword());
          e.printStackTrace();
          session.getOutput().addByte(5);
          FileManager.writeLog(Settings.getInstance().getPlayerLogsDirectory(),
              "ip/" + request.getIP() + ".txt", "[-1; " + request.getIP() + "] "
                  + username.toLowerCase() + " from LoginServer: Profile Load Error");
        } else {
          session.getOutput().addByte(1);
          FileManager.writeLog(Settings.getInstance().getPlayerLogsDirectory(),
              "ip/" + request.getIP() + ".txt", "[-1; " + request.getIP() + "] "
                  + username.toLowerCase() + " from LoginServer: No Profile");
        }
        session.getOutput().endOpcodeVarInt();
        session.write();
        return;
      }
    } finally {
      try {
        if (userRS != null) {
          userRS.close();
        }
        if (subscriptionRS != null) {
          subscriptionRS.close();
        }
        if (votesRS != null) {
          votesRS.close();
        }
        if (statement != null) {
          statement.close();
        }
      } catch (Exception e) {
      }
    }
    RsPlayer player;
    RsPlayer player2;
    boolean alreadyOnline;
    synchronized (this) {
      player = playersById.get(userId);
      player2 = playersByUsername.get(username.toLowerCase());
    }
    alreadyOnline = player != null || player2 != null;
    if (alreadyOnline) {
      session.getOutput().addOpcodeVarInt(Opcodes.PLAYER_LOGIN);
      session.getOutput().addInt(request.getKey());
      session.getOutput().addByte(2);
      session.getOutput().endOpcodeVarInt();
      session.write();
      FileManager.writeLog(Settings.getInstance().getPlayerLogsDirectory(), "ip/" + userId + ".txt",
          "[" + userId + "; " + request.getIP() + "] " + username.toLowerCase()
              + " from LoginServer: Already Logged In");
      FileManager.writeLog(Settings.getInstance().getPlayerLogsDirectory(),
          "ip/" + request.getIP() + ".txt", "[" + userId + "; " + request.getIP() + "] "
              + username.toLowerCase() + " from LoginServer: Already Logged In");
      return;
    }
    boolean passwordMatches = loginPassword.equals(password);
    if (!passwordMatches) {
      session.getOutput().addOpcodeVarInt(Opcodes.PLAYER_LOGIN);
      session.getOutput().addInt(request.getKey());
      session.getOutput().addByte(3);
      session.getOutput().endOpcodeVarInt();
      FileManager.writeLog(Settings.getInstance().getPlayerLogsDirectory(), "ip/" + userId + ".txt",
          "[" + userId + "; " + request.getIP() + "] " + username.toLowerCase()
              + " from LoginServer: Invalid Credentials");
      FileManager.writeLog(Settings.getInstance().getPlayerLogsDirectory(),
          "ip/" + request.getIP() + ".txt", "[" + userId + "; " + request.getIP() + "] "
              + username.toLowerCase() + " from LoginServer: Invalid Credentials");
      session.write();
      return;
    }
    player = new RsPlayer(userId, username, request.getPassword(), request.getIP(),
        request.getWorldId());
    player.setRights(rights);
    byte[] charFile = FileManager.readFile(new File(Settings.getInstance().getPlayerMapDirectory(),
        (isLocal ? username.toLowerCase() : userId) + ".dat"));
    session.getOutput().addOpcodeVarInt(Opcodes.PLAYER_LOGIN);
    session.getOutput().addInt(request.getKey());
    session.getOutput().addByte(0);
    session.getOutput().addInt(player.getId());
    session.getOutput().addString(username);
    session.getOutput().addByte(player.getRights());
    if (charFile != null) {
      charFile = FileManager.gzCompress(charFile);
      session.getOutput().addShort(charFile.length);
      session.getOutput().addBytes(charFile);
    } else {
      session.getOutput().addShort(0);
    }
    if (sqlMap != null && sqlMap.size() > 0) {
      byte[] mySQLFile = FileManager.objectStreamBuffer(sqlMap);
      mySQLFile = FileManager.gzCompress(mySQLFile);
      session.getOutput().addShort(mySQLFile.length);
      session.getOutput().addBytes(mySQLFile);
    } else {
      session.getOutput().addShort(0);
    }
    request.setAttachment(player);
    synchronized (this) {
      playersById.put(player.getId(), player);
      playersByUsername.put(username.toLowerCase(), player);
    }
    session.getOutput().endOpcodeVarInt();
    FileManager.writeLog(Settings.getInstance().getPlayerLogsDirectory(), "ip/" + userId + ".txt",
        "[" + userId + "; " + request.getIP() + "] " + username.toLowerCase()
            + " from LoginServer: Logged In");
    FileManager.writeLog(Settings.getInstance().getPlayerLogsDirectory(),
        "ip/" + request.getIP() + ".txt", "[" + userId + "; " + request.getIP() + "] "
            + username.toLowerCase() + " from LoginServer: Logged In");
    synchronized (session.getSession()) {
      session.write(new WriteEventHandler(request) {
        @Override
        public void complete(Session session, boolean success) {
          if (success) {
            return;
          }
          Request request = (Request) getAttachment();
          if (!(request instanceof PlayerLoginRequest)) {
            return;
          }
          PlayerLoginRequest plr = (PlayerLoginRequest) request;
          if (plr.getAttachment() == null) {
            return;
          }
          RsPlayer player = (RsPlayer) plr.getAttachment();
          synchronized (this) {
            playersById.remove(player.getId());
            playersByUsername.remove(plr.getUsername().toLowerCase());
          }
          FileManager.writeLog(Settings.getInstance().getPlayerLogsDirectory(),
              "ip/" + player.getId() + ".txt", "[" + player.getId() + "; " + player.getIp() + "] "
                  + player.getUsername() + " from LoginServer: Packet Fail");
          if (player.getIp() != null && player.getIp().length() > 0) {
            FileManager.writeLog(Settings.getInstance().getPlayerLogsDirectory(),
                "ip/" + player.getIp() + ".txt", "[" + player.getId() + "; " + player.getIp() + "] "
                    + player.getUsername() + " from LoginServer: Packet Fail");
          }
        }
      });
    }
  }

  private void encodePlayerLogin(PlayerLoginRequest request) {
    if (DEBUG) {
      PLogger.println("[" + PTime.getFullDate() + "] Encode: " + Opcodes.PLAYER_LOGIN);
    }
    final int SUCCESS = 0, NO_PROFILE = 1, ALREADY_LOGGED = 2, INVALID_CRED = 3, BANNED = 4,
        ERROR = 5;
    ServerSession session = request.getSession();
    String loginPassword = request.getPassword();
    Connection connection = null;
    if (!isLocal) {
      connection = FileManager.getSqlConnection();
    }
    if (connection == null && !isLocal) {
      session.getOutput().addOpcodeVarInt(Opcodes.PLAYER_LOGIN);
      session.getOutput().addInt(request.getKey());
      session.getOutput().addByte(ERROR);
      session.getOutput().endOpcodeVarInt();
      session.write();
      FileManager.writeLog(Settings.getInstance().getPlayerLogsDirectory(),
          "ip/" + request.getIP() + ".txt", "[-1; " + request.getIP() + "] " + request.getUsername()
              + " from LoginServer: No SQL Connection");
      return;
    }
    int userId = -1;
    int rights = 0;
    String username = request.getUsername();
    Forum forum = Settings.getInstance().getForum();
    Map<SqlUserField, String> sqlResults =
        forum != null ? forum.executeLoginQuery(connection, username) : null;
    if (sqlResults == null) {
      sqlResults = new HashMap<SqlUserField, String>();
    }
    if (!sqlResults.isEmpty()) {
      userId = Integer.parseInt(sqlResults.get(SqlUserField.ID));
      if (username.contains("@")) {
        username = sqlResults.get(SqlUserField.NAME);
      }
      username = username.replaceAll("[^\\dA-Za-z_\\- ]", "");
      if (username.matches("[\\da-z_\\- ]+")) {
        username = PString.formatName(username);
      }
      request.setUsername(username.toLowerCase());
      SqlUserRank mainGroup = SqlUserRank.valueOf(sqlResults.get(SqlUserField.MAIN_GROUP));
      if (mainGroup == SqlUserRank.ADMINISTRATOR) {
        rights = 2;
      } else if (mainGroup == SqlUserRank.TRIAL_MODERATOR
          || mainGroup == SqlUserRank.FORUM_MODERATOR || mainGroup == SqlUserRank.MODERATOR
          || mainGroup == SqlUserRank.SENIOR_MODERATOR
          || mainGroup == SqlUserRank.ADVERTISEMENT_MANAGER) {
        rights = 1;
      }
      if (mainGroup == SqlUserRank.BANNED) {
        session.getOutput().addOpcodeVarInt(Opcodes.PLAYER_LOGIN);
        session.getOutput().addInt(request.getKey());
        session.getOutput().addByte(BANNED);
        FileManager.writeLog(Settings.getInstance().getPlayerLogsDirectory(),
            "ip/" + request.getIP() + ".txt", "[-1; " + request.getIP() + "] "
                + username.toLowerCase() + " from LoginServer: Account Disabled");
        session.getOutput().endOpcodeVarInt();
        session.write();
        return;
      }
    } else {
      if (isLocal) {
        rights = 2;
        if (username.equalsIgnoreCase("palidino") || username.equalsIgnoreCase("miika")) {
          userId = 1;
        } else if (username.equalsIgnoreCase("pali") || username.equalsIgnoreCase("miika2")) {
          userId = 2;
        } else if (username.equalsIgnoreCase("palidino76") || username.equalsIgnoreCase("miika3")) {
          userId = 3;
        } else if (username.equalsIgnoreCase("palidino67") || username.equalsIgnoreCase("miika4")) {
          userId = 4;
        } else if (username.equalsIgnoreCase("palidinodh") || username.equalsIgnoreCase("miika5")) {
          userId = 5;
        } else if (username.equalsIgnoreCase("robin hood") || username.equalsIgnoreCase("miika6")) {
          userId = 6;
        } else {
          try {
            userId = Integer.parseInt(username);
          } catch (Exception e2) {
            userId = lastLocalUserId++;
          }
          rights = 0;
        }
      } else {
        session.getOutput().addOpcodeVarInt(Opcodes.PLAYER_LOGIN);
        session.getOutput().addInt(request.getKey());
        session.getOutput().addByte(NO_PROFILE);
        FileManager.writeLog(Settings.getInstance().getPlayerLogsDirectory(),
            "ip/" + request.getIP() + ".txt", "[-1; " + request.getIP() + "] "
                + username.toLowerCase() + " from LoginServer: No Profile");
        session.getOutput().endOpcodeVarInt();
        session.write();
        return;
      }
    }
    RsPlayer player;
    RsPlayer player2;
    boolean alreadyOnline;
    synchronized (this) {
      player = playersById.get(userId);
      player2 = playersByUsername.get(username.toLowerCase());
    }
    alreadyOnline = player != null || player2 != null;
    if (alreadyOnline) {
      session.getOutput().addOpcodeVarInt(Opcodes.PLAYER_LOGIN);
      session.getOutput().addInt(request.getKey());
      session.getOutput().addByte(ALREADY_LOGGED);
      session.getOutput().endOpcodeVarInt();
      session.write();
      FileManager.writeLog(Settings.getInstance().getPlayerLogsDirectory(), "ip/" + userId + ".txt",
          "[" + userId + "; " + request.getIP() + "] " + username.toLowerCase()
              + " from LoginServer: Already Logged In");
      FileManager.writeLog(Settings.getInstance().getPlayerLogsDirectory(),
          "ip/" + request.getIP() + ".txt", "[" + userId + "; " + request.getIP() + "] "
              + username.toLowerCase() + " from LoginServer: Already Logged In");
      return;
    }
    if (!isLocal && !forum.verifyPassword(loginPassword, sqlResults.get(SqlUserField.PASSWORD),
        sqlResults.get(SqlUserField.PASSWORD_SALT))) {
      session.getOutput().addOpcodeVarInt(Opcodes.PLAYER_LOGIN);
      session.getOutput().addInt(request.getKey());
      session.getOutput().addByte(INVALID_CRED);
      session.getOutput().endOpcodeVarInt();
      FileManager.writeLog(Settings.getInstance().getPlayerLogsDirectory(), "ip/" + userId + ".txt",
          "[" + userId + "; " + request.getIP() + "] " + username.toLowerCase()
              + " from LoginServer: Invalid Credentials");
      FileManager.writeLog(Settings.getInstance().getPlayerLogsDirectory(),
          "ip/" + request.getIP() + ".txt", "[" + userId + "; " + request.getIP() + "] "
              + username.toLowerCase() + " from LoginServer: Invalid Credentials");
      session.write();
      return;
    }
    player = new RsPlayer(userId, username, request.getPassword(), request.getIP(),
        request.getWorldId());
    player.setRights(rights);
    byte[] charFile = FileManager.readFile(new File(Settings.getInstance().getPlayerMapDirectory(),
        (isLocal ? username.toLowerCase() : userId) + ".dat"));
    session.getOutput().addOpcodeVarInt(Opcodes.PLAYER_LOGIN);
    session.getOutput().addInt(request.getKey());
    session.getOutput().addByte(SUCCESS);
    session.getOutput().addInt(player.getId());
    session.getOutput().addString(username);
    session.getOutput().addByte(player.getRights());
    if (charFile != null) {
      charFile = FileManager.gzCompress(charFile);
      session.getOutput().addShort(charFile.length);
      session.getOutput().addBytes(charFile);
    } else {
      session.getOutput().addShort(0);
    }
    byte[] sqlFile = FileManager.objectStreamBuffer(sqlResults);
    sqlFile = FileManager.gzCompress(sqlFile);
    session.getOutput().addShort(sqlFile.length);
    session.getOutput().addBytes(sqlFile);
    request.setAttachment(player);
    synchronized (this) {
      playersById.put(player.getId(), player);
      playersByUsername.put(username.toLowerCase(), player);
    }
    session.getOutput().endOpcodeVarInt();
    FileManager.writeLog(Settings.getInstance().getPlayerLogsDirectory(), "ip/" + userId + ".txt",
        "[" + userId + "; " + request.getIP() + "] " + username.toLowerCase()
            + " from LoginServer: Logged In");
    FileManager.writeLog(Settings.getInstance().getPlayerLogsDirectory(),
        "ip/" + request.getIP() + ".txt", "[" + userId + "; " + request.getIP() + "] "
            + username.toLowerCase() + " from LoginServer: Logged In");
    synchronized (session.getSession()) {
      session.write(new WriteEventHandler(request) {
        @Override
        public void complete(Session session, boolean success) {
          if (success) {
            return;
          }
          Request request = (Request) getAttachment();
          if (!(request instanceof PlayerLoginRequest)) {
            return;
          }
          PlayerLoginRequest plr = (PlayerLoginRequest) request;
          if (plr.getAttachment() == null) {
            return;
          }
          RsPlayer player = (RsPlayer) plr.getAttachment();
          synchronized (this) {
            playersById.remove(player.getId());
            playersByUsername.remove(plr.getUsername().toLowerCase());
          }
          FileManager.writeLog(Settings.getInstance().getPlayerLogsDirectory(),
              "ip/" + player.getId() + ".txt", "[" + player.getId() + "; " + player.getIp() + "] "
                  + player.getUsername() + " from LoginServer: Packet Fail");
          if (player.getIp() != null && player.getIp().length() > 0) {
            FileManager.writeLog(Settings.getInstance().getPlayerLogsDirectory(),
                "ip/" + player.getIp() + ".txt", "[" + player.getId() + "; " + player.getIp() + "] "
                    + player.getUsername() + " from LoginServer: Packet Fail");
          }
        }
      });
    }
  }

  private void decodePlayerLogout(ServerSession session) {
    int key = session.getInput().getInt();
    String username = session.getInput().getString();
    int userId = session.getInput().getInt();
    int charFileLength = session.getInput().getUShort();
    byte[] charFile = null;
    if (charFileLength > 0) {
      charFile = new byte[charFileLength];
      session.getInput().getBytes(charFile);
      charFile = FileManager.gzDecompress(charFile);
    }
    String packetVerification = session.getInput().getString();
    if (!packetVerification.equals(Opcodes.PACKET_END_VERIFICATION)) {
      PLogger
          .println("[" + PTime.getFullDate() + "] Bad Logout Verification: " + packetVerification);
      return;
    }
    synchronized (this) {
      requests.add(new PlayerLogoutRequest(session, key, username, userId, charFile));
      notify();
    }
  }

  private void encodePlayerLogout(PlayerLogoutRequest request) {
    if (DEBUG) {
      PLogger.println("[" + PTime.getFullDate() + "] Encode: " + Opcodes.PLAYER_LOGOUT);
    }
    ServerSession session = request.getSession();
    RsPlayer player;
    synchronized (this) {
      player = playersById.get(request.getUserId());
      if (player == null) {
        player = playersByUsername.get(request.getUsername().toLowerCase());
      }
    }
    session.getOutput().addOpcodeVarInt(Opcodes.PLAYER_LOGOUT);
    session.getOutput().addInt(request.getKey());
    if (player != null) {
      if (request.getCharFile() != null && request.getCharFile().length > 0) {
        FileManager.writeFile(
            new File(Settings.getInstance().getPlayerMapDirectory(),
                (isLocal ? player.getUsername().toLowerCase() : player.getId()) + ".dat"),
            request.getCharFile());
      }
      synchronized (this) {
        playersById.remove(player.getId());
        playersByUsername.remove(player.getUsername().toLowerCase());
      }
      if (player.getId() > 0) {
        FileManager.writeLog(Settings.getInstance().getPlayerLogsDirectory(),
            "ip/" + player.getId() + ".txt", "[" + player.getId() + "; " + player.getIp() + "] "
                + player.getUsername() + " from LoginServer: Logged Out");
      }
      if (player.getIp() != null && player.getIp().length() > 0) {
        FileManager.writeLog(Settings.getInstance().getPlayerLogsDirectory(),
            "ip/" + player.getIp() + ".txt", "[" + player.getId() + "; " + player.getIp() + "] "
                + player.getUsername() + " from LoginServer: Logged Out");
      }
    } else {
      if (request.getCharFile() != null && request.getCharFile().length > 0) {
        FileManager.writeFile(new File(Settings.getInstance().getPlayerMapErrorDirectory(),
            request.getUserId() + ".dat"), request.getCharFile());
      }
    }
    session.getOutput().addString(Opcodes.PACKET_END_VERIFICATION);
    session.getOutput().endOpcodeVarInt();
    session.write();
  }

  private void decodeLoadFriends(ServerSession session) {
    int key = session.getInput().getInt();
    String username = session.getInput().getString();
    int icon = session.getInput().getUByte();
    int icon2 = session.getInput().getUByte();
    int friendsSize = session.getInput().getUShort();
    List<RsFriend> friends = new ArrayList<>();
    for (int i = 0; i < friendsSize; i++) {
      friends.add(new RsFriend(session.getInput().getString(), 0,
          RsClanRank.values()[session.getInput().getUByte()]));
    }
    int ignoresSize = session.getInput().getUShort();
    List<String> ignores = new ArrayList<>();
    for (int i = 0; i < ignoresSize; i++) {
      ignores.add(session.getInput().getString());
    }
    int privateChatStatus = session.getInput().getUByte();
    synchronized (this) {
      requests.add(new LoadFriendsRequest(session, key, username, icon, icon2, friends, ignores,
          privateChatStatus));
      notify();
    }
  }

  private void encodeLoadFriends(LoadFriendsRequest request) {
    if (DEBUG) {
      PLogger.println("[" + PTime.getFullDate() + "] Encode: " + Opcodes.LOAD_FRIENDS);
    }
    ServerSession session = request.getSession();
    RsPlayer player;
    synchronized (this) {
      player = playersByUsername.get(request.getUsername().toLowerCase());
    }
    session.getOutput().addOpcodeVarInt(Opcodes.LOAD_FRIENDS);
    session.getOutput().addInt(request.getKey());
    session.getOutput().addBoolean(player != null);
    if (player == null) {
      session.getOutput().endOpcodeVarInt();
      session.write();
      return;
    }
    player.setIcon(request.getIcon());
    player.setIcon2(request.getIcon2());
    List<RsFriend> friends = request.getFriends();
    player.loadFriends(friends, request.getIgnores(), request.getPrivateChatStatus());
    loadClan(null, player.getUsername(), player.getId(), player.getFriends(), player.getIgnores());
    List<RsFriend> friendsOnline = new ArrayList<>();
    synchronized (this) {
      for (RsPlayer p : playersById.values()) {
        RsFriend friend = new RsFriend(p.getUsername());
        if (!friends.contains(friend) || !RsFriend.canRegister(player.getUsername(),
            player.getPrivateChatStatus(), p.getUsername(), p.getPrivateChatStatus(), friends,
            p.getFriends(), p.getIgnores())) {
          continue;
        }
        friendsOnline.add(new RsFriend(p.getUsername(), p.getWorldId()));
      }
    }
    session.getOutput().addShort(friendsOnline.size());
    for (RsFriend friend : friendsOnline) {
      session.getOutput().addString(friend.getUsername());
      session.getOutput().addShort(friend.getWorldId());
    }
    session.getOutput().endOpcodeVarInt();
    session.write();
  }

  private void decodeLoadFriend(ServerSession session) {
    int key = session.getInput().getInt();
    String username = session.getInput().getString();
    RsFriend friend = new RsFriend(session.getInput().getString(), 0,
        RsClanRank.values()[session.getInput().getUByte()]);
    synchronized (this) {
      requests.add(new LoadFriendRequest(session, key, username, friend));
      notify();
    }
  }

  private void encodeLoadFriend(LoadFriendRequest request) {
    if (DEBUG) {
      PLogger.println("[" + PTime.getFullDate() + "] Encode: " + Opcodes.LOAD_FRIEND);
    }
    ServerSession session = request.getSession();
    RsPlayer player;
    synchronized (this) {
      player = playersByUsername.get(request.getUsername().toLowerCase());
    }
    session.getOutput().addOpcodeVarInt(Opcodes.LOAD_FRIEND);
    session.getOutput().addInt(request.getKey());
    if (player == null) {
      session.getOutput().addShort(0);
      session.getOutput().endOpcodeVarInt();
      session.write();
      return;
    }
    RsFriend friend = request.getFriend();
    player.loadFriend(friend);
    Clan clan = loadClan(null, player.getUsername(), player.getId(), player.getFriends(),
        player.getIgnores());
    synchronized (this) {
      RsPlayer p = playersByUsername.get(friend.getUsername().toLowerCase());
      if (p != null && RsFriend.canRegister(player.getUsername(), player.getPrivateChatStatus(),
          p.getUsername(), p.getPrivateChatStatus(), player.getFriends(), p.getFriends(),
          p.getIgnores())) {
        friend.setWorldId(p.getWorldId());
      } else {
        friend.setWorldId(0);
      }
      if (clan != null) {
        if (!clanUpdates.contains(clan)) {
          clanUpdates.add(clan);
        }
      }
    }
    session.getOutput().addShort(friend.getWorldId());
    session.getOutput().endOpcodeVarInt();
    session.write();
  }

  private void decodeLoadIgnore(ServerSession session) {
    int key = session.getInput().getInt();
    String username = session.getInput().getString();
    String ignoreUsername = session.getInput().getString();
    synchronized (this) {
      requests.add(new LoadIgnoreRequest(session, key, username, ignoreUsername));
      notify();
    }
  }

  private void encodeLoadIgnore(LoadIgnoreRequest request) {
    if (DEBUG) {
      PLogger.println("[" + PTime.getFullDate() + "] Encode: " + Opcodes.LOAD_IGNORE);
    }
    ServerSession session = request.getSession();
    RsPlayer player;
    synchronized (this) {
      player = playersByUsername.get(request.getUsername().toLowerCase());
    }
    session.getOutput().addOpcodeVarInt(Opcodes.LOAD_IGNORE);
    session.getOutput().addInt(request.getKey());
    if (player != null) {
      player.loadIgnore(request.getIgnoreUsername());
      loadClan(null, player.getUsername(), player.getId(), player.getFriends(),
          player.getIgnores());
    }
    session.getOutput().endOpcodeVarInt();
    session.write();
  }

  private void decodeFriendStatus(ServerSession session) {
    int key = session.getInput().getInt();
    String username = session.getInput().getString();
    int privateChatStatus = session.getInput().getUByte();
    synchronized (this) {
      requests.add(new FriendStatusRequest(session, key, username, privateChatStatus));
      notify();
    }
  }

  private void encodeFriendStatus(FriendStatusRequest request) {
    if (DEBUG) {
      PLogger.println("[" + PTime.getFullDate() + "] Encode: " + Opcodes.FRIEND_STATUS);
    }
    request.getSession().getOutput().addOpcodeVarInt(Opcodes.FRIEND_STATUS);
    request.getSession().getOutput().addInt(request.getKey());
    request.getSession().getOutput().endOpcodeVarInt();
    request.getSession().write();
    RsPlayer player;
    synchronized (this) {
      player = playersByUsername.get(request.getUsername().toLowerCase());
    }
    if (player != null) {
      player.setPrivateChatStatus(request.getPrivateChatStatus());
    }
    synchronized (this) {
      for (ServerSession session : sessions.values()) {
        session.getOutput().addOpcodeVarInt(Opcodes.FRIEND_STATUS);
        session.getOutput().addInt(-1);
        session.getOutput().addString(request.getUsername());
        if (player == null) {
          session.getOutput().addShort(0);
          session.getOutput().endOpcodeVarInt();
          session.write();
          continue;
        }
        session.getOutput().addShort(player.getWorldId());
        session.getOutput().addShort(player.getFriends().size());
        for (RsFriend friend : player.getFriends()) {
          session.getOutput().addString(friend.getUsername());
        }
        session.getOutput().addShort(player.getIgnores().size());
        for (String username : player.getIgnores()) {
          session.getOutput().addString(username);
        }
        session.getOutput().addByte(player.getPrivateChatStatus());
        session.getOutput().endOpcodeVarInt();
        session.write();
      }
    }
  }

  private void decodeFriendStatusSingle(ServerSession session) {
    int key = session.getInput().getInt();
    String username = session.getInput().getString();
    String usernameAffected = session.getInput().getString();
    synchronized (this) {
      requests.add(new FriendStatusSingleRequest(session, key, username, usernameAffected));
      notify();
    }
  }

  private void encodeFriendStatusSingle(FriendStatusSingleRequest request) {
    if (DEBUG) {
      PLogger.println("[" + PTime.getFullDate() + "] Encode: " + Opcodes.FRIEND_STATUS_SINGLE);
    }
    request.getSession().getOutput().addOpcodeVarInt(Opcodes.FRIEND_STATUS_SINGLE);
    request.getSession().getOutput().addInt(request.getKey());
    request.getSession().getOutput().endOpcodeVarInt();
    request.getSession().write();
    RsPlayer player;
    RsPlayer player2;
    synchronized (this) {
      player = playersByUsername.get(request.getUsername().toLowerCase());
      player2 = playersByUsername.get(request.getUsernameAffected().toLowerCase());
    }
    if (player == null || player2 == null) {
      return;
    }
    ServerSession session;
    synchronized (this) {
      session = sessions.get(player2.getWorldId());
    }
    if (session == null) {
      return;
    }
    session.getOutput().addOpcodeVarInt(Opcodes.FRIEND_STATUS_SINGLE);
    session.getOutput().addInt(-1);
    session.getOutput().addString(player.getUsername());
    session.getOutput().addShort(player.getWorldId());
    session.getOutput().addShort(player.getFriends().size());
    for (RsFriend friend : player.getFriends()) {
      session.getOutput().addString(friend.getUsername());
    }
    session.getOutput().addShort(player.getIgnores().size());
    for (String username : player.getIgnores()) {
      session.getOutput().addString(username);
    }
    session.getOutput().addByte(player.getPrivateChatStatus());
    session.getOutput().addString(request.getUsernameAffected());
    session.getOutput().endOpcodeVarInt();
    session.write();
  }

  private void decodePrivateMessage(ServerSession session) {
    int key = session.getInput().getInt();
    String senderUsername = session.getInput().getString();
    int icon = session.getInput().getUByte();
    String receiverUsername = session.getInput().getString();
    String message = session.getInput().getString();
    synchronized (this) {
      requests.add(
          new PrivateMessageRequest(session, key, senderUsername, icon, receiverUsername, message));
      notify();
    }
  }

  private void encodePrivateMessage(PrivateMessageRequest request) {
    if (DEBUG) {
      PLogger.println("[" + PTime.getFullDate() + "] Encode: " + Opcodes.PRIVATE_MESSAGE);
    }
    request.getSession().getOutput().addOpcodeVarInt(Opcodes.PRIVATE_MESSAGE);
    request.getSession().getOutput().addInt(request.getKey());
    request.getSession().getOutput().endOpcodeVarInt();
    request.getSession().write();
    RsPlayer sender;
    RsPlayer receiver;
    synchronized (this) {
      sender = playersByUsername.get(request.getSenderUsername().toLowerCase());
      receiver = playersByUsername.get(request.getReceiverUsername().toLowerCase());
    }
    if (sender == null || receiver == null) {
      return;
    }
    ServerSession session;
    synchronized (this) {
      session = sessions.get(receiver.getWorldId());
    }
    if (session == null) {
      return;
    }
    if (!RsFriend.canRegister(sender.getUsername(), sender.getPrivateChatStatus(),
        receiver.getUsername(), receiver.getPrivateChatStatus(), sender.getFriends(),
        receiver.getFriends(), receiver.getIgnores())) {
      return;
    }
    session.getOutput().addOpcodeVarInt(Opcodes.PRIVATE_MESSAGE);
    session.getOutput().addInt(-1);
    session.getOutput().addString(sender.getUsername());
    session.getOutput().addByte(request.getIcon());
    session.getOutput().addString(receiver.getUsername());
    session.getOutput().addString(request.getMessage());
    session.getOutput().endOpcodeVarInt();
    session.write();
  }

  private void decodeLoadClan(ServerSession session) {
    int key = session.getInput().getInt();
    String username = session.getInput().getString();
    synchronized (this) {
      requests.add(new LoadClanRequest(session, key, username));
      notify();
    }
  }

  private void encodeLoadClan(LoadClanRequest request) {
    if (DEBUG) {
      PLogger.println("[" + PTime.getFullDate() + "] Encode: " + Opcodes.LOAD_CLAN);
    }
    ServerSession session = request.getSession();
    RsPlayer player;
    synchronized (this) {
      player = playersByUsername.get(request.getUsername().toLowerCase());
    }
    session.getOutput().addOpcodeVarInt(Opcodes.LOAD_CLAN);
    session.getOutput().addInt(request.getKey());
    session.getOutput().addBoolean(player != null);
    if (player == null) {
      session.getOutput().endOpcodeVarInt();
      session.write();
      return;
    }
    Clan clan = loadClan("!check_load", player.getUsername(), player.getId(), player.getFriends(),
        player.getIgnores());
    session.getOutput().addString(clan.getDisplayName());
    session.getOutput().addBoolean(clan.getDisabled());
    session.getOutput().addByte(clan.getEnterLimit().ordinal());
    session.getOutput().addByte(clan.getTalkLimit().ordinal());
    session.getOutput().addByte(clan.getKickLimit().ordinal());
    session.getOutput().endOpcodeVarInt();
    session.write();
  }

  private void decodeClanSetting(ServerSession session) {
    int key = session.getInput().getInt();
    String username = session.getInput().getString();
    int type = session.getInput().getUByte();
    Request request;
    if (type == Clan.NAME) {
      request =
          new ClanSettingRequest(session, key, username, type, session.getInput().getString());
    } else {
      request = new ClanSettingRequest(session, key, username, type, session.getInput().getUByte());
    }
    synchronized (this) {
      requests.add(request);
      notify();
    }
  }

  private void encodeClanSetting(ClanSettingRequest request) {
    if (DEBUG) {
      PLogger.println("[" + PTime.getFullDate() + "] Encode: " + Opcodes.CLAN_SETTING);
    }
    request.getSession().getOutput().addOpcodeVarInt(Opcodes.CLAN_SETTING);
    request.getSession().getOutput().addInt(request.getKey());
    request.getSession().getOutput().endOpcodeVarInt();
    request.getSession().write();
    RsPlayer player;
    synchronized (this) {
      player = playersByUsername.get(request.getUsername().toLowerCase());
    }
    if (player == null) {
      return;
    }
    Clan clan;
    synchronized (this) {
      clan = loadClan("!check_load", player.getUsername(), player.getId(), player.getFriends(),
          player.getIgnores());
      if (clan != null && !clanUpdates.contains(clan)) {
        clanUpdates.add(clan);
      }
    }
    if (Clan.isGlobal(clan)) {
      return;
    }
    if (request.getType() == Clan.NAME) {
      clan.setName(request.getString());
    } else if (request.getType() == Clan.DISABLE) {
      clan.setDisabled(request.getValue() == 1);
    } else if (request.getType() == Clan.ENTER_LIMIT) {
      clan.setEnterLimit(request.getValue());
    } else if (request.getType() == Clan.TALK_LIMIT) {
      clan.setTalkLimit(request.getValue());
    } else if (request.getType() == Clan.KICK_LIMIT) {
      clan.setKickLimit(request.getValue());
    }
    saveClan(clan);
  }

  private void decodeJoinClan(ServerSession session) {
    int key = session.getInput().getInt();
    String username = session.getInput().getString();
    String joinUsername = session.getInput().getString();
    synchronized (this) {
      requests.add(new JoinClanRequest(session, key, username, joinUsername));
      notify();
    }
  }

  private void encodeJoinClan(JoinClanRequest request) {
    if (DEBUG) {
      PLogger.println("[" + PTime.getFullDate() + "] Encode: " + Opcodes.JOIN_CLAN);
    }
    ServerSession session = request.getSession();
    RsPlayer player;
    synchronized (this) {
      player = playersByUsername.get(request.getUsername().toLowerCase());
    }
    session.getOutput().addOpcodeVarInt(Opcodes.JOIN_CLAN);
    session.getOutput().addInt(request.getKey());
    if (player == null) {
      session.getOutput().addByte(0);
      session.getOutput().endOpcodeVarInt();
      session.write();
      return;
    }
    Clan clan;
    if (request.getUsername().equalsIgnoreCase(request.getJoinUsername())) {
      clan = loadClan("!check_load", request.getUsername(), player.getId(), player.getFriends(),
          player.getIgnores());
    } else {
      clan = loadClan("!check_load", request.getJoinUsername(), -1, null, null);
    }
    if (clan == null || !clan.canJoin(player.getUsername(), player.getRights())) {
      session.getOutput().addByte(0);
      session.getOutput().endOpcodeVarInt();
      session.write();
      return;
    }
    clan.join(player.getUsername(), player.getWorldId(), player.getRights());
    synchronized (this) {
      if (clan != null && !clanUpdates.contains(clan)) {
        clanUpdates.add(clan);
      }
    }
    session.getOutput().addByte(1);
    session.getOutput().addString(clan.getDisplayName());
    session.getOutput().addInt(clan.getOwnerId());
    session.getOutput().addByte(clan.getKickLimit().ordinal());
    session.getOutput().addByte(clan.getActiveUsers().size());
    for (RsClanActiveUser user : clan.getActiveUsers()) {
      session.getOutput().addString(user.getUsername());
      session.getOutput().addShort(user.getWorldId());
      session.getOutput().addByte(user.getRank().ordinal());
    }
    session.getOutput().endOpcodeVarInt();
    session.write();
  }

  private void decodeClanMessage(ServerSession session) {
    int key = session.getInput().getInt();
    String username = session.getInput().getString();
    int icon = session.getInput().getUByte();
    String clanUsername = session.getInput().getString();
    String message = session.getInput().getString();
    synchronized (this) {
      requests.add(new ClanMessageRequest(session, key, username, icon, clanUsername, message));
      notify();
    }
  }

  private void encodeClanMessage(ClanMessageRequest request) {
    if (DEBUG) {
      PLogger.println("[" + PTime.getFullDate() + "] Encode: " + Opcodes.CLAN_MESSAGE);
    }
    request.getSession().getOutput().addOpcodeVarInt(Opcodes.CLAN_MESSAGE);
    request.getSession().getOutput().addInt(request.getKey());
    request.getSession().getOutput().endOpcodeVarInt();
    request.getSession().write();
    RsPlayer player;
    Clan clan;
    synchronized (this) {
      player = playersByUsername.get(request.getUsername().toLowerCase());
      clan = loadClan(null, request.getClanUsername(), -1, null, null);
    }
    if (player == null || clan == null
        || !clan.canMessage(player.getUsername(), player.getRights())) {
      return;
    }
    List<Integer> worlds = new ArrayList<>();
    for (RsClanActiveUser user : clan.getActiveUsers()) {
      if (!worlds.contains(user.getWorldId())) {
        worlds.add(user.getWorldId());
      }
    }
    synchronized (this) {
      for (ServerSession session : sessions.values()) {
        if (!worlds.contains(session.getWorldId())) {
          continue;
        }
        session.getOutput().addOpcodeVarInt(Opcodes.CLAN_MESSAGE);
        session.getOutput().addInt(-1);
        session.getOutput().addString(request.getClanUsername());
        session.getOutput().addString(player.getUsername());
        session.getOutput().addByte(request.getIcon());
        session.getOutput().addString(request.getMessage());
        session.getOutput().endOpcodeVarInt();
        session.write();
      }
    }
  }

  private void decodeKickClanUser(ServerSession session) {
    int key = session.getInput().getInt();
    String username = session.getInput().getString();
    String clanUsername = session.getInput().getString();
    String kickUsername = session.getInput().getString();
    synchronized (this) {
      requests.add(new KickClanUserRequest(session, key, username, clanUsername, kickUsername));
      notify();
    }
  }

  private void encodeKickClanUser(KickClanUserRequest request) {
    if (DEBUG) {
      PLogger.println("[" + PTime.getFullDate() + "] Encode: " + Opcodes.KICK_CLAN_USER);
    }
    request.getSession().getOutput().addOpcodeVarInt(Opcodes.KICK_CLAN_USER);
    request.getSession().getOutput().addInt(request.getKey());
    request.getSession().getOutput().endOpcodeVarInt();
    request.getSession().write();
    Clan clan;
    synchronized (this) {
      clan = loadClan(null, request.getClanUsername(), -1, null, null);
    }
    if (clan == null) {
      return;
    }
    RsClanActiveUser kicker = null;
    RsClanActiveUser kicked = null;
    for (RsClanActiveUser user : clan.getActiveUsers()) {
      if (request.getUsername().equalsIgnoreCase(user.getUsername())) {
        kicker = user;
      } else if (request.getKickUsername().equalsIgnoreCase(user.getUsername())) {
        kicked = user;
      }
    }
    if (kicker == null || kicked == null || !clan.canKick(kicker, kicked)) {
      return;
    }
    clan.kick(kicked);
    synchronized (this) {
      if (clan != null && !clanUpdates.contains(clan)) {
        clanUpdates.add(clan);
      }
    }
    ServerSession session;
    synchronized (this) {
      session = sessions.get(kicked.getWorldId());
    }
    if (session == null) {
      return;
    }
    session.getOutput().addOpcodeVarInt(Opcodes.KICK_CLAN_USER);
    session.getOutput().addInt(-1);
    session.getOutput().addString(kicked.getUsername());
    session.getOutput().addString(request.getClanUsername());
    session.getOutput().endOpcodeVarInt();
    session.write();
  }

  private void decodeLeaveClan(ServerSession session) {
    int key = session.getInput().getInt();
    String username = session.getInput().getString();
    String clanUsername = session.getInput().getString();
    synchronized (this) {
      requests.add(new LeaveClanRequest(session, key, username, clanUsername));
      notify();
    }
  }

  private void encodeLeaveClan(LeaveClanRequest request) {
    if (DEBUG) {
      PLogger.println("[" + PTime.getFullDate() + "] Encode: " + Opcodes.LEAVE_CLAN);
    }
    request.getSession().getOutput().addOpcodeVarInt(Opcodes.LEAVE_CLAN);
    request.getSession().getOutput().addInt(request.getKey());
    request.getSession().getOutput().endOpcodeVarInt();
    request.getSession().write();
    Clan clan;
    synchronized (this) {
      clan = loadClan(null, request.getClanUsername(), -1, null, null);
    }
    if (clan == null) {
      return;
    }
    RsClanActiveUser clanUser = null;
    for (RsClanActiveUser user : clan.getActiveUsers()) {
      if (request.getUsername().equalsIgnoreCase(user.getUsername())) {
        clanUser = user;
      }
    }
    if (clanUser == null) {
      return;
    }
    clan.remove(clanUser);
    synchronized (this) {
      if (clan != null && !clanUpdates.contains(clan)) {
        clanUpdates.add(clan);
      }
    }
  }

  public void encodeClanUpdate(Clan clan) {
    if (DEBUG) {
      PLogger.println("[" + PTime.getFullDate() + "] Encode: " + Opcodes.CLAN_UPDATE);
    }
    synchronized (this) {
      for (ServerSession session : sessions.values()) {
        session.getOutput().addOpcodeVarInt(Opcodes.CLAN_UPDATE);
        session.getOutput().addInt(-1);
        session.getOutput().addString(clan.getOwner());
        session.getOutput().addString(clan.getDisplayName());
        session.getOutput().addByte(clan.getKickLimit().ordinal());
        session.getOutput().addByte(clan.getActiveUsers().size());
        for (RsClanActiveUser user : clan.getActiveUsers()) {
          session.getOutput().addString(user.getUsername());
          session.getOutput().addShort(user.getWorldId());
          session.getOutput().addByte(user.getRank().ordinal());
        }
        session.getOutput().endOpcodeVarInt();
        session.write();
      }
    }
  }

  private void decodeClanRank(ServerSession session) {
    int key = session.getInput().getInt();
    String username = session.getInput().getString();
    RsFriend friend = new RsFriend(session.getInput().getString(), 0,
        RsClanRank.values()[session.getInput().getUByte()]);
    synchronized (this) {
      requests.add(new ClanRankRequest(session, key, username, friend));
      notify();
    }
  }

  private void encodeClanRank(ClanRankRequest request) {
    if (DEBUG) {
      PLogger.println("[" + PTime.getFullDate() + "] Encode: " + Opcodes.CLAN_RANK);
    }
    ServerSession session = request.getSession();
    RsPlayer player;
    synchronized (this) {
      player = playersByUsername.get(request.getUsername().toLowerCase());
    }
    session.getOutput().addOpcodeVarInt(Opcodes.CLAN_RANK);
    session.getOutput().addInt(request.getKey());
    if (player == null) {
      session.getOutput().addShort(0);
      session.getOutput().endOpcodeVarInt();
      session.write();
      return;
    }
    RsFriend friend = request.getFriend();
    player.loadFriendClanRank(friend);
    Clan clan = loadClan(null, player.getUsername(), player.getId(), player.getFriends(),
        player.getIgnores());
    synchronized (this) {
      RsPlayer p = playersByUsername.get(friend.getUsername().toLowerCase());
      if (p != null && RsFriend.canRegister(player.getUsername(), player.getPrivateChatStatus(),
          p.getUsername(), p.getPrivateChatStatus(), player.getFriends(), p.getFriends(),
          p.getIgnores())) {
        friend.setWorldId(p.getWorldId());
      } else {
        friend.setWorldId(0);
      }
      if (clan != null) {
        if (clan != null && !clanUpdates.contains(clan)) {
          clanUpdates.add(clan);
        }
      }
    }
    session.getOutput().addShort(friend.getWorldId());
    session.getOutput().endOpcodeVarInt();
    session.write();
  }

  private void decodeSQLUpdate(ServerSession session) {
    int key = session.getInput().getInt();
    String sql = session.getInput().getString();
    synchronized (this) {
      requests.add(new SQLUpdateRequest(session, key, sql));
      sqlRequests.add(new SQLUpdateRequest(session, key, sql));
      notify();
    }
  }

  private void encodeSQLUpdate(SQLUpdateRequest request) {
    if (DEBUG) {
      PLogger.println("[" + PTime.getFullDate() + "] Encode: " + Opcodes.SQL_UPDATE);
    }
    request.getSession().getOutput().addOpcodeVarInt(Opcodes.SQL_UPDATE);
    request.getSession().getOutput().addInt(request.getKey());
    request.getSession().getOutput().endOpcodeVarInt();
    request.getSession().write();
  }

  private boolean executeSQLUpdate(SQLUpdateRequest request) {
    String query = PString.cleanString(request.getSql());
    if (query.length() > 0) {
      try {
        lastSQL3 = lastSQL2;
        lastSQL2 = lastSQL1;
        lastSQL1 = query;
        Connection connection = FileManager.getSqlConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
        statement.close();
        return true;
      } catch (Exception e) {
        PLogger.error(query, e);
        return e.getMessage().contains("You have an error in your SQL syntax");
      }
    }
    return true;
  }

  private void decodeGERefresh(ServerSession session) {
    int key = session.getInput().getInt();
    int userId = session.getInput().getInt();
    String username = session.getInput().getString();
    int gameMode = session.getInput().getUByte();
    synchronized (this) {
      requests.add(new GERefreshRequest(session, key, userId, username, gameMode, 0));
      notify();
    }
  }

  private void encodeGERefresh(GERefreshRequest request) {
    if (DEBUG) {
      PLogger.println("[" + PTime.getFullDate() + "] Encode: " + Opcodes.GE_REFRESH);
    }
    ServerSession session = request.getSession();
    GrandExchangeUser user;
    synchronized (this) {
      user = grandExchange.get(request.getUserId());
    }
    session.getOutput().addOpcodeVarInt(Opcodes.GE_REFRESH);
    session.getOutput().addInt(request.getKey());
    if (user != null) {
      user.setUsername(request.getUsername());
      if (request.getGameMode() != 0) {
        user.setGameMode(request.getGameMode());
      }
      for (int i = 0; i < GrandExchangeItem.MAX_P2P_ITEMS; i++) {
        GrandExchangeItem item = user.getItem(i);
        if (item != null && item.canRemove()) {
          user.setItem(i, null);
          item = null;
        }
        if (item == null || item.getId() == -1 || item.getAmount() <= 0) {
          session.getOutput().addInt(-1);
          continue;
        }
        session.getOutput().addInt(item.getId());
        session.getOutput().addByte(item.getState());
        session.getOutput().addInt(item.getAmount());
        session.getOutput().addInt(item.getPrice());
        session.getOutput().addInt(item.getExchangedAmount());
        session.getOutput().addInt(item.getExchangedPrice());
        session.getOutput().addInt(item.getCollectedAmount());
        session.getOutput().addInt(item.getCollectedPrice());
        session.getOutput().addBoolean(item.getAborted());
      }
    } else {
      for (int i = 0; i < GrandExchangeItem.MAX_P2P_ITEMS; i++) {
        session.getOutput().addInt(-1);
      }
    }
    session.getOutput().endOpcodeVarInt();
    session.write();
  }

  private void encodeGERefresh(GrandExchangeUser user) {
    if (DEBUG) {
      PLogger.println("[" + PTime.getFullDate() + "] Encode: " + Opcodes.GE_REFRESH);
    }
    for (ServerSession session : sessions.values()) {
      session.getOutput().addOpcodeVarInt(Opcodes.GE_REFRESH);
      session.getOutput().addInt(-1);
      session.getOutput().addInt(user.getUserId());
      session.getOutput().addByte(user.getGameMode());
      for (int i = 0; i < GrandExchangeItem.MAX_P2P_ITEMS; i++) {
        GrandExchangeItem item = user.getItem(i);
        if (item != null && item.canRemove()) {
          user.setItem(i, null);
          item = null;
        }
        if (item == null || item.getId() == -1 || item.getAmount() <= 0) {
          session.getOutput().addInt(-1);
          continue;
        }
        session.getOutput().addInt(item.getId());
        session.getOutput().addByte(item.getState());
        session.getOutput().addInt(item.getAmount());
        session.getOutput().addInt(item.getPrice());
        session.getOutput().addInt(item.getExchangedAmount());
        session.getOutput().addInt(item.getExchangedPrice());
        session.getOutput().addInt(item.getCollectedAmount());
        session.getOutput().addInt(item.getCollectedPrice());
        session.getOutput().addBoolean(item.getAborted());
      }
      session.getOutput().endOpcodeVarInt();
      session.write();
    }
  }

  private void decodeGEBuyOffer(ServerSession session) {
    int key = session.getInput().getInt();
    int userId = session.getInput().getInt();
    String username = session.getInput().getString();
    String ip = session.getInput().getString();
    int gameMode = session.getInput().getUByte();
    int slot = session.getInput().getUByte();
    int id = session.getInput().getInt();
    String itemName = session.getInput().getString();
    int amount = session.getInput().getInt();
    int price = session.getInput().getInt();
    synchronized (this) {
      requests.add(new GEBuyOfferRequest(session, key, userId, username, ip, gameMode, slot, id,
          itemName, amount, price));
      notify();
    }
  }

  private void encodeGEBuyOffer(GEBuyOfferRequest request) {
    if (DEBUG) {
      PLogger.println("[" + PTime.getFullDate() + "] Encode: " + Opcodes.GE_BUY_OFFER);
    }
    request.getSession().getOutput().addOpcodeVarInt(Opcodes.GE_BUY_OFFER);
    request.getSession().getOutput().addInt(request.getKey());
    request.getSession().getOutput().endOpcodeVarInt();
    request.getSession().write();
    GrandExchangeUser user;
    synchronized (this) {
      user = grandExchange.get(request.getUserId());
      if (user == null) {
        user = new GrandExchangeUser(request.getUserId());
        grandExchange.put(request.getUserId(), user);
      }
      user.setUsername(request.getUsername());
      if (request.getGameMode() != 0) {
        user.setGameMode(request.getGameMode());
      }
    }
    user.setItem(request.getSlot(),
        new GrandExchangeItem(GrandExchangeItem.STATE_BUYING, request.getUserIP(), request.getId(),
            request.getName(), request.getAmount(), request.getPrice()));
    checkGrandExchangeStatus(user, null);
    saveGrandExchangeUser(user);
  }

  private void decodeGESellOffer(ServerSession session) {
    int key = session.getInput().getInt();
    int userId = session.getInput().getInt();
    String username = session.getInput().getString();
    String userIP = session.getInput().getString();
    int gameMode = session.getInput().getUByte();
    int slot = session.getInput().getUByte();
    int id = session.getInput().getInt();
    String name = session.getInput().getString();
    int amount = session.getInput().getInt();
    int price = session.getInput().getInt();
    synchronized (this) {
      requests.add(new GESellOfferRequest(session, key, userId, username, userIP, gameMode, slot,
          id, name, amount, price));
      notify();
    }
  }

  private void encodeGESellOffer(GESellOfferRequest request) {
    if (DEBUG) {
      PLogger.println("[" + PTime.getFullDate() + "] Encode: " + Opcodes.GE_SELL_OFFER);
    }
    request.getSession().getOutput().addOpcodeVarInt(Opcodes.GE_SELL_OFFER);
    request.getSession().getOutput().addInt(request.getKey());
    request.getSession().getOutput().endOpcodeVarInt();
    request.getSession().write();
    GrandExchangeUser user;
    synchronized (this) {
      user = grandExchange.get(request.getUserId());
      if (user == null) {
        user = new GrandExchangeUser(request.getUserId());
        grandExchange.put(request.getUserId(), user);
      }
      user.setUsername(request.getUsername());
      if (request.getGameMode() != 0) {
        user.setGameMode(request.getGameMode());
      }
    }
    user.setItem(request.getSlot(),
        new GrandExchangeItem(GrandExchangeItem.STATE_SELLING, request.getUserIP(), request.getId(),
            request.getName(), request.getAmount(), request.getPrice()));
    checkGrandExchangeStatus(user, null);
    saveGrandExchangeUser(user);
  }

  private void decodeGEAbortOffer(ServerSession session) {
    int key = session.getInput().getInt();
    int userId = session.getInput().getInt();
    int slot = session.getInput().getUByte();
    synchronized (this) {
      requests.add(new GEAbortOfferRequest(session, key, userId, slot));
      notify();
    }
  }

  private void encodeGEAbortOffer(GEAbortOfferRequest request) {
    if (DEBUG) {
      PLogger.println("[" + PTime.getFullDate() + "] Encode: " + Opcodes.GE_ABORT_OFFER);
    }
    request.getSession().getOutput().addOpcodeVarInt(Opcodes.GE_ABORT_OFFER);
    request.getSession().getOutput().addInt(request.getKey());
    request.getSession().getOutput().endOpcodeVarInt();
    request.getSession().write();
    GrandExchangeUser user;
    synchronized (this) {
      user = grandExchange.get(request.getUserId());
      if (user == null) {
        user = new GrandExchangeUser(request.getUserId());
        grandExchange.put(request.getUserId(), user);
      }
    }
    user.abortItem(request.getSlot());
    saveGrandExchangeUser(user);
  }

  private void decodeGECollectOffer(ServerSession session) {
    int key = session.getInput().getInt();
    int userId = session.getInput().getInt();
    int slot = session.getInput().getUByte();
    int collectedAmount = session.getInput().getInt();
    int collectedPrice = session.getInput().getInt();
    synchronized (this) {
      requests.add(
          new GECollectOfferRequest(session, key, userId, slot, collectedAmount, collectedPrice));
      notify();
    }
  }

  private void encodeGECollectOffer(GECollectOfferRequest request) {
    if (DEBUG) {
      PLogger.println("[" + PTime.getFullDate() + "] Encode: " + Opcodes.GE_COLLECT_OFFER);
    }
    request.getSession().getOutput().addOpcodeVarInt(Opcodes.GE_COLLECT_OFFER);
    request.getSession().getOutput().addInt(request.getKey());
    request.getSession().getOutput().endOpcodeVarInt();
    request.getSession().write();
    GrandExchangeUser user;
    synchronized (this) {
      user = grandExchange.get(request.getUserId());
      if (user == null) {
        user = new GrandExchangeUser(request.getUserId());
        grandExchange.put(request.getUserId(), user);
      }
    }
    user.collect(request.getSlot(), request.getCollectedAmount(), request.getCollectedPrice());
    saveGrandExchangeUser(user);
  }

  private void decodeGEPriceAverage(ServerSession session) {
    int key = session.getInput().getInt();
    int userId = session.getInput().getInt();
    int gameMode = session.getInput().getUByte();
    int itemId = session.getInput().getInt();
    synchronized (this) {
      requests.add(new GEPriceAverageRequest(session, key, userId, gameMode, itemId));
      notify();
    }
  }

  private void encodeGEPriceAverage(GEPriceAverageRequest request) {
    if (DEBUG) {
      PLogger.println("[" + PTime.getFullDate() + "] Encode: " + Opcodes.GE_PRICE_AVERAGE);
    }
    int totalBuy = 0;
    int totalSell = 0;
    long totalBuyPrice = 0;
    long totalSellPrice = 0;
    int cheapestSell = Integer.MAX_VALUE;
    for (GrandExchangeUser user : grandExchange.values()) {
      for (int i = 0; i < GrandExchangeItem.MAX_P2P_ITEMS; i++) {
        GrandExchangeItem item = user.getItems()[i];
        if (item == null || item.getAborted() || !item.getStateBuying() && !item.getStateSelling()
            || request.getItemId() != item.getId()
            || (request.isGameModeHard() && !user.isGameModeHard()
                || !request.isGameModeHard() && user.isGameModeHard())
                && GrandExchangeItem.isBlockedHardModeItem(item.getId())) {
          continue;
        }
        if (item.getStateBuying()) {
          totalBuy++;
          totalBuyPrice += item.getPrice();
        } else if (item.getStateSelling()) {
          totalSell++;
          totalSellPrice += item.getPrice();
          if (item.getPrice() < cheapestSell) {
            cheapestSell = item.getPrice();
          }
        }
      }
    }
    int averageBuy = 0;
    if (totalBuy > 0) {
      averageBuy = (int) (totalBuyPrice / totalBuy);
    }
    int averageSell = 0;
    if (totalSell > 0) {
      averageSell = (int) (totalSellPrice / totalSell);
    }
    request.getSession().getOutput().addOpcodeVarInt(Opcodes.GE_PRICE_AVERAGE);
    request.getSession().getOutput().addInt(request.getKey());
    request.getSession().getOutput().addInt(totalBuy);
    request.getSession().getOutput().addInt(averageBuy);
    request.getSession().getOutput().addInt(totalSell);
    request.getSession().getOutput().addInt(averageSell);
    request.getSession().getOutput().addInt(cheapestSell);
    request.getSession().getOutput().endOpcodeVarInt();
    request.getSession().write();
  }

  private void decodeGEHistory(ServerSession session) {
    int key = session.getInput().getInt();
    int userId = session.getInput().getInt();
    int type = session.getInput().getUByte();
    synchronized (this) {
      requests.add(new GEHistoryRequest(session, key, userId, type));
      notify();
    }
  }

  private void encodeGEHistory(GEHistoryRequest request) {
    if (DEBUG) {
      PLogger.println("[" + PTime.getFullDate() + "] Encode: " + Opcodes.GE_HISTORY);
    }
    request.getSession().getOutput().addOpcodeVarInt(Opcodes.GE_HISTORY);
    request.getSession().getOutput().addInt(request.getKey());
    int total = 0;
    if (request.getType() == GrandExchangeUser.HISTORY_RECENT
        || request.getType() == GrandExchangeUser.HISTORY_RECENT_BUY
        || request.getType() == GrandExchangeUser.HISTORY_RECENT_SELL
        || request.getType() == GrandExchangeUser.HISTORY_RANDOM
        || request.getType() == GrandExchangeUser.HISTORY_RANDOM_BUY
        || request.getType() == GrandExchangeUser.HISTORY_RANDOM_SELL) {
      int state =
          request.getType() == GrandExchangeUser.HISTORY_RECENT_BUY ? GrandExchangeItem.STATE_BUYING
              : request.getType() == GrandExchangeUser.HISTORY_RECENT_SELL
                  ? GrandExchangeItem.STATE_SELLING
                  : -1;
      List<GrandExchangeUser> users = new ArrayList<>(grandExchange.values());
      if (request.getType() == GrandExchangeUser.HISTORY_RECENT
          || request.getType() == GrandExchangeUser.HISTORY_RECENT_BUY
          || request.getType() == GrandExchangeUser.HISTORY_RECENT_SELL) {
        Collections.sort(users, (user1, user2) -> {
          GrandExchangeItem item1 = user1.getRecentItem(state);
          GrandExchangeItem item2 = user2.getRecentItem(state);
          return Long.compare(item2 != null ? item2.getCreation() : 0,
              item1 != null ? item1.getCreation() : 0);
        });
      } else if (request.getType() == GrandExchangeUser.HISTORY_RANDOM
          || request.getType() == GrandExchangeUser.HISTORY_RANDOM_BUY
          || request.getType() == GrandExchangeUser.HISTORY_RANDOM_SELL) {
        Collections.shuffle(users);
      }
      int state2 = -1;
      if (request.getType() == GrandExchangeUser.HISTORY_RECENT_BUY
          || request.getType() == GrandExchangeUser.HISTORY_RANDOM_BUY) {
        state2 = GrandExchangeItem.STATE_SELLING;
      } else if (request.getType() == GrandExchangeUser.HISTORY_RECENT_SELL
          || request.getType() == GrandExchangeUser.HISTORY_RANDOM_SELL) {
        state2 = GrandExchangeItem.STATE_BUYING;
      }
      for (int i = 0; i < users.size() && total < GrandExchangeUser.HISTORY_SIZE; i++) {
        GrandExchangeUser user = users.get(i);
        if (user != null) {
          if (user.getItems() != null) {
            for (int i2 = 0; i2 < GrandExchangeItem.MAX_P2P_ITEMS; i2++) {
              GrandExchangeItem item = user.getItems()[i2];
              if (request.getType() == GrandExchangeUser.HISTORY_RECENT
                  || request.getType() == GrandExchangeUser.HISTORY_RECENT_BUY
                  || request.getType() == GrandExchangeUser.HISTORY_RECENT_SELL) {
                item = user.getRecentItem(state);
              }
              if (item == null || item.getAborted() || item.getRemainingAmount() <= 0
                  || item.getState() == state2) {
                continue;
              }
              request.getSession().getOutput().addInt(item.getId());
              request.getSession().getOutput().addByte(item.getState());
              request.getSession().getOutput().addInt(item.getAmount());
              request.getSession().getOutput().addInt(item.getPrice());
              total++;
              break;
            }
          }
        }
      }
    }
    while (total++ < GrandExchangeUser.HISTORY_SIZE) {
      request.getSession().getOutput().addInt(-1);
      request.getSession().getOutput().addByte(0);
      request.getSession().getOutput().addInt(0);
      request.getSession().getOutput().addInt(0);
    }
    request.getSession().getOutput().endOpcodeVarInt();
    request.getSession().write();
  }

  private void decodeLog(ServerSession session, int length) {
    int key = session.getInput().getInt();
    String directory1 = session.getInput().getString();
    String directory2 = session.getInput().getString();
    String line = session.getInput().getString();
    lastLog3 = lastLog2;
    lastLog2 = lastLog1;
    lastLog1 = "[" + directory1 + ", " + directory2 + ", " + length + "] " + line;
    synchronized (this) {
      requests.add(new LogRequest(session, key, directory1, directory2, line));
      notify();
    }
  }

  private void encodeLog(LogRequest request) {
    if (DEBUG) {
      PLogger.println("[" + PTime.getFullDate() + "] Encode: " + Opcodes.LOG);
    }
    FileManager.writeLog(new File(request.getDirectory1()), request.getDirectory2(),
        request.getLine());
  }

  private void decodeGEShop(ServerSession session) {
    int key = session.getInput().getInt();
    String username = session.getInput().getString();
    synchronized (this) {
      requests.add(new GEShopRequest(session, key, username));
      notify();
    }
  }

  private void encodeGEShop(GEShopRequest request) {
    if (DEBUG) {
      PLogger.println("[" + PTime.getFullDate() + "] Encode: " + Opcodes.GE_SHOP);
    }
    ServerSession session = request.getSession();
    GrandExchangeUser user;
    synchronized (this) {
      user = getGrandExchangeUser(request.getUsername());
    }
    session.getOutput().addOpcodeVarInt(Opcodes.GE_SHOP);
    session.getOutput().addInt(request.getKey());
    if (user != null) {
      session.getOutput().addInt(user.getUserId());
      session.getOutput().addString(user.getUsername());
      session.getOutput().addByte(user.getGameMode());
      for (int i = 0; i < GrandExchangeItem.MAX_P2P_ITEMS; i++) {
        GrandExchangeItem item = user.getItem(i);
        if (item != null && item.canRemove()) {
          user.setItem(i, null);
          item = null;
        }
        if (item == null || item.getId() == -1 || item.getAmount() <= 0) {
          session.getOutput().addInt(-1);
          continue;
        }
        session.getOutput().addInt(item.getId());
        session.getOutput().addByte(item.getState());
        session.getOutput().addInt(item.getAmount());
        session.getOutput().addInt(item.getPrice());
        session.getOutput().addInt(item.getExchangedAmount());
        session.getOutput().addInt(item.getExchangedPrice());
        session.getOutput().addInt(item.getCollectedAmount());
        session.getOutput().addInt(item.getCollectedPrice());
        session.getOutput().addBoolean(item.getAborted());
      }
    } else {
      session.getOutput().addInt(-1);
      session.getOutput().addString("");
      session.getOutput().addByte(0);
      for (int i = 0; i < GrandExchangeItem.MAX_P2P_ITEMS; i++) {
        session.getOutput().addInt(-1);
      }
    }
    session.getOutput().endOpcodeVarInt();
    session.write();
  }

  private void decodeGEShopOffer(ServerSession session) {
    int key = session.getInput().getInt();
    int userId = session.getInput().getInt();
    String username = session.getInput().getString();
    String ip = session.getInput().getString();
    int gameMode = session.getInput().getUByte();
    int shopUserId = session.getInput().getInt();
    int slot = session.getInput().getUByte();
    int id = session.getInput().getInt();
    int amount = session.getInput().getInt();
    int price = session.getInput().getInt();
    int state = session.getInput().getUByte();
    synchronized (this) {
      requests.add(new GEShopOfferRequest(session, key, userId, username, ip, gameMode, shopUserId,
          slot, id, amount, price, state));
      notify();
    }
  }

  private void encodeGEShopOffer(GEShopOfferRequest request) {
    if (DEBUG) {
      PLogger.println("[" + PTime.getFullDate() + "] Encode: " + Opcodes.GE_SHOP_OFFER);
    }
    request.getSession().getOutput().addOpcodeVarInt(Opcodes.GE_SHOP_OFFER);
    request.getSession().getOutput().addInt(request.getKey());
    GrandExchangeUser user;
    synchronized (this) {
      user = grandExchange.get(request.getShopUserId());
    }
    GrandExchangeItem item = user != null ? user.getItem(request.getSlot()) : null;
    if (user == null || item == null || item.getAborted()
        || !item.getStateBuying() && !item.getStateSelling() || item.getId() != request.getId()
        || item.getState() != request.getGEState()
        || item.getRemainingAmount() < request.getAmount()
        || item.getStateBuying() && item.getPrice() != request.getPrice()
        || item.getIP() != null && request.getUserIP() != null
            && item.getIP().equals(request.getUserIP())
        || request.getAmount() < 1 || request.getPrice() < 1
        || (request.isGameModeHard() && !user.isGameModeHard()
            || !request.isGameModeHard() && user.isGameModeHard())
            && GrandExchangeItem.isBlockedHardModeItem(item.getId())) {
      request.getSession().getOutput().addBoolean(false);
      request.getSession().getOutput().endOpcodeVarInt();
      request.getSession().write();
      return;
    }
    String log1String = "";
    String log2String = "";
    String user1String =
        "[" + request.getUserId() + "; " + request.getUserIP() + "] " + request.getUsername();
    String user2String = "[" + user.getUserId() + "; " + item.getIP() + "] " + user.getUsername();
    String itemString = "[" + item.getId() + "] " + item.getName() + " x"
        + PNumber.formatNumber(request.getAmount()) + " for "
        + PNumber.formatNumber(item.getPrice());
    if (item.getStateBuying()) {
      log1String = user1String + " bought " + itemString + " from " + user2String
          + " on the Grand Exchange.";
      log2String =
          user2String + " sold " + itemString + " to " + user1String + " on the Grand Exchange.";
    } else if (item.getStateSelling()) {
      log1String =
          user1String + " sold " + itemString + " to " + user2String + " on the Grand Exchange.";
      log2String = user2String + " bought " + itemString + " from " + user1String
          + " on the Grand Exchange.";
    }
    FileManager.writeLog(Settings.getInstance().getPlayerLogsDirectory(),
        "exchange/" + request.getUserId() + ".txt", log1String);
    FileManager.writeLog(Settings.getInstance().getPlayerLogsDirectory(),
        "exchange/" + user.getUserId() + ".txt", log2String);
    item.increaseExchanged(request.getAmount(), request.getPrice());
    request.getSession().getOutput().addBoolean(true);
    request.getSession().getOutput().endOpcodeVarInt();
    request.getSession().write();
    encodeGERefresh(user);
    saveGrandExchangeUser(user);
  }

  private void decodeGEList(ServerSession session) {
    int key = session.getInput().getInt();
    int userId = session.getInput().getInt();
    int type = session.getInput().getUByte();
    int searchId = session.getInput().getInt();
    String searchString = session.getInput().getString();
    synchronized (this) {
      requests.add(new GEListRequest(session, key, userId, type, searchId, searchString));
      notify();
    }
  }

  private void encodeGEList(GEListRequest request) {
    if (DEBUG) {
      PLogger.println("[" + PTime.getFullDate() + "] Encode: " + Opcodes.GE_LIST);
    }
    String title = "";
    List<String> list = new ArrayList<>();
    synchronized (this) {
      if (request.getType() == GrandExchangeUser.LIST_RANDOM_PLAYERS) {
        title = "Random Users";
        List<GrandExchangeUser> users = new ArrayList<>(grandExchange.values());
        Collections.shuffle(users);
        for (GrandExchangeUser user : users) {
          int buyCount = user.getBuyCount();
          int sellCount = user.getSellCount();
          if (buyCount == 0 && sellCount == 0) {
            continue;
          }
          list.add("[" + user.getGameMode() + "] " + user.getUsername() + ": Buy: " + buyCount
              + ", Sell: " + sellCount);
          if (list.size() == 20) {
            break;
          }
        }
      } else if (request.getType() == GrandExchangeUser.LIST_BUY_ITEM) {
        title = "Buy Offers: " + request.getSearchString();
        List<GrandExchangeUser> users = new ArrayList<>(grandExchange.values());
        Collections.shuffle(users);
        for (GrandExchangeUser user : users) {
          GrandExchangeItem item =
              user.getItemFromId(request.getSearchId(), GrandExchangeItem.STATE_BUYING);
          if (item == null) {
            continue;
          }
          list.add("[" + user.getGameMode() + "] " + user.getUsername() + ": x"
              + item.getRemainingAmount() + ", " + PNumber.formatNumber(item.getPrice())
              + " coins");
          if (list.size() == 20) {
            break;
          }
        }
      } else if (request.getType() == GrandExchangeUser.LIST_SELL_ITEM) {
        title = "Sell Offers: " + request.getSearchString();
        List<GrandExchangeUser> users = new ArrayList<>(grandExchange.values());
        Collections.shuffle(users);
        for (GrandExchangeUser user : users) {
          GrandExchangeItem item =
              user.getItemFromId(request.getSearchId(), GrandExchangeItem.STATE_SELLING);
          if (item == null) {
            continue;
          }
          list.add("[" + user.getGameMode() + "] " + user.getUsername() + ": x"
              + item.getRemainingAmount() + ", " + PNumber.formatNumber(item.getPrice())
              + " coins");
          if (list.size() == 20) {
            break;
          }
        }
      } else if (request.getType() == GrandExchangeUser.LIST_ITEM) {
        title = "Offers: " + request.getSearchString();
        List<GrandExchangeUser> users = new ArrayList<>(grandExchange.values());
        Collections.shuffle(users);
        List<String> sells = new ArrayList<>();
        List<String> buys = new ArrayList<>();
        for (GrandExchangeUser user : users) {
          GrandExchangeItem sellingItem =
              user.getItemFromId(request.getSearchId(), GrandExchangeItem.STATE_SELLING);
          GrandExchangeItem buyingItem =
              user.getItemFromId(request.getSearchId(), GrandExchangeItem.STATE_BUYING);
          if (sellingItem != null) {
            sells.add("[" + user.getGameMode() + "] " + "[SELL] " + user.getUsername() + ": x"
                + sellingItem.getRemainingAmount() + ", "
                + PNumber.formatNumber(sellingItem.getPrice()) + " coins");
          } else if (buyingItem != null) {
            buys.add("[" + user.getGameMode() + "] " + "[BUY] " + user.getUsername() + ": x"
                + buyingItem.getRemainingAmount() + ", "
                + PNumber.formatNumber(buyingItem.getPrice()) + " coins");
          }
          if (sells.size() + buys.size() == 20) {
            break;
          }
        }
        list.addAll(sells);
        list.addAll(buys);
      }
    }
    ServerSession session = request.getSession();
    session.getOutput().addOpcodeVarInt(Opcodes.GE_LIST);
    session.getOutput().addInt(request.getKey());
    session.getOutput().addString(title);
    session.getOutput().addByte(list.size());
    for (String s : list) {
      session.getOutput().addString(s);
    }
    session.getOutput().endOpcodeVarInt();
    session.write();
  }

  private Clan loadClan(String name, String username, int userId, List<RsFriend> friends,
      List<String> ignores) {
    Clan clan = null;
    synchronized (this) {
      clan = clans.get(username.toLowerCase());
      if (clan == null) {
        clan = Clan.getGlobal(username);
        if (clan == null) {
          clan = loadClan(username);
        }
        if (clan != null && !Clan.isGlobal(clan)) {
          clan.setOwner(username);
          if (userId != -1) {
            clan.setOwnerId(userId);
          }
          clans.put(username.toLowerCase(), clan);
        }
      }
      if (clan == null && name == null || friends == null || ignores == null) {
        return clan;
      }
      boolean checkLoad = name != null && name.equals("!check_load");
      if (checkLoad) {
        name = "";
      }
      if (clan == null) {
        clan = new Clan(name, username, userId, friends, ignores);
        clans.put(username.toLowerCase(), clan);
      } else if (checkLoad) {
        return clan;
      }
      if (name != null && name.length() != 0) {
        clan.setName(name);
      }
      clan.updateFriends(friends, ignores);
    }
    saveClan(clan);
    return clan;
  }

  private Clan loadClan(String username) {
    return FileManager.fromJson(new File(Settings.getInstance().getPlayerClanDirectory(),
        username.replace(" ", "_").toLowerCase() + ".json"), Clan.class);
  }

  private void saveClan(Clan clan) {
    if (Clan.isGlobal(clan)) {
      return;
    }
    FileManager.toJson(new File(Settings.getInstance().getPlayerClanDirectory(),
        clan.getOwner().replace(" ", "_").toLowerCase() + ".json"), clan);
  }

  private void checkGrandExchangeStatus(GrandExchangeUser user, List<GrandExchangeUser> users) {
    if (user.getItems() == null) {
      return;
    }
    if (users == null) {
      users = new ArrayList<>(grandExchange.values());
      Collections.shuffle(users);
    }
    for (int i = 0; i < GrandExchangeItem.MAX_P2P_ITEMS; i++) {
      GrandExchangeItem item = user.getItems()[i];
      if (item == null || item.getAborted() || !item.getStateBuying() && !item.getStateSelling()
          || item.getRemainingAmount() <= 0) {
        continue;
      }
      for (int i2 = 0; i2 < users.size(); i2++) {
        GrandExchangeUser user2 = users.get(i2);
        if (user.getUserId() == user2.getUserId() && user.getUserId() != 1) {
          continue;
        }
        for (int i3 = 0; i3 < GrandExchangeItem.MAX_P2P_ITEMS; i3++) {
          GrandExchangeItem item2 = user2.getItems()[i3];
          if (item2 == null || item2.getAborted()
              || !item2.getStateBuying() && !item2.getStateSelling()
              || item.getId() != item2.getId() || item.getState() == item2.getState()
              || item2.getRemainingAmount() <= 0
              || item.getStateBuying() && item.getPrice() < item2.getPrice()
              || item.getStateSelling() && item.getPrice() > item2.getPrice()
              || item.getIP() != null && item2.getIP() != null && item.getIP().equals(item2.getIP())
                  && user.getUserId() != 1
              || (user.isGameModeHard() && !user2.isGameModeHard()
                  || !user.isGameModeHard() && user2.isGameModeHard())
                  && GrandExchangeItem.isBlockedHardModeItem(item.getId())) {
            continue;
          }
          int amount = Math.min(item.getRemainingAmount(), item2.getRemainingAmount());
          int price = Math.min(item.getPrice(), item2.getPrice());
          if (amount < 1 || price < 1) {
            continue;
          }
          String log1String = "";
          String log2String = "";
          String user1String =
              "[" + user.getUserId() + "; " + item.getIP() + "] " + user.getUsername();
          String user2String =
              "[" + user2.getUserId() + "; " + item2.getIP() + "] " + user2.getUsername();
          String itemString = "[" + item.getId() + "] " + item.getName() + " x"
              + PNumber.formatNumber(amount) + " for " + PNumber.formatNumber(price);
          if (item.getStateBuying()) {
            log1String = user1String + " bought " + itemString + " from " + user2String
                + " on the Grand Exchange.";
            log2String = user2String + " sold " + itemString + " to " + user1String
                + " on the Grand Exchange.";
          } else if (item.getStateSelling()) {
            log1String = user1String + " sold " + itemString + " to " + user2String
                + " on the Grand Exchange.";
            log2String = user2String + " bought " + itemString + " from " + user1String
                + " on the Grand Exchange.";
          }
          FileManager.writeLog(Settings.getInstance().getPlayerLogsDirectory(),
              "exchange/" + user.getUserId() + ".txt", log1String);
          FileManager.writeLog(Settings.getInstance().getPlayerLogsDirectory(),
              "exchange/" + user2.getUserId() + ".txt", log2String);
          item.increaseExchanged(amount, price);
          item2.increaseExchanged(amount, price);
          encodeGERefresh(user2);
          saveGrandExchangeUser(user);
          saveGrandExchangeUser(user2);
        }
      }
    }
  }

  private GrandExchangeUser getGrandExchangeUser(String username) {
    for (GrandExchangeUser user : grandExchange.values()) {
      if (username.equalsIgnoreCase(user.getUsername())) {
        return user;
      }
    }
    return null;
  }

  public void saveGrandExchangeUser(GrandExchangeUser user) {
    boolean hasItems = false;
    if (user.getItems() == null) {
      return;
    }
    for (int i = 0; i < GrandExchangeItem.MAX_P2P_ITEMS; i++) {
      if (user.getItems()[i] != null) {
        hasItems = true;
        break;
      }
    }
    if (!hasItems) {
      new File(Settings.getInstance().getPlayerExchangeDirectory(), user.getUserId() + ".xml")
          .delete();
      new File(Settings.getInstance().getPlayerExchangeDirectory(), user.getUserId() + ".json")
          .delete();
      return;
    }
    FileManager.toJson(
        new File(Settings.getInstance().getPlayerExchangeDirectory(), user.getUserId() + ".json"),
        user);
  }

  private void loadGrandExchangeUsers() {
    File[] listFiles = Settings.getInstance().getPlayerExchangeDirectory().listFiles();
    if (listFiles == null) {
      return;
    }
    for (File file : listFiles) {
      if (file.isHidden()
          || !file.getName().endsWith(".json") && !file.getName().endsWith(".xml")) {
        continue;
      }
      GrandExchangeUser user = null;
      if (file.getName().endsWith(".xml")) {
        Object loadXML = FileManager.loadXML(file);
        if (!(loadXML instanceof GrandExchangeUser)) {
          continue;
        }
        user = (GrandExchangeUser) loadXML;
      } else if (file.getName().endsWith(".json")) {
        user = FileManager.fromJson(file, GrandExchangeUser.class);
      }
      user.expireItems();
      if (user.getGameMode() == 0) {
        user.setGameMode(RsGameMode.NORMAL.ordinal());
      }
      grandExchange.put(user.getUserId(), user);
    }
  }

  public void replaceGrandExchangeItems(int n, int n2) {
    List<GrandExchangeUser> list = new ArrayList<>(grandExchange.values());
    for (int i = 0; i < list.size(); i++) {
      GrandExchangeUser grandExchangeUser = list.get(i);
      for (int j = 0; j < GrandExchangeItem.MAX_P2P_ITEMS; ++j) {
        GrandExchangeItem grandExchangeItem = grandExchangeUser.getItems()[j];
        if (grandExchangeItem != null && !grandExchangeItem.getAborted()
            && grandExchangeItem.getState() == 2 && n == grandExchangeItem.getId()) {
          if (grandExchangeItem.getRemainingAmount() != 0) {
            int remainingAmount = grandExchangeItem.getRemainingAmount();
            if (remainingAmount >= 1) {
              if (n2 >= 1) {
                int price = Math.min(n2, grandExchangeItem.getPrice());
                grandExchangeItem.increaseExchanged(remainingAmount, price);
                encodeGERefresh(grandExchangeUser);
                saveGrandExchangeUser(grandExchangeUser);
              }
            }
          }
        }
      }
    }
  }

  public void reduceGrandExchangeCoins(double percent) {
    if (percent <= 0 || percent >= 1) {
      return;
    }
    List<GrandExchangeUser> list = new ArrayList<>(grandExchange.values());
    for (int i = 0; i < list.size(); i++) {
      GrandExchangeUser grandExchangeUser = list.get(i);
      for (int j = 0; j < GrandExchangeItem.MAX_P2P_ITEMS; ++j) {
        GrandExchangeItem item = grandExchangeUser.getItems()[j];
        if (item == null) {
          continue;
        }
        if (item.getStateBuying()) {
          item.setPrice((int) Math.max(1, item.getPrice() * percent));
        }
        item.setExchangedPrice((int) (item.getExchangedPrice() * percent));
        item.setCollectedPrice((int) (item.getCollectedPrice() * percent));
      }
    }
  }

  public void printStats() {
    Runtime rt = Runtime.getRuntime();
    long usedMB = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
    PLogger.println("Memory Used: " + usedMB + "MB");
    PLogger.println("Max Memory: " + rt.maxMemory() / 1024 / 1024 + "MB");
    PLogger.println("Total Memory: " + rt.totalMemory() / 1024 / 1024 + "MB");
    PLogger.println("Free Memory: " + rt.freeMemory() / 1024 / 1024 + "MB");
    PLogger.println("Largest Packet: " + largestPacket);
    PLogger.println("sessions = " + sessions.size());
    PLogger.println("requests = " + requests.size());
    PLogger.println("clanUpdates = " + clanUpdates.size());
    PLogger.println("sqlRequests = " + sqlRequests.size());
    PLogger.println("playersByUsername = " + playersByUsername.size());
    PLogger.println("playersById = " + playersById.size());
    PLogger.println("clans = " + clans.size());
    PLogger.println("grandExchange = " + grandExchange.size());
  }

  public static ResponseServer getInstance() {
    return instance;
  }

  public static void init() {
    isLocal = Settings.getInstance().isLocal();
    if (!isLocal) {
      FileManager.loadSql();
    }
    String ip = Settings.getInstance().getResponseIP().split(":")[0];
    int port = Integer.parseInt(Settings.getInstance().getResponseIP().split(":")[1]);
    if (!Settings.getInstance().isWithResponseServer()) {
      ip = Settings.getInstance().getWorldIP();
    }
    instance = new ResponseServer(ip, port);
  }

  public static void main(String[] args) {
    try {
      try {
        Settings.setInstance(FileManager.fromJson(ResponseServer.class.getResourceAsStream(args[0]),
            Settings.class));
      } catch (Exception resourceError) {
        Settings.setInstance(FileManager.fromJson(new File(args[0]), Settings.class));
      }
      init();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
