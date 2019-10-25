var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Choose an Option";
lines.add("Chat Setup");
actions.add("close|script");
lines.add("View Ironman Group");
actions.add("close|script");
var obj0 = new DialogueEntry();
entries.add(obj0);
obj0.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

lines.add("<col=ff0000>WARNING</col>: Updating your group without your current members in the Clan Chat will remove them from your group.");
continueLine = "Click here to update group";
actions.add("close|script");
var obj1 = new DialogueEntry();
entries.add(obj1);
obj1.setTextContinue(continueLine, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

instance = new DialogueScript() {
    execute: function(player, index, childId, slot) {
        if (player.isLocked()) {
            return;
        }
        if (index == 0) {
            if (slot == 0) {
                player.getMessaging().openClanSettingsInterface();
            } else if (slot == 1) {
                if (!player.isGameModeGroupIronman()) {
                    return;
                }
                player.getWidgetManager().sendInteractiveOverlay(WidgetId.SLAYER_PARTNER);
                player.putAttribute("clan_chat_usernames", player.getMessaging().getClanChatUsermames());
                player.getGameEncoder().sendWidgetText(WidgetId.SLAYER_PARTNER, 7, "Update Group");
                if (!player.getMessaging().isClanChatOwner()
                        || player.getMessaging().getClanChatUsers() == null) {
                    player.getGameEncoder().sendWidgetText(WidgetId.SLAYER_PARTNER, 4, "Current Group Members:<br>"
                            + player.getGroupIronmanUsernames());
                } else {
                    player.getGameEncoder().sendWidgetText(WidgetId.SLAYER_PARTNER, 4, "Update Group Members to:<br>"
                            + player.getAttribute("clan_chat_usernames"));
                    player.getGameEncoder().sendWidgetText(WidgetId.SLAYER_PARTNER, 5, "Current Group Members:<br>"
                            + player.getGroupIronmanUsernames());
                }
            }
        } else if (index == 1) {
            var groupMap = player.canUpdateGroupIronman();
            if (groupMap == null) {
                return;
            }
            for each (var username in groupMap.values()) {
                var player2 = player.getWorld().getPlayerByUsername(username);
                player2.setGroupIronman(groupMap);
                player2.getGameEncoder().sendMessage("Your ironman group has been updated.");
            }
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}
