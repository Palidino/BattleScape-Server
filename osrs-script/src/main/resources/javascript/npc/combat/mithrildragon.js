var npc = null;

cs = new NCombatScript() {
    /* @Override */
    setNpcHook: function(n) {
        npc = n;
    },

    /* @Override */
    droppingItemHook: function(player, droppingItem, dropTableChance) {
        if (player.pluginScript("slayer_is_unlocked", SlayerUnlock.DULY_NOTED)
                && player.getSkills().isAnySlayerTask(npc)
                && droppingItem.getId() == ItemId.MITHRIL_BAR) {
            droppingItem = new Item(ItemId.MITHRIL_BAR_NOTED, droppingItem);
        }
        return droppingItem;
    }
};
