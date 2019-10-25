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

    droppingItemHook: function(player, droppingItem, dropTableChance) {
        if (!player.getSkills().isWildernessSlayerTask(npc)) {
            player.getGameEncoder().sendMessage("Your loot immediately fades away. Maybe a task would help...");
            return null;
        }
        return droppingItem;
    }
};
