package com.palidinodh.osrsscript.world.event.pvptournament.state;

public interface State {
  String getMessage();

  int getTime();

  void execute();
}
