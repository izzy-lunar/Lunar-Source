package com.ownxile.rs2.world.transport;

import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.player.Client;

public class Sailing {

	/*
	 * Sailing to crandor could be used for sailing to fishing platform
	 */

	private static final int[][] TRAVEL_DATA = { {}, // 0 - Null
			{ 2834, 3335, 14 }, // 1 - From Port Sarim to Entrana
			{ 3048, 3234, 14 }, // 2 - From Entrana to Port Sarim
			{ 2853, 3237, 12 }, // 3 - From Port Sarim to Crandor
			{ 2834, 3335, 13 }, // 4 - From Crandor to Port Sarim
			{ 2956, 3146, 7 }, // 5 - From Port Sarim to Karajama
			{ 3029, 3217, 7 }, // 6 - From Karajama to Port Sarim
			{ 2772, 3234, 3 }, // 7 - From Ardougne to Brimhaven
			{ 3029, 3217, 3 }, // 8 - From Brimhaven to Ardougne
			{}, // 9 - Null
			{}, // 10 - Null
			{ 2998, 3043, 23 }, // 11 - From Port Khazard to Ship Yard
			{ 2676, 3170, 23 }, // 12 - From Ship Yard to Port Khazard
			{ 2998, 3043, 17 }, // 13 - From Cairn Island to Ship Yard
			{ 2659, 2676, 12 }, // 14 - From Port Sarim to Pest Control
			{ 3041, 3202, 12 }, // 15 - From Pest Control to Port Sarim
			{ 2763, 2956, 10 }, // 16 - To Cairn Isle from Feldip Hills
			{ 2763, 2956, 10 }, // 17 - To Feldip from Cairn
			{ 2403, 3781, 5 }, // 18 - Jatizco
			{ 2310, 3784, 5 }, // 19 - Neitiznot
			{ 2211, 3809, 5 }, // 20 - Pirates cove
			{ 2126, 3793, 5 }, // 21 - Lunar isle
	};

	public static int getTime(int i) {
		return TRAVEL_DATA[i][2];
	}

	public static int getX(int i) {
		return TRAVEL_DATA[i][0];
	}

	public static int getY(int i) {
		return TRAVEL_DATA[i][1];
	}

	public static void startTravel(final Client player, final int i) {
		player.getFunction().hideMap(true);
		player.getFunction().movePlayer(0, 0, player.getId());
		player.getFunction().showInterface(3281);
		player.getFunction().sendFrame36(75, i);
		World.getSynchronizedTaskScheduler().schedule(
				new Task(getTime(i) - 1, false) {
					@Override
					protected void execute() {
						player.fading = true;
						player.getFunction().movePlayer(getX(i), getY(i), 0);
						stop();

					}
				});

		World.getSynchronizedTaskScheduler().schedule(
				new Task(getTime(i), false) {
					@Override
					protected void execute() {
						player.getFunction().hideMap(false);
						player.getFunction().sendFrame36(75, -1);
						player.getFunction().closeAllWindows();
						stop();
					}
				});
	}

}
