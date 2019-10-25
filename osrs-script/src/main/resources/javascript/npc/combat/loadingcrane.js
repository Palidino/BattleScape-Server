var npc = null;

cs = new NCombatScript() {
    /* @Override */
    setNpcHook: function(n) {
        npc = n;
    },

    /* @Override */
    canAttackEntityHook: function(entity, combatStyle) {
        return npc.getWorld().getTargetNPC(3616, entity) != null && npc.withinDistance(entity, 1);
    }
};
