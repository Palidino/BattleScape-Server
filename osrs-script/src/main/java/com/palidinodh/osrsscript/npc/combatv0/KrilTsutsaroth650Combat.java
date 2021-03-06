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
import com.palidinodh.osrscore.model.npc.combat.NpcCombatFocus;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatKillCount;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSpawn;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatType;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatEffect;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import lombok.var;

class KrilTsutsaroth650Combat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var krilTsutsarothDrop =
        NpcCombatDrop.builder().rareDropTableRate(NpcCombatDropTable.CHANCE_1_IN_256)
            .clue(NpcCombatDrop.ClueScroll.ELITE, NpcCombatDropTable.CHANCE_1_IN_250);
    var dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_5000)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PET_KRIL_TSUTSAROTH)));
    krilTsutsarothDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_254)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ZAMORAK_HILT)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STAFF_OF_THE_DEAD)));
    krilTsutsarothDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_254).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_1)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_3)));
    krilTsutsarothDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_127)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ZAMORAKIAN_SPEAR)));
    krilTsutsarothDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEAM_BATTLESTAFF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_SCIMITAR)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_DAGGER)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLOOD_RUNE, 80, 90)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_LANTADYME_NOTED, 7, 13)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LANTADYME_SEED, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_SWORD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_TORSTOL_NOTED, 1, 5)));
    krilTsutsarothDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_ARROW, 295, 300)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_PLATELEGS)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_PLATEBODY)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SUPER_ATTACK_3, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SUPER_RESTORE_3, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SUPER_STRENGTH_3, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ZAMORAK_BREW_3, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DEATH_RUNE, 120, 124)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 19362, 20073)));
    krilTsutsarothDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ASHES)));
    krilTsutsarothDrop.table(dropTable.build());


    var krilTsutsarothCombat = NpcCombatDefinition.builder();
    krilTsutsarothCombat.id(NpcId.KRIL_TSUTSAROTH_650);
    krilTsutsarothCombat.phrase("Attack them, you dogs!").phrase("Forward!")
        .phrase("Death to Saradomin's dogs!").phrase("Kill them, you cowards!")
        .phrase("The Dark One will have their souls!").phrase("Zamorak curse them!")
        .phrase("Rend them limb from limb!").phrase("No retreat!").phrase("Flay them all!");
    krilTsutsarothCombat.spawn(NpcCombatSpawn.builder().respawnDelay(100).build());
    krilTsutsarothCombat.hitpoints(NpcCombatHitpoints.total(255));
    krilTsutsarothCombat.stats(NpcCombatStats.builder().attackLevel(340).magicLevel(200)
        .defenceLevel(270).bonus(CombatBonus.MELEE_ATTACK, 160).bonus(CombatBonus.MELEE_DEFENCE, 80)
        .bonus(CombatBonus.DEFENCE_MAGIC, 130).bonus(CombatBonus.DEFENCE_RANGED, 80).build());
    krilTsutsarothCombat.aggression(NpcCombatAggression.builder().range(16).build());
    krilTsutsarothCombat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    krilTsutsarothCombat
        .focus(NpcCombatFocus.builder().keepWithinDistance(1).singleTargetFocus(true).build());
    krilTsutsarothCombat.killCount(NpcCombatKillCount.builder().sendMessage(true).build());
    krilTsutsarothCombat.type(NpcCombatType.DEMON).deathAnimation(6949).blockAnimation(6947);
    krilTsutsarothCombat.drop(krilTsutsarothDrop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_SLASH);
    style.damage(NpcCombatDamage.maximum(47));
    style.animation(6948).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    style.effect(NpcCombatEffect.builder().poison(16).build());
    krilTsutsarothCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.maximum(30));
    style.animation(6950).attackSpeed(6);
    style.castGraphic(new Graphic(1224, 100));
    style.projectile(NpcCombatProjectile.id(335));
    style.effect(NpcCombatEffect.builder().poison(16).build());
    style.multiTarget(true);
    krilTsutsarothCombat.style(style.build());


    return Arrays.asList(krilTsutsarothCombat.build());
  }
}
