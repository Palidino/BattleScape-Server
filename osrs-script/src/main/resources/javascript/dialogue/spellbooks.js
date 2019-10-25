var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Select an Option";
lines.add("Normal Magic");
actions.add("close|script");
lines.add("Ancient Magick");
actions.add("close|script");
lines.add("Lunar Magic");
actions.add("close|script");
var obj0 = new DialogueEntry();
entries.add(obj0);
obj0.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Choose spellbook:";
lines.add("Normal");
actions.add("close|script");
lines.add("Ancient");
actions.add("close|script");
lines.add("Lunar");
actions.add("close|script");
lines.add("Cancel");
actions.add("close");
var obj1 = new DialogueEntry();
entries.add(obj1);
obj1.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

instance = new DialogueScript() {
    execute: function(player, index, childId, slot) {
        if (player.isLocked()) {
            return;
        }
        if (index == 0) {
            player.getMagic().setVengeanceCast(false);
            if (slot == 0) {
                player.getMagic().setSpellbook(Magic.STANDARD_MAGIC);
            } else if (slot == 1) {
                player.getMagic().setSpellbook(Magic.ANCIENT_MAGIC);
            } else if (slot == 2) {
                player.getMagic().setSpellbook(Magic.LUNAR_MAGIC);
            }
        } else if (index == 1) {
            if (slot == 0) {
                player.getMagic().setMagicCapeSpellbook(Magic.STANDARD_MAGIC);
            } else if (slot == 1) {
                player.getMagic().setMagicCapeSpellbook(Magic.ANCIENT_MAGIC);
            } else if (slot == 2) {
                player.getMagic().setMagicCapeSpellbook(Magic.LUNAR_MAGIC);
            }
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}
