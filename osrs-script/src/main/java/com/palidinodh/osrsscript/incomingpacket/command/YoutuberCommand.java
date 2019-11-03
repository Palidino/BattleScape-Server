package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.io.cache.ItemId;
import com.palidinodh.osrscore.model.item.Item;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.Skills;
import lombok.var;

public class YoutuberCommand implements CommandHandler {
  @Override
  public String getExample() {
    return "username";
  }

  @Override
  public boolean canUse(Player player) {
    return player.getRights() == Player.RIGHTS_ADMIN;
  }

  @Override
  public void execute(Player player, String username) {
    var player2 = player.getWorld().getPlayerByUsername(username);

    player2.getBank().add(new Item(ItemId.SERPENTINE_HELM_UNCHARGED, 1));
    player2.getBank().add(new Item(ItemId.BANDOS_CHESTPLATE, 1));
    player2.getBank().add(new Item(ItemId.BANDOS_TASSETS, 1));
    player2.getBank().add(new Item(ItemId.ARMADYL_HELMET, 1));
    player2.getBank().add(new Item(ItemId.ARMADYL_CHESTPLATE, 1));
    player2.getBank().add(new Item(ItemId.ARMADYL_CHAINSKIRT, 1));
    player2.getBank().add(new Item(ItemId.ANCESTRAL_HAT, 1));
    player2.getBank().add(new Item(ItemId.ANCESTRAL_ROBE_TOP, 1));
    player2.getBank().add(new Item(ItemId.ANCESTRAL_ROBE_BOTTOM, 1));
    player2.getBank().add(new Item(ItemId.AHRIMS_HOOD, 1));
    player2.getBank().add(new Item(ItemId.AHRIMS_ROBETOP, 1));
    player2.getBank().add(new Item(ItemId.AHRIMS_ROBESKIRT, 1));
    player2.getBank().add(new Item(ItemId.AHRIMS_STAFF, 1));
    player2.getBank().add(new Item(ItemId.DHAROKS_HELM, 1));
    player2.getBank().add(new Item(ItemId.DHAROKS_PLATEBODY, 1));
    player2.getBank().add(new Item(ItemId.DHAROKS_PLATELEGS, 1));
    player2.getBank().add(new Item(ItemId.DHAROKS_GREATAXE, 1));
    player2.getBank().add(new Item(ItemId.KARILS_COIF, 1));
    player2.getBank().add(new Item(ItemId.KARILS_LEATHERTOP, 1));
    player2.getBank().add(new Item(ItemId.KARILS_LEATHERSKIRT, 1));
    player2.getBank().add(new Item(ItemId.KARILS_CROSSBOW, 1));
    player2.getBank().add(new Item(ItemId.VOID_KNIGHT_SET_32289, 1));
    player2.getBank().add(new Item(ItemId.FIGHTER_TORSO, 1));
    player2.getBank().add(new Item(ItemId.AMULET_OF_FURY, 1));
    player2.getBank().add(new Item(ItemId.NECKLACE_OF_ANGUISH, 1));
    player2.getBank().add(new Item(ItemId.AMULET_OF_TORTURE, 1));
    player2.getBank().add(new Item(ItemId.OCCULT_NECKLACE, 1));
    player2.getBank().add(new Item(ItemId.SALVE_AMULET_EI, 1));
    player2.getBank().add(new Item(ItemId.FIRE_CAPE, 1));
    player2.getBank().add(new Item(ItemId.INFERNAL_CAPE, 1));
    player2.getBank().add(new Item(ItemId.AVAS_ASSEMBLER, 1));
    player2.getBank().add(new Item(ItemId.IMBUED_SARADOMIN_CAPE, 1));
    player2.getBank().add(new Item(ItemId.IMBUED_GUTHIX_CAPE, 1));
    player2.getBank().add(new Item(ItemId.IMBUED_ZAMORAK_CAPE, 1));
    player2.getBank().add(new Item(ItemId.DIAMOND_DRAGON_BOLTS_E, 10000));
    player2.getBank().add(new Item(ItemId.DRAGONSTONE_DRAGON_BOLTS_E, 10000));
    player2.getBank().add(new Item(ItemId.DRAGONFIRE_SHIELD_11284, 1));
    player2.getBank().add(new Item(ItemId.DRAGONFIRE_WARD_22003, 1));
    player2.getBank().add(new Item(ItemId.ANCIENT_WYVERN_SHIELD_21634, 1));
    player2.getBank().add(new Item(ItemId.MAGES_BOOK, 1));
    player2.getBank().add(new Item(ItemId.TWISTED_BUCKLER, 1));
    player2.getBank().add(new Item(ItemId.DRAGON_DEFENDER, 1));
    player2.getBank().add(new Item(ItemId.ELYSIAN_SPIRIT_SHIELD, 1));
    player2.getBank().add(new Item(ItemId.DINHS_BULWARK, 1));
    player2.getBank().add(new Item(ItemId.FEROCIOUS_GLOVES, 1));
    player2.getBank().add(new Item(ItemId.BARROWS_GLOVES, 1));
    player2.getBank().add(new Item(ItemId.TORMENTED_BRACELET, 1));
    player2.getBank().add(new Item(ItemId.PRIMORDIAL_BOOTS, 1));
    player2.getBank().add(new Item(ItemId.PEGASIAN_BOOTS, 1));
    player2.getBank().add(new Item(ItemId.ETERNAL_BOOTS, 1));
    player2.getBank().add(new Item(ItemId.GUARDIAN_BOOTS, 1));
    player2.getBank().add(new Item(ItemId.BOOTS_OF_STONE, 1));
    player2.getBank().add(new Item(ItemId.BERSERKER_RING_I, 1));
    player2.getBank().add(new Item(ItemId.ARCHERS_RING_I, 1));
    player2.getBank().add(new Item(ItemId.SEERS_RING_I, 1));
    player2.getBank().add(new Item(ItemId.RING_OF_SUFFERING_I, 1));
    player2.getBank().add(new Item(ItemId.DEXTEROUS_PRAYER_SCROLL, 1));
    player2.getBank().add(new Item(ItemId.ARCANE_PRAYER_SCROLL, 1));
    player2.getBank().add(new Item(ItemId.ABYSSAL_TENTACLE, 1));
    player2.getBank().add(new Item(ItemId.ZAMORAKIAN_SPEAR, 1));
    player2.getBank().add(new Item(ItemId.ZAMORAKIAN_HASTA, 1));
    player2.getBank().add(new Item(ItemId.ARMADYL_GODSWORD, 1));
    player2.getBank().add(new Item(ItemId.ELDER_MAUL, 1));
    player2.getBank().add(new Item(ItemId.DRAGON_CLAWS, 1));
    player2.getBank().add(new Item(ItemId.DRAGON_WARHAMMER, 1));
    player2.getBank().add(new Item(ItemId.DRAGON_HUNTER_LANCE, 1));
    player2.getBank().add(new Item(ItemId.ABYSSAL_BLUDGEON, 1));
    player2.getBank().add(new Item(ItemId.TWISTED_BOW, 1));
    player2.getBank().add(new Item(ItemId.ARMADYL_CROSSBOW, 1));
    player2.getBank().add(new Item(ItemId.DRAGON_HUNTER_CROSSBOW, 1));
    player2.getBank().add(new Item(ItemId.TOXIC_BLOWPIPE_EMPTY, 1));
    player2.getBank().add(new Item(ItemId.DRAGON_CROSSBOW, 1));
    player2.getBank().add(new Item(ItemId.DRAGON_DART, 10000));
    player2.getBank().add(new Item(ItemId.DRAGON_ARROW, 10000));
    player2.getBank().add(new Item(ItemId.ZULRAHS_SCALES, 40000));
    player2.getBank().add(new Item(ItemId.UNCHARGED_TOXIC_TRIDENT, 1));
    player2.getBank().add(new Item(ItemId.TOXIC_STAFF_UNCHARGED, 1));
    player2.getBank().add(new Item(ItemId.KODAI_WAND, 1));
    player2.getBank().add(new Item(ItemId.MASTER_WAND, 1));
    player2.getBank().add(new Item(ItemId.SLAYER_HELMET_I, 1));
    player2.getBank().add(new Item(ItemId.BLACK_SLAYER_HELMET_I, 1));
    player2.getBank().add(new Item(ItemId.GREEN_SLAYER_HELMET_I, 1));
    player2.getBank().add(new Item(ItemId.HYDRA_SLAYER_HELMET_I, 1));
    player2.getBank().add(new Item(ItemId.PURPLE_SLAYER_HELMET_I, 1));
    player2.getBank().add(new Item(ItemId.RED_SLAYER_HELMET_I, 1));
    player2.getBank().add(new Item(ItemId.TURQUOISE_SLAYER_HELMET_I, 1));
    player2.getBank().add(new Item(ItemId.SUPER_COMBAT_POTION_4, 5000));
    player2.getBank().add(new Item(ItemId.BASTION_POTION_4, 5000));
    player2.getBank().add(new Item(ItemId.BATTLEMAGE_POTION_4, 5000));
    player2.getBank().add(new Item(ItemId.SUPER_RESTORE_4, 5000));
    player2.getBank().add(new Item(ItemId.STAMINA_POTION_4, 5000));
    player2.getBank().add(new Item(ItemId.SARADOMIN_BREW_4, 5000));

    for (var i = 0; i < Skills.SKILL_COUNT; i++) {
      player2.getSkills().setXP(i, Skills.XP_TABLE[99]);
      player2.getGameEncoder().sendSkillLevel(i);
    }
    player2.restore();

    player.getGameEncoder().sendMessage("Hooked up " + username + " with youtuber-gear..");
    player2.getGameEncoder().sendMessage("Your bank and stats have been set..");
  }
}
