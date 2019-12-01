package com.palidinodh.osrsscript.incomingpacket.widget;

import com.palidinodh.osrscore.io.incomingpacket.WidgetHandler;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;

@ReferenceId(WidgetId.MINIMAP_ORBS)
class MinimapOrbWidget implements WidgetHandler {
  @Override
  public void execute(Player player, int option, int widgetId, int childId, int slot, int itemId) {
    switch (childId) {
      case 1:
        if (option == 0) {
          player.getXPDrops().switchShow();
        } else if (option == 1) {
          player.getXPDrops().openSetup();
        }
        break;
      case 14:
        if (option == 0) {
          player.getPrayer().activateQuick();
        } else if (option == 1) {
          player.getPrayer().switchQuick();
        }
        break;
      case 22:
        player.getMovement().setRunning(!player.getMovement().getRunning());
        break;
      case 30:
        player.getCombat().activateSpecialAttack();
        break;
      case 43:
        if (option == 0) {
          player.getWidgetManager().changeWorldMapState();
        } else if (option == 1) {
          player.getWidgetManager().sendWorldMap(true);
        }
        break;
    }
  }
}
