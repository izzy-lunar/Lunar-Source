package com.ownxile.rs2.packets.item;

import com.ownxile.rs2.items.GroundItemHandler;
import com.ownxile.rs2.packets.Packet;
import com.ownxile.rs2.player.Client;

public class ItemOnGroundItem implements Packet {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		// int a1 = c.getInStream().readSignedWordBigEndian();
		final int itemUsed = c.getInStream().readSignedWordA();
		final int groundItem = c.getInStream().readUnsignedWord();

		int gItemY = c.getInStream().readSignedWordA();
		int itemUsedSlot = c.getInStream().readSignedWordBigEndianA();
		int gItemX = c.getInStream().readUnsignedWord();

		if (System.currentTimeMillis() - c.lastClick < 600) {
			return;
		}
		if (!c.getItems().playerHasItem(itemUsed, 1, itemUsedSlot)) {
			return;
		}
		if (!GroundItemHandler.itemExists(groundItem, gItemX, gItemY)) {
			return;
		}
		c.lastClick = System.currentTimeMillis();
		c.getFiremaking().initialize(itemUsed, groundItem);
	}

}
