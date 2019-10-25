var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Guide Book";
lines.add("Join 'Home' Clan Chat");
actions.add("close|script");
lines.add("Join Discord Chat");
actions.add("close|script");
lines.add("Store and Bonds");
actions.add("dialogue=bond,5");
lines.add("The Rules");
actions.add("close|script");
lines.add("Setting Combat Levels");
actions.add("close|script");
lines.add("Shops");
actions.add("close|script");
lines.add("Teleporting");
actions.add("close|script");
lines.add("Untradeable Items");
actions.add("close|script");
lines.add("Voting");
actions.add("close|script");
lines.add("Maximizing Boosts");
actions.add("close|script");
lines.add("Wilderness Guide");
actions.add("dialogue=wilderness,4");
lines.add("PvM/Slayer");
actions.add("close|script");
lines.add("PvM Kill and Loot Logs");
actions.add("close|dialogue=killcounts,0");
lines.add("Monster Rare Loot Rates");
actions.add("close|script");
var obj0 = new DialogueEntry();
entries.add(obj0);
obj0.setLargeSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

instance = new DialogueScript() {
    execute: function(player, index, childId, slot) {
        if (player.isLocked()) {
            return;
        }
        if (index == 0) {
            if (slot == 0) {
                player.getMessaging().joinClan(Main.getName().toLowerCase());
            } else if (slot == 1) {
                player.getGameEncoder().sendOpenURL(Main.getSettings().getDiscordUrl());
            } else if (slot == 1) {
                player.getGameEncoder().sendOpenURL(Main.getSettings().getRulesUrl());
            } else if (slot == 2) {
                var lines = new ArrayList();
                lines.add("You can set your combat levels by selecting them on the skills");
                lines.add("interface. Please note this is't possible if your game mode is");
                lines.add("hard or ironman.");
                Scroll.open(player, "Combat Levels", PString.toStringArray(lines));
            } else if (slot == 3) {
                var lines = new ArrayList();
                lines.add("Shops can be located inside the Edgeville General Store.");
                lines.add("");
                lines.add("Thessalia: Clothes and Amulets");
                lines.add("Horvik: Melee");
                lines.add("Lowe: Ranged");
                lines.add("Aubury: Magic");
                lines.add("Shop Keeper: Supplies");
                lines.add("Probita: Pets");
                lines.add("Mac: Capes of Accomplishment");
                lines.add("Skilling Seller: Skills");
                lines.add("Cap'n Izzy No-Beard: Agility");
                Scroll.open(player, "Shops", PString.toStringArray(lines));
            } else if (slot == 4) {
                var lines = new ArrayList();
                lines.add("The Home teleport spell on your magic spellbook will teleport");
                lines.add("you back home.");
                lines.add("");
                lines.add("You can teleport around the game by talking to the Wizard");
                lines.add("located just west of the Edgeville bank. He will list most");
                lines.add("locations, with a few exceptions such as the King Black Dragon.");
                lines.add("");
                lines.add("You can also purchase teleport tabs in the General Store.");
                Scroll.open(player, "Teleporting", PString.toStringArray(lines));
            } else if (slot == 5) {
                var lines = new ArrayList();
                lines.add("");
                lines.add("Armoured Gloves: Recipe for Disaster");
                lines.add("Ava's Accumulator: Pest Control");
                lines.add("Ava's Attractor: Lowe");
                lines.add("Barbarian Assault Equipment: Pest Control");
                lines.add("Castle Wars Equipment: Pest Control");
                lines.add("Defenders: Warriors' Guild");
                lines.add("Fire Cape: TzHaar Fight Cave");
                lines.add("God Books: Horror from the Deep");
                lines.add("God Cape: Mage Arena");
                lines.add("God Staff: Mage Arena");
                lines.add("Infernal Cape: TzHaar Inferno");
                lines.add("Rune Pouch: Boss Slayer");
                lines.add("Void Knight Equipment: Pest Control");
                lines.add("");
                lines.add("Some untradables can be alternatively obtained through the");
                lines.add("blood money shop.");
                lines.add("");
                lines.add("Perdu, located in the Edgeville bank, can repair broken");
                lines.add("untradable items and can also toggle automatic repairing of");
                lines.add("broken untradable items.");
                Scroll.open(player, "Untradeable Items", PString.toStringArray(lines));
            } else if (slot == 6) {
                var lines = new ArrayList();
                lines.add("You can vote for " + Main.getName() + " which will reward you with Vote");
                lines.add("Tickets. You can exchange vote tickets with the Vote manager");
                lines.add("located in the Edgeville bank for various items.");
                lines.add("");
                lines.add("Every time you vote, you have a 1 in 8,000 chance at obtaining");
                lines.add("an inverted santa hat.");
                lines.add("");
                lines.add("Benefits for voting in the last 12 hours include:");
                lines.add("x10% Drop Rates Including Pets");
                lines.add("5% Increased Skilling Success Rates");
                lines.add("x5% XP Rates");
                lines.add("");
                lines.add("If you wish to vote, you can click on the Vote Button location");
                lines.add("inside the main quest tab.");
                lines.add("");
                lines.add("Alternatively, please visit: " + Main.getSettings().getVoteUrl());
                Scroll.open(player, "Voting", PString.toStringArray(lines));
            } else if (slot == 8) {
                var lines = new ArrayList();
                lines.add("Bonuses are added together before being multiplied.");
                lines.add("");
                lines.add("<col=004080>Active Modifiers</col>");
                lines.add("Voted: " + player.hasVoted());
                lines.add("Wishing Well: " + WishingWell.isDonationBoostActive());
                lines.add("Premium Membership: " + player.isPremiumMember());
                lines.add("Ring of Wealth (i): " + player.getCharges().hasRoWICharge(0));
                lines.add("Hard Mode: " + player.isGameModeHard());
                lines.add("Set Combat Levels 25% Penalty: " + !player.getSkills().withinCombatLevelsAchieved());
                lines.add("Drop Rate Multiplier (Items): " + player.getCombat().getDropRateMultiplier(-1, null));
                lines.add("Drop Rate Multiplier (Pets): " + player.getCombat().getDropRateMultiplier(6662, null));
                lines.add("");
                lines.add("<col=004080>Drop Rate Modifiers</col>");
                lines.add("Voting: 10% (10% for Pets)");
                lines.add("Wishing Well: 10% (10% for Pets)");
                lines.add("Premium Membership: 10% (10% for Pets)");
                lines.add("Ring of Wealth (i): 10% (10% for Pets)");
                lines.add("Wilderness Slayer: 10% (10% for Pets)");
                lines.add("Hard Mode: 20% (0% for Pets)");
                lines.add("Set Combat Levels: -25%");
                lines.add("");
                lines.add("<col=004080>Experience</col>");
                lines.add("Voting: 5%");
                lines.add("Wishing Well: 10%");
                lines.add("Premium Membership: 5%");
                lines.add("Skilling Outfit: 10%");
                lines.add("Cooking Gauntlets: 10%");
                lines.add("Wilderness Slayer: 10%");
                lines.add("Wilderness Resource Area: 10%");
                lines.add("");
                lines.add("<col=004080>Skilling Success</col>");
                lines.add("Voting: 5%");
                lines.add("Premium Membership: 5%");
                lines.add("Skilling Outfit: 10%");
                lines.add("Wilderness: 10%");
                lines.add("<col=004080>Cooking:</col> Cooking Gauntlets: 10%, Cooking Cape: 100%");
                lines.add("<col=004080>Fishing:</col> Dragon/Infernal Harpoon: 20%, Guild: +7 Levels");
                lines.add("<col=004080>Mining:</col> Guild: +7 Levels");
                lines.add("<col=004080>Woodcutting:</col> Guild: +7 Levels");
                lines.add("<col=004080>Hunter - Net Catching:</col> Magic Butterfly Net: 10%");
                lines.add("<col=004080>Thieving:</col> Ardougne Cloak 4: 10%, Thieving Cape: 10%");
                lines.add("");
                lines.add("<col=004080>Boss Instances</col>");
                lines.add("<col=004080>Accuracy:</col> 12.5%");
                lines.add("<col=004080>Strength:</col> 2.5%");
                lines.add("<col=004080>Defence:</col> 2.5%");
                Scroll.open(player, "Maximizing Boosts", PString.toStringArray(lines));
            } else if (slot == 10) {
                var lines = new ArrayList();
                lines.add("PvM and Slayer is one of the best money makers in game. Killing");
                lines.add("monsters for coins is good in itself, but on top of that Slayer");
                lines.add("can reward you with Slayer Points and rare items.");
                lines.add("");
                lines.add("You can begin Slayer by speaking to Nieve located in the");
                lines.add("Edgeville Bank. You can choose which Slayer Master you want,");
                lines.add("however it is recommended you use the highest available for you.");
                lines.add("");
                lines.add("After you have been given an assignment, you will be informed");
                lines.add("in the chatbox of the location of the monster you have been");
                lines.add("assigned to kill. If you ever forget this location you can simply");
                lines.add("click on your Slayer task, found within the red entry in the");
                lines.add("quest tab.");
                lines.add("");
                lines.add("Completing a Slayer task will reward you with Slayer Points");
                lines.add("which can purchase rewards from Nieve.");
                lines.add("");
                lines.add("The Slayer ring can provide up to 8 teleports directly to your");
                lines.add("task, and can also be charged with Slayer points which will note");
                lines.add("bones monsters drop. It can be combined with an eternal gem to give");
                lines.add("unlimited teleports.");
                Scroll.open(player, "PvM/Slayer", PString.toStringArray(lines));
            } else if (slot == 12) {
                player.getCombat().openNPCRareLootList();
            }
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}
