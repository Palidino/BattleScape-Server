var npc = null;
var usingSpecialAttack = false;
var specialAttackCount = 0;
var specialAttackTile = null;

cs = new NCombatScript() {
    setNpcHook: function(n) {
        npc = n;
    },

    restore: function() {
        usingSpecialAttack = false;
        specialAttackCount = 0;
        specialAttackTile = null;
    },

    tick: function() {
        if (npc.isAttacking() && !usingSpecialAttack && PRandom.randomE(20) == 0) {
            usingSpecialAttack = true;
            specialAttackTile = new Tile(npc);
        }
    },

    attackSpeedHook: function(combatStyle) {
        return usingSpecialAttack ? 2 : combatStyle.getAttackSpeed();
    },

    applyAttackEndHook: function(combatStyle, entity, hitEvent) {
        if (!usingSpecialAttack) {
            return;
        }
        if (++specialAttackCount >= 4) {
            npc.getMovement().teleport(specialAttackTile);
            usingSpecialAttack = false;
            specialAttackCount = 0;
            specialAttackTile = null;
        } else {
            var t = new Tile(entity);
            var tries = 0;
            while (tries++ < 8 && (t.matchesTile(entity) || t.matchesTile(npc) || !Route.canMove(npc, t))) {
                t.setTile(entity);
                t = (PRandom.randomI(1) == 0) ? t.randomizeX(1) : t.randomizeY(1);
            }
            if (!Route.canMove(npc, t)) {
                t = npc;
            }
            npc.getMovement().teleport(t);
        }
    },

    accuracyHook: function(combatStyle, accuracy) {
        return usingSpecialAttack ? Integer.MAX_VALUE : accuracy;
    },

    droppingItemHook: function(player, droppingItem, dropTableChance) {
        if (!player.getSkills().isWildernessSlayerTask(npc)) {
            player.getGameEncoder().sendMessage("Your loot immediately fades away. Maybe a task would help...");
            return null;
        }
        return droppingItem;
    }
};
