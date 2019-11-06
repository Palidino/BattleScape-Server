package com.palidinodh.osrsscript.player.plugin.slayer.dialogue;

import java.util.ArrayList;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.model.dialogue.SelectionDialogue;
import com.palidinodh.osrscore.model.item.Item;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrsscript.player.plugin.slayer.SlayerPlugin;
import lombok.var;

public class SlayerRingDialogue extends SelectionDialogue {
  public SlayerRingDialogue(Player player, SlayerPlugin plugin) {
    addOption("Option", (childId, slot) -> {
      if (plugin.getTask().isComplete()) {
        player.getGameEncoder().sendMessage("You need a task to do this.");
        return;
      }
      if (plugin.getTask().getSlayerTask().getTeleports() == null
          || plugin.getTask().getSlayerTask().getTeleports().isEmpty()) {
        player.getGameEncoder().sendMessage("There are no teleports associated with this task.");
        return;
      }
      if (slot >= plugin.getTask().getSlayerTask().getTeleports().size()) {
        return;
      }
      if (!player.getController().canTeleport(30, true)) {
        return;
      }
      var ringSlot = player.getAttributeInt("slayer_ring_slot");
      Item ringItem = null;
      if (ringSlot >= 65536) {
        ringItem = player.getEquipment().getItem(ringSlot - 65536);
      } else {
        ringItem = player.getInventory().getItem(ringSlot);
      }
      if (ringItem == null) {
        player.getGameEncoder().sendMessage("Unable to locate your ring.");
        return;
      }
      var ringIds = new int[] {ItemId.SLAYER_RING_8, ItemId.SLAYER_RING_7, ItemId.SLAYER_RING_6,
          ItemId.SLAYER_RING_5, ItemId.SLAYER_RING_4, ItemId.SLAYER_RING_3, ItemId.SLAYER_RING_2,
          ItemId.SLAYER_RING_1, -1};
      var newRingId = -2;
      for (var i = 0; i < ringIds.length; i++) {
        if (ringItem.getId() == ringIds[i]) {
          newRingId = ringIds[i + 1];
          break;
        }
      }
      if (newRingId != -2) {
        if (ringSlot >= 65536) {
          player.getEquipment().setItem(ringSlot - 65536, new Item(newRingId, ringItem));
        } else {
          player.getInventory().setItem(ringSlot, new Item(newRingId, ringItem));
        }
      }
      player.getMagic()
          .standardTeleport(plugin.getTask().getSlayerTask().getTeleports().get(slot).getTile());
      player.getController().stopWithTeleport();
      player.clearHits();
    });
    open(player);
  }

  @Override
  public void sendWidgetTextHook(Player player) {
    var plugin = player.getPlugin(SlayerPlugin.class);
    var locations = new ArrayList<String>();
    for (var teleport : plugin.getTask().getSlayerTask().getTeleports()) {
      locations.add(teleport.getName());
    }
    if (plugin.getTask().getSlayerTask().getTeleports().size() == 1) {
      locations.add("Nevermind");
    }
    sendWidgetText(player, null, locations.toArray(new String[locations.size()]));
  }
}
