package com.palidinodh.osrsscript.packetdecoder;

import com.palidinodh.osrscore.io.PacketDecoder;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.io.Stream;

public class CloseWidgetDecoder extends PacketDecoder {
  public CloseWidgetDecoder() {
    super(20);
  }

  @Override
  public void execute(Player player, int index, int size, Stream stream) {
    player.clearIdleTime();
    player.getWidgetManager().removeInteractiveWidgets();
  }
}
