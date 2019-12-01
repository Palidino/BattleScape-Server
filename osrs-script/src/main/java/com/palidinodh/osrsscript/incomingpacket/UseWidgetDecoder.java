package com.palidinodh.osrsscript.incomingpacket;

import com.palidinodh.io.Stream;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.io.incomingpacket.InStreamKey;
import com.palidinodh.osrscore.io.incomingpacket.IncomingPacketDecoder;
import com.palidinodh.osrscore.io.incomingpacket.WidgetHandler;
import com.palidinodh.osrscore.model.map.route.Route;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.Runecrafting;
import com.palidinodh.osrscore.model.player.skill.SkillContainer;
import com.palidinodh.osrscore.util.RequestManager;
import com.palidinodh.osrscore.world.WorldEventHooks;
import com.palidinodh.osrsscript.incomingpacket.misc.ItemOnItemAction;
import com.palidinodh.osrsscript.incomingpacket.misc.UseWidgetAction;
import com.palidinodh.rs.setting.Settings;
import com.palidinodh.util.PLogger;
import lombok.var;

public class UseWidgetDecoder {
  static class WidgetOnWidgetDecoder extends IncomingPacketDecoder {
    @Override
    public boolean execute(Player player, Stream stream) {
      var useWidgetHash = getInt(InStreamKey.USE_WIDGET_HASH);
      var useWidgetId = WidgetHandler.getWidgetId(useWidgetHash);
      var useWidgetChildId = WidgetHandler.getWidgetChildId(useWidgetHash);
      var useWidgetSlot = getInt(InStreamKey.USE_WIDGET_SLOT);
      var useItemId = getInt(InStreamKey.USE_ITEM_ID);
      var onWidgetHash = getInt(InStreamKey.ON_WIDGET_HASH);
      var onWidgetId = WidgetHandler.getWidgetId(onWidgetHash);
      var onWidgetChildId = WidgetHandler.getWidgetChildId(onWidgetHash);
      var onWidgetSlot = getInt(InStreamKey.ON_WIDGET_SLOT);
      var onItemId = getInt(InStreamKey.ON_ITEM_ID);
      player.clearIdleTime();
      var message = "[WidgetOnWidget] useWidgetId=" + useWidgetId + "/"
          + WidgetId.valueOf(useWidgetId) + "; useWidgetChildId=" + useWidgetChildId
          + "; onWidgetId=" + onWidgetId + "/" + WidgetId.valueOf(onWidgetId) + "; onWidgetChildId="
          + onWidgetChildId + "; useItemId=" + useItemId + "/" + ItemId.valueOf(useItemId)
          + "; useWidgetSlot=" + useWidgetSlot + "; onItemId=" + onItemId + "/"
          + ItemId.valueOf(onItemId) + "; onWidgetSlot=" + onWidgetSlot;
      if (Settings.getInstance().isLocal()) {
        PLogger.println(message);
      }
      if (player.getOptions().getPrintPackets()) {
        player.getGameEncoder().sendMessage(message);
      }
      RequestManager.addUserPacketLog(player, message);
      if (player.isLocked() || player.getMovement().isViewing()) {
        return false;
      }
      if (!player.getWidgetManager().hasWidget(useWidgetId)) {
        return false;
      }
      if (!player.getWidgetManager().hasWidget(onWidgetId)) {
        return false;
      }
      if (useWidgetId == WidgetId.INVENTORY && onWidgetId == WidgetId.INVENTORY) {
        if (player.getInventory().getId(useWidgetSlot) != useItemId
            || player.getInventory().getId(onWidgetSlot) != onItemId) {
          return false;
        }
        if (player.getController().itemOnItemHook(useWidgetId, useWidgetChildId, onWidgetId,
            onWidgetChildId, useWidgetSlot, onWidgetSlot, useItemId, onItemId)) {
          return false;
        }
      }
      player.clearAllActions(false, false);
      return true;
    }

    @Override
    public boolean complete(Player player) {
      var useWidgetHash = getInt(InStreamKey.USE_WIDGET_HASH);
      var useWidgetId = WidgetHandler.getWidgetId(useWidgetHash);
      var onWidgetHash = getInt(InStreamKey.ON_WIDGET_HASH);
      var onWidgetId = WidgetHandler.getWidgetId(onWidgetHash);
      if (!player.getWidgetManager().hasWidget(useWidgetId)) {
        return true;
      }
      if (!player.getWidgetManager().hasWidget(onWidgetId)) {
        return true;
      }
      if (player.isLocked()) {
        return false;
      }
      if (player.getUseWidgetOnWidgetDelay() > 0) {
        return false;
      }
      player.setUseWidgetOnWidgetDelay(5);
      doAction(player);
      return true;
    }

    private void doAction(Player player) {
      var useWidgetHash = getInt(InStreamKey.USE_WIDGET_HASH);
      var useWidgetId = WidgetHandler.getWidgetId(useWidgetHash);
      var useWidgetChildId = WidgetHandler.getWidgetChildId(useWidgetHash);
      var useWidgetSlot = getInt(InStreamKey.USE_WIDGET_SLOT);
      var useItemId = getInt(InStreamKey.USE_ITEM_ID);
      var onWidgetHash = getInt(InStreamKey.ON_WIDGET_HASH);
      var onWidgetId = WidgetHandler.getWidgetId(onWidgetHash);
      var onWidgetChildId = WidgetHandler.getWidgetChildId(onWidgetHash);
      var onWidgetSlot = getInt(InStreamKey.ON_WIDGET_SLOT);
      var onItemId = getInt(InStreamKey.ON_ITEM_ID);
      if (player.getController().widgetOnWidgetHook(useWidgetId, useWidgetChildId, onWidgetId,
          onWidgetChildId, useWidgetSlot, useItemId, onWidgetSlot, onItemId)) {
        return;
      }
      for (var plugin : player.getPlugins()) {
        if (plugin.widgetOnWidgetHook(useWidgetId, useWidgetChildId, onWidgetId, onWidgetChildId,
            useWidgetSlot, useItemId, onWidgetSlot, onItemId)) {
          return;
        }
      }
      if (SkillContainer.widgetOnWidgetHooks(player, useWidgetId, useWidgetChildId, onWidgetId,
          onWidgetChildId, useWidgetSlot, useItemId, onWidgetSlot, onItemId)) {
        return;
      }
      if (useWidgetId == WidgetId.INVENTORY && onWidgetId == WidgetId.INVENTORY) {
        ItemOnItemAction.doAction(player, useWidgetId, useWidgetChildId, onWidgetId,
            onWidgetChildId, useWidgetSlot, onWidgetSlot, useItemId, onItemId);
      } else {
        UseWidgetAction.doActionWidgetOnWidget(player, useWidgetId, useWidgetChildId, onWidgetId,
            onWidgetChildId, useWidgetSlot, useItemId, onWidgetSlot, onItemId);
      }
    }
  }

  static class WidgetOnNpcDecoder extends IncomingPacketDecoder {
    @Override
    public boolean execute(Player player, Stream stream) {
      var widgetHash = getInt(InStreamKey.WIDGET_HASH);
      var widgetId = WidgetHandler.getWidgetId(widgetHash);
      var widgetChildId = WidgetHandler.getWidgetChildId(widgetHash);
      var widgetSlot = getInt(InStreamKey.WIDGET_SLOT);
      var itemId = getInt(InStreamKey.ITEM_ID);
      var targetIndex = getInt(InStreamKey.TARGET_INDEX);
      var ctrlRun = getInt(InStreamKey.CTRL_RUN);
      player.clearIdleTime();
      var npc = player.getWorld().getNpcByIndex(targetIndex);
      if (npc == null) {
        return false;
      }
      var message = "[WidgetOnNpc widgetId=" + widgetId + "/" + WidgetId.valueOf(widgetId)
          + "; widgetChildId=" + widgetChildId + "; itemId=" + itemId + "/" + ItemId.valueOf(itemId)
          + "; widgetSlot=" + widgetSlot + "; targetIndex=" + targetIndex + "; ctrlRun=" + ctrlRun
          + "; npc=" + npc.getId() + "/" + NpcId.valueOf(npc.getId());
      if (Settings.getInstance().isLocal()) {
        PLogger.println(message);
      }
      if (player.getOptions().getPrintPackets()) {
        player.getGameEncoder().sendMessage(message);
      }
      RequestManager.addUserPacketLog(player, message);
      if (player.isLocked()) {
        return false;
      }
      player.clearAllActions(false, true);
      player.setFaceEntity(npc);
      player.getMovement().setFollowing(npc);
      if (widgetId == WidgetId.SPELLBOOK) {
        player.getMagic().setSingleSpellId(widgetChildId);
        player.setAttacking(true);
        player.setInteractingEntity(npc);
        player.getCombat().setFollowing(npc);
        player.getMovement().follow();
        player.setFaceTile(npc);
        return false;
      }
      player.getMovement().fullRoute(npc, ctrlRun);
      return true;
    }

    @Override
    public boolean complete(Player player) {
      var widgetHash = getInt(InStreamKey.WIDGET_HASH);
      var widgetId = WidgetHandler.getWidgetId(widgetHash);
      var widgetChildId = WidgetHandler.getWidgetChildId(widgetHash);
      var widgetSlot = getInt(InStreamKey.WIDGET_SLOT);
      var targetIndex = getInt(InStreamKey.TARGET_INDEX);
      var npc = player.getWorld().getNpcByIndex(targetIndex);
      if (npc == null) {
        return true;
      }
      if (!player.getWidgetManager().hasWidget(widgetId)) {
        return true;
      }
      if (player.isLocked()) {
        return false;
      }
      if (!player.withinDistance(npc, 1)) {
        return false;
      }
      if (!Route.canReachEntity(player, npc, false)) {
        return false;
      }
      player.getMovement().clear();
      player.setFaceTile(npc);
      if (player.getController().widgetOnEntityHook(widgetId, widgetChildId, widgetSlot, npc)) {
        return true;
      }
      UseWidgetAction.doActionNpc(player, widgetId, widgetChildId, widgetSlot, npc);
      return true;
    }
  }

  static class WidgetOnPlayerDecoder extends IncomingPacketDecoder {
    @Override
    public boolean execute(Player player, Stream stream) {
      var widgetHash = getInt(InStreamKey.WIDGET_HASH);
      var widgetId = WidgetHandler.getWidgetId(widgetHash);
      var widgetChildId = WidgetHandler.getWidgetChildId(widgetHash);
      var widgetSlot = getInt(InStreamKey.WIDGET_SLOT);
      var itemId = getInt(InStreamKey.ITEM_ID);
      var targetIndex = getInt(InStreamKey.TARGET_INDEX);
      var ctrlRun = getInt(InStreamKey.CTRL_RUN);
      player.clearIdleTime();
      var targetPlayer = player.getWorld().getPlayerByIndex(targetIndex);
      if (targetPlayer == null) {
        return false;
      }
      var message = "[WidgetOnPlayer widgetId=" + widgetId + "/" + WidgetId.valueOf(widgetId)
          + "; widgetChildId=" + widgetChildId + "; itemId=" + itemId + "/" + ItemId.valueOf(itemId)
          + "; widgetSlot=" + widgetSlot + "; targetIndex=" + targetIndex + "; ctrlRun=" + ctrlRun
          + "; player=" + targetPlayer.getId() + "/" + targetPlayer.getUsername();
      if (Settings.getInstance().isLocal()) {
        PLogger.println(message);
      }
      if (player.getOptions().getPrintPackets()) {
        player.getGameEncoder().sendMessage(message);
      }
      RequestManager.addUserPacketLog(player, message);
      if (player.isLocked()) {
        return false;
      }
      player.clearAllActions(false, true);
      player.setFaceEntity(targetPlayer);
      player.getMovement().setFollowing(targetPlayer);
      if (widgetId == WidgetId.SPELLBOOK) {
        player.getMagic().setSingleSpellId(widgetChildId);
        player.setAttacking(true);
        player.setInteractingEntity(targetPlayer);
        player.getCombat().setFollowing(targetPlayer);
        player.getMovement().follow();
        player.setFaceTile(targetPlayer);
        return false;
      }
      player.getMovement().fullRoute(targetPlayer, ctrlRun);
      return true;
    }

    @Override
    public boolean complete(Player player) {
      var widgetHash = getInt(InStreamKey.WIDGET_HASH);
      var widgetId = WidgetHandler.getWidgetId(widgetHash);
      var widgetChildId = WidgetHandler.getWidgetChildId(widgetHash);
      var widgetSlot = getInt(InStreamKey.WIDGET_SLOT);
      var targetIndex = getInt(InStreamKey.TARGET_INDEX);
      var targetPlayer = player.getWorld().getPlayerByIndex(targetIndex);
      if (targetPlayer == null) {
        return true;
      }
      if (!player.getWidgetManager().hasWidget(widgetId)) {
        return true;
      }
      if (player.isLocked()) {
        return false;
      }
      if (!player.withinDistance(targetPlayer, 1)) {
        return false;
      }
      if (!Route.canReachEntity(player, targetPlayer, false)) {
        return false;
      }
      player.getMovement().clear();
      player.setFaceTile(targetPlayer);
      if (player.getController().widgetOnEntityHook(widgetId, widgetChildId, widgetSlot,
          targetPlayer)) {
        return true;
      }
      return true;
    }
  }

  static class WidgetOnMapObjectDecoder extends IncomingPacketDecoder {
    @Override
    public boolean execute(Player player, Stream stream) {
      var widgetHash = getInt(InStreamKey.WIDGET_HASH);
      var widgetId = WidgetHandler.getWidgetId(widgetHash);
      var widgetChildId = WidgetHandler.getWidgetChildId(widgetHash);
      var widgetSlot = getInt(InStreamKey.WIDGET_SLOT);
      var itemId = getInt(InStreamKey.ITEM_ID);
      var mapObjectId = getInt(InStreamKey.MAP_OBJECT_ID);
      var tileX = getInt(InStreamKey.TILE_X);
      var tileY = getInt(InStreamKey.TILE_Y);
      var ctrlRun = getInt(InStreamKey.CTRL_RUN);
      player.clearIdleTime();
      var mapObject =
          player.getController().getMapObject(mapObjectId, tileX, tileY, player.getClientHeight());
      if (mapObject == null) {
        return false;
      }
      if (player.getHeight() != player.getClientHeight()) {
        if (mapObject.getDef().hasOption("open") || mapObject.getDef().hasOption("close")) {
          return false;
        }
      }
      var message = "[WidgetOnMapObject] widgetId=" + widgetId + "; widgetChildId=" + widgetChildId
          + "; widgetSlot=" + widgetSlot + "; itemId=" + itemId + "; mapObjectId=" + mapObjectId
          + "; tileX=" + tileX + "; tileY=" + tileY + "; ctrlRun=" + ctrlRun;
      if (Settings.getInstance().isLocal()) {
        PLogger.println(message);
      }
      if (player.getOptions().getPrintPackets()) {
        player.getGameEncoder().sendMessage(message);
      }
      if (player.isLocked()) {
        return false;
      }
      player.clearAllActions(false, true);
      player.getMovement().fullRoute(mapObject, ctrlRun);
      return true;
    }

    @Override
    public boolean complete(Player player) {
      var widgetHash = getInt(InStreamKey.WIDGET_HASH);
      var widgetId = WidgetHandler.getWidgetId(widgetHash);
      var widgetChildId = WidgetHandler.getWidgetChildId(widgetHash);
      var widgetSlot = getInt(InStreamKey.WIDGET_SLOT);
      var itemId = getInt(InStreamKey.ITEM_ID);
      var mapObjectId = getInt(InStreamKey.MAP_OBJECT_ID);
      var tileX = getInt(InStreamKey.TILE_X);
      var tileY = getInt(InStreamKey.TILE_Y);
      var mapObject =
          player.getController().getMapObject(mapObjectId, tileX, tileY, player.getClientHeight());
      if (mapObject == null) {
        return true;
      }
      if (!player.getWidgetManager().hasWidget(widgetId)) {
        return true;
      }
      var range = 1;
      if (mapObject.getType() >= 4 && mapObject.getType() <= 8) {
        range = 0;
      }
      var canReach = !player.getMovement().isRouting() && player.withinDistanceC(mapObject, range);
      if (mapObject.getId() == 5249) {
        canReach = player.withinDistanceC(mapObject, range);
      }
      if (player.isLocked()) {
        return false;
      }
      if (!canReach) {
        return false;
      }
      player.getMovement().clear();
      player.setFaceTile(mapObject);
      if (widgetId == WidgetId.INVENTORY && player.getInventory().getId(widgetSlot) != itemId) {
        return true;
      }
      if (player.getController().widgetOnMapObjectHook(widgetId, widgetChildId, widgetSlot,
          mapObject)) {
        return true;
      }
      for (var plugin : player.getPlugins()) {
        if (plugin.widgetOnMapObjectHook(widgetId, widgetChildId, widgetSlot, itemId, mapObject)) {
          return true;
        }
      }
      for (var event : player.getWorld().getWorldEvents()) {
        if (!(event instanceof WorldEventHooks)) {
          continue;
        }
        if (((WorldEventHooks) event).widgetOnMapObjectHook(player, widgetId, widgetChildId,
            widgetSlot, itemId, mapObject)) {
          return true;
        }
      }
      if (SkillContainer.widgetOnMapObjectHooks(player, widgetId, widgetChildId, widgetSlot, itemId,
          mapObject)) {
        return true;
      }
      if (player.getFarming().widgetOnMapObjectHook(widgetId, widgetChildId, widgetSlot,
          mapObject)) {
        return true;
      }
      if (Runecrafting.widgetOnMapObjectHook(player, widgetId, widgetChildId, widgetSlot,
          mapObject)) {
        return true;
      }
      UseWidgetAction.doActionMapObject(player, widgetId, widgetChildId, widgetSlot, mapObject);
      return true;
    }
  }

  public static boolean used(int useItemId, int onItemId, int itemId1, int itemId2) {
    return useItemId == itemId1 && onItemId == itemId2
        || useItemId == itemId2 && onItemId == itemId1;
  }

  public static boolean hasMatch(int useItemId, int onItemId, int... options) {
    return getMatch(useItemId, onItemId, options) != -1;
  }

  public static int getMatch(int useItemId, int onItemId, int... options) {
    for (int option : options) {
      if (useItemId == option) {
        return option;
      }
      if (onItemId == option) {
        return option;
      }
    }
    return -1;
  }
}
