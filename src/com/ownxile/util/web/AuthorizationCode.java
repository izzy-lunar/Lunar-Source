package com.ownxile.util.web;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ownxile.GameServer;
import com.ownxile.core.World;
import com.ownxile.rs2.items.ItemAssistant;
import com.ownxile.rs2.player.Client;
import com.ownxile.util.Correction;
import com.ownxile.util.file.FileLog;

/**
 * @author Robbie
 * @description A class to handle authorization code claims
 */
public class AuthorizationCode {

	/*
	 * TODO Throttle control Variable code types eg: social media, donor, etc
	 */

	/**
	 * This checks whether the @AuthorizationCode entered is valid, if valid it
	 * will delete the code and return the data included in that part of the
	 * 
	 * @Database table.
	 */
	private static ResultSet isAuth(String auth) throws Exception {
		if (World.getSettings().isDatabase()) {
			PreparedStatement ps = GameServer.getDatabase().getConnection()
					.prepareStatement("SELECT * FROM auths WHERE code = ?");
			ps.setString(1, auth.toLowerCase());
			ResultSet results = ps.executeQuery();
			if (results.next()) {
				ps = GameServer.getDatabase().getConnection()
						.prepareStatement("DELETE FROM auths WHERE code = ?");
				ps.setString(1, auth.toLowerCase());
				ps.executeUpdate();
				ps.close();
				return results;
			}
			ps.close();
		}
		return null;
	}

	/**
	 * This handles valid @AuthorizationCode by distributing the correct @GameItem
	 * values to the @Client. The claim is then logged in a @FileLog.
	 */
	public static void handleReward(Client player, String auth)
			throws SQLException {
		ResultSet results = null;
		try {
			results = isAuth(auth);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (results != null) {
			int item = results.getInt(2);
			int amount = results.getInt(3);
			player.addItem(item, amount);
			World.sendMessage("@grd@"
					+ formulateAnnouncementString(player.playerName, item,
							amount));
			FileLog.writeLog("auths", player.playerName + " " + amount + "x "
					+ ItemAssistant.getItemName(item));
		}
	}

	/**
	 * Constructs a string consisting of the details of the @AuthorizationCode
	 * claim to be sent to the @World.
	 */
	private static String formulateAnnouncementString(String name, int item,
			int amount) {
		String message = null;
		if (amount > 1) {
			if (item == 995) {
				String cashS = Correction.getAmountString(amount);
				message = name + " has redeemed " + cashS + " coins.";
			} else
				message = name + " has redeemed " + amount + "x "
						+ ItemAssistant.getItemName(item) + ".";
		} else {
			message = name + " has redeemed an "
					+ ItemAssistant.getItemName(item) + ".";
		}
		return message;
	}
}
