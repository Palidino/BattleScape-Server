package com.palidinodh.osrsscript.player.plugin.clanwars;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.palidinodh.osrscore.io.cache.id.ScriptId;
import com.palidinodh.osrscore.io.cache.id.VarbitId;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.map.ObjectDef;
import com.palidinodh.osrscore.model.player.Messaging;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.PlayerPlugin;
import com.palidinodh.osrsscript.player.plugin.clanwars.dialogue.JoinBattleDialogue;
import com.palidinodh.osrsscript.player.plugin.clanwars.rule.Arena;
import com.palidinodh.osrsscript.player.plugin.clanwars.rule.Rule;
import com.palidinodh.osrsscript.player.plugin.clanwars.rule.RuleOption;
import com.palidinodh.osrsscript.player.plugin.clanwars.state.BarrierState;
import com.palidinodh.osrsscript.player.plugin.clanwars.state.CompletedState;
import com.palidinodh.osrsscript.player.plugin.clanwars.state.PlayerState;
import com.palidinodh.osrsscript.world.event.pvptournament.PvpTournament;
import com.palidinodh.util.PTime;
import lombok.Getter;
import lombok.Setter;
import lombok.var;

public class ClanWarsPlugin extends PlayerPlugin {
  public static final int COUNT_DOWN = 200;
  public static final int ORB_OBJECT_ID = 26742;

  private transient Player player;
  @Getter
  @Setter
  private transient Player opponent;
  @Getter
  @Setter
  private transient Player teammate;
  @Getter
  @Setter
  private transient PlayerState state = PlayerState.NONE;
  @Getter
  @Setter
  private transient CompletedState completed;
  @Getter
  @Setter
  private transient int countdown;
  @Getter
  public transient int[] rules;
  @Getter
  private transient boolean isTop;
  @Getter
  private transient int time;
  @Getter
  private transient int totalKills;
  private transient boolean inClanWarsChallengeArea;
  private transient boolean inTournamentPlayerStateArea;
  @Setter
  private transient int tournamentFightDelay;
  @Getter
  private int tournamentWins;
  @Getter
  @Setter
  private int points;

  @Override
  public void loadLegacy(Map<String, Object> map) {
    if (map.containsKey("clanwars.competitiveTournamentWins")) {
      tournamentWins = (int) map.get("clanwars.competitiveTournamentWins");
    }
  }

  @Override
  public void login() {
    player = getPlayer();
  }

  @Override
  public int getCurrency(String identifier) {
    if (identifier.equals("tournament points")) {
      return points;
    }
    return 0;
  }

  @Override
  public void changeCurrency(String identifier, int amount) {
    if (identifier.equals("tournament points")) {
      points += amount;
    }
  }

  @Override
  public void tick() {
    if (inClanWarsChallengeArea != player.inClanWarsChallengeArea()) {
      inClanWarsChallengeArea = player.inClanWarsChallengeArea();
      player.getGameEncoder().sendPlayerOption(inClanWarsChallengeArea ? "Challenge" : "null", 1,
          false);
    }
    var inClanWarsTournamentStatusArea = player.inClanWarsTournamentStatusArea();
    if (inTournamentPlayerStateArea
        || inTournamentPlayerStateArea != inClanWarsTournamentStatusArea) {
      inTournamentPlayerStateArea = inClanWarsTournamentStatusArea;
      if (inTournamentPlayerStateArea) {
        if (player.getWidgetManager().getOverlay() != WidgetId.LMS_LOBBY_OVERLAY) {
          player.getWidgetManager().sendOverlay(WidgetId.LMS_LOBBY_OVERLAY);
          player.getGameEncoder().sendWidgetText(WidgetId.LMS_LOBBY_OVERLAY, 7, "");
          player.getGameEncoder().sendWidgetText(WidgetId.LMS_LOBBY_OVERLAY, 9, "");
          player.getGameEncoder().sendWidgetText(WidgetId.LMS_LOBBY_OVERLAY, 11, "");
        }
      } else if (!inTournamentPlayerStateArea
          && player.getWidgetManager().getOverlay() == WidgetId.LMS_LOBBY_OVERLAY) {
        player.getWidgetManager().removeOverlay();
      }
    }
    if (countdown > 0) {
      countdown--;
      if (state == PlayerState.BATTLE) {
        if (countdown == 4) {
          loadBarrier(BarrierState.DROP);
        } else if (countdown == 0) {
          loadBarrier(BarrierState.DELETE);
          player.getGameEncoder().sendMessage("<col=ff0000>The war has begun!");
        }
      }
    }
    if (state == PlayerState.BATTLE) {
      time++;
    }
    if (tournamentFightDelay > 0) {
      tournamentFightDelay--;
      if (tournamentFightDelay == 0) {
        player.setForceMessage("FIGHT!");
      } else if (tournamentFightDelay % 2 == 0) {
        player.setForceMessage(PTime.tickToSec(tournamentFightDelay) + "");
      }
    }
  }

  @Override
  public boolean widgetHook(int option, int widgetId, int childId, int slot, int itemId) {
    if (player.isLocked()) {
      return false;
    }
    if (widgetId == WidgetId.CLAN_WARS_OPTIONS) {
      Rule ruleBySlot = null;
      switch (childId) {
        case 27:
          Stages.acceptRuleSelection(player, this);
          break;
        case 6:
          if (slot >= 24) {
            Stages.sendVarbits(player, this);
            player.getGameEncoder().sendMessage("This option is currently disabled.");
            break;
          }
          changeRule(Rule.GAME_END, slot / 3);
          break;
        case 10:
          int mapId = slot / 3;
          if (Arena.get(mapId) == null || Arena.get(mapId).getArenaTop().getX() == 0) {
            Stages.sendVarbits(player, this);
            player.getGameEncoder().sendMessage("This option is currently disabled.");
            break;
          }
          changeRule(Rule.ARENA, slot / 3);
          break;
        case 14:
          ruleBySlot = Rule.STRAGGLERS;
          break;
        case 17:
          Rule rule = null;
          if (slot == 0) {
            rule = Rule.IGNORE_FREEZING;
          } else if (slot == 3) {
            rule = Rule.PJ_TIMER;
          } else if (slot == 6) {
            rule = Rule.SINGLE_SPELLS;
          } else if (slot == 9) {
            rule = Rule.ALLOW_TRIDENT_IN_PVP;
          }
          if (rule != null) {
            if (ruleSelected(rule, RuleOption.DISABLED)) {
              changeRule(rule, RuleOption.ALLOWED);
            } else {
              changeRule(rule, RuleOption.DISABLED);
            }
          }
          break;
        case 19:
          ruleBySlot = Rule.MELEE;
          break;
        case 20:
          ruleBySlot = Rule.RANGING;
          break;
        case 21:
          ruleBySlot = Rule.MAGIC;
          break;
        case 22:
          ruleBySlot = Rule.PRAYER;
          break;
        case 23:
          ruleBySlot = Rule.FOOD;
          break;
        case 24:
          ruleBySlot = Rule.DRINKS;
          break;
        case 25:
          ruleBySlot = Rule.SPECIAL_ATTACKS;
          break;
      }
      if (ruleBySlot != null) {
        changeRule(ruleBySlot, slot / 3 - 1);
      }
      return true;
    } else if (widgetId == WidgetId.CLAN_WARS_CONFIRM) {
      switch (childId) {
        case 6:
          Stages.acceptRuleConfirmation(player, this);
          break;
      }
      return true;
    } else if (widgetId == WidgetId.TOURNAMENT_VIEWER) {
      if (player.getMovement().getTeleporting() || player.getMovement().getTeleported()
          || player.getMovement().isViewing()) {
        return true;
      }
      switch (childId) {
        case 8:
          teleportViewing(-1);
          break;
      }
      return true;
    }
    return false;
  }

  @Override
  public boolean mapObjectOptionHook(int option, MapObject mapObject) {
    switch (mapObject.getId()) {
      case 26743: // Viewing orb
        teleportViewing(0);
        return true;
      case 26642: // Challenge portal
      case 26643: // Challenge portal
      case 26644: // Challenge portal
        player.openDialogue(new JoinBattleDialogue(player, this));
        return true;
    }
    return false;
  }

  @Override
  public boolean playerOptionHook(int option, Player player2) {
    if (option == 0 && player.inClanWarsChallengeArea() && player2.inClanWarsChallengeArea()) {
      if (state != PlayerState.NONE
          || player2.getPlugin(ClanWarsPlugin.class).getState() != PlayerState.NONE) {
        return true;
      }
      if (!player.getMessaging().canClanChatEvent()) {
        player.getGameEncoder()
            .sendMessage("Your Clan Chat privledges aren't high enough to do that.");
        return true;
      } else if (!player2.getMessaging().canClanChatEvent()) {
        player.getGameEncoder()
            .sendMessage("Their Clan Chat privledges aren't high enough to do that.");
        return true;
      }
      opponent = player2;
      if (player == player2.getPlugin(ClanWarsPlugin.class).getOpponent()) {
        Stages.openRuleSelection(player, this);
        Stages.openRuleSelection(player2, player2.getPlugin(ClanWarsPlugin.class));
      } else {
        player.getGameEncoder().sendMessage("Sending Clan Wars challenge...");
        player2.getGameEncoder().sendMessage(
            player.getUsername() + " wishes to challenge your clan to a Clan War.",
            Messaging.CHAT_TYPE_DUEL, player.getUsername());
      }
      return true;
    }
    return false;
  }

  public void startWar(boolean startInstance, boolean isTop) {
    if (opponent == null) {
      return;
    }
    state = PlayerState.BATTLE;
    countdown = COUNT_DOWN;
    time = 0;
    this.isTop = isTop;
    player.rejuvenate();
    player.setController(new ClanWarsPC());
    if (startInstance) {
      player.getController().instance();
    } else {
      player.getController().setInstance(opponent.getController());
    }
    loadBarrier(BarrierState.LOAD);
    teleport();
    player.getWidgetManager().removeInteractiveWidgets();
  }

  public void joinWar() {
    if (teammate == null) {
      return;
    }
    var plugin = teammate.getPlugin(ClanWarsPlugin.class);
    isTop = plugin.isTop();
    countdown = plugin.getCountdown();
    time = plugin.getTime();
    totalKills = plugin.getTotalKills();
    player.setController(new ClanWarsPC());
    player.getController().setInstance(teammate.getController());
    player.getWidgetManager().removeInteractiveWidgets();
  }

  public void cancel() {
    opponent = null;
    teammate = null;
    state = PlayerState.NONE;
    rules = null;
    isTop = false;
    time = 0;
    countdown = 0;
    completed = CompletedState.NONE;
    totalKills = 0;
  }

  public void teleport() {
    if (rules == null) {
      return;
    }
    player.getMovement().teleport(getArena().getArenaTile(isTop));
  }

  public void teleportViewing() {
    if (rules == null) {
      return;
    }
    player.getMovement().teleport(getArena().getViewTile(isTop));
  }

  public void loadBarrier(BarrierState stage) {
    List<MapObject> barriers = null;
    var alreadyLoaded = false;
    if (getArena().getBarrierObjectId() == -1) {
      return;
    }
    for (var i = getArena().getBarrierStartX(); i <= getArena().getBarrierEndX(); i++) {
      if (ruleSelected(Rule.ARENA, RuleOption.SYLVAN_GLADE) && i > 3419 && i < 3428) {
        continue;
      } else if (ruleSelected(Rule.ARENA, RuleOption.FORSAKEN_QUARRY) && i > 3415 && i < 3432) {
        continue;
      }
      var barrier = new MapObject(getArena().getBarrierObjectId(), i, getArena().getBarrierY(), 0,
          10, MapObject.getRandomDirection());
      if (stage == BarrierState.DROP) {
        barrier.setId(getArena().getBarrierObjectId() + 1);
      } else if (stage == BarrierState.DELETE) {
        barrier.setId(-1);
      }
      var region = player.getController().getRegion(barrier.getRegionId(), true);
      var current = region.getSolidMapObject(i, getArena().getBarrierY(), 0);
      if (current != null && current.getId() == barrier.getId()) {
        alreadyLoaded = true;
        break;
      }
      if (stage != BarrierState.DELETE && current != null
          && current.getId() != getArena().getBarrierObjectId()
          && current.getId() + 1 != getArena().getBarrierObjectId()) {
        continue;
      }
      if (current != null) {
        barrier.setDirection(current.getDirection());
      }
      if (current != null) {
        region.addMapObject(new MapObject(-1, current));
      }
      if (stage != BarrierState.DELETE) {
        region.addMapObject(barrier);
      }
      if (barriers == null) {
        barriers = new ArrayList<>();
      }
      barriers.add(barrier);
    }
    if (alreadyLoaded) {
      return;
    }
    var players = player.getController().getPlayers();
    if (players.isEmpty() || barriers == null) {
      return;
    }
    for (var nearbyPlayer : players) {
      for (var barrier : barriers) {
        if (!nearbyPlayer.withinMapDistance(barrier)) {
          continue;
        }
        nearbyPlayer.getGameEncoder().sendMapObject(barrier);
      }
    }
  }

  public boolean ruleSelected(Rule rule, RuleOption option) {
    if (state == PlayerState.NONE || rules == null || rule == null || option == null) {
      return false;
    }
    return rules[rule.ordinal()] == option.getIndex();
  }

  public void changeRule(Rule rule, RuleOption option) {
    if (rule == null || option == null || !rule.hasOption(option)) {
      return;
    }
    changeRule(rule, option.getIndex());
  }

  public void changeRule(Rule rule, int slot) {
    if (state != PlayerState.RULE_SELECTION && state != PlayerState.ACCEPT_RULE_SELECTION) {
      return;
    }
    if (opponent == null || opponent.isLocked()) {
      return;
    }
    if (!player.getWidgetManager().hasWidget(WidgetId.CLAN_WARS_OPTIONS)
        || !opponent.getWidgetManager().hasWidget(WidgetId.CLAN_WARS_OPTIONS)) {
      return;
    }
    if (opponent.getPlugin(ClanWarsPlugin.class).getState() != PlayerState.RULE_SELECTION
        && opponent.getPlugin(ClanWarsPlugin.class)
            .getState() != PlayerState.ACCEPT_RULE_SELECTION) {
      return;
    }
    if (rule == null || slot < 0 || slot >= rule.getOptions().size()) {
      return;
    }
    rules[rule.ordinal()] = slot;
    opponent.getPlugin(ClanWarsPlugin.class).setRule(rule.ordinal(), slot);
    player.getGameEncoder().setVarbit(rule.getVarbit(), slot);
    opponent.getGameEncoder().setVarbit(rule.getVarbit(), slot);
    state = PlayerState.RULE_SELECTION;
    opponent.getPlugin(ClanWarsPlugin.class).setState(PlayerState.RULE_SELECTION);
  }

  public void sendBattleVarbits(int myTeamCount, int otherTeamCount, Player opponent) {
    player.getGameEncoder().setVarbit(VarbitId.CLAN_WARS_COUNTDOWN, countdown);
    player.getGameEncoder().setVarbit(VarbitId.CLAN_WARS_TEAMMATES, myTeamCount);
    player.getGameEncoder().setVarbit(VarbitId.CLAN_WARS_OPPONENTS, otherTeamCount);
    player.getGameEncoder().setVarbit(VarbitId.CLAN_WARS_TEAM_KILLS, totalKills);
    player.getGameEncoder().setVarbit(VarbitId.CLAN_WARS_OPPONENT_KILLS,
        opponent.getPlugin(ClanWarsPlugin.class).getTotalKills());
  }

  public void teleportViewing(int option) {
    if (player.isLocked() || player.getMovement().getTeleporting()
        || player.getMovement().getTeleported()) {
      return;
    }
    Tile tile = null;
    if (player.inClanWarsTournamentStatusArea()) {
      var viewerList = player.getWorld().getWorldEvent(PvpTournament.class).getViewerList();
      var activeLength = viewerList.split("\\|\\|").length;
      if (!player.getWidgetManager().hasWidget(WidgetId.TOURNAMENT_VIEWER)) {
        player.getWidgetManager().sendInventoryOverlay(WidgetId.TOURNAMENT_VIEWER);
        player.getGameEncoder().sendWidgetText(WidgetId.TOURNAMENT_VIEWER, 4, "");
        player.getGameEncoder().sendWidgetText(WidgetId.TOURNAMENT_VIEWER, 5, "");
      }
      player.getGameEncoder().sendScript(ScriptId.TOURNAMENT_VIEWER, viewerList);
      if (option < 0 || option >= activeLength) {
        return;
      }
      tile = new Tile(2594, 5411, 1 + option * 4);
    } else {
      if (state == PlayerState.NONE || state != PlayerState.VIEW || option < 0
          || getArena().getOrbs().length > 4) {
        return;
      }
      if (!player.getWidgetManager().hasWidget(WidgetId.CLAN_WARS_ORBS)) {
        player.getWidgetManager().sendInventoryOverlay(WidgetId.CLAN_WARS_ORBS);
      }
      player.getGameEncoder().sendScript(ScriptId.CLAN_WARS_ORBS,
          ObjectDef.getObjectDef(ORB_OBJECT_ID).getModelId(), option);
      tile = getArena().getOrbs()[option];
    }
    if (tile == null) {
      return;
    }
    player.getMovement().setViewing(tile.getX(), tile.getY(), tile.getHeight());
  }

  public Arena getArena() {
    return Arena.get(rules[Rule.ARENA.ordinal()]);
  }

  public RuleOption getRule(Rule rule) {
    if (state == PlayerState.NONE || rules == null || rule == null) {
      return null;
    }
    return rule.getOptions().get(rules[rule.ordinal()]);
  }

  public void setRule(int option, int slot) {
    rules[option] = slot;
  }

  public void setRules(int[] rules) {
    if (this.rules == null) {
      this.rules = new int[Rule.TOTAL];
    }
    System.arraycopy(rules, 0, this.rules, 0, Rule.TOTAL);
  }

  public void incrimentTournamentWins() {
    tournamentWins++;
  }

  public void incrimentTotalKills() {
    totalKills++;
  }
}
