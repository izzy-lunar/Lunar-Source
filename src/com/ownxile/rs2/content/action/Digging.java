package com.ownxile.rs2.content.action;

import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.items.ItemAssistant;
import com.ownxile.rs2.player.Client;

public class Digging {

	private static final int[][] DIG_DATA = { { 8070, 3093, 3483 },
			{ 1925, 3222, 3218 }, { 1923, 3092, 3492 }, { 1917, 3300, 3300 } };

	public static void findItem(final Client player, final int itemId) {
		if (System.currentTimeMillis() - player.lastDig > 10500) {
			if (!player.getItems().playerHasItem(itemId)) {
				player.lastDig = System.currentTimeMillis();
				World.getSynchronizedTaskScheduler().schedule(
						new Task(2, false) {
							@Override
							protected void execute() {
								player.getItems().addItem(itemId, 1);
								player.getItems();
								player.boxMessage("You find a "
										+ ItemAssistant.getItemName(itemId)
										+ ".");

								stop();
							}
						});
			}
		}
	}

	public static int getItemId(int i) {
		return DIG_DATA[i][0];
	}

	public static int getX(int i) {
		return DIG_DATA[i][1];
	}

	public static int getY(int i) {
		return DIG_DATA[i][2];
	}

	public static boolean handleDigging(Client client) {
		for (int i = 0; i < DIG_DATA.length; i++) {
			if (client.getX() == getX(i) && client.getY() == getY(i)) {
				findItem(client, getItemId(i));
				return true;
			}
		}
		return false;
	}
}
