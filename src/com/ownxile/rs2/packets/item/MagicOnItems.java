package com.ownxile.rs2.packets.item;

import com.ownxile.core.Plugin;
import com.ownxile.rs2.packets.Packet;
import com.ownxile.rs2.player.Client;

/**
 * Magic on items
 **/
public class MagicOnItems implements Packet {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		final int slot = c.getInStream().readSignedWord();
		final int itemId = c.getInStream().readSignedWordA();
		c.getInStream().readSignedWord();
		final int spellId = c.getInStream().readSignedWordA();

		c.usingMagic = true;
		if (!c.getItems().playerHasItem(itemId, 1, slot))
			return;
		if (Plugin.execute("cast_spell_" + spellId + "_on_" + itemId, c, slot)) {
			return;
		}
		c.getFunction().magicOnItems(slot, itemId, spellId);
		c.usingMagic = false;

	}

}
