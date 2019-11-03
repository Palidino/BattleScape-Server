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

public class ItemOptionDecoder extends PacketDecoder {
  public ItemOptionDecoder() {
    super(87, 98, 72, 7, 58);
  }

  @Override
  public void execute(Player player, int index, int size, Stream stream) {
    var widgetHash = -1;
    var slot = -1;
    var itemId = -1;
    if (index == 0) {
      itemId = stream.readUnsignedLEShort();
      widgetHash = stream.readInt1();
      slot = stream.readUnsignedShort();
    } else if (index == 1) {
      itemId = stream.readUnsignedShortA();
      widgetHash = stream.readInt2();
      slot = stream.readUnsignedLEShortA();
    } else if (index == 2) {
      widgetHash = stream.readLEInt();
      slot = stream.readUnsignedLEShortA();
      itemId = stream.readUnsignedLEShortA();
    } else if (index == 3) {
      itemId = stream.readUnsignedLEShortA();
      widgetHash = stream.readInt();
      slot = stream.readUnsignedLEShortA();
    } else if (index == 4) {
      widgetHash = stream.readInt();
      slot = stream.readUnsignedShort();
      itemId = stream.readUnsignedShort();
    } else if (index == 10) {
      itemId = stream.readUnsignedLEShortA();
      widgetHash = stream.readInt2();
      slot = stream.readUnsignedLEShort();
    }
    var widgetId = widgetHash >> 16;
    var childId = widgetHash & 65535;
    if (slot == 65535) {
      slot = -1;
    }
    if (itemId == 65535) {
      itemId = -1;
    }
    var message = "[ItemOption(" + index + ")] widgetId=" + widgetId + "/"
        + WidgetId.valueOf(widgetId) + "; childId=" + childId + "; slot=" + slot + "; itemId="
        + itemId + "/" + ItemId.valueOf(itemId);
    if (Settings.getInstance().isLocal()) {
      PLogger.println(message);
    }
    if (player.getOptions().getPrintPackets()) {
      player.getGameEncoder().sendMessage(message);
    }
    RequestManager.addUserPacketLog(player, message);
    if (player.isLocked()) {
      return;
    }
    if (player.getMovement().isViewing()) {
      return;
    }
    if (!player.getWidgetManager().hasWidget(widgetId)) {
      return;
    }
    index = index >= 5 ? index - 5 : index;
    player.clearIdleTime();
    WidgetDecoder.execute(player, index, widgetId, childId, slot, itemId);
  }
}
