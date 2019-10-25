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
        if (npc.getMovement().getWalkDir() == -1) {
            return;
        }
        var asObjectId = -1;
        if (npc.getId() == 1505) {
            asObjectId = Hunter.FERRET_OBJECT;
        } else if (npc.getId() == 2910) {
            asObjectId = Hunter.CHINCHOMPA_OBJECT;
        } else if (npc.getId() == 2911) {
            asObjectId = Hunter.CARNIVOROUS_CHINCHOMPA_OBJECT;
        } else if (npc.getId() == 2912) {
            asObjectId = Hunter.BLACK_CHINCHOMPA_OBJECT;
        }
        if (npc.isLocked() || asObjectId == -1) {
            return;
        }
        var mapObject = npc.getController().getSolidMapObject(npc);
        if (mapObject == null || mapObject.getId() != Hunter.BOX_TRAP_OBJECT) {
            return;
        }
        if (!(mapObject.getAttachment() instanceof TempMapObject)
                || !(mapObject.getAttachment().getAttachment() instanceof Integer)) {
            return;
        }
        var player = Main.getWorld().getPlayerById(mapObject.getAttachment().getAttachment());
        if (player == null || !player.getHunter().canHuntObject(asObjectId)) {
            return;
        } else if (player.getHunter().success(Hunter.getObjectLevelRequirement(asObjectId), true)) {
            mapObject.setId(asObjectId);
            npc.getCombat().timedDeath(2);
        } else {
            mapObject.setId(Hunter.BOX_TRAP_FAIL_OBJECT);
        }
        mapObject.getAttachment().setTick(HunterTrap.TRAP_EXPIRIY);
        npc.getController().sendMapObject(mapObject);
    }
};
