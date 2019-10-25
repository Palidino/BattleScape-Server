var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Select an Option";
lines.add("Demon's Run");
actions.add("close|script");
lines.add("Dragon's Den");
actions.add("close|script");
lines.add("Reeking Cove");
actions.add("close|script");
lines.add("The Shallows");
actions.add("close|script");
var obj0 = new DialogueEntry();
entries.add(obj0);
obj0.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

lines.add("You will lose all of your items dropped if you die!");
lines.add("I know I'm risking everything I have.");
actions.add("dialogue=catacombsofkourend,2");
lines.add("I need to prepare some more.");
actions.add("close");
var obj1 = new DialogueEntry();
entries.add(obj1);
obj1.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

lines.add("Are you sure?");
lines.add("Yes, I know items dropped in the instance will be lost.");
actions.add("close|script");
lines.add("On second thoughts, better not.");
actions.add("close");
var obj2 = new DialogueEntry();
entries.add(obj2);
obj2.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

instance = new DialogueScript() {
    execute: function(player, index, childId, slot) {
        if (player.isLocked()) {
            return;
        }
        if (index == 0) {
            if (player.getInventory().getCount(19677) < 1) {
                player.getGameEncoder().sendMessage("You need an Ancient shard to use this.");
                return;
            }
            player.getInventory().deleteItem(19677, 1);
            var tile = null;
            if (slot == 0) {
                var tile = new Tile(1719, 10103);
            } else if (slot == 1) {
                var tile = new Tile(1617, 10103);
            } else if (slot == 2) {
                var tile = new Tile(1650, 9985);
            } else if (slot == 3) {
                var tile = new Tile(1727, 9993);
            }
            if (tile == null) {
                return;
            }
            player.getInventory().deleteItem(19677, 1);
            player.getMovement().teleport(tile);
        } else if (index == 2) {
            if (!player.getInventory().hasItem(19685)) {
                player.getGameEncoder().sendMessage("You need a dark totem to do this.");
                return;
            }
            player.getInventory().deleteItem(19685, 1);
            var height = player.getWorld().getUniqueHeight();
            player.getMovement().animatedTeleport(new Tile(1693, 9892, height), Magic.ZEAH_MAGIC_ANIMATION_START,
                    Magic.ZEAH_MAGIC_GRAPHIC, 2);
            var skotizoNPC = new Npc(player.getController(), 7286, new Tile(1691, 9876, height));
            skotizoNPC.getCombat().setTarget(player);
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}

