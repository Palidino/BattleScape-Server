package com.palidinodh.osrsscript.world.event.pvptournament.dialogue;

import com.palidinodh.osrscore.model.dialogue.DialogueAction;
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
    DialogueAction action = (childId, slot) -> {
      player.getWidgetManager().removeInteractiveWidgets();
      if (!tournament.donateItem(player, item.getId(), slot)) {
        return;
      }
      if (tournament.getMode() != null) {
        return;
      }
      new ChooseModeDialogue(player);
    };
    addOption("Give to place #1", action);
    addOption("Give to place #2", action);
    addOption("Give to place #3", action);
    addOption("Give to place #4", action);
    open(player);
  }

  @Override
  public void sendWidgetTextHook(Player player) {
    sendWidgetText(player, item.getName() + " x" + PNumber.formatNumber(item.getAmount()));
  }
}
