package com.palidinodh.osrsscript.npc.combat;

import java.util.ArrayList;
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
import com.palidinodh.osrscore.model.HitType;
import com.palidinodh.osrscore.model.HitpointsBar;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.item.Item;
import com.palidinodh.osrscore.model.item.RandomItem;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.map.TempMapObject;
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
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatType;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatEffect;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.npc.combat.style.special.NpcCombatTargetTile;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.random.PRandom;
import com.palidinodh.util.PEvent;
import lombok.var;

public class VorkathCombat extends NpcCombat {
  private static final NpcCombatStyle POISON_ATTACK = NpcCombatStyle.builder()
      .type(
          NpcCombatStyleType.builder().hitType(HitType.MAGIC).subHitType(HitType.TYPELESS).build())
      .damage(NpcCombatDamage.builder().maximum(30).ignorePrayer(true).build())
      .projectile(
          NpcCombatProjectile.builder().id(1482).speedMinimumDistance(8).startHeight(30).build())
      .attackSpeed(1).targetTileGraphic(new Graphic(131))
      .specialAttack(NpcCombatTargetTile.builder().build()).build();
  private static final NpcCombatStyle FREEZE_ATTACK = NpcCombatStyle.builder()
      .type(
          NpcCombatStyleType.builder().hitType(HitType.MAGIC).subHitType(HitType.TYPELESS).build())
      .damage(NpcCombatDamage.builder().ignorePrayer(true).build())
      .projectile(NpcCombatProjectile.builder().id(395).startHeight(30).build()).attackSpeed(10)
      .targetGraphic(new Graphic(369)).effect(NpcCombatEffect.builder().magicBind(15).build())
      .build();

  @Inject
  private Npc npc;
  private NpcCombatStyle lastCombatStyle;
  private int autoAttacks;
  private int specialAttack;
  private List<Tile> poisonTiles = new ArrayList<>();
  private int poisonFireballs;
  private Npc spawn;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder().underKiller(true)
        .rareDropTableRate(NpcCombatDropTable.CHANCE_1_IN_256)
        .clue(NpcCombatDrop.ClueScroll.ELITE, NpcCombatDropTable.CHANCE_1_IN_65).rolls(2);
    var dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_2500)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRACONIC_VISAGE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SKELETAL_VISAGE)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_1500)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.JAR_OF_DECAY)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.VORKI)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_2500)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRACONIC_VISAGE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SKELETAL_VISAGE)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_1000)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGONBONE_NECKLACE)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_RARE).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.VORKATHS_HEAD_21907)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(10.0);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BATTLESTAFF_NOTED, 5, 15)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WRATH_RUNE, 30, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MAGIC_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SPIRIT_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MAHOGANY_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PALM_TREE_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.YEW_SEED)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(5.34);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_BOLTS_UNF, 50, 100)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_BATTLEAXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_LONGSWORD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_PLATELEGS)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_PLATESKIRT)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DEATH_RUNE, 300, 500)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RED_DRAGONHIDE_NOTED, 15, 25)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLACK_DRAGONHIDE_NOTED, 15, 25)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SAPPHIRE_BOLT_TIPS, 25, 35)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.EMERALD_BOLT_TIPS, 25, 35)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUBY_BOLT_TIPS, 31)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DIAMOND_BOLT_TIPS, 30)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.AMETHYST_BOLT_TIPS, 27, 30)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGONSTONE_BOLT_TIPS, 7, 28)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_DART_TIP, 86, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_DART_TIP, 10, 50)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_ARROWTIPS, 27, 50)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGONSTONE_NOTED, 1, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PAPAYA_TREE_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WATERMELON_SEED, 15)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RANARR_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SNAPDRAGON_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TORSTOL_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WILLOW_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TEAK_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MAPLE_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CALQUAT_TREE_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WRATH_TALISMAN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_BONES_NOTED, 7, 28)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_LONGSWORD, 2, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_KITESHIELD, 2, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CHAOS_RUNE, 650, 1000)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GREEN_DRAGONHIDE_NOTED, 25, 32)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLUE_DRAGONHIDE_NOTED, 20, 30)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANTITE_ORE_NOTED, 10, 30)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DIAMOND_NOTED, 10, 30)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CRUSHED_NEST_NOTED, 10, 30)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRAPES_NOTED, 250, 301)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MAGIC_LOGS_NOTED, 50)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 37000, 81000)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MANTA_RAY_NOTED, 25, 55)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_BOLTS_UNF, 50, 100)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SUPERIOR_DRAGON_BONES, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLUE_DRAGONHIDE, 2)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.VORKATH_732);
    combat
        .hitpoints(NpcCombatHitpoints.builder().total(750).bar(HitpointsBar.GREEN_RED_120).build());
    combat.stats(NpcCombatStats.builder().attackLevel(560).magicLevel(150).rangedLevel(308)
        .defenceLevel(214).bonus(CombatBonus.MELEE_ATTACK, 16).bonus(CombatBonus.ATTACK_MAGIC, 150)
        .bonus(CombatBonus.ATTACK_RANGED, 78).bonus(CombatBonus.DEFENCE_STAB, 26)
        .bonus(CombatBonus.DEFENCE_SLASH, 108).bonus(CombatBonus.DEFENCE_CRUSH, 108)
        .bonus(CombatBonus.DEFENCE_MAGIC, 240).bonus(CombatBonus.DEFENCE_RANGED, 26).build());
    combat.aggression(NpcCombatAggression.builder().range(10).build());
    combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    combat.killCount(NpcCombatKillCount.builder().sendMessage(true).build());
    combat.type(NpcCombatType.UNDEAD).deathAnimation(7949);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_SLASH);
    style.damage(NpcCombatDamage.maximum(16));
    style.animation(7951).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(32));
    style.animation(7952).attackSpeed(5);
    style.targetGraphic(new Graphic(1478, 124));
    style.projectile(NpcCombatProjectile.builder().id(1477).startHeight(30).build());
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.maximum(32));
    style.animation(7952).attackSpeed(5);
    style.targetGraphic(new Graphic(1480, 124));
    style.projectile(NpcCombatProjectile.builder().id(1479).startHeight(30).build());
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.DRAGONFIRE);
    style.damage(NpcCombatDamage.maximum(72));
    style.animation(7952).attackSpeed(5);
    style.targetGraphic(new Graphic(157, 124));
    style.projectile(NpcCombatProjectile.builder().id(393).startHeight(30).build());
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.DRAGONFIRE);
    style.damage(NpcCombatDamage.maximum(60));
    style.animation(7952).attackSpeed(5);
    style.targetGraphic(new Graphic(1472, 124));
    style.projectile(NpcCombatProjectile.builder().id(1470).startHeight(30).build());
    style.effect(NpcCombatEffect.builder().venom(6).build());
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.DRAGONFIRE);
    style.damage(NpcCombatDamage.maximum(60));
    style.animation(7952).attackSpeed(5);
    style.targetGraphic(new Graphic(1473, 124));
    style.projectile(NpcCombatProjectile.builder().id(1471).startHeight(30).build());
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.identifier(1);
    style.type(
        NpcCombatStyleType.builder().hitType(HitType.MAGIC).subHitType(HitType.TYPELESS).build());
    style.damage(NpcCombatDamage.builder().maximum(115).ignorePrayer(true).build());
    style.animation(7957).attackSpeed(5);
    style.targetGraphic(new Graphic(157));
    style.projectile(NpcCombatProjectile.builder().id(1481).startHeight(30).curve(32)
        .speedMinimumDistance(10).build());
    var targetTile = NpcCombatTargetTile.builder().adjacentHalfDamage(true);
    style.specialAttack(targetTile.build());
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }

  @Override
  public void restoreHook() {
    lastCombatStyle = null;
    autoAttacks = 6;
    specialAttack = PRandom.randomE(2);
    poisonTiles.clear();
    poisonFireballs = 0;
    npc.getWorld().removeNpc(spawn);
  }

  @Override
  public void tickStartHook() {
    if (lastCombatStyle != null && lastCombatStyle.getProjectile().getId() == 1481
        && npc.getHitDelay() == 1) {
      npc.setAnimation(-1);
    }
    if (npc.getEngagingEntity() != null
        && npc.getController().getMapObject(32000, npc.getEngagingEntity()) != null) {
      npc.getEngagingEntity()
          .addHit(new HitEvent(0, npc.getEngagingEntity(), new Hit(PRandom.randomI(10))));
    }
    if (specialAttack == 0 && npc.isAttacking() && npc.getHitDelay() == 0 && autoAttacks == 0) {
      autoAttacks = 6;
      specialAttack = 1;
      poisonFireballs = 25;
      var speed = getProjectileSpeed(10);
      for (var i = 0; i < 64; i++) {
        var tile = new Tile(2261 + PRandom.randomI(22), 4054 + PRandom.randomI(22));
        if (tile.within(2270, 4062, 2274, 4067) || tile.within(2269, 4063, 2275, 4067)) {
          continue;
        }
        poisonTiles.add(tile);
        var projectile = Graphic.Projectile.builder().id(1483).startTile(npc).endTile(tile)
            .projectileSpeed(speed).build();
        sendMapProjectile(projectile);
      }
      var event = new PEvent(speed.getEventDelay()) {
        @Override
        public void execute() {
          stop();
          if (npc.isLocked()) {
            return;
          }
          for (var tile : poisonTiles) {
            var poison = new MapObject(32000, tile, 10, PRandom.randomI(3));
            npc.getWorld().addEvent(
                new TempMapObject(25 - speed.getEventDelay(), npc.getController(), poison));
          }
        }
      };
      npc.getWorld().addEvent(event);
      npc.setAnimation(7957);
      npc.setHitDelay(2);
    }
  }

  @Override
  public NpcCombatStyle attackTickCombatStyleHook(NpcCombatStyle combatStyle, Entity opponent) {
    return (specialAttack == 1 && autoAttacks == 0) ? FREEZE_ATTACK
        : (poisonFireballs > 0 ? POISON_ATTACK : combatStyle);
  }

  @Override
  public NpcCombatStyle applyAttackCombatStyleHook(NpcCombatStyle combatStyle, Entity opponent) {
    return (opponent.getController().isMagicBound() && combatStyle.getIdentifier() == 1) ? null
        : combatStyle;
  }

  @Override
  public void applyAttackEndHook(NpcCombatStyle combatStyle, Entity opponent,
      int applyAttackLoopCount, HitEvent hitEvent) {
    lastCombatStyle = combatStyle;
    if (combatStyle.getProjectile().getId() == 1471 && opponent instanceof Player) {
      var player = (Player) opponent;
      player.getPrayer().deactivateAll();
      player.getGameEncoder().sendMessage("<col=ff0000>Your prayers have been disabled!");
    } else if (combatStyle == FREEZE_ATTACK) {
      var event = new PEvent(hitEvent.getTick()) {
        @Override
        public void execute() {
          stop();
          if (npc.isLocked()) {
            return;
          }
          npc.getWorld().removeNpc(spawn);
          var tile = PRandom.randomI(1) == 0 ? new Tile(2265, 4057) : new Tile(2278, 4069);
          spawn = new Npc(npc.getController(), NpcId.ZOMBIFIED_SPAWN_64, tile);
          spawn.getMovement().setFollowing(npc.getEngagingEntity());
        }
      };
      npc.getWorld().addEvent(event);
    }
  }

  @Override
  public void attackTickEndHook() {
    if (lastCombatStyle == POISON_ATTACK) {
      if (--poisonFireballs == 0) {
        npc.setHitDelay(10);
      }
    } else if (lastCombatStyle == FREEZE_ATTACK) {
      autoAttacks = 6;
      specialAttack = 0;
    } else {
      autoAttacks--;
    }
  }

  @Override
  public double damageReceivedHook(Entity opponent, double damage, HitType hitType,
      HitType defenceType) {
    if (poisonFireballs > 0) {
      damage *= 0.5;
    }
    return damage;
  }

  @Override
  public int dragonfireDamageHook(NpcCombatStyle combatStyle, Entity opponent, int damage) {
    if (opponent instanceof Player) {
      var player = (Player) opponent;
      if (player.getSkills().getSuperAntifireTime() > 0) {
        damage *= 0.6;
      } else if (player.getSkills().getAntifireTime() > 0) {
        damage *= 0.8;
      }
      if (player.getEquipment().wearingDragonfireShield()
          || player.getPrayer().hasActive("protect from magic")) {
        damage *= 0.4;
      }
      if (player.getSkills().getSuperAntifireTime() > 0
          && player.getEquipment().wearingDragonfireShield()) {
        damage = 0;
      }
    }
    return damage;
  }

  @Override
  public void deathDropItemsHook(Player player, int additionalPlayerLoopCount, Tile dropTile) {
    if ((player.getCombat().getNPCKillCount(npc.getDef().getCombat().getKillCountName(npc.getId()))
        % 50) != 0) {
      return;
    }
    npc.getController().addMapItem(new Item(ItemId.VORKATHS_HEAD_21907, 1), dropTile, player);
    player.getCombat().logNPCItem(npc.getDef().getCombat().getKillCountName(npc.getId()),
        ItemId.VORKATHS_HEAD_21907, 1);
  }
}
