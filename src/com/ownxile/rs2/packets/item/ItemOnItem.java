package com.ownxile.rs2.packets.item;

import com.ownxile.rs2.items.UseItem;
import com.ownxile.rs2.packets.Packet;
import com.ownxile.rs2.player.Client;

public class ItemOnItem implements Packet {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		final int usedWithSlot = c.getInStream().readUnsignedWord();
		final int itemUsedSlot = c.getInStream().readUnsignedWordA();
		final int useWith = c.playerItems[usedWithSlot] - 1;
		final int itemUsed = c.playerItems[itemUsedSlot] - 1;
		if (!c.getItems().playerHasItem(useWith, 1, usedWithSlot)
				|| !c.getItems().playerHasItem(itemUsed, 1, itemUsedSlot)) {
			return;
		}
		UseItem.itemonItem(c, itemUsed, useWith);
	}

}
