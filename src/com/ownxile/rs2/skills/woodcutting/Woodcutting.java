package com.ownxile.rs2.skills.woodcutting;

import com.ownxile.core.Plugin;
import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.world.object.type.Object;
import com.ownxile.util.Misc;

public class Woodcutting {

	private static final int[] AXES = { 1351, 1349, 1353, 1361, 1355, 1357,
			1359, 6739 };

	/**
	 * @param player
	 * @param objectId
	 */
	public static void attemptWoodcutting(Client player, int objectId) {
		final int weapon = player.playerEquipment[player.playerWeapon];
		final int woodcuttingLevel = player.playerLevel[player.playerWoodcutting];
		player.turnPlayerTo(player.objectX, player.objectY);
		int axe = hasAxe(player);
		if (player.isDoingSkill) {
			return;
		}
		if (axe != -1) {
			if (canUseAxe(weapon, woodcuttingLevel)) {
				if (getTreeRequirement(objectId) <= woodcuttingLevel) {
					startCuttingTree(player, objectId, axe);
					return;
				}
				player.sendMessage("You need a woodcutting level of "
						+ getTreeRequirement(objectId) + " to cut that tree.");
				return;
			}
			player.sendMessage("You don't have a high enough level to use this axe.");
			player.sendMessage("You need a woodcutting level of "
					+ axeLevelRequirement(weapon) + " to use this axe.");
			return;
		}
		player.sendMessage("You need an axe to cut this tree.");
		return;
	}

	/**
	 * @param The
	 *            axe the player is wielding
	 * @return The level required to use the axe
	 */

	public static int axeLevelRequirement(int axe) {
		switch (axe) {
		case 1353:
		case 1361:
			return 6;
		case 1355:
			return 21;
		case 1357:
			return 31;
		case 1359:
			return 41;
		case 6739:
			return 61;
		}
		return 1;
	}

	/**
	 * @param players
	 *            axe
	 * @param players
	 *            woodcutting level
	 * @return Can use the axe?
	 */
	public static boolean canUseAxe(int weapon, int woodcutting) {
		if (axeLevelRequirement(weapon) <= woodcutting) {
			return true;
		}
		return false;
	}

	/**
	 * @param player
	 * @return free slots
	 */
	public static int freeSlots(Client player) {
		int slots = 0;
		for (int playerItem : player.playerItems) {
			if (playerItem <= 0) {
				slots++;
			}
		}
		return slots;
	}

	/**
	 * @param The
	 *            axe the player is wearing
	 * @return The animation for that axe
	 */

	public static int getAxeAnimation(int weapon) {
		switch (weapon) {
		case 1351:
			return 879;
		case 1349:
			return 877;
		case 1353:
			return 875;
		case 1361:
			return 873;
		case 1355:
			return 871;
		case 1357:
			return 869;
		case 1359:
			return 867;
		case 6739:
			return 2846;
		}
		return -1;
	}

	/**
	 * @param tree
	 *            object id
	 * @return The name of logs it provides
	 */

	public static String getLogName(int objectId) {
		switch (objectId) {
		case 2023:
			return "achey tree logs";
		case 1281:
			return "oak logs";
		case 1308:
		case 5551:
		case 5552:
			return "willow logs";
		case 1307:
			return "maple logs";
		case 9034:
			return "mahogany logs";
		case 1309:
			return "yew logs";
		case 1306:
			return "magic logs";
		}
		return "logs";
	}

	/**
	 * @param The
	 *            tree object id
	 * @return The log item id
	 */

	public static int getTreeLogs(int objectId) {
		switch (objectId) {
		case 2023:// achey
		case 3881:
		case 3879:
			return 2862;
		case 1281:// oak
			return 1521;
		case 5551:
		case 5552:
		case 1308:// willow
			return 1519;
		case 1307:// maple
			return 1517;
		case 9034:// mahogany
			return 6332;
		case 1309:// yew
			return 1515;
		case 1306:// magic
			return 1513;
		}
		return 1511;

	}

	/**
	 * @param The
	 *            tree object id
	 * @return The woodcutting level required
	 */
	public static int getTreeRequirement(int objectId) {
		switch (objectId) {
		case 1281:// oak
			return 15;
		case 1308:// willow
		case 5551:
		case 5552:
			return 30;
		case 1307:// maple
			return 45;
		case 9034:// mahogany
			return 50;
		case 1309:// yew
			return 60;
		case 1306:// magic
			return 75;
		}
		return 1;

	}

	/**
	 * @param The
	 *            tree object id
	 * @return The xp that tree provides
	 */

	public static int getTreeXp(int objectId) {
		switch (objectId) {
		case 1281:// oak
			return 350;
		case 1308:// willow
		case 5551:
		case 5552:
			return 450;
		case 9034:// mahogany
			return 550;
		case 1307:// maple
			return 650;
		case 1309:// yew
			return 750;
		case 1306:// magic
			return 950;
		}
		return 250;

	}

	/**
	 * @param player
	 * @param objectId
	 * @param axeId
	 */
	public static void giveLogs(Client player, int objectId, int axeId) {
		player.getFunction().closeAllWindows();
		player.lastChop = System.currentTimeMillis();
		player.sendMessage("You get some " + getLogName(objectId) + ".");
		Plugin.execute("receive_birds_nest", player);
		player.getItems().addItem(getTreeLogs(objectId), 1);
		player.startAnimation(getAxeAnimation(axeId));
		player.getFunction().addSkillXP(getTreeXp(objectId),
				player.playerWoodcutting);
		player.getFunction().refreshSkill(player.playerWoodcutting);
		player.stumpCount++;
	}

	/**
	 * @param The
	 *            players weapon
	 * @return Is it an axe?
	 */
	public static int hasAxe(Client player) {
		int woodcuttingLevel = player.playerLevel[player.playerWoodcutting];
		for (int element : AXES) {
			if (player.playerEquipment[player.playerWeapon] == element
					|| player.hasItem(element)
					&& Woodcutting.canUseAxe(element, woodcuttingLevel)) {
				return element;
			}
		}
		return -1;
	}

	/**
	 * @param player
	 * @param objectId
	 * @param objectX
	 * @param objectY
	 */
	public static void removeTree2(final Client player, final int objectId,
			final int objectX, final int objectY) {
		final int x = objectX;
		final int y = objectY;
		player.stumpCount = 0;
		World.getObjectHandler().globalObject(1341, x, y, player.absZ, 10);
		World.getSynchronizedTaskScheduler().schedule(new Task(15, false) {
			@Override
			protected void execute() {
				World.getObjectHandler().globalObject(objectId, x, y,
						player.absZ, 10);
				stop();
			}
		});
	}

	/**
	 * @param player
	 * @param objectId
	 */
	private static void startCuttingTree(final Client player,
			final int objectId, final int axeId) {
		if (System.currentTimeMillis() - player.lastChop < 3000) {
			player.isDoingSkill = false;
			return;
		}
		if (player.isDoingSkill) {
			player.isDoingSkill = false;
			return;
		}
		if (freeSlots(player) > 0) {
			player.originalX = player.getX();
			player.originalY = player.getY();
			player.originalZ = player.getZ();
			player.isDoingSkill = true;
			player.sendMessage("You swing your axe at the tree.");
			player.lastChop = System.currentTimeMillis();
			player.startAnimation(getAxeAnimation(axeId));
			World.getSynchronizedTaskScheduler().schedule(new Task(4, false) {
				@Override
				protected void execute() {
					if (player.isDoingSkill && freeSlots(player) > 0
							&& player.absX == player.originalX
							&& player.absY == player.originalY
							&& player.absZ == player.originalZ) {
						giveLogs(player, objectId, axeId);
						if (Misc.random(5) == 2) {
							World.getObjectManager().placeObject(
									new Object(-1, player.objectX,
											player.objectY, 0, 0, 10,
											player.objectId, 24));
							stop();
							player.isDoingSkill = false;
						}
					} else {
						player.isDoingSkill = false;
						stop();
					}
				}
			});
		} else {
			player.sendMessage("You don't have enough inventory space for anymore logs.");
			player.isDoingSkill = false;
		}
	}
}