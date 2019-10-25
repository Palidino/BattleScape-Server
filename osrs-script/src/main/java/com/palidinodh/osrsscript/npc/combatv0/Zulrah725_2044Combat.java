package com.palidinodh.osrsscript.npc.combatv0;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.ItemId;
import com.palidinodh.osrscore.io.cache.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.HitType;
import com.palidinodh.osrscore.model.HitpointsBar;
import com.palidinodh.osrscore.model.item.RandomItem;
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
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSpawn;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatEffect;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import lombok.var;

public class Zulrah725_2044Combat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder().underKiller(true)
        .rareDropTableRate(NpcCombatDropTable.CHANCE_1_IN_32).rolls(2);
    var dropTable = NpcCombatDropTable.builder().chance(0.025).broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PET_SNAKELING)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(0.034).broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.JAR_OF_SWAMP)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(0.04).broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TANZANITE_MUTAGEN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MAGMA_MUTAGEN)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(0.58).broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TANZANITE_FANG)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MAGIC_FANG)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SERPENTINE_VISAGE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNCUT_ONYX)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(0.67);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CLUE_SCROLL_ELITE)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(15.0);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_MED_HELM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_HALBERD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TOADFLAX_NOTED, 25)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SNAPDRAGON_NOTED, 10)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DWARF_WEED_NOTED, 30)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TORSTOL_NOTED, 10)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TOADFLAX_SEED, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SNAPDRAGON_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DWARF_WEED_SEED, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TORSTOL_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SPIRIT_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CRYSTAL_SEED)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(60.0);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DEATH_RUNE, 300)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CHAOS_RUNE, 500)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PURE_ESSENCE_NOTED, 1500)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COAL_NOTED, 200)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNITE_ORE_NOTED, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MAGIC_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CALQUAT_TREE_SEED, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PAPAYA_TREE_SEED, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PALM_TREE_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ZULRAHS_SCALES, 500)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BATTLESTAFF_NOTED, 10)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ANTIDOTE_PLUS_PLUS_4_NOTED, 10)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MANTA_RAY_NOTED, 35)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MAHOGANY_LOGS_NOTED, 50)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SWAMP_TAR, 1000)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BIRD_NEST_NOTED, 10)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANTITE_BAR_NOTED, 20)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LAW_RUNE, 200)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BOW_STRING_NOTED, 1000)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CROSSBOW_STRING_NOTED, 1000)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SNAKESKIN_NOTED, 35)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGONSTONE_BOLT_TIPS, 12)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.YEW_LOGS_NOTED, 35)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ZUL_ANDRA_TELEPORT)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_BONES_NOTED, 12)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COCONUT_NOTED, 20)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRAPES_NOTED, 250)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ZULRAHS_SCALES, 100, 300)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ZUL_ANDRA_TELEPORT)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.ZULRAH_725_2044);
    combat.spawn(NpcCombatSpawn.builder().lock(6).animation(5071).build());
    combat
        .hitpoints(NpcCombatHitpoints.builder().total(500).bar(HitpointsBar.GREEN_RED_100).build());
    combat.stats(NpcCombatStats.builder().magicLevel(300).rangedLevel(300).defenceLevel(300)
        .bonus(CombatBonus.ATTACK_MAGIC, 50).bonus(CombatBonus.ATTACK_RANGED, 50)
        .bonus(CombatBonus.DEFENCE_MAGIC, 300).build());
    combat.aggression(NpcCombatAggression.builder().range(20).build());
    combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    combat.focus(NpcCombatFocus.builder().bypassMapObjects(true).build());
    combat.killCount(NpcCombatKillCount.builder().sendMessage(true).build());
    combat.combatScript("ZulrahCS").deathAnimation(5804).blockAnimation(5808);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(41));
    style.animation(5069).attackSpeed(3).attackRange(20);
    style.projectile(NpcCombatProjectile.builder().id(1044).speedType(HitType.MAGIC).build());
    style.effect(NpcCombatEffect.builder().venom(6).build());
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.maximum(41));
    style.animation(5069).attackSpeed(3).attackRange(20);
    style.projectile(NpcCombatProjectile.id(335));
    style.effect(NpcCombatEffect.builder().venom(6).build());
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
