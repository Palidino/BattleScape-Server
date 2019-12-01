package com.palidinodh.osrsscript.player.skill;

import java.util.ArrayList;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.ItemId;
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

class Mining extends SkillContainer {
  private static final MiningPickaxe[] PICKAXES =
      { new MiningPickaxe(ItemId.BRONZE_PICKAXE, 1, 625, 6753),
          new MiningPickaxe(ItemId.IRON_PICKAXE, 1, 626, 6754),
          new MiningPickaxe(ItemId.STEEL_PICKAXE, 6, 627, 6755),
          new MiningPickaxe(ItemId.BLACK_PICKAXE, 11, 625, 6753),
          new MiningPickaxe(ItemId.MITHRIL_PICKAXE, 21, 629, 6757),
          new MiningPickaxe(ItemId.ADAMANT_PICKAXE, 31, 628, 6756),
          new MiningPickaxe(ItemId.RUNE_PICKAXE, 41, 624, 6752),
          new MiningPickaxe(ItemId.GILDED_PICKAXE, 41, 8314, 8312),
          new MiningPickaxe(ItemId.DRAGON_PICKAXE, 61, 7139, 6758),
          new MiningPickaxe(ItemId.DRAGON_PICKAXE_12797, 61, 642, 335),
          new MiningPickaxe(ItemId._3RD_AGE_PICKAXE, 61, 7283, 7282),
          new MiningPickaxe(ItemId.INFERNAL_PICKAXE, 61, 4482, 4481),
          new MiningPickaxe(ItemId.INFERNAL_PICKAXE_UNCHARGED, 61, 4482, 4481) };
  public static final int UNIDENTIFIED_MINERAL_VARIABLE = 0;

  private static List<SkillEntry> entries = new ArrayList<>();

  @Override
  public int getSkillId() {
    return Skills.MINING;
  }

  @Override
  public List<SkillEntry> getEntries() {
    return entries;
  }

  @Override
  public int getEventTick(Player player, PEvent event, Npc npc, MapObject mapObject,
      SkillEntry entry) {
    return 5;
  }

  @Override
  public void eventStarted(Player player, PEvent event, Npc npc, MapObject mapObject,
      SkillEntry entry) {
    player.getGameEncoder().sendMessage("You swing your pickaxe at the rock.");
  }

  @Override
  public void eventStopped(Player player, PEvent event, Npc npc, MapObject mapObject,
      SkillEntry entry) {
    player.setAnimation(-1);
  }

  @Override
  public void actionSuccess(Player player, PEvent event, Npc npc, MapObject mapObject,
      SkillEntry entry) {
    double moreResourcesChance = 0;
    if (entry.getCreate() != null && entry.getCreate().getId() != ItemId.RUNITE_ORE
        && entry.getCreate().getId() != ItemId.AMETHYST
        && player.getEquipment().wearingAccomplishmentCape(getSkillId())) {
      moreResourcesChance = PNumber.addDoubles(moreResourcesChance, 5);
    }
    if (entry.getCreate() != null && (player.getEquipment().getHandId() == ItemId.MINING_GLOVES
        || player.getEquipment().getHandId() == ItemId.EXPERT_MINING_GLOVES)) {
      if (entry.getCreate().getId() == ItemId.SILVER_ORE) {
        moreResourcesChance = PNumber.addDoubles(moreResourcesChance, 50);
      } else if (entry.getCreate().getId() == ItemId.COAL) {
        moreResourcesChance = PNumber.addDoubles(moreResourcesChance, 40);
      } else if (entry.getCreate().getId() == ItemId.GOLD_ORE) {
        moreResourcesChance = PNumber.addDoubles(moreResourcesChance, 33.33);
      }
    }
    if (entry.getCreate() != null
        && (player.getEquipment().getHandId() == ItemId.SUPERIOR_MINING_GLOVES
            || player.getEquipment().getHandId() == ItemId.EXPERT_MINING_GLOVES)) {
      if (entry.getCreate().getId() == ItemId.MITHRIL_ORE) {
        moreResourcesChance = PNumber.addDoubles(moreResourcesChance, 25);
      } else if (entry.getCreate().getId() == ItemId.ADAMANTITE_ORE) {
        moreResourcesChance = PNumber.addDoubles(moreResourcesChance, 16.66);
      } else if (entry.getCreate().getId() == ItemId.RUNITE_ORE) {
        moreResourcesChance = PNumber.addDoubles(moreResourcesChance, 12.5);
      }
    }
    if (moreResourcesChance <= 0 || !PRandom.inRange(moreResourcesChance)) {
      setTemporaryMapObject(player, mapObject, entry);
    }
  }

  @Override
  public void clueRolled(Player player, Npc npc, MapObject mapObject, SkillEntry entry) {
    var clueId = ItemId.CLUE_GEODE_EASY;
    if (PRandom
        .randomE((int) (100 / player.getCombat().getDropRateMultiplier(clueId, null))) < 10) {
      clueId = ItemId.CLUE_GEODE_ELITE;
    } else if (PRandom
        .randomE((int) (100 / player.getCombat().getDropRateMultiplier(clueId, null))) < 20) {
      clueId = ItemId.CLUE_GEODE_HARD;
    } else if (PRandom
        .randomE((int) (100 / player.getCombat().getDropRateMultiplier(clueId, null))) < 30) {
      clueId = ItemId.CLUE_GEODE_MEDIUM;
    }
    player.getInventory().addOrDropItem(clueId);
  }

  @Override
  public Item createHook(Player player, Item item, Npc npc, MapObject mapObject, SkillEntry entry) {
    if (entry.getCreate() != null && entry.getCreate().getId() == ItemId.UNCUT_OPAL) {
      int createId;
      if (PRandom.inRange(player.getCombat().getDropRate(ItemId.UNCUT_ONYX, 0.0025))) {
        createId = ItemId.ZENYTE_SHARD;
      } else if (PRandom.inRange(player.getCombat().getDropRate(ItemId.UNCUT_ONYX, 0.005))) {
        createId = ItemId.UNCUT_ONYX;
      } else if (PRandom.inRange(player.getCombat().getDropRate(ItemId.UNCUT_ONYX, 0.01))) {
        createId = ItemId.UNCUT_DRAGONSTONE;
      } else if (PRandom.randomE(128) < 4) {
        createId = ItemId.UNCUT_DIAMOND;
      } else if (PRandom.randomE(128) < 5) {
        createId = ItemId.UNCUT_RUBY;
      } else if (PRandom.randomE(128) < 5) {
        createId = ItemId.UNCUT_EMERALD;
      } else if (PRandom.randomE(128) < 9) {
        createId = ItemId.UNCUT_SAPPHIRE;
      } else if (PRandom.randomE(128) < 15) {
        createId = ItemId.UNCUT_RED_TOPAZ;
      } else if (PRandom.randomE(128) < 30) {
        createId = ItemId.UNCUT_JADE;
      } else {
        createId = ItemId.UNCUT_OPAL;
      }
      return new Item(createId, item.getAmount());
    }
    if (entry.getCreate() != null && entry.getCreate().getId() == ItemId.VOLCANIC_ASH) {
      return new Item(PRandom.randomE(32) == 0 ? ItemId.SODA_ASH : ItemId.VOLCANIC_ASH,
          item.getAmount());
    }
    MiningPickaxe pickaxe = getPickaxe(player);
    int smithingXp = 0;
    if (entry.getCreate() != null && pickaxe.getItemId() == ItemId.INFERNAL_PICKAXE
        && PRandom.randomE(3) == 0) {
      switch (entry.getCreate().getId()) {
        case ItemId.GOLD_ORE:
          smithingXp = 9;
          break;
        case ItemId.SANDSTONE_1KG:
        case ItemId.SANDSTONE_2KG:
        case ItemId.SANDSTONE_5KG:
        case ItemId.SANDSTONE_10KG:
        case ItemId.GRANITE_500G:
        case ItemId.GRANITE_2KG:
        case ItemId.GRANITE_5KG:
          smithingXp = 6 + PRandom.randomI(15);
          break;
      }
    }
    if (smithingXp != 0) {
      player.setGraphic(86, 100);
      player.getSkills().addXp(Skills.SMITHING, smithingXp);
      player.getCharges().degradeItem(pickaxe.getItemId());
      return null;
    }
    if (entry.getVariable(UNIDENTIFIED_MINERAL_VARIABLE) > 0
        && PRandom.randomE(entry.getVariable(UNIDENTIFIED_MINERAL_VARIABLE)) == 0) {
      player.getInventory().addOrDropItem(ItemId.UNIDENTIFIED_MINERALS, 1);
    }
    return item;
  }

  @Override
  public int experienceHook(Player player, int experience, Npc npc, MapObject mapObject,
      SkillEntry entry) {
    if (player.getEquipment().wearingProspectorOutfit()) {
      experience *= 1.1;
    }
    return experience;
  }

  @Override
  public int animationHook(Player player, int animation, Npc npc, MapObject mapObject,
      SkillEntry entry) {
    if (entry.getCreate() != null && entry.getCreate().getId() == ItemId.AMETHYST) {
      return getPickaxe(player).getWallAnimation();
    }
    return getPickaxe(player).getAnimation();
  }

  @Override
  public boolean canDoActionHook(Player player, PEvent event, Npc npc, MapObject mapObject,
      SkillEntry entry) {
    if (getPickaxe(player) == null) {
      player.getGameEncoder().sendMessage("You need a pickaxe to do this.");
      return false;
    }
    return true;
  }

  @Override
  public boolean skipActionHook(Player player, PEvent event, Npc npc, MapObject mapObject,
      SkillEntry entry) {
    var power =
        (player.getSkills().getLevel(getSkillId()) / 2) + (getPickaxe(player).getLevel() / 2) + 8;
    var failure = entry.getLevel() + 2;
    if (player.inWildernessResourceArea()) {
      failure /= 2;
    }
    if (inMembersMiningGuild(player)) {
      power += 7;
    }
    var chance = 0.0;
    if (power > failure) {
      chance = 1 - (failure + 2) / ((power + 1) * 2.0);
    } else {
      chance = power / ((failure + 1) * 2.0);
    }
    if (player.getEquipment().wearingProspectorOutfit()) {
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

  public boolean inMembersMiningGuild(Player player) {
    return player.getX() >= 3009 && player.getY() >= 9698 && player.getX() <= 3058
        && player.getY() <= 9729;
  }

  private MiningPickaxe getPickaxe(Player player) {
    for (var i = PICKAXES.length - 1; i >= 0; i--) {
      var pickaxe = PICKAXES[i];
      if (!player.carryingItem(pickaxe.getItemId())) {
        continue;
      }
      if (player.getSkills().getLevel(Skills.MINING) < pickaxe.getLevel()) {
        continue;
      }
      return pickaxe;
    }
    return null;
  }

  static {
    entries.add(SkillEntry.builder().level(1).experience(17).mapObject(new SkillModel(7453, 0))
        .mapObject(new SkillModel(7484, 0)).create(new RandomItem(ItemId.COPPER_ORE))
        .pet(new SkillPet(ItemId.ROCK_GOLEM, 741600)).clueChance(741600)
        .temporaryMapObject(new SkillTemporaryMapObject(7469, 1, 4))
        .temporaryMapObject(new SkillTemporaryMapObject(7468, 1, 4))
        .variable(UNIDENTIFIED_MINERAL_VARIABLE, 200).build());
    entries.add(SkillEntry.builder().level(1).experience(17).mapObject(new SkillModel(7485, 0))
        .mapObject(new SkillModel(7486, 0)).create(new RandomItem(ItemId.TIN_ORE))
        .pet(new SkillPet(ItemId.ROCK_GOLEM, 741600)).clueChance(741600)
        .temporaryMapObject(new SkillTemporaryMapObject(7468, 1, 4))
        .temporaryMapObject(new SkillTemporaryMapObject(7469, 1, 4))
        .variable(UNIDENTIFIED_MINERAL_VARIABLE, 200).build());
    entries.add(SkillEntry.builder().level(15).experience(35).mapObject(new SkillModel(7455, 0))
        .mapObject(new SkillModel(7488, 0)).create(new RandomItem(ItemId.IRON_ORE))
        .pet(new SkillPet(ItemId.ROCK_GOLEM, 741600)).clueChance(741600)
        .temporaryMapObject(new SkillTemporaryMapObject(7469, 3, 4))
        .temporaryMapObject(new SkillTemporaryMapObject(7468, 3, 4))
        .variable(UNIDENTIFIED_MINERAL_VARIABLE, 100).build());
    entries.add(SkillEntry.builder().level(30).experience(50).mapObject(new SkillModel(7456, 0))
        .mapObject(new SkillModel(7489, 0)).create(new RandomItem(ItemId.COAL))
        .pet(new SkillPet(ItemId.ROCK_GOLEM, 290640)).clueChance(290640)
        .temporaryMapObject(new SkillTemporaryMapObject(7469, 7, 4))
        .temporaryMapObject(new SkillTemporaryMapObject(7468, 7, 4))
        .variable(UNIDENTIFIED_MINERAL_VARIABLE, 60).build());
    entries.add(SkillEntry.builder().level(40).experience(65).mapObject(new SkillModel(7458, 0))
        .mapObject(new SkillModel(7491, 0)).create(new RandomItem(ItemId.GOLD_ORE))
        .pet(new SkillPet(ItemId.ROCK_GOLEM, 290640)).clueChance(290640)
        .temporaryMapObject(new SkillTemporaryMapObject(7469, 10, 4))
        .temporaryMapObject(new SkillTemporaryMapObject(7468, 10, 4))
        .variable(UNIDENTIFIED_MINERAL_VARIABLE, 50).build());
    entries.add(SkillEntry.builder().level(40).experience(65).mapObject(new SkillModel(7463, 0))
        .mapObject(new SkillModel(7464, 0)).create(new RandomItem(ItemId.UNCUT_OPAL))
        .pet(new SkillPet(ItemId.ROCK_GOLEM, 211886)).clueChance(211886)
        .temporaryMapObject(new SkillTemporaryMapObject(7469, 10, 4))
        .temporaryMapObject(new SkillTemporaryMapObject(7468, 10, 4))
        .variable(UNIDENTIFIED_MINERAL_VARIABLE, 50).build());
    entries.add(SkillEntry.builder().level(55).experience(80).mapObject(new SkillModel(7459, 0))
        .mapObject(new SkillModel(7492, 0)).create(new RandomItem(ItemId.MITHRIL_ORE))
        .pet(new SkillPet(ItemId.ROCK_GOLEM, 148320)).clueChance(148320)
        .temporaryMapObject(new SkillTemporaryMapObject(7469, 13, 4))
        .temporaryMapObject(new SkillTemporaryMapObject(7468, 13, 4))
        .variable(UNIDENTIFIED_MINERAL_VARIABLE, 40).build());
    entries.add(SkillEntry.builder().level(70).experience(95).mapObject(new SkillModel(7460, 0))
        .mapObject(new SkillModel(7493, 0)).create(new RandomItem(ItemId.ADAMANTITE_ORE))
        .pet(new SkillPet(ItemId.ROCK_GOLEM, 59328)).clueChance(59328)
        .temporaryMapObject(new SkillTemporaryMapObject(7469, 17, 4))
        .temporaryMapObject(new SkillTemporaryMapObject(7468, 17, 4))
        .variable(UNIDENTIFIED_MINERAL_VARIABLE, 30).build());
    entries.add(SkillEntry.builder().level(85).experience(125).mapObject(new SkillModel(7461, 0))
        .mapObject(new SkillModel(7494, 0)).create(new RandomItem(ItemId.RUNITE_ORE))
        .pet(new SkillPet(ItemId.ROCK_GOLEM, 42377)).clueChance(42377)
        .temporaryMapObject(new SkillTemporaryMapObject(7469, 21, 4))
        .temporaryMapObject(new SkillTemporaryMapObject(7468, 21, 4))
        .variable(UNIDENTIFIED_MINERAL_VARIABLE, 20).build());
    entries.add(SkillEntry.builder().level(92).experience(240).mapObject(new SkillModel(30371, 0))
        .mapObject(new SkillModel(30372, 0)).create(new RandomItem(ItemId.AMETHYST))
        .pet(new SkillPet(ItemId.ROCK_GOLEM, 46350)).clueChance(46350)
        .temporaryMapObject(new SkillTemporaryMapObject(30373, 23, 4))
        .variable(UNIDENTIFIED_MINERAL_VARIABLE, 20).build());
    entries.add(SkillEntry.builder().level(22).experience(10).mapObject(new SkillModel(30985, 0))
        .create(new RandomItem(ItemId.VOLCANIC_ASH)).pet(new SkillPet(ItemId.ROCK_GOLEM, 741600))
        .clueChance(741600).temporaryMapObject(new SkillTemporaryMapObject(30986, 5, 4))
        .variable(UNIDENTIFIED_MINERAL_VARIABLE, 80).build());
    entries.add(SkillEntry.builder().level(10).experience(17).mapObject(new SkillModel(7462, 0))
        .mapObject(new SkillModel(7495, 0)).create(new RandomItem(ItemId.BLURITE_ORE))
        .temporaryMapObject(new SkillTemporaryMapObject(7469, 2, 4))
        .temporaryMapObject(new SkillTemporaryMapObject(7468, 2, 4))
        .variable(UNIDENTIFIED_MINERAL_VARIABLE, 150).build());
    entries.add(SkillEntry.builder().level(30).experience(5).mapObject(new SkillModel(7471, 0))
        .mapObject(new SkillModel(10796, 0)).mapObject(new SkillModel(8981, 0))
        .create(new RandomItem(ItemId.PURE_ESSENCE)).build());
  }
}


@Getter
class MiningPickaxe {
  private int itemId;
  private int level;
  private int animation;
  private int wallAnimation;

  public MiningPickaxe(int itemId, int level, int animation, int wallAnimation) {
    this.itemId = itemId;
    this.level = level;
    this.animation = animation;
    this.wallAnimation = wallAnimation;
  }
}
