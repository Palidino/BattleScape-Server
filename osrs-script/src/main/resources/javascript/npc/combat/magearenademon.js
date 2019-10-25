var npc = null;

cs = new NCombatScript() {
    /* @Override */
    setNpcHook: function(n) {
        npc = n;
    },

    /* @Override */
    tileHitEventHook: function(combatStyle, tileHitEvent, speed) {
        if (npc.getId() == 7859) {
            var event = new Event(tileHitEvent.getTick()) {
                /* @Override */
                execute: function() {
                    event.stop();
                    if (npc.isLocked()) {
                        return;
                    }
                    var spawn = new Npc(npc.getController(), 7514, tileHitEvent.getTile());
                }
            };
            npc.getWorld().addEvent(event);
        }
    },

    /* @Override */
    canBeAttackedHook: function(player, sendMessage, hitType) {
        var spell = player.getMagic().getActiveSpell();
        if (hitType != HitType.MAGIC || spell == null) {
            if (sendMessage) {
                player.getGameEncoder().sendMessage("This demon can only be affected by magic.");
            }
            return false;
        }
        if (npc.getId() == 7858 && !spell.getName().equals("saradomin strike")) {
            if (sendMessage) {
                player.getGameEncoder().sendMessage("This demon can only be affected by Saradomin Strike.");
            }
            return false;
        } else if (npc.getId() == 7859 && !spell.getName().equals("claws of guthix")) {
            if (sendMessage) {
                player.getGameEncoder().sendMessage("This demon can only be affected by Claws of Guthix.");
            }
            return false;
        } else if (npc.getId() == 7860 && !spell.getName().equals("flames of zamorak")) {
            if (sendMessage) {
                player.getGameEncoder().sendMessage("This demon can only be affected by Flames of Zamorak.");
            }
            return false;
        }
        return true;
    }
};
