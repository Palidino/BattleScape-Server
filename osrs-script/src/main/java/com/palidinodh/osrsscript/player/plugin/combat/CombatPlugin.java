package com.palidinodh.osrsscript.player.plugin.combat;

import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.PlayerPlugin;
import lombok.var;

public class CombatPlugin extends PlayerPlugin {
  private transient Player player;
  private transient InventoryEquipmentWidgetHook inventoryEquipmentWidgetHook;

  @Override
  public void login() {
    player = getPlayer();
    inventoryEquipmentWidgetHook = new InventoryEquipmentWidgetHook(player, this);
  }

  @Override
  public boolean widgetHook(int option, int widgetId, int childId, int slot, int itemId) {
    var isEquipment = widgetId == WidgetId.EQUIPMENT || widgetId == WidgetId.EQUIPMENT_BONUSES;
    if (widgetId == WidgetId.INVENTORY || isEquipment) {
      return inventoryEquipmentWidgetHook.widgetHook(option, widgetId, childId, slot, itemId);
    }
    return false;
  }
}
