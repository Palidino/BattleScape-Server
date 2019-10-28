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
      itemId = stream.getUShortLE();
      widgetHash = stream.getIntV2();
      slot = stream.getUShort();
    } else if (index == 1) {
      itemId = stream.getUShort128();
      widgetHash = stream.getIntV3();
      slot = stream.getUShortLE128();
    } else if (index == 2) {
      widgetHash = stream.getIntLE();
      slot = stream.getUShortLE128();
      itemId = stream.getUShortLE128();
    } else if (index == 3) {
      itemId = stream.getUShortLE128();
      widgetHash = stream.getInt();
      slot = stream.getUShortLE128();
    } else if (index == 4) {
      widgetHash = stream.getInt();
      slot = stream.getUShort();
      itemId = stream.getUShort();
    } else if (index == 10) {
      itemId = stream.getUShortLE128();
      widgetHash = stream.getIntV3();
      slot = stream.getUShortLE();
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
