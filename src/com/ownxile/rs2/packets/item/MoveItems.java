package com.ownxile.rs2.packets.item;

import com.ownxile.rs2.packets.Packet;
import com.ownxile.rs2.player.Client;

/**
 * Move Items
 **/
public class MoveItems implements Packet {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int interfaceId = c.getInStream().readUnsignedWordBigEndianA();
		c.insert = c.getInStream().readSignedByteC();
		int itemFrom = c.getInStream().readUnsignedWordBigEndianA();
		int itemTo = c.getInStream().readUnsignedWordBigEndian();

		// c.sendMessage("junk: " + somejunk);
		if (c.inTrade) {
			c.getTrade().declineTrade();
			return;
		}
		if (c.tradeStatus == 1) {
			c.getTrade().declineTrade();
			return;
		}
		if (c.duelStatus == 1) {
			c.getDuel().declineDuel();
			return;
		}

		// c.sendMessage("junk: " + somejunk);
		if (interfaceId == 5) {
			c.getItems().toTab(0, itemFrom);
			return;
		} else if (interfaceId == 13) {
			c.getItems().toTab(1, itemFrom);
			return;
		} else if (interfaceId == 26) {
			c.getItems().toTab(2, itemFrom);
			return;
		} else if (interfaceId == 39) {
			c.getItems().toTab(3, itemFrom);
			return;
		} else if (interfaceId == 52) {
			c.getItems().toTab(4, itemFrom);
			return;
		} else if (interfaceId == 65) {
			c.getItems().toTab(5, itemFrom);
			return;
		} else if (interfaceId == 78) {
			c.getItems().toTab(6, itemFrom);
			return;
		} else if (interfaceId == 91) {
			c.getItems().toTab(7, itemFrom);
			return;
		} else if (interfaceId == 104) {
			c.getItems().toTab(8, itemFrom);
			return;
		}
		c.getItems().moveItems(itemFrom, itemTo, interfaceId, c.insert);
	}
}
