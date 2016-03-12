package com.ownxile.rs2.packets.clicking;

import com.ownxile.core.Plugin;
import com.ownxile.rs2.content.click.ItemClickReaction;
import com.ownxile.rs2.packets.Packet;
import com.ownxile.rs2.player.Client;

public class ThirdClickItem implements Packet {

	@SuppressWarnings("unused")
	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		final int itemId11 = c.getInStream().readSignedWordBigEndianA();
		final int itemId1 = c.getInStream().readSignedWordA();
		final int itemId = c.getInStream().readSignedWordA();
		if (!c.getItems().playerHasItem(itemId, 1)) {
			return;
		}
		if (System.currentTimeMillis() - c.lastClick < 600) {
			return;
		}
		c.lastClick = System.currentTimeMillis();
		if (!Plugin.execute("third_click_item_" + itemId, c)) {
			ItemClickReaction.executeClick(c, ItemClickReaction.THIRD_CLICK,
					itemId, -1);
		}

	}

}
