var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Select an Option";
lines.add("Open Voting Page");
actions.add("close|script");
lines.add("Information");
actions.add("close|script");
lines.add("View Shop");
actions.add("close|script");
var obj0 = new DialogueEntry();
entries.add(obj0);
obj0.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Select an Option";
lines.add("Pay 2 Vote tickets and 25 Boss Slayer Points");
actions.add("close|script");
lines.add("Nevermind");
actions.add("close");
var obj1 = new DialogueEntry();
entries.add(obj1);
obj1.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Please Remember to Vote!";
lines.add("Open Voting Page");
actions.add("close|script");
lines.add("Open and View Information");
actions.add("close|script");
lines.add("Maybe Later");
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
            if (slot == 0) {
                player.getGameEncoder().sendOpenUrl(Settings.getInstance().getVoteUrl());
            } else if (slot == 1) {
                player.getGameEncoder().sendMessage("Please check the guide book in the quest tab for more information.");
            } else if (slot == 2) {
                player.openShop("vote");
            }
        } else if (index == 1) {
            if (slot == 0) {
                if (player.getInventory().getCount(ItemId.VOTE_TICKET) < 2) {
                    player.getGameEncoder().sendMessage("You need 2 Vote tickets to do this.");
                    return;
                } else if (player.getSlayer().getBossTaskAmount() == 0) {
                    player.getGameEncoder().sendMessage("You don't have a Boss Slayer task to cancel.");
                    return;
                } else if (player.getSlayer().getBossPoints() < 25) {
                    player.getGameEncoder().sendMessage("You need 25 Boss Slayer points to do this.");
                    return;
                }
                player.getSlayer().setBossTaskAmount(0);
                player.getInventory().deleteItem(ItemId.VOTE_TICKET, 2);
                player.getSlayer().setBossPoints(player.getSlayer().getBossPoints() - 25);
                player.getGameEncoder().sendMessage("Your Boss Slayer task has been cancelled.");
            }
        } else if (index == 2) {
            player.getGameEncoder().sendOpenUrl(Settings.getInstance().getVoteUrl());
            if (slot == 1) {
                player.getGameEncoder().sendMessage("Please check the guide book in the quest tab for more information.");
            }
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}
