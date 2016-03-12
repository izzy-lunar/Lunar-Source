package com.ownxile.rs2.skills.mining;

import com.ownxile.config.SkillConfig;
import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.player.Client;
import com.ownxile.util.Misc;

public class Mining {

	public static void prospectRock(final Client c, final String itemName) {
		c.sendMessage("You examine the rock for ores...");
		World.getSynchronizedTaskScheduler().schedule(new Task(4, false) {
			@Override
			protected void execute() {
				c.sendMessage("The rock contains " + itemName + ".");
				stop();
			}
		});
	}

	Client c;

	private int exp;
	public boolean isMining = false;
	private int oreType;
	private final int[] PICK_REQS = { 1, 1, 6, 6, 21, 31, 41, 61 };
	int pickType, levelReq;
	private final int[] RANDOM_GEMS = { 1623, 1621, 1619, 1617, 1631 };

	private final int VALID_PICK[] = { 1265, 1267, 1269, 1273, 1271, 1275, 8292 };

	public Mining(Client c) {
		this.c = c;
	}

	public int getMiningTimer() {
		int time = Misc.random(5);
		return time + Misc.random(oreModifier());
	}

	private int getPickaxeAnimation() {
		switch (c.playerEquipment[c.playerWeapon]) {
		case 1275:// Rune Pickaxe
			return 624;
		case 1271:// Adamant Pickaxe
			return 628;
		case 1273:// Mithril Pickaxe
			return 629;
		case 1269:// Steel Pickaxe
			return 627;
		case 1267:// Iron Pickaxe
			return 626;
		case 1265:// Bronze Pickaxe
			return 625;
		}
		return 624;
	}

	public int goodPick() {
		for (int j = VALID_PICK.length - 1; j >= 0; j--) {
			if (c.playerEquipment[c.playerWeapon] == VALID_PICK[j]) {
				if (c.playerLevel[c.playerMining] >= PICK_REQS[j]) {
					return VALID_PICK[j];
				}
			}
		}
		for (int playerItem : c.playerItems) {
			for (int j = VALID_PICK.length - 1; j >= 0; j--) {
				if (playerItem == VALID_PICK[j] + 1) {
					if (c.playerLevel[c.playerMining] >= PICK_REQS[j]) {
						return VALID_PICK[j];
					}
				}
			}
		}
		return -1;
	}

	private final int randomNumber = 17;

	private void mineOre() {
		if (c.getItems().addItem(oreType, 1) && Misc.random(60) != randomNumber) {
			c.miningTimer = getMiningTimer();
			c.startAnimation(getPickaxeAnimation());
			boolean essence = oreType == 7936 || oreType == 1436;
			String typeName = essence ? "rune essence" : "ore";
			c.sendMessage("You manage to mine some " + typeName + ".");
			c.getFunction().addSkillXP(exp * SkillConfig.MINING_EXPERIENCE,
					c.playerMining);
			c.getFunction().refreshSkill(c.playerMining);
		} else if (Misc.random(30) == randomNumber) {
			c.getItems().addItem(
					RANDOM_GEMS[(int) (RANDOM_GEMS.length * Math.random())], 1);
			c.getFunction().addSkillXP(100 * SkillConfig.MINING_EXPERIENCE,
					c.playerMining);
			c.sendMessage("You find a gem!");
			resetMining();
		} else {
			resetMining();
			c.getFunction().resetVariables();
			c.startAnimation(65535);
		}
	}

	public void miningEvent(final Client c, boolean essence) {
		if (isMining) {
			return;
		}
		isMining = true;
		c.originalX = c.getX();
		c.originalY = c.getY();
		c.originalZ = c.getZ();
		World.getSynchronizedTaskScheduler().schedule(
				new Task(essence ? 1 : 2, false) {
					@Override
					protected void execute() {
						if (isMining && c.miningTimer == 4
								|| c.miningTimer == 8 || c.miningTimer == 12
								|| c.miningTimer == 16 || c.miningTimer == 20) {
							c.startAnimation(getPickaxeAnimation());
						}
						if (c.miningTimer > 0) {
							c.miningTimer--;
						}
						if (c.getItems().freeSlots() < 1 || c.mining[0] == 0
								|| c.mining[1] == 0 || c.mining[2] == 0
								|| c.absX != c.originalX
								|| c.absY != c.originalY
								|| c.absZ != c.originalZ) {
							isMining = false;
							resetMining();
							c.startAnimation(65535);
							this.stop();
						}
						if (c.miningTimer == 0 && c.mining[0] > 0) {
							c.miningTimer = getMiningTimer();
							mineOre();
						}
					}
				});
	}

	private int oreModifier() {
		switch (c.mining[0]) {
		case 451:// Runite Ore
			return 15;
		case 449:// Adamantite Ore
			return 8;
		case 447:// Mithril
			return 6;
		case 444:// Gold
			return 6;
		case 453:// Coal
			return 5;
		case 442:// Silver
			return 4;
		case 440:// Iron
			return 3;
		case 436:// Copper
		case 428:// Tin
		case 434:// Clay
			return 1;
		case 7936:
			return -1;
		case 1436:
			return -1;
		}
		return 1;
	}

	public void resetMining() {
		this.oreType = -1;
		this.exp = -1;
		this.levelReq = -1;
		this.pickType = -1;
		c.miningTimer = -1;
	}

	public void startMining(int oreType, int levelReq, int exp) {
		c.turnPlayerTo(c.objectX, c.objectY);
		if (!c.hasInventorySpace(1)) {
			c.sendMessage("You don't have enough free inventory slots to mine ore.");
			return;
		}
		if (goodPick() > 0) {
			if (c.playerLevel[c.playerMining] >= levelReq) {
				this.oreType = oreType;
				this.exp = exp;
				this.levelReq = levelReq;
				this.pickType = goodPick();
				c.sendMessage("You swing your pick at the rock.");
				c.miningTimer = getMiningTimer();
				if (!isMining) {
					boolean essence = oreType == 7936 || oreType == 1436;
					miningEvent(c, essence);
				}
				c.startAnimation(getPickaxeAnimation());
			} else {
				resetMining();
				c.sendMessage("You need a mining level of " + levelReq
						+ " to mine this rock.");
				c.startAnimation(65535);
			}
		} else {
			resetMining();
			c.sendMessage("You need a pickaxe to mine this rock.");
			c.startAnimation(65535);
			c.getFunction().resetVariables();
		}
	}
}