var entries = new ArrayList();
var lines = new ArrayList();
var actions = new ArrayList();

title = "Select an Option";
lines.add("Check Charges");
actions.add("close|script");
lines.add("Charge Type 1 (Coins: 2M Max)");
actions.add("close|script");
lines.add("Charge Type 2 (Coins: 20M Max)");
actions.add("close|script");
lines.add("Charge Type 3 (Bond)");
actions.add("close|script");
lines.add("<col=0000A0>Toggle Settings");
actions.add("close");
lines.add("Enable All");
actions.add("close|script");
lines.add("Disable All");
actions.add("close|script");
lines.add("Force Aggression");
actions.add("close|script");
lines.add("Inventory Activation");
actions.add("close|script");
lines.add("Collect Bones");
actions.add("close|script");
lines.add("Collect Herbs");
actions.add("close|script");
lines.add("Collect Clue Scrolls");
actions.add("close|script");
lines.add("Collect Ensouled Heads");
actions.add("close|script");
lines.add("Collect Ancient Shards");
actions.add("close|script");
lines.add("Collect Dark Totems");
actions.add("close|script");
lines.add("Collect Tokkul");
actions.add("close|script");
lines.add("Collect Numulite");
actions.add("close|script");
lines.add("Collect Emblems");
actions.add("close|script");
var obj0 = new DialogueEntry();
entries.add(obj0);
obj0.setLargeSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

instance = new DialogueScript() {
    execute: function(player, index, childId, slot) {
        if (player.isLocked()) {
            return;
        }
        if (index == 0) {
            if (slot == 0) {
                player.getCharges().checkRoWICharge();
            } else if (slot == 1) {
                player.getCharges().chargeRoWI(0);
            } else if (slot == 2) {
                player.getCharges().chargeRoWI(1);
            } else if (slot == 3) {
                player.getCharges().chargeRoWI(2);
            } else if (slot == 5) {
                player.getCharges().setRowSettings(Integer.MAX_VALUE);
                player.getGameEncoder().sendMessage("All settings enabled.");
            } else if (slot == 6) {
                player.getCharges().setRowSettings(0);
                player.getGameEncoder().sendMessage("All settings disabled.");
            } else if (slot == 7) {
                player.getCharges().toggleRoWSetting(ItemCharges.ROW_AGGRESSION);
                player.getGameEncoder().sendMessage("Force aggression: "
                        + player.getCharges().rowSetting(ItemCharges.ROW_AGGRESSION));
            } else if (slot == 8) {
                player.getCharges().toggleRoWSetting(ItemCharges.ROW_INVENTORY_ACTIVATION);
                player.getGameEncoder().sendMessage("Inventory activation: "
                        + player.getCharges().rowSetting(ItemCharges.ROW_INVENTORY_ACTIVATION));
            } else if (slot == 9) {
                player.getCharges().toggleRoWSetting(ItemCharges.ROW_COLLECT_BONES);
                player.getGameEncoder().sendMessage("Collect bones: "
                        + player.getCharges().rowSetting(ItemCharges.ROW_COLLECT_BONES));
            } else if (slot == 10) {
                player.getCharges().toggleRoWSetting(ItemCharges.ROW_COLLECT_HERB);
                player.getGameEncoder().sendMessage("Collect herbs: "
                        + player.getCharges().rowSetting(ItemCharges.ROW_COLLECT_HERB));
            } else if (slot == 11) {
                player.getCharges().toggleRoWSetting(ItemCharges.ROW_COLLECT_CLUE);
                player.getGameEncoder().sendMessage("Collect clue scrolls: "
                        + player.getCharges().rowSetting(ItemCharges.ROW_COLLECT_CLUE));
            } else if (slot == 12) {
                player.getCharges().toggleRoWSetting(ItemCharges.ROW_COLLECT_ENSOULED);
                player.getGameEncoder().sendMessage("Collect ensouled heads: "
                        + player.getCharges().rowSetting(ItemCharges.ROW_COLLECT_ENSOULED));
            } else if (slot == 13) {
                player.getCharges().toggleRoWSetting(ItemCharges.ROW_COLLECT_ASHARD);
                player.getGameEncoder().sendMessage("Collect ancient shards: "
                        + player.getCharges().rowSetting(ItemCharges.ROW_COLLECT_ASHARD));
            } else if (slot == 14) {
                player.getCharges().toggleRoWSetting(ItemCharges.ROW_COLLECT_DTOTEM);
                player.getGameEncoder().sendMessage("Collect dark totems: "
                        + player.getCharges().rowSetting(ItemCharges.ROW_COLLECT_DTOTEM));
            } else if (slot == 15) {
                player.getCharges().toggleRoWSetting(ItemCharges.ROW_COLLECT_TOKKUL);
                player.getGameEncoder().sendMessage("Collect tokkul: "
                        + player.getCharges().rowSetting(ItemCharges.ROW_COLLECT_TOKKUL));
            } else if (slot == 16) {
                player.getCharges().toggleRoWSetting(ItemCharges.ROW_COLLECT_NUMULITE);
                player.getGameEncoder().sendMessage("Collect numulite: "
                        + player.getCharges().rowSetting(ItemCharges.ROW_COLLECT_NUMULITE));
            } else if (slot == 17) {
                player.getCharges().toggleRoWSetting(ItemCharges.ROW_COLLECT_EMBLEMS);
                player.getGameEncoder().sendMessage("Collect emblems: "
                        + player.getCharges().rowSetting(ItemCharges.ROW_COLLECT_EMBLEMS));
            }
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}
