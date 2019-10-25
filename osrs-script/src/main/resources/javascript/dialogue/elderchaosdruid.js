var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Select an Option";
lines.add("Exchange 'bones': 50 coins");
actions.add("script|close");
lines.add("Exchange 5: 250 coins");
actions.add("script|close");
lines.add("Exchange All: # coins");
actions.add("script|close");
lines.add("Exchange X");
actions.add("close|script");
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
            var amount = 0;
            if (slot == 0) {
                amount = 1;
            } else if (slot == 1) {
                amount = 5;
            } else if (slot == 2) {
                amount = Item.MAX_AMOUNT;
            }
            var valueEntered = new ValueEnteredEvent.IntegerEvent() {
                execute: function(value) {
                    value = Math.min(value, player.getInventory().getCount(itemId));
                    value = Math.min(value, player.getInventory().getRemainingSlots());
                    value = Math.min(value, Item.MAX_AMOUNT / 50);
                    if (player.getInventory().getCount(ItemId.COINS) < value * 50) {
                        player.getGameEncoder().sendMessage("You don't have enough coins to do this.");
                        return;
                    }
                    player.getInventory().deleteItem(ItemId.COINS, value * 50);
                    player.getInventory().deleteItem(itemId, value);
                    player.getInventory().addItem(ItemDef.getUnnotedId(itemId), value);
                }
            }
            if (slot == 3) {
               player.getGameEncoder().sendEnterAmount(valueEntered);
            } else {
                valueEntered.execute(amount);
            }
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}
