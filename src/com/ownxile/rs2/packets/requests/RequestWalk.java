package com.ownxile.rs2.packets.requests;

import com.ownxile.config.GameConfig;
import com.ownxile.rs2.Point.Position;
import com.ownxile.rs2.content.action.Walking;
import com.ownxile.rs2.packets.Packet;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.PlayerHandler;

public class RequestWalk implements Packet {

	public boolean noclip = false;

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		long time = System.currentTimeMillis();
		if (packetType == 248 || packetType == 164) {
			c.faceUpdate(0);
			c.npcIndex = 0;
			c.playerIndex = 0;
			if (c.followId > 0 || c.followId2 > 0 || c.followId3 > 0) {
				c.getFunction().resetFollow();
			}
		}
		if (c.isBanking) {
			c.isBanking = false;
		}
		if (c.playerTitle == 0) {
			c.startChat(9845200);
			return;
		}
		if (c.nextChat > 0 && !c.cannotCloseWindows) {
			c.endChat();
		}
		if (!Walking.canWalk(c)) {
			return;
		}
		if (noclip) {
			noclip = false;
		}
		if (c.duelStatus <= 4 && c.duelStatus > 0) {
			final Client o = (Client) PlayerHandler.players[c.duelingWith];
			c.getDuel().declineDuel();
			o.getDuel().declineDuel();
			return;
		}
		if (c.isBanking) {
			c.sendMessage("Please use the �Close window� Option.");
			return;
		}
		if (c.duelRule[1] && c.duelStatus == 5) {
			if (PlayerHandler.players[c.duelingWith] != null) {
				if (!c.goodDistance(c.getX(), c.getY(),
						PlayerHandler.players[c.duelingWith].getX(),
						PlayerHandler.players[c.duelingWith].getY(), 1)
						|| c.attackTimer == 0) {
					c.sendMessage("Walking has been disabled in this duel!");
				}
			}
			c.playerIndex = 0;
			return;
		}
		if (c.freezeTimer > 0) {
			if (PlayerHandler.players[c.playerIndex] != null) {
				if (c.goodDistance(c.getX(), c.getY(),
						PlayerHandler.players[c.playerIndex].getX(),
						PlayerHandler.players[c.playerIndex].getY(), 1)
						&& packetType != 98) {
					c.playerIndex = 0;
					return;
				}
			}
			if (packetType != 98) {
				c.sendMessage("A magical force stops you from moving.");
				c.playerIndex = 0;
			}
			return;
		}
		if (c.inCastleWars() && c.absZ != 0) {
			if (c.isSkywalking()) {
				c.dying.respawn();
				return;
			}
		}
		if (c.playerEquipment[c.playerRing] == 7927) {
			c.getFunction()
					.state("You cannot move whilst wearing easter ring.");
			return;
		}
		if (c.playerEquipment[c.playerRing] == 6583) {
			c.getFunction().state(
					"You cannot move whilst wearing ring of stone.");
			return;
		}
		if (System.currentTimeMillis() - c.lastSpear < 4000) {
			c.sendMessage("You have been stunned.");
			c.playerIndex = 0;
			return;
		}

		if (c.jail == 1 && !c.inJailArea()) {
			c.move(Position.JAIL_SPAWN);
			return;
		}
		if (time - c.lastTeleport < 3900) {
			return;
		}
		if (time - c.lastAgility < 600) {
			return;
		}
		if (!c.isBanking) {
			c.getFunction().removeAllWindows();
		}
		/*
		 * if (c.isResting) { Resting.stopResting(c); return; }
		 */
		if (packetType == 98) {
			c.mageAllowed = true;
		}
		/*
		 * if((c.duelStatus >= 1 && c.duelStatus <= 4) || c.duelStatus == 6) {
		 * if(c.duelStatus == 6) { c.getTradeAndDuel().claimStakedItems(); }
		 * return; }
		 */

		if (c.inTrade) {
			c.getTrade().declineTrade();
		}
		if (packetType == 248) {
			packetSize -= 14;
		}

		c.isShopping = false;
		c.newWalkCmdSteps = (packetSize - 5) / 2;
		if (++c.newWalkCmdSteps > GameConfig.WALK_QUEUE_SIZE) {
			c.newWalkCmdSteps = 0;
			return;
		}
		c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;
		final int firstStepX = c.getInStream().readSignedWordBigEndianA()
				- c.getMapRegionX() * 8;
		for (int i = 1; i < c.newWalkCmdSteps; i++) {
			c.getNewWalkCmdX()[i] = c.getInStream().readSignedByte();
			c.getNewWalkCmdY()[i] = c.getInStream().readSignedByte();
		}

		final int firstStepY = c.getInStream().readSignedWordBigEndian()
				- c.getMapRegionY() * 8;
		c.setNewWalkCmdIsRunning(c.getInStream().readSignedByteC() == 1);
		for (int i1 = 0; i1 < c.newWalkCmdSteps; i1++) {
			c.getNewWalkCmdX()[i1] += firstStepX;
			c.getNewWalkCmdY()[i1] += firstStepY;
		}
	}

}
