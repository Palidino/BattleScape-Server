package com.palidinodh.osrsscript.incomingpacket;

import com.palidinodh.io.Stream;
import com.palidinodh.osrscore.io.cache.ItemId;
import com.palidinodh.osrscore.io.cache.WidgetId;
import com.palidinodh.osrscore.io.incomingpacket.InStreamKey;
import com.palidinodh.osrscore.io.incomingpacket.IncomingPacketDecoder;
import com.palidinodh.osrscore.io.incomingpacket.WidgetHandler;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.util.RequestManager;
import com.palidinodh.rs.setting.Settings;
import com.palidinodh.util.PLogger;
import lombok.var;

public class RotateWidgetDecoder {
  public static class RotateSingleWidgetDecoder extends IncomingPacketDecoder {
    @Override
    public boolean execute(Player player, Stream stream) {
      var widgetHash = getInt(InStreamKey.WIDGET_HASH);
      var widgetId = WidgetHandler.getWidgetId(widgetHash);
      var widgetChildId = WidgetHandler.getWidgetChildId(widgetHash);
      var useWidgetSlot = getInt(InStreamKey.USE_WIDGET_SLOT);
      var onWidgetSlot = getInt(InStreamKey.ON_WIDGET_SLOT);
      player.clearIdleTime();
      if (player.isLocked()) {
        return false;
      }
      if (!player.getWidgetManager().hasWidget(widgetId)) {
        return false;
      }
      var message = "[RotateSingleWidget] widgetId=" + widgetId + "/" + WidgetId.valueOf(widgetId)
          + "; widgetChildId=" + widgetChildId + "; useWidgetSlot=" + useWidgetSlot
          + "; onWidgetSlot=" + onWidgetSlot;
      if (Settings.getInstance().isLocal()) {
        PLogger.println(message);
      }
      if (player.getOptions().getPrintPackets()) {
        player.getGameEncoder().sendMessage(message);
      }
      RequestManager.addUserPacketLog(player, message);
      if (widgetId == WidgetId.INVENTORY || widgetId == WidgetId.BANK_INVENTORY
          || widgetId == WidgetId.EQUIPMENT_BONUSES_INVENTORY) {
        player.getInventory().rotateItems(useWidgetSlot, onWidgetSlot);
      } else if (widgetId == WidgetId.BANK) {
        if (widgetChildId == 13) {
          player.getBank().rotateItems(-1, -1, player.getBank().getRealSlot(useWidgetSlot),
              player.getBank().getRealSlot(onWidgetSlot));
        } else if (widgetChildId == 11) {
          if (onWidgetSlot - 10 == 0) {
            player.getBank().collapseTab(useWidgetSlot - 10, onWidgetSlot - 10);
          } else {
            player.getBank().rotateTabs(useWidgetSlot - 10, onWidgetSlot - 10);
          }
        }
      }
      return true;
    }
  }

  public static class RotateTwoWidgetsDecoder extends IncomingPacketDecoder {
    @Override
    public boolean execute(Player player, Stream stream) {
      var useWidgetHash = getInt(InStreamKey.USE_WIDGET_HASH);
      var useWidgetId = WidgetHandler.getWidgetId(useWidgetHash);
      var useWidgetChildId = WidgetHandler.getWidgetChildId(useWidgetHash);
      var useWidgetSlot = getInt(InStreamKey.USE_WIDGET_SLOT);
      var useItemId = getInt(InStreamKey.USE_ITEM_ID);
      var onWidgetHash = getInt(InStreamKey.ON_WIDGET_HASH);
      var onWidgetId = WidgetHandler.getWidgetId(onWidgetHash);
      var onWidgetChildId = WidgetHandler.getWidgetChildId(onWidgetHash);
      var onWidgetSlot = getInt(InStreamKey.ON_WIDGET_SLOT);
      var onItemId = getInt(InStreamKey.ON_ITEM_ID);
      player.clearIdleTime();
      if (player.isLocked()) {
        return false;
      }
      if (!player.getWidgetManager().hasWidget(useWidgetId)) {
        return false;
      }
      if (!player.getWidgetManager().hasWidget(onWidgetId)) {
        return false;
      }
      var message = "[RotateTwoWidgets] useWidgetId=" + useWidgetId + "/"
          + WidgetId.valueOf(useWidgetId) + "; useWidgetChildId=" + useWidgetChildId
          + "; onWidgetId=" + onWidgetId + "/" + WidgetId.valueOf(onWidgetId) + "; onWidgetChildId="
          + onWidgetChildId + "; useWidgetSlot=" + useWidgetSlot + "; onWidgetSlot=" + onWidgetSlot
          + "; useItemId=" + useItemId + "/" + ItemId.valueOf(useItemId) + "; onItemId=" + onItemId
          + "/" + ItemId.valueOf(onItemId);
      if (Settings.getInstance().isLocal()) {
        PLogger.println(message);
      }
      if (player.getOptions().getPrintPackets()) {
        player.getGameEncoder().sendMessage(message);
      }
      RequestManager.addUserPacketLog(player, message);
      if (useWidgetId == WidgetId.BANK && onWidgetId == WidgetId.BANK) {
        if (useWidgetChildId == 13 && onWidgetChildId == 11) {
          player.getBank().moveItemToTab(onItemId, -1, useWidgetSlot, onWidgetSlot - 10);
        }
      }
      return true;
    }
  }
}
