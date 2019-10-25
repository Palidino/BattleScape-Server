var CONSUMABLE_TABLE = [
    new RandomItem(ItemId.MONKFISH, 3), new RandomItem(ItemId.SHARK, 2),
    new RandomItem(ItemId.SUPER_COMBAT_POTION_2, 1), new RandomItem(ItemId.RANGING_POTION_3, 1),
    new RandomItem(ItemId.SUPERANTIPOISON_2, 1), new RandomItem(ItemId.DARK_CRAB, 2),
    new RandomItem(ItemId.SARADOMIN_BREW_4, 1), new RandomItem(ItemId.SUPER_RESTORE_4, 1),
    new RandomItem(ItemId.PRAYER_POTION_4, 2)
];

var npc = null;

cs = new NCombatScript() {
    /* @Override */
    setNpcHook: function(n) {
        npc = n;
    },

    /* @Override */
    dropItemHook: function(player, dropTile, dropForIndex, hasRoWICharge) {
        npc.getController().addMapItem(RandomItem.getItem(CONSUMABLE_TABLE), dropTile, player);
    }
};
