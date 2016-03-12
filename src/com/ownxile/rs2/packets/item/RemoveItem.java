package com.ownxile.rs2.packets.item;

import com.ownxile.rs2.packets.Packet;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.world.games.PartyRoom;
import com.ownxile.rs2.world.shops.Shopping;

/**
 * Remove Item
 **/
public class RemoveItem implements Packet {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		final int interfaceId = c.getInStream().readUnsignedWordA();
		final int removeSlot = c.getInStream().readUnsignedWordA();
		final int removeId = c.getInStream().readUnsignedWordA();
		switch (interfaceId) {

		case 1688:
			c.getItems().removeItem(removeId, removeSlot);
			break;

		case 5064:
			if (!c.getItems().playerHasItem(removeId))
				return;
			if (c.isBanking) {
				c.getItems().bankItem(removeId, removeSlot, 1);
			} else {
				PartyRoom.depositItem(c, removeId, 1);
			}
			break;

		case 5382:
			c.getItems().fromBank(removeId, removeSlot, 1);
			break;

		case 3900:
			Shopping.valueShopItem(c, removeId, removeSlot);
			break;

		case 3823:
			Shopping.valueInventoryItem(c, removeId, removeSlot);
			break;

		case 3322:
			if (!c.getItems().playerHasItem(removeId, 1, removeSlot))
				return;
			if (c.duelStatus <= 0) {
				c.getTrade().tradeItem(removeId, removeSlot, 1);
			} else {
				c.getDuel().stakeItem(removeId, removeSlot, 1);
			}
			break;

		case 3415:
			if (c.duelStatus <= 0) {
				c.getTrade().fromTrade(removeId, removeSlot, 1);
			}
			break;

		case 6669:
			c.getDuel().fromDuel(removeId, removeSlot, 1);
			break;

		case 1119:
		case 1120:
		case 1121:
		case 1122:
		case 1123:
			c.getSmithing().readInput(c.playerLevel[c.playerSmithing],
					Integer.toString(removeId), c, 1);
			break;
		}
	}

}
