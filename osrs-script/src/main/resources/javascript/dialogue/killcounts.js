var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Select an Option";
lines.add("Slayer Kill Log");
actions.add("close|script");
lines.add("Boss Kill Log");
actions.add("close|script");
lines.add("Rare Loot Log");
actions.add("close|script");
lines.add("Rare Loot Rates");
actions.add("close|script");
var obj0 = new DialogueEntry();
entries.add(obj0);
obj0.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Rare Loot Log";
for (var i = 0; i < 100; i++) {
    actions.add("close|script");
}
var obj1 = new DialogueEntry();
entries.add(obj1);
obj1.setLargeSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Rare Loot Rates";
for (var i = 0; i < 100; i++) {
    actions.add("close|script");
}
var obj2 = new DialogueEntry();
entries.add(obj2);
obj2.setLargeSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

instance = new DialogueScript() {
    execute: function(player, index, childId, slot) {
        if (index == 0) {
            if (slot == 0) {
                var names = new ArrayList();
                names.add("Crawling hand");
                names.add("Cave crawler");
                names.add("Banshee");
                names.add("Rockslug");
                names.add("Cockatrice");
                names.add("Pyrefiend");
                names.add("Basilisk");
                names.add("Infernal Mage");
                names.add("Brine rat");
                names.add("Bloodveld");
                names.add("Jelly");
                names.add("Turoth");
                names.add("Cave horror");
                names.add("Aberrant spectre");
                names.add("Dust devil");
                names.add("Fossil Island wyvern");
                names.add("Kurask");
                names.add("Skeletal wyvern");
                names.add("Gargoyle");
                names.add("Brutal black dragon");
                names.add("Nechryael");
                names.add("Spiritual mage");
                names.add("Abyssal demon");
                names.add("Cave kraken");
                names.add("Dark beast");
                names.add("Smoke devil");
                var streakList = "";
                var countList = "";
                var nameList = "";
                for (var i = 0; i < names.size(); i++) {
                    var name = names.get(i);
                    streakList += "0";
                    countList += player.getCombat().getNPCKillCount(name);
                    nameList += name;
                    streakList += "|";
                    countList += "|";
                    nameList += "|";
                }
                player.getWidgetManager().sendInteractiveOverlay(WidgetId.KILL_LOG);
                player.getGameEncoder().sendScript(1584, "Slayer Kill Log", names.size(), streakList, countList,
                        nameList);
            } else if (slot == 1) {
                var names = new ArrayList();
                names.add("Kree'arra");
                names.add("Commander Zilyana");
                names.add("General Graardor");
                names.add("K'ril Tsutsaroth");
                names.add("Dagannoth Rex");
                names.add("Dagannoth Prime");
                names.add("Dagannoth Supreme");
                names.add("King Black Dragon");
                names.add("Callisto");
                names.add("Venenatis");
                names.add("Vet'ion");
                names.add("Scorpia");
                names.add("Crazy archaeologist");
                names.add("Chaos Fanatic");
                names.add("Chaos Elemental");
                names.add("Barrows chest");
                names.add("Corporeal Beast");
                names.add("Zulrah");
                names.add("Thermonuclear smoke devil");
                names.add("Cerberus");
                names.add("Abyssal Sire");
                names.add("Giant Mole");
                names.add("Deranged archaeologist");
                names.add("Grotesque Guardians");
                names.add("Kraken");
                var streakList = "";
                var countList = "";
                var nameList = "";
                for (var i = 0; i < names.size(); i++) {
                    var name = names.get(i);
                    streakList += "0";
                    countList += player.getCombat().getNPCKillCount(name);
                    nameList += name;
                    streakList += "|";
                    countList += "|";
                    nameList += "|";
                }
                player.getWidgetManager().sendInteractiveOverlay(WidgetId.KILL_LOG);
                player.getGameEncoder().sendScript(1584, "Boss Kill Log", names.size(), streakList, countList,
                        nameList);
            } else if (slot == 2) {
                player.getCombat().openNPCKillCountList();
            } else if (slot == 3) {
                player.getCombat().openNPCRareLootList();
            }
        } else if (index == 1) {
            player.getCombat().openNPCKillCountLog(slot);
        } else if (index == 2) {
            player.getCombat().openNPCRateLootTable(slot);
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}
