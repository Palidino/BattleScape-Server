var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Select an Option";
lines.add("Tree Gnome Village");
actions.add("close|script");
lines.add("Gnome Stronghold");
actions.add("close|script");
lines.add("Battlefield of Khazard");
actions.add("close|script");
lines.add("Grand Exchange");
actions.add("close|script");
lines.add("Cabbage Patch");
actions.add("close|script");
lines.add("Myths' Guild");
actions.add("close|script");
var obj0 = new DialogueEntry();
entries.add(obj0);
obj0.setLargeSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

instance = new DialogueScript() {
    execute: function(player, index, childId, slot) {
        if (player.isLocked()) {
            return;
        }
        if (index == 0) {
            if (!player.getController().canTeleport(true)) {
                return;
            }
            var tile = null;
            if (slot == 0) {
                tile = new Tile(2542, 3169);
            } else if (slot == 1) {
                tile = new Tile(2461, 3444);
            } else if (slot == 2) {
                tile = new Tile(2555, 3259);
            } else if (slot == 3) {
                tile = new Tile(3185, 3508);
            } else if (slot == 4) {
                tile = new Tile(3053, 3291);
            } else if (slot == 5) {
                tile = new Tile(2488, 2850);
            }
            if (tile == null) {
                return;
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
