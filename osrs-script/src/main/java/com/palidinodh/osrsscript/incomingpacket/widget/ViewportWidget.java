package com.palidinodh.osrsscript.incomingpacket.widget;

import com.palidinodh.osrscore.io.incomingpacket.WidgetHandler;
import com.palidinodh.osrscore.io.cache.WidgetChild;
import com.palidinodh.osrscore.io.cache.WidgetId;
import com.palidinodh.osrscore.model.player.Player;

public class ViewportWidget implements WidgetHandler {
  @Override
  public int[] getIds() {
    return new int[] {WidgetId.FIXED_VIEWPORT, WidgetId.RESIZABLE_BOX_VIEWPORT,
        WidgetId.RESIZABLE_LINE_VIEWPORT};
  }

  @Override
  public void execute(Player player, int index, int widgetId, int childId, int slot, int itemId) {
    if (widgetId == WidgetId.FIXED_VIEWPORT) {
      WidgetChild.FixedViewport child = WidgetChild.FixedViewport.getByChild(childId);
      if (child.getIcon() != null) {
        player.getOptions().setViewingIcon(child.getIcon());
        if (child.getIcon() == WidgetChild.ViewportIcon.QUESTS) {
          player.getWidgetManager().sendQuestOverlay();
        } else if (child.getIcon() == WidgetChild.ViewportIcon.MAGIC && index == 1) {
          player.getMagic().setDisableSpellFiltering(!player.getMagic().getDisableSpellFiltering());
        }
      }
    } else if (widgetId == WidgetId.RESIZABLE_BOX_VIEWPORT) {
      WidgetChild.ResizeableBoxViewport child =
          WidgetChild.ResizeableBoxViewport.getByChild(childId);
      if (child.getIcon() != null) {
        player.getOptions().setViewingIcon(child.getIcon());
        if (child.getIcon() == WidgetChild.ViewportIcon.QUESTS) {
          player.getWidgetManager().sendQuestOverlay();
        } else if (child.getIcon() == WidgetChild.ViewportIcon.MAGIC && index == 1) {
          player.getMagic().setDisableSpellFiltering(!player.getMagic().getDisableSpellFiltering());
        }
      }
    } else if (widgetId == WidgetId.RESIZABLE_LINE_VIEWPORT) {
      WidgetChild.ResizeableLineViewport child =
          WidgetChild.ResizeableLineViewport.getByChild(childId);
      if (child.getIcon() != null) {
        player.getOptions().setViewingIcon(child.getIcon());
        if (child.getIcon() == WidgetChild.ViewportIcon.QUESTS) {
          player.getWidgetManager().sendQuestOverlay();
        } else if (child.getIcon() == WidgetChild.ViewportIcon.MAGIC && index == 1) {
          player.getMagic().setDisableSpellFiltering(!player.getMagic().getDisableSpellFiltering());
        }
      }
    }
  }
}
