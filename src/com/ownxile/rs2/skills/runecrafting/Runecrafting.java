package com.ownxile.rs2.skills.runecrafting;

import com.ownxile.config.SkillConfig;
import com.ownxile.rs2.player.Client;

public class Runecrafting {

	/**
	 * An array containing the object IDs of the runecrafting altars.
	 */
	public static int[] altarID = { 2478, 2479, 2480, 2481, 2482, 2483, 2484,
			2487, 2486, 2485, 2488, 2489 };

	/**
	 * 2D Array containing the levels required to craft the specific
	 * com.ownxile.
	 */
	public static int[][] craftLevelReq = { { 556, 1 }, { 558, 2 }, { 555, 5 },
			{ 557, 9 }, { 554, 14 }, { 559, 20 }, { 564, 27 }, { 562, 35 },
			{ 561, 1 }, { 563, 54 }, { 560, 65 }, { 565, 77 } };

	/**
	 * 2D Array containing the levels that you can craft multiple runes.
	 */
	public static int[][] multipleRunes = {
			{ 11, 22, 33, 44, 55, 66, 77, 88, 99 },
			{ 14, 28, 42, 56, 70, 84, 98 }, { 19, 38, 57, 76, 95 },
			{ 26, 52, 78 }, { 35, 70 }, { 46, 92 }, { 59 }, { 74 }, { 91 },
			{ 100 }, { 100 }, { 100 } };

	/**
	 * Pure essence ID constant.
	 */
	private static final int PURE_ESS = 7936;

	/**
	 * Rune essence ID constant.
	 */
	private static final int RUNE_ESS = 1436;

	public static int[] runecraftExp = { 50, 60, 70, 80, 90, 100, 110, 120,
			130, 140, 150, 160 };

	/**
	 * An array containing the com.ownxile item numbers.
	 */
	public static int[] runes = { 556, 558, 555, 557, 554, 559, 564, 562, 561,
			563, 560, 565 };

	/**
	 * Crafts the specific com.ownxile.
	 */
	public static boolean craftRunes(Client c, int object) {
		int runeID = 0;

		for (int i = 0; i < altarID.length; i++) {
			if (object == altarID[i]) {
				runeID = runes[i];
			}
		}
		for (int i = 0; i < craftLevelReq.length; i++) {
			if (runeID == runes[i]) {
				if (c.playerLevel[20] >= craftLevelReq[i][1]) {
					if (c.getItems().playerHasItem(RUNE_ESS)) {
						int multiplier = 1;
						for (int j = 0; j < multipleRunes[i].length; j++) {
							if (c.playerLevel[20] >= multipleRunes[i][j]) {
								multiplier += 1;
							}
						}
						replaceEssence(c, RUNE_ESS, runeID, multiplier, i);
						c.startAnimation(791);
						c.gfx100(186);
						c.turnPlayerTo(c.objectX, c.objectY);
						return true;
					}
					if (c.getItems().playerHasItem(PURE_ESS)) {
						int multiplier = 1;
						for (int j = 0; j < multipleRunes[i].length; j++) {
							if (c.playerLevel[20] >= multipleRunes[i][j]) {
								multiplier += 1;
							}
						}
						multiplier += 1;
						replaceEssence(c, PURE_ESS, runeID, multiplier, i);
						c.startAnimation(791);
						c.gfx100(186);
						c.turnPlayerTo(c.objectX, c.objectY);
						return true;
					}
					c.sendMessage("You need to have essence to craft runes.");
					return true;
				}
				c.sendMessage("You need a Runecrafting level of "
						+ craftLevelReq[i][1] + " to craft this com.ownxile.");
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks through all 28 item inventory slots for the specified item.
	 */
	private static boolean itemInInv(Client c, int itemID, int slot,
			boolean checkWholeInv) {
		if (checkWholeInv) {
			for (int i = 0; i < 28; i++) {
				if (c.playerItems[i] == itemID + 1) {
					return true;
				}
			}
		} else {
			if (c.playerItems[slot] == itemID + 1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Replaces essence in the inventory with the specified com.ownxile.
	 */
	private static void replaceEssence(Client c, int essType, int runeID,
			int multiplier, int index) {
		int exp = 0;
		for (int i = 0; i < 28; i++) {
			if (itemInInv(c, essType, i, false)) {
				c.getItems().deleteItem(essType, i, 1);
				c.getItems().addItem(runeID, 1 * multiplier);
				exp += runecraftExp[index];
			}
		}
		c.getFunction().addSkillXP(exp * SkillConfig.RUNECRAFTING_EXPERIENCE,
				c.playerRunecrafting);
	}

}