var IDLE_ID = 7561, COMBAT_ID = 7563, BABY_ID = 7562;
var SPAWN_TILE = new Tile(3314, 5331, 1), BABY_SPAWN_TILE = new Tile(3310, 5317, 1);

var npc = null;
var loaded = false;
var baby = null;

cs = new NCombatScript() {
    /* @Override */
    setNpcHook: function(n) {
        npc = n;
    },

    /* @Override */
    restore: function() {
        npc.getWorld().removeNpc(baby);
    },

    /* @Override */
    tick: function() {
        if (!loaded) {
            this.loadProfile();
            return;
        }
        if (!npc.getController().isRegionLoaded()) {
            return;
        }
        if (baby != null && npc.getId() == IDLE_ID && (!baby.isVisible() || baby.isDead())) {
            npc.getCombat().clear();
            npc.setTransformationId(COMBAT_ID);
            npc.setAnimation(7423);
            npc.setLock(7);
            npc.getMovement().clear();
            npc.getMovement().addMovement(npc.getX() - 6, npc.getY());
        }
    },

    /* @Override */
    hitTypeHook: function(hitType) {
        if (hitType == HitType.MAGIC && PRandom.randomE(5) != 0) {
            return HitType.RANGED;
        }
        return hitType;
    },

    /* @Override */
    canBeAttackedHook: function(player, sendMessage, hitType) {
        return npc.getId() != IDLE_ID;
    },

    loadProfile: function() {
        if (loaded || npc.getController().getPlayers().isEmpty()) {
            return;
        }
        loaded = true;
        npc.getSpawnTile().setTile((npc.getId() == BABY_ID) ? BABY_SPAWN_TILE : SPAWN_TILE);
        npc.setTile(npc.getSpawnTile());
        var averageHP = 0;
        var playerMultiplier = 1;
        var players = npc.getController().getPlayers();
        for each (var player in players) {
            averageHP += player.getMaxHitpoints();
            playerMultiplier = PNumber.addDoubles(playerMultiplier, 0.5);
        }
        averageHP /= players.size();
        var hitpoints = ((50 + (players.size() * 25) + (averageHP * 2)) * playerMultiplier)|0;
        if (npc.getId() == BABY_ID) {
            npc.setMaxHitpoints(hitpoints / 2);
        } else {
            npc.setMaxHitpoints(hitpoints);
            npc.setTransformationId(IDLE_ID);
            baby = new Npc(npc.getController(), BABY_ID, BABY_SPAWN_TILE);
            baby.getController().setMultiCombatFlag(true);
            baby.setMoveDistance(4);
        }
        npc.setHitpoints(npc.getMaxHitpoints());
    }
}
