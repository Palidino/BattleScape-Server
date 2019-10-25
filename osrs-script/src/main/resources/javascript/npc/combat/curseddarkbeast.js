var SPECIAL_ATTACK = new NCombatStyle();
SPECIAL_ATTACK.setType(HitType.MAGIC);
SPECIAL_ATTACK.setAnimation(2731);
SPECIAL_ATTACK.setMaxHit(25);
SPECIAL_ATTACK.setAttackSpeed(2);
SPECIAL_ATTACK.setTargetGraphic(new Graphic(101, 124));
SPECIAL_ATTACK.setProjectileId(130);
SPECIAL_ATTACK.setSpeedMinDistance(8);
SPECIAL_ATTACK.setTargetTile(true);
SPECIAL_ATTACK.setTargetTileRadius(1);
SPECIAL_ATTACK.setTargetTileRadiusProjectiles(true);

var npc = null;
var usingSpecialAttack = false;
var specialAttackCount = 0;

cs = new NCombatScript() {
    setNpcHook: function(n) {
        npc = n;
    },

    restore: function() {
        usingSpecialAttack = false;
        specialAttackCount = 0;
    },

    tick: function() {
        if (npc.isAttacking() && !usingSpecialAttack && PRandom.randomE(20) == 0) {
            usingSpecialAttack = true;
        }
    },

    combatStyleHook: function(combatStyle) {
        return usingSpecialAttack ? SPECIAL_ATTACK : combatStyle;
    },

    applyAttackEndHook: function(combatStyle, entity, hitEvent) {
        if (!usingSpecialAttack) {
            return;
        }
        if (++specialAttackCount >= 3) {
            usingSpecialAttack = false;
            specialAttackCount = 0;
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
