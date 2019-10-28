var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Select an Option";
lines.add("Fight Agrith Naar");
actions.add("close|script");
lines.add("Nevermind");
actions.add("close");
var obj0 = new DialogueEntry();
entries.add(obj0);
obj0.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

instance = new DialogueScript() {
    execute: function(player, index, childId, slot) {
        if (player.isLocked()) {
            return;
        }
        if (index == 0) {
            if (slot == 0) {
                var height = player.getWorld().getUniqueHeight();
                player.getMovement().teleport(2720, 4888, height + 2);
                var event = new PEvent(5) {
                    execute: function() {
                        event.stop();
                        if (!player.isVisible()) {
                            return;
                        }
                        var npc = new Npc(player.getController(), 6327, new Tile(2719, 4899, height + 2));
                        npc.setLock(4);
                        npc.setAnimation(4623);
                        npc.setGraphic(482);
                        npc.getCombat().setTarget(player);
                    }
                };
                player.getWorld().addEvent(event);
            }
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}
