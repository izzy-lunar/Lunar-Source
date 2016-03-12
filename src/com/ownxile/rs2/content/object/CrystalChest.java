package com.ownxile.rs2.content.object;

import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.world.games.TreasureTrails;
import com.ownxile.util.Misc;

public class CrystalChest {

	/*
	 * Integer to identify the key
	 */
	public static final int KEY = 989;

	/*
	 * Array to store the parts of the key
	 */
	public static final int[] KEY_HALVES = { 985, 987 };

	/*
	 * Integar to define the opening animation
	 */
	private static final int OPEN_ANIMATION = 881;

	/*
	 * A boolean to check if you can open the chest
	 */
	public static boolean canOpen(Client client) {
		return client.getItems().playerHasItem(KEY);
	}

	public static int loopHalf() {
		return KEY_HALVES[1];
	}

	/*
	 * Identifiers
	 */

	/*
	 * A method to replace the key halves with the key
	 */
	public static void makeKey(Client client) {
		if (client.getItems().playerHasItem(toothHalf(), 1)
				&& client.getItems().playerHasItem(loopHalf(), 1)) {
			client.getItems().deleteItem(toothHalf(), 1);
			client.getItems().deleteItem(loopHalf(), 1);
			client.getItems().addItem(KEY, 1);
		}
	}

	/*
	 * A event that resets the object and give the reward after 3 cycles
	 */
	public static void searchChest(final Client client, final int id,
			final int x, final int y) {
		if (!client.hasInventorySpace(4)) {
			client.sendMessage("You need at least 4 free inventory spaces to open the chest.");
			return;
		}
		if (canOpen(client)) {
			client.sendMessage("You unlock the chest with your key.");
			client.getItems().deleteItem(KEY, 1);
			client.startAnimation(OPEN_ANIMATION);
			client.getFunction().addObject(id + 1, x, y, client.absZ, 2, 10);
			World.getSynchronizedTaskScheduler().schedule(new Task(3, false) {
				@Override
				protected void execute() {
					client.getItems().addItem(995, Misc.random(8230));
					TreasureTrails.addReward(client, 2);
					client.sendMessage("You find some treasure in the chest.");
					client.getFunction()
							.addObject(id, x, y, client.absZ, 2, 10);
					stop();
				}
			});
		} else {
			client.sendMessage("The chest is locked.");
		}
	}

	public static int toothHalf() {
		return KEY_HALVES[0];
	}
}
