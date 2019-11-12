package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.google.inject.Inject;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Entity;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.HitMark;
import com.palidinodh.osrscore.model.HitType;
import com.palidinodh.osrscore.model.HitpointsBar;
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
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSlayer;
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

public class HydraCombat extends NpcCombat {
  private static final NpcCombatDropTable SUPERIOR_DROP_TABLE = NpcCombatDropTable.builder()
      .chance(3.15).log(true)
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.IMBUED_HEART, 1, 1, 1)))
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ETERNAL_GEM, 1, 1, 1)))
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DUST_BATTLESTAFF, 1, 1, 3)))
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MIST_BATTLESTAFF, 1, 1, 3))).build();
  private static final NpcCombatDropTable CURSED_DROP_TABLE =
      NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_5000).log(true)
          .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.HYDRA_LEATHER).weight(2)))
          .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.HYDRAS_CLAW).weight(1))).build();
  private static final int[] ANIMATIONS = {8261, 8262, 8263};
  private static final Graphic[] TILE_GRAPHICS =
      {new Graphic(1654), new Graphic(1655), new Graphic(1656), new Graphic(1657),
          new Graphic(1658), new Graphic(1659), new Graphic(1660), new Graphic(1661)};
  private static final NpcCombatStyle SPECIAL_ATTACK = NpcCombatStyle.builder()
      .type(NpcCombatStyleType.builder().hitType(HitType.MAGIC).subHitType(HitType.TYPELESS)
          .hitMark(HitMark.POISON).build())
      .damage(NpcCombatDamage.builder().maximum(4).alwaysMaximum(true).ignorePrayer(true).build())
      .attackSpeed(6).targetGraphic(new Graphic(1645)).targetTileGraphic(new Graphic(1654))
      .projectile(NpcCombatProjectile.builder().id(1644).speedMinimumDistance(8).build())
      .effect(NpcCombatEffect.builder().poison(4).build())
      .specialAttack(NpcCombatTargetTile.builder().duration(16).radius(1)
          .breakOff(NpcCombatTargetTile.BreakOff.builder().count(2).distance(3).build()).build())
      .build();

  @Inject
  private Npc npc;
  private HitType hitType;
  private int hitCount;
  private int poisonDelay;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder()
        .clue(NpcCombatDrop.ClueScroll.ELITE, NpcCombatDropTable.CHANCE_1_IN_512)
        .clue(NpcCombatDrop.ClueScroll.HARD, NpcCombatDropTable.CHANCE_1_IN_128);
    var dropTable =
        NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_5000).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_KNIFE, 200, 400)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_THROWNAXE, 200, 400)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_5000).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.HYDRA_TAIL)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(0.056)
        .order(NpcCombatDropTable.Order.RANDOM_UNIQUE).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.HYDRAS_EYE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.HYDRAS_HEART)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.HYDRAS_FANG)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_RARE);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ONYX_BOLT_TIPS, 8, 30)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BATTLESTAFF, 2, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.FIRE_BATTLESTAFF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WATER_BATTLESTAFF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_KITESHIELD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_PLATEBODY)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MYSTIC_ROBE_BOTTOM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_LONGSWORD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLOOD_RUNE, 15, 44)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CHAOS_RUNE, 22, 50)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.IRIT_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TOADFLAX_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.AVANTOE_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.POTATO_CACTUS_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BELLADONNA_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_GUAM_LEAF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_MARRENTILL)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_HARRALANDER)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_TARROMIN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_RANARR_WEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_IRIT_LEAF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_CADANTINE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_RANARR_WEED_NOTED, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_TORSTOL_NOTED, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SUPER_COMBAT_POTION_1)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_BONES_NOTED, 3, 5)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SUPER_RESTORE_1, 1, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.EMERALD_BOLT_TIPS, 20)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUBY_BOLT_TIPS, 20)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DIAMOND_BOLT_TIPS, 20)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLACK_DHIDE_CHAPS)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WATER_RUNE, 71, 89)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.FIRE_RUNE, 56, 86)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DEATH_RUNE, 30, 60)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LAW_RUNE, 32, 59)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 510, 4338)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MONKFISH)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.HYDRA_BONES)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.HYDRA_194);
    combat.hitpoints(NpcCombatHitpoints.total(300));
    combat.stats(NpcCombatStats.builder().magicLevel(210).rangedLevel(210).defenceLevel(100)
        .bonus(CombatBonus.MELEE_DEFENCE, 160).bonus(CombatBonus.DEFENCE_MAGIC, 160).build());
    combat.slayer(NpcCombatSlayer.builder().level(95).build());
    combat.aggression(NpcCombatAggression.PLAYERS);
    combat.deathAnimation(8264);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(22));
    style.animation(8263).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.maximum(22));
    style.animation(8263).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    var cursedCombat = NpcCombatDefinition.builder();
    cursedCombat.id(NpcId.CURSED_HYDRA_400_16012);
    cursedCombat
        .hitpoints(NpcCombatHitpoints.builder().total(400).bar(HitpointsBar.GREEN_RED_60).build());
    cursedCombat.stats(
        NpcCombatStats.builder().attackLevel(80).magicLevel(210).rangedLevel(210).defenceLevel(100)
            .bonus(CombatBonus.MELEE_DEFENCE, 160).bonus(CombatBonus.DEFENCE_MAGIC, 160).build());
    cursedCombat.slayer(NpcCombatSlayer.builder().level(95).build());
    cursedCombat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).bind(true).build());
    cursedCombat.deathAnimation(8257);
    cursedCombat.drop(drop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(22));
    style.animation(8255).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    cursedCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.maximum(22));
    style.animation(8256).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    cursedCombat.style(style.build());


    return Arrays.asList(combat.build(), cursedCombat.build());
  }

  @Override
  public void restoreHook() {
    hitType = PRandom.randomE(2) == 0 ? HitType.RANGED : HitType.MAGIC;
    hitCount = 0;
    poisonDelay = 8;
  }

  @Override
  public HitType attackTickHitTypeHook(HitType hitType, Entity opponent) {
    return hitType;
  }

  @Override
  public NpcCombatStyle attackTickCombatStyleHook(NpcCombatStyle combatStyle, Entity opponent) {
    return poisonDelay == 0 ? SPECIAL_ATTACK : combatStyle;
  }

  @Override
  public int attackTickAnimationHook(NpcCombatStyle combatStyle, Entity popponent) {
    return ANIMATIONS[PRandom.randomE(ANIMATIONS.length)];
  }

  @Override
  public Graphic applyAttackTargetTileGraphicHook(NpcCombatStyle combatStyle, Entity opponent) {
    if (combatStyle.getTargetTileGraphic() != null
        && combatStyle.getTargetTileGraphic().getId() == 1654) {
      return TILE_GRAPHICS[PRandom.randomE(TILE_GRAPHICS.length)];
    }
    return combatStyle.getTargetTileGraphic();
  }

  @Override
  public void attackTickEndHook() {
    if (++hitCount == 3) {
      hitType = hitType == HitType.RANGED ? HitType.MAGIC : HitType.RANGED;
      hitCount = 0;
    }
    if (--poisonDelay < 0) {
      poisonDelay = 8;
    }
  }

  @Override
  public NpcCombatDropTable deathDropItemsTableHook(Npc npc, Player player, int dropRateDivider,
      int roll, NpcCombatDropTable table) {
    if (npc.getId() == NpcId.CURSED_HYDRA_400_16012) {
      if (!player.getSkills().isWildernessSlayerTask(npc)) {
        player.getGameEncoder().sendMessage("Without an assigned task, the loot turns to dust...");
        return null;
      }
      if (CURSED_DROP_TABLE.canDrop(npc, player, dropRateDivider, roll)) {
        return CURSED_DROP_TABLE;
      }
    }
    if (npc.getId() == NpcId.CURSED_HYDRA_400_16012
        && SUPERIOR_DROP_TABLE.canDrop(npc, player, dropRateDivider, roll)) {
      return SUPERIOR_DROP_TABLE;
    }
    return table;
  }

  @Override
  public double dropTableChanceHook(Player player, int dropRateDivider, int roll,
      NpcCombatDropTable table) {
    var chance = table.getChance();
    if (table != SUPERIOR_DROP_TABLE && chance < 1 && player.getSkills().isAnySlayerTask(npc)) {
      chance *= 5;
    }
    if (npc.getId() == NpcId.CURSED_HYDRA_400_16012 && table == SUPERIOR_DROP_TABLE) {
      chance /= 32;
    }
    return chance;
  }
}
