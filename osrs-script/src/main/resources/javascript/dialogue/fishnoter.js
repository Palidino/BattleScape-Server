var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Select an Option";
lines.add("Note Raw Fish");
actions.add("close|script");
lines.add("Note Cooked Fish");
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
            var notedFish = false;
            if (slot == 0) {
                var darkCrabs = player.getInventory().getCount(11934);
                if (darkCrabs > 0) {
                    player.getInventory().deleteItem(11934, darkCrabs);
                    player.getInventory().addItem(11935, darkCrabs);
                    notedFish = true;
                }
                var anglerfish = player.getInventory().getCount(13439);
                if (anglerfish > 0) {
                    player.getInventory().deleteItem(13439, anglerfish);
                    player.getInventory().addItem(13440, anglerfish);
                    notedFish = true;
                }
            } else if (slot == 1) {
                var darkCrabs = player.getInventory().getCount(11936);
                if (darkCrabs > 0) {
                    player.getInventory().deleteItem(11936, darkCrabs);
                    player.getInventory().addItem(11937, darkCrabs);
                    notedFish = true;
                }
                var anglerfish = player.getInventory().getCount(13441);
                if (anglerfish > 0) {
                    player.getInventory().deleteItem(13441, anglerfish);
                    player.getInventory().addItem(13442, anglerfish);
                    notedFish = true;
                }
            }
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}
