package com.ownxile.rs2.packets.option;

import com.ownxile.rs2.packets.Packet;
import com.ownxile.rs2.player.Client;

/**
 * Fixed X amount packet - banking, trading etc
 **/
public class OptionX2 implements Packet {
	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int Xamount = c.getInStream().readDWord();
		if (Xamount < 0)// this should work fine
		{
			Xamount = c.getItems().getItemAmount(c.xRemoveId);
		}
		if (Xamount == 0) {
			Xamount = 1;
		}
		switch (c.xInterfaceId) {
		case 5064:
			if (!c.getItems().playerHasItem(c.playerItems[c.xRemoveSlot],
					Xamount))
				return;
			c.getItems().bankItem(c.playerItems[c.xRemoveSlot], c.xRemoveSlot,
					Xamount);
			break;

		case 5382:
			c.getItems().fromBank(c.bankItems[c.xRemoveSlot], c.xRemoveSlot,
					Xamount);
			break;

		case 3322:
			if (!c.getItems().playerHasItem(c.xRemoveId, Xamount)) {
				return;
			}
			if (c.duelStatus <= 0) {
				c.getTrade().tradeItem(c.xRemoveId, c.xRemoveSlot, Xamount);
			} else {
				c.getDuel().stakeItem(c.xRemoveId, c.xRemoveSlot, Xamount);
			}
			break;

		case 3415:
			if (!c.getItems().playerHasItem(c.xRemoveId, Xamount)) {
				return;
			}
			if (c.duelStatus <= 0) {
				c.getTrade().fromTrade(c.xRemoveId, c.xRemoveSlot, Xamount);
			}
			break;

		case 6669:
			if (!c.getItems().playerHasItem(c.xRemoveId, Xamount)) {
				return;
			}
			c.getDuel().fromDuel(c.xRemoveId, c.xRemoveSlot, Xamount);
			break;
		}
	}
}