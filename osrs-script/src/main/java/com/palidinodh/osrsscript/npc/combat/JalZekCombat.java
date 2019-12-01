package com.palidinodh.osrsscript.npc.combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.google.inject.Inject;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.random.PRandom;
import com.palidinodh.util.PCollection;
import lombok.var;

class JalZekCombat extends NpcCombat {
  @Inject
  private Npc npc;
  private List<Npc> monsters = new ArrayList<>();

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.JAL_ZEK_490).id(NpcId.JAL_ZEK_490_7703);
    combat.hitpoints(NpcCombatHitpoints.total(220));
    combat.stats(NpcCombatStats.builder().attackLevel(370).magicLevel(300).rangedLevel(510)
        .defenceLevel(260).bonus(CombatBonus.ATTACK_MAGIC, 80).build());
    combat.aggression(NpcCombatAggression.builder().range(8).always(true).sameTarget(true).build());
    combat.immunity(NpcCombatImmunity.builder().venom(true).build());
    combat.deathAnimation(7613);

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_STAB);
    style.damage(NpcCombatDamage.maximum(70));
    style.animation(7612).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.maximum(70));
    style.animation(7610).attackSpeed(4).attackRange(28);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }

  @Override
  @SuppressWarnings("rawtypes")
  public Object script(String name, Object... args) {
    if (name.equals("monsters")) {
      List<Npc> list = PCollection.castList((List) args[0], Npc.class);
      for (Npc n2 : list) {
        if (npc == n2) {
          continue;
        }
        monsters.add(n2);
      }
    }
    return null;
  }

  @Override
  public void restoreHook() {
    npc.getWorld().removeNpcs(monsters);
    monsters.clear();
  }

  @Override
  public void tickStartHook() {
    if (npc.isLocked() || npc.isAttackLocked() || npc.getHitDelay() != 0
        || !(npc.getEngagingEntity() instanceof Player) || monsters.isEmpty()
        || PRandom.randomE(4) != 0) {
      return;
    }
    Npc deadNpc = null;
    for (var i = 0; i < monsters.size(); i++) {
      var listNpc = monsters.get(PRandom.randomE(monsters.size()));
      if (listNpc.isVisible()) {
        continue;
      }
      monsters.remove(listNpc);
      deadNpc = listNpc;
      break;
    }
    if (deadNpc == null) {
      return;
    }
    var p = (Player) npc.getEngagingEntity();
    Tile tile;
    if (npc.getId() == NpcId.JAL_ZEK_490_7703) {
      tile = new Tile(2275, 5351, npc.getHeight());
    } else {
      tile = new Tile(2267, 5340, npc.getHeight());
      tile.moveTile(PRandom.randomI(8), PRandom.randomI(2));
    }
    var summoned = new Npc(npc.getController(), deadNpc.getId(), tile);
    summoned.getMovement().setClipNPCs(true);
    summoned.setHitpoints(summoned.getMaxHitpoints() / 2);
    summoned.getCombat().setTarget(p);
    summoned.setGraphic(444, 100);
    p.getCombat().getTzHaar().addMonster(summoned);
    npc.setHitDelay(8);
    npc.setAttackLock(4);
    npc.setAnimation(7611);
    npc.setFaceEntity(summoned);
    npc.setFaceTile(summoned);
  }
}
