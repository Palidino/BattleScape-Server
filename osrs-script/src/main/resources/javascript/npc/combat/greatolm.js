var HEAD = 0, LEFT_HAND = 1, RIGHT_HAND = 2;
var EAST_OBJECT_TILES = [
    new Tile(3238, 5738),
    new Tile(3238, 5733),
    new Tile(3238, 5743)
];
var EAST_NPC_TILES = [
    new Tile(3239, 5738),
    new Tile(3238, 5733),
    new Tile(3238, 5743)
];
var WEST_OBJECT_TILES = [
    new Tile(3220, 5738),
    new Tile(3220, 5743),
    new Tile(3220, 5733)
];
var WEST_NPC_TILES = [
    new Tile(3222, 5738),
    new Tile(3223, 5743),
    new Tile(3223, 5733)
];
var OBJECT_IDS = [ 29881, 29884, 29887 ];
var NPC_IDS = [ 7554, 7555, 7553 ];
var SPAWN_ANIMATIONS = [ 7335, 7354, 7350 ];
var DESPAWN_ANIMATIONS = [ 7348, 7370, 7352 ];
var DESPAWN_TIMES = [ 2, 1, 1 ];
var DEFAULT_ANIMATIONS = [ 7336, 7355, 7351 ];
var OLM_PHASE_3_ANIMATIONS = PCollection.toMap(
    7335, 7383,
    7336, 7374,
    7345, 7371
);

var npc = null;
var loaded = false;
var visible = false;
var olm = null;
var objects = null;
var handHitpoints = 0;
var phase = 1;
var normalUniqueAttackDelay = 0;
var rotatingSpecials = 0;
var attackRotation = 0;

cs = new NCombatScript() {
    /* @Override */
    setNpcHook: function(n) {
        npc = n;
    },

    /* @Override */
    script: function(name, args) {
        if (name.equals("olm")) {
            olm = args;
        } else if (name.equals("objects")) {
            objects = args;
        } else if (name.equals("phase")) {
            phase = args[0];
        }
        return null;
    },

    /* @Override */
    restore: function() {
        if (npc.getId() != NPC_IDS[HEAD]) {
            return;
        }
        this.despawnNpc(HEAD, 0, true);
        this.despawnNpc(LEFT_HAND, 0, true);
        this.despawnNpc(RIGHT_HAND, 0, true);
        if (olm != null) {
            npc.getWorld().removeNpc(olm[LEFT_HAND]);
            npc.getWorld().removeNpc(olm[RIGHT_HAND]);
        }
    },

    /* @Override */
    tick: function() {
        if (npc.getId() != NPC_IDS[HEAD]) {
            return;
        }
        if (!loaded) {
            this.loadProfile();
        }
        if (!npc.getController().isRegionLoaded()) {
            return;
        }
        if (!visible) {
            for each (var player in npc.getController().getPlayers()) {
                if (npc.withinDistance(player, 6)) {
                    visible = true;
                    break;
                }
            }
            if (visible) {
                this.rotate(false);
            }
            return;
        }
        if (npc.isLocked()) {
            return;
        }
        if (normalUniqueAttackDelay > 0) {
            normalUniqueAttackDelay--;
        }
        if (npc.getHitDelay() == 6 || npc.getHitDelay() == 2) {
            this.setAnimation(HEAD, DEFAULT_ANIMATIONS[HEAD], true);
        }
        if (npc.isAttacking() && npc.getHitDelay() == 0) {
            if ((attackRotation == 0 || attackRotation == 2) && normalUniqueAttackDelay == 0
                    && PRandom.randomE(4) == 0) {
                var attack = PRandom.randomI(2);
                if (attack == 0) {
                    this.spheres();
                } else if (attack == 1) {
                    this.acidSpray();
                } else if (attack == 2) {
                    this.acidDrip();
                }
                normalUniqueAttackDelay = 32;
            } else if (attackRotation == 1) {
                attackRotation++;
                if (!olm[LEFT_HAND].isLocked()) {
                    if (rotatingSpecials == 0) {
                        this.crystalBurst();
                    } else if (rotatingSpecials == 1) {
                        this.lightning();
                    } else if (rotatingSpecials == 2) {
                        this.swamp();
                    }
                    rotatingSpecials = (rotatingSpecials + 1) % 3;
                }
            }
        }
        if (phase < 3 && olm != null && !olm[LEFT_HAND].isVisible() && !olm[RIGHT_HAND].isVisible()) {
            this.rotateStart();
        }
    },

    /* @Override */
    animationHook: function(combatStyle) {
        this.setAnimation(HEAD, combatStyle.getAnimation(), true);
        return -1;
    },

    /* @Override */
    attackSpeedHook: function(combatStyle) {
        if (attackRotation++ == 2) {
            attackRotation = 0;
            return combatStyle.getAttackSpeed() * 2;
        }
        return combatStyle.getAttackSpeed();
    },

    /* @Override */
    damageReceivedHook: function(damage, entity, hitType, defenceType) {
        if (npc.getId() == NPC_IDS[RIGHT_HAND] && hitType != HitType.MAGIC) {
            damage = 0;
        }
        if (npc.getId() == NPC_IDS[LEFT_HAND] && phase < 3 && !olm[LEFT_HAND].isLocked() && !olm[RIGHT_HAND].isLocked()
                && !olm[RIGHT_HAND].isDead() && (npc.getHitpoints() * 0.8) <= olm[RIGHT_HAND].getHitpoints()
                && damage > 0 && PRandom.randomE(16) == 0) {
            npc.setLock(44);
            for each (var player in npc.getController().getPlayers()) {
                player.getGameEncoder().sendMessage("The Great Olm's left claw clenches to protect itself temporarily.");
            }
            this.setAnimation(LEFT_HAND, 7360, false);
            var js = this;
            var event = new PEvent(Event.MILLIS_600) {
                execute: function() {
                    if (event.getExecutions() == Event.SEC_27 || !npc.isVisible()) {
                        event.stop();
                    }
                    if (event.getExecutions() == 1) {
                        js.setAnimation(LEFT_HAND, 7361, false);
                    } else if (event.getExecutions() == 40) {
                        js.setAnimation(LEFT_HAND, 7362, false);
                    } else if (event.getExecutions() == 42) {
                        js.setAnimation(LEFT_HAND, DEFAULT_ANIMATIONS[LEFT_HAND], false);
                    }
                }
            }
            npc.getWorld().addEvent(event);
        }
        return damage;
    },

    /* @Override */
    canAttackEntityHook: function(entity, combatStyle) {
        return visible;
    },

    /* @Override */
    canBeAttackedHook: function(player, sendMessage, hitType) {
        return npc.getId() != NPC_IDS[HEAD] || visible && phase == 3 && olm[LEFT_HAND].isLocked()
                && olm[RIGHT_HAND].isLocked();
    },

    /* @Override */
    applyDeadStartHook: function(deathDelay) {
        var index = this.getIndex();
        this.despawnNpc(index, DESPAWN_TIMES[index], false);
        if (index == HEAD) {
            for each (var player in npc.getController().getPlayers()) {
                player.getGameEncoder().sendMessage("As the Great Olm collapses, the crystal blocking your exit has been shattered.");
            }
            npc.getController().addMapObject(new MapObject(-1, new Tile(3232, 5749), 10, 0));
            npc.getController().addMapObject(new MapObject(30028, new Tile(3233, 5751), 10, 0));
        } else if (phase == 3 && olm[LEFT_HAND].isDead() && olm[RIGHT_HAND].isDead()) {
            this.fallingCrystals();
        }
    },

    /* @Override */
    dropItemHook: function(player, dropTile, dropForIndex, hasRoWICharge) {
        var index = this.getIndex();
        if (index == HEAD && dropForIndex == 0) {
            player.getController().getVariable("decide_rewards");
        }
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
            playerMultiplier += 0.5;
        }
        averageHP /= players.size();
        var hitpoints = ((300 + (players.size() * 50) + (averageHP * 2)) * playerMultiplier)|0;
        npc.setMaxHitpoints(hitpoints);
        npc.setHitpoints(npc.getMaxHitpoints());
        handHitpoints = ((150 + (players.size() * 50) + (averageHP * 2)) * playerMultiplier)|0;
    },

    getIndex: function() {
        if (npc.getId() == NPC_IDS[LEFT_HAND]) {
            return LEFT_HAND;
        } else if (npc.getId() == NPC_IDS[RIGHT_HAND]) {
            return RIGHT_HAND;
        }
        return HEAD;
    },

    despawnNpc: function(index, time, force) {
        if (time != 0) {
            this.setAnimation(index, DESPAWN_ANIMATIONS[index], false);
        }
        var event = new PEvent(time) {
            execute: function() {
                event.stop();
                var matchesEast = npc.matchesTile(EAST_NPC_TILES[index]);
                var matchesWest = npc.matchesTile(WEST_NPC_TILES[index]);
                if (matchesEast || !matchesEast && !matchesWest || force) {
                    npc.getController().addMapObject(new MapObject(OBJECT_IDS[index] - 1, EAST_OBJECT_TILES[index],
                            10, 1));
                }
                if (matchesWest || !matchesEast && !matchesWest || force) {
                    npc.getController().addMapObject(new MapObject(OBJECT_IDS[index] - 1, WEST_OBJECT_TILES[index],
                            10, 3));
                }
            }
        };
        if (time == 0) {
            event.execute();
        } else {
            npc.getWorld().addEvent(event);
        }
    },

    setAnimation: function(index, animation, all) {
        var animations = [ DEFAULT_ANIMATIONS[HEAD], DEFAULT_ANIMATIONS[LEFT_HAND], DEFAULT_ANIMATIONS[RIGHT_HAND] ];
        if (phase == 3 && OLM_PHASE_3_ANIMATIONS.containsKey(animation)) {
            animations[index] = OLM_PHASE_3_ANIMATIONS.get(animation);
        } else {
            animations[index] = animation;
        }
        for each (var player in npc.getController().getPlayers()) {
            if (all) {
                for (var i = 0; i < objects.length; i++) {
                    if (olm == null || olm[i].isLocked()) {
                        continue;
                    }
                    player.getGameEncoder().sendMapObjectAnimation(objects[i], animations[i]);
                }
            } else {
                player.getGameEncoder().sendMapObjectAnimation(objects[index], animations[index]);
            }
        }
    },

    getAttackablePlayers: function() {
        var players = npc.getController().getPlayers();
        for (var it = players.iterator(); it.hasNext();) {
            var player = it.next();
            if (player.isLocked() || !npc.withinDistance(player, 32) || player.getY() < 5730) {
                it.remove();
            }
        }
        return players;
    },

    rotateStart: function() {
        var west = npc.matchesTile(EAST_NPC_TILES[HEAD]);
        if (phase++ == 0) {
            west = PRandom.randomI(1) == 0;
        }
        olm[LEFT_HAND].getCombat().getCombatScript().script("phase", phase);
        olm[RIGHT_HAND].getCombat().getCombatScript().script("phase", phase);
        this.despawnNpc(HEAD, DESPAWN_TIMES[HEAD], false);
        npc.setVisible(false);
        npc.lock();
        var js = this;
        var event = new PEvent(Event.MILLIS_1200) {
            execute: function() {
                if (event.getExecutions() == 0) {
                    return;
                } else if (event.getExecutions() == 17) {
                    event.stop();
                    npc.setVisible(true);
                    npc.unlock();
                    js.rotate(west);
                }
                var count = 1 + PRandom.randomI(3);
                for (var i = 0; i < count; i++) {
                    var tile = new Tile(3228, 5730);
                    tile.moveTile(PRandom.randomI(9), PRandom.randomI(18));
                    cs.sendMapProjectile(null, (new Tile(tile)).moveY(1), tile, 1357, 255, 10, 0, 51 + 120, 0, 0);
                    npc.getController().sendMapGraphic(tile, 1358, 0, 51 + 100);
                    npc.getController().sendMapGraphic(tile, 1449, 0, 0);
                    var the = new TileHitEvent(Event.MILLIS_3600, npc.getController(), tile, 20, HitType.TYPELESS);
                    the.setAdjacentHalfDamage(true);
                    npc.getWorld().addEvent(the);
                }
            }
        };
        npc.getWorld().addEvent(event);
    },

    rotate: function(west) {
        var objectTiles = west ? WEST_OBJECT_TILES : EAST_OBJECT_TILES;
        var npcTiles = west ? WEST_NPC_TILES : EAST_NPC_TILES;
        if (olm == null) {
            olm = [ npc, new Npc(npc.getController(), NPC_IDS[LEFT_HAND], npcTiles[LEFT_HAND]),
                    new Npc(npc.getController(), NPC_IDS[RIGHT_HAND], npcTiles[RIGHT_HAND]) ];
        } else {
            npc.getWorld().addNpc(olm[LEFT_HAND]);
            olm[LEFT_HAND].getMovement().teleport(npcTiles[LEFT_HAND]);
            npc.getWorld().addNpc(olm[RIGHT_HAND]);
            olm[RIGHT_HAND].getMovement().teleport(npcTiles[RIGHT_HAND]);
        }
        npc.setLock(6);
        npc.getMovement().teleport(npcTiles[HEAD]);
        var hands = [ olm[LEFT_HAND], olm[RIGHT_HAND] ];
        for each (var hand in hands) {
            hand.setLock(6);
            hand.getController().setMultiCombatFlag(true);
            hand.setMaxHitpoints(handHitpoints);
            hand.setHitpoints(hand.getMaxHitpoints());
            hand.getCombat().getCombatScript().script("olm", olm);
        }
        var direction = west ? 3 : 1;
        objects = [
            new MapObject(OBJECT_IDS[HEAD], objectTiles[HEAD], 10, direction),
            new MapObject(OBJECT_IDS[LEFT_HAND], objectTiles[LEFT_HAND], 10, direction),
            new MapObject(OBJECT_IDS[RIGHT_HAND], objectTiles[RIGHT_HAND], 10, direction)
        ];
        olm[LEFT_HAND].getCombat().getCombatScript().script("objects", objects);
        olm[RIGHT_HAND].getCombat().getCombatScript().script("objects", objects);
        for each (var player in npc.getController().getPlayers()) {
            for (var i = 0; i < objects.length; i++) {
                player.getGameEncoder().sendMapObjectAnimation(new MapObject(objects[i].getId() - 1,
                        objects[i]), SPAWN_ANIMATIONS[i]);
            }
        }
        var event = new PEvent(Event.MILLIS_3000) {
            execute: function() {
                event.stop();
                for each (var mapObject in objects) {
                    npc.getController().addMapObject(mapObject);
                }
            }
        };
        npc.getWorld().addEvent(event);
        npc.setHitpoints(npc.getMaxHitpoints());
    },

    crystalBurst: function() {
        npc.setHitDelay(4);
        this.setAnimation(LEFT_HAND, 7356, true);
        var crystals = new ArrayList();
        for each (var player in this.getAttackablePlayers()) {
            var crystal = new MapObject(30033, player, 10, MapObject.getRandomDirection());
            var hasTileMatch = false;
            for each (var crystal in crystals) {
                if (!crystal.matchesTile(crystal)) {
                    continue;
                }
                hasTileMatch = true;
                break;
            }
            if (hasTileMatch) {
                continue;
            }
            crystals.add(crystal);
            npc.getController().addMapObject(crystal);
        }
        var event = new PEvent(Event.MILLIS_2400) {
            execute: function() {
                if (event.getExecutions() == 0) {
                    event.setTick(Event.MILLIS_1200);
                    var players = npc.getController().getPlayers();
                    for each (var crystal in crystals) {
                        for each (var player in players) {
                            if (player.isLocked() || !crystal.withinDistance(player, 0)) {
                                continue;
                            }
                            var hitEvent = new HitEvent(0, player, new Hit(30 + PRandom.randomI(15)));
                            player.addHit(hitEvent);
                            player.setInCombatDelay(Entity.COMBAT_DELAY);
                            player.getGameEncoder().sendMessage("The crystal beneath your feet grows rapidly and shunts you to the side.");
                        }
                        npc.getController().addMapObject(new MapObject(30034, crystal));
                    }
                } else {
                    event.stop();
                    for each (var crystal in crystals) {
                        npc.getController().addMapObject(new MapObject(-1, crystal));
                        npc.getController().sendMapGraphic(crystal, new Graphic(1353));
                    }
                }
            }
        };
        npc.getWorld().addEvent(event);
    },

    lightning: function() {
        npc.setHitDelay(4);
        this.setAnimation(LEFT_HAND, 7358, true);
        var tiles = PCollection.toList(
            new Tile(3228, 5748), new Tile(3229, 5748), new Tile(3230, 5748), new Tile(3231, 5748),
            new Tile(3232, 5748), new Tile(3233, 5748), new Tile(3234, 5748), new Tile(3235, 5748),
            new Tile(3236, 5748), new Tile(3237, 5747), new Tile(3228, 5731), new Tile(3229, 5731),
            new Tile(3230, 5731), new Tile(3231, 5730), new Tile(3232, 5730), new Tile(3233, 5730),
            new Tile(3234, 5730), new Tile(3235, 5731), new Tile(3236, 5731), new Tile(3237, 5731)
        );
        Collections.shuffle(tiles);
        var selectedTiles = [
            tiles.get(0), tiles.get(1), tiles.get(2), tiles.get(3)
        ];
        var directions = [];
        for each (var tile in selectedTiles) {
            directions.push((tile.getY() > 5739) ? Tile.SOUTH : Tile.NORTH);
        }
        var event = new PEvent(Event.MILLIS_600) {
            execute: function() {
                var stillWorking = false;
                var players = npc.getController().getPlayers();
                for (var i = 0; i < selectedTiles.length; i++) {
                    var tile = selectedTiles[i];
                    var direction = directions[i];
                    var toTile = (new Tile(tile)).moveY(direction == Tile.NORTH ? 1 : -1);
                    var routeDirection = direction == Tile.NORTH ? Route.NORTH : Route.SOUTH
                    if (Route.blocked(npc, toTile, routeDirection)) {
                        continue;
                    }
                    stillWorking = true;
                    npc.getController().sendMapGraphic(tile, new Graphic(1356));
                    for each (var player in players) {
                        if (player.isLocked() || !tile.withinDistance(player, 0)) {
                            continue;
                        }
                        var hitEvent = new HitEvent(0, player, new Hit(PRandom.randomI(33)));
                        player.addHit(hitEvent);
                        player.setInCombatDelay(Entity.COMBAT_DELAY);
                        player.getPrayer().deactivate("protect from magic");
                        player.getPrayer().deactivate("protect from missiles");
                        player.getPrayer().deactivate("protect from melee");
                        player.getPrayer().setDamageProtectionPrayerBlock(8);
                        player.getController().setMagicBind(8, npc);
                        player.getGameEncoder().sendMessage("<col=ff0000>You've been eletrocuted to the spot!</col>");
                        player.getGameEncoder().sendMessage("You've been injured and can't use protection prayers!");
                    }
                    tile.moveY(direction == Tile.NORTH ? 1 : -1);
                }
                if (!stillWorking) {
                    event.stop();
                }
            }
        };
        npc.getWorld().addEvent(event);
    },

    swamp: function() {
        npc.setHitDelay(4);
        this.setAnimation(LEFT_HAND, 7359, true);
        var playerMap = new HashMap();
        var players = this.getAttackablePlayers();
        if (players.isEmpty()) {
            return;
        }
        Collections.shuffle(players);
        if (players.size() > 1 && (players.size() % 2) != 0) {
            players.remove(players.size() - 1);
        }
        if (players.size() == 1) {
            var tile = new Tile(3228, 5731);
            tile.moveTile(PRandom.randomI(9), PRandom.randomI(16));
            playerMap.put(players.get(0), tile);
            players.get(0).getGameEncoder().sendMessage("You have been paired with <col=ff0000>a random location</col>! The magical power will enact soon.");
        } else {
            for (var i = 0; i < players.size(); i += 2) {
                var player1 = players.get(i);
                var player2 = players.get(i + 1);
                playerMap.put(player1, player2);
                player1.getGameEncoder().sendMessage("You have been paired with <col=ff0000>"
                        + player2.getUsername() + "</col>! The magical power will enact soon.");
                player2.getGameEncoder().sendMessage("You have been paired with <col=ff0000>"
                        + player1.getUsername() + "</col>! The magical power will enact soon.");
            }
        }
        var graphicIds = [ 1359, 1360, 1361, 1362 ];
        var event = new PEvent(Event.MILLIS_600) {
            execute: function() {
                if (event.getExecutions() == Event.MILLIS_5400) {
                    event.stop();
                }
                var graphicIndex = -1;
                for each (var entry in playerMap.entrySet()) {
                    graphicIndex = (graphicIndex + 1) % graphicIds.length;
                    var key = entry.getKey();
                    var value = entry.getValue();
                    if (!key.isVisible() || !value.isVisible() || !npc.withinDistance(key, 32)
                            || !npc.withinDistance(value, 32) || key.getY() < 5730 || value.getY() < 5730) {
                        continue;
                    }
                    if (event.getExecutions() == Event.MILLIS_5400) {
                        if (key.withinDistance(value, 0)) {
                            key.getGameEncoder().sendMessage("The teleport attack has no effect!");
                        } else {
                            key.getMovement().teleport(value);
                            key.setGraphic(1039);
                            var hitEvent = new HitEvent(0, key, new Hit(key.getDistance(value) * 5));
                            key.addHit(hitEvent);
                            key.setInCombatDelay(Entity.COMBAT_DELAY);
                            if (value instanceof Player) {
                                value.getMovement().teleport(key);
                                value.setGraphic(1039);
                                var hitEvent = new HitEvent(0, value, new Hit(value.getDistance(key) * 5));
                                value.addHit(hitEvent);
                                value.setInCombatDelay(Entity.COMBAT_DELAY);
                                key.getGameEncoder().sendMessage("Yourself and " + value.getUsername()
                                        + " have swapped places!");
                                value.getGameEncoder().sendMessage("Yourself and " + key.getUsername()
                                        + " have swapped places!");
                            }
                        }
                    } else {
                        var graphic = new Graphic(graphicIds[graphicIndex]);
                        key.setGraphic(graphic);
                        if (value instanceof Player) {
                            value.setGraphic(graphic);
                        } else {
                            npc.getController().sendMapGraphic(value, graphic);
                        }
                    }
                }
            },

            stopHook: function() {
                playerMap.clear();
            }
        };
        npc.getWorld().addEvent(event);
    },

    spheres: function() {
        npc.setHitDelay(4);
        this.setAnimation(HEAD, 7345, true);
        var players = this.getAttackablePlayers();
        if (players.isEmpty()) {
            return;
        }
        var projectileIds = [ 1345, 1343, 1341 ];
        var contactIds = [ 1346, 1344, 1342 ];
        var types = [];
        var projectile = cs.getSpeed(12);
        for each (var player in players) {
            var index = PRandom.arrayRandom(World.MELEE, World.RANGED, World.MAGIC);
            types.push(index);
            var message = "";
            if (index == World.MELEE) {
                message = "<col=ff0000>The Great olm fires a sphere of aggression your way.</col>"
            } else if (index == World.RANGED) {
                message = "<col=00ff00>The Great olm fires a sphere of accuracy and dexterity your way.</col>"
            } else if (index == World.MAGIC) {
                message = "<col=0000ff>The Great olm fires a sphere of magical power your way.</col>"
            }
            if (player.getPrayer().hasActive("protect from magic")
                    || player.getPrayer().hasActive("protect from missiles")
                    || player.getPrayer().hasActive("protect from melee")) {
                message += " Your prayers have been sapped.";
                player.getPrayer().deactivate("protect from magic");
                player.getPrayer().deactivate("protect from missiles");
                player.getPrayer().deactivate("protect from melee");
                player.getPrayer().adjustPoints(-(player.getPrayer().getPoints() / 2));
            }
            player.getGameEncoder().sendMessage(message);
            cs.sendMapProjectile(player, npc, player, projectileIds[index], 43, 31, projectile.clientDelay,
                    projectile.clientSpeed, 16, 64);
            player.setGraphic(new Graphic(contactIds[index], 124, projectile.getContactDelay()));
        }
        var event = new PEvent(projectile.eventDelay) {
            execute: function() {
                event.stop();
                for (var i = 0; i < players.size(); i++) {
                    var player = players.get(i);
                    var type = types[i];
                    if (!player.isVisible()
                            || type == World.MELEE && player.getPrayer().hasActive("protect from melee")
                            || type == World.RANGED && player.getPrayer().hasActive("protect from missiles")
                            || type == World.MAGIC && player.getPrayer().hasActive("protect from magic")) {
                        continue;
                    }
                    var hitEvent = new HitEvent(0, player, new Hit(player.getHitpoints() / 2));
                    player.addHit(hitEvent);
                    player.setInCombatDelay(Entity.COMBAT_DELAY);
                }
            }
        };
        npc.getWorld().addEvent(event);
    },

    acidSpray: function() {
        npc.setHitDelay(4);
        this.setAnimation(HEAD, 7345, true);
        var projectile = cs.getSpeed(10);
        var pools = [];
        var poolTiles = [];
        for (var i = 0; i < 10; i++) {
            var tile = new Tile(3228, 5730);
            tile.moveTile(PRandom.randomI(9), PRandom.randomI(18));
            if (npc.getController().getMapObjectByType(10, tile.getX(), tile.getY(), tile.getHeight()) != null) {
                continue;
            }
            poolTiles.push(tile);
            cs.sendMapProjectile(null, npc, tile, 1354, 43, 31, projectile.clientDelay, projectile.clientSpeed,
                    16, 64);
        }
        var event = new PEvent(projectile.eventDelay) {
            execute: function() {
                if (event.getExecutions() == 0) {
                    event.setTick(0);
                    for each (var poolTile in poolTiles) {
                        var pool = new MapObject(30032, poolTile, 10, MapObject.getRandomDirection());
                        pools.push(pool);
                        npc.getController().addMapObject(pool);
                    }
                } else if (event.getExecutions() < 14) {
                    for each (var player in npc.getController().getPlayers()) {
                        for each (var pool in pools) {
                            if (player.isLocked() || !pool.withinDistance(player, 0)) {
                                continue;
                            }
                            var hitEvent = new HitEvent(0, player, new Hit(3 + PRandom.randomI(3), HitMark.POISON));
                            player.addHit(hitEvent);
                            player.setInCombatDelay(Entity.COMBAT_DELAY);
                        }
                    }
                } else {
                    event.stop();
                    for each (var pool in pools) {
                        npc.getController().addMapObject(new MapObject(-1, pool));
                    }
                }
            }
        };
        npc.getWorld().addEvent(event);
    },

    acidDrip: function() {
        var players = this.getAttackablePlayers();
        if (players.isEmpty()) {
            return;
        }
        Collections.shuffle(players);
        npc.setHitDelay(4);
        this.setAnimation(HEAD, 7345, true);
        var selectedPlayer = players.get(0);
        var projectile = cs.getSpeed(selectedPlayer);
        cs.sendMapProjectile(selectedPlayer, npc, selectedPlayer, 1354, 43, 31, projectile.clientDelay,
                projectile.clientSpeed, 16, 64);
        selectedPlayer.getGameEncoder().sendMessage("<col=ff0000>The Great Olm has smothered you in acid. It starts to drip off slowly.</col");
        if (!selectedPlayer.isPoisonImmune() && (selectedPlayer.getPoisonDelay() == 0
                || selectedPlayer.getPoisonDamage() < 2)) {
            if (selectedPlayer.getPoisonDelay() == 0) {
                selectedPlayer.getGameEncoder().sendMessage("You have been poisoned!");
            }
            selectedPlayer.setPoison(2);
        }
        var pools = [];
        var times = [];
        var event = new PEvent(projectile.eventDelay) {
            execute: function() {
                event.setTick(Event.MILLIS_600);
                var addedPool = null;
                var addedPool2 = null;
                if (event.getExecutions() < 22 && selectedPlayer.isVisible() && !selectedPlayer.isLocked()
                        && npc.withinDistance(selectedPlayer, 32) && selectedPlayer.getY() >= 5730
                        && npc.getController().getMapObjectByType(10, selectedPlayer) == null) {
                    addedPool = new MapObject(30032, selectedPlayer, 10, MapObject.getRandomDirection());
                    pools.push(addedPool);
                    times.push(16);
                    npc.getController().addMapObject(addedPool);
                    if (selectedPlayer.getMovement().getRunning() && selectedPlayer.getMovement().isRouting()) {
                        var nextTile = selectedPlayer.getMovement().getNextTile();
                        if (npc.getController().getMapObjectByType(10, nextTile) == null) {
                            addedPool2 = new MapObject(30032, nextTile, 10, MapObject.getRandomDirection());
                            pools.push(addedPool2);
                            times.push(16);
                            npc.getController().addMapObject(addedPool2);
                        }
                    }
                }
                for (var i = 0; i < times.length; i++) {
                    if (times[i]-- == 0) {
                        npc.getController().addMapObject(new MapObject(-1, pools[i]));
                        pools[i].setVisible(false);
                    }
                }
                for each (var player in npc.getController().getPlayers()) {
                    for each (var pool in pools) {
                        if (addedPool == pool || addedPool2 == pool || player.isLocked()
                                || !pool.withinDistance(player, 0)) {
                            continue;
                        }
                        var hitEvent = new HitEvent(0, player, new Hit(3 + PRandom.randomI(3), HitMark.POISON));
                        player.addHit(hitEvent);
                        player.setInCombatDelay(Entity.COMBAT_DELAY);
                    }
                }
                if (event.getExecutions() == 38) {
                    event.stop();
                    for each (var pool in pools) {
                        if (pool.isVisible()) {
                            npc.getController().addMapObject(new MapObject(-1, pool));
                        }
                    }
                }
            }
        };
        npc.getWorld().addEvent(event);
    },

    fallingCrystals: function() {
        var js = this;
        var event = new PEvent(Event.MILLIS_1200) {
            execute: function() {
                if (olm[HEAD].isDead()) {
                    event.stop();
                    return;
                }
                var count = 1 + PRandom.randomI(3);
                for (var i = 0; i < count; i++) {
                    var tile = new Tile(3228, 5730);
                    tile.moveTile(PRandom.randomI(9), PRandom.randomI(18));
                    npc.getController().sendMapProjectile(null, (new Tile(tile)).moveY(1), tile, 1357, 255, 10,
                            0, 51 + 120, 0, 0);
                    npc.getController().sendMapGraphic(tile, 1358, 0, 51 + 100);
                    npc.getController().sendMapGraphic(tile, 1449, 0, 0);
                    var the = new TileHitEvent(Event.MILLIS_3600, npc.getController(), tile, 20, HitType.TYPELESS);
                    the.setAdjacentHalfDamage(true);
                    npc.getWorld().addEvent(the);
                }
            }
        };
        npc.getWorld().addEvent(event);
    },
}
