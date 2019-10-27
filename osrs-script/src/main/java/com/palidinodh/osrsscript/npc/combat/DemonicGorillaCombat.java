package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.ItemId;
import com.palidinodh.osrscore.io.cache.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Entity;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.Hit;
import com.palidinodh.osrscore.model.HitType;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.TileHitEvent;
import com.palidinodh.osrscore.model.item.Item;
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
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatType;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.random.PRandom;
import com.palidinodh.rs.setting.Settings;
import lombok.var;

public class DemonicGorillaCombat extends NpcCombat {
  private Npc npc;
  private HitType attackStyle;
  private int lastStyleChange;
  private int misses;
  private int damageTaken;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder().rareDropTableRate(NpcCombatDropTable.CHANCE_1_IN_256)
        .clue(NpcCombatDrop.ClueScroll.ELITE, NpcCombatDropTable.CHANCE_1_IN_500)
        .clue(NpcCombatDrop.ClueScroll.HARD, NpcCombatDropTable.CHANCE_1_IN_100);
    var dropTable =
        NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_1500).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.HEAVY_BALLISTA)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_750).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LIGHT_BALLISTA)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_300).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ZENYTE_SHARD)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_RARE);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PAPAYA_TREE_SEED, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PALM_TREE_SEED, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WILLOW_SEED, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MAPLE_SEED, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.YEW_SEED, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MAGIC_SEED, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SPIRIT_SEED, 2)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNITE_BOLTS, 102, 150)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_SCIMITAR)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SHARK, 2, 3)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_DWARF_WEED_NOTED, 7, 12)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WATERMELON_SEED, 30)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RANARR_SEED, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SNAPDRAGON_SEED, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TORSTOL_SEED, 1, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DIAMOND_NOTED, 4, 6)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANTITE_BAR_NOTED, 6)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNITE_BAR_NOTED, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_JAVELIN_HEADS, 31, 55)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_JAVELIN_HEADS, 5, 43)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_PLATELEGS)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_PLATESKIRT)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_CHAINBODY)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LAW_RUNE, 50, 75)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DEATH_RUNE, 50, 75)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PRAYER_POTION_3, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SARADOMIN_BREW_2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_KWUARM_NOTED, 7, 12)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_CADANTINE_NOTED, 7, 12)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_LANTADYME_NOTED, 7, 12)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 5366, 9991)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.JAVELIN_SHAFT, 266, 1238)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ASHES)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.DEMONIC_GORILLA_275).id(NpcId.DEMONIC_GORILLA_275_7145)
        .id(NpcId.DEMONIC_GORILLA_275_7146);
    combat.hitpoints(NpcCombatHitpoints.total(380));
    combat.stats(NpcCombatStats.builder().attackLevel(205).magicLevel(195).rangedLevel(195)
        .defenceLevel(200).bonus(CombatBonus.MELEE_ATTACK, 43).bonus(CombatBonus.ATTACK_MAGIC, 40)
        .bonus(CombatBonus.ATTACK_RANGED, 43).bonus(CombatBonus.MELEE_DEFENCE, 50)
        .bonus(CombatBonus.DEFENCE_MAGIC, 50).bonus(CombatBonus.DEFENCE_RANGED, 50).build());
    combat.aggression(NpcCombatAggression.PLAYERS);
    combat.immunity(NpcCombatImmunity.builder().venom(true).build());
    combat.type(NpcCombatType.DEMON).deathAnimation(7229).blockAnimation(7224);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(30));
    style.animation(7226).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(30));
    style.animation(7227).attackSpeed(5);
    style.targetGraphic(new Graphic(1303));
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.maximum(30));
    style.animation(7225).attackSpeed(5);
    style.targetGraphic(new Graphic(1305));
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
    lastStyleChange = 0;
    misses = 0;
    damageTaken = 0;
    chooseProtectionPrayer(HitType.getRandomType(HitType.MELEE, HitType.RANGED, HitType.MAGIC));
    attackStyle = HitType.getRandomType(HitType.MELEE, HitType.RANGED, HitType.MAGIC);
  }

  @Override
  public void tickStartHook() {
    if (!npc.isAttacking() && lastStyleChange++ >= 50) {
      chooseAttackStyle();
    }
    if (npc.getHitDelay() == 0 && npc.isAttacking() && attackStyle != HitType.MELEE
        && PRandom.randomE(5) == 0 && npc.getCombat().canAttackEntity(npc.getEngagingEntity(), null)
        && npc.withinDistance(npc.getEngagingEntity(), 10)) {
      npc.getWorld().addEvent(
          new TileHitEvent(4, npc.getController(), npc.getEngagingEntity(), 30, HitType.TYPELESS));
      npc.getWorld().sendMapGraphic(npc.getController(), npc.getEngagingEntity(), 71, 0, 126);
      var tile = new Tile(npc.getEngagingEntity());
      tile.setY(tile.getY() - 1);
      npc.getController().sendMapProjectile(null, tile, npc.getEngagingEntity(), 856, 200, 1, 0,
          126, 16, 64);
      npc.setAnimation(7228);
      npc.setHitDelay(5);
    }
  }

  @Override
  public void npcApplyHitEndHook(Hit hit) {
    if (!npc.isDead() && hit.getDamage() > 0) {
      damageTaken += hit.getDamage();
      if (damageTaken >= 50) {
        chooseProtectionPrayer(hit.getAttackType());
      }
    }
  }

  @Override
  public HitType attackTickHitTypeHook(HitType hitType, Entity opponent) {
    return attackStyle;
  }

  @Override
  public double damageInflictedHook(NpcCombatStyle combatStyle, Entity opponent, double damage) {
    if (damage <= 0 && ++misses >= 3) {
      misses = 0;
      chooseAttackStyle();
    }
    return damage;
  }

  @Override
  public double damageReceivedHook(Entity opponent, double damage, HitType hitType,
      HitType defenceType) {
    if (npc.getId() == NpcId.DEMONIC_GORILLA_275 && hitType == HitType.MELEE) {
      damage = 0;
    } else if (npc.getId() == NpcId.DEMONIC_GORILLA_275_7145 && hitType == HitType.RANGED) {
      damage = 0;
    } else if (npc.getId() == NpcId.DEMONIC_GORILLA_275_7146 && hitType == HitType.MAGIC) {
      damage = 0;
    }
    return damage;
  }

  @Override
  public void attackTickEndHook() {
    lastStyleChange = 0;
  }

  @Override
  public void deathDropItemsHook(Player player, int additionalPlayerLoopCount, Tile dropTile) {
    if (Settings.getInstance().isSpawn()
        && PRandom.inRange(player.getCombat().getDropRate(ItemId.DRAGON_CLAWS, 0.04))) {
      npc.getController().addMapItem(new Item(ItemId.DRAGON_CLAWS), dropTile, player);
    }
  }

  private void chooseProtectionPrayer(HitType hitType) {
    damageTaken = 0;
    if (hitType == HitType.MELEE) {
      npc.setTransformationId(NpcId.DEMONIC_GORILLA_275);
    } else if (hitType == HitType.RANGED) {
      npc.setTransformationId(NpcId.DEMONIC_GORILLA_275_7145);
    } else if (hitType == HitType.MAGIC) {
      npc.setTransformationId(NpcId.DEMONIC_GORILLA_275_7146);
    }
  }

  private void chooseAttackStyle() {
    lastStyleChange = 0;
    if (attackStyle == HitType.MELEE) {
      attackStyle = HitType.getRandomType(HitType.RANGED, HitType.MAGIC);
    } else if (attackStyle == HitType.RANGED) {
      attackStyle = HitType.getRandomType(HitType.MELEE, HitType.MAGIC);
    } else if (attackStyle == HitType.MAGIC) {
      attackStyle = HitType.getRandomType(HitType.MELEE, HitType.RANGED);
    }
  }
}
