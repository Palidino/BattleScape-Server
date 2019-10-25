var npc = null;

cs = new NCombatScript() {
    /* @Override */
    setNpcHook: function(n) {
        npc = n;
    },

    /* @Override */
    canBeAggressiveHook: function(entity) {
        if (!(entity instanceof Player)) {
            return true;
        }
        if (npc.getId() == NpcId.BATTLE_MAGE_54 && (entity.getEquipment().getWeaponId() == ItemId.ZAMORAK_STAFF
                || entity.getEquipment().getCapeId() == ItemId.ZAMORAK_CAPE
                || entity.getEquipment().getCapeId() == ItemId.ZAMORAK_MAX_CAPE
                || entity.getEquipment().getCapeId() == ItemId.IMBUED_ZAMORAK_CAPE
                || entity.getEquipment().getCapeId() == ItemId.IMBUED_ZAMORAK_MAX_CAPE)) {
            return false;
        } else if (npc.getId() == 1611 && (entity.getEquipment().getWeaponId() == ItemId.SARADOMIN_STAFF
                || entity.getEquipment().getCapeId() == ItemId.SARADOMIN_CAPE
                || entity.getEquipment().getCapeId() == ItemId.SARADOMIN_MAX_CAPE
                || entity.getEquipment().getCapeId() == ItemId.IMBUED_SARADOMIN_CAPE
                || entity.getEquipment().getCapeId() == ItemId.IMBUED_SARADOMIN_MAX_CAPE)) {
            return false;
        } else if (npc.getId() == 1612 && (entity.getEquipment().getWeaponId() == ItemId.GUTHIX_STAFF
                || entity.getEquipment().getCapeId() == ItemId.GUTHIX_CAPE
                || entity.getEquipment().getCapeId() == ItemId.GUTHIX_MAX_CAPE
                || entity.getEquipment().getCapeId() == ItemId.IMBUED_GUTHIX_CAPE
                || entity.getEquipment().getCapeId() == ItemId.IMBUED_GUTHIX_MAX_CAPE)) {
            return false;
        }
        return true;
    }
};
