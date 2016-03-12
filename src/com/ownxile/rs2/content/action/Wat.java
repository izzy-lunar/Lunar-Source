package com.ownxile.rs2.content.action;

import com.ownxile.rs2.player.Client;
import com.ownxile.util.Misc;

public class Wat {

	private final static int WIN_PERCENTAGE = 50;

	/**
	 * @param player
	 * @param cost
	 */
	public static void gambleMoney(Client player, int cost) {
		if (player.getItems().playerHasItem(995, cost)) {
			if (Misc.random(100) >= WIN_PERCENTAGE) {
				player.getItems().addItem(995, cost);
				player.sendMessage("Congratulations, you have won " + cost
						+ " coins!");
				return;
			}
			player.sendMessage("Unlucky, you lose " + cost + " coins.");
			player.getItems().deleteItem2(995, cost);
			return;
		}
		player.sendMessage("You can't afford to gamble " + cost + " coins.");
	}

}
