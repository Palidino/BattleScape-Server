package com.palidinodh.osrsscript.incomingpacket;

import com.palidinodh.io.Stream;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
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
      return rotate(player, stream, this);
    }
  }

  public static class RotateTwoWidgetsDecoder extends IncomingPacketDecoder {
    @Override
    public boolean execute(Player player, Stream stream) {
      return rotate(player, stream, this);
    }
  }

  private static boolean rotate(Player player, Stream stream, IncomingPacketDecoder packet) {
    var useWidgetHash = packet.getInt(InStreamKey.USE_WIDGET_HASH);
    var useWidgetId = WidgetHandler.getWidgetId(useWidgetHash);
    var useWidgetChildId = WidgetHandler.getWidgetChildId(useWidgetHash);
    var useWidgetSlot = packet.getInt(InStreamKey.USE_WIDGET_SLOT);
    var useItemId = packet.getInt(InStreamKey.USE_ITEM_ID);
    var onWidgetHash = packet.getInt(InStreamKey.ON_WIDGET_HASH);
    if (!packet.containsKey(InStreamKey.ON_WIDGET_HASH)) {
      onWidgetHash = useWidgetHash;
    }
    var onWidgetId = WidgetHandler.getWidgetId(onWidgetHash);
    var onWidgetChildId = WidgetHandler.getWidgetChildId(onWidgetHash);
    var onWidgetSlot = packet.getInt(InStreamKey.ON_WIDGET_SLOT);
    var onItemId = packet.getInt(InStreamKey.ON_ITEM_ID);
    player.clearIdleTime();
    if (player.isLocked()) {
      return false;
    }
    if (!player.getWidgetManager().hasWidget(useWidgetId)) {
      return false;
    }
    if (onWidgetId != -1 && !player.getWidgetManager().hasWidget(onWidgetId)) {
      return false;
    }
    var message = "[RotateWidgets] useWidgetId=" + useWidgetId + "/" + WidgetId.valueOf(useWidgetId)
        + "; useWidgetChildId=" + useWidgetChildId + "; onWidgetId=" + onWidgetId + "/"
        + WidgetId.valueOf(onWidgetId) + "; onWidgetChildId=" + onWidgetChildId + "; useWidgetSlot="
        + useWidgetSlot + "; onWidgetSlot=" + onWidgetSlot + "; useItemId=" + useItemId + "/"
        + ItemId.valueOf(useItemId) + "; onItemId=" + onItemId + "/" + ItemId.valueOf(onItemId);
    if (Settings.getInstance().isLocal()) {
      PLogger.println(message);
    }
    if (player.getOptions().getPrintPackets()) {
      player.getGameEncoder().sendMessage(message);
    }
    RequestManager.addUserPacketLog(player, message);
    if (useWidgetId == WidgetId.INVENTORY && onWidgetId == WidgetId.INVENTORY
        || useWidgetId == WidgetId.BANK_INVENTORY && onWidgetId == WidgetId.BANK_INVENTORY
        || useWidgetId == WidgetId.EQUIPMENT_BONUSES_INVENTORY
            && onWidgetId == WidgetId.EQUIPMENT_BONUSES_INVENTORY) {
      player.getInventory().rotateItems(useWidgetSlot, onWidgetSlot);
    } else if (useWidgetId == WidgetId.BANK && onWidgetId == WidgetId.BANK) {
      if (useWidgetChildId == 13 && onWidgetChildId == 11) {
        player.getBank().moveItemToTab(onItemId, -1, useWidgetSlot, onWidgetSlot - 10);
      } else if (useWidgetChildId == 13 && onWidgetChildId == 13) {
        player.getBank().rotateItems(useItemId, -1, player.getBank().getRealSlot(useWidgetSlot),
            player.getBank().getRealSlot(onWidgetSlot));
      } else if (useWidgetChildId == 11 && onWidgetChildId == 11) {
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
