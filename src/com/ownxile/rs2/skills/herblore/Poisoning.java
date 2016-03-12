package com.ownxile.rs2.skills.herblore;

import com.ownxile.rs2.items.ItemAssistant;
import com.ownxile.rs2.player.Client;

public class Poisoning {

	private static final String[][] poisionData = { { "arrow" }, { "dagger" },
			{ "spear" }, { "knife" }, { "dart" }, { "javelin" }, { "bolts" } };

	private static String getPoisionPrefix(int poisionId) {
		switch (poisionId) {
		case 187:
			return "(p)";
		case 5937:
			return "(p+)";
		case 5940:
			return "(p++)";
		}
		return null;
	}

	private static boolean isPoisionable(String weaponName) {
		for (String[] element : poisionData) {
			if (weaponName.toLowerCase().contains(element[0])) {
				return true;
			}
		}
		return false;
	}

	public static boolean useItemonItem(Client c, int id, int id2) {
		String poisionPrefix = null;
		int amount = 5;
		int toPoision = 0;
		int poisionVial = 0;
		c.getItems();
		String wep = ItemAssistant.getItemName(id);
		if (isPoisionable(wep)) {
			poisionPrefix = getPoisionPrefix(id2);
			if (!c.getItems().isStackable(id)) {
				amount = 1;
			}
			poisionVial = id2;
			toPoision = id;
		}
		c.getItems();
		String name2 = ItemAssistant.getItemName(id2);
		if (isPoisionable(name2)) {
			poisionPrefix = getPoisionPrefix(id);
			if (!c.getItems().isStackable(id2)) {
				amount = 1;
			}
			poisionVial = id;
			toPoision = id2;
		}
		if (poisionPrefix == null) {
			return false;
		}
		if (c.getItems().getItemAmount(toPoision) < amount) {
			amount = c.getItems().getItemAmount(toPoision);
		}
		int itemToAdd = c.getItems().getItemId(wep + poisionPrefix);
		c.getItems().deleteItem(toPoision, amount);
		c.getItems().deleteItem(poisionVial, 1);
		c.getItems().addItem(itemToAdd, amount);
		c.getItems().addItem(229, 1);
		c.sendMessage("You drop the poision over the weapon.");
		return true;
	}

}
