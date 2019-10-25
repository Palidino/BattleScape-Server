package com.palidinodh.osrsscript.world.pvptournament.dialogue;

import com.palidinodh.osrscore.io.ValueEnteredEvent;
import com.palidinodh.osrscore.model.dialogue.SelectionDialogue;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrsscript.world.pvptournament.PvpTournament;
import com.palidinodh.osrsscript.world.pvptournament.prize.DefaultPrize;
import com.palidinodh.osrsscript.world.pvptournament.prize.OsrsPrize;
import com.palidinodh.osrsscript.world.pvptournament.state.IdleState;
import lombok.var;

public class AdminCofferDialogue extends SelectionDialogue {
  public AdminCofferDialogue(Player player) {
    var tournament = player.getWorld().getWorldEvent(PvpTournament.class);
    addOption("View prizes", (childId, slot) -> {
      tournament.viewPrizes(player);
    });
    addOption("View shop", (childId, slot) -> {
      player.openShop("pvp_tournament");
    });
    addOption("Select mode", (childId, slot) -> {
      new ChooseModeDialogue(player);
    });
    addOption("Set OSRS prize", (childId, slot) -> {
      player.getGameEncoder().sendEnterAmount("OSRS prize:", new ValueEnteredEvent.IntegerEvent() {
        @Override
        public void execute(int value) {
          if (!(tournament.getState() instanceof IdleState)
              || !(tournament.getPrize() instanceof DefaultPrize)
                  && !(tournament.getPrize() instanceof OsrsPrize)) {
            player.getGameEncoder().sendMessage("A tournament has already been configured.");
            return;
          }
          tournament.setPrize(new OsrsPrize(value));
          new ChooseModeDialogue(player);
        }
      });
    });
    open(player);
  }
}
