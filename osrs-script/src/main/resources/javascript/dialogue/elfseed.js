var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Select an Option";
lines.add("Crystal Bow");
actions.add("close|script");
lines.add("Crystal Shield");
actions.add("close|script");
lines.add("Crystal Halberd");
actions.add("close|script");
var obj0 = new DialogueEntry();
entries.add(obj0);
obj0.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

instance = new DialogueScript() {
    execute: function(player, index, childId, slot) {
        if (index == 0) {
            if (!player.getInventory().hasItem(4207)) {
                player.getGameEncoder().sendMessage("You need an elf seed to do this.");
                return;
            } else if (player.getInventory().getCount(ItemId.COINS) < 150000) {
                player.getGameEncoder().sendMessage("You need 150,000 coins to do this.");
                return;
            }
            player.getInventory().deleteItem(4207, 1);
            player.getInventory().deleteItem(ItemId.COINS, 150000);
            if (slot == 0) {
                player.getInventory().addItem(4212, 1);
            } else if (slot == 1) {
                player.getInventory().addItem(4224, 1);
            } else if (slot == 2) {
                player.getInventory().addItem(13091, 1);
            }
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}
