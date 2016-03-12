package com.ownxile.rs2.skills.farming;

import java.util.ArrayList;
import java.util.List;

import com.ownxile.config.SkillConfig;
import com.ownxile.rs2.items.ItemAssistant;
import com.ownxile.rs2.player.Client;
import com.ownxile.util.Misc;

public class Farming {

	private final static int[] FARMING_REQS = { 1, 14, 19, 26, 32, 38, 44, 50,
			56, 62, 67, 73, 79, 85 };

	private final static int[] HERB_EXPS = { 13, 15, 18, 24, 31, 39, 49, 62,
			78, 99, 120, 152, 192, 225 };
	private final static int[] HERBS = { 199, 201, 203, 205, 207, 3049, 209,
			211, 213, 3051, 215, 2485, 217, 219 };
	private final static int PATCH_CLEAN = 8132;
	private final static int PATCH_HERBS = 8137;
	private final static int DEAD_HERBS2 = 8149;
	private final static int RAKE = 5341;
	private final static int SEED_DIBBER = 5343;
	private final static int[] SEED_PLANT_EXP = { 11, 14, 16, 22, 27, 34, 43,
			55, 69, 88, 107, 135, 171, 200 };
	private final static int[] VALID_SEEDS = { 5291, 5292, 5293, 5294, 5295,
			5296, 5297, 5298, 5299, 5300, 5301, 5302, 5303, 5304 };
	private final static int WATER_CAN = 5340;
	private final Client c;

	private List<PatchSpot> patches = new ArrayList<PatchSpot>();

	public PatchSpot getSpot(int x, int y) {
		for (PatchSpot patchSpot : patches) {
			if (patchSpot.getAbsX() == x && patchSpot.getAbsY() == y)
				return patchSpot;
		}
		PatchSpot patchSpot = new PatchSpot(x, y);
		patches.add(patchSpot);
		return patchSpot;
	}

	public Farming(Client c) {
		this.c = c;
	}

	public void checkItemOnObject(int itemId, int obX, int obY) {
		PatchSpot patch = getSpot(obX, obY);
		if (patch.getStage() == -1 && itemId != RAKE) {
			c.boxMessage("You need to rake this patch.");
			return;
		}

		if (itemId == WATER_CAN) {
			if (patch.getStage() == 1) {
				waterSeed(obX, obY);
				c.boxMessage("You water the seed and the herb plant begins to grow.");
			} else if (patch.getStage() > 1) {
				c.boxMessage("The plant has already been watered.");
			} else {
				c.boxMessage("There's no seed to water.");
			}
		}
		if (itemId == RAKE) {
			rakePatch(patch, obX, obY); // raking is called
			patch.setStage(0);
		}
		for (int j = 0; j < VALID_SEEDS.length; j++) {
			if (itemId == VALID_SEEDS[j]) {
				if (patch.getStage() == 0) {
					plantSeed(obX, obY, VALID_SEEDS[j], HERBS[j], HERB_EXPS[j],
							j);
					patch.setStage(1);
				} else
					c.boxMessage("You need to rake this patch.");
			}
		}

	}

	private void cleanPatch(int obX, int obY) {
		c.getFunction().addObject(PATCH_CLEAN, obX, obY, 0, 0, 10);
	}

	public int getExp(int herbId) {
		for (int j = 0; j < HERBS.length; j++) {
			if (HERBS[j] == herbId) {
				return HERB_EXPS[j];
			}
		}
		return 0;
	}

	public void pickHerb(int obX, int obY) {
		PatchSpot patch = getSpot(obX, obY);
		if (patch != null) {
			if (patch.herbs > 0 && patch.getHerbId() > 0) {
				if (System.currentTimeMillis() - c.waitTime > 2000) {
					if (c.getItems().addItem(patch.getHerbId(), 1)) {
						c.getFunction().addSkillXP(
								getExp(patch.getHerbId())
										* SkillConfig.FARMING_EXPERIENCE,
								c.playerFarming);
						patch.herbs -= 1;
						c.startAnimation(2286);
						c.sendMessage("You pick a "
								+ ItemAssistant.getItemName(patch.getHerbId())
								+ ".");
						c.waitTime = System.currentTimeMillis();
						if (patch.herbs == 0) {
							patch.setHerbId(-1);
							updateHerbPatch(obX, obY);
						}
					}
				}
			}
		}
	}

	private void plantSeed(int x, int y, int seedId, int herbId, int exp,
			int slot) {
		if (c.playerLevel[c.playerFarming] < FARMING_REQS[slot]) {
			c.sendMessage("You require a farming level of "
					+ FARMING_REQS[slot] + " to farm this seed.");
		} else if (c.getItems().playerHasItem(seedId, 1)
				&& c.getItems().playerHasItem(SEED_DIBBER, 1)) {
			c.getItems()
					.deleteItem(seedId, c.getItems().getItemSlot(seedId), 1);
			c.getFunction().addSkillXP(
					SEED_PLANT_EXP[slot] * SkillConfig.FARMING_EXPERIENCE,
					c.playerFarming);
			c.startAnimation(2291);
			c.getFunction().refreshSkill(c.playerFarming);
			final int herbAmount = Misc.random(4) + 3;
			PatchSpot patch = getSpot(x, y);
			patch.herbs = herbAmount;
			patch.setHerbId(herbId);
			c.sendMessage("You plant your seed.");
			c.getTask().delayMessage(
					"The seed needs to be watered before it can grow.", 1);
		} else {
			c.sendMessage("You need a seed dibber to plant that seed.");
		}
	}

	private void rakePatch(PatchSpot patch, int obX, int obY) {
		c.startAnimation(2273);
		cleanPatch(obX, obY); // calls the clear patch.
		c.getFunction().addSkillXP(
				getExp(patch.getHerbId()) * SkillConfig.FARMING_EXPERIENCE,
				c.playerFarming);
	}

	public void updateHerbPatch(int obX, int obY) {
		PatchSpot patch = getSpot(obX, obY);
		if (patch.getHerbId() > 0 && patch.herbs > 0 && patch.getStage() > 1) {
			c.getFunction().addObject(PATCH_HERBS + patch.getStage(), obX, obY,
					0, 0, 10);
		} else {
			c.getFunction().addObject(PATCH_CLEAN, obX, obY, 0, 0, 10);
			patch.setStage(-1);
		}
	}

	private void waterSeed(int obX, int obY) {
		PatchSpot patch = getSpot(obX, obY);
		patch.setStage(2);
		c.startAnimation(2293);
		updateHerbPatch(obX, obY);
	}

	public String getInspectMessage(int obX, int obY) {
		PatchSpot patch = getSpot(obX, obY);
		switch (patch.getStage()) {
		case -1:
			return "You need to rake the patch!";
		case 0:
			return "You can plant seeds on this patch.";
		case 1:
			return "You need to water the seeds.";
		case 2:
		case 3:
			return "The herbs are starting to grow.";
		case 4:
		case 5:
			return "The " + ItemAssistant.getItemName(patch.getHerbId())
					+ " herbs wll soon be ready.";
		case 6:
		case 7:
			return "This plant contains " + patch.herbs + " "
					+ ItemAssistant.getItemName(patch.getHerbId()) + "s.";

		}
		return "";
	}

	public void growPlants() {
		for (PatchSpot patch : patches) {
			if (patch.getStage() > 1 && patch.getStage() < 7) {
				int objectId = PATCH_HERBS + patch.getStage();
				if (patch.getStage() == 5 && Misc.random(5) == 5) {
					objectId = DEAD_HERBS2;
					patch.setStage(-1);
					c.sendMessage("One of your herb plants has died.");
				}
				c.getFunction().addObject(objectId, patch.getAbsX(),
						patch.getAbsY(), 0, 0, 10);
				if (patch.getStage() == 6) {
					c.sendMessage("One of your "
							+ ItemAssistant.getItemName(patch.getHerbId())
							+ " plants is fully grown and ready to pick.");
				}
				patch.setStage(patch.getStage() + 1);
			}

		}
	}

	public void loadPlants() {
		for (PatchSpot patch : patches) {
			if (patch.getStage() > 1 && patch.getStage() < 8) {
				c.getFunction()
						.addObject(
								PATCH_HERBS
										+ (patch.getStage() == 7 ? 6
												: patch.getStage()),
								patch.getAbsX(), patch.getAbsY(), 0, 0, 10);
			} else if (patch.getStage() >= 0)
				c.getFunction().addObject(PATCH_CLEAN, patch.getAbsX(),
						patch.getAbsY(), 0, 0, 10);

		}
	}
}