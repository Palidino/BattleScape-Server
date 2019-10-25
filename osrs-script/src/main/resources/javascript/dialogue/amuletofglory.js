var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Select an Option";
lines.add("Edgeville");
actions.add("close|script");
lines.add("Karamja");
actions.add("close|script");
lines.add("Draynor Village");
actions.add("close|script");
lines.add("Al Kharid");
actions.add("close|script");
var obj0 = new DialogueEntry();
entries.add(obj0);
obj0.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

instance = new DialogueScript() {
    execute: function(player, index, childId, slot) {
        if (player.isLocked()) {
            return;
        }
        if (index == 0) {
            if (!player.getController().canTeleport(30, true)) {
                return;
            }
            var tile = null;
            if (slot == 0) {
                tile = new Tile(3087, 3496);
            } else if (slot == 1) {
                tile = new Tile(2918, 3176);
            } else if (slot == 2) {
                tile = new Tile(3105, 3251);
            } else if (slot == 3) {
                tile = new Tile(3293, 3163);
            }
            player.getMovement().animatedTeleport(tile, Magic.NORMAL_MAGIC_ANIMATION_START,
                    Magic.NORMAL_MAGIC_ANIMATION_END, Magic.NORMAL_MAGIC_GRAPHIC, null, 2);
            player.getController().stopWithTeleport();
            player.clearHits();
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}
