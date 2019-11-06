package com.palidinodh.osrsscript.incomingpacket;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import com.palidinodh.io.Stream;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.io.incomingpacket.InStreamKey;
import com.palidinodh.osrscore.io.incomingpacket.IncomingPacketDecoder;
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

public class NpcOptionDecoder extends IncomingPacketDecoder {
  private static Map<Integer, Method> actionMethods = new HashMap<>();

  @Override
  public boolean execute(Player player, Stream stream) {
    var option = getInt(InStreamKey.PACKET_OPTION);
    var npcIndex = getInt(InStreamKey.TARGET_INDEX);
    var ctrlRun = getInt(InStreamKey.CTRL_RUN);
    player.clearIdleTime();
    var npc = player.getWorld().getNpcByIndex(npcIndex);
    if (npc == null) {
      return false;
    }
    var message = "[NpcOption(" + option + ")] index=" + npcIndex + "; ctrlRun=" + ctrlRun
        + "; id=" + npc.getId() + "/" + NpcId.valueOf(npc.getId());
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
    if (player.getMovement().isViewing()) {
      return false;
    }
    player.clearAllActions(false, true);
    player.setFaceEntity(npc);
    if (npc.getMoveRange() == null && !npc.getDef().getLCName().contains("fishing")) {
      player.getMovement().setFollowFront(npc);
    } else {
      player.getMovement().setFollowing(npc);
    }
    if (option == 1 && npc.getMaxHitpoints() > 0) {
      player.setAttacking(true);
      player.setInteractingEntity(npc);
      player.getCombat().setFollowing(npc);
      player.getMovement().follow();
      if (player.getMagic().getAutoSpellId() != 0 && player.getHitDelay() <= 0) {
        player.setHitDelay(2);
      }
      return false;
    }
    player.getMovement().follow();
    return true;
  }

  @Override
  public boolean complete(Player player) {
    var option = getInt(InStreamKey.PACKET_OPTION);
    var npcIndex = getInt(InStreamKey.TARGET_INDEX);
    var npc = player.getWorld().getNpcByIndex(npcIndex);
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
    AchievementDiary.npcOptionUpdate(player, option, npc);
    if (player.getController().npcOptionHook(option, npc)) {
      return true;
    }
    for (var plugin : player.getPlugins()) {
      if (plugin.npcOptionHook(option, npc)) {
        return true;
      }
    }
    if (SkillContainer.npcOptions(player, option, npc)) {
      return true;
    }
    if (npc.getDef().isOption(option, "pick-up") && Familiar.isPetNPC(npc.getId())) {
      player.getFamiliar().removeFamiliar();
      return true;
    }
    if (player.getArea().npcOptionHook(option, npc)) {
      return true;
    }
    if (!actionMethods.containsKey(npc.getId())) {
      try {
        var classReference = Class
            .forName(Settings.getInstance().getScriptPackage() + ".incomingpacket.misc.NpcOptions");
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
      if (basicAction(player, option, npc)) {
        return true;
      }
      player.getGameEncoder().sendMessage("Nothing interesting happens.");
    } else {
      try {
        actionMethod.invoke(null, player, option, npc);
      } catch (Exception e) {
        PLogger.error(e);
        player.getGameEncoder().sendMessage("Nothing interesting happens.");
      }
    }
    return true;
  }

  private static boolean basicAction(Player player, int option, Npc npc) {
    switch (npc.getDef().getLCName()) {
      case "banker":
      case "ghost banker":
      case "gnome banker":
        if (option == 0) {
          player.openDialogue("bank", 1);
        } else if (option == 2) {
          player.getBank().open();
        }
        return true;
    }
    return false;
  }
}
