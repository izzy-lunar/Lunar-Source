package com.ownxile.rs2.packets.requests;

import com.ownxile.config.GameConfig;
import com.ownxile.rs2.packets.Packet;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.Player;
import com.ownxile.rs2.player.PlayerHandler;

public class RequestTrade implements Packet {

	private boolean canTrade(Player player) {
		return System.currentTimeMillis() - player.originalLoginTime < 60000 * GameConfig.NEW_PLAYER_WAIT;
	}

	@Override
	public void processPacket(Client player, int packetType, int packetSize) {
		final int tradeId = player.getInStream().readSignedWordBigEndian();
		player.getFunction().resetFollow();
		if (player.inTrade || PlayerHandler.players[tradeId].inTrade
				|| PlayerHandler.players[tradeId].jail == 1 || player.inWild()
				|| player.inDuelArena() || player.inFightPits()
				|| player.playerTitle == 0) {
			player.sendMessage("Other player is very busy at the moment.");
			return;
		}
		if (player.arenas()) {
			player.sendMessage("You can't trade inside the arena!");
			return;
		}
		if (player.isIronman() || player.isUltimateIronman()) {
			player.sendMessage("Ironman accounts cannot trade with other players.");
			return;
		}
		if (player.playerEquipment[player.playerRing] == 7927) {
			player.getFunction().say("Im an Egg - Not a market salesman!");
			return;
		}
		if (canTrade(player)) {
			player.sendMessage("You must have a minimum playing time of "
					+ GameConfig.NEW_PLAYER_WAIT + " minutes to trade.");
			return;
		}
		if (player.inCastleWars()) {
			return;
		}
		if (player.playerRights == 2 || player.playerRights == 4
				|| player.playerRights == 3) {
			player.sendMessage("Unable to complete trade request.");
			return;
		}
		if (tradeId != player.playerId) {
			player.getTrade().requestTrade(tradeId);
		}
	}

}
