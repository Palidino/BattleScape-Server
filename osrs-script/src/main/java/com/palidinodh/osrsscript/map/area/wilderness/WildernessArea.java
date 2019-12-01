package com.palidinodh.osrsscript.map.area.wilderness;

import com.palidinodh.osrscore.model.map.Area;
import com.palidinodh.rs.ReferenceId;

@ReferenceId({ 11831, 11832, 11833, 11834, 11835, 11836, 11837, 12087, 12088, 12089, 12090, 12091,
    12092, 12093, 12343, 12344, 12345, 12346, 12347, 12348, 12349, 12599, 12600, 12601, 12602,
    12603, 12604, 12605, 12855, 12856, 12857, 12858, 12859, 12860, 12861, 13111, 13112, 13113,
    13114, 13115, 13116, 13117, 13367, 13368, 13369, 13370, 13371, 13372, 13373, 12190, 12192,
    12193, 12443, 12444, 12961, 12701, 12702, 12703, 12957, 12958, 12959 })
public class WildernessArea extends Area {
  @Override
  public boolean inWilderness() {
    return getTile().getRegionId() != 12442 || getTile().getY() > 9919;
  }
}
