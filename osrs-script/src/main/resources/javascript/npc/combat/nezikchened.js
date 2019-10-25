var npc = null;

cs = new NCombatScript() {
    /* @Override */
    setNpcHook: function(n) {
        npc = n;
    },

    /* @Override */
    dropItemHook: function(player, dropTile, dropForIndex, hasRoWICharge) {
        if (player.getCombat().getLegendsQuest() == 0) {
            player.getGameEncoder().sendMessage("The demon's body falls to the floor in a pile of ashes.");
            player.getGameEncoder().sendMessage("It's time to move deeper into the dungeon...");
            player.getMovement().teleport(2792, 9337, 0);
        }
        if (player.getCombat().getLegendsQuest() == 2) {
            player.getGameEncoder().sendMessage("The demon falls one last time, realizing its defeat.");
        }
        if (player.getCombat().getLegendsQuest() == 0 || player.getCombat().getLegendsQuest() == 2) {
            player.getCombat().setLegendsQuest(player.getCombat().getLegendsQuest() + 1);
            if (player.getCombat().isLegendsQuestComplete()) {
                player.getGameEncoder().sendMessage("<col=ff0000>You have completed Legends Quest!");
                player.getInventory().addOrDropItem(ItemId.COINS, 100000);
            }
        }
    }
};
