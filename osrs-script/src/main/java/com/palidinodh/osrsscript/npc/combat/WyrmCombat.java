package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.google.inject.Inject;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.item.RandomItem;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTable;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTableDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSlayer;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.player.Player;
import lombok.var;

public class WyrmCombat extends NpcCombat {
  @Inject
  private Npc npc;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder().clue(NpcCombatDrop.ClueScroll.HARD,
        NpcCombatDropTable.CHANCE_1_IN_256);
    var dropTable =
        NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_5000).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_KNIFE, 75, 150)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_THROWNAXE, 75, 150)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_5000).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_HARPOON)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_SWORD)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_RARE);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SNAPDRAGON_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DWARF_WEED_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TORSTOL_SEED)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_DAGGER)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLOOD_RUNE, 25, 30)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.EARTH_BATTLESTAFF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_MED_HELM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_BATTLEAXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_MARRENTILL, 1, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_TARROMIN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_HARRALANDER, 1, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_RANARR_WEED, 1, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_IRIT_LEAF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_AVANTOE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_KWUARM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_CADANTINE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SNAPE_GRASS_SEED, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.POTATO_CACTUS_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BELLADONNA_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.POISON_IVY_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CACTUS_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.IRIT_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.KWUARM_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TOADFLAX_SEED)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PURE_ESSENCE_NOTED, 201, 300)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_ARROWTIPS, 8, 12)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_ARROWTIPS, 8, 12)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNCUT_RUBY)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNCUT_DIAMOND)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNCUT_SAPPHIRE)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.FIRE_RUNE, 200)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.EARTH_RUNE, 77, 150)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SOUL_RUNE, 15, 20)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_AXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RED_DHIDE_CHAPS)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_SQ_SHIELD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_BATTLEAXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_2H_SWORD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 953, 1500)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BASS)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WYRM_BONES)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.WYRM_99).id(NpcId.WYRM_99_8611);
    combat.hitpoints(NpcCombatHitpoints.total(130));
    combat.stats(NpcCombatStats.builder().attackLevel(85).magicLevel(80).rangedLevel(80)
        .defenceLevel(80).bonus(CombatBonus.DEFENCE_STAB, 20).bonus(CombatBonus.DEFENCE_SLASH, 50)
        .bonus(CombatBonus.DEFENCE_CRUSH, 50).bonus(CombatBonus.DEFENCE_MAGIC, 50)
        .bonus(CombatBonus.DEFENCE_RANGED, 20).build());
    combat.slayer(NpcCombatSlayer.builder().level(62).build());
    combat.aggression(NpcCombatAggression.PLAYERS);
    combat.immunity(NpcCombatImmunity.builder().poison(true).build());
    combat.deathAnimation(8272);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_STAB);
    style.damage(NpcCombatDamage.maximum(13));
    style.animation(8270).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(13));
    style.animation(8271).attackSpeed(4);
    style.targetGraphic(new Graphic(1635));
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }

  @Override
  public void tickStartHook() {
    if (npc.isLocked()) {
      return;
    }
    if (npc.getId() != NpcId.WYRM_99 && npc.getInCombatDelay() == 0 && !npc.isAttacking()) {
      npc.setTransformationId(NpcId.WYRM_99);
      npc.setAnimation(8269);
    } else if (npc.getId() != NpcId.WYRM_99_8611
        && (npc.getInCombatDelay() > 0 || npc.isAttacking())) {
      npc.setTransformationId(NpcId.WYRM_99_8611);
      npc.setAnimation(8268);
    }
  }

  @Override
  public double dropTableChanceHook(Player player, int dropRateDivider, int roll,
      NpcCombatDropTable table) {
    var chance = table.getChance();
    if (chance < 1 && player.getSkills().isAnySlayerTask(npc)) {
      chance *= 5;
    }
    return chance;
  }
}
