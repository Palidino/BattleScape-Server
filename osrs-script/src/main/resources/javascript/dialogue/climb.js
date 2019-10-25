var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Select an Option";
lines.add("Climb Up");
actions.add("close|script");
lines.add("Climb Down");
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
                var tile = new Tile(player.getX(), player.getY(), player.getHeight() + 1);
                player.getMovement().ladderUpTeleport(tile);
            } else if (slot == 1) {
                var tile = new Tile(player.getX(), player.getY(), player.getHeight() - 1);
                player.getMovement().ladderDownTeleport(tile);
            }
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}
