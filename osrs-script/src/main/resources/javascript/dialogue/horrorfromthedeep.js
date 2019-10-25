var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Select an Option";
lines.add("View Books");
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
                if (!player.getCombat().getHorrorFromTheDeep()) {
                    player.getGameEncoder().sendMessage("You can't view this shop.");
                    return;
                }
                player.getWidgetManager().sendInteractiveOverlay(WidgetId.JOSSIKS_SALVAGED_PRAYERBOOKS);
                var event = new Event(4) {
                    execute: function() {
                        event.stop();
                        if (!player.isVisible()) {
                            return;
                        }
                        player.getGameEncoder().sendWidgetText(WidgetId.JOSSIKS_SALVAGED_PRAYERBOOKS, 3, 11, "100K");
                        player.getGameEncoder().sendWidgetText(WidgetId.JOSSIKS_SALVAGED_PRAYERBOOKS, 4, 11, "100K");
                        player.getGameEncoder().sendWidgetText(WidgetId.JOSSIKS_SALVAGED_PRAYERBOOKS, 5, 11, "100K");
                        player.getGameEncoder().sendWidgetText(WidgetId.JOSSIKS_SALVAGED_PRAYERBOOKS, 6, 11, "500K");
                        player.getGameEncoder().sendWidgetText(WidgetId.JOSSIKS_SALVAGED_PRAYERBOOKS, 7, 11, "500K");
                        player.getGameEncoder().sendWidgetText(WidgetId.JOSSIKS_SALVAGED_PRAYERBOOKS, 8, 11, "500K");
                    }
                };
                player.getWorld().addEvent(event);
            } else if (slot == 1) {
                if (player.getCombat().getHorrorFromTheDeep()) {
                    player.getGameEncoder().sendMessage("You've already completed this.");
                    return;
                }
                player.getMovement().teleport(2515, 4632, 4);
                var mother = new Npc(player.getController(), 983, new Tile(2520, 4648, 4));
                mother.getCombat().setTarget(player);
            }
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}
