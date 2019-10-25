var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Select an Option";
lines.add("Public FFA");
actions.add("close|script");
lines.add("Create Instance");
actions.add("close|script");
lines.add("Join Instance");
actions.add("close|script");
lines.add("Clan Wars Tournament");
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
            if (slot == 0) {
                player.getMovement().teleport(3327, 4752, 0);
                player.setController(new ClanWarsFreeForAllPC());
            } else if (slot == 1) {
                player.getMovement().teleport(3327, 4752, 0);
                player.setController(new ClanWarsFreeForAllPC());
                player.getController().instance();
                player.getMovement().teleport(3327, 4752, 0);
            } else if (slot == 2) {
                player.getGameEncoder().sendEnterString("Enter username:", new ValueEnteredEvent.StringEvent() {
                    execute: function(value) {
                        var player2 = player.getWorld().getPlayerByUsername(value);
                        if (player2 == null) {
                            player.getGameEncoder().sendMessage("Unable to locate " + value);
                            return;
                        }
                        if (!player2.getController().getVariable("clan_wars_free_for_all")) {
                            player.getGameEncoder().sendMessage(value + " is not in FFA");
                            return;
                        }
                        if (!player2.getController().isInstanced()) {
                            player.getGameEncoder().sendMessage(value + " is not in a FFA instance");
                            return;
                        }
                        var myClanChat = player.getMessaging().getClanChatUsername();
                        var otherClanChat = player2.getMessaging().getClanChatUsername();
                        if (otherClanChat != null && !otherClanChat.equals(myClanChat)) {
                            player.getGameEncoder().sendMessage("You are not in the same Clan Chat as this player.");
                            return;
                        }
                        player.getMovement().teleport(3327, 4752, 0);
                        player.setController(new ClanWarsFreeForAllPC());
                        player.getController().setInstance(player2.getController());
                    }
                });
            } else if (slot == 3) {
                if (player.isLocked() || !player.getController().canTeleport(true)) {
                    return;
                }
                player.getMovement().animatedTeleport(new Tile(2207, 4939), Magic.NORMAL_MAGIC_ANIMATION_START,
                        Magic.NORMAL_MAGIC_ANIMATION_END, Magic.NORMAL_MAGIC_GRAPHIC, null, 2);
            }
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}
