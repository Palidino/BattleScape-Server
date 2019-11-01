function FollowTile(tile, tick) {
    this.tile = tile;
    this.tick = tick;
}

function FireAttack(polygon, tiles1, tiles2, followTile) {
    this.polygon = polygon;
    this.tiles1 = tiles1;
    this.tiles2 = tiles2;
    this.followTile = followTile;
}

var POISON_TILE_GRAPHICS = [
    new Graphic(1654), new Graphic(1655), new Graphic(1656), new Graphic(1657),
    new Graphic(1658), new Graphic(1659), new Graphic(1660), new Graphic(1661)
];

var POISON_ATTACK = new NCombatStyle();
POISON_ATTACK.setType(HitType.MAGIC);
POISON_ATTACK.setSubType(HitType.TYPELESS);
POISON_ATTACK.setHitMark(HitMark.POISON);
POISON_ATTACK.setAnimation(8234);
POISON_ATTACK.setMaxHit(12);
POISON_ATTACK.setFullDamage(true);
POISON_ATTACK.setAttackSpeed(6);
POISON_ATTACK.setTargetGraphic(new Graphic(1645));
POISON_ATTACK.setTargetTileGraphic(new Graphic(1654));
POISON_ATTACK.setProjectileId(1644);
POISON_ATTACK.setPoison(4);
POISON_ATTACK.setIgnorePrayer(true);
POISON_ATTACK.setSpeedMinDistance(8);
POISON_ATTACK.setTargetTile(true);
POISON_ATTACK.setTargetTileRadius(1);
POISON_ATTACK.setTargetTileDuration(16);
POISON_ATTACK.setTargetTileBreakOff(4);
POISON_ATTACK.setTargetTileBreakOffDistance(4);

var BLUE_VENT = new MapObject(34570, new Tile(1362, 10272, 0), 10, 0);
var GREEN_VENT = new MapObject(34569, new Tile(1371, 10272, 0), 10, 0);
var RED_VENT = new MapObject(34568, new Tile(1371, 10263, 0), 10, 0);
var VENTS = [ BLUE_VENT, GREEN_VENT, RED_VENT ];

var FIRE_TILES_NORTH = [
    new Tile(0, 1),
    new Tile(1365, 10271), new Tile(1366, 10271), new Tile(1367, 10271), new Tile(1368, 10271)
];
var FIRE_TILES_EAST = [
    new Tile(1, 0),
    new Tile(1370, 10269), new Tile(1370, 10268), new Tile(1370, 10267), new Tile(1370, 10266)
];
var FIRE_TILES_SOUTH = [
    new Tile(0, -1),
    new Tile(1365, 10264), new Tile(1366, 10264), new Tile(1367, 10264), new Tile(1368, 10264)
];
var FIRE_TILES_WEST = [
    new Tile(-1, 0),
    new Tile(1363, 10269), new Tile(1363, 10268), new Tile(1363, 10267), new Tile(1363, 10266)
];
var FIRE_TILES_NORTH_WEST = [
    new Tile(-1, 1),
    new Tile(1363, 10271), new Tile(1363, 10269), new Tile(1363, 10270), new Tile(1364, 10271), new Tile(1365, 10271)
];
var FIRE_TILES_SOUTH_WEST = [
    new Tile(-1, -1),
    new Tile(1363, 10264), new Tile(1365, 10264), new Tile(1364, 10264), new Tile(1363, 10265), new Tile(1363, 10266)
];
var FIRE_TILES_NORTH_EAST = [
    new Tile(1, 1),
    new Tile(1370, 10271), new Tile(1369, 10271), new Tile(1368, 10271), new Tile(1370, 10270), new Tile(1370, 10269)
];
var FIRE_TILES_SOUTH_EAST = [
    new Tile(-1, 1),
    new Tile(1370, 10264), new Tile(1370, 10265), new Tile(1370, 10266), new Tile(1369, 10264), new Tile(1368, 10264)
];

var NORTH_FIRE = new FireAttack(new PPolygon(),
        FIRE_TILES_NORTH_WEST, FIRE_TILES_NORTH_EAST, new Tile(1366, 10271));
var EAST_FIRE = new FireAttack(new PPolygon([ 1366, 1377, 1377 ], [ 10267, 10278, 10257 ]),
        FIRE_TILES_NORTH_EAST, FIRE_TILES_SOUTH_EAST, new Tile(1370, 10267));
var SOUTH_FIRE = new FireAttack(new PPolygon([ 1366, 1377, 1356 ], [ 10268, 10257, 10257 ]),
        FIRE_TILES_SOUTH_WEST, FIRE_TILES_SOUTH_EAST, new Tile(1366, 10264));
var WEST_FIRE = new FireAttack(new PPolygon([ 1367, 1356, 1356 ], [ 10267, 10257, 10278 ]),
        FIRE_TILES_NORTH_WEST, FIRE_TILES_SOUTH_WEST, new Tile(1363, 10267));
var NORTH_EAST_FIRE = new FireAttack(new PPolygon([ 1369, 1369, 1377, 1377 ], [ 10270, 10278, 10278, 10270 ]),
        FIRE_TILES_NORTH, FIRE_TILES_EAST, new Tile(1370, 10271));
var SOUTH_EAST_FIRE = new FireAttack(new PPolygon([ 1369, 1377, 1377, 1369 ], [ 10265, 10265, 10257, 10257 ]),
        FIRE_TILES_EAST, FIRE_TILES_SOUTH, new Tile(1370, 10271));
var SOUTH_WEST_FIRE = new FireAttack(new PPolygon([ 1364, 1364, 1356, 1356 ], [ 10265, 10257, 10257, 10265 ]),
        FIRE_TILES_WEST, FIRE_TILES_SOUTH, new Tile(1363, 10264));
var NORTH_WEST_FIRE = new FireAttack(new PPolygon([ 1364, 1356, 1356, 1364 ], [ 10270, 10270, 10278, 10278 ]),
        FIRE_TILES_NORTH, FIRE_TILES_WEST, new Tile(1363, 10271));
var FIRE_ATTACKS = [
    NORTH_EAST_FIRE, SOUTH_EAST_FIRE, SOUTH_WEST_FIRE, NORTH_WEST_FIRE, EAST_FIRE, SOUTH_FIRE, WEST_FIRE
];

var npc = null;
var hitStyle = PRandom.randomE(2) == 0 ? HitType.RANGED : HitType.MAGIC;
var currentCombatStyle;
var hitCount = 0;
var specialDelay = 3;
var damageReduction = true;
var lightningCastEvent = null;
var lightningEvents = new ArrayList();
var ventDelay = 10;
var fireBleedEvent = null;

cs = new NCombatScript() {
    setNpcHook: function(n) {
        npc = n;
    },

    restore: function() {
        npc.attackUnlock();
        hitStyle = PRandom.randomE(2) == 0 ? HitType.RANGED : HitType.MAGIC;
        hitCount = 0;
        specialDelay = 3;
        damageReduction = true;
        ventDelay = 8;
        this.cancelLightningAttack();
        POISON_ATTACK.setAnimation(8234);
        this.setFireBleed(null);
    },

    tick: function() {
        if (npc.isLocked()) {
            return;
        }
        if (npc.getId() == NpcId.ALCHEMICAL_HYDRA_426
                && PRandom.getPercent(npc.getHitpoints(), npc.getMaxHitpoints()) <= 75) {
            npc.setLock(2);
            npc.setTransformationId(NpcId.ALCHEMICAL_HYDRA_426_8616);
            npc.setAnimation(8237);
            return;
        } else if (npc.getId() == NpcId.ALCHEMICAL_HYDRA_426_8616) {
            npc.setLock(2);
            npc.setTransformationId(NpcId.ALCHEMICAL_HYDRA_426_8619);
            npc.setAnimation(8238);
            specialDelay = 3;
            damageReduction = true;
            return;
        } else if (npc.getId() == NpcId.ALCHEMICAL_HYDRA_426_8619
                && PRandom.getPercent(npc.getHitpoints(), npc.getMaxHitpoints()) <= 50) {
            npc.setLock(2);
            npc.setTransformationId(NpcId.ALCHEMICAL_HYDRA_426_8617);
            npc.setAnimation(8244);
            this.cancelLightningAttack();
            return;
        } else if (npc.getId() == NpcId.ALCHEMICAL_HYDRA_426_8617) {
            npc.setLock(2);
            npc.setTransformationId(NpcId.ALCHEMICAL_HYDRA_426_8620);
            npc.setAnimation(8245);
            specialDelay = 3;
            damageReduction = true;
            return;
        } else if (npc.getId() == NpcId.ALCHEMICAL_HYDRA_426_8620
                && PRandom.getPercent(npc.getHitpoints(), npc.getMaxHitpoints()) <= 25) {
            npc.setLock(2);
            npc.setTransformationId(NpcId.ALCHEMICAL_HYDRA_426_8618);
            npc.setAnimation(8251);
            this.cancelLightningAttack();
            return;
        } else if (npc.getId() == NpcId.ALCHEMICAL_HYDRA_426_8618) {
            npc.setLock(2);
            npc.setTransformationId(NpcId.ALCHEMICAL_HYDRA_426_8621);
            npc.setAnimation(8252);
            specialDelay = 3;
            hitStyle = hitStyle == HitType.RANGED ? HitType.MAGIC : HitType.RANGED;
            damageReduction = false;
            POISON_ATTACK.setAnimation(8255);
            return;
        }
        if (ventDelay > 0) {
            ventDelay--;
            if (ventDelay == 0) {
                ventDelay = 8;
                this.activateVents();
            }
        }
        if (npc.getId() == NpcId.ALCHEMICAL_HYDRA_426_8619 && npc.getHitDelay() == 0 && specialDelay == 0) {
            this.lightningAttackStart();
        } else if (npc.getId() == NpcId.ALCHEMICAL_HYDRA_426_8620 && npc.getHitDelay() == 0 && specialDelay == 0) {
            this.fireAttackStart();
        }
    },

    hitTypeHook: function(hitType) {
        return hitStyle;
    },

    combatStyleHook: function(combatStyle) {
        if (specialDelay == 0) {
            if (npc.getId() == NpcId.ALCHEMICAL_HYDRA_426 || npc.getId() == NpcId.ALCHEMICAL_HYDRA_426_8621) {
                currentCombatStyle = POISON_ATTACK;
                return POISON_ATTACK;
            }
        }
        currentCombatStyle = combatStyle;
        return combatStyle;
    },

    damageReceivedHook: function(damage, entity, hitType, defenceType) {
        if (damageReduction) {
            damage /= 2;
            if (entity instanceof Player) {
                entity.getGameEncoder().sendMessage("The Alchemical Hydra's defences partially absorb your attack!");
            }
        }
        return damage;
    },

    targetTileGraphicHook: function(combatStyle) {
        if (combatStyle.getTargetTileGraphic() != null && combatStyle.getTargetTileGraphic().getId() == 1654) {
            return POISON_TILE_GRAPHICS[PRandom.randomE(POISON_TILE_GRAPHICS.length)];
        }
        return combatStyle.getTargetTileGraphic();
    },

    attackEndHook: function() {
        if (currentCombatStyle != POISON_ATTACK
                && (npc.getId() == NpcId.ALCHEMICAL_HYDRA_426_8621 || ++hitCount == 3)) {
            hitStyle = hitStyle == HitType.RANGED ? HitType.MAGIC : HitType.RANGED;
            hitCount = 0;
        }
        if (--specialDelay < 0) {
            specialDelay = 9;
        }
    },

    canBeAttackedHook: function(player, sendMessage, hitType) {
        if (!Settings.getInstance().isSpawn() && !player.getSkills().isAnySlayerTask(npc) && !Main.ownerPrivledges(player)
                && !player.isUsergroup(SqlUserRank.YOUTUBER)) {
            if (sendMessage) {
                player.getGameEncoder().sendMessage("This can only be attacked on an appropriate Slayer task.");
            }
            return false;
        }
        if (npc.isAttacking() && (npc.getAttackingEntity() != null && npc.getAttackingEntity() != player
                || npc.getLastHitByEntity() != null && player != npc.getLastHitByEntity())) {
            if (sendMessage) {
                player.getGameEncoder().sendMessage("The Alchemical Hydra is busy attacking someone else.");
            }
            return false;
        }
        return true;
    },

    dropChanceHook: function(player, dropChance, roll) {
        if (roll != 0 && dropChance < 5) {
            return 0;
        }
        return dropChance;
    },

    lightningAttackStart: function() {
        npc.setHitDelay(6);
        specialDelay = 9;
        npc.setAnimation(8241);
        var tiles = PCollection.toList(
            new Tile(1362, 10272),
            new Tile(1371, 10272),
            new Tile(1371, 10263),
            new Tile(1362, 10263)
        );
        var initialTile = PRandom.listRandom(tiles).randomize(2);
        var initialSpeed = cs.getSpeed(10);
        npc.getCombat().sendMapProjectile(null, npc, initialTile, 1664, 43, 31, initialSpeed.clientDelay,
                initialSpeed.clientSpeed, 16, 64);
        var tile = null;
        var js = this;
        lightningCastEvent = new PEvent(initialSpeed.eventDelay - 1) {
            execute: function() {
                if (tile != null) {
                    js.lightningAttack(tile);
                } else {
                    npc.getController().sendMapGraphic(initialTile, new Graphic(1664));
                }
                if (tiles.isEmpty()) {
                    lightningCastEvent.stop();
                    return;
                }
                var lastTile = tile != null ? tile : initialTile;
                tile = tiles.remove(PRandom.randomE(tiles.size()));
                var speed = cs.getSpeed(2);
                npc.getCombat().sendMapProjectile(null, lastTile, tile, 1665, 43, 31, speed.clientDelay,
                        speed.clientSpeed, 16, 64);
                lightningCastEvent.setTick(speed.eventDelay - 1);
            }
        }
        npc.getWorld().addEvent(lightningCastEvent);
    },

    lightningAttack: function(tile) {
        var player = npc.getAttackingEntity();
        if (player == null) {
            return;
        }
        var damageEvent = null;
        var event = new PEvent() {
            execute: function() {
                if (player.isLocked()) {
                    event.stop();
                    return;
                }
                if (tile.matchesTile(player)) {
                    player.getGameEncoder().sendMessage("<col=ff0000>The eletricity temporarily paralyzes you!");
                    player.applyHit(new Hit(PRandom.randomI(20)));
                    player.getController().setMagicBind(8);
                    event.stop();
                }
                if ((event.getExecutions() % 2) != 0) {
                    return;
                }
                npc.getController().sendMapGraphic(tile, new Graphic(1666));
                if (tile.getX() < player.getX()) {
                    tile.moveX(1);
                } else if (tile.getX() > player.getX()) {
                    tile.moveX(-1);
                }
                if (tile.getY() < player.getY()) {
                    tile.moveY(1);
                } else if (tile.getY() > player.getY()) {
                    tile.moveY(-1);
                }
                if (event.getExecutions() == 16) {
                    event.stop();
                }
            }
        }
        npc.getWorld().addEvent(event);
        lightningEvents.add(event);
    },

    cancelLightningAttack: function() {
        if (lightningCastEvent != null) {
            lightningCastEvent.stop();
            lightningCastEvent = null;
        }
        if (!lightningEvents.isEmpty()) {
            for each (var event in lightningEvents) {
                event.stop();
            }
            lightningEvents.clear();
        }
    },

    fireAttackStart: function() {
        var player = npc.getAttackingEntity();
        if (player == null) {
            return;
        }
        npc.attackLock();
        var js = this;
        var event = new PEvent() {
            execute: function() {
                if (player.isLocked()) {
                    event.stop();
                    npc.attackUnlock();
                    return;
                }
                npc.getMovement().quickRoute(1364, 10265);
                if (npc.getX() == 1364 && npc.getY() == 10265) {
                    js.fireAttack(player);
                    event.stop();
                }
            }
        }
        npc.getWorld().addEvent(event);
    },

    fireAttack: function(player) {
        var js = this;
        player.getController().setMagicBind(2);
        var fire = NORTH_FIRE;
        for each (var aFire in FIRE_ATTACKS) {
            if (!aFire.polygon.contains(player.getX(), player.getY())) {
                continue;
            }
            fire = aFire;
            break;
        }
        var firstTiles = Tile.newTiles(fire.tiles1);
        var secondTiles = Tile.newTiles(fire.tiles2);
        var followTile = new Tile(fire.followTile);
        var followTiles = new ArrayList();
        followTiles.add(new FollowTile(new Tile(followTile), 0));
        var speed = cs.getSpeed(2);
        var hasCastFollow = false;
        var eventFollow = new PEvent(1) {
            execute: function() {
                if (player.isLocked() || followTiles.isEmpty() || npc.isLocked()
                        || npc.getId() != NpcId.ALCHEMICAL_HYDRA_426_8620) {
                    eventFollow.stop();
                    npc.attackUnlock();
                    return;
                }
                if (!hasCastFollow) {
                    hasCastFollow = true;
                    npc.setAnimation(8248);
                    npc.setFaceTile(followTile);
                    npc.getCombat().sendMapProjectile(null, npc, followTile, 1667, 43, 0, speed.clientDelay,
                            speed.clientSpeed, 16, 64);
                    npc.getController().sendMapGraphic(followTile, new Graphic(1668, 0, speed));
                    npc.attackUnlock();
                    npc.setHitDelay(15);
                    specialDelay = 9;
                    return;
                }
                for (var it = followTiles.iterator(); it.hasNext(); ) {
                    var aFollowTile = it.next();
                    if (eventFollow.getExecutions() - aFollowTile.tick >= 42) {
                        it.remove();
                        continue;
                    }
                    if (!aFollowTile.tile.matchesTile(player)) {
                        continue;
                    }
                    player.applyHit(new Hit(PRandom.randomI(20)));
                    js.setFireBleed(player);
                }
                if (eventFollow.getExecutions() > 15) {
                    return;
                }
                if (followTile.matchesTile(player)) {
                    return;
                }
                if (followTile.getX() < player.getX()) {
                    followTile.moveX(1);
                } else if (followTile.getX() > player.getX()) {
                    followTile.moveX(-1);
                }
                if (followTile.getY() < player.getY()) {
                    followTile.moveY(1);
                } else if (followTile.getY() > player.getY()) {
                    followTile.moveY(-1);
                }
                var hasTile = false;
                for each (var aFollowTile in followTiles) {
                    if (followTile.matchesTile(aFollowTile.tile)) {
                        hasTile = true;
                        break;
                    }
                }
                if (hasTile) {
                    return;
                }
                followTiles.add(new FollowTile(new Tile(followTile), eventFollow.getExecutions()));
                npc.getController().sendMapGraphic(followTile, new Graphic(1668));
            }
        }
        var spawnedFirstTiles = new ArrayList();
        var spawnedSecondTiles = new ArrayList();
        npc.setAnimation(8248);
        npc.setFaceTile(firstTiles[1]);
        var isFirst = true;
        for each (var tile in firstTiles) {
            if (isFirst) {
                isFirst = false;
                continue;
            }
            npc.getCombat().sendMapProjectile(null, npc, tile, 1667, 43, 0, speed.clientDelay,
                    speed.clientSpeed, 16, 64);
        }
        for (var i = 0; i < 16; i++) {
            var noTilesFound = true;
            isFirst = true;
            for each (var tile in firstTiles) {
                if (isFirst) {
                    isFirst = false;
                    continue;
                }
                if (npc.getController().getMapClip(tile) == 0) {
                    npc.getController().sendMapGraphic(tile, new Graphic(1668, 0, speed.getContactDelay() + i * 5));
                    spawnedFirstTiles.add(new Tile(tile));
                    noTilesFound = false;
                }
                tile.moveTile(firstTiles[0].getX(), firstTiles[0].getY());
            }
            if (noTilesFound) {
                break;
            }
        }
        var eventWall1 = new PEvent() {
            execute: function() {
                if (player.isLocked()) {
                    eventWall1.stop();
                    npc.attackUnlock();
                    return;
                }
                for each (var tile in spawnedSecondTiles) {
                    if (!tile.matchesTile(player)) {
                        continue;
                    }
                    player.applyHit(new Hit(PRandom.randomI(20)));
                    js.setFireBleed(player);
                }
                if (eventWall1.getExecutions() >= 42 + speed.eventDelay) {
                    eventWall1.stop();
                }
            }
        }
        npc.getWorld().addEvent(eventWall1);
        var hasSecondAttacked = false;
        var wallEvent2 = new PEvent(1) {
            execute: function() {
                wallEvent2.setTick(0);
                if (player.isLocked()) {
                    wallEvent2.stop();
                    npc.attackUnlock();
                    return;
                }
                if (!hasSecondAttacked) {
                    hasSecondAttacked = true;
                    npc.setAnimation(8248);
                    npc.setFaceTile(secondTiles[1]);
                    var isFirst = true;
                    for each (var tile in secondTiles) {
                        if (isFirst) {
                            isFirst = false;
                            continue;
                        }
                        npc.getCombat().sendMapProjectile(null, npc, tile, 1667, 43, 0, speed.clientDelay,
                                speed.clientSpeed, 16, 64);
                    }
                    for (var i = 0; i < 16; i++) {
                        var noTilesFound = true;
                        isFirst = true;
                        for each (var tile in secondTiles) {
                            if (isFirst) {
                                isFirst = false;
                                continue;
                            }
                            if (npc.getController().getMapClip(tile) == 0) {
                                npc.getController().sendMapGraphic(tile, new Graphic(1668, 0,
                                        speed.getContactDelay() + i * 5));
                                spawnedSecondTiles.add(new Tile(tile));
                                noTilesFound = false;
                            }
                            tile.moveTile(secondTiles[0].getX(), secondTiles[0].getY());
                        }
                        if (noTilesFound) {
                            break;
                        }
                    }
                    npc.getWorld().addEvent(eventFollow);
                    return;
                }
                for each (var tile in spawnedSecondTiles) {
                    if (!tile.matchesTile(player)) {
                        continue;
                    }
                    player.applyHit(new Hit(PRandom.randomI(20)));
                    js.setFireBleed(player);
                }
                if (wallEvent2.getExecutions() >= 43 + speed.eventDelay) {
                    wallEvent2.stop();
                }
            }
        }
        npc.getWorld().addEvent(wallEvent2);
    },

    setFireBleed: function(player) {
        if (fireBleedEvent != null) {
            fireBleedEvent.stop();
        }
        if (player == null) {
            return;
        }
        fireBleedEvent = new PEvent() {
            execute: function() {
                if (fireBleedEvent.getExecutions() == 4) {
                    fireBleedEvent.stop();
                }
                player.applyHit(new Hit(5));
            }
        };
        npc.getWorld().addEvent(fireBleedEvent);
    },

    activateVents: function() {
        var player = npc.getAttackingEntity();
        if (player == null) {
            return;
        }
        player.getGameEncoder().sendMapObjectAnimation(BLUE_VENT, 8279);
        player.getGameEncoder().sendMapObjectAnimation(GREEN_VENT, 8279);
        player.getGameEncoder().sendMapObjectAnimation(RED_VENT, 8279);
        var weakness = RED_VENT;
        if (npc.getId() == NpcId.ALCHEMICAL_HYDRA_426_8619) {
            weakness = GREEN_VENT;
        } else if (npc.getId() == NpcId.ALCHEMICAL_HYDRA_426_8620) {
            weakness = BLUE_VENT;
        }
        var event = new PEvent() {
            execute: function() {
                if (player.isLocked()) {
                    event.stop();
                    return;
                }
                if (event.getExecutions() == 4) {
                    event.stop();
                    player.getGameEncoder().sendMapObjectAnimation(BLUE_VENT, 8280);
                    player.getGameEncoder().sendMapObjectAnimation(GREEN_VENT, 8280);
                    player.getGameEncoder().sendMapObjectAnimation(RED_VENT, 8280);
                    return;
                }
                for each (var vent in VENTS) {
                    if (!player.withinDistance(vent, 1)) {
                        continue;
                    }
                    player.applyHit(new Hit(PRandom.randomI(20)));
                    break;
                }
                if (npc.getId() == NpcId.ALCHEMICAL_HYDRA_426_8621) {
                    return;
                }
                for each (var vent in VENTS) {
                    if (!npc.withinDistance(vent, 1)) {
                        continue;
                    }
                    if (vent == weakness) {
                        if (damageReduction) {
                            damageReduction = false;
                            player.getGameEncoder().sendMessage("The chemicals neutralise the Alchemical Hydra's defences!");
                        }
                    } else if (!damageReduction) {
                        damageReduction = true;
                        player.getGameEncoder().sendMessage("The chemicals are absorbed by the Alchemical Hydra, empowering it further!");
                    }
                    break;
                }
            }
        }
        npc.getWorld().addEvent(event);
    }
};
