var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Select an Option";
lines.add("Bank");
actions.add("close|script");
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
                player.getBank().open();
            } else if (slot == 1) {
                if (player.getCombat().getRecipeForDisasterStage() == 0) {
                    player.getGameEncoder().sendMessage("You can't view this shop.");
                    return;
                }
                player.openShop("culinaromancer" + player.getCombat().getRecipeForDisasterStage());
            } else if (slot == 2) {
                if (player.getCombat().getRecipeForDisasterStage() == 6) {
                    player.getGameEncoder().sendMessage("You've already completed this.");
                    return;
                }
                player.setController(new RFDFinalBattlePC());
            }
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}
