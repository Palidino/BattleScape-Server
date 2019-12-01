package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.google.inject.Inject;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.HitType;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.TileHitEvent;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatFocus;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import lombok.var;

class JalMejjakCombat extends NpcCombat {
  @Inject
  private Npc npc;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.JAL_MEJJAK_250);
    combat.hitpoints(NpcCombatHitpoints.total(80));
    combat.stats(NpcCombatStats.builder().defenceLevel(100).build());
    combat.aggression(NpcCombatAggression.builder().range(8).always(true).sameTarget(true).build());
    combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    combat.focus(NpcCombatFocus.builder().bypassMapObjects(true).build());
    combat.deathAnimation(2866).blockAnimation(2869);

    var style = NpcCombatStyle.builder();
    style
        .type(NpcCombatStyleType.builder().hitType(HitType.MAGIC).subHitType(HitType.HEAL).build());
    style.damage(NpcCombatDamage.maximum(23));
    style.animation(2868).attackSpeed(3);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }

  @Override
  public void tickStartHook() {
    if (npc.isLocked() || npc.isDead() || npc.getHitDelay() > 0
        || npc.getEngagingEntity() instanceof Npc) {
      return;
    }
    npc.setAnimation(2868);
    for (var i = 0; i < 3; i++) {
      var tile = new Tile(npc.getX(), npc.getY() - 7);
      tile.randomize(2);
      var projectile = Graphic.Projectile.builder().id(660).startTile(npc).endTile(tile)
          .projectileSpeed(getProjectileSpeed(tile)).startHeight(1).endHeight(1).curve(255).build();
      sendMapProjectile(projectile);
      npc.getController().sendMapGraphic(tile, new Graphic(659, 0, projectile.getContactDelay()));
      var the = new TileHitEvent(projectile.getEventDelay(), npc.getController(), tile, 10,
          HitType.MAGIC);
      the.setRadius(1);
      the.setIgnorePrayer(true);
      npc.getWorld().addEvent(the);
    }
    npc.setHitDelay(3);
  }
}
