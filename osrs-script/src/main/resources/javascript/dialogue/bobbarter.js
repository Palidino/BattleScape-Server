var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Select an Option";
lines.add("Decant Potions");
actions.add("close|script");
lines.add("Shop");
actions.add("shop=herb_exchange");
lines.add("Toggle Automatic Vial Discarding");
actions.add("close|script");
var obj0 = new DialogueEntry();
entries.add(obj0);
obj0.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

instance = new DialogueScript() {
    execute: function(player, index, childId, slot) {
        if (index == 0) {
            if (slot == 0) {
                player.getSkills().decantAllPotions();
            } else if (slot == 2) {
                player.getSkills().setAutomaticVialDiscard(!player.getSkills().getAutomaticVialDiscard());
                player.getGameEncoder().sendMessage("Automatic Vial Discarding: "
                        + player.getSkills().getAutomaticVialDiscard());
            }
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}
