package com.ownxile.core;

import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.Player;
import com.ownxile.rs2.player.PlayerSave;

public class KillGame extends Thread {

	@Override
	public void run() {
		saveAll();
		System.out.println("Server has safetly shutdown.");
	}

	private void saveAll() {
		for (Player player : World.getPlayers()) {
			if (player == null) {
				continue;
			}

			Client client = (Client) player;
			if (client.inTrade) {
				client.getTrade().declineTrade();
			}
			PlayerSave.saveGame(client);
		}
	}

}