package com.palidinodh.osrsscript.world.event.pvptournament.dialogue;

import com.palidinodh.osrscore.model.dialogue.OptionsDialogue;
import com.palidinodh.osrscore.model.item.Item;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrsscript.world.event.pvptournament.PvpTournament;
import com.palidinodh.util.PNumber;
import lombok.var;

public class DonateItemDialogue extends OptionsDialogue {
  private Item item;

  public DonateItemDialogue(Player player, Item item) {
    this.item = item;
    var tournament = player.getWorld().getWorldEvent(PvpTournament.class);
    action((c, s) -> {
      player.getWidgetManager().removeInteractiveWidgets();
      if (!tournament.donateItem(player, item.getId(), s)) {
        return;
      }
      if (tournament.getMode() != null) {
        return;
      }
      new ChooseModeDialogue(player);
    });
    addOption("Give to place #1");
    addOption("Give to place #2");
    addOption("Give to place #3");
    addOption("Give to place #4");
  }

  @Override
  public void sendWidgetTextHook(Player player) {
    sendWidgetText(player, item.getName() + " x" + PNumber.formatNumber(item.getAmount()));
  }
}
