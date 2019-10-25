var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Select an Option";
lines.add("Start");
actions.add("script|close");
var obj0 = new DialogueEntry();
entries.add(obj0);
obj0.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

instance = new DialogueScript() {
    execute: function(player, index, childId, slot) {
        if (player.isLocked()) {
            return;
        }
        if (index == 0) {
            var itemId = player.getAttributeInt("item_id_" + childId);
            if (itemId == 0) {
                return;
            }
            Smithing.start(player, itemId, slot);
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}
