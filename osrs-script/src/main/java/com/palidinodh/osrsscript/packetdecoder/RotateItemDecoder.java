package com.palidinodh.osrsscript.packetdecoder;

import com.palidinodh.io.Stream;
import com.palidinodh.osrscore.io.PacketDecoder;
import com.palidinodh.osrscore.io.cache.ItemId;
import com.palidinodh.osrscore.io.cache.WidgetId;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.util.RequestManager;
import com.palidinodh.rs.setting.Settings;
import com.palidinodh.util.PLogger;
import lombok.var;

public class RotateItemDecoder extends PacketDecoder {
  public RotateItemDecoder() {
    super(57, 80);
  }

  @Override
  public void execute(Player player, int index, int size, Stream stream) {
    var fromWidgetHash = -1;
    var fromSlot = 0;
    var fromItemId = -1;
    var toWidgetHash = -1;
    var toSlot = 0;
    var toItemId = -1;
    if (index == 0) {
      fromSlot = stream.readUnsignedShortA();
      fromItemId = stream.readUnsignedShortA();
      toSlot = stream.readUnsignedShort();
      fromWidgetHash = stream.readLEInt();
      toWidgetHash = stream.readInt1();
      toItemId = stream.readUnsignedShort();
    } else if (index == 1) {
      toSlot = stream.readUnsignedShort();
      fromSlot = stream.readUnsignedLEShort();
      fromWidgetHash = toWidgetHash = stream.readInt();
      stream.readUnsignedByteC();
    }
    if (fromItemId == 65535) {
      fromItemId = -1;
    }
    if (toItemId == 65535) {
      toItemId = -1;
    }
    var fromWidgetId = fromWidgetHash >> 16;
    var fromChildId = fromWidgetHash & 65535;
    var toWidgetId = toWidgetHash >> 16;
    var toChildId = toWidgetHash & 65535;
    if (player.isLocked()) {
      return;
    }
    if (fromWidgetId != -1 && !player.getWidgetManager().hasWidget(fromWidgetId)) {
      return;
    }
    if (toWidgetId != -1 && !player.getWidgetManager().hasWidget(toWidgetId)) {
      return;
    }
    var message = "[RotateItems(" + index + ")] fromWidgetId=" + fromWidgetId + "/"
        + WidgetId.valueOf(fromWidgetId) + "; fromChildId=" + fromChildId + "; toWidgetId="
        + toWidgetId + "/" + WidgetId.valueOf(toWidgetId) + "; toChildId=" + toChildId
        + "; fromSlot=" + fromSlot + "; toSlot=" + toSlot + "; fromItemId=" + fromItemId + "/"
        + ItemId.valueOf(fromItemId) + "; toItemId=" + toItemId + "/" + ItemId.valueOf(toItemId);
    if (Settings.getInstance().isLocal()) {
      PLogger.println(message);
    }
    if (player.getOptions().getPrintPackets()) {
      player.getGameEncoder().sendMessage(message);
    }
    RequestManager.addUserPacketLog(player, message);
    player.clearIdleTime();
    if (fromWidgetId == WidgetId.INVENTORY && toWidgetId == WidgetId.INVENTORY
        || fromWidgetId == WidgetId.BANK_INVENTORY && toWidgetId == WidgetId.BANK_INVENTORY
        || fromWidgetId == WidgetId.EQUIPMENT_BONUSES_INVENTORY
            && toWidgetId == WidgetId.EQUIPMENT_BONUSES_INVENTORY) {
      player.getInventory().rotateItems(fromSlot, toSlot);
    } else if (fromWidgetId == WidgetId.BANK && toWidgetId == WidgetId.BANK) {
      if (fromChildId == 13 && toChildId == 11) {
        player.getBank().moveItemToTab(toItemId, -1, fromSlot, toSlot - 10);
      } else if (fromChildId == 13 && toChildId == 13) {
        player.getBank().rotateItems(fromItemId, -1, player.getBank().getRealSlot(fromSlot),
            player.getBank().getRealSlot(toSlot));
      } else if (fromChildId == 11 && toChildId == 11) {
        if (toSlot - 10 == 0) {
          player.getBank().collapseTab(fromSlot - 10, toSlot - 10);
        } else {
          player.getBank().rotateTabs(fromSlot - 10, toSlot - 10);
        }
      }
    }
  }
}
