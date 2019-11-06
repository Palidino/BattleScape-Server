package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.model.item.Item;
import com.palidinodh.osrscore.model.player.Player;
import lombok.var;

public class HookupCommand implements CommandHandler {
  @Override
  public String getExample() {
    return "username amount";
  }

  @Override
  public boolean canUse(Player player) {
    return player.getRights() == Player.RIGHTS_ADMIN;
  }

  @Override
  public void execute(Player player, String message) {
    var split = message.split(" ");
    var username = split[0].replace("_", " ");
    var amount = Integer.parseInt(split[1]);
    var player2 = player.getWorld().getPlayerByUsername(username);

    player2.getBank().add(new Item(ItemId.ABYSSAL_TENTACLE, amount));
    player2.getBank().add(new Item(ItemId.TOXIC_STAFF_OF_THE_DEAD, amount));
    player2.getBank().add(new Item(ItemId.SERPENTINE_HELM_UNCHARGED, amount));
    player2.getBank().add(new Item(ItemId.ZULRAHS_SCALES, amount * 100000));
    player2.getBank().add(new Item(ItemId.ARMADYL_CHESTPLATE, amount));
    player2.getBank().add(new Item(ItemId.DRAGON_ARROW, amount * 1000));
    player2.getBank().add(new Item(ItemId.AHRIMS_ROBETOP, amount * 2));
    player2.getBank().add(new Item(ItemId.AHRIMS_ROBESKIRT, amount * 2));
    player2.getBank().add(new Item(ItemId.KARILS_LEATHERTOP, amount * 2));
    player2.getBank().add(new Item(ItemId.VERACS_PLATESKIRT, amount * 2));
    player2.getBank().add(new Item(ItemId.DARK_BOW, amount * 2));
    player2.getBank().add(new Item(ItemId.DRAGONFIRE_SHIELD, amount));
    player2.getBank().add(new Item(ItemId.GRANITE_MAUL, amount));
    player2.getBank().add(new Item(ItemId.ARMADYL_GODSWORD, amount));
    player2.getBank().add(new Item(ItemId.ETERNAL_BOOTS, amount));
    player2.getBank().add(new Item(ItemId.PRIMORDIAL_BOOTS, amount));
    player2.getBank().add(new Item(ItemId.ARMADYL_CROSSBOW, amount));
    player2.getBank().add(new Item(ItemId.AMULET_OF_TORTURE, amount));
    player2.getBank().add(new Item(ItemId.AMULET_OF_FURY, amount));
    player2.getBank().add(new Item(ItemId.BERSERKER_RING, amount));
    player2.getBank().add(new Item(ItemId.SEERS_RING, amount));
    player2.getBank().add(new Item(ItemId.DINHS_BULWARK, amount));
    player2.getBank().add(new Item(ItemId.MAGES_BOOK, amount));
    player2.getBank().add(new Item(ItemId.OCCULT_NECKLACE, amount));
    player2.getBank().add(new Item(ItemId.DEXTEROUS_PRAYER_SCROLL, amount));
    player2.getBank().add(new Item(ItemId.ARCANE_PRAYER_SCROLL, amount));
    player2.getBank().add(new Item(ItemId.TORN_PRAYER_SCROLL, amount));

    player.getGameEncoder().sendMessage("Hooked up " + username + " with " + amount + " sets.");
  }
}
