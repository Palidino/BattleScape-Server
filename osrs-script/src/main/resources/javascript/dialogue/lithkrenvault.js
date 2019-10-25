var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Select an Option";
lines.add("Room 1");
actions.add("close|teleport=1560,5074");
lines.add("Room 2");
actions.add("close|teleport=1560,5074,4");
var obj0 = new DialogueEntry();
entries.add(obj0);
obj0.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Select an Option";
lines.add("Room 1");
actions.add("teleport=1575,5074");
lines.add("Room 2");
actions.add("teleport=1575,5074,4");
var obj0 = new DialogueEntry();
entries.add(obj0);
obj0.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

instance = new DialogueScript() {
    execute: function(player, index, childId, slot) { },

    getDialogueEntries: function() {
        return entries;
    }
}
