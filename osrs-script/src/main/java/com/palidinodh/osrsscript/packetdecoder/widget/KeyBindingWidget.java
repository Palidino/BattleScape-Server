package com.palidinodh.osrsscript.packetdecoder.widget;

import com.palidinodh.osrscore.io.Widget;
import com.palidinodh.osrscore.io.cache.WidgetId;
import com.palidinodh.osrscore.model.player.Options;
import com.palidinodh.osrscore.model.player.Player;

public class KeyBindingWidget implements Widget {
  @Override
  public int[] getIds() {
    return new int[] {WidgetId.KEYBINDING};
  }

  @Override
  public void execute(Player player, int index, int widgetId, int childId, int slot, int itemId) {
    if (player.isLocked()) {
      return;
    }
    switch (childId) {
      case 9:
        player.putAttribute("binding_tab", Options.BIND_COMBAT);
        break;
      case 16:
        player.putAttribute("binding_tab", Options.BIND_STATS);
        break;
      case 23:
        player.putAttribute("binding_tab", Options.BIND_QUEST);
        break;
      case 30:
        player.putAttribute("binding_tab", Options.BIND_INVENTORY);
        break;
      case 37:
        player.putAttribute("binding_tab", Options.BIND_EQUIPMENT);
        break;
      case 44:
        player.putAttribute("binding_tab", Options.BIND_PRAYER);
        break;
      case 51:
        player.putAttribute("binding_tab", Options.BIND_MAGIC);
        break;
      case 58:
        player.putAttribute("binding_tab", Options.BIND_FRIENDS);
        break;
      case 65:
        player.putAttribute("binding_tab", Options.BIND_IGNORES);
        break;
      case 72:
        player.putAttribute("binding_tab", Options.BIND_LOGOUT);
        break;
      case 79:
        player.putAttribute("binding_tab", Options.BIND_OPTIONS);
        break;
      case 86:
        player.putAttribute("binding_tab", Options.BIND_EMOTES);
        break;
      case 93:
        player.putAttribute("binding_tab", Options.BIND_CLAN);
        break;
      case 100:
        player.putAttribute("binding_tab", Options.BIND_MUSIC);
        break;
      case 103:
        player.putAttribute("binding_tab", Options.BIND_CURRENT_INTERFACE);
        player.getOptions().addKeyBinding(Options.KEY_ESC);
        break;
      case 104:
        player.getOptions().restoreDefaultKeyBinding();
        player.getGameEncoder().sendMessage("Default keys loaded.");
        break;
      case 111:
        player.getOptions().addKeyBinding(slot);
        break;
    }
  }
}
