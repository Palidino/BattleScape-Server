package com.palidinodh.osrsscript.npc.combat;

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
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import lombok.var;

public class AbyssalLeechCombat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder().clue(NpcCombatDrop.ClueScroll.MEDIUM,
        NpcCombatDropTable.CHANCE_1_IN_256);
    var dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_128);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COSMIC_TALISMAN)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(2.39).order(NpcCombatDropTable.Order.UNIQUE);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SMALL_POUCH)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MEDIUM_POUCH)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LARGE_POUCH)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GIANT_POUCH)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_RARE);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LAW_TALISMAN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ELEMENTAL_TALISMAN)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CHAOS_TALISMAN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.NATURE_TALISMAN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BINDING_NECKLACE)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.AIR_TALISMAN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MIND_TALISMAN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.EARTH_TALISMAN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WATER_TALISMAN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.FIRE_TALISMAN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BODY_TALISMAN)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ASHES)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.ABYSSAL_LEECH_41);
    combat.hitpoints(NpcCombatHitpoints.total(10));
    combat.stats(NpcCombatStats.builder().attackLevel(95).defenceLevel(25)
        .bonus(CombatBonus.MELEE_ATTACK, 100).bonus(CombatBonus.DEFENCE_STAB, 50)
        .bonus(CombatBonus.DEFENCE_SLASH, 50).bonus(CombatBonus.DEFENCE_CRUSH, 100)
        .bonus(CombatBonus.DEFENCE_MAGIC, 50).bonus(CombatBonus.DEFENCE_RANGED, 10).build());
    combat.aggression(NpcCombatAggression.PLAYERS);
    combat.deathAnimation(2183).blockAnimation(2182);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_STAB);
    style.damage(NpcCombatDamage.maximum(2));
    style.animation(2181).attackSpeed(1);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
