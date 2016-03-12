package com.ownxile.rs2.skills.agility;

import com.ownxile.rs2.Zone;
import com.ownxile.rs2.player.Client;

public class WildernessCourse extends AgilityCourse {

	@Override
	public void handleObjects(Client player) {
		switch (player.objectId) {
		case 2288:
			if (player.objectY > player.absY
					&& player.objectY - player.absY < 2) {
				startMovement(player, player.objectX, player.objectY + 12,
						2240, 50,
						"You attempt to squeeze through the tunnel...",
						"...you succesfully squeeze through.");
			}
			break;
		case 2283:// ropeswing
			if (player.objectY + 2 > player.absY
					&& player.objectY - player.absY < 3) {
				startMovement(player, player.objectX, player.objectY + 6, 828,
						30, "You climb up onto the rope swing...",
						"...you succesfully travel across.");
			}
			break;

		case 2311:// stepping stone
			if (settingZone.inZone(player)) {
				startMovement(player, player.objectX - 5, player.objectY, 769,
						30, "You attempt to jump to the stepping stone...",
						"...you make it across the stepping stones.");
			}
			break;

		case 2297:// log balance
			if (player.absY < 3946 && player.absX > 3000 && player.absX < 3003
					&& player.absY > 3000)
				startMovement(player, player.objectX - 7, player.objectY, 769,
						30, "You attempt to balance along the log...",
						"...you make it across the log.");
			player.agilStage = 1;

			break;

		case 2328:// climb rocks

			if (rockZone.inZone(player) && player.agilStage == 1) {
				startMovement(player, player.objectX, player.objectY - 2, 828,
						30, "You attempt to climb up the rocks...",
						"...you succesfully climb up.");

				player.agilStage = 0;
			}
			break;
		}

	}

	private static Zone rockZone = null;
	private static Zone settingZone = null;

	@Override
	public void setZone() {
		zone = new Zone(2987, 3008, 3931, 3968);
		settingZone = new Zone(3002, 3004, 3959, 3961);
		rockZone = new Zone(2992, 2997, 3937, 3939);

	}

}
