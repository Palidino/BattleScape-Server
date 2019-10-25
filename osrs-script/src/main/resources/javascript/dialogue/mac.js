var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Select an Option";
lines.add("Hoods of Accomplishment");
actions.add("close|script");
lines.add("Capes of Accomplishment");
actions.add("close|script");
lines.add("Capes of Accomplishment (T)");
actions.add("close|script");
var obj0 = new DialogueEntry();
entries.add(obj0);
obj0.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

instance = new DialogueScript() {
    execute: function(player, index, childId, slot) {
        if (index == 0) {
            if (slot == 0) {
                if (player.getSkills().getLevel99Count() < 1) {
                    player.getGameEncoder().sendMessage("You can't view this shop.");
                    return;
                }
                player.openShop("skillcape_hoods");
            } else if (slot == 1) {
                if (player.getSkills().getLevel99Count() < 1) {
                    player.getGameEncoder().sendMessage("You can't view this shop.");
                    return;
                }
                player.openShop("skillcapes");
            } else if (slot == 2) {
                if (player.getSkills().getLevel99Count() < 2) {
                    player.getGameEncoder().sendMessage("You can't view this shop.");
                    return;
                }
                player.openShop("skillcapes_trimmed");
            }
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}
