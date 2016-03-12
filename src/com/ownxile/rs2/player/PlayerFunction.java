package com.ownxile.rs2.player;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.ownxile.config.CombatConfig;
import com.ownxile.config.FileConfig;
import com.ownxile.config.GameConfig;
import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.items.GroundItemHandler;
import com.ownxile.rs2.items.ItemAssistant;
import com.ownxile.rs2.npcs.NPC;
import com.ownxile.rs2.npcs.NPCHandler;
import com.ownxile.rs2.world.games.CastleWars;
import com.ownxile.rs2.world.games.Gambling;
import com.ownxile.rs2.world.region.RegionManager;
import com.ownxile.rs2.world.shops.Shopping;
import com.ownxile.util.Correction;
import com.ownxile.util.Formula;
import com.ownxile.util.Misc;
import com.ownxile.util.file.FileLog;
import com.ownxile.util.time.DateAndTime;
import com.ownxile.util.web.vote.VoteCache;

public class PlayerFunction {

	public static int barrows[] = { 4708, 4710, 4712, 4714, 4716, 4718, 4720,
			4722, 4724, 4726, 4728, 4730, 4732, 4734, 4736, 4738, 4745, 4747,
			4749, 4751, 4753, 4755, 4757, 4759 };

	public static int pots[] = {};

	public static int runes[] = { 4740, 558, 560, 565 };

	public static void updateInterface(Client player, Client other) {
		player.getFunction().sendFrame126(
				Misc.formatPlayerName(other.playerName), 27307);
		int level = other.wildLevel - 4;
		if (level < 0)
			level = 0;
		String lvl = "Lvl " + (level) + "-" + other.wildLevel + ", Cmb "
				+ other.combatLevel;
		player.getFunction().sendFrame126(lvl, 27308);
	}

	public int tempItems[] = new int[GameConfig.BANK_SIZE];
	public int tempItemsN[] = new int[GameConfig.BANK_SIZE];
	public int tempItemsT[] = new int[GameConfig.BANK_SIZE];
	public int tempItemsS[] = new int[GameConfig.BANK_SIZE];

	public void resetQuests() {
		if (client.getQuestFunction().hasCompletedAll()) {
			for (int i = 0; i <= World.totalQuests; i++) {
				client.getQuest(i).setStage(0);
				client.refreshQuestTab();
			}
			client.boxMessage("You have reset all of your quests.");
		} else {
			client.boxMessage("You need to have completed all quests to reset.");
		}
	}

	public void openShop(int id) {
		Shopping.openShop(client, id);
	}

	private static final int[] PARTYHATS = { 1038, 1038, 1038, 1040, 1040,
			1040, 1042, 1044, 1044, 1044, 1046, 1046, 1046, 1048, 1048 };

	private Map<Integer, TinterfaceText> interfaceText = new HashMap<Integer, TinterfaceText>();

	public class TinterfaceText {
		public int id;
		public String currentState;

		public TinterfaceText(String s, int id) {
			this.currentState = s;
			this.id = id;
		}

	}

	public void enterWildernessPortal(final int x, final int y) {
		if (client.inWild()) {
			return;
		}
		client.startAnimation(7376);
		closeAllWindows();
		World.getSynchronizedTaskScheduler().schedule(new Task(1, false) {
			protected void execute() {
				movePlayer(x, y, 0);
				client.startAnimation(7377);

				stop();
			}
		});

	}

	public void displayStaff() {
		sendFrame126("Staff Online", 903); // Title - -2
		sendFrame126("  ", 14165); // Bottom Left - Page-2
		sendFrame126(" ", 14166); // Bottom Right - Page-2
		int[] staffIds = new int[23];
		int staffIndex = 0;
		for (int i = 0; i < PlayerHandler.players.length; i++) {
			if (PlayerHandler.players[i] == null)
				continue;
			if (PlayerHandler.players[i].playerRights > 0
					&& PlayerHandler.players[i].playerRights < 4) {
				staffIds[staffIndex] = i;
				staffIndex++;
			}
		}
		client.sendMessage("There are currently " + staffIndex
				+ " staff online!");
		for (int i = 0; i < staffIds.length; i++) {
			if (PlayerHandler.players[staffIds[i]] == null) {
				sendFrame126("", 843 + i);

			} else
				sendFrame126("@blu@"
						+ PlayerHandler.players[staffIds[i]].playerName,
						843 + i);
		}
		showInterface(837);
	}

	public boolean checkPacket126Update(String text, int id) {
		if (!interfaceText.containsKey(id)) {
			interfaceText.put(id, new TinterfaceText(text, id));
		} else {
			TinterfaceText t = interfaceText.get(id);
			if (text.equals(t.currentState)) {
				return false;
			}
			t.currentState = text;
		}
		return true;
	}

	public String getPartyhat() {
		if (client.hasItem(962)) {
			client.deleteItem(962);
			int partyhat = PARTYHATS[Misc.random(PARTYHATS.length - 1)];
			client.addItem(partyhat);
			String name = ItemAssistant.getItemName(partyhat);
			return name;
		}
		return "";
	}

	public void switchMagic(int i) {
		closeAllWindows();
		client.endChat();
		if (ItemAssistant.getItemName(
				client.playerEquipment[client.playerWeapon]).contains("staff")) {
			client.setSidebarInterface(0, 328);
		}
		switch (i) {
		case 1:

			if (client.playerMagicBook == 0) {
				client.playerMagicBook = 1;
				client.setSidebarInterface(6, 12855);
				client.sendMessage("Your mind is filled with an ancient wisdom.");
				client.gfx0(436);
				client.getFunction().resetAutocast();
			} else {
				if (client.playerEquipment[client.playerWeapon] == 4675
						|| client.playerEquipment[client.playerWeapon] == 15050
						|| client.playerEquipment[client.playerWeapon] == 15040) {
					client.setSidebarInterface(0, 328);
				}
				client.setSidebarInterface(6, 1151); // modern
				client.playerMagicBook = 0;
				client.gfx0(436);
				client.sendMessage("You feel a drain on your memory.");
				client.autocastId = -1;
				client.getFunction().resetAutocast();
			}
			break;
		case 2:
			client.playerMagicBook = 2;
			client.gfx0(436);
			client.setSidebarInterface(6, 29999);
			client.sendMessage("Your mind is filled with a lunar wisdom.");
			client.getFunction().resetAutocast();
			break;
		}
	}

	/**
	 * unused // just for notes
	 */
	public void idk2() {
		sendFrame126("Home Teleport", 19220);
		sendFrame126("Teleport to the homeland", 19222);
		sendFrame126("Home Teleport", 21756);
		sendFrame126("Teleport to the homeland", 21757);

		// modern
		sendFrame126("Dungeon Teleports", 19641); // varrock
		sendFrame126("Enter and explore any dungeon", 19642); // varrock
																// description
		sendFrame126("Dungeon Teleports", 21833); // varrock
		sendFrame126("Enter and explore any dungeon", 21834); // varrock
																// description
		sendFrame126("@gre@0/0", 19646);
		sendFrame126("@gre@0/0", 19647);
		sendFrame126("@gre@0/0", 19648); // description
		sendFrame126("@gre@0/0", 21839);
		sendFrame126("@gre@0/0", 21840);
		sendFrame126("@gre@0/0", 21841);

		sendFrame126("Minigame Teleports", 19722); // lumbridge
		sendFrame126("Play any desired minigame", 19723); // lumbridge
															// description
		sendFrame126("Minigame Teleports", 21933); // lumbridge
		sendFrame126("Play any desired minigame", 21934); // lumbridge
															// description
		sendFrame126("@gre@0/0", 19727);
		sendFrame126("@gre@0/0", 19728);
		sendFrame126("@gre@0/0", 19729); // description
		sendFrame126("@gre@0/0", 21940);
		sendFrame126("@gre@0/0", 21942);

		sendFrame126("Boss Teleports", 19803); // falador
		sendFrame126("Fight a boss of your choice", 19804); // falador
															// description
		sendFrame126("Boss Teleports", 22052); // falador
		sendFrame126("Fight a boss of your choice", 22053); // falador
															// description
		sendFrame126("@gre@0/0", 19808);
		sendFrame126("@gre@0/0", 19809);
		sendFrame126("@gre@0/0", 19810);
		sendFrame126("@gre@0/0", 22056);
		sendFrame126("@gre@0/0", 22057);

		sendFrame126("City Teleports", 19960); // camelot
		sendFrame126("Visit a city of your choice", 19961); // camelot
															// description
		sendFrame126("City Teleports", 22123); // camelot
		sendFrame126("Visit a city of your choice", 22124); // camelot
															// description
		sendFrame126("@gre@0/0", 19964);
		sendFrame126("@gre@0/0", 19965);
		sendFrame126("@gre@0/0", 22127);
		sendFrame126("@gre@0/0", 22128);

		sendFrame126("Town Teleports", 20195); // ardougne
		sendFrame126("Visit a town of your choice", 20196); // ardougne
															// description
		sendFrame126("Town Teleports", 22232); // ardougne
		sendFrame126("Visit a town of your choice", 22233); // ardougne
															// description
		sendFrame126("@gre@0/0", 20199);
		sendFrame126("@gre@0/0", 20200);
		sendFrame126("@gre@0/0", 22237);
		sendFrame126("@gre@0/0", 22238);
		sendFrame126("@gre@0/0", 22239);

		sendFrame126("Alternative Teleports", 20354); // watchtower
		sendFrame126("A range of alternative teleports", 20355); // watchtower
																	// description
		sendFrame126("Alternative Teleports", 22307); // watchtower
		sendFrame126("A range of alternative teleports", 22308); // watchtower
																	// description
		sendFrame126("@gre@0/0", 20358);
		sendFrame126("@gre@0/0", 20359);
		sendFrame126("@gre@0/0", 22311);
		sendFrame126("@gre@0/0", 22312);

	}

	public static boolean giveItems(Client player, int[] items) {
		if (player.getItems().freeSlots() >= items.length) {
			for (int item : items) {
				player.getItems().addItem(item, 1);
			}
			return true;
		} else {
			player.sendMessage("You do not have enough inventory space to receive these items.");
		}
		return false;
	}

	public void sendCameraChange(Client player, int x, int y, int height,
			int speed, int angle) {
		player.getOutStream().createFrame(177);
		player.getOutStream().writeByte((player.absX / 64) + x);
		player.getOutStream().writeByte((player.absY / 64) + y);
		player.getOutStream().writeWord(height);
		player.getOutStream().writeByte(speed);
		player.getOutStream().writeByte(angle);
	}

	public void sendStillCameraChange(Client player, int x, int y, int height,
			int speed, int angle) {
		player.getOutStream().createFrame(166);
		player.getOutStream().writeByte(x / 64);
		player.getOutStream().writeByte(y / 64);
		player.getOutStream().writeWord(height);
		player.getOutStream().writeByte(speed);
		player.getOutStream().writeByte(angle);
	}

	public int clickedNpcIndex;

	private final Client client;

	public int CraftInt, Dcolor, FletchInt;

	public final String[] KILL_MESSAGES = {
			"You were clearly a better fighter than ",
			"With a crushing blow you finish ", "You have defeated ",
			"Well done, you destroyed ", "You have wiped the floor with ",
			"You have proven your superiority over ",
			"Well done, you've pwned ", "It's all over for ", };

	private long lastPick;

	public int mapStatus = 0;

	/**
	 * Show option, attack, trade, follow etc
	 **/
	public String optionType = "null";

	private Properties p = new Properties();

	public PlayerFunction(Client client) {
		this.client = client;
	}

	public void addObject(int objectId, int objectX, int objectY,
			int objectHeight, int face, int objectType) {
		if (client.distanceToPoint(objectX, objectY) > 60) {
			return;
		}
		if (client.absZ != objectHeight) {
			return;
		}
		if (client.getOutStream() != null && client != null) {
			client.getOutStream().createFrame(85);
			client.getOutStream().writeByteC(
					objectY - client.getMapRegionY() * 8);
			client.getOutStream().writeByteC(
					objectX - client.getMapRegionX() * 8);
			client.getOutStream().createFrame(101);
			client.getOutStream().writeByteC((objectType << 2) + (face & 3));
			client.getOutStream().writeByte(0);

			if (objectId != -1) { // removing
				client.getOutStream().createFrame(151);
				client.getOutStream().writeByteS(0);
				client.getOutStream().writeWordBigEndian(objectId);
				client.getOutStream()
						.writeByteS((objectType << 2) + (face & 3));
			}
			client.flushOutStream();
		}

	}

	public boolean addSkillXP(int amount, int skill) {
		if (client.xpLocked) {
			return false;
		}
		if (amount + client.playerXP[skill] > 200000000
				|| client.playerXP[skill] > 200000000) {
			client.playerXP[skill] = 200000000;
			return false;
		}
		if (World.getSettings().isDoubleExp()) {
			amount = amount * 2;
		}
		final int oldLevel = getLevelForXP(client.playerXP[skill]);
		client.playerXP[skill] += amount;
		if (oldLevel < getLevelForXP(client.playerXP[skill])) {
			if (client.playerLevel[skill] < client
					.getLevelForXP(client.playerXP[skill])
					&& skill != 3
					&& skill != 5) {
				client.playerLevel[skill] = client
						.getLevelForXP(client.playerXP[skill]);
			}
			levelUp(skill);
			client.gfx100(199);
			requestUpdates();
		}
		setSkillLevel(skill, client.playerLevel[skill], client.playerXP[skill]);
		refreshSkill(skill);
		return true;
	}

	public void addStarter(boolean receivedOnAnotherAccount) {
		if (client.starter == 0) {
			client.starter = 1;
			if (client.playerTitle < 9)
				client.addItem(10586, 11 - (2 + client.playerTitle * 2));
			if (client.isIronman() || client.isUltimateIronman()) {
				client.getItems().addItem(8012, 10);
				client.getItems().addItem(8013, 10);
				client.getItems().addItem(9705, 1);
				client.getItems().addItem(9706, 100);
				client.getItems().addItem(9703, 1);
				client.getItems().addItem(9704, 1);

			} else if (client.playerTitle > 1) {// not a easymode
				client.getItems().addItem(995,
						receivedOnAnotherAccount ? 500000 : 100000);
				client.getItems().addItem(8012, 10);
				client.getItems().addItem(8013, 10);
				client.getItems().addItem(557, 100);
				client.getItems().addItem(554, 200);
				client.getItems().addItem(555, 200);
				client.getItems().addItem(558, 200);
				client.getItems().addItem(562, 200);
				client.getItems().addItem(556, 1000);
				client.getItems().addItem(9705, 1);
				client.getItems().addItem(9706, 1000);
				client.getItems().addItem(9703, 1);
				client.getItems().addItem(9704, 1);
			} else {
				client.playerTitle = 1;
				client.getItems().addItem(995,
						!receivedOnAnotherAccount ? 1500000 : 200000);
				client.getItems().addItem(8012, 10);
				client.getItems().addItem(8013, 10);
				client.getItems().addItem(555, 200);
				client.getItems().addItem(9705, 1);
				client.getItems().addItem(9706, 1000);
				client.getItems().addItem(9703, 1);
				client.getItems().addItem(9704, 1);
				client.getItems().addItem(158,
						!receivedOnAnotherAccount ? 10 : 5);
				client.getItems().addItem(146,
						!receivedOnAnotherAccount ? 10 : 5);
				client.getItems().addItem(3025,
						!receivedOnAnotherAccount ? 5 : 2);
				client.getItems().addItem(386,
						!receivedOnAnotherAccount ? 100 : 5);
				client.getItems().addItem(555,
						!receivedOnAnotherAccount ? 600 : 60);
				client.getItems().addItem(560,
						!receivedOnAnotherAccount ? 400 : 40);
				client.getItems().addItem(565,
						!receivedOnAnotherAccount ? 200 : 20);
				client.getItems().addItem(892,
						!receivedOnAnotherAccount ? 200 : 30);
				if (!receivedOnAnotherAccount) {
					client.getItems().addItem(5699, 5);
					client.getItems().addItem(4588, 3);
					client.getItems().addItem(862, 5);
					client.getItems().addItem(1705, 5);
					client.getItems().addItem(11119, 5);
				}
			}
		}
	}

	public void annoy(int npcId) {
		final List<NPC> npcs = new ArrayList<NPC>();
		npcs.add(World.addNonCombatNpc(npcId, client.absX + 1, client.absY + 1,
				0, 0));
		npcs.add(World.addNonCombatNpc(npcId, client.absX, client.absY + 1, 0,
				0));
		npcs.add(World.addNonCombatNpc(npcId, client.absX + 1, client.absY + 2,
				0, 0));
		npcs.add(World.addNonCombatNpc(npcId, client.absX, client.absY + 2, 0,
				0));

		npcs.add(World.addNonCombatNpc(npcId, client.absX - 1, client.absY + 1,
				0, 0));
		npcs.add(World.addNonCombatNpc(npcId, client.absX - 1, client.absY + 2,
				0, 0));

		World.getSynchronizedTaskScheduler().schedule(new Task(5, false) {
			int count = 0;

			protected void execute() {
				count++;
				if (count < 10) {
					for (NPC npc : npcs) {
						npc.forceChat("HAHAHAAHAHA " + client.playerName
								+ " is a cunt.");
						npc.startAnimation(861);
						npc.facePlayer(client.playerId);
						client.startAnimation(860);
					}
				} else {
					for (NPC npc : npcs) {
						npc.teleportNpc(0, 0, 0);
					}
					stop();
				}
			}
		});

	}

	public void startDuel() {

		World.getSynchronizedTaskScheduler().schedule(new Task(2, true) {
			int count = 3;

			protected void execute() {
				if (count > 0) {
					client.forcedChat(count + "");
					count--;
				} else {
					client.forcedChat("FIGHT!");
					client.duelStatus = 5;
					closeAllWindows();
					stop();
				}
			}
		});
	}

	public void appendPoison(int damage) {
		if (System.currentTimeMillis() - client.lastPoison > 90000) {
			client.sendMessage("You have been poisoned!");
			client.poisonDamage = damage;
			client.lastPoison = System.currentTimeMillis();
		}
	}

	public void applyRingOfLife() {
		if (client.playerEquipment[client.playerRing] == 2570) {
			if (client.playerLevel[3] > 0
					&& client.playerLevel[3] <= client
							.getLevelForXP(client.playerXP[3]) / 10
					&& client.underAttackBy > 0) {
				final int wildlvl = (client.absY - 3520) / 8 + 1;
				if (wildlvl < 20) {
					client.getItems().deleteEquipment(2570, client.playerRing);
					startTeleport(3092, 3492, 0, "modern");
				}
			}
		}
	}

	public void becomeNpc(int npc) {
		try {
			if (npc < 9999) {
				client.npcId2 = npc;
				client.isNpc = true;
				client.updateRequired = true;
				client.getUpdateFlags().appearanceUpdateRequired = true;
			}
		} catch (final Exception e) {
		}
	}

	public void bookPreach(final String s, final String s1, final String s2) {
		client.getFunction().closeAllWindows();
		World.getSynchronizedTaskScheduler().schedule(new Task(2, true) {
			int count = 0;

			protected void execute() {
				client.startAnimation(1670);
				count++;
				switch (count) {
				case 1:
					client.forcedChat(s);
					break;
				case 2:
					client.forcedChat(s1);
					break;
				case 3:
					client.forcedChat(s2);
					break;
				case 4:
					stop();
					break;

				}
			}
		});
	}

	public boolean canAfford(int amount) {
		if (client.getItems().playerHasItem(995, amount)) {
			return true;
		} else {
			closeAllWindows();
			client.sendMessage("You can't afford to do this.");
			return false;
		}
	}

	public boolean canTeleport(boolean restricted) {
		if (client.duelStatus == 5) {
			client.sendMessage("You can't teleport during a duel!");
			return false;
		}
		if (client.inWild() && restricted) {
			if (client.wildLevel > GameConfig.NO_TELEPORT_WILD_LEVEL) {
				client.sendMessage("You can't teleport above level "
						+ GameConfig.NO_TELEPORT_WILD_LEVEL
						+ " in the wilderness.");
				closeAllWindows();
				return false;
			}
			if (System.currentTimeMillis() - client.logoutDelay < 10000) {
				client.sendMessage("You are currently under attack and unable to teleport.");
				return false;
			}
		}
		if (System.currentTimeMillis() - client.teleBlockDelay < client.teleBlockLength) {
			client.sendMessage("You are teleblocked!");
			return false;
		}
		if (client.inCastleWars()) {
			return false;
		}
		if (client.inWaitingRoom()) {
			return false;
		}
		if (CastleWars.isInCw(client)) {
			client.sendMessage("You cannot tele from a Castle Wars Game!");
			return false;
		}
		if (client.inFightCaves()) {
			state("You cannot teleport inside fight caves");
			return false;
		}
		if (client.inFightPits() || client.inFightPitsWait()) {
			state("You cannot teleport inside this minigame.");
			return false;
		}
		if (client.jail == 1) {
			client.sendMessage("You cannot teleport inside jail.");
			closeAllWindows();
			return false;
		}
		return true;
	}

	public boolean canWalk(int moveX, int moveY) {
		if (RegionManager.getClipping(client.getX(), client.getY(),
				client.getZ(), moveX, moveY)) {
			return true;
		}
		return false;
	}

	/*
	 * Vengeance
	 */
	public void castVeng() {
		if (client.playerLevel[6] < 94) {
			client.sendMessage("You need a magic level of 94 to cast this spell.");
			return;
		}
		if (client.playerLevel[1] < 40) {
			client.sendMessage("You need a defence level of 40 to cast this spell.");
			return;
		}
		if (!client.getItems().playerHasItem(9075, 4)
				|| !client.getItems().playerHasItem(557, 10)
				|| !client.getItems().playerHasItem(560, 2)) {
			client.sendMessage("You don't have the required runes to cast this spell.");
			return;
		}
		if (System.currentTimeMillis() - client.lastCast < 30000) {
			client.sendMessage("You can only cast vengeance every 30 seconds.");
			return;
		}
		if (client.vengOn) {
			client.sendMessage("You already have vengeance casted.");
			return;
		}
		client.startAnimation(4410);
		client.gfx100(726);// Just use player.gfx100
		client.getItems().deleteItem2(9075, 4);
		client.getItems().deleteItem2(557, 10);// For these you need to change
												// to
												// deleteItem(item, itemslot,
												// amount);.
		client.getItems().deleteItem2(560, 2);
		addSkillXP(10000, 6);
		refreshSkill(6);
		client.vengOn = true;
		client.lastCast = System.currentTimeMillis();
	}

	public void changeCamera(int x, int y, int height, int speed, int angle) {
		client.outStream.createFrame(177);
		client.outStream.writeByte(x / 64);
		client.outStream.writeByte(y / 64);
		client.outStream.writeWord(height);
		client.outStream.writeByte(speed);
		client.outStream.writeByte(angle);
	}

	/**
	 * Location change for digging, levers etc
	 **/

	public void changeLocation() {
		switch (client.newLocation) {
		case 1:
			// sendFrame99(2);
			movePlayer(3578, 9706, -1);
			break;
		case 2:
			// sendFrame99(2);
			movePlayer(3568, 9683, -1);
			break;
		case 3:
			// sendFrame99(2);
			movePlayer(3557, 9703, -1);
			break;
		case 4:
			// sendFrame99(2);
			movePlayer(3556, 9718, -1);
			break;
		case 5:
			// sendFrame99(2);
			movePlayer(3534, 9704, -1);
			break;
		case 6:
			// sendFrame99(2);
			movePlayer(3546, 9684, -1);
			break;
		}
		client.newLocation = 0;
	}

	public void checkDateAndTime() {
		final Calendar cal = new GregorianCalendar();

		final int YEAR = cal.get(Calendar.YEAR);
		final int MONTH = cal.get(Calendar.MONTH) + 1;
		final int DAY = cal.get(Calendar.DAY_OF_MONTH);
		final int HOUR = cal.get(Calendar.HOUR_OF_DAY);
		final int MIN = cal.get(Calendar.MINUTE);
		final int SECOND = cal.get(Calendar.SECOND);

		String day = "";
		String month = "";
		String hour = "";
		String minute = "";
		String second = "";

		if (DAY < 10) {
			day = "0" + DAY;
		} else {
			day = "" + DAY;
		}
		if (MONTH < 10) {
			month = "0" + MONTH;
		} else {
			month = "" + MONTH;
		}
		if (HOUR < 10) {
			hour = "0" + HOUR;
		} else {
			hour = "" + HOUR;
		}
		if (MIN < 10) {
			minute = "0" + MIN;
		} else {
			minute = "" + MIN;
		}
		if (SECOND < 10) {
			second = "0" + SECOND;
		} else {
			second = "" + SECOND;
		}

		client.date = day + "/" + month + "/" + YEAR;
		client.currentTime = hour + ":" + minute + ":" + second;
	}

	public boolean checkForFlags() {
		final int[][] itemsToCheck = { { 995, 100000000 }, { 35, 5 },
				{ 667, 5 }, { 2402, 5 }, { 746, 5 }, { 4151, 150 },
				{ 565, 100000 }, { 560, 100000 }, { 555, 300000 },
				{ 11235, 10 } };
		for (int[] element : itemsToCheck) {
			if (element[1] < client.getItems().getTotalCount(element[0])) {
				return true;
			}
		}
		return false;
	}

	public void checkForMiscItems() {
		if (client.playerEquipment[client.playerWeapon] == 4024) {
			if (client.getItems().freeSlots() > 0) {
				client.getItems().removeItem(4024, 3);
			} else {
				client.playerEquipment[client.playerWeapon] = -1;
				client.getItems().addBankItem(4024, 1);
			}
		} else if (client.playerEquipment[client.playerWeapon] == 4026) {
			if (client.getItems().freeSlots() > 0) {
				client.getItems().removeItem(4026, 3);
			} else {
				client.playerEquipment[client.playerWeapon] = -1;
				client.getItems().addBankItem(4026, 1);
			}
		} else if (client.playerEquipment[client.playerWeapon] == 4025) {
			if (client.getItems().freeSlots() > 0) {
				client.getItems().removeItem(4025, 3);
			} else {
				client.playerEquipment[client.playerWeapon] = -1;
				client.getItems().addBankItem(4025, 1);
			}
		} else if (client.playerEquipment[client.playerWeapon] == 4027) {
			if (client.getItems().freeSlots() > 0) {
				client.getItems().removeItem(4027, 3);
			} else {
				client.playerEquipment[client.playerWeapon] = -1;
				client.getItems().addBankItem(4027, 1);
			}
		} else if (client.playerEquipment[client.playerWeapon] == 4029) {
			if (client.getItems().freeSlots() > 0) {
				client.getItems().removeItem(4029, 3);
			} else {
				client.playerEquipment[client.playerWeapon] = -1;
				client.getItems().addBankItem(4029, 1);
			}
		} else if (client.playerEquipment[client.playerRing] == 6583) {
			if (client.getItems().freeSlots() > 0) {
				client.getItems().removeItem(6583, 12);
			} else {
				client.playerEquipment[client.playerRing] = -1;
				client.getItems().addBankItem(6583, 1);
			}
		} else if (client.playerEquipment[client.playerRing] == 7927) {
			if (client.getItems().freeSlots() > 0) {
				client.getItems().removeItem(7927, 12);
			} else {
				client.playerEquipment[client.playerRing] = -1;
				client.getItems().addBankItem(7927, 1);
			}
		}
	}

	public boolean checkForPlayer(int x, int y) {
		for (final Player p : PlayerHandler.players) {
			if (p != null) {
				if (p.getX() == x && p.getY() == y) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean checkOwner() {
		for (String element : GameConfig.OWNERS) {
			if (client.playerName.equalsIgnoreCase(element)) {
				return true;
			}
		}
		return false;
	}

	public void checkPouch(int i) {
		if (i < 0) {
			return;
		}
		client.sendMessage("This pouch has " + client.pouches[i]
				+ " com.ownxile ess in it.");
	}

	public boolean checkSpace(int spaces) {
		if (client.getItems().freeSlots() >= spaces) {
			return true;
		} else {
			closeAllWindows();
			return false;
		}
	}

	public String checkTimeOfDay() {
		final Calendar cal = new GregorianCalendar();
		final int TIME_OF_DAY = cal.get(Calendar.AM_PM);
		if (TIME_OF_DAY > 0) {
			return "PM";
		} else {
			return "AM";
		}
	}

	public void checkWeapon() {
		if (!World.getSettings().membersWorld()) {
			/*
			 * for (final int i : GameConfig.MEMBERS_WEAPONS) { if
			 * (client.playerEquipment[client.playerWeapon] == i)
			 * client.getItems().removeItem(i, 3); }
			 */
		}
	}

	public void clearClanChat() {
		client.clanId = -1;
		sendFrame126("Talking in: ", 18139);
		sendFrame126("Owner: ", 18140);
		for (int j = 18144; j < 18244; j++) {
			sendFrame126("", j);
		}
	}

	public void clippedStep() {
		if (RegionManager.getClipping(client.getX() - 1, client.getY(),
				client.getZ(), -1, 0)) {
			walkTo3(-1, 0);
		} else if (RegionManager.getClipping(client.getX() + 1, client.getY(),
				client.getZ(), 1, 0)) {
			walkTo3(1, 0);
		} else if (RegionManager.getClipping(client.getX(), client.getY() - 1,
				client.getZ(), 0, -1)) {
			walkTo3(0, -1);
		} else if (RegionManager.getClipping(client.getX(), client.getY() + 1,
				client.getZ(), 0, 1)) {
			walkTo3(0, 1);
		}
	}

	public void closeAllWindows() {
		if (client.cannotCloseWindows) {
			return;
		}
		if (client.getOutStream() != null && client != null) {
			client.getOutStream().createFrame(219);
			client.flushOutStream();
			client.isBanking = false;
			client.walkableInterfaceId = -1;
			client.interfaceId = -1;
		}
	}

	public void combatFollowPlayer() {
		if (PlayerHandler.players[client.followId] == null
				|| PlayerHandler.players[client.followId].isDead) {
			client.followId = 0;
			return;
		}
		if (client.freezeTimer > 0 || client.isDead
				|| client.playerLevel[3] <= 0) {
			return;
		}

		final int otherX = PlayerHandler.players[client.followId].getX();
		final int otherY = PlayerHandler.players[client.followId].getY();
		final boolean withinDistance = client.goodDistance(otherX, otherY,
				client.getX(), client.getY(), 1);
		client.goodDistance(otherX, otherY, client.getX(), client.getY(), 1);
		final boolean hallyDistance = client.goodDistance(otherX, otherY,
				client.getX(), client.getY(), 2);
		final boolean bowDistance = client.goodDistance(otherX, otherY,
				client.getX(), client.getY(), 8);
		final boolean rangeWeaponDistance = client.goodDistance(otherX, otherY,
				client.getX(), client.getY(), 4);
		final boolean sameSpot = client.absX == otherX && client.absY == otherY;
		if (!client.goodDistance(otherX, otherY, client.getX(), client.getY(),
				25)) {
			client.followId = 0;
			return;
		}
		if (client
				.goodDistance(otherX, otherY, client.getX(), client.getY(), 1)) {
			if (otherX != client.getX() && otherY != client.getY()) {
				stopDiagonal(otherX, otherY);
				return;
			}
		}

		if ((client.usingBow || client.mageFollow || client.playerIndex > 0
				&& client.autocastId > 0)
				&& bowDistance && !sameSpot) {
			return;
		}

		if (client.getCombat().usingHally() && hallyDistance && !sameSpot) {
			return;
		}

		if (client.usingRangeWeapon && rangeWeaponDistance && !sameSpot) {
			return;
		}

		client.faceUpdate(client.followId + 32768);

		final int absX = client.getX();
		final int absY = client.getY();
		// playerWalk(otherOldX, otherOldY);

		if (otherX == absX && otherY == absY) {
			final int r = Misc.random(3);
			switch (r) {
			case 0:
				playerWalk(otherX, otherY - 1);
				break;
			case 1:
				playerWalk(otherX, otherY + 1);
				break;
			case 2:
				playerWalk(otherX + 1, otherY);
				break;
			case 3:
				playerWalk(otherX - 1, otherY);
				break;
			}
		} else if (client.isRunning2 && !withinDistance) {
			if (otherY > absY && otherX == absX) {
				playerWalk(otherX, otherY - 1);

			} else if (otherY < absY && otherX == absX) {
				playerWalk(otherX, otherY + 1);

			} else if (otherX > absX && otherY == absY) {
				playerWalk(otherX - 1, otherY);

			} else if (otherX < absX && otherY == absY) {
				playerWalk(otherX + 1, otherY);

			} else if (otherX < absX && otherY < absY) {
				playerWalk(otherX + 1, otherY + 1);

			} else if (otherX > absX && otherY > absY) {
				playerWalk(otherX - 1, otherY - 1);

			} else if (otherX < absX && otherY > absY) {
				playerWalk(otherX + 1, otherY - 1);

			} else if (otherX > absX && otherY < absY) {
				playerWalk(otherX + 1, otherY - 1);
			}
		} else {
			if (otherY - 1 == absY && otherX - 1 == absX) {
				playerWalk(absX, absY);
			} else if (otherY > absY && otherX == absX) {
				playerWalk(otherX, otherY - 1);

			} else if (otherY < absY && otherX == absX) {
				playerWalk(otherX, otherY + 1);

			} else if (otherX > absX && otherY == absY) {
				playerWalk(otherX - 1, otherY);

			} else if (otherX < absX && otherY == absY) {
				playerWalk(otherX + 1, otherY);

			} else if (otherX < absX && otherY < absY) {
				playerWalk(otherX + 1, otherY + 1);

			} else if (otherX > absX && otherY > absY) {
				playerWalk(otherX - 1, otherY - 1);

			} else if (otherX < absX && otherY > absY) {
				playerWalk(otherX + 1, otherY - 1);

			} else if (otherX > absX && otherY < absY) {
				playerWalk(otherX - 1, otherY + 1);

			}
		}
		client.faceUpdate(client.followId + 32768);
	}

	public void createInterface(int item1, int item2, int item3) {
		client.getFunction().sendFrame164(8880);
		client.getFunction().sendFrame126("What would you like to make?", 8879);
		client.getFunction().sendFrame246(8884, 250, item2); // middle
		client.getFunction().sendFrame246(8883, 250, item1); // left picture
		client.getFunction().sendFrame246(8885, 250, item3); // right pic
		client.getFunction().sendFrame126(ItemAssistant.getItemName(item1),
				8889);
		client.getFunction().sendFrame126(ItemAssistant.getItemName(item2),
				8893);
		client.getFunction().sendFrame126(ItemAssistant.getItemName(item3),
				8897);
	}

	public void createNpcHint(int id) {
		if (client.getOutStream() != null && client != null) {
			client.getOutStream().createFrame(254); // The packet ID
			client.getOutStream().writeByte(1); // 1=NPC, 10=Player
			client.getOutStream().writeWord(id); // NPC/Player ID
			client.getOutStream().write3Byte(0); // Junk
			client.flushOutStream();
		}
	}

	public void createObjectHint(int x, int y, int height, int pos) {
		if (client.getOutStream() != null && client != null) {
			client.getOutStream().createFrame(254);
			client.getOutStream().writeByte(pos);
			client.getOutStream().writeWord(x);
			client.getOutStream().writeWord(y);
			client.getOutStream().writeByte(height);
			client.flushOutStream();
		}
	}

	public void createPlayerHint(int type, int id) {
		if (client.getOutStream() != null && client != null) {
			client.getOutStream().createFrame(254);
			client.getOutStream().writeByte(type);
			client.getOutStream().writeWord(id);
			client.getOutStream().write3Byte(0);
			client.flushOutStream();
		}
	}

	public void createPlayersObjectAnim(int X, int Y, int animationID,
			int tileObjectType, int orientation) {
		try {
			client.getOutStream().createFrame(85);
			client.getOutStream().writeByteC(Y - client.mapRegionY * 8);
			client.getOutStream().writeByteC(X - client.mapRegionX * 8);
			int x = 0;
			int y = 0;
			client.getOutStream().createFrame(160);
			client.getOutStream().writeByteS(((x & 7) << 4) + (y & 7));
			client.getOutStream().writeByteS(
					(tileObjectType << 2) + (orientation & 3));
			client.getOutStream().writeWordA(animationID);// animation id
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// projectiles for everyone within 25 squares
	public void createPlayersProjectile(int x, int y, int offX, int offY,
			int angle, int speed, int gfxMoving, int startHeight,
			int endHeight, int lockon, int time) {
		for (int i = 0; i < GameConfig.MAX_PLAYERS; i++) {
			final Player p = PlayerHandler.players[i];
			if (p != null) {
				final Client person = (Client) p;
				if (person != null) {
					if (person.getOutStream() != null) {
						if (person.distanceToPoint(x, y) <= 25) {
							if (p.absZ == client.absZ) {
								person.getFunction().createProjectile(x, y,
										offX, offY, angle, speed, gfxMoving,
										startHeight, endHeight, lockon, time);
							}
						}
					}
				}
			}
		}
	}

	public void createPlayersProjectile2(int x, int y, int offX, int offY,
			int angle, int speed, int gfxMoving, int startHeight,
			int endHeight, int lockon, int time, int slope) {
		for (int i = 0; i < GameConfig.MAX_PLAYERS; i++) {
			final Player p = PlayerHandler.players[i];
			if (p != null) {
				final Client person = (Client) p;
				if (person != null) {
					if (person.getOutStream() != null) {
						if (person.distanceToPoint(x, y) <= 25) {
							person.getFunction().createProjectile2(x, y, offX,
									offY, angle, speed, gfxMoving, startHeight,
									endHeight, lockon, time, slope);
						}
					}
				}
			}
		}
	}

	// creates gfx for everyone
	public void createPlayersStillGfx(int id, int x, int y, int height, int time) {
		for (int i = 0; i < GameConfig.MAX_PLAYERS; i++) {
			final Player p = PlayerHandler.players[i];
			if (p != null) {
				final Client person = (Client) p;
				if (person != null) {
					if (person.getOutStream() != null) {
						if (person.distanceToPoint(x, y) <= 25) {
							person.getFunction().stillGfx(id, x, y, height,
									time);
						}
					}
				}
			}
		}
	}

	/**
	 * Creating projectile
	 **/
	public void createProjectile(int x, int y, int offX, int offY, int angle,
			int speed, int gfxMoving, int startHeight, int endHeight,
			int lockon, int time) {
		if (client.getOutStream() != null && client != null) {
			client.getOutStream().createFrame(85);
			client.getOutStream()
					.writeByteC(y - client.getMapRegionY() * 8 - 2);
			client.getOutStream()
					.writeByteC(x - client.getMapRegionX() * 8 - 3);
			client.getOutStream().createFrame(117);
			client.getOutStream().writeByte(angle);
			client.getOutStream().writeByte(offY);
			client.getOutStream().writeByte(offX);
			client.getOutStream().writeWord(lockon);
			client.getOutStream().writeWord(gfxMoving);
			client.getOutStream().writeByte(startHeight);
			client.getOutStream().writeByte(endHeight);
			client.getOutStream().writeWord(time);
			client.getOutStream().writeWord(speed);
			client.getOutStream().writeByte(16);
			client.getOutStream().writeByte(64);
			client.flushOutStream();
		}
	}

	public void createProjectile2(int x, int y, int offX, int offY, int angle,
			int speed, int gfxMoving, int startHeight, int endHeight,
			int lockon, int time, int slope) {
		if (client.getOutStream() != null && client != null) {
			client.getOutStream().createFrame(85);
			client.getOutStream()
					.writeByteC(y - client.getMapRegionY() * 8 - 2);
			client.getOutStream()
					.writeByteC(x - client.getMapRegionX() * 8 - 3);
			client.getOutStream().createFrame(117);
			client.getOutStream().writeByte(angle);
			client.getOutStream().writeByte(offY);
			client.getOutStream().writeByte(offX);
			client.getOutStream().writeWord(lockon);
			client.getOutStream().writeWord(gfxMoving);
			client.getOutStream().writeByte(startHeight);
			client.getOutStream().writeByte(endHeight);
			client.getOutStream().writeWord(time);
			client.getOutStream().writeWord(speed);
			client.getOutStream().writeByte(slope);
			client.getOutStream().writeByte(64);
			client.flushOutStream();
		}
	}

	public void delayMessage(final String s, int delay) {
		World.getSynchronizedTaskScheduler().schedule(new Task(delay, false) {
			@Override
			protected void execute() {
				client.sendMessage(s);
				stop();
			}
		});

	}

	public void destroyInterface(int itemId, int amount) {
		final String itemName = ItemAssistant.getItemName(itemId);
		final String[][] info = {
				{ "Are you sure you want to destroy this item?", "14174" },
				{ "Yes.", "14175" }, { "No.", "14176" }, { "", "14177" },
				{ " ", "14182" }, { " ", "14183" }, { itemName, "14184" } };
		sendFrame34(itemId, 0, 14171, 1);
		for (String[] element : info) {
			sendFrame126(element[0], Integer.parseInt(element[1]));
		}
		client.destroyItem = itemId;
		client.destroyAmount = amount;
		sendFrame164(14170);
	}

	public void destroyItem(int itemId, int amount) {
		if (itemId > 0) {
			final String itemName = ItemAssistant.getItemName(itemId);
			final int itemSlot = client.getItems().getItemSlot(itemId);
			client.getItems().deleteItem(itemId, itemSlot, amount);
			client.sendMessage(amount + "x " + itemName + " destroyed.");
			client.destroyItem = 0;
			client.destroyAmount = 0;
		}
	}

	/**
	 * Show an arrow icon on the selected player.
	 * 
	 * @Param i - Either 0 or 1; 1 is arrow, 0 is none.
	 * @Param j - The player/Npc that the arrow will be displayed above.
	 * @Param k - Keep this set as 0
	 * @Param l - Keep this set as 0
	 */
	public void drawHeadicon(int i, int j, int k, int l) {
		client.outStream.createFrame(254);
		client.outStream.writeByte(i);
		if (i == 1 || i == 10) {
			client.outStream.writeWord(j);
			client.outStream.writeWord(k);
			client.outStream.writeByte(l);
		} else {
			client.outStream.writeWord(k);
			client.outStream.writeWord(l);
			client.outStream.writeByte(j);
		}
	}

	public void emptyPouch(int i) {
		if (i < 0) {
			return;
		}
		int toAdd = client.pouches[i];
		if (toAdd > client.getItems().freeSlots()) {
			toAdd = client.getItems().freeSlots();
		}
		if (toAdd > 0) {
			client.getItems().addItem(1436, toAdd);
			client.pouches[i] -= toAdd;
		}
	}

	public void enterCaves() {
		client.getTask().movePlayer(2413, 5117, client.playerId * 4);
		client.waveId = 0;
		client.tzhaarToKill = -1;
		client.tzhaarKilled = -1;
		World.getFightCaves().npcChat(client, "Prepare to fight!");
		World.getSynchronizedTaskScheduler().schedule(new Task(16, false) {
			protected void execute() {
				World.getFightCaves().spawnNextWave(
						(Client) PlayerHandler.players[client.playerId]);
				stop();
			}
		});
	}

	public void facebookReward() {
		yell("Facebook",
				"I have claimed a Facebook auth worth 50m Cash + 100 RP.");
		client.getItems().addItem(995, 50000000);
		client.addPoints(100);
		FileLog.writeLog("Facebook", client.playerName);
		client.facebook = 1;
	}

	public void fillPouch(int i) {
		if (i < 0) {
			return;
		}
		int toAdd = client.POUCH_SIZE[i] - client.pouches[i];
		if (toAdd > client.getItems().getItemAmount(1436)) {
			toAdd = client.getItems().getItemAmount(1436);
		}
		if (toAdd > client.POUCH_SIZE[i] - client.pouches[i]) {
			toAdd = client.POUCH_SIZE[i] - client.pouches[i];
		}
		if (toAdd > 0) {
			client.getItems().deleteItem(1436, toAdd);
			client.pouches[i] += toAdd;
		}
	}

	public int findKiller() {
		int killer = client.playerId;
		int damage = 0;
		for (int j = 0; j < GameConfig.MAX_PLAYERS; j++) {
			if (PlayerHandler.players[j] == null) {
				continue;
			}
			if (j == client.playerId) {
				continue;
			}
			if (client.goodDistance(client.absX, client.absY,
					PlayerHandler.players[j].absX,
					PlayerHandler.players[j].absY, 40)
					|| client.goodDistance(client.absX, client.absY + 9400,
							PlayerHandler.players[j].absX,
							PlayerHandler.players[j].absY, 40)
					|| client.goodDistance(client.absX, client.absY,
							PlayerHandler.players[j].absX,
							PlayerHandler.players[j].absY + 9400, 40)) {
				if (client.damageTaken[j] > damage) {
					damage = client.damageTaken[j];
					killer = j;
				}
			}
		}
		return killer;
	}

	public boolean fixAllBarrows() {
		int totalCost = 0;
		final int cashAmount = client.getItems().getItemAmount(995);
		for (int j = 0; j < client.playerItems.length; j++) {
			boolean breakOut = false;
			for (int[] brokenBarrow : client.getItems().brokenBarrows) {
				if (client.playerItems[j] - 1 == brokenBarrow[1]) {
					if (totalCost + 80000 > cashAmount) {
						breakOut = true;
						client.sendMessage("You have run out of money.");
						break;
					} else {
						totalCost += 80000;
					}
					client.playerItems[j] = brokenBarrow[0] + 1;
				}
			}
			if (breakOut) {
				break;
			}
		}
		if (totalCost > 0) {
			client.getItems().deleteItem(995,
					client.getItems().getItemSlot(995), totalCost);
			return true;
		}
		return false;
	}

	public void flashSelectedSidebar(int i1) {
		client.getOutStream().createFrame(24);
		client.getOutStream().writeByteA(i1);
	}

	public void followNpc() {
		if (NPCHandler.npcs[client.followId2] == null
				|| NPCHandler.npcs[client.followId2].isDead) {
			client.followId2 = 0;
			return;
		}
		if (client.freezeTimer > 0) {
			return;
		}
		if (client.isDead || client.playerLevel[3] <= 0) {
			return;
		}

		final int otherX = NPCHandler.npcs[client.followId2].getX();
		final int otherY = NPCHandler.npcs[client.followId2].getY();
		final boolean withinDistance = client.goodDistance(otherX, otherY,
				client.getX(), client.getY(), 2);

		client.goodDistance(otherX, otherY, client.getX(), client.getY(), 1);
		final boolean hallyDistance = client.goodDistance(otherX, otherY,
				client.getX(), client.getY(), 2);
		final boolean bowDistance = client.goodDistance(otherX, otherY,
				client.getX(), client.getY(), 8);
		final boolean rangeWeaponDistance = client.goodDistance(otherX, otherY,
				client.getX(), client.getY(), 4);
		final boolean sameSpot = client.absX == otherX && client.absY == otherY;
		if (!client.goodDistance(otherX, otherY, client.getX(), client.getY(),
				25)) {
			client.followId2 = 0;
			return;
		}
		if (client
				.goodDistance(otherX, otherY, client.getX(), client.getY(), 1)) {
			if (otherX != client.getX() && otherY != client.getY()) {
				stopDiagonal(otherX, otherY);
				return;
			}
		}

		if ((client.usingBow || client.mageFollow || client.npcIndex > 0
				&& client.autocastId > 0)
				&& bowDistance && !sameSpot) {
			return;
		}

		if (client.getCombat().usingHally() && hallyDistance && !sameSpot) {
			return;
		}

		if (client.usingRangeWeapon && rangeWeaponDistance && !sameSpot) {
			return;
		}

		client.faceUpdate(client.followId2);

		final int absX = client.getX();
		final int absY = client.getY();
		// playerWalk(otherOldX, otherOldY);

		if (otherX == absX && otherY == absY) {
			final int r = Misc.random(3);
			switch (r) {
			case 0:
				playerWalk(otherX, otherY - 1);
				break;
			case 1:
				playerWalk(otherX, otherY + 1);
				break;
			case 2:
				playerWalk(otherX + 1, otherY);
				break;
			case 3:
				playerWalk(otherX - 1, otherY);
				break;
			}
		} else if (client.isRunning2 && !withinDistance) {
			if (otherY > absY && otherX == absX) {
				playerWalk(otherX, otherY - 1);

			} else if (otherY < absY && otherX == absX) {
				playerWalk(otherX, otherY + 1);

			} else if (otherX > absX && otherY == absY) {
				playerWalk(otherX - 1, otherY);

			} else if (otherX < absX && otherY == absY) {
				playerWalk(otherX + 1, otherY);

			} else if (otherX < absX && otherY < absY) {
				playerWalk(otherX + 1, otherY + 1);

			} else if (otherX > absX && otherY > absY) {
				playerWalk(otherX - 1, otherY - 1);

			} else if (otherX < absX && otherY > absY) {
				playerWalk(otherX + 1, otherY - 1);

			} else if (otherX > absX && otherY < absY) {
				playerWalk(otherX + 1, otherY - 1);
			}
		} else {
			if (otherY - 1 == absY && otherX - 1 == absX) {
				playerWalk(absX, absY);
			} else if (otherY > absY && otherX == absX) {
				playerWalk(otherX, otherY - 1);

			} else if (otherY < absY && otherX == absX) {
				playerWalk(otherX, otherY + 1);

			} else if (otherX > absX && otherY == absY) {
				playerWalk(otherX - 1, otherY);

			} else if (otherX < absX && otherY == absY) {
				playerWalk(otherX + 1, otherY);

			} else if (otherX < absX && otherY < absY) {
				playerWalk(otherX + 1, otherY + 1);

			} else if (otherX > absX && otherY > absY) {
				playerWalk(otherX - 1, otherY - 1);

			} else if (otherX < absX && otherY > absY) {
				playerWalk(otherX + 1, otherY - 1);

			} else if (otherX > absX && otherY < absY) {
				playerWalk(otherX - 1, otherY + 1);

			}
		}
		client.faceUpdate(client.followId2);
	}

	@SuppressWarnings("unused")
	public void followPlayer() {
		if (PlayerHandler.players[client.followId3] == null
				|| PlayerHandler.players[client.followId3].isDead) {
			client.followId3 = 0;
			return;
		}
		if (client.freezeTimer > 0 || client.isDead
				|| client.playerLevel[3] <= 0) {
			return;
		}

		final int otherX = PlayerHandler.players[client.followId3].getX();
		final int otherY = PlayerHandler.players[client.followId3].getY();
		final int otherOldX = PlayerHandler.players[client.followId3].oldX;
		final int otherOldY = PlayerHandler.players[client.followId3].oldY;
		final boolean withinDistance = client.goodDistance(otherX, otherY,
				client.getX(), client.getY(), 1);
		final boolean goodDistance = client.goodDistance(otherX, otherY,
				client.getX(), client.getY(), 1);
		final boolean hallyDistance = client.goodDistance(otherX, otherY,
				client.getX(), client.getY(), 2);
		final boolean bowDistance = client.goodDistance(otherX, otherY,
				client.getX(), client.getY(), 8);
		final boolean rangeWeaponDistance = client.goodDistance(otherX, otherY,
				client.getX(), client.getY(), 4);
		final boolean sameSpot = client.absX == otherX && client.absY == otherY;
		if (!client.goodDistance(otherX, otherY, client.getX(), client.getY(),
				25)) {
			client.followId3 = 0;
			return;
		}
		/*
		 * if (player .goodDistance(otherX, otherY, player.getX(),
		 * player.getY(), 1)) { if (otherX != player.getX() && otherY !=
		 * player.getY()) { stopDiagonal(otherX, otherY); return; } }
		 */

		if ((client.usingBow || client.mageFollow || client.playerIndex > 0
				&& client.autocastId > 0)
				&& bowDistance && !sameSpot) {
			return;
		}

		if (client.getCombat().usingHally() && hallyDistance && !sameSpot) {
			return;
		}

		if (client.usingRangeWeapon && rangeWeaponDistance && !sameSpot) {
			return;
		}

		client.faceUpdate(client.followId3 + 32768);

		final int absX = client.getX();
		final int absY = client.getY();
		playerWalk(otherOldX, otherOldY);

		/*
		 * if (otherX == absX && otherY == absY) { int r = Misc.random(3);
		 * switch (r) { case 0: playerWalk(otherX, otherY - 1); break; case 1:
		 * playerWalk(otherX, otherY + 1); break; case 2: playerWalk(otherX + 1,
		 * otherY); break; case 3: playerWalk(otherX - 1, otherY); break; } }
		 * else if (player.isRunning2 && !withinDistance) { if (otherY > absY &&
		 * otherX == absX) { playerWalk(otherX, otherY - 1);
		 * 
		 * } else if (otherY < absY && otherX == absX) { playerWalk(otherX,
		 * otherY + 1);
		 * 
		 * } else if (otherX > absX && otherY == absY) { playerWalk(otherX - 1,
		 * otherY);
		 * 
		 * } else if (otherX < absX && otherY == absY) { playerWalk(otherX + 1,
		 * otherY);
		 * 
		 * } else if (otherX < absX && otherY < absY) { playerWalk(otherX + 1,
		 * otherY + 1);
		 * 
		 * } else if (otherX > absX && otherY > absY) { playerWalk(otherX - 1,
		 * otherY - 1);
		 * 
		 * } else if (otherX < absX && otherY > absY) { playerWalk(otherX + 1,
		 * otherY - 1);
		 * 
		 * } else if (otherX > absX && otherY < absY) { playerWalk(otherX + 1,
		 * otherY - 1); } } else { if ((otherY - 1) == absY && (otherX - 1) ==
		 * absX) { playerWalk(absX, absY); } else if (otherY > absY && otherX ==
		 * absX) { playerWalk(otherX, otherY - 1);
		 * 
		 * } else if (otherY < absY && otherX == absX) { playerWalk(otherX,
		 * otherY + 1);
		 * 
		 * } else if (otherX > absX && otherY == absY) { playerWalk(otherX - 1,
		 * otherY);
		 * 
		 * } else if (otherX < absX && otherY == absY) { playerWalk(otherX + 1,
		 * otherY);
		 * 
		 * } else if (otherX < absX && otherY < absY) { playerWalk(otherX + 1,
		 * otherY + 1);
		 * 
		 * } else if (otherX > absX && otherY > absY) { playerWalk(otherX - 1,
		 * otherY - 1);
		 * 
		 * } else if (otherX < absX && otherY > absY) { playerWalk(otherX + 1,
		 * otherY - 1);
		 * 
		 * } else if (otherX > absX && otherY < absY) { playerWalk(otherX - 1,
		 * otherY + 1);
		 * 
		 * } }
		 */
		client.faceUpdate(client.followId3 + 32768);
	}

	/**
	 * Reseting animations for everyone
	 **/

	public void frame1() {
		for (int i = 0; i < GameConfig.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				final Client person = (Client) PlayerHandler.players[i];
				if (person != null) {
					if (person.getOutStream() != null && !person.disconnected) {
						if (client
								.distanceToPoint(person.getX(), person.getY()) <= 25) {
							person.getOutStream().createFrame(1);
							person.flushOutStream();
							person.getFunction().requestUpdates();
						}
					}
				}
			}
		}
	}

	public boolean fullGuthans() {
		return client.playerEquipment[client.playerHat] == 4724
				&& client.playerEquipment[client.playerChest] == 4728
				&& client.playerEquipment[client.playerLegs] == 4730
				&& client.playerEquipment[client.playerWeapon] == 4726;
	}

	public boolean fullVeracs() {
		return client.playerEquipment[client.playerHat] == 4753
				&& client.playerEquipment[client.playerChest] == 4757
				&& client.playerEquipment[client.playerLegs] == 4759
				&& client.playerEquipment[client.playerWeapon] == 4755;
	}

	public Client getClient() {
		return client;
	}

	public int getLevelForXP(int exp) {
		int points = 0;
		int output = 0;
		if (exp > 13034430) {
			return 99;
		}
		for (int lvl = 1; lvl <= 99; lvl++) {
			points += Math.floor((double) lvl + 300.0
					* Math.pow(2.0, (double) lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if (output >= exp) {
				return lvl;
			}
		}
		return 0;
	}

	public int getMove(int place1, int place2) {
		if (System.currentTimeMillis() - client.lastSpear < 4000) {
			return 0;
		}
		if (place1 - place2 == 0) {
			return 0;
		} else if (place1 - place2 < 0) {
			return 1;
		} else if (place1 - place2 > 0) {
			return -1;
		}
		return 0;
	}

	public NPC getNpc() {
		return NPCHandler.npcs[client.npcClickIndex];
	}

	public int getNpcId(int id) {
		for (int i = 0; i < NPCHandler.MAX_VALUE; i++) {
			if (NPCHandler.npcs[i] != null) {
				if (NPCHandler.npcs[i].npcId == id) {
					return i;
				}
			}
		}
		return -1;
	}

	public int getRunningMove(int i, int j) {
		if (j - i > 2) {
			return 2;
		} else if (j - i < -2) {
			return -2;
		} else {
			return j - i;
		}
	}

	public void getSpeared(int otherX, int otherY) {
		int x = client.absX - otherX;
		int y = client.absY - otherY;
		client.gfx100(254);
		if (x > 0) {
			x = 1;
		} else if (x < 0) {
			x = -1;
		}
		if (y > 0) {
			y = 1;
		} else if (y < 0) {
			y = -1;
		}
		moveCheck(x, y);
		client.lastSpear = System.currentTimeMillis();
	}

	public int getTotalLevel() {
		return getLevelForXP(client.playerXP[0])
				+ getLevelForXP(client.playerXP[1])
				+ getLevelForXP(client.playerXP[2])
				+ getLevelForXP(client.playerXP[3])
				+ getLevelForXP(client.playerXP[4])
				+ getLevelForXP(client.playerXP[5])
				+ getLevelForXP(client.playerXP[6])
				+ getLevelForXP(client.playerXP[7])
				+ getLevelForXP(client.playerXP[8])
				+ getLevelForXP(client.playerXP[9])
				+ getLevelForXP(client.playerXP[10])
				+ getLevelForXP(client.playerXP[11])
				+ getLevelForXP(client.playerXP[12])
				+ getLevelForXP(client.playerXP[13])
				+ getLevelForXP(client.playerXP[14])
				+ getLevelForXP(client.playerXP[15])
				+ getLevelForXP(client.playerXP[16])
				+ getLevelForXP(client.playerXP[17])
				+ getLevelForXP(client.playerXP[18])
				+ getLevelForXP(client.playerXP[19])
				+ getLevelForXP(client.playerXP[20]);
	}

	public int getWearingAmount() {
		int count = 0;
		for (int element : client.playerEquipment) {
			if (element > 0) {
				count++;
			}
		}
		return count;
	}

	public int getXPForLevel(int level) {
		int points = 0;
		int output = 0;

		for (int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor((double) lvl + 300.0
					* Math.pow(2.0, (double) lvl / 7.0));
			if (lvl >= level) {
				return output;
			}
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}

	public void giveVoteReward(String date) {
		int total = client.getFunction().getTotalLevel();
		int cash = 1000000 + (Formula.calculateCashAmount(total) / 2) * 2;
		int rp = 2 + Formula.calculateRPAmount(total);
		String cashS = Correction.getAmountString(cash);
		World.totalCashAmount += cash;
		World.totalRPAmount += rp;
		World.voterNames[World.votes] = client.playerName;
		World.votes++;
		client.addPoints(rp);
		FileLog.writeLog("Voters" + date, client.playerName + ":"
				+ client.connectedFrom);
		client.lastVoteDay = date;
		World.sendMessage("@dre@" + client.playerName
				+ "@bla@ has voted for @dre@" + rp + " points@bla@ and @dre@"
				+ cashS + " gold@bla@.");
		if (client.getItems().freeSlots() > 2) {
			client.getItems().addItem(995, cash);
			client.getItems().addItem(386, 50);
			client.getItems().addItem(989, 1);
		} else {
			client.getItems().addBankItem(995, cash);
			client.getItems().addBankItem(385, 50);
			client.getItems().addBankItem(989, 1);
		}
		VoteCache.addClaimToCache(client.playerName);
	}

	public void giveVoteRewarda() {
		String date = DateAndTime.getTodaysDate();
		client.addPoints(100);
		client.addItem(995, 1000000);
		World.sendMessage("@dre@Notice: @bla@" + client.playerName
				+ " has voted for 100 points and 1 million gold.");
		client.lastVoteDay = date;
	}

	public void handleDfs() {
		if (client.inCastleWars() || client.inDuelArena()) {
			client.sendMessage("You may not use this special in minigames.");
			return;
		}
		if (System.currentTimeMillis() - client.dfsDelay > 30000) {
			if (client.playerIndex > 0
					&& PlayerHandler.players[client.playerIndex] != null) {
				client.startAnimation(6696);
				client.gfx0(1165);
				client.dfsDelay = System.currentTimeMillis();
				World.getSynchronizedTaskScheduler().schedule(
						new Task(2, false) {
							protected void execute() {
								if (PlayerHandler.players[client.playerIndex].playerLevel[3] > 0) {
									final int damage = Misc.random(20) + 3;
									PlayerHandler.players[client.playerIndex]
											.getHit(damage);

									PlayerHandler.players[client.playerIndex]
											.gfx100(1167);
								}
								stop();
							}
						});
			} else {
				client.sendMessage("I should be in combat before using this.");
			}
		} else {
			final String timePassed = Correction.getTimeString(client.dfsDelay);
			client.sendMessage("You last used the special " + timePassed
					+ ", you need to wait 30 seconds.");
		}
	}

	public boolean handleTransformationItem(int itemId) {
		if (client.hasInventorySpace(1)) {
			switch (itemId) {
			case 4024:
				client.getFunction().becomeNpc(1456);
				client.gfx0(160);
				return true;
			case 4025:
				client.getFunction().becomeNpc(1451);
				client.gfx0(160);
				return true;
			case 4026:
				client.getFunction().becomeNpc(1460);
				client.gfx0(160);
				return true;
			case 4027:
				client.getFunction().becomeNpc(1461);
				client.gfx0(160);
				return true;
			case 4029:
				client.getFunction().becomeNpc(1465);
				client.gfx0(160);
				return true;
			case 6583:
				client.getFunction().becomeNpc(2626);
				return true;
			case 7927:
				client.getFunction().becomeNpc(3689 + Misc.random(5));
				client.getFunction().resetFollow();
				client.resetWalkingQueue();
				return true;
			}
		}
		return false;
	}

	public void handleWeaponStyle() {
		if (client.fightMode == 0) {
			sendFrame36(43, client.fightMode);
		} else if (client.fightMode == 1) {
			sendFrame36(43, 3);
		} else if (client.fightMode == 2) {
			sendFrame36(43, 1);
		} else if (client.fightMode == 3) {
			sendFrame36(43, 2);
		}
	}

	public boolean hasAntiFireShield() {
		return client.playerEquipment[client.playerShield] == 1540
				|| client.playerEquipment[client.playerShield] == 11284
				|| client.playerEquipment[client.playerShield] == 11283;
	}

	public void hellRatResult(int npcIndex) {
		if (Misc.random(3) == 1) {
			client.forcedChat("Yes I got him!");
			state("The Hell-Rat struggles and some meat falls from the ground.");
			GroundItemHandler.createGroundItem(client, 2134,
					NPCHandler.npcs[npcIndex].getX(),
					NPCHandler.npcs[npcIndex].getY(), client.absZ, 1,
					client.playerId);
			GroundItemHandler.createGroundItem(client, 592,
					NPCHandler.npcs[npcIndex].getX(),
					NPCHandler.npcs[npcIndex].getY(), client.absZ, 1,
					client.playerId);
		} else {
			state("The Hell-Rat escapes from your grasp.");
		}
	}

	public void hideMap(boolean hide) {
		// client.getOutStream().createFrame(99);
		// client.getOutStream().writeByte(hide ? 2 : 0);
	}

	public void hideMinimapFlag() {
		client.getOutStream().createFrame(78);
	}

	public void infractionMessage(String target, String infractionVerb) {

		sendMessageAll("@bla@[@blu@Punishment@bla@] @dre@" + client.playerName
				+ " has " + infractionVerb + " "
				+ Misc.formatPlayerName(target) + ".");
		FileLog.writeLog("punishments", client.playerName + " "
				+ infractionVerb + " " + target + "</br>");
	}

	public boolean inPitsWait() {
		return client.getX() <= 2404 && client.getX() >= 2394
				&& client.getY() <= 5175 && client.getY() >= 5169;
	}

	public boolean isInPM(long l) {
		for (long friend : client.friends) {
			if (friend != 0) {
				if (l == friend) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isSuperAdmin() {
		for (String element : GameConfig.SUPER_ADMIN_PRIVILEGES) {
			if (client.playerName.equalsIgnoreCase(element)) {
				return true;
			}
		}
		return false;
	}

	public boolean itemCheck(int item) {
		if (!client.getItems().playerHasItem(item, 1)) {
			return true;
		} else {
			// closeAllWindows();
			return false;
		}
	}

	public void itemOnInterface(int interfaceChild, int zoom, int itemId) {
		if (client.getOutStream() != null & client != null) {
			client.outStream.createFrame(246);
			client.outStream.writeWordBigEndian(interfaceChild);
			client.outStream.writeWord(zoom);
			client.outStream.writeWord(itemId);
			client.flushOutStream();
		}
	}

	public void itemsOnDeath() {
		if (client.inWild()) {
			return;
		}
		if (!client.isSkulled) {
			client.getItems().resetKeepItems();
			client.getItems().keepItem(0, false);
			client.getItems().keepItem(1, false);
			client.getItems().keepItem(2, false);
			client.sendMessage("You are not skulled and will keep these 3 items when you die.");
		} else {
			client.getItems().resetKeepItems();
			client.sendMessage("You are skulled and will lose all your items on death.");
		}
		client.getItems().sendItemsKept();
		showInterface(6960);
		client.getItems().resetKeepItems();

	}

	public void jadSpawn() {
		client.getChat().sendChat(41, 2618);
		World.getSynchronizedTaskScheduler().schedule(new Task(16, false) {
			protected void execute() {
				World.getFightCaves().spawnNextWave(
						(Client) PlayerHandler.players[client.playerId]);
				stop();
			}
		});
	}

	public void jumpTo(final int x, final int y) {
		client.startAnimation(3067);
		World.getSynchronizedTaskScheduler().schedule(new Task(1, false) {
			@Override
			protected void execute() {
				client.getFunction().movePlayer(x, y, 0);
				stop();
			}
		});
	}

	public void levelUp(int skill) {
		final int totalLevel = getTotalLevel();
		sendFrame126("level: " + totalLevel, 3984);
		int level = getLevelForXP(client.playerXP[skill]);
		if (level == 99) {

			if (skill > 6) {
				World.sendMessage("@grd@" + client.playerName
						+ " has achieved 99 "
						+ GameConfig.SKILL_LEVEL_NAMES[skill] + "!");
				FileLog.writeLatestAchievment(client.playerName
						+ " has achieved 99 "
						+ GameConfig.SKILL_LEVEL_NAMES[skill] + ".");

			} else if (client.playerTitle > 2) {
				World.sendMessage("@grd@" + client.playerName
						+ " has achieved 99 "
						+ GameConfig.SKILL_LEVEL_NAMES[skill] + " on "
						+ CombatConfig.getDifficulty(client.playerTitle)
						+ " mode!");
				FileLog.writeLatestAchievment(client.playerName
						+ " has achieved 99 "
						+ GameConfig.SKILL_LEVEL_NAMES[skill] + " on "
						+ CombatConfig.getDifficulty(client.playerTitle)
						+ " mode!");
			}
		}

		switch (skill) {
		case 0:
			sendFrame126(
					"@blu@Congratulations, you just advanced an attack level!",
					6248);
			sendFrame126("Your attack level is now " + level + ".", 6249);

			client.sendMessage("Congratulations, you just advanced an attack level.");
			sendFrame164(6247);
			break;

		case 1:
			sendFrame126(
					"@blu@Congratulations, you just advanced a defence level!",
					6254);
			sendFrame126("Your defence level is now " + level + ".", 6255);
			client.sendMessage("Congratulations, you just advanced a defence level.");
			sendFrame164(6253);
			break;

		case 2:
			sendFrame126(
					"@blu@Congratulations, you just advanced a strength level!",
					6207);
			sendFrame126("Your strength level is now " + level + ".", 6208);
			client.sendMessage("Congratulations, you just advanced a strength level.");
			sendFrame164(6206);
			break;

		case 3:
			sendFrame126(
					"@blu@Congratulations, you just advanced a hitpoints level!",
					6217);
			sendFrame126("Your hitpoints level is now " + level + ".", 6218);
			client.sendMessage("Congratulations, you just advanced a hitpoints level.");
			sendFrame164(6216);
			break;

		case 4:
			sendFrame126(
					"@blu@Congratulations, you just advanced a ranged level!",
					5453);
			sendFrame126("Your ranged level is now " + level + ".", 6114);
			client.sendMessage("Congratulations, you just advanced a ranging level.");
			sendFrame164(4443);
			break;

		case 5:
			sendFrame126(
					"@blu@Congratulations, you just advanced a prayer level!",
					6243);
			sendFrame126("Your prayer level is now " + level + ".", 6244);
			client.sendMessage("Congratulations, you just advanced a prayer level.");
			sendFrame164(6242);
			break;

		case 6:
			sendFrame126(
					"@blu@Congratulations, you just advanced a magic level!",
					6212);
			sendFrame126("Your magic level is now " + level + ".", 6213);
			client.sendMessage("Congratulations, you just advanced a magic level.");
			sendFrame164(6211);
			break;

		case 7:
			sendFrame126(
					"@blu@Congratulations, you just advanced a cooking level!",
					6227);
			sendFrame126("Your cooking level is now " + level + ".", 6228);
			client.sendMessage("Congratulations, you just advanced a cooking level.");
			sendFrame164(6226);
			break;

		case 8:
			sendFrame126(
					"@blu@Congratulations, you just advanced a woodcutting level!",
					4273);
			sendFrame126("Your woodcutting level is now " + level + ".", 4274);
			client.sendMessage("Congratulations, you just advanced a woodcutting level.");
			sendFrame164(4272);
			break;

		case 9:
			sendFrame126(
					"@blu@Congratulations, you just advanced a fletching level!",
					6232);
			sendFrame126("Your fletching level is now " + level + ".", 6233);
			client.sendMessage("Congratulations, you just advanced a fletching level.");
			sendFrame164(6231);
			break;

		case 10:
			sendFrame126(
					"@blu@Congratulations, you just advanced a fishing level!",
					6259);
			sendFrame126("Your fishing level is now " + level + ".", 6260);
			client.sendMessage("Congratulations, you just advanced a fishing level.");
			sendFrame164(6258);
			break;

		case 11:
			sendFrame126(
					"@blu@Congratulations, you just advanced a firemaking level!",
					4283);
			sendFrame126("Your firemaking level is now " + level + ".", 4284);
			client.sendMessage("Congratulations, you just advanced a firemaking level.");
			sendFrame164(4282);
			break;

		case 12:
			sendFrame126(
					"@blu@Congratulations, you just advanced a crafting level!",
					6264);
			sendFrame126("Your crafting level is now " + level + ".", 6265);
			client.sendMessage("Congratulations, you just advanced a crafting level.");
			sendFrame164(6263);
			break;

		case 13:
			sendFrame126(
					"@blu@Congratulations, you just advanced a smithing level!",
					6222);
			sendFrame126("Your smithing level is now " + level + ".", 6223);
			client.sendMessage("Congratulations, you just advanced a smithing level.");
			sendFrame164(6221);
			break;

		case 14:
			sendFrame126(
					"@blu@Congratulations, you just advanced a mining level!",
					4417);
			sendFrame126("Your mining level is now " + level + ".", 4438);
			client.sendMessage("Congratulations, you just advanced a mining level.");
			sendFrame164(4416);
			break;

		case 15:
			sendFrame126(
					"@blu@Congratulations, you just advanced a herblore level!",
					6238);
			sendFrame126("Your herblore level is now " + level + ".", 6239);
			client.sendMessage("Congratulations, you just advanced a herblore level.");
			sendFrame164(6237);
			break;

		case 16:
			sendFrame126(
					"@blu@Congratulations, you just advanced a agility level!",
					4278);
			sendFrame126("Your agility level is now " + level + ".", 4279);
			client.sendMessage("Congratulations, you just advanced an agility level.");
			sendFrame164(4277);
			break;

		case 17:
			sendFrame126(
					"@blu@Congratulations, you just advanced a thieving level!",
					4263);
			sendFrame126("Your theiving level is now " + level + ".", 4264);
			client.sendMessage("Congratulations, you just advanced a thieving level.");
			sendFrame164(4261);
			break;

		case 18:
			sendFrame126(
					"@blu@Congratulations, you just advanced a slayer level!",
					12123);
			sendFrame126("Your slayer level is now " + level + ".", 12124);
			client.sendMessage("Congratulations, you just advanced a slayer level.");
			sendFrame164(12122);
			break;

		case 20:
			sendFrame126(
					"@blu@Congratulations, you just advanced a runecrafting level!",
					4268);
			sendFrame126("Your runecrafting level is now " + level + ".", 4269);
			client.sendMessage("Congratulations, you just advanced a runecrafting level.");
			sendFrame164(4267);
			break;
		}
		client.dialogueAction = 0;
		client.nextChat = 0;
	}

	public void listScroll(String name, String player, String b, String r,
			String f) {
		// player.flushOutStream();
		sendFrame126(name, 8144);
		sendFrame126(" ", 8145);
		sendFrame126("  ", 8146);
		sendFrame126(player, 8147);
		sendFrame126(b, 8148);
		sendFrame126(r, 8149);
		sendFrame126(f, 8150);
		sendFrame126(" ", 8151);
		sendFrame126(" ", 8152);
		sendFrame126(" ", 8153);
		sendFrame126(" ", 8154);
		sendFrame126(" ", 8155);
		sendFrame126(" ", 8156);
		sendFrame126(" ", 8157);
		sendFrame126(" ", 8158);
		sendFrame126(" ", 8159);
		sendFrame126(" ", 8160);
		sendFrame126(" ", 8161);
		sendFrame126(" ", 8162);
		sendFrame126(" ", 8163);
		showInterface(8134);
	}

	public void loadAnnouncements() {
		try {
			loadIni("./Announcements.ini");
			if (p.getProperty("announcement1").length() > 0) {
				client.sendMessage(p.getProperty("announcement1"));
			}
			if (p.getProperty("announcement2").length() > 0) {
				client.sendMessage(p.getProperty("announcement2"));
			}
			if (p.getProperty("announcement3").length() > 0) {
				client.sendMessage(p.getProperty("announcement3"));
			}
		} catch (final Exception e) {
		}
	}

	private void loadIni(String a) {
		try {
			p.load(new FileInputStream(a));
		} catch (final Exception e) {
		}
	}

	public void loadPM(long playerName, int world) {
		if (client.getOutStream() != null && client != null) {
			if (world != 0) {
				world += 9;
			}
			client.getOutStream().createFrame(50);
			client.getOutStream().writeQWord(playerName);
			client.getOutStream().writeByte(world);
			client.flushOutStream();
		}
	}

	public void loadSidebars() {
		client.setSidebarInterface(1, 3917);
		client.setSidebarInterface(2, 638);
		client.setSidebarInterface(3, 3213);
		client.setSidebarInterface(4, 1644);
		client.setSidebarInterface(5, 5608);
		if (client.playerMagicBook == 0) {
			client.setSidebarInterface(6, 1151); // modern
		} else {
			if (client.playerMagicBook == 2) {
				client.setSidebarInterface(6, 29999); // lunar
			} else {
				client.setSidebarInterface(6, 12855); // ancient
			}
		}
		client.setSidebarInterface(7, 18128);
		client.setSidebarInterface(8, 5065);
		client.setSidebarInterface(9, 5715);
		client.setSidebarInterface(10, 2449);
		client.setSidebarInterface(11, 904);
		client.setSidebarInterface(12, 147);
		client.setSidebarInterface(13, -1);
		client.setSidebarInterface(0, 2423);
	}

	public String longToPlayerName(long l) {
		int i = 0;
		while (l != 0L) {
			final long l1 = l;
			l /= 37L;
			client.charName[11 - i++] = GameConfig.playerNameXlateTable[(int) (l1 - l * 37L)];
		}
		return new String(client.charName, 12 - i, i);
	}

	public void lotteryResult() {
		closeAllWindows();
		if (Misc.random(client.lotteryChance) == 1) {
			winLottery();
		} else {
			client.startAnimation(860);
			client.sendMessage("Oh no you lost the lottery.");
			state("Oh no you lost the lottery.");
		}
	}

	/**
	 * Magic on items
	 **/

	public void magicOnItems(int slot, int itemId, int spellId) {
		if (!client.getItems().playerHasItem(itemId, 1, slot)) {
			return;
		}
		// System.out.println("attempting to cast " + spellId);
		switch (spellId) {
		case 1162: // low alch
			if (System.currentTimeMillis() - client.alchDelay > 1000) {
				if (!client.getCombat().checkMagicReqs(50)) {
					break;
				}
				if (itemId == 995) {
					client.sendMessage("You can't alch coins");
					break;
				}
				if (!client.getItems().playerHasItem(itemId, 1)) {
					client.sendMessage("You can't equip an item while alching.");
					return;
				}
				client.getItems().deleteItem(itemId, slot, 1);
				client.getItems().addItem(995,
						ItemAssistant.getItemValue(itemId) / 3);
				client.startAnimation(712);
				client.gfx100(112);
				client.alchDelay = System.currentTimeMillis();
				sendFrame106(6);
				addSkillXP(
						client.MAGIC_SPELLS[49][7]
								* CombatConfig.getCombatXP(client.playerTitle),
						6);
				refreshSkill(6);
			}
			break;
		case 1173:
			if (!client.getItems().playerHasItem(itemId, 1)) {
				client.sendMessage("You can't equip an item while alching.");
				return;
			}
			client.getSmithing().getSuperHeat().startHeating(itemId);
			break;
		case 1178: // high alch
			if (System.currentTimeMillis() - client.alchDelay > 2000) {
				if (!client.getCombat().checkMagicReqs(50)) {
					break;
				}
				if (itemId == 995) {
					client.sendMessage("You can't alch coins");
					break;
				}
				if (!client.getItems().playerHasItem(itemId, 1)) {
					client.sendMessage("You can't equip an item while alching.");
					return;
				}
				client.getItems().deleteItem(itemId, slot, 1);
				client.getItems().addItem(995,
						(int) (ItemAssistant.getItemValue(itemId) * .75));
				client.startAnimation(713);
				client.gfx100(113);// 713, 113,
				client.alchDelay = System.currentTimeMillis();
				sendFrame106(6);
				addSkillXP(
						client.MAGIC_SPELLS[50][7]
								* CombatConfig.getCombatXP(client.playerTitle),
						6);
				refreshSkill(6);
			}
			break;
		}
	}

	public boolean membersAction() {
		if (!World.getSettings().membersWorld()) {
			client.sendMessage("Login to a members' server to perform this action.");
			return false;
		} else {
			return true;
		}
	}

	public void moveCheck(int xMove, int yMove) {
		movePlayer(client.absX + xMove, client.absY + yMove, client.absZ);
	}

	public void movePlayer(int x, int y, int h) {
		if (h != client.absZ) {

		}
		client.teleportToX = x;
		client.teleportToY = y;
		client.absZ = h;
		requestUpdates();
		client.resetWalkingQueue();
		client.clearUpdateFlags();
		if (client.fading)
			client.fading = false;
		else
			this.closeAllWindows();
		client.lastTeleport = System.currentTimeMillis() - 3600;
		// World.getObjectHandler().updateObjects(client);
		// client.stopMovement2();
	}

	public void movePlayerCutscene(final int x, final int y, final int z) {
		client.getFunction().showInterface(18460);
		World.getSynchronizedTaskScheduler().schedule(new Task(3, false) {
			@Override
			protected void execute() {
				client.getFunction().movePlayer(x, y, z);
				client.getFunction().closeAllWindows();
				client.playerChat("Huh... how did I get here?");
				stop();
			}
		});
	}

	/**
	 * MulitCombat icon
	 * 
	 * @param i1
	 *            0 = off 1 = on
	 */
	public void multiWay(int i1) {
		client.outStream.createFrame(61);
		client.outStream.writeByte(i1);
		client.updateRequired = true;
		client.getUpdateFlags().setAppearanceUpdateRequired(true);
	}

	/**
	 * Objects, add and remove
	 **/
	public void object(int objectId, int objectX, int objectY, int objectZ,
			int face, int objectType) {
		if (objectZ != client.getZ()) {
			return;
		}
		if (client.distanceToPoint(objectX, objectY) > 60) {
			return;
		}
		if (client.getOutStream() != null && client != null) {
			client.getOutStream().createFrame(85);
			client.getOutStream().writeByteC(
					objectY - client.getMapRegionY() * 8);
			client.getOutStream().writeByteC(
					objectX - client.getMapRegionX() * 8);
			client.getOutStream().createFrame(101);
			client.getOutStream().writeByteC((objectType << 2) + (face & 3));
			client.getOutStream().writeByte(0);

			if (objectId != -1) { // removing
				client.getOutStream().createFrame(151);
				client.getOutStream().writeByteS(0);
				client.getOutStream().writeWordBigEndian(objectId);
				client.getOutStream()
						.writeByteS((objectType << 2) + (face & 3));
			}
			client.flushOutStream();
		}
	}

	public void objectAnim(int X, int Y, int animationID, int tileObjectType,
			int orientation) {
		for (Player p : PlayerHandler.players) {
			if (p != null) {
				Client players = (Client) p;
				if (players.distanceToPoint(X, Y) <= 25) {
					players.getFunction().createPlayersObjectAnim(X, Y,
							animationID, tileObjectType, orientation);
				}
			}
		}
	}

	/**
	 * Open bank
	 **/

	public int getTabCount() {
		// count tabs
		int tabs = 0;
		if (!checkEmpty(client.bankItems1))
			tabs++;
		if (!checkEmpty(client.bankItems2))
			tabs++;
		if (!checkEmpty(client.bankItems3))
			tabs++;
		if (!checkEmpty(client.bankItems4))
			tabs++;
		if (!checkEmpty(client.bankItems5))
			tabs++;
		if (!checkEmpty(client.bankItems6))
			tabs++;
		if (!checkEmpty(client.bankItems7))
			tabs++;
		if (!checkEmpty(client.bankItems8))
			tabs++;
		return tabs;
	}

	public boolean checkEmpty(int[] array) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] != 0)
				return false;
		}
		return true;
	}

	public void sendTabs() {
		// remove empty tab
		Client player = client;
		boolean moveRest = false;
		if (checkEmpty(player.bankItems1)) { // tab 1 empty
			player.bankItems1 = Arrays.copyOf(player.bankItems2,
					player.bankingItems.length);
			player.bankItems1N = Arrays.copyOf(player.bankItems2N,
					player.bankingItems.length);
			player.bankItems2 = new int[GameConfig.BANK_SIZE];
			player.bankItems2N = new int[GameConfig.BANK_SIZE];
			moveRest = true;
		}
		if (checkEmpty(player.bankItems2) || moveRest) {
			player.bankItems2 = Arrays.copyOf(player.bankItems3,
					player.bankingItems.length);
			player.bankItems2N = Arrays.copyOf(player.bankItems3N,
					player.bankingItems.length);
			player.bankItems3 = new int[GameConfig.BANK_SIZE];
			player.bankItems3N = new int[GameConfig.BANK_SIZE];
			moveRest = true;
		}
		if (checkEmpty(player.bankItems3) || moveRest) {
			player.bankItems3 = Arrays.copyOf(player.bankItems4,
					player.bankingItems.length);
			player.bankItems3N = Arrays.copyOf(player.bankItems4N,
					player.bankingItems.length);
			player.bankItems4 = new int[GameConfig.BANK_SIZE];
			player.bankItems4N = new int[GameConfig.BANK_SIZE];
			moveRest = true;
		}
		if (checkEmpty(player.bankItems4) || moveRest) {
			player.bankItems4 = Arrays.copyOf(player.bankItems5,
					player.bankingItems.length);
			player.bankItems4N = Arrays.copyOf(player.bankItems5N,
					player.bankingItems.length);
			player.bankItems5 = new int[GameConfig.BANK_SIZE];
			player.bankItems5N = new int[GameConfig.BANK_SIZE];
			moveRest = true;
		}
		if (checkEmpty(player.bankItems5) || moveRest) {
			player.bankItems5 = Arrays.copyOf(player.bankItems6,
					player.bankingItems.length);
			player.bankItems5N = Arrays.copyOf(player.bankItems6N,
					player.bankingItems.length);
			player.bankItems6 = new int[GameConfig.BANK_SIZE];
			player.bankItems6N = new int[GameConfig.BANK_SIZE];
			moveRest = true;
		}
		if (checkEmpty(player.bankItems6) || moveRest) {
			player.bankItems6 = Arrays.copyOf(player.bankItems7,
					player.bankingItems.length);
			player.bankItems6N = Arrays.copyOf(player.bankItems7N,
					player.bankingItems.length);
			player.bankItems7 = new int[GameConfig.BANK_SIZE];
			player.bankItems7N = new int[GameConfig.BANK_SIZE];
			moveRest = true;
		}
		if (checkEmpty(player.bankItems7) || moveRest) {
			player.bankItems7 = Arrays.copyOf(player.bankItems8,
					player.bankingItems.length);
			player.bankItems7N = Arrays.copyOf(player.bankItems8N,
					player.bankingItems.length);
			player.bankItems8 = new int[GameConfig.BANK_SIZE];
			player.bankItems8N = new int[GameConfig.BANK_SIZE];
		}
		if (player.bankingTab > getTabCount())
			player.bankingTab = getTabCount();
		player.getFunction().sendNewFrame126(Integer.toString(getTabCount()),
				27001);
		player.getFunction().sendNewFrame126(
				Integer.toString(player.bankingTab), 27002);
		itemOnInterface(player, 22035, 0,
				getInterfaceModel(0, player.bankItems1, player.bankItems1N),
				getAmount(player.bankItems1[0], player.bankItems1N[0]));
		itemOnInterface(player, 22036, 0,
				getInterfaceModel(0, player.bankItems2, player.bankItems2N),
				getAmount(player.bankItems2[0], player.bankItems2N[0]));
		itemOnInterface(player, 22037, 0,
				getInterfaceModel(0, player.bankItems3, player.bankItems3N),
				getAmount(player.bankItems3[0], player.bankItems3N[0]));
		itemOnInterface(player, 22038, 0,
				getInterfaceModel(0, player.bankItems4, player.bankItems4N),
				getAmount(player.bankItems4[0], player.bankItems4N[0]));
		itemOnInterface(player, 22039, 0,
				getInterfaceModel(0, player.bankItems5, player.bankItems5N),
				getAmount(player.bankItems5[0], player.bankItems5N[0]));
		itemOnInterface(player, 22040, 0,
				getInterfaceModel(0, player.bankItems6, player.bankItems6N),
				getAmount(player.bankItems6[0], player.bankItems6N[0]));
		itemOnInterface(player, 22041, 0,
				getInterfaceModel(0, player.bankItems7, player.bankItems7N),
				getAmount(player.bankItems7[0], player.bankItems7N[0]));
		itemOnInterface(player, 22042, 0,
				getInterfaceModel(0, player.bankItems8, player.bankItems8N),
				getAmount(player.bankItems8[0], player.bankItems8N[0]));

		player.getFunction().sendNewFrame126("1", 27000);
	}

	public int getBankItems(int tab) {
		int ta = 0, tb = 0, tc = 0, td = 0, te = 0, tf = 0, tg = 0, th = 0, ti = 0;
		for (int i = 0; i < client.bankItems.length; i++)
			if (client.bankItems[i] > 0)
				ta++;
		for (int i = 0; i < client.bankItems1.length; i++)
			if (client.bankItems1[i] > 0)
				tb++;
		for (int i = 0; i < client.bankItems2.length; i++)
			if (client.bankItems2[i] > 0)
				tc++;
		for (int i = 0; i < client.bankItems3.length; i++)
			if (client.bankItems3[i] > 0)
				td++;
		for (int i = 0; i < client.bankItems4.length; i++)
			if (client.bankItems4[i] > 0)
				te++;
		for (int i = 0; i < client.bankItems5.length; i++)
			if (client.bankItems5[i] > 0)
				tf++;
		for (int i = 0; i < client.bankItems6.length; i++)
			if (client.bankItems6[i] > 0)
				tg++;
		for (int i = 0; i < client.bankItems7.length; i++)
			if (client.bankItems7[i] > 0)
				th++;
		for (int i = 0; i < client.bankItems8.length; i++)
			if (client.bankItems8[i] > 0)
				ti++;
		if (tab == 0)
			return ta;
		if (tab == 1)
			return tb;
		if (tab == 2)
			return tc;
		if (tab == 3)
			return td;
		if (tab == 4)
			return te;
		if (tab == 5)
			return tf;
		if (tab == 6)
			return tg;
		if (tab == 7)
			return th;
		if (tab == 8)
			return ti;
		return ta + tb + tc + td + te + tf + tg + th + ti; // return total

	}

	public static void itemOnInterface(Client player, int frame, int slot,
			int id, int amount) {
		//if (id > 0) { prevents item on tab removal when uncommented
			player.getOutStream().createFrameVarSizeWord(34);
			player.getOutStream().writeWord(frame);
			player.getOutStream().writeByte(slot);
			player.getOutStream().writeWord(id + 1);
			if (amount > 254) {
				player.getOutStream().writeByte(255);
				player.getOutStream().writeDWord(amount);
			} else {
				player.getOutStream().writeByte(amount);
			}
			player.getOutStream().endFrameVarSizeWord();
		//}
	}

	public static int getAmount(int itemId, int amount) {
		/*
		 * if (itemId <= 0) return 1; if (Item.itemStackable[itemId]) return
		 * amount;
		 */
		return 1;
	}

	public int getInterfaceModel(int slot, int[] array, int[] arrayN) {
		int model = array[slot] - 1;
		if (model == 995) {
			if (arrayN[slot] > 9999) {
				model = 1004;
			} else if (arrayN[slot] > 999) {
				model = 1003;
			} else if (arrayN[slot] > 249) {
				model = 1002;
			} else if (arrayN[slot] > 99) {
				model = 1001;
			} else if (arrayN[slot] > 24) {
				model = 1000;
			} else if (arrayN[slot] > 4) {
				model = 999;
			} else if (arrayN[slot] > 3) {
				model = 998;
			} else if (arrayN[slot] > 2) {
				model = 997;
			} else if (arrayN[slot] > 1) {
				model = 996;
			}
		}
		return model;
	}

	/**
	 * Open bank
	 **/
	public void openUpBank(int tab) {
		if (client.isUltimateIronman()) {
			client.boxMessage("Ultimate Ironmen cannot use banks.");
			return;
		}
		if (client.takeAsNote) {
			sendFrame36(115, 1);
		} else {
			sendFrame36(115, 0);
		}
		if (client.inWild()) {
			client.isBanking = false;
			return;

		}
		if (client.inTrade || client.tradeStatus == 1) {
			Client o = (Client) PlayerHandler.players[client.tradeWith];
			if (o != null) {
				o.getTrade().declineTrade();
			}
		}
		if (client.duelStatus == 1) {
			Client o = (Client) PlayerHandler.players[client.duelingWith];
			if (o != null) {
				o.getDuel().resetDuel();
			}
		}
		if (client.getOutStream() != null && client != null) {
			client.isBanking = true;
			client.bankingTab = tab;
			sendTabs();
			if (client.bankingTab == 0) {
				client.bankingItems = client.bankItems;
				client.bankingItemsN = client.bankItemsN;
			}
			if (client.bankingTab == 1) {
				client.bankingItems = client.bankItems1;
				client.bankingItemsN = client.bankItems1N;
			}
			if (client.bankingTab == 2) {
				client.bankingItems = client.bankItems2;
				client.bankingItemsN = client.bankItems2N;
			}
			if (client.bankingTab == 3) {
				client.bankingItems = client.bankItems3;
				client.bankingItemsN = client.bankItems3N;
			}
			if (client.bankingTab == 4) {
				client.bankingItems = client.bankItems4;
				client.bankingItemsN = client.bankItems4N;
			}
			if (client.bankingTab == 5) {
				client.bankingItems = client.bankItems5;
				client.bankingItemsN = client.bankItems5N;
			}
			if (client.bankingTab == 6) {
				client.bankingItems = client.bankItems6;
				client.bankingItemsN = client.bankItems6N;
			}
			if (client.bankingTab == 7) {
				client.bankingItems = client.bankItems7;
				client.bankingItemsN = client.bankItems7N;
			}
			if (client.bankingTab == 8) {
				client.bankingItems = client.bankItems8;
				client.bankingItemsN = client.bankItems8N;
			}
			client.getItems().resetItems(5064);
			client.getItems().rearrangeBank();
			client.getItems().resetBank();
			client.getItems().resetTempItems();
			client.getOutStream().createFrame(248);
			client.getOutStream().writeWordA(24959);// cant we use the server
													// sided
			// method to just update item
			// when you remove a bank item?
			client.getOutStream().writeWord(5063);
			client.flushOutStream();
		}
	}

	public void openBank(int tab) {
		if (client.isUltimateIronman()) {
			client.boxMessage("Ultimate Ironmen cannot use banks.");
			return;
		}
		if (client.takeAsNote) {
			sendFrame36(115, 1);
		} else {
			sendFrame36(115, 0);
		}
		if (client.inWild()) {
			client.isBanking = false;
			return;

		}
		if (client.inTrade || client.tradeStatus == 1) {
			Client o = (Client) PlayerHandler.players[client.tradeWith];
			if (o != null) {
				o.getTrade().declineTrade();
			}
		}
		if (client.duelStatus == 1) {
			Client o = (Client) PlayerHandler.players[client.duelingWith];
			if (o != null) {
				o.getDuel().resetDuel();
			}
		}
		if (client.getOutStream() != null && client != null) {
			client.isBanking = true;
			client.bankingTab = tab;
			sendTabs();
			if (client.bankingTab == 0) {
				client.bankingItems = client.bankItems;
				client.bankingItemsN = client.bankItemsN;
			}
			if (client.bankingTab == 1) {
				client.bankingItems = client.bankItems1;
				client.bankingItemsN = client.bankItems1N;
			}
			if (client.bankingTab == 2) {
				client.bankingItems = client.bankItems2;
				client.bankingItemsN = client.bankItems2N;
			}
			if (client.bankingTab == 3) {
				client.bankingItems = client.bankItems3;
				client.bankingItemsN = client.bankItems3N;
			}
			if (client.bankingTab == 4) {
				client.bankingItems = client.bankItems4;
				client.bankingItemsN = client.bankItems4N;
			}
			if (client.bankingTab == 5) {
				client.bankingItems = client.bankItems5;
				client.bankingItemsN = client.bankItems5N;
			}
			if (client.bankingTab == 6) {
				client.bankingItems = client.bankItems6;
				client.bankingItemsN = client.bankItems6N;
			}
			if (client.bankingTab == 7) {
				client.bankingItems = client.bankItems7;
				client.bankingItemsN = client.bankItems7N;
			}
			if (client.bankingTab == 8) {
				client.bankingItems = client.bankItems8;
				client.bankingItemsN = client.bankItems8N;
			}
			client.getItems().resetItems(5064);
			client.getItems().rearrangeBank();
			client.getItems().resetBank();
			client.getItems().resetTempItems();
			client.getOutStream().createFrame(248);
			client.getOutStream().writeWordA(24959);
			client.getOutStream().writeWord(5063);
			client.flushOutStream();
		}
	}

	/**
	 * Take bank item that searched.
	 * 
	 * @param itemID
	 * @param fromSlot
	 * @param amount
	 * @param x
	 */
	public void takeOut(int itemID, int fromSlot, int amount, boolean x) {
		for (int j = 0; j < GameConfig.BANK_SIZE; j++) {
			if (client.bankItems[j] > 0) {
				if (client.bankItems[j] - 1 == itemID) {
					if (x) {// means their using remove x
						if (amount > client.getItems().freeSlots()
								& amount > client.bankItemsN[j]
								& client.bankItemsN[j] >= amount) {
							client.bankItemsN[j] -= client.getItems()
									.freeSlots();
							client.getItems().addItem(itemID,
									client.getItems().freeSlots());
						} else {
							client.getItems().addItem(itemID, amount);
							client.bankItemsN[j] -= amount;
							if (client.bankItemsN[j] == 0)
								client.bankItems[j] = 0;
							client.itemsN[fromSlot] = 0;
						}
					} else if (amount == -1) {// their using remove all
						if (client.bankItemsN[j] > client.getItems()
								.freeSlots()) {
							client.bankItemsN[j] -= client.getItems()
									.freeSlots();
							client.itemsN[fromSlot] -= client.getItems()
									.freeSlots();
							client.getItems().addItem(itemID,
									client.getItems().freeSlots());
						}
						client.getItems().addItem(itemID, client.bankItemsN[j]);
						client.bankItemsN[j] = 0;
						client.bankItems[j] = 0;
						client.items[fromSlot] = 0;
						client.itemsN[fromSlot] = 0;
						break;
					} else if ((client.bankItemsN[j] - amount) > 0) {
						if (amount > client.bankItemsN[j]) {
							if (!client.getItems().addItem(itemID,
									client.bankItemsN[j]))
								break;
							client.bankItemsN[j] -= client.bankItemsN[j];
							client.bankItems[j] = 0;
							client.itemsN[fromSlot] -= client.bankItemsN[j];
							client.items[fromSlot] = 0;
							break;
						}
						if (!client.getItems().addItem(itemID, amount))
							break;
						client.bankItemsN[j] -= amount;
						client.itemsN[fromSlot] -= amount;
						client.items[fromSlot] = 0;
						if (client.bankItemsN[j] == 0)
							client.bankItems[j] = 0;
					} else {
						if (amount > client.bankItemsN[j]) {
							if (!client.getItems().addItem(itemID,
									client.bankItemsN[j]))
								break;
							client.bankItems[j] = 0;
							client.bankItemsN[j] -= client.bankItemsN[j];
							client.itemsN[fromSlot] -= client.bankItemsN[j];
							client.items[fromSlot] = 0;
							break;
						}
						if (!client.getItems().addItem(itemID, amount))
							break;
						client.bankItems[j] = 0;
						client.bankItemsN[j] -= amount;
						client.items[fromSlot] = 0;
						client.itemsN[fromSlot] -= amount;
					}
				}
			}
		}
		client.getItems().resetTempItems();
		client.lastSearch = true;
		searchBank(client.searchName);
	}

	public void destroySearch() {
		client.lastSearch = false;
		client.isSearching = false;
		client.items = new int[500];
		client.itemsN = new int[500];
		client.searchName = "";
		client.getItems().resetTempItems();
		client.getItems().rearrangeBank();
		client.getItems().resetBank();
		client.getItems().resetKeepItems();
		sendFrame126("The Bank of OwnXile", 5383);
	}

	private void showItems(int items[], int itemsN[]) {
		client.getOutStream().createFrameVarSizeWord(53);
		client.getOutStream().writeWord(5382);
		client.getOutStream().writeWord(GameConfig.BANK_SIZE);
		for (int j = 0; j < items.length; j++) {
			if (items[j] > 254) {
				client.getOutStream().writeByte(255);
				client.getOutStream().writeDWord_v2(itemsN[j]);
			} else {
				client.getOutStream().writeByte(itemsN[j]);
			}
			if (itemsN[j] < 1) {
				items[j] = 0;
			}
			if (items[j] > GameConfig.ITEM_LIMIT | items[j] < 0) {
				items[j] = GameConfig.ITEM_LIMIT;
			}
			client.getOutStream().writeWordBigEndianA(items[j]);
		}
		client.getOutStream().endFrameVarSizeWord();
		client.flushOutStream();
	}

	public void searchBank(String str) {
		sendFrame126("The Bank of OwnXile", 5383);// didnt this change?
		client.items = new int[500];
		client.itemsN = new int[500];
		int p = 0;
		int slot = 0;
		for (int j = 0; j < client.bankItems.length; j++) {
			if (client.bankItems[j] > 0) {
				if (ItemAssistant.getItemName(client.bankItems[j] - 1)
						.toLowerCase().contains(str.toLowerCase())) {
					client.items[slot] = client.bankItems[j];
					client.itemsN[slot] = client.bankItemsN[j];
					slot++;
					p++;
				}
			}
		}
		if (p > 0) {
			sendFrame126("Bank of OwnXile - (search: '" + str + "')", 5383);
			showItems(client.items, client.itemsN);
			client.getItems().resetTempItems();
			slot = 0;
			client.isSearching = true;
		} else {
			if (client.lastSearch & client.isSearching) {
				destroySearch();
				return;
			} else {
				sendFrame126("No results were found for '" + str + "'.", 5383);
				client.isSearching = false;
			}
		}
	}

	public void openUpBankd() {
		if (client.inWild()) {
			client.sendMessage("You can't bank in the wilderness!");
			return;
		}
		if (client.isUltimateIronman()) {
			client.boxMessage("Ultimate Ironmen cannot use banks.");
			return;
		}
		if (client.getOutStream() != null && client != null) {
			client.isBanking = true;
			client.getItems().resetItems(5064);
			client.getItems().rearrangeBank();
			client.getItems().resetBank();
			client.getItems().resetTempItems();
			client.getOutStream().createFrame(248);
			client.getOutStream().writeWordA(23000);
			client.getOutStream().writeWord(5063);
			client.flushOutStream();
		}
	}

	public void pickCadava() {
		if (System.currentTimeMillis() - lastPick > 1000) {
			if (client.hasInventorySpace(1)) {
				lastPick = System.currentTimeMillis();
				client.startAnimation(2292);
				client.addItem(753);
				client.sendMessage("You pick some cadava berries from the bush.");
			} else {
				client.sendMessage("You do not have enough space in your inventory to pick from this bush.");

			}
		}
	}

	public void pickMonkeyNuts() {
		if (System.currentTimeMillis() - lastPick > 1000) {
			if (client.hasInventorySpace(1)) {
				lastPick = System.currentTimeMillis();
				client.startAnimation(2292);
				client.addItem(4012);
				client.sendMessage("You pick some monkey nuts from the bush.");
			} else {
				client.sendMessage("You do not have enough space in your inventory to pick from this bush.");

			}
		}
	}

	public boolean playerHasChicken() {
		return client.playerEquipment[client.playerWeapon] == 4566;
	}

	/**
	 * Following
	 **/

	public void playerWalk(int x, int y) {
		PlayerPathFinding.getPathFinder().findRoute(client, x, y, true, 1, 1);
	}

	public void playSong(int music) {
		if (music == client.currentSong) {
			return;
		}
		client.currentSong = music;
		client.getOutStream().createFrame(74);
		client.getOutStream().writeWordBigEndian(music);
	}

	public void playTempSong(int songID) {
		// client.outStream.createFrame(121);
		// client.outStream.writeWordBigEndian(songID);
		// client.outStream.writeWordBigEndian(15);
		// client.flushOutStream();
	}

	public void playTempSong(int songID, int songID2) {
		// client.outStream.createFrame(121);
		// client.outStream.writeWordBigEndian(songID);
		// client.outStream.writeWordBigEndian(songID2);
		// client.flushOutStream();
	}

	/**
	 * Drink AntiPosion Potions
	 * 
	 * @param itemId
	 *            The itemId
	 * @param itemSlot
	 *            The itemSlot
	 * @param newItemId
	 *            The new item After Drinking
	 * @param healType
	 *            The type of poison it heals
	 */
	public void potionPoisonHeal(int itemId, int itemSlot, int newItemId,
			int healType) {
		client.getItems();
		client.attackTimer = client.getCombat().getAttackDelay(
				ItemAssistant.getItemName(
						client.playerEquipment[client.playerWeapon])
						.toLowerCase());
		if (client.duelRule[5]) {
			client.sendMessage("Potions has been disabled in this duel!");
			return;
		}
		if (!client.isDead
				&& System.currentTimeMillis() - client.foodDelay > 2000) {
			if (client.getItems().playerHasItem(itemId, 1, itemSlot)) {
				client.sendMessage("You drink the "
						+ ItemAssistant.getItemName(itemId).toLowerCase() + ".");
				client.foodDelay = System.currentTimeMillis();
				// Actions
				if (healType == 1) {
					// Cures The Poison
				} else if (healType == 2) {
					// Cures The Poison + protects from getting poison again
				}
				client.startAnimation(0x33D);
				client.getItems().deleteItem(itemId, itemSlot, 1);
				client.getItems().addItem(newItemId, 1);
				requestUpdates();
			}
		}
	}

	public void processTeleport() {
		client.teleportToX = client.teleX;
		client.teleportToY = client.teleY;
		client.absZ = client.teleHeight;
		if (client.teleEndAnimation > 0) {
			client.startAnimation(client.teleEndAnimation);
		}

	}

	public void promoteUser(int right, String rankName, String target) {
		try {
			for (int i = 0; i < GameConfig.MAX_PLAYERS; i++) {
				if (PlayerHandler.players[i] != null) {
					if (PlayerHandler.players[i].playerName
							.equalsIgnoreCase(target)) {
						if (PlayerHandler.players[i].playerRights < right) {
							PlayerHandler.players[i].playerRights = right;
							// PlayerHandler.players[i].UpdateRights(right);
							client.sendMessage("You have successfully promoted the user to: @blu@"
									+ rankName);
							PlayerHandler.players[i].disconnected = true;
						} else {
							client.sendMessage("This command cannot be used to demote a player.");

						}
					}
				}
			}
		} catch (final Exception e) {
			client.sendMessage("Player Must Be Offline.");
		}

	}

	public void pullGambleLever() {
		client.startAnimation(2140);
		World.getSynchronizedTaskScheduler().schedule(new Task(3, false) {
			@Override
			protected void execute() {
				Gambling.getInstance().run(client);
				stop();
			}
		});
	}

	public void pullLever(final int obid, final int x, final int y,
			final int ox, final int oy, final int face) {
		client.startAnimation(2140);
		client.turnPlayerTo(client.objectX, client.objectY);
		World.getObjectHandler().globalObject2(2421, ox, oy, client.absZ, 5,
				face);
		World.getSynchronizedTaskScheduler().schedule(new Task(1, true) {
			private int i;

			@Override
			protected void execute() {
				i++;
				if (i == 3) {
					client.getFunction().startTeleport2(x, y, 0);
				}
				if (i == 8) {
					World.getObjectHandler().globalObject2(obid, ox, oy,
							client.absZ, 5, face);
					stop();
				}
			}
		});
	}

	public void punish(String who, int punishment) {
		who = who.toLowerCase();
		Client target = null;
		for (Player player : PlayerHandler.players) {
			if (player == null) {
				continue;
			}
			if (player.playerName.equalsIgnoreCase(who)) {
				target = (Client) player;
			}
		}
		switch (punishment) {
		case 1:// ipmute
			if (target != null) {
				FileConfig.mutedHosts.add(target.connectedFrom);
				infractionMessage(who, "host muted");
			} else {
				client.sendMessage("@blu@The player @dre@" + who
						+ " @blu@is currently offline.");
			}
			break;
		case 2:// mute
			if (!FileConfig.mutedAccounts.contains(who)) {
				FileConfig.mutedAccounts.add(who);
				infractionMessage(who, "muted");
			} else {
				client.sendMessage("@blu@The player @dre@" + who
						+ " @blu@is already muted.");
			}
			break;
		case 3:// ban
			if (!FileConfig.bannedAccounts.contains(who)) {
				FileConfig.bannedAccounts.add(who);
				infractionMessage(who, "banned");
				if (target != null) {
					target.disconnected = true;
				}
			} else {
				client.sendMessage("@blu@The player @dre@" + who
						+ " @blu@is already banned.");
			}
			break;
		case 4:// ipban
			if (target != null) {
				target.disconnected = true;
				PlayerHandler.disconnectAddress(target.connectedFrom);
				FileConfig.bannedHosts.add(target.connectedFrom);
				infractionMessage(who, "host banned");
			} else {
				client.sendMessage("@blu@The player @dre@" + who
						+ " @blu@is currently offline.");
			}
			break;
		case 5:// teletome??
			if (target != null) {
				movePlayer(target.getX(), target.getY(), target.getZ());
			} else {
				client.sendMessage("@blu@The player @dre@" + who
						+ " @blu@is currently offline.");
			}

			break;
		case 6:// kick
			if (target != null) {
				target.disconnected = true;
				infractionMessage(who, "kicked");
			} else {
				client.sendMessage("@blu@The player @dre@" + who
						+ " @blu@is currently offline.");
			}
			break;
		case 7:// unmute
			if (FileConfig.mutedAccounts.contains(who)) {
				FileConfig.mutedAccounts.remove(who);
				infractionMessage(who, "unmuted");
			} else {
				client.sendMessage("@blu@The player @dre@" + who
						+ " @blu@is not muted.");
			}
			break;
		case 8:// unipmute
			if (target != null) {
				if (FileConfig.mutedHosts.contains(target.connectedFrom)) {
					FileConfig.mutedHosts.remove(target.connectedFrom);
					infractionMessage(who, "removed the host mute for");
				} else {
					client.sendMessage("@blu@The host @dre@"
							+ target.connectedFrom + " @blu@is not muted.");
				}
			} else {
				client.sendMessage("@blu@The player @dre@" + who
						+ " @blu@is currently offline.");
			}
			break;
		case 9:// jail
			if (target != null) {
				if (target.jail == 1) {
					client.sendMessage("The player @dre@" + target.playerName
							+ "@bla@ is already in jail.");
				} else if (target.underAttackBy > 0
						|| target.underAttackBy2 > 0) {
					client.sendMessage("You cannot jail a player who is in combat.");
				} else {
					target.teleportToX = 2097;
					target.teleportToY = 4430;
					target.jail = 1;
					infractionMessage(who, "imprisoned");
				}
			}
			break;
		case 10:
			if (target != null) {
				if (target.jail == 0) {
					client.sendMessage("The player @dre@" + target.playerName
							+ "@bla@ is not in jail.");
				} else if (!target.inMinigame()) {
					target.teleBlockDelay = 0;
					target.jail = 0;
					target.duelStatus = 0;
					target.teleportToX = GameConfig.RESPAWN_X;
					target.teleportToY = GameConfig.RESPAWN_Y;
					infractionMessage(who, "released");
				}
			}
			break;
		case 11:// perm ban
			if (!FileConfig.permBannedAccounts.contains(who)) {
				FileConfig.permBannedAccounts.add(who);
				infractionMessage(who, "permanently banned");
				if (target != null) {
					target.disconnected = true;
				}
			} else {
				client.sendMessage("@blu@The player @dre@" + who
						+ " @blu@has already been permanently banned.");
			}
			break;
		}
	}

	public int randomBarrows() {
		return barrows[(int) (Math.random() * barrows.length)];
	}

	public int randomPots() {
		return pots[(int) (Math.random() * pots.length)];
	}

	public int randomRunes() {
		return runes[(int) (Math.random() * runes.length)];
	}

	public void refreshSkill(int i) {
		client.calcCombat();
		switch (i) {
		case 0:
			sendFrame126("" + client.playerLevel[0] + "", 4004);
			sendFrame126("" + getLevelForXP(client.playerXP[0]) + "", 4005);
			sendFrame126("" + client.playerXP[0] + "", 4044);
			sendFrame126(
					"" + getXPForLevel(getLevelForXP(client.playerXP[0]) + 1)
							+ "", 4045);
			break;

		case 1:
			sendFrame126("" + client.playerLevel[1] + "", 4008);
			sendFrame126("" + getLevelForXP(client.playerXP[1]) + "", 4009);
			sendFrame126("" + client.playerXP[1] + "", 4056);
			sendFrame126(
					"" + getXPForLevel(getLevelForXP(client.playerXP[1]) + 1)
							+ "", 4057);
			break;

		case 2:
			sendFrame126("" + client.playerLevel[2] + "", 4006);
			sendFrame126("" + getLevelForXP(client.playerXP[2]) + "", 4007);
			sendFrame126("" + client.playerXP[2] + "", 4050);
			sendFrame126(
					"" + getXPForLevel(getLevelForXP(client.playerXP[2]) + 1)
							+ "", 4051);
			break;

		case 3:
			sendFrame126("" + client.playerLevel[3] + "", 4016);
			sendFrame126("" + getLevelForXP(client.playerXP[3]) + "", 4017);
			sendFrame126("" + client.playerXP[3] + "", 4080);
			sendFrame126(
					"" + getXPForLevel(getLevelForXP(client.playerXP[3]) + 1)
							+ "", 4081);
			break;

		case 4:
			sendFrame126("" + client.playerLevel[4] + "", 4010);
			sendFrame126("" + getLevelForXP(client.playerXP[4]) + "", 4011);
			sendFrame126("" + client.playerXP[4] + "", 4062);
			sendFrame126(
					"" + getXPForLevel(getLevelForXP(client.playerXP[4]) + 1)
							+ "", 4063);
			break;

		case 5:
			sendFrame126("" + client.playerLevel[5] + "", 4012);
			sendFrame126("" + getLevelForXP(client.playerXP[5]) + "", 4013);
			sendFrame126("" + client.playerXP[5] + "", 4068);
			sendFrame126(
					"" + getXPForLevel(getLevelForXP(client.playerXP[5]) + 1)
							+ "", 4069);
			sendFrame126("" + client.playerLevel[5] + "/"
					+ getLevelForXP(client.playerXP[5]) + "", 687);// Prayer
																	// frame
			break;

		case 6:
			sendFrame126("" + client.playerLevel[6] + "", 4014);
			sendFrame126("" + getLevelForXP(client.playerXP[6]) + "", 4015);
			sendFrame126("" + client.playerXP[6] + "", 4074);
			sendFrame126(
					"" + getXPForLevel(getLevelForXP(client.playerXP[6]) + 1)
							+ "", 4075);
			break;

		case 7:
			sendFrame126("" + client.playerLevel[7] + "", 4034);
			sendFrame126("" + getLevelForXP(client.playerXP[7]) + "", 4035);
			sendFrame126("" + client.playerXP[7] + "", 4134);
			sendFrame126(
					"" + getXPForLevel(getLevelForXP(client.playerXP[7]) + 1)
							+ "", 4135);
			break;

		case 8:
			sendFrame126("" + client.playerLevel[8] + "", 4038);
			sendFrame126("" + getLevelForXP(client.playerXP[8]) + "", 4039);
			sendFrame126("" + client.playerXP[8] + "", 4146);
			sendFrame126(
					"" + getXPForLevel(getLevelForXP(client.playerXP[8]) + 1)
							+ "", 4147);
			break;

		case 9:
			sendFrame126("" + client.playerLevel[9] + "", 4026);
			sendFrame126("" + getLevelForXP(client.playerXP[9]) + "", 4027);
			sendFrame126("" + client.playerXP[9] + "", 4110);
			sendFrame126(
					"" + getXPForLevel(getLevelForXP(client.playerXP[9]) + 1)
							+ "", 4111);
			break;

		case 10:
			sendFrame126("" + client.playerLevel[10] + "", 4032);
			sendFrame126("" + getLevelForXP(client.playerXP[10]) + "", 4033);
			sendFrame126("" + client.playerXP[10] + "", 4128);
			sendFrame126(""
					+ getXPForLevel(getLevelForXP(client.playerXP[10]) + 1)
					+ "", 4129);
			break;

		case 11:
			sendFrame126("" + client.playerLevel[11] + "", 4036);
			sendFrame126("" + getLevelForXP(client.playerXP[11]) + "", 4037);
			sendFrame126("" + client.playerXP[11] + "", 4140);
			sendFrame126(""
					+ getXPForLevel(getLevelForXP(client.playerXP[11]) + 1)
					+ "", 4141);
			break;

		case 12:
			sendFrame126("" + client.playerLevel[12] + "", 4024);
			sendFrame126("" + getLevelForXP(client.playerXP[12]) + "", 4025);
			sendFrame126("" + client.playerXP[12] + "", 4104);
			sendFrame126(""
					+ getXPForLevel(getLevelForXP(client.playerXP[12]) + 1)
					+ "", 4105);
			break;

		case 13:
			sendFrame126("" + client.playerLevel[13] + "", 4030);
			sendFrame126("" + getLevelForXP(client.playerXP[13]) + "", 4031);
			sendFrame126("" + client.playerXP[13] + "", 4122);
			sendFrame126(""
					+ getXPForLevel(getLevelForXP(client.playerXP[13]) + 1)
					+ "", 4123);
			break;

		case 14:
			sendFrame126("" + client.playerLevel[14] + "", 4028);
			sendFrame126("" + getLevelForXP(client.playerXP[14]) + "", 4029);
			sendFrame126("" + client.playerXP[14] + "", 4116);
			sendFrame126(""
					+ getXPForLevel(getLevelForXP(client.playerXP[14]) + 1)
					+ "", 4117);
			break;

		case 15:
			sendFrame126("" + client.playerLevel[15] + "", 4020);
			sendFrame126("" + getLevelForXP(client.playerXP[15]) + "", 4021);
			sendFrame126("" + client.playerXP[15] + "", 4092);
			sendFrame126(""
					+ getXPForLevel(getLevelForXP(client.playerXP[15]) + 1)
					+ "", 4093);
			break;

		case 16:
			sendFrame126("" + client.playerLevel[16] + "", 4018);
			sendFrame126("" + getLevelForXP(client.playerXP[16]) + "", 4019);
			sendFrame126("" + client.playerXP[16] + "", 4086);
			sendFrame126(""
					+ getXPForLevel(getLevelForXP(client.playerXP[16]) + 1)
					+ "", 4087);
			break;

		case 17:
			sendFrame126("" + client.playerLevel[17] + "", 4022);
			sendFrame126("" + getLevelForXP(client.playerXP[17]) + "", 4023);
			sendFrame126("" + client.playerXP[17] + "", 4098);
			sendFrame126(""
					+ getXPForLevel(getLevelForXP(client.playerXP[17]) + 1)
					+ "", 4099);
			break;

		case 18:
			sendFrame126("" + client.playerLevel[18] + "", 12166);
			sendFrame126("" + getLevelForXP(client.playerXP[18]) + "", 12167);
			sendFrame126("" + client.playerXP[18] + "", 12171);
			sendFrame126(""
					+ getXPForLevel(getLevelForXP(client.playerXP[18]) + 1)
					+ "", 12172);
			break;

		case 19:
			sendFrame126("" + client.playerLevel[19] + "", 13926);
			sendFrame126("" + getLevelForXP(client.playerXP[19]) + "", 13927);
			sendFrame126("" + client.playerXP[19] + "", 13921);
			sendFrame126(""
					+ getXPForLevel(getLevelForXP(client.playerXP[19]) + 1)
					+ "", 13922);
			break;

		case 20:
			sendFrame126("" + client.playerLevel[20] + "", 4152);
			sendFrame126("" + getLevelForXP(client.playerXP[20]) + "", 4153);
			sendFrame126("" + client.playerXP[20] + "", 4157);
			sendFrame126(""
					+ getXPForLevel(getLevelForXP(client.playerXP[20]) + 1)
					+ "", 4158);
			break;
		}
	}

	public void removeAllWindows() {
		if (client.cannotCloseWindows) {
			return;
		}
		if (client.getOutStream() != null && client != null) {
			resetVariables();
			client.getOutStream().createFrame(219);
			client.flushOutStream();
		}
	}

	public void requestUpdates() {
		client.updateRequired = true;
		client.getUpdateFlags().setAppearanceUpdateRequired(true);
	}

	/**
	 * reseting animation
	 **/
	public void resetAnimation() {
		client.getItems();
		client.getCombat().getPlayerAnimIndex(
				ItemAssistant.getItemName(
						client.playerEquipment[client.playerWeapon])
						.toLowerCase());
		client.startAnimation(client.playerStandIndex);
		requestUpdates();
	}

	public void resetAutocast() {
		client.autocastId = 0;
		client.autocasting = false;
		sendFrame36(108, 0);
	}

	public void resetBarrows() {
		client.barrowsNpcs[0][1] = 0;
		client.barrowsNpcs[1][1] = 0;
		client.barrowsNpcs[2][1] = 0;
		client.barrowsNpcs[3][1] = 0;
		client.barrowsNpcs[4][1] = 0;
		client.barrowsNpcs[5][1] = 0;
		client.barrowsKillCount = 0;
		client.randomCoffin = Misc.random(3) + 1;
	}

	public void resetCamera() {
		client.outStream.createFrame(107);
		client.updateRequired = true;
		client.getUpdateFlags().appearanceUpdateRequired = true;
	}

	public void resetCombatLevels() {
		for (int i = 0; i < 7; i++) {
			if (i == 3) {
				client.playerLevel[i] = 10;
				client.playerXP[i] = 1300;
				refreshSkill(i);
			} else {
				client.playerLevel[i] = 1;
				client.playerXP[i] = 0;
				refreshSkill(i);
			}
		}
	}

	public void resetLevel(int i) {
		client.playerLevel[i] = 1;
		client.playerXP[i] = 0;
		refreshSkill(i);
	}

	public void resetDamageDone() {
		for (int i = 0; i < PlayerHandler.players.length; i++) {
			if (PlayerHandler.players[i] != null) {
				PlayerHandler.players[i].damageTaken[client.playerId] = 0;
			}
		}
	}

	public void resetFollow() {
		client.followId = 0;
		client.followId2 = 0;
		client.followId3 = 0;
		client.mageFollow = false;
	}

	public void resetFollowers() {
		for (Player player : PlayerHandler.players) {
			if (player != null) {
				if (player.followId == client.playerId
						|| player.followId3 == client.playerId) {
					resetFollow();
				}
			}
		}
	}

	public void resetStats() {
		for (int i = 0; i < 20; i++) {
			client.playerLevel[i] = client.getFunction().getLevelForXP(
					client.playerXP[i]);
			client.getFunction().refreshSkill(i);
		}
	}

	public void resetTb() {
		client.teleBlockLength = 0;
		client.teleBlockDelay = 0;
	}

	public void resetTzhaar() {
		client.waveId = -1;
		client.tzhaarToKill = -1;
		client.tzhaarKilled = -1;
		movePlayer(2438, 5168, 0);
	}

	public void resetVariables() {
		client.getCrafting().resetCrafting();
		client.smeltInterface = false;
		client.smeltType = 0;
		client.smeltAmount = 0;
		client.woodcut[0] = client.woodcut[1] = client.woodcut[2] = 0;
		client.mining[0] = client.mining[1] = client.mining[2] = 0;
	}

	public void say(String s) {
		sendFrame200(969, 591);
		sendFrame126(client.playerName, 970);
		sendFrame126(s, 971);
		sendFrame185(969);
		sendFrame164(968);
		client.nextChat = 0;
	}

	public void say2(String s, int anim) {
		sendFrame200(969, anim);
		sendFrame126(client.playerName, 970);
		sendFrame126(s, 971);
		sendFrame185(969);
		sendFrame164(968);
		client.nextChat = 0;
	}

	public void sendClan(String name, String message, String clan, int rights) {
		client.outStream.createFrameVarSizeWord(217);
		client.outStream.writeString(name);
		client.outStream.writeString(message);
		client.outStream.writeString(clan);
		client.outStream.writeWord(GameConfig.getClanCrown(rights));
		client.outStream.endFrameVarSize();
	}

	public void sendClanStyleMessage(String clan, String name, String s) {
		client.sendMessage("[@blu@" + clan + "@bla@]: "
				+ Misc.formatPlayerName(name) + ": @dre@" + Misc.ucFirst(s));
	}

	public void sendConfig(int id, int value) {
		if (client.getOutStream() != null && client != null) {
			if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
				client.getOutStream().createFrame(86);
				client.getOutStream().writeWordBigEndian(id);
				client.getOutStream().writeDWord(value);
			} else {
				client.getOutStream().createFrame(36);
				client.getOutStream().writeWordBigEndian(id);
				client.getOutStream().writeByte(value);
			}
			client.flushOutStream();
		}
	}

	public void sendCrashFrame() {
		if (client.getOutStream() != null && client != null) {
			client.getOutStream().createFrame(123);
			client.flushOutStream();
		}
	}

	public void sendFrame106(int sideIcon) {
		if (client.getOutStream() != null && client != null) {
			client.getOutStream().createFrame(106);
			client.getOutStream().writeByteC(sideIcon);
			client.flushOutStream();
			requestUpdates();
		}
	}

	public void sendFrame107() {
		if (client.getOutStream() != null && client != null) {
			client.getOutStream().createFrame(107);
			client.flushOutStream();
		}
	}

	public void sendFrame126(String s, int id) {
		if (!checkPacket126Update(s, id)) {
			return;
		}
		if (client.getOutStream() != null && client != null) {
			client.getOutStream().createFrameVarSizeWord(126);
			client.getOutStream().writeString(s);
			client.getOutStream().writeWordA(id);
			client.getOutStream().endFrameVarSizeWord();
			client.flushOutStream();
		}
	}

	public void sendNewFrame126(String s, int id) {

		if (client.getOutStream() != null && client != null) {
			client.getOutStream().createFrameVarSizeWord(126);
			client.getOutStream().writeString(s);
			client.getOutStream().writeWordA(id);
			client.getOutStream().endFrameVarSizeWord();
			client.flushOutStream();
		}
	}

	public void sendFrame164(int Frame) {
		if (client.getOutStream() != null && client != null) {
			client.getOutStream().createFrame(164);
			client.getOutStream().writeWordBigEndian_dup(Frame);
			client.flushOutStream();
		}
	}

	public void sendFrame171(int MainFrame, int SubFrame) {
		if (client.getOutStream() != null && client != null) {
			client.getOutStream().createFrame(171);
			client.getOutStream().writeByte(MainFrame);
			client.getOutStream().writeWord(SubFrame);
			client.flushOutStream();
		}
	}

	public void sendFrame185(int Frame) {
		if (client.getOutStream() != null && client != null) {
			client.getOutStream().createFrame(185);
			client.getOutStream().writeWordBigEndianA(Frame);
		}
	}

	public void sendFrame200(int MainFrame, int SubFrame) {
		if (client.getOutStream() != null && client != null) {
			client.getOutStream().createFrame(200);
			client.getOutStream().writeWord(MainFrame);
			client.getOutStream().writeWord(SubFrame);
			client.flushOutStream();
		}
	}

	public void sendFrame246(int MainFrame, int SubFrame, int SubFrame2) {
		if (client.getOutStream() != null && client != null) {
			client.getOutStream().createFrame(246);
			client.getOutStream().writeWordBigEndian(MainFrame);
			client.getOutStream().writeWord(SubFrame);
			client.getOutStream().writeWord(SubFrame2);
			client.flushOutStream();
		}
	}

	public void sendFrame248(int MainFrame, int SubFrame) {
		if (client.getOutStream() != null && client != null) {
			client.getOutStream().createFrame(248);
			client.getOutStream().writeWordA(MainFrame);
			client.getOutStream().writeWord(SubFrame);
			client.flushOutStream();
		}
	}

	public void sendFrame34(int id, int slot, int column, int amount) {
		if (client.getOutStream() != null && client != null) {
			client.outStream.createFrameVarSizeWord(34);
			client.outStream.writeWord(column);
			client.outStream.writeByte(4);
			client.outStream.writeDWord(slot);
			client.outStream.writeWord(id + 1);
			client.outStream.writeByte(amount);
			client.outStream.endFrameVarSizeWord();
		}
	}

	public void sendFrame36(int id, int state) {
		if (client.getOutStream() != null && client != null) {
			client.getOutStream().createFrame(36);
			client.getOutStream().writeWordBigEndian(id);
			client.getOutStream().writeByte(state);
			client.flushOutStream();
		}
	}

	public void sendFrame70(int i, int o, int id) {
		if (client.getOutStream() != null && client != null) {
			client.getOutStream().createFrame(70);
			client.getOutStream().writeWord(i);
			client.getOutStream().writeWordBigEndian(o);
			client.getOutStream().writeWordBigEndian(id);
			client.flushOutStream();
		}
	}

	public void sendFrame75(int MainFrame, int SubFrame) {
		if (client.getOutStream() != null && client != null) {
			client.getOutStream().createFrame(75);
			client.getOutStream().writeWordBigEndianA(MainFrame);
			client.getOutStream().writeWordBigEndianA(SubFrame);
			client.flushOutStream();
		}
	}

	public void sendFrame87(int id, int state) {
		if (client.getOutStream() != null && client != null) {
			client.getOutStream().createFrame(87);
			client.getOutStream().writeWordBigEndian_dup(id);
			client.getOutStream().writeDWord_v1(state);
			client.flushOutStream();
		}
	}

	public void sendFrame991(int state) {
		if (client.getOutStream() != null && client != null) {
			if (mapStatus != state) {
				mapStatus = state;
				client.getOutStream().createFrame(99);
				client.getOutStream().writeByte(state);
				client.flushOutStream();
			}
		}
	}

	public void sendLink(String s) {
		if (client.getOutStream() != null && client != null) {
			client.getOutStream().createFrameVarSizeWord(187);
			client.getOutStream().writeString(s);
		}
	}

	public void sendMessageAll(String Message) {
		for (Player player : PlayerHandler.players) {
			if (player != null) {
				final Client c2 = (Client) player;
				c2.sendMessage(Message);
			}
		}
	}

	public void sendPM(long name, int rights, byte[] chatmessage,
			int messagesize) {
		client.getOutStream().createFrameVarSize(196);
		client.getOutStream().writeQWord(name);
		client.getOutStream().writeDWord(client.lastChatId++);
		client.getOutStream().writeByte(GameConfig.getClanCrown(rights));
		client.getOutStream().writeBytes(chatmessage, messagesize, 0);
		client.getOutStream().endFrameVarSize();
		client.flushOutStream();
		Misc.textUnpack(chatmessage, messagesize);
		client.getFunction().longToPlayerName(name);
	}

	public void sendYellAll2(String channel, String message) {
		for (Player player : PlayerHandler.players) {
			if (player == null) {
				continue;
			}
			Client c = (Client) player;
			c.getFunction().sendClan(client.playerName, message, channel,
					client.playerRights);

		}
		// Engine.getIRCBot().sendMessage(player.playerName + ": " + message);
	}

	public void setChatOptions(int publicChat, int privChat, int tradeBlock) {
		if (client.getOutStream() != null && client != null) {
			client.getOutStream().createFrame(206);
			client.getOutStream().writeByte(publicChat);
			client.getOutStream().writeByte(privChat);
			client.getOutStream().writeByte(tradeBlock);
			client.flushOutStream();
		}
	}

	public void setConfig(int id, int state) {
		client.outStream.createFrame(36);
		client.outStream.writeWordBigEndian(id);
		client.outStream.writeByte(state);

	}

	public void setPrivateMessaging(int i) {
		if (client.getOutStream() != null && client != null) {
			client.getOutStream().createFrame(221);
			client.getOutStream().writeByte(i);
			client.flushOutStream();
		}
	}

	public void setSkillLevel(int skillNum, int currentLevel, int XP) {
		if (client.getOutStream() != null && client != null) {
			client.getOutStream().createFrame(134);
			client.getOutStream().writeByte(skillNum);
			client.getOutStream().writeDWord_v1(XP);
			client.getOutStream().writeByte(currentLevel);
			client.flushOutStream();
		}
	}

	public boolean setStat(int statId, int newLevel, String skillName) {
		if (client.inWild()) {
			return false;
		}
		for (int element : client.playerEquipment) {
			if (element > 0) {
				client.sendMessage("Please take all your armour and weapons off before using this command.");
				return false;
			}
		}
		try {
			client.playerXP[statId] = getXPForLevel(newLevel) + 5;
			client.playerLevel[statId] = getLevelForXP(client.playerXP[statId]);
			refreshSkill(statId);
			client.sendMessage("You have changed the " + skillName
					+ " level to " + newLevel + ".");
			return true;
		} catch (final Exception e) {
		}
		return false;

	}

	public void shakeScreen(int verticleAmount, int verticleSpeed,
			int horizontalAmount, int horizontalSpeed) {
		client.outStream.createFrame(35);
		client.outStream.writeByte(verticleAmount);
		client.outStream.writeByte(verticleSpeed);
		client.outStream.writeByte(horizontalAmount);
		client.outStream.writeByte(horizontalSpeed);
	}

	public void showInfo(String title, String information) {
		sendFrame126(information, 357);
		sendFrame126("Click here to continue", 358);
		sendFrame164(356);
		client.nextChat = 0;
	}

	public void showInterface(int interfaceid) {
		if (client.getOutStream() != null && client != null) {
			client.getOutStream().createFrame(97);
			client.getOutStream().writeWord(interfaceid);
			client.flushOutStream();
		}
	}

	public void showOption(int i, int l, String s) {
		if (client.getOutStream() != null && client != null) {
			if (!optionType.equalsIgnoreCase(s)) {
				optionType = s;
				client.getOutStream().createFrameVarSize(104);
				client.getOutStream().writeByteC(i);
				client.getOutStream().writeByteA(l);
				client.getOutStream().writeString(s);
				client.getOutStream().endFrameVarSize();
				client.flushOutStream();
			}
		}
	}

	public void spawnHim() {
		World.getNpcHandler().spawnNpc(client, 1264, client.getX() + 1,
				client.getY(), -1, 0, 120, 20, 200, 200, true, true);

	}

	/**
	 * Teleporting
	 **/
	public boolean spellTeleport(int x, int y, int height) {
		return startTeleport(x, y, height,
				client.playerMagicBook == 1 ? "ancient" : "modern");
	}

	public void spinCamera(int x, int y, int height, int speed, int angle) {
		client.outStream.createFrame(166);
		client.outStream.writeByte(x);
		client.outStream.writeByte(y);
		client.outStream.writeWord(height);
		client.outStream.writeByte(speed);
		client.outStream.writeByte(angle);
	}

	public void spiritTreeTeleport(final int x, final int y, final int z) {
		client.gfx0(1228);
		closeAllWindows();
		client.startAnimation(4731);
		World.getSynchronizedTaskScheduler().schedule(new Task(3, false) {
			protected void execute() {
				client.getFunction().movePlayer(x, y, z);
				client.startAnimation(715);
				stop();
			}
		});
	}

	public void startInfo(String text, String text1, String text2,
			String text3, String text4) {
		sendFrame126(text, 6180);
		sendFrame126(text1, 6181);
		sendFrame126(text2, 6182);
		sendFrame126(text3, 6183);
		sendFrame126(text4, 6184);
		sendFrame164(6179);
	}

	public boolean startTeleport(int x, int y, int height, String teleportType) {
		if (!client.isDead && client.teleTimer == 0 && canTeleport(true)) {
			if (client.playerIndex > 0 || client.npcIndex > 0) {
				client.getCombat().resetPlayerAttack();
			}
			client.stopMovement();
			client.lastTeleport = System.currentTimeMillis();
			removeAllWindows();
			client.teleX = x;
			client.teleY = y;
			client.npcIndex = 0;
			client.playerIndex = 0;
			client.faceUpdate(0);
			client.teleHeight = height;

			if (teleportType.equalsIgnoreCase("modern")) {
				client.startAnimation(714);
				client.teleTimer = 11;
				client.teleGfx = 308;
				client.teleEndAnimation = 715;
			}
			if (teleportType.equalsIgnoreCase("ancient")) {
				client.startAnimation(1979);
				client.teleGfx = 0;
				client.teleTimer = 11;
				client.teleEndAnimation = 0;
				client.gfx0(392);//392
			}
			return true;
		}
		return false;
	}

	public boolean startTeleport2(int x, int y, int height) {
		if (!client.isDead && client.teleTimer == 0 && canTeleport(false)) {
			client.stopMovement();
			client.lastTeleport = System.currentTimeMillis();
			removeAllWindows();
			client.teleX = x;
			client.teleY = y;
			client.npcIndex = 0;
			client.playerIndex = 0;
			client.faceUpdate(0);
			client.teleHeight = height;
			client.startAnimation(714);
			client.teleGfx = 308;
			client.teleTimer = 11;
			client.teleEndAnimation = 715;
			return true;
		}
		return false;
	}

	public void state(String s) {
		sendFrame126(s, 357);
		sendFrame126("Click here to continue", 358);
		sendFrame164(356);
		client.nextChat = 0;
	}

	public void state(String s, String s2) {
		sendFrame126(s, 360);
		sendFrame126(s2, 361);
		sendFrame126("Click here to continue", 362);
		sendFrame164(359);
		client.nextChat = 0;
	}

	public void state(String s, String s2, String s3) {
		sendFrame126(s, 364);
		sendFrame126(s2, 365);
		sendFrame126(s3, 366);
		sendFrame126("Click here to continue", 367);
		sendFrame164(363);
		client.nextChat = 0;
	}

	public void state(String s, String s2, String s3, String s4) {
		sendFrame126(s, 369);
		sendFrame126(s2, 370);
		sendFrame126(s3, 371);
		sendFrame126(s4, 372);
		sendFrame126("Click here to continue", 373);
		sendFrame164(368);
		client.nextChat = 0;
	}

	/**
	 ** GFX
	 **/
	public void stillGfx(int id, int x, int y, int height, int time) {
		if (client.getOutStream() != null && client != null) {
			client.getOutStream().createFrame(85);
			client.getOutStream().writeByteC(y - client.getMapRegionY() * 8);
			client.getOutStream().writeByteC(x - client.getMapRegionX() * 8);
			client.getOutStream().createFrame(4);
			client.getOutStream().writeByte(0);
			client.getOutStream().writeWord(id);
			client.getOutStream().writeByte(height);
			client.getOutStream().writeWord(time);
			client.flushOutStream();
		}
	}

	public void stopDiagonal(int otherX, int otherY) {
		if (client.freezeDelay > 0) {
			return;
		}
		if (client.freezeTimer > 0) {
			return;
		}
		client.newWalkCmdSteps = 1;
		int xMove = otherX - client.getX();
		int yMove = 0;
		if (!canWalk(xMove, yMove)) {
			xMove = 0;
		}

		if (xMove == 0) {
			yMove = otherY - client.getY();
		}

		if (!canWalk(xMove, yMove)) {
			yMove = 0;
		}

		int k = client.getX() + xMove;
		k -= client.mapRegionX * 8;
		client.getNewWalkCmdX()[0] = client.getNewWalkCmdY()[0] = 0;
		int l = client.getY() + yMove;
		l -= client.mapRegionY * 8;

		for (int n = 0; n < client.newWalkCmdSteps; n++) {
			client.getNewWalkCmdX()[n] += k;
			client.getNewWalkCmdY()[n] += l;
		}

	}

	public void teleport(final int x, final int y, final int z) {
		if (client.playerLevel[3] < 1)
			return;
		client.gfx0(342);
		client.startAnimation(1816);
		closeAllWindows();
		World.getSynchronizedTaskScheduler().schedule(new Task(2, false) {
			protected void execute() {
				client.getFunction().movePlayer(x, y, z);
				client.startAnimation(715);
				stop();
			}
		});
	}

	public void updatePM(int pID, int world) { // used for private chat updates
		Player p = PlayerHandler.players[pID];
		if (p == null || p.playerName == null || p.playerName.equals("null")) {
			return;
		}
		Client o = (Client) p;
		long l = Misc.playerNameToInt64(PlayerHandler.players[pID].playerName);

		if (p.privChat == 0) {
			for (long friend : client.friends) {
				if (friend != 0) {
					if (l == friend) {
						loadPM(l, world);
						return;
					}
				}
			}
		} else if (p.privChat == 1) {
			for (long friend : client.friends) {
				if (friend != 0) {
					if (l == friend) {
						if (o.getFunction().isInPM(
								Misc.playerNameToInt64(client.playerName))) {
							loadPM(l, world);
							return;
						} else {
							loadPM(l, 0);
							return;
						}
					}
				}
			}
		} else if (p.privChat == 2) {
			for (long friend : client.friends) {
				if (friend != 0) {
					if (l == friend && client.playerRights < 2) {
						loadPM(l, 0);
						return;
					}
				}
			}
		}
	}

	public void updatePrivateChat() {
		setPrivateMessaging(2);
		for (int i1 = 0; i1 < GameConfig.MAX_PLAYERS; i1++) {
			Player p = PlayerHandler.players[i1];
			if (p != null && p.isActive) {
				Client o = (Client) p;
				if (o != null) {
					o.getFunction().updatePM(client.playerId, 1);
				}
			}
		}
		boolean pmLoaded = false;

		for (long friend : client.friends) {
			if (friend != 0) {
				for (int i2 = 1; i2 < GameConfig.MAX_PLAYERS; i2++) {
					Player p = PlayerHandler.players[i2];
					if (p != null && p.isActive
							&& Misc.playerNameToInt64(p.playerName) == friend) {
						Client o = (Client) p;
						if (o != null) {
							if (client.playerRights >= 2
									|| p.privChat == 0
									|| p.privChat == 1
									&& o.getFunction()
											.isInPM(Misc
													.playerNameToInt64(client.playerName))) {
								loadPM(friend, 1);
								pmLoaded = true;
							}
							break;
						}
					}
				}
				if (!pmLoaded) {
					loadPM(friend, 0);
				}
				pmLoaded = false;
			}
			for (int i1 = 1; i1 < GameConfig.MAX_PLAYERS; i1++) {
				Player p = PlayerHandler.players[i1];
				if (p != null && p.isActive) {
					Client o = (Client) p;
					if (o != null) {
						o.getFunction().updatePM(client.playerId, 1);
					}
				}
			}
		}
	}

	public void vengMe() {
		if (System.currentTimeMillis() - client.lastVeng > 30000) {
			if (client.getItems().playerHasItem(557, 10)
					&& client.getItems().playerHasItem(9075, 4)
					&& client.getItems().playerHasItem(560, 2)) {
				client.vengOn = true;
				client.lastVeng = System.currentTimeMillis();
				client.startAnimation(4410);
				client.gfx100(726);
				client.getItems().deleteItem(557,
						client.getItems().getItemSlot(557), 10);
				client.getItems().deleteItem(560,
						client.getItems().getItemSlot(560), 2);
				client.getItems().deleteItem(9075,
						client.getItems().getItemSlot(9075), 4);
			} else {
				client.sendMessage("You do not have the required runes to cast this spell. (9075 for astrals)");
			}
		} else {
			client.sendMessage("You must wait 30 seconds before casting this again.");
		}
	}

	public void vengOther() {
		Client c2 = (Client) PlayerHandler.players[client.playerIndex];
		if (!c2.acceptAid) {
			client.boxMessage("This player has accept aid turned off.");
			return;
		}
		if (System.currentTimeMillis() - client.lastVeng > 30000) {
			if (client.getItems().playerHasItem(557, 10)
					&& client.getItems().playerHasItem(9075, 4)
					&& client.getItems().playerHasItem(560, 2)) {
				c2.vengOn = true;
				client.lastVeng = System.currentTimeMillis();
				client.startAnimation(4411);

				client.gfx100(725);
				client.turnPlayerTo(c2.getX(), c2.getY());
				c2.sendMessage(client.playerName
						+ " gives you the power of Vengeance!");
				client.getItems().deleteItem(557,
						client.getItems().getItemSlot(557), 10);
				client.getItems().deleteItem(560,
						client.getItems().getItemSlot(560), 2);
				client.getItems().deleteItem(9075,
						client.getItems().getItemSlot(9075), 4);
			} else {
				client.sendMessage("You do not have the required runes to cast this spell.");
			}
		} else {
			client.sendMessage("You must wait 30 seconds before casting this again.");
		}
	}

	public void viewingOrb() {
		movePlayer(2398, 5150, 0);
		for (int i = 0; i < 14; i++) {
			client.setSidebarInterface(i, -1);
			client.setSidebarInterface(0, -1);
		}
		client.orb = true;
		client.setSidebarInterface(3, 3209);
		closeAllWindows();
		becomeNpc(3642);
	}

	public void voteReward(String date) {
		giveVoteReward(date);
		client.boxMessage("Thanks for voting.");
		if (World.votes > 4) {
			World.sendMessage("@blu@" + World.voterNames[0] + ", "
					+ World.voterNames[1] + ", " + World.voterNames[2] + ", "
					+ World.voterNames[3] + " and " + World.voterNames[4]
					+ " have all voted.");
			World.sendMessage("@blu@They received a combined total of "
					+ Correction.getAmountString(World.totalCashAmount)
					+ " and " + World.totalRPAmount + " OXP.");
			World.votes = 0;
			World.totalCashAmount = 0;
			World.totalRPAmount = 0;
			for (int i = 0; i < 5; i++) {
				World.voterNames[i] = null;
			}
		}
	}

	public void walkableInterface(int id) {
		if (id != client.walkableInterfaceId) {
			client.walkableInterfaceId = id;
			if (client.getOutStream() != null && client != null) {
				client.getOutStream().createFrame(208);
				client.getOutStream().writeWordBigEndian_dup(id);
				client.flushOutStream();
			}
		}
	}

	public void walkTo(int x, int y) {
		Client c = client;
		int walkToX = x - c.getX() + c.getX() - c.mapRegionX * 8;
		int walkToY = y - c.getY() + c.getY() - c.mapRegionY * 8;
		c.getNewWalkCmdX()[0] = walkToX;
		c.getNewWalkCmdY()[0] = walkToY;
		c.newWalkCmdSteps = 0;
	}

	public void walkTo2(int i, int j) {
		if (client.freezeDelay > 0) {
			return;
		}
		client.newWalkCmdSteps = 0;
		if (++client.newWalkCmdSteps > 50) {
			client.newWalkCmdSteps = 0;
		}
		int k = client.getX() + i;
		k -= client.mapRegionX * 8;
		client.getNewWalkCmdX()[0] = client.getNewWalkCmdY()[0] = 0;
		int l = client.getY() + j;
		l -= client.mapRegionY * 8;

		for (int n = 0; n < client.newWalkCmdSteps; n++) {
			client.getNewWalkCmdX()[n] += k;
			client.getNewWalkCmdY()[n] += l;
		}
	}

	public void walkTo3(int i, int j) {
		client.newWalkCmdSteps = 0;
		if (++client.newWalkCmdSteps > 50) {
			client.newWalkCmdSteps = 0;
		}
		int k = client.getX() + i;
		k -= client.mapRegionX * 8;
		client.getNewWalkCmdX()[0] = client.getNewWalkCmdY()[0] = 0;
		int l = client.getY() + j;
		l -= client.mapRegionY * 8;

		for (int n = 0; n < client.newWalkCmdSteps; n++) {
			client.getNewWalkCmdX()[n] += k;
			client.getNewWalkCmdY()[n] += l;
		}
	}

	public void walkToCheck(int i, int j) {
		if (client.freezeDelay > 0) {
			return;
		}
		client.newWalkCmdSteps = 0;
		if (++client.newWalkCmdSteps > 50) {
			client.newWalkCmdSteps = 0;
		}
		int k = client.getX() + i;
		k -= client.mapRegionX * 8;
		client.getNewWalkCmdX()[0] = client.getNewWalkCmdY()[0] = 0;
		int l = client.getY() + j;
		l -= client.mapRegionY * 8;

		for (int n = 0; n < client.newWalkCmdSteps; n++) {
			client.getNewWalkCmdX()[n] += k;
			client.getNewWalkCmdY()[n] += l;
		}
	}

	public boolean wearingGreegree() {
		return client.playerEquipment[client.playerWeapon] == 4024
				|| client.playerEquipment[client.playerWeapon] == 4026
				|| client.playerEquipment[client.playerWeapon] == 4027
				|| client.playerEquipment[client.playerWeapon] == 4029
				|| client.playerEquipment[client.playerWeapon] == 4025;
	}

	public void winLottery() {
		client.getItems().addItem(995,
				client.lotteryChance * client.lotteryCost);
		client.startAnimation(862);
		client.forcedChat("WINNER WINNER CHICKEN DINNER!");
		client.sendMessage("Congratulations, you have won the lottery!");
		state("Congratulations, you have won the lottery!");
	}

	public void yell(String type, String message) {
		message = Misc.ucFirst(message);
		for (Player player : PlayerHandler.players) {
			if (player == null) {
				continue;
			}
			Client c = (Client) player;
			c.getFunction().sendClan(client.playerName, message, type,
					client.playerRights);
			System.out.println(client.playerName + ": " + message);
		}
	}

}
