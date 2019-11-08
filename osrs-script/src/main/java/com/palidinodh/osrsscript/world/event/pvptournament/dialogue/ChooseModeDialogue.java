package com.palidinodh.osrsscript.world.event.pvptournament.dialogue;

import com.palidinodh.osrscore.model.dialogue.LargeOptionsDialogue;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrsscript.world.event.pvptournament.PvpTournament;
import lombok.var;

public class ChooseModeDialogue extends LargeOptionsDialogue {
  public ChooseModeDialogue(Player player) {
    var tournament = player.getWorld().getWorldEvent(PvpTournament.class);
    addOption("Option", (childId, slot) -> {
      tournament.selectCustomMode(player, slot);
    });
    open(player);
  }

  @Override
  public void sendWidgetTextHook(Player player) {
    sendWidgetText(player, null, PvpTournament.getModeNames());
  }
}
