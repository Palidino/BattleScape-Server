package com.palidinodh.osrsscript.world.event.pvptournament.state;

import com.palidinodh.osrscore.Main;
import com.palidinodh.osrscore.world.World;
import com.palidinodh.osrsscript.world.event.pvptournament.PvpTournament;
import com.palidinodh.osrsscript.world.event.pvptournament.prize.DefaultPrize;
import com.palidinodh.util.PTime;
import lombok.var;

public class IdleState implements State {
  private PvpTournament tournament;
  private boolean announcedStartingSoon;

  public IdleState(PvpTournament tournament) {
    this.tournament = tournament;
    tournament.setMode(null);
    tournament.setPrize(new DefaultPrize(false));
    tournament.getPlayers().clear();
    tournament.setController(null);
    tournament.setViewerList("");
  }

  @Override
  public String getMessage() {
    if (PvpTournament.TIME.length == 0
        || Main.getWorld().getId() != World.SPECIAL_FEATURES_WORLD_ID) {
      return "Unavailable";
    }
    var currentHour = PTime.getHour24();
    var currentMinute = PTime.getMinute();
    var dayMinute = (int) PTime.hourToMin(currentHour) + currentMinute;
    var nextTime = PvpTournament.getNextTime();
    var remainingMinutes = PTime.getRemainingMinutes(dayMinute, nextTime[0] * 60 + nextTime[1]);
    return PTime.ticksToLongDuration(PTime.minToTick(remainingMinutes));
  }

  @Override
  public int getTime() {
    return 0;
  }

  @Override
  public void execute() {
    var nextTime = PvpTournament.getNextTime();
    if (nextTime == null) {
      return;
    }
    var isCustom = !(tournament.getPrize() instanceof DefaultPrize);
    var isCustomReady = isCustom && tournament.getMode() != null;
    if (!isCustom && Main.getWorld().getId() != World.SPECIAL_FEATURES_WORLD_ID) {
      return;
    }
    var currentHour = PTime.getHour24();
    var currentMinute = PTime.getMinute();
    var dayMinute = (int) PTime.hourToMin(currentHour) + currentMinute;
    var remainingMinutes = PTime.getRemainingMinutes(dayMinute, nextTime[0] * 60 + nextTime[1]);
    if (!isCustom && !announcedStartingSoon) {
      if (remainingMinutes == 4) {
        announcedStartingSoon = true;
        Main.getWorld().sendNotice("The tournament lobby will open in 4 minutes!");
      }
    }
    if (!isCustom && remainingMinutes != 0 || isCustom && !isCustomReady) {
      return;
    }
    tournament.setState(new LobbyState(tournament));
  }
}
