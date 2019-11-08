package com.palidinodh.osrsscript.map.area;

import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.map.Area;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrsscript.player.skill.agility.AgilityObstacle;
import com.palidinodh.osrsscript.player.skill.agility.MoveAgilityEvent;
import com.palidinodh.osrsscript.player.skill.agility.TeleportAgilityEvent;
import lombok.var;

public class EastArdougneArea extends Area {
  public EastArdougneArea() {
    super(10547);
  }

  @Override
  public boolean mapObjectOptionHook(int index, MapObject mapObject) {
    var player = getPlayer();
    AgilityObstacle obstacle = null;
    switch (mapObject.getId()) {
      case ObjectId.WOODEN_BEAMS:
        obstacle = new AgilityObstacle(player, 90, 43);
        player.setFaceTile(new Tile(2673, 3299));
        obstacle.add(TeleportAgilityEvent.builder().tile(new Tile(2673, 3298, 1)).completeDelay(1)
            .startAnimation(737).build());
        obstacle.add(TeleportAgilityEvent.builder().tile(new Tile(2673, 3298, 2))
            .startAnimation(737).build());
        obstacle.add(TeleportAgilityEvent.builder().tile(new Tile(2671, 3299, 3))
            .startAnimation(2588).build());
        obstacle.start(mapObject);
        return true;
      case ObjectId.GAP_11406:
        obstacle = new AgilityObstacle(player, 90, 65);
        obstacle.add(TeleportAgilityEvent.builder().tile(new Tile(2667, 3311, 1)).completeDelay(1)
            .startAnimation(2586).endAnimation(2588).build());
        obstacle.add(TeleportAgilityEvent.builder().tile(new Tile(2665, 3315, 1)).completeDelay(1)
            .startAnimation(2586).endAnimation(2588).build());
        obstacle.add(TeleportAgilityEvent.builder().tile(new Tile(2665, 3318, 3)).completeDelay(1)
            .startAnimation(2586).endAnimation(2588).build());
        obstacle.start(mapObject);
        return true;
      case ObjectId.PLANK_11631:
        obstacle = new AgilityObstacle(player, 90, 50);
        obstacle.add(MoveAgilityEvent.builder().tile(new Tile(2656, 3318, 3)).noclip(true)
            .animation(762).build());
        obstacle.start(mapObject);
        return true;
      case ObjectId.GAP_11429:
        obstacle = new AgilityObstacle(player, 90, 21);
        obstacle.add(TeleportAgilityEvent.builder().tile(new Tile(2653, 3314, 3)).completeDelay(1)
            .startAnimation(2586).endAnimation(2588).build());
        obstacle.start(mapObject);
        return true;
      case ObjectId.GAP_11430:
        obstacle = new AgilityObstacle(player, 90, 28);
        obstacle.add(TeleportAgilityEvent.builder().tile(new Tile(2651, 3309, 3)).completeDelay(1)
            .startAnimation(2586).endAnimation(2588).build());
        obstacle.start(mapObject);
        return true;
      case ObjectId.STEEP_ROOF:
        obstacle = new AgilityObstacle(player, 90, 57);
        obstacle.add(MoveAgilityEvent.builder().tile(new Tile(2656, 3297, 3)).noclip(true)
            .animation(756).build());
        obstacle.start(mapObject);
        return true;
      case ObjectId.GAP_11630:
        obstacle = new AgilityObstacle(player, 90, 529);
        obstacle.add(TeleportAgilityEvent.builder().tile(new Tile(2658, 3298, 1)).completeDelay(1)
            .startAnimation(2586).endAnimation(2588).build());
        obstacle.add(MoveAgilityEvent.builder().tile(new Tile(2661, 3298, 1)).noclip(true).build());
        obstacle.add(TeleportAgilityEvent.builder().tile(new Tile(2663, 3297, 1)).completeDelay(1)
            .startAnimation(2586).endAnimation(2588).build());
        obstacle.add(MoveAgilityEvent.builder().tile(new Tile(2666, 3297, 1)).noclip(true).build());
        obstacle.add(TeleportAgilityEvent.builder().tile(new Tile(2668, 3297, 0)).completeDelay(1)
            .startAnimation(2586).endAnimation(2588).build());
        obstacle.start(mapObject);
        return true;
    }
    return false;
  }
}
