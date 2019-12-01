package com.palidinodh.osrsscript.map.area.godwars;

import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.VarbitId;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.map.Area;
import com.palidinodh.rs.ReferenceId;
import lombok.Getter;
import lombok.Setter;
import lombok.var;

@ReferenceId({ 11346, 11347, 11578, 11602, 11603 })
public class GodWarsArea extends Area {
  private int armadylKillcount;
  private int bandosKillcount;
  private int zamorakKillcount;
  private int saradominKillcount;
  @Getter
  @Setter
  private int altarDelay;

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
