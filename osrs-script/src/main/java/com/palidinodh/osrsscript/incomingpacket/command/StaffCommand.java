package com.palidinodh.osrsscript.incomingpacket.command;

import java.util.ArrayList;
import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.model.dialogue.Scroll;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.setting.SqlUserRank;
import com.palidinodh.util.PString;
import lombok.var;

public class StaffCommand implements CommandHandler {

  @Override
  public String getExample() {
    return "- Shows the current staff online.";
  }

  @Override
  public void execute(Player player, String message) {
    var lines = new ArrayList<String>();
    for (var staff : player.getWorld().getStaffPlayers()) {
      var rank = "";
      if (player.getRights() == Player.RIGHTS_ADMIN) {
        rank = "Administrator";
      } else if (staff.isUsergroup(SqlUserRank.COMMUNITY_MANAGER)) {
        rank = "Community Manager";
      } else if (staff.isUsergroup(SqlUserRank.SENIOR_MODERATOR)) {
        rank = "Head Moderator";
      } else if (staff.isUsergroup(SqlUserRank.MODERATOR)) {
        rank = "Moderator";
      } else if (staff.isUsergroup(SqlUserRank.TRIAL_MODERATOR)) {
        rank = "Junior Moderator";
      }
      lines.add(staff.getMessaging().getIconImage() + staff.getUsername() + " - " + rank);
    }
    Scroll.open(player, "Staff Members Online", PString.toStringArray(lines));
  }
}
