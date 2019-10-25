var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

lines.add("<col=ff0000>WARNING</col>: Skipping too many targets in a short period of time can cause you to incur a target restriction penalty. You should not use this too frequently.");
continueLine = "Click here to continue";
actions.add("dialogue=bountyhunter,1");
var obj0 = new DialogueEntry();
entries.add(obj0);
obj0.setTextContinue(continueLine, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Do you want to skip your target?";
lines.add("Yes.");
actions.add("close|script");
lines.add("No.");
actions.add("close");
var obj1 = new DialogueEntry();
entries.add(obj1);
obj1.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Username: Single-way, Wilderness level X";
lines.add("Teleport <col=ff0000>near</col> level X <col=ff0000>single-way</col> Wilderness.");
actions.add("close|script");
lines.add("Cancel.");
actions.add("close");
var obj2 = new DialogueEntry();
entries.add(obj2);
obj2.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

instance = new DialogueScript() {
    execute: function(player, index, childId, slot) {
        if (index == 1) {
            if (slot == 0) {
                player.getCombat().getBountyHunter().abandonTarget();
            }
        } else if (index == 2) {
            if (slot == 0) {
                player.getCombat().getBountyHunter().teleportToTarget();
            }
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}
