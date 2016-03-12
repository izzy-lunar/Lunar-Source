package com.ownxile.rs2.content.object;

import com.ownxile.rs2.player.Client;

public class Cactus {

	// todo cactus cutting event

	public static void cutCactus(Client player) {
	}

	private static final int[] WATERSKINS = { 1825, 1827, 1829, 1831 };

	private int getNextWaterskin(int itemId) {
		switch (itemId) {
		case 1825:
			return 1823;
		case 1827:
			return 1825;
		case 1829:
			return 1827;
		case 1831:
			return 1829;
		}
		return -1;
	}

	@SuppressWarnings("unused")
	private boolean replaceWaterskin(Client player) {
		for (int i = 0; i < WATERSKINS.length; i++) {
			if (player.getItems().playerHasItem(WATERSKINS[i])) {
				player.deleteItem(WATERSKINS[i]);
				player.addItem(getNextWaterskin(WATERSKINS[i]));
				return true;
			}
		}
		return false;
	}
}
