var npc = null;
var objects = new ArrayList();

cs = new NCombatScript() {
    /* @Override */
    setNpcHook: function(n) {
        npc = n;
    },

    /* @Override */
    restore: function() {
        for each (var object in objects) {
            object.stop();
        }
    },

    /* @Override */
    tick: function() {
        if (!npc.isLocked() && !npc.getController().hasSolidMapObject(npc)) {
            var tmo = new TempMapObject(30, npc.getController(), new MapObject(32984, npc, 10,
                    MapObject.getRandomDirection()));
            objects.add(tmo);
            npc.getWorld().addEvent(tmo);
        }
        if (!npc.isLocked() && (PRandom.randomE(4) == 0 || !npc.getMovement().isRouting())) {
            npc.getMovement().clear();
            npc.getMovement().generateRandomPath();
        }
    }
}
