var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Select an Option";
lines.add("Manage Offers");
actions.add("close|script");
lines.add("Lookup Item");
actions.add("close|script");
lines.add("Lookup Username");
actions.add("close|script");
lines.add("Random Usernames");
actions.add("close|script");
var obj0 = new DialogueEntry();
entries.add(obj0);
obj0.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Select an Option";
var obj1 = new DialogueEntry();
entries.add(obj1);
obj1.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Title";
for (var i = 0; i < 20; i++) {
    actions.add("close|script");
}
var obj2 = new DialogueEntry();
entries.add(obj2);
obj2.setLargeSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

instance = new DialogueScript() {
    execute: function(player, index, childId, slot) {
        if (player.isLocked()) {
            return;
        }
        if (Settings.getInstance().isBeta()) {
            player.getGameEncoder().sendMessage("You can't use this on beta worlds.");
            return;
        }
        if (index == 0) {
            if (slot == 0) {
                player.getGrandExchange().open();
            } else if (slot == 1) {
                player.getGameEncoder().sendItemSearch("Search for an item", new ValueEnteredEvent.IntegerEvent() {
                    execute: function(value) {
                        RequestManager.getInstance().addGEList(player, GrandExchangeUser.LIST_ITEM, value,
                                ItemDef.getName(value));
                    }
                });
            } else if (slot == 2) {
                player.getGameEncoder().sendEnterString("Enter username:", new ValueEnteredEvent.StringEvent() {
                    execute: function(value) {
                        RequestManager.getInstance().addGEShop(player, value);
                    }
                });
            } else if (slot == 3) {
                RequestManager.getInstance().addGEList(player, GrandExchangeUser.LIST_RANDOM_PLAYERS, 0, "");
            }
        } else if (index == 2) {
            var list = player.getGrandExchange().getViewingList();
            if (list == null || slot >= list.size || list.get(slot) == null) {
                return;
            }
            var entry = list.get(slot);
            if (entry.lastIndexOf("]") != -1) {
                entry = entry.substring(entry.lastIndexOf("]") + 2);
            }
            var username = entry.substring(0, entry.indexOf(":"));
            if (username.length() == 0) {
                return;
            }
            RequestManager.getInstance().addGEShop(player, username);
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}
