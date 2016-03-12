package com.ownxile.rs2.content.item;

import com.ownxile.core.task.Task;
import com.ownxile.rs2.Location;
import com.ownxile.rs2.player.Client;

public class WaterSkin extends Task {

	private static final int[] WATERSKINS = { 1823, 1825, 1827, 1829 };

	private Client player;

	public WaterSkin(Client player) {
		super(100);
		this.player = player;
	}

	private int getNextWaterskin(int itemId) {
		switch (itemId) {
		case 1823:
			return 1825;
		case 1825:
			return 1827;
		case 1827:
			return 1829;
		case 1829:
			return 1831;

		}
		return -1;
	}

	private boolean inDesert() {
		return Location.DESERT.isInLocation(player);
	}

	private boolean hasWaterSkin() {
		for (int i = 0; i < WATERSKINS.length; i++) {
			if (player.getItems().playerHasItem(WATERSKINS[i])) {
				player.deleteItem(WATERSKINS[i]);
				player.addItem(getNextWaterskin(WATERSKINS[i]));
				return true;
			}
		}
		return false;
	}

	protected void execute() {
		if (inDesert()) {
			if (hasWaterSkin()) {
				player.sendMessage("You take a drink of water.");
				player.startAnimation(829);
			} else {
				player.getHit(2);
				player.sendMessage("You begin to die of thirst due to the desert heat.");
			}

		}

	}

}
