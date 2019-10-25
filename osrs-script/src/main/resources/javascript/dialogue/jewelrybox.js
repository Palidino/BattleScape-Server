var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Select an Option";
for (var i = 0; i < 25; i++) {
    lines.add("");
    actions.add("close|script");
}
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
            switch (slot) {
            case 0: // duel arena
                tile = new Tile(3315, 3235);
                break;
            case 1: // castle wars
                tile = new Tile(2441, 3091);
                break;
            case 2: // clan wars
                tile = new Tile(3388, 3159);
                break;
            case 3: // burthope
                tile = new Tile(2899, 3554);
                break;
            case 4: // barbarian outpost
                tile = new Tile(2520, 3571);
                break;
            case 5: // corporeal beast
                tile = new Tile(2965, 4382, 2);
                break;
            case 7: // wintertodt camp
                tile = new Tile(1625, 3938);
                break;
            case 8: // warriors' guild
                tile = new Tile(2883, 3550);
                break;
            case 9: // champions' guild
                tile = new Tile(3190, 3368);
                break;
            case 10: // monastery
                tile = new Tile(3052, 3487);
                break;
            case 11: // ranging guild
                tile = new Tile(2654, 3442);
                break;
            case 12: // fishing guild
                tile = new Tile(2613, 3390);
                break;
            case 13: // mining guild
                tile = new Tile(3049, 9762);
                break;
            case 14: // crafting guild
                tile = new Tile(2933, 3292);
                break;
            case 15: // cooking guild
                tile = new Tile(3145, 3435);
                break;
            case 16: // woodcutting guild
                tile = new Tile(1662, 3505);
                break;
            case 17: // miscellania
                tile = new Tile(2535, 3861);
                break;
            case 18: // grand exchange
                tile = new Tile(3163, 3481);
                break;
            case 19: // falador park
                tile = new Tile(2995, 3375);
                break;
            case 21: // edgeville
                tile = new Tile(3087, 3496);
                break;
            case 22: // karamja
                tile = new Tile(2918, 3176);
                break;
            case 23: // draynor
                tile = new Tile(3105, 3251);
                break;
            case 24: // al kharid
                tile = new Tile(3293, 3163);
                break;
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
