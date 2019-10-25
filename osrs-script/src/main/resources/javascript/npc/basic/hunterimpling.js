var npc = null;

var changeTime = 0;

instance = new NScript() {
    setNpc: function(n) {
        npc = n;
    },

    getVariable: function(name) {
        return null;
    },

    restore: function() {
        npc.setTransformationId(this.getNpcId());
        changeTime = 250 + PRandom.randomI(250);
    },

    tick: function() {
        if (changeTime > 0) {
            changeTime--;
            if (changeTime == 0) {
                this.restore();
            }
        }
    },

    getNpcId: function() {
        if (PRandom.randomE(20) == 0) {
            return 7233; // Lucky
        } else if (PRandom.randomE(18) == 0) {
            return 1644; // Dragon
        } else if (PRandom.randomE(16) == 0) {
            return 1643; // Ninja
        } else if (PRandom.randomE(14) == 0) {
            return 1642; // Magpie
        } else if (PRandom.randomE(12) == 0) {
            return 1641; // Nature
        } else if (PRandom.randomE(10) == 0) {
            return 1640; // Eclectic
        } else if (PRandom.randomE(8) == 0) {
            return 1639; // Essence
        } else if (PRandom.randomE(6) == 0) {
            return 1638; // Earth
        } else if (PRandom.randomE(4) == 0) {
            return 1637; // Gourmet
        } else if (PRandom.randomE(2) == 0) {
            return 1636; // Young
        }
        return 1635; // Baby
    }
};
