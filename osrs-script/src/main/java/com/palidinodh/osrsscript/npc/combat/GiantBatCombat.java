package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.ItemId;
import com.palidinodh.osrscore.io.cache.NpcId;
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
import lombok.var;

public class GiantBatCombat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder();
    var dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BAT_BONES)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.GIANT_BAT_27);
    combat.hitpoints(NpcCombatHitpoints.total(32));
    combat.stats(NpcCombatStats.builder().attackLevel(22).defenceLevel(22)
        .bonus(CombatBonus.DEFENCE_STAB, 10).bonus(CombatBonus.DEFENCE_SLASH, 10)
        .bonus(CombatBonus.DEFENCE_CRUSH, 12).bonus(CombatBonus.DEFENCE_MAGIC, 10)
        .bonus(CombatBonus.DEFENCE_RANGED, 8).build());
    combat.aggression(NpcCombatAggression.PLAYERS);
    combat.deathAnimation(4917).blockAnimation(4916);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.damage(NpcCombatDamage.maximum(3));
    style.animation(4915).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
