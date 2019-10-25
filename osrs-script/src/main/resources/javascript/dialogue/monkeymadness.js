var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Select an Option";
lines.add("Shop");
actions.add("close|script");
lines.add("Miniquest");
actions.add("close|script");
var obj0 = new DialogueEntry();
entries.add(obj0);
obj0.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

instance = new DialogueScript() {
    execute: function(player, index, childId, slot) {
        if (index == 0) {
            if (slot == 0) {
                if (!player.getCombat().getMonkeyMadness()) {
                    player.getGameEncoder().sendMessage("You can't view this shop.");
                    return;
                }
                player.openShop("monkey_madness");
            } else if (slot == 1) {
                if (player.getCombat().getMonkeyMadness()) {
                    player.getGameEncoder().sendMessage("You've already completed this.");
                    return;
                }
                player.getMovement().teleport(2702, 9173, 1);
                var jungleDemonNPC = new Npc(player.getController(), 1443, new Tile(2700, 9183, 1));
                jungleDemonNPC.getCombat().setTarget(player);
            }
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}

