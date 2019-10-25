var npc = null;

var countdown1 = 5;
var countdown2 = 2;

instance = new NScript() {
    setNpc: function(n) {
        npc = n;
    },

    getVariable: function(name) {
        if (name.startsWith("countdown1=")) {
            countdown1 = Integer.parseInt(name.substring(11));
        }
        return null;
    },

    restore: function() { },

    tick: function() {
        if (countdown1 > 0) {
            countdown1--;
            if (countdown1 == 1) {
                npc.getMovement().setFollowing(null);
                npc.getMovement().clear();
            } else if (countdown1 == 0) {
                npc.getCombat().timedDeath(countdown2);
            }
        } else if (countdown2 > 0) {
            countdown2--;
            if (countdown2 == 0) {
                npc.getController().sendMapGraphic(npc, new Graphic(1295));
                for each (var player in npc.getController().getPlayers()) {
                    if (npc.withinDistance(player, 2)) {
                        var hitEvent = new HitEvent(0, player, new Hit(4 + PRandom.randomI(6)));
                        player.addHit(hitEvent);
                    }
                }
            }
        }
    }
};
