var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Loadout Options";
for (var i = 0; i < 15; i++) {
    lines.add("Add New Loadout");
    actions.add("close|script");
}
var obj0 = new DialogueEntry();
entries.add(obj0);
obj0.setLargeSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Select an Option";
lines.add("Load");
actions.add("close|script");
lines.add("Save As Quick Loadout");
actions.add("close|script");
lines.add("Replace");
actions.add("close|script");
lines.add("Delete");
actions.add("dialogue=loadout,3");
var obj1 = new DialogueEntry();
entries.add(obj1);
obj1.setLargeSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Select an Option";
lines.add("Combat Levels: Ignore");
actions.add("close|script");
lines.add("Equipment: Save");
actions.add("close|script");
lines.add("Inventory: Save");
actions.add("close|script");
lines.add("Rune Pouch: Save");
actions.add("close|script");
lines.add("Spell Book: Save");
actions.add("close|script");
lines.add("Add Loadout");
actions.add("close|script");
var obj2 = new DialogueEntry();
entries.add(obj2);
obj2.setLargeSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Select an Option";
lines.add("Delete Loadout");
actions.add("close|script");
lines.add("Nevermind");
actions.add("close");
var obj3 = new DialogueEntry();
entries.add(obj3);
obj3.setLargeSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Select an Option";
lines.add("Equipment: Save");
actions.add("close|script");
lines.add("Inventory: Save");
actions.add("close|script");
lines.add("Rune Pouch: Save");
actions.add("close|script");
lines.add("Spell Book: Save");
actions.add("close|script");
lines.add("Add Loadout");
actions.add("close|script");
var obj4 = new DialogueEntry();
entries.add(obj4);
obj4.setLargeSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

instance = new DialogueScript() {
    execute: function(player, index, childId, slot) {
        if (index == 0) {
            if (player.getBank().needsPinInput(false)) {
                return;
            }
            player.putAttribute("loadout_slot", slot);
            player.putAttribute("loadout_skills", false);
            player.putAttribute("loadout_equipment", true);
            player.putAttribute("loadout_inventory", true);
            player.putAttribute("loadout_rune_puch", player.getInventory().hasItem(ItemId.RUNE_POUCH));
            player.putAttribute("loadout_spellbook", true);
            if (slot >= player.getCombat().getCustomLoadoutsSize()) {
                player.openDialogue("loadout", 4);
                DialogueOld.setText(player, null,
                        "Equipment: " + (player.getAttributeBool("loadout_equipment") ? "Save" : "Ignore"),
                        "Inventory: " + (player.getAttributeBool("loadout_inventory") ? "Save" : "Ignore"),
                        "Rune Pouch: " + (player.getAttributeBool("loadout_rune_puch") ? "Save" : "Ignore"),
                        "Spell Book: " + (player.getAttributeBool("loadout_spellbook") ? "Save" : "Ignore"),
                        "Add Loadout");
            } else {
                player.openDialogue("loadout", 1);
            }
        } else if (index == 1) {
            var loadoutSlot = player.getAttributeInt("loadout_slot");
            if (slot == 0 || slot == 2) {
                if (slot == 0 && player.getCombat().getCustomLoadoutName(loadoutSlot) != null) {
                    player.getCombat().loadCustomLoadout(loadoutSlot);
                    return;
                }
                player.openDialogue("loadout", 4);
            } else if (slot == 1) {
                player.getWidgetManager().setQuickLoadoutIndex(loadoutSlot);
                player.getGameEncoder().sendMessage("Loadout has been saved as a quick loadout.");
            }
        } else if (index == 2) {
            if (slot == 0) {
                player.putAttribute("loadout_skills", !player.getAttributeBool("loadout_skills"));
            } else if (slot == 1) {
                player.putAttribute("loadout_equipment", !player.getAttributeBool("loadout_equipment"));
            } else if (slot == 2) {
                player.putAttribute("loadout_inventory", !player.getAttributeBool("loadout_inventory"));
            } else if (slot == 3) {
                player.putAttribute("loadout_rune_puch", !player.getAttributeBool("loadout_rune_puch"));
            } else if (slot == 4) {
                player.putAttribute("loadout_spellbook", !player.getAttributeBool("loadout_spellbook"));
            } else if (slot == 5) {
                player.getGameEncoder().sendEnterString("Enter Loadout Name:", new ValueEnteredEvent.StringEvent() {
                    execute: function(value) {
                        player.getCombat().addCustomLoadout(player.getAttributeInt("loadout_slot"),
                                new CustomLoadout(player, value));
                        player.openDialogue("loadout", 0);
                        DialogueOld.setText(player, null, player.getCombat().getCustomLoadoutsDialgoue());
                    }
                });
                return;
            }
            player.openDialogue("loadout", 2);
            DialogueOld.setText(player, null,
                    "Combat Levels: " + (player.getAttributeBool("loadout_skills") ? "Save" : "Ignore"),
                    "Equipment: " + (player.getAttributeBool("loadout_equipment") ? "Save" : "Ignore"),
                    "Inventory: " + (player.getAttributeBool("loadout_inventory") ? "Save" : "Ignore"),
                    "Rune Pouch: " + (player.getAttributeBool("loadout_rune_puch") ? "Save" : "Ignore"),
                    "Spell Book: " + (player.getAttributeBool("loadout_spellbook") ? "Save" : "Ignore"),
                    "Add Loadout");
        } else if (index == 3) {
            if (player.getBank().needsPinInput(false)) {
                return;
            }
            if (slot == 0) {
                player.getCombat().clearCustomLoadout(player.getAttributeInt("loadout_slot"));
                player.openDialogue("loadout", 0);
                DialogueOld.setText(player, null, player.getCombat().getCustomLoadoutsDialgoue());
            }
        } else if (index == 4) {
            if (slot == 0) {
                player.putAttribute("loadout_equipment", !player.getAttributeBool("loadout_equipment"));
            } else if (slot == 1) {
                player.putAttribute("loadout_inventory", !player.getAttributeBool("loadout_inventory"));
            } else if (slot == 2) {
                player.putAttribute("loadout_rune_puch", !player.getAttributeBool("loadout_rune_puch"));
            } else if (slot == 3) {
                player.putAttribute("loadout_spellbook", !player.getAttributeBool("loadout_spellbook"));
            } else if (slot == 4) {
                player.getGameEncoder().sendEnterString("Enter Loadout Name:", new ValueEnteredEvent.StringEvent() {
                    execute: function(value) {
                        player.getCombat().addCustomLoadout(player.getAttributeInt("loadout_slot"),
                                new CustomLoadout(player, value));
                        player.openDialogue("loadout", 0);
                        DialogueOld.setText(player, null, player.getCombat().getCustomLoadoutsDialgoue());
                    }
                });
                return;
            }
            player.openDialogue("loadout", 4);
            DialogueOld.setText(player, null,
                    "Equipment: " + (player.getAttributeBool("loadout_equipment") ? "Save" : "Ignore"),
                    "Inventory: " + (player.getAttributeBool("loadout_inventory") ? "Save" : "Ignore"),
                    "Rune Pouch: " + (player.getAttributeBool("loadout_rune_puch") ? "Save" : "Ignore"),
                    "Spell Book: " + (player.getAttributeBool("loadout_spellbook") ? "Save" : "Ignore"),
                    "Add Loadout");
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}
