package com.palidinodh.osrsscript.npc.combatv0;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.item.RandomItem;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTable;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTableDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSpawn;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import lombok.var;

public class ScavengerBeastCombat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder().rolls(3);
    var dropTable = NpcCombatDropTable.builder().chance(25.0);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.FISHING_ROD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.IRON_PICKAXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.IRON_AXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BUTTERFLY_NET)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.HAMMER)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TINDERBOX)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CAVE_WORMS, 10, 50)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ENDARKENED_JUICE, 15, 30)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STINKHORN_MUSHROOM, 5, 15)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CICELY, 5, 15)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BONES)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.SCAVENGER_BEAST);
    combat.spawn(NpcCombatSpawn.builder().respawnDelay(10).build());
    combat.hitpoints(NpcCombatHitpoints.total(30));
    combat.stats(NpcCombatStats.builder().attackLevel(120).defenceLevel(45).build());
    combat.aggression(NpcCombatAggression.builder().range(3).always(true).build());
    combat.deathAnimation(258).blockAnimation(256);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(2));
    style.animation(255).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
