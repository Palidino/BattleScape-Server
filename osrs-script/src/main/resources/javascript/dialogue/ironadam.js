var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Select an Option";
lines.add("Remove Iron Man Status");
actions.add("dialogue=ironadam,1");
lines.add("Nevermind");
actions.add("close");
var obj0 = new DialogueEntry();
entries.add(obj0);
obj0.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

lines.add("<col=ff0000>WARNING</col>: Removing your Iron Man status is permanent.");
continueLine = "Click here to continue";
actions.add("dialogue=ironadam,2");
var obj1 = new DialogueEntry();
entries.add(obj1);
obj1.setTextContinue(continueLine, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Are You Sure?";
lines.add("Remove Iron Man Status");
actions.add("close|script");
lines.add("Nevermind");
actions.add("close");
var obj2 = new DialogueEntry();
entries.add(obj2);
obj2.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

instance = new DialogueScript() {
    execute: function(player, index, childId, slot) {
        if (index == 2) {
            if (slot == 0) {
                if (!player.getBank().getPinSet()) {
                    player.getGameEncoder().sendMessage("Removing your Iron Man status requires having a PIN.");
                    return;
                } else if (player.getBank().needsPinInput(false)) {
                    return;
                }
                player.setGameMode(RsPlayer.GAME_MODE_NORMAL);
            }
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}
