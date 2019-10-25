var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Select an Option";
lines.add("Clanning Wilderness #1 (1 Defence Cap)");
actions.add("close|script");
lines.add("Clanning Wilderness #2 (20 Defence Cap)");
actions.add("close|script");
lines.add("Clanning Wilderness #3 (70 Defence Cap)");
actions.add("close|script");
lines.add("Clanning Wilderness #4");
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
            var x = player.getX();
            var y = player.getY();
            var height = 0;
            if (slot == 0) {
                height = 4;
            } else if (slot == 1) {
                height = 8;
            } else if (slot == 2) {
                height = 12;
            } else if (slot == 3) {
                height = 16;
            }
            var tile = new Tile(x, y, height);
            if (!player.getController().canTeleport(tile, true)) {
                return;
            }
            player.getMovement().teleport(tile);
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}

