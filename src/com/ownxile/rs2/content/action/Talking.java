package com.ownxile.rs2.content.action;

import com.ownxile.rs2.packets.Packet;
import com.ownxile.rs2.player.Client;

public class Talking implements Packet {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		c.setChatTextEffects(c.getInStream().readUnsignedByteS());
		c.setChatTextColor(c.getInStream().readUnsignedByteS());
		c.setChatTextSize((byte) (c.packetSize - 2));
		c.inStream.readBytes_reverseA(c.getChatText(), c.getChatTextSize(), 0);
		if (c.isMuted()) {
			c.getFunction().state("Muted accounts may not talk.");
			return;
		}
		c.setChatTextUpdateRequired(true);
	}
}