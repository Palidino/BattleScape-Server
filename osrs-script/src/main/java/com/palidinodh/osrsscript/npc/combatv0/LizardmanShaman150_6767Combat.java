package com.palidinodh.osrsscript.npc.combatv0;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.item.RandomItem;
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
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import lombok.var;

public class LizardmanShaman150_6767Combat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder().rareDropTableRate(NpcCombatDropTable.CHANCE_1_IN_256);
    var dropTable = NpcCombatDropTable.builder().chance(0.02).broadcast(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_WARHAMMER)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(0.083);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CLUE_SCROLL_ELITE)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(0.4);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.XERICS_TALISMAN_INERT)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(0.5);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CLUE_SCROLL_HARD)));
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
    combat.id(NpcId.LIZARDMAN_SHAMAN_150_6767);
    combat.hitpoints(NpcCombatHitpoints.total(150));
    combat.stats(NpcCombatStats.builder().attackLevel(120).magicLevel(130).rangedLevel(120)
        .defenceLevel(140).bonus(CombatBonus.MELEE_ATTACK, 45).bonus(CombatBonus.ATTACK_RANGED, 45)
        .bonus(CombatBonus.DEFENCE_SLASH, 40).bonus(CombatBonus.DEFENCE_CRUSH, 30)
        .bonus(CombatBonus.DEFENCE_MAGIC, 50).build());
    combat.aggression(NpcCombatAggression.PLAYERS);
    combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
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


    return Arrays.asList(combat.build());
  }
}
