package com.palidinodh.osrsscript.incomingpacket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.palidinodh.io.Readers;
import com.palidinodh.io.Stream;
import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.io.incomingpacket.InStreamKey;
import com.palidinodh.osrscore.io.incomingpacket.IncomingPacketDecoder;
import com.palidinodh.osrscore.model.dialogue.Scroll;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.util.RequestManager;
import com.palidinodh.rs.setting.Settings;
import com.palidinodh.util.PLogger;
import lombok.var;

class CommandDecoder extends IncomingPacketDecoder {
  private static Map<String, CommandHandler> commands = new HashMap<>();

  @Override
  public boolean execute(Player player, Stream stream) {
    var commandName = getString(InStreamKey.STRING_INPUT);
    player.clearIdleTime();
    RequestManager.addUserPacketLog(player, "[Command] commandName=" + commandName);
    if (commandName.equals("commands")) {
      var examples = new ArrayList<String>();
      for (var entry : commands.entrySet()) {
        if (!entry.getValue().canUse(player)) {
          continue;
        }
        examples.add(getExample(entry.getKey(), entry.getValue()));
      }
      Scroll.open(player, "Commands", examples);
      return true;
    }
    var message = "";
    if (commandName.contains(" ")) {
      var indexOfSpace = commandName.indexOf(" ");
      message = commandName.substring(indexOfSpace + 1);
      commandName = commandName.substring(0, indexOfSpace);
    }
    commandName = commandName.toLowerCase();
    var command = commands.get(commandName);
    if (command == null) {
      player.getGameEncoder().sendMessage("Command not found.");
      return false;
    }
    if (!command.canUse(player)) {
      player.getGameEncoder().sendMessage("You don't have permission to use this command.");
      return false;
    }
    try {
      command.execute(player, message);
    } catch (Exception e) {
      player.getGameEncoder().sendMessage(getExample(commandName, command));
      if (Settings.getInstance().isLocal()) {
        e.printStackTrace();
      }
    }
    return true;
  }

  private String getExample(String commandName, CommandHandler command) {
    return "::" + commandName.toLowerCase() + " " + command.getExample();
  }

  static {
    try {
      var classes = Readers.getScriptClasses(CommandHandler.class, "incomingpacket.command");
      for (var clazz : classes) {
        var classInstance = Readers.newInstance(clazz);
        commands.put(clazz.getSimpleName().replace("Command", "").toLowerCase(), classInstance);
      }
    } catch (Exception e) {
      PLogger.error(e);
    }
  }
}
