package com.palidinodh.osrsscript.packetdecoder.widget;

import com.palidinodh.osrscore.Main;
import com.palidinodh.osrscore.io.Widget;
import com.palidinodh.osrscore.io.cache.WidgetId;
import com.palidinodh.osrscore.model.player.Player;

public class OptionsWidget implements Widget {
  @Override
  public int[] getIds() {
    return new int[] {WidgetId.OPTIONS, WidgetId.ADVANCED_OPTIONS};
  }

  @Override
  public void execute(Player player, int index, int widgetId, int childId, int slot, int itemId) {
    if (widgetId == WidgetId.OPTIONS) {
      if (childId >= 18 && childId <= 21) {
        player.getOptions().setBrightness(childId - 18 + 1);
      } else if (childId >= 45 && childId <= 49) {
        player.getOptions().setMusicVolume(49 - childId);
      } else if (childId >= 51 && childId <= 55) {
        player.getOptions().setSoundEffectVolume(55 - childId);
      } else if (childId >= 57 && childId <= 61) {
        player.getOptions().setAreaSoundVolume(61 - childId);
      }
      switch (childId) {
        case 5:
          player.getOptions().setMouseWheelZoom(!player.getOptions().getMouseWheelZoom());
          break;
        case 35:
          if (player.isLocked()) {
            break;
          }
          player.getWidgetManager().sendInteractiveOverlay(WidgetId.ADVANCED_OPTIONS);
          break;
        case 63:
          player.getOptions().setChatEffects(!player.getOptions().getChatEffects());
          break;
        case 65:
          player.getOptions().setSplitPrivateChat(!player.getOptions().getSplitPrivateChat());
          break;
        case 67:
          player.getOptions().setHidePrivateChat(!player.getOptions().getHidePrivateChat());
          break;
        case 69:
          player.getOptions().setProfanityFilter(!player.getOptions().getProfanityFilter());
          break;
        case 73:
          player.getOptions()
              .setLogNotificationTimeout(!player.getOptions().getLogNotificationTimeout());
          break;
        case 75:
          player.getGameEncoder().sendOpenURL(Main.getSettings().getSupportUrl());
          break;
        case 77:
          player.getOptions().setOneMouseButton(!player.getOptions().getOneMouseButton());
          break;
        case 79:
          player.getOptions()
              .setMiddleMouseCameraControl(!player.getOptions().getMiddleMouseCameraControl());
          break;
        case 81:
          player.getOptions()
              .setFollowerOptionsPriority(!player.getOptions().getFollowerOptionsPriority());
          break;
        case 83:
          player.getOptions().sendKeyBindingInterface();
          break;
        case 85:
          player.getOptions().setShiftClickDrop(!player.getOptions().getShiftClickDrop());
          break;
        case 92:
          player.getOptions().setAcceptAid(!player.getOptions().getAcceptAid());
          break;
        case 95:
          player.getMovement().setRunning(!player.getMovement().getRunning());
          break;
        case 100:
          player.getBonds().sendPouch();
          break;
        case 106:
          player.getOptions().setPlayerAttackOption(slot - 1);
          break;
        case 107:
          player.getOptions().setNPCAttackOption(slot - 1);
          break;
      }
    } else if (widgetId == WidgetId.ADVANCED_OPTIONS) {
      if (player.isLocked()) {
        return;
      }
      switch (childId) {
        case 4:
          player.getOptions().setScrollbarLeft(!player.getOptions().getScrollbarLeft());
          break;
        case 6:
          player.getOptions()
              .setTransparentSidePanel(!player.getOptions().getTransparentSidePanel());
          break;
        case 8:
          player.getOptions().setRemainingXPTooltips(!player.getOptions().getRemainingXPTooltips());
          break;
        case 10:
          player.getOptions().setPrayerTooltips(!player.getOptions().getPrayerTooltips());
          break;
        case 12:
          player.getOptions()
              .setSpecialAttackTooltips(!player.getOptions().getSpecialAttackTooltips());
          break;
        case 16:
          player.getOptions().setDataOrbs(!player.getOptions().getDataOrbs());
          player.getWidgetManager().sendGameViewport();
          break;
        case 18:
          player.getOptions().setTransparentChatbox(!player.getOptions().getTransparentChatbox());
          break;
        case 20:
          player.getOptions().setClickThroughChatbox(!player.getOptions().getClickThroughChatbox());
          break;
        case 21:
          player.getOptions().setBottomLineResizable(!player.getOptions().getBottomLineResizable());
          player.getWidgetManager().sendGameViewport();
          break;
        case 23:
          player.getOptions().setSidePanelsClosable(!player.getOptions().getSidePanelsClosable());
          break;
      }
    }
  }
}
