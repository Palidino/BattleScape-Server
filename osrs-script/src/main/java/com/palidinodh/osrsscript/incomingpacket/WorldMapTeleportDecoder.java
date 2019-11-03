package com.palidinodh.osrsscript.incomingpacket;

import com.palidinodh.io.Stream;
import com.palidinodh.osrscore.io.incomingpacket.InStreamKey;
import com.palidinodh.osrscore.io.incomingpacket.IncomingPacketDecoder;
import com.palidinodh.osrscore.model.player.Player;
import lombok.var;

public class WorldMapTeleportDecoder extends IncomingPacketDecoder {
  @Override
  public boolean execute(Player player, Stream stream) {
    var tileHash = getInt(InStreamKey.TILE_HASH);
    player.clearIdleTime();
    if (player.getRights() != Player.RIGHTS_ADMIN) {
      return false;
    }
    var x = tileHash >> 14 & 16383;
    var y = tileHash & 16383;
    var z = tileHash >> 28 & 3;
    player.getMovement().teleport(x, y, z);
    return true;
  }
}
