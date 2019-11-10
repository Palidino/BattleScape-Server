package com.palidinodh.osrsscript.world.event.pvptournament;

import java.util.ArrayList;
import java.util.List;
import com.palidinodh.osrscore.Main;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.Controller;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.dialogue.Scroll;
import com.palidinodh.osrscore.model.item.Item;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.player.PCombat;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.util.RequestManager;
import com.palidinodh.osrscore.world.WorldEventHooks;
import com.palidinodh.osrsscript.player.plugin.clanwars.ClanWarsPC;
import com.palidinodh.osrsscript.player.plugin.clanwars.ClanWarsPlugin;
import com.palidinodh.osrsscript.player.plugin.clanwars.state.PlayerState;
import com.palidinodh.osrsscript.world.event.pvptournament.dialogue.AdminCofferDialogue;
import com.palidinodh.osrsscript.world.event.pvptournament.dialogue.CofferDialogue;
import com.palidinodh.osrsscript.world.event.pvptournament.dialogue.DonateItemDialogue;
import com.palidinodh.osrsscript.world.event.pvptournament.prize.CustomPrize;
import com.palidinodh.osrsscript.world.event.pvptournament.prize.DefaultPrize;
import com.palidinodh.osrsscript.world.event.pvptournament.prize.Prize;
import com.palidinodh.osrsscript.world.event.pvptournament.state.IdleState;
import com.palidinodh.osrsscript.world.event.pvptournament.state.LobbyState;
import com.palidinodh.osrsscript.world.event.pvptournament.state.State;
import com.palidinodh.random.PRandom;
import com.palidinodh.rs.setting.SqlUserRank;
import com.palidinodh.util.PEvent;
import com.palidinodh.util.PNumber;
import com.palidinodh.util.PTime;
import lombok.Getter;
import lombok.Setter;
import lombok.var;

public class PvpTournament extends PEvent implements WorldEventHooks {
  public static final boolean DISABLED = false;
  public static final Tile LOBBY_TILE = new Tile(3079, 3479);
  public static final Tile ARENA_TILE_1 = new Tile(2594, 5406, 1),
      ARENA_TILE_2 = new Tile(2594, 5416, 1);
  public static final String[] TIME = {"0:30", "2:30", "4:30", "6:30", "8:30", "10:30", "12:30",
      "14:30", "16:30", "18:30", "20:30", "22:30"};
  public static final int MAX_TIME = (int) PTime.minToTick(60),
      MAX_ROUND_TIME = (int) PTime.minToTick(5);
  public static final int LOBBY_JOIN_TIME = 200;
  public static final int MINIMUM_PLAYERS = 4;
  public static final int TIME_BETWEEN_ROUNDS = 25;
  public static final int FIGHT_COUNTDOWN = 17;
  public static final int CUSTOM_COINS_MINIMUM = 5_000_000;

  @Getter
  private List<Mode> recentModes = new ArrayList<>();
  @Getter
  @Setter
  private transient Controller controller;
  @Getter
  @Setter
  private transient Mode mode;
  @Getter
  @Setter
  private transient Prize prize = new DefaultPrize(false);
  @Getter
  private transient List<Player> players = new ArrayList<>();
  @Getter
  @Setter
  private transient String viewerList = "";
  @Getter
  @Setter
  private transient State state = new IdleState(this);

  @Override
  public Object script(String name, Object... args) {
    if (name.equals("pvp_tournament_message")) {
      return state.getMessage();
    }
    return null;
  }

  @Override
  public void execute() {
    state.execute();
  }

  @Override
  public boolean widgetOnMapObjectHook(Player player, int widgetId, int childId, int slot,
      int itemId, MapObject mapObject) {
    if (widgetId == WidgetId.INVENTORY) {
      switch (mapObject.getId()) {
        case ObjectId.COFFER:
          if (!DonatableItems.isDonatable(itemId)) {
            player.getGameEncoder().sendMessage("The coffer won't take this item.");
            break;
          }
          player.openDialogue(new DonateItemDialogue(player, player.getInventory().getItem(slot)));
          break;
      }
    }
    return false;
  }

  @Override
  public boolean mapObjectOptionHook(Player player, int index, MapObject mapObject) {
    switch (mapObject.getId()) {
      case ObjectId.COFFER:
        if (player.getRights() == Player.RIGHTS_ADMIN
            || player.isUsergroup(SqlUserRank.SENIOR_MODERATOR)
            || player.isUsergroup(SqlUserRank.COMMUNITY_MANAGER)) {
          player.openDialogue(new AdminCofferDialogue(player));
        } else {
          player.openDialogue(new CofferDialogue(player));
        }
        return true;
      case ObjectId.GATE_26081:
      case ObjectId.GATE_26082:
        addPlayer(player);
        return true;
    }
    return false;
  }

  public void addPlayer(Player player) {
    if (!player.getInventory().isEmpty() || !player.getEquipment().isEmpty()) {
      player.getGameEncoder().sendMessage("No items can be taken beyond this point.");
      return;
    }
    if (!(state instanceof LobbyState)) {
      player.getGameEncoder().sendMessage("The lobby is currently closed.");
      return;
    }
    if (players.contains(player)) {
      return;
    }
    for (var aPlayer : players) {
      if (Main.adminPrivledges(player)) {
        break;
      }
      if (!player.getIP().equals(aPlayer.getIP())) {
        continue;
      }
      player.getGameEncoder()
          .sendMessage("A player with your IP has already joined the tournament.");
      return;
    }
    var plugin = player.getPlugin(ClanWarsPlugin.class);
    plugin.setState(PlayerState.TOURNAMENT);
    plugin.setRules(mode.getRules());
    player.setController(new ClanWarsPC());
    player.restore();
    player.getCombat().setSpecialAttackAmount(PCombat.MAX_SPECIAL_ATTACK);
    player.getSkills().setCombatLevel();
    player.getWidgetManager().removeInteractiveWidgets();
    player.getMovement().teleport(LOBBY_TILE);
    players.add(player);
    if (mode != null) {
      player.getMagic().setSpellbook(mode.getSpellbook());
      player.getLoadout().openSelection();
    }
  }

  public void removePlayer(Player player) {
    players.remove(player);
  }

  public void checkPrizes(Player player, boolean isWinner) {
    var isCustom = !(prize instanceof DefaultPrize);
    var plugin = player.getPlugin(ClanWarsPlugin.class);
    if (isWinner) {
      plugin.incrimentTournamentWins();
      if (PRandom.randomE(240) == 0) {
        player.getBank().add(new Item(ItemId.RAINBOW_PARTYHAT, 1));
        player.getWorld().sendItemDropNews(player, ItemId.RAINBOW_PARTYHAT, "a tournament");
      }
    }
    if (prize == null) {
      return;
    }
    var position = players.size();
    var points = getPoints(position);
    if (points > 0 && !isCustom) {
      plugin.setPoints(PNumber.addInt(plugin.getPoints(), points));
    }
    var prizes = prize.getItems(position);
    if (prizes == null || prizes.isEmpty()) {
      return;
    }
    var lines = new ArrayList<String>();
    for (var it = prizes.iterator(); it.hasNext();) {
      var prize = it.next();
      it.remove();
      if (!player.isGameModeNormal() && !player.isGameModeHard()
          && prize.getId() != ItemId.BOND_32318 && prize.getId() != ItemId.PUMPKIN_TOKEN_32338) {
        continue;
      }
      var prizeAmount = prize.getAmount();
      if (prize.getId() == ItemId.BOND_32318) {
        player.getBonds().setPouch(player.getBonds().getPouch() + prizeAmount);
      } else {
        player.getBank().add(new Item(prize.getId(), prizeAmount));
      }
      lines.add(prize.getName() + " x" + PNumber.formatNumber(prizeAmount));
      RequestManager.addPlayerLog("clanwarstournament/" + player.getLogFilename(),
          "[" + player.getId() + "; " + player.getIP() + "] " + player.getUsername() + " won ["
              + prize.getId() + "] " + prize.getName() + " x" + PNumber.formatNumber(prizeAmount)
              + " in the tournament.");
    }
    Scroll.open(player, "#" + (position + 1) + ": Rewards", lines);
    player.getGameEncoder()
        .sendMessage("Your rewards have been placed in your bank or bond pouch.");
  }

  public void teleportViewing(Player player, String selected) {
    if (player.isLocked() || player.getMovement().getTeleporting()
        || player.getMovement().getTeleported()) {
      return;
    }
    var options = viewerList.split("\\|\\|");
    for (var i = 0; i < options.length; i++) {
      if (selected.equals(options[i])) {
        player.getPlugin(ClanWarsPlugin.class).teleportViewing(i);
        return;
      }
    }
    player.getPlugin(ClanWarsPlugin.class).teleportViewing(-1);
  }

  public void viewPrizes(Player player) {
    var lines = new ArrayList<String>();
    for (var i = 0; i < 10; i++) {
      var prizes = prize.getItems(i);
      if (prizes == null || prizes.isEmpty()) {
        continue;
      }
      lines.add("<col=004080>Placing #" + (i + 1) + "</col>");
      for (var item : prizes) {
        lines.add(item.getName() + " x" + PNumber.formatNumber(item.getAmount()));
      }
    }
    Scroll.open(player, "Donations", lines);
  }

  public void selectCustomMode(Player player, int index) {
    if (index < 0 || index > Mode.MODES.size()) {
      return;
    }
    if (mode != null) {
      player.getGameEncoder().sendMessage("A mode has already been selected.");
      return;
    }
    mode = Mode.MODES.get(index);
    if (prize instanceof DefaultPrize) {
      prize = new CustomPrize();
    }
  }

  public boolean donateItem(Player player, int itemId, int placing) {
    var isDefault = prize instanceof DefaultPrize;
    var isCustomizable = state instanceof IdleState
        && (prize instanceof DefaultPrize || prize instanceof CustomPrize)
        || state instanceof LobbyState && prize instanceof CustomPrize;
    if (player.getRights() == 0 && !player.isUsergroup(SqlUserRank.COMMUNITY_MANAGER)) {
      player.getGameEncoder().sendMessage("This feature is currently limited to staff.");
      return false;
    }
    if (player.getBank().needsPinInput(false)) {
      return false;
    }
    if (!DonatableItems.isDonatable(itemId)) {
      player.getGameEncoder().sendMessage("You can't donate this item.");
      return false;
    }
    if (!isCustomizable) {
      player.getGameEncoder().sendMessage("You can't donate items right now.");
      return false;
    }
    if (isDefault && placing > 0) {
      player.getGameEncoder().sendMessage("The first donation must be given to first place.");
      return false;
    }
    var item = player.getInventory().getItem(player.getInventory().getSlotById(itemId));
    if (itemId == -1 || item == null) {
      player.getGameEncoder().sendMessage("Unable to find item.");
      return false;
    }
    var itemAmount = item.getAmount();
    if (isDefault && (itemId != ItemId.COINS || itemAmount < CUSTOM_COINS_MINIMUM)) {
      player.getGameEncoder().sendMessage("The first donation must be at least "
          + PNumber.formatNumber(CUSTOM_COINS_MINIMUM) + " coins.");
      return false;
    }
    if (isDefault) {
      prize = new CustomPrize();
    }
    prize.addItem(placing, new Item(item));
    player.getInventory().deleteItem(item);
    player.getWorld().sendClanWarsTournamentMessage(player.getUsername() + " has donated "
        + item.getName() + " x" + PNumber.formatNumber(itemAmount) + " to the tournament.");
    RequestManager.addPlayerLog("clanwarstournament/" + player.getLogFilename(),
        "[" + player.getId() + "; " + player.getIP() + "] " + player.getUsername() + " donated ["
            + item.getId() + "] " + item.getName() + " x" + PNumber.formatNumber(itemAmount)
            + " to the tournament.");
    return true;
  }

  public void sendWidgetText(Player player) {
    if (player.getWidgetManager().getOverlay() != WidgetId.LMS_LOBBY_OVERLAY) {
      return;
    }
    var plugin = player.getPlugin(ClanWarsPlugin.class);
    var opponent = plugin.getOpponent();
    player.getGameEncoder().sendWidgetText(WidgetId.LMS_LOBBY_OVERLAY, 6, state.getMessage());
    if (player.inClanWarsBattle()) {
      player.getGameEncoder().sendWidgetText(WidgetId.LMS_LOBBY_OVERLAY, 8,
          "Remaining: " + PTime.ticksToDuration(state.getTime()));
      String opponentName =
          opponent != null && !opponent.isLocked() ? opponent.getUsername() : "None";
      player.getGameEncoder().sendWidgetText(WidgetId.LMS_LOBBY_OVERLAY, 10,
          "Opponent: " + opponentName);
    } else {
      player.getGameEncoder().sendWidgetText(WidgetId.LMS_LOBBY_OVERLAY, 8,
          "Mode: " + (mode != null ? mode.getName() : "None"));
      player.getGameEncoder().sendWidgetText(WidgetId.LMS_LOBBY_OVERLAY, 10,
          "Wins/Points: " + PNumber.abbreviateNumber(plugin.getTournamentWins()) + "/"
              + PNumber.abbreviateNumber(plugin.getPoints()));
    }
  }

  public int getPoints(int position) {
    switch (position) {
      case 0:
        return 5;
      case 1:
        return 4;
      case 2:
      case 3:
        return 3;
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
        return 2;
      default:
        return 1;
    }
  }

  public static String[] getModeNames() {
    var names = new String[Mode.MODES.size()];
    for (var i = 0; i < Mode.MODES.size(); i++) {
      names[i] = Mode.MODES.get(i).getName();
    }
    return names;
  }

  public static int[] getNextTime() {
    if (TIME == null) {
      return null;
    }
    var currentHour = PTime.getHour24();
    var currentMinute = PTime.getMinute();
    for (var i = 0; i < TIME.length; i++) {
      var time = TIME[i].split(":");
      var hour = Integer.parseInt(time[0]);
      var minute = Integer.parseInt(time[1]);
      if (currentHour > hour || currentHour == hour && currentMinute > minute) {
        continue;
      }
      return new int[] {hour, minute};
    }
    var time = TIME[0].split(":");
    return new int[] {Integer.parseInt(time[0]), Integer.parseInt(time[1])};
  }
}
