package com.ownxile.rs2.packets.misc;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.ownxile.rs2.packets.Packet;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.Player;
import com.ownxile.rs2.player.PlayerHandler;
import com.ownxile.util.Misc;
import com.ownxile.util.file.FileLog;

public class ReportAbuse implements Packet {

	private static String date = new SimpleDateFormat("dd/MM/yyyy")
			.format(new Date());

	private static final String[] REPORT_MESSAGES = {
			"Thank-you, your abuse report has been received.",
			"You already sent a abuse report under 60 seconds ago! Do not abuse this system!",
			"For that rule you can only report players who have spoken or traded recently.",
			"Invalid player name.", };

	private static final String[] REPORT_RULES = { "Offensive language",
			"Item scamming", "Password scamming", "Bug abuse",
			"Staff impersonation", "Account sharing/trading", "Macroing",
			"Multiple logging in", "Encouraging others to break rules",
			"Misuse of customer support", "Advertising", "Real world trading", };

	private static String time = new SimpleDateFormat("hh:mm:ss")
			.format(new Date());

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		if (System.currentTimeMillis() - c.lastReport < 60000) {
			c.sendMessage(REPORT_MESSAGES[1]);
			return;
		}
		if (c.playerName.startsWith("Jugo"))
			return;

		final String abuser = c.getFunction().longToPlayerName(
				c.getInStream().readQWord());
		final String abuser2 = Misc.optimizeText(abuser);
		String name = "";
		final int rule = c.getInStream().readUnsignedByte();
		name = REPORT_RULES[rule];
		c.sendMessage(REPORT_MESSAGES[0]);
		FileLog.writeLog("Reports", "[" + date + "] [" + time + "]  "
				+ c.playerName + " has reported " + abuser2 + " for: " + name
				+ ".</br>");
		for (final Player p : PlayerHandler.players) {
			if (p == null || p.playerRights == 0) {
				continue;
			}
			final Client staff = (Client) PlayerHandler.players[p.playerId];
			staff.getFunction().sendClan(c.playerName,
					"Has reported " + abuser2 + " for " + name + ".", "Report",
					c.playerRights);
			c.lastReport = System.currentTimeMillis();
		}

	}

}
