var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Select an Option";
lines.add("Start From Boosted");
actions.add("close|script");
lines.add("Start From Beginning");
actions.add("close|script");
var obj0 = new DialogueEntry();
entries.add(obj0);
obj0.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Select an Option";
lines.add("Exchange Fire Cape for 8,000 Tokkul");
actions.add("dialogue=tzhaar,2");
lines.add("Exchange Fire Cape for 1/200 Pet Chance");
actions.add("dialogue=tzhaar,3");
var obj1 = new DialogueEntry();
entries.add(obj1);
obj1.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Select an Option";
lines.add("Exchange Fire Cape for 8,000 Tokkul");
actions.add("close|script");
lines.add("Nevermind");
actions.add("close");
var obj2 = new DialogueEntry();
entries.add(obj2);
obj2.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Select an Option";
lines.add("Exchange Fire Cape for 1/200 Pet Chance");
actions.add("close|script");
lines.add("Nevermind");
actions.add("close");
var obj3 = new DialogueEntry();
entries.add(obj3);
obj3.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Select an Option";
lines.add("Start From Boosted");
actions.add("close|script");
lines.add("Start From Beginning");
actions.add("close|script");
lines.add("Fight 3 Jads (Practice)");
actions.add("close|script");
lines.add("Fight TzKal-Zuk (Practice)");
actions.add("close|script");
var obj4 = new DialogueEntry();
entries.add(obj4);
obj4.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Sacrifice Fire Cape";
lines.add("Yes.");
actions.add("close|script");
lines.add("No.");
actions.add("close");
var obj5 = new DialogueEntry();
entries.add(obj5);
obj5.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Exchange Infernal Cape for 1/50 Pet Chance";
lines.add("Yes.");
actions.add("close|script");
lines.add("No.");
actions.add("close");
var obj6 = new DialogueEntry();
entries.add(obj6);
obj6.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

instance = new DialogueScript() {
    execute: function(player, index, childId, slot) {
        if (index == 0) {
            if (slot == 0) {
                if (!player.isGameModeNormal() && !player.isGameModeHard() && !player.getCombat().getTzHaar().getFightCave()) {
                    player.getGameEncoder().sendMessage("You can't do that until you've completed the Fight Cave.");
                    return;
                }
                player.getCombat().getTzHaar().startFightCave(0);
            } else if (slot == 1) {
                player.getCombat().getTzHaar().startFightCave(1);
            }
        } else if (index == 2) {
            if (slot == 0) {
                if (player.getInventory().getCount(ItemId.FIRE_CAPE) == 0) {
                    player.getGameEncoder().sendMessage("You need a Fire cape to do this.");
                    return;
                }
                player.getInventory().deleteItem(ItemId.FIRE_CAPE, 1);
                player.getInventory().addItem(ItemId.TOKKUL, 8000);
            }
        } else if (index == 3) {
            if (slot == 0) {
                if (!player.getInventory().hasItem(ItemId.FIRE_CAPE)) {
                    player.getGameEncoder().sendMessage("You need a Fire cape to do this.");
                    return;
                }
                player.getInventory().deleteItem(ItemId.FIRE_CAPE, 1);
                if (!player.getFamiliar().rollPet(ItemId.TZREK_JAD, 0.5)) {
                    player.getGameEncoder().sendMessage("It seems luck is not on your side!");
                }
            }
        } else if (index == 4) {
            if (slot == 0) {
                player.getCombat().getTzHaar().startInferno(0);
            } else if (slot == 1) {
                player.getCombat().getTzHaar().startInferno(1);
            } else if (slot == 2) {
                player.getCombat().getTzHaar().startInferno(-1);
            } else if (slot == 3) {
                player.getCombat().getTzHaar().startInferno(-2);
            }
        } else if (index == 5) {
            if (slot == 0) {
                if (!player.getInventory().hasItem(ItemId.FIRE_CAPE)) {
                    player.getGameEncoder().sendMessage("You need a Fire Cape to do this.");
                    return;
                }
                player.getGameEncoder().sendMessage("You sacrifice your Fire Cape, allowing you access to The Inferno...");
                player.getInventory().deleteItem(ItemId.FIRE_CAPE, 1);
                player.getCombat().getTzHaar().setInfernoSacrificedCape(true);
            }
        } else if (index == 6) {
            if (slot == 0) {
                if (!player.getInventory().hasItem(ItemId.INFERNAL_CAPE)) {
                    player.getGameEncoder().sendMessage("You need an Infernal cape to do this.");
                    return;
                }
                player.getInventory().deleteItem(ItemId.INFERNAL_CAPE, 1);
                if (!player.getFamiliar().rollPet(ItemId.JAL_NIB_REK, 0.5)) {
                    player.getGameEncoder().sendMessage("It seems luck is not on your side!");
                }
            }
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}
