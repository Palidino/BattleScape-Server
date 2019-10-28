package com.palidinodh.io;

import java.util.HashMap;
import java.util.Map;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.util.logging.ExceptionLogger;
import com.palidinodh.rs.setting.DiscordChannel;
import com.palidinodh.rs.setting.Settings;

public class JavaCord {
  private static DiscordApi api = null;
  private static Map<String, Command> commands = new HashMap<>();

  public static void init() {
    if (Settings.getInstance().getDiscordToken() == null) {
      return;
    }
    new DiscordApiBuilder().setToken(Settings.getInstance().getDiscordToken()).login()
        .thenAccept(api -> {
          JavaCord.api = api;
          // System.out.println("Invite Url: " + api.createBotInvite());
          sendMessage(
              Settings.getInstance().isLocal() ? DiscordChannel.LOCAL
                  : DiscordChannel.ANNOUNCEMENTS,
              Settings.getInstance().getName() + " is now online!");
          api.addMessageCreateListener(event -> {
            if (!event.getMessageAuthor().isServerAdmin()) {
              return;
            }
            String message = event.getMessageContent();
            if (!message.startsWith("::")) {
              return;
            }
            String commandName = message.substring(2).split(" ")[0].toLowerCase();
            Command command = commands.get(commandName);
            if (command == null) {
              return;
            }
            try {
              int commandNameLength = 2 + commandName.length() + 1;
              message =
                  commandNameLength < message.length() ? message.substring(commandNameLength) : "";
              command.execute(event, message);
            } catch (Exception e) {
              e.printStackTrace();
            }
          });
        }).exceptionally(ExceptionLogger.get());
  }

  public static void sendMessage(DiscordChannel channel, String msg) {
    if (api == null || Settings.getInstance() == null) {
      return;
    }
    try {
      String channelName = Settings.getInstance().getDiscordChannel(channel);
      if (channelName == null) {
        return;
      }
      msg = msg.replaceAll("<.*>", "");
      if (msg.endsWith(" @everyone")) {
        int everyoneIndex = msg.lastIndexOf(" @everyone");
        msg = "```" + msg.substring(0, everyoneIndex) + "``` @everyone";
      } else {
        msg = "```" + msg + "```";
      }
      new MessageBuilder().append(msg)
          .send((TextChannel) api.getTextChannelsByNameIgnoreCase(channelName).toArray()[0]);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void disconnect() {
    if (api == null) {
      return;
    }
    api.disconnect();
    api = null;
  }

  public static void addCommand(String name, Command command) {
    commands.put(name, command);
  }

  public interface Command {
    void execute(MessageCreateEvent event, String message);
  }
}
