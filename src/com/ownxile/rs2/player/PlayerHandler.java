package com.ownxile.rs2.player;

import java.net.InetSocketAddress;

import com.ownxile.config.GameConfig;
import com.ownxile.core.World;
import com.ownxile.core.World.WorldStatus;
import com.ownxile.rs2.npcs.NPCHandler;
import com.ownxile.util.Misc;
import com.ownxile.util.Stream;

public class PlayerHandler {

	public static Player players[] = new Player[GameConfig.MAX_PLAYERS];
	public static boolean updateAnnounced;
	public static boolean updateRunning;
	public static int updateSeconds;
	public static long updateStartTime;

	static {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				for (int i = 0; i < GameConfig.MAX_PLAYERS; i++) {
					if (players[i] != null) {
						PlayerSave.saveGame((Client) players[i]);
					}
				}
			}
		});
	}

	public static void disconnectAddress(String address) {
		for (int i = 0; i < GameConfig.MAX_PLAYERS; i++) {
			if (players[i] != null) {
				if (players[i].connectedFrom.equalsIgnoreCase(address)) {
					players[i].disconnected = true;
				}
			}
		}
	}

	public static Player findPlayer(String name) {
		for (Player player : players) {
			if (player == null) {
				continue;
			}
			if (player.playerName.equalsIgnoreCase(name)) {
				return player;
			}
		}
		return null;
	}

	public static int getAddressConnectionCount(String address) {
		int hits = 0;
		for (int i = 0; i < GameConfig.MAX_PLAYERS; i++) {
			if (players[i] != null) {
				if (players[i].connectedFrom.equalsIgnoreCase(address)) {
					hits++;
				}
			}
		}
		return hits;
	}

	public static int getPlayerCount() {
		double i = 1.6;
		for (Player player : players) {
			if (player != null) {
				i++;
			}
		}
		return (int) i;
	}

	public static boolean isPlayerOn(String playerName) {
		for (int i = 0; i < GameConfig.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				if (PlayerHandler.players[i].playerName
						.equalsIgnoreCase(playerName)) {
					return true;
				}
			}
		}
		return false;
	}

	private long time;

	private final Stream updateBlock = new Stream(
			new byte[GameConfig.BUFFER_SIZE]);

	private void catchDisconnect(int i) {
		if (players[i].inTrade) {
			final Client o = (Client) PlayerHandler.players[players[i].tradeWith];
			if (o != null) {
				o.getTrade().declineTrade();
			}
		}
		if (players[i].duelStatus == 5) {
			final Client o1 = (Client) PlayerHandler.players[players[i].duelingWith];
			if (o1 != null) {
				o1.getDuel().duelVictory();
			}
		} else if (players[i].duelStatus <= 4 && players[i].duelStatus >= 1) {
			final Client o1 = (Client) PlayerHandler.players[players[i].duelingWith];
			if (o1 != null) {
				o1.getDuel().declineDuel();
			}
		}
		PlayerSave.saveGame((Client) PlayerHandler.players[i]);
	}

	public int getFreeSlot() {
		for (int i = 1; i < GameConfig.MAX_PLAYERS; i++) {
			if (players[i] == null) {
				return i;
			}
		}
		return -1;
	}

	public Player getPlayerByName(String name) {
		name = name.toLowerCase();
		for (final Player p : players) {
			if (p != null && p.playerName.toLowerCase().equals(name)) {
				return p;
			}
		}
		return null;
	}

	public Player getRandomPlayer() {
		return players[Misc.random(PlayerHandler.getPlayerCount())];
	}

	private void handlePlayerUpdating(Player player) {
		player.preProcessing();
		while (player.processQueuedPackets()) {
			;
		}
		player.process();
		player.postProcessing();
		player.getNextPlayerMovement();

	}

	public void kickAll() {
		for (int i = 1; i < GameConfig.MAX_PLAYERS; i++) {
			if (players[i] != null) {
				players[i].disconnected = true;
			}
		}
	}

	public boolean newPlayerClient(Client client1, int slot) {
		if (slot == -1) {
			return false;
		}
		client1.handler = this;
		client1.playerId = slot;
		players[slot] = client1;
		players[slot].isActive = true;
		// players[slot].hostname = ((InetSocketAddress) client1.getSession()
		// .getRemoteAddress()).getAddress().getHostName();

		players[slot].connectedFrom = ((InetSocketAddress) client1.getSession()
				.getRemoteAddress()).getAddress().getHostAddress();
		if (GameConfig.SERVER_DEBUG) {
			Misc.println("Player Slot " + slot + " slot 0 " + players[0]
					+ " Player Hit " + players[slot]);
		}
		return true;
	}

	public void removePlayer(final Player plr) {
		if (plr.privChat != 2) {
			for (int i = 1; i < GameConfig.MAX_PLAYERS; i++) {
				if (players[i] == null || players[i].isActive == false) {
					continue;
				}
				final Client o = (Client) PlayerHandler.players[i];
				if (o != null) {
					o.getFunction().updatePM(plr.playerId, 0);
				}
			}
		}
		plr.destruct();
	}

	public void tick() {
		time = System.currentTimeMillis();
		if (updateRunning && !updateAnnounced) {
			updateAnnounced = true;
			World.setStatus(WorldStatus.UPDATING);
		}
		if (updateRunning && time - updateStartTime > updateSeconds * 1000) {
			kickAll();
		}
		for (int i = 0; i < GameConfig.MAX_PLAYERS; i++) {
			if (players[i] == null || !players[i].isActive) {
				continue;
			}
			try {
				if (players[i].disconnected
						&& time - players[i].logoutDelay > 10000
						|| players[i].properLogout) {
					catchDisconnect(i);
					removePlayer(players[i]);
					players[i] = null;
					continue;
				}
				handlePlayerUpdating(players[i]);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i < GameConfig.MAX_PLAYERS; i++) {
			if (players[i] == null || !players[i].isActive) {
				continue;
			}
			try {
				if (players[i].disconnected
						&& time - players[i].logoutDelay > 10000
						|| players[i].properLogout) {
					catchDisconnect(i);
					removePlayer(players[i]);
					players[i] = null;
					continue;
				}
				if (!players[i].initialized) {
					players[i].initialize();
					players[i].initialized = true;
				} else {
					players[i].update();
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i < GameConfig.MAX_PLAYERS; i++) {
			if (players[i] == null || !players[i].isActive) {
				continue;
			}
			try {
				players[i].clearUpdateFlags();
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void updateNPC(Player plr, Stream str) {
		synchronized (plr) {
			updateBlock.currentOffset = 0;

			str.createFrameVarSizeWord(65);
			str.initBitAccess();

			str.writeBits(8, plr.npcListSize);
			final int size = plr.npcListSize;
			plr.npcListSize = 0;
			for (int i = 0; i < size; i++) {
				if (plr.RebuildNPCList == false
						&& plr.withinDistance(plr.npcList[i]) == true) {
					plr.npcList[i].updateNPCMovement(str);
					plr.npcList[i].appendNPCUpdateBlock(updateBlock);
					plr.npcList[plr.npcListSize++] = plr.npcList[i];
				} else {
					final int id = plr.npcList[i].npcId;
					plr.npcInListBitmap[id >> 3] &= ~(1 << (id & 7));
					str.writeBits(1, 1);
					str.writeBits(2, 3);
				}
			}

			for (int i = 0; i < NPCHandler.MAX_VALUE; i++) {
				if (NPCHandler.npcs[i] != null) {
					final int id = NPCHandler.npcs[i].npcId;
					if (plr.RebuildNPCList == false
							&& (plr.npcInListBitmap[id >> 3] & 1 << (id & 7)) != 0) {

					} else if (plr.withinDistance(NPCHandler.npcs[i]) == true) {
						plr.addNewNPC(NPCHandler.npcs[i], str, updateBlock);
					}
				}
			}
			plr.RebuildNPCList = false;
			if (updateBlock.currentOffset > 0) {
				str.writeBits(14, 16383);
				str.finishBitAccess();
				str.writeBytes(updateBlock.buffer, updateBlock.currentOffset, 0);
			} else {
				str.finishBitAccess();
			}
			str.endFrameVarSizeWord();
		}
	}

	public void updatePlayer(Player plr, Stream str) {
		synchronized (plr) {
			updateBlock.currentOffset = 0;
			if (updateRunning && !updateAnnounced) {
				str.createFrame(114);
				str.writeWordBigEndian(updateSeconds * 50 / 30);
			}
			plr.updateThisPlayerMovement(str);
			final boolean saveChatTextUpdate = plr.isChatTextUpdateRequired();
			plr.setChatTextUpdateRequired(false);
			plr.appendPlayerUpdateBlock(updateBlock);
			plr.setChatTextUpdateRequired(saveChatTextUpdate);
			str.writeBits(8, plr.playerListSize);
			final int size = plr.playerListSize;
			plr.playerListSize = 0;
			for (int i = 0; i < size; i++) {
				if (!plr.didTeleport && !plr.playerList[i].didTeleport
						&& plr.withinDistance(plr.playerList[i])
						&& str.currentOffset + updateBlock.currentOffset < 4800) {
					plr.playerList[i].updatePlayerMovement(str);
					plr.playerList[i].appendPlayerUpdateBlock(updateBlock);
					plr.playerList[plr.playerListSize++] = plr.playerList[i];
				} else {
					final int id = plr.playerList[i].playerId;
					plr.playerInListBitmap[id >> 3] &= ~(1 << (id & 7));
					str.writeBits(1, 1);
					str.writeBits(2, 3);
				}
			}
			int added = 0;
			for (int i = 0; i < GameConfig.MAX_PLAYERS; i++) {
				if (str.currentOffset + updateBlock.currentOffset >= 4800) {
					break;
				}
				if (players[i] == null || !players[i].isActive
						|| players[i] == plr) {
					continue;
				}
				final int id = players[i].playerId;
				if ((plr.playerInListBitmap[id >> 3] & 1 << (id & 7)) != 0) {
					continue;
				}
				if (!plr.withinDistance(players[i])) {
					continue;
				}
				if (added++ >= 10) {
					break;
				}
				plr.addNewPlayer(players[i], str, updateBlock);
			}
			if (updateBlock.currentOffset > 0) {
				str.writeBits(11, 2047);
				str.finishBitAccess();
				str.writeBytes(updateBlock.buffer, updateBlock.currentOffset, 0);
			} else {
				str.finishBitAccess();
			}

			str.endFrameVarSizeWord();
		}
	}

	public void yell(String name, int rights, String message) {
		for (final Player player : players) {
			if (player == null) {
				continue;
			}
			final Client client = (Client) player;
			client.getFunction().sendClan(name, Misc.optimizeText(message),
					"Yell", rights);
		}
	}

}
