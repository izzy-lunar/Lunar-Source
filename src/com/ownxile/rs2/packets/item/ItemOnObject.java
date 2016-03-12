package com.ownxile.rs2.packets.item;

import com.ownxile.core.Plugin;
import com.ownxile.rs2.items.UseItem;
import com.ownxile.rs2.packets.Packet;
import com.ownxile.rs2.player.Client;

public class ItemOnObject implements Packet {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {

		@SuppressWarnings("unused")
		final int a = c.getInStream().readUnsignedWord();
		final int objectId = c.getInStream().readSignedWordBigEndian();
		final int objectY = c.getInStream().readSignedWordBigEndianA();
		@SuppressWarnings("unused")
		final int b = c.getInStream().readUnsignedWord();
		final int objectX = c.getInStream().readSignedWordBigEndianA();
		final int itemId = c.getInStream().readUnsignedWord();
		if (!c.getItems().playerHasItem(itemId, 1)) {
			return;
		}
		if (Plugin.execute("item_" + itemId + "_on_object_" + objectId, c)) {
			return;
		}
		if (c.withinDistance(objectX, objectY, 4))
			UseItem.ItemonObject(c, objectId, objectX, objectY, itemId);

	}

}
