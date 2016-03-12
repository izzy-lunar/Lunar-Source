package com.ownxile.rs2.packets.option;

import com.ownxile.rs2.items.ItemOperation;
import com.ownxile.rs2.packets.Packet;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.world.games.PartyRoom;
import com.ownxile.rs2.world.shops.Shopping;

/**
 * Bank 5 Items
 **/
public class Option5 implements Packet {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		final int interfaceId = c.getInStream().readSignedWordBigEndianA();
		final int removeId = c.getInStream().readSignedWordBigEndianA();
		final int removeSlot = c.getInStream().readSignedWordBigEndian();

		switch (interfaceId) {

		case 1688:
			ItemOperation.operateItem(c, removeId);
			break;
		case 3900:
			Shopping.buyItem(c, removeId, removeSlot, 1);
			break;

		case 3823:
			if (!c.getItems().playerHasItem(removeId))
				return;
			Shopping.sellItem(c, removeId, removeSlot, 1);
			break;

		case 5064:
			if (!c.getItems().playerHasItem(removeId))
				return;
			if (c.isBanking) {
				c.getItems().bankItem(removeId, removeSlot, 5);
			} else {
				PartyRoom.depositItem(c, removeId, 5);
			}
			break;

		case 5382:
			c.getItems().fromBank(removeId, removeSlot, 5);
			break;

		case 3322:
			if (c.duelStatus <= 0) {
				c.getTrade().tradeItem(removeId, removeSlot, 5);
			} else {
				c.getDuel().stakeItem(removeId, removeSlot, 5);
			}
			break;

		case 3415:
			if (c.duelStatus <= 0) {
				c.getTrade().fromTrade(removeId, removeSlot, 5);
			}
			break;

		case 6669:
			c.getDuel().fromDuel(removeId, removeSlot, 5);
			break;

		case 1119:
		case 1120:
		case 1121:
		case 1122:
		case 1123:
			c.getSmithing().readInput(c.playerLevel[c.playerSmithing],
					Integer.toString(removeId), c, 5);
			break;

		}
	}

}
