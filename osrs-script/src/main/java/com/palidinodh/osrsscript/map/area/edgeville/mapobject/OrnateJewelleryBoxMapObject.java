package com.palidinodh.osrsscript.map.area.edgeville.mapobject;

import com.palidinodh.osrscore.io.cache.WidgetSetting;
import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.io.cache.id.ScriptId;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.io.incomingpacket.MapObjectHandler;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;

@ReferenceId(ObjectId.ORNATE_JEWELLERY_BOX)
class OrnateJewelleryBoxMapObject implements MapObjectHandler {
  @Override
  public void execute(Player player, int option, MapObject mapObject) {
    player.getWidgetManager().sendInteractiveOverlay(WidgetId.JEWELLERY_BOX);
    player.getGameEncoder().sendScript(ScriptId.JEWELLERY_BOX, 15, "Ornate Jewellery Box", 3);
    player.getGameEncoder().sendWidgetSettings(WidgetId.JEWELLERY_BOX, 0, 0, 24,
        WidgetSetting.OPTION_0);
  }
}
