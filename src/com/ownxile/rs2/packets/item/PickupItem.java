package com.ownxile.rs2.packets.item;

import com.ownxile.rs2.items.GroundItemHandler;
import com.ownxile.rs2.packets.Packet;
import com.ownxile.rs2.player.Client;

/**
 * Pickup Item
 **/
public class PickupItem implements Packet {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		c.pItemY = c.getInStream().readSignedWordBigEndian();
		c.pItemId = c.getInStream().readUnsignedWord();
		c.pItemX = c.getInStream().readSignedWordBigEndian();
		if (Math.abs(c.getX() - c.pItemX) > 25
				|| Math.abs(c.getY() - c.pItemY) > 25) {
			c.resetWalkingQueue();
			return;
		}
		if (c.isDead) {
			return;
		}
		if (!GroundItemHandler.itemExists(c.pItemId, c.pItemX, c.pItemY)) {
			c.stopMovement();
			return;
		}
		c.getCombat().resetPlayerAttack();
		if (c.getX() == c.pItemX && c.getY() == c.pItemY) {
			GroundItemHandler.removeGroundItem(c, c.pItemId, c.pItemX,
					c.pItemY, true);
		} else {
			c.walkingToItem = true;
		}

	}

}
