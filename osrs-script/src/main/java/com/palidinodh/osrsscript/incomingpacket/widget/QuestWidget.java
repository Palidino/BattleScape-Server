package com.palidinodh.osrsscript.incomingpacket.widget;

import com.palidinodh.osrscore.io.incomingpacket.WidgetHandler;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.player.AchievementDiary;
import com.palidinodh.osrscore.model.player.Player;

public class QuestWidget implements WidgetHandler {
  @Override
  public int[] getIds() {
    return new int[] {WidgetId.QUEST_CONTAINER, WidgetId.QUEST, WidgetId.ACHIEVEMENT_DIARY};
  }

  @Override
  public void execute(Player player, int index, int widgetId, int childId, int slot, int itemId) {
    if (widgetId == WidgetId.QUEST_CONTAINER) {
      if (childId == 3) {
        player.getWidgetManager().sendQuestOverlay(WidgetId.QUEST);
      } else if (childId == 4) {
        player.getWidgetManager().sendQuestOverlay(WidgetId.ACHIEVEMENT_DIARY);
      }
    } else if (widgetId == WidgetId.ACHIEVEMENT_DIARY) {
      if (player.isLocked()) {
        return;
      }
      if (childId == 2) {
        if (slot == 2) {
          AchievementDiary.getDiary(AchievementDiary.Name.FALADOR).sendTaskList(player);
        } /*
           * else if (slot == 3) {
           * AchievementDiary.getDiary(AchievementDiary.Name.WILDERNESS).sendTaskList(player); }
           */ else {
          player.getGameEncoder().sendMessage("This diary is currently unavailable.");
        }
      }
    }
  }
}
