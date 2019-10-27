package com.palidinodh.osrsscript.packetdecoder;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import com.palidinodh.io.Stream;
import com.palidinodh.osrscore.io.PacketDecoder;
import com.palidinodh.osrscore.io.cache.NpcId;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.player.AchievementDiary;
import com.palidinodh.osrscore.model.player.Familiar;
import com.palidinodh.osrscore.model.player.Hunter;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.skill.SkillContainer;
import com.palidinodh.osrscore.util.RequestManager;
import com.palidinodh.rs.setting.Settings;
import com.palidinodh.util.PLogger;
import lombok.var;

public class NpcOptionDecoder extends PacketDecoder {
  private static Map<Integer, Method> actionMethods = new HashMap<>();

  public NpcOptionDecoder() {
    super(71, 1, 33, 59, 31);
  }

  @Override
  public void execute(Player player, int index, int size, Stream stream) {
    var id = -1;
    var moveType = 0;
    if (index == 0) {
      moveType = stream.getUReversedByte();
      id = stream.getUShortLE();
    } else if (index == 1) {
      id = stream.getUShort128();
      moveType = stream.getUByte();
    } else if (index == 2) {
      id = stream.getUShortLE128();
      moveType = stream.getUByte();
    } else if (index == 3) {
      id = stream.getUShort();
      moveType = stream.getUByte128();
    } else if (index == 4) {
      moveType = stream.getUByte();
      id = stream.getUShort128();
    }
    var npc = player.getWorld().getNPCByIndex(id);
    if (npc == null) {
      return;
    }
    var message = "[NpcOption(" + index + ")] index=" + id + "; moveType=" + moveType + "; id="
        + npc.getId() + "/" + NpcId.valueOf(npc.getId());
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
    player.clearIdleTime();
    player.clearAllActions(false, true);
    player.setFaceEntity(npc);
    if (npc.getMoveRange() == null && !npc.getDef().getLCName().contains("fishing")) {
      player.getMovement().setFollowFront(npc);
    } else {
      player.getMovement().setFollowing(npc);
    }
    if (index == 1 && npc.getMaxHitpoints() > 0) {
      player.setAttacking(true);
      player.setInteractingEntity(npc);
      player.getCombat().setFollowing(npc);
      player.getMovement().follow();
      if (player.getMagic().getAutoSpellId() != 0 && player.getHitDelay() <= 0) {
        player.setHitDelay(2);
      }
      return;
    }
    player.getMovement().follow();
    player.putAttribute("packet_decoder_index", index);
    player.putAttribute("packet_decoder_npc_index", id);
    if (complete(player)) {
      stop(player);
      return;
    }
    player.putAttribute("packet_decoder", this);
  }

  @Override
  public boolean complete(Player player) {
    var index = player.getAttributeInt("packet_decoder_index");
    var id = player.getAttributeInt("packet_decoder_npc_index");
    var npc = player.getWorld().getNPCByIndex(id);
    if (npc == null) {
      return true;
    }
    var range = 1;
    if (npc.getDef().hasOption("bank")) {
      range = 2;
    }
    if (player.getMovement().isRouting() && npc.getMovement().isRouting()) {
      range++;
    }
    if (player.isLocked()) {
      return false;
    }
    if (!player.withinDistance(npc, range)) {
      return false;
    }
    if (!npc.getMovement().isRouting() && npc.getSizeX() == 1 && player.getX() != npc.getX()
        && player.getY() != npc.getY()) {
      return false;
    }
    if (Hunter.getHuntedNPC(npc.getId()) != null && !player.getHunter().catchNPCStage(npc)) {
      return false;
    }
    player.setFaceEntity(null);
    player.getMovement().setFollowing(null);
    if (player.getX() == npc.getX() || player.getY() == npc.getY()) {
      player.getMovement().clear();
    }
    player.setFaceTile(npc);
    AchievementDiary.npcOptionUpdate(player, index, npc);
    if (player.getController().npcOptionHook(index, npc)) {
      return true;
    }
    for (var plugin : player.getPlugins()) {
      if (plugin.npcOptionHook(index, npc)) {
        return true;
      }
    }
    if (SkillContainer.npcOptions(player, index, npc)) {
      return true;
    }
    if (npc.getDef().isOption(index, "pick-up") && Familiar.isPetNPC(npc.getId())) {
      player.getFamiliar().removeFamiliar();
      return true;
    }
    if (player.getArea().npcOptionHook(index, npc)) {
      return true;
    }
    if (!actionMethods.containsKey(npc.getId())) {
      try {
        var classReference =
            Class.forName("com.palidinodh.osrsscript.packetdecoder.misc.NpcOptions");
        var methodName = "npc" + npc.getId();
        var actionMethod =
            classReference.getMethod(methodName, Player.class, Integer.TYPE, Npc.class);
        if ((actionMethod.getModifiers() & Modifier.STATIC) == 0) {
          actionMethod = null;
        }
        actionMethods.put(npc.getId(), actionMethod);
      } catch (Exception e) {
        actionMethods.put(npc.getId(), null);
      }
    }
    var actionMethod = actionMethods.get(npc.getId());
    if (actionMethod == null) {
      if (basicAction(player, index, npc)) {
        return true;
      }
      player.getGameEncoder().sendMessage("Nothing interesting happens.");
    } else {
      try {
        actionMethod.invoke(null, player, index, npc);
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
    player.removeAttribute("packet_decoder_npc_index");
  }

  private static boolean basicAction(Player player, int index, Npc npc) {
    switch (npc.getDef().getLCName()) {
      case "banker":
      case "ghost banker":
      case "gnome banker":
        if (index == 0) {
          player.openDialogue("bank", 1);
        } else if (index == 2) {
          player.getBank().open();
        }
        return true;
    }
    return false;
  }
}
