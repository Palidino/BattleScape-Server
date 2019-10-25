var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Select an Option";
for (var i = 0; i < 40; i++) {
    lines.add("");
    actions.add("close|script");
}
var obj0 = new DialogueEntry();
entries.add(obj0);
obj0.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Select an Option";
lines.add("Open Bank");
actions.add("close|script");
lines.add("PIN Settings");
actions.add("close|script");
var obj1 = new DialogueEntry();
entries.add(obj1);
obj1.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

instance = new DialogueScript() {
    execute: function(player, index, childId, slot) {
        if (player.isLocked()) {
            return;
        }
        if (index == 0) {
            if (slot == 19 || slot == 22) {
                player.getBank().openPinSettingsConfirm(false);
            } else if (slot == 23 || slot == 26) {
                player.getBank().openPinSettingsConfirm(true);
            } else if (slot == 33) {
                if (player.getAttributeBool("remove_bank_pin")) {
                    player.getBank().deletePin();
                } else {
                    player.getBank().openPinEnter(true);
                }
            } else if (slot == 36) {
                player.removeAttribute("remove_bank_pin");
            }
        } else if (index == 1) {
            if (slot == 0) {
                player.getBank().open();
            } else if (slot == 1) {
                player.getBank().openPinSettings();
            }
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}
