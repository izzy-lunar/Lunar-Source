package com.ownxile.rs2.packets.item;

import com.ownxile.rs2.items.GroundItemHandler;
import com.ownxile.rs2.packets.Packet;
import com.ownxile.rs2.player.Client;

public class MagicOnFloorItems implements Packet {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int itemY = c.getInStream().readSignedWordBigEndian();
		int itemId = c.getInStream().readUnsignedWord();
		int itemX = c.getInStream().readSignedWordBigEndian();
		c.getInStream().readUnsignedWordA();
		if (!GroundItemHandler.itemExists(itemId, itemX, itemY)) {
			c.stopMovement();
			return;
		}
		if (c.playerRights == 3) {
			c.sendMessage("itemY: " + itemY + " itemX: " + itemX + " itemId: "
					+ itemId);/*
							 * if (!ItemHandler.itemExists(itemId, itemX,
							 * itemY)) { c.stopMovement(); return; }
							 * c.usingMagic = true; if
							 * (!c.getCombat().checkMagicReqs(51)) {
							 * c.stopMovement(); return; }
							 * 
							 * if (c.goodDistance(c.getX(), c.getY(), itemX,
							 * itemY, 12)) { int offY = (c.getX() - itemX) * -1;
							 * int offX = (c.getY() - itemY) * -1; c.teleGrabX =
							 * itemX; c.teleGrabY = itemY; c.teleGrabItem =
							 * itemId; c.turnPlayerTo(itemX, itemY);
							 * c.teleGrabDelay = System.currentTimeMillis();
							 * c.startAnimation(c.MAGIC_SPELLS[51][2]);
							 * c.gfx100(c.MAGIC_SPELLS[51][3]);
							 * c.getFunction().createPlayersStillGfx(144, itemX,
							 * itemY, 0, 72);
							 * c.getFunction().createPlayersProjectile(c.getX(),
							 * c.getY(), offX, offY, 50, 70,
							 * c.MAGIC_SPELLS[51][4], 50, 10, 0, 50);
							 * c.getFunction().addSkillXP(c.MAGIC_SPELLS[51][7],
							 * 6); c.getFunction().refreshSkill(6);
							 * c.stopMovement(); }
							 */
		}
	}

}
