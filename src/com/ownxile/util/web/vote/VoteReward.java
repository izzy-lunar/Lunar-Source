package com.ownxile.util.web.vote;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.ownxile.GameServer;
import com.ownxile.core.World;
import com.ownxile.rs2.player.Client;
import com.ownxile.util.time.DateAndTime;

public class VoteReward {

	public static void handleReward(Client player) {
		if (!VoteCache.canVote(player.playerName)) {
			player.boxMessage(
					"You recently claimed a vote reward, you must",
					"wait @blu@"
							+ VoteCache.getHoursTillVote(player.playerName)
							+ "@bla@ till you can claim again.");
			return;
		}
		int reward = 0;
		try {
			reward = requiresReward(player);
		} catch (Exception e1) {
			// e1.printStackTrace();
		}
		if (reward == 2) {
			String date = DateAndTime.getTodaysDate();
			player.getFunction().voteReward(date);
		} else if (reward == 1) {
			player.sendMessage("@red@You haven't voted on all the toplists.");
		} else {
			player.sendMessage("You have not voted, visit our website at ownxile.com or use @dre@::vote@bla@.");
		}
	}

	private static int requiresReward(Client player) throws Exception {
		if (World.getSettings().isDatabase()) {
			PreparedStatement ps = GameServer.getDatabase().getConnection()
					.prepareStatement("SELECT * FROM votes WHERE name = ?");
			ps.setString(1, player.playerName.toLowerCase());
			ResultSet results = ps.executeQuery();
			if (results.next()) {
				// if (results.getInt(2) == 2) {
				ps = GameServer.getDatabase().getConnection()
						.prepareStatement("DELETE FROM votes WHERE name = ?");
				ps.setString(1, player.playerName.toLowerCase());
				ps.executeUpdate();
				ps.close();
				return 2;
			}

			// return results.getInt(2);

			ps.close();

		}
		return 0;
	}

}
