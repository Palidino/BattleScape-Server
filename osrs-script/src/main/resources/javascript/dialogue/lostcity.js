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
                if (!player.getCombat().getLostCity()) {
                    player.getGameEncoder().sendMessage("You can't view this shop.");
                    return;
                }
                player.openShop("lost_city");
            } else if (slot == 1) {
                if (player.getCombat().getLostCity()) {
                    player.getGameEncoder().sendMessage("You've already completed this.");
                    return;
                }
                for (var i = 0; i < player.getInventory().size(); i++) {
                    var itemId = player.getInventory().getId(i);
                    if (itemId == -1 || ItemDef.getEquipSlot(itemId) == null) {
                        continue;
                    }
                    var isAllowed = ItemDef.getName(itemId).contains("arrow") || itemId == 3840 || itemId == 3842
                            || itemId == 3844 || itemId == 12608 || itemId == 12610 || itemId == 12612;
                    if (isAllowed) {
                        continue;
                    }
                    player.getGameEncoder().sendMessage("You can't take " + ItemDef.getName(itemId) + " to Entrana.");
                    return;
                }
                for (var i = 0; i < player.getEquipment().size(); i++) {
                    var itemId = player.getEquipment().getId(i);
                    if (itemId == -1 || ItemDef.getEquipSlot(itemId) == null) {
                        continue;
                    }
                    var isAllowed = ItemDef.getName(itemId).contains("arrow") || itemId == 3840 || itemId == 3842
                            || itemId == 3844 || itemId == 12608 || itemId == 12610 || itemId == 12612;
                    if (isAllowed) {
                        continue;
                    }
                    player.getGameEncoder().sendMessage("You can't take " + ItemDef.getName(itemId) + " to Entrana.");
                    return;
                }
                player.getMovement().teleport(2830, 9772);
            }
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}

