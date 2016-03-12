package com.ownxile.rs2.content.item;

import com.ownxile.config.CombatConfig;
import com.ownxile.rs2.items.ItemAssistant;
import com.ownxile.rs2.player.Client;

public class BuryBones {

	private static final int[][] BONE_DATA = { { 526, 5 }, { 532, 15 },
			{ 534, 30 }, { 536, 72 }, { 6729, 125 }, { 4834, 100 },
			{ 6812, 216 } };

	public static void bonesOnAltar(Client c, int id) {
		c.getItems().deleteItem(id, c.getItems().getItemSlot(id), 1);
		c.sendMessage("The gods are pleased with your offering.");
		c.getFunction().addSkillXP(
				(int) ((int) getExp(id)
						* CombatConfig.getPrayerXP(c.playerTitle) * 2.5), 5);
		c.gfx0(435);
		c.startAnimation(896);
	}

	public static void buryBone(Client c, int id, int slot) {
		if (System.currentTimeMillis() - c.buryDelay > 1500) {
			c.getItems().deleteItem(id, slot, 1);
			c.sendMessage("You bury the " + ItemAssistant.getItemName(id) + ".");
			c.getFunction().addSkillXP(
					getExp(id) * CombatConfig.getPrayerXP(c.playerTitle), 5);
			c.buryDelay = System.currentTimeMillis();
			c.startAnimation(827);
		}
	}

	public static int getExp(int id) {
		for (int[] element : BONE_DATA) {
			if (element[0] == id) {
				return element[1];
			}
		}
		return 0;
	}

	public static boolean isBone(int id) {
		for (int[] element : BONE_DATA) {
			if (element[0] == id) {
				return true;
			}
		}
		return false;
	}
}