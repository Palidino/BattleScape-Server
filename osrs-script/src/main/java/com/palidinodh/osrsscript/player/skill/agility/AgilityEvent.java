package com.palidinodh.osrsscript.player.skill.agility;

import com.palidinodh.osrscore.model.player.Player;
import lombok.Getter;
import lombok.Setter;

public abstract class AgilityEvent {
  @Getter
  @Setter
  private int delay;

  abstract boolean complete(Player player);
}
