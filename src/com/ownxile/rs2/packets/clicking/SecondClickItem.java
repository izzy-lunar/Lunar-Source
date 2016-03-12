package com.ownxile.rs2.packets.clicking;

import com.ownxile.core.Plugin;
import com.ownxile.rs2.content.click.ItemClickReaction;
import com.ownxile.rs2.packets.Packet;
import com.ownxile.rs2.player.Client;

public class SecondClickItem implements Packet {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		final int itemId = c.getInStream().readSignedWordA();
		if (!c.getItems().playerHasItem(itemId, 1)) {
			return;
		}
		if (System.currentTimeMillis() - c.lastClick < 600) {
			return;
		}
		c.lastClick = System.currentTimeMillis();
		if (!Plugin.execute("second_click_item_" + itemId, c)) {
			ItemClickReaction.executeClick(c, ItemClickReaction.SECOND_CLICK,
					itemId, -1);
		}
	}

}
