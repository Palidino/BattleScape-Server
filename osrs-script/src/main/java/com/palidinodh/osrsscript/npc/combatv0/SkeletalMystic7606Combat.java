package com.palidinodh.osrsscript.npc.combatv0;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.item.RandomItem;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTable;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTableDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatType;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatEffect;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.player.Skills;
import lombok.var;

class SkeletalMystic7606Combat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder();
    var dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BUCHU_SEED, 8, 12)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GOLPAR_SEED, 8, 12)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.NOXIFER_SEED, 8, 12)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.SKELETAL_MYSTIC_7606);
    combat.hitpoints(NpcCombatHitpoints.total(150));
    combat.stats(NpcCombatStats.builder().attackLevel(140).magicLevel(140).defenceLevel(187)
        .bonus(CombatBonus.MELEE_ATTACK, 85).bonus(CombatBonus.ATTACK_MAGIC, 40)
        .bonus(CombatBonus.DEFENCE_STAB, 155).bonus(CombatBonus.DEFENCE_SLASH, 155)
        .bonus(CombatBonus.DEFENCE_CRUSH, 115).bonus(CombatBonus.DEFENCE_MAGIC, 140)
        .bonus(CombatBonus.DEFENCE_RANGED, 115).build());
    combat.aggression(NpcCombatAggression.builder().range(12).always(true).build());
    combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    combat.combatScript("SkeletalMysticCS").type(NpcCombatType.UNDEAD).deathAnimation(5491)
        .blockAnimation(5489);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_SLASH);
    style.damage(NpcCombatDamage.builder().maximum(16).prayerEffectiveness(0.5).build());
    style.animation(5485).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(
        NpcCombatDamage.builder().maximum(16).prayerEffectiveness(0.5).splashOnMiss(true).build());
    style.animation(5523).attackSpeed(4);
    style.targetGraphic(new Graphic(131, 124));
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(
        NpcCombatDamage.builder().maximum(16).prayerEffectiveness(0.5).splashOnMiss(true).build());
    style.animation(5523).attackSpeed(4);
    style.targetGraphic(new Graphic(110, 124));
    style.projectile(NpcCombatProjectile.id(335));
    style.effect(NpcCombatEffect.builder().statDrain(Skills.DEFENCE, 1).build());
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
