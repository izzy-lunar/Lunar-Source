package com.ownxile.rs2.packets.clicking;

import com.ownxile.rs2.packets.Packet;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.PlayerHandler;

/**
 * Clicking stuff (interfaces)
 **/
public class ClickingStuff implements Packet {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		if (c.inTrade) {
			if (!c.acceptedTrade) {
				c.getTrade().declineTrade();
			}
		}
		if (System.currentTimeMillis() - c.lastClick < 600) {
			return;
		}
		c.lastClick = System.currentTimeMillis();
		if (c.isBanking) {
			c.isBanking = false;
		}

		final Client o = (Client) PlayerHandler.players[c.duelingWith];
		if (o != null) {
			if (c.duelStatus >= 1 && c.duelStatus <= 4) {
				c.getDuel().declineDuel();
				o.getDuel().declineDuel();
			}
		}

		if (c.duelStatus == 6) {
			c.getDuel().claimStakedItems();
		}

	}

}
