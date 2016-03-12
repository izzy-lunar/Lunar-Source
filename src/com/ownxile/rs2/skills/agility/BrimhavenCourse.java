package com.ownxile.rs2.skills.agility;

import com.ownxile.rs2.player.Client;

/**
 * @absX 2761
 * @absY 9546
 */
public class BrimhavenCourse extends AgilityCourse {

	/**
	 * @param player
	 * @param agility
	 */
	@Override
	public void handleObjects(Client player) {
		switch (player.objectId) {
		case 3578:

			startMovement(player, player.objectX, player.objectY, 769, 50,
					"You attempt to jump to the pillar...",
					"...you make it onto the pillar.");
			break;
		case 3559:
			if (player.absX == 2763 && player.absY == 9546) {
				player.getFunction().walkTo(7, 0);
			}

			break;
		case 3561:
			if (player.absX == 2770 && player.absY == 9546) {
				player.getFunction().walkTo(-7, 0);
			}

			break;
		}
	}

	@Override
	public void setZone() {

	}
}
