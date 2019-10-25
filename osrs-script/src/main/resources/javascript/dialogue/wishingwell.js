var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Select an Option";
lines.add("Wishing Well");
actions.add("dialogue=wishingwell,1");
lines.add("PK Raffle");
actions.add("dialogue=wishingwell,3");
var obj0 = new DialogueEntry();
entries.add(obj0);
obj0.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Select an Option";
lines.add("View Stats");
actions.add("close|script");
lines.add("Buy Entry");
actions.add("close|script");
lines.add("Donate");
actions.add("dialogue=wishingwell,2");
lines.add("Collect");
actions.add("close|script");
var obj1 = new DialogueEntry();
entries.add(obj1);
obj1.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Select an Option";
lines.add("Donate Directly into Pot");
actions.add("close|script");
lines.add("Donate for Boosts"); // (50% Exchange Rate)
actions.add("close|script");
var obj2 = new DialogueEntry();
entries.add(obj2);
obj2.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Select an Option";
lines.add("View Stats");
actions.add("close|script");
lines.add("Collect");
actions.add("close|script");
var obj3 = new DialogueEntry();
entries.add(obj3);
obj3.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Item Name: Value";
lines.add("Put item in the well for boosts.");
actions.add("script");
lines.add("Nevermind.");
actions.add("close");
var obj4 = new DialogueEntry();
entries.add(obj4);
obj4.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

instance = new DialogueScript() {
    execute: function(player, index, childId, slot) {
        if (player.isLocked()) {
            return;
        }
        if (index == 1) {
            if (slot == 0) {
                WishingWell.viewStats(player);
            } else if (slot == 1) {
                WishingWell.addEntry(player);
            } else if (slot == 3) {
                WishingWell.collect(player);
            }
        } else if (index == 2) {
            if (slot == 0) {
                player.getGameEncoder().sendEnterAmount(new ValueEnteredEvent.IntegerEvent() {
                    execute: function(value) {
                        WishingWell.donate(player, value, false);
                    }
                });
            } else if (slot == 1) {
                player.getGameEncoder().sendEnterAmount(new ValueEnteredEvent.IntegerEvent() {
                    execute: function(value) {
                        WishingWell.donate(player, value, true);
                    }
                });
            }
        } else if (index == 3) {
            if (slot == 0) {
                PKRaffle.viewStats(player);
            } else if (slot == 1) {
                PKRaffle.collect(player);
            }
        } else if (index == 4) {
            var itemId = player.getAttributeInt("wishing_well_item_id");
            player.getWidgetManager().removeInteractiveWidgets();
            WishingWell.donateItemForBoost(player, itemId);
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}
