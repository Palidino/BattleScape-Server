package com.palidinodh.osrsscript.packetdecoder;

import com.palidinodh.io.Stream;
import com.palidinodh.osrscore.io.PacketDecoder;
import com.palidinodh.osrscore.io.cache.ItemId;
import com.palidinodh.osrscore.io.cache.WidgetId;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.skill.SkillContainer;
import com.palidinodh.osrscore.util.RequestManager;
import com.palidinodh.osrsscript.incomingpacket.misc.ItemOnItemAction;
import com.palidinodh.rs.setting.Settings;
import com.palidinodh.util.PLogger;
import lombok.var;

public class ItemOnItemDecoder extends PacketDecoder {
  public ItemOnItemDecoder() {
    super(63);
  }

  @Override
  public void execute(Player player, int index, int size, Stream stream) {
    var useWidgetHash = stream.readInt1();
    var onWidgetHash = stream.readInt2();
    var useSlot = stream.readUnsignedLEShortA();
    var useItemId = stream.readUnsignedLEShort();
    var onSlot = stream.readUnsignedLEShort();
    var onItemId = stream.readUnsignedLEShortA();
    var useWidgetId = useWidgetHash >> 16;
    var useChildId = useWidgetHash & 65535;
    var onWidgetId = onWidgetHash >> 16;
    var onChildId = onWidgetHash & 65535;
    if (useSlot == 65535) {
      useSlot = -1;
    }
    if (onSlot == 65535) {
      onSlot = -1;
    }
    var message = "[ItemOnItem] useWidgetId=" + useWidgetId + "/" + WidgetId.valueOf(useWidgetId)
        + "; useChildId=" + useChildId + "; onWidgetId=" + onWidgetId + "/"
        + WidgetId.valueOf(onWidgetId) + "; onChildId=" + onChildId + "; useSlot=" + useSlot
        + "; onSlot=" + onSlot + "; useItemId=" + useItemId + "/" + ItemId.valueOf(useItemId)
        + "; onItemId=" + onItemId + "/" + ItemId.valueOf(onItemId);
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
    if (!player.getWidgetManager().hasWidget(useWidgetId)) {
      return;
    }
    if (!player.getWidgetManager().hasWidget(onWidgetId)) {
      return;
    }
    player.clearIdleTime();
    if (useWidgetId == WidgetId.INVENTORY && onWidgetId == WidgetId.INVENTORY
        && (player.getInventory().getId(useSlot) != useItemId
            || player.getInventory().getId(onSlot) != onItemId)) {
      return;
    }
    if (player.getController().itemOnItemHook(useWidgetId, useChildId, onWidgetId, onChildId,
        useSlot, onSlot, useItemId, onItemId)) {
      return;
    }
    for (var plugin : player.getPlugins()) {
      if (plugin.widgetOnWidgetHook(useWidgetId, useChildId, onWidgetId, onChildId, useSlot,
          useItemId, onSlot, onItemId)) {
        return;
      }
    }
    if (SkillContainer.widgetOnWidgetHooks(player, useWidgetId, useChildId, onWidgetId, onChildId,
        useSlot, useItemId, onSlot, onItemId)) {
      return;
    }
    ItemOnItemAction.doAction(player, useWidgetId, useChildId, onWidgetId, onChildId, useSlot,
        onSlot, useItemId, onItemId);
  }
}
