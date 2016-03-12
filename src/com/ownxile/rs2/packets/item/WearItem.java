package com.ownxile.rs2.packets.item;

import com.ownxile.core.Plugin;
import com.ownxile.rs2.packets.Packet;
import com.ownxile.rs2.player.Client;

/**
 * Wear Item
 **/
public class WearItem implements Packet {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		c.wearId = c.getInStream().readUnsignedWord();
		c.wearSlot = c.getInStream().readUnsignedWordA();
		c.interfaceId = c.getInStream().readUnsignedWordA();
		/*
		 * if (c.playerIndex > 0 || c.npcIndex > 0) {
		 * c.getCombat().resetPlayerAttack(); }
		 */
		if (!c.getItems().playerHasItem(c.wearId, 1, c.wearSlot)) {
			return;
		}
		if (Plugin.execute("wear_item_" + c.wearId, c)) {
			return;
		}
		c.getItems().wearItem(c.wearId, c.wearSlot);
	}

}
