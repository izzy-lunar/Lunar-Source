package com.ownxile.rs2.world.transport;

import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.player.Client;

public class KaramjaCart {

	public static final void travel(final Client player, final int x,
			final int y) {
		player.getFunction().showInterface(18460);
		player.getFunction().hideMap(true);
		player.fading = true;
		World.getSynchronizedTaskScheduler().schedule(new Task(3, false) {
			@Override
			protected void execute() {
				player.getFunction().movePlayer(x, y, 0);
				stop();
			}
		});
		World.getSynchronizedTaskScheduler().schedule(new Task(4, false) {
			@Override
			protected void execute() {
				player.getFunction().showInterface(18452);
				stop();
			}
		});
		World.getSynchronizedTaskScheduler().schedule(new Task(7, false) {
			@Override
			protected void execute() {
				player.getFunction().closeAllWindows();
				player.getFunction().hideMap(false);
				stop();
			}
		});

	}

	public static final void travel(final Client player, final int x,
			final int y, final int z) {
		player.getFunction().showInterface(18460);
		player.getFunction().hideMap(true);
		player.fading = true;
		World.getSynchronizedTaskScheduler().schedule(new Task(3, false) {
			@Override
			protected void execute() {
				player.getFunction().movePlayer(x, y, z);
				stop();
			}
		});
		World.getSynchronizedTaskScheduler().schedule(new Task(4, false) {
			@Override
			protected void execute() {
				player.getFunction().showInterface(18452);
				stop();
			}
		});
		World.getSynchronizedTaskScheduler().schedule(new Task(7, false) {
			@Override
			protected void execute() {
				player.getFunction().closeAllWindows();
				player.getFunction().hideMap(false);
				stop();
			}
		});

	}

}
