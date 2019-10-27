package com.palidinodh.osrsscript.player.skill;

import java.util.ArrayList;
import java.util.List;
import com.palidinodh.osrscore.io.cache.ItemId;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.item.Item;
import com.palidinodh.osrscore.model.item.RandomItem;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.Skills;
import com.palidinodh.osrscore.model.player.skill.SkillContainer;
import com.palidinodh.osrscore.model.player.skill.SkillEntry;
import com.palidinodh.osrscore.model.player.skill.SkillModel;
import com.palidinodh.osrscore.model.player.skill.SkillPet;
import com.palidinodh.osrscore.model.player.skill.SkillTemporaryMapObject;
import com.palidinodh.random.PRandom;
import com.palidinodh.util.PEvent;
import com.palidinodh.util.PNumber;
import lombok.Getter;
import lombok.var;

public class Woodcutting extends SkillContainer {
  private static final WoodcuttingHatchet[] HATCHETS =
      {new WoodcuttingHatchet(ItemId.BRONZE_AXE, 1, 879),
          new WoodcuttingHatchet(ItemId.IRON_AXE, 1, 877),
          new WoodcuttingHatchet(ItemId.STEEL_AXE, 6, 875),
          new WoodcuttingHatchet(ItemId.MITHRIL_AXE, 21, 871),
          new WoodcuttingHatchet(ItemId.ADAMANT_AXE, 31, 869),
          new WoodcuttingHatchet(ItemId.RUNE_AXE, 41, 867),
          new WoodcuttingHatchet(ItemId.GILDED_AXE, 41, 8303),
          new WoodcuttingHatchet(ItemId.DRAGON_AXE, 61, 2846),
          new WoodcuttingHatchet(ItemId._3RD_AGE_AXE, 61, 7264),
          new WoodcuttingHatchet(ItemId.INFERNAL_AXE, 61, 2117),
          new WoodcuttingHatchet(ItemId.INFERNAL_AXE_UNCHARGED, 61, 2117)};
  private static final Item[] COLORED_EGG_NESTS = {new Item(ItemId.BIRD_NEST),
      new Item(ItemId.BIRD_NEST_5071), new Item(ItemId.BIRD_NEST_5072)};
  private static final Item[] EVIL_CHICKEN_OUTFIT =
      {new Item(ItemId.EVIL_CHICKEN_FEET), new Item(ItemId.EVIL_CHICKEN_WINGS),
          new Item(ItemId.EVIL_CHICKEN_HEAD), new Item(ItemId.EVIL_CHICKEN_LEGS)};

  private static List<SkillEntry> entries = new ArrayList<>();

  @Override
  public int getSkillId() {
    return Skills.WOODCUTTING;
  }

  @Override
  public List<SkillEntry> getEntries() {
    return entries;
  }

  @Override
  public Object script(String name, Object... args) {
    if (name.equals("farming_resources")) {
      return 8;
    } else if (name.equals("animation")) {
      var hatchet = getHatchet((Player) args[0]);
      return hatchet != null ? hatchet.getAnimation() : -1;
    }
    return null;
  }

  @Override
  public int getEventTick(Player player, PEvent event, Npc npc, MapObject mapObject,
      SkillEntry entry) {
    return 5;
  }

  @Override
  public void eventStarted(Player player, PEvent event, Npc npc, MapObject mapObject,
      SkillEntry entry) {
    player.getGameEncoder().sendMessage("You swing your axe at the tree.");
  }

  @Override
  public void eventStopped(Player player, PEvent event, Npc npc, MapObject mapObject,
      SkillEntry entry) {
    player.setAnimation(-1);
  }

  @Override
  public void actionSuccess(Player player, PEvent event, Npc npc, MapObject mapObject,
      SkillEntry entry) {
    setTemporaryMapObject(player, mapObject, entry);
    var nestChance = 128;
    if (player.getEquipment().wearingAccomplishmentCape(getSkillId())) {
      nestChance /= 1.1;
    }
    if (PRandom.randomE(nestChance - entry.getLevel()) == 0) {
      player.getController().addMapItem(PRandom.arrayRandom(COLORED_EGG_NESTS), player, player);
      player.getGameEncoder().sendMessage("<col=ff0000>A bird's nest falls out of the tree.</col>");
    }
  }

  @Override
  public void clueRolled(Player player, Npc npc, MapObject mapObject, SkillEntry entry) {
    var clueId = ItemId.CLUE_NEST_EASY;
    if (PRandom.randomE(100) < 10) {
      clueId = ItemId.CLUE_NEST_ELITE;
    } else if (PRandom.randomE(100) < 20) {
      clueId = ItemId.CLUE_NEST_HARD;
    } else if (PRandom.randomE(100) < 30) {
      clueId = ItemId.CLUE_NEST_MEDIUM;
    }
    player.getInventory().addOrDropItem(clueId);
  }

  @Override
  public Item createHook(Player player, Item item, Npc npc, MapObject mapObject, SkillEntry entry) {
    var fireContainer = SkillContainer.get(Skills.FIREMAKING);
    var fireEntry = fireContainer.findEntryFromConsume(item.getId());
    if (getHatchet(player).getItemId() == ItemId.INFERNAL_AXE && fireEntry != null
        && PRandom.randomE(3) == 0) {
      var fireXp = fireContainer.experienceHook(player, fireEntry.getExperience(), npc, mapObject,
          fireEntry);
      player.setGraphic(86, 100);
      player.getSkills().addXp(Skills.FIREMAKING, fireXp / 2);
      player.getCharges().degradeItem(ItemId.INFERNAL_AXE);
      fireContainer.rollPet(player, fireEntry);
      if (PRandom.randomE(160 - fireEntry.getLevel()) == 0) {
        player.getInventory().addOrDropItem(ItemId.SUPPLY_CRATE);
      }
      return null;
    }
    player.getGameEncoder().sendMessage("You get some " + item.getDef().getLCName() + ".");
    return item;
  }

  @Override
  public int experienceHook(Player player, int experience, Npc npc, MapObject mapObject,
      SkillEntry entry) {
    if (player.getEquipment().wearingLumberjackOutfit()) {
      experience *= 1.1;
    }
    return experience;
  }

  @Override
  public int animationHook(Player player, int animation, Npc npc, MapObject mapObject,
      SkillEntry entry) {
    return getHatchet(player).getAnimation();
  }

  @Override
  public boolean canDoActionHook(Player player, PEvent event, Npc npc, MapObject mapObject,
      SkillEntry entry) {
    if (getHatchet(player) == null) {
      player.getGameEncoder().sendMessage("You need an axe to do this.");
      return false;
    }
    return true;
  }

  @Override
  public boolean skipActionHook(Player player, PEvent event, Npc npc, MapObject mapObject,
      SkillEntry entry) {
    var power =
        (player.getSkills().getLevel(getSkillId()) / 2) + (getHatchet(player).getLevel() / 2) + 8;
    var failure = entry.getLevel() + 2;
    if (player.inWildernessResourceArea()) {
      failure /= 2;
    }
    if (inWoodcuttingGuild(player)) {
      power += 7;
    }
    var chance = 0.0;
    if (power > failure) {
      chance = 1 - (failure + 2) / ((power + 1) * 2.0);
    } else {
      chance = power / ((failure + 1) * 2.0);
    }
    if (player.getEquipment().wearingLumberjackOutfit()) {
      chance = Math.min(PNumber.addDoubles(chance, 0.1), 1.0);
    }
    if (player.isPremiumMember()) {
      chance = Math.min(PNumber.addDoubles(chance, 0.05), 1.0);
    }
    if (player.hasVoted()) {
      chance = Math.min(PNumber.addDoubles(chance, 0.05), 1.0);
    }
    if (player.getController().inWilderness()) {
      chance = Math.min(PNumber.addDoubles(chance, 0.1), 1.0);
    }
    return PRandom.randomI(100) < Math.max(0.01, 1 - chance) * 100;
  }

  @Override
  public int clueChanceHook(Player player, int chance, Npc npc, MapObject mapObject,
      SkillEntry entry) {
    if (player.getEquipment().wearingLumberjackOutfit()) {
      return (int) (chance / 1.05);
    }
    return chance;
  }

  @Override
  public boolean mapObjectOptionHook(Player player, int index, MapObject mapObject) {
    switch (mapObject.getId()) {
      case 29088: // Woodcutting Guild shrine
        var eggCount = player.getInventory().getCount(ItemId.BIRDS_EGG)
            + player.getInventory().getCount(ItemId.BIRDS_EGG_5077)
            + player.getInventory().getCount(ItemId.BIRDS_EGG_5078);
        if (eggCount == 0) {
          player.getGameEncoder().sendMessage("You have no eggs to exchange.");
          return true;
        }
        player.getInventory().deleteItem(ItemId.BIRDS_EGG, Item.MAX_AMOUNT);
        player.getInventory().deleteItem(ItemId.BIRDS_EGG_5077, Item.MAX_AMOUNT);
        player.getInventory().deleteItem(ItemId.BIRDS_EGG_5078, Item.MAX_AMOUNT);
        player.getInventory().addOrDropItem(ItemId.COINS, 250_000 * eggCount);
        player.getInventory().addOrDropItem(ItemId.BIRD_NEST_5073, 1 * eggCount);
        if (PRandom.randomE(132 / eggCount) == 0) {
          if (!player.hasItem(ItemId.EVIL_CHICKEN_FEET)) {
            player.getInventory().addOrDropItem(ItemId.EVIL_CHICKEN_FEET);
          } else if (!player.hasItem(ItemId.EVIL_CHICKEN_WINGS)) {
            player.getInventory().addOrDropItem(ItemId.EVIL_CHICKEN_WINGS);
          } else if (!player.hasItem(ItemId.EVIL_CHICKEN_HEAD)) {
            player.getInventory().addOrDropItem(ItemId.EVIL_CHICKEN_HEAD);
          } else if (!player.hasItem(ItemId.EVIL_CHICKEN_LEGS)) {
            player.getInventory().addOrDropItem(ItemId.EVIL_CHICKEN_LEGS);
          } else {
            player.getInventory().addOrDropItem(PRandom.arrayRandom(EVIL_CHICKEN_OUTFIT));
          }
        }
        return true;
    }
    return false;
  }

  private boolean inWoodcuttingGuild(Tile tile) {
    return tile.getX() >= 1607 && tile.getY() >= 3487 && tile.getX() <= 1657 && tile.getY() <= 3518
        || tile.getX() >= 1563 && tile.getY() >= 3472 && tile.getX() <= 1600 && tile.getY() <= 3503;
  }

  private WoodcuttingHatchet getHatchet(Player player) {
    for (var i = HATCHETS.length - 1; i >= 0; i--) {
      var hatchet = HATCHETS[i];
      if (!player.carryingItem(hatchet.getItemId())) {
        continue;
      }
      if (player.getSkills().getLevel(Skills.WOODCUTTING) < hatchet.getLevel()) {
        continue;
      }
      return hatchet;
    }
    return null;
  }

  static {
    entries.add(SkillEntry.builder().level(1).experience(25).mapObject(new SkillModel(1276, 0))
        .mapObject(new SkillModel(1277, 0)).mapObject(new SkillModel(1278, 0))
        .mapObject(new SkillModel(1279, 0)).mapObject(new SkillModel(1280, 0))
        .mapObject(new SkillModel(1282, 0)).mapObject(new SkillModel(1283, 0))
        .mapObject(new SkillModel(1284, 0)).mapObject(new SkillModel(1285, 0))
        .mapObject(new SkillModel(1286, 0)).mapObject(new SkillModel(1289, 0))
        .mapObject(new SkillModel(1290, 0)).mapObject(new SkillModel(1291, 0))
        .create(new RandomItem(ItemId.LOGS)).pet(new SkillPet(ItemId.BEAVER, 317647))
        .clueChance(317647).temporaryMapObject(new SkillTemporaryMapObject(1342, 1, 8)).build());
    entries.add(SkillEntry.builder().level(15).experience(38).mapObject(new SkillModel(1751, 0))
        .create(new RandomItem(ItemId.OAK_LOGS)).pet(new SkillPet(ItemId.BEAVER, 361146))
        .clueChance(361146).temporaryMapObject(new SkillTemporaryMapObject(1342, 3, 8)).build());
    entries.add(SkillEntry.builder().level(30).experience(68).mapObject(new SkillModel(1750, 0))
        .mapObject(new SkillModel(1756, 0)).mapObject(new SkillModel(1758, 0))
        .mapObject(new SkillModel(1760, 0)).create(new RandomItem(ItemId.WILLOW_LOGS))
        .pet(new SkillPet(ItemId.BEAVER, 289286)).clueChance(289286)
        .temporaryMapObject(new SkillTemporaryMapObject(1342, 7, 8)).build());
    entries.add(SkillEntry.builder().level(45).experience(100).mapObject(new SkillModel(1759, 0))
        .create(new RandomItem(ItemId.MAPLE_LOGS)).pet(new SkillPet(ItemId.BEAVER, 221918))
        .clueChance(221918).temporaryMapObject(new SkillTemporaryMapObject(1342, 11, 8)).build());
    entries.add(SkillEntry.builder().level(60).experience(175).mapObject(new SkillModel(1753, 0))
        .create(new RandomItem(ItemId.YEW_LOGS)).pet(new SkillPet(ItemId.BEAVER, 145013))
        .clueChance(145013).temporaryMapObject(new SkillTemporaryMapObject(1342, 15, 8)).build());
    entries.add(SkillEntry.builder().level(75).experience(250).mapObject(new SkillModel(1761, 0))
        .create(new RandomItem(ItemId.MAGIC_LOGS)).pet(new SkillPet(ItemId.BEAVER, 72321))
        .clueChance(72321).temporaryMapObject(new SkillTemporaryMapObject(1342, 18, 8)).build());
    entries.add(SkillEntry.builder().level(90).experience(380).mapObject(new SkillModel(29668, 0))
        .mapObject(new SkillModel(29670, 0)).create(new RandomItem(ItemId.REDWOOD_LOGS))
        .pet(new SkillPet(ItemId.BEAVER, 72321)).clueChance(72321).build());
    entries.add(SkillEntry.builder().level(1).experience(45).mapObject(new SkillModel(29763, 0))
        .create(new RandomItem(ItemId.KINDLING_20799)).build());
  }
}


@Getter
class WoodcuttingHatchet {
  private int itemId;
  private int level;
  private int animation;

  public WoodcuttingHatchet(int itemId, int level, int animation) {
    this.itemId = itemId;
    this.level = level;
    this.animation = animation;
  }
}
