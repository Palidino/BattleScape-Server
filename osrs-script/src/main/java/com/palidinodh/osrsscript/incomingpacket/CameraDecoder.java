package com.palidinodh.osrsscript.incomingpacket;

import com.palidinodh.io.Stream;
import com.palidinodh.osrscore.io.incomingpacket.IncomingPacketDecoder;
import com.palidinodh.osrscore.model.player.Player;

class CameraDecoder extends IncomingPacketDecoder {
  @Override
  public boolean execute(Player player, Stream stream) {
    return true;
  }
}
