var MOVE_PATHS = [
    new Tile(2781, 4459),
    new Tile(2787, 4444),
    new Tile(2788, 4463),
    new Tile(2794, 4451),
    new Tile(2781, 4454)
];

var npc = null;
var hasMoved = false;
var moveDelay = 0;

cs = new NCombatScript() {
    /* @Override */
    setNpcHook: function(n) {
        npc = n;
    },

    /* @Override */
    tick: function() {
        if (npc.isLocked() || hasMoved && npc.getLastHitByDelay() > 10 || moveDelay-- > 0
                || npc.getMovement().isRouting()) {
            return;
        }
        hasMoved = true;
        npc.getCombat().clear();
        npc.getMovement().clear();
        npc.getMovement().addMovement(MOVE_PATHS[PRandom.randomE(MOVE_PATHS.length)]);
        npc.setLock(npc.getMovement().getMoveListSize());
        moveDelay = npc.getLock() + PRandom.randomI(16);
    },

    /* @Override */
    dropItemHook: function(player, dropTile, dropForIndex, hasRoWICharge) {
        if (player.getCombat().getHauntedMine() == 2) {
            player.getCombat().setHauntedMine(3);
        }
    }
};
