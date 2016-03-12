package com.ownxile.rs2.world.games;

import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.items.ItemAssistant;
import com.ownxile.rs2.npcs.NPC;
import com.ownxile.rs2.player.Client;

/**
 * @author Robbie <Roboyto>
 * @description The Warriors Guild
 */
public class WarriorsGuild {

	private static final int[][] ARMOUR_DATA = { { 1075, 1117, 1155, 4278, 5 },
			{ 1067, 1115, 1153, 4279, 7 }, { 1069, 1119, 1157, 4280, 10 },
			{ 1077, 1125, 1165, 4281, 15 }, { 1071, 1121, 1159, 4282, 15 },
			{ 1091, 1123, 1161, 4283, 17 }, { 1079, 1127, 1163, 4284, 20 } };

	public static boolean armourOnAnimator(final Client c, int itemId) {
		final int i = getSlot(itemId);
		if (isArmour(itemId, i) && hasSet(c, i)) {
			c.deleteItem(ARMOUR_DATA[i][0]);
			c.deleteItem(ARMOUR_DATA[i][1]);
			c.deleteItem(ARMOUR_DATA[i][2]);
			c.boxMessage("You use the set of armour on the animator...");
			c.startAnimation(827);
			World.getSynchronizedTaskScheduler().schedule(new Task(2, false) {
				NPC animatedArmour = null;
				int timer = 0;

				@Override
				protected void execute() {
					timer++;
					switch (timer) {
					case 1:
						animatedArmour = World.getNpcHandler().spawnNpc(c,
								ARMOUR_DATA[i][3], 2851, 3540, 0, 0,
								(ARMOUR_DATA[i][3] - 4277) * 20,
								(ARMOUR_DATA[i][3] - 4277) * 2,
								(ARMOUR_DATA[i][3] - 4277) * 15,
								(ARMOUR_DATA[i][3] - 4277) * 15, true, true);
						break;
					case 2:
						c.boxMessage("The armour springs to life!");
						animatedArmour.forceChat("I'M ALIVE!");
						break;
					case 3:
						stop();
						break;

					}
				}
			});
			return true;
		}
		return false;
	}

	public static int getDefender(Client entity) {
		if (entity.defenderDrop == 8851) {

			entity.lastClickedNpcId = 4289;
			entity.npcChat("Well done, you earned a Dragon defender!");
			return 8844;
		} else {
			entity.lastClickedNpcId = 4289;
			entity.npcChat("Well done, you earned a "
					+ ItemAssistant.getItemName(entity.defenderDrop) + "!");
			return entity.defenderDrop;
		}
	}

	public static int getSlot(int itemId) {
		for (int i = 0; i < ARMOUR_DATA.length; i++) {
			for (int t = 0; t < 3; t++) {
				if (ARMOUR_DATA[i][t] == itemId) {
					return i;
				}
			}
		}
		return -1;
	}

	public static int getTokenAmount(int npcId) {
		for (int[] element : ARMOUR_DATA) {
			if (element[3] == npcId) {
				return element[4];
			}
		}
		return -1;
	}

	private static boolean hasSet(Client c, int i) {
		return c.hasItem(ARMOUR_DATA[i][0]) && c.hasItem(ARMOUR_DATA[i][1])
				&& c.hasItem(ARMOUR_DATA[i][2]);
	}

	public static boolean isAnimated(int npcType) {
		for (int[] element : ARMOUR_DATA) {
			if (element[3] == npcType) {
				return true;
			}
		}
		return false;
	}

	public static boolean isArmour(int itemId, int i) {
		for (int t = 0; t < 3; t++) {
			if (ARMOUR_DATA[i][t] == itemId) {
				return true;
			}
		}
		return false;
	}
}
