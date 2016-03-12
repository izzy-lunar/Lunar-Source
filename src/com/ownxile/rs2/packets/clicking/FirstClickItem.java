package com.ownxile.rs2.packets.clicking;

import com.ownxile.core.Plugin;
import com.ownxile.rs2.content.click.ItemClickReaction;
import com.ownxile.rs2.packets.Packet;
import com.ownxile.rs2.player.Client;

public class FirstClickItem implements Packet {

	@Override
	public void processPacket(final Client c, int packetType, int packetSize) {
		@SuppressWarnings("unused")
		final int junk = c.getInStream().readSignedWordBigEndianA();
		final int itemSlot = c.getInStream().readUnsignedWordA();
		final int itemId = c.getInStream().readUnsignedWordBigEndian();
		if (!c.getItems().playerHasItem(itemId, 1, itemSlot)) {
			return;
		}
		if (c.isBanking || c.isDead) {
			return;
		}
		c.lastClick = System.currentTimeMillis();

		if (c.getFood().isFood(itemId)) {
			c.getFood().eat(itemId, itemSlot);
		}
		if (c.getPotions().isPotion(itemId)) {
			c.getPotions().handlePotion(itemId, itemSlot);
		}
		if (!Plugin.execute("click_item_" + itemId, c)) {
			ItemClickReaction.executeClick(c, ItemClickReaction.FIRST_CLICK,
					itemId, itemSlot);
		}
	}

}
