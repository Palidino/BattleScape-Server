package com.palidinodh.osrsscript.player.plugin.clanwars;

import java.util.List;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.Entity;
import com.palidinodh.osrscore.model.HitType;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.item.ItemDef;
import com.palidinodh.osrscore.model.map.MapItem;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.player.Loadout;
import com.palidinodh.osrscore.model.player.Magic;
import com.palidinodh.osrscore.model.player.Messaging;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.Prayer;
import com.palidinodh.osrscore.model.player.Skills;
import com.palidinodh.osrscore.model.player.controller.PController;
import com.palidinodh.osrsscript.world.event.pvptournament.PvpTournament;
import com.palidinodh.rs.setting.Settings;

public class ClanWarsPC extends PController {
  public static final long serialVersionUID = 12022016L;

  private transient Player p;
  private transient PvpTournament tournament;
  private transient ClanWarsPlugin plugin;
  private Tile exitTile;
  private int lastUpdate = 4;
  private String clanChatUsername;
  private int tournamentInterfaceDelay;

  @Override
  public void setEntityHook(Entity entity) {
    p = (Player) entity;
    if (p == null) {
      return;
    }
    plugin = p.getPlugin(ClanWarsPlugin.class);
    setBlockTeleport(true);
    exitTile = new Tile(entity);
    p.getGameEncoder().sendPlayerOption("null", 1, false);
    p.getGameEncoder().sendHintIconReset();
    if (plugin.getState() == ClanWarsPlayerState.TOURNAMENT) {
      tournament = p.getWorld().getWorldEvent(PvpTournament.class);
      p.getPrayer().setAllowAllPrayers(true);
      p.getMagic().sendMagicConfigs();
    } else {
      p.getGameEncoder().sendPlayerOption("Attack", 2, false);
      p.getWidgetManager().sendOverlay(88);
      plugin.sendBattleVarbits(0, 0, null);
      p.getAppearance()
          .setHiddenTeamId(plugin.isTop() ? ItemId.CLAN_WARS_CAPE : ItemId.CLAN_WARS_CAPE_12675);
    }
    clanChatUsername = p.getMessaging().getClanChatUsername();
  }

  @Override
  public void stopHook() {
    if (plugin.getState() == ClanWarsPlayerState.TOURNAMENT) {
      p.getGameEncoder().sendHideWidget(WidgetId.LMS_LOBBY_OVERLAY, 0, false);
      p.getInventory().clear();
      p.getEquipment().clear();
      tournament.removePlayer(p);
      p.getMagic().sendMagicConfigs();
    } else {
      p.getGameEncoder().sendPlayerOption("Challenge", 1, false);
      p.getAppearance().setHiddenTeamId(-1);
      p.rejuvenate();
    }
    p.getGameEncoder().sendPlayerOption("null", 2, false);
    p.getWidgetManager().removeOverlay();
    p.getWidgetManager().removeFullOverlay();
    p.getWidgetManager().removeInteractiveWidgets();
    p.getMovement().teleport(exitTile);
    p.restore();
    ClanWarsStages.openCompletedState(p, plugin);
    p.getCombat().setUsingWildernessInterface(false);
    p.getSkills().setCombatLevel();
    p.getGameEncoder().sendWorldMode(p.getWorld().getMode());
    p.getPrayer().setAllowAllPrayers(false);
  }

  @Override
  public Object getVariableHook(String identifier) {
    if (identifier.equals("clan_wars")) {
      return true;
    } else if (identifier.equals("clan_wars_battle")) {
      return plugin.getState() == ClanWarsPlayerState.BATTLE;
    }
    return null;
  }

  @Override
  public void tickHook() {
    if (plugin.getState() == ClanWarsPlayerState.TOURNAMENT && !p.isLocked()
        && !p.getMovement().getTeleporting() && !p.getMovement().getTeleported()) {
      if (!p.inClanWarsTournamentLobby() && !p.inClanWarsBattle()) {
        stop();
        return;
      }
      if (plugin.getOpponent() != null && !plugin.getOpponent().inClanWarsBattle()) {
        plugin.setOpponent(null);
      }
    }
    setMultiCombatFlag(plugin.getState() != ClanWarsPlayerState.TOURNAMENT);
    if (lastUpdate > 0) {
      lastUpdate--;
      if (lastUpdate == 0) {
        lastUpdate = 8;
        checkStatus();
      }
    }
    if (p != null && p.getMovement().isViewing() && p.getMovement().getViewing() != null
        && p.getMovement().getViewing().getX() != 0 && !p.getMovement().getTeleporting()
        && !p.getMovement().getTeleported()
        && !p.getWidgetManager().hasWidget(WidgetId.CLAN_WARS_ORBS)
        && !p.getWidgetManager().hasWidget(WidgetId.TOURNAMENT_VIEWER)) {
      p.getMovement().stopViewing();
    }
    if (tournamentInterfaceDelay > 0) {
      tournamentInterfaceDelay--;
      if (p != null && tournamentInterfaceDelay == 0) {
        p.getGameEncoder().setVarp(1434, 0);
        p.getGameEncoder().sendWorldMode(p.getWorld().getMode());
      }
    }
  }

  @Override
  public void applyDead() {
    p.clearHits();
    if (p.getRespawnDelay() == Entity.PLAYER_RESPAWN_DELAY - 2) {
      p.setAnimation(DEATH_ANIMATION);
    } else if (p.getRespawnDelay() == 0) {
      for (Player player2 : getPlayers()) {
        if (player2.getPlugin(ClanWarsPlugin.class).getState() != ClanWarsPlayerState.BATTLE
            || plugin.isTop() == player2.getPlugin(ClanWarsPlugin.class).isTop()) {
          continue;
        }
        player2.getPlugin(ClanWarsPlugin.class).incrimentTotalKills();
      }
      boolean isTournament = plugin.getState() == ClanWarsPlayerState.TOURNAMENT;
      if (isTournament) {
        p.getInventory().clear();
        p.getEquipment().clear();
        tournament.removePlayer(p);
        tournament.checkPrizes(p, false);
      }
      if (plugin.ruleSelected(ClanWarsRule.GAME_END, ClanWarsRuleOption.LAST_TEAM_STANDING)
          && !isTournament) {
        p.restore();
        plugin.setState(ClanWarsPlayerState.VIEW);
        plugin.teleportViewing();
      } else {
        stop();
      }
    }
  }

  @Override
  public boolean canMagicBindHook() {
    return true/* && plugin.ruleSelected(ClanWars.IGNORE_FREEZING, ClanWars.DISABLED) */;
  }

  @Override
  public boolean allowMultiTargetAttacksHook() {
    return plugin.ruleSelected(ClanWarsRule.SINGLE_SPELLS, ClanWarsRuleOption.DISABLED);
  }

  @Override
  public MapItem addMapItemHook(MapItem mapItem) {
    if (plugin.getState() == ClanWarsPlayerState.TOURNAMENT) {
      mapItem.setNeverAppear();
      if (!p.inClanWarsBattle()) {
        mapItem = null;
      }
    }
    return mapItem;
  }

  @Override
  public boolean canEatHook(int itemId) {
    if (plugin.ruleSelected(ClanWarsRule.FOOD, ClanWarsRuleOption.DISABLED)) {
      p.getGameEncoder().sendMessage("You can't eat food in this war.");
      return false;
    }
    if (plugin.ruleSelected(ClanWarsRule.IGNORE_FREEZING, ClanWarsRuleOption.ALLOWED)
        && ItemDef.isMembers(itemId)) {
      p.getGameEncoder().sendMessage("You can't eat this food in this war.");
      return false;
    }
    return true;
  }

  @Override
  public boolean canDrinkHook(int itemId) {
    if (plugin.ruleSelected(ClanWarsRule.DRINKS, ClanWarsRuleOption.DISABLED)) {
      p.getGameEncoder().sendMessage("You can't drink potions in this war.");
      return false;
    }
    if (plugin.ruleSelected(ClanWarsRule.FOOD, ClanWarsRuleOption.DISABLED)
        && Skills.getDrink(itemId).doesHeal()) {
      p.getGameEncoder().sendMessage("You can't eat food in this war.");
      return false;
    }
    if (plugin.ruleSelected(ClanWarsRule.IGNORE_FREEZING, ClanWarsRuleOption.ALLOWED)
        && ItemDef.isMembers(itemId)) {
      p.getGameEncoder().sendMessage("You can't drink this potion in this war.");
      return false;
    }
    return true;
  }

  @Override
  public int getLevelForXP(int index) {
    if (plugin.getState() == ClanWarsPlayerState.TOURNAMENT) {
      if (index < tournament.getMode().getSkillLevels().length) {
        return tournament.getMode().getSkillLevels()[index];
      } else {
        return 99;
      }
    }
    return super.getLevelForXP(index);
  }

  @Override
  public int getXP(int index) {
    if (plugin.getState() == ClanWarsPlayerState.TOURNAMENT) {
      if (index < tournament.getMode().getSkillLevels().length) {
        return Skills.XP_TABLE[tournament.getMode().getSkillLevels()[index]];
      } else {
        return Skills.XP_TABLE[99];
      }
    }
    return super.getXP(index);
  }

  @Override
  public boolean canGainXP() {
    return false;
  }

  @Override
  public int getXPMultiplier(int skillId) {
    return plugin.getState() == ClanWarsPlayerState.TOURNAMENT ? 1 : -1;
  }

  @Override
  public boolean canTradeHook(Player player2) {
    p.getGameEncoder().sendMessage("You can't trade during a war.");
    return false;
  }

  @Override
  public boolean canBankHook() {
    return false;
  }

  @Override
  public boolean canEquipHook(int itemId, int slot) {
    if (plugin.ruleSelected(ClanWarsRule.IGNORE_FREEZING, ClanWarsRuleOption.ALLOWED)
        && ItemDef.isMembers(itemId)) {
      return false;
    }
    return true;
  }

  @Override
  public boolean canActivatePrayerHook(int childId) {
    if (plugin.ruleSelected(ClanWarsRule.PRAYER, ClanWarsRuleOption.DISABLED)) {
      return false;
    } else if (plugin.ruleSelected(ClanWarsRule.PRAYER, ClanWarsRuleOption.NO_OVERHEADS)) {
      if (Prayer.getPrayerDef(childId) != null
          && Prayer.getPrayerDef(childId).getHeadiconId() != -1) {
        return false;
      }
    }
    if (plugin.ruleSelected(ClanWarsRule.IGNORE_FREEZING, ClanWarsRuleOption.ALLOWED)
        && Prayer.getPrayerDef(childId) != null
        && Prayer.getPrayerDef(childId).getIdentifier() != null) {
      String identifier = Prayer.getPrayerDef(childId).getIdentifier();
      if (identifier.equals("retribution") || identifier.equals("redemption")
          || identifier.equals("smite") || identifier.equals("preserve")
          || identifier.equals("chivalry") || identifier.equals("piety")
          || identifier.equals("rigour") || identifier.equals("augury")) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean canAttackPlayer(Player p2, boolean sendMessage, HitType hitType) {
    boolean tridentAttack = hitType == HitType.MAGIC && p.getMagic().getActiveSpell() != null
        && (p.getMagic().getActiveSpell().getName().equals("trident of the seas")
            || p.getMagic().getActiveSpell().getName().equals("trident of the swamp")
            || p.getMagic().getActiveSpell().getName().equals("sanguinesti staff"));
    if (plugin.ruleSelected(ClanWarsRule.MELEE, ClanWarsRuleOption.DISABLED)
        && hitType == HitType.MELEE) {
      if (sendMessage) {
        p.getGameEncoder().sendMessage("You can't use melee in this war.");
      }
      return false;
    } else if (plugin.ruleSelected(ClanWarsRule.RANGING, ClanWarsRuleOption.DISABLED)
        && hitType == HitType.RANGED) {
      if (sendMessage) {
        p.getGameEncoder().sendMessage("You can't use ranged in this war.");
      }
      return false;
    } else if (plugin.ruleSelected(ClanWarsRule.MAGIC, ClanWarsRuleOption.DISABLED)
        && hitType == HitType.MAGIC) {
      if (sendMessage) {
        p.getGameEncoder().sendMessage("You can't use magic in this war.");
      }
      return false;
    } else if (plugin.ruleSelected(ClanWarsRule.MAGIC, ClanWarsRuleOption.STANDARD_SPELLS)
        && hitType == HitType.MAGIC
        && (p.getMagic().getSpellbook() != Magic.STANDARD_MAGIC || tridentAttack)) {
      if (sendMessage) {
        p.getGameEncoder().sendMessage("You can't use this spellbook in this war.");
      }
      return false;
    } else if (plugin.ruleSelected(ClanWarsRule.MAGIC, ClanWarsRuleOption.BINDING_ONLY)
        && hitType == HitType.MAGIC && p.getMagic().getActiveSpell() != null
        && !p.getMagic().getActiveSpell().getName().equals("bind")
        && !p.getMagic().getActiveSpell().getName().equals("snare")
        && !p.getMagic().getActiveSpell().getName().equals("entangle")) {
      if (sendMessage) {
        p.getGameEncoder().sendMessage("You can't use this spell in this war.");
      }
      return false;
    } else if (plugin.ruleSelected(ClanWarsRule.ALLOW_TRIDENT_IN_PVP, ClanWarsRuleOption.DISABLED)
        && tridentAttack) {
      if (sendMessage) {
        p.getGameEncoder().sendMessage("You can't use this spell in this war.");
      }
      return false;
    } else if (plugin.getCountdown() > 0 || plugin.getState() != ClanWarsPlayerState.BATTLE
        && plugin.getState() != ClanWarsPlayerState.TOURNAMENT) {
      return false;
    } else if (p2 != null
        && p2.getPlugin(ClanWarsPlugin.class).getState() != ClanWarsPlayerState.BATTLE
        && p2.getPlugin(ClanWarsPlugin.class).getState() != ClanWarsPlayerState.TOURNAMENT) {
      if (sendMessage) {
        p.getGameEncoder().sendMessage("You can't attack this player.");
      }
      return false;
    } else if (plugin.ruleSelected(ClanWarsRule.IGNORE_FREEZING, ClanWarsRuleOption.ALLOWED)
        && hitType == HitType.MAGIC && p.getMagic().getActiveSpell() != null) {
      if (p.getMagic().getSpellbook() != Magic.STANDARD_MAGIC) {
        if (sendMessage) {
          p.getGameEncoder().sendMessage("You can't use this spellbook in this war.");
        }
        return false;
      } else if (p.getMagic().getActiveSpell().getChildId() > 39
          || p.getMagic().getActiveSpell().getChildId() == 30) {
        if (sendMessage) {
          p.getGameEncoder().sendMessage("You can't use this spell in this war.");
        }
        return false;
      }
    }
    if (plugin.getState() == ClanWarsPlayerState.TOURNAMENT) {
      if (!p.inClanWarsBattle()) {
        return false;
      }
      if (plugin.getOpponent() != p2) {
        if (sendMessage) {
          p.getGameEncoder().sendMessage("This player isn't your opponent.");
        }
        return false;
      }
      return true;
    }
    return p2 != null && plugin.isTop() != p2.getPlugin(ClanWarsPlugin.class).isTop();
  }

  @Override
  public boolean canActivateSpecialAttackHook() {
    if (plugin.ruleSelected(ClanWarsRule.SPECIAL_ATTACKS, ClanWarsRuleOption.DISABLED)) {
      p.getGameEncoder().sendMessage("You can't use special attacks in this war.");
      return false;
    }
    if (plugin.ruleSelected(ClanWarsRule.SPECIAL_ATTACKS,
        ClanWarsRuleOption.NO_STAFF_OF_THE_DEAD)) {
      if (p.getEquipment().getWeaponId() == 11791 || p.getEquipment().getWeaponId() == 12902
          || p.getEquipment().getWeaponId() == 12904 || p.getEquipment().getWeaponId() == 22296) {
        p.getGameEncoder().sendMessage("You can't this special attack in this war.");
        return false;
      }
    }
    return true;
  }

  @Override
  public int[] runePouchHook() {
    return plugin.getState() == ClanWarsPlayerState.TOURNAMENT
        ? tournament.getMode().getRunes().stream().mapToInt(i -> i).toArray()
        : null;
  }

  @Override
  public boolean spawnLoadouts() {
    return plugin.getState() == ClanWarsPlayerState.TOURNAMENT;
  }

  @Override
  public boolean inLoadoutZoneHook() {
    return plugin.getState() == ClanWarsPlayerState.TOURNAMENT ? p.inClanWarsTournamentLobby()
        : false;
  }

  @Override
  public List<Loadout.Entry> getLoadoutEntriesHook() {
    return plugin.getState() == ClanWarsPlayerState.TOURNAMENT ? tournament.getMode().getLoadouts()
        : null;
  }

  @Override
  public boolean widgetHook(int option, int widgetId, int childId, int slot, int itemId) {
    if (p.isLocked() || p.getMovement().getTeleporting() || p.getMovement().getTeleported()) {
      return true;
    }
    if (widgetId == WidgetId.BANK || widgetId == WidgetId.BANK_INVENTORY
        || widgetId == WidgetId.DEPOSIT_BOX || widgetId == WidgetId.LOOTING_BAG_DEPOSIT
        || widgetId == WidgetId.TRADE || widgetId == WidgetId.TRADE_INVENTORY
        || widgetId == WidgetId.DUEL_STAKE) {
      stop();
      return true;
    } else if (widgetId == WidgetId.RUNE_POUCH
        || widgetId == WidgetId.INVENTORY && itemId == ItemId.RUNE_POUCH) {
      p.getGameEncoder().sendMessage("You can't change your pouch right now.");
      return true;
    } else if (widgetId == Messaging.INTERFACE_CLAN_ID) {
      p.getGameEncoder().sendMessage("You can't change Clan Chat settings right now.");
      return true;
    } else if (widgetId == WidgetId.CUSTOM_BOND_POUCH) {
      p.getGameEncoder().sendMessage("You can't use bonds right now.");
      return true;
    } else if (plugin.ruleSelected(ClanWarsRule.MAGIC, ClanWarsRuleOption.DISABLED)
        && (widgetId == Magic.INTERFACE_ID || widgetId == Magic.INTERFACE_SPELL_SELECT_ID)) {
      p.getGameEncoder().sendMessage("You can't use magic in this war.");
      return true;
    } else if (plugin.ruleSelected(ClanWarsRule.MAGIC, ClanWarsRuleOption.BINDING_ONLY)
        && (widgetId == Magic.INTERFACE_ID || widgetId == Magic.INTERFACE_SPELL_SELECT_ID)) {
      p.getGameEncoder().sendMessage("You can't use this spellbook in this war.");
      return true;
    } else if (plugin.ruleSelected(ClanWarsRule.MAGIC, ClanWarsRuleOption.STANDARD_SPELLS)
        && (widgetId == Magic.INTERFACE_ID || widgetId == Magic.INTERFACE_SPELL_SELECT_ID)
        && p.getMagic().getSpellbook() != Magic.STANDARD_MAGIC) {
      p.getGameEncoder().sendMessage("You can't use this spellbook in this war.");
      return true;
    } else if (widgetId == WidgetId.CLAN_WARS_ORBS) {
      switch (childId) {
        case 3:
          p.getMovement().stopViewing();
          p.getWidgetManager().removeInteractiveWidgets();
          break;
        case 7:
          plugin.teleportViewing(0);
          break;
        case 8:
          plugin.teleportViewing(1);
          break;
        case 4:
          plugin.teleportViewing(2);
          break;
        case 5:
          plugin.teleportViewing(3);
          break;
        case 6:
          plugin.teleportViewing(4);
          break;
      }
      return true;
    }
    return false;
  }

  @Override
  public boolean mapObjectOptionHook(int option, MapObject mapObject) {
    if (p.isLocked() || p.getMovement().getTeleporting() || p.getMovement().getTeleported()) {
      return true;
    }
    switch (mapObject.getId()) {
      case 32446:
        if (plugin.getState() != ClanWarsPlayerState.TOURNAMENT
            || tournament.getMode().getLoadouts() == null) {
          break;
        }
        p.getLoadout().openSelection();
        return true;
    }
    return false;
  }

  @Override
  public boolean itemOnItemHook(int useWidgetId, int useChildId, int onWidgetId, int onChildId,
      int useSlot, int onSlot, int useItemId, int onItemId) {
    if (p.isLocked() || p.getMovement().getTeleporting() || p.getMovement().getTeleported()) {
      return true;
    }
    if (PController.hasItemOnItemMatch(useItemId, onItemId, ItemId.RUNE_POUCH)
        || PController.hasItemOnItemMatch(useItemId, onItemId, ItemId.LOOTING_BAG)) {
      p.getGameEncoder().sendMessage("You can't do this right now.");
      return true;
    }
    return false;
  }

  public void checkStatus() {
    if (plugin.getState() == ClanWarsPlayerState.TOURNAMENT) {
      return;
    }
    if (clanChatUsername == null || !p.getMessaging().matchesClanChat(clanChatUsername)) {
      stop();
      return;
    }
    int myTeamCount = 0;
    int otherTeamCount = 0;
    List<Player> players = getPlayers();
    Player opponent = null;
    for (int i = 0; i < players.size(); i++) {
      Player p2 = players.get(i);
      if (p2.getPlugin(ClanWarsPlugin.class).getState() != ClanWarsPlayerState.BATTLE) {
        continue;
      }
      if (plugin.isTop() == p2.getPlugin(ClanWarsPlugin.class).isTop()) {
        myTeamCount++;
      } else {
        otherTeamCount++;
        opponent = p2;
      }
    }
    boolean meetsGameEnd = false;
    if (plugin.ruleSelected(ClanWarsRule.GAME_END, ClanWarsRuleOption.KILLS_25)
        && plugin.getTotalKills() >= 25) {
      meetsGameEnd = true;
    } else if (plugin.ruleSelected(ClanWarsRule.GAME_END, ClanWarsRuleOption.KILLS_50)
        && plugin.getTotalKills() >= 50) {
      meetsGameEnd = true;
    } else if (plugin.ruleSelected(ClanWarsRule.GAME_END, ClanWarsRuleOption.KILLS_100)
        && plugin.getTotalKills() >= 100) {
      meetsGameEnd = true;
    } else if (plugin.ruleSelected(ClanWarsRule.GAME_END, ClanWarsRuleOption.KILLS_200)
        && plugin.getTotalKills() >= 200) {
      meetsGameEnd = true;
    } else if (plugin.ruleSelected(ClanWarsRule.GAME_END, ClanWarsRuleOption.KILLS_500)
        && plugin.getTotalKills() >= 500) {
      meetsGameEnd = true;
    } else if (plugin.ruleSelected(ClanWarsRule.GAME_END, ClanWarsRuleOption.KILLS_1000)
        && plugin.getTotalKills() >= 1000) {
      meetsGameEnd = true;
    } else if (plugin.ruleSelected(ClanWarsRule.GAME_END, ClanWarsRuleOption.KILLS_5000)
        && plugin.getTotalKills() >= 5000) {
      meetsGameEnd = true;
    } else if (plugin.ruleSelected(ClanWarsRule.GAME_END, ClanWarsRuleOption.KILLS_10000)
        && plugin.getTotalKills() >= 10000) {
      meetsGameEnd = true;
    }
    plugin.sendBattleVarbits(myTeamCount, otherTeamCount, opponent);
    boolean ignore5 = plugin.ruleSelected(ClanWarsRule.STRAGGLERS, ClanWarsRuleOption.IGNORE_5);
    if (!meetsGameEnd && (!ignore5 && otherTeamCount > 0 || ignore5 && otherTeamCount > 5)) {
      return;
    }
    final Player myPlayer = p;
    String lostClanName = null;
    for (int i = 0; i < players.size(); i++) {
      Player p2 = players.get(i);
      if (plugin.isTop() == p2.getPlugin(ClanWarsPlugin.class).isTop()) {
        p2.getPlugin(ClanWarsPlugin.class).setCompleted(CompletedState.WIN);
      } else {
        p2.getPlugin(ClanWarsPlugin.class).setCompleted(CompletedState.LOSE);
        lostClanName = p2.getMessaging().getClanChatName();
      }
      p2.getController().stop();
    }
    if (Settings.isBeta()) {
      if (lostClanName != null) {
        myPlayer.getWorld().sendNews(myPlayer.getMessaging().getClanChatName() + " has defeated "
            + lostClanName + " in a clan war!");
      } else {
        myPlayer.getWorld()
            .sendNews(myPlayer.getMessaging().getClanChatName() + " has won a clan war!");
      }
    }
  }
}
