package com.palidinodh.osrsscript.npc.combatv0;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.HitpointsBar;
import com.palidinodh.osrscore.model.item.RandomItem;
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
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import lombok.var;

public class KingKurask295Combat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop =
        NpcCombatDrop.builder().rareDropTableRate(NpcCombatDropTable.CHANCE_1_IN_256).rolls(3);
    var dropTable = NpcCombatDropTable.builder().chance(0.03).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.KURASK_HEAD)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(0.29).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LEAF_BLADED_BATTLEAXE)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(0.58).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MYSTIC_ROBE_TOP_LIGHT)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_128);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CLUE_SCROLL_HARD)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(0.79).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LEAF_BLADED_SWORD)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(2.6).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.IMBUED_HEART)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ETERNAL_GEM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DUST_BATTLESTAFF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MIST_BATTLESTAFF)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.POISON_IVY_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CACTUS_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.KWUARM_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CADANTINE_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LANTADYME_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DWARF_WEED_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SNAPDRAGON_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TORSTOL_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_LONGSWORD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_PLATEBODY)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_AXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_IRIT_LEAF, 1, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_AVANTOE, 1, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_KWUARM, 1, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_CADANTINE, 1, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_DWARF_WEED, 1, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_RANARR_WEED, 1, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_LANTADYME, 1, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PAPAYA_FRUIT_NOTED, 10)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WHITE_BERRIES_NOTED, 12)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COCONUT_NOTED, 10)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_GUAM_LEAF, 1, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_MARRENTILL, 1, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_TARROMIN, 1, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_HARRALANDER, 1, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 44, 5000)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LIMPWURT_ROOT_NOTED, 10, 20)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.FLAX_NOTED, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BIG_BONES_NOTED, 20)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MITHRIL_KITESHIELD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.NATURE_RUNE, 5, 30)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BELLADONNA_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TOADFLAX_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.IRIT_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.AVANTOE_SEED)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BONES)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.KING_KURASK_295);
    combat
        .hitpoints(NpcCombatHitpoints.builder().total(420).bar(HitpointsBar.GREEN_RED_60).build());
    combat.stats(NpcCombatStats.builder().attackLevel(190).defenceLevel(250)
        .bonus(CombatBonus.DEFENCE_SLASH, 50).bonus(CombatBonus.DEFENCE_CRUSH, 50).build());
    combat.slayer(NpcCombatSlayer.builder().level(70).experience(2767).build());
    combat.aggression(NpcCombatAggression.PLAYERS);
    combat.immunity(NpcCombatImmunity.builder().poison(true).build());
    combat.killCount(NpcCombatKillCount.builder().asName("Superior Slayer Creature").build());
    combat.combatScript("SuperiorSlayerCS").deathAnimation(4233).blockAnimation(4232);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(33));
    style.animation(4234).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
