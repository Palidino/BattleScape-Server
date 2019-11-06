package com.palidinodh.osrsscript.npc.combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Entity;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.Hit;
import com.palidinodh.osrscore.model.HitType;
import com.palidinodh.osrscore.model.HitpointsBar;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.item.Item;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatKillCount;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSpawn;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.player.PCombat;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.setting.Settings;
import lombok.var;

public class EventChaosElementalCombat extends NpcCombat {
  private Npc npc;
  private List<Npc> fanatics = new ArrayList<>();
  private boolean summonedFanatics;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder().underKiller(true).additionalPlayers(3);


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.CHAOS_ELEMENTAL_610_16016);
    combat.spawn(NpcCombatSpawn.builder().respawnDelay(36000).build());
    combat.hitpoints(
        NpcCombatHitpoints.builder().total(2000).bar(HitpointsBar.GREEN_RED_120).build());
    combat.stats(NpcCombatStats.builder().attackLevel(270).magicLevel(270).rangedLevel(270)
        .defenceLevel(270).bonus(CombatBonus.MELEE_DEFENCE, 70).bonus(CombatBonus.DEFENCE_MAGIC, 70)
        .bonus(CombatBonus.DEFENCE_RANGED, 70).build());
    combat.aggression(NpcCombatAggression.builder().range(8).build());
    combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    combat.killCount(NpcCombatKillCount.builder().sendMessage(true).build());
    combat.deathAnimation(3147);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE);
    style.damage(NpcCombatDamage.maximum(28));
    style.animation(3146).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    style.multiTarget(true);
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(28));
    style.animation(3146).attackSpeed(5);
    style.castGraphic(new Graphic(556, 100)).targetGraphic(new Graphic(558, 100));
    style.projectile(NpcCombatProjectile.id(335));
    style.multiTarget(true);
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.maximum(28));
    style.animation(3146).attackSpeed(5);
    style.castGraphic(new Graphic(556, 100)).targetGraphic(new Graphic(558, 100));
    style.projectile(NpcCombatProjectile.id(335));
    style.multiTarget(true);
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }

  @Override
  public void spawnHook() {
    npc = getNpc();
  }

  @Override
  public void restoreHook() {
    summonedFanatics = false;
    npc.getWorld().removeNpcs(fanatics);
    fanatics.clear();
  }

  @Override
  public void tickStartHook() {
    if (fanatics.isEmpty()) {
      return;
    }
    for (var it = fanatics.iterator(); it.hasNext();) {
      var npc2 = it.next();
      if (npc2.isDead()) {
        it.remove();
      }
    }
  }

  @Override
  public void npcApplyHitEndHook(Hit hit) {
    if (npc.isDead() || npc.getHitpoints() > npc.getMaxHitpoints() / 2) {
      return;
    }
    if (summonedFanatics) {
      return;
    }
    summonedFanatics = true;
    fanatics.add(new Npc(npc.getController(), NpcId.CHAOS_FANATIC_202, npc.getX() - 1,
        npc.getY() + 2, npc.getHeight()));
    fanatics.add(new Npc(npc.getController(), NpcId.CHAOS_FANATIC_202, npc.getX() + 1,
        npc.getY() - 2, npc.getHeight()));
    for (var npc2 : fanatics) {
      npc2.setMoveDistance(4);
    }
  }

  @Override
  public void attackedActionsOpponentHook(Entity opponent) {
    if (!(opponent instanceof Player)) {
      return;
    }
    var player = (Player) opponent;
    player.getCombat().setPKSkullDelay(PCombat.SKULL_DELAY);
  }

  @Override
  public boolean canBeAttackedHook(Entity opponent, boolean sendMessage, HitType hitType) {
    if (!(opponent instanceof Player)) {
      return false;
    }
    var player = (Player) opponent;
    if (!fanatics.isEmpty()) {
      if (sendMessage) {
        player.getGameEncoder().sendMessage("Chaos Elemental is currently immune to attacks.");
      }
      return false;
    }
    if (player.getEquipment().wearingFullVoid(HitType.MELEE)
        || player.getEquipment().wearingFullVoid(HitType.RANGED)
        || player.getEquipment().wearingFullVoid(HitType.MELEE)) {
      player.getGameEncoder()
          .sendMessage("Your armour doesn't seem to be effective against the Chaos Elemental.");
    }
    return true;
  }

  @Override
  public double damageReceivedHook(Entity opponent, double damage, HitType hitType,
      HitType defenceType) {
    if (!(opponent instanceof Player)) {
      return damage;
    }
    var player = (Player) opponent;
    if ((!npc.getController().inMultiCombat() || !opponent.getController().inMultiCombat())
        && damage > 6) {
      damage = 6;
    }
    if (player.getEquipment().wearingFullVoid(HitType.MELEE)
        || player.getEquipment().wearingFullVoid(HitType.RANGED)
        || player.getEquipment().wearingFullVoid(HitType.MELEE)) {
      damage = 0;
    }
    return damage;
  }

  @Override
  public void deathDropItemsHook(Player player, int additionalPlayerLoopCount, Tile dropTile) {
    if (additionalPlayerLoopCount == 0) {
      npc.getController().addMapItem(new Item(ItemId.BLOODIER_KEY), dropTile, player);
    } else {
      npc.getController().addMapItem(new Item(ItemId.BLOODY_KEY), dropTile, player);
    }
    if (Settings.getInstance().isEconomy()) {
      player.getInventory().addOrDropItem(ItemId.BLOOD_MONEY, 50_000);
    }
  }
}
