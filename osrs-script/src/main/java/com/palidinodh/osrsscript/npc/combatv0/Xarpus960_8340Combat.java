package com.palidinodh.osrsscript.npc.combatv0;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.HitMark;
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
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatEffect;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.npc.combat.style.special.NpcCombatTargetTile;
import lombok.var;

public class Xarpus960_8340Combat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder();
    var dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.OVERLOAD_PLUS_4, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.REVITALISATION_PLUS_4, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PRAYER_ENHANCE_PLUS_4)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.XARPUS_960_8340);
    combat
        .hitpoints(NpcCombatHitpoints.builder().total(400).bar(HitpointsBar.GREEN_RED_100).build());
    combat.stats(NpcCombatStats.builder().magicLevel(220).defenceLevel(250)
        .bonus(CombatBonus.DEFENCE_RANGED, 160).build());
    combat.aggression(NpcCombatAggression.builder().range(12).always(true).build());
    combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).bind(true).build());
    combat.focus(NpcCombatFocus.builder().disableFollowingOpponent(true).build());
    combat.combatScript("xarpus").deathAnimation(8063);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.builder().hitType(HitType.MAGIC).subHitType(HitType.TYPELESS)
        .hitMark(HitMark.POISON).build());
    style.damage(
        NpcCombatDamage.builder().maximum(6).alwaysMaximum(true).ignorePrayer(true).build());
    style.animation(8059).attackSpeed(6);
    style.targetGraphic(new Graphic(1556));
    style.projectile(NpcCombatProjectile.builder().id(1555).speedMinimumDistance(8).build());
    style.effect(NpcCombatEffect.builder().poison(3).build());
    var targetTile = NpcCombatTargetTile.builder().radius(1);
    targetTile.breakOff(NpcCombatTargetTile.BreakOff.builder().count(1).distance(7).maximumDamage(8)
        .afterTargettedTile(true).build());
    style.specialAttack(targetTile.build());
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
