var npc = null;
var changeDelay = 0;

cs = new NCombatScript() {
    /* @Override */
    setNpcHook: function(n) {
        npc = n;
    },

    /* @Override */
    restore: function() {
        this.setChangeDelay();
    },

    /* @Override */
    tick: function() {
        if (npc.isLocked()) {
            return;
        }
        if (changeDelay > 0) {
            changeDelay--;
            if (changeDelay == 0) {
                this.setChangeDelay();
                if (npc.getId() == 983) {
                    npc.setTransformationId(984);
                    npc.setForceMessage("Krrrrrrk");
                } else if (npc.getId() == 984) {
                    npc.setTransformationId(988);
                    npc.setForceMessage("Chkhkhkhkhk");
                } else if (npc.getId() == 988) {
                    npc.setTransformationId(986);
                    npc.setForceMessage("Krrrrrrssssssss");
                } else if (npc.getId() == 986) {
                    npc.setTransformationId(985);
                    npc.setForceMessage("Sssssrrrkkkkkk");
                } else if (npc.getId() == 985) {
                    npc.setTransformationId(987);
                    npc.setForceMessage("Krkrkrkrkrkrkrkr");
                } else if (npc.getId() == 987) {
                    npc.setTransformationId(983);
                    npc.setForceMessage("Tktktktktktkt");
                }
            }
        }
    },

    /* @Override */
    damageReceivedHook: function(damage, entity, hitType, defenceType) {
        if (!(entity instanceof Player)) {
            return damage;
        }
        var spell = entity.getMagic().getActiveSpell();
        if (npc.getId() == 983 && (hitType != HitType.MAGIC || spell == null
                || !spell.getName().startsWith("wind"))) {
            return 0;
        } else if (npc.getId() == 988 && hitType != HitType.MELEE) {
            return 0;
        } else if (npc.getId() == 984 && (hitType != HitType.MAGIC || spell == null
                || !spell.getName().startsWith("water"))) {
            return 0;
        } else if (npc.getId() == 985 && (hitType != HitType.MAGIC || spell == null
                || !spell.getName().startsWith("fire"))) {
            return 0;
        } else if (npc.getId() == 987 && hitType != HitType.RANGED) {
            return 0;
        } else if (npc.getId() == 986 && (hitType != HitType.MAGIC || spell == null
                || !spell.getName().startsWith("earth"))) {
            return 0;
        }
        return damage;
    },

    /* @Override */
    dropItemHook: function(player, dropTile, dropForIndex, hasRoWICharge) {
        player.getGameEncoder().sendMessage("You have defeated the Dagannoth Mother!");
        player.getCombat().setHorrorFromTheDeep(true);
        player.getGameEncoder().sendMessage("<col=ff0000>You have completed Horror from the Deep!");
        player.getInventory().addOrDropItem(ItemId.COINS, 25000);
    },

    setChangeDelay: function() {
        changeDelay = 25 + PRandom.randomI(8);
    }
};
