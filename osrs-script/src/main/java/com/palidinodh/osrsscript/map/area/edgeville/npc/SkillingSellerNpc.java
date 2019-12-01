package com.palidinodh.osrsscript.map.area.edgeville.npc;

import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.io.incomingpacket.NpcHandler;
import com.palidinodh.osrscore.model.dialogue.DialogueOption;
import com.palidinodh.osrscore.model.dialogue.OptionsDialogue;
import com.palidinodh.osrscore.model.item.Item;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.random.PRandom;
import com.palidinodh.rs.ReferenceId;
import lombok.var;

@ReferenceId(NpcId.SKILLING_SELLER)
class SkillingSellerNpc implements NpcHandler {
  @Override
  public void execute(Player player, int option, Npc npc) {
    if (option == 0) {
      player.openDialogue(new OptionsDialogue(new DialogueOption("View shop", (c, s) -> {
        player.openShop("skilling");
      }), new DialogueOption("Exchange special skilling items", (c, s) -> {
        var unusualFishCount = player.getInventory().getCount(ItemId.UNUSUAL_FISH);
        if (unusualFishCount > 0) {
          var value = 300_000;
          if (player.isGameModeIronmanRelated()) {
            value /= 2;
          }
          player.getInventory().deleteItem(ItemId.UNUSUAL_FISH, unusualFishCount);
          player.getInventory().addOrDropItem(ItemId.COINS, value * unusualFishCount);
          if (PRandom.randomE(156 / unusualFishCount) == 0) {
            player.getInventory().addOrDropItem(ItemId.GOLDEN_TENCH);
            player.getGameEncoder().sendMessage(
                "<col=ff0000>The cormorant has brought you a very strange tench.</col>");
          }
        }
        var coloredEggCount = player.getInventory().getCount(ItemId.BIRDS_EGG)
            + player.getInventory().getCount(ItemId.BIRDS_EGG_5077)
            + player.getInventory().getCount(ItemId.BIRDS_EGG_5078);
        if (coloredEggCount > 0) {
          var value = 500_000;
          if (player.isGameModeIronmanRelated()) {
            value /= 2;
          }
          player.getInventory().deleteItem(ItemId.BIRDS_EGG, coloredEggCount);
          player.getInventory().deleteItem(ItemId.BIRDS_EGG_5077, coloredEggCount);
          player.getInventory().deleteItem(ItemId.BIRDS_EGG_5078, coloredEggCount);
          player.getInventory().addOrDropItem(ItemId.COINS, value * coloredEggCount);
          player.getInventory().addOrDropItem(ItemId.BIRD_NEST_5073, 1 * coloredEggCount);
          if (PRandom.randomE(132 / coloredEggCount) == 0) {
            var evilChickenOutfit = new Item[] { new Item(ItemId.EVIL_CHICKEN_FEET),
                new Item(ItemId.EVIL_CHICKEN_WINGS), new Item(ItemId.EVIL_CHICKEN_HEAD),
                new Item(ItemId.EVIL_CHICKEN_LEGS) };
            if (!player.hasItem(ItemId.EVIL_CHICKEN_FEET)) {
              player.getInventory().addOrDropItem(ItemId.EVIL_CHICKEN_FEET);
            } else if (!player.hasItem(ItemId.EVIL_CHICKEN_WINGS)) {
              player.getInventory().addOrDropItem(ItemId.EVIL_CHICKEN_WINGS);
            } else if (!player.hasItem(ItemId.EVIL_CHICKEN_HEAD)) {
              player.getInventory().addOrDropItem(ItemId.EVIL_CHICKEN_HEAD);
            } else if (!player.hasItem(ItemId.EVIL_CHICKEN_LEGS)) {
              player.getInventory().addOrDropItem(ItemId.EVIL_CHICKEN_LEGS);
            } else {
              player.getInventory().addOrDropItem(PRandom.arrayRandom(evilChickenOutfit));
            }
          }
        }
        var unidentifiedMineralCount = player.getInventory().getCount(ItemId.UNIDENTIFIED_MINERALS);
        if (unidentifiedMineralCount > 0) {
          var value = 300_000;
          if (player.isGameModeIronmanRelated()) {
            value /= 2;
          }
          player.getInventory().deleteItem(ItemId.UNIDENTIFIED_MINERALS, unidentifiedMineralCount);
          player.getInventory().addOrDropItem(ItemId.COINS, value * unidentifiedMineralCount);
        }
        if (unusualFishCount == 0 && coloredEggCount == 0 && unidentifiedMineralCount == 0) {
          player.getGameEncoder().sendMessage("You have no special items to exchange.");
        }
      }), new DialogueOption("Nevermind")));
    } else if (option == 2) {
      player.openShop("skilling");
    }
  }
}
