var PHASE_IDS = [ 8360, 8361, 8362, 8363, 8364, 8365 ];

var npc = null;
var loaded = false;
var initialAttackDelay = false;
var bloodSpots = new ArrayList();
var phase = 0;
var spiders = new ArrayList();
var spawns = new ArrayList();
var increasedDamage = 0;

cs = new NCombatScript() {
    /* @Override */
    setNpcHook: function(n) {
        npc = n;
    },

    /* @Override */
    restore: function() {
        npc.getWorld().removeNpcs(spiders);
        npc.getWorld().removeNpcs(spawns);
    },

    /* @Override */
    tick: function() {
        if (!npc.getController().isRegionLoaded()) {
            return;
        }
        if (!loaded) {
            this.loadProfile();
            return;
        }
        this.updateHitpoints();
        if (!initialAttackDelay) {
            for each (var player in npc.getController().getPlayers()) {
                if (player.getX() >= 3160 && player.getX() <= 3184 && player.getY() >= 4435 && player.getY() <= 4458) {
                    initialAttackDelay = true;
                    npc.setHitDelay(10);
                    break;
                }
            }
        }
        if (npc.isDead() && npc.getId() != PHASE_IDS[phase + 1]) {
            npc.setTransformationId(PHASE_IDS[phase + 1]);
        }
        if (npc.isLocked()) {
            return;
        }
        for (var it = spiders.iterator(); it.hasNext();) {
            var npc2 = it.next();
            if (!npc2.isVisible()) {
                it.remove();
            } else if (npc.withinDistance(npc2, 1)) {
                npc.applyHit(new Hit(npc2.getHitpoints(), HitMark.HEAL));
                npc2.getCombat().timedDeath();
                increasedDamage += 1;
                it.remove();
            }
        }
        for each (var player in npc.getController().getPlayers()) {
            var mapObject = npc.getController().getMapObject(32984, player);
            if (mapObject == null) {
                continue;
            }
            var damage = 10 + PRandom.randomI(5);
            player.addHit(new HitEvent(0, player, new Hit(damage)));
            npc.applyHit(new Hit(damage, HitMark.HEAL));
        }
        if (phase == 0 && npc.getHitpoints() <= npc.getMaxHitpoints() * 0.7
                || phase == 1 && npc.getHitpoints() <= npc.getMaxHitpoints() * 0.5
                || phase == 2 && npc.getHitpoints() <= npc.getMaxHitpoints() * 0.3) {
            phase++;
            npc.setTransformationId(PHASE_IDS[phase]);
            this.spawnNylocasMatomenos();
            if (phase == 3) {
                increasedDamage += 17;
            }
        }
    },

    /* @Override */
    maxHitHook: function(combatStyle) {
        return combatStyle.getMaxHit() + increasedDamage;
    },

    /* @Override */
    tileHitEventHook: function(combatStyle, tileHitEvent, speed) {
        for each (var event in bloodSpots) {
            if (tileHitEvent.getTile().matchesTile(event.getAttachment())) {
                return;
            }
        }
        var event = new Event(speed.eventDelay) {
            execute: function() {
                if (event.getExecutions() == 0) {
                    event.setTick(0);
                } else if (!npc.isVisible() || event.getExecutions() == 10) {
                    event.stop();
                    bloodSpots.remove(event);
                    if (PRandom.randomE(4) == 0) {
                        var npc2 = new Npc(npc.getController(), NpcId.BLOOD_SPAWN_55, event.getAttachment());
                        npc2.getController().setMultiCombatFlag(true);
                        npc2.setMoveDistance(32);
                        spawns.add(npc2);
                    }
                } else {
                    for each (var player in npc.getController().getPlayers()) {
                        if (!player.matchesTile(event.getAttachment())) {
                            continue;
                        }
                        var damage = 10 + PRandom.randomI(5);
                        player.addHit(new HitEvent(0, player, new Hit(damage)));
                        npc.applyHit(new Hit(damage, HitMark.HEAL));
                    }
                }
            }
        };
        event.setAttachment(tileHitEvent.getTile());
        npc.getWorld().addEvent(event);
        bloodSpots.add(event);
    },

    loadProfile: function() {
        var players = npc.getController().getPlayers();
        if (loaded || players.isEmpty()) {
            return;
        }
        loaded = true;
        var hitpoints = npc.getMaxHitpoints();
        if (players.size() == 4) {
            hitpoints = (hitpoints * 0.875)|0;
        } else if (players.size() == 3) {
            hitpoints = (hitpoints * 0.75)|0;
        } else if (players.size() == 2) {
            hitpoints = (hitpoints * 0.625)|0;
        } else if (players.size() == 1) {
            hitpoints = (hitpoints * 0.5)|0;
        }
        npc.setMaxHitpoints(hitpoints);
        npc.setHitpoints(npc.getMaxHitpoints());
        lastSavedHitpoints = npc.getHitpoints();
    },

    updateHitpoints: function() {
        if (!npc.isVisible()) {
            return;
        }
        for each (var player in npc.getController().getPlayers()) {
            if (!npc.withinDistance(player, 32)) {
                continue;
            }
            player.getGameEncoder().setVarp(1575, npc.getHitpoints() + (npc.getMaxHitpoints() * 2048));
            var hitpointsPercent = PRandom.getPercent(npc.getHitpoints(), npc.getMaxHitpoints())|0;
            player.getGameEncoder().sendWidgetText(596, 4, "Health: " + hitpointsPercent + "%");
        }
    },

    spawnNylocasMatomenos: function() {
        var spiderTiles = PCollection.toList(
            new Tile(3173, 4436),
            new Tile(3177, 4436),
            new Tile(3181, 4436),
            new Tile(3185, 4436),
            new Tile(3185, 4438),
            new Tile(3173, 4456),
            new Tile(3177, 4456),
            new Tile(3181, 4456),
            new Tile(3185, 4456),
            new Tile(3185, 4454)
        );
        var playerCount = npc.getController().getPlayers().size()
        for (var i = 0; i < playerCount * 2; i++) {
            var tile = spiderTiles.remove(PRandom.randomE(spiderTiles.size()));
            var npc2 = new Npc(npc.getController(), 8366, tile);
            npc2.getController().setMultiCombatFlag(true);
            npc2.getMovement().setFollowing(npc);
            spiders.add(npc2);
        }
    }
}
