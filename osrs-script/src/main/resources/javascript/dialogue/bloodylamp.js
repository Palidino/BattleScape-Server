var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Select an Option";
lines.add("X Herblore XP");
actions.add("close|script");
lines.add("X Mining XP");
actions.add("close|script");
lines.add("X Smithing XP");
actions.add("close|script");
var obj0 = new DialogueEntry();
entries.add(obj0);
obj0.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

instance = new DialogueScript() {
    execute: function(player, index, childId, slot) {
        if (index == 0) {
            if (!player.getInventory().hasItem(6543)) {
                player.getGameEncoder().sendMessage("You need an antique lamp to do this.");
                return;
            }
            player.getInventory().deleteItem(6543, 1);
            var skill = Skills.HERBLORE;
            if (slot == 1) {
                skill = Skills.MINING;
            } else if (slot == 2) {
                skill = Skills.SMITHING;
            }
            var level = player.getController().getLevelForXP(skill);
            var xp = (12.5 * (level * level - level * 2 + 100) * 2)|0;
            player.getSkills().addXp(skill, xp, false);
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}
