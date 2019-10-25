var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Select an Option";
lines.add("Create Raid");
actions.add("close|script");
lines.add("Join Raid");
actions.add("close|script");
var obj0 = new DialogueEntry();
entries.add(obj0);
obj0.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "No-one may join the party after the raid begins.";
lines.add("Begin a short raid.");
actions.add("close|script");
lines.add("Begin a long raid.");
actions.add("close|script");
lines.add("ToB (beta).");
actions.add("close|script");
lines.add("Don't begin the raid yet.");
actions.add("close");
var obj1 = new DialogueEntry();
entries.add(obj1);
obj1.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

instance = new DialogueScript() {
    execute: function(player, index, childId, slot) {
        if (index == 0) {
            var clanChatUsername = player.getMessaging().getClanChatUsername();
            var playerInstance = player.getWorld().getPlayerRaidInstance(clanChatUsername, player.getController());
            if (slot == 0) {
                if (playerInstance != null) {
                    player.getGameEncoder().sendMessage("There is already a raid instance for this Clan Chat.");
                    return;
                } else if (!player.getMessaging().canClanChatEvent()) {
                    player.getGameEncoder().sendMessage("Your Clan Chat privledges aren't high enough to do that.");
                    return;
                }
                player.setController(PController.getController("chambersofxeric"));
                player.getController().instance();
                player.getMovement().teleport(player.getController().getVariable("spawn_coords"));
                player.getWorld().putPlayerRaidInstance(clanChatUsername, player.getController());
            } else if (slot == 1) {
                if (playerInstance == null) {
                    player.getGameEncoder().sendMessage("Unable to locate a raid instance for this Clan Chat.");
                    return;
                } else if (playerInstance.getVariable("is_loaded")) {
                    player.getGameEncoder().sendMessage("The raid has already started.");
                    return;
                }
                player.setController(PController.getController("chambersofxeric"));
                player.getController().setInstance(playerInstance);
                player.getMovement().teleport(player.getController().getVariable("spawn_coords"));
            }
        } else if (index == 1) {
            var clanChatUsername = player.getMessaging().getClanChatUsername();
            var playerInstance = player.getWorld().getPlayerRaidInstance(clanChatUsername, player.getController());
            if (slot == 0) {
                if (playerInstance == null) {
                    player.getGameEncoder().sendMessage("Unable to locate a raid instance for this Clan Chat.");
                    return;
                } else if (playerInstance.getVariable("is_loaded")) {
                    player.getGameEncoder().sendMessage("The raid has already started.");
                    return;
                } else if (!player.getMessaging().canClanChatEvent()) {
                    player.getGameEncoder().sendMessage("Your Clan Chat privledges aren't high enough to do that.");
                    return;
                }
                playerInstance.setVariable("load", false);
            } else if (slot == 1) {
                if (playerInstance == null) {
                    player.getGameEncoder().sendMessage("Unable to locate a raid instance for this Clan Chat.");
                    return;
                } else if (playerInstance.getVariable("is_loaded")) {
                    player.getGameEncoder().sendMessage("The raid has already started.");
                    return;
                } else if (!player.getMessaging().canClanChatEvent()) {
                    player.getGameEncoder().sendMessage("Your Clan Chat privledges aren't high enough to do that.");
                    return;
                }
                playerInstance.setVariable("load", true);
            } else if (slot == 2) {
                if (playerInstance == null) {
                    player.getGameEncoder().sendMessage("Unable to locate a raid instance for this Clan Chat.");
                    return;
                } else if (playerInstance.getVariable("is_loaded")) {
                    player.getGameEncoder().sendMessage("The raid has already started.");
                    return;
                } else if (!player.getMessaging().canClanChatEvent()) {
                    player.getGameEncoder().sendMessage("Your Clan Chat privledges aren't high enough to do that.");
                    return;
                } else if (player.getRights() == 0 && player.getId() != 50 && player.getId() != 2413
                        && player.getId() != 16478 && player.getId() != 17079 && player.getId() != 44913
                        && player.getId() != 32982 && player.getId() != 44608 && player.getId() != 69593) {
                    player.getGameEncoder().sendMessage("You don't have access to ToB.");
                    return;
                }
                playerInstance.setVariable("load_tob", true);
            }
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}
