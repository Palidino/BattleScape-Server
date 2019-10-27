package com.palidinodh.osrsscript.packetdecoder;

import java.util.HashMap;
import java.util.Map;
import com.palidinodh.io.FileManager;
import com.palidinodh.io.Stream;
import com.palidinodh.osrscore.io.PacketDecoder;
import com.palidinodh.osrscore.io.Widget;
import com.palidinodh.osrscore.io.cache.ItemId;
import com.palidinodh.osrscore.io.cache.WidgetId;
import com.palidinodh.osrscore.model.player.AchievementDiary;
import com.palidinodh.osrscore.model.player.Equipment;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.skill.SkillContainer;
import com.palidinodh.osrscore.util.RequestManager;
import com.palidinodh.rs.setting.Settings;
import com.palidinodh.util.PLogger;
import lombok.var;

public class WidgetDecoder extends PacketDecoder {
  private static Map<Integer, Widget> widgets = new HashMap<>();

  /*
   * 0-9: normal widget option; 10: old widget option; 11: dialogue option
   */
  public WidgetDecoder() {
    super(68, 21, 48, 19, 40, 66, 85, 14, 84, 0, 69, 70);
  }

  @Override
  public void execute(Player player, int index, int size, Stream stream) {
    var widgetHash = -1;
    var slot = -1;
    var itemId = -1;
    if (index == 11) {
      widgetHash = stream.getIntV3();
      slot = stream.getUShort128();
    } else {
      widgetHash = stream.getInt();
      slot = -1;
      itemId = -1;
      if (size == 8) {
        slot = stream.getUShort();
        itemId = stream.getUShort();
      }
    }
    var widgetId = widgetHash >> 16;
    var childId = widgetHash & 65535;
    if (slot == 65535) {
      slot = -1;
    }
    if (itemId == 65535) {
      itemId = -1;
    }
    var message = "[Widget(" + index + ")] widgetId=" + widgetId + "/" + WidgetId.valueOf(widgetId)
        + "; childId=" + childId + "; slot=" + slot + "; itemId=" + itemId + "/"
        + ItemId.valueOf(itemId);
    if (Settings.getInstance().isLocal()) {
      PLogger.println(message);
    }
    if (player.getOptions().getPrintPackets()) {
      player.getGameEncoder().sendMessage(message);
    }
    RequestManager.addUserPacketLog(player, message);
    if (!player.getWidgetManager().hasWidget(widgetId)) {
      return;
    }
    if (index >= 10) {
      index = 0;
    }
    player.clearIdleTime();
    execute(player, index, widgetId, childId, slot, itemId);
  }

  public static void execute(Player player, int index, int widgetId, int childId, int slot,
      int itemId) {
    if (widgetId == WidgetId.INVENTORY && player.getInventory().getId(slot) != itemId) {
      return;
    }
    if (widgetId == WidgetId.EQUIPMENT || widgetId == WidgetId.EQUIPMENT_BONUSES) {
      var equipmentSlot = Equipment.Slot.getFromWidget(widgetId, childId);
      if (equipmentSlot != null) {
        slot = equipmentSlot.ordinal();
        itemId = player.getEquipment().getId(equipmentSlot);
      }
    }
    if ((widgetId == WidgetId.INVENTORY || widgetId == WidgetId.EQUIPMENT
        || widgetId == WidgetId.EQUIPMENT_BONUSES) && player.isLocked()) {
      return;
    }
    AchievementDiary.widgetUpdate(player, index, widgetId, childId, slot, itemId);
    if (player.getController().widgetHook(index, widgetId, childId, slot, itemId)) {
      return;
    }
    for (var plugin : player.getPlugins()) {
      if (plugin.widgetHook(index, widgetId, childId, slot, itemId)) {
        return;
      }
    }
    if (player.getRandomEvent().widgetHook(index, widgetId, childId, slot, itemId)) {
      return;
    }
    if (SkillContainer.widgets(player, index, widgetId, childId, slot, itemId)) {
      return;
    }
    var widget = widgets.get(widgetId);
    if (widget != null) {
      widget.execute(player, index, widgetId, childId, slot, itemId);
    }
  }

  static {
    try {
      var classes =
          FileManager.getClasses(Widget.class, "com.palidinodh.osrsscript.packetdecoder.widget");
      for (var clazz : classes) {
        var classInstance = (Widget) clazz.newInstance();
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
