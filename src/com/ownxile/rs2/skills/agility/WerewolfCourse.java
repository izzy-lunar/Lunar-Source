package com.ownxile.rs2.skills.agility;

import com.ownxile.rs2.Zone;
import com.ownxile.rs2.player.Client;
import com.ownxile.util.Misc;

public class WerewolfCourse extends AgilityCourse {

	@Override
	public void handleObjects(Client player) {
		if (player.objectY - player.absY > 2) {
			return;
		}
		switch (player.objectId) {
		case 5138:
			if (player.objectY > player.absY && player.absX > 3535
					|| player.objectX != player.absX
					&& player.objectY == player.absY && player.absX > 3535) {
				startMovement(player, player.objectX, player.objectY, 769, 10,
						"You attempt to jump to the stepping stone...",
						"...you make it onto the stone.");
			}
			break;
		case 5133:
		case 5135:
		case 5134:
			if (player.objectY > player.absY && player.absX > 3535) {
				startMovement(player, player.objectX, player.objectY + 1, 839,
						20, "You attempt to jump over the hurdle...",
						"...you succesfully jump over.");
			}
			break;

		case 5152:
			if (player.objectY > player.absY) {
				startMovement(player, player.objectX, player.objectY + 5, 2240,
						50, "You attempt to squeeze through the tunnel...",
						"...you succesfully squeeze through.");
				player.agilStage = 1;
			}
			break;
		case 5136:
			if (player.objectX < player.absX) {
				startMovement(player, player.objectX - 2, player.objectY, 828,
						30, "You attempt to climb up the rocks...",
						"...you succesfully climb up.");
			}
			break;
		case 5140:
		case 5139:
		case 5141:
			if (player.absX == 3528 || player.absX == 3530
					|| player.absX == 3529) {
				if (player.absY > 9898 && player.agilStage == 1) {
					startMovement(player, 3528, player.objectY - 42, 828, 120,
							"You climb onto the zipline...",
							"...and slide down to finish the course.");
					if (Misc.random(10) == 10) {
						player.addItem(4179);
					}
					player.agilStage = 0;
				} else {
					player.getFunction().punish("cheater faggot", 9);

				}
			}
			break;
		}
	}

	@Override
	public void setZone() {
		zone = new Zone(3520, 3545, 9867, 9915);

	}

}
