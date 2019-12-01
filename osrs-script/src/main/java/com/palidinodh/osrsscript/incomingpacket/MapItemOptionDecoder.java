package com.palidinodh.osrsscript.incomingpacket;

import com.palidinodh.io.Stream;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.incomingpacket.InStreamKey;
import com.palidinodh.osrscore.io.incomingpacket.IncomingPacketDecoder;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.item.ItemDef;
import com.palidinodh.osrscore.model.map.route.Route;
import com.palidinodh.osrscore.model.player.PCombat;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.util.RequestManager;
import com.palidinodh.rs.setting.Settings;
import com.palidinodh.util.PLogger;
import lombok.var;

class MapItemOptionDecoder extends IncomingPacketDecoder {
  @Override
  public boolean execute(Player player, Stream stream) {
    var option = getInt(InStreamKey.PACKET_OPTION);
    var itemId = getInt(InStreamKey.ITEM_ID);
    var tileX = getInt(InStreamKey.TILE_X);
    var tileY = getInt(InStreamKey.TILE_Y);
    var ctrlRun = getInt(InStreamKey.CTRL_RUN);
    player.clearIdleTime();
    var message = "[MapItemOption(" + option + ")] itemId=" + itemId + "/" + ItemId.valueOf(itemId)
        + "; tileX=" + tileX + "; tileY=" + tileY + "; ctrlRun=" + ctrlRun;
    if (Settings.getInstance().isLocal()) {
      PLogger.println(message);
    }
    if (player.getOptions().getPrintPackets()) {
      player.getGameEncoder().sendMessage(message);
    }
    RequestManager.addUserPacketLog(player, message);
    if (player.isLocked()) {
      return false;
    }
    if (player.getMovement().isViewing()) {
      return false;
    }
    player.getMovement().fullRoute(tileX, tileY, ctrlRun);
    return true;
  }

  @Override
  public boolean complete(Player player) {
    var option = getInt(InStreamKey.PACKET_OPTION);
    var itemId = getInt(InStreamKey.ITEM_ID);
    var tileX = getInt(InStreamKey.TILE_X);
    var tileY = getInt(InStreamKey.TILE_Y);
    var reach = 0;
    var t = new Tile(tileX, tileY, player.getHeight());
    if (!player.getMovement().isRouting() && player.withinDistance(t, 1)
        && Route.canReachTile(player, t, true)) {
      reach = 1;
    }
    if (player.isLocked()) {
      return false;
    }
    if (!player.withinDistance(t, reach)) {
      return false;
    }
    if (reach == 1 && !player.withinDistance(t, 0)) {
      player.setAnimation(827);
    }
    player.getMovement().clear();
    if (option == 2) {
      var result = player.getWorld().pickupMapItem(player, itemId, tileX, tileY);
      if (result != null && result.partialSuccess()
          && (itemId == ItemId.BLOODY_KEY || itemId == ItemId.BLOODIER_KEY)
          && (player.getController().inWilderness() || player.getController().inPvPWorld())) {
        player.getCombat().setPKSkullDelay(PCombat.SKULL_DELAY);
        player.getMovement().setEnergy(0);
        player.getGameEncoder()
            .sendMessage("<col=ff0000>Carrying the key prevents you from teleporting.");
        if (itemId == ItemId.BLOODIER_KEY) {
          player.getWorld()
              .sendMessage("<col=ff0000>A " + ItemDef.getName(itemId) + " has been picked up by "
                  + player.getUsername() + " at level " + player.getWildernessLevel()
                  + " wilderness!");
        }
      }
    }
    return true;
  }
}
