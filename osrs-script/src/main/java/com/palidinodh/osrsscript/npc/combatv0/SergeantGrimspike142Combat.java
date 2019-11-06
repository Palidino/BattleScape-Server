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
import com.palidinodh.osrscore.model.npc.combat.NpcCombatKillCount;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSpawn;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import lombok.var;

public class SergeantGrimspike142Combat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder();
    var dropTable = NpcCombatDropTable.builder().chance(0.02).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BANDOS_CHESTPLATE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BANDOS_TASSETS)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BANDOS_BOOTS)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(0.29).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_1)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_3)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_128);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CLUE_SCROLL_HARD)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_DART, 95, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COSMIC_RUNE, 25, 30)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.NATURE_RUNE, 15, 20)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_ARROW, 95, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 1300, 1500)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COMBAT_POTION_3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RIGHT_EYE_PATCH)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SHARK, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CHILLI_POTATO, 3)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BONES)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.SERGEANT_GRIMSPIKE_142);
    combat.spawn(NpcCombatSpawn.builder().respawnDelay(50).respawnWithId(NpcId.GENERAL_GRAARDOR_624)
        .respawnWithId(NpcId.SERGEANT_STRONGSTACK_141).respawnWithId(NpcId.SERGEANT_STEELWILL_142)
        .build());
    combat.hitpoints(NpcCombatHitpoints.total(146));
    combat.stats(NpcCombatStats.builder().attackLevel(80).magicLevel(50).rangedLevel(150)
        .defenceLevel(132).bonus(CombatBonus.ATTACK_RANGED, 20).build());
    combat.aggression(NpcCombatAggression.builder().range(16).build());
    combat.killCount(NpcCombatKillCount.builder().asName("General Graardor's bodyguard").build());
    combat.deathAnimation(6156).blockAnimation(6155);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(21));
    style.animation(6154).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
