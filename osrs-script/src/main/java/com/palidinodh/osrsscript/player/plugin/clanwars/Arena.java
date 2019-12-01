package com.palidinodh.osrsscript.player.plugin.clanwars;

import com.palidinodh.osrscore.model.Tile;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
enum Arena {
  WASTELAND("Wasteland", new Tile(3295, 5004), new Tile(3296, 5107), new Tile(3321, 5051),
      new Tile(3321, 5060),
      new Tile[] { new Tile(3282, 5093), new Tile(3311, 5086), new Tile(3298, 5057),
          new Tile(3282, 5033), new Tile(3309, 5028) },
      26784, 3269, 3315, 5055),
  PLATEAU("Plateau", new Tile(3292, 4932), new Tile(3292, 4987), new Tile(3323, 4943),
      new Tile(3323, 4976),
      new Tile[] { new Tile(3279, 4976), new Tile(3306, 4976), new Tile(3286, 4958),
          new Tile(3281, 4943), new Tile(3306, 4951) },
      26784, 3268, 3317, 4959),
  SYLVAN_GLADE("Sylvan Glade", new Tile(3423, 4998), new Tile(3422, 5050), new Tile(3424, 5018),
      new Tile(3423, 5028),
      new Tile[] { new Tile(3407, 5039), new Tile(3438, 5040), new Tile(3413, 5022),
          new Tile(3410, 5009), new Tile(3440, 5013) },
      26788, 3396, 3451, 5023),
  FORSAKEN_QUARRY("Forsaken Quarry", new Tile(3423, 5064), new Tile(3423, 5111),
      new Tile(3424, 5086), new Tile(3423, 5089),
      new Tile[] { new Tile(3404, 5101), new Tile(3441, 5103), new Tile(3434, 5090),
          new Tile(3410, 5074), new Tile(3438, 5073) },
      26786, 3396, 3451, 5087),
  TURRETS("Turrets", new Tile(3167, 5015), new Tile(3167, 5096), new Tile(3167, 5004),
      new Tile(3168, 5107),
      new Tile[] { new Tile(3157, 5090), new Tile(3178, 5089), new Tile(3167, 5056),
          new Tile(3159, 5019), new Tile(3176, 5021) },
      26790, 3144, 3191, 5055),
  CLAN_CUP("Clan Cup", new Tile(), new Tile(), new Tile(), new Tile(),
      new Tile[] { new Tile(), new Tile(), new Tile(), new Tile(), new Tile() }, -1, -1, -1, -1),
  SOGGY_SWAMP("Soggy Swamp", new Tile(), new Tile(), new Tile(), new Tile(),
      new Tile[] { new Tile(), new Tile(), new Tile(), new Tile(), new Tile() }, -1, -1, -1, -1),
  GHASTLY_SWAMP("Ghastly Swamp", new Tile(), new Tile(), new Tile(), new Tile(),
      new Tile[] { new Tile(), new Tile(), new Tile(), new Tile(), new Tile() }, -1, -1, -1, -1),
  NORTHLEACH_QUELL("Northleach Quell", new Tile(), new Tile(), new Tile(), new Tile(),
      new Tile[] { new Tile(), new Tile(), new Tile(), new Tile(), new Tile() }, -1, -1, -1, -1),
  GRIDLOCK("Gridlock", new Tile(), new Tile(), new Tile(), new Tile(),
      new Tile[] { new Tile(), new Tile(), new Tile(), new Tile(), new Tile() }, -1, -1, -1, -1),
  ETHREAL("Ethereal", new Tile(), new Tile(), new Tile(), new Tile(),
      new Tile[] { new Tile(), new Tile(), new Tile(), new Tile(), new Tile() }, -1, -1, -1, -1),
  CLASSIC("Classic", new Tile(3422, 4683), new Tile(3424, 4788), new Tile(3449, 4734),
      new Tile(3449, 4737),
      new Tile[] { new Tile(3407, 4771), new Tile(3444, 4780), new Tile(3423, 4747),
          new Tile(3400, 4704), new Tile(3439, 4700) },
      26784, 3395, 3445, 4735),
  LUMBRIDGE("Lumbridge", new Tile(3417, 4810), new Tile(3417, 4843), new Tile(3436, 4822),
      new Tile(3437, 4832),
      new Tile[] { new Tile(3413, 4837), new Tile(3430, 4839), new Tile(3420, 4836),
          new Tile(3413, 4816), new Tile(3425, 4814) },
      -1, -1, -1, -1),
  FALADOR("Falador", new Tile(3482, 4750), new Tile(3470, 4763), new Tile(3472, 4745),
      new Tile(3462, 4759), new Tile[] { new Tile(3476, 4766), new Tile(3514, 4752),
          new Tile(3492, 4759), new Tile(3477, 4753), new Tile(3506, 4749) },
      -1, -1, -1, -1);

  private String name;
  private Tile arenaTop;
  private Tile arenaBottom;
  private Tile viewTop;
  private Tile viewBottom;
  private Tile[] orbs;
  private int barrierObjectId;
  private int barrierStartX;
  private int barrierEndX;
  private int barrierY;

  public Tile getArenaTile(boolean isTop) {
    return isTop ? arenaTop : arenaBottom;
  }

  public Tile getViewTile(boolean isTop) {
    return isTop ? viewTop : viewBottom;
  }

  public static Arena get(int index) {
    return values()[index];
  }
}
