var COMBAT_ID = NpcId.CURSED_KRAKEN_127_16013;
var WHIRLPOOL_ID = NpcId.CURSED_WHIRLPOOL_127_16014;

var npc = null;

cs = new NCombatScript() {
    setNpcHook: function(n) {
        npc = n;
    },

    tick: function() {
        if (npc.isLocked()) {
            return;
        }
        if (npc.getInCombatDelay() == 0 && !npc.isAttacking() && npc.getId() != WHIRLPOOL_ID) {
            npc.setTransformationId(WHIRLPOOL_ID);
        } else if (npc.getInCombatDelay() > 0 || npc.isAttacking()) {
            npc.getMovement().clear();
            if (npc.getId() != COMBAT_ID) {
                npc.setTransformationId(COMBAT_ID);
                npc.setAnimation(7135);
                npc.setHitDelay(4);
            }
        }
    },

    canBeAttackedHook: function(player, sendMessage, hitType) {
        if (hitType == HitType.MELEE || hitType == HitType.RANGED) {
            if (sendMessage) {
                player.getGameEncoder().sendMessage("Only magic seems effective against these...");
            }
            return false;
        }
        return true;
    },

    droppingItemHook: function(player, droppingItem, dropTableChance) {
        if (!player.getSkills().isWildernessSlayerTask(npc)) {
            player.getGameEncoder().sendMessage("Your loot immediately fades away. Maybe a task would help...");
            return null;
        }
        return droppingItem;
    }
};
