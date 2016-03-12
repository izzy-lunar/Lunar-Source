package com.ownxile.rs2.packets.misc;

import com.ownxile.rs2.packets.Packet;
import com.ownxile.rs2.player.Client;

public class Dialogue implements Packet {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		if (c.nextChat > 0) {
			c.getChat().sendChat(c.nextChat, c.talkingNpc);
		} else {
			c.getChat().sendChat(0, -1);
		}

	}

}
