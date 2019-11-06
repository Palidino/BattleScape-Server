package com.palidinodh.osrsscript.player.plugin.clanwars;

import com.palidinodh.osrscore.io.cache.id.ScriptId;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.Skills;
import com.palidinodh.osrscore.model.player.WidgetManager;
import com.palidinodh.osrsscript.player.plugin.clanwars.rule.Rule;
import com.palidinodh.osrsscript.player.plugin.clanwars.rule.RuleOption;
import com.palidinodh.osrsscript.player.plugin.clanwars.state.CompletedState;
import com.palidinodh.osrsscript.player.plugin.clanwars.state.PlayerState;
import com.palidinodh.random.PRandom;
import lombok.var;

public class Stages {
  public static void openRuleSelection(Player player, ClanWarsPlugin plugin) {
    if (plugin.getOpponent() == null || plugin.getOpponent().isLocked()
        || player.getWidgetManager().hasInteractiveWidgets()) {
      plugin.cancel();
      return;
    }
    plugin.setTeammate(null);
    plugin.setRules(Rule.getDefault());
    plugin.setState(PlayerState.RULE_SELECTION);
    player.getWidgetManager().sendInteractiveOverlay(WidgetId.CLAN_WARS_OPTIONS,
        new WidgetManager.CloseEvent() {
          @Override
          public void execute() {
            var opponentPlayer = plugin.getOpponent();
            plugin.cancel();
            if (opponentPlayer == null || opponentPlayer.isLocked()
                || !opponentPlayer.getWidgetManager().hasWidget(WidgetId.CLAN_WARS_OPTIONS)) {
              return;
            }
            player.getGameEncoder().sendMessage("You decline the war.");
            opponentPlayer.getGameEncoder()
                .sendMessage(player.getUsername() + " declined the war.");
            opponentPlayer.getWidgetManager().removeInteractiveWidgets();
          }
        });
    player.getGameEncoder().sendWidgetSettings(WidgetId.CLAN_WARS_OPTIONS, 6, 0, 70, 2);
    player.getGameEncoder().sendWidgetSettings(WidgetId.CLAN_WARS_OPTIONS, 10, 0, 34, 2);
    player.getGameEncoder().sendWidgetSettings(WidgetId.CLAN_WARS_OPTIONS, 14, 0, 4, 2);
    player.getGameEncoder().sendWidgetSettings(WidgetId.CLAN_WARS_OPTIONS, 17, 0, 10, 2);
    player.getGameEncoder().sendWidgetSettings(WidgetId.CLAN_WARS_OPTIONS, 19, 0, 7, 2);
    player.getGameEncoder().sendWidgetSettings(WidgetId.CLAN_WARS_OPTIONS, 20, 0, 7, 2);
    player.getGameEncoder().sendWidgetSettings(WidgetId.CLAN_WARS_OPTIONS, 21, 0, 13, 2);
    player.getGameEncoder().sendWidgetSettings(WidgetId.CLAN_WARS_OPTIONS, 22, 0, 10, 2);
    player.getGameEncoder().sendWidgetSettings(WidgetId.CLAN_WARS_OPTIONS, 23, 0, 7, 2);
    player.getGameEncoder().sendWidgetSettings(WidgetId.CLAN_WARS_OPTIONS, 24, 0, 7, 2);
    player.getGameEncoder().sendWidgetSettings(WidgetId.CLAN_WARS_OPTIONS, 25, 0, 10, 2);
    player.getGameEncoder().sendWidgetText(WidgetId.CLAN_WARS_OPTIONS, 2,
        "Clan Wars Setup: Challenging " + plugin.getOpponent().getUsername());
    player.getGameEncoder().sendWidgetText(WidgetId.CLAN_WARS_OPTIONS, 17, 2, "F2P Content Only");
    player.getGameEncoder().sendWidgetText(WidgetId.CLAN_WARS_OPTIONS, 17, 5, "Limit to 1 Defence");
    sendVarbits(player, plugin);
  }

  public static void acceptRuleSelection(Player player, ClanWarsPlugin plugin) {
    if (plugin.getOpponent() == null || plugin.getOpponent().isLocked()) {
      player.getWidgetManager().removeInteractiveWidgets();
      return;
    }
    if (!player.getWidgetManager().hasWidget(WidgetId.CLAN_WARS_OPTIONS)
        || !plugin.getOpponent().getWidgetManager().hasWidget(WidgetId.CLAN_WARS_OPTIONS)) {
      player.getWidgetManager().removeInteractiveWidgets();
      return;
    }
    var opponentPlugin = plugin.getOpponent().getPlugin(ClanWarsPlugin.class);
    if (opponentPlugin.getState() == PlayerState.RULE_SELECTION) {
      player.getGameEncoder().sendMessage("<col=ff0000>Waiting for other player...");
      plugin.getOpponent().getGameEncoder().sendMessage("<col=ff0000>Other player has accepted.");
      plugin.setState(PlayerState.ACCEPT_RULE_SELECTION);
      return;
    }
    if (opponentPlugin.getState() != PlayerState.ACCEPT_RULE_SELECTION) {
      return;
    }
    opponentPlugin.setState(PlayerState.RULE_CONFIRMATION);
    plugin.setState(PlayerState.RULE_CONFIRMATION);
    openRuleConfirmation(player, plugin);
    openRuleConfirmation(plugin.getOpponent(), opponentPlugin);
  }

  public static void openRuleConfirmation(Player player, ClanWarsPlugin plugin) {
    player.getWidgetManager().removeWidgetCloseEvents();
    player.getWidgetManager().sendInteractiveOverlay(WidgetId.CLAN_WARS_CONFIRM,
        new WidgetManager.CloseEvent() {
          @Override
          public void execute() {
            var opponentPlayer = plugin.getOpponent();
            plugin.cancel();
            if (opponentPlayer == null || opponentPlayer.isLocked()
                || !opponentPlayer.getWidgetManager().hasWidget(WidgetId.CLAN_WARS_OPTIONS)) {
              return;
            }
            player.getGameEncoder().sendMessage("You decline the war.");
            opponentPlayer.getGameEncoder()
                .sendMessage(player.getUsername() + " declined the war.");
            opponentPlayer.getWidgetManager().removeInteractiveWidgets();
          }
        });
    player.getGameEncoder().sendWidgetText(WidgetId.CLAN_WARS_CONFIRM, 2,
        "Clan Wars Setup: Challenging " + plugin.getOpponent().getUsername());
    player.getGameEncoder().sendScript(ScriptId.CLAN_WARS_RULE_DESCRIPTIONS,
        Rule.getDescriptions(plugin.getRules()));
    sendVarbits(player, plugin);
  }

  public static void acceptRuleConfirmation(Player player, ClanWarsPlugin plugin) {
    if (plugin.getState() == PlayerState.JOIN) {
      acceptJoin(player, plugin);
      return;
    }
    if (plugin.getState() == PlayerState.VIEW_START) {
      acceptViewing(player, plugin);
      return;
    }
    if (plugin.getOpponent() == null || plugin.getOpponent().isLocked()) {
      player.getWidgetManager().removeInteractiveWidgets();
      return;
    }
    if (!player.getWidgetManager().hasWidget(WidgetId.CLAN_WARS_CONFIRM)
        || !plugin.getOpponent().getWidgetManager().hasWidget(WidgetId.CLAN_WARS_CONFIRM)) {
      player.getWidgetManager().removeInteractiveWidgets();
      return;
    }
    var opponentPlugin = plugin.getOpponent().getPlugin(ClanWarsPlugin.class);
    if (opponentPlugin.getState() == PlayerState.RULE_CONFIRMATION) {
      player.getGameEncoder().sendMessage("<col=ff0000>Waiting for other player...");
      plugin.getOpponent().getGameEncoder().sendMessage("<col=ff0000>Other player has accepted.");
      plugin.setState(PlayerState.ACCEPT_RULE_CONFIRMATION);
      return;
    }
    if (opponentPlugin.getState() != PlayerState.ACCEPT_RULE_CONFIRMATION) {
      return;
    }
    if ((player.carryingMembersItems() || plugin.getOpponent().carryingMembersItems())
        && plugin.ruleSelected(Rule.IGNORE_FREEZING, RuleOption.ALLOWED)) {
      player.getGameEncoder()
          .sendMessage("Members items can't be taken with the F2P Content rule set.");
      plugin.getOpponent().getGameEncoder()
          .sendMessage("Members items can't be taken with the F2P Content rule set.");
      return;
    }
    if (player.getWorld().getPlayerInstance(player, "clan_wars_battle") != null) {
      player.getGameEncoder()
          .sendMessage("There is already a clan wars instance for this Clan Chat.");
      plugin.getOpponent().getGameEncoder()
          .sendMessage("There is already a clan wars instance for their Clan Chat.");
      return;
    }
    if (player.getWorld().getPlayerInstance(plugin.getOpponent(), "clan_wars_battle") != null) {
      player.getGameEncoder()
          .sendMessage("There is already a clan wars instance for their Clan Chat.");
      plugin.getOpponent().getGameEncoder()
          .sendMessage("There is already a clan wars instance for this Clan Chat.");
      return;
    }
    for (var nearbyPlayer : player.getController().getPlayers()) {
      if (nearbyPlayer == player || nearbyPlayer == plugin.getOpponent()) {
        continue;
      }
      if (!nearbyPlayer.getMessaging().matchesClanChat(player.getMessaging().getClanChatUsername())
          && !nearbyPlayer.getMessaging()
              .matchesClanChat(plugin.getOpponent().getMessaging().getClanChatUsername())) {
        continue;
      }
      nearbyPlayer.getGameEncoder().sendMessage(
          "<col=7F007F>Your clan is in battle! Step through the portals to join them.");
    }
    plugin.startWar(true, PRandom.randomI(1) != 0);
    opponentPlugin.startWar(false, !plugin.isTop());
  }

  public static void openJoin(Player player, ClanWarsPlugin plugin) {
    plugin.cancel();
    var clanUsername = player.getMessaging().getClanChatUsername();
    if (clanUsername == null) {
      player.getGameEncoder().sendMessage("You need to be in a Clan Chat to join.");
      return;
    }
    plugin.setTeammate(player.getWorld().getPlayerInstance(player, "clan_wars_battle"));
    if (plugin.getTeammate() == null) {
      player.getGameEncoder().sendMessage("Unable to find active war.");
      return;
    }
    plugin.setRules(plugin.getTeammate().getPlugin(ClanWarsPlugin.class).getRules());
    plugin.setState(PlayerState.JOIN);
    player.getWidgetManager().sendInteractiveOverlay(WidgetId.CLAN_WARS_CONFIRM,
        new WidgetManager.CloseEvent() {
          @Override
          public void execute() {
            if (plugin.getState() == PlayerState.BATTLE) {
              return;
            }
            plugin.cancel();
          }
        });
    player.getGameEncoder().sendWidgetText(WidgetId.CLAN_WARS_CONFIRM, 2, "Joining Clan Wars");
    player.getGameEncoder().sendScript(ScriptId.CLAN_WARS_RULE_DESCRIPTIONS,
        Rule.getDescriptions(plugin.getRules()));
    sendVarbits(player, plugin);
  }

  public static void acceptJoin(Player player, ClanWarsPlugin plugin) {
    if (!player.getWidgetManager().hasWidget(WidgetId.CLAN_WARS_CONFIRM)) {
      player.getWidgetManager().removeInteractiveWidgets();
      return;
    }
    if (plugin.getTeammate() == null || plugin.getTeammate().isLocked()) {
      player.getWidgetManager().removeInteractiveWidgets();
      return;
    }
    var opponentPlugin = plugin.getOpponent().getPlugin(ClanWarsPlugin.class);
    if (!player.getMessaging()
        .matchesClanChat(plugin.getTeammate().getMessaging().getClanChatUsername())
        || opponentPlugin.getState() != PlayerState.BATTLE) {
      player.getGameEncoder().sendMessage("Error joining war.");
      player.getWidgetManager().removeInteractiveWidgets();
      return;
    }
    if (plugin.ruleSelected(Rule.GAME_END, RuleOption.LAST_TEAM_STANDING)
        && opponentPlugin.getCountdown() == 0) {
      player.getGameEncoder().sendMessage("The war has already started.");
      player.getWidgetManager().removeInteractiveWidgets();
      return;
    }
    if (plugin.ruleSelected(Rule.IGNORE_FREEZING, RuleOption.ALLOWED)
        && player.carryingMembersItems()) {
      player.getGameEncoder().sendMessage("Members items can't be taken into this war.");
      return;
    }
    if (plugin.ruleSelected(Rule.PJ_TIMER, RuleOption.ALLOWED)
        && player.getController().getLevelForXP(Skills.DEFENCE) > 1) {
      player.getGameEncoder().sendMessage("This war is limited to 1 Defence players.");
      player.getWidgetManager().removeInteractiveWidgets();
      return;
    }
    plugin.setState(PlayerState.BATTLE);
    plugin.joinWar();
    player.rejuvenate();
    plugin.teleport();
  }

  public static void openView(Player player, ClanWarsPlugin plugin) {
    plugin.cancel();
    var clanUsername = player.getMessaging().getClanChatUsername();
    if (clanUsername == null) {
      player.getGameEncoder().sendMessage("You need to be in a Clan Chat to join.");
      return;
    }
    plugin.setTeammate(player.getWorld().getPlayerInstance(player, "clan_wars_battle"));
    if (plugin.getTeammate() == null) {
      player.getGameEncoder().sendMessage("Unable to find active war.");
      return;
    }
    plugin.setRules(plugin.getTeammate().getPlugin(ClanWarsPlugin.class).getRules());
    plugin.setState(PlayerState.VIEW_START);
    player.getWidgetManager().sendInteractiveOverlay(WidgetId.CLAN_WARS_CONFIRM,
        new WidgetManager.CloseEvent() {
          @Override
          public void execute() {
            if (plugin.getState() == PlayerState.VIEW) {
              return;
            }
            plugin.cancel();
          }
        });
    player.getGameEncoder().sendWidgetText(WidgetId.CLAN_WARS_CONFIRM, 2, "Viewing Clan Wars");
    player.getGameEncoder().sendScript(ScriptId.CLAN_WARS_RULE_DESCRIPTIONS,
        Rule.getDescriptions(plugin.getRules()));
    sendVarbits(player, plugin);
  }

  public static void acceptViewing(Player player, ClanWarsPlugin plugin) {
    if (plugin.getTeammate() == null || plugin.getTeammate().isLocked()
        || !player.getWidgetManager().hasWidget(WidgetId.CLAN_WARS_CONFIRM)
        || !player.getMessaging()
            .matchesClanChat(plugin.getTeammate().getMessaging().getClanChatUsername())
        || plugin.getTeammate().getPlugin(ClanWarsPlugin.class).getState() != PlayerState.BATTLE) {
      player.getGameEncoder().sendMessage("Error joining war.");
      player.getWidgetManager().removeInteractiveWidgets();
      return;
    }
    plugin.setState(PlayerState.VIEW);
    plugin.joinWar();
    plugin.teleportViewing();
  }

  public static void openCompletedState(Player player, ClanWarsPlugin plugin) {
    if (plugin.getCompleted() == CompletedState.WIN) {
      player.getWidgetManager().sendInteractiveOverlay(WidgetId.CLAN_WARS_COMPLETE);
      player.getGameEncoder().sendScript(ScriptId.CLAN_WARS_GAME_END_DESCRIPTION, 1,
          CompletedState.getDescription(plugin.getRule(Rule.GAME_END), true));
      player.getGameEncoder().sendMessage("Congratulations, you've won the war!");
    } else if (plugin.getCompleted() == CompletedState.LOSE) {
      player.getWidgetManager().sendInteractiveOverlay(WidgetId.CLAN_WARS_COMPLETE);
      player.getGameEncoder().sendScript(ScriptId.CLAN_WARS_GAME_END_DESCRIPTION,
          CompletedState.getDescription(plugin.getRule(Rule.GAME_END), false));
      player.getGameEncoder().sendMessage("Oh dear, you've lost the war!");
    }
    plugin.cancel();
  }

  public static void sendVarbits(Player player, ClanWarsPlugin plugin) {
    for (var rule : Rule.values()) {
      player.getGameEncoder().setVarbit(rule.getVarbit(), plugin.getRules()[rule.ordinal()]);
    }
  }
}
