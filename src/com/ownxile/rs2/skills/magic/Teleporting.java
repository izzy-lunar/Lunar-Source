package com.ownxile.rs2.skills.magic;

import com.ownxile.rs2.player.Client;

public class Teleporting {

	public static void teleport(Client client, int x, int y, int z) {
		if (client.playerIndex > 0 || client.npcIndex > 0) {
			client.getCombat().resetPlayerAttack();
		}
		client.stopMovement();
		client.lastTeleport = System.currentTimeMillis();
		client.getFunction().removeAllWindows();
		client.npcIndex = 0;
		client.playerIndex = 0;
		client.faceUpdate(0);

		switch (client.playerMagicBook) {
		case 1:
			break;
		default:
			break;
		}
	}

}
