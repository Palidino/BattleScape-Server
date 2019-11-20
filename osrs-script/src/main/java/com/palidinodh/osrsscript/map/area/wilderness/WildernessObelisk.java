package com.palidinodh.osrsscript.map.area.wilderness;

import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.model.Tile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.var;

@AllArgsConstructor
@Getter
public enum WildernessObelisk {
  LEVEL_13(ObjectId.OBELISK_14829,
      new Tile[] { new Tile(3154, 3618), new Tile(3154, 3622), new Tile(3158, 3622),
          new Tile(3158, 3618) }),
  LEVEL_19(ObjectId.OBELISK_14830,
      new Tile[] { new Tile(3225, 3665), new Tile(3225, 3669), new Tile(3229, 3669),
          new Tile(3229, 3665) }),
  LEVEL_27(ObjectId.OBELISK_14827,
      new Tile[] { new Tile(3033, 3730), new Tile(3033, 3734), new Tile(3037, 3734),
          new Tile(3037, 3730) }),
  LEVEL_35(ObjectId.OBELISK_14828,
      new Tile[] { new Tile(3104, 3792), new Tile(3104, 3796), new Tile(3108, 3796),
          new Tile(3108, 3792) }),
  LEVEL_44(ObjectId.OBELISK_14826,
      new Tile[] { new Tile(2978, 3864), new Tile(2978, 3868), new Tile(2982, 3868),
          new Tile(2982, 3864) }),
  LEVEL_50(ObjectId.OBELISK_14831, new Tile[] { new Tile(3305, 3914), new Tile(3305, 3918),
      new Tile(3309, 3918), new Tile(3309, 3914) });

  private int objectId;
  private Tile[] tiles;

  public static WildernessObelisk getByObjectId(int objectId) {
    for (var obelisk : values()) {
      if (obelisk.getObjectId() != objectId) {
        continue;
      }
      return obelisk;
    }
    return null;
  }
}
