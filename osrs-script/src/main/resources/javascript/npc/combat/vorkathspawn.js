var npc = null;
var countdown1 = 2;
var countdown2 = 2;
var following = null;
var explosionDamage = 0;

cs = new NCombatScript() {
    /* @Override */
    setNpcHook: function(n) {
        npc = n;
    },

    /* @Override */
    restore: function() {
        following = null;
    },

    /* @Override */
    tick: function() {
        if (npc.getMovement().getFollowing() != null) {
            following = npc.getMovement().getFollowing();
        }
        if (!npc.isLocked() && !npc.withinMapDistance(following, 32)) {
            npc.getCombat().timedDeath();
            return;
        }
        if (!npc.withinDistance(following, 1)) {
            return;
        }
        if (--countdown1 >= 0) {
            if (countdown1 == 1) {
                npc.getMovement().setFollowing(null);
                npc.getMovement().clear();
            } else if (countdown1 == 0) {
                explosionDamage = (npc.getHitpoints() * 1.4)|0;
                npc.getCombat().timedDeath(countdown2);
            }
        } else if (--countdown2 >= 0) {
            if (countdown2 == 0) {
                if (npc.withinDistance(following, 1)) {
                    var hitEvent = new HitEvent(0, following, new Hit(explosionDamage));
                    following.addHit(hitEvent);
                    following.getController().setMagicBind(0);
                }
            }
        }
    },

    damageReceivedHook: function(damage, entity, hitType, defenceType) {
        return (entity instanceof Player && hitType == HitType.MAGIC && damage > 0
                && entity.getMagic().getActiveSpell() != null
                && entity.getMagic().getActiveSpell().getName().contains("crumble undead"))
                ? npc.getHitpoints() : damage;
    }
};
