package com.palidinodh.osrsscript.npc.combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.Main;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Entity;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.Hit;
import com.palidinodh.osrscore.model.HitMark;
import com.palidinodh.osrscore.model.HitType;
import com.palidinodh.osrscore.model.HitpointsBar;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.item.RandomItem;
import com.palidinodh.osrscore.model.map.MapObject;
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
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSlayer;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSpawn;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatEffect;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.npc.combat.style.special.NpcCombatTargetTile;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.random.PRandom;
import com.palidinodh.rs.setting.Settings;
import com.palidinodh.util.PCollection;
import com.palidinodh.util.PEvent;
import com.palidinodh.util.PPolygon;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.var;

public class AlchemicalHydraCombat extends NpcCombat {
  private static final NpcCombatStyle.NpcCombatStyleBuilder POISON_ATTACK_BUILDER = NpcCombatStyle
      .builder()
      .type(NpcCombatStyleType.builder().hitType(HitType.MAGIC).subHitType(HitType.TYPELESS)
          .hitMark(HitMark.POISON).build())
      .damage(NpcCombatDamage.builder().maximum(12).alwaysMaximum(true).ignorePrayer(true).build())
      .effect(NpcCombatEffect.builder().poison(4).build()).attackSpeed(6)
      .targetGraphic(new Graphic(1645)).targetTileGraphic(new Graphic(1654))
      .projectile(NpcCombatProjectile.builder().id(1644).speedMinimumDistance(8).build())
      .specialAttack(NpcCombatTargetTile.builder().radius(1).duration(16)
          .breakOff(NpcCombatTargetTile.BreakOff.builder().count(4).distance(4).build()).build());
  private static final NpcCombatStyle POISON_ATTACK_1 =
      POISON_ATTACK_BUILDER.animation(8234).build();
  private static final NpcCombatStyle POISON_ATTACK_2 =
      POISON_ATTACK_BUILDER.animation(8255).build();
  private static final Graphic[] POISON_TILE_GRAPHICS =
      {new Graphic(1654), new Graphic(1655), new Graphic(1656), new Graphic(1657),
          new Graphic(1658), new Graphic(1659), new Graphic(1660), new Graphic(1661)};
  private static final MapObject BLUE_VENT = new MapObject(34570, new Tile(1362, 10272, 0), 10, 0);
  private static final MapObject GREEN_VENT = new MapObject(34569, new Tile(1371, 10272, 0), 10, 0);
  private static final MapObject RED_VENT = new MapObject(34568, new Tile(1371, 10263, 0), 10, 0);
  private static final MapObject[] VENTS = {BLUE_VENT, GREEN_VENT, RED_VENT};
  private static final Tile[] FIRE_TILES_NORTH = {new Tile(0, 1), new Tile(1365, 10271),
      new Tile(1366, 10271), new Tile(1367, 10271), new Tile(1368, 10271)};
  private static final Tile[] FIRE_TILES_EAST = {new Tile(1, 0), new Tile(1370, 10269),
      new Tile(1370, 10268), new Tile(1370, 10267), new Tile(1370, 10266)};
  private static final Tile[] FIRE_TILES_SOUTH = {new Tile(0, -1), new Tile(1365, 10264),
      new Tile(1366, 10264), new Tile(1367, 10264), new Tile(1368, 10264)};
  private static final Tile[] FIRE_TILES_WEST = {new Tile(-1, 0), new Tile(1363, 10269),
      new Tile(1363, 10268), new Tile(1363, 10267), new Tile(1363, 10266)};
  private static final Tile[] FIRE_TILES_NORTH_WEST = {new Tile(-1, 1), new Tile(1363, 10271),
      new Tile(1363, 10269), new Tile(1363, 10270), new Tile(1364, 10271), new Tile(1365, 10271)};
  private static final Tile[] FIRE_TILES_SOUTH_WEST = {new Tile(-1, -1), new Tile(1363, 10264),
      new Tile(1365, 10264), new Tile(1364, 10264), new Tile(1363, 10265), new Tile(1363, 10266)};
  private static final Tile[] FIRE_TILES_NORTH_EAST = {new Tile(1, 1), new Tile(1370, 10271),
      new Tile(1369, 10271), new Tile(1368, 10271), new Tile(1370, 10270), new Tile(1370, 10269)};
  private static final Tile[] FIRE_TILES_SOUTH_EAST = {new Tile(-1, 1), new Tile(1370, 10264),
      new Tile(1370, 10265), new Tile(1370, 10266), new Tile(1369, 10264), new Tile(1368, 10264)};
  private static final FireAttack NORTH_FIRE = new FireAttack(new PPolygon(), FIRE_TILES_NORTH_WEST,
      FIRE_TILES_NORTH_EAST, new Tile(1366, 10271));
  private static final FireAttack EAST_FIRE =
      new FireAttack(new PPolygon(new int[] {1366, 1377, 1377}, new int[] {10267, 10278, 10257}),
          FIRE_TILES_NORTH_EAST, FIRE_TILES_SOUTH_EAST, new Tile(1370, 10267));
  private static final FireAttack SOUTH_FIRE =
      new FireAttack(new PPolygon(new int[] {1366, 1377, 1356}, new int[] {10268, 10257, 10257}),
          FIRE_TILES_SOUTH_WEST, FIRE_TILES_SOUTH_EAST, new Tile(1366, 10264));
  private static final FireAttack WEST_FIRE =
      new FireAttack(new PPolygon(new int[] {1367, 1356, 1356}, new int[] {10267, 10257, 10278}),
          FIRE_TILES_NORTH_WEST, FIRE_TILES_SOUTH_WEST, new Tile(1363, 10267));
  private static final FireAttack NORTH_EAST_FIRE = new FireAttack(
      new PPolygon(new int[] {1369, 1369, 1377, 1377}, new int[] {10270, 10278, 10278, 10270}),
      FIRE_TILES_NORTH, FIRE_TILES_EAST, new Tile(1370, 10271));
  private static final FireAttack SOUTH_EAST_FIRE = new FireAttack(
      new PPolygon(new int[] {1369, 1377, 1377, 1369}, new int[] {10265, 10265, 10257, 10257}),
      FIRE_TILES_EAST, FIRE_TILES_SOUTH, new Tile(1370, 10271));
  private static final FireAttack SOUTH_WEST_FIRE = new FireAttack(
      new PPolygon(new int[] {1364, 1364, 1356, 1356}, new int[] {10265, 10257, 10257, 10265}),
      FIRE_TILES_WEST, FIRE_TILES_SOUTH, new Tile(1363, 10264));
  private static final FireAttack NORTH_WEST_FIRE = new FireAttack(
      new PPolygon(new int[] {1364, 1356, 1356, 1364}, new int[] {10270, 10270, 10278, 10278}),
      FIRE_TILES_NORTH, FIRE_TILES_WEST, new Tile(1363, 10271));
  private static final FireAttack[] FIRE_ATTACKS = {NORTH_EAST_FIRE, SOUTH_EAST_FIRE,
      SOUTH_WEST_FIRE, NORTH_WEST_FIRE, EAST_FIRE, SOUTH_FIRE, WEST_FIRE};

  private Npc npc;
  private HitType hitStyle;
  private NpcCombatStyle currentCombatStyle;
  private int hitCount;
  private int specialDelay;
  private boolean damageReduction;
  private PEvent lightningCastEvent;
  private List<PEvent> lightningEvents = new ArrayList<>();
  private int ventDelay;
  private PEvent fireBleedEvent;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder().rareDropTableRate(NpcCombatDropTable.CHANCE_1_IN_256)
        .clue(NpcCombatDrop.ClueScroll.ELITE, NpcCombatDropTable.CHANCE_1_IN_256)
        .clue(NpcCombatDrop.ClueScroll.HARD, NpcCombatDropTable.CHANCE_1_IN_100).rolls(2);
    var dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_3000)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.IKKLE_HYDRA)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_2000)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.JAR_OF_CHEMICALS)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_1024);
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGONFRUIT_TREE_SEED, 2, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.REDWOOD_TREE_SEED, 2, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CELASTRUS_SEED, 2, 3)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_1000).log(true);
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_THROWNAXE, 500, 1000)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_THROWNAXE, 500, 1000)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_1000)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.HYDRAS_CLAW)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_256)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.HYDRA_TAIL)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.HYDRA_LEATHER)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_256)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ALCHEMICAL_HYDRA_HEADS)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(0.56)
        .order(NpcCombatDropTable.Order.RANDOM_UNIQUE).broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.HYDRAS_EYE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.HYDRAS_HEART)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.HYDRAS_FANG)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_RARE);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TORSTOL_SEED, 2, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RANARR_SEED, 2, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SNAPDRAGON_SEED, 2, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WILLOW_SEED, 2, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WATERMELON_SEED, 30, 45)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MAPLE_SEED, 2, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.YEW_SEED, 2, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TEAK_SEED, 2, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MAHOGANY_SEED, 2, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PAPAYA_TREE_SEED, 2, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PALM_TREE_SEED, 2, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MAGIC_SEED, 2, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SPIRIT_SEED, 2, 3)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_PLATELEGS)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_PLATESKIRT)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MYSTIC_ROBE_TOP_LIGHT)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MYSTIC_ROBE_BOTTOM_LIGHT)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ONYX_BOLTS_E, 35, 50)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_RANARR_WEED_NOTED, 10, 15)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_AVANTOE_NOTED, 10, 15)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_SNAPDRAGON_NOTED, 10, 15)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_TORSTOL_NOTED, 10, 15)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_KWUARM_NOTED, 25, 30)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_CADANTINE_NOTED, 25, 30)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_LANTADYME_NOTED, 25, 30)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_DWARF_WEED_NOTED, 25, 30)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CRYSTAL_KEY)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MYSTIC_FIRE_STAFF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MYSTIC_WATER_STAFF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BATTLESTAFF_NOTED, 8, 12)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLACK_DHIDE_BODY)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_LONGSWORD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_MED_HELM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_PLATEBODY)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_BATTLEAXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CHAOS_RUNE, 150, 300)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DEATH_RUNE, 150, 300)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLOOD_RUNE, 150, 300)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ASTRAL_RUNE, 150, 300)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGONSTONE_BOLTS_E, 100, 120)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 40000, 60000)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SHARK, 2, 4)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RANGING_POTION_3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SUPER_RESTORE_3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_BONES_NOTED, 30)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.HYDRA_BONES, 2)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.ALCHEMICAL_HYDRA_426);
    combat.spawn(NpcCombatSpawn.builder().respawnDelay(42).build());
    combat.hitpoints(
        NpcCombatHitpoints.builder().total(1100).bar(HitpointsBar.GREEN_RED_100).build());
    combat.stats(NpcCombatStats.builder().attackLevel(100).magicLevel(260).rangedLevel(260)
        .defenceLevel(100).bonus(CombatBonus.ATTACK_MAGIC, 45).bonus(CombatBonus.ATTACK_RANGED, 45)
        .bonus(CombatBonus.DEFENCE_STAB, 75).bonus(CombatBonus.DEFENCE_SLASH, 150)
        .bonus(CombatBonus.DEFENCE_CRUSH, 150).bonus(CombatBonus.DEFENCE_MAGIC, 150)
        .bonus(CombatBonus.DEFENCE_RANGED, 45).build());
    combat.slayer(NpcCombatSlayer.builder().level(95).build());
    combat.aggression(NpcCombatAggression.builder().range(12).build());
    combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).bind(true).build());
    combat.focus(NpcCombatFocus.builder().keepWithinDistance(3).build());
    combat.killCount(NpcCombatKillCount.builder().sendMessage(true).build());
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(17));
    style.animation(8235).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    style.attackCount(2);
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.maximum(17));
    style.animation(8236).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    style.attackCount(2);
    combat.style(style.build());


    var combat2 = NpcCombatDefinition.builder();
    combat2.id(NpcId.ALCHEMICAL_HYDRA_426_8616).id(NpcId.ALCHEMICAL_HYDRA_426_8617)
        .id(NpcId.ALCHEMICAL_HYDRA_426_8618);
    combat2.spawn(NpcCombatSpawn.builder().respawnDelay(42).build());
    combat2.stats(NpcCombatStats.builder().attackLevel(100).magicLevel(260).rangedLevel(260)
        .defenceLevel(100).build());
    combat2.slayer(NpcCombatSlayer.builder().level(95).build());


    var combat3 = NpcCombatDefinition.builder();
    combat3.id(NpcId.ALCHEMICAL_HYDRA_426_8619);
    combat3.spawn(NpcCombatSpawn.builder().respawnDelay(42).build());
    combat3.hitpoints(
        NpcCombatHitpoints.builder().total(1100).bar(HitpointsBar.GREEN_RED_100).build());
    combat3.stats(NpcCombatStats.builder().attackLevel(100).magicLevel(260).rangedLevel(260)
        .defenceLevel(100).bonus(CombatBonus.ATTACK_MAGIC, 45).bonus(CombatBonus.ATTACK_RANGED, 45)
        .bonus(CombatBonus.DEFENCE_STAB, 75).bonus(CombatBonus.DEFENCE_SLASH, 150)
        .bonus(CombatBonus.DEFENCE_CRUSH, 150).bonus(CombatBonus.DEFENCE_MAGIC, 150)
        .bonus(CombatBonus.DEFENCE_RANGED, 45).build());
    combat3.slayer(NpcCombatSlayer.builder().level(95).build());
    combat3.aggression(NpcCombatAggression.builder().range(12).build());
    combat3.immunity(NpcCombatImmunity.builder().poison(true).venom(true).bind(true).build());
    combat3.focus(NpcCombatFocus.builder().keepWithinDistance(3).build());
    combat3.killCount(NpcCombatKillCount.builder().sendMessage(true).build());
    combat3.drop(drop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(20));
    style.animation(8242).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    combat3.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.maximum(17));
    style.animation(8243).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    style.attackCount(2);
    combat3.style(style.build());


    var combat4 = NpcCombatDefinition.builder();
    combat4.id(NpcId.ALCHEMICAL_HYDRA_426_8620);
    combat4.spawn(NpcCombatSpawn.builder().respawnDelay(42).build());
    combat4.hitpoints(
        NpcCombatHitpoints.builder().total(1100).bar(HitpointsBar.GREEN_RED_100).build());
    combat4.stats(NpcCombatStats.builder().attackLevel(100).magicLevel(260).rangedLevel(260)
        .defenceLevel(100).bonus(CombatBonus.ATTACK_MAGIC, 45).bonus(CombatBonus.ATTACK_RANGED, 45)
        .bonus(CombatBonus.DEFENCE_STAB, 75).bonus(CombatBonus.DEFENCE_SLASH, 150)
        .bonus(CombatBonus.DEFENCE_CRUSH, 150).bonus(CombatBonus.DEFENCE_MAGIC, 150)
        .bonus(CombatBonus.DEFENCE_RANGED, 45).build());
    combat4.slayer(NpcCombatSlayer.builder().level(95).build());
    combat4.aggression(NpcCombatAggression.builder().range(12).build());
    combat4.immunity(NpcCombatImmunity.builder().poison(true).venom(true).bind(true).build());
    combat4.focus(NpcCombatFocus.builder().keepWithinDistance(3).build());
    combat4.killCount(NpcCombatKillCount.builder().sendMessage(true).build());
    combat4.drop(drop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(20));
    style.animation(8249).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    combat4.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.maximum(20));
    style.animation(8250).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    combat4.style(style.build());


    var combat5 = NpcCombatDefinition.builder();
    combat5.id(NpcId.ALCHEMICAL_HYDRA_426_8621);
    combat5.spawn(NpcCombatSpawn.builder().respawnDelay(42).build());
    combat5.hitpoints(
        NpcCombatHitpoints.builder().total(1100).bar(HitpointsBar.GREEN_RED_100).build());
    combat5.stats(NpcCombatStats.builder().attackLevel(100).magicLevel(260).rangedLevel(260)
        .defenceLevel(100).bonus(CombatBonus.ATTACK_MAGIC, 45).bonus(CombatBonus.ATTACK_RANGED, 45)
        .bonus(CombatBonus.DEFENCE_STAB, 75).bonus(CombatBonus.DEFENCE_SLASH, 150)
        .bonus(CombatBonus.DEFENCE_CRUSH, 150).bonus(CombatBonus.DEFENCE_MAGIC, 150)
        .bonus(CombatBonus.DEFENCE_RANGED, 45).build());
    combat5.slayer(NpcCombatSlayer.builder().level(95).build());
    combat5.aggression(NpcCombatAggression.builder().range(12).build());
    combat5.immunity(NpcCombatImmunity.builder().poison(true).venom(true).bind(true).build());
    combat5.focus(NpcCombatFocus.builder().keepWithinDistance(3).build());
    combat5.killCount(NpcCombatKillCount.builder().sendMessage(true).build());
    combat5.deathAnimation(8257);
    combat5.drop(drop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(26));
    style.animation(8255).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    combat5.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.maximum(26));
    style.animation(8256).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    combat5.style(style.build());


    return Arrays.asList(combat.build(), combat2.build(), combat3.build(), combat4.build(),
        combat5.build());
  }

  @Override
  public void spawnHook() {
    npc = getNpc();
  }

  @Override
  public void restoreHook() {
    npc.attackUnlock();
    hitStyle = PRandom.randomE(2) == 0 ? HitType.RANGED : HitType.MAGIC;
    hitCount = 0;
    specialDelay = 3;
    damageReduction = true;
    ventDelay = 8;
    cancelLightningAttack();
    setFireBleed(null);
  }

  @Override
  public void tickStartHook() {
    if (npc.isLocked()) {
      return;
    }
    if (npc.getId() == NpcId.ALCHEMICAL_HYDRA_426
        && PRandom.getPercent(npc.getHitpoints(), npc.getMaxHitpoints()) <= 75) {
      npc.setLock(2);
      npc.setTransformationId(NpcId.ALCHEMICAL_HYDRA_426_8616);
      npc.setAnimation(8237);
      return;
    } else if (npc.getId() == NpcId.ALCHEMICAL_HYDRA_426_8616) {
      npc.setLock(2);
      npc.setTransformationId(NpcId.ALCHEMICAL_HYDRA_426_8619);
      npc.setAnimation(8238);
      specialDelay = 3;
      damageReduction = true;
      return;
    } else if (npc.getId() == NpcId.ALCHEMICAL_HYDRA_426_8619
        && PRandom.getPercent(npc.getHitpoints(), npc.getMaxHitpoints()) <= 50) {
      npc.setLock(2);
      npc.setTransformationId(NpcId.ALCHEMICAL_HYDRA_426_8617);
      npc.setAnimation(8244);
      cancelLightningAttack();
      return;
    } else if (npc.getId() == NpcId.ALCHEMICAL_HYDRA_426_8617) {
      npc.setLock(2);
      npc.setTransformationId(NpcId.ALCHEMICAL_HYDRA_426_8620);
      npc.setAnimation(8245);
      specialDelay = 3;
      damageReduction = true;
      return;
    } else if (npc.getId() == NpcId.ALCHEMICAL_HYDRA_426_8620
        && PRandom.getPercent(npc.getHitpoints(), npc.getMaxHitpoints()) <= 25) {
      npc.setLock(2);
      npc.setTransformationId(NpcId.ALCHEMICAL_HYDRA_426_8618);
      npc.setAnimation(8251);
      cancelLightningAttack();
      return;
    } else if (npc.getId() == NpcId.ALCHEMICAL_HYDRA_426_8618) {
      npc.setLock(2);
      npc.setTransformationId(NpcId.ALCHEMICAL_HYDRA_426_8621);
      npc.setAnimation(8252);
      specialDelay = 3;
      hitStyle = hitStyle == HitType.RANGED ? HitType.MAGIC : HitType.RANGED;
      damageReduction = false;
      return;
    }
    if (ventDelay > 0) {
      ventDelay--;
      if (ventDelay == 0) {
        ventDelay = 8;
        activateVents();
      }
    }
    if (npc.getId() == NpcId.ALCHEMICAL_HYDRA_426_8619 && npc.getHitDelay() == 0
        && specialDelay == 0) {
      lightningAttackStart();
    } else if (npc.getId() == NpcId.ALCHEMICAL_HYDRA_426_8620 && npc.getHitDelay() == 0
        && specialDelay == 0) {
      fireAttackStart();
    }
  }

  @Override
  public HitType attackTickHitTypeHook(HitType hitType, Entity opponent) {
    return hitStyle;
  }

  @Override
  public NpcCombatStyle attackTickCombatStyleHook(NpcCombatStyle combatStyle, Entity opponent) {
    if (specialDelay == 0) {
      if (npc.getId() == NpcId.ALCHEMICAL_HYDRA_426
          || npc.getId() == NpcId.ALCHEMICAL_HYDRA_426_8621) {
        var poisonAttack =
            npc.getId() == NpcId.ALCHEMICAL_HYDRA_426 ? POISON_ATTACK_1 : POISON_ATTACK_2;
        currentCombatStyle = poisonAttack;
        return poisonAttack;
      }
    }
    currentCombatStyle = combatStyle;
    return combatStyle;
  }

  @Override
  public double damageReceivedHook(Entity opponent, double damage, HitType hitType,
      HitType defenceType) {
    if (damageReduction) {
      damage /= 2;
      if (opponent instanceof Player) {
        var player = (Player) opponent;
        player.getGameEncoder()
            .sendMessage("The Alchemical Hydra's defences partially absorb your attack!");
      }
    }
    return damage;
  }

  @Override
  public Graphic applyAttackTargetTileGraphicHook(NpcCombatStyle combatStyle, Entity opponent) {
    if (combatStyle.getTargetTileGraphic() != null
        && combatStyle.getTargetTileGraphic().getId() == 1654) {
      return POISON_TILE_GRAPHICS[PRandom.randomE(POISON_TILE_GRAPHICS.length)];
    }
    return combatStyle.getTargetTileGraphic();
  }

  @Override
  public void attackTickEndHook() {
    if (currentCombatStyle != POISON_ATTACK_1 && currentCombatStyle != POISON_ATTACK_2
        && (npc.getId() == NpcId.ALCHEMICAL_HYDRA_426_8621 || ++hitCount == 3)) {
      hitStyle = hitStyle == HitType.RANGED ? HitType.MAGIC : HitType.RANGED;
      hitCount = 0;
    }
    if (--specialDelay < 0) {
      specialDelay = 9;
    }
  }

  @Override
  public boolean canBeAttackedHook(Entity opponent, boolean sendMessage, HitType hitType) {
    if (!(opponent instanceof Player)) {
      return false;
    }
    var player = (Player) opponent;
    if (!Settings.getInstance().isSpawn() && !player.getSkills().isAnySlayerTask(npc)
        && !Main.ownerPrivledges(player)) {
      if (sendMessage) {
        player.getGameEncoder()
            .sendMessage("This can only be attacked on an appropriate Slayer task.");
      }
      return false;
    }
    if (npc.isAttacking() && (npc.getAttackingEntity() != null && npc.getAttackingEntity() != player
        || npc.getLastHitByEntity() != null && player != npc.getLastHitByEntity())) {
      if (sendMessage) {
        player.getGameEncoder().sendMessage("The Alchemical Hydra is busy attacking someone else.");
      }
      return false;
    }
    return true;
  }

  @Override
  public boolean dropTableCanDropHook(Player player, int dropRateDivider, int roll,
      NpcCombatDropTable table) {
    if (roll != 0 && table.getChance() < 1) {
      return false;
    }
    return true;
  }

  private void lightningAttackStart() {
    npc.setHitDelay(6);
    specialDelay = 9;
    npc.setAnimation(8241);
    var tiles = PCollection.toList(new Tile(1362, 10272), new Tile(1371, 10272),
        new Tile(1371, 10263), new Tile(1362, 10263));
    var initialTile = PRandom.listRandom(tiles).randomize(2);
    var projectile = Graphic.Projectile.builder().id(1664).startTile(npc).endTile(initialTile)
        .projectileSpeed(getProjectileSpeed(10)).build();
    sendMapProjectile(projectile);
    lightningCastEvent = new PEvent(projectile.getEventDelay() - 1) {
      private Tile tile;

      @Override
      public void execute() {
        if (tile != null) {
          lightningAttack(tile);
        } else {
          npc.getController().sendMapGraphic(initialTile, new Graphic(1664));
        }
        if (tiles.isEmpty()) {
          stop();
          return;
        }
        var lastTile = tile != null ? tile : initialTile;
        tile = tiles.remove(PRandom.randomE(tiles.size()));
        var projectile = Graphic.Projectile.builder().id(1665).startTile(lastTile).endTile(tile)
            .projectileSpeed(getProjectileSpeed(2)).build();
        sendMapProjectile(projectile);

        setTick(projectile.getEventDelay() - 1);
      }
    };
    npc.getWorld().addEvent(lightningCastEvent);
  }

  private void lightningAttack(Tile tile) {
    var entity = npc.getAttackingEntity();
    if (!(entity instanceof Player)) {
      return;
    }
    var player = (Player) entity;
    var event = new PEvent() {
      @Override
      public void execute() {
        if (player.isLocked()) {
          stop();
          return;
        }
        if (tile.matchesTile(player)) {
          player.getGameEncoder()
              .sendMessage("<col=ff0000>The eletricity temporarily paralyzes you!");
          player.applyHit(new Hit(PRandom.randomI(20)));
          player.getController().setMagicBind(8);
          stop();
        }
        if ((getExecutions() % 2) != 0) {
          return;
        }
        npc.getController().sendMapGraphic(tile, new Graphic(1666));
        if (tile.getX() < player.getX()) {
          tile.moveX(1);
        } else if (tile.getX() > player.getX()) {
          tile.moveX(-1);
        }
        if (tile.getY() < player.getY()) {
          tile.moveY(1);
        } else if (tile.getY() > player.getY()) {
          tile.moveY(-1);
        }
        if (getExecutions() == 16) {
          stop();
        }
      }
    };
    npc.getWorld().addEvent(event);
    lightningEvents.add(event);
  }

  private void cancelLightningAttack() {
    if (lightningCastEvent != null) {
      lightningCastEvent.stop();
      lightningCastEvent = null;
    }
    if (!lightningEvents.isEmpty()) {
      for (var event : lightningEvents) {
        event.stop();
      }
      lightningEvents.clear();
    }
  }

  private void fireAttackStart() {
    var entity = npc.getAttackingEntity();
    if (!(entity instanceof Player)) {
      return;
    }
    var player = (Player) entity;
    npc.attackLock();
    var event = new PEvent() {
      @Override
      public void execute() {
        if (player.isLocked()) {
          stop();
          npc.attackUnlock();
          return;
        }
        npc.getMovement().quickRoute(1364, 10265);
        if (npc.getX() == 1364 && npc.getY() == 10265) {
          fireAttack(player);
          stop();
        }
      }
    };
    npc.getWorld().addEvent(event);
  }

  private void fireAttack(Player player) {
    player.getController().setMagicBind(2);
    var fire = NORTH_FIRE;
    for (var aFire : FIRE_ATTACKS) {
      if (!aFire.polygon.contains(player.getX(), player.getY())) {
        continue;
      }
      fire = aFire;
      break;
    }
    var firstTiles = Tile.newTiles(fire.tiles1);
    var secondTiles = Tile.newTiles(fire.tiles2);
    var followTile = new Tile(fire.followTile);
    var followTiles = new ArrayList<FollowTile>();
    followTiles.add(new FollowTile(new Tile(followTile), 0));
    var speed = getProjectileSpeed(2);
    var eventFollow = new PEvent(1) {
      private boolean hasCastFollow;

      @Override
      public void execute() {
        if (player.isLocked() || followTiles.isEmpty() || npc.isLocked()
            || npc.getId() != NpcId.ALCHEMICAL_HYDRA_426_8620) {
          stop();
          npc.attackUnlock();
          return;
        }
        if (!hasCastFollow) {
          hasCastFollow = true;
          npc.setAnimation(8248);
          npc.setFaceTile(followTile);
          var projectile = Graphic.Projectile.builder().id(1667).startTile(npc).endTile(followTile)
              .projectileSpeed(getProjectileSpeed(2)).endHeight(0).build();
          sendMapProjectile(projectile);
          npc.getController().sendMapGraphic(followTile,
              new Graphic(1668, 0, projectile.getContactDelay()));
          npc.attackUnlock();
          npc.setHitDelay(15);
          specialDelay = 9;
          return;
        }
        for (var it = followTiles.iterator(); it.hasNext();) {
          var aFollowTile = it.next();
          if (getExecutions() - aFollowTile.getTick() >= 42) {
            it.remove();
            continue;
          }
          if (!aFollowTile.getTile().matchesTile(player)) {
            continue;
          }
          player.applyHit(new Hit(PRandom.randomI(20)));
          setFireBleed(player);
        }
        if (getExecutions() > 15) {
          return;
        }
        if (followTile.matchesTile(player)) {
          return;
        }
        if (followTile.getX() < player.getX()) {
          followTile.moveX(1);
        } else if (followTile.getX() > player.getX()) {
          followTile.moveX(-1);
        }
        if (followTile.getY() < player.getY()) {
          followTile.moveY(1);
        } else if (followTile.getY() > player.getY()) {
          followTile.moveY(-1);
        }
        var hasTile = false;
        for (var aFollowTile : followTiles) {
          if (!followTile.matchesTile(aFollowTile.tile)) {
            continue;
          }
          hasTile = true;
          break;
        }
        if (hasTile) {
          return;
        }
        followTiles.add(new FollowTile(new Tile(followTile), getExecutions()));
        npc.getController().sendMapGraphic(followTile, new Graphic(1668));
      }
    };
    var spawnedFirstTiles = new ArrayList<Tile>();
    var spawnedSecondTiles = new ArrayList<Tile>();
    npc.setAnimation(8248);
    npc.setFaceTile(firstTiles[1]);
    var isFirst = true;
    for (var tile : firstTiles) {
      if (isFirst) {
        isFirst = false;
        continue;
      }
      var projectile = Graphic.Projectile.builder().id(1667).startTile(npc).endTile(tile)
          .projectileSpeed(getProjectileSpeed(2)).endHeight(0).build();
      sendMapProjectile(projectile);
    }
    for (var i = 0; i < 16; i++) {
      var noTilesFound = true;
      isFirst = true;
      for (var tile : firstTiles) {
        if (isFirst) {
          isFirst = false;
          continue;
        }
        if (npc.getController().getMapClip(tile) == 0) {
          npc.getController().sendMapGraphic(tile,
              new Graphic(1668, 0, speed.getContactDelay() + i * 5));
          spawnedFirstTiles.add(new Tile(tile));
          noTilesFound = false;
        }
        tile.moveTile(firstTiles[0].getX(), firstTiles[0].getY());
      }
      if (noTilesFound) {
        break;
      }
    }
    var eventWall1 = new PEvent() {
      @Override
      public void execute() {
        if (player.isLocked()) {
          stop();
          npc.attackUnlock();
          return;
        }
        for (var tile : spawnedSecondTiles) {
          if (!tile.matchesTile(player)) {
            continue;
          }
          player.applyHit(new Hit(PRandom.randomI(20)));
          setFireBleed(player);
        }
        if (getExecutions() >= 42 + speed.getEventDelay()) {
          stop();
        }
      }
    };
    npc.getWorld().addEvent(eventWall1);
    var wallEvent2 = new PEvent(1) {
      private boolean hasSecondAttacked;

      @Override
      public void execute() {
        setTick(0);
        if (player.isLocked()) {
          stop();
          npc.attackUnlock();
          return;
        }
        if (!hasSecondAttacked) {
          hasSecondAttacked = true;
          npc.setAnimation(8248);
          npc.setFaceTile(secondTiles[1]);
          var isFirst = true;
          for (var tile : secondTiles) {
            if (isFirst) {
              isFirst = false;
              continue;
            }
            npc.getCombat().sendMapProjectile(null, npc, tile, 1667, 43, 0, speed.getClientDelay(),
                speed.getClientSpeed(), 16, 64);
          }
          for (var i = 0; i < 16; i++) {
            var noTilesFound = true;
            isFirst = true;
            for (var tile : secondTiles) {
              if (isFirst) {
                isFirst = false;
                continue;
              }
              if (npc.getController().getMapClip(tile) == 0) {
                npc.getController().sendMapGraphic(tile,
                    new Graphic(1668, 0, speed.getContactDelay() + i * 5));
                spawnedSecondTiles.add(new Tile(tile));
                noTilesFound = false;
              }
              tile.moveTile(secondTiles[0].getX(), secondTiles[0].getY());
            }
            if (noTilesFound) {
              break;
            }
          }
          npc.getWorld().addEvent(eventFollow);
          return;
        }
        for (var tile : spawnedSecondTiles) {
          if (!tile.matchesTile(player)) {
            continue;
          }
          player.applyHit(new Hit(PRandom.randomI(20)));
          setFireBleed(player);
        }
        if (getExecutions() >= 43 + speed.getEventDelay()) {
          stop();
        }
      }
    };
    npc.getWorld().addEvent(wallEvent2);
  }

  private void setFireBleed(Player player) {
    if (fireBleedEvent != null) {
      fireBleedEvent.stop();
    }
    if (player == null) {
      return;
    }
    fireBleedEvent = new PEvent() {
      @Override
      public void execute() {
        if (getExecutions() == 4) {
          stop();
        }
        player.applyHit(new Hit(5));
      }
    };
    npc.getWorld().addEvent(fireBleedEvent);
  }

  private void activateVents() {
    var entity = npc.getAttackingEntity();
    if (!(entity instanceof Player)) {
      return;
    }
    var player = (Player) entity;
    player.getGameEncoder().sendMapObjectAnimation(BLUE_VENT, 8279);
    player.getGameEncoder().sendMapObjectAnimation(GREEN_VENT, 8279);
    player.getGameEncoder().sendMapObjectAnimation(RED_VENT, 8279);
    var weakness = RED_VENT;
    if (npc.getId() == NpcId.ALCHEMICAL_HYDRA_426_8619) {
      weakness = GREEN_VENT;
    } else if (npc.getId() == NpcId.ALCHEMICAL_HYDRA_426_8620) {
      weakness = BLUE_VENT;
    }
    var decidedWeakness = weakness;
    var event = new PEvent() {
      @Override
      public void execute() {
        if (player.isLocked()) {
          stop();
          return;
        }
        if (getExecutions() == 4) {
          stop();
          player.getGameEncoder().sendMapObjectAnimation(BLUE_VENT, 8280);
          player.getGameEncoder().sendMapObjectAnimation(GREEN_VENT, 8280);
          player.getGameEncoder().sendMapObjectAnimation(RED_VENT, 8280);
          return;
        }
        for (var vent : VENTS) {
          if (!player.withinDistance(vent, 1)) {
            continue;
          }
          player.applyHit(new Hit(PRandom.randomI(20)));
          break;
        }
        if (npc.getId() == NpcId.ALCHEMICAL_HYDRA_426_8621) {
          return;
        }
        for (var vent : VENTS) {
          if (!npc.withinDistance(vent, 1)) {
            continue;
          }
          if (vent == decidedWeakness) {
            if (damageReduction) {
              damageReduction = false;
              player.getGameEncoder()
                  .sendMessage("The chemicals neutralise the Alchemical Hydra's defences!");
            }
          } else if (!damageReduction) {
            damageReduction = true;
            player.getGameEncoder().sendMessage(
                "The chemicals are absorbed by the Alchemical Hydra, empowering it further!");
          }
          break;
        }
      }
    };
    npc.getWorld().addEvent(event);
  }

  @AllArgsConstructor
  @Getter
  private static class FollowTile {
    private Tile tile;
    private int tick;
  }

  @AllArgsConstructor
  @Getter
  private static class FireAttack {
    private PPolygon polygon;
    private Tile[] tiles1;
    private Tile[] tiles2;
    private Tile followTile;
  }
}
