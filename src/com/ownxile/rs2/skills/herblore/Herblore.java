package com.ownxile.rs2.skills.herblore;

import com.ownxile.config.SkillConfig;
import com.ownxile.rs2.items.ItemAssistant;
import com.ownxile.rs2.player.Client;

public class Herblore {

	private final Client c;
	private final int[][] information = { { 199, 249, 1, 3 },
			{ 201, 251, 5, 4 }, { 203, 253, 11, 5 }, { 205, 255, 20, 6 },
			{ 207, 257, 25, 8 }, { 3049, 2998, 30, 8 }, { 209, 259, 40, 9 },
			{ 211, 261, 48, 10 }, { 213, 263, 54, 11 }, { 3051, 3000, 59, 12 },
			{ 215, 265, 65, 13 }, { 2485, 2481, 67, 13 }, { 217, 267, 70, 14 },
			{ 219, 269, 75, 15 } };
	private final int[][] information2 = { { 249, 121, 2428, 1, 25 },
			{ 251, 175, 2446, 5, 38 }, { 253, 115, 113, 12, 50 },
			{ 255, 127, 2430, 22, 63 }, { 257, 139, 2434, 38, 88 },
			{ 261, 2448, 2448, 40, 100 }, { 259, 145, 2436, 45, 100 },
			{ 263, 157, 2440, 55, 125 }, { 3000, 3026, 3024, 63, 143 },
			{ 265, 163, 2442, 66, 155 }, { 2481, 2454, 2452, 69, 158 },
			{ 267, 169, 2444, 72, 163 }, { 269, 189, 2450, 78, 175 },
			{ 2998, 6687, 6685, 81, 180 } };

	// {identifiedHerbId,3dosepot,4dosepot,levelreq,exp}
	public Herblore(Client c) {
		this.c = c;
	}

	private int getSlot(int herb) {
		for (int j = 0; j < information2.length; j++) {
			if (information2[j][0] == herb) {
				return j;
			}
		}
		return -1;
	}

	public void handleHerbClick(int herbId) {
		for (int j = 0; j < information.length; j++) {
			if (information[j][0] == herbId) {
				idHerb(j);
			}
		}
	}

	public void handlePotMaking(int item1, int item2) {
		if (item1 == 227 && isIdedHerb(item2)) {
			makePot(item2);
		} else if (item2 == 227 && isIdedHerb(item1)) {
			makePot(item1);
		}
	}

	private void idHerb(int slot) {
		if (c.getItems().playerHasItem(information[slot][0])) {
			if (c.playerLevel[c.playerHerblore] >= information[slot][2]) {
				c.getItems().deleteItem(information[slot][0],
						c.getItems().getItemSlot(information[slot][0]), 1);
				c.getItems().addItem(information[slot][1], 1);
				c.getFunction().addSkillXP(
						information[slot][3] * SkillConfig.HERBLORE_EXPERIENCE,
						c.playerHerblore);
				c.sendMessage("You identify the herb as a "
						+ ItemAssistant.getItemName(information[slot][1]) + ".");
			} else {
				c.sendMessage("You need a herblore level of "
						+ information[slot][2] + " to identify this herb.");
			}
		}
	}

	public boolean isIdedHerb(int item) {
		for (int[] element : information2) {
			if (element[0] == item) {
				return true;
			}
		}
		return false;
	}

	public boolean isUnidHerb(int clickId) {
		for (int[] element : information) {
			if (element[0] == clickId) {
				return true;
			}
		}
		return false;
	}

	private void makePot(int herbId) {
		if (c.getItems().playerHasItem(227)
				&& c.getItems().playerHasItem(herbId)) {
			final int slot = getSlot(herbId);
			if (c.playerLevel[c.playerHerblore] >= information2[slot][3]) {
				c.getItems().deleteItem(herbId,
						c.getItems().getItemSlot(herbId), 1);
				c.getItems().deleteItem(227, c.getItems().getItemSlot(227), 1);
				c.startAnimation(1652);
				/*
				 * if (c.playerLevel[c.playerHerblore] < information2[slot][3] +
				 * 15) { c.getItems().addItem(information2[slot][1],1);
				 * c.sendMessage("You make a " +
				 * ItemAssistant.getItemName(information2[slot][1]) + "."); }
				 * else {
				 */
				c.getItems().addItem(information2[slot][2], 1);
				c.sendMessage("You make a "
						+ ItemAssistant.getItemName(information2[slot][2])
						+ ".");
				// }
				c.getFunction()
						.addSkillXP(
								information2[slot][4]
										* SkillConfig.HERBLORE_EXPERIENCE,
								c.playerHerblore);
			} else {
				c.sendMessage("You need a herblore level of "
						+ information2[slot][3] + " to make this pot.");
			}
		}
	}

}