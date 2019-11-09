package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.google.inject.Inject;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Entity;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.HitEvent;
import com.palidinodh.osrscore.model.HitType;
import com.palidinodh.osrscore.model.Tile;
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
import com.palidinodh.osrscore.model.npc.combat.NpcCombatKillCount;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSpawn;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.random.PRandom;
import lombok.var;

public class ArmadylGodWarsChamberCombat extends NpcCombat {
  @Inject
  private Npc npc;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var kreearraDrop = NpcCombatDrop.builder().rareDropTableRate(NpcCombatDropTable.CHANCE_1_IN_256)
        .clue(NpcCombatDrop.ClueScroll.ELITE, NpcCombatDropTable.CHANCE_1_IN_250);
    var dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_5000)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PET_KREEARRA)));
    kreearraDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_508)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ARMADYL_HILT)));
    kreearraDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_254).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_1)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_3)));
    kreearraDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_127)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ARMADYL_HELMET)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ARMADYL_CHESTPLATE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ARMADYL_CHAINSKIRT)));
    kreearraDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MIND_RUNE, 500, 601)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_ARROW, 100, 105)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGONSTONE_BOLTS_E, 5, 15)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RANGING_POTION_3, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SUPER_DEFENCE_3, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_SWORD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.YEW_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CRYSTAL_KEY)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_TORSTOL_NOTED, 1, 5)));
    kreearraDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_CROSSBOW)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNITE_BOLTS, 18, 25)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLACK_DHIDE_BODY)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 18000, 21000)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DWARF_WEED_NOTED, 5, 22)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DWARF_WEED_SEED, 3)));
    kreearraDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BIG_BONES)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.FEATHER, 1, 15)));
    kreearraDrop.table(dropTable.build());


    var kreearraCombat = NpcCombatDefinition.builder();
    kreearraCombat.id(NpcId.KREEARRA_580);
    kreearraCombat.spawn(NpcCombatSpawn.builder().respawnDelay(100).build());
    kreearraCombat.hitpoints(NpcCombatHitpoints.total(255));
    kreearraCombat.stats(NpcCombatStats.builder().attackLevel(300).magicLevel(200).rangedLevel(380)
        .defenceLevel(260).bonus(CombatBonus.MELEE_ATTACK, 136)
        .bonus(CombatBonus.ATTACK_RANGED, 120).bonus(CombatBonus.MELEE_DEFENCE, 180)
        .bonus(CombatBonus.DEFENCE_MAGIC, 200).bonus(CombatBonus.DEFENCE_RANGED, 200).build());
    kreearraCombat.aggression(NpcCombatAggression.builder().range(16).build());
    kreearraCombat
        .immunity(NpcCombatImmunity.builder().poison(true).venom(true).melee(true).build());
    kreearraCombat.killCount(NpcCombatKillCount.builder().sendMessage(true).build());
    kreearraCombat.deathAnimation(6979).blockAnimation(6978);
    kreearraCombat.drop(kreearraDrop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_STAB);
    style.damage(NpcCombatDamage.maximum(26));
    style.animation(6981).attackSpeed(3);
    style.projectile(NpcCombatProjectile.id(335));
    kreearraCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(71));
    style.animation(6980).attackSpeed(3);
    style.projectile(NpcCombatProjectile.id(335));
    style.multiTarget(true);
    kreearraCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(
        NpcCombatStyleType.builder().hitType(HitType.MAGIC).subHitType(HitType.RANGED).build());
    style.damage(NpcCombatDamage.maximum(21));
    style.animation(6980).attackSpeed(3);
    style.projectile(NpcCombatProjectile.id(335));
    style.multiTarget(true);
    kreearraCombat.style(style.build());


    var wingmanSkreeDrop = NpcCombatDrop.builder().clue(NpcCombatDrop.ClueScroll.HARD,
        NpcCombatDropTable.CHANCE_1_IN_128);
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_508).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_1)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_3)));
    wingmanSkreeDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_127).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ARMADYL_HELMET).weight(1)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ARMADYL_CHESTPLATE).weight(1)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ARMADYL_CHAINSKIRT).weight(1)));
    dropTable
        .drop(NpcCombatDropTableDrop.builder().item(new RandomItem(ItemId.COINS, 1000, 5000, 120))
            .log(NpcCombatDropTableDrop.Log.NO).build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SMOKE_RUNE, 15, 25)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MANTA_RAY, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MUSHROOM_POTATO, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CRUSHED_NEST, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_KWUARM)));
    wingmanSkreeDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_ARROW, 95, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_DART, 95, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 1300, 1500)));
    wingmanSkreeDrop.table(dropTable.build());


    var wingmanSkreeCombat = NpcCombatDefinition.builder();
    wingmanSkreeCombat.id(NpcId.WINGMAN_SKREE_143);
    wingmanSkreeCombat.spawn(NpcCombatSpawn.builder().respawnDelay(50)
        .respawnWithId(NpcId.KREEARRA_580).respawnWithId(NpcId.FLOCKLEADER_GEERIN_149)
        .respawnWithId(NpcId.FLIGHT_KILISA_159).build());
    wingmanSkreeCombat.hitpoints(NpcCombatHitpoints.total(121));
    wingmanSkreeCombat.stats(NpcCombatStats.builder().attackLevel(80).magicLevel(150)
        .rangedLevel(100).defenceLevel(160).bonus(CombatBonus.MELEE_ATTACK, 45).build());
    wingmanSkreeCombat.aggression(NpcCombatAggression.builder().range(16).build());
    wingmanSkreeCombat.immunity(NpcCombatImmunity.builder().melee(true).build());
    wingmanSkreeCombat
        .killCount(NpcCombatKillCount.builder().asName("Kree'arra's bodyguard").build());
    wingmanSkreeCombat.deathAnimation(6959).blockAnimation(6958);
    wingmanSkreeCombat.drop(wingmanSkreeDrop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.maximum(16));
    style.animation(6955).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    wingmanSkreeCombat.style(style.build());


    var flockleaderGeerinDrop = NpcCombatDrop.builder().clue(NpcCombatDrop.ClueScroll.HARD,
        NpcCombatDropTable.CHANCE_1_IN_128);
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_508).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_1)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_3)));
    flockleaderGeerinDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_127).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ARMADYL_HELMET).weight(1)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ARMADYL_CHESTPLATE).weight(1)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ARMADYL_CHAINSKIRT).weight(1)));
    dropTable
        .drop(NpcCombatDropTableDrop.builder().item(new RandomItem(ItemId.COINS, 1000, 5000, 120))
            .log(NpcCombatDropTableDrop.Log.NO).build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MANTA_RAY, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MUSHROOM_POTATO, 3)));
    flockleaderGeerinDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SMOKE_RUNE, 13, 25)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_DART, 95, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_ARROW, 95, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CRUSHED_NEST, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_KWUARM)));
    flockleaderGeerinDrop.table(dropTable.build());


    var flockleaderGeerinCombat = NpcCombatDefinition.builder();
    flockleaderGeerinCombat.id(NpcId.FLOCKLEADER_GEERIN_149);
    flockleaderGeerinCombat
        .spawn(NpcCombatSpawn.builder().respawnDelay(50).respawnWithId(NpcId.KREEARRA_580)
            .respawnWithId(NpcId.WINGMAN_SKREE_143).respawnWithId(NpcId.FLIGHT_KILISA_159).build());
    flockleaderGeerinCombat.hitpoints(NpcCombatHitpoints.total(132));
    flockleaderGeerinCombat.stats(NpcCombatStats.builder().attackLevel(80).magicLevel(50)
        .rangedLevel(150).defenceLevel(175).bonus(CombatBonus.ATTACK_RANGED, 60).build());
    flockleaderGeerinCombat.aggression(NpcCombatAggression.builder().range(16).build());
    flockleaderGeerinCombat.immunity(NpcCombatImmunity.builder().melee(true).build());
    flockleaderGeerinCombat
        .killCount(NpcCombatKillCount.builder().asName("Kree'arra's bodyguard").build());
    flockleaderGeerinCombat.deathAnimation(6959).blockAnimation(6958);
    flockleaderGeerinCombat.drop(flockleaderGeerinDrop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(25));
    style.animation(6956).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    flockleaderGeerinCombat.style(style.build());


    var flightKilisaDrop = NpcCombatDrop.builder().clue(NpcCombatDrop.ClueScroll.HARD,
        NpcCombatDropTable.CHANCE_1_IN_128);
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_508).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_1)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_3)));
    flightKilisaDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_127).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ARMADYL_HELMET).weight(1)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ARMADYL_CHESTPLATE).weight(1)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ARMADYL_CHAINSKIRT).weight(1)));
    dropTable
        .drop(NpcCombatDropTableDrop.builder().item(new RandomItem(ItemId.COINS, 1000, 5000, 120))
            .log(NpcCombatDropTableDrop.Log.NO).build());
    flightKilisaDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MANTA_RAY, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MUSHROOM_POTATO, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CRUSHED_NEST, 2)));
    flightKilisaDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 1006, 1500)));
    flightKilisaDrop.table(dropTable.build());


    var flightKilisaCombat = NpcCombatDefinition.builder();
    flightKilisaCombat.id(NpcId.FLIGHT_KILISA_159);
    flightKilisaCombat.spawn(NpcCombatSpawn.builder().respawnDelay(50)
        .respawnWithId(NpcId.KREEARRA_580).respawnWithId(NpcId.WINGMAN_SKREE_143)
        .respawnWithId(NpcId.FLOCKLEADER_GEERIN_149).build());
    flightKilisaCombat.hitpoints(NpcCombatHitpoints.total(159));
    flightKilisaCombat.stats(NpcCombatStats.builder().attackLevel(124).magicLevel(50)
        .rangedLevel(169).defenceLevel(175).build());
    flightKilisaCombat.aggression(NpcCombatAggression.builder().range(16).build());
    flightKilisaCombat.immunity(NpcCombatImmunity.builder().melee(true).build());
    flightKilisaCombat
        .killCount(NpcCombatKillCount.builder().asName("Kree'arra's bodyguard").build());
    flightKilisaCombat.deathAnimation(6959).blockAnimation(6958);
    flightKilisaCombat.drop(flightKilisaDrop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_SLASH);
    style.damage(NpcCombatDamage.maximum(18));
    style.animation(6957).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    flightKilisaCombat.style(style.build());


    return Arrays.asList(kreearraCombat.build(), wingmanSkreeCombat.build(),
        flockleaderGeerinCombat.build(), flightKilisaCombat.build());
  }

  @Override
  public void restoreHook() {
    if (npc.getId() != NpcId.KREEARRA_580) {
      return;
    }
    var respawns = new int[] { NpcId.WINGMAN_SKREE_143, NpcId.FLOCKLEADER_GEERIN_149,
        NpcId.FLIGHT_KILISA_159 };
    for (var id : respawns) {
      var respawningNpc = npc.getController().getNpc(id);
      if (respawningNpc != null && !respawningNpc.isVisible() && respawningNpc.getRespawns()) {
        respawningNpc.restore();
      }
    }
  }

  @Override
  public HitType attackTickHitTypeHook(HitType hitType, Entity opponent) {
    if (npc.getId() != NpcId.KREEARRA_580) {
      return hitType;
    }
    if (hitType == HitType.MELEE && !opponent.isAttacking() && opponent.getHitDelay() <= -2) {
      return hitType;
    }
    return hitType == HitType.MELEE ? HitType.getRandomType(HitType.RANGED, HitType.MAGIC)
        : hitType;
  }

  @Override
  public NpcCombatStyle applyAttackCombatStyleHook(NpcCombatStyle combatStyle, Entity opponent) {
    if (npc.getId() != NpcId.KREEARRA_580) {
      return combatStyle;
    }
    if (combatStyle.getType().getHitType() == HitType.MELEE) {
      return combatStyle;
    }
    return PRandom.randomI(1) == 0 ? npc.getDef().getCombat().getRangedAttack()
        : npc.getDef().getCombat().getMagicAttack();
  }

  @Override
  public void applyAttackEndHook(NpcCombatStyle combatStyle, Entity opponent,
      int applyAttackLoopCount, HitEvent hitEvent) {
    if (npc.getId() != NpcId.KREEARRA_580) {
      return;
    }
    if (PRandom.randomI(4) == 0) {
      var x = opponent.getX();
      var y = opponent.getY();
      if (x < npc.getX() && y <= npc.getY() && Route.canMove(opponent, x, y, x - 1, y)) {
        x--;
      } else if (x > npc.getX() && y <= npc.getY() && Route.canMove(opponent, x, y, x + 1, y)) {
        x++;
      } else if (Route.canMove(opponent, x, y, x, y + 1)) {
        y++;
      } else if (x < npc.getX() && Route.canMove(opponent, x, y, x - 1, y)) {
        x--;
      } else if (x > npc.getX() && Route.canMove(opponent, x, y, x + 1, y)) {
        x++;
      }
      opponent.setLock(1);
      opponent.setGraphic(245, 100);
      if (opponent.getX() != x || opponent.getY() != y) {
        opponent.getMovement().teleport(x, y, opponent.getHeight());
      }
    }
  }

  @Override
  public Graphic.ProjectileSpeed projectileSpeedHook(NpcCombatStyle combatStyle,
      Graphic.ProjectileSpeed speed, Tile tile, HitType hitType, int minimumDistance) {
    if (npc.getId() != NpcId.KREEARRA_580) {
      return speed;
    }
    var distance = Math.max(minimumDistance, npc.getDistance(tile));
    var eventDelay = 0;
    var clientSpeed = 0;
    var clientDelay = 0;
    if (hitType != HitType.MELEE) {
      eventDelay = 1;
      if (distance >= 3 && distance <= 8) {
        eventDelay = 2;
      } else if (distance >= 9) {
        eventDelay = 3;
      }
      clientSpeed = 5 + distance * 5;
      clientDelay = 41;
    }
    return new Graphic.ProjectileSpeed(eventDelay, clientDelay, clientSpeed);
  }

  @Override
  public void deathDropItemsHook(Player player, int additionalPlayerLoopCount, Tile dropTile) {
    player.getArea().script("increase_armadyl_killcount");
  }
}
