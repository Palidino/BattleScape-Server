package com.palidinodh.osrsscript.map.area.edgeville;

import com.palidinodh.osrscore.model.map.Area;
import com.palidinodh.osrsscript.world.event.pvptournament.PvpTournament;
import com.palidinodh.rs.ReferenceId;
import lombok.var;

@ReferenceId({ 12342, 12441, 12442 })
public class EdgevilleArea extends Area {
  @Override
  public void tickPlayer() {
    var player = getPlayer();
    var tournament = player.getWorld().getWorldEvent(PvpTournament.class);
    tournament.sendWidgetText(player);
  }
}
