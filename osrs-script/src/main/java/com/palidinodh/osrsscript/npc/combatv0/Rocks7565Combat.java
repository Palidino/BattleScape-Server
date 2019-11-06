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

public class Rocks7565Combat extends NpcCombat {
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
    combat.id(NpcId.ROCKS_7565);
    combat.hitpoints(NpcCombatHitpoints.total(1600));
    combat.stats(
        NpcCombatStats.builder().attackLevel(272).magicLevel(272).rangedLevel(272).defenceLevel(272)
            .bonus(CombatBonus.MELEE_DEFENCE, 50).bonus(CombatBonus.DEFENCE_MAGIC, 50).build());
    combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).bind(true).build());
    combat.focus(NpcCombatFocus.builder().disableFacingOpponent(true).disableFollowingOpponent(true)
        .build());
    combat.combatScript("VasaNistirioCS").deathAnimation(7415);
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
