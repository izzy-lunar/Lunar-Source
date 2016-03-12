package com.ownxile.rs2.content.item;

import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.world.games.TreasureTrails;

public class Casket {

	public static final int[] CASKETS = { 2714, 2715, 2717 };

	public static int getCasketReward(int difficulty) {
		if (difficulty < 2) {
			return 2714;
		} else if (difficulty > 3) {
			return 2717;
		}
		return 2715;
	}

	public static void handleItem(Client player, int item, int slot) {
		if (player.hasInventorySpace(4)) {
			for (int i = 0; i < CASKETS.length; i++) {
				if (item == CASKETS[i]) {
					player.getItems().deleteItem(item, slot, 1);
					TreasureTrails.addReward(player, i);
					return;
				}
			}
		} else {
			player.sendMessage("You need 4 free inventory spaces to open the casket.");
		}
	}
}
