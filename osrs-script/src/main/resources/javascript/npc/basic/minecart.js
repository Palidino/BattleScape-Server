var npc = null;

instance = new NScript() {
    setNpc: function(n) {
        npc = n;
    },

    getVariable: function(name) {
        return null;
    },

    restore: function() { },

    tick: function() {
        if (!npc.isLocked()) {
            if (npc.getX() == 2727 && npc.getY() == 4509) {
                npc.getMovement().clear();
                npc.getMovement().addMovement(2727, 4491);
                npc.setLock(4509 - 4491 + 2);
            } else if (npc.getX() == 2727 && npc.getY() == 4491) {
                npc.getMovement().clear();
                npc.getMovement().addMovement(2727, 4509);
                npc.setLock(4509 - 4491 + 2);
            } else if (npc.getX() == 2739 && npc.getY() == 4531) {
                npc.getMovement().clear();
                npc.getMovement().addMovement(2739, 4528);
                npc.setLock(4531 - 4528 + 2);
            } else if (npc.getX() == 2739 && npc.getY() == 4528) {
                npc.getMovement().clear();
                npc.getMovement().addMovement(2739, 4531);
                npc.setLock(4531 - 4528 + 2);
            } else if (npc.getX() == 2781 && npc.getY() == 4462) {
                npc.getMovement().clear();
                npc.getMovement().addMovement(2795, 4462);
                npc.setLock(2795 - 2781 + 2);
            } else if (npc.getX() == 2795 && npc.getY() == 4462) {
                npc.getMovement().clear();
                npc.getMovement().addMovement(2781, 4462);
                npc.setLock(2795 - 2781 + 2);
            } else if (npc.getX() == 2791 && npc.getY() == 4452) {
                npc.getMovement().clear();
                npc.getMovement().addMovement(2791, 4447);
                npc.setLock(4452 - 4447 + 2);
            } else if (npc.getX() == 2791 && npc.getY() == 4447) {
                npc.getMovement().clear();
                npc.getMovement().addMovement(2791, 4452);
                npc.setLock(4452 - 4447 + 2);
            } else if (npc.getX() == 2785 && npc.getY() == 4452) {
                npc.getMovement().clear();
                npc.getMovement().addMovement(2785, 4447);
                npc.setLock(4452 - 4447 + 2);
            } else if (npc.getX() == 2785 && npc.getY() == 4447) {
                npc.getMovement().clear();
                npc.getMovement().addMovement(2785, 4452);
                npc.setLock(4452 - 4447 + 2);
            }
        }
        for each (var player in npc.getController().getPlayers()) {
            if (npc.getMovement().getWalkDir() == -1 || !player.matchesTile(npc)) {
                continue;
            }
            var tile = new Tile(player.getX() + Tile.DIRECTION_X[npc.getMovement().getWalkDir()],
                    player.getY() + Tile.DIRECTION_Y[npc.getMovement().getWalkDir()]);
            var forceMovement = new ForceMovement(tile, 1, Tile.getReverseDirection(npc.getMovement().getWalkDir()));
            player.setForceMovementMove(forceMovement, 1441, 1, null, false);
            player.applyHit(new Hit(PRandom.randomI(9)));
            if (player.isDead()) {
                player.putAttribute("death_reason", "a haunted mine cart");
            }
        }
    }
};
