package com.palidinodh.osrsscript.world.event.pvptournament;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.palidinodh.osrscore.io.cache.ItemId;
import com.palidinodh.osrscore.model.item.Item;
import com.palidinodh.osrscore.model.player.Loadout;
import com.palidinodh.osrscore.model.player.Magic;
import com.palidinodh.osrsscript.player.plugin.clanwars.rule.Rule;
import com.palidinodh.osrsscript.player.plugin.clanwars.rule.RuleOption;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.var;

@Builder
@Getter
public class Mode {
  public static final List<Mode> MODES;

  private String name;
  private int spellbook;
  private int brewCap;
  private int[] rules;
  @Builder.Default
  private int teamSize = 1;
  @Builder.Default
  private int attackLevel = 1;
  @Builder.Default
  private int defenceLevel = 1;
  @Builder.Default
  private int strengthLevel = 1;
  @Builder.Default
  private int hitpointsLevel = 10;
  @Builder.Default
  private int rangedLevel = 1;
  @Builder.Default
  private int prayerLevel = 1;
  @Builder.Default
  private int magicLevel = 1;
  @Singular
  private List<Integer> runes;
  @Singular
  private List<Loadout.Entry> loadouts;

  public int[] getSkillLevels() {
    return new int[] {attackLevel, defenceLevel, strengthLevel, hitpointsLevel, rangedLevel,
        prayerLevel, magicLevel};
  }

  public static int[] buildRules(Object... settings) {
    var rules = Rule.getDefault();
    for (var i = 0; i < settings.length; i += 2) {
      if (!(settings[i] instanceof Rule) || !(settings[i + 1] instanceof RuleOption)) {
        break;
      }
      Rule rule = (Rule) settings[i];
      RuleOption option = (RuleOption) settings[i + 1];
      rules[rule.ordinal()] = option.getIndex();
    }
    return rules;
  }

  static {
    List<Mode> modes = new ArrayList<>();

    var mode = Mode.builder().name("<img=8> Main Hybrid").spellbook(Magic.ANCIENT_MAGIC).brewCap(3);
    mode.rules(buildRules(Rule.PRAYER, RuleOption.NO_OVERHEADS, Rule.SPECIAL_ATTACKS,
        RuleOption.NO_STAFF_OF_THE_DEAD));
    mode.attackLevel(99).strengthLevel(99).rangedLevel(99).magicLevel(99).defenceLevel(99)
        .hitpointsLevel(99).prayerLevel(99);
    mode.rune(ItemId.DEATH_RUNE).rune(ItemId.BLOOD_RUNE).rune(ItemId.WATER_RUNE);
    mode.loadout(new Loadout.Entry("Load-out",
        new Item[] {new Item(ItemId.HELM_OF_NEITIZNOT), new Item(ItemId.IMBUED_SARADOMIN_CAPE),
            new Item(ItemId.AMULET_OF_GLORY_4), new Item(ItemId.ANCIENT_STAFF),
            new Item(ItemId.MYSTIC_ROBE_TOP), new Item(ItemId.SPIRIT_SHIELD), null,
            new Item(ItemId.MYSTIC_ROBE_BOTTOM), null, new Item(ItemId.BARROWS_GLOVES),
            new Item(ItemId.DRAGON_BOOTS), null, new Item(ItemId.SEERS_RING_I), null},
        new Item[] {new Item(ItemId.SARADOMIN_DHIDE), new Item(ItemId.ABYSSAL_WHIP),
            new Item(ItemId.SUPER_COMBAT_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.FIGHTER_TORSO), new Item(ItemId.DRAGON_DEFENDER),
            new Item(ItemId.SARADOMIN_BREW_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.RUNE_PLATELEGS), new Item(ItemId.INFERNAL_CAPE),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.SANFEW_SERUM_4),
            new Item(ItemId.DRAGON_DAGGER_P_PLUS_PLUS), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.RUNE_POUCH)},
        -1));
    mode.loadout(new Loadout.Entry("+ Granite Maul", new Item(ItemId.GRANITE_MAUL)));
    modes.add(mode.build());

    mode = Mode.builder().name("<img=6> Main Hybrid").spellbook(Magic.ANCIENT_MAGIC).brewCap(3);
    mode.rules(buildRules(Rule.PRAYER, RuleOption.NO_OVERHEADS, Rule.SPECIAL_ATTACKS,
        RuleOption.NO_STAFF_OF_THE_DEAD));
    mode.attackLevel(99).strengthLevel(99).rangedLevel(99).magicLevel(99).defenceLevel(99)
        .hitpointsLevel(99).prayerLevel(99);
    mode.rune(ItemId.DEATH_RUNE).rune(ItemId.BLOOD_RUNE).rune(ItemId.WATER_RUNE);
    mode.loadout(new Loadout.Entry("Load-out",
        new Item[] {new Item(ItemId.SERPENTINE_HELM), new Item(ItemId.IMBUED_SARADOMIN_CAPE),
            new Item(ItemId.AMULET_OF_FURY), new Item(ItemId.TOXIC_STAFF_OF_THE_DEAD),
            new Item(ItemId.AHRIMS_ROBETOP), new Item(ItemId.BLESSED_SPIRIT_SHIELD), null,
            new Item(ItemId.AHRIMS_ROBESKIRT), null, new Item(ItemId.BARROWS_GLOVES),
            new Item(ItemId.DRAGON_BOOTS), null, new Item(ItemId.SEERS_RING_I), null},
        new Item[] {new Item(ItemId.KARILS_LEATHERTOP), new Item(ItemId.ABYSSAL_TENTACLE),
            new Item(ItemId.SUPER_COMBAT_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.DHAROKS_PLATEBODY), new Item(ItemId.DRAGON_DEFENDER),
            new Item(ItemId.SARADOMIN_BREW_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.DHAROKS_PLATELEGS), new Item(ItemId.INFERNAL_CAPE),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.SANFEW_SERUM_4), null,
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.RUNE_POUCH)},
        -1));
    mode.loadout(new Loadout.Entry("+ Armadyl Godsword", new Item(ItemId.ARMADYL_GODSWORD)));
    mode.loadout(
        new Loadout.Entry("+ Dragon Dagger (P++)", new Item(ItemId.DRAGON_DAGGER_P_PLUS_PLUS)));
    mode.loadout(new Loadout.Entry("+ Granite Maul", new Item(ItemId.GRANITE_MAUL)));
    modes.add(mode.build());

    mode = Mode.builder().name("<img=4> Main Hybrid").spellbook(Magic.ANCIENT_MAGIC).brewCap(3);
    mode.rules(buildRules(Rule.PRAYER, RuleOption.NO_OVERHEADS, Rule.SPECIAL_ATTACKS,
        RuleOption.NO_STAFF_OF_THE_DEAD));
    mode.attackLevel(99).strengthLevel(99).rangedLevel(99).magicLevel(99).defenceLevel(99)
        .hitpointsLevel(99).prayerLevel(99);
    mode.rune(ItemId.DEATH_RUNE).rune(ItemId.BLOOD_RUNE).rune(ItemId.WATER_RUNE);
    mode.loadout(new Loadout.Entry("Load-out #1",
        new Item[] {new Item(ItemId.SERPENTINE_HELM), new Item(ItemId.IMBUED_SARADOMIN_CAPE),
            new Item(ItemId.AMULET_OF_FURY), new Item(ItemId.TOXIC_STAFF_OF_THE_DEAD),
            new Item(ItemId.ANCESTRAL_ROBE_TOP), new Item(ItemId.ARCANE_SPIRIT_SHIELD), null,
            new Item(ItemId.ANCESTRAL_ROBE_BOTTOM), null, new Item(ItemId.BARROWS_GLOVES),
            new Item(ItemId.PRIMORDIAL_BOOTS), null, new Item(ItemId.SEERS_RING_I), null},
        new Item[] {new Item(ItemId.ARMADYL_CHESTPLATE), new Item(ItemId.ABYSSAL_TENTACLE),
            new Item(ItemId.SUPER_COMBAT_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.BANDOS_CHESTPLATE), new Item(ItemId.AVERNIC_DEFENDER),
            new Item(ItemId.SARADOMIN_BREW_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.BANDOS_TASSETS), new Item(ItemId.INFERNAL_CAPE),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.SANFEW_SERUM_4),
            new Item(ItemId.DRAGON_CLAWS), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.RUNE_POUCH)},
        -1));
    mode.loadout(new Loadout.Entry("Load-out #2",
        new Item[] {new Item(ItemId.SERPENTINE_HELM), new Item(ItemId.IMBUED_SARADOMIN_CAPE),
            new Item(ItemId.AMULET_OF_FURY), new Item(ItemId.TOXIC_STAFF_OF_THE_DEAD),
            new Item(ItemId.ANCESTRAL_ROBE_TOP), new Item(ItemId.ARCANE_SPIRIT_SHIELD), null,
            new Item(ItemId.ANCESTRAL_ROBE_BOTTOM), null, new Item(ItemId.BARROWS_GLOVES),
            new Item(ItemId.PRIMORDIAL_BOOTS), null, new Item(ItemId.SEERS_RING_I), null},
        new Item[] {new Item(ItemId.ARMADYL_CHESTPLATE), new Item(ItemId.ABYSSAL_TENTACLE),
            new Item(ItemId.SUPER_COMBAT_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.BANDOS_CHESTPLATE), new Item(ItemId.AVERNIC_DEFENDER),
            new Item(ItemId.SARADOMIN_BREW_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.BANDOS_TASSETS), new Item(ItemId.INFERNAL_CAPE),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.SANFEW_SERUM_4),
            new Item(ItemId.ARMADYL_GODSWORD), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.RUNE_POUCH)},
        -1));
    mode.loadout(new Loadout.Entry("Load-out #3",
        new Item[] {new Item(ItemId.SERPENTINE_HELM), new Item(ItemId.IMBUED_SARADOMIN_CAPE),
            new Item(ItemId.AMULET_OF_FURY), new Item(ItemId.TOXIC_STAFF_OF_THE_DEAD),
            new Item(ItemId.ANCESTRAL_ROBE_TOP), new Item(ItemId.ARCANE_SPIRIT_SHIELD), null,
            new Item(ItemId.ANCESTRAL_ROBE_BOTTOM), null, new Item(ItemId.BARROWS_GLOVES),
            new Item(ItemId.PRIMORDIAL_BOOTS), null, new Item(ItemId.SEERS_RING_I), null},
        new Item[] {new Item(ItemId.ARMADYL_CHESTPLATE), new Item(ItemId.GHRAZI_RAPIER),
            new Item(ItemId.SUPER_COMBAT_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.BANDOS_CHESTPLATE), new Item(ItemId.AVERNIC_DEFENDER),
            new Item(ItemId.SARADOMIN_BREW_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.BANDOS_TASSETS), new Item(ItemId.INFERNAL_CAPE),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.SANFEW_SERUM_4),
            new Item(ItemId.DRAGON_CLAWS), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.RUNE_POUCH)},
        -1));
    mode.loadout(new Loadout.Entry("Load-out #4",
        new Item[] {new Item(ItemId.SERPENTINE_HELM), new Item(ItemId.IMBUED_SARADOMIN_CAPE),
            new Item(ItemId.AMULET_OF_FURY), new Item(ItemId.TOXIC_STAFF_OF_THE_DEAD),
            new Item(ItemId.ANCESTRAL_ROBE_TOP), new Item(ItemId.ARCANE_SPIRIT_SHIELD), null,
            new Item(ItemId.ANCESTRAL_ROBE_BOTTOM), null, new Item(ItemId.BARROWS_GLOVES),
            new Item(ItemId.PRIMORDIAL_BOOTS), null, new Item(ItemId.SEERS_RING_I), null},
        new Item[] {new Item(ItemId.ARMADYL_CHESTPLATE), new Item(ItemId.GHRAZI_RAPIER),
            new Item(ItemId.SUPER_COMBAT_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.BANDOS_CHESTPLATE), new Item(ItemId.AVERNIC_DEFENDER),
            new Item(ItemId.SARADOMIN_BREW_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.BANDOS_TASSETS), new Item(ItemId.INFERNAL_CAPE),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.SANFEW_SERUM_4),
            new Item(ItemId.ARMADYL_GODSWORD), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.RUNE_POUCH)},
        -1));
    mode.loadout(new Loadout.Entry("+ Granite Maul", new Item(ItemId.GRANITE_MAUL)));
    modes.add(mode.build());

    mode = Mode.builder().name("<img=8> Main NH").spellbook(Magic.ANCIENT_MAGIC).brewCap(3);
    mode.rules(buildRules(Rule.SPECIAL_ATTACKS, RuleOption.NO_STAFF_OF_THE_DEAD));
    mode.attackLevel(99).strengthLevel(99).rangedLevel(99).magicLevel(99).defenceLevel(99)
        .hitpointsLevel(99).prayerLevel(99);
    mode.rune(ItemId.DEATH_RUNE).rune(ItemId.BLOOD_RUNE).rune(ItemId.WATER_RUNE);
    mode.loadout(new Loadout.Entry("Load-out",
        new Item[] {new Item(ItemId.HELM_OF_NEITIZNOT), new Item(ItemId.IMBUED_SARADOMIN_CAPE),
            new Item(ItemId.AMULET_OF_GLORY_4), new Item(ItemId.ANCIENT_STAFF),
            new Item(ItemId.MYSTIC_ROBE_TOP), new Item(ItemId.SPIRIT_SHIELD), null,
            new Item(ItemId.MYSTIC_ROBE_BOTTOM), null, new Item(ItemId.BARROWS_GLOVES),
            new Item(ItemId.DRAGON_BOOTS), null, new Item(ItemId.SEERS_RING_I),
            new Item(ItemId.DRAGONSTONE_BOLTS_E, 8000)},
        new Item[] {new Item(ItemId.RUNE_CROSSBOW), new Item(ItemId.ABYSSAL_WHIP),
            new Item(ItemId.SUPER_COMBAT_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.SARADOMIN_DHIDE), new Item(ItemId.DRAGON_DEFENDER),
            new Item(ItemId.BASTION_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.RUNE_PLATELEGS), new Item(ItemId.INFERNAL_CAPE),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.DRAGON_DAGGER_P_PLUS_PLUS), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.SANFEW_SERUM_4),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.RUNE_POUCH)},
        -1));
    mode.loadout(new Loadout.Entry("+ Granite Maul", new Item(ItemId.GRANITE_MAUL)));
    modes.add(mode.build());

    mode = Mode.builder().name("<img=6> Main NH").spellbook(Magic.ANCIENT_MAGIC).brewCap(3);
    mode.rules(buildRules(Rule.SPECIAL_ATTACKS, RuleOption.NO_STAFF_OF_THE_DEAD));
    mode.attackLevel(99).strengthLevel(99).rangedLevel(99).magicLevel(99).defenceLevel(99)
        .hitpointsLevel(99).prayerLevel(99);
    mode.rune(ItemId.DEATH_RUNE).rune(ItemId.BLOOD_RUNE).rune(ItemId.WATER_RUNE);
    mode.loadout(new Loadout.Entry("Load-out",
        new Item[] {new Item(ItemId.SERPENTINE_HELM), new Item(ItemId.IMBUED_SARADOMIN_CAPE),
            new Item(ItemId.AMULET_OF_FURY), new Item(ItemId.TOXIC_STAFF_OF_THE_DEAD),
            new Item(ItemId.AHRIMS_ROBETOP), new Item(ItemId.BLESSED_SPIRIT_SHIELD), null,
            new Item(ItemId.AHRIMS_ROBESKIRT), null, new Item(ItemId.BARROWS_GLOVES),
            new Item(ItemId.DRAGON_BOOTS), null, new Item(ItemId.SEERS_RING_I),
            new Item(ItemId.DRAGONSTONE_DRAGON_BOLTS_E, 8000)},
        new Item[] {new Item(ItemId.ARMADYL_CROSSBOW), new Item(ItemId.ABYSSAL_TENTACLE),
            new Item(ItemId.SUPER_COMBAT_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.KARILS_LEATHERTOP), new Item(ItemId.DRAGON_DEFENDER),
            new Item(ItemId.BASTION_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.DHAROKS_PLATELEGS), new Item(ItemId.INFERNAL_CAPE),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.SARADOMIN_BREW_4), null,
            new Item(ItemId.ANGLERFISH), new Item(ItemId.SANFEW_SERUM_4),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.RUNE_POUCH)},
        -1));
    mode.loadout(new Loadout.Entry("+ Armadyl Godsword", new Item(ItemId.ARMADYL_GODSWORD)));
    mode.loadout(
        new Loadout.Entry("+ Dragon Dagger (P++)", new Item(ItemId.DRAGON_DAGGER_P_PLUS_PLUS)));
    mode.loadout(new Loadout.Entry("+ Granite Maul", new Item(ItemId.GRANITE_MAUL)));
    modes.add(mode.build());

    mode = Mode.builder().name("<img=4> Main NH").spellbook(Magic.ANCIENT_MAGIC).brewCap(3);
    mode.rules(buildRules(Rule.SPECIAL_ATTACKS, RuleOption.NO_STAFF_OF_THE_DEAD));
    mode.attackLevel(99).strengthLevel(99).rangedLevel(99).magicLevel(99).defenceLevel(99)
        .hitpointsLevel(99).prayerLevel(99);
    mode.rune(ItemId.DEATH_RUNE).rune(ItemId.BLOOD_RUNE).rune(ItemId.WATER_RUNE);
    mode.loadout(new Loadout.Entry("Load-out #1",
        new Item[] {new Item(ItemId.SERPENTINE_HELM), new Item(ItemId.IMBUED_SARADOMIN_CAPE),
            new Item(ItemId.AMULET_OF_FURY), new Item(ItemId.TOXIC_STAFF_OF_THE_DEAD),
            new Item(ItemId.ANCESTRAL_ROBE_TOP), new Item(ItemId.ARCANE_SPIRIT_SHIELD), null,
            new Item(ItemId.ANCESTRAL_ROBE_BOTTOM), null, new Item(ItemId.BARROWS_GLOVES),
            new Item(ItemId.PRIMORDIAL_BOOTS), null, new Item(ItemId.SEERS_RING_I),
            new Item(ItemId.DRAGONSTONE_DRAGON_BOLTS_E, 8000)},
        new Item[] {new Item(ItemId.ARMADYL_CROSSBOW), new Item(ItemId.ABYSSAL_TENTACLE),
            new Item(ItemId.SUPER_COMBAT_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.ARMADYL_CHESTPLATE), new Item(ItemId.AVERNIC_DEFENDER),
            new Item(ItemId.BASTION_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.BANDOS_TASSETS), new Item(ItemId.INFERNAL_CAPE),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.DRAGON_CLAWS), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.SANFEW_SERUM_4),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.RUNE_POUCH)},
        -1));
    mode.loadout(new Loadout.Entry("Load-out #2",
        new Item[] {new Item(ItemId.SERPENTINE_HELM), new Item(ItemId.IMBUED_SARADOMIN_CAPE),
            new Item(ItemId.AMULET_OF_FURY), new Item(ItemId.TOXIC_STAFF_OF_THE_DEAD),
            new Item(ItemId.ANCESTRAL_ROBE_TOP), new Item(ItemId.ARCANE_SPIRIT_SHIELD), null,
            new Item(ItemId.ANCESTRAL_ROBE_BOTTOM), null, new Item(ItemId.BARROWS_GLOVES),
            new Item(ItemId.PRIMORDIAL_BOOTS), null, new Item(ItemId.SEERS_RING_I),
            new Item(ItemId.DRAGONSTONE_DRAGON_BOLTS_E, 8000)},
        new Item[] {new Item(ItemId.ARMADYL_CROSSBOW), new Item(ItemId.ABYSSAL_TENTACLE),
            new Item(ItemId.SUPER_COMBAT_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.ARMADYL_CHESTPLATE), new Item(ItemId.AVERNIC_DEFENDER),
            new Item(ItemId.BASTION_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.BANDOS_TASSETS), new Item(ItemId.INFERNAL_CAPE),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.ARMADYL_GODSWORD), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.SANFEW_SERUM_4),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.RUNE_POUCH)},
        -1));
    mode.loadout(new Loadout.Entry("Load-out #3",
        new Item[] {new Item(ItemId.SERPENTINE_HELM), new Item(ItemId.IMBUED_SARADOMIN_CAPE),
            new Item(ItemId.AMULET_OF_FURY), new Item(ItemId.TOXIC_STAFF_OF_THE_DEAD),
            new Item(ItemId.ANCESTRAL_ROBE_TOP), new Item(ItemId.ARCANE_SPIRIT_SHIELD), null,
            new Item(ItemId.ANCESTRAL_ROBE_BOTTOM), null, new Item(ItemId.BARROWS_GLOVES),
            new Item(ItemId.PRIMORDIAL_BOOTS), null, new Item(ItemId.SEERS_RING_I),
            new Item(ItemId.DRAGONSTONE_DRAGON_BOLTS_E, 8000)},
        new Item[] {new Item(ItemId.ARMADYL_CROSSBOW), new Item(ItemId.GHRAZI_RAPIER),
            new Item(ItemId.SUPER_COMBAT_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.ARMADYL_CHESTPLATE), new Item(ItemId.AVERNIC_DEFENDER),
            new Item(ItemId.BASTION_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.BANDOS_TASSETS), new Item(ItemId.INFERNAL_CAPE),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.DRAGON_CLAWS), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.SANFEW_SERUM_4),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.RUNE_POUCH)},
        -1));
    mode.loadout(new Loadout.Entry("Load-out #4",
        new Item[] {new Item(ItemId.SERPENTINE_HELM), new Item(ItemId.IMBUED_SARADOMIN_CAPE),
            new Item(ItemId.AMULET_OF_FURY), new Item(ItemId.TOXIC_STAFF_OF_THE_DEAD),
            new Item(ItemId.ANCESTRAL_ROBE_TOP), new Item(ItemId.ARCANE_SPIRIT_SHIELD), null,
            new Item(ItemId.ANCESTRAL_ROBE_BOTTOM), null, new Item(ItemId.BARROWS_GLOVES),
            new Item(ItemId.PRIMORDIAL_BOOTS), null, new Item(ItemId.SEERS_RING_I),
            new Item(ItemId.DRAGONSTONE_DRAGON_BOLTS_E, 8000)},
        new Item[] {new Item(ItemId.ARMADYL_CROSSBOW), new Item(ItemId.GHRAZI_RAPIER),
            new Item(ItemId.SUPER_COMBAT_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.ARMADYL_CHESTPLATE), new Item(ItemId.AVERNIC_DEFENDER),
            new Item(ItemId.BASTION_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.BANDOS_TASSETS), new Item(ItemId.INFERNAL_CAPE),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.ARMADYL_GODSWORD), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.SANFEW_SERUM_4),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.RUNE_POUCH)},
        -1));
    mode.loadout(new Loadout.Entry("+ Granite Maul", new Item(ItemId.GRANITE_MAUL)));
    modes.add(mode.build());

    mode = Mode.builder().name("<img=8> Pure NH").spellbook(Magic.ANCIENT_MAGIC).brewCap(3);
    mode.rules(buildRules(Rule.SPECIAL_ATTACKS, RuleOption.NO_STAFF_OF_THE_DEAD));
    mode.attackLevel(99).strengthLevel(99).rangedLevel(99).magicLevel(99).defenceLevel(1)
        .hitpointsLevel(99).prayerLevel(99);
    mode.rune(ItemId.DEATH_RUNE).rune(ItemId.BLOOD_RUNE).rune(ItemId.WATER_RUNE);
    mode.loadout(new Loadout.Entry("Load-out",
        new Item[] {new Item(ItemId.GHOSTLY_HOOD), new Item(ItemId.IMBUED_ZAMORAK_CAPE),
            new Item(ItemId.AMULET_OF_GLORY_4), new Item(ItemId.ANCIENT_STAFF),
            new Item(ItemId.GHOSTLY_ROBE), new Item(ItemId.BOOK_OF_DARKNESS), null,
            new Item(ItemId.GHOSTLY_ROBE_6108), null, new Item(ItemId.MITHRIL_GLOVES),
            new Item(ItemId.CLIMBING_BOOTS), null, new Item(ItemId.SEERS_RING_I),
            new Item(ItemId.DRAGONSTONE_BOLTS_E, 8000)},
        new Item[] {new Item(ItemId.RUNE_CROSSBOW), new Item(ItemId.AVAS_ACCUMULATOR),
            new Item(ItemId.SUPER_COMBAT_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.ZAMORAK_CHAPS), new Item(ItemId.DRAGON_DAGGER_P_PLUS_PLUS),
            new Item(ItemId.BASTION_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.DRAGON_SCIMITAR), new Item(ItemId.INFERNAL_CAPE),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.BOOK_OF_WAR), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.SANFEW_SERUM_4),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.RUNE_POUCH)},
        -1));
    mode.loadout(new Loadout.Entry("+ Granite Maul", new Item(ItemId.GRANITE_MAUL)));
    modes.add(mode.build());

    mode = Mode.builder().name("<img=4> Pure NH").spellbook(Magic.ANCIENT_MAGIC).brewCap(3);
    mode.rules(buildRules(Rule.SPECIAL_ATTACKS, RuleOption.NO_STAFF_OF_THE_DEAD));
    mode.attackLevel(99).strengthLevel(99).rangedLevel(99).magicLevel(99).defenceLevel(1)
        .hitpointsLevel(99).prayerLevel(99);
    mode.rune(ItemId.DEATH_RUNE).rune(ItemId.BLOOD_RUNE).rune(ItemId.WATER_RUNE);
    mode.loadout(new Loadout.Entry("Load-out #1",
        new Item[] {new Item(ItemId.ELDER_CHAOS_HOOD), new Item(ItemId.IMBUED_ZAMORAK_CAPE),
            new Item(ItemId.AMULET_OF_FURY), new Item(ItemId.TOXIC_STAFF_OF_THE_DEAD),
            new Item(ItemId.ELDER_CHAOS_TOP), new Item(ItemId.MAGES_BOOK), null,
            new Item(ItemId.ELDER_CHAOS_ROBE), null, new Item(ItemId.REGEN_BRACELET),
            new Item(ItemId.CLIMBING_BOOTS), null, new Item(ItemId.SEERS_RING_I),
            new Item(ItemId.DRAGONSTONE_BOLTS_E, 8000)},
        new Item[] {new Item(ItemId.ARMADYL_CROSSBOW), new Item(ItemId.AVAS_ASSEMBLER),
            new Item(ItemId.SUPER_COMBAT_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.ZAMORAK_CHAPS), new Item(ItemId.DRAGON_CLAWS),
            new Item(ItemId.BASTION_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.DRAGON_SCIMITAR), new Item(ItemId.INFERNAL_CAPE),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.BOOK_OF_WAR), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.SANFEW_SERUM_4),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANTI_VENOM_PLUS_4), new Item(ItemId.RUNE_POUCH)},
        -1));
    mode.loadout(new Loadout.Entry("Load-out #2",
        new Item[] {new Item(ItemId.ELDER_CHAOS_HOOD), new Item(ItemId.IMBUED_ZAMORAK_CAPE),
            new Item(ItemId.AMULET_OF_FURY), new Item(ItemId.TOXIC_STAFF_OF_THE_DEAD),
            new Item(ItemId.ELDER_CHAOS_TOP), new Item(ItemId.MAGES_BOOK), null,
            new Item(ItemId.ELDER_CHAOS_ROBE), null, new Item(ItemId.REGEN_BRACELET),
            new Item(ItemId.CLIMBING_BOOTS), null, new Item(ItemId.SEERS_RING_I),
            new Item(ItemId.DRAGONSTONE_BOLTS_E, 8000)},
        new Item[] {new Item(ItemId.ARMADYL_CROSSBOW), new Item(ItemId.AVAS_ASSEMBLER),
            new Item(ItemId.SUPER_COMBAT_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.ZAMORAK_CHAPS), new Item(ItemId.ARMADYL_GODSWORD),
            new Item(ItemId.BASTION_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.DRAGON_SCIMITAR), new Item(ItemId.INFERNAL_CAPE),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.BOOK_OF_WAR), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.SANFEW_SERUM_4),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANTI_VENOM_PLUS_4), new Item(ItemId.RUNE_POUCH)},
        -1));
    mode.loadout(new Loadout.Entry("+ Elder Maul", new Item(ItemId.ELDER_MAUL)));
    mode.loadout(new Loadout.Entry("+ Granite Maul", new Item(ItemId.GRANITE_MAUL)));
    modes.add(mode.build());

    mode = Mode.builder().name("Dharoks").spellbook(Magic.LUNAR_MAGIC).brewCap(3);
    mode.rules(buildRules(Rule.PRAYER, RuleOption.NO_OVERHEADS, Rule.SPECIAL_ATTACKS,
        RuleOption.NO_STAFF_OF_THE_DEAD));
    mode.attackLevel(99).strengthLevel(99).rangedLevel(99).magicLevel(99).defenceLevel(80)
        .hitpointsLevel(99).prayerLevel(99);
    mode.rune(ItemId.ASTRAL_RUNE).rune(ItemId.DEATH_RUNE).rune(ItemId.EARTH_RUNE);
    mode.loadout(new Loadout.Entry("Load-out #1",
        new Item[] {new Item(ItemId.DHAROKS_HELM), new Item(ItemId.INFERNAL_CAPE),
            new Item(ItemId.AMULET_OF_TORTURE), new Item(ItemId.ABYSSAL_TENTACLE),
            new Item(ItemId.DHAROKS_PLATEBODY), new Item(ItemId.AVERNIC_DEFENDER), null,
            new Item(ItemId.DHAROKS_PLATELEGS), null, new Item(ItemId.FEROCIOUS_GLOVES),
            new Item(ItemId.PRIMORDIAL_BOOTS), null, new Item(ItemId.BERSERKER_RING_I), null},
        new Item[] {new Item(ItemId.DHAROKS_GREATAXE), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.SUPER_COMBAT_POTION_4), new Item(ItemId.SARADOMIN_BREW_4), null,
            new Item(ItemId.ANGLERFISH), new Item(ItemId.SANFEW_SERUM_4),
            new Item(ItemId.SARADOMIN_BREW_4), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.SANFEW_SERUM_4),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.RUNE_POUCH)},
        -1));
    mode.loadout(new Loadout.Entry("Load-out #2",
        new Item[] {new Item(ItemId.DHAROKS_HELM), new Item(ItemId.INFERNAL_CAPE),
            new Item(ItemId.AMULET_OF_TORTURE), new Item(ItemId.GHRAZI_RAPIER),
            new Item(ItemId.DHAROKS_PLATEBODY), new Item(ItemId.AVERNIC_DEFENDER), null,
            new Item(ItemId.DHAROKS_PLATELEGS), null, new Item(ItemId.FEROCIOUS_GLOVES),
            new Item(ItemId.PRIMORDIAL_BOOTS), null, new Item(ItemId.BERSERKER_RING_I), null},
        new Item[] {new Item(ItemId.DHAROKS_GREATAXE), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.SUPER_COMBAT_POTION_4), new Item(ItemId.SARADOMIN_BREW_4), null,
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.SANFEW_SERUM_4),
            new Item(ItemId.SARADOMIN_BREW_4), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.SANFEW_SERUM_4),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.RUNE_POUCH)},
        -1));
    mode.loadout(
        new Loadout.Entry("+ Super Combat Potion (4)", new Item(ItemId.SUPER_COMBAT_POTION_4)));
    mode.loadout(new Loadout.Entry("+ Armadyl Godsword", new Item(ItemId.ARMADYL_GODSWORD)));
    mode.loadout(new Loadout.Entry("+ Dragon Warhammer", new Item(ItemId.DRAGON_WARHAMMER)));
    mode.loadout(new Loadout.Entry("+ Granite Maul", new Item(ItemId.GRANITE_MAUL)));
    modes.add(mode.build());

    mode = Mode.builder().name("Berserker").spellbook(Magic.LUNAR_MAGIC).brewCap(3);
    mode.rules(buildRules(Rule.PRAYER, RuleOption.NO_OVERHEADS, Rule.SPECIAL_ATTACKS,
        RuleOption.NO_STAFF_OF_THE_DEAD));
    mode.attackLevel(60).strengthLevel(99).rangedLevel(99).magicLevel(99).defenceLevel(45)
        .hitpointsLevel(99).prayerLevel(99);
    mode.rune(ItemId.ASTRAL_RUNE).rune(ItemId.DEATH_RUNE).rune(ItemId.EARTH_RUNE);
    mode.loadout(new Loadout.Entry("Load-out",
        new Item[] {new Item(ItemId.BERSERKER_HELM), new Item(ItemId.INFERNAL_CAPE),
            new Item(ItemId.AMULET_OF_TORTURE), new Item(ItemId.DRAGON_SCIMITAR),
            new Item(ItemId.FIGHTER_TORSO), new Item(ItemId.RUNE_DEFENDER), null,
            new Item(ItemId.RUNE_PLATELEGS), null, new Item(ItemId.BARROWS_GLOVES),
            new Item(ItemId.RUNE_BOOTS), null, new Item(ItemId.BERSERKER_RING_I), null},
        new Item[] {new Item(ItemId.BARRELCHEST_ANCHOR), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.SUPER_COMBAT_POTION_4), new Item(ItemId.SARADOMIN_BREW_4), null,
            new Item(ItemId.ANGLERFISH), new Item(ItemId.SANFEW_SERUM_4),
            new Item(ItemId.SARADOMIN_BREW_4), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.SANFEW_SERUM_4),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.RUNE_POUCH)},
        -1));
    mode.loadout(
        new Loadout.Entry("+ Dragon Dagger (P++)", new Item(ItemId.DRAGON_DAGGER_P_PLUS_PLUS)));
    mode.loadout(new Loadout.Entry("+ Dragon Mace", new Item(ItemId.DRAGON_MACE)));
    mode.loadout(new Loadout.Entry("+ Dragon Warhammer", new Item(ItemId.DRAGON_WARHAMMER)));
    mode.loadout(new Loadout.Entry("+ Granite Maul", new Item(ItemId.GRANITE_MAUL)));
    modes.add(mode.build());

    mode = Mode.builder().name("Berserker Ranged").spellbook(Magic.LUNAR_MAGIC).brewCap(3);
    mode.rules(buildRules(Rule.PRAYER, RuleOption.NO_OVERHEADS, Rule.SPECIAL_ATTACKS,
        RuleOption.NO_STAFF_OF_THE_DEAD));
    mode.attackLevel(60).strengthLevel(99).rangedLevel(99).magicLevel(99).defenceLevel(45)
        .hitpointsLevel(99).prayerLevel(99);
    mode.rune(ItemId.ASTRAL_RUNE).rune(ItemId.DEATH_RUNE).rune(ItemId.EARTH_RUNE);
    mode.loadout(new Loadout.Entry("Load-out #1",
        new Item[] {new Item(ItemId.BERSERKER_HELM), new Item(ItemId.INFERNAL_CAPE),
            new Item(ItemId.AMULET_OF_TORTURE), new Item(ItemId.MAGIC_SHORTBOW_I),
            new Item(ItemId.FIGHTER_TORSO), null, null, new Item(ItemId.ZAMORAK_CHAPS), null,
            new Item(ItemId.BARROWS_GLOVES), new Item(ItemId.RUNE_BOOTS), null,
            new Item(ItemId.BERSERKER_RING_I), new Item(ItemId.AMETHYST_ARROW_P_PLUS_PLUS, 8000)},
        new Item[] {null, new Item(ItemId.RUNE_DEFENDER), new Item(ItemId.SUPER_COMBAT_POTION_4),
            new Item(ItemId.RANGING_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.SANFEW_SERUM_4),
            new Item(ItemId.SARADOMIN_BREW_4), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.SANFEW_SERUM_4),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.RUNE_POUCH)},
        -1));
    mode.loadout(new Loadout.Entry("Load-out #2",
        new Item[] {new Item(ItemId.BERSERKER_HELM), new Item(ItemId.INFERNAL_CAPE),
            new Item(ItemId.AMULET_OF_TORTURE), new Item(ItemId.RUNE_KNIFE_P_PLUS_PLUS, 8000),
            new Item(ItemId.FIGHTER_TORSO), new Item(ItemId.RUNE_DEFENDER), null,
            new Item(ItemId.ZAMORAK_CHAPS), null, new Item(ItemId.BARROWS_GLOVES),
            new Item(ItemId.RUNE_BOOTS), null, new Item(ItemId.BERSERKER_RING_I), null},
        new Item[] {null, new Item(ItemId.RANGING_POTION_4), new Item(ItemId.SUPER_COMBAT_POTION_4),
            new Item(ItemId.SARADOMIN_BREW_4), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.SANFEW_SERUM_4),
            new Item(ItemId.SARADOMIN_BREW_4), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.SANFEW_SERUM_4),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.RUNE_POUCH)},
        -1));
    mode.loadout(
        new Loadout.Entry("+ Dragon Dagger (P++)", new Item(ItemId.DRAGON_DAGGER_P_PLUS_PLUS)));
    mode.loadout(new Loadout.Entry("+ Dragon Mace", new Item(ItemId.DRAGON_MACE)));
    mode.loadout(new Loadout.Entry("+ Dragon Warhammer", new Item(ItemId.DRAGON_WARHAMMER)));
    mode.loadout(new Loadout.Entry("+ Dark Bow", new Item(ItemId.DARK_BOW),
        new Item(ItemId.DRAGON_ARROW_P_PLUS_PLUS, 8000)));
    mode.loadout(new Loadout.Entry("+ Barrelchest Anchor", new Item(ItemId.BARRELCHEST_ANCHOR)));
    mode.loadout(new Loadout.Entry("+ Heavy Ballista", new Item(ItemId.HEAVY_BALLISTA),
        new Item(ItemId.DRAGON_JAVELIN_P_PLUS_PLUS, 8000)));
    mode.loadout(new Loadout.Entry("+ Granite Maul", new Item(ItemId.GRANITE_MAUL)));
    modes.add(mode.build());

    mode = Mode.builder().name("<img=8> Main Melee").spellbook(Magic.LUNAR_MAGIC).brewCap(3);
    mode.rules(buildRules(Rule.PRAYER, RuleOption.NO_OVERHEADS, Rule.SPECIAL_ATTACKS,
        RuleOption.NO_STAFF_OF_THE_DEAD));
    mode.attackLevel(99).strengthLevel(99).rangedLevel(99).magicLevel(99).defenceLevel(99)
        .hitpointsLevel(99).prayerLevel(99);
    mode.rune(ItemId.ASTRAL_RUNE).rune(ItemId.DEATH_RUNE).rune(ItemId.EARTH_RUNE);
    mode.loadout(new Loadout.Entry("Load-out",
        new Item[] {new Item(ItemId.HELM_OF_NEITIZNOT), new Item(ItemId.INFERNAL_CAPE),
            new Item(ItemId.AMULET_OF_GLORY_4), new Item(ItemId.ABYSSAL_TENTACLE),
            new Item(ItemId.FIGHTER_TORSO), new Item(ItemId.DRAGON_DEFENDER), null,
            new Item(ItemId.DRAGON_PLATELEGS), null, new Item(ItemId.BARROWS_GLOVES),
            new Item(ItemId.DRAGON_BOOTS), null, new Item(ItemId.BERSERKER_RING_I), null},
        new Item[] {null, new Item(ItemId.ANGLERFISH), new Item(ItemId.SUPER_COMBAT_POTION_4),
            new Item(ItemId.SARADOMIN_BREW_4), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.SANFEW_SERUM_4),
            new Item(ItemId.SARADOMIN_BREW_4), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.SANFEW_SERUM_4),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.RUNE_POUCH)},
        -1));
    mode.loadout(new Loadout.Entry("+ Armadyl Godsword", new Item(ItemId.ARMADYL_GODSWORD)));
    mode.loadout(
        new Loadout.Entry("+ Dragon Dagger (P++)", new Item(ItemId.DRAGON_DAGGER_P_PLUS_PLUS)));
    mode.loadout(new Loadout.Entry("+ Granite Maul", new Item(ItemId.GRANITE_MAUL)));
    modes.add(mode.build());

    mode = Mode.builder().name("<img=6> Main Melee").spellbook(Magic.LUNAR_MAGIC).brewCap(3);
    mode.rules(buildRules(Rule.PRAYER, RuleOption.NO_OVERHEADS, Rule.SPECIAL_ATTACKS,
        RuleOption.NO_STAFF_OF_THE_DEAD));
    mode.attackLevel(99).strengthLevel(99).rangedLevel(99).magicLevel(99).defenceLevel(99)
        .hitpointsLevel(99).prayerLevel(99);
    mode.rune(ItemId.ASTRAL_RUNE).rune(ItemId.DEATH_RUNE).rune(ItemId.EARTH_RUNE);
    mode.loadout(new Loadout.Entry("Load-out",
        new Item[] {new Item(ItemId.SERPENTINE_HELM), new Item(ItemId.INFERNAL_CAPE),
            new Item(ItemId.AMULET_OF_TORTURE), new Item(ItemId.ABYSSAL_TENTACLE),
            new Item(ItemId.FIGHTER_TORSO), new Item(ItemId.DRAGON_DEFENDER), null,
            new Item(ItemId.DHAROKS_PLATELEGS), null, new Item(ItemId.FEROCIOUS_GLOVES),
            new Item(ItemId.DRAGON_BOOTS), null, new Item(ItemId.BERSERKER_RING_I), null},
        new Item[] {null, new Item(ItemId.ANGLERFISH), new Item(ItemId.SUPER_COMBAT_POTION_4),
            new Item(ItemId.SARADOMIN_BREW_4), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.SANFEW_SERUM_4),
            new Item(ItemId.SARADOMIN_BREW_4), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.SANFEW_SERUM_4),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.RUNE_POUCH)},
        -1));
    mode.loadout(new Loadout.Entry("+ Armadyl Godsword", new Item(ItemId.ARMADYL_GODSWORD)));
    mode.loadout(
        new Loadout.Entry("+ Dragon Dagger (P++)", new Item(ItemId.DRAGON_DAGGER_P_PLUS_PLUS)));
    mode.loadout(new Loadout.Entry("+ Granite Maul", new Item(ItemId.GRANITE_MAUL)));
    modes.add(mode.build());

    mode = Mode.builder().name("<img=4> Main Melee").spellbook(Magic.LUNAR_MAGIC).brewCap(3);
    mode.rules(buildRules(Rule.PRAYER, RuleOption.NO_OVERHEADS, Rule.SPECIAL_ATTACKS,
        RuleOption.NO_STAFF_OF_THE_DEAD));
    mode.attackLevel(99).strengthLevel(99).rangedLevel(99).magicLevel(99).defenceLevel(99)
        .hitpointsLevel(99).prayerLevel(99);
    mode.rune(ItemId.ASTRAL_RUNE).rune(ItemId.DEATH_RUNE).rune(ItemId.EARTH_RUNE);
    mode.loadout(new Loadout.Entry("Load-out #1",
        new Item[] {new Item(ItemId.SERPENTINE_HELM), new Item(ItemId.INFERNAL_CAPE),
            new Item(ItemId.AMULET_OF_TORTURE), new Item(ItemId.ABYSSAL_TENTACLE),
            new Item(ItemId.BANDOS_CHESTPLATE), new Item(ItemId.AVERNIC_DEFENDER), null,
            new Item(ItemId.BANDOS_TASSETS), null, new Item(ItemId.FEROCIOUS_GLOVES),
            new Item(ItemId.PRIMORDIAL_BOOTS), null, new Item(ItemId.BERSERKER_RING_I), null},
        new Item[] {null, new Item(ItemId.ANGLERFISH), new Item(ItemId.SUPER_COMBAT_POTION_4),
            new Item(ItemId.SARADOMIN_BREW_4), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.SANFEW_SERUM_4),
            new Item(ItemId.SARADOMIN_BREW_4), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.SANFEW_SERUM_4),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.RUNE_POUCH)},
        -1));
    mode.loadout(new Loadout.Entry("Load-out #2",
        new Item[] {new Item(ItemId.SERPENTINE_HELM), new Item(ItemId.INFERNAL_CAPE),
            new Item(ItemId.AMULET_OF_TORTURE), new Item(ItemId.GHRAZI_RAPIER),
            new Item(ItemId.BANDOS_CHESTPLATE), new Item(ItemId.AVERNIC_DEFENDER), null,
            new Item(ItemId.BANDOS_TASSETS), null, new Item(ItemId.FEROCIOUS_GLOVES),
            new Item(ItemId.PRIMORDIAL_BOOTS), null, new Item(ItemId.BERSERKER_RING_I), null},
        new Item[] {null, new Item(ItemId.ANGLERFISH), new Item(ItemId.SUPER_COMBAT_POTION_4),
            new Item(ItemId.SARADOMIN_BREW_4), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.SANFEW_SERUM_4),
            new Item(ItemId.SARADOMIN_BREW_4), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.SANFEW_SERUM_4),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.RUNE_POUCH)},
        -1));
    mode.loadout(new Loadout.Entry("+ Dragon Claws", new Item(ItemId.DRAGON_CLAWS)));
    mode.loadout(new Loadout.Entry("+ Armadyl Godsword", new Item(ItemId.ARMADYL_GODSWORD)));
    mode.loadout(new Loadout.Entry("+ Granite Maul", new Item(ItemId.GRANITE_MAUL)));
    modes.add(mode.build());

    mode = Mode.builder().name("<img=8> Pure").spellbook(Magic.STANDARD_MAGIC).brewCap(3);
    mode.rules(buildRules(Rule.PRAYER, RuleOption.NO_OVERHEADS, Rule.SPECIAL_ATTACKS,
        RuleOption.NO_STAFF_OF_THE_DEAD));
    mode.attackLevel(99).strengthLevel(99).rangedLevel(99).magicLevel(99).defenceLevel(1)
        .hitpointsLevel(99).prayerLevel(99);
    mode.rune(ItemId.AIR_RUNE).rune(ItemId.FIRE_RUNE).rune(ItemId.WRATH_RUNE);
    mode.loadout(new Loadout.Entry("Melee Load-out #1",
        new Item[] {new Item(ItemId.IRON_FULL_HELM), new Item(ItemId.INFERNAL_CAPE),
            new Item(ItemId.AMULET_OF_STRENGTH), new Item(ItemId.ABYSSAL_TENTACLE),
            new Item(ItemId.IRON_PLATEBODY), new Item(ItemId.BOOK_OF_WAR), null,
            new Item(ItemId.IRON_PLATELEGS), null, new Item(ItemId.MITHRIL_GLOVES),
            new Item(ItemId.CLIMBING_BOOTS), null, new Item(ItemId.BERSERKER_RING_I),
            new Item(ItemId.WAR_BLESSING)},
        new Item[] {new Item(ItemId.DRAGON_DAGGER_P_PLUS_PLUS), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.SUPER_COMBAT_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH)},
        -1));
    mode.loadout(new Loadout.Entry("Melee Load-out #2",
        new Item[] {new Item(ItemId.BEARHEAD), new Item(ItemId.INFERNAL_CAPE),
            new Item(ItemId.AMULET_OF_TORTURE), new Item(ItemId.ABYSSAL_TENTACLE),
            new Item(ItemId.IRON_PLATEBODY), new Item(ItemId.BOOK_OF_WAR), null,
            new Item(ItemId.IRON_PLATELEGS), null, new Item(ItemId.REGEN_BRACELET),
            new Item(ItemId.CLIMBING_BOOTS), null, new Item(ItemId.BERSERKER_RING_I),
            new Item(ItemId.WAR_BLESSING)},
        new Item[] {new Item(ItemId.ARMADYL_GODSWORD), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.SUPER_COMBAT_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH)},
        -1));
    mode.loadout(new Loadout.Entry("Melee Load-out #3",
        new Item[] {new Item(ItemId.BEARHEAD), new Item(ItemId.INFERNAL_CAPE),
            new Item(ItemId.AMULET_OF_TORTURE), new Item(ItemId.GHRAZI_RAPIER),
            new Item(ItemId.IRON_PLATEBODY), new Item(ItemId.BOOK_OF_WAR), null,
            new Item(ItemId.IRON_PLATELEGS), null, new Item(ItemId.REGEN_BRACELET),
            new Item(ItemId.CLIMBING_BOOTS), null, new Item(ItemId.BERSERKER_RING_I),
            new Item(ItemId.WAR_BLESSING)},
        new Item[] {new Item(ItemId.ARMADYL_GODSWORD), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.SUPER_COMBAT_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH)},
        -1));
    mode.loadout(new Loadout.Entry("Ranged Load-out #1",
        new Item[] {new Item(ItemId.LEATHER_COWL), new Item(ItemId.AVAS_ASSEMBLER),
            new Item(ItemId.AMULET_OF_GLORY_4), new Item(ItemId.RUNE_KNIFE_P_PLUS_PLUS, 8000),
            new Item(ItemId.LEATHER_BODY), new Item(ItemId.BOOK_OF_LAW), null,
            new Item(ItemId.SARADOMIN_CHAPS), null, new Item(ItemId.MITHRIL_GLOVES),
            new Item(ItemId.CLIMBING_BOOTS), null, new Item(ItemId.ARCHERS_RING_I),
            new Item(ItemId.DRAGON_ARROW_P_PLUS_PLUS, 8000)},
        new Item[] {new Item(ItemId.DARK_BOW), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.BASTION_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH)},
        -1));
    mode.loadout(new Loadout.Entry("Ranged Load-out #2",
        new Item[] {new Item(ItemId.LEATHER_COWL), new Item(ItemId.AVAS_ASSEMBLER),
            new Item(ItemId.AMULET_OF_GLORY_4), new Item(ItemId.MAGIC_SHORTBOW_I),
            new Item(ItemId.LEATHER_BODY), null, null, new Item(ItemId.SARADOMIN_CHAPS), null,
            new Item(ItemId.MITHRIL_GLOVES), new Item(ItemId.CLIMBING_BOOTS), null,
            new Item(ItemId.ARCHERS_RING_I), new Item(ItemId.AMETHYST_ARROW_P_PLUS_PLUS, 8000)},
        new Item[] {new Item(ItemId.DARK_BOW), new Item(ItemId.DRAGON_ARROW_P_PLUS_PLUS, 8000),
            new Item(ItemId.BASTION_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH)},
        -1));
    mode.loadout(new Loadout.Entry("Ranged Load-out #3",
        new Item[] {new Item(ItemId.LEATHER_COWL), new Item(ItemId.AVAS_ASSEMBLER),
            new Item(ItemId.AMULET_OF_GLORY_4), new Item(ItemId.RUNE_CROSSBOW),
            new Item(ItemId.LEATHER_BODY), new Item(ItemId.BOOK_OF_LAW), null,
            new Item(ItemId.SARADOMIN_CHAPS), null, new Item(ItemId.MITHRIL_GLOVES),
            new Item(ItemId.CLIMBING_BOOTS), null, new Item(ItemId.ARCHERS_RING_I),
            new Item(ItemId.DRAGONSTONE_BOLTS_E, 8000)},
        new Item[] {new Item(ItemId.DARK_BOW), new Item(ItemId.DRAGON_ARROW_P_PLUS_PLUS, 8000),
            new Item(ItemId.BASTION_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH)},
        -1));
    mode.loadout(new Loadout.Entry("Magic Load-out",
        new Item[] {new Item(ItemId.GHOSTLY_HOOD), new Item(ItemId.IMBUED_ZAMORAK_CAPE),
            new Item(ItemId.AMULET_OF_GLORY_4), new Item(ItemId.STAFF_OF_THE_DEAD),
            new Item(ItemId.GHOSTLY_ROBE), new Item(ItemId.TOME_OF_FIRE), null,
            new Item(ItemId.GHOSTLY_ROBE_6108), null, new Item(ItemId.MITHRIL_GLOVES),
            new Item(ItemId.CLIMBING_BOOTS), null, new Item(ItemId.SEERS_RING_I),
            new Item(ItemId.ANCIENT_BLESSING)},
        new Item[] {new Item(ItemId.SARADOMIN_SWORD), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.BATTLEMAGE_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.RUNE_POUCH)},
        -1));
    mode.loadout(new Loadout.Entry("+ Dragon Thrownaxe", new Item(ItemId.DRAGON_THROWNAXE, 8000)));
    mode.loadout(new Loadout.Entry("+ Granite Maul", new Item(ItemId.GRANITE_MAUL)));
    modes.add(mode.build());

    mode = Mode.builder().name("<img=4> Pure").spellbook(Magic.STANDARD_MAGIC).brewCap(3);
    mode.rules(buildRules(Rule.PRAYER, RuleOption.NO_OVERHEADS, Rule.SPECIAL_ATTACKS,
        RuleOption.NO_STAFF_OF_THE_DEAD));
    mode.attackLevel(99).strengthLevel(99).rangedLevel(99).magicLevel(99).defenceLevel(1)
        .hitpointsLevel(99).prayerLevel(99);
    mode.rune(ItemId.AIR_RUNE).rune(ItemId.FIRE_RUNE).rune(ItemId.WRATH_RUNE);
    mode.loadout(new Loadout.Entry("Melee Load-out #1",
        new Item[] {new Item(ItemId.BEARHEAD), new Item(ItemId.INFERNAL_CAPE),
            new Item(ItemId.AMULET_OF_TORTURE), new Item(ItemId.ABYSSAL_TENTACLE),
            new Item(ItemId.IRON_PLATEBODY), new Item(ItemId.BOOK_OF_WAR), null,
            new Item(ItemId.IRON_PLATELEGS), null, new Item(ItemId.REGEN_BRACELET),
            new Item(ItemId.CLIMBING_BOOTS), null, new Item(ItemId.BERSERKER_RING_I),
            new Item(ItemId.WAR_BLESSING)},
        new Item[] {new Item(ItemId.DRAGON_CLAWS), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.SUPER_COMBAT_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN)},
        -1));
    mode.loadout(new Loadout.Entry("Melee Load-out #2",
        new Item[] {new Item(ItemId.BEARHEAD), new Item(ItemId.INFERNAL_CAPE),
            new Item(ItemId.AMULET_OF_TORTURE), new Item(ItemId.ABYSSAL_TENTACLE),
            new Item(ItemId.IRON_PLATEBODY), new Item(ItemId.BOOK_OF_WAR), null,
            new Item(ItemId.IRON_PLATELEGS), null, new Item(ItemId.REGEN_BRACELET),
            new Item(ItemId.CLIMBING_BOOTS), null, new Item(ItemId.BERSERKER_RING_I),
            new Item(ItemId.WAR_BLESSING)},
        new Item[] {new Item(ItemId.ARMADYL_GODSWORD), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.SUPER_COMBAT_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN)},
        -1));
    mode.loadout(new Loadout.Entry("Melee Load-out #3",
        new Item[] {new Item(ItemId.BEARHEAD), new Item(ItemId.INFERNAL_CAPE),
            new Item(ItemId.AMULET_OF_TORTURE), new Item(ItemId.GHRAZI_RAPIER),
            new Item(ItemId.IRON_PLATEBODY), new Item(ItemId.BOOK_OF_WAR), null,
            new Item(ItemId.IRON_PLATELEGS), null, new Item(ItemId.REGEN_BRACELET),
            new Item(ItemId.CLIMBING_BOOTS), null, new Item(ItemId.BERSERKER_RING_I),
            new Item(ItemId.WAR_BLESSING)},
        new Item[] {new Item(ItemId.DRAGON_CLAWS), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.SUPER_COMBAT_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH)},
        -1));
    mode.loadout(new Loadout.Entry("Melee Load-out #4",
        new Item[] {new Item(ItemId.BEARHEAD), new Item(ItemId.INFERNAL_CAPE),
            new Item(ItemId.AMULET_OF_TORTURE), new Item(ItemId.GHRAZI_RAPIER),
            new Item(ItemId.IRON_PLATEBODY), new Item(ItemId.BOOK_OF_WAR), null,
            new Item(ItemId.IRON_PLATELEGS), null, new Item(ItemId.REGEN_BRACELET),
            new Item(ItemId.CLIMBING_BOOTS), null, new Item(ItemId.BERSERKER_RING_I),
            new Item(ItemId.WAR_BLESSING)},
        new Item[] {new Item(ItemId.ARMADYL_GODSWORD), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.SUPER_COMBAT_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN)},
        -1));
    mode.loadout(new Loadout.Entry("Ranged Load-out #1",
        new Item[] {new Item(ItemId.ROBIN_HOOD_HAT), new Item(ItemId.AVAS_ASSEMBLER),
            new Item(ItemId.NECKLACE_OF_ANGUISH), new Item(ItemId.DRAGON_KNIFE_P_PLUS_PLUS, 8000),
            new Item(ItemId.RANGERS_TUNIC), new Item(ItemId.BOOK_OF_LAW), null,
            new Item(ItemId.SARADOMIN_CHAPS), null, new Item(ItemId.RANGER_GLOVES),
            new Item(ItemId.RANGER_BOOTS), null, new Item(ItemId.ARCHERS_RING_I),
            new Item(ItemId.DRAGON_ARROW_P_PLUS_PLUS, 8000)},
        new Item[] {new Item(ItemId.DARK_BOW), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.BASTION_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN)},
        -1));
    mode.loadout(new Loadout.Entry("Ranged Load-out #2",
        new Item[] {new Item(ItemId.ROBIN_HOOD_HAT), new Item(ItemId.AVAS_ASSEMBLER),
            new Item(ItemId.NECKLACE_OF_ANGUISH), new Item(ItemId._3RD_AGE_BOW),
            new Item(ItemId.RANGERS_TUNIC), null, null, new Item(ItemId.SARADOMIN_CHAPS), null,
            new Item(ItemId.RANGER_GLOVES), new Item(ItemId.RANGER_BOOTS), null,
            new Item(ItemId.ARCHERS_RING_I), new Item(ItemId.DRAGON_ARROW_P_PLUS_PLUS, 8000)},
        new Item[] {new Item(ItemId.DARK_BOW), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.BASTION_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH)},
        -1));
    mode.loadout(new Loadout.Entry("Ranged Load-out #3",
        new Item[] {new Item(ItemId.ROBIN_HOOD_HAT), new Item(ItemId.AVAS_ASSEMBLER),
            new Item(ItemId.NECKLACE_OF_ANGUISH), new Item(ItemId.ARMADYL_CROSSBOW),
            new Item(ItemId.RANGERS_TUNIC), new Item(ItemId.BOOK_OF_LAW), null,
            new Item(ItemId.SARADOMIN_CHAPS), null, new Item(ItemId.RANGER_GLOVES),
            new Item(ItemId.RANGER_BOOTS), null, new Item(ItemId.ARCHERS_RING_I),
            new Item(ItemId.DRAGONSTONE_BOLTS_E, 8000)},
        new Item[] {new Item(ItemId.DARK_BOW), new Item(ItemId.DRAGON_ARROW_P_PLUS_PLUS, 8000),
            new Item(ItemId.BASTION_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN)},
        -1));
    mode.loadout(new Loadout.Entry("Magic Load-out",
        new Item[] {new Item(ItemId.ELDER_CHAOS_HOOD), new Item(ItemId.IMBUED_ZAMORAK_CAPE),
            new Item(ItemId.OCCULT_NECKLACE), new Item(ItemId.TOXIC_STAFF_OF_THE_DEAD),
            new Item(ItemId.ELDER_CHAOS_TOP), new Item(ItemId.TOME_OF_FIRE), null,
            new Item(ItemId.ELDER_CHAOS_ROBE), null, new Item(ItemId.TORMENTED_BRACELET),
            new Item(ItemId.WIZARD_BOOTS), null, new Item(ItemId.SEERS_RING_I),
            new Item(ItemId.ANCIENT_BLESSING)},
        new Item[] {new Item(ItemId.SARADOMIN_SWORD), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.BATTLEMAGE_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.RUNE_POUCH)},
        -1));
    mode.loadout(new Loadout.Entry("+ Dragon Thrownaxe", new Item(ItemId.DRAGON_THROWNAXE, 8000)));
    mode.loadout(new Loadout.Entry("+ Granite Maul", new Item(ItemId.GRANITE_MAUL)));
    modes.add(mode.build());

    mode = Mode.builder().name("F2P Pure").spellbook(Magic.LUNAR_MAGIC).brewCap(0);
    mode.rules(
        buildRules(Rule.PRAYER, RuleOption.NO_OVERHEADS, Rule.IGNORE_FREEZING, RuleOption.ALLOWED));
    mode.attackLevel(40).strengthLevel(80).rangedLevel(80).magicLevel(69).defenceLevel(1)
        .hitpointsLevel(80).prayerLevel(59);
    mode.rune(ItemId.DEATH_RUNE).rune(ItemId.BLOOD_RUNE).rune(ItemId.WATER_RUNE);
    mode.loadout(new Loadout.Entry("Load-out #1",
        new Item[] {new Item(ItemId.GREEN_HALLOWEEN_MASK), new Item(ItemId.BLACK_CAPE),
            new Item(ItemId.STRENGTH_AMULET_T), new Item(ItemId.RUNE_SCIMITAR),
            new Item(ItemId.MONKS_ROBE_TOP), null, null, new Item(ItemId.GREEN_DHIDE_CHAPS_G), null,
            new Item(ItemId.LEATHER_GLOVES), new Item(ItemId.LEATHER_BOOTS), null, null,
            new Item(ItemId.ADAMANT_ARROW, 8000)},
        new Item[] {new Item(ItemId.SWORDFISH), new Item(ItemId.RUNE_2H_SWORD),
            new Item(ItemId.STRENGTH_POTION_4), new Item(ItemId.SWORDFISH),
            new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH),
            new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH),
            new Item(ItemId.ANCHOVY_PIZZA), new Item(ItemId.ANCHOVY_PIZZA),
            new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH),
            new Item(ItemId.ANCHOVY_PIZZA), new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH),
            new Item(ItemId.SWORDFISH), new Item(ItemId.ANCHOVY_PIZZA), new Item(ItemId.SWORDFISH),
            new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH), new Item(ItemId.ANCHOVY_PIZZA),
            new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH),
            new Item(ItemId.SWORDFISH)},
        -1));
    mode.loadout(new Loadout.Entry("Load-out #2",
        new Item[] {new Item(ItemId.GREEN_HALLOWEEN_MASK), new Item(ItemId.BLACK_CAPE),
            new Item(ItemId.STRENGTH_AMULET_T), new Item(ItemId.MAPLE_SHORTBOW),
            new Item(ItemId.MONKS_ROBE_TOP), null, null, new Item(ItemId.GREEN_DHIDE_CHAPS_G), null,
            new Item(ItemId.GREEN_DHIDE_VAMB), new Item(ItemId.LEATHER_BOOTS), null, null,
            new Item(ItemId.ADAMANT_ARROW, 8000)},
        new Item[] {new Item(ItemId.SWORDFISH), new Item(ItemId.RUNE_2H_SWORD),
            new Item(ItemId.STRENGTH_POTION_4), new Item(ItemId.SWORDFISH),
            new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH),
            new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH),
            new Item(ItemId.ANCHOVY_PIZZA), new Item(ItemId.ANCHOVY_PIZZA),
            new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH),
            new Item(ItemId.ANCHOVY_PIZZA), new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH),
            new Item(ItemId.SWORDFISH), new Item(ItemId.ANCHOVY_PIZZA), new Item(ItemId.SWORDFISH),
            new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH), new Item(ItemId.ANCHOVY_PIZZA),
            new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH),
            new Item(ItemId.SWORDFISH)},
        -1));
    mode.loadout(new Loadout.Entry("+ Rune Scimitar", new Item(ItemId.RUNE_SCIMITAR)));
    mode.loadout(new Loadout.Entry("+ Rune Sword", new Item(ItemId.RUNE_SWORD)));
    mode.loadout(new Loadout.Entry("+ Rune Battleaxe", new Item(ItemId.RUNE_BATTLEAXE)));
    mode.loadout(new Loadout.Entry("+ Rune Longsword", new Item(ItemId.RUNE_LONGSWORD)));
    modes.add(mode.build());

    mode = Mode.builder().name("Void Range").spellbook(Magic.LUNAR_MAGIC).brewCap(3);
    mode.rules(buildRules(Rule.PRAYER, RuleOption.NO_OVERHEADS));
    mode.attackLevel(99).strengthLevel(99).rangedLevel(99).magicLevel(99).defenceLevel(42)
        .hitpointsLevel(99).prayerLevel(99);
    mode.rune(ItemId.DEATH_RUNE).rune(ItemId.ASTRAL_RUNE).rune(ItemId.EARTH_RUNE);
    mode.loadout(new Loadout.Entry("Load-out",
        new Item[] {new Item(ItemId.VOID_RANGER_HELM), new Item(ItemId.AVAS_ASSEMBLER),
            new Item(ItemId.NECKLACE_OF_ANGUISH), new Item(ItemId.DRAGON_KNIFE, 8000),
            new Item(ItemId.VOID_KNIGHT_TOP), new Item(ItemId.RUNE_KITESHIELD), null,
            new Item(ItemId.VOID_KNIGHT_ROBE), null, new Item(ItemId.VOID_KNIGHT_GLOVES),
            new Item(ItemId.SNAKESKIN_BOOTS), null, new Item(ItemId.ARCHERS_RING_I), null},
        new Item[] {new Item(ItemId.RANGING_POTION_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.SARADOMIN_BREW_4), new Item(ItemId.SANFEW_SERUM_4),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.SANFEW_SERUM_4),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            null, null, new Item(ItemId.RUNE_POUCH)},
        -1));
    mode.loadout(new Loadout.Entry("+ Heavy Ballista", new Item(ItemId.HEAVY_BALLISTA),
        new Item(ItemId.DRAGON_JAVELIN_P_PLUS_PLUS, 8000)));
    mode.loadout(new Loadout.Entry("+ Dark Bow", new Item(ItemId.DARK_BOW),
        new Item(ItemId.DRAGON_ARROW_P_PLUS_PLUS, 8000)));
    mode.loadout(new Loadout.Entry("+ Armadyl Crossbow", new Item(ItemId.ARMADYL_CROSSBOW),
        new Item(ItemId.DRAGONSTONE_DRAGON_BOLTS_E, 8000)));
    mode.loadout(new Loadout.Entry("+ Dragon Thrownaxe", new Item(ItemId.DRAGON_THROWNAXE, 8000)));
    modes.add(mode.build());

    mode = Mode.builder().name("No Arm Main Melee").spellbook(Magic.LUNAR_MAGIC).brewCap(3);
    mode.rules(buildRules(Rule.PRAYER, RuleOption.NO_OVERHEADS, Rule.SPECIAL_ATTACKS,
        RuleOption.NO_STAFF_OF_THE_DEAD));
    mode.attackLevel(99).strengthLevel(99).rangedLevel(99).magicLevel(99).defenceLevel(99)
        .hitpointsLevel(99).prayerLevel(99);
    mode.rune(ItemId.ASTRAL_RUNE).rune(ItemId.DEATH_RUNE).rune(ItemId.EARTH_RUNE);
    mode.loadout(new Loadout.Entry("Load-out #1",
        new Item[] {new Item(ItemId.HELM_OF_NEITIZNOT), new Item(ItemId.INFERNAL_CAPE),
            new Item(ItemId.AMULET_OF_TORTURE), new Item(ItemId.ABYSSAL_TENTACLE),
            new Item(ItemId.MONKS_ROBE_TOP), new Item(ItemId.AVERNIC_DEFENDER), null,
            new Item(ItemId.MONKS_ROBE), null, new Item(ItemId.FEROCIOUS_GLOVES),
            new Item(ItemId.PRIMORDIAL_BOOTS), null, new Item(ItemId.BERSERKER_RING_I), null},
        new Item[] {null, new Item(ItemId.ANGLERFISH), new Item(ItemId.SUPER_COMBAT_POTION_4),
            new Item(ItemId.SARADOMIN_BREW_4), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.SANFEW_SERUM_4),
            new Item(ItemId.SARADOMIN_BREW_4), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.SANFEW_SERUM_4),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.RUNE_POUCH)},
        -1));
    mode.loadout(new Loadout.Entry("Load-out #2",
        new Item[] {new Item(ItemId.HELM_OF_NEITIZNOT), new Item(ItemId.INFERNAL_CAPE),
            new Item(ItemId.AMULET_OF_TORTURE), new Item(ItemId.GHRAZI_RAPIER),
            new Item(ItemId.MONKS_ROBE_TOP), new Item(ItemId.AVERNIC_DEFENDER), null,
            new Item(ItemId.MONKS_ROBE), null, new Item(ItemId.FEROCIOUS_GLOVES),
            new Item(ItemId.PRIMORDIAL_BOOTS), null, new Item(ItemId.BERSERKER_RING_I), null},
        new Item[] {null, new Item(ItemId.ANGLERFISH), new Item(ItemId.SUPER_COMBAT_POTION_4),
            new Item(ItemId.SARADOMIN_BREW_4), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.SANFEW_SERUM_4),
            new Item(ItemId.SARADOMIN_BREW_4), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.SANFEW_SERUM_4),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.RUNE_POUCH)},
        -1));
    mode.loadout(
        new Loadout.Entry("+ Dragon Dagger (P++)", new Item(ItemId.DRAGON_DAGGER_P_PLUS_PLUS)));
    mode.loadout(new Loadout.Entry("+ Armadyl Godsword", new Item(ItemId.ARMADYL_GODSWORD)));
    mode.loadout(new Loadout.Entry("+ Elder Maul", new Item(ItemId.ELDER_MAUL)));
    mode.loadout(new Loadout.Entry("+ Granite Maul", new Item(ItemId.GRANITE_MAUL)));
    modes.add(mode.build());

    mode = Mode.builder().name("F2P No Arm Main").spellbook(Magic.LUNAR_MAGIC).brewCap(0);
    mode.rules(
        buildRules(Rule.PRAYER, RuleOption.NO_OVERHEADS, Rule.IGNORE_FREEZING, RuleOption.ALLOWED));
    mode.attackLevel(99).strengthLevel(99).rangedLevel(99).magicLevel(99).defenceLevel(80)
        .hitpointsLevel(99).prayerLevel(59);
    mode.rune(ItemId.DEATH_RUNE).rune(ItemId.BLOOD_RUNE).rune(ItemId.WATER_RUNE);
    mode.loadout(new Loadout.Entry("Load-out #1",
        new Item[] {new Item(ItemId.RED_HALLOWEEN_MASK), new Item(ItemId.BLACK_CAPE),
            new Item(ItemId.STRENGTH_AMULET_T), new Item(ItemId.MAPLE_SHORTBOW),
            new Item(ItemId.MONKS_ROBE_TOP), null, null, new Item(ItemId.MONKS_ROBE), null,
            new Item(ItemId.GREEN_DHIDE_VAMB), new Item(ItemId.LEATHER_BOOTS), null, null,
            new Item(ItemId.ADAMANT_ARROW, 8000)},
        new Item[] {new Item(ItemId.SWORDFISH), new Item(ItemId.RUNE_2H_SWORD),
            new Item(ItemId.STRENGTH_POTION_4), new Item(ItemId.SWORDFISH),
            new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH),
            new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH),
            new Item(ItemId.ANCHOVY_PIZZA), new Item(ItemId.ANCHOVY_PIZZA),
            new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH),
            new Item(ItemId.ANCHOVY_PIZZA), new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH),
            new Item(ItemId.SWORDFISH), new Item(ItemId.ANCHOVY_PIZZA), new Item(ItemId.SWORDFISH),
            new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH), new Item(ItemId.ANCHOVY_PIZZA),
            new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH),
            new Item(ItemId.SWORDFISH)},
        -1));
    mode.loadout(new Loadout.Entry("Load-out #2",
        new Item[] {new Item(ItemId.RED_HALLOWEEN_MASK), new Item(ItemId.BLACK_CAPE),
            new Item(ItemId.STRENGTH_AMULET_T), new Item(ItemId.RUNE_SCIMITAR),
            new Item(ItemId.MONKS_ROBE_TOP), null, null, new Item(ItemId.MONKS_ROBE), null,
            new Item(ItemId.LEATHER_GLOVES), new Item(ItemId.LEATHER_BOOTS), null, null,
            new Item(ItemId.ADAMANT_ARROW, 8000)},
        new Item[] {new Item(ItemId.SWORDFISH), new Item(ItemId.RUNE_2H_SWORD),
            new Item(ItemId.STRENGTH_POTION_4), new Item(ItemId.SWORDFISH),
            new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH),
            new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH),
            new Item(ItemId.ANCHOVY_PIZZA), new Item(ItemId.ANCHOVY_PIZZA),
            new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH),
            new Item(ItemId.ANCHOVY_PIZZA), new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH),
            new Item(ItemId.SWORDFISH), new Item(ItemId.ANCHOVY_PIZZA), new Item(ItemId.SWORDFISH),
            new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH), new Item(ItemId.ANCHOVY_PIZZA),
            new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH), new Item(ItemId.SWORDFISH),
            new Item(ItemId.SWORDFISH)},
        -1));
    mode.loadout(new Loadout.Entry("+ Rune Scimitar", new Item(ItemId.RUNE_SCIMITAR)));
    mode.loadout(new Loadout.Entry("+ Rune Sword", new Item(ItemId.RUNE_SWORD)));
    mode.loadout(new Loadout.Entry("+ Rune Battleaxe", new Item(ItemId.RUNE_BATTLEAXE)));
    mode.loadout(new Loadout.Entry("+ Rune Longsword", new Item(ItemId.RUNE_LONGSWORD)));
    modes.add(mode.build());

    mode = Mode.builder().name("Dharoks Brid").spellbook(Magic.ANCIENT_MAGIC).brewCap(3);
    mode.rules(buildRules(Rule.PRAYER, RuleOption.NO_OVERHEADS, Rule.SPECIAL_ATTACKS,
        RuleOption.NO_STAFF_OF_THE_DEAD));
    mode.attackLevel(99).strengthLevel(99).rangedLevel(99).magicLevel(99).defenceLevel(70)
        .hitpointsLevel(99).prayerLevel(99);
    mode.rune(ItemId.DEATH_RUNE).rune(ItemId.BLOOD_RUNE).rune(ItemId.WATER_RUNE);
    mode.loadout(new Loadout.Entry("Load-out",
        new Item[] {new Item(ItemId.DHAROKS_HELM), new Item(ItemId.INFERNAL_CAPE),
            new Item(ItemId.AMULET_OF_FURY), new Item(ItemId.ABYSSAL_TENTACLE),
            new Item(ItemId.DHAROKS_PLATEBODY), new Item(ItemId.DRAGON_DEFENDER), null,
            new Item(ItemId.DHAROKS_PLATELEGS), null, new Item(ItemId.BARROWS_GLOVES),
            new Item(ItemId.DRAGON_BOOTS), null, new Item(ItemId.RING_OF_RECOIL), null},
        new Item[] {new Item(ItemId.MYSTIC_ROBE_TOP_DARK), new Item(ItemId.ANCIENT_STAFF),
            new Item(ItemId.RING_OF_RECOIL), new Item(ItemId.SUPER_COMBAT_POTION_4),
            new Item(ItemId.MYSTIC_ROBE_BOTTOM_DARK), new Item(ItemId.SPIRIT_SHIELD),
            new Item(ItemId.SANFEW_SERUM_4), new Item(ItemId.SARADOMIN_BREW_4),
            new Item(ItemId.BLACK_DHIDE_BODY), new Item(ItemId.IMBUED_SARADOMIN_CAPE),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.GRANITE_MAUL), new Item(ItemId.DHAROKS_GREATAXE),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.COOKED_KARAMBWAN),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.COOKED_KARAMBWAN), new Item(ItemId.ANGLERFISH),
            new Item(ItemId.ANGLERFISH), new Item(ItemId.ANGLERFISH), new Item(ItemId.RUNE_POUCH)},
        -1));
    mode.loadout(new Loadout.Entry("+ Dragon Warhammer", new Item(ItemId.DRAGON_WARHAMMER)));
    mode.loadout(
        new Loadout.Entry("+ Dragon Dagger (P++)", new Item(ItemId.DRAGON_DAGGER_P_PLUS_PLUS)));
    mode.loadout(new Loadout.Entry("+ Anchor", new Item(ItemId.BARRELCHEST_ANCHOR)));
    mode.loadout(new Loadout.Entry("+ Granite Maul", new Item(ItemId.GRANITE_MAUL)));
    modes.add(mode.build());

    mode = Mode.builder().name("Tentacle Stake").spellbook(Magic.STANDARD_MAGIC).brewCap(0);
    mode.rules(buildRules(Rule.PRAYER, RuleOption.DISABLED, Rule.SPECIAL_ATTACKS,
        RuleOption.DISABLED, Rule.FOOD, RuleOption.DISABLED, Rule.DRINKS, RuleOption.DISABLED));
    mode.attackLevel(99).strengthLevel(99).rangedLevel(99).magicLevel(99).defenceLevel(99)
        .hitpointsLevel(99).prayerLevel(99);
    mode.loadout(new Loadout.Entry("Load-out",
        new Item[] {null, null, null, new Item(ItemId.ABYSSAL_TENTACLE), null, null, null, null,
            null, null, null, null, null, null},
        new Item[] {null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null},
        -1));
    modes.add(mode.build());

    MODES = Collections.unmodifiableList(modes);
  }
}
