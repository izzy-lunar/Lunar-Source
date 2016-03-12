package com.ownxile.rs2.world.games;

import java.util.HashMap;
import java.util.Iterator;

import com.ownxile.core.World;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.Player;
import com.ownxile.rs2.player.PlayerHandler;
import com.ownxile.util.Misc;

public class CastleWars {

	private static final int[][] COLLAPSE_ROCKS = { { 2399, 2402, 9511, 9514 },
			{ 2391, 2392, 9501, 9502 }, { 2400, 2403, 9493, 9496 },
			{ 2408, 2411, 9502, 9505 } };
	private static final int[][] GAME_ROOM = { { 2426, 3076 }, { 2372, 3131 } };
	private static final int GAME_TIMER = 1000;
	private static HashMap<Client, Integer> gameRoom = new HashMap<Client, Integer>();

	private static int[] gameScores = { 0, 0 };
	private static int gameTimer = -1;

	private static final int PLAYERS_REQUIRED = 4;
	public static final int SARA = 1;
	public static final int SARA_BANNER = 4037;

	public static final int SARA_CAPE = 4041;
	public static final boolean SARA_DOOR_LOCKED = false;
	public static boolean saraFlagGrounded = false;
	public static final int TICKET = 4067;
	private static final int[][] WAIT_ROOM = { { 2377, 9485 }, { 2421, 9524 } };
	private static final int WAIT_TIMER = 300;
	private static HashMap<Client, Integer> waitRoom = new HashMap<Client, Integer>();
	private static int waitTimer = WAIT_TIMER;
	public static final int ZAMMY = 2;
	public static final int ZAMMY_BANNER = 4039;
	public static final int ZAMMY_CAPE = 4042;
	public static final boolean ZAMMY_DOOR_LOCKED = false;
	public static boolean zammyFlagGrounded = false;
	public static int zamorakWins, zammyFlag, saradominWins, saraFlag;

	public static void addFlag(Client player, int flagId) {
		if (!player.isDead) {
			player.playerEquipment[player.playerWeapon] = flagId;
			player.playerEquipmentN[player.playerWeapon] = 1;
			player.getItems().updateSlot(player.playerWeapon);
			player.getUpdateFlags().appearanceUpdateRequired = true;
			player.updateRequired = true;
		}
	}

	public static void addHat(Client player, int capeId) {
		player.playerEquipment[player.playerHat] = capeId;
		player.playerEquipmentN[player.playerHat] = 1;
		player.getItems().updateSlot(player.playerHat);
		player.getUpdateFlags().appearanceUpdateRequired = true;
		player.updateRequired = true;
	}

	public static void addTickets(Client player, int amount) {
		if (player.hasInventorySpace(1)) {
			player.getItems().addItem(TICKET, amount);
		} else {
			player.getItems().addBankItem(TICKET, amount);
			player.sendMessage("@dre@You receive " + amount
					+ " castle wars tickets in your bank.");
		}
	}

	public static void addToWaitRoom(Client player, int team) {
		if (player == null) {
			return;
		} else if (player.playerEquipment[player.playerHat] > 0
				|| player.playerEquipment[player.playerCape] > 0) {
			player.sendMessage("You must remove any headwear or cape before entering.");
			return;
		}
		toWaitingRoom(player, team);
	}

	public static void captureFlag(Client player) {
		if (player.playerEquipment[player.playerWeapon] > 0) {
			player.sendMessage("Please remove your weapon before attempting to get the flag again!");
			return;
		}
		if (!player.isDead) {
			final int team = gameRoom.get(player);
			if (team == 2 && saraFlag == 0) { // sara flag
				setSaraFlag(1);
				addFlag(player, SARA_BANNER);
				createHintIcon(player, 2);
			} else if (team == 1 && zammyFlag == 0) {
				setZammyFlag(1);
				addFlag(player, ZAMMY_BANNER);
				createHintIcon(player, 1);
			}
		}
	}

	public static void castleWarsSeason(Client player) {
		player.getFunction().sendFrame126("Saradomin: " + saradominWins, 11335);
		player.getFunction().sendFrame126("Zamorak: " + zamorakWins, 11336);
		player.getFunction().sendFrame164(11333);
	}

	public static void collapseCave(int cave) {
		final Iterator<Client> iterator = gameRoom.keySet().iterator();
		while (iterator.hasNext()) {
			final Client teamPlayer = iterator.next();
			if (teamPlayer.absX > COLLAPSE_ROCKS[cave][0]
					&& teamPlayer.absX < COLLAPSE_ROCKS[cave][1]
					&& teamPlayer.absY > COLLAPSE_ROCKS[cave][2]
					&& teamPlayer.absY < COLLAPSE_ROCKS[cave][3]) {
				final int dmg = teamPlayer.playerLevel[3];
				teamPlayer.handleHitMask(dmg);
				teamPlayer.dealDamage(99);
			}
		}
	}

	public static void createFlagHintIcon(int team, int x, int y) {
		final Iterator<Client> iterator = gameRoom.keySet().iterator();
		while (iterator.hasNext()) {
			final Client teamPlayer = iterator.next();
			if (teamPlayer.CWteam() == team) {
				teamPlayer.getFunction().createObjectHint(x, y, 170, 2);
			}
		}
	}

	public static void createHintIcon(Client player, int t) {
		final Iterator<Client> iterator = gameRoom.keySet().iterator();
		while (iterator.hasNext()) {
			final Client teamPlayer = iterator.next();
			if (teamPlayer.CWteam() != t) {
				teamPlayer.getFunction().createPlayerHint(10, player.playerId);
			}
		}
	}

	public static void deleteFlag(Client player) {
		if (player.getItems().playerHasItem(4037)) {
			player.getItems().deleteItem(4037, 1);
		}
		if (player.getItems().playerHasItem(4039)) {
			player.getItems().deleteItem(4039, 1);
		}
		if (player.playerEquipment[player.playerWeapon] == 4037) {
			deleteWeapon(player);
		}
		if (player.playerEquipment[player.playerWeapon] == 4039) {
			deleteWeapon(player);
		}
	}

	public static void deleteGameItems(Client player) {
		switch (player.playerEquipment[3]) {
		case 4037:
		case 4039:
			player.getItems().removeItem(player.playerEquipment[3], 3);
			break;
		}
		switch (player.playerEquipment[0]) {
		case 4042:
		case 4041:
			player.getItems().removeItem(player.playerEquipment[0], 0);
			break;
		}
		final int[] items = { 4049, 1265, 4045, 4053, 4042, 4041, 4037, 4039 };
		for (int item : items) {
			if (player.getItems().playerHasItem(item)) {
				player.getItems().deleteItem2(item,
						player.getItems().getItemAmount(item));
			}
		}
	}

	public static void deleteWeapon(Client player) {
		player.playerEquipment[player.playerWeapon] = -1;
		player.playerEquipmentN[player.playerWeapon] = 0;
		player.getItems().updateSlot(3);
		player.getUpdateFlags().appearanceUpdateRequired = true;
		player.updateRequired = true;
		player.getItems().resetItems(3214);
	}

	public static void dropFlag(Client player, int flagId) {
		int object = -1;
		switch (flagId) {
		case SARA_BANNER: // sara
			setSaraFlag(2);
			object = 4900;
			createFlagHintIcon(1, player.getX(), player.getY());
			saraFlagGrounded = true;
			break;
		case ZAMMY_BANNER: // zammy
			setZammyFlag(2);
			object = 4901;
			createFlagHintIcon(2, player.getX(), player.getY());
			zammyFlagGrounded = true;
			break;
		}

		World.getObjectHandler().globalObject(object, player.getX(),
				player.getY(), player.getZ(), 10);

	}

	public static void endGame() {
		// System.out.println("Finishing Castle Wars.");
		for (Player player2 : PlayerHandler.players) {
			if (player2 != null) {
				final Client player = (Client) player2;
				if (player.inCastleWars()) {
					CastleWars.deleteGameItems(player);
					player.getFunction().createPlayerHint(10, -1);
					if (player.getItems().playerHasEquipped(SARA_BANNER)) {
						player.getItems().removeItem(player.playerEquipment[3],
								3);
						setSaraFlag(0); // safe flag
					} else if (player.getItems()
							.playerHasEquipped(ZAMMY_BANNER)) {
						player.getItems().removeItem(player.playerEquipment[3],
								3);
						setZammyFlag(0); // safe flag
					}
					if (gameScores[0] == gameScores[1]) {
						player.addPoints(20);
						player.sendMessage("The game ended in a tie, you gain 20 OXP.");
						addTickets(player, 1);
					} else if (player.CWteam() == 1) {
						if (gameScores[0] > gameScores[1]) {
							player.addPoints(40);
							addTickets(player, 3);
							player.sendMessage("Saradomin wins the game, you gain 40 OXP.");
						} else if (gameScores[0] < gameScores[1]) {
							player.sendMessage("Saradomin loses the game.");
						}
					} else if (player.CWteam() == 2) {
						if (gameScores[1] > gameScores[0]) {
							player.addPoints(40);
							player.sendMessage("Zamorak wins the game, you gain 40 OXP.");
							addTickets(player, 3);
						} else if (gameScores[1] < gameScores[0]) {
							player.sendMessage("Zamorak loses the game.");
						}
					}
					gameRoom.remove(player);
					leaveGame(player);
				}
			}
		}

		if (gameScores[1] > gameScores[0]) {
			zamorakWins++;
			GameBuilder
					.announce("The Zamorak team won Castle Wars and each player gained 40 OXP.");
		} else if (gameScores[0] > gameScores[1]) {
			saradominWins++;
			GameBuilder
					.announce("The Saradomin team won Castle Wars and each player gained 40 OXP.");
		} else {
			GameBuilder.announce("The Castle Wars game ended in a draw.");
		}

		resetGame();
	}

	public static int getSaraPlayers() {
		int players = 0;
		final Iterator<Integer> iterator = !waitRoom.isEmpty() ? waitRoom
				.values().iterator() : gameRoom.values().iterator();
		while (iterator.hasNext()) {
			if (iterator.next() == 1) {
				players++;
			}
		}
		return players;
	}

	public static int getTeamNumber(Player player) {
		if (player == null) {
			return -1;
		}
		if (gameRoom.containsKey(player)) {
			return gameRoom.get(player);
		}
		return -1;
	}

	public static int getZammyPlayers() {
		int players = 0;
		final Iterator<Integer> iterator = !waitRoom.isEmpty() ? waitRoom
				.values().iterator() : gameRoom.values().iterator();
		while (iterator.hasNext()) {
			if (iterator.next() == 2) {
				players++;
			}
		}
		return players;
	}

	public static boolean isInCw(Client player) {
		return gameRoom.containsKey(player);
	}

	public static boolean isInCwWait(Client player) {
		return waitRoom.containsKey(player);
	}

	public static void leaveGame(Client player) {
		// only called on login in cw location
		deleteGameItems(player);
		player.getItems().removeItem(player.playerEquipment[0], 0);
		player.getItems().deleteItem2(player.playerEquipment[0], 1);
		player.getFunction().movePlayer(2439 + Misc.random(4),
				3085 + Misc.random(5), 0);
		if (player.getItems().playerHasItem(SARA_CAPE)) {
			player.getItems().deleteItem2(SARA_CAPE, 1);
		}
		if (player.getItems().playerHasItem(ZAMMY_CAPE)) {
			player.getItems().deleteItem2(ZAMMY_CAPE, 1);
		}
	}

	public static void leaveWaitingRoom(Client player) {
		if (player == null) {
			System.out.println("player is null");
			return;
		}
		if (waitRoom.containsKey(player)) {
			deleteGameItems(player);
			waitRoom.remove(player);
			// player.getAssist().createPlayerHints(10, -1);
			player.getItems().removeItem(player.playerEquipment[0], 0);
			player.getItems().removeItem(
					player.playerEquipment[player.playerHat], player.playerHat);
			player.getFunction().movePlayer(2439 + Misc.random(4),
					3085 + Misc.random(5), 0);
			if (player.getItems().playerHasItem(SARA_CAPE)) {
				player.getItems().deleteItem2(SARA_CAPE, 1);
			}
			if (player.getItems().playerHasItem(ZAMMY_CAPE)) {
				player.getItems().deleteItem2(ZAMMY_CAPE, 1);
			}

			return;
		}
	}

	public static void pickupFlag(Client player) {
		if (player.isDead) {
			return;
		}
		if (player.playerEquipment[player.playerWeapon] > 0) {
			player.sendMessage("Please remove your weapon before attempting to get the flag again!");
			return;
		}
		switch (player.objectId) {
		case 4900: // sara

			if (saraFlagGrounded) {
				if (player.CWteam() == SARA) {
					setSaraFlag(0);
					sendMessageCW("The Saradomin flag has been returned by "
							+ player.playerName + ".");
					saraFlagGrounded = false;
				}
				if (player.CWteam() == ZAMMY) {
					setSaraFlag(1);
					addFlag(player, 4037);
					createHintIcon(player, gameRoom.get(player));
					saraFlagGrounded = false;
				}
			} else {
				player.sendMessage("Flag has already been taken.");
			}

			break;
		case 4901: // zammy
			if (zammyFlagGrounded) {

				if (player.CWteam() == ZAMMY) {
					setZammyFlag(0);
					sendMessageCW("The Zamorak flag has been returned by "
							+ player.playerName + ".");

					zammyFlagGrounded = false;
				}

				if (player.CWteam() == SARA) {
					setZammyFlag(1);
					addFlag(player, 4039);
					createHintIcon(player, gameRoom.get(player));

					zammyFlagGrounded = false;
				}
			} else {
				player.sendMessage("Flag has already been taken");
			}
			break;
		}
		final Iterator<Client> iterator = gameRoom.keySet().iterator();
		while (iterator.hasNext()) {
			final Client teamPlayer = iterator.next();
			teamPlayer.getFunction().createObjectHint(player.objectX,
					player.objectY, 170, -1);
			teamPlayer.getFunction().object(-1, player.objectX, player.objectY,
					player.getZ(), 0, 10);
		}
		return;

	}

	public static void removeHintIcon(Client player, int t) {
		final Iterator<Client> iterator = gameRoom.keySet().iterator();
		while (iterator.hasNext()) {
			final Client teamPlayer = iterator.next();
			if (teamPlayer.CWteam() != t) {
				teamPlayer.getFunction().createPlayerHint(-1, player.playerId);
			}
		}
	}

	public static void removePlayerFromCw(Client player) {
		if (player == null) {
			System.out
					.println("Error removing player from castle wars [REASON = null]");
			return;
		}
		if (gameRoom.containsKey(player)) {
			if (player.getItems().playerHasEquipped(SARA_BANNER)) {
				player.getItems().removeItem(player.playerEquipment[3], 3);
				setSaraFlag(0); // safe flag
			} else if (player.getItems().playerHasEquipped(ZAMMY_BANNER)) {
				player.getItems().removeItem(player.playerEquipment[3], 3);
				setZammyFlag(0); // safe flag
			}
			player.getFunction().movePlayer(2440, 3089, 0);
			player.sendMessage("The castlewars game has ended for you.");
			leaveGame(player);
			gameRoom.remove(player);
			deleteGameItems(player);
		}
	}

	public static void resetGame() {
		setSaraFlag(0);
		setZammyFlag(0);
		gameTimer = -1;
		waitTimer = WAIT_TIMER;
		gameScores[0] = 0;
		gameScores[1] = 0;
	}

	public static void returnFlag(Client player, int wearItem) {
		if (player == null) {
			return;
		}
		if (wearItem != SARA_BANNER && wearItem != ZAMMY_BANNER) {
			return;
		}
		if (System.currentTimeMillis() - player.lastFlagScore < 30000) {
			leaveGame(player);
			return;
		}
		final int team = gameRoom.get(player);
		switch (team) {
		case 1:
			if (wearItem == SARA_BANNER) {
				setSaraFlag(0);
				sendMessageCW("The Saradomin flag has been returned by "
						+ player.playerName + ".");
			} else {
				setZammyFlag(0);
				gameScores[0]++;
				sendMessageCW(player.playerName
						+ " scores a point for Saradomin.");
				player.lastFlagScore = System.currentTimeMillis();
			}
			break;
		case 2:
			if (wearItem == ZAMMY_BANNER) {
				setZammyFlag(0);
				sendMessageCW("The Zamorak flag has been returned by "
						+ player.playerName + ".");
			} else {
				setSaraFlag(0);
				gameScores[1]++;
				sendMessageCW(player.playerName
						+ " scores a point for Zamorak.");
				zammyFlag = 0;
				player.lastFlagScore = System.currentTimeMillis();
			}
			break;
		}
		player.getFunction().createPlayerHint(10, -1);
		removeHintIcon(player, player.CWteam());
		player.playerEquipment[player.playerWeapon] = -1;
		player.playerEquipmentN[player.playerWeapon] = 0;
		player.getItems().updateSlot(3);
		player.getUpdateFlags().appearanceUpdateRequired = true;
		player.updateRequired = true;
		player.getItems().resetItems(3214);
	}

	public static void sendMessageCW(String msg) {
		for (Player player2 : PlayerHandler.players) {
			if (player2 != null) {
				final Client player = (Client) player2;
				if (player.inCastleWars()) {
					player.sendMessage("@blu@Castle Wars@bla@: @dre@" + msg);
				}
			}
		}
	}

	public static void setSaraFlag(int status) {
		saraFlag = status;
	}

	public static void setZammyFlag(int status) {
		zammyFlag = status;
	}

	public static void startGame() {
		if (waitRoom.size() >= PLAYERS_REQUIRED) {
			waitTimer = GAME_TIMER + WAIT_TIMER;
			gameTimer = GAME_TIMER;
			gameScores[0] = 0;
			gameScores[1] = 0;
			GameBuilder.announce("Castle Wars has started with "
					+ waitRoom.size() + " players, it will last 10 minutes.");
			final Iterator<Client> iterator = waitRoom.keySet().iterator();
			while (iterator.hasNext()) {
				final Client player = iterator.next();
				final int team = waitRoom.get(player);
				if (player == null) {
					continue;
				}
				player.getFunction().walkableInterface(-1);
				player.getFunction().movePlayer(
						GAME_ROOM[team - 1][0] + Misc.random(3),
						GAME_ROOM[team - 1][1] - Misc.random(3), 1);
				player.getFunction().movePlayer(
						GAME_ROOM[team - 1][0] + Misc.random(3),
						GAME_ROOM[team - 1][1] - Misc.random(3), 1);
				gameRoom.put(player, team);
			}
			waitRoom.clear();
		} else {
			waitTimer = WAIT_TIMER;
			final Iterator<Client> iterator = waitRoom.keySet().iterator();
			while (iterator.hasNext()) {
				Client player = iterator.next();
				player.sendMessage("The game could not be started, must have a minimum of "
						+ PLAYERS_REQUIRED + " players.");
			}
		}
	}

	public static void toWaitingRoom(Client player, int team) {
		if (team == 1) {
			if (getSaraPlayers() - getZammyPlayers() > 1) {
				player.sendMessage("The saradomin team is full, try the other team!");
				return;
			} else {
				addHat(player, SARA_CAPE);
				waitRoom.put(player, team);
				player.getFunction().movePlayer(
						WAIT_ROOM[team - 1][0] + Misc.random(5),
						WAIT_ROOM[team - 1][1] + Misc.random(5), 0);
			}
		} else if (team == 2) {
			if (getZammyPlayers() - getSaraPlayers() > 1) {
				player.sendMessage("The Zamorak team is full, try the other team!");
				return;
			} else {
				addHat(player, ZAMMY_CAPE);
				waitRoom.put(player, team);
				player.getFunction().movePlayer(
						WAIT_ROOM[team - 1][0] + Misc.random(5),
						WAIT_ROOM[team - 1][1] + Misc.random(5), 0);
			}
		} else if (team == 3) {
			toWaitingRoom(player, getZammyPlayers() > getSaraPlayers() ? 1 : 2);
			return;
		}
	}

	public static void updateGameInterface(Client player) {
		/*
		 * if (getSaraPlayers() > 0 && getZammyPlayers() > 0) { Iterator<Client>
		 * iterator = Room.keySet().iterator(); while (iterator.hasNext()) {
		 * Client player = (Client) iterator.next(); int config; if (player ==
		 * null) { continue; }
		 */
		int config;
		player.getFunction().walkableInterface(11146);
		player.getFunction().sendFrame126("Zamorak = " + gameScores[1], 11147);
		player.getFunction()
				.sendFrame126(gameScores[0] + " = Saradomin", 11148);
		player.getFunction().sendFrame126(gameTimer / 100 + 1 + " Min", 11155);
		player.getFunction().sendFrame126("Health " + player.playerLevel[3],
				11154);

		config = 2097152 * saraFlag;
		player.getFunction().sendFrame87(378, config);
		config = 2097152 * zammyFlag; // flags 0 = safe 1 = taken 2 =
										// dropped
		player.getFunction().sendFrame87(377, config);

		/*
		 * } }
		 */
	}

	public static void waitingRoom() {
		final Iterator<Client> iterator = waitRoom.keySet().iterator();
		while (iterator.hasNext()) {
			final Client player = iterator.next();
			if (player != null) {
				player.getFunction().walkableInterface(11479);
				player.getFunction().sendFrame36(380, waitTimer / 100 + 1);
			}
		}
	}

	public void tick() {
		if (waitTimer > 0) {
			waitTimer--;
			waitingRoom();
		} else if (waitTimer == 0 && waitRoom.size() > 0) {
			startGame();
		} else if (waitTimer == 0) {
			waitTimer = WAIT_TIMER;
		}
		if (gameTimer > 0) {
			gameTimer--;
		} else if (gameTimer == 0) {
			endGame();
		}
	}
}