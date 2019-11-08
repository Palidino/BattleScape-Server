package com.palidinodh.osrsscript.incomingpacket.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.dialogue.LargeOptionsDialogue;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.setting.SqlUserRank;
import com.palidinodh.util.PTime;
import lombok.var;

public class PlayersCommand implements CommandHandler {
  @Override
  public boolean canUse(Player player) {
    return player.isUsergroup(SqlUserRank.TRIAL_MODERATOR)
        || player.getRights() == Player.RIGHTS_MOD || player.getRights() == Player.RIGHTS_ADMIN
        || player.isUsergroup(SqlUserRank.COMMUNITY_MANAGER);
  }

  @Override
  public void execute(Player player, String message) {
    var players = new ArrayList<Player>(player.getWorld().getPlayers());
    Collections.sort(players, new Comparator<Player>() {
      @Override
      public int compare(Player p1, Player p2) {
        return Integer.compare(p1.getIdleTime(), p2.getIdleTime());
      }
    });
    var dialogue = new LargeOptionsDialogue("Players").action((childId, slot) -> {
      if (player.isDead() || player.getInCombatDelay() > 0) {
        return;
      }
      if (slot < 0 || slot >= players.size()) {
        return;
      }
      var player2 = players.get(slot);
      if (player2 == null || !player2.isVisible()) {
        player.getGameEncoder().sendMessage("Unable to find user.");
        return;
      } else if (player == player2) {
        player.getGameEncoder().sendMessage("You can't view yourself.");
        return;
      } else if (player.getController().isInstanced()) {
        player.getGameEncoder().sendMessage("You can't view while in an instance.");
        return;
      } else if (player2.getController().isInstanced()) {
        player.getGameEncoder()
            .sendMessage(player2.getUsername() + " is in an instance located at: " + player2.getX()
                + ", " + player2.getY() + ", " + player2.getHeight() + ".");
        return;
      }
      var viewTile = new Tile(player2);
      viewTile.randomize(1);
      player.getMovement().setViewing(viewTile);
      player.getWidgetManager().sendInventoryOverlay(WidgetId.UNMORPH);
    });
    for (var player2 : players) {
      var icon = player2.getMessaging().getIconImage();
      var username = player2.getUsername();
      var location = getLocation(player2);
      dialogue
          .addOption(icon + username + ": on: " + PTime.ticksToLongDuration(player2.getTotalTicks())
              + ", map: " + PTime.ticksToLongDuration(player2.getLastMapUpdate()) + ", idle: "
              + PTime.ticksToLongDuration(player2.getIdleTime()) + ", loc: " + location);
    }
    player.openDialogue(dialogue);
    player.getGameEncoder().sendMessage(
        "There are currently " + player.getWorld().getPlayerCount() + " players online, with "
            + player.getWorld().getWildernessPlayerCount() + " in the wilderness.");
  }

  private static String getLocation(Player player) {
    var location = "Dungeon";
    if (player.getX() >= 1562 && player.getY() >= 3471 && player.getX() <= 1658
        && player.getY() <= 3519) {
      location = "Woodcutting Guild";
    } else if (player.getX() >= 1710 && player.getY() >= 3435 && player.getX() <= 1875
        && player.getY() <= 3473) {
      location = "Sand Crabs";
    } else if (player.getX() >= 1808 && player.getY() >= 3480 && player.getX() <= 1821
        && player.getY() <= 3493) {
      location = "Hosidius Farm";
    } else if (player.getX() >= 1740 && player.getY() >= 3670 && player.getX() <= 1865
        && player.getY() <= 3804) {
      location = "Piscarilius House";
    } else if (player.getX() >= 1143 && player.getY() >= 3311 && player.getX() <= 2001
        && player.getY() <= 4099) {
      location = "Great Kourend";
    } else if (player.getX() >= 2045 && player.getY() >= 3837 && player.getX() <= 2186
        && player.getY() <= 3969) {
      location = "Lunar Isle";
    } else if (player.getX() >= 2171 && player.getY() >= 3781 && player.getX() <= 2238
        && player.getY() <= 3842) {
      location = "Pirate's Cove";
    } else if (player.getX() >= 2303 && player.getY() >= 3776 && player.getX() <= 2367
        && player.getY() <= 3821) {
      location = "Neitiznot";
    } else if (player.getX() >= 2368 && player.getY() >= 3776 && player.getX() <= 2435
        && player.getY() <= 3827) {
      location = "Jatizso";
    } else if (player.getX() >= 2368 && player.getY() >= 3844 && player.getX() <= 2381
        && player.getY() <= 3858) {
      location = "Fremennik Isles Small Mine";
    } else if (player.getX() >= 2297 && player.getY() >= 3772 && player.getX() <= 2439
        && player.getY() <= 3906) {
      location = "Fremennik Isles";
    } else if (player.getX() >= 2490 && player.getY() >= 3710 && player.getX() <= 2565
        && player.getY() <= 3772) {
      location = "Waterbirth Island";
    } else if (player.getX() >= 2483 && player.getY() >= 3811 && player.getX() <= 2636
        && player.getY() <= 3928) {
      location = "Miscellania";
    } else if (player.getX() >= 2602 && player.getY() >= 2552 && player.getX() <= 2710
        && player.getY() <= 2683) {
      location = "Pest Control";
    } else if (player.getX() >= 2688 && player.getY() >= 2686 && player.getX() <= 2824
        && player.getY() <= 2816) {
      location = "Ape Atoll";
    } else if (player.getX() >= 2882 && player.getY() >= 2694 && player.getX() <= 2943
        && player.getY() <= 2747) {
      location = "Crash Island";
    } else if (player.getX() >= 3770 && player.getY() >= 2812 && player.getX() <= 3844
        && player.getY() <= 2879) {
      location = "Harmony Island";
    } else if (player.getX() >= 3635 && player.getY() >= 2920 && player.getX() <= 3866
        && player.getY() <= 3077) {
      location = "Mos Le'Harmless";
    } else if (player.getX() >= 2807 && player.getY() >= 3218 && player.getX() <= 2874
        && player.getY() <= 3312) {
      location = "Crandor";
    } else if (player.getX() >= 2756 && player.getY() >= 3268 && player.getX() <= 2800
        && player.getY() <= 3295) {
      location = "Fishing Platform";
    } else if (player.getX() >= 2800 && player.getY() >= 3327 && player.getX() <= 2877
        && player.getY() <= 3394) {
      location = "Entrana";
    } else if (player.getX() >= 3043 && player.getY() >= 3060 && player.getX() <= 3157
        && player.getY() <= 3134) {
      location = "Tutorial Island";
    } else if (player.getX() >= 2426 && player.getY() >= 2812 && player.getX() <= 2478
        && player.getY() <= 2875) {
      location = "Myth's Guild";
    } else if (player.getX() >= 2426 && player.getY() >= 2812 && player.getX() <= 2478
        && player.getY() <= 2875) {
      location = "Myth's Guild";
    } else if (player.getX() >= 2537 && player.getY() >= 2837 && player.getX() <= 2607
        && player.getY() <= 2876) {
      location = "Corsair Cove";
    } else if (player.getX() >= 2489 && player.getY() >= 2877 && player.getX() <= 2626
        && player.getY() <= 2948) {
      location = "Feldip Hills Hunter";
    } else if (player.getX() >= 2363 && player.getY() >= 3066 && player.getX() <= 2476
        && player.getY() <= 3141) {
      location = "Castle Wars";
    } else if (player.getX() >= 2312 && player.getY() >= 3144 && player.getX() <= 2362
        && player.getY() <= 3199) {
      location = "Lleyta";
    } else if (player.getX() >= 2178 && player.getY() >= 3037 && player.getX() <= 2223
        && player.getY() <= 3074) {
      location = "Zul-Andra";
    } else if (player.getX() >= 2256 && player.getY() >= 3063 && player.getX() <= 2280
        && player.getY() <= 3084) {
      location = "Zulrah's Shrine";
    } else if (player.getX() >= 2139 && player.getY() >= 3100 && player.getX() <= 2226
        && player.getY() <= 3164) {
      location = "Tyras Camp";
    } else if (player.getX() >= 2173 && player.getY() >= 3230 && player.getX() <= 2218
        && player.getY() <= 3265) {
      location = "Elf Camp";
    } else if (player.getX() >= 2174 && player.getY() >= 3268 && player.getX() <= 2300
        && player.getY() <= 3390) {
      location = "Prifddinas";
    } else if (player.getX() >= 2104 && player.getY() >= 3113 && player.getX() <= 2359
        && player.getY() <= 3458) {
      location = "Elven Land";
    } else if (player.getX() >= 2467 && player.getY() >= 3411 && player.getX() <= 2492
        && player.getY() <= 3442) {
      location = "Gnome Agility";
    } else if (player.getX() >= 2467 && player.getY() >= 3411 && player.getX() <= 2492
        && player.getY() <= 3442) {
      location = "Gnome Agility";
    } else if (player.getX() >= 2371 && player.getY() >= 3387 && player.getX() <= 2504
        && player.getY() <= 3523) {
      location = "Tree Gnome Stronghold";
    } else if (player.getX() >= 2527 && player.getY() >= 3541 && player.getX() <= 2554
        && player.getY() <= 3557) {
      location = "Barbarian Agility";
    } else if (player.getX() >= 2496 && player.getY() >= 3531 && player.getX() <= 2563
        && player.getY() <= 3592) {
      location = "Barbarian Outpost";
    } else if (player.getX() >= 2494 && player.getY() >= 3462 && player.getX() <= 2540
        && player.getY() <= 3535) {
      location = "Pyre Ship/Grotto";
    } else if (player.getX() >= 2488 && player.getY() >= 3596 && player.getX() <= 2591
        && player.getY() <= 3653) {
      location = "Lighthouse";
    } else if (player.getX() >= 2531 && player.getY() >= 3070 && player.getX() <= 2624
        && player.getY() <= 3111) {
      location = "Yanille";
    } else if (player.getX() >= 2577 && player.getY() >= 3393 && player.getX() <= 2624
        && player.getY() <= 3428) {
      location = "Fishing Guild";
    } else if (player.getX() >= 2605 && player.getY() >= 3439 && player.getX() <= 2624
        && player.getY() <= 3448) {
      location = "Fishing Guild Platform";
    } else if (player.getX() >= 2681 && player.getY() >= 3450 && player.getX() <= 2742
        && player.getY() <= 3517) {
      location = "Seers' Village";
    } else if (player.getX() >= 2597 && player.getY() >= 3642 && player.getX() <= 2695
        && player.getY() <= 3744) {
      location = "Rellekka";
    } else if (player.getX() >= 2686 && player.getY() >= 3746 && player.getX() <= 2749
        && player.getY() <= 3809) {
      location = "Fremennik Hunter";
    } else if (player.getX() >= 3286 && player.getY() >= 3273 && player.getX() <= 3313
        && player.getY() <= 3319) {
      location = "Al Kharid Mine";
    } else if (player.getX() >= 3310 && player.getY() >= 3197 && player.getX() <= 3402
        && player.getY() <= 3287) {
      location = "Duel Arena";
    } else if (player.getX() >= 3341 && player.getY() >= 3122 && player.getX() <= 3395
        && player.getY() <= 3181) {
      location = "Clan Wars";
    } else if (player.getX() >= 3293 && player.getY() >= 3105 && player.getX() <= 3326
        && player.getY() <= 3135) {
      location = "Shantay Pass";
    } else if (player.getX() >= 3190 && player.getY() >= 3228 && player.getX() <= 3196
        && player.getY() <= 3234) {
      location = "Lumbridge Farm";
    } else if (player.getX() >= 3188 && player.getY() >= 3188 && player.getX() <= 3254
        && player.getY() <= 3260) {
      location = "Lumbridge";
    } else if (player.getX() >= 3135 && player.getY() >= 3135 && player.getX() <= 3252
        && player.getY() <= 3213) {
      location = "Lumbridge Swamp";
    } else if (player.getX() >= 3253 && player.getY() >= 3135 && player.getX() <= 3340
        && player.getY() <= 3222) {
      location = "Al Kharid";
    } else if (player.getX() >= 3351 && player.getY() >= 3289 && player.getX() <= 3375
        && player.getY() <= 3325) {
      location = "Mage Training Arena";
    } else if (player.getX() >= 3091 && player.getY() >= 3143 && player.getX() <= 3125
        && player.getY() <= 3209) {
      location = "Wizards' Tower";
    } else if (player.getX() >= 2977 && player.getY() >= 3102 && player.getX() <= 3042
        && player.getY() <= 3190) {
      location = "Mudskipper Point";
    } else if (player.getX() >= 2911 && player.getY() >= 3192 && player.getX() <= 2997
        && player.getY() <= 3263) {
      location = "Rimmington";
    } else if (player.getX() >= 2900 && player.getY() >= 3266 && player.getX() <= 2944
        && player.getY() <= 3299) {
      location = "Crafting Guild";
    } else if (player.getX() >= 3004 && player.getY() >= 3191 && player.getX() <= 3072
        && player.getY() <= 3263) {
      location = "Port Sarim";
    } else if (player.getX() >= 3069 && player.getY() >= 3229 && player.getX() <= 3134
        && player.getY() <= 3299) {
      location = "Draynor Village";
    } else if (player.getX() >= 2933 && player.getY() >= 3307 && player.getX() <= 3066
        && player.getY() <= 3396) {
      location = "Falador";
    } else if (player.getX() >= 3012 && player.getY() >= 3280 && player.getX() <= 3069
        && player.getY() <= 3314) {
      location = "Falador Farm";
    } else if (player.getX() >= 2788 && player.getY() >= 3406 && player.getX() <= 2865
        && player.getY() <= 3470) {
      location = "Catherby";
    } else if (player.getX() >= 2831 && player.getY() >= 3533 && player.getX() <= 2879
        && player.getY() <= 3556) {
      location = "Warriors' Guild";
    } else if (player.getX() >= 3006 && player.getY() >= 3438 && player.getX() <= 3026
        && player.getY() <= 3455) {
      location = "Dwarven Mine";
    } else if (player.getX() >= 3070 && player.getY() >= 3404 && player.getX() <= 3105
        && player.getY() <= 3449) {
      location = "Barbarian Village";
    } else if (player.getX() >= 3135 && player.getY() >= 3442 && player.getX() <= 3149
        && player.getY() <= 3454) {
      location = "Cooks' Guild";
    } else if (player.getX() >= 3136 && player.getY() >= 3466 && player.getX() <= 3197
        && player.getY() <= 3518) {
      location = "Grand Exchange";
    } else if (player.getX() >= 3040 && player.getY() >= 3471 && player.getX() <= 3064
        && player.getY() <= 3510) {
      location = "Edgeville Monastery";
    } else if (player.getX() >= 3065 && player.getY() >= 3461 && player.getX() <= 3135
        && player.getY() <= 3520) {
      location = "Edgeville";
    } else if (player.getX() >= 3226 && player.getY() >= 3456 && player.getX() <= 3232
        && player.getY() <= 3462) {
      location = "Varrock Farm";
    } else if (player.getX() >= 3170 && player.getY() >= 3359 && player.getX() <= 3185
        && player.getY() <= 3380) {
      location = "Varrock South West Mine";
    } else if (player.getX() >= 3276 && player.getY() >= 3354 && player.getX() <= 3296
        && player.getY() <= 3371) {
      location = "Varrock South East Mine";
    } else if (player.getX() >= 3173 && player.getY() >= 3373 && player.getX() <= 3291
        && player.getY() <= 3508) {
      location = "Varrock";
    } else if (player.getX() >= 3401 && player.getY() >= 3527 && player.getX() <= 3454
        && player.getY() <= 3582) {
      location = "Slayer Tower";
    } else if (player.getX() >= 3466 && player.getY() >= 3459 && player.getX() <= 3521
        && player.getY() <= 3515) {
      location = "Canifis";
    } else if (player.getX() >= 3544 && player.getY() >= 3266 && player.getX() <= 3585
        && player.getY() <= 3317) {
      location = "Barrows";
    } else if (player.getX() >= 3423 && player.getY() >= 3202 && player.getX() <= 3469
        && player.getY() <= 3258) {
      location = "Abandoned Mine";
    } else if (player.getX() >= 2487 && player.getY() >= 3177 && player.getX() <= 2492
        && player.getY() <= 3182) {
      location = "Tree Gnome Village Farm";
    } else if (player.getX() >= 2485 && player.getY() >= 3132 && player.getX() <= 2560
        && player.getY() <= 3205) {
      location = "Tree Gnome Village";
    } else if (player.getX() >= 2561 && player.getY() >= 3136 && player.getX() <= 2620
        && player.getY() <= 3199) {
      location = "Fight Arena";
    } else if (player.getX() >= 2590 && player.getY() >= 3202 && player.getX() <= 2622
        && player.getY() <= 3219) {
      location = "Ardougne Monastery";
    } else if (player.getX() >= 2626 && player.getY() >= 3138 && player.getX() <= 2689
        && player.getY() <= 3192) {
      location = "Port Khazard";
    } else if (player.getX() >= 2635 && player.getY() >= 3359 && player.getX() <= 2640
        && player.getY() <= 3368) {
      location = "Ardougne Master Farmer";
    } else if (player.getX() >= 2615 && player.getY() >= 3340 && player.getX() <= 2678
        && player.getY() <= 3392) {
      location = "Ardougne Farm";
    } else if (player.getX() >= 2431 && player.getY() >= 3262 && player.getX() <= 2559
        && player.getY() <= 3336) {
      location = "West Ardougne";
    } else if (player.getX() >= 2560 && player.getY() >= 3254 && player.getX() <= 2688
        && player.getY() <= 3391) {
      location = "East Ardougne";
    } else if (player.getX() >= 2750 && player.getY() >= 2879 && player.getX() <= 2988
        && player.getY() <= 2942) {
      location = "Kharazi Jungle";
    } else if (player.getX() >= 2815 && player.getY() >= 2941 && player.getX() <= 2881
        && player.getY() <= 3008) {
      location = "Shilo Village";
    } else if (player.getX() >= 2815 && player.getY() >= 2941 && player.getX() <= 2881
        && player.getY() <= 3008) {
      location = "Shilo Village";
    } else if (player.getX() >= 2945 && player.getY() >= 3014 && player.getX() <= 3005
        && player.getY() <= 3070) {
      location = "Karamja Ship Yard";
    } else if (player.getX() >= 2752 && player.getY() >= 2961 && player.getX() <= 2779
        && player.getY() <= 2991) {
      location = "Cairn Isle";
    } else if (player.getX() >= 2770 && player.getY() >= 3050 && player.getX() <= 2820
        && player.getY() <= 3079) {
      location = "Tai Bwo Wannai";
    } else if (player.getX() >= 2818 && player.getY() >= 3136 && player.getX() <= 2964
        && player.getY() <= 3207) {
      location = "Musa Point";
    } else if (player.getX() >= 2639 && player.getY() >= 3210 && player.getX() <= 2659
        && player.getY() <= 3226) {
      location = "Tower of Life";
    } else if (player.getX() >= 2685 && player.getY() >= 3256 && player.getX() <= 2637
        && player.getY() <= 3204) {
      location = "Ardougne Necromancer";
    } else if (player.getX() >= 2697 && player.getY() >= 3134 && player.getX() <= 2817
        && player.getY() <= 3248) {
      location = "Brimhaven";
    } else if (player.getX() >= 2746 && player.getY() >= 2880 && player.getX() <= 2991
        && player.getY() <= 3137) {
      location = "Karamja";
    } else if (player.getX() >= 2915 && player.getY() >= 3473 && player.getX() <= 2935
        && player.getY() <= 3492) {
      location = "Druids' Circle";
    } else if (player.getX() >= 2927 && player.getY() >= 3508 && player.getX() <= 2945
        && player.getY() <= 3523) {
      location = "Chaos Temple";
    } else if (player.getX() >= 2882 && player.getY() >= 3503 && player.getX() <= 2903
        && player.getY() <= 3519) {
      location = "Heroes' Guild";
    } else if (player.getX() >= 2942 && player.getY() >= 3479 && player.getX() <= 2971
        && player.getY() <= 3519) {
      location = "Goblin Village";
    } else if (player.getX() >= 2933 && player.getY() >= 3435 && player.getX() <= 2939
        && player.getY() <= 3441) {
      location = "Taverley Farm";
    } else if (player.getX() >= 2874 && player.getY() >= 3407 && player.getX() <= 2942
        && player.getY() <= 3455) {
      location = "Taverley";
    } else if (player.getX() >= 3080 && player.getY() >= 3328 && player.getX() <= 3130
        && player.getY() <= 3389) {
      location = "Draynor Manor";
    } else if (player.getX() >= 2471 && player.getY() >= 2949 && player.getX() <= 2654
        && player.getY() <= 3012) {
      location = "Feldip Hills";
    } else if (player.getX() >= 2446 && player.getY() >= 3013 && player.getX() <= 2589
        && player.getY() <= 3069) {
      location = "Gu'Tunoth";
    } else if (player.getX() >= 2440 && player.getY() >= 3212 && player.getX() <= 2484
        && player.getY() <= 3260) {
      location = "Red Salamanders";
    } else if (player.getX() >= 2305 && player.getY() >= 3661 && player.getX() <= 2364
        && player.getY() <= 3705) {
      location = "Piscatoris Fishing Colony";
    } else if (player.getX() >= 2364 && player.getY() >= 3573 && player.getX() <= 2394
        && player.getY() <= 3620) {
      location = "Piscatoris Falconer";
    } else if (player.getX() >= 2302 && player.getY() >= 3458 && player.getX() <= 2367
        && player.getY() <= 3513) {
      location = "Eagles' Peak";
    } else if (player.getX() >= 2259 && player.getY() >= 3460 && player.getX() <= 2405
        && player.getY() <= 3665) {
      location = "Piscatoris";
    } else if (player.getX() >= 2717 && player.getY() >= 3350 && player.getX() <= 2739
        && player.getY() <= 3387) {
      location = "Legends' Guild";
    } else if (player.getX() >= 2840 && player.getY() >= 3573 && player.getX() <= 2886
        && player.getY() <= 3610) {
      location = "Death Plateau";
    } else if (player.getX() >= 2906 && player.getY() >= 3733 && player.getX() <= 2933
        && player.getY() <= 3758) {
      location = "God Wars";
    } else if (player.getX() >= 3182 && player.getY() >= 3351 && player.getX() <= 3199
        && player.getY() <= 3365) {
      location = "Champions' Guild";
    } else if (player.getX() >= 3460 && player.getY() >= 3257 && player.getX() <= 3525
        && player.getY() <= 3313) {
      location = "Mort'ton";
    } else if (player.getX() >= 3473 && player.getY() >= 3161 && player.getX() <= 3538
        && player.getY() <= 3245) {
      location = "Burgh de Rott";
    } else if (player.getX() >= 3530 && player.getY() >= 3426 && player.getX() <= 3580
        && player.getY() <= 3453) {
      location = "Swamp Lizards";
    } else if (player.getX() >= 3650 && player.getY() >= 3509 && player.getX() <= 3669
        && player.getY() <= 3528) {
      location = "Ectofuntus";
    } else if (player.getX() >= 3582 && player.getY() >= 3513 && player.getX() <= 3637
        && player.getY() <= 3546) {
      location = "Phasmatys Farm";
    } else if (player.getX() >= 3648 && player.getY() >= 3452 && player.getX() <= 3712
        && player.getY() <= 3547) {
      location = "Port Phasmatys";
    } else if (player.getX() >= 3140 && player.getY() >= 2956 && player.getX() <= 3198
        && player.getY() <= 3000) {
      location = "Bandit Camp";
    } else if (player.getX() >= 3143 && player.getY() >= 3016 && player.getX() <= 3196
        && player.getY() <= 3060) {
      location = "Bedabin Camp";
    } else if (player.getX() >= 3390 && player.getY() >= 3073 && player.getX() <= 3419
        && player.getY() <= 3131) {
      location = "Desert Hunter";
    } else if (player.getX() >= 3768 && player.getY() >= 3514 && player.getX() <= 3846
        && player.getY() <= 3581) {
      location = "Dragontooth Island";
    } else if (player.getX() >= 3628 && player.getY() >= 3690 && player.getX() <= 3858
        && player.getY() <= 3913) {
      location = "Fossil Island";
    } else if (player.getX() >= 2753 && player.getY() >= 3648 && player.getX() <= 2863
        && player.getY() <= 3706) {
      location = "Troll Stronghold";
    } else if (player.getX() >= 2864 && player.getY() >= 3648 && player.getX() <= 2921
        && player.getY() <= 3706) {
      location = "Trollheim";
    } else if (player.getController().inWilderness()) {
      location = "Wilderness";
    } else if (player.getController().inPvPWorld()) {
      location = "PvP World";
    } else if (player.getX() >= 1137 && player.getY() >= 2491 && player.getX() <= 3905
        && player.getY() <= 4163) {
      location = "Mainland";
    } else if (player.getX() >= 2368 && player.getY() >= 5055 && player.getX() <= 2429
        && player.getY() <= 5119) {
      location = "Fight Cave";
    } else if (player.getX() >= 2373 && player.getY() >= 5127 && player.getX() <= 2422
        && player.getY() <= 5175) {
      location = "Fight Pit";
    } else if (player.getX() >= 2368 && player.getY() >= 5058 && player.getX() <= 2557
        && player.getY() <= 5183) {
      location = "TzHaar";
    } else if (player.getX() >= 1721 && player.getY() >= 5122 && player.getX() <= 1799
        && player.getY() <= 5254) {
      location = "Mole Lair";
    } else if (player.getX() >= 1602 && player.getY() >= 5311 && player.getX() <= 1801
        && player.getY() <= 5382) {
      location = "Ancient Cavern";
    } else if (player.getX() >= 2245 && player.getY() >= 5315 && player.getX() <= 2298
        && player.getY() <= 5374) {
      location = "The Inferno";
    } else if (player.getX() >= 3006 && player.getY() >= 5565 && player.getX() <= 3074
        && player.getY() <= 5636) {
      location = "Ourania Cave";
    } else if (player.getX() >= 2823 && player.getY() >= 9541 && player.getX() <= 2870
        && player.getY() <= 9601) {
      location = "Karamja Dungeon";
    } else if (player.getX() >= 2825 && player.getY() >= 9602 && player.getX() <= 2868
        && player.getY() <= 9662) {
      location = "Crandor Dungeon";
    } else if (player.getX() >= 2981 && player.getY() >= 9535 && player.getX() <= 3084
        && player.getY() <= 9606) {
      location = "Asgarnian Ice Dungeon";
    } else if (player.getX() >= 2528 && player.getY() >= 9860 && player.getX() <= 2614
        && player.getY() <= 9923) {
      location = "Waterfall Dungeon";
    } else if (player.getX() >= 2233 && player.getY() >= 9978 && player.getX() <= 2311
        && player.getY() <= 10052) {
      location = "Kraken Cove";
    } else if (player.getX() >= 2883 && player.getY() >= 9880 && player.getX() <= 2945
        && player.getY() <= 9920) {
      location = "Heroes' Guild Dungeon";
    } else if (player.getX() >= 2812 && player.getY() >= 9896 && player.getX() <= 2897
        && player.getY() <= 9980) {
      location = "Ice Queen's Dungeon";
    } else if (player.getX() >= 2689 && player.getY() >= 9951 && player.getX() <= 2813
        && player.getY() <= 10045) {
      location = "Fremennik Slayer Dungeon";
    } else if (player.getX() >= 2431 && player.getY() >= 10109 && player.getX() <= 2561
        && player.getY() <= 10179) {
      location = "Waterbirth Dungeon";
    } else if (player.getX() >= 2812 && player.getY() >= 9670 && player.getX() <= 2970
        && player.getY() <= 9855) {
      location = "Taverley Dungeon";
    } else if (player.getX() >= 3200 && player.getY() >= 9344 && player.getX() <= 3330
        && player.getY() <= 9407) {
      location = "Smoke Dungeon";
    } else if (player.getX() >= 3523 && player.getY() >= 9665 && player.getX() <= 3580
        && player.getY() <= 9727) {
      location = "Barrows Dungeon";
    } else if (player.getX() >= 2625 && player.getY() >= 9409 && player.getX() <= 2753
        && player.getY() <= 9599) {
      location = "Brimhaven Dungeon";
    } else if (player.getX() >= 2495 && player.getY() >= 9981 && player.getX() <= 2546
        && player.getY() <= 10045) {
      location = "Lighthouse Dungeon";
    } else if (player.getX() >= 1588 && player.getY() >= 9973 && player.getX() <= 1749
        && player.getY() <= 10117) {
      location = "Catacombs of Kourend";
    } else if (player.getX() >= 1401 && player.getY() >= 10047 && player.getX() <= 1475
        && player.getY() <= 10111) {
      location = "Chasm of Fire";
    } else if (player.getX() >= 3579 && player.getY() >= 10174 && player.getX() <= 3655
        && player.getY() <= 10310) {
      location = "Fossil Island Wyverns";
    } else if (player.getX() >= 3254 && player.getY() >= 5120 && player.getX() <= 3374
        && player.getY() <= 5482) {
      location = "Chambers of Xeric";
    } else if (player.getX() >= 3010 && player.getY() >= 9697 && player.getX() <= 3057
        && player.getY() <= 9756) {
      location = "Mining Guild";
    } else if (player.getX() >= 2971 && player.getY() >= 9757 && player.getX() <= 3061
        && player.getY() <= 9854) {
      location = "Dwarvern Mine Dungeon";
    } else if (player.getX() >= 2881 && player.getY() >= 4802 && player.getX() <= 2940
        && player.getY() <= 4862) {
      location = "Rune Essence Mine";
    }
    return location;
  }
}
