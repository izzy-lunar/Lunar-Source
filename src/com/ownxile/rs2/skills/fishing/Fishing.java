package com.ownxile.rs2.skills.fishing;

import com.ownxile.config.SkillConfig;
import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.items.ItemAssistant;
import com.ownxile.rs2.player.Client;
import com.ownxile.util.Misc;

public class Fishing {

	private enum FishingSpot {

		BAIT(316, new int[] { 327, 345 }, 307, 313, new int[] { 1, 10 }, true,
				new int[] { 20, 30 }, 623),

		BAITA(326, new int[] { 327, 345 }, 307, 313, new int[] { 1, 10 }, true,
				new int[] { 20, 30 }, 623),

		SMALLNET(313, new int[] { 317, 321 }, 303, -1, new int[] { 1, 15 },
				false, new int[] { 10, 40 }, 621),

		BIGNET(316, new int[] { 353, 341, 363 }, 305, -1, new int[] { 16, 23,
				46 }, false, new int[] { 23, 45, 101 }, 620),

		HARPOON2(313, new int[] { 383, 389 }, 311, -1, new int[] { 76, 91 },
				true, new int[] { 111, 153 }, 618),

		CAGE(312, new int[] { 377 }, 301, -1, new int[] { 40 }, false,
				new int[] { 91 }, 619),

		HARPOON(312, new int[] { 359, 371 }, 311, -1, new int[] { 35, 50 },
				true, new int[] { 81, 103 }, 618),

		LURE(309, new int[] { 335, 331 }, 309, 314, new int[] { 20, 30 },
				false, new int[] { 50, 70 }, 623),

		LURE2(309, new int[] { 349 }, 307, 313, new int[] { 25 }, true,
				new int[] { 61 }, 623),

		MONKNET(326, new int[] { 395 }, 303, -1, new int[] { 96 }, false,
				new int[] { 132 }, 621);

		int npcId, equipment, bait, anim;
		int[] rawFish, fishingReqs, xp;
		boolean second;

		private FishingSpot(int npcId, int[] rawFish, int equipment, int bait,
				int[] fishingReqs, boolean second, int[] xp, int anim) {
			this.npcId = npcId;
			this.rawFish = rawFish;
			this.equipment = equipment;
			this.bait = bait;
			this.fishingReqs = fishingReqs;
			this.second = second;
			this.xp = xp;
			this.anim = anim;
		}

		public int getAnim() {
			return anim;
		}

		public int getBait() {
			return bait;
		}

		public int getEquipment() {
			return equipment;
		}

		public int[] getLevelReq() {
			return fishingReqs;
		}

		public int getNPCId() {
			return npcId;
		}

		public int[] getRawFish() {
			return rawFish;
		}

		public boolean getSecond() {
			return second;
		}

		public int[] getXp() {
			return xp;
		}
	}

	public static FishingSpot forSpot(int npcId, boolean secondClick) {
		for (FishingSpot s : FishingSpot.values()) {
			if (secondClick) {
				if (s.getSecond()) {
					if (s.getNPCId() == npcId) {
						if (s != null) {
							return s;
						}
					}
				}
			} else {
				if (s.getNPCId() == npcId) {
					if (s != null) {
						return s;
					}
				}
			}
		}
		return null;
	}

	public static int getMax(Client c, int[] reqs) {
		int tempInt = -1;
		for (int i : reqs) {
			if (c.playerLevel[c.playerFishing] >= i) {
				tempInt++;
			}
		}
		return tempInt;
	}

	public static boolean hasBarb(Client c, FishingSpot s) {
		boolean has = c.hasItem(10129)
				|| c.playerEquipment[c.playerWeapon] == 10129;
		return has && s.equipment == 311;
	}

	public static void setupFishing2(Client c, FishingSpot s) {
		if (c.isDoingSkill) {
			return;
		}
		if (c.playerLevel[c.playerFishing] >= s.getLevelReq()[0]) {
			if (c.getItems().playerHasItem(s.getEquipment()) || hasBarb(c, s)) {
				if (s.getBait() != -1) {
					if (c.getItems().playerHasItem(s.getBait(), 1)) {
						startFishing(c, s);
					} else {
						c.boxMessage("You don't have enough bait to fish here.");
						c.startAnimation(65535);
					}
				} else {
					startFishing(c, s);
				}
			} else {
				c.boxMessage("You need a "
						+ Misc.ucFirst(ItemAssistant.getItemName(
								s.getEquipment()).toLowerCase())
						+ " to fish here.");
			}
		} else {
			c.boxMessage("You need a Fishing level of at least "
					+ s.getLevelReq()[0] + " to fish here.");
		}
	}

	public static void startFishing(final Client c, final FishingSpot s) {
		c.startAnimation(s.getAnim());
		c.originalX = c.getX();
		c.originalY = c.getY();
		c.originalZ = c.getZ();
		c.isDoingSkill = true;
		World.getSynchronizedTaskScheduler().schedule(new Task(2) {
			int cycle = 0;

			@Override
			public void execute() {
				if (Misc.random(hasBarb(c, s) ? 1 : 2) == 1) {
					cycle++;
				}
				c.startAnimation(s.getAnim());
				if (c.originalX != c.getX() || c.originalY != c.getY()
						|| c.originalZ != c.getZ()) {
					stop();
					c.isDoingSkill = false;
					c.startAnimation(65535);
					return;
				}
				if (c.getItems().freeSlots() < 1) {
					c.boxMessage("You cannot catch fish with a full inventory.");
					stop();
					c.isDoingSkill = false;
					c.startAnimation(65535);
					return;
				}
				if (cycle == 1) {
					int wat = Misc.random(100) >= 70 ? getMax(c, s.fishingReqs)
							: getMax(c, s.fishingReqs) != 0 ? getMax(c,
									s.fishingReqs) - 1 : 0;
					c.sendMessage("You catch a "
							+ ItemAssistant.getItemName(s.getRawFish()[wat])
									.toLowerCase().replace("_", " ") + ".");
					if (s.getBait() != -1) {
						c.getItems().deleteItem(s.getBait(),
								c.getItems().getItemSlot(s.getBait()), 1);
					}
					c.getItems().addItem(s.getRawFish()[wat], 1);
					c.getFunction().addSkillXP(
							s.getXp()[wat] * SkillConfig.FISHING_EXPERIENCE,
							c.playerFishing);
					cycle = 0;
				}
			}
		});
	}
}