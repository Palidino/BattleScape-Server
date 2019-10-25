var IDLE_ID = 8338, HEAL_ID = 8339, COMBAT_ID = 8340;
var HEAL_TIME = 160, DIRECTION_TIME = 9;
var ATTACKING_DIRECTIONS = [
    new Tile(3177, 4394, 1), new Tile(3177, 4380, 1), new Tile(3163, 4380, 1), new Tile(3163, 4394, 1)
];

var PHASE_3_ATTACK = new NCombatStyle();
PHASE_3_ATTACK.setType(HitType.MAGIC);
PHASE_3_ATTACK.setSubType(HitType.TYPELESS);
PHASE_3_ATTACK.setHitMark(HitMark.POISON);
PHASE_3_ATTACK.setAnimation(8059);
PHASE_3_ATTACK.setMaxHit(49);
PHASE_3_ATTACK.setAttackSpeed(6);
PHASE_3_ATTACK.setTargetGraphic(new Graphic(1556));
PHASE_3_ATTACK.setProjectileId(1555);
PHASE_3_ATTACK.setIgnorePrayer(true);


var npc = null;
var loaded = false;
var baseHitpoints = 0;
var healTime = HEAL_TIME;
var exhumedList = new ArrayList();
var acidicList = new ArrayList();
var hasScreeched = false;
var attackingDirection = PRandom.arrayRandom(ATTACKING_DIRECTIONS);
var directionChange = DIRECTION_TIME;

cs = new NCombatScript() {
    /* @Override */
    setNpcHook: function(n) {
        npc = n;
    },

    /* @Override */
    tick: function() {
        if (!loaded) {
            this.loadProfile();
            return;
        }
        if (!npc.getController().isRegionLoaded()) {
            return;
        }
        if (npc.getId() == IDLE_ID) {
            var players = npc.getController().getPlayers();
            for each (var player in players) {
                if (!npc.withinDistance(player, 4)) {
                    continue;
                }
                npc.setTransformationId(HEAL_ID);
                break;
            }
        } else if (npc.getId() == HEAL_ID) {
            this.healPhase();
        } else if (npc.getId() == COMBAT_ID) {
            this.combatPhase();
        }
    },

    /* @Override */
    damageReceivedHook: function(damage, entity, hitType, defenceType) {
        if (!hasScreeched || npc.getHitDelay() > 0 || !entity.withinDistance(attackingDirection, 7)) {
            return damage;
        }
        npc.getCombat().applyAttack(PHASE_3_ATTACK, entity, true,
                cs.getSpeed(entity), 0);
        return damage;
    },

    /* @Override */
    tileHitEventHook: function(combatStyle, tileHitEvent, speed) {
        for each (var event in acidicList) {
            if ((event.getAttachment() instanceof MapObject)
                    && tileHitEvent.getTile().matchesTile(event.getAttachment())
                    || (event.getAttachment() instanceof TempMapObject)
                    && tileHitEvent.getTile().matchesTile(event.getAttachment().getTempMapObject(0))) {
                return;
            }
        }
        var mapObject = new MapObject(32744, tileHitEvent.getTile(), 22, MapObject.getRandomDirection());
        var event = new Event(speed.eventDelay) {
            execute: function() {
                if (event.getExecutions() == 0) {
                    event.setTick(0);
                    var tempMapObject = new TempMapObject(Integer.MAX_VALUE, npc.getController(), mapObject);
                    npc.getWorld().addEvent(tempMapObject);
                    event.setAttachment(tempMapObject);
                } else if (event.getExecutions() == 1) {
                    for each (var player in npc.getController().getPlayers()) {
                        player.getGameEncoder().sendMapObjectAnimation(mapObject, 8068);
                    }
                } else if (!npc.isVisible()) {
                    event.stop();
                } else {
                    for each (var player in npc.getController().getPlayers()) {
                        if (!player.matchesTile(mapObject)) {
                            continue;
                        }
                        player.addHit(new HitEvent(0, player, new Hit(6, HitMark.POISON)));
                    }
                }
            },

            stopHook: function() {
                if (!(event.getAttachment() instanceof TempMapObject)) {
                    return;
                }
                event.getAttachment().stop();
            }
        };
        event.setAttachment(mapObject);
        npc.getWorld().addEvent(event);
        acidicList.add(event);
    },

    /* @Override */
    canAttackEntityHook: function(entity, combatStyle) {
        return npc.getHitpoints() > npc.getMaxHitpoints() * 0.23 || combatStyle == PHASE_3_ATTACK;
    },

    /* @Override */
    canBeAttackedHook: function(player, sendMessage, hitType) {
        return npc.getId() != IDLE_ID;
    },

    loadProfile: function() {
        if (loaded || npc.getController().getPlayers().isEmpty()) {
            return;
        }
        loaded = true;
        var averageHP = 0;
        var playerMultiplier = 1;
        var players = npc.getController().getPlayers();
        for each (var player in players) {
            averageHP += player.getMaxHitpoints();
            playerMultiplier = PNumber.addDoubles(playerMultiplier, 0.5);
        }
        averageHP /= players.size();
        baseHitpoints = ((50 + (players.size() * 25) + (averageHP * 2)) * playerMultiplier)|0;
        npc.setMaxHitpoints(baseHitpoints);
        npc.setHitpoints(npc.getMaxHitpoints());
    },

    healPhase: function() {
        if (npc.getMaxHitpoints() >= baseHitpoints * 1.33) {
            healTime = 0;
        }
        if (healTime-- > 0) {
            var playerCount = npc.getController().getPlayers().size();
            var divider = HEAL_TIME / (playerCount * 2);
            var exhumedCount = Math.min(playerCount, Math.max(1, ((HEAL_TIME - healTime) / 20)|0));
            var newExhumedCount = exhumedCount - exhumedList.size();
            for (var iterator = exhumedList.iterator(); iterator.hasNext();) {
                var exumedEvent = iterator.next();
                if (exumedEvent.isRunning()) {
                    continue;
                }
                iterator.remove();
                newExhumedCount++;
            }
            for (var i = 0; i < newExhumedCount; i++) {
                var tile = new Tile(3170, 4387, 1);
                var attempts = 0;
                do {
                    tile.randomize(7);
                } while (attempts++ < 4 && npc.withinDistance(tile, 0));
                var mapObject = new MapObject(32743, tile, 22, 0);
                var tempMapObject = new TempMapObject(16, npc.getController(), mapObject);
                npc.getWorld().addEvent(tempMapObject);
                var eventListener = new EventListener() {
                    execute: function(event) {
                        var mapObject = event.getAttachment().getTempMapObject(0);
                        if (event.getExecutions() == 1) {
                            if (mapObject != null) {
                                for each (var player in npc.getController().getPlayers()) {
                                    player.getGameEncoder().sendMapObjectAnimation(mapObject, 8065);
                                }
                            }
                        } else if (event.getExecutions() == 16 || !npc.isVisible() || npc.getId() != HEAL_ID) {
                            event.stop();
                            if (mapObject != null) {
                                npc.getController().sendMapGraphic(mapObject, new Graphic(1549));
                            }
                        } else if (event.getExecutions() > 1) {
                            var hasPlayer = false;
                            for each (var player in npc.getController().getPlayers()) {
                                if (!player.matchesTile(mapObject)) {
                                    continue;
                                }
                                hasPlayer = true;
                                break;
                            }
                            if (!hasPlayer && mapObject != null) {
                                var speed = cs.getSpeed(tile);
                                cs.sendMapProjectile(npc, mapObject, npc, 1550, 0, 50, speed.clientDelay,
                                        speed.clientSpeed, 48, 64);
                                npc.addHit(new HitEvent(speed.eventDelay, npc, new Hit(6, HitMark.HEAL)));
                                if (npc.getMaxHitpoints() < baseHitpoints * 1.33) {
                                    npc.setHitpoints(npc.getHitpoints() + 6);
                                    npc.setMaxHitpoints(npc.getMaxHitpoints() + 6);
                                }
                            }
                        }
                    },

                    stopHook: function(event) {
                        event.getAttachment().stop();
                    }
                };
                var event = npc.getWorld().addEvent(eventListener, 0);
                event.setAttachment(tempMapObject);
                exhumedList.add(event);
            }
        } else if (healTime <= 0) {
            npc.setTransformationId(COMBAT_ID);
            npc.setAnimation(8061);
            for each (var event in exhumedList) {
                event.stop();
            }
        }
    },

    combatPhase: function() {
        for each (var player in npc.getController().getPlayers()) {
            if (!npc.withinDistance(player, 0)) {
                continue;
            }
            player.addHit(new HitEvent(0, player, new Hit(6)));
        }
        if (npc.getHitpoints() > npc.getMaxHitpoints() * 0.23) {
            return;
        }
        if (!hasScreeched) {
            hasScreeched = true;
            npc.setForceMessage("Screeeeech!");
            npc.setFaceTile(attackingDirection);
            npc.getCombat().setDisableAutoRetaliate(true);
            for each (var player in npc.getController().getPlayers()) {
                player.getGameEncoder().sendMessage("Xarpus begins to stare intently.");
            }
            npc.setHitDelay(12);
        }
        if (directionChange-- > 0) {
            return;
        }
        attackingDirection = PRandom.arrayRandom(ATTACKING_DIRECTIONS);
        directionChange = DIRECTION_TIME;
        npc.setFaceTile(attackingDirection);
    }
}
