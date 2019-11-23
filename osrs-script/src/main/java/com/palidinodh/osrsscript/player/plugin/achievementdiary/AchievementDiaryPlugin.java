package com.palidinodh.osrsscript.player.plugin.achievementdiary;

import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.PlayerPlugin;
import com.palidinodh.osrscore.model.player.Skills;
import com.palidinodh.util.PEvent;
import com.palidinodh.util.PTime;
import lombok.var;

public class AchievementDiaryPlugin extends PlayerPlugin {
  private transient Player player;

  private String faladorShield1And2;
  private String faladorShield3And4;

  @Override
  public void login() {
    player = getPlayer();
  }

  @Override
  public boolean widgetHook(int option, int widgetId, int childId, int slot, int itemId) {
    var isEquipment = widgetId == WidgetId.EQUIPMENT || widgetId == WidgetId.EQUIPMENT_BONUSES;
    if (widgetId == WidgetId.INVENTORY || isEquipment) {
      var prayerLevel = player.getController().getLevelForXP(Skills.PRAYER);
      switch (itemId) {
        case ItemId.FALADOR_SHIELD_1:
        case ItemId.FALADOR_SHIELD_2:
          if (isEquipment && option == 1 || !isEquipment && option == 2) {
            if (PTime.getYearMonthDay().equals(faladorShield1And2)) {
              player.getGameEncoder().sendMessage("You can only use this once a day.");
              return true;
            }
            faladorShield1And2 = PTime.getYearMonthDay();
            if (itemId == ItemId.FALADOR_SHIELD_1) {
              player.getPrayer().adjustPoints((int) (prayerLevel * 0.25));
            } else if (itemId == ItemId.FALADOR_SHIELD_2) {
              player.getPrayer().adjustPoints((int) (prayerLevel * 0.5));
            }
            player.getGameEncoder().sendMessage("Your prayer points have been restored.");
            return true;
          }
          break;
        case ItemId.FALADOR_SHIELD_3:
        case ItemId.FALADOR_SHIELD_4:
          if (isEquipment && option == 1 || !isEquipment && option == 2) {
            if (!PTime.getYearMonthDay().equals(faladorShield3And4)) {
              faladorShield3And4 = PTime.getYearMonthDay();
              player.getPrayer().adjustPoints(prayerLevel);
              player.getGameEncoder().sendMessage("Your prayer points have been restored.");
            } else if (!PTime.getYearMonthDay().equals(faladorShield1And2)) {
              faladorShield1And2 = PTime.getYearMonthDay();
              if (itemId == ItemId.FALADOR_SHIELD_3) {
                player.getPrayer().adjustPoints((int) (prayerLevel * 0.5));
              } else if (itemId == ItemId.FALADOR_SHIELD_4) {
                player.getPrayer().adjustPoints(prayerLevel);
              }
              player.getGameEncoder().sendMessage("Your prayer points have been restored.");
            } else {
              player.getGameEncoder().sendMessage("You can only use this once a day.");
            }
            return true;
          } else if (isEquipment && option == 2 || !isEquipment && option == 3) {
            if (player.getRegionId() != 6992 && player.getRegionId() != 6993) {
              player.getGameEncoder().sendMessage("You can't use this here.");
              return true;
            }
            var mole = player.getController().getNpc(NpcId.GIANT_MOLE_230);
            if (mole == null) {
              player.getGameEncoder().sendMessage("Unable to locate the giant mole.");
              return true;
            }
            player.getGameEncoder().sendHintIconTile(mole);
            player.getWorld().addEvent(new PEvent(16) {
              @Override
              public void execute() {
                stop();
                if (!player.isVisible()) {
                  return;
                }
                player.getGameEncoder().sendHintIconReset();
              }
            });
            return true;
          }
          break;
      }
    }
    return false;
  }
}
