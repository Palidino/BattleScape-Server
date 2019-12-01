package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.google.inject.Inject;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Entity;
import com.palidinodh.osrscore.model.ForceMovement;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.Hit;
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
import com.palidinodh.osrscore.model.npc.combat.NpcCombatFocus;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatKillCount;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.random.PRandom;
import lombok.var;

class CallistoCombat extends NpcCombat {
  @Inject
  private Npc npc;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder().clue(NpcCombatDrop.ClueScroll.ELITE,
        NpcCombatDropTable.CHANCE_1_IN_100);
    var dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_4096)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.VESTAS_LONGSWORD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STATIUSS_WARHAMMER)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.VESTAS_SPEAR)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MORRIGANS_JAVELIN, 100)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MORRIGANS_THROWING_AXE, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ZURIELS_STAFF)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_2000).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CALLISTO_CUB)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_512).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TYRANNICAL_RING)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_256).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_2H_SWORD)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(0.59).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_PICKAXE)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_RARE);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.YEW_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MAGIC_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PALM_TREE_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNCUT_DRAGONSTONE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_TOADFLAX_NOTED, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DARK_FISHING_BAIT, 375)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_2H_SWORD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SOUL_RUNE, 250)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DEATH_RUNE, 300)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CHAOS_RUNE, 400)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLOOD_RUNE, 200)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SNAPDRAGON_SEED, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RANARR_SEED, 5)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNCUT_RUBY_NOTED, 20)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNCUT_DIAMOND_NOTED, 10)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COCONUT_NOTED, 60)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SUPERCOMPOST_NOTED, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CRUSHED_NEST_NOTED, 75)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CANNONBALL, 250)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MAGIC_LOGS_NOTED, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LIMPWURT_ROOT_NOTED, 50)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RED_DRAGONHIDE_NOTED, 75)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MAHOGANY_LOGS_NOTED, 400)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_TORSTOL_NOTED, 1, 5)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_PICKAXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DARK_CRAB_NOTED, 8)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SUPER_RESTORE_4, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 12000, 20000)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BIG_BONES)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.CALLISTO_470);
    combat.hitpoints(NpcCombatHitpoints.total(255));
    combat.stats(NpcCombatStats.builder().attackLevel(350).defenceLevel(440)
        .bonus(CombatBonus.DEFENCE_STAB, 135).bonus(CombatBonus.DEFENCE_SLASH, 104)
        .bonus(CombatBonus.DEFENCE_CRUSH, 175).bonus(CombatBonus.DEFENCE_MAGIC, 900)
        .bonus(CombatBonus.DEFENCE_RANGED, 230).build());
    combat.aggression(NpcCombatAggression.builder().range(8).build());
    combat.immunity(NpcCombatImmunity.builder().venom(true).build());
    combat.focus(NpcCombatFocus.builder().disableFollowingOpponent(true).build());
    combat.killCount(NpcCombatKillCount.builder().sendMessage(true).build());
    combat.deathAnimation(4929).blockAnimation(4928);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(60));
    style.animation(4925).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(
        NpcCombatStyleType.builder().hitType(HitType.MAGIC).subHitType(HitType.TYPELESS).build());
    style.damage(NpcCombatDamage.builder().maximum(3).alwaysMaximum(true).build());
    style.animation(4925).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }

  @Override
  public void tickStartHook() {
    var entity = npc.getEngagingEntity();
    if (!npc.isAttacking() || !npc.withinDistance(entity, 10) || entity.isLocked()) {
      return;
    }
    var shockwaveChance = Math.max(32 - npc.getDistance(entity), 1);
    if (PRandom.randomE(shockwaveChance) == 0) {
      entity.setLock(1);
      var projectile = Graphic.Projectile.builder().id(395).startTile(npc).entity(entity)
          .projectileSpeed(getProjectileSpeed(entity)).build();
      sendMapProjectile(projectile);
      entity.setGraphic(245, 100);
      var hitEvent = new HitEvent(0, entity, npc, new Hit(PRandom.randomI(60)));
      entity.addHit(hitEvent);
      if (entity instanceof Player) {
        var player = (Player) entity;
        player.getGameEncoder()
            .sendMessage("Callisto's fury sends an almighty shockwave through you.");
      }
    }
  }

  @Override
  public HitType attackTickHitTypeHook(HitType hitType, Entity opponent) {
    var withinDistance = npc.withinDistance(npc.getEngagingEntity(), 1);
    if (withinDistance && PRandom.randomE(4) != 0 || !withinDistance && PRandom.randomE(8) != 0) {
      return HitType.MELEE;
    }
    return hitType;
  }

  @Override
  public void applyAttackEndHook(NpcCombatStyle combatStyle, Entity opponent,
      int applyAttackLoopCount, HitEvent hitEvent) {
    if (combatStyle.getType().getHitType() == HitType.MAGIC) {
      var projectile = Graphic.Projectile.builder().id(1256).startTile(npc).entity(opponent)
          .projectileSpeed(getProjectileSpeed(opponent)).build();
      sendMapProjectile(projectile);
      npc.getController().sendMapGraphic(opponent, new Graphic(1255));
      if (opponent instanceof Player) {
        Player player = (Player) opponent;
        player.getGameEncoder().sendMessage("Callisto's roar throws you backwards.");
      }
      var distance = 4;
      Tile tile;
      var direction = 0;
      if (opponent.getY() < npc.getY()) {
        tile = new Tile(opponent.getX(), opponent.getY() - distance);
        while (!Route.canMove(opponent, tile) && distance > 0) {
          tile = new Tile(opponent.getX(), opponent.getY() - (--distance));
        }
        direction = Tile.NORTH;
      } else {
        tile = new Tile(opponent.getX(), opponent.getY() + distance);
        while (!Route.canMove(opponent, tile) && distance > 0) {
          tile = new Tile(opponent.getX(), opponent.getY() + --distance);
        }
        direction = Tile.SOUTH;
      }
      var fm = new ForceMovement(tile, 1, direction);
      opponent.setLock(4);
      opponent.setForceMovementTeleport(fm, 734, 1, null);
    }
    if (PRandom.randomE(8) == 0) {
      npc.setGraphic(157);
      npc.adjustHitpoints(Math.min(hitEvent.getDamage(), 10), 0);
    }
  }
}
