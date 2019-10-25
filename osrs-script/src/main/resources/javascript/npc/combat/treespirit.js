var npc = null;

cs = new NCombatScript() {
    /* @Override */
    setNpcHook: function(n) {
        npc = n;
    },

    /* @Override */
    dropItemHook: function(player, dropTile, dropForIndex, hasRoWICharge) {
        player.getCombat().setLostCity(true);
        player.getMovement().teleport(3109, 3514, 0);
        player.getGameEncoder().sendMessage("<col=ff0000>You have completed Lost City!");
        player.getInventory().addOrDropItem(ItemId.COINS, 25000);
    }
};
