package com.palidinodh.osrsscript.packetdecoder;

import com.palidinodh.io.Stream;
import com.palidinodh.osrscore.io.PacketDecoder;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.WidgetManager;
import lombok.var;

public class WindowSizeDecoder extends PacketDecoder {
  public WindowSizeDecoder() {
    super(35);
  }

  @Override
  @SuppressWarnings("unused")
  public void execute(Player player, int index, int size, Stream stream) {
    var viewportType = WidgetManager.ViewportType.get(stream.getUByte());
    var width = stream.getUShort();
    var height = stream.getUShort();
    var currentViewportType = player.getWidgetManager().getViewportType();
    if (viewportType == null) {
      return;
    }
    if (viewportType == currentViewportType) {
      return;
    }
    player.clearIdleTime();
    player.getWidgetManager().setViewportType(viewportType);
    player.getWidgetManager().sendGameViewport();
  }
}
