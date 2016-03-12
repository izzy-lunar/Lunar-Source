package com.ownxile.rs2.packets.requests;

import com.ownxile.rs2.combat.Attack;
import com.ownxile.rs2.combat.Cast;
import com.ownxile.rs2.packets.Packet;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.PlayerHandler;

public class RequestAttack implements Packet {

	public static final int ATTACK_PLAYER = 73, MAGE_PLAYER = 249;

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		c.playerIndex = 0;
		c.npcIndex = 0;
		if (c.isDead) {
			return;
		}
		if (c.inTrade) {
			return;
		}
		if (c.playerEquipment[c.playerRing] == 7927) {
			return;
		}
		switch (packetType) {
		case ATTACK_PLAYER:
			int index = c.getInStream().readSignedWordBigEndian();
			if (Attack.attackPlayer(c, index)) {
				c.turnPlayerTo(PlayerHandler.players[index].getX(),
						PlayerHandler.players[index].getY());
			}
			break;

		/**
		 * Attack player with magic
		 **/
		case MAGE_PLAYER:
			index = c.getInStream().readSignedWordA();
			if (Cast.castSpell(c, index)) {
				c.turnPlayerTo(PlayerHandler.players[index].getX(),
						PlayerHandler.players[index].getY());
			}
			break;

		}

	}

}
