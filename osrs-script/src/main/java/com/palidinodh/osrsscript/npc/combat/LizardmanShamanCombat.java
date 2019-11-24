package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.google.inject.Inject;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Entity;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.Hit;
import com.palidinodh.osrscore.model.HitEvent;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.TileHitEvent;
import com.palidinodh.osrscore.model.item.RandomItem;
import com.palidinodh.osrscore.model.map.route.Route;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTable;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTableDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatEffect;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.npc.combat.style.special.NpcCombatTargetTile;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.random.PRandom;
import lombok.var;

public class LizardmanShamanCombat extends NpcCombat {
  private static final NpcCombatDropTable SUPERIOR_DROP_TABLE = NpcCombatDropTable.builder()
      .chance(2.32).log(true)
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.IMBUED_HEART, 1, 1, 1)))
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ETERNAL_GEM, 1, 1, 1)))
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DUST_BATTLESTAFF, 1, 1, 3)))
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MIST_BATTLESTAFF, 1, 1, 3))).build();
  private NpcCombatStyle ACIDIC = NpcCombatStyle.builder().type(NpcCombatStyleType.MAGIC)
      .damage(NpcCombatDamage.builder().maximum(30).minimum(25).ignorePrayer(true).build())
      .effect(NpcCombatEffect.builder().poison(10).build())
      .projectile(NpcCombatProjectile.builder().id(1293).speedMinimumDistance(8).build())
      .animation(7193).attackSpeed(4).attackRange(5).targetTileGraphic(new Graphic(1294))
      .specialAttack(NpcCombatTargetTile.builder().radius(1).build()).build();
  private static final int JUMP_TIME = 6;
  private static final int JUMP_LAND = 1;

  @Inject
  private Npc npc;
  private boolean loaded;
  private Tile jumpTile;
  private int jumpTime;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder().rareDropTableRate(NpcCombatDropTable.CHANCE_1_IN_256)
        .clue(NpcCombatDrop.ClueScroll.HARD, NpcCombatDropTable.CHANCE_1_IN_1200)
        .clue(NpcCombatDrop.ClueScroll.ELITE, NpcCombatDropTable.CHANCE_1_IN_200);
    var dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_5000)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_WARHAMMER)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_250).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.XERICS_TALISMAN_INERT)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_RARE);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_BATTLEAXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WATERMELON_SEED, 15)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TORSTOL_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MAPLE_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PAPAYA_TREE_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PALM_TREE_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.YEW_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MAGIC_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SPIRIT_SEED)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RED_DHIDE_VAMB)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MYSTIC_EARTH_STAFF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.EARTH_BATTLESTAFF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_MED_HELM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_CHAINBODY)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_WARHAMMER)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNITE_ORE_NOTED, 3, 5)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_KWUARM_NOTED, 1, 3)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_DWARF_WEED_NOTED, 1, 3)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_LANTADYME_NOTED, 1, 3)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_CADANTINE_NOTED, 1, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WILLOW_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RANARR_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SNAPDRAGON_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CHAOS_RUNE, 45, 57)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.FIRE_RUNE, 66, 74)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DEATH_RUNE, 22, 30)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.IRON_ORE_NOTED, 24, 35)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COAL_NOTED, 20, 23)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.AIR_RUNE, 71, 80)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 100, 6000)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.XERICIAN_FABRIC, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LIZARDMAN_FANG, 10, 14)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CHILLI_POTATO, 2)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BIG_BONES)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.LIZARDMAN_SHAMAN_150);
    combat.hitpoints(NpcCombatHitpoints.total(150));
    combat.stats(NpcCombatStats.builder().attackLevel(120).magicLevel(130).rangedLevel(120)
        .defenceLevel(140).bonus(CombatBonus.MELEE_ATTACK, 45).bonus(CombatBonus.ATTACK_RANGED, 45)
        .bonus(CombatBonus.DEFENCE_SLASH, 40).bonus(CombatBonus.DEFENCE_CRUSH, 30)
        .bonus(CombatBonus.DEFENCE_MAGIC, 50).build());
    combat.aggression(NpcCombatAggression.PLAYERS);
    combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    combat.deathAnimation(7196).blockAnimation(7194);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(31));
    style.animation(7158).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(31));
    style.animation(7193).attackSpeed(4).attackRange(5);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    var raidsDrop = NpcCombatDrop.builder();
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BIG_BONES)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BUCHU_SEED, 8, 12)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GOLPAR_SEED, 8, 12)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.NOXIFER_SEED, 8, 12)));
    raidsDrop.table(dropTable.build());


    var raidsCombat = NpcCombatDefinition.builder();
    raidsCombat.id(NpcId.LIZARDMAN_SHAMAN);
    raidsCombat.hitpoints(NpcCombatHitpoints.total(150));
    raidsCombat.stats(NpcCombatStats.builder().attackLevel(130).magicLevel(130).rangedLevel(130)
        .defenceLevel(210).bonus(CombatBonus.MELEE_ATTACK, 58).bonus(CombatBonus.ATTACK_RANGED, 56)
        .bonus(CombatBonus.DEFENCE_STAB, 102).bonus(CombatBonus.DEFENCE_SLASH, 160)
        .bonus(CombatBonus.DEFENCE_CRUSH, 150).bonus(CombatBonus.DEFENCE_MAGIC, 160).build());
    raidsCombat.aggression(NpcCombatAggression.builder().range(6).always(true).build());
    raidsCombat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    raidsCombat.deathAnimation(7196).blockAnimation(7194);
    raidsCombat.drop(raidsDrop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(31));
    style.animation(7158).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    raidsCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(31));
    style.animation(7193).attackSpeed(4).attackRange(9);
    style.projectile(NpcCombatProjectile.id(335));
    raidsCombat.style(style.build());


    var cursedCombat = NpcCombatDefinition.builder();
    cursedCombat.id(NpcId.CURSED_LIZARDMAN_SHAMAN_150_16015);
    cursedCombat.hitpoints(NpcCombatHitpoints.total(200));
    cursedCombat.stats(NpcCombatStats.builder().attackLevel(120).magicLevel(130).rangedLevel(120)
        .defenceLevel(140).bonus(CombatBonus.MELEE_ATTACK, 45).bonus(CombatBonus.ATTACK_RANGED, 45)
        .bonus(CombatBonus.DEFENCE_SLASH, 40).bonus(CombatBonus.DEFENCE_CRUSH, 30)
        .bonus(CombatBonus.DEFENCE_MAGIC, 50).build());
    cursedCombat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    cursedCombat.deathAnimation(7196).blockAnimation(7194);
    cursedCombat.drop(drop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(31));
    style.animation(7158).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    cursedCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(31));
    style.animation(7193).attackSpeed(4).attackRange(5);
    style.projectile(NpcCombatProjectile.id(335));
    cursedCombat.style(style.build());


    return Arrays.asList(combat.build(), raidsCombat.build(), cursedCombat.build());
  }

  @Override
  public void restoreHook() {
    loaded = false;
    jumpTile = null;
    jumpTime = 0;
  }

  @Override
  public void tickStartHook() {
    if (!loaded
        && (npc.getId() == NpcId.LIZARDMAN_SHAMAN || npc.getId() == NpcId.LIZARDMAN_SHAMAN_7574)) {
      loadProfile();
      return;
    }
    if (!npc.isVisible() || npc.isDead()) {
      return;
    }
    if (jumpTime > 0) {
      jumpTime--;
      if (jumpTile != null) {
        if (jumpTime == JUMP_TIME / 2) {
          npc.getMovement().teleport(jumpTile);
        }
        if (jumpTime == 0) {
          npc.clearHits();
          npc.setAnimation(6946);
          jumpTile = null;
          jumpTime = JUMP_LAND;
          npc.setLock(jumpTime);
        }
      } else {
        if (jumpTime == 0) {
          npc.clearHits();
          var players = npc.getController().getPlayers();
          for (var player : players) {
            if (npc.withinDistance(player, 0)) {
              var hitEvent = new HitEvent(0, player, new Hit(25 + PRandom.randomI(5)));
              player.addHit(hitEvent);
            }
          }
        }
      }
    }
    if (jumpTile == null && jumpTime == 0 && !npc.isLocked() && npc.getHitDelay() == 0
        && npc.isAttacking() && npc.getEngagingEntity() instanceof Player && PRandom.randomE(4) == 0
        && npc.withinDistance(npc.getEngagingEntity(), 5)
        && Route.canMove(npc, npc.getEngagingEntity())) {
      npc.setAnimation(7152);
      jumpTile = new Tile(npc.getEngagingEntity());
      jumpTime = JUMP_TIME;
      npc.setLock(jumpTime + 1);
    } else if (!npc.isLocked() && npc.getHitDelay() == 0 && npc.isAttacking()
        && npc.getEngagingEntity() instanceof Player && PRandom.randomE(4) == 0
        && npc.withinDistance(npc.getEngagingEntity(), 5)) {
      npc.setAnimation(7157);
      npc.setHitDelay(4);
      var tiles = new Tile[] { new Tile(npc.getEngagingEntity()).moveX(1),
          new Tile(npc.getEngagingEntity()).moveY(1), new Tile(npc.getEngagingEntity()).moveX(-1) };
      for (var i = 0; i < tiles.length; i++) {
        var t = tiles[i];
        if (!Route.canMove(npc.getEngagingEntity(), t)) {
          continue;
        }
        var spawn = new Npc(npc.getController(), NpcId.SPAWN_6768, t);
        spawn.getMovement().setFollowing(npc.getEngagingEntity());
        spawn.getBasicScript().getVariable("countdown1=" + (7 + i * 2));
      }
    }
  }

  @Override
  public NpcCombatStyle applyAttackCombatStyleHook(NpcCombatStyle combatStyle, Entity opponent) {
    if (!(npc.getEngagingEntity() instanceof Player) || PRandom.randomE(4) != 0) {
      return combatStyle;
    }
    int type = PRandom.randomE(1);
    if (type == 0) {
      return ACIDIC;
    }
    return combatStyle;
  }

  @Override
  public double damageInflictedHook(NpcCombatStyle combatStyle, Entity opponent, double damage) {
    if (combatStyle == ACIDIC) {
      if (damage > 0 && damage < 25) {
        damage = 25;
      }
      if (opponent instanceof Player) {
        Player player = (Player) opponent;
        if (player.getEquipment().wearingFullShayzien5()) {
          damage = 0;
        }
      }
    }
    return damage;
  }

  @Override
  public void targetTileHitEventHook(NpcCombatStyle combatStyle, Entity opponent,
      TileHitEvent tileHitEvent, Graphic.Projectile projectile) {
    tileHitEvent.setShamanPoison(true);
  }

  @Override
  public NpcCombatDropTable deathDropItemsTableHook(Npc npc, Player player, int dropRateDivider,
      int roll, NpcCombatDropTable table) {
    if (npc.getId() == NpcId.CURSED_LIZARDMAN_SHAMAN_150_16015) {
      if (!player.getSkills().isWildernessSlayerTask(npc)) {
        player.getGameEncoder().sendMessage("Without an assigned task, the loot turns to dust...");
        return null;
      }
    }
    if (npc.getId() == NpcId.CURSED_LIZARDMAN_SHAMAN_150_16015
        && SUPERIOR_DROP_TABLE.canDrop(npc, player, dropRateDivider, roll)) {
      return SUPERIOR_DROP_TABLE;
    }
    return table;
  }

  @Override
  public double dropTableChanceHook(Player player, int dropRateDivider, int roll,
      NpcCombatDropTable table) {
    var chance = table.getChance();
    if (npc.getId() == NpcId.CURSED_LIZARDMAN_SHAMAN_150_16015 && table == SUPERIOR_DROP_TABLE) {
      chance /= 32;
    }
    return chance;
  }

  private void loadProfile() {
    if (loaded || npc.getController().getPlayers().isEmpty()) {
      return;
    }
    loaded = true;
    var averageHP = 0;
    var playerMultiplier = 1.0;
    var players = npc.getController().getPlayers();
    for (var player : players) {
      averageHP += player.getMaxHitpoints();
      playerMultiplier += 0.5;
    }
    averageHP /= players.size();
    var hitpoints = (int) ((50 + players.size() * 25 + averageHP * 2) * playerMultiplier / 2.5);
    npc.setMaxHitpoints(hitpoints);
    npc.setHitpoints(npc.getMaxHitpoints());
  }
}
