package com.palidinodh.osrsscript.player.plugin.combat;

import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrsscript.player.plugin.combat.dialogue.CombatLampLevel99Dialogue;
import lombok.var;

public class InventoryEquipmentWidgetHook {
  private Player player;
  @SuppressWarnings("unused")
  private CombatPlugin plugin;

  public InventoryEquipmentWidgetHook(Player player, CombatPlugin plugin) {
    this.player = player;
    this.plugin = plugin;
  }

  public boolean widgetHook(int index, int widgetId, int childId, int slot, int itemId) {
    var isEquipment = widgetId == WidgetId.EQUIPMENT || widgetId == WidgetId.EQUIPMENT_BONUSES;
    if (widgetId == WidgetId.INVENTORY || isEquipment) {
      switch (itemId) {
        case ItemId.COMBAT_LAMP_LEVEL_99_32337:
          new CombatLampLevel99Dialogue(player);
          return true;
      }
    }
    return false;
  }
}
