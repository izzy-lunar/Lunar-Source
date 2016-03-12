package com.ownxile.rs2.skills.smithing;

import com.ownxile.config.CombatConfig;
import com.ownxile.config.SkillConfig;
import com.ownxile.rs2.items.ItemAssistant;
import com.ownxile.rs2.player.Client;

public class SuperHeat {

	private Client c;

	public SuperHeat(Client c) {
		this.c = c;
	}

	private final int[] SMELT_EXP = { 6, 13, 15, 18, 23, 30, 38, 50 };
	private final int[] SMELT_BARS = { 2349, 2351, 2355, 2353, 2357, 2359,
			2361, 2363 };
	private final int[] ORE = { 438, 440, 442, 440, 444, 447, 449, 451 };

	private final int[] BAR_REQS = { 1, 15, 20, 30, 40, 50, 70, 85 };

	public boolean isOre(int item) {
		for (int i = 0; i < ORE.length; i++) {
			if (item == ORE[i])
				return true;
		}
		return false;
	}

	public boolean canHeat(int barType) {
		for (int j = 0; j < SMELT_BARS.length; j++) {
			if (barType == SMELT_BARS[j]) {
				return c.playerLevel[c.playerSmithing] >= BAR_REQS[j];
			}
		}
		return false;
	}

	public int getExp(int ore) {
		for (int j = 0; j < ORE.length; j++) {
			if (ore == ORE[j]) {
				return SMELT_EXP[j];
			}
		}
		return 0;
	}

	public int getBar(int ore) {
		for (int j = 0; j < ORE.length; j++) {
			if (ore == ORE[j]) {
				return SMELT_BARS[j];
			}
		}
		return 0;
	}

	public void resetSmithingStuff() {
		c.getFunction().resetVariables();
		c.getFunction().closeAllWindows();
	}

	public boolean hasOres2(int barType) {
		return c.getItems().playerHasItem(barType);
	}

	public boolean hasOresAmount(int ore) {
		return c.getItems().playerHasItem(ore);
	}

	public void heat(int barType) {
		c.getItems().deleteItem2(barType, 1);
		c.getItems().deleteItem2(561, 1);
		c.getItems().deleteItem2(554, 4);
		c.getItems().addItem(getBar(barType), 1);
		c.getFunction().addSkillXP(
				getExp(barType) * SkillConfig.SMITHING_EXPERIENCE,
				c.playerSmithing);
		c.getFunction().addSkillXP(
				getExp(barType) * CombatConfig.getCombatXP(c.playerTitle),
				c.playerMagic);
		c.getFunction().refreshSkill(c.playerSmithing);
		c.getFunction().refreshSkill(c.playerMagic);
	}

	public void startHeating(final int oreId) {

		if (hasOres2(oreId) && isOre(oreId)) {
			c.getFunction().closeAllWindows();
			c.startAnimation(722);
			c.gfx0(148);
			c.sendMessage("You superheat the "
					+ ItemAssistant.getItemName(oreId) + " into a "
					+ ItemAssistant.getItemName(getBar(oreId)) + ".");
			heat(oreId);
		} else {
			c.sendMessage("You can't superheat a "
					+ ItemAssistant.getItemName(oreId) + ".");
		}
	}
}