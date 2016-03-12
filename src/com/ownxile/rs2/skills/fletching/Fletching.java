package com.ownxile.rs2.skills.fletching;

import com.ownxile.config.SkillConfig;
import com.ownxile.rs2.player.Client;

public class Fletching {

	public final int[][] ARROWS = { { 52, 314, 53, 15, 1 },
			{ 53, 39, 882, 40, 1 }, { 53, 40, 884, 58, 15 },
			{ 53, 41, 886, 95, 30 }, { 53, 42, 888, 132, 45 },
			{ 53, 43, 890, 170, 60 }, { 53, 44, 892, 207, 75 } };

	public final int[][] BUTTONS = { { 34185, 1, 1 }, { 34184, 1, 5 },
			{ 34183, 1, 10 }, { 34182, 1, 27 }, { 34189, 2, 1 },
			{ 34188, 2, 5 }, { 34187, 2, 10 }, { 34186, 2, 27 },
			{ 34193, 3, 1 }, { 34193, 3, 5 }, { 34193, 3, 10 },
			{ 34193, 3, 27 } };

	Client c;

	public final int[] EXPERIENCE = { 5, 16, 33, 50, 67, 83 };

	public boolean fletching = false;
	int fletchType = 0, amount = 0, log = 0;

	public final int[] LOG_IDS = { 1511, 1521, 1519, 1517, 1515, 1513 };

	public final int[] LONGBOWS = { 48, 56, 58, 62, 66, 70 };

	public final int[] REQUIREMENTS = { 5, 20, 35, 50, 65, 80 };

	public int shaft = 52;

	public final int[] shortbows = { 50, 54, 60, 64, 68, 72 };

	public Fletching(Client c) {
		this.c = c;
	}

	public void fletchBow(int index) {
		final int toAdd = getItemToAdd(index);
		final int amountToAdd = getAmountToAdd(toAdd);
		for (int j = 0; j < amount; j++) {
			if (c.getItems().playerHasItem(LOG_IDS[index], 1)) {
				if (c.playerLevel[c.playerFletching] >= REQUIREMENTS[index]
						|| fletchType == 3) {
					c.getItems().deleteItem(LOG_IDS[index],
							c.getItems().getItemSlot(LOG_IDS[index]), 1);
					c.getItems().addItem(toAdd, amountToAdd);
					c.startAnimation(1248);
					c.getFunction().addSkillXP(
							getExp(index) * SkillConfig.FLETCHING_EXPERIENCE,
							c.playerFletching);
					c.getFunction().closeAllWindows();
				} else {
					c.boxMessage("This bow requires a fletching level of  "
							+ REQUIREMENTS[index] + ".");
					break;
				}
			} else {
				break;
			}
		}
	}

	public int getAmountToAdd(int id) {
		if (id == 52) {
			return 15;
		} else {
			return 1;
		}
	}

	public int getExp(int index) {
		if (fletchType == 3) {
			return 5;
		} else if (fletchType == 1) {
			return EXPERIENCE[index];
		} else {
			return EXPERIENCE[index] + 8;
		}

	}

	public int getItemToAdd(int index) {
		if (fletchType == 3) {
			return shaft;
		} else if (fletchType == 1) {
			return shortbows[index];
		} else if (fletchType == 2) {
			return LONGBOWS[index];
		}
		return 0;
	}

	public void handleFletchingClick(int clickId) {
		for (int[] button : BUTTONS) {
			if (button[0] == clickId) {
				fletchType = button[1];
				amount = button[2];
				for (int i = 0; i < LOG_IDS.length; i++) {
					if (log == LOG_IDS[i]) {
						fletchBow(i);
						break;
					}
				}
				break;
			}
		}
	}

	public void handleLog(int item1, int item2) {
		if (item1 == 946) {
			openFletching(item2);
		} else {
			openFletching(item1);
		}
	}

	public void makeArrows(int item1, int item2) {
		for (int[] arrow : ARROWS) {
			if (item1 == arrow[0] && item2 == arrow[1] || item2 == arrow[0]
					&& item1 == arrow[1]) {
				if (c.getItems().playerHasItem(item1, 15)
						&& c.getItems().playerHasItem(item2, 15)) {
					if (c.playerLevel[c.playerFletching] >= arrow[4]) {
						c.getItems().deleteItem(item1,
								c.getItems().getItemSlot(item1), 15);
						c.getItems().deleteItem(item2,
								c.getItems().getItemSlot(item2), 15);
						c.getItems().addItem(arrow[2], 15);
						c.getFunction().addSkillXP(
								arrow[3] * SkillConfig.FLETCHING_EXPERIENCE,
								c.playerFletching);
						c.startAnimation(1248);
						c.getFunction().closeAllWindows();
					} else {
						c.boxMessage("You need a fletching level of "
								+ arrow[4] + " to fletch this.");
					}
				} else {
					c.boxMessage("You must have 15 of each supply to do this.");
				}
			}
		}
	}

	public void openFletching(int item) {
		if (item == 1511) {
			c.getFunction().sendFrame164(8880);
			c.getFunction().sendFrame126("What would you like to make?", 8879);
			c.getFunction().sendFrame246(8884, 200, 839); // middle
			c.getFunction().sendFrame246(8883, 200, 841); // left picture
			c.getFunction().sendFrame246(8885, 200, 52); // right pic
			c.getFunction().sendFrame126("Shortbow", 8889);
			c.getFunction().sendFrame126("Longbow", 8893);
			c.getFunction().sendFrame126("Arrow Shafts", 8897);
			log = item;
		} else if (item == 1521) {
			c.getFunction().sendFrame164(8880);
			c.getFunction().sendFrame126("What would you like to make?", 8879);
			c.getFunction().sendFrame246(8884, 200, 845); // middle
			c.getFunction().sendFrame246(8883, 200, 843); // left picture
			c.getFunction().sendFrame246(8885, 200, 52); // right pic
			c.getFunction().sendFrame126("Oak Shortbow", 8889);
			c.getFunction().sendFrame126("Oak Longbow", 8893);
			c.getFunction().sendFrame126("Arrow Shafts", 8897);
			log = item;
		} else if (item == 1519) {
			c.getFunction().sendFrame164(8880);
			c.getFunction().sendFrame126("What would you like to make?", 8879);
			c.getFunction().sendFrame246(8884, 200, 847); // middle
			c.getFunction().sendFrame246(8883, 200, 849); // left picture
			c.getFunction().sendFrame246(8885, 200, 52); // right pic
			c.getFunction().sendFrame126("Willow Shortbow", 8889);
			c.getFunction().sendFrame126("Willow Longbow", 8893);
			c.getFunction().sendFrame126("Arrow Shafts", 8897);
			log = item;
		} else if (item == 1517) {
			c.getFunction().sendFrame164(8880);
			c.getFunction().sendFrame126("What would you like to make?", 8879);
			c.getFunction().sendFrame246(8884, 200, 851); // middle
			c.getFunction().sendFrame246(8883, 200, 853); // left picture
			c.getFunction().sendFrame246(8885, 200, 52); // right pic
			c.getFunction().sendFrame126("Maple Shortbow", 8889);
			c.getFunction().sendFrame126("Maple Longbow", 8893);
			c.getFunction().sendFrame126("Arrow Shafts", 8897);
			log = item;
		} else if (item == 1515) {
			c.getFunction().sendFrame164(8880);
			c.getFunction().sendFrame126("What would you like to make?", 8879);
			c.getFunction().sendFrame246(8884, 200, 855); // middle
			c.getFunction().sendFrame246(8883, 200, 857); // left picture
			c.getFunction().sendFrame246(8885, 200, 52); // right pic
			c.getFunction().sendFrame126("Yew Shortbow", 8889);
			c.getFunction().sendFrame126("Yew Longbow", 8893);
			c.getFunction().sendFrame126("Arrow Shafts", 8897);
			log = item;
		} else if (item == 1513) {
			c.getFunction().sendFrame164(8880);
			c.getFunction().sendFrame126("What would you like to make?", 8879);
			c.getFunction().sendFrame246(8884, 200, 859); // middle
			c.getFunction().sendFrame246(8883, 200, 861); // left picture
			c.getFunction().sendFrame246(8885, 200, 52); // right pic
			c.getFunction().sendFrame126("Magic Shortbow", 8889);
			c.getFunction().sendFrame126("Magic Longbow", 8893);
			c.getFunction().sendFrame126("Arrow Shafts", 8897);
			log = item;
		}
		fletching = true;
	}
}