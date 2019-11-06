package com.palidinodh.osrsscript.incomingpacket.widget;

import com.palidinodh.osrscore.io.ValueEnteredEvent;
import com.palidinodh.osrscore.io.incomingpacket.WidgetHandler;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.util.RequestManager;
import com.palidinodh.rs.adaptive.Clan;
import com.palidinodh.rs.adaptive.RsClanRank;
import lombok.var;

public class ClanChatWidget implements WidgetHandler {
  @Override
  public int[] getIds() {
    return new int[] {WidgetId.CLAN_CHAT, WidgetId.CLAN_CHAT_OPTIONS};
  }

  @Override
  public void execute(Player player, int index, int widgetId, int childId, int slot, int itemId) {
    if (player.isLocked()) {
      return;
    }
    if (widgetId == WidgetId.CLAN_CHAT) {
      switch (childId) {
        case 23:
          if (player.isGameModeGroupIronman()) {
            player.openDialogue("clanchat", 0);
          } else {
            player.getMessaging().openClanSettingsInterface();
          }
          break;
      }
    } else if (widgetId == WidgetId.CLAN_CHAT_OPTIONS) {
      var settingValue = RsClanRank.NOT_IN_CLAN;
      switch (childId) {
        case 10:
          if (index == 0) {
            player.getGameEncoder().sendEnterString("Enter chat prefix:",
                new ValueEnteredEvent.StringEvent() {
                  @Override
                  public void execute(String value) {
                    player.getMessaging().sendClanSetupName(value);
                    RequestManager.getInstance().addClanSetting(player, Clan.NAME, value);
                    if (player.getMessaging().getClanChatDisabled()) {
                      player.getMessaging().setClanChatDisabled(false);
                      RequestManager.getInstance().addClanSetting(player, Clan.DISABLE,
                          RsClanRank.NOT_IN_CLAN);
                    }
                  }
                });
          } else if (index == 1) {
            var disabled = !player.getMessaging().getClanChatDisabled();
            player.getMessaging().setClanChatDisabled(disabled);
            var name = player.getMessaging().getMyClanChatName();
            if (disabled) {
              name = Clan.DISABLED_NAME;
            }
            player.getMessaging().sendClanSetupName(name);
            RequestManager.getInstance().addClanSetting(player, Clan.DISABLE,
                player.getMessaging().getClanChatDisabled() ? RsClanRank.ANYONE
                    : RsClanRank.NOT_IN_CLAN);
          }
          break;
        case 13:
          if (index == 0) {
            settingValue = RsClanRank.ANYONE;
          } else if (index == 1) {
            settingValue = RsClanRank.ANY_FRIENDS;
          } else if (index == 2) {
            settingValue = RsClanRank.RECRUIT;
          } else if (index == 3) {
            settingValue = RsClanRank.CORPORAL;
          } else if (index == 4) {
            settingValue = RsClanRank.SERGEANT;
          } else if (index == 5) {
            settingValue = RsClanRank.LIEUTENANT;
          } else if (index == 6) {
            settingValue = RsClanRank.CAPTAIN;
          } else if (index == 7) {
            settingValue = RsClanRank.GENERAL;
          } else if (index == 8) {
            settingValue = RsClanRank.ONLY_ME;
          }
          player.getMessaging().sendClanSetupEnterLimit(settingValue);
          RequestManager.getInstance().addClanSetting(player, Clan.ENTER_LIMIT, settingValue);
          break;
        case 16:
          if (index == 0) {
            settingValue = RsClanRank.ANYONE;
          } else if (index == 1) {
            settingValue = RsClanRank.ANY_FRIENDS;
          } else if (index == 2) {
            settingValue = RsClanRank.RECRUIT;
          } else if (index == 3) {
            settingValue = RsClanRank.CORPORAL;
          } else if (index == 4) {
            settingValue = RsClanRank.SERGEANT;
          } else if (index == 5) {
            settingValue = RsClanRank.LIEUTENANT;
          } else if (index == 6) {
            settingValue = RsClanRank.CAPTAIN;
          } else if (index == 7) {
            settingValue = RsClanRank.GENERAL;
          } else if (index == 8) {
            settingValue = RsClanRank.ONLY_ME;
          }
          player.getMessaging().sendClanSetupTalkLimit(settingValue);
          RequestManager.getInstance().addClanSetting(player, Clan.TALK_LIMIT, settingValue);
          break;
        case 19:
          if (index == 3) {
            settingValue = RsClanRank.CORPORAL;
          } else if (index == 4) {
            settingValue = RsClanRank.SERGEANT;
          } else if (index == 5) {
            settingValue = RsClanRank.LIEUTENANT;
          } else if (index == 6) {
            settingValue = RsClanRank.CAPTAIN;
          } else if (index == 7) {
            settingValue = RsClanRank.GENERAL;
          } else if (index == 8) {
            settingValue = RsClanRank.ONLY_ME;
          }
          player.getMessaging().sendClanSetupKickLimit(settingValue);
          RequestManager.getInstance().addClanSetting(player, Clan.KICK_LIMIT, settingValue);
          break;
      }
    }
  }
}
