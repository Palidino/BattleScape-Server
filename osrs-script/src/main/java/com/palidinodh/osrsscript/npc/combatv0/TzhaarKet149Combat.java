package com.palidinodh.osrsscript.npc.combatv0;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.ItemId;
import com.palidinodh.osrscore.io.cache.NpcId;
import com.palidinodh.osrscore.model.item.RandomItem;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTable;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTableDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatKillCount;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import lombok.var;

public class TzhaarKet149Combat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder();
    var dropTable = NpcCombatDropTable.builder().chance(0.1).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNCUT_ONYX)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(0.6).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.OBSIDIAN_HELMET)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.OBSIDIAN_PLATEBODY)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.OBSIDIAN_PLATELEGS)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(2.3).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TZHAAR_KET_OM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TOKTZ_KET_XIL)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.OBSIDIAN_CAPE)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(2.8);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ENSOULED_TZHAAR_HEAD_13499)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_RARE);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNCUT_DIAMOND)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNCUT_SAPPHIRE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNCUT_EMERALD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNCUT_RUBY)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TOKKUL, 1, 70)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.TZHAAR_KET_149);
    combat.hitpoints(NpcCombatHitpoints.total(140));
    combat
        .stats(NpcCombatStats.builder().attackLevel(120).magicLevel(40).defenceLevel(120).build());
    combat.killCount(NpcCombatKillCount.builder().asName("TzHaar").build());
    combat.deathAnimation(2607).blockAnimation(2605);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(15));
    style.animation(2610).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
