function Room(entrance, exit) {
    this.name = "";
    this.entrancePassageCoords = entrance;
    this.exitPassageCoords = exit;
    this.npcs = null;
    this.customEntranceObject = null;
    this.customExitObject = null;
    this.customMapObjects = [];
    this.npcRefs = null;
    this.npcRefs2Players = null;
    this.npcRefs3Players = null;
    this.npcRefs4Players = null;
    this.npcRefs5Players = null;
    this.npcRefs6Players = null;

    this.isComplete = function() {
        var allMonstersDead = true;
        if (this.npcs != null) {
            for each (var npc in this.npcs) {
                if (npc.isVisible() && !npc.getRespawns()) {
                    allMonstersDead = false;
                }
            }
        }
        return allMonstersDead;
    }
}

function NPCReference(id, tile, moveDistance) {
    moveDistance = typeof moveDistance !== "undefined" ? moveDistance : 0;
    this.id = id;
    this.tile = tile;
    this.moveDistance = moveDistance;
}

var FIRST_ROOM = new Room(new Tile(3298, 5188), new Tile(3307, 5204));
var PRE_OLM_ROOM = new Room(new Tile(3314, 5160), new Tile(3308, 5173));
var OLM_ROOM = new Room(new Tile(3232, 5721), null);
OLM_ROOM.npcRefs = [ new NPCReference(7554, new Tile(3238, 5738, 0)) ];
var RESOURCE_ROOM_1 = new Room(new Tile(3311, 5441, 0), new Tile(3311, 5471, 0));
RESOURCE_ROOM_1.customEntranceObject = new MapObject(29789, new Tile(3311, 5438, 0), 10, 1);
RESOURCE_ROOM_1.customExitObject = new MapObject(29789, new Tile(3311, 5472, 0), 10, 1);
var RESOURCE_ROOM_2 = new Room(new Tile(3311, 5441, 1), new Tile(3311, 5471, 1));
RESOURCE_ROOM_2.customEntranceObject = new MapObject(29789, new Tile(3311, 5438, 1), 10, 1);
RESOURCE_ROOM_2.customExitObject = new MapObject(29789, new Tile(3311, 5472, 1), 10, 1);

var ROOM_LIZARDMAN_SHAMANS = new Room(new Tile(3307, 5208), new Tile(3311, 5275));
ROOM_LIZARDMAN_SHAMANS.npcRefs = [
    new NPCReference(7573, new Tile(3306, 5259), 4),
    new NPCReference(7574, new Tile(3315, 5266), 4),
    new NPCReference(7548, new Tile(3302, 5237), 4),
    new NPCReference(7548, new Tile(3302, 5230), 4),
    new NPCReference(7548, new Tile(3308, 5226), 4),
    new NPCReference(7548, new Tile(3307, 5238), 4)
];
ROOM_LIZARDMAN_SHAMANS.npcRefs3Players = [ new NPCReference(7573, new Tile(3306, 5266), 4) ];
ROOM_LIZARDMAN_SHAMANS.npcRefs6Players = [ new NPCReference(7574, new Tile(3315, 5259), 4) ];
ROOM_LIZARDMAN_SHAMANS.customMapObjects.push(new MapObject(29768, new Tile(3310, 5251), 10, 0));
ROOM_LIZARDMAN_SHAMANS.customMapObjects.push(new MapObject(29768, new Tile(3311, 5251), 10, 0));

var ROOM_SKELETAL_MYSTICS = new Room(new Tile(3311, 5218, 1), new Tile(3309, 5273, 1));
ROOM_SKELETAL_MYSTICS.customEntranceObject = new MapObject(29789, new Tile(3311, 5215, 1), 10, 1);
ROOM_SKELETAL_MYSTICS.npcRefs = [
    new NPCReference(7604, new Tile(3312, 5259, 1), 4),
    new NPCReference(7605, new Tile(3306, 5262, 1), 4),
    new NPCReference(7606, new Tile(3303, 5254, 1), 4),
    new NPCReference(7548, new Tile(3298, 5227, 1), 4),
    new NPCReference(7548, new Tile(3299, 5232, 1), 4),
    new NPCReference(7548, new Tile(3302, 5237, 1), 4),
    new NPCReference(7548, new Tile(3302, 5222, 1), 4),
    new NPCReference(7548, new Tile(3305, 5226, 1), 4),
    new NPCReference(7548, new Tile(3307, 5231, 1), 4)
];
ROOM_SKELETAL_MYSTICS.npcRefs4Players = [ new NPCReference(7604, new Tile(3313, 5266, 1), 4) ];

var NORMAL_ROOMS = PCollection.toList(ROOM_LIZARDMAN_SHAMANS, ROOM_SKELETAL_MYSTICS);
Collections.shuffle(NORMAL_ROOMS);

var ROOM_VASA_NISTIRIO = new Room(new Tile(3311, 5279), new Tile(3311, 5307));
ROOM_VASA_NISTIRIO.name = "Vasa Nistirio";
ROOM_VASA_NISTIRIO.npcRefs = [ new NPCReference(7565, new Tile(3309, 5293)) ];
ROOM_VASA_NISTIRIO.customMapObjects.push(new MapObject(30019, new Tile(3311, 5280), 10, 0));

var ROOM_TEKTON = new Room(new Tile(3309, 5277, 1), new Tile(3310, 5305, 1));
ROOM_TEKTON.name = "Tekton";
ROOM_TEKTON.npcRefs = [ new NPCReference(7540, new Tile(3307, 5294, 1)) ];
ROOM_TEKTON.customMapObjects.push(new MapObject(30021, new Tile(3311, 5281, 1), 10, 0));

var ROOM_VANGUARDS = new Room(new Tile(3311, 5311), new Tile(3311, 5340));
ROOM_VANGUARDS.name = "Vanguards";
ROOM_VANGUARDS.npcRefs = [ new NPCReference(7527, new Tile(3316, 5329)) ];

var ROOM_MUTTADILE = new Room(new Tile(3310, 5309, 1), new Tile(3308, 5336, 1));
ROOM_MUTTADILE.name = "Muttadile";
ROOM_MUTTADILE.npcRefs = [ new NPCReference(7561, new Tile(3314, 5331, 1)) ];
ROOM_MUTTADILE.customMapObjects.push(new MapObject(29767, new Tile(3311, 5313, 1), 10, 0));
ROOM_MUTTADILE.customMapObjects.push(new MapObject(29767, new Tile(3312, 5313, 1), 10, 0));

var ROOM_XARPUS = new Room(new Tile(3170, 4375, 1), new Tile(3170, 4399, 1));
ROOM_XARPUS.name = "Xarpus";
ROOM_XARPUS.npcRefs = [ new NPCReference(8338, new Tile(3169, 4386, 1)) ];
ROOM_XARPUS.customEntranceObject = new MapObject(32751, new Tile(3169, 4374, 1), 10, 2);

var ROOM_MAIDEN_SUGADINTI = new Room(new Tile(3219, 4459), new Tile(3177, 4422));
ROOM_MAIDEN_SUGADINTI.name = "Maiden Sugadinti";
ROOM_MAIDEN_SUGADINTI.npcRefs = [ new NPCReference(8360, new Tile(3162, 4444)) ];
ROOM_MAIDEN_SUGADINTI.customEntranceObject = new MapObject(2156, new Tile(3219, 4460), 10, 1);

var ROOM_PESTILENT_BLOAT = new Room(new Tile(3321, 4447), new Tile(3269, 4447));
ROOM_PESTILENT_BLOAT.name = "Pestilent Bloat";
ROOM_PESTILENT_BLOAT.npcRefs = [ new NPCReference(8359, new Tile(3299, 4451)) ];
ROOM_PESTILENT_BLOAT.customEntranceObject = new MapObject(2156, new Tile(3322, 4447), 10, 2);

var ROOM_VERZIK_VITUR = new Room(new Tile(3168, 4298), null);
ROOM_VERZIK_VITUR.name = "Virzuk Vitur";
ROOM_VERZIK_VITUR.npcRefs = [ new NPCReference(NpcId.VERZIK_VITUR_1040_8370, new Tile(3166, 4323)) ];

var BOSS_ROOMS = PCollection.toList(ROOM_VASA_NISTIRIO, ROOM_TEKTON, ROOM_VANGUARDS, ROOM_MUTTADILE);
Collections.shuffle(BOSS_ROOMS);

var UNIQUE_REWARDS = [
    new RandomItem(ItemId.DEXTEROUS_PRAYER_SCROLL, 1).weight(20),
    new RandomItem(ItemId.ARCANE_PRAYER_SCROLL, 1).weight(20),
    new RandomItem(ItemId.TWISTED_BUCKLER, 1).weight(4),
    new RandomItem(ItemId.DRAGON_HUNTER_CROSSBOW, 1).weight(4),
    new RandomItem(ItemId.DINHS_BULWARK, 1).weight(3),
    new RandomItem(ItemId.ANCESTRAL_HAT, 1).weight(3),
    new RandomItem(ItemId.ANCESTRAL_ROBE_TOP, 1).weight(3),
    new RandomItem(ItemId.ANCESTRAL_ROBE_BOTTOM, 1).weight(3),
    new RandomItem(ItemId.DRAGON_CLAWS, 1).weight(3),
    new RandomItem(ItemId.ELDER_MAUL, 1).weight(2),
    new RandomItem(ItemId.KODAI_INSIGNIA, 1).weight(2),
    new RandomItem(ItemId.TWISTED_BOW, 1).weight(2)
];
var REWARDS = [
    new RandomItem(ItemId.DEATH_RUNE, 3100), new RandomItem(ItemId.BLOOD_RUNE, 4095),
    new RandomItem(ItemId.SOUL_RUNE, 6554), new RandomItem(ItemId.RUNE_ARROW, 9437),
    new RandomItem(ItemId.DRAGON_ARROW, 926), new RandomItem(ItemId.GRIMY_TOADFLAX_NOTED, 354),
    new RandomItem(ItemId.GRIMY_RANARR_WEED_NOTED, 164), new RandomItem(ItemId.GRIMY_IRIT_LEAF_NOTED, 668),
    new RandomItem(ItemId.GRIMY_AVANTOE_NOTED, 354), new RandomItem(ItemId.GRIMY_KWUARM_NOTED, 323),
    new RandomItem(ItemId.GRIMY_SNAPDRAGON_NOTED, 131), new RandomItem(ItemId.GRIMY_CADANTINE_NOTED, 319),
    new RandomItem(ItemId.GRIMY_LANTADYME_NOTED, 319), new RandomItem(ItemId.GRIMY_DWARF_WEED_NOTED, 616),
    new RandomItem(ItemId.GRIMY_TORSTOL_NOTED, 153), new RandomItem(ItemId.SILVER_ORE_NOTED, 6553),
    new RandomItem(ItemId.COAL_NOTED, 6553), new RandomItem(ItemId.GOLD_ORE_NOTED, 2892),
    new RandomItem(ItemId.MITHRIL_ORE_NOTED, 2892), new RandomItem(ItemId.ADAMANTITE_ORE_NOTED, 729),
    new RandomItem(ItemId.RUNITE_ORE_NOTED, 87), new RandomItem(ItemId.UNCUT_SAPPHIRE_NOTED, 642),
    new RandomItem(ItemId.UNCUT_EMERALD_NOTED, 923), new RandomItem(ItemId.UNCUT_RUBY_NOTED, 524),
    new RandomItem(ItemId.UNCUT_DIAMOND_NOTED, 253), new RandomItem(ItemId.ANCIENT_TABLET, 1),
    new RandomItem(ItemId.LIZARDMAN_FANG, 4898), new RandomItem(ItemId.PURE_ESSENCE_NOTED, 65535),
    new RandomItem(ItemId.SALTPETRE_NOTED, 5461), new RandomItem(ItemId.TEAK_PLANK_NOTED, 1310),
    new RandomItem(ItemId.MAHOGANY_PLANK_NOTED, 550), new RandomItem(ItemId.DYNAMITE_NOTED, 2390),
    new RandomItem(ItemId.TORN_PRAYER_SCROLL, 1), new RandomItem(ItemId.DARK_RELIC, 1),
    new RandomItem(ItemId.CLUE_SCROLL_ELITE, 1)
];

var DRINKS = new ArrayList();
DRINKS.add(new Skills.Drink(20976, 20975, 20974, 20973, [
    [ Skills.HITPOINTS, 2, 8, 1 ],
    [ Skills.DEFENCE, 2, 12, 1 ],
    [ Skills.ATTACK, 0, -10, 0 ],
    [ Skills.STRENGTH, 0, -10, 0 ],
    [ Skills.RANGED, 0, -10, 0 ],
    [ Skills.MAGIC, 0, -10, 0 ]
])); // Xeric's aid (-)
DRINKS.add(new Skills.Drink(20980, 20979, 20978, 20977, [
    [ Skills.HITPOINTS, 3, 10, 1 ],
    [ Skills.DEFENCE, 3, 15, 1 ],
    [ Skills.ATTACK, 0, -10, 0 ],
    [ Skills.STRENGTH, 0, -10, 0 ],
    [ Skills.RANGED, 0, -10, 0 ],
    [ Skills.MAGIC, 0, -10, 0 ]
])); // Xeric's aid
DRINKS.add(new Skills.Drink(20984, 20983, 20982, 20981, [
    [ Skills.HITPOINTS, 5, 15, 1 ],
    [ Skills.DEFENCE, 5, 20, 1 ],
    [ Skills.ATTACK, 0, -10, 0 ],
    [ Skills.STRENGTH, 0, -10, 0 ],
    [ Skills.RANGED, 0, -10, 0 ],
    [ Skills.MAGIC, 0, -10, 0 ]
])); // Xeric's aid (+)
DRINKS.add(new Skills.Drink(20916, 20915, 20914, 20913, [
    [ Skills.ATTACK, 4, 10, 1 ],
    [ Skills.DEFENCE, 4, 10, 1 ],
    [ Skills.STRENGTH, 4, 10, 1 ]
])); // Elder (-)
DRINKS.add(new Skills.Drink(20920, 20919, 20918, 20917, [
    [ Skills.ATTACK, 5, 13, 1 ],
    [ Skills.DEFENCE, 5, 13, 1 ],
    [ Skills.STRENGTH, 5, 13, 1 ]
])); // Elder
DRINKS.add(new Skills.Drink(20924, 20923, 20922, 20921, [
    [ Skills.ATTACK, 6, 16, 1 ],
    [ Skills.DEFENCE, 6, 16, 1 ],
    [ Skills.STRENGTH, 6, 16, 1 ]
])); // Elder (+)
DRINKS.add(new Skills.Drink(20928, 20927, 20926, 20925, [ [ Skills.RANGED, 4, 10, 1 ] ])); // Twisted (-)
DRINKS.add(new Skills.Drink(20932, 20931, 20930, 20929, [ [ Skills.RANGED, 5, 13, 1 ] ])); // Twisted
DRINKS.add(new Skills.Drink(20936, 20935, 20934, 20933, [ [ Skills.RANGED, 6, 16, 1 ] ])); // Twisted (+)
DRINKS.add(new Skills.Drink(20940, 20939, 20938, 20937, [ [ Skills.MAGIC, 4, 10, 1 ] ])); // Kodai (-)
DRINKS.add(new Skills.Drink(20944, 20943, 20942, 20941, [ [ Skills.MAGIC, 5, 13, 1 ] ])); // Kodai
DRINKS.add(new Skills.Drink(20948, 20947, 20946, 20945, [ [ Skills.MAGIC, 6, 16, 1 ] ])); // Kodai (+)
var REVIT_MINUS = [];
for (var i = 0; i < Skills.SKILL_COUNT; i++) {
    if (i != Skills.HITPOINTS) {
        REVIT_MINUS.push([ i, 10, 18, 0 ]);
    }
}
DRINKS.add(new Skills.Drink(20952, 20951, 20950, 20949, Java.to(REVIT_MINUS, "int[][]"))); // Revitalisation (-)
var REVIT = [];
for (var i = 0; i < Skills.SKILL_COUNT; i++) {
    if (i != Skills.HITPOINTS) {
        REVIT.push([ i, 12, 20, 0 ]);
    }
}
DRINKS.add(new Skills.Drink(20956, 20955, 20954, 20953, Java.to(REVIT, "int[][]"))); // Revitalisation
var REVIT_PLUS = [];
for (var i = 0; i < Skills.SKILL_COUNT; i++) {
    if (i != Skills.HITPOINTS) {
        REVIT_PLUS.push([ i, 15, 25, 0 ]);
    }
}
DRINKS.add(new Skills.Drink(20960, 20959, 20958, 20957, Java.to(REVIT_PLUS, "int[][]"))); // Revitalisation (+)
var OVERLOAD_MINUS = new Skills.Drink(20988, 20987, 20986, 20985, null);
OVERLOAD_MINUS.action = new Skills.ItemAction() {
    execute: function(player) {
        var event = new Event(0) {
            execute: function() {
                if (!player.isVisible() || player.isDead()) {
                    event.stop();
                    return;
                }
                if (event.getExecutions() == 475) {
                    player.getGameEncoder().sendMessage("<col=ff0000>Your overload effect has almost ran out!</col>");
                }
                if (event.getExecutions() == 500) {
                    if (!player.isLocked() && event.getExecutions() == 500) {
                        player.adjustHitpoints(50);
                        player.getGameEncoder().sendMessage("<col=ff0000>The effects of overload have worn off, and you feel normal again.</col>");
                    }
                    event.stop();
                    return;
                }
                if ((event.getExecutions() % 2) == 0 && event.getExecutions() <= 8) {
                    player.setAnimation(3170);
                    player.applyHit(new Hit(10));
                }
                if (event.getExecutions() == 0 || (event.getExecutions() % 25) == 0) {
                    var skillIds = [ Skills.ATTACK, Skills.DEFENCE, Skills.STRENGTH, Skills.RANGED, Skills.MAGIC ];
                    for each (skillId in skillIds) {
                        player.getSkills().changeStat(skillId, (player.getController().getLevelForXP(skillId)
                                * 0.1 + 4)|0, true, 0);
                    }
                }
            }
        };
        player.getSkills().setOverload(event);
    }
};
DRINKS.add(OVERLOAD_MINUS);
var OVERLOAD = new Skills.Drink(20992, 20991, 20990, 20989, null);
OVERLOAD.action = new Skills.ItemAction() {
    execute: function(player) {
        var event = new Event(0) {
            execute: function() {
                if (!player.isVisible() || player.isDead()) {
                    event.stop();
                    return;
                }
                if (event.getExecutions() == 475) {
                    player.getGameEncoder().sendMessage("<col=ff0000>Your overload effect has almost ran out!</col>");
                }
                if (event.getExecutions() == 500) {
                    if (!player.isLocked() && event.getExecutions() == 500) {
                        player.adjustHitpoints(50);
                        player.getGameEncoder().sendMessage("<col=ff0000>The effects of overload have worn off, and you feel normal again.</col>");
                    }
                    event.stop();
                    return;
                }
                if ((event.getExecutions() % 2) == 0 && event.getExecutions() <= 8) {
                    player.setAnimation(3170);
                    player.applyHit(new Hit(10));
                }
                if (event.getExecutions() == 0 || (event.getExecutions() % 25) == 0) {
                    var skillIds = [ Skills.ATTACK, Skills.DEFENCE, Skills.STRENGTH, Skills.RANGED, Skills.MAGIC ];
                    for each (skillId in skillIds) {
                        player.getSkills().changeStat(skillId, (player.getController().getLevelForXP(skillId)
                                * 0.13 + 5)|0, true, 0);
                    }
                }
            }
        };
        player.getSkills().setOverload(event);
    }
};
DRINKS.add(OVERLOAD);
var OVERLOAD_PLUS = new Skills.Drink(20996, 20995, 20994, 20993, null);
OVERLOAD_PLUS.action = new Skills.ItemAction() {
    execute: function(player) {
        var event = new Event(0) {
            execute: function() {
                if (!player.isVisible() || player.isDead()) {
                    event.stop();
                    return;
                }
                if (event.getExecutions() == 475) {
                    player.getGameEncoder().sendMessage("<col=ff0000>Your overload effect has almost ran out!</col>");
                }
                if (event.getExecutions() == 500) {
                    if (!player.isLocked() && event.getExecutions() == 500) {
                        player.adjustHitpoints(50);
                        player.getGameEncoder().sendMessage("<col=ff0000>The effects of overload have worn off, and you feel normal again.</col>");
                    }
                    event.stop();
                    return;
                }
                if ((event.getExecutions() % 2) == 0 && event.getExecutions() <= 8) {
                    player.setAnimation(3170);
                    player.applyHit(new Hit(10));
                }
                if (event.getExecutions() == 0 || (event.getExecutions() % 25) == 0) {
                    var skillIds = [ Skills.ATTACK, Skills.DEFENCE, Skills.STRENGTH, Skills.RANGED, Skills.MAGIC ];
                    for each (skillId in skillIds) {
                        player.getSkills().changeStat(skillId, (player.getController().getLevelForXP(skillId)
                                * 0.16 + 6)|0, true, 0);
                    }
                }
            }
        };
        player.getSkills().setOverload(event);
    }
};
DRINKS.add(OVERLOAD_PLUS);
var PRAYER_ENHANCE_MINUS = new Skills.Drink(20964, 20963, 20962, 20961, null);
PRAYER_ENHANCE_MINUS.action = new Skills.ItemAction() {
    execute: function(player) {
        var event = new Event(0) {
            execute: function() {
                if (!player.isVisible() || player.isDead()) {
                    event.stop();
                    return;
                }
                if (event.getExecutions() == 433) {
                    player.getGameEncoder().sendMessage("<col=ff0000>Your prayer enhance effect has almost worn off!</col>");
                }
                if (event.getExecutions() == 458) {
                    event.stop();
                    if (event.getExecutions() == 458) {
                        player.getGameEncoder().sendMessage("<col=ff0000>Your prayer enhance effect has worn off.</col>");
                    }
                } else if ((event.getExecutions() % 15) == 0) {
                    player.getPrayer().adjustPoints(1);
                }
            }
        };
        player.getSkills().setPrayerEnhance(event);
    }
};
DRINKS.add(PRAYER_ENHANCE_MINUS);
var PRAYER_ENHANCE = new Skills.Drink(20968, 20967, 20966, 20965, null);
PRAYER_ENHANCE.action = new Skills.ItemAction() {
    execute: function(player) {
        var event = new Event(0) {
            execute: function() {
                if (!player.isVisible() || player.isDead()) {
                    event.stop();
                    return;
                }
                if (event.getExecutions() == 433) {
                    player.getGameEncoder().sendMessage("<col=ff0000>Your prayer enhance effect has almost worn off!</col>");
                }
                if (event.getExecutions() == 458) {
                    event.stop();
                    if (event.getExecutions() == 458) {
                        player.getGameEncoder().sendMessage("<col=ff0000>Your prayer enhance effect has worn off.</col>");
                    }
                } else if ((event.getExecutions() % 10) == 0) {
                    player.getPrayer().adjustPoints(1);
                }
            }
        };
        player.getSkills().setPrayerEnhance(event);
    }
};
DRINKS.add(PRAYER_ENHANCE);
var PRAYER_ENHANCE_PLUS = new Skills.Drink(20972, 20971, 20970, 20969, null);
PRAYER_ENHANCE_PLUS.action = new Skills.ItemAction() {
    execute: function(player) {
        var event = new Event(0) {
            execute: function() {
                if (!player.isVisible() || player.isDead()) {
                    event.stop();
                    return;
                }
                if (event.getExecutions() == 433) {
                    player.getGameEncoder().sendMessage("<col=ff0000>Your prayer enhance effect has almost worn off!</col>");
                }
                if (event.getExecutions() == 458) {
                    event.stop();
                    if (event.getExecutions() == 458) {
                        player.getGameEncoder().sendMessage("<col=ff0000>Your prayer enhance effect has worn off.</col>");
                    }
                } else if ((event.getExecutions() % 5) == 0) {
                    player.getPrayer().adjustPoints(1);
                }
            }
        };
        player.getSkills().setPrayerEnhance(event);
    }
};
DRINKS.add(PRAYER_ENHANCE_PLUS);
for each (var drink in DRINKS) {
    drink.empty = -1;
}

var FOOD = new ArrayList();
FOOD.add(new Skills.Food(20856, 5)); // Pysk
FOOD.add(new Skills.Food(20858, 8)); // Suphi
FOOD.add(new Skills.Food(20860, 11)); // Leckish
FOOD.add(new Skills.Food(20862, 14)); // Brawk
FOOD.add(new Skills.Food(20864, 17)); // Mycil
FOOD.add(new Skills.Food(20866, 20)); // Roqed
FOOD.add(new Skills.Food(20868, 23)); // Kyren
FOOD.add(new Skills.Food(20870, 5)); // Guanic
FOOD.add(new Skills.Food(20873, 8)); // Prael
FOOD.add(new Skills.Food(20875, 11)); // Giral
FOOD.add(new Skills.Food(20877, 14)); // Phluxia
FOOD.add(new Skills.Food(20879, 17)); // Kryket
FOOD.add(new Skills.Food(20881, 20)); // Murng
FOOD.add(new Skills.Food(20883, 23)); // Psykk

var BOOKS = [ 20886, 20888, 20890, 20893, 20895, 20897, 20899 ];
var GAME_ITEMS = [];
GAME_ITEMS.push(ItemId.KINDLING_20799);
GAME_ITEMS.push(ItemId.EMPTY_GOURD_VIAL);
GAME_ITEMS.push(ItemId.WATER_FILLED_GOURD_VIAL);
for (var id = 20853; id <= 20996; id++) {
    if (!BOOKS.includes(id)) {
        GAME_ITEMS.push(id);
    }
}
for (var id = 21036; id <= 21042; id++) {
    if (!BOOKS.includes(id)) {
        GAME_ITEMS.push(id);
    }
}

var player;
var totalPoints = 0;
var points = 0;
var damageInflicted = 0;
var time = 0;
var timerStarted = 0;
var spawnRoom = null;
var myRoom = null;
var rooms = new ArrayList();
var sharedStorage = null;
var rewards = null;
var giveRewards = true;

pc = new PController() {
    /* @Override */
    setEntityHook: function(entity) {
        player = entity;
        if (player == null) {
            return;
        }
        player.setLargeVisibility();
        player.getCombat().setDamageInflicted(0);
        player.getGameEncoder().setVarp(1430, 0);
        player.getGameEncoder().setVarp(1431, -2147483648);
        player.getGameEncoder().setVarp(1432, 0);
        player.getWidgetManager().sendOverlay(WidgetId.CHAMBERS_OF_XERIC_OVERLAY);
        pc.setKeepItemsOnDeath(true);
        pc.setMultiCombatFlag(true);
        pc.setBlockTeleport(true);
        pc.setExitTile(new Tile(1233, 3568));
        sharedStorage = new ItemList(250).setPlayer(player).setName("shared storage").setWidget(-70001, 582)
                .setAlwaysStack(true);
    },

    /* @Override */
    stopHook: function() {
        if (player != null) {
            player.getGameEncoder().setVarp(1431, 0);
            for each (var itemId in GAME_ITEMS) {
                player.getInventory().deleteAll(itemId);
                player.getEquipment().deleteAll(itemId);
            }
            player.getWidgetManager().removeInteractiveWidgets();
            player.getWidgetManager().removeOverlay();
            player.restore();
            player = null;
        }
    },

    /* @Override */
    getVariableHook: function(name) {
        if (name.equals("raid_instance")) {
            return true;
        } else if (name.equals("is_empty")) {
            if (pc.getPlayer() == null) {
                pc.clearUpdates();
            }
            var isEmpty = pc.getPlayers().isEmpty();
            if (isEmpty) {
                for each (var room in rooms) {
                    if (room.npcs != null && !room.npcs.isEmpty()) {
                        pc.getWorld().removeNpcs(room.npcs);
                    }
                }
            }
            return isEmpty;
        } else if (name.equals("is_loaded")) {
            return !rooms.isEmpty();
        } else if (name.equals("spawn_room")) {
            return spawnRoom;
        } else if (name.equals("rooms")) {
            return rooms;
        } else if (name.equals("points")) {
            return points;
        } else if (name.equals("time")) {
            return time;
        } else if (name.equals("timer_started")) {
            if (timerStarted > 0) {
                timerStarted++;
            }
            return timerStarted == 2;
        } else if (name.equals("shared_storage")) {
            return sharedStorage;
        } else if (name.equals("decide_rewards")) {
            this.decideRewards();
            return true;
        } else if (name.equals("rewards")) {
            if (rewards == null) {
                rewards = new ItemList(4).setPlayer(player).setName("rewards").setWidget(-70000, 581)
                        .setAlwaysStack(true);
            }
            return rewards;
        } else if (name.equals("spawn_coords")) {
            if (spawnRoom == null) {
                spawnRoom = myRoom = FIRST_ROOM;
                pc.setRespawnTile(spawnRoom.entrancePassageCoords);
            }
            return spawnRoom.entrancePassageCoords;
        }
        return null;
    },

    /* @Override */
    setVariableHook: function(name, object) {
        if (name.equals("timer_started")) {
            timerStarted = object;
        } else if (name.equals("load") || name.equals("load_tob")) {
            rooms.add(spawnRoom);
            if (name.equals("load_tob")) {
                giveRewards = false;
                rooms.add(ROOM_MAIDEN_SUGADINTI);
                rooms.add(ROOM_PESTILENT_BLOAT);
                rooms.add(ROOM_XARPUS);
            } else if (object) {
                rooms.add(NORMAL_ROOMS.get(0));
                rooms.add(BOSS_ROOMS.get(0));
                rooms.add(BOSS_ROOMS.get(1));
                rooms.add(NORMAL_ROOMS.get(1));
                rooms.add(BOSS_ROOMS.get(2));
                rooms.add(BOSS_ROOMS.get(3));
            } else {
                rooms.add(NORMAL_ROOMS.get(0));
                rooms.add(BOSS_ROOMS.get(0));
                rooms.add(BOSS_ROOMS.get(1));
            }
            var resourceOrder = PRandom.randomI(1) == 0;
            if (!name.equals("load_tob")) {
                rooms.add(2, resourceOrder ? RESOURCE_ROOM_1 : RESOURCE_ROOM_2);
                rooms.add(resourceOrder ? RESOURCE_ROOM_2 : RESOURCE_ROOM_1);
                rooms.add(PRE_OLM_ROOM);
                rooms.add(OLM_ROOM);
            }
            var names = "";
            for each (var room in rooms) {
                if (room.name.length > 0) {
                    names += room.name + ", ";
                }
            }
            names = names.substring(0, names.length - 2);
            var players = player.getController().getPlayers();
            var playerCount = players.size();
            for each (var player2 in players) {
                player2.getGameEncoder().sendMessage("<col=ef20ff>The raid has begun!</col>",
                    Messaging.CHAT_TYPE_CLAN_CHAT_INFO);
                player2.getGameEncoder().sendMessage("<col=ef20ff>Room layout: " + names + "</col>",
                    Messaging.CHAT_TYPE_CLAN_CHAT_INFO);
                player2.getGameEncoder().setVarp(1430, playerCount * 2);
                player2.getController().setVariable("timer_started", 1);
                player2.getOptions().setVarp1429();
                if (name.equals("load_tob")) {
                    player.getWidgetManager().sendOverlay(WidgetId.TZKAL_ZUK);
                    player.getGameEncoder().sendHideWidget(WidgetId.TZKAL_ZUK, 2, true);
                    player.getGameEncoder().sendHideWidget(WidgetId.TZKAL_ZUK, 5, true);
                    player.getGameEncoder().setVarp(1575, 0 + (1 * 2048));
                }
            }
            pc.addMapObject(new MapObject(29780, new Tile(3312, 5463), 10, 3));
            pc.addMapObject(new MapObject(29780, new Tile(3317, 5456, 1), 10, 0));
            pc.addMapObject(new MapObject(29780, new Tile(3308, 5164), 10, 1));
            for each (var room in rooms) {
                if (room.customEntranceObject != null) {
                    pc.addMapObject(room.customEntranceObject);
                }
                if (room.customExitObject != null) {
                    pc.addMapObject(room.customExitObject);
                }
                for each (mapObject in room.customMapObjects) {
                    pc.addMapObject(mapObject);
                }
                if (room.npcRefs != null) {
                    room.npcs = new ArrayList();
                    for each (var npcRef in room.npcRefs) {
                        var npc = new Npc(pc, npcRef.id, npcRef.tile);
                        npc.setMoveDistance(npcRef.moveDistance);
                        room.npcs.add(npc);
                    }
                }
                if (playerCount >= 2 && room.npcRefs2Players != null) {
                    for each (var npcRef in room.npcRefs2Players) {
                        var npc = new Npc(pc, npcRef.id, npcRef.tile);
                        npc.setMoveDistance(npcRef.moveDistance);
                        room.npcs.add(npc);
                    }
                }
                if (playerCount >= 3 && room.npcRefs3Players != null) {
                    for each (var npcRef in room.npcRefs3Players) {
                        var npc = new Npc(pc, npcRef.id, npcRef.tile);
                        npc.setMoveDistance(npcRef.moveDistance);
                        room.npcs.add(npc);
                    }
                }
                if (playerCount >= 4 && room.npcRefs4Players != null) {
                    for each (var npcRef in room.npcRefs4Players) {
                        var npc = new Npc(pc, npcRef.id, npcRef.tile);
                        npc.setMoveDistance(npcRef.moveDistance);
                        room.npcs.add(npc);
                    }
                }
                if (playerCount >= 5 && room.npcRefs5Players != null) {
                    for each (var npcRef in room.npcRefs5Players) {
                        var npc = new Npc(pc, npcRef.id, npcRef.tile);
                        npc.setMoveDistance(npcRef.moveDistance);
                        room.npcs.add(npc);
                    }
                }
                if (playerCount >= 6 && room.npcRefs6Players != null) {
                    for each (var npcRef in room.npcRefs6Players) {
                        var npc = new Npc(pc, npcRef.id, npcRef.tile);
                        npc.setMoveDistance(npcRef.moveDistance);
                        room.npcs.add(npc);
                    }
                }
                if (room.npcs != null) {
                    for each (var npc in room.npcs) {
                        if (npc.getId() >= 7546 && npc.getId() <= 7549) {
                            npc.setRespawns(true);
                        }
                        npc.getController().setMultiCombatFlag(true);
                    }
                }
            }
        }
    },

    /* @Override */
    tickHook: function() {
        if (player != null && player.getIndex() == 0) {
            setEntity(null);
        }
        if (!rooms.isEmpty()) {
            time++;
        }
        if (player == null) {
            return;
        }
        if (damageInflicted != player.getCombat().getDamageInflicted()) {
            if (player.getAttackingEntity() instanceof Npc && !player.getAttackingEntity().getRespawns()) {
                points += (player.getCombat().getDamageInflicted() - damageInflicted) * 5;
                points = Math.min(points, 131071);
                player.getGameEncoder().setVarp(1432, points * 1024);
            }
            damageInflicted = player.getCombat().getDamageInflicted();
        }
        var tempTotalPoints = 0;
        for each (var player2 in pc.getPlayers()) {
            tempTotalPoints += player2.getController().getVariable("points");
        }
        if (totalPoints != tempTotalPoints) {
            totalPoints = tempTotalPoints;
            player.getGameEncoder().setVarp(1431, totalPoints + -2147483648);
        }
    },

    /* @Override */
    setInstanceHook: function(instance) {
        if (instance.getVariable("raid_instance")) {
            spawnRoom = myRoom = instance.getVariable("spawn_room");
            rooms = instance.getVariable("rooms");
            sharedStorage = instance.getVariable("shared_storage");
            pc.setRespawnTile(spawnRoom.entrancePassageCoords);
        }
    },

    /* @Override */
    applyDeadHook: function() {
        if (player.getRespawnDelay() == -1) {
            var pointsLost = (points * 0.4)|0
            points -= pointsLost;
            player.getGameEncoder().setVarp(1432, points * 1024);
            totalPoints -= pointsLost;
            player.getGameEncoder().setVarp(1431, totalPoints + -2147483648);
        }
    },

    /* @Override */
    getFoodHook: function(itemId, food) {
        for each (var food2 in FOOD) {
            if (food2.itemId == itemId) {
                return food2;
            }
        }
        return food;
    },

    /* @Override */
    getDrinkHook: function(itemId, drink) {
        if (itemId >= 20985 && itemId <= 20996) {
            if (player.getSkills().getOverload() != null) {
                player.getGameEncoder().sendMessage("You are still suffering the effects of a fresh dose of overload.");
                return null;
            } else if (player.getHitpoints() < 50) {
                player.getGameEncoder().sendMessage("You don't have enough hitpoints to drink this.");
                return null;
            }
        }
        for each (var drink2 in DRINKS) {
            if (drink2.isDose(itemId)) {
                return drink2;
            }
        }
        return drink;
    },

    /* @Override */
    widgetHook: function(index, widgetId, childId, slot, itemId) {
        sharedStorage.setPlayer(player);
        switch (widgetId) {
        case WidgetId.SHARED_STORAGE:
            switch (childId) {
            case 7:
                var item = sharedStorage.getItem(sharedStorage.getSlotById(itemId));
                if (item == null || item.getId() != itemId) {
                    break;
                }
                if (index == 0) {
                    var resp = player.getInventory().addItem(item.getId(), Math.min(1, item.getAmount()));
                    sharedStorage.deleteItem(item.getId(), resp.successAmount);
                } else if (index == 1) {
                    var resp = player.getInventory().addItem(item.getId(), Math.min(5, item.getAmount()));
                    sharedStorage.deleteItem(item.getId(), resp.successAmount);
                } else if (index == 2) {
                    var resp = player.getInventory().addItem(item.getId(), Math.min(10, item.getAmount()));
                    sharedStorage.deleteItem(item.getId(), resp.successAmount);
                } else if (index == 3) {
                    var resp = player.getInventory().addItem(item.getId(), item.getAmount());
                    sharedStorage.deleteItem(item.getId(), resp.successAmount);
                } else if (index == 4) {
                    var js = this;
                    player.getGameEncoder().sendEnterAmount(new ValueEnteredEvent.IntegerEvent() {
                        execute: function(value) {
                            var resp = player.getInventory().addItem(item.getId(), Math.min(value, item.getAmount()));
                            sharedStorage.deleteItem(item.getId(), resp.successAmount);
                            js.sendSharedStoredItems();
                        }
                    });
                }
                break;
            }
            this.sendSharedStoredItems();
            return true;
        case WidgetId.SHARED_STORAGE_INVENTORY:
            switch (childId) {
            case 8:
                player.putAttribute("raids_storage_warning", true);
                player.getOptions().setVarp115();
                break;
            case 1:
                var item = player.getInventory().getItem(slot);
                if (item == null || item.getId() != itemId) {
                    break;
                } else if (GAME_ITEMS.indexOf(item.getId()) == -1) {
                    player.getGameEncoder().sendMessage("You can't store this item.");
                    break;
                }
                var itemAmount = player.getInventory().getCount(item.getId());
                if (index == 0) {
                    var resp = sharedStorage.addItem(item.getId(), Math.min(1, itemAmount));
                    player.getInventory().deleteItem(item.getId(), resp.successAmount);
                } else if (index == 1) {
                    var resp = sharedStorage.addItem(item.getId(), Math.min(5, itemAmount));
                    player.getInventory().deleteItem(item.getId(), resp.successAmount);
                } else if (index == 2) {
                    var resp = sharedStorage.addItem(item.getId(), Math.min(10, itemAmount));
                    player.getInventory().deleteItem(item.getId(), resp.successAmount);
                } else if (index == 3) {
                    var resp = sharedStorage.addItem(item.getId(), itemAmount);
                    player.getInventory().deleteItem(item.getId(), resp.successAmount);
                } else if (index == 4) {
                    var js = this;
                    player.getGameEncoder().sendEnterAmount(new ValueEnteredEvent.IntegerEvent() {
                        execute: function(value) {
                            var itemAmount = player.getInventory().getCount(item.getId());
                            var resp = sharedStorage.addItem(item.getId(), Math.min(value, itemAmount));
                            player.getInventory().deleteItem(item.getId(), resp.successAmount);
                            js.sendSharedStoredItems();
                        }
                    });
                }
                break;
            }
            this.sendSharedStoredItems();
            return true;
        case WidgetId.CHAMBERS_OF_XERIC_REWARDS:
            switch (childId) {
            case 5:
                var item = rewards.getItem(slot);
                if (item == null || item.getId() != itemId) {
                    break;
                }
                var resp = player.getInventory().addItem(item);
                rewards.deleteItem(item.getId(), resp.successAmount);
                break;
            }
            rewards.sendItems();
            return true;
        }
        return false;
    },

    /* @Override */
    mapObjectOptionHook: function(index, mapObject) {
        switch (mapObject.getId()) {
        case 30066: // Energy well
            player.setGraphic(436);
            player.getGameEncoder().sendMessage("The pool restores you.");
            player.rejuvenate();
            player.getCombat().setSpecialAttackAmount(PCombat.MAX_SPECIAL_ATTACK);
            break;
        case 29769: // Storage unit
        case 29770: // Small storage unit
        case 29779: // Medium storage unit
        case 29780: // Large storage unit
            sharedStorage.setPlayer(player);
            player.getWidgetManager().sendInteractiveOverlay(WidgetId.SHARED_STORAGE);
            player.getGameEncoder().sendWidgetText(WidgetId.SHARED_STORAGE, 4, sharedStorage.size() + "");
            player.getGameEncoder().sendWidgetSettings(WidgetId.SHARED_STORAGE, 7, 0, 250, 1054);
            player.getWidgetManager().sendInventoryOverlay(WidgetId.SHARED_STORAGE_INVENTORY);
            player.getGameEncoder().sendWidgetSettings(WidgetId.SHARED_STORAGE_INVENTORY, 1, 0, 27, 1086);
            player.getInventory().setUpdate(true);
            sharedStorage.sendItems();
            return true;
        case 29889: // Bubbles
            var event = new Event(1) {
                execute: function() {
                    if (!player.isVisible()) {
                        event.stop();
                        return;
                    }
                    if (!player.getInventory().hasItem(307)) {
                        player.getGameEncoder().sendMessage("You need a fishing rod for this.");
                        event.stop();
                        return;
                    } else if (!player.getInventory().hasItem(20853)) {
                        player.getGameEncoder().sendMessage("You need cave worms for this.");
                        event.stop();
                        return;
                    } else if (player.getInventory().isFull()) {
                        event.stop();
                        return;
                    }
                    player.setAnimation(1363);
                    if (event.getTick() == 1) {
                        event.setTick(5);
                        return;
                    }
                    player.getInventory().deleteItem(20853, 1);
                    if (player.getSkills().getLevel(Skills.FISHING) >= 90) {
                        player.getInventory().addItem(20867, 1);
                    } else if (player.getSkills().getLevel(Skills.FISHING) >= 75) {
                        player.getInventory().addItem(20865, 1);
                    } else if (player.getSkills().getLevel(Skills.FISHING) >= 60) {
                        player.getInventory().addItem(20863, 1);
                    } else if (player.getSkills().getLevel(Skills.FISHING) >= 45) {
                        player.getInventory().addItem(20861, 1);
                    } else if (player.getSkills().getLevel(Skills.FISHING) >= 30) {
                        player.getInventory().addItem(20859, 1);
                    } else if (player.getSkills().getLevel(Skills.FISHING) >= 15) {
                        player.getInventory().addItem(20857, 1);
                    } else {
                        player.getInventory().addItem(20855, 1);
                    }
                }
            };
            player.setAction(event);
            return true;
        case 29773: // Weeds
            var event = new Event(1) {
                execute: function() {
                    if (!player.isVisible()) {
                        event.stop();
                        return;
                    }
                    if (!player.getInventory().hasItem(5341)) {
                        player.getGameEncoder().sendMessage("You need a rake for this.");
                        event.stop();
                        return;
                    } else if (player.getInventory().isFull()) {
                        event.stop();
                        return;
                    }
                    player.setAnimation(2273);
                    if (event.getTick() == 1) {
                        event.setTick(5);
                        return;
                    }
                    var seed = PRandom.randomE(3);
                    if (seed == 0) {
                        player.getInventory().addItem(20903, 1 + PRandom.randomI(2));
                    } else if (seed == 1) {
                        player.getInventory().addItem(20906, 1 + PRandom.randomI(2));
                    } else if (seed == 2) {
                        player.getInventory().addItem(20909, 1 + PRandom.randomI(2));
                    }
                }
            };
            player.setAction(event);
            return true;
        case 29765: // Herb patch
            player.getGameEncoder().sendMessage("Maybe I can plant something here.");
            return true;
        case 29771: // Old tools
            if (!player.getInventory().hasItem(5343)) {
                player.getInventory().addItem(5343, 1);
            }
            if (!player.getInventory().hasItem(5341)) {
                player.getInventory().addItem(5341, 1);
            }
            if (!player.getInventory().hasItem(952)) {
                player.getInventory().addItem(952, 1);
            }
            return true;
        case 29772: // Gourd tree
            player.getInventory().addItem(20800, index == 0 ? 1 : player.getInventory().getRemainingSlots());
            player.getGameEncoder().sendMessage("You pick some gourd fruits from the tree, tearing the tops off in the process.");
            player.setAnimation(2280);
            return true;
        case 29789: // Passage
        case 29735: // Hole
        case 29996: // Rope
        case 29778: // Steps
        case 32751: // Door
        case 2156: // Magic portal
        case 33113: // Formidable passage
            if (mapObject.getId() == 29778 && (mapObject.getX() == 3298 && mapObject.getY() == 5185
                    || mapObject.getX() == 3231 && mapObject.getY() == 5755)) {
                // Exit spawn room
                pc.stop();
                return true;
            }
            if (rooms.isEmpty()) {
                player.openDialogue("raids", 1);
                return true;
            }
            var forceForward = mapObject.getId() == 33113;
            var forceBackward = mapObject.getId() == 29996 && mapObject.getDirection() == 2
                    || mapObject.getId() == 2156;
            for (var i = 0; i < rooms.size(); i++) {
                var room = rooms.get(i);
                if (i - 1 >= 0 && (player.getY() > mapObject.getY() || forceBackward) && !forceForward
                        && room.entrancePassageCoords.withinDistance(mapObject, 4)) {
                    var newRoom = rooms.get(i - 1);
                    if (rewards == null) {
                        player.getMovement().teleport(newRoom.exitPassageCoords);
                        myRoom = newRoom;
                    }
                    return true;
                } else if ((i + 1 < rooms.size() && player.getY() < mapObject.getY() || forceForward)
                        && !forceBackward && room.exitPassageCoords.withinDistance(mapObject, 4)) {
                    if (!room.isComplete()) {
                        player.getGameEncoder().sendMessage("You need to complete this room first.");
                        return true;
                    }
                    var newRoom = rooms.get(i + 1);
                    player.getMovement().teleport(newRoom.entrancePassageCoords);
                    myRoom = newRoom;
                    if (i > 0) {
                        pc.setRespawnTile(new Tile(rooms.get(i).exitPassageCoords));
                    }
                    return true;
                }
            }
            return true;
        case 29879: // Mystical barrier
            player.getMovement().clear();
            if (player.getY() < 5730) {
                player.getMovement().addMovement(player.getX(), player.getY() + 2);
                player.getGameEncoder().sendMessage("As you pass through the barrier, a sense of dread washes over you.");
            }
            return true;
        case 29767: // Noxious tendrils
        case 29768: // Spirit tendrils
        case 30019: // Magical fire
        case 30021: // Fire
        case 32755: // Barrier
            if (mapObject.getSizeX() == 1 && player.getX() != mapObject.getX() && player.getY() != mapObject.getY()) {
                return true;
            } else if (mapObject.getSizeX() == 2 && player.getX() != mapObject.getX()
                    && player.getY() != mapObject.getY() && player.getX() + 1 != mapObject.getX()
                    && player.getY() + 1 != mapObject.getY()) {
                return true;
            } else if (player.getInCombatDelay() > 0) {
                player.getGameEncoder().sendMessage("You can't cross this while in combat.");
                return true;
            }
            if (mapObject.getId() == 32755) {
                player.getGameEncoder().sendHideWidget(WidgetId.TZKAL_ZUK, 2, myRoom.isComplete());
                player.getGameEncoder().sendHideWidget(WidgetId.TZKAL_ZUK, 5, myRoom.isComplete());
            }
            var distance = mapObject.getSizeX() + 1;
            player.getMovement().clear();
            if (player.getY() < mapObject.getY()) {
                player.getMovement().addMovement(player.getX(), player.getY() + distance);
            } else if (player.getY() > mapObject.getY()) {
                player.getMovement().addMovement(player.getX(), player.getY() - distance);
            } else if (player.getX() < mapObject.getX()) {
                player.getMovement().addMovement(player.getX() + distance, player.getY());
            } else if (player.getX() > mapObject.getX()) {
                player.getMovement().addMovement(player.getX() - distance, player.getY());
            }
            return true;
        case 30028: // ancient chest
            if (rewards == null || rewards.isEmpty()) {
                return true;
            }
            player.getWidgetManager().sendInteractiveOverlay(WidgetId.CHAMBERS_OF_XERIC_REWARDS);
            player.getGameEncoder().sendWidgetSettings(WidgetId.CHAMBERS_OF_XERIC_REWARDS, 5, 0, 4, 1054);
            rewards.sendItems();
            return true;
        }
        return false;
    },

    /* @Override */
    widgetOnMapObjectHook: function(widgetId, childId, slot, mapObject) {
        if (widgetId != WidgetId.INVENTORY) {
            return false;
        }
        var itemId = player.getInventory().getId(slot);
        switch (mapObject.getId()) {
        case 29878: // Geyser
            if (itemId == 20800) { // Empty gourd vial
                var event = new Event(0) {
                    execute: function() {
                        if (!player.isVisible() || !player.getInventory().hasItem(itemId)) {
                            event.stop();
                            return;
                        }
                        player.setAnimation(832);
                        player.getInventory().deleteItem(itemId, 1);
                        player.getInventory().addItem(20801, 1);
                    }
                };
                player.setAction(event);
            }
            return true;
        case 29765: // Herb patch
            if (itemId == 20903 || itemId == 20906 || itemId == 20909) { // Golpar/Buchu/Noxifer
                if (!player.getInventory().hasItem(5343)) {
                    player.getGameEncoder().sendMessage("You need a seed dibber to do this.");
                    return;
                }
                player.getInventory().deleteItem(itemId, 1);
                var event = new Event(0) {
                    execute: function() {
                        if (!player.isVisible()) {
                            event.stop();
                            return;
                        }
                        player.setAnimation(2291);
                        player.getInventory().addOrDropItem(itemId - 2, 1);
                        if (PRandom.randomE(6) == 0) {
                            event.stop();
                        }
                    }
                };
                player.setAction(event);
            }
            return true;
        }
        return false;
    },

    /* @Override */
    addMapItemHook: function(mapItem) {
        if (GAME_ITEMS.includes(mapItem.getId())) {
            mapItem.setAlwaysAppear();
            mapItem.setGameMode(-1);
        }
        return mapItem;
    },

    decideRewards: function() {
        if (!giveRewards) {
            return;
        }
        var players = pc.getPlayers();
        var playerEntries = new ArrayList();
        for each (var player2 in players) {
            player2.getGameEncoder().sendMessage("<col=ef20ff>Congratulations - your raid is complete! Duration:</col> "
                    + "<col=ff0000>" + Time.ticksToDuration(time) + "</col>", Messaging.CHAT_TYPE_CLAN_CHAT_INFO);
            for (var i = player2.getController().getVariable("points"); i >= 0; i -= 8675) {
                playerEntries.add(player2);
            }
            player2.getGameEncoder().setVarp(1432, 128 + points * 1024);
            player2.getCombat().setSpecialAttackAmount(PCombat.MAX_SPECIAL_ATTACK);
        }
        var sentSpecialLootMessage = false;
        var totalUniques = 0;
        var playersRewarded = [];
        for (var i = totalPoints; i > 0 && totalUniques < 3; i -= 570000) {
            var selectedPlayer = PRandom.listRandom(playerEntries);
            var percent = selectedPlayer.getCombat().getForcedDropRate(-1, Math.min(i / 4338, 65),
                    NpcDef.getNpcDef(7554));
            selectedPlayer.getCharges().depleteRoWICharge(0);
            if (PRandom.inRange(percent)) {
                playersRewarded.push(selectedPlayer.getId());
                playerEntries.remove(selectedPlayer);
                totalUniques++;
                var item = RandomItem.getItem(UNIQUE_REWARDS);
                selectedPlayer.getController().getVariable("rewards").addItem(item);
                selectedPlayer.getCombat().logNPCItem("Chambers of Xeric", item.getId(), item.getAmount());
                for each (var player2 in players) {
                    if (!sentSpecialLootMessage) {
                        player2.getGameEncoder().sendMessage("<col=ef20ff>Special loot:</col>",
                                Messaging.CHAT_TYPE_CLAN_CHAT_INFO);
                    }
                    player2.getGameEncoder().sendMessage("<col=ef20ff>" + selectedPlayer.getUsername()
                            + "</col> - <col=ff0000>" + item.getName() + "</col>", Messaging.CHAT_TYPE_CLAN_CHAT_INFO);
                }
                sentSpecialLootMessage = true;
                player.getWorld().sendItemDropNews(selectedPlayer, item.getId(), "the Chambers of Xeric");
                RequestManager.addPlayerLog(selectedPlayer, "raids",selectedPlayer.getLogName() + " received "
                        + item.getLogName() + " from the Chambers of Xeric.");
                var petItem = new Item(20851, 1);
                if (PRandom.inRange(selectedPlayer.getCombat().getDropRate(petItem.getId(), 0.5))) {
                    selectedPlayer.getInventory().addOrDropItem(petItem);
                    selectedPlayer.getCombat().logNPCItem("Chambers of Xeric", petItem.getId(), petItem.getAmount());
                    player.getWorld().sendItemDropNews(selectedPlayer, petItem.getId(), "the Chambers of Xeric");
                }
                selectedPlayer.getGameEncoder().setVarp(1432, 256 + points * 1024);
            }
        }
        for each (var player2 in players) {
            if (playersRewarded.indexOf(player2.getId()) != -1) {
                continue;
            }
            var amountDivider = 131071 / points;
            for (var i = 0; i < 2; i++) {
                var item = RandomItem.getItem(REWARDS);
                var amount = Math.min((item.getAmount() / amountDivider)|0, item.getAmount());
                if (amount == 0) {
                    amount = 1;
                }
                item = new Item(item.getId(), amount);
                player2.getController().getVariable("rewards").addItem(item);
            }
        }
    },

    sendSharedStoredItems: function() {
        for each (var player2 in pc.getPlayers()) {
            sharedStorage.setPlayer(player2);
            sharedStorage.sendItems();
        }
    }
}
