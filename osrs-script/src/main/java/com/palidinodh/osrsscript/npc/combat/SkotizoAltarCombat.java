package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.ItemId;
import com.palidinodh.osrscore.io.cache.NpcId;
import com.palidinodh.osrscore.model.Entity;
import com.palidinodh.osrscore.model.HitType;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatFocus;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSpawn;
import com.palidinodh.osrscore.model.player.Player;
import lombok.var;

public class SkotizoAltarCombat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.AWAKENED_ALTAR);
    combat.spawn(NpcCombatSpawn.builder().respawnDelay(6000).build());
    combat.hitpoints(NpcCombatHitpoints.total(100));
    combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    combat.focus(NpcCombatFocus.builder().retaliationDisabled(true).build());


    return Arrays.asList(combat.build());
  }

  @Override
  public double damageReceivedHook(Entity opponent, double damage, HitType hitType,
      HitType defenceType) {
    if (!(opponent instanceof Player)) {
      return damage;
    }
    var player = (Player) opponent;
    if (hitType == HitType.MELEE && player.getEquipment().getWeaponId() == ItemId.ARCLIGHT) {
      damage = 100;
    }
    return damage;
  }
}
