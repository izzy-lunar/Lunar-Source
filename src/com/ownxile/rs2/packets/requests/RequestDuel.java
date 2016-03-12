package com.ownxile.rs2.packets.requests;

import com.ownxile.rs2.packets.Packet;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.PlayerHandler;

/**
 * Challenge Player
 **/
public class RequestDuel implements Packet {

	@Override
	public void processPacket(Client e, int packetType, int packetSize) {
		switch (packetType) {
		case 128:
			final int answerPlayer = e.getInStream().readUnsignedWord();
			if (PlayerHandler.players[answerPlayer] == null)
				return;
			if (answerPlayer == e.playerId)
				return;
			if (e.connectedFrom
					.equalsIgnoreCase(PlayerHandler.players[answerPlayer].connectedFrom)) {
				e.sendMessage("You can'duel someone on the same address as you.");
				return;
			}
			if (e.arenas() || e.duelStatus == 5) {
				e.sendMessage("You can't challenge inside the arena!");
				return;
			}
			e.getDuel().requestDuel(answerPlayer);
			break;
		}
	}
}
