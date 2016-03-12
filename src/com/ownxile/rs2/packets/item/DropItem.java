package com.ownxile.rs2.packets.item;

import com.ownxile.config.GameConfig;
import com.ownxile.rs2.items.GroundItemHandler;
import com.ownxile.rs2.npcs.PetHandler;
import com.ownxile.rs2.packets.Packet;
import com.ownxile.rs2.player.Client;

public class DropItem implements Packet {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		final int itemId = c.getInStream().readUnsignedWordA();
		c.getInStream().readUnsignedByte();
		c.getInStream().readUnsignedByte();
		final int slot = c.getInStream().readUnsignedWordA();
		int amount = c.playerItemsN[slot];
		if (!c.getItems().playerHasItem(itemId, amount, slot)) {
			return;
		}
		if (System.currentTimeMillis() - c.lastClick < 600) {
			return;
		}
		if (c.inTrade) {
			c.sendMessage("You cannot drop items in the trade screen.");
			return;
		}
		if (c.arenas()) {
			c.sendMessage("You can't drop items inside the arena!");
			return;
		}
		if (c.isDoingSkill)
			return;
		if (PetHandler.spawnPet(c, itemId, slot, false)) {
			return;
		}
		boolean untradeable = false;
		for (final int i : GameConfig.UNTRADEABLE_ITEMS) {
			if (i == itemId) {
				untradeable = true;
				break;
			}
		}
		if (c.playerItemsN[slot] != 0 && itemId != -1
				&& c.playerItems[slot] == itemId + 1) {
			if (untradeable || c.playerRights > -1) {
				c.lastClick = System.currentTimeMillis();
				c.getFunction().destroyInterface(itemId, amount);
			} else {
				c.getItems().deleteItem(itemId, slot, amount);
				GroundItemHandler.createGroundItem(c, itemId, c.getX(),
						c.getY(), c.getZ(), amount, c.getId());
			}
		}

	}
}
