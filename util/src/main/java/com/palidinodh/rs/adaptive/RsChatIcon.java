package com.palidinodh.rs.adaptive;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RsChatIcon {
  NONE(0, -1), SILVER_CROWN(1, 0), GOLD_CROWN(2, 1), IRONMAN(3, 2), ULTIMATE_IRONMAN(4,
      3), HARDCORE_IRONMAN(5, 10), PINK_CROWN(6, 20), PURPLE_CROWN(7, 21), GREEN_CROWN(8,
          22), BLUE_CROWN(9, 23), RED_DONATOR(10, 24), GREEN_DONATOR(11, 25), BLUE_DONATOR(12,
              26), YELLOW_DONATOR(13,
                  27), BLACK_DONATOR(14, 28), YOUTUBE(15, 29), PREMIUM_MEMBER(16, 30);

  private int osrsRankIndex;
  private int osrsSpriteIndex;
}
