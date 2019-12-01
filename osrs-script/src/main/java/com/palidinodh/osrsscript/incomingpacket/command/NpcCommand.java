package com.palidinodh.osrsscript.incomingpacket.command;

import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.setting.Settings;
import lombok.var;

class NpcCommand implements CommandHandler {
  @Override
  public String getExample() {
    return "id_or_name";
  }

  @Override
  public boolean canUse(Player player) {
    return Settings.getInstance().isLocal();
  }

  @Override
  public void execute(Player player, String message) {
    var id = -1;
    if (message.matches("[0-9]+")) {
      id = Integer.parseInt(message);
    } else {
      id = NpcId.valueOf(message.toUpperCase());
    }
    var npc = new Npc(player.getController(), id, player.getX(), player.getY(), player.getHeight());
    player.getGameEncoder().sendMessage("Spawned " + npc.getDef().getName());
  }
}
