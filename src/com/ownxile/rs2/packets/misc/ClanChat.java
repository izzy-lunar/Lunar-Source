package com.ownxile.rs2.packets.misc;

import com.ownxile.core.World;
import com.ownxile.rs2.packets.Packet;
import com.ownxile.rs2.player.Client;

/**
 * Chat
 **/
public class ClanChat implements Packet {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		String textSent = c.getFunction().longToPlayerName(
				c.getInStream().readQWord());
		textSent = textSent.replaceAll("_", " ");
		// c.sendMessage(textSent);
		World.getClanChat().joinClanChat(c, textSent);
	}
}
