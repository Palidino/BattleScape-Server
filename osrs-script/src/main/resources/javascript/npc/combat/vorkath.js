var POISON_FIREBALL = new NCombatStyle();
POISON_FIREBALL.setType(HitType.MAGIC);
POISON_FIREBALL.setSubType(HitType.TYPELESS);
POISON_FIREBALL.setMaxHit(30);
POISON_FIREBALL.setAttackSpeed(1);
POISON_FIREBALL.setTargetGraphic(new Graphic(131));
POISON_FIREBALL.setProjectileId(1482);
POISON_FIREBALL.setProjectileStartHeight(30);
POISON_FIREBALL.setIgnorePrayer(true);
POISON_FIREBALL.setSpeedMinDistance(8);
POISON_FIREBALL.setTargetTile(true);

var FREEZE_FIREBALL = new NCombatStyle();
FREEZE_FIREBALL.setType(HitType.MAGIC);
FREEZE_FIREBALL.setSubType(HitType.TYPELESS);
FREEZE_FIREBALL.setMaxHit(0);
FREEZE_FIREBALL.setAttackSpeed(10);
FREEZE_FIREBALL.setTargetGraphic(new Graphic(369));
FREEZE_FIREBALL.setProjectileId(395);
FREEZE_FIREBALL.setProjectileStartHeight(30);
FREEZE_FIREBALL.setIgnorePrayer(true);
FREEZE_FIREBALL.setMagicBind(15);

var npc = null;
var lastCombatStyle;
var autoAttacks;
var specialAttack;
var poisonTiles;
var poisonFireballs;
var spawn = null;

cs = new NCombatScript() {
    setNpcHook: function(n) {
        npc = n;
    },

    restore: function() {
        lastCombatStyle = null;
        autoAttacks = 6;
        specialAttack = PRandom.randomE(2);
        poisonTiles = null;
        poisonFireballs = 0;
        npc.getWorld().removeNpc(spawn);
    },

    tick: function() {
        if (lastCombatStyle != null && lastCombatStyle.getProjectileId() == 1481 && npc.getHitDelay() == 1) {
            npc.setAnimation(-1);
        }
        if (npc.getEngagingEntity() != null && npc.getController().getMapObject(32000, npc.getEngagingEntity())) {
            npc.getEngagingEntity().addHit(new HitEvent(0, npc.getEngagingEntity(), new Hit(PRandom.randomI(10))));
        }
        if (specialAttack == 0 && npc.isAttacking() && npc.getHitDelay() == 0 && autoAttacks == 0) {
            autoAttacks = 6;
            specialAttack = 1;
            poisonTiles = [];
            poisonFireballs = 25;
            var speed = cs.getSpeed(10);
            for (var i = 0; i < 64; i++) {
                var tile = new Tile(2261 + PRandom.randomI(22), 4054 + PRandom.randomI(22));
                if (tile.within(2270, 4062, 2274, 4067) || tile.within(2269, 4063, 2275, 4067)) {
                    continue;
                }
                poisonTiles.push(tile);
                npc.getCombat().sendMapProjectile(null, npc, tile, 1483, 43, 31, speed.clientDelay,
                        speed.clientSpeed, 48, 64);
            }
            var event = new Event(speed.eventDelay) {
                /* @Override */
                execute: function() {
                    event.stop();
                    if (npc.isLocked()) {
                        return;
                    }
                    for each (var tile in poisonTiles) {
                        var poison = new MapObject(32000, tile, 10, PRandom.randomI(3));
                        npc.getWorld().addEvent(new TempMapObject(25 - speed.eventDelay, npc.getController(),
                                poison));
                    }
                }
            };
            npc.getWorld().addEvent(event);
            npc.setAnimation(7957);
            npc.setHitDelay(2);
        }
    },

    combatStyleHook: function(combatStyle) {
        return (specialAttack == 1 && autoAttacks == 0) ? FREEZE_FIREBALL : (poisonFireballs > 0
                ? POISON_FIREBALL : combatStyle);
    },

    applyAttackCombatStyleHook: function(combatStyle, entity) {
        return (entity.getController().isMagicBound() && combatStyle.getIdentifier() == 1) ? null : combatStyle;
    },

    applyAttackEndHook: function(combatStyle, entity, hitEvent) {
        lastCombatStyle = combatStyle;
        if (combatStyle.getProjectileId() == 1471 && entity instanceof Player) {
            entity.getPrayer().deactivateAll();
            entity.getGameEncoder().sendMessage("<col=ff0000>Your prayers have been disabled!");
        } else if (combatStyle == FREEZE_FIREBALL) {
            var event = new Event(hitEvent.getTick()) {
                /* @Override */
                execute: function() {
                    event.stop();
                    if (npc.isLocked()) {
                        return;
                    }
                    npc.getWorld().removeNpc(spawn);
                    var tile = PRandom.randomI(1) == 0 ? new Tile(2265, 4057, 0) : new Tile(2278, 4069, 0);
                    spawn = new Npc(npc.getController(), 8063, tile);
                    spawn.getMovement().setFollowing(npc.getEngagingEntity());
                }
            }
            npc.getWorld().addEvent(event);
        }
    },

    attackEndHook: function() {
        if (lastCombatStyle == POISON_FIREBALL) {
            if (--poisonFireballs == 0) {
                npc.setHitDelay(10);
            }
        } else if (lastCombatStyle == FREEZE_FIREBALL) {
            autoAttacks = 6;
            specialAttack = 0;
        } else {
            autoAttacks--;
        }
    },

    damageReceivedHook: function(damage, entity, hitType, defenceType) {
        if (poisonFireballs > 0) {
            damage *= 0.5;
        }
        return damage;
    },

    proccessDragonfireDamage: function(combatStyle, damage, entity) {
        if (entity instanceof Player) {
            if (entity.getSkills().getSuperAntifireTime() > 0) {
                damage *= 0.6;
            } else if (entity.getSkills().getAntifireTime() > 0) {
                damage *= 0.8;
            }
            if (entity.getEquipment().wearingDragonfireShield()
                    || entity.getPrayer().hasActive("protect from magic")) {
                damage *= 0.4;
            }
            if (entity.getSkills().getSuperAntifireTime() && entity.getEquipment().wearingDragonfireShield()) {
                damage = 0;
            }
        }
        return damage;
    },

    dropItemHook: function(player, dropTile, dropForIndex, hasRoWICharge) {
        if ((player.getCombat().getNPCKillCount(npc.getDef().getKillCountName()) % 50) != 0) {
            return;
        }
        npc.getController().addMapItem(new Item(ItemId.VORKATHS_HEAD_21907, 1), dropTile, player);
        player.getCombat().logNPCItem(npc.getDef().getKillCountName(), ItemId.VORKATHS_HEAD_21907, 1);
    }
};
