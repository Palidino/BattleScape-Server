package com.palidinodh.osrsscript.incomingpacket;

import com.palidinodh.io.Stream;
import com.palidinodh.osrscore.io.incomingpacket.InStreamKey;
import com.palidinodh.osrscore.io.incomingpacket.IncomingPacketDecoder;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.WidgetManager;
import lombok.var;

class WindowSizeDecoder extends IncomingPacketDecoder {
  @Override
  @SuppressWarnings("unused")
  public boolean execute(Player player, Stream stream) {
    var viewportType = WidgetManager.ViewportType.get(getInt(InStreamKey.VIEWPORT));
    var width = getInt(InStreamKey.WIDTH);
    var height = getInt(InStreamKey.HEIGHT);
    player.clearIdleTime();
    var currentViewportType = player.getWidgetManager().getViewportType();
    if (viewportType == null) {
      return false;
    }
    if (viewportType == currentViewportType) {
      return false;
    }
    player.getWidgetManager().setViewportType(viewportType);
    player.getWidgetManager().sendGameViewport();
    return true;
  }
}
