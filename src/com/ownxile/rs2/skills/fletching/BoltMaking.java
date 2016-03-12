package com.ownxile.rs2.skills.fletching;

import com.ownxile.config.SkillConfig;
import com.ownxile.rs2.items.ItemAssistant;
import com.ownxile.rs2.player.Client;
import com.ownxile.util.Misc;

public class BoltMaking {

	private static final int AMOUNT = 10;
	/**
	 * Data for making bolt tips
	 */

	private static final int[][] boltTips = {
	/** Uncut Gem ID || Bolt Tips ID || Animation ID */
	{ 1613, 9188, 892, 39 }, // topaz bolt tips
			{ 1611, 9187, 891, 24 }, // jade bolt tips
			{ 1607, 9189, 888, 47 }, // sapphire bolt tips
			{ 1605, 9190, 889, 55 }, // emerald bolt tips
			{ 1603, 9191, 887, 63 }, // ruby bolt tips
			{ 1601, 9192, 886, 70 }, // diamond bolt tips
			{ 1615, 9193, 885, 82 }, // dragon bolt tips
	};

	/**
	 * Data for making bolts
	 */

	private static int[][] craftingVariables = {
	/** Unf Bolt ID || Bolt Tips ID || Tipped Bolts || Level Req || Exp Gained */
	{ 9378, 9188, 9336, 48, 39 }, // topaz tipped bolts
			{ 9379, 9187, 9239, 26, 24 }, // jade tipped bolts
			{ 9379, 9189, 9240, 56, 47 }, // sapphire tipped bolts
			{ 9379, 9190, 9241, 58, 55 }, // emerald tipped bolts
			{ 9380, 9191, 9242, 63, 63 }, // ruby tipped bolts
			{ 9380, 9192, 9243, 65, 70 }, // diamond tipped bolts
			{ 9381, 9193, 9244, 71, 82 }, // dragonstone tipped bolts
			{ 9381, 9194, 9245, 73, 94 }, // onyx tipped bolts
	};

	private static final int DELETE = 1;

	/**
	 * Method to craft bolts with bolt tips
	 * 
	 * @param c
	 * @param itemUsed
	 * @param useWith
	 */

	public static void handleBoltCrafting(Client c, int itemUsed, int useWith) {
		for (int[] craftingVariable : craftingVariables) {
			if ((itemUsed == craftingVariable[0] || itemUsed == craftingVariable[1])
					&& (useWith == craftingVariable[0] || useWith == craftingVariable[1])) {
				if (c.playerLevel[c.playerFletching] >= craftingVariable[3]) {
					if (System.currentTimeMillis() - c.alchDelay > 1200) {
						if (c.getItems().playerHasItem(craftingVariable[0],
								AMOUNT)
								&& c.getItems().playerHasItem(
										craftingVariable[1], AMOUNT)) {
							c.getItems().deleteItem(
									craftingVariable[0],
									c.getItems().getItemSlot(
											craftingVariable[0]), AMOUNT);
							c.getItems().deleteItem(
									craftingVariable[1],
									c.getItems().getItemSlot(
											craftingVariable[1]), AMOUNT);
							c.getItems().addItem(craftingVariable[2], AMOUNT);
							c.getTask().addSkillXP(
									craftingVariable[4]
											* SkillConfig.FLETCHING_EXPERIENCE,
									c.playerFletching);
							c.sendMessage("You carefully craft some gem tipped bolts.");
							c.alchDelay = System.currentTimeMillis();
						} else {
							c.sendMessage("You need at least "
									+ AMOUNT
									+ " "
									+ Misc.formatPlayerName(ItemAssistant
											.getItemName(craftingVariable[0]))
									+ " & "
									+ Misc.formatPlayerName(ItemAssistant
											.getItemName(craftingVariable[1]))
									+ ".");
						}
					}
				} else {
					c.sendMessage("You need at least " + craftingVariable[3]
							+ " fletching to make this.");
				}
			}
		}
	}

	/**
	 * Method to cut gems into bolt tips
	 * 
	 * @param c
	 * @param itemUsed
	 * @param useWith
	 */

	public static void handleBoltTipCrafting(Client c, int itemUsed, int useWith) {
		for (int[] boltTip : boltTips) {
			if ((itemUsed == boltTip[0] || itemUsed == 1755)
					&& (useWith == boltTip[0] || useWith == 1755)) {
				if (c.playerLevel[c.playerFletching] >= boltTip[3]) {
					if (System.currentTimeMillis() - c.alchDelay > 1400) {
						if (c.getItems().playerHasItem(boltTip[0], DELETE)) {
							c.getItems().deleteItem(boltTip[0], DELETE);
							c.getItems().addItem(boltTip[1], AMOUNT);
							c.startAnimation(boltTip[2]);
							c.sendMessage("You carefully craft the "
									+ ItemAssistant.getItemName(boltTip[0])
									+ " into bolt tips.");
							c.alchDelay = System.currentTimeMillis();
						} else {
							c.getItems();
							c.sendMessage("You need at least "
									+ DELETE
									+ " "
									+ Misc.formatPlayerName(ItemAssistant
											.getItemName(boltTip[0])) + " gem.");
						}
					}
				} else {
					c.sendMessage("You need a fletching level of " + boltTip[3]
							+ " to make "
							+ ItemAssistant.getItemName(boltTip[1]) + ".");
				}
			}
		}
	}

}
