var npc = null;
var derwen = null;
var healDelay = 4;

cs = new NCombatScript() {
    /* @Override */
    setNpcHook: function(n) {
        npc = n;
    },

    /* @Override */
    tick: function() {
        if (npc.isLocked() || --healDelay > 0) {
            return;
        }
        healDelay = 4;
        if (derwen == null) {
            derwen = npc.getWorld().getNPC(7859, npc);
        }
        if (derwen == null || derwen.isLocked()) {
            npc.getCombat().timedDeath();
            return;
        }
        var speed = cs.getSpeed(derwen);
        npc.getCombat().sendMapProjectile(derwen, npc, derwen, 1513, 13, 31, speed.clientDelay, speed.clientSpeed,
                16, 64);
        derwen.addHit(new HitEvent(speed.eventDelay, derwen, npc, new Hit(5, HitMark.HEAL)));
    }
};
