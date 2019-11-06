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
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import lombok.var;

public class LizardmanShaman7574Combat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder();
    var dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BIG_BONES)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BUCHU_SEED, 8, 12)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GOLPAR_SEED, 8, 12)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.NOXIFER_SEED, 8, 12)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.LIZARDMAN_SHAMAN_7574);
    combat.hitpoints(NpcCombatHitpoints.total(150));
    combat.stats(NpcCombatStats.builder().attackLevel(130).magicLevel(130).rangedLevel(130)
        .defenceLevel(210).bonus(CombatBonus.MELEE_ATTACK, 58).bonus(CombatBonus.ATTACK_RANGED, 56)
        .bonus(CombatBonus.DEFENCE_STAB, 102).bonus(CombatBonus.DEFENCE_SLASH, 160)
        .bonus(CombatBonus.DEFENCE_CRUSH, 150).bonus(CombatBonus.DEFENCE_MAGIC, 160).build());
    combat.aggression(NpcCombatAggression.builder().range(6).build());
    combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    combat.combatScript("LizardmanShamanCS").deathAnimation(7196).blockAnimation(7194);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(31));
    style.animation(7158).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(31));
    style.animation(7193).attackSpeed(4).attackRange(9);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
