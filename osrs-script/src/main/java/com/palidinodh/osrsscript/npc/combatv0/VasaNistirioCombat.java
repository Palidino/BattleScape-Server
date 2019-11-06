package com.palidinodh.osrsscript.npc.combatv0;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.HitType;
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
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.npc.combat.style.special.NpcCombatTargetTile;
import lombok.var;

public class VasaNistirioCombat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder();
    var dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ENDARKENED_JUICE, 5)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TWISTED_PLUS_4, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.XERICS_AID_PLUS_4, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TWISTED_PLUS_4, 2)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.VASA_NISTIRIO);
    combat.hitpoints(NpcCombatHitpoints.total(400));
    combat.stats(NpcCombatStats.builder().magicLevel(230).rangedLevel(230).defenceLevel(175)
        .bonus(CombatBonus.ATTACK_RANGED, 100).bonus(CombatBonus.DEFENCE_STAB, 170)
        .bonus(CombatBonus.DEFENCE_SLASH, 190).bonus(CombatBonus.DEFENCE_CRUSH, 50)
        .bonus(CombatBonus.DEFENCE_MAGIC, 400).bonus(CombatBonus.DEFENCE_RANGED, 60).build());
    combat.aggression(NpcCombatAggression.builder().range(12).always(true).build());
    combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).bind(true).build());
    combat.focus(NpcCombatFocus.builder().disableFacingOpponent(true).disableFollowingOpponent(true)
        .build());
    combat.combatScript("VasaNistirioCS").underneathDamage(1).deathAnimation(7415);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(
        NpcCombatStyleType.builder().hitType(HitType.RANGED).subHitType(HitType.TYPELESS).build());
    style.damage(NpcCombatDamage.builder().maximum(30).prayerEffectiveness(0.5).build());
    style.attackSpeed(2).attackRange(7);
    style.targetGraphic(new Graphic(1330));
    style.projectile(NpcCombatProjectile.builder().id(1329).speedMinimumDistance(10).build());
    style.multiTarget(true);
    var targetTile = NpcCombatTargetTile.builder().radius(1);
    style.specialAttack(targetTile.build());
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
