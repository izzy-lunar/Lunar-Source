package com.ownxile.rs2.player;

import com.ownxile.config.GameConfig;
import com.ownxile.core.World;
import com.ownxile.core.task.impl.AnnouncementMessage;
import com.ownxile.rs2.Point.Position;
import com.ownxile.rs2.items.DonatorItem;
import com.ownxile.rs2.items.ItemAssistant;
import com.ownxile.rs2.npcs.KillLog;
import com.ownxile.rs2.npcs.NPCHandler;
import com.ownxile.rs2.npcs.PetHandler;
import com.ownxile.rs2.world.games.CastleWars;
import com.ownxile.util.file.FileLog;
import com.ownxile.util.web.vote.VoteCache;

public class PlayerLogin {

	private final Client player;

	public PlayerLogin(Client client) {
		player = client;
	}

	@SuppressWarnings("unused")
	private int getLoginInterfaceChildId(int idk) {
		switch (idk) {
		case 0:
			return 17511;
		case 1:
			return 15819; // Christmas Type
		case 2:
			return 15812;// Security Type
		case 3:
			return 15801;// Item Scam Type
		case 4:
			return 15791;// Password Safety
		case 5:
			return 15774;// Good/Bad Password
		case 6:
			return 15767; // Drama Type
		}
		return 17511;
	}

	private String[] blockOne = { "You can vote for up to 150 OX points",
			"and 10M gp on our website", "@gre@http://ownxile.com", " ", " ",
			" " };

	@SuppressWarnings("unused")
	private void loginScreen() {
		player.getTask().sendFrame126("Welcome to OwnXile", 15257);
		player.getTask().sendFrame126(
				"You last logged in from @red@" + player.connectedFrom, 15258);
		if (player.lastVoteDay.equalsIgnoreCase("never"))
			player.getTask().sendFrame126(
					blockOne[0] + "\\n" + blockOne[1] + "\\n" + blockOne[2]
							+ "\\n" + blockOne[3] + "\\n" + blockOne[4] + "\\n"
							+ blockOne[5] + "\\n", 15259);
		else
			player.getTask().sendFrame126(
					blockOne[0] + "\\n" + blockOne[1] + "\\n" + blockOne[2]
							+ "\\n" + blockOne[3] + "\\n" + blockOne[4] + "\\n"
							+ blockOne[5] + "\\n", 15259);
		player.getTask()
				.sendFrame126(
						"You have a total of "
								+ player.getPoints()
								+ " OX points to spend at\\n the reward store in Edgeville.",
						15260);
		player.getTask()
				.sendFrame126(
						"You can gain points by completing quests,\\n tasks, bosses, killing players and more.",
						15261);
		player.getTask().sendFrame126("", 15262);
		player.getTask()
				.sendFrame126(
						"Your account does not currently have a bank pin\\nthis may be due to the fact this feature\\ndoes not currently exist however it will\\nbe implemented in the near future.",
						15270);
		player.getTask().showInterface(15244);
		player.getTask().sendFrame126(
				"Expect plenty of big updates as we approach October.", 15814);
		player.getTask().sendFrame126(
				"@dre@Please vote for us recive up to 150 oxp and 10m GP.",
				15815);
		player.getTask().sendFrame126("Announcement", 15816);
		player.getTask().showInterface(15812);
	}

	public void correctCoordinates() {
		player.getFunction().sendFrame126("", 16131);
		player.getFunction().idk2();
		if (World.getGodwars().inDungeon(player) && player.absZ != 2) {
			player.getFunction().movePlayer(player.getX(), player.getY(), 2);
		}
		if (player.inPcBoat() || player.inPestControl()) {
			player.getFunction().movePlayer(2657, 2639, 0);
		}
		if (player.inCastleWars()) {
			player.getFunction().movePlayer(2441, 3095, 0);
			player.getItems().removeItem(player.playerEquipment[0], 0);
			player.getItems().deleteItem2(player.playerEquipment[0], 0);
			if (player.getItems().playerHasItem(CastleWars.SARA_CAPE)) {
				player.getItems().deleteItem2(CastleWars.SARA_CAPE, 0);
			}
			if (player.getItems().playerHasItem(CastleWars.ZAMMY_CAPE)) {
				player.getItems().deleteItem2(CastleWars.ZAMMY_CAPE, 0);
			}
		}
		if (player.inFightPits()) {
			player.getFunction().movePlayer(2399, 5173, 0);
		}
		if (player.inFightCaves()) {
			player.getFunction().movePlayer(2438, 5168, 0);
		}
		if (player.inWaitingRoom()) {
			player.getFunction().movePlayer(2441, 3095, 0);
			player.getItems().removeItem(player.playerEquipment[0], 0);
			player.getItems().deleteItem2(player.playerEquipment[0], 0);
			if (player.getItems().playerHasItem(CastleWars.SARA_CAPE)) {
				player.getItems().deleteItem2(CastleWars.SARA_CAPE, 0);
			}
			if (player.getItems().playerHasItem(CastleWars.ZAMMY_CAPE)) {
				player.getItems().deleteItem2(CastleWars.ZAMMY_CAPE, 0);
			}
		}
	}

	public void handleLoginText() {
		for (int i = 19000; i < 1000; i++)
			player.getFunction().sendFrame126("ID: " + i, i);

		player.getFunction().sendFrame126("" + player.runEnergy, 149);
		player.getFunction().sendFrame126(" ", 7383);
		player.getFunction().sendFrame126(" ", 7339);
		player.getFunction().sendFrame126(" ", 7338);
		player.getFunction().sendFrame126(" ", 7340);
		player.getFunction().sendFrame126(" ", 7346);
		player.getFunction().sendFrame126(" ", 7341);
		player.getFunction().sendFrame126(" ", 7342);
		player.getFunction().sendFrame126(" ", 7337);
		player.getFunction().sendFrame126(" ", 7343);
		player.getFunction().sendFrame126(" ", 7335);
		player.getFunction().sendFrame126(" ", 7344);
		player.getFunction().sendFrame126(" ", 7345);
		player.getFunction().sendFrame126(" ", 7347);
		player.getFunction().sendFrame126(" ", 7348);
		player.getFunction().sendFrame126(" ", 15139);
		player.getFunction().sendFrame126(" ", 2450);
		player.getFunction().sendFrame126(" ", 2451);
		player.getFunction().sendFrame126(" ", 2452);
		player.getFunction().sendFrame126("", 16023);
		player.getFunction().sendFrame126("16kg", 15139);
		player.getFunction().sendFrame126("" + player.bandos, 16217);
		player.getFunction().sendFrame126("" + player.armadyl, 16216);
		player.getFunction().sendFrame126("" + player.saradomin, 16218);
		player.getFunction().sendFrame126("" + player.zamorak, 16219);
		player.getFunction().sendFrame126(
				"level: " + player.getFunction().getTotalLevel(), 3984);

	}

	private boolean hasRetaliate() {
		return player.autoRet == 1;
	}

	public boolean itemExistsOnAccount(int itemId, int type) {
		if (player.getItems().hasItemInBank(itemId + 1)) {
			return false;
		}
		if (player.getItems().playerHasItem(itemId)) {
			return false;
		}
		if (player.playerEquipment[type] == itemId) {
			return false;
		}
		return true;
	}

	public boolean itemExistsOnAccount(int itemId, int amount, int type) {
		if (player.getItems().hasItemInBank(itemId + 1, amount)) {
			return true;
		}
		if (player.getItems().playerHasItem(itemId, amount)) {
			return true;
		}
		return false;
	}

	public void login() {
		FileLog.writeLog("game_logins", player.playerName + " from "
				+ player.connectedFrom + "-" + player.hostname);
		World.getHighscores().addToQueue1(player);
		player.loadQuests();
		player.getFunction().loadSidebars();
		loginPackets();
		player.getFunction().resetAnimation();
		player.getFunction().frame1();
		player.getFunction().resetFollow();
		player.flushOutStream();
		player.startConstantTasks();
		sendLoops();
		correctCoordinates();
		player.getFunction().showOption(4, 0, "Follow");
		player.getFunction().showOption(5, 0, "Trade With");
		player.getItems().resetItems(3214);
		player.saveCharacter = true;
		sendEquipment();
		player.getFunction().checkForMiscItems();
		player.getFunction().handleWeaponStyle();
		handleLoginText();
		// player.getFunction().playTempSong(2, 3);
		// player.getFunction().playSong(1);
		/*
		 * if (player.dupeFlag && player.playerRights < 1) {
		 * FileLog.writeLog("Faggots", player.playerName);
		 * ConnectionList.addNameToBanList(player.playerName);
		 * ConnectionList.addNameToFile(player.playerName); player.disconnected
		 * = true; }
		 */

		player.getFunction().setChatOptions(
				player.pubChat < 4 ? player.pubChat : 0,
				player.privChat < 4 ? player.privChat : 0,
				player.tradeChat < 4 ? player.tradeChat : 0);

		if (player.jail == 1 && !player.inJailArea()) {
			player.move(Position.JAIL_SPAWN);
		}
		player.getQuestFunction().refreshQuestTab();
		player.getFunction().clearClanChat();

		player.getFunction().updatePrivateChat();
		if (!PetHandler.isPetNpc(player.summonId)
				&& !KillLog.isBossPet(player.summonId)) {
			player.summonId = 0;
		}
		if (player.summonId > 0) {
			PetHandler.summonPet(player, player.summonId);
		}
		// loginScreen();

		player.getSlayer().loadTask();
		System.out.println("[" + player.getId() + "] " + player.playerName
				+ " has connected from " + player.connectedFrom + " - "
				+ player.hostname + ".");
		player.calcCombat();

		player.handler.updatePlayer(player, player.outStream);
		player.handler.updateNPC(player, player.outStream);
		player.sendMessage("Welcome to " + GameConfig.SERVER_NAME + ".");
		if (player.playerTitle == 0) {
			player.canChangeAppearance = true;
			player.getFunction().createNpcHint(NPCHandler.getSlotForNpc(945));
			player.getFunction().showInterface(3559);
			player.lastClickedNpcId = 945;
		}
		AnnouncementMessage event = World.getAnnouncementMessage();
		if (event.getMessage() != null) {
			player.sendMessage("@dre@There is currently an event at "
					+ event.getMessage() + " being run by "
					+ event.getManagerName() + "!");
		}/*
		if (VoteCache.canVote(player.playerName)
				&& World.getSettings().isDatabase()) {
			player.sendMessage("You are eligible to claim up to 100 OX points and 10M cash using @blu@::vote@bla@.");
		} else {
			player.sendMessage("You can claim another vote reward in "
					+ VoteCache.getHoursTillVote(player.playerName) + ".");
		}*/
		if (World.getSettings().isDoubleExp())
			player.sendMessage("Double experience is @blu@enabled@bla@.");
		if (player.clanName != null) {
			if (player.clanName.length() > 0) {
				World.getClanChat().joinClanChat(player,
						player.clanName.toLowerCase());
			} else {
				World.getClanChat().joinClanChat(player, "OwnXile");

			}
		}
		// World.getHighscores().addToQueue(player);

	}

	public void loginPackets() {
		player.outStream.createFrame(249);
		player.outStream.writeByteA(World.getSettings().membersWorld() ? 1 : 0);
		player.outStream.writeWordBigEndianA(player.playerId);
		player.getFunction().sendFrame36(505, 0);
		player.getFunction().sendFrame36(506, 0);
		player.getFunction().sendFrame36(507, 0);
		player.getFunction().sendFrame36(508, 1);
		player.getFunction().sendFrame36(108, 0);
		player.getFunction().sendFrame107();
		player.getFunction().sendFrame36(172, hasRetaliate() ? 1 : 0);
		player.getFunction().sendFrame36(173, 0);
		player.getPlayerOptions().updateSettings();
	}

	public void sendEquipment() {
		if (CastleWars.SARA_BANNER == player.playerEquipment[player.playerWeapon]
				|| CastleWars.ZAMMY_BANNER == player.playerEquipment[player.playerWeapon]) {
			CastleWars.deleteWeapon(player);
		}

		player.getItems()
				.sendWeapon(
						player.playerEquipment[player.playerWeapon],
						ItemAssistant
								.getItemName(player.playerEquipment[player.playerWeapon]));
		player.getItems().resetBonus();
		player.getItems().getBonus();
		player.getItems().writeBonus();
		player.getItems().setEquipment(
				player.playerEquipment[player.playerHat], 1, player.playerHat);
		player.getItems()
				.setEquipment(player.playerEquipment[player.playerCape], 1,
						player.playerCape);
		player.getItems().setEquipment(
				player.playerEquipment[player.playerAmulet], 1,
				player.playerAmulet);
		player.getItems().setEquipment(
				player.playerEquipment[player.playerArrows],
				player.playerEquipmentN[player.playerArrows],
				player.playerArrows);
		player.getItems().setEquipment(
				player.playerEquipment[player.playerChest], 1,
				player.playerChest);
		player.getItems().setEquipment(
				player.playerEquipment[player.playerShield], 1,
				player.playerShield);
		player.getItems()
				.setEquipment(player.playerEquipment[player.playerLegs], 1,
						player.playerLegs);
		player.getItems().setEquipment(
				player.playerEquipment[player.playerHands], 1,
				player.playerHands);
		player.getItems()
				.setEquipment(player.playerEquipment[player.playerFeet], 1,
						player.playerFeet);
		player.getItems()
				.setEquipment(player.playerEquipment[player.playerRing], 1,
						player.playerRing);
		player.getItems().setEquipment(
				player.playerEquipment[player.playerWeapon],
				player.playerEquipmentN[player.playerWeapon],
				player.playerWeapon);
		player.getItems().addSpecialBar(
				player.playerEquipment[player.playerWeapon]);
	}

	public void sendLoops() {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (j == player.playerId) {
				continue;
			}
			if (PlayerHandler.players[j] != null) {
				if (PlayerHandler.players[j].playerName
						.equalsIgnoreCase(player.playerName)) {
					player.disconnected = true;
				}
			}
		}
		for (int i = 0; i < 25; i++) {
			player.getFunction().setSkillLevel(i, player.playerLevel[i],
					player.playerXP[i]);
			player.getFunction().refreshSkill(i);
		}
		for (int p = 0; p < player.PRAYER.length; p++) {
			player.prayerActive[p] = false;
			player.getFunction().sendFrame36(player.PRAYER_GLOW[p], 0);
		}
	}

	@SuppressWarnings("unused")
	private void unusedMethod0() {
		for (DonatorItem donatorItem : World.donatorItems) {
			if (donatorItem.contains(player.playerName)
					&& itemExistsOnAccount(donatorItem.getId(),
							player.playerWeapon)) {
				player.sendMessage(ItemAssistant.getItemName(donatorItem
						.getId()) + " has been added to your bank.");
				player.getItems().addBankItem(donatorItem.getId(), 1);
			}
		}

		if (itemExistsOnAccount(1053, 10, player.playerHat)) {
			player.dupeFlag = true;
		} else if (itemExistsOnAccount(11696, 10, player.playerHat)) {
			player.dupeFlag = true;
		} else if (itemExistsOnAccount(7336, 8, player.playerHat)) {
			player.dupeFlag = true;
		} else if (itemExistsOnAccount(11283, 15, player.playerHat)) {
			player.dupeFlag = true;
		} else if (itemExistsOnAccount(13848, 20, player.playerChest)) {
			player.dupeFlag = true;
		} else if (itemExistsOnAccount(13899, 7, player.playerChest)) {
			player.dupeFlag = true;
		} else if (itemExistsOnAccount(11732, 7, player.playerChest)) {
			player.dupeFlag = true;
		}
	}
}
