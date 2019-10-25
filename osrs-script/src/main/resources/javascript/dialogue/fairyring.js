function Ring(key, description, tile) {
    this.key = key;
    this.description = description;
    this.tile = tile;
}

var RINGS = PCollection.toList(
    new Ring("AIQ", "Asgarnia: Mudskipper Point", new Tile(2996, 3114)),
    new Ring("AIR", "Islands: South-east of Ardougne", new Tile(2700, 3247)),
    new Ring("AJR", "Kandarin: Slayer cave south-east of Rellekka", new Tile(2780, 3613)),
    new Ring("AJS", "Islands: Penguins near Miscellania", new Tile(2500, 3896)),
    new Ring("AKQ", "Kandarin: Piscatoris Hunter area", new Tile(2319, 3619)),
    new Ring("AKS", "Feldip Hills: Feldip Hunter area", new Tile(2571, 2956)),
    new Ring("ALP", "Islands: Lighthouse", new Tile(2503, 3636)),
    new Ring("ALQ", "Morytania: Haunted Woods east of Canifis", new Tile(3597, 3495)),
    new Ring("ALR", "Other Realms: Abyssal Area", new Tile(3059, 4875)),
    new Ring("ALS", "Kandarin: McGrubor's Wood", new Tile(2644, 3495)),
    new Ring("BIP", "Islands: South-west of Mort Myre", new Tile(3410, 3324)),
    new Ring("BIQ", "Kharidian Desert: near Kalphite Hive", new Tile(3251, 3095)),
    new Ring("BIS", "Kandarin: Ardougne Zoo - Unicorns", new Tile(2635, 3266)),
    new Ring("BJS", "Islands: Near Zul-Andra", new Tile(2150, 3070)),
    new Ring("BKP", "Feldip Hills: South of Castle Wars", new Tile(2385, 3035)),
    new Ring("BKR", "Morytania: Mort Myre Swamp, south of Canifis", new Tile(3469, 3431)),
    new Ring("BLP", "Dungeons: TzHaar area", new Tile(2437, 5126)),
    new Ring("BLR", "Kandarin: Legends' Guild", new Tile(2740, 3351)),
    new Ring("CIP", "Islands: Miscellania", new Tile(2513, 3884)),
    new Ring("CIQ", "Kandarin: North-west of Yanille", new Tile(2528, 3127)),
    new Ring("CIS", "Zeah: North of the Arceuus House Library", new Tile(1639, 3869)),
    new Ring("CJR", "Kandarin: Sinclair Mansion (East)", new Tile(2705, 3576)),
    new Ring("CKR", "Karamja: South of Tai Bwo Wannai Village", new Tile(2801, 3003)),
    new Ring("CKS", "Morytania: Canifis", new Tile(3447, 3470)),
    new Ring("DIP", "Other Realms: Abyssal Nexus", new Tile(3037, 4763)),
    new Ring("DIS", "Misthalin: Wizards' Tower", new Tile(3108, 3149)),
    new Ring("DJP", "Kandarin: Tower of Life", new Tile(2658, 3230)),
    new Ring("DJR", "Zeah: Chasm of Fire", new Tile(1455, 3658)),
    new Ring("DKP", "Karamja: South of Musa Point", new Tile(2900, 3111)),
    new Ring("DKR", "Misthalin: Edgeville", new Tile(3129, 3496)),
    new Ring("DKS", "Kandarin: Polar Hunter area", new Tile(2744, 3719)),
    new Ring("DLQ", "Kharidian Desert: North of Nardah", new Tile(3423, 3016)),
    new Ring("DLR", "Poison Waste south of Isafdar", new Tile(2213, 3099))
);

var entries = new ArrayList();
var title = "";
var lines = new ArrayList();
var actions = new ArrayList();

title = "Select an Option";
for each (var ring in RINGS) {
    lines.add(ring.key + ": " + ring.description);
    actions.add("close|script");
}
var obj0 = new DialogueEntry();
entries.add(obj0);
obj0.setLargeSelection(title, PString.toStringArray(lines, true), PString.toStringArray(actions, true));

instance = new DialogueScript() {
    execute: function(player, index, childId, slot) {
        if (player.isLocked()) {
            return;
        }
        if (index == 0) {
            if (!player.getController().canTeleport(true)) {
                return;
            }
            if (slot >= RINGS.size()) {
                return;
            }
            var ring = RINGS.get(slot);
            if (ring == null) {
                return;
            }
            player.getMovement().animatedTeleport(ring.tile, -1, -1, new Graphic(569), null, 2);
            player.getController().stopWithTeleport();
            player.clearHits();
        }
    },

    getDialogueEntries: function() {
        return entries;
    }
}
