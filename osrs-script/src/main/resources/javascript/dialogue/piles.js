var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Banknote # x Item name";
lines.add("Yes - # gp");
actions.add("script|close");
lines.add("Cancel");
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
            var itemId = player.getAttributeInt("use_item_id");
            if (slot == 0) {
                var amount = player.getInventory().getCount(itemId);
                if (amount == 0 || ItemDef.getNotedId(itemId) == -1) {
                    return;
                }
                var cost = amount * 50;
                if (player.getInventory().getCount(ItemId.COINS) < cost) {
                    player.getGameEncoder().sendMessage("You need " + PNumber.formatNumber(cost) + " coins to do this.");
                    return;
                }
                player.getInventory().deleteItem(itemId, amount);
                player.getInventory().deleteItem(ItemId.COINS, cost);
                player.getInventory().addItem(ItemDef.getNotedId(itemId), amount);
            }
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}
