package com.palidinodh.osrsscript.map.area.edgeville.npc;

import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.io.incomingpacket.NpcHandler;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;

@ReferenceId({ NpcId.JOSSIK, NpcId.EVIL_DAVE_4806, NpcId.RADIMUS_ERKLE, NpcId.GUILDMASTER,
    NpcId.MONK_OF_ENTRANA, NpcId.KING_NARNODE_SHAREEN, NpcId.ONEIROMANCER, NpcId.ZEALOT })
class MiniquestNpc implements NpcHandler {
  @Override
  public void execute(Player player, int option, Npc npc) {
    switch (npc.getId()) {
      case NpcId.JOSSIK:
        player.openDialogue("horrorfromthedeep", 0);
        break;
      case NpcId.EVIL_DAVE_4806:
        player.openDialogue("shadowofthestorm", 0);
        break;
      case NpcId.RADIMUS_ERKLE:
        if (!player.getCombat().isLegendsQuestComplete()) {
          if (player.getCombat().getRecipeForDisasterStage() != 6) {
            player.getGameEncoder().sendMessage("You need to complete Recipe for Disaster.");
            break;
          } else if (!player.getCombat().getHorrorFromTheDeep()) {
            player.getGameEncoder().sendMessage("You need to complete Horror from the Deep.");
            break;
          } else if (!player.getCombat().getDreamMentor()) {
            player.getGameEncoder().sendMessage("You need to complete Dream Mentor.");
            break;
          } else if (!player.getCombat().getMageArena()) {
            player.getGameEncoder().sendMessage("You need to complete the Mage Arena.");
            break;
          } else if (!player.getCombat().getLostCity()) {
            player.getGameEncoder().sendMessage("You need to complete Lost City.");
            break;
          } else if (!player.getCombat().getDragonSlayer()) {
            player.getGameEncoder().sendMessage("You need to complete Dragon Slayer.");
            break;
          } else if (!player.getCombat().getMonkeyMadness()) {
            player.getGameEncoder().sendMessage("You need to complete Monkey Madness.");
            break;
          } else if (player.getCombat().getHauntedMine() <= 3) {
            player.getGameEncoder().sendMessage("You need to complete Haunted Mine.");
            break;
          }
          player.getMovement().teleport(new Tile(2774, 9338));
        } else {
          player.getMovement().teleport(new Tile(2728, 3351));
        }
        break;
      case NpcId.GUILDMASTER:
        player.openDialogue("dragonslayer", 0);
        break;
      case NpcId.MONK_OF_ENTRANA:
        player.openDialogue("lostcity", 0);
        break;
      case NpcId.KING_NARNODE_SHAREEN:
        player.openDialogue("monkeymadness", 0);
        break;
      case NpcId.ONEIROMANCER:
        player.openDialogue("dreammentor", 0);
        break;
      case NpcId.ZEALOT:
        player.openDialogue("hauntedmine", 0);
        break;
    }
  }
}
