var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var continueLine = "";
var actions = new ArrayList();

for (var i = 0; i < 7; i++) { // 0-6
    title = "Select an Option";
    lines.add("Normal XP");
    actions.add("close|script");
    lines.add("RS XP");
    actions.add("close|script");
    if (i != Skills.HITPOINTS) {
        lines.add("XP Disabled");
        actions.add("close|script");
        lines.add("Reset Level");
        actions.add("close|script");
    }
    var obj = new DialogueEntry();
    entries.add(obj);
    obj.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));
}

for (var i = 0; i < 7; i++) { // 7-13
    title = "Select an Option";
    lines.add("Set Level");
    actions.add("close|script");
    lines.add("RS XP");
    actions.add("close|script");
    lines.add("XP Disabled");
    actions.add("close|script");
    var obj = new DialogueEntry();
    entries.add(obj);
    obj.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));
}

instance = new DialogueScript() {
    execute: function(player, index, childId, slot) {
        if (player.isLocked()) {
            return;
        }
        if (index >= 0 && index <= 6) {
            if (slot == 0) {
                player.getSkills().setXPLock(index, Skills.NORMAL_XP);
                player.getGameEncoder().sendMessage(Skills.SKILL_NAMES[index] + " XP rate normal.");
            } else if (slot == 1) {
                player.getSkills().setXPLock(index, Skills.X1_XP);
                player.getGameEncoder().sendMessage(Skills.SKILL_NAMES[index] + " XP rate x1.");
            } else if (slot == 2) {
                player.getSkills().setXPLock(index, Skills.DISABLED_XP);
                player.getGameEncoder().sendMessage(Skills.SKILL_NAMES[index] + " XP rate disabled.");
            } else if (slot == 3) {
                if (player.getController().getLevelForXP(index) == 1) {
                    player.getGameEncoder().sendMessage("You can't reset this stat.");
                    return;
                } else if (player.getInventory().getCount(ItemId.COINS) < 500000) {
                    player.getGameEncoder().sendMessage("You need 500K coins to do this.");
                    return;
                } else if (player.getBank().needsPinInput(false)) {
                    return;
                }
                if (player.getSkills().changeCombatXP(index, 1)) {
                    player.getInventory().deleteItem(ItemId.COINS, 500000);
                }
            }
        } else if (index >= 7 && index <= 13) {
            index -= 7;
            if (slot == 0) {
                player.getGameEncoder().sendEnterAmount(new ValueEnteredEvent.IntegerEvent() {
                    execute: function(value) {
                        player.getSkills().changeCombatXP(index, value);
                    }
                });
            } else if (slot == 1) {
                player.getSkills().setXPLock(index, Skills.X1_XP);
                player.getGameEncoder().sendMessage(Skills.SKILL_NAMES[index] + " XP rate x1.");
            } else if (slot == 2) {
                player.getSkills().setXPLock(index, Skills.DISABLED_XP);
                player.getGameEncoder().sendMessage(Skills.SKILL_NAMES[index] + " XP rate disabled.");
            }
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}
