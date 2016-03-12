package com.ownxile.util.web;

import com.ownxile.rs2.player.Player;

/**
 * @author Robbie
 * @function Contains player data that is sent to the database
 */
public class PlayerDatabaseObject {

	public String playerName;
	public int playerRights;
	public int[] playerXP = new int[25];
	public Long totalExp;
	public Short totalLevel;

	public PlayerDatabaseObject(Player player) {
		this.playerName = player.playerName;
		this.totalLevel = player.getTotalLevel();
		this.totalExp = player.getTotalExp();
		this.playerXP = player.playerXP;
		if (player.playerTitle > 0)
			playerRights = 10 + player.playerTitle;
		else
			this.playerRights = player.playerRights;
	}

}
