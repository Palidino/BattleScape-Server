var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Return to Zulrah's shrine?";
lines.add("Yes.");
actions.add("close|script");
lines.add("No.");
actions.add("close");
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
                player.setController(new ZulrahPC());
                player.getController().instance();
                player.getMovement().teleport(2268, 3068);
            }
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}
