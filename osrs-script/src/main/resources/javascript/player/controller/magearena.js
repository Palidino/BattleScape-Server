var ARENA_2_MONSTERS = [
    [ 1610, 1611, 1612 ],
    [ 7422, 6385, 891 ],
    [ 7422, 6385, 891, 1610, 1611, 1612 ],
    [ 7244, 2214, 6594 ],
    [ 7244, 2214, 6594, 1610, 1611, 1612 ],
    [ 7244, 2214, 6594, 7422, 6385, 891 ],
    [ 7244, 2214, 6594, 7422, 6385, 891, 1610, 1611, 1612 ],
    [ 3131, 2207, 7310 ],
    [ 3131, 2207, 7310, 1610, 1611, 1612 ],
    [ 3131, 2207, 7310, 7422, 6385, 891 ],
    [ 3131, 2207, 7310, 7422, 6385, 891, 1610, 1611, 1612 ],
    [ 3131, 2207, 7310, 7244, 2214, 6594 ],
    [ 3131, 2207, 7310, 7244, 2214, 6594, 1610, 1611, 1612 ],
    [ 3131, 2207, 7310, 7244, 2214, 6594, 7422, 6385, 891 ],
    [ 3131, 2207, 7310, 7244, 2214, 6594, 7422, 6385, 891, 1610, 1611, 1612 ],
    [ 7860, 7858, 7859 ]
];
var ARENA_2_SPAWNS = [
    [ 3098, 3925 ], // 1-1
    [ 3112, 3938 ], // 4-4
    [ 3108, 3925 ], // 1-3
    [ 3098, 3940 ], // 4-1
    [ 3103, 3925 ], // 1-2
    [ 3108, 3940 ], // 4-3
    [ 3103, 3940 ], // 4-2
    [ 3095, 3930 ], // 2-1
    [ 3110, 3935 ], // 3-4
    [ 3095, 3935 ], // 3-1
    [ 3110, 3930 ], // 2-4
    [ 3100, 3930 ], // 2-2
    [ 3105, 3935 ], // 3-3
    [ 3100, 3935 ], // 3-2
    [ 3105, 3930 ] // 2-3
];
var SPECIAL_ITEMS = [
    ItemId.SUPER_MAGIC_POTION_4, ItemId.SUPER_MAGIC_POTION_3, ItemId.SUPER_MAGIC_POTION_2, ItemId.SUPER_MAGIC_POTION_1,
    ItemId.ABSORPTION_4, ItemId.ABSORPTION_3, ItemId.ABSORPTION_2, ItemId.ABSORPTION_1
];
var ABSORPTION_POTION = new Skills.Drink(ItemId.ABSORPTION_4, ItemId.ABSORPTION_3, ItemId.ABSORPTION_2,
        ItemId.ABSORPTION_1, null);
ABSORPTION_POTION.empty = -1;
ABSORPTION_POTION.action = (new Skills.ItemAction() {
    execute: function(player) {
        absorption += 50;
        player.getGameEncoder().sendMessage("You have " + PNumber.formatNumber(absorption) + " points of absorption.");
    }
});

var player;
var monsters = new ArrayList();
var spawnDelay = 8;
var paused = false;
var arena2WaveId = 1;
var spawnIndex = PRandom.randomE(ARENA_2_SPAWNS.length);
var time = 0;
var recurrentDamage = 0;
var zapper = 0;
var zapperNPC = null;
var powerSurge = 0;
var absorption = 0;

pc = new PController() {
    /* @Override */
    setEntityHook: function(entity) {
        player = entity;
        if (player == null) {
            return;
        }
        pc.setExitTile(new Tile(2539, 4715, 0));
        pc.setKeepItemsOnDeath(true);
        pc.setBlockTeleport(true);
        pc.setMultiCombatFlag(true);
        pc.setDisableMonsterDrops(true);
        player.setLargeVisibility();
        pc.instance();
    },

    /* @Override */
    stopHook: function() {
        player.restore();
        player.getWorld().removeNpcs(monsters);
        player.getWorld().removeNpc(zapperNPC);
        for each (var specialId in SPECIAL_ITEMS) {
            player.getInventory().deleteAll(specialId);
            player.getEquipment().deleteAll(specialId);
        }
        player = null;
    },

    /* @Override */
    setVariableHook: function(name, object) {
        if (name.equals("arena2_wave_id")) {
            arena2WaveId = object;
        }
    },

    /* @Override */
    loadHook: function(map) {
        arena2WaveId = map.get("controller.arena2WaveId");
        spawnIndex = map.get("controller.spawnIndex");
        time = map.get("controller.time");
    },

    /* @Override */
    saveHook: function(map) {
        if (map != null) {
            map.put("controller.arena2WaveId", arena2WaveId);
            map.put("controller.spawnIndex", spawnIndex);
            map.put("controller.time", time);
        }
        return true;
    },

    /* @Override */
    instanceHook: function() {
        pc.addMapObject(new MapObject(-1, new Tile(3097, 3938, 0), 10, 0)); // Skeleton
        pc.addMapObject(new MapObject(-1, new Tile(3104, 3938, 0), 10, 0)); // Skeleton
        pc.addMapObject(new MapObject(-1, new Tile(3111, 3938, 0), 10, 0)); // Skeleton
        pc.addMapObject(new MapObject(-1, new Tile(3099, 3931, 0), 10, 0)); // Skeleton
        pc.addMapObject(new MapObject(-1, new Tile(3108, 3931, 0), 10, 0)); // Skeleton
        pc.addMapObject(new MapObject(-1, new Tile(3105, 3925, 0), 10, 0)); // Skeleton
        pc.addMapObject(new MapObject(-1, new Tile(3110, 3927, 0), 10, 0)); // Skeleton
        pc.addMapObject(new MapObject(12351, new Tile(3092, 3934, 0), 10, 2)); // Barrier
        pc.addMapObject(new MapObject(12351, new Tile(3092, 3933, 0), 10, 2)); // Barrier
    },

    /* @Override */
    tickHook: function() {
        if (!monsters.isEmpty()) {
            for (var it = monsters.iterator(); it.hasNext();) {
                var npc = it.next();
                if (npc.isVisible() || !npc.isDead()) {
                    continue;
                }
                it.remove();
            }
            if (monsters.isEmpty() && arena2WaveId > 0) {
                player.getGameEncoder().sendMessage("Wave completed!");
                spawnDelay = 4;
                if (arena2WaveId++ == ARENA_2_MONSTERS.length) {
                    this.finishArena2();
                }
            }
        }
        if (player.isDead()) {
            return;
        }
        if (spawnDelay > 0) {
            if (!paused && --spawnDelay == 0) {
                player.getGameEncoder().sendMessage("<col=ff0000>Wave: " + arena2WaveId);
                this.spawnArena2Monsters();
            }
            return;
        }
        time++;
        if (recurrentDamage > 0) {
            recurrentDamage--;
        }
        if (zapper > 0) {
            zapper--;
            if (zapper == 0) {
                player.getWorld().removeNpc(zapperNPC);
            }
        }
        if (powerSurge == 0 && PRandom.randomE(192) == 0) {
            powerSurge = -1;
            pc.addMapObject(new MapObject(26264, this.getRandomTile(), 10, 0)); // Power surge
            player.getGameEncoder().sendMessage("<col=4443FA>A power-up has spawned: <col=ff0000>Power surge");
        }
        if (recurrentDamage == 0 && PRandom.randomE(128) == 0) {
            recurrentDamage = -1;
            pc.addMapObject(new MapObject(26265, this.getRandomTile(), 10, 0)); // Recurrent damage
            player.getGameEncoder().sendMessage("<col=4443FA>A power-up has spawned: <col=ff0000>Recurrent damage");
        }
        if (zapper == 0 && PRandom.randomE(96) == 0) {
            zapper = -1;
            pc.addMapObject(new MapObject(26256, this.getRandomTile(), 10, 0)); // Zapper
            player.getGameEncoder().sendMessage("<col=4443FA>A power-up has spawned: <col=ff0000>Zapper");
        }
        if (PRandom.randomE(128) == 0) {
            if (PRandom.randomE(4) == 0) {
                pc.addMapItem(new Item(ItemId.SUPER_MAGIC_POTION_4 + PRandom.randomI(3), 1), player, player);
            } else {
                pc.addMapItem(new Item(ItemId.ABSORPTION_4 + PRandom.randomI(3), 1), player, player);
            }
        }
    },

    /* @Override */
    applyDeadHook: function() {
        pc.stopWithTeleport();
    },

    /* @Override */
    damageInflictedHook: function(damage, entity, hitType, defenceType) {
        if (recurrentDamage > 0) {
            damage *= 1.75;
        }
        return damage;
    },

    /* @Override */
    damageReceivedHook: function(damage, entity, hitType, defenceType) {
        if (absorption > 0) {
            var absorbed = Math.min(damage, absorption);
            damage -= absorbed;
            absorption -= absorbed;
            if (absorption == 0) {
                player.getGameEncoder().sendMessage("<col=ff0000>You have run out of absorption.");
            }
        }
        return damage;
    },

    /* @Override */
    getDrinkHook: function(itemId, drink) {
        if (drink != null && ABSORPTION_POTION.isDose(drink.dose1)) {
            return ABSORPTION_POTION;
        }
        return drink;
    },

    /* @Override */
    mapObjectOptionHook: function(index, mapObject) {
        switch (mapObject.getId()) {
        case 12351: // Barrier
            pc.exitTileTeleport();
            return true;
        case 26265: // Recurrent damage
            if (recurrentDamage > 0) {
                player.getGameEncoder().sendMessage("You can't use this right now.");
                return true;
            }
            recurrentDamage = 75;
            pc.addMapObject(new MapObject(-1, mapObject));
            return true;
        case 26256: // Zapper
            if (zapper > 0) {
                player.getGameEncoder().sendMessage("You can't use this right now.");
                return true;
            }
            zapper = 100;
            zapperNPC = new Npc(pc, 6341, mapObject);
            zapperNPC.setLargeVisibility();
            zapperNPC.getMovement().setIgnoreNPCs(true);
            zapperNPC.getController().setMultiCombatFlag(true);
            pc.addMapObject(new MapObject(-1, mapObject));
            return true;
        case 26264: // Power surge
            if (monsters.isEmpty() || arena2WaveId == ARENA_2_MONSTERS.length) {
                player.getGameEncoder().sendMessage("This seems to be useless now.");
                return true;
            }
            powerSurge = 0;
            for each (var npc in monsters) {
                npc.getCombat().timedDeath();
            }
            pc.addMapObject(new MapObject(-1, mapObject));
            return true;
        }
        return false;
    },

    /* @Override */
    logoutWidgetHook: function() {
        if (paused) {
            return;
        }
        paused = true;
        if (spawnDelay > 0) {
            player.getGameEncoder().sendMessage("<col=ff0000>The Mage Arena has been paused.");
        } else {
            player.getGameEncoder().sendMessage("<col=ff0000>The Mage Arena will be paused after this wave.");
        }
    },

    /* @Override */
    inWilderness: function() {
        return false;
    },

    finishArena2: function() {
        pc.exitTileTeleport();
        player.getGameEncoder().sendMessage("Duration: <col=ff0000>" + Time.ticksToDuration(time));
        if (!player.getInventory().addItem(ItemId.IMBUED_SARADOMIN_CAPE, 1).success()) {
            player.getBank().add(new Item(ItemId.IMBUED_SARADOMIN_CAPE, 1));
        }
        if (!player.getInventory().addItem(ItemId.IMBUED_GUTHIX_CAPE, 1).success()) {
            player.getBank().add(new Item(ItemId.IMBUED_GUTHIX_CAPE, 1));
        }
        if (!player.getInventory().addItem(ItemId.IMBUED_ZAMORAK_CAPE, 1).success()) {
            player.getBank().add(new Item(ItemId.IMBUED_ZAMORAK_CAPE, 1));
        }
        player.getCombat().setMageArena2(true);
    },

    spawnArena2Monsters: function() {
        var npcIds = ARENA_2_MONSTERS[arena2WaveId - 1];
        for (var i = 0; i < npcIds.length; i++) {
            var coords = this.getArena2Coords(npcIds[i]);
            var npc = new Npc(pc, npcIds[i], new Tile(coords[0], coords[1], player.getHeight()));
            npc.setLargeVisibility();
            npc.getMovement().setClipNPCs(true);
            npc.getController().setMultiCombatFlag(true);
            npc.getCombat().setTarget(player);
            monsters.add(npc);
        }
    },

    getArena2Coords: function(npcId) {
        spawnIndex = (spawnIndex + 1) % ARENA_2_SPAWNS.length;
        return ARENA_2_SPAWNS[spawnIndex];
    },

    getRandomTile: function() {
        var tile;
        if (PRandom.randomE(2) == 0) {
            tile = new Tile(3093, 3930);
            tile.moveTile(PRandom.randomI(24), PRandom.randomI(7));
        } else {
            tile = new Tile(3102, 3921);
            tile.moveTile(PRandom.randomI(6), PRandom.randomI(25));
        }
        return tile;
    }
};
