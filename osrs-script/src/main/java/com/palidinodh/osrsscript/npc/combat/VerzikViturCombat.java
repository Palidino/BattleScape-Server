package com.palidinodh.osrsscript.npc.combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Entity;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.Hit;
import com.palidinodh.osrscore.model.HitEvent;
import com.palidinodh.osrscore.model.HitType;
import com.palidinodh.osrscore.model.HitpointsBar;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatFocus;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSpawn;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.random.PRandom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.var;

public class VerzikViturCombat extends NpcCombat {
  private static final Pillar[] PILLARS = {new Pillar(new Tile(3161, 4318),
      new Tile[] {new Tile(3160, 4318), new Tile(3159, 4317), new Tile(3160, 4317),
          new Tile(3161, 4317), new Tile(3162, 4317), new Tile(3159, 4316), new Tile(3160, 4316)}),
      new Pillar(new Tile(3161, 4312),
          new Tile[] {new Tile(3160, 4313), new Tile(3160, 4312), new Tile(3160, 4311),
              new Tile(3161, 4311), new Tile(3162, 4311), new Tile(3160, 4310),
              new Tile(3161, 4310)}),
      new Pillar(new Tile(3161, 4306),
          new Tile[] {new Tile(3160, 4306), new Tile(3160, 4305), new Tile(3161, 4305),
              new Tile(3162, 4305), new Tile(3160, 4304), new Tile(3161, 4304)}),
      new Pillar(new Tile(3173, 4318),
          new Tile[] {new Tile(3176, 4318), new Tile(3177, 4317), new Tile(3176, 4317),
              new Tile(3175, 4317), new Tile(3174, 4317), new Tile(3177, 4316),
              new Tile(3176, 4316)}),
      new Pillar(new Tile(3173, 4312),
          new Tile[] {new Tile(3176, 4313), new Tile(3176, 4312), new Tile(3176, 4311),
              new Tile(3175, 4311), new Tile(3174, 4311), new Tile(3176, 4310),
              new Tile(3175, 4310)}),
      new Pillar(new Tile(3173, 4306), new Tile[] {new Tile(3176, 4306), new Tile(3176, 4305),
          new Tile(3175, 4305), new Tile(3174, 4305), new Tile(3176, 4304), new Tile(3175, 4304)})};
  private static final int PHASE_1_HIT_DELAY = 16;

  private Npc npc;
  private List<Npc> pillars = new ArrayList<>();
  private List<Npc> spiders = new ArrayList<>();
  private int redSpiderDelay;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat1 = NpcCombatDefinition.builder();
    combat1.id(NpcId.VERZIK_VITUR_1040_8370);
    combat1.spawn(NpcCombatSpawn.builder().respawnId(NpcId.VERZIK_VITUR_1040_8371).build());
    combat1.hitpoints(
        NpcCombatHitpoints.builder().total(2000).bar(HitpointsBar.GREEN_RED_100).build());
    combat1.stats(NpcCombatStats.builder().attackLevel(400).magicLevel(400).rangedLevel(400)
        .defenceLevel(20).bonus(CombatBonus.ATTACK_MAGIC, 80).bonus(CombatBonus.ATTACK_RANGED, 80)
        .bonus(CombatBonus.MELEE_DEFENCE, 20).bonus(CombatBonus.DEFENCE_MAGIC, 20)
        .bonus(CombatBonus.DEFENCE_RANGED, 20).build());
    combat1.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    combat1.focus(NpcCombatFocus.builder().retaliationDisabled(true).build());
    combat1.blockAnimation(8110);


    var combat2Start = NpcCombatDefinition.builder();
    combat2Start.id(NpcId.VERZIK_VITUR_1040_8371);


    var combat2 = NpcCombatDefinition.builder();
    combat2.id(NpcId.VERZIK_VITUR_1265);
    combat2.hitpoints(
        NpcCombatHitpoints.builder().total(3250).bar(HitpointsBar.GREEN_RED_100).build());
    combat2.stats(NpcCombatStats.builder().attackLevel(400).magicLevel(400).rangedLevel(400)
        .defenceLevel(200).bonus(CombatBonus.ATTACK_MAGIC, 80).bonus(CombatBonus.ATTACK_RANGED, 80)
        .bonus(CombatBonus.DEFENCE_STAB, 100).bonus(CombatBonus.DEFENCE_SLASH, 60)
        .bonus(CombatBonus.DEFENCE_CRUSH, 100).bonus(CombatBonus.DEFENCE_MAGIC, 70)
        .bonus(CombatBonus.DEFENCE_RANGED, 250).build());
    combat2.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    combat2.focus(NpcCombatFocus.builder().retaliationDisabled(true).build());


    return Arrays.asList(combat1.build(), combat2Start.build(), combat2.build());
  }

  @Override
  public void spawnHook() {
    npc = getNpc();
    if (npc.getId() == NpcId.VERZIK_VITUR_1040_8370) {
      for (var pillar : PILLARS) {
        pillars.add(new Npc(npc.getController(), NpcId.SUPPORTING_PILLAR, pillar.getTile()));
      }
      npc.setHitDelay(PHASE_1_HIT_DELAY);
    } else if (npc.getId() == NpcId.VERZIK_VITUR_1040_8371) {
      npc.setAnimation(8114);
    }
  }

  @Override
  public void despawnHook() {
    for (var pillarNpc : pillars) {
      if (pillarNpc.isLocked()) {
        continue;
      }
      npc.getWorld().removeNpc(pillarNpc);
    }
    pillars.clear();
    for (var spiderNpc : spiders) {
      if (spiderNpc.isLocked()) {
        continue;
      }
      npc.getWorld().removeNpc(spiderNpc);
    }
    spiders.clear();
  }

  @Override
  public void applyDeadStartHook(int deathDelay) {
    for (var pillarNpc : pillars) {
      if (pillarNpc.isLocked()) {
        continue;
      }
      pillarNpc.getCombat2().timedDeath();
    }
    pillars.clear();
  }

  @Override
  public void tickStartHook() {
    if (npc.isLocked()) {
      return;
    }
    for (var it = spiders.iterator(); it.hasNext();) {
      var spiderNpc = it.next();
      if (!spiderNpc.isDead()) {
        continue;
      }
      it.remove();
    }
    if (npc.getId() == NpcId.VERZIK_VITUR_1040_8370) {
      phase1Tick();
    } else if (npc.getId() == NpcId.VERZIK_VITUR_1040_8371
        || npc.getId() == NpcId.VERZIK_VITUR_1265) {
      phase2Tick();
    }
  }

  private void phase1Tick() {
    if (npc.getHitDelay() == 2) {
      npc.setAnimation(8109);
      return;
    }
    if (npc.getHitDelay() > 0) {
      return;
    }
    var players = npc.getController().getPlayers();
    var hitEntities = new ArrayList<Entity>();
    for (var player : players) {
      if (player.isLocked()) {
        continue;
      }
      var isProtected = false;
      for (var i = 0; i < PILLARS.length; i++) {
        if (pillars.get(i).isLocked() || !PILLARS[i].isSafe(player)) {
          continue;
        }
        if (!hitEntities.contains(pillars.get(i))) {
          hitEntities.add(pillars.get(i));
        }
        isProtected = true;
        break;
      }
      if (isProtected) {
        continue;
      }
      hitEntities.add(player);
    }
    for (var entity : hitEntities) {
      var projectile = Graphic.Projectile.builder().id(1580).startTile(npc).entity(entity)
          .projectileSpeed(npc.getCombat().getSpeed(null, entity, HitType.MAGIC, 10)).build();
      npc.getCombat().sendMapProjectile(projectile);
      entity.addHit(
          new HitEvent(projectile.getEventDelay(), entity, npc, new Hit(PRandom.randomI(104))));
      if (entity instanceof Player) {
        entity.setGraphic(new Graphic(1581, 0, projectile.getContactDelay()));
      } else {
        entity.setGraphic(new Graphic(1582, 0, projectile.getContactDelay()));
      }
    }
    npc.setHitDelay(PHASE_1_HIT_DELAY);
  }

  private void phase2Tick() {
    if (npc.getId() == NpcId.VERZIK_VITUR_1040_8371) {
      var tile = new Tile(3166, 4312);
      if (!npc.matchesTile(tile)) {
        npc.getMovement().clear();
        npc.getMovement().addMovement(tile);
      } else {
        npc.setTransformationId(NpcId.VERZIK_VITUR_1265);
      }
      return;
    }
    if (PRandom.getPercent(npc.getHitpoints(), npc.getMaxHitpoints()) < 35) {
      if (redSpiderDelay-- <= 0) {
        redSpiderDelay = 25;
        var redSpider1 =
            new Npc(npc.getController(), NpcId.NYLOCAS_MATOMENOS_115_8385, new Tile(3163, 4314));
        redSpider1.setMoveDistance(2);
        var redSpider2 =
            new Npc(npc.getController(), NpcId.NYLOCAS_MATOMENOS_115_8385, new Tile(3172, 4314));
        redSpider2.setMoveDistance(2);
      }
    }
  }

  @AllArgsConstructor
  @Getter
  private static class Pillar {
    private Tile tile;
    private Tile[] safeTiles;

    public boolean isSafe(Tile tile) {
      for (var safeTile : safeTiles) {
        if (!tile.matchesTile(safeTile)) {
          continue;
        }
        return true;
      }
      return false;
    }
  }
}
