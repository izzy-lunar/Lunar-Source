package com.ownxile.rs2.packets;

import com.ownxile.rs2.player.Client;

public interface Packet {

	public void processPacket(Client player, int packetType, int packetSize);

}
