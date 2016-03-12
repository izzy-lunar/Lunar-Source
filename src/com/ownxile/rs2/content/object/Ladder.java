package com.ownxile.rs2.content.object;

import com.ownxile.core.Plugin;
import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.player.Client;

public class Ladder {

	public static final int[] LADDERS = {};

	public static boolean checkForPlugin(Client c, int objectId, int objectX,
			int objectY, int objectZ) {
		for (int element : LADDERS) {
			if (objectId == element) {
				if (Plugin.execute("ladder_" + objectX + "_" + objectY + "_"
						+ objectZ, c)) {
					return true;
				}
			}
		}
		return false;
	}

	public static void climb(final Client player, final int X, final int Y,
			final int destinationZ) {
		if (player.freezeTimer > 0) {
			player.sendMessage("You cannot use this object whilst frozen.");
			return;
		}
		if (!player.isClimbing
				&& System.currentTimeMillis() - player.lastLadder > 3000) {
			player.isClimbing = true;
			player.startAnimation(isGoingUp(Y, player.absY, destinationZ,
					player.absZ) ? 828 : 827);
			player.lastLadder = System.currentTimeMillis();
			World.getSynchronizedTaskScheduler().schedule(new Task(2, false) {
				@Override
				protected void execute() {
					player.isClimbing = false;
					player.getFunction().movePlayer(X, Y, destinationZ);
					stop();
				}
			});
		}

	}

	private static boolean isGoingUp(int toY, int fromY, int toZ, int fromZ) {
		if (toY < fromY) {
			return true;
		}
		if (toZ > fromZ) {
			return true;
		}
		return false;
	}

}