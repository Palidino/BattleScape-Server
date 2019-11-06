package com.palidinodh.osrsscript.npc.combatv0;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.HitType;
import com.palidinodh.osrscore.model.HitpointsBar;
import com.palidinodh.osrscore.model.item.RandomItem;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTable;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTableDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatKillCount;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSlayer;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import lombok.var;

public class InsatiableMutatedBloodveld278Combat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop =
        NpcCombatDrop.builder().rareDropTableRate(NpcCombatDropTable.CHANCE_1_IN_256).rolls(3);
    var dropTable = NpcCombatDropTable.builder().chance(0.5);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ANCIENT_SHARD)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(1.8).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.IMBUED_HEART)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ETERNAL_GEM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DUST_BATTLESTAFF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MIST_BATTLESTAFF)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_RARE);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLACK_BOOTS)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_MED_HELM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_DAGGER)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_BATTLEAXE)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(8.0);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CLUE_SCROLL_HARD)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MITHRIL_BATTLEAXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MITHRIL_PLATEBODY)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_KNIFE, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_CHAINBODY)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MITHRIL_FULL_HELM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_LONGSWORD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_SCIMITAR)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SOUL_RUNE, 4)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GOLD_ORE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MEAT_PIE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MITHRIL_BAR)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUBY_AMULET)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLACK_MED_HELM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MITHRIL_AXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.AIR_RUNE, 105)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.FIRE_RUNE, 75)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BOW_STRING)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BONES)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ENSOULED_BLOODVELD_HEAD_13496)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.INSATIABLE_MUTATED_BLOODVELD_278);
    combat
        .hitpoints(NpcCombatHitpoints.builder().total(410).bar(HitpointsBar.GREEN_RED_60).build());
    combat.stats(NpcCombatStats.builder().attackLevel(250).defenceLevel(130).build());
    combat.slayer(NpcCombatSlayer.builder().level(50).experience(4100).build());
    combat.killCount(NpcCombatKillCount.builder().asName("Superior Slayer Creature").build());
    combat.combatScript("SuperiorSlayerCS").deathAnimation(1553).blockAnimation(1550);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(
        NpcCombatStyleType.builder().hitType(HitType.MELEE).subHitType(HitType.MAGIC).build());
    style.damage(NpcCombatDamage.maximum(20));
    style.animation(1552).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
