package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.item.RandomItem;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTable;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTableDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import lombok.var;

public class ScorpionCombat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder();
    var dropTable = NpcCombatDropTable.builder().chance(4);
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ENSOULED_SCORPION_HEAD_13460)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.SCORPION_14);
    combat.hitpoints(NpcCombatHitpoints.total(17));
    combat.stats(NpcCombatStats.builder().attackLevel(11).defenceLevel(11)
        .bonus(CombatBonus.DEFENCE_STAB, 5).bonus(CombatBonus.DEFENCE_SLASH, 15)
        .bonus(CombatBonus.DEFENCE_CRUSH, 15).bonus(CombatBonus.DEFENCE_RANGED, 5).build());
    combat.deathAnimation(6256).blockAnimation(6255);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.damage(NpcCombatDamage.maximum(2));
    style.animation(6254).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
