var CORNERS = [
    new Tile(3299, 4451), new Tile(3299, 4440), new Tile(3288, 4440), new Tile(3288, 4451)
];

var npc = null;
var loaded = false;
var direction = 1;
var reverseDirection = false;
var directionChangeDelay = 40;
var sleepDelay = -1;
var cycle = 0;
var meatDelay = 1;

cs = new NCombatScript() {
    /* @Override */
    setNpcHook: function(n) {
        npc = n;
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
        if (npc.isLocked()) {
            return;
        }
        if (sleepDelay == -1) {
            for each (var player in npc.getController().getPlayers()) {
                if (this.inArea(player)) {
                    sleepDelay = 40;
                    break;
                }
            }
        }
        if (sleepDelay > 0 && !npc.getController().isMagicBound()) {
            sleepDelay--;
            if (sleepDelay == 0) {
                sleepDelay = 40;
                npc.setAnimation(8082);
                npc.getController().setMagicBind(32);
                cycle++;
                npc.getMovement().setRunning(cycle % 3 == 0);
            }
        }
        if (npc.getMovement().getMagicBindDelay() == Movement.MAGIC_REBIND_DELAY) {
            this.stomp();
        }
        if (directionChangeDelay > 0) {
            directionChangeDelay--;
            if (directionChangeDelay == 0) {
                directionChangeDelay = 40;
                reverseDirection = !reverseDirection;
            }
        }
        npc.getMovement().clear();
        if (npc.matchesTile(CORNERS[direction])) {
            if (reverseDirection) {
                direction--;
                if (direction < 0) {
                    direction = CORNERS.length - 1;
                }
            } else {
                direction++;
                if (direction >= CORNERS.length) {
                    direction = 0;
                }
            }
        }
        if (!npc.getController().isMagicBound()) {
            npc.getMovement().addMovement(CORNERS[direction]);
            this.checkLineOfSight();
            if (cycle > 0) {
                this.dropMeat();
            }
        }
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

    checkLineOfSight: function() {
        var minX = npc.getX();
        var minY = npc.getY();
        var maxX = CORNERS[direction].getX() + npc.getSizeX() - 1;
        var maxY = CORNERS[direction].getY() + npc.getSizeY() - 1;
        if (minX > maxX) {
            minX = CORNERS[direction].getX();
            maxX = npc.getX() + npc.getSizeX() - 1;
        }
        if (minY > maxY) {
            minY = CORNERS[direction].getY();
            maxY = npc.getY() + npc.getSizeY() - 1;
        }
        var players = npc.getController().getPlayers();
        for (var i = 0; i < players.size(); i++) {
            var player = players.get(i);
            if (!this.inArea(player) || !npc.withinDistance(player, 4) && (player.getX() < minX
                    || player.getX() > maxX || player.getY() < minY || player.getY() > maxY)) {
                continue;
            }
            var speed = cs.getSpeed(player);
            cs.sendMapProjectile(player, npc, player, 1568, 0, 0, speed.clientDelay, speed.clientSpeed, 0, 0);
            player.addHit(new HitEvent(speed.eventDelay, player, new Hit(npc, 10 + PRandom.randomI(10))));
            player.setInCombatDelay(Entity.COMBAT_DELAY);
            for (var i2 = 0; i2 < players.size(); i2++) {
                var player2 = players.get(i2);
                var lineOfSight = this.inNorth(player) && this.inNorth(player2) || this.inEast(player)
                        && this.inEast(player2) || this.inSouth(player) && this.inSouth(player2)
                        || this.inWest(player) && this.inWest(player2);
                if (!lineOfSight || player == player2) {
                    continue;
                }
                speed = cs.getSpeed(player2);
                cs.sendMapProjectile(player2, player, player2, 1568, 0, 0, speed.clientDelay, speed.clientSpeed,
                        0, 0);
                player2.addHit(new HitEvent(speed.eventDelay, player2, new Hit(npc, 10 + PRandom.randomI(10))));
                player2.setInCombatDelay(Entity.COMBAT_DELAY);
            }
        }
    },

    stomp: function() {
        for each (var player in npc.getController().getPlayers()) {
            if (player.isLocked() || !npc.withinDistance(player, 1)) {
                continue;
            }
            player.addHit(new HitEvent(0, player, new Hit(npc, player.getHitpoints() / 2)));
            player.setInCombatDelay(Entity.COMBAT_DELAY);
        }
    },

    dropMeat: function() {
        if (meatDelay > 0) {
            meatDelay--;
            if (meatDelay == 0) {
                meatDelay = 6;
                for (var i = 0; i < 14; i++) {
                    var tile = new Tile(3288, 4440);
                    tile.moveTile(PRandom.randomI(15), PRandom.randomI(15));
                    if (npc.getController().getMapClip(tile) != 0) {
                        continue;
                    }
                    npc.getController().sendMapGraphic(tile, new Graphic(1570 + PRandom.randomI(3)));
                    var the = new TileHitEvent(4, npc.getController(), tile, 50, HitType.TYPELESS);
                    the.setMinDamage(30);
                    the.setBind(4);
                    the.setGraphic(new Graphic(1575, 100));
                    npc.getWorld().addEvent(the);
                }
            }
        }
    },

    inArea: function(tile) {
        return tile.getX() >= 3288 && tile.getY() >= 4440 && tile.getX() <= 3303 && tile.getY() <= 4455;
    },

    inNorth: function(tile) {
        return tile.getX() >= 3288 && tile.getY() >= 4451 && tile.getX() <= 3303 && tile.getY() <= 4455;
    },

    inEast: function(tile) {
        return tile.getX() >= 3299 && tile.getY() >= 4440 && tile.getX() <= 3303 && tile.getY() <= 4455;
    },

    inSouth: function(tile) {
        return tile.getX() >= 3288 && tile.getY() >= 4440 && tile.getX() <= 3303 && tile.getY() <= 4444;
    },

    inWest: function(tile) {
        return tile.getX() >= 3288 && tile.getY() >= 4440 && tile.getX() <= 3292 && tile.getY() <= 4455;
    }
};
