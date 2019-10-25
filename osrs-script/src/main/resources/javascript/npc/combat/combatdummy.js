var npc = null;

cs = new NCombatScript() {
    /* @Override */
    setNpcHook: function(n) {
        npc = n;
    },

    /* @Override */
    tick: function() {
        if (npc.isDead()) {
            npc.restore();
        }
        npc.setHitpoints(npc.getMaxHitpoints());
    }
};
