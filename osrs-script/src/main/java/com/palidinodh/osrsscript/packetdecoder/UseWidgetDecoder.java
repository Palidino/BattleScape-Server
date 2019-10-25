package com.palidinodh.osrsscript.packetdecoder;

import com.palidinodh.osrscore.Main;
import com.palidinodh.osrscore.io.PacketDecoder;
import com.palidinodh.osrscore.io.cache.ItemId;
import com.palidinodh.osrscore.io.cache.WidgetId;
import com.palidinodh.osrscore.model.map.route.Route;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.Runecrafting;
import com.palidinodh.osrscore.model.player.skill.SkillContainer;
import com.palidinodh.osrscore.util.RequestManager;
import com.palidinodh.osrscore.world.WorldEventHooks;
import com.palidinodh.osrsscript.packetdecoder.misc.UseWidgetAction;
import com.palidinodh.io.Stream;
import com.palidinodh.util.PLogger;
import lombok.var;

public class UseWidgetDecoder extends PacketDecoder {
  public UseWidgetDecoder() {
    super(86, 55, 23, 81, 2, 44, 92);
  }

  @Override
  public void execute(Player player, int index, int size, Stream stream) {
    if (index == 0 || index == 1 || index == 2 || index == 3) {
      executeWidgetOnEntity(player, index, size, stream);
    } else if (index == 4) {
      executeWidgetOnWidget(player, index, size, stream);
    } else if (index == 5 || index == 6) {
      executeWidgetOnMapObject(player, index, size, stream);
    }
  }

  @Override
  public boolean complete(Player player) {
    var index = player.getAttributeInt("packet_decoder_index");
    if (index == 0 || index == 1 || index == 2 || index == 3) {
      return completeWidgetOnEntity(player);
    } else if (index == 4) {
      return completeWidgetOnWidget(player);
    } else if (index == 5 || index == 6) {
      return completeWidgetOnMapObject(player);
    }
    return true;
  }

  @Override
  public void stop(Player player) {
    player.removeAttribute("packet_decoder_index");
    player.removeAttribute("packet_decoder_widget_id");
    player.removeAttribute("packet_decoder_child_id");
    player.removeAttribute("packet_decoder_slot");
    player.removeAttribute("packet_decoder_item_id");
    player.removeAttribute("packet_decoder_npc_index");
    player.removeAttribute("packet_decoder_player_index");
    player.removeAttribute("packet_decoder_object_id");
    player.removeAttribute("packet_decoder_object_x");
    player.removeAttribute("packet_decoder_object_y");
    player.removeAttribute("packet_decoder_use_widget_id");
    player.removeAttribute("packet_decoder_use_child_id");
    player.removeAttribute("packet_decoder_on_widget_id");
    player.removeAttribute("packet_decoder_on_child_id");
    player.removeAttribute("packet_decoder_use_slot");
    player.removeAttribute("packet_decoder_use_item_id");
    player.removeAttribute("packet_decoder_on_slot");
    player.removeAttribute("packet_decoder_on_item_id");
  }

  private void executeWidgetOnEntity(Player player, int index, int size, Stream stream) {
    var widgetHash = -1;
    var slot = -1;
    var itemId = -1;
    var id = -1;
    var moveType = 0;
    if (index == 0) {
      id = stream.getUShortLE128();
      moveType = stream.getUReversedByte();
      widgetHash = stream.getIntLE();
      slot = stream.getUShort128();
    } else if (index == 1) {
      widgetHash = stream.getIntV3();
      moveType = stream.getU128Byte();
      slot = stream.getUShort128();
      id = stream.getUShort();
    } else if (index == 2) {
      itemId = stream.getUShortLE128();
      slot = stream.getUShort();
      id = stream.getUShortLE128();
      moveType = stream.getUReversedByte();
      widgetHash = stream.getInt();
    } else if (index == 3) {
      slot = stream.getUShortLE128();
      moveType = stream.getU128Byte();
      widgetHash = stream.getIntV2();
      id = stream.getUShort();
      itemId = stream.getUShort();
    }
    var widgetId = widgetHash >> 16;
    var childId = widgetHash & 65535;
    if (slot == 65535) {
      slot = -1;
    }
    var entity = index == 0 || index == 2 ? player.getWorld().getNPCByIndex(id)
        : player.getWorld().getPlayerByIndex(id);
    if (entity == null) {
      return;
    }
    var message =
        "[WidgetOnEntity(" + index + ")] widgetId=" + widgetId + "/" + WidgetId.valueOf(widgetId)
            + "; childId=" + childId + "; itemId=" + itemId + "/" + ItemId.valueOf(itemId)
            + "; slot=" + slot + "; id=" + id + "; moveType=" + moveType + "; entity="
            + (entity instanceof Player ? ((Player) entity).getUsername() : entity.getId());
    if (Main.isLocal()) {
      PLogger.println(message);
    }
    if (player.getOptions().getPrintPackets()) {
      player.getGameEncoder().sendMessage(message);
    }
    RequestManager.addUserPacketLog(player, message);
    if (player.isLocked()) {
      return;
    }
    player.clearIdleTime();
    player.clearAllActions(false, true);
    player.setFaceEntity(entity);
    player.getMovement().setFollowing(entity);
    if (widgetId == WidgetId.SPELLBOOK) {
      player.getMagic().setSingleSpellId(childId);
      player.setAttacking(true);
      player.setInteractingEntity(entity);
      player.getCombat().setFollowing(entity);
      player.getMovement().follow();
      player.setFaceTile(entity);
      return;
    }
    player.getMovement().fullRoute(entity, moveType);
    player.putAttribute("packet_decoder_index", index);
    player.putAttribute("packet_decoder_widget_id", widgetId);
    player.putAttribute("packet_decoder_child_id", childId);
    player.putAttribute("packet_decoder_slot", slot);
    player.putAttribute(
        entity instanceof Npc ? "packet_decoder_npc_index" : "packet_decoder_player_index", id);
    if (complete(player)) {
      stop(player);
      return;
    }
    player.putAttribute("packet_decoder", this);
  }

  private boolean completeWidgetOnEntity(Player player) {
    var index = player.getAttributeInt("packet_decoder_index");
    var widgetId = player.getAttributeInt("packet_decoder_widget_id");
    var childId = player.getAttributeInt("packet_decoder_child_id");
    var slot = player.getAttributeInt("packet_decoder_slot");
    var id = player.getAttributeInt(
        index == 0 || index == 2 ? "packet_decoder_npc_index" : "packet_decoder_player_index");
    var entity = index == 0 || index == 2 ? player.getWorld().getNPCByIndex(id)
        : player.getWorld().getPlayerByIndex(id);
    if (entity == null) {
      return true;
    }
    if (!player.getWidgetManager().hasWidget(widgetId)) {
      return true;
    }
    if (player.isLocked()) {
      return false;
    }
    if (!player.withinDistance(entity, 1)) {
      return false;
    }
    if (!Route.canReachEntity(player, entity, false)) {
      return false;
    }
    player.getMovement().clear();
    player.setFaceTile(entity);
    if (player.getController().widgetOnEntityHook(widgetId, childId, slot, entity)) {
      return true;
    }
    if (entity instanceof Npc) {
      UseWidgetAction.doActionNpc(player, index, widgetId, childId, slot, (Npc) entity);
    } else if (entity instanceof Player) {
      UseWidgetAction.doActionPlayer(player, index, widgetId, childId, slot, (Player) entity);
    }
    return true;
  }

  private void executeWidgetOnWidget(Player player, int index, int size, Stream stream) {
    var useWidgetHash = stream.getIntV2();
    var onWidgetHash = stream.getInt();
    var onSlot = stream.getUShort();
    var onItemId = stream.getUShortLE();
    var useItemId = stream.getUShortLE();
    var useWidgetId = useWidgetHash >> 16;
    var onWidgetId = onWidgetHash >> 16;
    var useChildId = useWidgetHash & 65535;
    var onChildId = onWidgetHash & 65535;
    var useSlot = -1;
    if (useItemId == 65535) {
      useItemId = -1;
    }
    if (onItemId == 65535) {
      onItemId = -1;
    }
    if (useSlot == 65535) {
      useSlot = -1;
    }
    if (onSlot == 65535) {
      onSlot = -1;
    }
    var message = "[WidgetOnWidget] useWidgetId=" + useWidgetId + "/"
        + WidgetId.valueOf(useWidgetId) + "; useChildId=" + useChildId + "; onWidgetId="
        + onWidgetId + "/" + WidgetId.valueOf(onWidgetId) + "; onChildId=" + onChildId
        + "; useItemId=" + useItemId + "/" + ItemId.valueOf(useItemId) + "; onItemId=" + onItemId
        + "/" + ItemId.valueOf(onItemId) + "; onSlot=" + onSlot;
    if (Main.isLocal()) {
      PLogger.println(message);
    }
    if (player.getOptions().getPrintPackets()) {
      player.getGameEncoder().sendMessage(message);
    }
    RequestManager.addUserPacketLog(player, message);
    if (player.isLocked()) {
      return;
    }
    player.clearAllActions(false, false);
    player.putAttribute("packet_decoder_index", index);
    player.putAttribute("packet_decoder_use_widget_id", useWidgetId);
    player.putAttribute("packet_decoder_use_child_id", useChildId);
    player.putAttribute("packet_decoder_on_widget_id", onWidgetId);
    player.putAttribute("packet_decoder_on_child_id", onChildId);
    player.putAttribute("packet_decoder_use_item_id", useItemId);
    player.putAttribute("packet_decoder_use_slot", useSlot);
    player.putAttribute("packet_decoder_on_slot", onSlot);
    player.putAttribute("packet_decoder_on_item_id", onItemId);
    if (complete(player)) {
      stop(player);
      return;
    }
    player.putAttribute("packet_decoder", this);
  }

  private boolean completeWidgetOnWidget(Player player) {
    var useWidgetId = player.getAttributeInt("packet_decoder_use_widget_id");
    var useChildId = player.getAttributeInt("packet_decoder_use_child_id");
    var onWidgetId = player.getAttributeInt("packet_decoder_on_widget_id");
    var onChildId = player.getAttributeInt("packet_decoder_on_child_id");
    var useSlot = player.getAttributeInt("packet_decoder_use_slot");
    var useItemId = player.getAttributeInt("packet_decoder_use_item_id");
    var onSlot = player.getAttributeInt("packet_decoder_on_slot");
    var onItemId = player.getAttributeInt("packet_decoder_on_item_id");
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
    if (player.getController().widgetOnWidgetHook(useWidgetId, useChildId, onWidgetId, onChildId,
        useSlot, useItemId, onSlot, onItemId)) {
      return true;
    }
    for (var plugin : player.getPlugins()) {
      if (plugin.widgetOnWidgetHook(useWidgetId, useChildId, onWidgetId, onChildId, useSlot,
          useItemId, onSlot, onItemId)) {
        return true;
      }
    }
    if (SkillContainer.widgetOnWidgetHooks(player, useWidgetId, useChildId, onWidgetId, onChildId,
        useSlot, useItemId, onSlot, onItemId)) {
      return true;
    }
    UseWidgetAction.doActionWidgetOnWidget(player, useWidgetId, useChildId, onWidgetId, onChildId,
        useSlot, useItemId, onSlot, onItemId);
    return true;
  }

  private void executeWidgetOnMapObject(Player player, int index, int size, Stream stream) {
    var x = 0;
    var y = 0;
    var id = -1;
    var slot = -1;
    var itemId = -1;
    var widgetHash = -1;
    var moveType = 0;
    if (index - 5 == 0) {
      y = stream.getUShortLE128();
      slot = stream.getUShortLE();
      moveType = stream.getUByte128();
      widgetHash = stream.getIntLE();
      x = stream.getUShortLE128();
      id = stream.getUShort();
      itemId = stream.getUShortLE();
    } else if (index - 5 == 1) {
      moveType = stream.getUByte();
      x = stream.getUShort();
      id = stream.getUShort128();
      widgetHash = stream.getIntV3();
      itemId = stream.getUShort();
      y = stream.getUShortLE();
    }
    var widgetId = widgetHash >> 16;
    var childId = widgetHash & 65535;
    if (slot == 65535) {
      slot = -1;
    }
    var mapObject = player.getController().getMapObject(id, x, y, player.getClientHeight());
    if (mapObject == null) {
      return;
    }
    if (player.getHeight() != player.getClientHeight()) {
      if (mapObject.getDef().hasOption("open") || mapObject.getDef().hasOption("close")) {
        return;
      }
    }
    var message = "[WidgetOnMapObject] widgetId=" + widgetId + "; childId=" + childId + "; slot="
        + slot + "; itemId=" + itemId + "; id=" + id + "; x=" + x + "; y=" + y + "; moveType="
        + moveType;
    if (Main.isLocal()) {
      PLogger.println(message);
    }
    if (player.getOptions().getPrintPackets()) {
      player.getGameEncoder().sendMessage(message);
    }
    if (player.isLocked()) {
      return;
    }
    player.clearAllActions(false, true);
    player.getMovement().fullRoute(mapObject, moveType);
    player.putAttribute("packet_decoder_index", index);
    player.putAttribute("packet_decoder_widget_id", widgetId);
    player.putAttribute("packet_decoder_child_id", childId);
    player.putAttribute("packet_decoder_slot", slot);
    player.putAttribute("packet_decoder_item_id", itemId);
    player.putAttribute("packet_decoder_object_id", id);
    player.putAttribute("packet_decoder_object_x", x);
    player.putAttribute("packet_decoder_object_y", y);
    if (complete(player)) {
      stop(player);
      return;
    }
    player.putAttribute("packet_decoder", this);
  }

  private boolean completeWidgetOnMapObject(Player player) {
    var widgetId = player.getAttributeInt("packet_decoder_widget_id");
    var childId = player.getAttributeInt("packet_decoder_child_id");
    var slot = player.getAttributeInt("packet_decoder_slot");
    var itemId = player.getAttributeInt("packet_decoder_item_id");
    var id = player.getAttributeInt("packet_decoder_object_id");
    var x = player.getAttributeInt("packet_decoder_object_x");
    var y = player.getAttributeInt("packet_decoder_object_y");
    var mapObject = player.getController().getMapObject(id, x, y, player.getClientHeight());
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
    if (widgetId == WidgetId.INVENTORY && player.getInventory().getId(slot) != itemId) {
      return true;
    }
    if (player.getController().widgetOnMapObjectHook(widgetId, childId, slot, mapObject)) {
      return true;
    }
    for (var plugin : player.getPlugins()) {
      if (plugin.widgetOnMapObjectHook(widgetId, childId, slot, itemId, mapObject)) {
        return true;
      }
    }
    for (var event : player.getWorld().getWorldEvents()) {
      if (!(event instanceof WorldEventHooks)) {
        continue;
      }
      if (((WorldEventHooks) event).widgetOnMapObjectHook(player, widgetId, childId, slot, itemId,
          mapObject)) {
        return true;
      }
    }
    if (SkillContainer.widgetOnMapObjectHooks(player, widgetId, childId, slot, itemId, mapObject)) {
      return true;
    }
    if (player.getFarming().widgetOnMapObjectHook(widgetId, childId, slot, mapObject)) {
      return true;
    }
    if (Runecrafting.widgetOnMapObjectHook(player, widgetId, childId, slot, mapObject)) {
      return true;
    }
    UseWidgetAction.doActionMapObject(player, widgetId, childId, slot, mapObject);
    return true;
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
