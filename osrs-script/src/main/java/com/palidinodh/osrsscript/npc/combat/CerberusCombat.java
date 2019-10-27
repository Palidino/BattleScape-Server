package com.palidinodh.osrsscript.npc.combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import com.palidinodh.osrscore.io.cache.ItemId;
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
import com.palidinodh.osrscore.model.item.RandomItem;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTable;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTableDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatKillCount;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSlayer;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSpawn;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.random.PRandom;
import com.palidinodh.rs.setting.Settings;
import lombok.var;

public class CerberusCombat extends NpcCombat {
  private static final NpcCombatStyle SPECIAL_ATTACK = NpcCombatStyle.builder()
      .type(NpcCombatStyleType.MELEE_SLASH).damage(NpcCombatDamage.maximum(23)).animation(4491)
      .attackSpeed(4).attackRange(10).build();
  private static final int TRIPLE_DELAY = 66;
  private static final int SOULS_DELAY = 50;
  private static final int AOE_DELAY = 25;
  private static final int AOE_LENGTH = 14;

  private Npc npc;
  private int tripleDelay;
  private int soulsDelay;
  private int aoeDelay;
  private List<Npc> souls = new ArrayList<>();
  private List<AoeAttack> aoeAttacks = new ArrayList<>();

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder().rareDropTableRate(NpcCombatDropTable.CHANCE_1_IN_256)
        .clue(NpcCombatDrop.ClueScroll.ELITE, NpcCombatDropTable.CHANCE_1_IN_100);
    var dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_3000)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.HELLPUPPY)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_2000)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.JAR_OF_SOULS)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_128)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PRIMORDIAL_CRYSTAL)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PEGASIAN_CRYSTAL)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ETERNAL_CRYSTAL)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SMOULDERING_STONE)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_2H_SWORD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_HALBERD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLACK_DHIDE_BODY)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_FULL_HELM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_AXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_PICKAXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_CHAINBODY)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_PLATEBODY)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLOOD_RUNE, 60)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNITE_BOLTS_UNF, 40)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CANNONBALL, 50)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TORSTOL_SEED, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_TORSTOL_NOTED, 6)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNITE_ORE_NOTED, 5)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.KEY_MASTER_TELEPORT, 3)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LAVA_BATTLESTAFF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BATTLESTAFF_NOTED, 6)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PURE_ESSENCE_NOTED, 300)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.FIRE_RUNE, 300)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DEATH_RUNE, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SOUL_RUNE, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SUPER_RESTORE_4, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COAL_NOTED, 120)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 10000, 20000)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNHOLY_SYMBOL)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SUMMER_PIE, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ASHES_NOTED, 50)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_BONES_NOTED, 20)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WINE_OF_ZAMORAK_NOTED, 15)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.FIRE_ORB_NOTED, 20)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNCUT_DIAMOND_NOTED, 5)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ASHES)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.CERBERUS_318);
    combat.spawn(NpcCombatSpawn.builder().respawnDelay(14).build());
    combat
        .hitpoints(NpcCombatHitpoints.builder().total(600).bar(HitpointsBar.GREEN_RED_100).build());
    combat.stats(NpcCombatStats.builder().attackLevel(220).magicLevel(220).rangedLevel(220)
        .defenceLevel(100).bonus(CombatBonus.MELEE_ATTACK, 50).bonus(CombatBonus.ATTACK_MAGIC, 50)
        .bonus(CombatBonus.ATTACK_RANGED, 50).bonus(CombatBonus.DEFENCE_STAB, 50)
        .bonus(CombatBonus.DEFENCE_SLASH, 100).bonus(CombatBonus.DEFENCE_CRUSH, 25)
        .bonus(CombatBonus.DEFENCE_MAGIC, 100).bonus(CombatBonus.DEFENCE_RANGED, 100).build());
    combat.slayer(NpcCombatSlayer.builder().level(91).build());
    combat.aggression(NpcCombatAggression.PLAYERS);
    combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    combat.killCount(NpcCombatKillCount.builder().sendMessage(true).build());
    combat.deathAnimation(4495).blockAnimation(4489);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_SLASH);
    style.damage(NpcCombatDamage.maximum(23));
    style.animation(4491).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(23));
    style.animation(4490).attackSpeed(4);
    style.targetGraphic(new Graphic(1244, 100));
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.maximum(23));
    style.animation(4490).attackSpeed(4);
    style.targetGraphic(new Graphic(1243, 100));
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }

  @Override
  public void spawnHook() {
    npc = getNpc();
  }

  @Override
  public void restoreHook() {
    tripleDelay = TRIPLE_DELAY;
    soulsDelay = SOULS_DELAY;
    aoeDelay = AOE_DELAY;
    removeSouls();
    aoeAttacks.clear();
  }

  @Override
  public void tickStartHook() {
    if (!npc.isVisible() || npc.isDead()) {
      removeSouls();
      return;
    }
    if (npc.getController().getPlayers().isEmpty()) {
      if (npc.getHitpoints() < npc.getMaxHitpoints()) {
        npc.restore();
      }
      return;
    }
    checkTriple();
    checkSouls();
    checkAOE();
  }

  @Override
  public HitType attackTickHitTypeHook(HitType hitType, Entity opponent) {
    if (tripleDelay == TRIPLE_DELAY) {
      return HitType.MAGIC;
    }
    if (tripleDelay == TRIPLE_DELAY - 1) {
      return HitType.RANGED;
    }
    if (tripleDelay == TRIPLE_DELAY - 2) {
      return HitType.MELEE;
    }
    return hitType;
  }

  @Override
  public NpcCombatStyle attackTickCombatStyleHook(NpcCombatStyle combatStyle, Entity opponent) {
    if (tripleDelay == TRIPLE_DELAY - 2) {
      return SPECIAL_ATTACK;
    }
    return combatStyle;
  }

  @Override
  public int attackTickAttackSpeedHook(NpcCombatStyle combatStyle) {
    if (tripleDelay == TRIPLE_DELAY || tripleDelay == TRIPLE_DELAY - 1
        || tripleDelay == TRIPLE_DELAY - 2) {
      return 2;
    }
    return combatStyle.getAttackSpeed();
  }

  @Override
  public boolean canBeAttackedHook(Entity opponent, boolean sendMessage, HitType hitType) {
    if (!(opponent instanceof Player)) {
      return false;
    }
    var player = (Player) opponent;
    if (!Settings.getInstance().isSpawn() && !player.getSkills().isAnySlayerTask(npc)) {
      if (sendMessage) {
        player.getGameEncoder()
            .sendMessage("This can only be attacked on an appropriate Slayer task.");
      }
      return false;
    }
    if (npc.isAttacking() && npc.getLastHitByEntity() != null
        && player != npc.getLastHitByEntity()) {
      if (sendMessage) {
        player.getGameEncoder().sendMessage("Cerberus is busy attacking someone else.");
      }
      return false;
    }
    return true;
  }

  private void checkTriple() {
    if (!npc.isAttacking()) {
      return;
    }
    if (tripleDelay > 0) {
      tripleDelay--;
      return;
    }
    var entity = npc.getEngagingEntity();
    if (!npc.isAttacking() || !npc.withinDistance(entity, 10) || entity.isLocked()) {
      return;
    }
    tripleDelay = TRIPLE_DELAY;
  }

  private void checkSouls() {
    if (soulsDelay > 0) {
      soulsDelay--;
      if (souls == null) {
        return;
      }
      var entity = npc.getEngagingEntity();
      if (soulsDelay == 49) {
        for (Npc npc : souls) {
          npc.getMovement().clear();
          npc.getMovement().addMovement(npc.getX(), npc.getY() - 9);
        }
      } else if (soulsDelay == 36) {
        if (entity != null) {
          soulAttack(souls.get(0), entity);
        }
      } else if (soulsDelay == 33) {
        if (entity != null) {
          soulAttack(souls.get(1), entity);
        }
        souls.get(0).getMovement().clear();
        souls.get(0).getMovement().addMovement(souls.get(0).getSpawnTile());
      } else if (soulsDelay == 30) {
        if (entity != null) {
          soulAttack(souls.get(2), entity);
        }
        souls.get(1).getMovement().clear();
        souls.get(1).getMovement().addMovement(souls.get(1).getSpawnTile());
      } else if (soulsDelay == 27) {
        souls.get(2).getMovement().clear();
        souls.get(2).getMovement().addMovement(souls.get(2).getSpawnTile());
      } else if (soulsDelay == 16) {
        removeSouls();
      }
    } else {
      startSouls();
    }
  }

  private void soulAttack(Npc soul, Entity entity) {
    if (!(entity instanceof Player) || !soul.withinMapDistance(entity)) {
      return;
    }
    var player = (Player) entity;
    soul.setFaceTile(entity);
    var speed = getProjectileSpeed(entity);
    var hitType = HitType.TYPELESS;
    var prayer = "";
    if (soul.getId() == NpcId.SUMMONED_SOUL_96) {
      soul.setAnimation(4503);
      var projectile = Graphic.Projectile.builder().id(11).startTile(soul).entity(entity)
          .projectileSpeed(speed).build();
      sendMapProjectile(projectile);
      hitType = HitType.RANGED;
      prayer = "protect from missiles";
    } else if (soul.getId() == NpcId.SUMMONED_SOUL_96_5868) {
      soul.setAnimation(4504);
      var projectile = Graphic.Projectile.builder().id(100).startTile(soul).entity(entity)
          .projectileSpeed(speed).build();
      sendMapProjectile(projectile);
      hitType = HitType.MAGIC;
      prayer = "protect from magic";
    } else if (soul.getId() == NpcId.SUMMONED_SOUL_79) {
      speed.setClientDelay(speed.getClientDelay() / 2);
      speed.setClientSpeed(speed.getClientSpeed() + speed.getClientDelay());
      var projectile = Graphic.Projectile.builder().id(1248).startTile(soul).entity(entity)
          .projectileSpeed(speed).build();
      sendMapProjectile(projectile);
      hitType = HitType.MELEE;
      prayer = "protect from melee";
    }
    if (player.getPrayer().hasActive(prayer)) {
      if (player.getEquipment().getShieldId() == 12821) {
        player.getPrayer().adjustPoints(-15);
      } else {
        player.getPrayer().adjustPoints(-30);
      }
    } else {
      var hitEvent = new HitEvent(speed.getEventDelay(), entity, new Hit(30, HitMark.HIT, hitType));
      entity.addHit(hitEvent);
    }
  }

  private void startSouls() {
    if (soulsDelay > 0 || npc.getHitpoints() >= 400 || PRandom.randomE(20) != 0) {
      return;
    }
    var entity = npc.getEngagingEntity();
    if (!npc.isAttacking() || !npc.withinDistance(entity, 10) || entity.isLocked()) {
      return;
    }
    npc.getWorld().removeNpcs(souls);
    souls.clear();
    Tile[] tiles = null;
    if (npc.getRegionId() == 4883) {
      tiles = new Tile[] {new Tile(1239, 1265), new Tile(1240, 1265), new Tile(1241, 1265)};
    } else if (npc.getRegionId() == 5395) {
      tiles = new Tile[] {new Tile(1367, 1265), new Tile(1368, 1265), new Tile(1369, 1265)};
    } else if (npc.getRegionId() == 5140) {
      tiles = new Tile[] {new Tile(1303, 1329), new Tile(1304, 1329), new Tile(1305, 1329)};
    }
    if (tiles == null) {
      return;
    }
    souls.add(new Npc(npc.getController(), NpcId.SUMMONED_SOUL_96, npc));
    souls.add(new Npc(npc.getController(), NpcId.SUMMONED_SOUL_96_5868, npc));
    souls.add(new Npc(npc.getController(), NpcId.SUMMONED_SOUL_79, npc));
    for (var npc : souls) {
      npc.setRespawns(npc.getRespawns());
    }
    Collections.shuffle(souls);
    for (var i = 0; i < souls.size(); i++) {
      souls.get(i).setTile(tiles[i]);
      souls.get(i).setSpawnTile(tiles[i]);
    }
    npc.setForceMessage("Aaarrrooooooo");
    soulsDelay = SOULS_DELAY;
  }

  private void removeSouls() {
    npc.getWorld().removeNpcs(souls);
    souls.clear();
  }

  private void checkAOE() {
    if (aoeDelay > 0) {
      aoeDelay--;
      if (aoeAttacks == null) {
        return;
      }
      var entity = npc.getEngagingEntity();
      var withinDistance = npc.withinDistance(entity, 10);
      for (var aoe : aoeAttacks) {
        aoe.tick--;
        if (aoe.tick < 0) {
          continue;
        }
        if (aoe.tick == 1) {
          npc.getController().sendMapGraphic(aoe.tile, new Graphic(1247));
        }
        if (!withinDistance || entity.isLocked()
            || aoe.tick - AOE_LENGTH + 2 >= aoe.speed.getEventDelay()) {
          continue;
        }
        if (entity.withinDistance(aoe.tile, 0)) {
          var hitEvent = new HitEvent(0, entity, new Hit(10 + PRandom.randomI(5)));
          entity.addHit(hitEvent);
        } else {
          if (!entity.withinDistance(aoe.tile, 1)) {
            continue;
          }
          var hitEvent = new HitEvent(0, entity, new Hit(7));
          entity.addHit(hitEvent);
        }
      }
    } else {
      startAOE();
    }
  }

  private void startAOE() {
    if (aoeDelay > 0 || npc.getHitpoints() >= 200 || PRandom.randomE(20) != 0) {
      return;
    }
    var entity = npc.getEngagingEntity();
    if (!npc.isAttacking() || !npc.withinDistance(entity, 10) || entity.isLocked()) {
      return;
    }
    aoeAttacks.clear();
    var tiles =
        new Tile[] {new Tile(entity), new Tile(entity).randomize(2), new Tile(entity).randomize(4)};
    for (var tile : tiles) {
      var projectile = Graphic.Projectile.builder().id(1247).startTile(npc).endTile(tile)
          .projectileSpeed(getProjectileSpeed(entity)).build();
      sendMapProjectile(projectile);
      npc.getController().sendMapGraphic(tile, new Graphic(1246, 0, projectile.getContactDelay()));
      aoeAttacks.add(new AoeAttack(tile, projectile.getProjectileSpeed()));
    }
    npc.setForceMessage("Grrrrrrrrrrrrrr");
    aoeDelay = AOE_DELAY + PRandom.randomI(AOE_DELAY);
  }

  private class AoeAttack {
    private Tile tile;
    private Graphic.ProjectileSpeed speed;
    private int tick;

    private AoeAttack(Tile tile, Graphic.ProjectileSpeed speed) {
      this.tile = tile;
      this.speed = speed;
      tick = speed.getEventDelay() + AOE_LENGTH;
    }
  }
}
