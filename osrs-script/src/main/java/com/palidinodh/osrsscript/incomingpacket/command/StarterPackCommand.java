package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.setting.SqlUserRank;

public class StarterPackCommand implements CommandHandler {
  @Override
  public boolean canUse(Player player) {
    return player.getRights() == Player.RIGHTS_ADMIN
        || player.isUsergroup(SqlUserRank.COMMUNITY_MANAGER);
  }

  @Override
  public void execute(Player player, String message) {
    player.getInventory().addOrDropItem(ItemId.DRAGON_SCIMITAR);
    player.getInventory().addOrDropItem(ItemId.RUNE_FULL_HELM);
    player.getInventory().addOrDropItem(ItemId.RUNE_PLATEBODY);
    player.getInventory().addOrDropItem(ItemId.RUNE_PLATELEGS);
    player.getInventory().addOrDropItem(ItemId.RUNE_KITESHIELD);
    player.getInventory().addOrDropItem(ItemId.RUNE_BOOTS);
    player.getInventory().addOrDropItem(ItemId.RUNE_CROSSBOW);
    player.getInventory().addOrDropItem(ItemId.DIAMOND_BOLTS_E, 100);
    player.getInventory().addOrDropItem(ItemId.BLACK_DHIDE_BODY);
    player.getInventory().addOrDropItem(ItemId.BLACK_DHIDE_CHAPS);
    player.getInventory().addOrDropItem(ItemId.BLACK_DHIDE_VAMB);
    player.getInventory().addOrDropItem(ItemId.BLACK_DHIDE_SHIELD);
    player.getInventory().addOrDropItem(ItemId.MYSTIC_AIR_STAFF);
    player.getInventory().addOrDropItem(ItemId.DEATH_RUNE, 100);
    player.getInventory().addOrDropItem(ItemId.BLOOD_RUNE, 100);
    player.getInventory().addOrDropItem(ItemId.MYSTIC_HAT);
    player.getInventory().addOrDropItem(ItemId.MYSTIC_ROBE_TOP);
    player.getInventory().addOrDropItem(ItemId.MYSTIC_ROBE_BOTTOM);
    player.getInventory().addOrDropItem(ItemId.MYSTIC_GLOVES);
    player.getInventory().addOrDropItem(ItemId.MYSTIC_BOOTS);
    player.getInventory().addOrDropItem(ItemId.COINS, 250000);
    player.getInventory().addOrDropItem(ItemId.MONKFISH_NOTED, 150);
    player.getInventory().addOrDropItem(ItemId.SUPER_ATTACK_4_NOTED, 15);
    player.getInventory().addOrDropItem(ItemId.SUPER_STRENGTH_4_NOTED, 15);
    player.getInventory().addOrDropItem(ItemId.SUPER_DEFENCE_4_NOTED, 15);
    player.getInventory().addOrDropItem(ItemId.PRAYER_POTION_4_NOTED, 60);
  }
}
