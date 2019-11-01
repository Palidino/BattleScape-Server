var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var continueLine = "";
var actions = new ArrayList();

title = "Select an Option";
lines.add("Mage Arena 1");
actions.add("close|script");
lines.add("Mage Arena 2");
actions.add("dialogue=kolodion,1");
lines.add("View Shop");
actions.add("close|script");
var obj0 = new DialogueEntry();
entries.add(obj0);
obj0.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

lines.add("<col=ff0000>WARNING</col>: The final wave requires all three god spells to complete. Make sure you have the correct runes, spells, and staves before you continue.");
continueLine = "Click here to continue";
actions.add("dialogue=kolodion,2");
var obj1 = new DialogueEntry();
entries.add(obj1);
obj1.setTextContinue(continueLine, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Select an Option";
lines.add("Start From Boosted");
actions.add("close|script");
lines.add("Start From Beginning");
actions.add("close|script");
lines.add("Final Wave");
actions.add("close|script");
var obj2 = new DialogueEntry();
entries.add(obj2);
obj2.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

instance = new DialogueScript() {
    execute: function(player, index, childId, slot) {
        if (index == 0) {
            if (slot == 0) {
                if (player.getCombat().getMageArena()) {
                    player.getGameEncoder().sendMessage("You've already completed this.");
                    return;
                }
                var height = player.getWorld().getUniqueHeight();
                player.getMovement().teleport(3105, 3934, height);
                var event = new PEvent(5) {
                    execute: function() {
                        event.stop();
                        if (!player.isVisible()) {
                            return;
                        }
                        var kolodion = new Npc(player.getController(), 1605, new Tile(3106, 3934, height));
                        kolodion.getCombat().setTarget(player);
                    }
                };
                player.getWorld().addEvent(event);
            } else if (slot == 2) {
                if (!player.getCombat().getMageArena()) {
                    player.getGameEncoder().sendMessage("You can't view this shop.");
                    return;
                }
                player.openShop("kolodion");
            }
        } else if (index == 2) {
            if (!player.getCombat().getMageArena()) {
                player.getGameEncoder().sendMessage("You need to complete the Mage Arena first.");
                return;
            } else if (slot == 1 && !player.isGameModeNormal()) {
                player.getGameEncoder().sendMessage("You can't do that.");
                return;
            } else if (slot == 2 && !player.getCombat().getMageArena2()) {
                player.getGameEncoder().sendMessage("You need to complete the minigame at least once to do that.");
                return;
            }
            player.setController(PController.getController("magearena"));
            if (slot == 0) {
                player.getController().setVariable("arena2_wave_id", 8);
            } else if (slot == 2) {
                player.getController().setVariable("arena2_wave_id", 16);
            }
            player.getMovement().teleport(3104, 3933);
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}
