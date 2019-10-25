var npc = null;

cs = new NCombatScript() {
    setNpcHook: function(n) {
        npc = n;
    },

    dropItemHook: function(player, dropTile, dropForIndex, hasRoWICharge) {
        if (PRandom.randomE(5) == 0) {
            npc.getController().addMapItem(new Item(ItemId.MYSTERIOUS_EMBLEM, 1), dropTile, player);
        }
    }
};
