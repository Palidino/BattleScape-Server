package com.palidinodh.osrsscript.incomingpacket;

import com.palidinodh.io.Stream;
import com.palidinodh.osrscore.io.ValueEnteredEvent;
import com.palidinodh.osrscore.io.incomingpacket.InStreamKey;
import com.palidinodh.osrscore.io.incomingpacket.IncomingPacketDecoder;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.setting.Settings;
import com.palidinodh.util.PLogger;
import lombok.var;

public class TextInputDecoder {
  public class NumberInputDecoder extends IncomingPacketDecoder {
    @Override
    public boolean execute(Player player, Stream stream) {
      var value = getInt(InStreamKey.NUMBER_INPUT);
      player.clearIdleTime();
      var message = "[NumberInput] value=" + value;
      if (Settings.getInstance().isLocal()) {
        PLogger.println(message);
      }
      if (player.getOptions().getPrintPackets()) {
        player.getGameEncoder().sendMessage(message);
      }
      var event = (ValueEnteredEvent.IntegerEvent) player.removeAttribute("entered_integer_event");
      if (event == null) {
        return false;
      }
      if (player.isLocked()) {
        event.close();
        return false;
      }
      event.execute(value);
      event.close();
      return true;
    }
  }

  public class StringInputDecoder extends IncomingPacketDecoder {
    @Override
    public boolean execute(Player player, Stream stream) {
      var value = getString(InStreamKey.STRING_INPUT);
      player.clearIdleTime();
      var message = "[ValueEnteredString] value=" + value;
      if (Settings.getInstance().isLocal()) {
        PLogger.println(message);
      }
      if (player.getOptions().getPrintPackets()) {
        player.getGameEncoder().sendMessage(message);
      }
      var event = (ValueEnteredEvent.StringEvent) player.removeAttribute("entered_string_event");
      if (event == null) {
        return false;
      }
      if (player.isLocked()) {
        event.close();
        return false;
      }
      event.execute(value);
      event.close();
      return true;
    }
  }

  public class ItemInputDecoder extends IncomingPacketDecoder {
    @Override
    public boolean execute(Player player, Stream stream) {
      var itemId = getInt(InStreamKey.ITEM_ID);
      player.clearIdleTime();
      var message = "[ItemEntered] itemId=" + itemId;
      if (Settings.getInstance().isLocal()) {
        PLogger.println(message);
      }
      if (player.getOptions().getPrintPackets()) {
        player.getGameEncoder().sendMessage(message);
      }
      var event = (ValueEnteredEvent.IntegerEvent) player.removeAttribute("entered_integer_event");
      if (event == null) {
        return false;
      }
      if (player.isLocked()) {
        event.close();
        return false;
      }
      event.execute(itemId);
      event.close();
      return true;
    }
  }
}
