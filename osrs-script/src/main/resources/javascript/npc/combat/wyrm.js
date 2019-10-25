var npc = null;

cs = new NCombatScript() {
    setNpcHook: function(n) {
        npc = n;
    },

    tick: function() {
        if (npc.isLocked()) {
            return;
        }
        if (npc.getId() != NpcId.WYRM_99 && npc.getInCombatDelay() == 0 && !npc.isAttacking()) {
            npc.setTransformationId(NpcId.WYRM_99);
            npc.setAnimation(8269);
        } else if (npc.getId() != NpcId.WYRM_99_8611 && (npc.getInCombatDelay() > 0 || npc.isAttacking())) {
            npc.setTransformationId(NpcId.WYRM_99_8611);
            npc.setAnimation(8268);
        }
    }
};
