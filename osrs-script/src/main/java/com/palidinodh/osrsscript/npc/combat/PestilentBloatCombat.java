package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.google.inject.Inject;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Entity;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.Hit;
import com.palidinodh.osrscore.model.HitEvent;
import com.palidinodh.osrscore.model.HitType;
import com.palidinodh.osrscore.model.HitpointsBar;
import com.palidinodh.osrscore.model.Movement;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.TileHitEvent;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatFocus;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.random.PRandom;
import lombok.var;

public class PestilentBloatCombat extends NpcCombat {
  private static final Tile[] CORNERS =
      { new Tile(3299, 4451), new Tile(3299, 4440), new Tile(3288, 4440), new Tile(3288, 4451) };

  @Inject
  private Npc npc;
  private boolean loaded;
  private int direction = 1;
  private boolean reverseDirection;
  private int directionChangeDelay = 40;
  private int sleepDelay = -1;
  private int cycle;
  private int meatDelay = 1;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.PESTILENT_BLOAT_870);
    combat.hitpoints(
        NpcCombatHitpoints.builder().total(2000).bar(HitpointsBar.GREEN_RED_100).build());
    combat.stats(NpcCombatStats.builder().attackLevel(250).magicLevel(150).rangedLevel(180)
        .defenceLevel(100).bonus(CombatBonus.MELEE_ATTACK, 150)
        .bonus(CombatBonus.ATTACK_RANGED, 180).bonus(CombatBonus.DEFENCE_STAB, 40)
        .bonus(CombatBonus.DEFENCE_SLASH, 20).bonus(CombatBonus.DEFENCE_CRUSH, 40)
        .bonus(CombatBonus.DEFENCE_MAGIC, 600).bonus(CombatBonus.DEFENCE_RANGED, 800).build());
    combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    combat.focus(NpcCombatFocus.builder().retaliationDisabled(true).build());


    return Arrays.asList(combat.build());
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
    if (npc.isLocked()) {
      return;
    }
    if (sleepDelay == -1) {
      for (var player : npc.getController().getPlayers()) {
        if (inArea(player)) {
          sleepDelay = 40;
          break;
        }
      }
    }
    if (sleepDelay > 0 && !npc.getController().isMagicBound()) {
      sleepDelay--;
      if (sleepDelay == 0) {
        sleepDelay = 40;
        npc.setAnimation(8082);
        npc.getController().setMagicBind(32);
        cycle++;
        npc.getMovement().setRunning(cycle % 3 == 0);
      }
    }
    if (npc.getMovement().getMagicBindDelay() == Movement.MAGIC_REBIND_DELAY) {
      stomp();
    }
    if (directionChangeDelay > 0) {
      directionChangeDelay--;
      if (directionChangeDelay == 0) {
        directionChangeDelay = 40;
        reverseDirection = !reverseDirection;
      }
    }
    npc.getMovement().clear();
    if (npc.matchesTile(CORNERS[direction])) {
      if (reverseDirection) {
        direction--;
        if (direction < 0) {
          direction = CORNERS.length - 1;
        }
      } else {
        direction++;
        if (direction >= CORNERS.length) {
          direction = 0;
        }
      }
    }
    if (!npc.getController().isMagicBound()) {
      npc.getMovement().addMovement(CORNERS[direction]);
      checkLineOfSight();
      if (cycle > 0) {
        dropMeat();
      }
    }
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

  private void checkLineOfSight() {
    var minX = npc.getX();
    var minY = npc.getY();
    var maxX = CORNERS[direction].getX() + npc.getSizeX() - 1;
    var maxY = CORNERS[direction].getY() + npc.getSizeY() - 1;
    if (minX > maxX) {
      minX = CORNERS[direction].getX();
      maxX = npc.getX() + npc.getSizeX() - 1;
    }
    if (minY > maxY) {
      minY = CORNERS[direction].getY();
      maxY = npc.getY() + npc.getSizeY() - 1;
    }
    var players = npc.getController().getPlayers();
    for (var i = 0; i < players.size(); i++) {
      var player = players.get(i);
      if (!this.inArea(player) || !npc.withinDistance(player, 4) && (player.getX() < minX
          || player.getX() > maxX || player.getY() < minY || player.getY() > maxY)) {
        continue;
      }
      var projectile = Graphic.Projectile.builder().id(1568).startTile(npc).entity(player)
          .projectileSpeed(getProjectileSpeed(player)).startHeight(0).endHeight(0).curve(0)
          .radius(0).build();
      sendMapProjectile(projectile);
      player.addHit(
          new HitEvent(projectile.getEventDelay(), player, new Hit(npc, 10 + PRandom.randomI(10))));
      player.setInCombatDelay(Entity.COMBAT_DELAY);
      for (var i2 = 0; i2 < players.size(); i2++) {
        var player2 = players.get(i2);
        var lineOfSight = this.inNorth(player) && this.inNorth(player2)
            || this.inEast(player) && this.inEast(player2)
            || this.inSouth(player) && this.inSouth(player2)
            || this.inWest(player) && this.inWest(player2);
        if (!lineOfSight || player == player2) {
          continue;
        }
        projectile = Graphic.Projectile.builder().id(1568).startTile(player).entity(player2)
            .projectileSpeed(getProjectileSpeed(player2)).startHeight(0).endHeight(0).curve(0)
            .radius(0).build();
        sendMapProjectile(projectile);
        player2.addHit(new HitEvent(projectile.getEventDelay(), player2,
            new Hit(npc, 10 + PRandom.randomI(10))));
        player2.setInCombatDelay(Entity.COMBAT_DELAY);
      }
    }
  }

  private void stomp() {
    for (var player : npc.getController().getPlayers()) {
      if (player.isLocked() || !npc.withinDistance(player, 1)) {
        continue;
      }
      player.addHit(new HitEvent(0, player, new Hit(npc, player.getHitpoints() / 2)));
      player.setInCombatDelay(Entity.COMBAT_DELAY);
    }
  }

  private void dropMeat() {
    if (meatDelay > 0) {
      meatDelay--;
      if (meatDelay == 0) {
        meatDelay = 6;
        for (var i = 0; i < 14; i++) {
          var tile = new Tile(3288, 4440);
          tile.moveTile(PRandom.randomI(15), PRandom.randomI(15));
          if (npc.getController().getMapClip(tile) != 0) {
            continue;
          }
          npc.getController().sendMapGraphic(tile, new Graphic(1570 + PRandom.randomI(3)));
          var the = new TileHitEvent(4, npc.getController(), tile, 50, HitType.TYPELESS);
          the.setMinDamage(30);
          the.setBind(4);
          the.setGraphic(new Graphic(1575, 100));
          npc.getWorld().addEvent(the);
        }
      }
    }
  }

  private boolean inArea(Tile tile) {
    return tile.getX() >= 3288 && tile.getY() >= 4440 && tile.getX() <= 3303 && tile.getY() <= 4455;
  }

  private boolean inNorth(Tile tile) {
    return tile.getX() >= 3288 && tile.getY() >= 4451 && tile.getX() <= 3303 && tile.getY() <= 4455;
  }

  private boolean inEast(Tile tile) {
    return tile.getX() >= 3299 && tile.getY() >= 4440 && tile.getX() <= 3303 && tile.getY() <= 4455;
  }

  private boolean inSouth(Tile tile) {
    return tile.getX() >= 3288 && tile.getY() >= 4440 && tile.getX() <= 3303 && tile.getY() <= 4444;
  }

  private boolean inWest(Tile tile) {
    return tile.getX() >= 3288 && tile.getY() >= 4440 && tile.getX() <= 3292 && tile.getY() <= 4455;
  }
}
