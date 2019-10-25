package com.palidinodh.osrsscript.npc.combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Entity;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.Hit;
import com.palidinodh.osrscore.model.HitEvent;
import com.palidinodh.osrscore.model.HitMark;
import com.palidinodh.osrscore.model.HitType;
import com.palidinodh.osrscore.model.HitpointsBar;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.TileHitEvent;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatFocus;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.npc.combat.style.special.NpcCombatTargetTile;
import com.palidinodh.util.PCollection;
import com.palidinodh.util.PEvent;
import com.palidinodh.random.PRandom;
import lombok.var;

public class MaidenOfSugadintiCombat extends NpcCombat {
  private static int[] PHASE_IDS =
      {NpcId.THE_MAIDEN_OF_SUGADINTI_940, NpcId.THE_MAIDEN_OF_SUGADINTI_940_8361,
          NpcId.THE_MAIDEN_OF_SUGADINTI_940_8362, NpcId.THE_MAIDEN_OF_SUGADINTI_940_8363,
          NpcId.THE_MAIDEN_OF_SUGADINTI_940_8364, NpcId.THE_MAIDEN_OF_SUGADINTI_940_8365};

  private Npc npc;
  private boolean loaded;
  private boolean initialAttackDelay;
  private List<PEvent> bloodSpots = new ArrayList<>();
  private int phase;
  private List<Npc> spiders = new ArrayList<>();
  private List<Npc> spawns = new ArrayList<>();
  private int increasedDamage;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.THE_MAIDEN_OF_SUGADINTI_940).id(NpcId.THE_MAIDEN_OF_SUGADINTI_940_8361)
        .id(NpcId.THE_MAIDEN_OF_SUGADINTI_940_8363).id(NpcId.THE_MAIDEN_OF_SUGADINTI_940_8364)
        .id(NpcId.THE_MAIDEN_OF_SUGADINTI_940_8365);
    combat.hitpoints(
        NpcCombatHitpoints.builder().total(3500).bar(HitpointsBar.GREEN_RED_100).build());
    combat.stats(NpcCombatStats.builder().attackLevel(350).magicLevel(350).rangedLevel(350)
        .defenceLevel(200).bonus(CombatBonus.ATTACK_MAGIC, 300).build());
    combat.aggression(NpcCombatAggression.builder().range(25).always(true).build());
    combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).bind(true).build());
    combat
        .focus(NpcCombatFocus.builder().attackClosest(true).disableFollowingOpponent(true).build());
    combat.deathAnimation(8093);

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.builder().hitType(HitType.MAGIC).weight(8).build());
    style.damage(NpcCombatDamage.builder().maximum(36).prayerEffectiveness(0.6).build());
    style.animation(8092).attackSpeed(8).attackRange(25);
    style.projectile(NpcCombatProjectile.builder().id(1577).startHeight(0).endHeight(0).curve(0)
        .radius(0).build());
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(
        NpcCombatStyleType.builder().hitType(HitType.MAGIC).subHitType(HitType.TYPELESS).build());
    style.damage(NpcCombatDamage.builder().maximum(15).ignorePrayer(true).build());
    style.animation(8091).attackSpeed(8).attackRange(25);
    style.targetGraphic(new Graphic(1579));
    style.projectile(NpcCombatProjectile.builder().id(1578).speedMinimumDistance(8).build());
    style.multiTarget(true);
    var targetTile = NpcCombatTargetTile.builder();
    targetTile.breakOff(NpcCombatTargetTile.BreakOff.builder().count(2).onlyFocusedOpponent(true)
        .distance(3).build());
    style.specialAttack(targetTile.build());
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }

  @Override
  public void spawnHook() {
    npc = getNpc();
  }

  @Override
  public void restoreHook() {
    npc.getWorld().removeNpcs(spiders);
    npc.getWorld().removeNpcs(spawns);
  }

  @Override
  public void tickStartHook() {
    if (!npc.getController().isRegionLoaded()) {
      return;
    }
    if (!loaded) {
      loadProfile();
      return;
    }
    updateHitpoints();
    if (!initialAttackDelay) {
      for (var player : npc.getController().getPlayers()) {
        if (player.getX() >= 3160 && player.getX() <= 3184 && player.getY() >= 4435
            && player.getY() <= 4458) {
          initialAttackDelay = true;
          npc.setHitDelay(10);
          break;
        }
      }
    }
    if (npc.isDead() && npc.getId() != PHASE_IDS[phase + 1]) {
      npc.setTransformationId(PHASE_IDS[phase + 1]);
    }
    if (npc.isLocked()) {
      return;
    }
    for (var it = spiders.iterator(); it.hasNext();) {
      var npc2 = it.next();
      if (!npc2.isVisible()) {
        it.remove();
      } else if (npc.withinDistance(npc2, 1)) {
        npc.applyHit(new Hit(npc2.getHitpoints(), HitMark.HEAL));
        npc2.getCombat().timedDeath();
        increasedDamage += 1;
        it.remove();
      }
    }
    for (var player : npc.getController().getPlayers()) {
      var mapObject = npc.getController().getMapObject(32984, player);
      if (mapObject == null) {
        continue;
      }
      var damage = 10 + PRandom.randomI(5);
      player.addHit(new HitEvent(0, player, new Hit(damage)));
      npc.applyHit(new Hit(damage, HitMark.HEAL));
    }
    if (phase == 0 && npc.getHitpoints() <= npc.getMaxHitpoints() * 0.7
        || phase == 1 && npc.getHitpoints() <= npc.getMaxHitpoints() * 0.5
        || phase == 2 && npc.getHitpoints() <= npc.getMaxHitpoints() * 0.3) {
      phase++;
      npc.setTransformationId(PHASE_IDS[phase]);
      spawnNylocasMatomenos();
      if (phase == 3) {
        increasedDamage += 17;
      }
    }
  }

  @Override
  public int applyAttackMaximumDamageHook(NpcCombatStyle combatStyle, Entity opponent) {
    return combatStyle.getDamage().getMaximum() + increasedDamage;
  }

  @Override
  public void targetTileHitEventHook(NpcCombatStyle combatStyle, Entity opponent,
      TileHitEvent tileHitEvent, Graphic.Projectile projectile) {
    for (var event : bloodSpots) {
      if (tileHitEvent.getTile().matchesTile((Tile) event.getAttachment())) {
        return;
      }
    }
    var event = new PEvent(projectile.getEventDelay()) {
      @Override
      public void execute() {
        if (getExecutions() == 0) {
          setTick(0);
        } else if (!npc.isVisible() || getExecutions() == 10) {
          stop();
          bloodSpots.remove(this);
          if (PRandom.randomE(4) == 0) {
            var npc2 = new Npc(npc.getController(), NpcId.BLOOD_SPAWN_55, (Tile) getAttachment());
            npc2.getController().setMultiCombatFlag(true);
            npc2.setMoveDistance(32);
            spawns.add(npc2);
          }
        } else {
          for (var player : npc.getController().getPlayers()) {
            if (!player.matchesTile((Tile) getAttachment())) {
              continue;
            }
            var damage = 10 + PRandom.randomI(5);
            player.addHit(new HitEvent(0, player, new Hit(damage)));
            npc.applyHit(new Hit(damage, HitMark.HEAL));
          }
        }
      }
    };
    event.setAttachment(tileHitEvent.getTile());
    npc.getWorld().addEvent(event);
    bloodSpots.add(event);
  }

  private void loadProfile() {
    var players = npc.getController().getPlayers();
    if (loaded || players.isEmpty()) {
      return;
    }
    loaded = true;
    var hitpoints = npc.getMaxHitpoints();
    if (players.size() == 4) {
      hitpoints = (int) (hitpoints * 0.875);
    } else if (players.size() == 3) {
      hitpoints = (int) (hitpoints * 0.75);
    } else if (players.size() == 2) {
      hitpoints = (int) (hitpoints * 0.625);
    } else if (players.size() == 1) {
      hitpoints = (int) (hitpoints * 0.5);
    }
    npc.setMaxHitpoints(hitpoints);
    npc.setHitpoints(npc.getMaxHitpoints());
  }

  private void updateHitpoints() {
    if (!npc.isVisible()) {
      return;
    }
    for (var player : npc.getController().getPlayers()) {
      if (!npc.withinDistance(player, 32)) {
        continue;
      }
      player.getGameEncoder().setVarp(1575, npc.getHitpoints() + (npc.getMaxHitpoints() * 2048));
      var hitpointsPercent = (int) PRandom.getPercent(npc.getHitpoints(), npc.getMaxHitpoints());
      player.getGameEncoder().sendWidgetText(596, 4, "Health: " + hitpointsPercent + "%");
    }
  }

  private void spawnNylocasMatomenos() {
    var spiderTiles = PCollection.toList(new Tile(3173, 4436), new Tile(3177, 4436),
        new Tile(3181, 4436), new Tile(3185, 4436), new Tile(3185, 4438), new Tile(3173, 4456),
        new Tile(3177, 4456), new Tile(3181, 4456), new Tile(3185, 4456), new Tile(3185, 4454));
    var playerCount = npc.getController().getPlayers().size();
    for (var i = 0; i < playerCount * 2; i++) {
      var tile = spiderTiles.remove(PRandom.randomE(spiderTiles.size()));
      var npc2 = new Npc(npc.getController(), NpcId.NYLOCAS_MATOMENOS_115, tile);
      npc2.getController().setMultiCombatFlag(true);
      npc2.getMovement().setFollowing(npc);
      spiders.add(npc2);
    }
  }
}
