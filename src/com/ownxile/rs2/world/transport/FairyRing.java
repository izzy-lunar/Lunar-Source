package com.ownxile.rs2.world.transport;

import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.player.Client;
import com.ownxile.util.Misc;

public class FairyRing {

	private static final int[][] RING_POSITIONS = { { 3092, 3492 },
			{ 3218, 3222 } };
	private static final int TELEPORT_ANIM = 2553;
	private static final int TELEPORT_GFX = 569;

	public static int getX(int i) {
		return RING_POSITIONS[i][0];
	}

	public static int getY(int i) {
		return RING_POSITIONS[i][1];
	}

	public static void randomTeleport(Client player) {
		int random = Misc.random(RING_POSITIONS.length - 1);
		player.getFunction().movePlayer(getX(random), getY(random), 0);
	}

	public static void ringTeleport(final Client player, final int x,
			final int y) {
		player.getFunction().walkTo2(player.objectX, player.objectY);
		player.getFunction().closeAllWindows();
		World.getSynchronizedTaskScheduler().schedule(new Task(2, false) {
			@Override
			protected void execute() {
				player.gfx100(TELEPORT_GFX);
				player.startAnimation(TELEPORT_ANIM);
				stop();

			}
		});
		World.getSynchronizedTaskScheduler().schedule(new Task(5, false) {
			@Override
			protected void execute() {
				player.getFunction().movePlayer(x, y, 0);
				stop();
			}
		});
	}

	public static void zanarisTeleport(final Client player, final int x,
			final int y) {
		player.gfx100(TELEPORT_GFX);
		World.getSynchronizedTaskScheduler().schedule(new Task(2, false) {
			@Override
			protected void execute() {
				player.startAnimation(TELEPORT_ANIM);
				stop();

			}
		});
		World.getSynchronizedTaskScheduler().schedule(new Task(5, false) {
			@Override
			protected void execute() {
				player.getFunction().movePlayer(x, y, 0);
				if (player.getQuest(14).getStage() == 3) {
					player.startChat(352080);
				}
				stop();
			}
		});
	}
}
