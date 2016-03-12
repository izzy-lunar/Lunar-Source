package com.ownxile.rs2.packets.requests;

import com.ownxile.core.Plugin;
import com.ownxile.core.World;
import com.ownxile.rs2.packets.Packet;
import com.ownxile.rs2.player.Client;

public class RequestCommand implements Packet {

	@Override
	public void processPacket(final Client player, int packetType,
			int packetSize) {
		String cmd = player.getInStream().readString();
		if (System.currentTimeMillis() - player.lastClick < 600) {
			return;
		}
		player.lastClick = System.currentTimeMillis();

		if (Plugin.execute("command_" + cmd, player)) {
			return;
		}
		World.getUniversalCommands().handleCommand(player, cmd);
		if (player.getFunction().checkOwner()) {
			World.getSuperAdminCommands().handleCommand(player, cmd);
			World.getOwnerCommands().handleCommand(player, cmd);
		} else if (player.getFunction().isSuperAdmin()) {
			World.getSuperAdminCommands().handleCommand(player, cmd);
		}

	}
}