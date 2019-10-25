var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Select an Option";
lines.add("Collect");
actions.add("close|script");
lines.add("View Shop");
actions.add("close|script");
lines.add("Obtaining");
actions.add("close|script");
var obj0 = new DialogueEntry();
entries.add(obj0);
obj0.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

instance = new DialogueScript() {
    execute: function(player, index, childId, slot) {
        if (player.isLocked()) {
            return;
        }
        if (index == 0) {
            if (slot == 0) {
                var hours = player.getLoyaltyHours();
                if (hours == 0) {
                    player.getGameEncoder().sendMessage("There is currently nothing for you to be given.");
                    return;
                }
                player.setLoyaltyHours(0);
                player.getInventory().addOrDropItem(ItemId.COINS, 100000 * hours);
                player.getInventory().addOrDropItem(ItemId.LOYALTY_TICKET_32287, 10 * hours);
            } else if (slot == 1) {
                player.openShop("loyalty");
            } else if (slot == 2) {
                player.getGameEncoder().sendMessage("Every hour of time played builds your loyalty. The only requirement is that you've voted.");
            }
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}
