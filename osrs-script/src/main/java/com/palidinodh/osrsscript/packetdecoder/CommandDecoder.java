package com.palidinodh.osrsscript.packetdecoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.palidinodh.io.FileManager;
import com.palidinodh.io.Stream;
import com.palidinodh.osrscore.io.Command;
import com.palidinodh.osrscore.io.PacketDecoder;
import com.palidinodh.osrscore.model.dialogue.Scroll;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.util.RequestManager;
import com.palidinodh.rs.setting.Settings;
import com.palidinodh.util.PLogger;
import lombok.var;

public class CommandDecoder extends PacketDecoder {
  private static Map<String, Command> commands = new HashMap<>();

  public CommandDecoder() {
    super(60, 45);
  }

  @Override
  public void execute(Player player, int index, int size, Stream stream) {
    if (index == 0) {
      var commandName = stream.getString();
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
        return;
      }
      var message = "";
      if (commandName.contains(" ")) {
        var indexOfSpace = commandName.indexOf(" ");
        message = commandName.substring(indexOfSpace + 1);
        commandName = commandName.substring(0, indexOfSpace);
      }
      commandName = commandName.toLowerCase();
      player.clearIdleTime();
      var command = commands.get(commandName);
      if (command == null) {
        player.getGameEncoder().sendMessage("Command not found.");
        return;
      }
      if (!command.canUse(player)) {
        player.getGameEncoder().sendMessage("You don't have permission to use this command.");
        return;
      }
      try {
        command.execute(player, message);
      } catch (Exception e) {
        player.getGameEncoder().sendMessage(getExample(commandName, command));
        if (Settings.getInstance().isLocal()) {
          e.printStackTrace();
        }
      }
    } else if (index == 1) {
      var tileHash = stream.getIntV2();
      player.clearIdleTime();
      if (player.getRights() != 2) {
        return;
      }
      var x = tileHash >> 14 & 16383;
      var y = tileHash & 16383;
      var z = tileHash >> 28 & 3;
      player.getMovement().teleport(x, y, z);
    }
  }

  private String getExample(String commandName, Command command) {
    return "::" + commandName.toLowerCase() + " " + command.getExample();
  }

  static {
    try {
      var classes = FileManager.getClassScripts("packetdecoder.command");
      for (var className : classes) {
        var classReference = Class.forName(className);
        if (!Command.class.isAssignableFrom(classReference)) {
          continue;
        }
        if (!classReference.getName().endsWith("Command")) {
          continue;
        }
        var commandName = classReference.getName();
        var lastPeriodIndex = commandName.lastIndexOf(".");
        if (lastPeriodIndex != -1) {
          commandName = commandName.substring(lastPeriodIndex + 1);
        }
        commandName = commandName.substring(0, commandName.length() - 7).toLowerCase();
        commands.put(commandName, (Command) classReference.newInstance());
      }
    } catch (Exception e) {
      PLogger.error(e);
    }
  }
}
