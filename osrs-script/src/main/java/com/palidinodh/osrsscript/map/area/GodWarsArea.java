package com.palidinodh.osrsscript.map.area;

import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.VarbitId;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.map.Area;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.player.Prayer;
import com.palidinodh.osrscore.model.player.Skills;
import com.palidinodh.util.PTime;
import lombok.var;

public class GodWarsArea extends Area {
  private int armadylKillcount;
  private int bandosKillcount;
  private int zamorakKillcount;
  private int saradominKillcount;
  private int altarDelay;

  public GodWarsArea() {
    super(11346, 11347, 11578, 11602, 11603);
  }

  @Override
  public Object script(String identifier, Object... parameters) {
    var player = getPlayer();
    if (identifier.equals("increase_armadyl_killcount")) {
      armadylKillcount += player.isPremiumMember() ? 2 : 1;
    } else if (identifier.equals("increase_bandos_killcount")) {
      bandosKillcount += player.isPremiumMember() ? 2 : 1;
    } else if (identifier.equals("increase_saradomin_killcount")) {
      saradominKillcount += player.isPremiumMember() ? 2 : 1;
    } else if (identifier.equals("increase_zamorak_killcount")) {
      zamorakKillcount += player.isPremiumMember() ? 2 : 1;
    } else if (identifier.equals("get_armadyl_killcount")) {
      return armadylKillcount;
    } else if (identifier.equals("get_bandos_killcount")) {
      return bandosKillcount;
    } else if (identifier.equals("get_saradomin_killcount")) {
      return saradominKillcount;
    } else if (identifier.equals("get_zamorak_killcount")) {
      return zamorakKillcount;
    } else if (identifier.equals("clear_armadyl_killcount")) {
      if (armadylKillcount >= 40) {
        armadylKillcount -= 40;
      } else {
        player.getInventory().deleteItem(ItemId.ECUMENICAL_KEY);
      }
    } else if (identifier.equals("clear_bandos_killcount")) {
      if (bandosKillcount >= 40) {
        bandosKillcount -= 40;
      } else {
        player.getInventory().deleteItem(ItemId.ECUMENICAL_KEY);
      }
    } else if (identifier.equals("clear_saradomin_killcount")) {
      if (saradominKillcount >= 40) {
        saradominKillcount -= 40;
      } else {
        player.getInventory().deleteItem(ItemId.ECUMENICAL_KEY);
      }
    } else if (identifier.equals("clear_zamorak_killcount")) {
      if (zamorakKillcount >= 40) {
        zamorakKillcount -= 40;
      } else {
        player.getInventory().deleteItem(ItemId.ECUMENICAL_KEY);
      }
    }
    sendOverlay();
    return null;
  }

  @Override
  public void loadPlayer() {
    var player = getPlayer();
    player.getGameEncoder().setVarbit(VarbitId.GOD_WARS_ENTRANCE_ROPE, 1);
    player.getGameEncoder().setVarbit(VarbitId.GOD_WARS_SARADOMIN_FIRST_ROPE, 1);
    player.getGameEncoder().setVarbit(VarbitId.GOD_WARS_SARADOMIN_SECOND_ROPE, 1);
  }

  @Override
  public void unloadPlayer() {
    var player = getPlayer();
    player.getWidgetManager().removeFullOverlay();
  }

  @Override
  public void tickPlayer() {
    var player = getPlayer();
    if (player.getWidgetManager().getFullOverlay() != WidgetId.GOD_WARS_OVERLAY) {
      player.getWidgetManager().sendFullOverlay(WidgetId.GOD_WARS_OVERLAY);
      sendOverlay();
    }
    altarDelay -= altarDelay > 0 ? 1 : 0;
  }

  @Override
  public boolean mapObjectOptionHook(int index, MapObject mapObject) {
    var player = getPlayer();
    switch (mapObject.getId()) {
      case 26419: // Hole: entrance
        player.getMovement().ladderDownTeleport(new Tile(2882, 5311, 2));
        return true;
      case 26370: // Rope: exit
        player.getMovement().ladderUpTeleport(new Tile(2916, 3746, 0));
        return true;
      case 26371: // Rope: Saradomin
        player.getMovement().ladderUpTeleport(new Tile(2912, 5300, 2));
        return true;
      case 26375: // Rope: Saradomin
        player.getMovement().ladderUpTeleport(new Tile(2920, 5276, 1));
        return true;
      case 26380: // Pillar: Armadyl
        if (player.getY() >= 5279) {
          player.getMovement().teleport(new Tile(2872, 5269, 2));
        } else {
          player.getMovement().teleport(new Tile(2872, 5279, 2));
        }
        return true;
      case 26461: // Big door: Bandos
        if (player.getX() >= 2851) {
          player.getMovement().teleport(new Tile(2850, 5333, 2));
        } else {
          player.getMovement().teleport(new Tile(2851, 5333, 2));
        }
        return true;
      case 26518: // Ice bridge: Zamorak
        if (player.getY() <= 5333) {
          player.getMovement().teleport(new Tile(2885, 5345, 2));
        } else {
          player.getMovement().teleport(new Tile(2885, 5332, 2));
        }
        return true;
      case 26561: // Rock: Saradomin
        player.getMovement().ladderDownTeleport(new Tile(2915, 5300, 1));
        return true;
      case 26562: // Rock: Saradomin
        player.getMovement().ladderDownTeleport(new Tile(2919, 5274, 0));
        return true;
      case 26363: // Zamorak altar
      case 26364: // Saradomin altar
      case 26365: // Armadyl altar
      case 26366: // Bandos altar
        if (index == 0) {
          if (altarDelay > 0) {
            var message =
                "The gods blessed you recently. They will ignore your prayers for another ";
            var seconds = PTime.tickToSec(altarDelay);
            if (seconds > 60) {
              message += seconds / 60 + " minutes.";
            } else {
              message += seconds + " seconds.";
            }
            player.getGameEncoder().sendMessage(message);
            return true;
          }
          player.getPrayer().adjustPoints(player.getController().getLevelForXP(Skills.PRAYER));
          player.setAnimation(Prayer.PRAY_ANIMATION);
          player.getGameEncoder().sendMessage("You recharge your Prayer.");
          altarDelay = (int) PTime.minToTick(10);
        } else if (index == 1) {
          switch (mapObject.getId()) {
            case 26363: // Zamorak altar
              player.getMagic().standardTeleport(new Tile(2925, 5333, 2));
              player.getController().stopWithTeleport();
              break;
            case 26364: // Saradomin altar
              player.getMagic().standardTeleport(new Tile(2909, 5265));
              player.getController().stopWithTeleport();
              break;
            case 26365: // Armadyl altar
              player.getMagic().standardTeleport(new Tile(2839, 5294, 2));
              player.getController().stopWithTeleport();
              break;
            case 26366: // Bandos altar
              player.getMagic().standardTeleport(new Tile(2862, 5354, 2));
              player.getController().stopWithTeleport();
              break;
          }
        }
        return true;
      case 26502: // Big door: Armadyl
        if (player.getY() > 5294) {
          player.getGameEncoder().sendMessage("You must use the altar to leave.");
          return true;
        }
        player.openDialogue("bossinstance", 7);
        return true;
      case 26503: // Big door: Bandos
        if (player.getX() > 2862) {
          player.getGameEncoder().sendMessage("You must use the altar to leave.");
          return true;
        }
        player.openDialogue("bossinstance", 8);
        return true;
      case 26504: // Big door: Saradomin
        if (player.getX() < 2909) {
          player.getGameEncoder().sendMessage("You must use the altar to leave.");
          return true;
        }
        player.openDialogue("bossinstance", 10);
        return true;
      case 26505: // Big door: Zamorak
        if (player.getY() < 5333) {
          player.getGameEncoder().sendMessage("You must use the altar to leave.");
          return true;
        }
        player.openDialogue("bossinstance", 9);
        return true;
    }
    return false;
  }

  private void sendOverlay() {
    var player = getPlayer();
    if (player.getCombat().getSaradominsLight()) {
      player.getGameEncoder().setVarbit(VarbitId.GOD_WARS_SARADOMINS_LIGHT, 1);
    }
    player.getGameEncoder().setVarbit(VarbitId.GOD_WARS_SARADOMIN_KILLS, saradominKillcount);
    player.getGameEncoder().setVarbit(VarbitId.GOD_WARS_ARMADYL_KILLS, armadylKillcount);
    player.getGameEncoder().setVarbit(VarbitId.GOD_WARS_BANDOS_KILLS, bandosKillcount);
    player.getGameEncoder().setVarbit(VarbitId.GOD_WARS_ZAMORAK_KILLS, zamorakKillcount);
  }
}
