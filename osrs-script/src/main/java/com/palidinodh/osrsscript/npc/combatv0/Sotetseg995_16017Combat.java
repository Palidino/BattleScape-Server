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

public class Sotetseg995_16017Combat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder().underKiller(true)
        .rareDropTableRate(NpcCombatDropTable.CHANCE_1_IN_256);
    var dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLOODY_KEY)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.SOTETSEG_995_16017);
    combat.spawn(NpcCombatSpawn.builder().respawnDelay(36000).build());
    combat.hitpoints(
        NpcCombatHitpoints.builder().total(4000).bar(HitpointsBar.GREEN_RED_120).build());
    combat.stats(NpcCombatStats.builder().attackLevel(250).magicLevel(250).rangedLevel(250)
        .defenceLevel(200).bonus(CombatBonus.MELEE_DEFENCE, 70).bonus(CombatBonus.DEFENCE_MAGIC, 30)
        .bonus(CombatBonus.DEFENCE_RANGED, 150).build());
    combat.aggression(NpcCombatAggression.builder().range(8).build());
    combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    combat.killCount(NpcCombatKillCount.builder().sendMessage(true).build());
    combat.combatScript("SotetsegCS").deathAnimation(8140);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.builder().maximum(50).delayedPrayerProtectable(true).build());
    style.animation(8138).attackSpeed(5);
    style.projectile(NpcCombatProjectile.builder().id(1607).speedType(HitType.MAGIC)
        .speedMinimumDistance(14).build());
    style.effect(NpcCombatEffect.builder().damageProtectionPrayerBlock(8).build());
    style.multiTarget(true);
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(50).delayedPrayerProtectable(true).build());
    style.animation(8138).attackSpeed(5);
    style.projectile(NpcCombatProjectile.builder().id(1606).speedType(HitType.MAGIC)
        .speedMinimumDistance(14).build());
    style.effect(NpcCombatEffect.builder().damageProtectionPrayerBlock(8).build());
    style.multiTarget(true);
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
