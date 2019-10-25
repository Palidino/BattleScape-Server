var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Select an Option";
lines.add("Shop");
actions.add("close|script");
lines.add("Teleport to Abandoned Mine");
actions.add("close|script");
var obj0 = new DialogueEntry();
entries.add(obj0);
obj0.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

instance = new DialogueScript() {
    execute: function(player, index, childId, slot) {
        if (index == 0) {
            if (slot == 0) {
                if (player.getCombat().getHauntedMine() < 3) {
                    player.getGameEncoder().sendMessage("You can't view this shop.");
                    return;
                }
                player.openShop("haunted_mine");
            } else if (slot == 1) {
                player.getMovement().teleport(3451, 3235);
            }
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}
