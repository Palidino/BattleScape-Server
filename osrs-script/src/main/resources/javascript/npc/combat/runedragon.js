var npc = null;
var boltEffect;

cs = new NCombatScript() {
    /* @Override */
    setNpcHook: function(n) {
        npc = n;
    },

    /* @Override */
    tick: function() {
        if (!npc.isLocked() && npc.getHitDelay() == 0 && npc.isAttacking()
                && npc.getEngagingEntity() instanceof Player && PRandom.randomE(4) == 0
                && npc.withinDistance(npc.getEngagingEntity(), 10)) {
            npc.setAnimation(81);
            npc.setHitDelay(4);
            var tiles = [
                new Tile(npc.getEngagingEntity()).randomize(4), new Tile(npc.getEngagingEntity()).randomize(4)
            ];
            for each (var tile in tiles) {
                if (!Route.canMove(npc.getEngagingEntity(), tile)) {
                    continue;
                }
                var spawn = new Npc(npc.getController(), 8032, tile);
                spawn.getMovement().setFollowing(npc.getEngagingEntity());
            }
        }
    },

    /* @Override */
    applyAttackStartHook: function(combatStyle, entity, count) {
        boltEffect = combatStyle.getType() == HitType.RANGED && PRandom.randomE(10) == 0;
    },

    /* @Override */
    damageInflictedHook: function(combatStyle, damage, entity) {
        if (boltEffect) {
            damage *= 1.2;
            npc.applyHit(new Hit((damage * 0.25)|0, HitMark.HEAL));
        }
        return damage;
    },

    /* @Override */
    targetGraphicHook: function(combatStyle) {
        if (boltEffect) {
            new Graphic(753);
        }
        return combatStyle.getTargetGraphic();
    }
};
