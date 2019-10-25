var entries = new ArrayList();
var lines = new ArrayList();
var actions = new ArrayList();

title = "Do you want to enter? For you, it's 7,500 coins.";
lines.add("Yes");
actions.add("close|script");
lines.add("No");
actions.add("close");
var obj0 = new DialogueEntry();
entries.add(obj0);
obj0.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Select an Option";
lines.add("View Guide");
actions.add("dialogue=wilderness,4");
lines.add("Show/Hide Bounty Hunter Target Info");
actions.add("close|script");
lines.add("Show/Hide Streaks");
actions.add("close|script");
lines.add("Show/Hide Bounty Hunter Target Indicator");
actions.add("close|script");
lines.add("Kill Death Ratio");
actions.add("close|script");
lines.add("Get Skull");
actions.add("close|script");
var obj1 = new DialogueEntry();
entries.add(obj1);
obj1.setLargeSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

lines.add("Your current KDR is 0 kills and 0 deaths (0) and your best KDR is 0. Your current spree is 0 and your best spree is 0. Your total kills are 0 and your total deaths are 0.");
continueLine = "Click here to continue";
actions.add("dialogue=wilderness,3");
var obj2 = new DialogueEntry();
entries.add(obj2);
obj2.setTextContinue(continueLine, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Select an Option";
lines.add("Announce KDR");
actions.add("close|script");
lines.add("Reset KDR");
actions.add("close|script");
lines.add("Close");
actions.add("close");
var obj3 = new DialogueEntry();
entries.add(obj3);
obj3.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Select an Option";
lines.add("Blood Money");
actions.add("close|script");
lines.add("Mysterious Emblems");
actions.add("close|script");
lines.add("Slayer Tasks (Krystilia)");
actions.add("close|script");
lines.add("Chaos Elemental");
actions.add("close|script");
lines.add("Bosses and Demi-Bosses");
actions.add("close|script");
lines.add("Special Boosts, Resource Area, and Monsters");
actions.add("close|script");
var obj4 = new DialogueEntry();
entries.add(obj4);
obj4.setLargeSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

instance = new DialogueScript() {
    execute: function(player, index, childId, slot) {
        if (index == 0) {
            if (player.getInventory().getCount(ItemId.COINS) < 7500) {
                player.getGameEncoder().sendMessage("You don't have enough coins.");
                return;
            }
            var object = player.getController().getMapObject(26760, 3184, 3944, player.getClientHeight());
            if (object == null || object.isBusy() || player.getX() != object.getX() || object.getDirection() != 1) {
                return;
            }
            player.getMovement().clear();
            if (player.getY() == object.getY()) {
                player.getMovement().addMovement(object.getX(), object.getY() + 1);
            } else {
                player.getMovement().addMovement(object.getX(), object.getY());
            }
            Region.openDoor(player, object, 1, false, false);
            player.getInventory().deleteItem(ItemId.COINS, 7500);
        } else if (index == 1) {
            if (slot == 1) {
                player.getCombat().getBountyHunter().setShow(!player.getCombat().getBountyHunter().getShow());
                player.getGameEncoder().sendMessage("Bounty overlay: "
                        + player.getCombat().getBountyHunter().getShow());
            } else if (slot == 2) {
                player.getCombat().setShowKDR(!player.getCombat().showKDR());
                player.getGameEncoder().sendMessage("Streaks: " + player.getCombat().showKDR());
            } else if (slot == 3) {
                player.getCombat().getBountyHunter().setTargetIndicator(!player.getCombat().getBountyHunter()
                        .getTargetIndicator());
                player.getGameEncoder().sendMessage("Target Indicator (Arrow): "
                        + player.getCombat().getBountyHunter().getTargetIndicator());
            } else if (slot == 4) {
                player.openDialogue("wilderness", 2);
                DialogueOld.setText(player, null, "Your current KDR is " + player.getCombat().getKills()
                        + " kills and " + player.getCombat().getDeaths() + " deaths ("
                        + PNumber.formatDouble2(player.getCombat().getKDR()) + ") and your best KDR is "
                        + PNumber.formatDouble2(player.getCombat().getHighestKDR()) + ". Your current spree is "
                        + player.getCombat().getKillingSpree() + " and your best spree is "
                        + player.getCombat().getHighestKillingSpree() + ". Your total kills are "
                        + player.getCombat().getTotalKills() + " and your total deaths are "
                        + player.getCombat().getTotalDeaths() + ".");
            } else if (slot == 5) {
                player.getCombat().setPKSkullDelay(PCombat.SKULL_DELAY);
            }
        } else if (index == 3) {
            if (slot == 0) {
                player.setForceMessage("My KDR is " + player.getCombat().getKills() + "/"
                        + player.getCombat().getDeaths() + " (" + PNumber.formatDouble2(player.getCombat().getKDR())
                        + ")");
            } else if (slot == 1) {
                if (!player.inEdgeville()) {
                    player.getGameEncoder().sendMessage("You can only do this in Edgeville.");
                    return;
                }
                player.getCombat().setKills(0);
                player.getCombat().setDeaths(0);
            }
        } else if (index == 4) {
            if (slot == 0) {
                var lines = new ArrayList();
                lines.add("Blood money is used to purchase rare items from the Emblem Trader.");
                lines.add("Blood money is obtained by killing a player in the wilderness.");
                lines.add("");
                lines.add("10,000 blood money is rewarded per kill. If the kill is a Bounty");
                lines.add("Hunter target kill, an additional 2,500 blood money is rewarded.");
                lines.add("Any blood money your opponent is carrying is given to you if you");
                lines.add("kill them.");
                Scroll.open(player, "Blood Money", PString.toStringArray(lines));
            } else if (slot == 1) {
                var lines = new ArrayList();
                lines.add("Mysterious emblems are given when you kill a player. A tier 1");
                lines.add("emblem is always been, and a random tier emblem is given if the");
                lines.add("player killed was your Bounty Hunter target and was skulled. To");
                lines.add("turn an emblem in, take it to the Emblem Trader.");
                lines.add("");
                lines.add("Killing a player with emblems in your inventory will upgrade your");
                lines.add("highest tier emblem to the next tier.");
                lines.add("");
                lines.add("<col=004080>Value</col>");
                lines.add("Tier 1: 200K coins and 500 blood money");
                lines.add("Tier 2: 400K coins and 1K blood money");
                lines.add("Tier 3: 800K coins and 2K blood money");
                lines.add("Tier 4: 1.6M coins and 4K blood money");
                lines.add("Tier 5: 3M coins and 7.5K blood money");
                lines.add("Tier 6: 4.8M coins and 12K blood money");
                lines.add("Tier 7: 7M coins and 17.5K blood money");
                lines.add("Tier 8: 10M coins and 25K blood money");
                lines.add("Tier 9: 14M coins and 35K blood money");
                lines.add("Tier 10: 20M coins and 50K blood money");
                Scroll.open(player, "Mysterious Emblems", PString.toStringArray(lines));
            } else if (slot == 2) {
                var lines = new ArrayList();
                lines.add("You can get a wilderness Slayer task be speaking to Krystilia");
                lines.add("located in Edgeville. Wilderness Slayer tasks are seperate");
                lines.add("normal and boss tasks. There are multiple unique monsters");
                lines.add("in the wilderness that can be assigned, such as abyssal");
                lines.add("demons, lizardmen, and smoke devils.");
                lines.add("");
                lines.add("Benefits");
                lines.add("25% more coins");
                lines.add("10% more combat and slayer experience");
                lines.add("Additional drop table roll");
                lines.add("Blood money based on the monster's combat level");
                lines.add("Chance of a mysterious emblem");
                lines.add("Chance of a Slayer's enchantment");
                lines.add("Chance of a dark crab or prayer potion(3)");
                lines.add("Blood money once a task is completed");
                lines.add("A carried mysterious emblem upgraded");
                Scroll.open(player, "Slayer", PString.toStringArray(lines));
            } else if (slot == 3) {
                var lines = new ArrayList();
                lines.add("The Chaos Elemental spawns at 12AM, 6AM, 12PM, and 6PM. The");
                lines.add("time is based on the clock found in the quest tab.");
                lines.add("");
                lines.add("It spawns near the following locations: Rogues' Castle, King");
                lines.add("Black Dragon dungeon entrance, Lava Dragon Isle, and Bone Yard.");
                lines.add("");
                lines.add("The Chaos Elemental has 1,000 hitpoints and will rotate combat");
                lines.add("styles it's weak to. It uses a handful of different attacks,");
                lines.add("including mechanics from all three wilderness bosses. Attacking");
                lines.add("it will skull you.");
                lines.add("");
                lines.add("Using the wrong attack style will reduce your damage to 60% and");
                lines.add("increase its defence x4 for your attacks. Attacking it from a");
                lines.add("single combat area will reduce your damage to 6.");
                lines.add("");
                lines.add("When it reaches half of its hitpoints, it will spawn three Chaos");
                lines.add("Fanatics. They must be killed before the Chaos Elemental will take");
                lines.add("any more damage.");
                lines.add("");
                lines.add("Once killed, loot will be given to up to three players who did the");
                lines.add("most damage. These players will be given a 2:30 teleblock and a 2:30");
                lines.add("protect item block.");
                lines.add("");
                lines.add("Possible loot includes are rare items found in the game. If your");
                lines.add("game mode isn't normal, the most rare items are removed from the");
                lines.add("potential loot table.");
                Scroll.open(player, "Chaos Elemental", PString.toStringArray(lines));
            } else if (slot == 4) {
                var lines = new ArrayList();
                lines.add("<col=004080>Bosses</col>");
                lines.add("The three bosses are Callisto, Venenatis, and Vet'ion.");
                lines.add("");
                lines.add("Callisto is located south of the Demonic Ruins, Venenatis is");
                lines.add("located east of the Bone Yard, and Vet'ion is located north of");
                lines.add("the Bone Yard and south of Lava Dragon Isle.");
                lines.add("");
                lines.add("All three drop ancient warriors' armour and weapons");
                lines.add("(PvP equipment), dragon pickaxe, and dragon 2h sword. Callisto");
                lines.add("drops the tyrannical ring, Venenatis drops the treasonous ring,");
                lines.add("and Vet'ion drops the ring of the gods.");
                lines.add("");
                lines.add("<col=004080>Demi-Bosses</col>");
                lines.add("The three demi-bosses are the Chaos Fanatic, the Crazy");
                lines.add("Archaeologist, and Scorpia.");
                lines.add("");
                lines.add("All three drop the odium and malediction wards.");
                Scroll.open(player, "Bosses and Demi-Bosses", PString.toStringArray(lines));
            } else if (slot == 5) {
                var lines = new ArrayList();
                lines.add("<col=004080>Experience</col>");
                lines.add("Slayer: 10%");
                lines.add("Combat from Slayer: 10%");
                lines.add("Resource Area Skilling: 10%");
                lines.add("");
                lines.add("<col=004080>Skilling Success:</col> 10%");
                lines.add("");
                lines.add("<col=004080>Resource Area</col>");
                lines.add("This area requires 7.5K to enter.");
                lines.add("Inside, you'll find yew and magic trees, adamant and rune rocks,");
                lines.add("a furnace and anvil, dark crabs and anglerfish, and a fire.");
                lines.add("All rocks, include runite, will respawn twice as fast.");
                lines.add("Piles can be found inside and will note items for 50 coins each.");
                lines.add("Anglerfish are caught at double the speed, and both anglerfish and");
                lines.add("dark crabs give two fish per catch.");
                Scroll.open(player, "Special Boosts, Resource Area, and Monsters", PString.toStringArray(lines));
            }
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}
