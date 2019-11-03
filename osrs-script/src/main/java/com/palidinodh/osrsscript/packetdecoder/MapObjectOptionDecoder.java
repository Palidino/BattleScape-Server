package com.palidinodh.osrsscript.packetdecoder;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import com.palidinodh.io.Stream;
import com.palidinodh.osrscore.io.PacketDecoder;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.map.Region;
import com.palidinodh.osrscore.model.player.AchievementDiary;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.Prayer;
import com.palidinodh.osrscore.model.player.Skills;
import com.palidinodh.osrscore.model.player.skill.SkillContainer;
import com.palidinodh.osrscore.util.RequestManager;
import com.palidinodh.osrscore.world.WorldEventHooks;
import com.palidinodh.rs.setting.Settings;
import com.palidinodh.util.PLogger;
import lombok.var;

public class MapObjectOptionDecoder extends PacketDecoder {
  private static Map<Integer, Method> actionMethods = new HashMap<>();

  public MapObjectOptionDecoder() {
    super(10, 79, 89, 3, 94);
  }

  @Override
  public void execute(Player player, int index, int size, Stream stream) {
    var id = -1;
    var x = -1;
    var y = -1;
    var moveType = 0;
    if (index == 0) {
      y = stream.readUnsignedShort();
      moveType = stream.readUnsignedByteS();
      id = stream.readUnsignedShortA();
      x = stream.readUnsignedLEShortA();
    } else if (index == 1) {
      moveType = stream.readUnsignedByteA();
      id = stream.readUnsignedShort();
      y = stream.readUnsignedShort();
      x = stream.readUnsignedShort();
    } else if (index == 2) {
      x = stream.readUnsignedLEShortA();
      id = stream.readUnsignedShortA();
      y = stream.readUnsignedLEShortA();
      moveType = stream.readUnsignedByteA();
    } else if (index == 3) {
      moveType = stream.readUnsignedByteA();
      y = stream.readUnsignedShortA();
      x = stream.readUnsignedShortA();
      id = stream.readUnsignedLEShort();
    } else if (index == 4) {
      id = stream.readUnsignedShort();
      moveType = stream.readUnsignedByteA();
      x = stream.readUnsignedShortA();
      y = stream.readUnsignedLEShortA();
    }
    var mapObject = player.getController().getMapObject(id, x, y, player.getClientHeight());
    if (mapObject == null) {
      return;
    }
    if (!mapObject.getDef().hasOptions()) {
      return;
    }
    if (player.getHeight() != player.getClientHeight()) {
      if (mapObject.getDef().hasOption("open") || mapObject.getDef().hasOption("close")) {
        return;
      }
    }
    var message = "[MapObjectOption(" + index + ")] id=" + id + "/" + mapObject.getName() + "; x="
        + x + "; y=" + y + "; moveType=" + moveType + ", type=" + mapObject.getType()
        + ", direction=" + mapObject.getDirection();
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
    if (player.getObjectOptionDelay() > 0) {
      return;
    }
    player.clearIdleTime();
    player.getMovement().fullRoute(mapObject, moveType);
    player.putAttribute("packet_decoder_index", index);
    player.putAttribute("packet_decoder_map_object", mapObject);
    if (complete(player)) {
      stop(player);
      return;
    }
    player.putAttribute("packet_decoder", this);
  }

  @Override
  public boolean complete(Player player) {
    var index = player.getAttributeInt("packet_decoder_index");
    var mapObject = (MapObject) player.getAttribute("packet_decoder_map_object");
    if (mapObject == null) {
      return true;
    }
    if (!mapObject.isVisible()) {
      return true;
    }
    var range = 1;
    if (mapObject.getType() >= 4 && mapObject.getType() <= 8) {
      range = 0;
    }
    if (mapObject.getId() == 30352) { // The Inferno Entrance
      range = 5;
    } else if (mapObject.getId() == 31561) { // Revenant Pillar
      range = 2;
    }
    if (player.isLocked()) {
      return false;
    }
    if (player.getMovement().isRouting() && mapObject.getOriginal() == null
        && (player.getX() != mapObject.getX() || player.getY() != mapObject.getY())) {
      return false;
    }
    if (!player.withinDistanceC(mapObject, range)) {
      return false;
    }
    player.getMovement().clear();
    if (!player.matchesTile(mapObject)) {
      player.setFaceTile(mapObject);
    }
    AchievementDiary.mapObjectOptionUpdate(player, index, mapObject);
    if (player.getController().mapObjectOptionHook(index, mapObject)) {
      return true;
    }
    for (var plugin : player.getPlugins()) {
      if (plugin.mapObjectOptionHook(index, mapObject)) {
        return true;
      }
    }
    for (var event : player.getWorld().getWorldEvents()) {
      if (!(event instanceof WorldEventHooks)) {
        continue;
      }
      if (((WorldEventHooks) event).mapObjectOptionHook(player, index, mapObject)) {
        return true;
      }
    }
    if (SkillContainer.mapObjectOptions(player, index, mapObject)) {
      return true;
    }
    if (player.getFarming().mapObjectOptionHook(index, mapObject)) {
      return true;
    }
    if (player.getArea().mapObjectOptionHook(index, mapObject)) {
      return true;
    }
    if (!actionMethods.containsKey(mapObject.getId())) {
      try {
        var className =
            "com.palidinodh.osrsscript.incomingpacket.misc.MapObject" + mapObject.getId() / 16384;
        var classReference = Class.forName(className);
        var methodName = "mapObject" + mapObject.getId();
        var actionMethod =
            classReference.getMethod(methodName, Player.class, Integer.TYPE, MapObject.class);
        if ((actionMethod.getModifiers() & Modifier.STATIC) == 0) {
          actionMethod = null;
        }
        actionMethods.put(mapObject.getId(), actionMethod);
      } catch (Exception e) {
        actionMethods.put(mapObject.getId(), null);
      }
    }
    var actionMethod = actionMethods.get(mapObject.getId());
    if (actionMethod == null) {
      if (basicAction(player, index, mapObject)) {
        return true;
      }
      player.getGameEncoder().sendMessage("Nothing interesting happens.");
    } else {
      try {
        actionMethod.invoke(null, player, index, mapObject);
      } catch (Exception e) {
        PLogger.error(e);
        player.getGameEncoder().sendMessage("Nothing interesting happens.");
      }
    }
    return true;
  }

  @Override
  public void stop(Player player) {
    player.removeAttribute("packet_decoder_index");
    player.removeAttribute("packet_decoder_map_object");
  }

  private static boolean basicAction(Player player, int index, MapObject mapObject) {
    switch (mapObject.getName().toLowerCase()) {
      case "door":
      case "gate":
      case "large door":
        if (mapObject.getDef().getOptions() == null) {
          break;
        }
        if (!"Open".equals(mapObject.getDef().getOptions()[0])) {
          break;
        }
        if (mapObject.getSizeX() != 1 || mapObject.getSizeY() != 1) {
          break;
        }
        var hasOtherOptions = false;
        for (int i = 1; i < mapObject.getDef().getOptions().length; i++) {
          if (mapObject.getDef().getOptions()[i] != null) {
            hasOtherOptions = true;
          }
        }
        if (hasOtherOptions) {
          break;
        }
        Region.openDoors(player, mapObject);
        return true;
      case "bank booth":
      case "bank chest":
      case "bank counter":
        player.getBank().open();
        return true;
      case "bank deposit box":
      case "deposit box":
      case "bank deposit pot":
        player.getBank().openDepositBox();
        return true;
      case "fairy ring":
        player.openDialogue("fairyring", 0);
        return true;
      case "altar":
      case "chaos altar":
        if (!mapObject.getDef().hasOption("pray")) {
          break;
        }
        player.getPrayer().adjustPoints(player.getController().getLevelForXP(Skills.PRAYER));
        player.setAnimation(Prayer.PRAY_ANIMATION);
        return true;
    }
    return false;
  }
}
