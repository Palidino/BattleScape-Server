var SPECIAL_ATTACK = new NCombatStyle();
SPECIAL_ATTACK.setType(HitType.MAGIC);
SPECIAL_ATTACK.setSubType(HitType.TYPELESS);
SPECIAL_ATTACK.setAnimation(7815);
SPECIAL_ATTACK.setMaxHit(38);
SPECIAL_ATTACK.setAttackSpeed(4);
SPECIAL_ATTACK.setProjectileId(1453);
SPECIAL_ATTACK.setIgnorePrayer(true);
SPECIAL_ATTACK.setSpeedMinDistance(8);
SPECIAL_ATTACK.setMagicBind(6);
SPECIAL_ATTACK.setTargetTile(true);

var npc = null;

cs = new NCombatScript() {
    setNpcHook: function(n) {
        npc = n;
    },

    combatStyleHook: function(combatStyle) {
        return PRandom.randomE(8) == 0 ? SPECIAL_ATTACK : combatStyle;
    },

    dropItemHook: function(player, dropTile, dropForIndex, hasRoWICharge) {
        if (npc.inCatacombsofKourend()) {
            var totemBase = player.hasItem(19679);
            var totemMiddle = player.hasItem(19681);
            var totemTop = player.hasItem(19683);
            var hasAll = totemBase && totemMiddle && totemTop;
            var totemIds = new ArrayList();
            if (!totemBase || hasAll) {
                totemIds.add(19679);
            }
            if (!totemMiddle || hasAll) {
                totemIds.add(19681);
            }
            if (!totemTop || hasAll) {
                totemIds.add(19683);
            }
            var itemId = totemIds.get(PRandom.randomE(totemIds.size()));
            npc.getController().addMapItem(new Item(itemId, 1), dropTile, player);
        }
    }
};
