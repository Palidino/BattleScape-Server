package com.palidinodh.osrsscript.incomingpacket.widget;

import com.palidinodh.osrscore.io.incomingpacket.WidgetHandler;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.random.PRandom;
import com.palidinodh.rs.ReferenceId;

@ReferenceId(WidgetId.EMOTES)
class EmotesWidget implements WidgetHandler {
  @Override
  public void execute(Player player, int option, int widgetId, int childId, int slot, int itemId) {
    if (player.isLocked()) {
      return;
    }
    if (player.getInCombatDelay() > 0 || player.isAttacking()) {
      return;
    }
    int animationId = -1;
    Graphic graphic = null;
    int lock = 0;
    switch (slot) {
      case 0: // Yes
        animationId = 855;
        break;
      case 1: // No
        animationId = 856;
        break;
      case 2: // Bow
        if (player.getEquipment().getLegId() == ItemId.PANTALOONS) {
          animationId = 5312;
        } else {
          animationId = 858;
        }
        break;
      case 3: // Angry
        if (player.getEquipment().getHeadId() == ItemId.A_POWDERED_WIG) {
          animationId = 5315;
        } else {
          animationId = 859;
        }
        break;
      case 4: // Think
        animationId = 857;
        break;
      case 5: // Wave
        animationId = 863;
        break;
      case 6: // Shrug
        animationId = 2113;
        break;
      case 7: // Cheer
        animationId = 862;
        break;
      case 8: // Beckon
        animationId = 864;
        break;
      case 9: // Laugh
        animationId = 861;
        break;
      case 10: // Jump for Joy
        animationId = 2109;
        break;
      case 11: // Yawn
        if (player.getEquipment().getHeadId() == ItemId.SLEEPING_CAP) {
          animationId = 5313;
        } else {
          animationId = 2111;
        }
        graphic = new Graphic(277);
        break;
      case 12: // Dance
        if (player.getEquipment().getLegId() == ItemId.FLARED_TROUSERS) {
          animationId = 5316;
        } else {
          animationId = 866;
        }
        break;
      case 13: // Jig
        animationId = 2106;
        break;
      case 14: // Spin
        animationId = 2107;
        break;
      case 15: // Headbang
        animationId = 2108;
        break;
      case 16: // Cry
        animationId = 860;
        break;
      case 17: // Blow Kiss
        animationId = 1374;
        break;
      case 18: // Panic
        animationId = 2105;
        break;
      case 19: // Raspberry
        animationId = 2110;
        break;
      case 20: // Clap
        animationId = 865;
        break;
      case 21: // Salute
        animationId = 2112;
        break;
      case 22: // Goblin Bow
        animationId = 2127;
        break;
      case 23: // Goblin Salute
        animationId = 2128;
        break;
      case 24: // Glass Box
        animationId = 1131;
        break;
      case 25: // Climb Rope
        animationId = 1130;
        break;
      case 26: // Lean
        animationId = 1129;
        break;
      case 27: // Glass Wall
        animationId = 1128;
        break;
      case 28: // Idea
        animationId = 4276;
        graphic = new Graphic(712);
        break;
      case 29: // Stamp
        animationId = 4278;
        break;
      case 30: // Flap
        animationId = 4280;
        break;
      case 31: // Slap Head
        animationId = 4275;
        break;
      case 32: // Zombie Walk
        animationId = 3544;
        break;
      case 33: // Zombie Dance
        animationId = 3543;
        break;
      case 34: // Scared
        animationId = 2836;
        break;
      case 35: // Rabbit Hop
        animationId = 6111;
        break;
      case 36: // Sit Up
        animationId = 2763;
        break;
      case 37: // Push Up
        animationId = 2762;
        break;
      case 38: // Star Jump
        animationId = 2761;
        break;
      case 39: // Jog
        animationId = 2764;
        break;
      case 40: // Zombie Hand
        animationId = 1708;
        graphic = new Graphic(320);
        break;
      case 41: // Hypermobile Drinker
        animationId = 7131;
        break;
      case 42: // Skill Cape
        switch (player.getEquipment().getCapeId()) {
          case ItemId.ATTACK_CAPE:
          case ItemId.ATTACK_CAPE_T:
            animationId = 4959;
            graphic = new Graphic(823);
            lock = 7;
            break;
          case ItemId.DEFENCE_CAPE:
          case ItemId.DEFENCE_CAPE_T:
            animationId = 4961;
            graphic = new Graphic(824);
            lock = 11;
            break;
          case ItemId.STRENGTH_CAPE:
          case ItemId.STRENGTH_CAPE_T:
            animationId = 4981;
            graphic = new Graphic(828);
            lock = 17;
            break;
          case ItemId.HITPOINTS_CAPE:
          case ItemId.HITPOINTS_CAPE_T:
            animationId = 4971;
            graphic = new Graphic(833);
            lock = 7;
            break;
          case ItemId.RANGING_CAPE:
          case ItemId.RANGING_CAPE_T:
            animationId = 4973;
            graphic = new Graphic(832);
            lock = 10;
            break;
          case ItemId.PRAYER_CAPE:
          case ItemId.PRAYER_CAPE_T:
            animationId = 4979;
            graphic = new Graphic(829);
            lock = 11;
            break;
          case ItemId.MAGIC_CAPE:
          case ItemId.MAGIC_CAPE_T:
            animationId = 4939;
            graphic = new Graphic(813);
            lock = 6;
            break;
          case ItemId.COOKING_CAPE:
          case ItemId.COOKING_CAPE_T:
            animationId = 4955;
            graphic = new Graphic(821);
            lock = 26;
            break;
          case ItemId.WOODCUTTING_CAPE:
          case ItemId.WOODCUT_CAPE_T:
            animationId = 4957;
            graphic = new Graphic(822);
            lock = 22;
            break;
          case ItemId.FLETCHING_CAPE:
          case ItemId.FLETCHING_CAPE_T:
            animationId = 4937;
            graphic = new Graphic(812);
            lock = 14;
            break;
          case ItemId.FISHING_CAPE:
          case ItemId.FISHING_CAPE_T:
            animationId = 4951;
            graphic = new Graphic(819);
            lock = 14;
            break;
          case ItemId.FIREMAKING_CAPE:
          case ItemId.FIREMAKING_CAPE_T:
            animationId = 4975;
            graphic = new Graphic(831);
            lock = 8;
            break;
          case ItemId.CRAFTING_CAPE:
          case ItemId.CRAFTING_CAPE_T:
            animationId = 4949;
            graphic = new Graphic(818);
            lock = 15;
            break;
          case ItemId.SMITHING_CAPE:
          case ItemId.SMITHING_CAPE_T:
            animationId = 4943;
            graphic = new Graphic(815);
            lock = 20;
            break;
          case ItemId.MINING_CAPE:
          case ItemId.MINING_CAPE_T:
            animationId = 4941;
            graphic = new Graphic(814);
            lock = 9;
            break;
          case ItemId.HERBLORE_CAPE:
          case ItemId.HERBLORE_CAPE_T:
            animationId = 4969;
            graphic = new Graphic(835);
            lock = 15;
            break;
          case ItemId.AGILITY_CAPE:
          case ItemId.AGILITY_CAPE_T:
            animationId = 4977;
            graphic = new Graphic(830);
            lock = 8;
            break;
          case ItemId.THIEVING_CAPE:
          case ItemId.THIEVING_CAPE_T:
            animationId = 4965;
            graphic = new Graphic(826);
            lock = 6;
            break;
          case ItemId.SLAYER_CAPE:
          case ItemId.SLAYER_CAPE_T:
            animationId = 4967;
            graphic = new Graphic(827);
            lock = 6;
            break;
          case ItemId.FARMING_CAPE:
          case ItemId.FARMING_CAPE_T:
            animationId = 4963;
            graphic = new Graphic(825);
            lock = 13;
            break;
          case ItemId.RUNECRAFT_CAPE:
          case ItemId.RUNECRAFT_CAPE_T:
            animationId = 4947;
            graphic = new Graphic(817);
            lock = 10;
            break;
          case ItemId.HUNTER_CAPE:
          case ItemId.HUNTER_CAPE_T:
            animationId = 5158;
            graphic = new Graphic(907);
            lock = 13;
            break;
          case ItemId.CONSTRUCT_CAPE:
          case ItemId.CONSTRUCT_CAPE_T:
            animationId = 4953;
            graphic = new Graphic(820);
            lock = 14;
            break;
          case ItemId.MAX_CAPE:
          case ItemId.MAX_CAPE_13280:
          case ItemId.FIRE_MAX_CAPE:
          case ItemId.SARADOMIN_MAX_CAPE:
          case ItemId.ZAMORAK_MAX_CAPE:
          case ItemId.GUTHIX_MAX_CAPE:
          case ItemId.ACCUMULATOR_MAX_CAPE:
          case ItemId.ARDOUGNE_MAX_CAPE:
          case ItemId.INFERNAL_MAX_CAPE:
          case ItemId.IMBUED_SARADOMIN_MAX_CAPE:
          case ItemId.IMBUED_ZAMORAK_MAX_CAPE:
          case ItemId.IMBUED_GUTHIX_MAX_CAPE:
          case ItemId.ASSEMBLER_MAX_CAPE:
            animationId = 7121;
            graphic = new Graphic(1286);
            lock = 9;
            break;
          default:
            player.getGameEncoder()
                .sendMessage("You need to be wearing a skill cape to do this emote.");
            break;
        }
        lock += 1;
        break;
      case 43: // Air Guitar
        animationId = 4751;
        graphic = new Graphic(1239);
        break;
      case 45: // Smooth Dance
        animationId = 7533;
        break;
      case 46: // Crazy Dance
        animationId = PRandom.randomE(2) == 1 ? 7537 : 7536;
        break;
    }
    if (animationId != -1) {
      player.setAnimation(animationId);
      if (graphic != null) {
        player.setGraphic(graphic);
      }
      if (lock > 0) {
        player.setLock(lock);
        player.getMovement().clear();
      }
    }
  }
}
