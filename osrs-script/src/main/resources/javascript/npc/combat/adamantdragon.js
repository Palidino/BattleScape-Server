var npc = null;
var boltEffect;

cs = new NCombatScript() {
    /* @Override */
    setNpcHook: function(n) {
        npc = n;
    },

    /* @Override */
    applyAttackStartHook: function(combatStyle, entity, count) {
        boltEffect = combatStyle.getType() == HitType.RANGED && PRandom.randomE(20) == 0;
    },

    /* @Override */
    damageInflictedHook: function(combatStyle, damage, entity) {
        if (boltEffect) {
            damage *= 1.15;
        }
        return damage;
    },

    /* @Override */
    targetGraphicHook: function(combatStyle) {
        if (boltEffect) {
            new Graphic(758);
        }
        return combatStyle.getTargetGraphic();
    },

    /* @Override */
    accuracyHook: function(combatStyle, accuracy) {
        if (boltEffect) {
            accuracy *= 1024;
        }
        return accuracy;
    }
};
