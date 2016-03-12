package com.ownxile.rs2.packets.item;

import com.ownxile.rs2.packets.Packet;
import com.ownxile.rs2.player.Client;

public class ItemOnPlayer implements Packet {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		@SuppressWarnings("unused")
		final int playerId = c.inStream.readUnsignedWord();
		// final int itemId =
		// c.playerItems[c.inStream.readSignedWordBigEndian()] - 1;

	}

}
