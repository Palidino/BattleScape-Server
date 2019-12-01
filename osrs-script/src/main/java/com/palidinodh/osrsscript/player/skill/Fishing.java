package com.palidinodh.osrsscript.player.skill;

import java.util.ArrayList;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.Graphic;
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
import com.palidinodh.random.PRandom;
import com.palidinodh.util.PEvent;
import com.palidinodh.util.PNumber;
import lombok.var;

class Fishing extends SkillContainer {
  private static final int TOTAL_RESOURCES = 30;
  private static final Item DRAGON_HARPOON = new Item(ItemId.DRAGON_HARPOON);
  private static final Item INFERNAL_HARPOON = new Item(ItemId.INFERNAL_HARPOON);
  private static final Item UNCHARGED_INFERNAL_HARPOON =
      new Item(ItemId.INFERNAL_HARPOON_UNCHARGED);
  private static final Graphic FLYING_FISH_GRAPHIC = new Graphic(1387);
  private static final int FLYING_FISH_ATTACHMENT = 0;

  private static List<SkillEntry> entries = new ArrayList<>();

  @Override
  public int getSkillId() {
    return Skills.FISHING;
  }

  @Override
  public List<SkillEntry> getEntries() {
    return entries;
  }

  @Override
  public int getEventTick(Player player, PEvent event, Npc npc, MapObject mapObject,
      SkillEntry entry) {
    return entry.getCreate().getId() == ItemId.MINNOW ? 1 : 5;
  }

  @Override
  public void eventStarted(Player player, PEvent event, Npc npc, MapObject mapObject,
      SkillEntry entry) {
    player.getGameEncoder().sendMessage("You attempt to catch a fish.");
    player.setFaceEntity(npc);
  }

  @Override
  public void eventStopped(Player player, PEvent event, Npc npc, MapObject mapObject,
      SkillEntry entry) {
    player.setAnimation(-1);
  }

  @Override
  public void actionSuccess(Player player, PEvent event, Npc npc, MapObject mapObject,
      SkillEntry entry) {
    if (entry.getCreate().getId() == ItemId.MINNOW && PRandom.randomE(64) == 0) {
      npc.setGraphic(FLYING_FISH_GRAPHIC);
      event.setAttachment(FLYING_FISH_ATTACHMENT, true);
    }
    if (npc.getId() == NpcId.FISHING_SPOT_7730 || npc.getId() == NpcId.FISHING_SPOT_7731
        || npc.getId() == NpcId.FISHING_SPOT_7732 || npc.getId() == NpcId.FISHING_SPOT_7733) {
      return;
    }
    if (npc.getMaxHitpoints() == 0) {
      npc.setMaxHitpoints(TOTAL_RESOURCES);
      npc.setHitpoints(TOTAL_RESOURCES);
    }
    npc.adjustHitpoints(-1);
    if (entry.getCreate().getId() == ItemId.LEAPING_STURGEON) {
      player.getSkills().addXp(Skills.STRENGTH, 7);
      var agilityExperience = 7;
      if (player.getEquipment().wearingMinimumGraceful()) {
        agilityExperience *= 1.1;
      }
      player.getSkills().addXp(Skills.AGILITY, agilityExperience);
    }
  }

  @Override
  public void clueRolled(Player player, Npc npc, MapObject mapObject, SkillEntry entry) {
    var clueId = ItemId.CLUE_BOTTLE_EASY;
    if (PRandom
        .randomE((int) (100 / player.getCombat().getDropRateMultiplier(clueId, null))) < 10) {
      clueId = ItemId.CLUE_BOTTLE_ELITE;
    } else if (PRandom
        .randomE((int) (100 / player.getCombat().getDropRateMultiplier(clueId, null))) < 20) {
      clueId = ItemId.CLUE_BOTTLE_HARD;
    } else if (PRandom
        .randomE((int) (100 / player.getCombat().getDropRateMultiplier(clueId, null))) < 30) {
      clueId = ItemId.CLUE_BOTTLE_MEDIUM;
    }
    player.getInventory().addOrDropItem(clueId);
  }

  @Override
  public Item createHook(Player player, Item item, Npc npc, MapObject mapObject, SkillEntry entry) {
    var unusualChance = 128;
    if (entry.getCreate().getId() == ItemId.MINNOW) {
      unusualChance *= 5;
    }
    if (PRandom.randomE(unusualChance - entry.getLevel()) == 0) {
      player.getGameEncoder().sendMessage("<col=ff0000>You catch an unusual fish.</col>");
      return new Item(ItemId.UNUSUAL_FISH);
    }
    var cookingContainer = SkillContainer.getBySkillId(Skills.COOKING);
    var cookEntry = cookingContainer.findEntryFromConsume(item.getId());
    if (usingInfernalHarpoon(player, entry) && cookEntry != null && PRandom.randomE(3) == 0) {
      var cookXp = cookingContainer.experienceHook(player, cookEntry.getExperience(), npc,
          mapObject, cookEntry);
      player.setGraphic(86, 100);
      player.getSkills().addXp(Skills.COOKING, cookXp / 2);
      player.getCharges().degradeItem(ItemId.INFERNAL_HARPOON);
      return null;
    }
    var createName = item.getName().replace("Raw ", "");
    if (createName.endsWith("s")) {
      player.getGameEncoder().sendFilteredMessage("You catch some " + createName + ".");
    } else if (item.getAmount() > 1) {
      player.getGameEncoder().sendFilteredMessage("You catch some " + createName + "s.");
    } else {
      player.getGameEncoder().sendFilteredMessage("You catch a " + createName + ".");
    }
    return item;
  }

  @Override
  public int experienceHook(Player player, int experience, Npc npc, MapObject mapObject,
      SkillEntry entry) {
    if (player.getEquipment().wearingAnglerOutfit()) {
      experience *= 1.1;
    }
    return experience;
  }

  @Override
  public int animationHook(Player player, int animation, Npc npc, MapObject mapObject,
      SkillEntry entry) {
    if (entry.getTool().getId() == ItemId.HARPOON) {
      if (usingInfernalHarpoon(player, entry) || usingUnchargedInfernalHarpoon(player, entry)) {
        return 7402;
      } else if (usingDragonHarpoon(player, entry)) {
        return 7401;
      }
    }
    return animation;
  }

  @Override
  public Item toolHook(Player player, Item tool, Npc npc, MapObject mapObject, SkillEntry entry) {
    if (tool.getId() == ItemId.HARPOON) {
      if (usingInfernalHarpoon(player, entry)) {
        return INFERNAL_HARPOON;
      } else if (usingUnchargedInfernalHarpoon(player, entry)) {
        return UNCHARGED_INFERNAL_HARPOON;
      } else if (usingDragonHarpoon(player, entry)) {
        return DRAGON_HARPOON;
      }
    }
    return tool;
  }

  @Override
  public boolean canDoActionHook(Player player, PEvent event, Npc npc, MapObject mapObject,
      SkillEntry entry) {
    if (entry.getCreate() != null && entry.getCreate().getId() == ItemId.INFERNAL_EEL
        && player.getEquipment().getHandId() != ItemId.ICE_GLOVES) {
      player.getGameEncoder().sendMessage("You need to be wearing ice gloves to fish here.");
      return false;
    }
    return true;
  }

  @Override
  public boolean skipActionHook(Player player, PEvent event, Npc npc, MapObject mapObject,
      SkillEntry entry) {
    if (event != null && Boolean.TRUE.equals(event.getAttachment(FLYING_FISH_ATTACHMENT))) {
      npc.setGraphic(FLYING_FISH_GRAPHIC);
      player.getInventory().deleteItem(entry.getCreate().getId(), 16 + PRandom.randomI(10));
      return true;
    }
    var power = player.getSkills().getLevel(getSkillId()) + 8;
    var failure = entry.getLevel() + 2;
    if (player.inWildernessResourceArea()) {
      failure /= 2;
    }
    if (inFishingGuild(player)) {
      power += 7;
    }
    var chance = 0.0;
    if (power > failure) {
      chance = 1 - (failure + 2) / ((power + 1) * 2.0);
    } else {
      chance = power / ((failure + 1) * 2.0);
    }
    if (player.getEquipment().wearingAnglerOutfit()) {
      chance = Math.min(PNumber.addDoubles(chance, 0.1), 1.0);
    }
    if (usingDragonHarpoon(player, entry) || usingInfernalHarpoon(player, entry)
        || usingUnchargedInfernalHarpoon(player, entry)) {
      chance = Math.min(PNumber.addDoubles(chance, 0.2), 1.0);
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
    if (player.getEquipment().wearingAnglerOutfit()) {
      return (int) (chance / 1.05);
    }
    return chance;
  }

  @Override
  public boolean npcOptionHook(Player player, int option, Npc npc) {
    switch (npc.getId()) {
      case NpcId.ROACHEY:
        player.openShop("skilling");
        return true;
    }
    return false;
  }

  private boolean usingDragonHarpoon(Player player, SkillEntry entry) {
    return player.getSkills().getLevel(getSkillId()) >= 61 && entry.usesTool(ItemId.HARPOON)
        && player.carryingItem(ItemId.DRAGON_HARPOON);
  }

  private boolean usingInfernalHarpoon(Player player, SkillEntry entry) {
    return player.getSkills().getLevel(getSkillId()) >= 75 && entry.usesTool(ItemId.HARPOON)
        && player.carryingItem(ItemId.INFERNAL_HARPOON);
  }

  private boolean usingUnchargedInfernalHarpoon(Player player, SkillEntry entry) {
    return player.getSkills().getLevel(getSkillId()) >= 75 && entry.usesTool(ItemId.HARPOON)
        && player.carryingItem(ItemId.INFERNAL_HARPOON);
  }

  private boolean inFishingGuild(Tile tile) {
    return tile.getX() >= 2595 && tile.getX() <= 2622 && tile.getY() >= 3405 && tile.getY() <= 3446;
  }

  static {
    entries.add(SkillEntry.builder().level(1).experience(10).animation(621)
        .npc(new SkillModel(NpcId.FISHING_SPOT_1514, 0))
        .npc(new SkillModel(NpcId.FISHING_SPOT_1518, 0)).tool(new Item(ItemId.SMALL_FISHING_NET))
        .create(new RandomItem(ItemId.RAW_SHRIMPS)).pet(new SkillPet(ItemId.HERON, 435165))
        .clueChance(870330).build());
    entries.add(SkillEntry.builder().level(5).experience(20).animation(623)
        .npc(new SkillModel(NpcId.FISHING_SPOT_1514, 2)).tool(new Item(ItemId.FISHING_ROD))
        .consume(new RandomItem(ItemId.FISHING_BAIT)).create(new RandomItem(ItemId.RAW_SARDINE))
        .pet(new SkillPet(ItemId.HERON, 528000)).clueChance(1056000).build());
    entries.add(SkillEntry.builder().level(15).experience(40).animation(621)
        .npc(new SkillModel(NpcId.FISHING_SPOT_1518, 2)).tool(new Item(ItemId.SMALL_FISHING_NET))
        .create(new RandomItem(ItemId.RAW_ANCHOVIES)).pet(new SkillPet(ItemId.HERON, 435165))
        .clueChance(870330).build());
    entries.add(SkillEntry.builder().level(30).experience(70).animation(622)
        .npc(new SkillModel(NpcId.ROD_FISHING_SPOT, 0)).tool(new Item(ItemId.FLY_FISHING_ROD))
        .consume(new RandomItem(ItemId.FEATHER)).create(new RandomItem(ItemId.RAW_SALMON))
        .pet(new SkillPet(ItemId.HERON, 461808)).clueChance(923616).build());
    entries.add(SkillEntry.builder().level(25).experience(60).animation(623)
        .npc(new SkillModel(NpcId.ROD_FISHING_SPOT, 2)).tool(new Item(ItemId.FISHING_ROD))
        .consume(new RandomItem(ItemId.FISHING_BAIT)).create(new RandomItem(ItemId.RAW_PIKE))
        .pet(new SkillPet(ItemId.HERON, 305792)).clueChance(305792).build());
    entries.add(SkillEntry.builder().level(40).experience(90).animation(619)
        .npc(new SkillModel(NpcId.FISHING_SPOT_1510, 0)).tool(new Item(ItemId.LOBSTER_POT))
        .create(new RandomItem(ItemId.RAW_LOBSTER)).pet(new SkillPet(ItemId.HERON, 116129))
        .clueChance(116129).build());
    entries.add(SkillEntry.builder().level(50).experience(100).animation(618)
        .npc(new SkillModel(NpcId.FISHING_SPOT_1510, 2)).tool(new Item(ItemId.HARPOON))
        .create(new RandomItem(ItemId.RAW_SWORDFISH)).pet(new SkillPet(ItemId.HERON, 128885))
        .clueChance(257770).build());
    entries.add(SkillEntry.builder().level(46).experience(100).animation(620)
        .npc(new SkillModel(NpcId.FISHING_SPOT_1511, 0)).tool(new Item(ItemId.BIG_FISHING_NET))
        .create(new RandomItem(ItemId.RAW_BASS)).pet(new SkillPet(ItemId.HERON, 128885))
        .clueChance(1147827).build());
    entries.add(SkillEntry.builder().level(76).experience(110).animation(618)
        .npc(new SkillModel(NpcId.FISHING_SPOT_1511, 2))
        .npc(new SkillModel(NpcId.FISHING_SPOT_1520, 2)).tool(new Item(ItemId.HARPOON))
        .create(new RandomItem(ItemId.RAW_SHARK)).pet(new SkillPet(ItemId.HERON, 82243))
        .clueChance(82243).build());
    entries.add(SkillEntry.builder().level(62).experience(120).animation(621)
        .npc(new SkillModel(NpcId.FISHING_SPOT_4316, 0)).tool(new Item(ItemId.SMALL_FISHING_NET))
        .create(new RandomItem(ItemId.RAW_MONKFISH)).pet(new SkillPet(ItemId.HERON, 138583))
        .clueChance(138583).build());
    entries.add(SkillEntry.builder().level(65).experience(105).animation(620)
        .npc(new SkillModel(NpcId.FISHING_SPOT_4712, 0)).tool(new Item(ItemId.BIG_FISHING_NET))
        .create(new RandomItem(ItemId.RAW_KARAMBWAN)).pet(new SkillPet(ItemId.HERON, 170874))
        .clueChance(170874).build());
    entries.add(SkillEntry.builder().level(85).experience(130).animation(619)
        .npc(new SkillModel(NpcId.FISHING_SPOT_1536, 0)).tool(new Item(ItemId.LOBSTER_POT))
        .consume(new RandomItem(ItemId.DARK_FISHING_BAIT))
        .create(new RandomItem(ItemId.RAW_DARK_CRAB)).pet(new SkillPet(ItemId.HERON, 149434))
        .clueChance(149434).build());
    entries.add(SkillEntry.builder().level(82).experience(120).animation(623)
        .npc(new SkillModel(NpcId.ROD_FISHING_SPOT_6825, 0)).tool(new Item(ItemId.FISHING_ROD))
        .consume(new RandomItem(ItemId.SANDWORMS)).create(new RandomItem(ItemId.RAW_ANGLERFISH))
        .pet(new SkillPet(ItemId.HERON, 78649)).clueChance(78649).build());
    entries.add(SkillEntry.builder().level(82).experience(26).animation(621)
        .npc(new SkillModel(NpcId.FISHING_SPOT_7730, 0))
        .npc(new SkillModel(NpcId.FISHING_SPOT_7731, 0)).tool(new Item(ItemId.SMALL_FISHING_NET))
        .create(new RandomItem(ItemId.MINNOW, 10, 14)).pet(new SkillPet(ItemId.HERON, 977778))
        .build());
    entries.add(SkillEntry.builder().level(70).extraLevel(Skills.STRENGTH, 45)
        .extraLevel(Skills.AGILITY, 45).experience(80).animation(622)
        .npc(new SkillModel(NpcId.FISHING_SPOT_1542, 0)).tool(new Item(ItemId.BARBARIAN_ROD))
        .consume(new RandomItem(ItemId.FEATHER)).create(new RandomItem(ItemId.LEAPING_STURGEON))
        .pet(new SkillPet(ItemId.HERON, 426954)).clueChance(1147827).build());
    entries.add(SkillEntry.builder().level(80).experience(95).animation(622)
        .npc(new SkillModel(NpcId.ROD_FISHING_SPOT_7676, 0)).tool(new Item(ItemId.OILY_FISHING_ROD))
        .consume(new RandomItem(ItemId.FISHING_BAIT)).create(new RandomItem(ItemId.INFERNAL_EEL))
        .pet(new SkillPet(ItemId.HERON, 99000)).clueChance(99000).build());
    entries.add(SkillEntry.builder().level(87).experience(105).animation(622)
        .npc(new SkillModel(NpcId.FISHING_SPOT_6488, 0)).tool(new Item(ItemId.FISHING_ROD))
        .consume(new RandomItem(ItemId.FISHING_BAIT)).create(new RandomItem(ItemId.SACRED_EEL))
        .pet(new SkillPet(ItemId.HERON, 99000)).clueChance(99000).build());
  }
}
