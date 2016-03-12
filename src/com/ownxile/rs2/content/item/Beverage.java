package com.ownxile.rs2.content.item;

import com.ownxile.rs2.items.ItemAssistant;
import com.ownxile.rs2.player.Client;

public class Beverage {

	private static final int[] BEERS = { 1917, 1915, 4627, 1911, 7752, 5765,
			7750, 1913, 1909, 7754, 1905, 1907 };

	private static void drink(Client player, int itemId) {
		player.startAnimation(829);
		player.getItems();
		player.sendMessage("You drink the " + ItemAssistant.getItemName(itemId)
				+ ".");
		player.getFunction().delayMessage("You feel a little tipsy.", 1);
		player.deleteItem(itemId);
		player.addItem(1919);
	}

	public static void freeBeer(Client player) {
		for (int element : BEERS) {
			player.addItem(element);
		}
	}

	public static boolean handleBeverage(Client player, int itemId) {
		for (int element : BEERS) {
			if (element == itemId) {
				drink(player, itemId);
				return true;
			}
		}
		return false;
	}

}
