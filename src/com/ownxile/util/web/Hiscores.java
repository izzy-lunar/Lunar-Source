package com.ownxile.util.web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import com.ownxile.GameServer;
import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.player.Client;

/**
 * @author Robbie <Roboyto>
 * @description Queued Hiscore Updating
 */
public class Hiscores {

	private Connection connnection = null;

	private final int CYCLES_PER_UPDATE = 1500;

	private Hashtable<String, PlayerDatabaseObject> saveQueue = new Hashtable<String, PlayerDatabaseObject>();

	private final int TOTAL_LEVEL_REQUIREMENT = 500;

	private boolean updateInProgress = false;

	public void addToQueue1(Client player) {
		if (updateInProgress) {
			return;
		}
		if (!World.getSettings().highscoresEnabled()) {
			return;
		}
		if (player.playerRights > 1) {
			return;
		}
		PlayerDatabaseObject p = new PlayerDatabaseObject(player);
		if (p == null)
			return;
		if (p.totalLevel < TOTAL_LEVEL_REQUIREMENT) {
			return;
		}
		if (saveQueue.containsKey(p.playerName)) {
			return;
		}
		saveQueue.put(p.playerName, p);
	}

	public void init() {
		if (!World.getSettings().highscoresEnabled()) {
			return;
		}
		World.getAsynchronousTaskScheduler().schedule(
				new Task(CYCLES_PER_UPDATE, false) {
					@Override
					protected void execute() {
						try {
							update();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	}

	private void deleteExistingPlayer(PlayerDatabaseObject p)
			throws SQLException, Exception {
		PreparedStatement ps = connnection
				.prepareStatement("SELECT * FROM players WHERE name = ?");
		ps.setString(1, p.playerName.toLowerCase());
		ResultSet results = ps.executeQuery();
		if (results.next()) {
			ps = GameServer.getDatabase().getConnection()
					.prepareStatement("DELETE FROM players WHERE name = ?");
			ps.setString(1, p.playerName.toLowerCase());
			ps.executeUpdate();
			ps.close();
		}
		ps.close();
	}

	public void insertPlayer(PlayerDatabaseObject p) throws SQLException,
			Exception {
		PreparedStatement preparedStatement = connnection
				.prepareStatement("INSERT INTO players");
		preparedStatement.setString(1, p.playerName.toLowerCase());
		preparedStatement.setInt(2, p.totalLevel);
		preparedStatement.setLong(3, p.totalExp);
		for (int i = 4; i < 25; i++) {
			preparedStatement.setInt(i, p.playerXP[i]);
		}
		preparedStatement.setInt(25, p.playerRights);
		preparedStatement.executeUpdate();
		preparedStatement.close();

	}

	public void update() throws Exception {
		System.out.println("Attempting to insert latest hiscores tables.");
		updateInProgress = true;
		if (connnection == null) {
			try {
				connnection = GameServer.getDatabase().getConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (connnection != null) {
			for (PlayerDatabaseObject p : saveQueue.values()) {
				try {
					deleteExistingPlayer(p);
					insertPlayer(p);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		saveQueue.clear();
		saveQueue = new Hashtable<String, PlayerDatabaseObject>();
		updateInProgress = false;
		System.out.println("The hiscores have been updated.");
	}

}
