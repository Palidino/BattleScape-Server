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
        if (npc.getId() == 2906) {
            asObjectId = Hunter.SWAMP_LIZARD_OBJECT;
        } else if (npc.getId() == 2903) {
            asObjectId = Hunter.ORANGE_SALAMANDER_OBJECT;
        } else if (npc.getId() == 2904) {
            asObjectId = Hunter.RED_SALAMANDER_OBJECT;
        } else if (npc.getId() == 2905) {
            asObjectId = Hunter.BLACK_SALAMANDER_OBJECT;
        }
        if (npc.isLocked() || asObjectId == -1) {
            return;
        }
        var mapObject = npc.getController().getSolidMapObject(npc);
        if (mapObject == null || mapObject.getId() != Hunter.NET_TRAP_OBJECT) {
            return;
        }
        if (!(mapObject.getAttachment() instanceof TempMapObject)
                || !(mapObject.getAttachment().getAttachment() instanceof Integer)) {
            return;
        }
        var player = Main.getWorld().getPlayerById(mapObject.getAttachment().getAttachment());
        if (player == null || !player.getHunter().canHuntObject(asObjectId)) {
            return;
        }
        mapObject.getAttachment().resetMapObject(0);
        if (player.getHunter().success(Hunter.getObjectLevelRequirement(asObjectId), true)) {
            mapObject.getAttachment().getTempMapObject(1).setId(asObjectId);
            npc.getCombat().timedDeath(2);
        } else {
            mapObject.getAttachment().getTempMapObject(1).setId(Hunter.NET_TRAP_FAIL_OBJECT);
        }
        npc.getController().sendMapObject(mapObject.getAttachment().getTempMapObject(1));
    }
};
