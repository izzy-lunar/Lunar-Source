package com.ownxile.rs2.world.transport;

import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.player.Client;

/*
 * @author Robbie <Roboyto>
 */

public class GnomeGlider {

	private static final int[][] GLIDER_DATA = { { 3058, 2848, 3497, 0, 1 }, // TO
																				// MOUNTAIN
			{ 3057, 2465, 3501, 3, 2 }, // TO GRAND TREE
			{ 3059, 3321, 3427, 0, 3 }, // TO CASTLE
			{ 3060, 3278, 3212, 0, 4 }, // TO DESERT
			{ 3056, 2802, 2705, 0, 8 }, // TO APE ATOL
			{ 48054, 2544, 2970, 0, 10 }, // TO OGRE AREA
	};

	public static int getButton(int i) {
		return GLIDER_DATA[i][0];
	}

	public static int getH(int i) {
		return GLIDER_DATA[i][3];
	}

	public static int getLength() {
		return GLIDER_DATA.length;
	}

	public static int getMove(int i) {
		return GLIDER_DATA[i][4];
	}

	public static int getX(int i) {
		return GLIDER_DATA[i][1];
	}

	public static int getY(int i) {
		return GLIDER_DATA[i][2];
	}

	public static void handleButtons(Client client, int button) {
		for (int i = 0; i < getLength(); i++) {
			if (getButton(i) == button) {
				handleFlight(client, i);
				client.flushOutStream();
			}
		}
	}

	public static void handleFlight(final Client client, final int flightId) {
		client.getFunction().sendFrame36(153, getMove(flightId));
		World.getSynchronizedTaskScheduler().schedule(new Task(1, true) {
			int i;

			@Override
			protected void execute() {
				i++;
				if (i == 3) {
					client.getFunction().movePlayer(getX(flightId),
							getY(flightId), getH(flightId));
				} else if (i == 4) {
					client.getFunction().closeAllWindows();
					client.getFunction().sendFrame36(153, -1);
					stop();
				}
			}
		});
	}

	@SuppressWarnings("unused")
	private static final void interfaceText(Client player) {
		player.getFunction().sendFrame126("Ta Quir Priw", 809);
		player.getFunction().sendFrame126("Lemanto Andra", 810);
		player.getFunction().sendFrame126("Sindarpos", 811);
		player.getFunction().sendFrame126("Gandius", 812);
		player.getFunction().sendFrame126("Kar-Hewo", 813);
		player.getFunction().sendFrame126("Lemantolly", 12338);
		player.getFunction().sendFrame126("Undri", 12339);
	}
}
