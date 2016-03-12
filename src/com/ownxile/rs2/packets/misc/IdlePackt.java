package com.ownxile.rs2.packets.misc;

import com.ownxile.rs2.packets.Packet;
import com.ownxile.rs2.player.Client;

public class IdlePackt implements Packet {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		if (c.underAttackBy < 1 || c.underAttackBy2 < 1) {
			// if (c.playerRights < 2)
			// c.logout();
		}
	}
}