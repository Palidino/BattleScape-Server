package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.osrscore.io.cache.id.DialogueAnimationId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.model.dialogue.ChatDialogue;
import com.palidinodh.osrscore.model.dialogue.DialogueChain;
import com.palidinodh.osrscore.model.dialogue.DialogueOption;
import com.palidinodh.osrscore.model.dialogue.MessageDialogue;
import com.palidinodh.osrscore.model.dialogue.NormalChatDialogue;
import com.palidinodh.osrscore.model.dialogue.OptionsDialogue;
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
    var animationId = Integer.parseInt(message);
    player.openDialogue(
        new ChatDialogue(-1, animationId, "Hello, I'm looking for Miika.",
            new DialogueOption("wat")),
        new NormalChatDialogue(NpcId.BOB, "Who?"),
        new OptionsDialogue(new DialogueOption("You know who!", DialogueChain.ACTION_NEXT),
            new DialogueOption("Nevermind", (childId, slot) -> {
              player.openDialogue(new NormalChatDialogue("Nevermind, I'll go."));
            })),
        new NormalChatDialogue("You know who!"),
        new NormalChatDialogue(NpcId.BOB, "OH, YOU MEAN MIKASA!"),
        new NormalChatDialogue(
            "123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789"),
        new NormalChatDialogue(NpcId.BOB,
            "THIS IS A LONG LINE TEST I DON'T KNOW WHAT ELSE TO SAY SO I'M JUST GOING TO KEEP TYPING WORDS UNTIL I CAN'T THINK OF ANYTHING ELSE."),
        new MessageDialogue("* Bob begins walking away from you. *"));
  }
}
