var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Select an Option";
lines.add("Repair Broken Items");
actions.add("close|script");
lines.add("Reclaim Items");
actions.add("close|script");
var obj0 = new DialogueEntry();
entries.add(obj0);
obj0.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Select an Option";
lines.add("Reclaim Items for 100K");
actions.add("close|script");
lines.add("Nevermind");
actions.add("close");
var obj1 = new DialogueEntry();
entries.add(obj1);
obj1.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

instance = new DialogueScript() {
    execute: function(player, index, childId, slot) {
        if (player.isLocked()) {
            return;
        }
        if (index == 0) {
            if (slot == 0) {
                for (var i = 0; i < player.getInventory().size(); i++) {
                    var item = player.getInventory().getItem(i);
                    if (item == null || item.getDef().getRepairedId() == -1) {
                        continue;
                    }
                    if (item.getDef().getRepairCost() == null) {
                        player.getGameEncoder().sendMessage("No repair cost set for " + item.getDef().getName());
                        continue;
                    }
                    var hasRepairCost = true;
                    for (var i2 = 0; i2 < item.getDef().getRepairCost().length; i2++) {
                        var repair = item.getDef().getRepairCost()[i2];
                        if (player.getInventory().getCount(repair.getId()) < repair.getAmount()) {
                            player.getGameEncoder().sendMessage("You need " + repair.getDef().getName()
                                    + " x " + repair.getAmount());
                            hasRepairCost = false;
                        }
                    }
                    if (!hasRepairCost) {
                        continue;
                    }
                    for (var i2 = 0; i2 < item.getDef().getRepairCost().length; i2++) {
                        var repair = item.getDef().getRepairCost()[i2];
                        player.getInventory().deleteItem(repair.getId(), repair.getAmount());
                    }
                    var itemAmount = item.getAmount();
                    player.getInventory().deleteItem(item, i);
                    player.getInventory().addItem(item.getDef().getRepairedId(), itemAmount, i);
                }
            } else if (slot == 1) {
                if (player.getCombat().getItemCollection() == null
                        || player.getCombat().getItemCollection().isEmpty()) {
                    player.getGameEncoder().sendMessage("Perdu has no items for you to reclaim.");
                    return;
                }
                player.openDialogue("perdu", 1);
                DialogueOld.setText(player, null, "Reclaim Items for "
                        + PNumber.abbreviateNumber(player.getCombat().getItemCollection().getCAttachment()),
                        "Nevermind");
            }
        } else if (index == 1) {
            if (slot == 0) {
                if (player.getInventory().getCount(ItemId.COINS)
                        < player.getCombat().getItemCollection().getCAttachment()) {
                    player.getGameEncoder().sendMessage("You need "
                            + PNumber.abbreviateNumber(player.getCombat().getItemCollection().getCAttachment())
                            + " coins to reclaim your items.");
                    return;
                }
                player.getInventory().deleteItem(ItemId.COINS, player.getCombat().getItemCollection().getCAttachment());
                for each (var item in player.getCombat().getItemCollection()) {
                    if (player.getController().isFood(item.getId()) || player.getController().isDrink(item.getId())) {
                        player.getInventory().addOrDropItem(item.getNotedId(), item.getAmount());
                    } else {
                        player.getInventory().addOrDropItem(item);
                    }
                }
                player.getCombat().getItemCollection().clear();
            }
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}
