var npc = null;

cs = new NCombatScript() {
    setNpcHook: function(n) {
        npc = n;
    },

    droppingItemHook: function(player, droppingItem, dropTableChance) {
        if (!player.getSkills().isWildernessSlayerTask(npc)) {
            player.getGameEncoder().sendMessage("Your loot immediately fades away. Maybe a task would help...");
            return null;
        }
        return droppingItem;
    }
};
