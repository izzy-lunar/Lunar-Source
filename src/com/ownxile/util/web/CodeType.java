package com.ownxile.util.web;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.ownxile.GameServer;
import com.ownxile.core.World;

public abstract class CodeType {

	/**
	 * represents the command to activate the code and the context of the @World
	 * message
	 */
	private String codeName;

	/**
	 * 
	 */
	public abstract void getReward();

	/**
	 * @param table
	 * @param row
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public ResultSet getResult(String table, String row, String value)
			throws Exception {
		if (World.getSettings().isDatabase()) {
			PreparedStatement ps = GameServer
					.getDatabase()
					.getConnection()
					.prepareStatement(
							"SELECT * FROM" + table + " WHERE " + table
									+ " = ?");
			ps.setString(1, value.toLowerCase());
			ResultSet results = ps.executeQuery();
			if (results.next()) {
				ps = GameServer
						.getDatabase()
						.getConnection()
						.prepareStatement(
								"DELETE FROM" + table + " WHERE " + table
										+ " = ?");
				ps.setString(1, value.toLowerCase());
				ps.executeUpdate();
				ps.close();
				return results;
			}
			ps.close();
		}
		return null;
	}

	/**
	 * @return the codeName
	 */
	public String getCodeName() {
		return codeName;
	}

	/**
	 * @param codeName
	 *            the codeName to set
	 */
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

}
