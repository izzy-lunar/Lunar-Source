package com.ownxile.rs2.packets.requests;

import com.ownxile.rs2.packets.Packet;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.PlayerHandler;

/**
 * Follow Player
 **/
public class FollowRequest implements Packet {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		final int followPlayer3 = c.getInStream().readUnsignedWordBigEndian();
		if (PlayerHandler.players[followPlayer3] == null) {
			return;
		}
		if (PlayerHandler.players[followPlayer3].absZ != c.absZ) {
			return;
		}
		if (c.inFightPits() || c.inFightPitsWait()) {
			return;
		}
		if (c.playerEquipment[c.playerRing] == 7927
				|| c.playerEquipment[c.playerRing] == 6583) {
			return;
		}
		if (c.inTrade || c.jail == 1) {
			return;
		}
		c.playerIndex = 0;
		c.npcIndex = 0;
		c.mageFollow = false;
		c.usingBow = false;
		c.usingRangeWeapon = false;
		c.followDistance = 1;
		c.followId3 = followPlayer3;
		/*
		 * if (c.absX > 2650 && c.absX < 2670 && c.absY > 2635 && c.absY < 2655)
		 * { return; } if (c.absX == 2432 || c.absX < 2433) { return; } if
		 * (c.absX > 2365 && c.absX < 2550 && c.absY > 5100 && c.absY < 5300) {
		 * return; } if (c.inCastleWars() ||
		 * PlayerHandler.players[followPlayer].inCastleWars()) { return; }
		 */
	}
}
