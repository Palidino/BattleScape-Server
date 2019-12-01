package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.google.inject.Inject;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Entity;
import com.palidinodh.osrscore.model.Tile;
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
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSlayer;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.player.Player;
import lombok.var;

class ArmadylGodWarsCombat extends NpcCombat {
  @Inject
  private Npc npc;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var spiritualMageDrop =
        NpcCombatDrop.builder().rareDropTableRate(NpcCombatDropTable.CHANCE_1_IN_256)
            .clue(NpcCombatDrop.ClueScroll.HARD, NpcCombatDropTable.CHANCE_1_IN_128);
    var dropTable =
        NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_128).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_BOOTS)));
    spiritualMageDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DEATH_RUNE, 15)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ASTRAL_RUNE, 15)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LAW_RUNE, 15)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MIST_RUNE, 20)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DUST_RUNE, 15)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MUD_RUNE, 20)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MIND_RUNE, 50)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLOOD_RUNE, 10)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ANTIPOISON_3_NOTED, 5)));
    spiritualMageDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PURE_ESSENCE_NOTED, 90)));
    spiritualMageDrop.table(dropTable.build());


    var spirituaMageCombat = NpcCombatDefinition.builder();
    spirituaMageCombat.id(NpcId.SPIRITUAL_MAGE_122);
    spirituaMageCombat.hitpoints(NpcCombatHitpoints.total(86));
    spirituaMageCombat.stats(NpcCombatStats.builder().magicLevel(150).defenceLevel(111)
        .bonus(CombatBonus.DEFENCE_STAB, 9).bonus(CombatBonus.DEFENCE_SLASH, 12)
        .bonus(CombatBonus.DEFENCE_CRUSH, 5).bonus(CombatBonus.DEFENCE_MAGIC, 45)
        .bonus(CombatBonus.DEFENCE_RANGED, 28).build());
    spirituaMageCombat.slayer(NpcCombatSlayer.builder().level(83).build());
    spirituaMageCombat.aggression(NpcCombatAggression.PLAYERS);
    spirituaMageCombat.deathAnimation(6959).blockAnimation(6958);
    spirituaMageCombat.drop(spiritualMageDrop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(16).splashOnMiss(true).build());
    style.animation(6955).attackSpeed(3);
    style.projectile(NpcCombatProjectile.id(335));
    spirituaMageCombat.style(style.build());


    var aviansieDrop = NpcCombatDrop.builder();
    aviansieDrop.clue(NpcCombatDrop.ClueScroll.HARD, NpcCombatDropTable.CHANCE_1_IN_128);
    dropTable = NpcCombatDropTable.builder().chance(2.8);
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ENSOULED_AVIANSIE_HEAD_13505)));
    aviansieDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_RARE);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_CADANTINE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_LANTADYME)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_DWARF_WEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNITE_LIMBS)));
    aviansieDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LAW_RUNE, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.NATURE_RUNE, 9)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MIND_RUNE, 5)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLOOD_RUNE, 11)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CHAOS_RUNE, 3, 16)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BODY_RUNE, 11)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_HARRALANDER)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_RANARR_WEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_IRIT_LEAF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_AVANTOE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_KWUARM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_DAGGER_P_PLUS)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SWORDFISH, 5)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ANTIPOISON_3_NOTED, 5)));
    aviansieDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.AIR_RUNE, 15, 30)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WATER_RUNE, 30)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_GUAM_LEAF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_MARRENTILL)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_TARROMIN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SILVER_ORE)));
    aviansieDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BONES)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.FEATHER, 1, 7)));
    aviansieDrop.table(dropTable.build());


    var aviansie69Combat = NpcCombatDefinition.builder();
    aviansie69Combat.id(NpcId.AVIANSIE_69);
    aviansie69Combat.hitpoints(NpcCombatHitpoints.total(70));
    aviansie69Combat.stats(NpcCombatStats.builder().rangedLevel(71).defenceLevel(70).build());
    aviansie69Combat.aggression(NpcCombatAggression.PLAYERS);
    aviansie69Combat.immunity(NpcCombatImmunity.builder().melee(true).build());
    aviansie69Combat.deathAnimation(6959).blockAnimation(6958);
    aviansie69Combat.drop(aviansieDrop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(8));
    style.animation(6956).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    aviansie69Combat.style(style.build());


    var aviansie79Combat = NpcCombatDefinition.builder();
    aviansie79Combat.id(NpcId.AVIANSIE_79);
    aviansie79Combat.hitpoints(NpcCombatHitpoints.total(83));
    aviansie79Combat.stats(NpcCombatStats.builder().rangedLevel(85).defenceLevel(70).build());
    aviansie79Combat.aggression(NpcCombatAggression.PLAYERS);
    aviansie69Combat.immunity(NpcCombatImmunity.builder().melee(true).build());
    aviansie79Combat.deathAnimation(6959).blockAnimation(6958);
    aviansie79Combat.drop(aviansieDrop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(9));
    style.animation(6956).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    aviansie79Combat.style(style.build());


    var aviansie92Combat = NpcCombatDefinition.builder();
    aviansie92Combat.id(NpcId.AVIANSIE_92);
    aviansie92Combat.hitpoints(NpcCombatHitpoints.total(95));
    aviansie92Combat.stats(NpcCombatStats.builder().rangedLevel(90).defenceLevel(100).build());
    aviansie92Combat.aggression(NpcCombatAggression.PLAYERS);
    aviansie69Combat.immunity(NpcCombatImmunity.builder().melee(true).build());
    aviansie92Combat.deathAnimation(6959).blockAnimation(6958);
    aviansie92Combat.drop(aviansieDrop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(10));
    style.animation(6956).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    aviansie92Combat.style(style.build());


    var aviansie137Combat = NpcCombatDefinition.builder();
    aviansie137Combat.id(NpcId.AVIANSIE_137);
    aviansie137Combat.hitpoints(NpcCombatHitpoints.total(124));
    aviansie137Combat.stats(NpcCombatStats.builder().rangedLevel(136).defenceLevel(160).build());
    aviansie137Combat.aggression(NpcCombatAggression.PLAYERS);
    aviansie69Combat.immunity(NpcCombatImmunity.builder().melee(true).build());
    aviansie137Combat.deathAnimation(6959).blockAnimation(6958);
    aviansie137Combat.drop(aviansieDrop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(15));
    style.animation(6956).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    aviansie137Combat.style(style.build());


    return Arrays.asList(spirituaMageCombat.build(), aviansie69Combat.build(),
        aviansie79Combat.build(), aviansie92Combat.build(), aviansie137Combat.build());
  }

  @Override
  public Item dropTableDropGetItemHook(Player player, Tile tile, int dropRateDivider, int roll,
      NpcCombatDropTable table, NpcCombatDropTableDrop drop, Item item) {
    if (npc.getController().inWilderness() && item.getId() == ItemId.ADAMANTITE_BAR) {
      item = new Item(item.getNotedId(), item);
    }
    return item;
  }

  public void deathDropItemsHook(Player player, int additionalPlayerLoopCount, Tile dropTile) {
    player.getArea().script("increase_armadyl_killcount");
  }

  @Override
  public boolean canBeAggressiveHook(Entity opponent) {
    if (!(opponent instanceof Player)) {
      return true;
    }
    var player = (Player) opponent;
    if (player.getEquipment().hasItemIC("Armadyl")
        || player.getEquipment().getShieldId() == ItemId.BOOK_OF_LAW
        || player.getEquipment().getAmmoId() == ItemId.HONOURABLE_BLESSING) {
      return false;
    }
    return true;
  }
}
