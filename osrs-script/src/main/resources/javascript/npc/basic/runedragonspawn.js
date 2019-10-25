var npc = null;

var countdown = 8;

instance = new NScript() {
    setNpc: function(n) {
        npc = n;
    },

    getVariable: function(name) {
        return null;
    },

    restore: function() { },

    tick: function() {
        if (--countdown == 0) {
            npc.getCombat().timedDeath();
        }
        if (npc.isLocked()) {
            return;
        }
        var following = npc.getMovement().getFollowing();
        if (npc.withinDistance(following, 1) && following instanceof Player) {
            following.addHit(new HitEvent(0, following, null,
                    new Hit(PRandom.randomI(following.getEquipment().getFootId() == 7159 ? 2 : 8))));
        }
    }
};
