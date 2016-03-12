package com.ownxile.rs2.content.action;

import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.Player;

public class Resting {

	static final int CYCLES_PER_INCREASE = 3, REST_ANIMATION = 11786,
			STAND_UP_ANIMATION = 11788, ENERGY_INCREASE = 4;

	public static void startResting(final Client player) {
		player.startAnimation(REST_ANIMATION);
		player.stopMovement();
		player.isResting = true;
		World.getSynchronizedTaskScheduler().schedule(
				new Task(CYCLES_PER_INCREASE, false) {
					@Override
					protected void execute() {
						if (player.isResting && player.runEnergy < 100) {
							player.runEnergy += ENERGY_INCREASE;
							player.getFunction().sendFrame126(
									"" + player.runEnergy, 149);
						} else {
							stop();
						}
					}
				});
	}

	public static void stopResting(final Player player) {
		player.startAnimation(STAND_UP_ANIMATION);
		player.stopMovement();
		World.getSynchronizedTaskScheduler().schedule(new Task(2, false) {
			@Override
			protected void execute() {
				player.isResting = false;
				stop();
			}
		});
	}

}
