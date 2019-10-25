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
        if (npc.getId() == 5549) {
            asObjectId = Hunter.CRIMSON_SWIFT_OBJECT;
        } else if (npc.getId() == 5551) {
            asObjectId = Hunter.GOLDEN_WARBLER_OBJECT;
        } else if (npc.getId() == 5552) {
            asObjectId = Hunter.COPPER_LONGTAIL_OBJECT;
        } else if (npc.getId() == 5550) {
            asObjectId = Hunter.CERULEAN_TWITCH_OBJECT;
        } else if (npc.getId() == 5548) {
            asObjectId = Hunter.TROPICAL_WAGTAIL_OBJECT;
        }
        if (npc.isLocked() || asObjectId == -1) {
            return;
        }
        var mapObject = npc.getController().getSolidMapObject(npc);
        if (mapObject == null || mapObject.getId() != Hunter.BIRD_SNARE_OBJECT) {
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
            mapObject.setId(Hunter.BIRD_SNARE_FAIL_OBJECT);
        }
        mapObject.getAttachment().setTick(HunterTrap.TRAP_EXPIRIY);
        npc.getController().sendMapObject(mapObject);
    }
};
