var npc = null;

cs = new NCombatScript() {
    /* @Override */
    setNpcHook: function(n) {
        npc = n;
    },

    /* @Override */
    canBeAttackedHook: function(player, sendMessage, hitType) {
        if (!Settings.getInstance().isSpawn() && !player.getSkills().isAnySlayerTask(npc) && !player.isUsergroup(SqlUserRank.YOUTUBER)) {
            if (sendMessage) {
                player.getGameEncoder().sendMessage("This can only be attacked on an appropriate Slayer task.");
            }
            return false;
        }
        return true;
    }
};
