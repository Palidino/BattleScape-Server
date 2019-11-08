package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.osrscore.io.cache.id.HumanAnimationId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.model.dialogue.DialogueChain;
import com.palidinodh.osrscore.model.dialogue.DialogueOption;
import com.palidinodh.osrscore.model.dialogue.MessageDialogue;
import com.palidinodh.osrscore.model.dialogue.NpcDialogue;
import com.palidinodh.osrscore.model.dialogue.PlayerDialogue;
import com.palidinodh.osrscore.model.dialogue.SelectionDialogue;
import com.palidinodh.osrscore.model.player.Player;
import lombok.var;

/*
 * Freely edit this to quickly test features. For commands that need more than a single use/test,
 * consider a proper command for it.
 */
@SuppressWarnings("all")
public class TestCommand implements CommandHandler {
  @Override
  public boolean canUse(Player player) {
    return player.getRights() == Player.RIGHTS_ADMIN;
  }

  @Override
  public void execute(Player player, String message) {
    // lms fog
    // var baseX = 2968;
    // var baseY = 3915;
    // player.getGameEncoder().setVarbit(5314, 1);
    // player.getGameEncoder().setVarbit(5320, baseX);
    // player.getGameEncoder().setVarbit(5316, baseY);
    // player.getGameEncoder().setVarbit(5317, 8);
    // 562-564: sad
    // 567-569: general chatting?
    player.openDialogue(new DialogueChain(new PlayerDialogue("Hello, I'm looking for Miika."),
        new NpcDialogue(NpcId.BOB, HumanAnimationId.DIALOGUE_SCEPTICAL_1, "Who?"),
        new SelectionDialogue(new DialogueOption("You know who!", DialogueChain.ACTION_NEXT),
            new DialogueOption("Nevermind", (childId, slot) -> {
              player.openDialogue(new PlayerDialogue("Nevermind, I'll go."));
            })),
        new PlayerDialogue("You know who!"), new NpcDialogue(NpcId.BOB, "OH, YOU MEAN MIKASA!"),
        new MessageDialogue("* Bob begins walking away from you. *")));
  }
}
