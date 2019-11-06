package com.palidinodh.osrsscript.world.event.pvptournament.state;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import com.palidinodh.osrscore.Main;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.Controller;
import com.palidinodh.osrscore.model.Movement;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.item.Item;
import com.palidinodh.osrscore.model.player.ItemCharges;
import com.palidinodh.osrscore.model.player.PCombat;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrsscript.player.plugin.clanwars.ClanWarsPlugin;
import com.palidinodh.osrsscript.world.event.pvptournament.PvpTournament;
import com.palidinodh.util.PTime;
import lombok.var;

public class RoundsState implements State {
  private enum State {
    LOBBY, FIGHT
  }

  private PvpTournament tournament;
  private State state = State.FIGHT;
  private int remainingTime = PvpTournament.MAX_TIME;
  private int timer = PvpTournament.TIME_BETWEEN_ROUNDS;
  private int round;
  private int firstWinner = -1;
  private Map<Player, Item[]> playersEquipment = new HashMap<>();
  private Map<Player, Item[]> playersInventory = new HashMap<>();

  public RoundsState(PvpTournament tournament) {
    this.tournament = tournament;
    tournament.setController(Controller.getDefaultController(PvpTournament.ARENA_TILE_1));
    for (var player : tournament.getPlayers()) {
      player.getController().setInstance(tournament.getController());
      player.getGameEncoder().sendPlayerOption("Attack", 2, false);
      player.getWidgetManager().removeInteractiveWidgets();
      player.getWidgetManager().removeOverlay();
      player.getPlugin(ClanWarsPlugin.class).setCountdown(timer);
    }
  }

  @Override
  public String getMessage() {
    return "Round: " + round;
  }

  @Override
  public int getTime() {
    return timer;
  }

  @Override
  public void execute() {
    remainingTime--;
    timer--;
    if (state == State.LOBBY) {
      lobbyTick();
    } else if (state == State.FIGHT) {
      fightTick();
    }
    if (remainingTime == 0 || tournament.getPlayers().size() <= 1) {
      if (tournament.getPlayers().size() == 1) {
        var player = tournament.getPlayers().get(0);
        player.getInventory().clear();
        player.getEquipment().clear();
        player.getController().stop();
        tournament.checkPrizes(player, true);
        player.getWorld().sendPvpNews(
            player.getMessaging().getIconImage() + player.getUsername() + " won the tournament!");
      }
      while (!tournament.getPlayers().isEmpty()) {
        tournament.getPlayers().get(0).getController().stop();
      }
      tournament.setState(new IdleState(tournament));
    }
  }

  public void lobbyTick() {
    for (var player : tournament.getPlayers()) {
      var brewIds = new int[] {ItemId.SARADOMIN_BREW_1, ItemId.SARADOMIN_BREW_2,
          ItemId.SARADOMIN_BREW_3, ItemId.SARADOMIN_BREW_4};
      for (var brewDeleteId : brewIds) {
        if (!player.getInventory().hasItem(brewDeleteId)) {
          continue;
        }
        var brewCount = 0;
        for (var brewId : brewIds) {
          brewCount += player.getInventory().getCount(brewId);
        }
        if (brewCount <= tournament.getMode().getBrewCap()) {
          break;
        }
        player.getInventory().deleteItem(brewDeleteId,
            brewCount - tournament.getMode().getBrewCap());
      }
    }
    if (timer > 0) {
      return;
    }
    for (var i = 0; i < tournament.getPlayers().size(); i++) {
      var player = tournament.getPlayers().get(i);
      if (player.getAppearance().getNpcId() == Movement.VIEWING_NPC_ID) {
        player.getController().stop();
        i--;
      } else if (!player.isVisible()) {
        tournament.getPlayers().remove(i--);
      }
    }
    state = State.FIGHT;
    timer = PvpTournament.MAX_ROUND_TIME;
    Collections.shuffle(tournament.getPlayers());
    var height = PvpTournament.ARENA_TILE_1.getHeight();
    var viewerList = "";
    if (firstWinner != -1) {
      var previousFirstWinner = Main.getWorld().getPlayerById(firstWinner);
      if (previousFirstWinner != null && tournament.getPlayers().contains(previousFirstWinner)) {
        tournament.getPlayers().remove(previousFirstWinner);
        tournament.getPlayers().add(previousFirstWinner);
      }
    }
    for (var i = 0; i < tournament.getPlayers().size(); i += 2) {
      var player1 = tournament.getPlayers().get(i);
      if (i == 0) {
        player1.getController().removeMapItems(10324);
      }
      if (i == tournament.getPlayers().size() - 1) {
        player1.getGameEncoder()
            .sendMessage("<col=ff0000>You don't have an opponent for this round.");
        break;
      }
      var player2 = tournament.getPlayers().get(i + 1);
      startRound(player1, player2, new Tile(PvpTournament.ARENA_TILE_1).setHeight(height));
      startRound(player2, player1, new Tile(PvpTournament.ARENA_TILE_2).setHeight(height));
      height += 4;
      if (i < 22) {
        viewerList += player1.getUsername() + " vs " + player2.getUsername() + "||";
      }
    }
    if (viewerList.length() > 2) {
      viewerList = viewerList.substring(0, viewerList.length() - 2);
    }
    tournament.setViewerList(viewerList);
    firstWinner = -1;
  }

  public void startRound(Player player, Player opponent, Tile teleportTile) {
    var plugin = player.getPlugin(ClanWarsPlugin.class);
    plugin.setOpponent(opponent);
    player.getGameEncoder()
        .sendMessage("<col=ff0000>Your opponent is " + opponent.getUsername() + "!");
    player.setCombatImmunity(PvpTournament.FIGHT_COUNTDOWN);
    plugin.setTournamentFightDelay(PvpTournament.FIGHT_COUNTDOWN);
    player.getCombat().setDamageInflicted(0);
    player.getWidgetManager().removeInteractiveWidgets();
    player.getCharges().setRingOfRecoil(ItemCharges.RING_OF_RECOIL);
    player.restore();
    player.getCombat().setSpecialAttackAmount(PCombat.MAX_SPECIAL_ATTACK);
    playersEquipment.put(player, player.getEquipment().copy());
    playersInventory.put(player, player.getInventory().copy());
    player.getMovement().teleport(teleportTile);
    player.getGameEncoder().sendHideWidget(WidgetId.LMS_LOBBY_OVERLAY, 0, true);
    player.getGameEncoder().sendPlayerOption("Attack", 2, false);
  }

  public void fightTick() {
    boolean matchesRemain = false;
    for (var i = 0; i < tournament.getPlayers().size(); i++) {
      var player = tournament.getPlayers().get(i);
      var plugin = player.getPlugin(ClanWarsPlugin.class);
      tournament.sendWidgetText(player);
      if (plugin.getOpponent() != null && !tournament.getPlayers().contains(plugin.getOpponent())) {
        plugin.setOpponent(null);
      }
      if (timer == 0 && plugin.getOpponent() != null) {
        var opponent = plugin.getOpponent();
        var loser =
            player.getCombat().getDamageInflicted() < opponent.getCombat().getDamageInflicted()
                ? player
                : opponent;
        var winner = loser == player ? opponent : player;
        loser.getController().stop();
        loser.getGameEncoder().sendMessage(
            "<col=ff0000>You have run out of time and dealt less damage than your opponent! You have been disqualified.");
        winner.getGameEncoder().sendMessage(
            "<col=ff0000>You have run out of time and dealt more damage than your opponent! Your opponent has been disqualified.");
        winner.getPlugin(ClanWarsPlugin.class).setOpponent(null);
        if (player == loser) {
          i--;
        }
      }
      if (plugin.getOpponent() != null) {
        matchesRemain = true;
      } else if (!player.isLocked() && player.inClanWarsBattle()) {
        player.getEquipment().setItems(playersEquipment.get(player));
        player.getInventory().setItems(playersInventory.get(player));
        player.getEquipment().weaponUpdate(true);
        player.restore();
        player.getCombat().setSpecialAttackAmount(PCombat.MAX_SPECIAL_ATTACK);
        player.getMovement().teleport(PvpTournament.LOBBY_TILE);
        player.getGameEncoder().sendHideWidget(WidgetId.LMS_LOBBY_OVERLAY, 0, false);
        if (firstWinner == -1) {
          firstWinner = player.getId();
        }
      }
    }
    if (!matchesRemain) {
      state = State.LOBBY;
      timer = PvpTournament.TIME_BETWEEN_ROUNDS;
      round++;
      tournament.setViewerList("");
      for (var player : tournament.getPlayers()) {
        if (timer < 100) {
          player.getGameEncoder().sendMessage(
              "<col=ff0000>The next round will begin in " + PTime.tickToSec(timer) + " seconds.");
        } else if (timer < 200) {
          player.getGameEncoder().sendMessage("<col=ff0000>The next round will begin in 1 minute.");
        } else {
          player.getGameEncoder().sendMessage(
              "<col=ff0000>The next round will begin in " + PTime.tickToMin(timer) + " minutes.");
        }
        if (player.getMovement().isViewing()) {
          player.getMovement().stopViewing();
          player.getWidgetManager().removeInteractiveWidgets();
        }
      }
    }
  }
}
