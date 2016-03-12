package com.ownxile.rs2.world.games;

import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.Player;
import com.ownxile.rs2.player.PlayerHandler;

/**
 * @author Robbie <Roboyto>
 * @description a class to hold universal minigame methods
 */
public class GameBuilder {

	public static void announce(String s) {
		for (Player player : PlayerHandler.players) {
			if (player == null) {
				continue;
			}
			Client client = (Client) player;
			client.sendMessage("@blu@Minigame@bla@: @dre@" + s);
		}
	}

}
