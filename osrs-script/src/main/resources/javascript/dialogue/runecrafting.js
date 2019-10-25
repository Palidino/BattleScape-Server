var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Select an Option";
lines.add("Air Altar");
actions.add("close|script");
lines.add("Water Altar");
actions.add("close|script");
lines.add("Earth Altar");
actions.add("close|script");
lines.add("Fire Altar");
actions.add("close|script");
var obj0 = new DialogueEntry();
entries.add(obj0);
obj0.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

instance = new DialogueScript() {
    execute: function(player, index, childId, slot) {
        if (index == 0) {
            if (slot == 0) {
                Runecrafting.talismanTeleport(player, Runecrafting.Altar.AIR);
            } else if (slot == 1) {
                Runecrafting.talismanTeleport(player, Runecrafting.Altar.WATER);
            } else if (slot == 2) {
                Runecrafting.talismanTeleport(player, Runecrafting.Altar.EARTH);
            } else if (slot == 3) {
                Runecrafting.talismanTeleport(player, Runecrafting.Altar.FIRE);
            }
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}
