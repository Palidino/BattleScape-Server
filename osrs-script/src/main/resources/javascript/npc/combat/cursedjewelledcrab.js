var npc = null;
var changeDelay = 0;

cs = new NCombatScript() {
    setNpcHook: function(n) {
        npc = n;
    },

    script: function(name, args) {
        if (name.equals("smash")) {
            if (npc.getId() != NpcId.CURSED_JEWELLED_CRAB_180_16001) {
                npc.setTransformationId(NpcId.CURSED_JEWELLED_CRAB_180_16001);
                this.setChangeDelay();
            }
        }
        return null;
    },

    restore: function() {
        changeDelay = 1;
    },

    tick: function() {
        if (npc.isLocked()) {
            return;
        }
        if (changeDelay > 0) {
            changeDelay--;
            if (changeDelay == 0) {
                this.setChangeDelay();
                npc.setTransformationId(PRandom.arrayRandom(NpcId.CURSED_JEWELLED_CRAB_BLUE_180_16004,
                        NpcId.CURSED_JEWELLED_CRAB_GREEN_180_16003, NpcId.CURSED_JEWELLED_CRAB_RED_180_16002));
            }
        }
    },

    damageReceivedHook: function(damage, entity, hitType, defenceType) {
        if (npc.getId() == NpcId.CURSED_JEWELLED_CRAB_BLUE_180_16004 && hitType == HitType.MAGIC) {
            return 0;
        } else if (npc.getId() == NpcId.CURSED_JEWELLED_CRAB_GREEN_180_16003 && hitType == HitType.RANGED) {
            return 0;
        } else if (npc.getId() == NpcId.CURSED_JEWELLED_CRAB_RED_180_16002 && hitType == HitType.MELEE) {
            return 0;
        }
        if (npc.getId() != NpcId.CURSED_JEWELLED_CRAB_180_16001) {
            if (hitType == HitType.MAGIC) {
                npc.setTransformationId(NpcId.CURSED_JEWELLED_CRAB_BLUE_180_16004);
                this.setChangeDelay();
            } else if (hitType == HitType.RANGED) {
                npc.setTransformationId(NpcId.CURSED_JEWELLED_CRAB_GREEN_180_16003);
                this.setChangeDelay();
            } else if (hitType == HitType.MELEE) {
                npc.setTransformationId(NpcId.CURSED_JEWELLED_CRAB_RED_180_16002);
                this.setChangeDelay();
            }
        }
        return damage;
    },

    droppingItemHook: function(player, droppingItem, dropTableChance) {
        if (!player.getSkills().isWildernessSlayerTask(npc)) {
            player.getGameEncoder().sendMessage("Your loot immediately fades away. Maybe a task would help...");
            return null;
        }
        return droppingItem;
    },

    setChangeDelay: function() {
        changeDelay = 25 + PRandom.randomI(4);
    }
};
