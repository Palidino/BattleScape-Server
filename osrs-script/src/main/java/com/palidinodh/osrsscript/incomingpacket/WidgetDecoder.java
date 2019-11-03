package com.palidinodh.osrsscript.incomingpacket;

import java.util.HashMap;
import java.util.Map;
import com.palidinodh.io.FileManager;
import com.palidinodh.io.Stream;
import com.palidinodh.osrscore.io.incomingpacket.WidgetHandler;
import com.palidinodh.osrscore.io.cache.ItemId;
import com.palidinodh.osrscore.io.cache.WidgetId;
import com.palidinodh.osrscore.io.incomingpacket.InStreamKey;
import com.palidinodh.osrscore.io.incomingpacket.IncomingPacketDecoder;
import com.palidinodh.osrscore.model.player.AchievementDiary;
import com.palidinodh.osrscore.model.player.Equipment;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.skill.SkillContainer;
import com.palidinodh.osrscore.util.RequestManager;
import com.palidinodh.rs.setting.Settings;
import com.palidinodh.util.PLogger;
import lombok.var;

public class WidgetDecoder extends IncomingPacketDecoder {
  private static Map<Integer, WidgetHandler> widgets = new HashMap<>();

  @Override
  public boolean execute(Player player, Stream stream) {
    var option = getInt(InStreamKey.PACKET_OPTION);
    var widgetHash = getInt(InStreamKey.WIDGET_HASH);
    var widgetId = WidgetHandler.getWidgetId(widgetHash);
    var widgetChildId = WidgetHandler.getWidgetChildId(widgetHash);
    var widgetSlot = getInt(InStreamKey.WIDGET_SLOT);
    var itemId = getInt(InStreamKey.ITEM_ID);
    player.clearIdleTime();
    var message = "[Widget(" + option + ")] widgetId=" + widgetId + "/" + WidgetId.valueOf(widgetId)
        + "; widgetChildId=" + widgetChildId + "; widgetSlot=" + widgetSlot + "; itemId=" + itemId
        + "/" + ItemId.valueOf(itemId);
    if (Settings.getInstance().isLocal()) {
      PLogger.println(message);
    }
    if (player.getOptions().getPrintPackets()) {
      player.getGameEncoder().sendMessage(message);
    }
    RequestManager.addUserPacketLog(player, message);
    if (!player.getWidgetManager().hasWidget(widgetId)) {
      return false;
    }
    if (widgetId == WidgetId.INVENTORY && player.getInventory().getId(widgetSlot) != itemId) {
      return false;
    }
    if (widgetId == WidgetId.EQUIPMENT || widgetId == WidgetId.EQUIPMENT_BONUSES) {
      var equipmentSlot = Equipment.Slot.getFromWidget(widgetId, widgetChildId);
      if (equipmentSlot != null) {
        widgetSlot = equipmentSlot.ordinal();
        itemId = player.getEquipment().getId(equipmentSlot);
      }
    }
    if ((widgetId == WidgetId.INVENTORY || widgetId == WidgetId.EQUIPMENT
        || widgetId == WidgetId.EQUIPMENT_BONUSES)
        && (player.isLocked() || player.getMovement().isViewing())) {
      return false;
    }
    AchievementDiary.widgetUpdate(player, option, widgetId, widgetChildId, widgetSlot, itemId);
    if (player.getController().widgetHook(option, widgetId, widgetChildId, widgetSlot, itemId)) {
      return false;
    }
    for (var plugin : player.getPlugins()) {
      if (plugin.widgetHook(option, widgetId, widgetChildId, widgetSlot, itemId)) {
        return false;
      }
    }
    if (player.getRandomEvent().widgetHook(option, widgetId, widgetChildId, widgetSlot, itemId)) {
      return false;
    }
    if (SkillContainer.widgets(player, option, widgetId, widgetChildId, widgetSlot, itemId)) {
      return false;
    }
    var widget = widgets.get(widgetId);
    if (widget != null) {
      widget.execute(player, option, widgetId, widgetChildId, widgetSlot, itemId);
    }
    return true;
  }

  static {
    try {
      var classes = FileManager.getClasses(WidgetHandler.class,
          "com.palidinodh.osrsscript.incomingpacket.widget");
      for (var clazz : classes) {
        var classInstance = (WidgetHandler) clazz.newInstance();
        for (var widgetId : classInstance.getIds()) {
          if (widgets.containsKey(widgetId)) {
            throw new Exception(clazz.getName() + " - " + widgetId + ": widget id already used.");
          }
          widgets.put(widgetId, classInstance);
        }
      }
    } catch (Exception e) {
      PLogger.error(e);
    }
  }
}
