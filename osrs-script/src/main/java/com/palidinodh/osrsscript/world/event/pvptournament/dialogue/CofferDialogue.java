package com.palidinodh.osrsscript.world.event.pvptournament.dialogue;

import com.palidinodh.osrscore.model.dialogue.OptionsDialogue;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrsscript.world.event.pvptournament.PvpTournament;
import lombok.var;

public class CofferDialogue extends OptionsDialogue {
  public CofferDialogue(Player player) {
    var tournament = player.getWorld().getWorldEvent(PvpTournament.class);
    addOption("View prizes", (c, s) -> {
      tournament.viewPrizes(player);
    });
    addOption("View shop", (c, s) -> {
      player.openShop("pvp_tournament");
    });
  }
}
