package com.palidinodh.osrsscript.world.pvptournament.prize;

import com.palidinodh.util.PNumber;

public class OsrsPrize implements Prize {
  private int value;

  public OsrsPrize(int value) {
    this.value = value;
  }

  @Override
  public String getMessage() {
    return "Prizes include " + PNumber.abbreviateNumber(value) + " OSRS coins.";
  }
}
