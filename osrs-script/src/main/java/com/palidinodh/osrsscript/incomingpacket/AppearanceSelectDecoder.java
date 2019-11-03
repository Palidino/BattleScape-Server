package com.palidinodh.osrsscript.incomingpacket;

import com.palidinodh.io.Stream;
import com.palidinodh.osrscore.io.incomingpacket.IncomingPacketDecoder;
import com.palidinodh.osrscore.model.player.Appearance;
import com.palidinodh.osrscore.model.player.Appearance.Style;
import com.palidinodh.osrscore.model.player.IdentityKit;
import com.palidinodh.osrscore.model.player.Player;
import lombok.var;

public class AppearanceSelectDecoder extends IncomingPacketDecoder {
  @Override
  public boolean execute(Player player, Stream stream) {
    player.clearIdleTime();
    var gender = stream.readUnsignedByte();
    if (gender != Appearance.MALE && gender != Appearance.FEMALE) {
      return false;
    }
    var styles = new int[player.getAppearance().getStyles().length];
    for (var i = 0; i < styles.length; i++) {
      var id = stream.readUnsignedByte();
      if (gender == Appearance.FEMALE && Style.get(i) == Style.BEARD) {
        styles[i] = -1;
        continue;
      }
      if (!IdentityKit.isValidStyle(id, i, gender)) {
        return false;
      }
      styles[i] = id;
    }
    var colors = new int[player.getAppearance().getColors().length];
    for (var i = 0; i < colors.length; i++) {
      var id = stream.readUnsignedByte();
      colors[i] = id < IdentityKit.COLORS[i].length ? id : 0;
    }
    player.getAppearance().setGender(gender);
    player.getAppearance().setStyles(styles);
    player.getAppearance().setColors(colors);
    player.getAppearance().setUpdate(true);
    player.getWidgetManager().removeInteractiveOverlay();
    return true;
  }
}
