var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Select an Option";
lines.add("Burthope");
actions.add("close|script");
lines.add("Barbarian Outpost");
actions.add("close|script");
lines.add("Corporeal Beast");
actions.add("close|script");
lines.add("Tears of Guthix");
actions.add("close");
lines.add("Wintertodt Camp");
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
            if (!player.getController().canTeleport(true)) {
                return;
            }
            var tile = null;
            if (slot == 0) {
                tile = new Tile(2899, 3554);
            } else if (slot == 1) {
                tile = new Tile(2520, 3571);
            } else if (slot == 2) {
                tile = new Tile(2965, 4382, 2);
            } else if (slot == 4) {
                tile = new Tile(1625, 3938);
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
