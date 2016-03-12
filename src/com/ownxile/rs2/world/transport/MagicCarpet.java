package com.ownxile.rs2.world.transport;

import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.player.Client;

public class MagicCarpet {

	private static final int RIDE_CARPET = 2261;

	public static final int[][] MAGIC_CARPET_DESTINATIONS = { {}, {}

	};

	public static final void travelTo(final Client player) {
		World.getSynchronizedTaskScheduler().schedule(new Task(4, false) {
			@Override
			protected void execute() {
				player.setForceMovement(0, 20, false, false, 30, 20, 2,
						RIDE_CARPET);
				stop();
			}
		});

	}
}
