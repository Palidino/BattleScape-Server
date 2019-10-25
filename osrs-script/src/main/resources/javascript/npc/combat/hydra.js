var ANIMATIONS = [ 8261, 8262, 8263 ];
var TILE_GRAPHICS = [
    new Graphic(1654), new Graphic(1655), new Graphic(1656), new Graphic(1657),
    new Graphic(1658), new Graphic(1659), new Graphic(1660), new Graphic(1661)
];

var POISON_ATTACK = new NCombatStyle();
POISON_ATTACK.setType(HitType.MAGIC);
POISON_ATTACK.setSubType(HitType.TYPELESS);
POISON_ATTACK.setHitMark(HitMark.POISON);
POISON_ATTACK.setMaxHit(4);
POISON_ATTACK.setFullDamage(true);
POISON_ATTACK.setAttackSpeed(6);
POISON_ATTACK.setTargetGraphic(new Graphic(1645));
POISON_ATTACK.setTargetTileGraphic(new Graphic(1654));
POISON_ATTACK.setProjectileId(1644);
POISON_ATTACK.setPoison(4);
POISON_ATTACK.setIgnorePrayer(true);
POISON_ATTACK.setSpeedMinDistance(8);
POISON_ATTACK.setTargetTile(true);
POISON_ATTACK.setTargetTileDuration(16);
POISON_ATTACK.setTargetTileRadius(1);
POISON_ATTACK.setTargetTileBreakOff(2);
POISON_ATTACK.setTargetTileBreakOffDistance(3);

var npc = null;
var hitStyle = PRandom.randomE(2) == 0 ? HitType.RANGED : HitType.MAGIC;
var hitCount = 0;
var poisonDelay = 8;

cs = new NCombatScript() {
    setNpcHook: function(n) {
        npc = n;
    },

    restore: function() {
        hitStyle = PRandom.randomE(2) == 0 ? HitType.RANGED : HitType.MAGIC;
        hitCount = 0;
        poisonDelay = 8;
    },

    hitTypeHook: function(hitType) {
        return hitStyle;
    },

    combatStyleHook: function(combatStyle) {
        return poisonDelay == 0 ? POISON_ATTACK : combatStyle;
    },

    animationHook: function(combatStyle) {
        return ANIMATIONS[PRandom.randomE(ANIMATIONS.length)];
    },

    targetTileGraphicHook: function(combatStyle) {
        if (combatStyle.getTargetTileGraphic() != null && combatStyle.getTargetTileGraphic().getId() == 1654) {
            return TILE_GRAPHICS[PRandom.randomE(TILE_GRAPHICS.length)];
        }
        return combatStyle.getTargetTileGraphic();
    },

    applyAttackEndHook: function(combatStyle, entity, hitEvent) {
        if (++hitCount == 3) {
            hitStyle = hitStyle == HitType.RANGED ? HitType.MAGIC : HitType.RANGED;
            hitCount = 0;
        }
        if (--poisonDelay < 0) {
            poisonDelay = 8;
        }
    }
};
