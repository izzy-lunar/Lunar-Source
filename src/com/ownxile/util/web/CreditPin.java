package com.ownxile.util.web;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ownxile.GameServer;
import com.ownxile.core.World;
import com.ownxile.rs2.player.Client;
import com.ownxile.util.file.FileLog;

public class CreditPin {

	/**
	 * This checks whether the @CreditPin entered is valid, if valid it will
	 * delete the code and return the data included in that part of the
	 * 
	 * @Database table.
	 */
	private static ResultSet isPin(String pin) throws Exception {
		if (World.getSettings().isDatabase()) {
			PreparedStatement ps = GameServer.getDatabase().getConnection()
					.prepareStatement("SELECT * FROM pins WHERE code = ?");
			ps.setString(1, pin.toLowerCase());
			ResultSet results = ps.executeQuery();
			if (results.next()) {
				ps = GameServer.getDatabase().getConnection()
						.prepareStatement("DELETE FROM pins WHERE code = ?");
				ps.setString(1, pin.toLowerCase());
				ps.executeUpdate();
				ps.close();
				return results;
			}
			ps.close();
		}
		return null;
	}

	public static void requestCredits(Client player, String pin)
			throws SQLException {
		ResultSet results = null;
		try {
			results = isPin(pin);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (results != null) {
			double credits = results.getDouble(2);
			player.donorPoints += credits;
			player.sendMessage("You redeem the pin worth $" + credits + ".");
			player.sendMessage("You now have $" + player.donorPoints + ".");
			FileLog.writeLog("credits", player.playerName + "_$" + credits);
		}

	}
}
