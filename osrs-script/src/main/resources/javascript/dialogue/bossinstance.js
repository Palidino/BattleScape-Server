var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Select an Option";
lines.add("Cancel Instance");
actions.add("close|script");
lines.add("Nevermind");
actions.add("close");
var obj0 = new DialogueEntry();
entries.add(obj0);
obj0.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Select an Option";
lines.add("Create Instance");
actions.add("close|script");
lines.add("Join Instance");
actions.add("close|script");
var obj1 = new DialogueEntry();
entries.add(obj1);
obj1.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Select an Option";
lines.add("Enter Area");
actions.add("close|script");
lines.add("Create Instance");
actions.add("close|script");
lines.add("Join Instance");
actions.add("close|script");
var obj2 = new DialogueEntry();
entries.add(obj2);
obj2.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Select an Option";
lines.add("Create Instance");
actions.add("close|script");
lines.add("Join Instance");
actions.add("close|script");
var obj3 = new DialogueEntry();
entries.add(obj3);
obj3.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Select an Option";
lines.add("Enter Area");
actions.add("close|script");
lines.add("Create Instance");
actions.add("close|script");
lines.add("Join Instance");
actions.add("close|script");
var obj4 = new DialogueEntry();
entries.add(obj4);
obj4.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Select an Option";
lines.add("Enter Area");
actions.add("close|script");
lines.add("Create Instance");
actions.add("close|script");
lines.add("Join Instance");
actions.add("close|script");
var obj5 = new DialogueEntry();
entries.add(obj5);
obj5.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Select an Option";
lines.add("Enter Area");
actions.add("close|script");
lines.add("Create Instance");
actions.add("close|script");
lines.add("Join Instance");
actions.add("close|script");
var obj6 = new DialogueEntry();
entries.add(obj6);
obj6.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Select an Option";
lines.add("Enter Area");
actions.add("close|script");
lines.add("Create Instance");
actions.add("close|script");
lines.add("Join Instance");
actions.add("close|script");
var obj7 = new DialogueEntry();
entries.add(obj7);
obj7.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Select an Option";
lines.add("Enter Area");
actions.add("close|script");
lines.add("Create Instance");
actions.add("close|script");
lines.add("Join Instance");
actions.add("close|script");
var obj8 = new DialogueEntry();
entries.add(obj8);
obj8.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Select an Option";
lines.add("Enter Area");
actions.add("close|script");
lines.add("Create Instance");
actions.add("close|script");
lines.add("Join Instance");
actions.add("close|script");
var obj9 = new DialogueEntry();
entries.add(obj9);
obj9.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Select an Option";
lines.add("Enter Area");
actions.add("close|script");
lines.add("Create Instance");
actions.add("close|script");
lines.add("Join Instance");
actions.add("close|script");
var obj10 = new DialogueEntry();
entries.add(obj10);
obj10.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Select an Option";
lines.add("Create Instance");
actions.add("close|script");
lines.add("Join Instance");
actions.add("close|script");
var obj11 = new DialogueEntry();
entries.add(obj11);
obj11.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Select an Option";
lines.add("Enter Area");
actions.add("close|script");
lines.add("Create Instance");
actions.add("close|script");
lines.add("Join Instance");
actions.add("close|script");
var obj12 = new DialogueEntry();
entries.add(obj12);
obj12.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Select an Option";
lines.add("Enter Area");
actions.add("close|script");
lines.add("Create Instance");
actions.add("close|script");
lines.add("Join Instance");
actions.add("close|script");
var obj13 = new DialogueEntry();
entries.add(obj13);
obj13.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

title = "Select an Option";
lines.add("Enter Area");
actions.add("close|script");
lines.add("Create Instance");
actions.add("close|script");
lines.add("Join Instance");
actions.add("close|script");
var obj14 = new DialogueEntry();
entries.add(obj14);
obj14.setSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

var NORMAL = 0;
var LADDER_DOWN = 1;
var LADDER_UP = 2;

instance = new DialogueScript() {
    execute: function(player, index, childId, slot) {
        if (player.isLocked()) {
            return;
        }
        if (index == 0) {
            var clanChatUsername = player.getMessaging().getClanChatUsername();
            var playerInstance = player.getWorld().getPlayerBossInstance(clanChatUsername, player.getController());
            if (playerInstance == null) {
                player.getGameEncoder().sendMessage("There is no boss instance for this Clan Chat.");
                return;
            } else if (!player.getMessaging().canClanChatEvent()) {
                player.getGameEncoder().sendMessage("Your Clan Chat privledges aren't high enough to do that.");
                return;
            }
            if (slot == 0) {
                playerInstance.getVariable("expire");
            }
            return;
        }
        var dialogueEntry = this.getDialogueEntries().get(index);
        var enterSlot = (dialogueEntry.getLines().length == 3) ? 0 : -1;
        var startSlot = (dialogueEntry.getLines().length == 3) ? 1 : 0;
        var joinSlot = (dialogueEntry.getLines().length == 3) ? 2 : 1;
        var clanChatUsername = player.getMessaging().getClanChatUsername();
        var playerInstance = player.getWorld().getPlayerBossInstance(clanChatUsername, player.getController());
        var bossName = "";
        var tile = null;
        var teleportType = NORMAL;
        var areaScript = null;
        var deleteItemId = -1;
        var slayerId = -1;
        if (index == 1) {
            bossName = "boss_instance_corporeal_beast";
            tile = new Tile(2974, 4384, 2);
        } else if (index == 2) {
            bossName = "boss_instance_dagannoth_kings";
            tile = new Tile(2900, 4449, 0);
            teleportType = LADDER_UP;
        } else if (index == 3) {
            bossName = "boss_instance_king_black_dragon";
            tile = new Tile(2271, 4680, 0);
        } else if (index == 4) {
            bossName = "boss_instance_cerberus";
            tile = new Tile(1304, 1291, 0);
            slayerId = 5862;
        } else if (index == 5) {
            bossName = "boss_instance_thermonuclear_smoke_devil";
            tile = new Tile(2376, 9452, 0);
            teleportType = LADDER_DOWN;
            slayerId = 499;
        } else if (index == 6) {
            bossName = "boss_instance_giant_mole";
            tile = new Tile(1752, 5236, 0);
            teleportType = LADDER_DOWN;
            if (player.getInventory().hasItem(13119) || player.getInventory().hasItem(13120)) {
                var event = new PEvent(10) {
                    execute: function() {
                        if (!player.isVisible()) {
                            event.stop();
                            return;
                        }
                        if (!player.inMoleLair()) {
                            event.stop();
                            player.getGameEncoder().sendHintIconReset();
                        } else {
                            var mole = player.getWorld().getNPC(5779, player);
                            if (mole == null || !mole.isVisible()) {
                                player.getGameEncoder().sendHintIconReset();
                            } else if (player.withinVisibilityDistance(mole)) {
                                player.getGameEncoder().sendHintIconNpc(mole.getIndex());
                            } else {
                                player.getGameEncoder().sendHintIconTile(mole);
                            }
                        }
                    }
                };
                player.getWorld().addEvent(event);
            }
        } else if (index == 7) {
            if (!Settings.getInstance().isSpawn() && (slot == enterSlot || slot == startSlot)) {
                var killcount = player.getArea().script("get_armadyl_killcount");
                if (killcount == null) {
                    player.getGameEncoder().sendMessage("There was an error establishing your killcount.");
                    return;
                } else if (killcount < 40 && !player.getInventory().hasItem(11942)) {
                    player.getGameEncoder().sendMessage("You need 40 killcount to enter.");
                    return;
                }
            }
            bossName = "boss_instance_kree'arra";
            tile = new Tile(2839, 5296, 2);
            areaScript = "clear_armadyl_killcount";
            deleteItemId = 11942;
        } else if (index == 8) {
            if (!Settings.getInstance().isSpawn() && (slot == enterSlot || slot == startSlot)) {
                var killcount = player.getArea().script("get_bandos_killcount");
                if (killcount == null) {
                    player.getGameEncoder().sendMessage("There was an error establishing your killcount.");
                    return;
                } else if (killcount < 40 && !player.getInventory().hasItem(11942)) {
                    player.getGameEncoder().sendMessage("You need 40 killcount to enter.");
                    return;
                }
            }
            bossName = "boss_instance_general_graardor";
            tile = new Tile(2864, 5354, 2);
            areaScript = "clear_bandos_killcount";
            deleteItemId = 11942;
        } else if (index == 9) {
            if (!Settings.getInstance().isSpawn() && (slot == enterSlot || slot == startSlot)) {
                var killcount = player.getArea().script("get_zamorak_killcount");
                if (killcount == null) {
                    player.getGameEncoder().sendMessage("There was an error establishing your killcount.");
                    return;
                } else if (killcount < 40 && !player.getInventory().hasItem(11942)) {
                    player.getGameEncoder().sendMessage("You need 40 killcount to enter.");
                    return;
                }
            }
            bossName = "boss_instance_k'ril_tsutsaroth";
            tile = new Tile(2925, 5331, 2);
            areaScript = "clear_zamorak_killcount";
            deleteItemId = 11942;
        } else if (index == 10) {
            if (!Settings.getInstance().isSpawn() && (slot == enterSlot || slot == startSlot)) {
                var killcount = player.getArea().script("get_saradomin_killcount");
                if (killcount == null) {
                    player.getGameEncoder().sendMessage("There was an error establishing your killcount.");
                    return;
                } else if (killcount < 40 && !player.getInventory().hasItem(11942)) {
                    player.getGameEncoder().sendMessage("You need 40 killcount to enter.");
                    return;
                }
            }
            bossName = "boss_instance_commander_zilyana";
            tile = new Tile(2907, 5265, 0);
            areaScript = "clear_saradomin_killcount";
            deleteItemId = 11942;
        } else if (index == 11) {
            bossName = "boss_instance_abyssal_sire";
            tile = new Tile(2983, 4820, 0);
            slayerId = NpcId.ABYSSAL_SIRE_350;
        } else if (index == 12) {
            bossName = "boss_instance_kraken";
            tile = new Tile(2280, 10022, 0);
            teleportType = LADDER_DOWN;
            slayerId = NpcId.KRAKEN_291;
        } else if (index == 13) {
            bossName = "boss_instance_kalphite_queen";
            tile = new Tile(3506, 9494, 0);
            teleportType = LADDER_DOWN;
        } else if (index == 14) {
            bossName = "boss_instance_hydra";
            tile = new Tile(1356, 10258, 0);
            slayerId = NpcId.ALCHEMICAL_HYDRA_426;
        }
        var requiresCost = true; //player.getSkills().isSlayerTask(slayerId); // Enable to allow free instances on task
        if ((bossName.equals("boss_instance_kraken") || bossName.equals("boss_instance_hydra"))
                && player.getSkills().isAnySlayerTask(slayerId)) {
            // Kraken/hydra only has one spawn
            requiresCost = false;
        }
        if (slot == enterSlot) {
            if (areaScript != null) {
                player.getArea().script(areaScript);
            }
            player.getInventory().deleteItem(deleteItemId, 1);
            if (teleportType == NORMAL) {
                player.getMovement().teleport(tile);
            } else if (teleportType == LADDER_UP) {
                player.getMovement().ladderUpTeleport(tile);
            } else if (teleportType == LADDER_DOWN) {
                player.getMovement().ladderDownTeleport(tile);
            }
        } else if (slot == startSlot) {
            if (requiresCost && !player.getInventory().hasItem(ItemId.BOSS_INSTANCE_SCROLL_32313)
                    && !player.getCharges().hasRoWICharge(1)) {
                player.getGameEncoder().sendMessage("You need an instance creation item to do this.");
                return;
            } else if (playerInstance != null) {
                player.getGameEncoder().sendMessage("There is already a boss instance for this Clan Chat.");
                return;
            } else if (!player.getMessaging().canClanChatEvent()) {
                player.getGameEncoder().sendMessage("Your Clan Chat privledges aren't high enough to do that.");
                return;
            }
            if (areaScript != null) {
                player.getArea().script(areaScript);
            }
            player.getInventory().deleteItem(deleteItemId, 1);
            if (requiresCost) {
                if (!player.getInventory().deleteItem(ItemId.BOSS_INSTANCE_SCROLL_32313).success()) {
                    player.getCharges().depleteRoWICharge(1);
                }
            }
            player.getCombat().setDamageInflicted(0);
            player.setController(new BossInstancePC());
            player.getController().instance();
            if (teleportType == NORMAL) {
                player.getMovement().teleport(tile);
            } else if (teleportType == LADDER_UP) {
                player.getMovement().ladderUpTeleport(tile);
            } else if (teleportType == LADDER_DOWN) {
                player.getMovement().ladderDownTeleport(tile);
            }
            player.getController().getVariable(bossName);
            player.getWorld().putPlayerBossInstance(clanChatUsername, player.getController());
        } else if (slot == joinSlot) {
            if (playerInstance == null) {
                player.getGameEncoder().sendMessage("Unable to locate a boss instance for this Clan Chat.");
                return;
            } else if (!bossName.equals(playerInstance.getVariable("boss_instance_name"))) {
                player.getGameEncoder().sendMessage("The boss instance for this Clan Chat is located at "
                        + playerInstance.getVariable("boss_name") + ".");
                return;
            }
            if (areaScript != null) {
                player.getArea().script(areaScript);
            }
            player.getInventory().deleteItem(deleteItemId, 1);
            player.getCombat().setDamageInflicted(0);
            player.setController(new BossInstancePC());
            player.getController().setInstance(playerInstance);
            if (teleportType == NORMAL) {
                player.getMovement().teleport(tile);
            } else if (teleportType == LADDER_UP) {
                player.getMovement().ladderUpTeleport(tile);
            } else if (teleportType == LADDER_DOWN) {
                player.getMovement().ladderDownTeleport(tile);
            }
            player.getWorld().putPlayerBossInstance(clanChatUsername, player.getController());
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}
