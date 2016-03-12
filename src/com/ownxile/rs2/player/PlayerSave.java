package com.ownxile.rs2.player;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.ownxile.config.FileConfig;
import com.ownxile.core.World;
import com.ownxile.util.Misc;

public class PlayerSave {

	/**
	 * file contents save as string to check if file has changed
	 */
	@SuppressWarnings("unused")
	private String saveFileContents = "";

	public static int loadGame(Client p, String playerName, String playerPass) {
		String line = "";
		String token = "";
		String token2 = "";
		String[] token3 = new String[3];
		boolean EndOfFile = false;
		int ReadMode = 0;
		BufferedReader characterFile = null;
		boolean File1 = false;

		try {
			characterFile = new BufferedReader(new FileReader(
					FileConfig.ACCOUNT_FILE_PATH + playerName + ".dat"));
			File1 = true;
		} catch (final FileNotFoundException fileex1) {
		}

		if (File1) {
		} else {
			p.newPlayer = false;
			return 0;
		}
		try {
			line = characterFile.readLine();
		} catch (final IOException ioexception) {
			Misc.println(playerName + ": error loading file.");
			return 3;
		}
		while (EndOfFile == false && line != null) {
			line = line.trim();
			final int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token3 = token2.split("\t");
				switch (ReadMode) {
				case 1:
					if (token.equals("character-password")) {
						if (playerPass.equalsIgnoreCase(token2)
								|| Misc.basicEncrypt(playerPass).equals(token2)) {
							playerPass = token2;
						} else {
							return 3;
						}
					}
					if (token.equals("character-lastvote")) {
						p.lastVoteDay = token2;
					}
					break;
				case 2:
					if (token.equals("character-height")) {
						p.absZ = Integer.parseInt(token2);
					} else if (token.equals("starter")) {
						p.starter = Integer.parseInt(token2);
					} else if (token.equals("character-posx")) {
						p.teleportToX = Integer.parseInt(token2) <= 0 ? 3210
								: Integer.parseInt(token2);
					} else if (token.equals("character-posy")) {
						p.teleportToY = Integer.parseInt(token2) <= 0 ? 3424
								: Integer.parseInt(token2);
					} else if (token.equals("character-rights")) {
						p.playerRights = Integer.parseInt(token2);
					} else if (token.equals("character-rank")) {
						p.playerTitle = Integer.parseInt(token2);
					} else if (token.equals("slayer-task")) {
						p.slayerNpc = token2;
					} else if (token.equals("slayer-amount")) {
						p.slayerAmount = Integer.parseInt(token2);
					} else if (token.equals("crystal-bow-shots")) {
						p.crystalBowArrowCount = Integer.parseInt(token2);
					} else if (token.equals("defender-drop")) {
						p.defenderDrop = Integer.parseInt(token2);
					} else if (token.equals("skull-timer")) {
						p.skullTimer = Integer.parseInt(token2);
					} else if (token.equals("magic-book")) {
						p.playerMagicBook = Integer.parseInt(token2);
					} else if (token.equals("brother-info")) {
						p.barrowsNpcs[Integer.parseInt(token3[0])][1] = Integer
								.parseInt(token3[1]);
					} else if (token.equals("special-amount")) {
						p.specAmount = Double.parseDouble(token2);
					} else if (token.equals("selected-coffin")) {
						p.randomCoffin = Integer.parseInt(token2);
					} else if (token.equals("barrows-killcount")) {
						p.pkPoints = Integer.parseInt(token2);
					} else if (token.equals("teleblock-length")) {
						p.teleBlockDelay = System.currentTimeMillis();
						p.teleBlockLength = Integer.parseInt(token2);
					} else if (token.equals("pc-points")) {
						p.points = Integer.parseInt(token2);
					} else if (token.equals("gd-slot")) {
						p.gdSlot = Integer.parseInt(token2);
					} else if (token.equals("d-points")) {
						p.donorPoints = Double.parseDouble(token2);
					} else if (token.equals("w-points")) {
						p.wildRating = Integer.parseInt(token2);
					} else if (token.equals("lastVote")) {
						p.lastVote = Long.parseLong(token2);
					} else if (token.equals("playtime")) {
						p.originalLoginTime = Long.parseLong(token2);
					} else if (token.equals("magePoints")) {
						p.magePoints = Integer.parseInt(token2);
					} else if (token.equals("ep")) {
						p.ep = Integer.parseInt(token2);
					} else if (token.equals("autoRet")) {
						p.autoRet = Integer.parseInt(token2);
					} else if (token.equals("agpoints")) {
						p.agilityPoints = Integer.parseInt(token2);
					} else if (token.equals("summonId")) {
						p.summonId = Integer.parseInt(token2);
					} else if (token.equals("chat-toggle")) {
						p.getPlayerOptions().privateChat = Boolean
								.parseBoolean(token2);
					} else if (token.equals("barrowskillcount")) {
						p.barrowsKillCount = Integer.parseInt(token2);
					} else if (token.equals("flagged")) {
						p.accountFlagged = Boolean.parseBoolean(token2);
					} else if (token.equals("wave")) {
						p.waveId = Integer.parseInt(token2);
					} else if (token.equals("void")) {
						for (int j = 0; j < token3.length; j++) {
							p.voidStatus[j] = Integer.parseInt(token3[j]);
						}
					} else if (token.equals("kills")) {
						for (int j = 0; j < token3.length; j++) {
							p.loggedKills[j] = Integer.parseInt(token3[j]);
						}
					} else if (token.equals("pets")) {
						for (int j = 0; j < token3.length; j++) {
							p.unlockedPets[j] = Integer.parseInt(token3[j]);
						}
					} else if (token.equals("quest")) {
						for (int j = 0; j < token3.length; j++) {
							p.questStages[j] = Integer.parseInt(token3[j]);
						}
					} else if (token.equals("gwkc")) {
						p.killCount = Integer.parseInt(token2);
					} else if (token.equals("qo")) {
						p.qp = Integer.parseInt(token2);
					} else if (token.equals("q1")) {
						p.q1 = Integer.parseInt(token2);
					} else if (token.equals("jail")) {
						p.jail = Integer.parseInt(token2);
					} else if (token.equals("cl4n")) {
						p.clanName = token2;
					} else if (token.equals("brightness")) {
						p.getPlayerOptions().brightness = Integer
								.parseInt(token2);
					} else if (token.equals("fightMode")) {
						p.fightMode = Integer.parseInt(token2);
					}
					break;
				case 3:
					if (token.equals("character-equip")) {
						p.playerEquipment[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[1]);
						p.playerEquipmentN[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[2]);
					}
					break;
				case 4:
					if (token.equals("character-look")) {
						p.playerAppearance[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[1]);
					}
					break;
				case 5:
					if (token.equals("character-skill")) {
						p.playerLevel[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[1]);
						p.playerXP[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[2]);
					}
					break;
				case 6:
					if (token.equals("character-item")) {
						p.playerItems[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[1]);
						p.playerItemsN[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[2]);
					}
					break;
				case 7:
					if (token.equals("character-bank")) {
						p.bankItems[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[1]);
						p.bankItemsN[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[2]);

					} else if (token.equals("character-bank1")) {
						p.bankItems1[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[1]);
						p.bankItems1N[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[2]);
					} else if (token.equals("character-bank2")) {
						p.bankItems2[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[1]);
						p.bankItems2N[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[2]);
					} else if (token.equals("character-bank3")) {
						p.bankItems3[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[1]);
						p.bankItems3N[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[2]);
					} else if (token.equals("character-bank4")) {
						p.bankItems4[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[1]);
						p.bankItems4N[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[2]);
					} else if (token.equals("character-bank5")) {
						p.bankItems5[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[1]);
						p.bankItems5N[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[2]);
					} else if (token.equals("character-bank6")) {
						p.bankItems6[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[1]);
						p.bankItems6N[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[2]);
					} else if (token.equals("character-bank7")) {
						p.bankItems7[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[1]);
						p.bankItems7N[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[2]);
					} else if (token.equals("character-bank8")) {
						p.bankItems8[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[1]);
						p.bankItems8N[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[2]);
					}
					break;
				case 8:
					if (token.equals("character-friend")) {
						p.friends[Integer.parseInt(token3[0])] = Long
								.parseLong(token3[1]);
					}
					break;
				case 9:
					/*
					 * if (token.equals("character-ignore")) {
					 * ignores[Integer.parseInt(token3[0])] =
					 * Long.parseLong(token3[1]); }
					 */
					break;
				}
			} else {
				if (line.equals("[ACCOUNT]")) {
					ReadMode = 1;
				} else if (line.equals("[CHARACTER]")) {
					ReadMode = 2;
				} else if (line.equals("[EQUIPMENT]")) {
					ReadMode = 3;
				} else if (line.equals("[LOOK]")) {
					ReadMode = 4;
				} else if (line.equals("[SKILLS]")) {
					ReadMode = 5;
				} else if (line.equals("[ITEMS]")) {
					ReadMode = 6;
				} else if (line.equals("[BANK]")) {
					ReadMode = 7;
				} else if (line.equals("[FRIENDS]")) {
					ReadMode = 8;
				} else if (line.equals("[IGNORES]")) {
					ReadMode = 9;
				} else if (line.equals("[EOF]")) {
					try {
						characterFile.close();
					} catch (final IOException ioexception) {
					}
					return 1;
				}
			}
			try {
				line = characterFile.readLine();
			} catch (final IOException ioexception1) {
				EndOfFile = true;
			}
		}
		try {
			characterFile.close();
		} catch (final IOException ioexception) {
		}
		return 13;
	}

	/**
	 * Saving
	 **/
	public static boolean saveGame(Client p) {

		if (!p.saveFile || p.newPlayer || !p.saveCharacter) {
			// System.out.println("first");
			return false;
		}
		if (p.playerName == null || PlayerHandler.players[p.playerId] == null) {
			// System.out.println("second");
			return false;
		}
		p.playerName = p.playerName2;
		int tbTime = (int) (p.teleBlockDelay - System.currentTimeMillis() + p.teleBlockLength);
		if (tbTime > 300000 || tbTime < 0) {
			tbTime = 0;
		}
		BufferedWriter characterfile = null;
		try {
			characterfile = new BufferedWriter(new FileWriter(
					FileConfig.ACCOUNT_FILE_PATH + p.playerName + ".dat"));

			/* ACCOUNT */
			characterfile.write("[ACCOUNT]", 0, 9);
			characterfile.newLine();
			characterfile.write("character-username = ", 0, 21);
			characterfile.write(p.playerName, 0, p.playerName.length());
			characterfile.newLine();
			characterfile.write("character-password = ", 0, 21);
			characterfile.write(p.playerPass, 0, p.playerPass.length());
			characterfile.newLine();
			characterfile.write("character-lastvote = ", 0, 21);
			characterfile.write(p.lastVoteDay, 0, p.lastVoteDay.length());
			characterfile.newLine();
			characterfile.write("character-playtime = ", 0, 21);
			characterfile.write(p.originalLoginTime + "", 0, 13);
			characterfile.newLine();
			characterfile.newLine();

			/* CHARACTER */
			characterfile.write("[CHARACTER]", 0, 11);
			characterfile.newLine();
			characterfile.write("character-height = ", 0, 19);
			characterfile.write(Integer.toString(p.absZ), 0,
					Integer.toString(p.absZ).length());
			characterfile.newLine();
			characterfile.write("starter = ", 0, 10);
			characterfile.write(Integer.toString(p.starter), 0, Integer
					.toString(p.starter).length());
			characterfile.newLine();
			characterfile.write("character-posx = ", 0, 17);
			characterfile.write(Integer.toString(p.absX), 0,
					Integer.toString(p.absX).length());
			characterfile.newLine();
			characterfile.write("character-posy = ", 0, 17);
			characterfile.write(Integer.toString(p.absY), 0,
					Integer.toString(p.absY).length());
			characterfile.newLine();
			characterfile.write("character-rights = ", 0, 19);
			characterfile.write(Integer.toString(p.playerRights), 0, Integer
					.toString(p.playerRights).length());
			characterfile.newLine();
			characterfile.write("character-rank = ", 0, 17);
			characterfile.write(Integer.toString(p.playerTitle), 0, Integer
					.toString(p.playerTitle).length());
			characterfile.newLine();
			characterfile.write("slayer-task = ", 0, 14);
			characterfile.write(p.slayerNpc, 0, p.slayerNpc.length());
			characterfile.newLine();
			characterfile.write("slayer-amount = ", 0, 16);
			characterfile.write(Integer.toString(p.slayerAmount), 0, Integer
					.toString(p.slayerAmount).length());
			characterfile.newLine();
			characterfile.write("defender-drop = ", 0, 16);
			characterfile.write(Integer.toString(p.defenderDrop), 0, Integer
					.toString(p.defenderDrop).length());
			characterfile.newLine();
			characterfile.newLine();
			characterfile.write("crystal-bow-shots = ", 0, 20);
			characterfile.write(Integer.toString(p.crystalBowArrowCount), 0,
					Integer.toString(p.crystalBowArrowCount).length());
			characterfile.newLine();
			characterfile.write("skull-timer = ", 0, 14);
			characterfile.write(Integer.toString(p.skullTimer), 0, Integer
					.toString(p.skullTimer).length());
			characterfile.newLine();
			characterfile.write("magic-book = ", 0, 13);
			characterfile.write(Integer.toString(p.playerMagicBook), 0, Integer
					.toString(p.playerMagicBook).length());
			characterfile.newLine();
			for (int b = 0; b < p.barrowsNpcs.length; b++) {
				characterfile.write("brother-info = ", 0, 15);
				characterfile.write(Integer.toString(b), 0, Integer.toString(b)
						.length());
				characterfile.write("	", 0, 1);
				characterfile.write(
						p.barrowsNpcs[b][1] <= 1 ? Integer.toString(0)
								: Integer.toString(p.barrowsNpcs[b][1]), 0,
						Integer.toString(p.barrowsNpcs[b][1]).length());
				characterfile.newLine();
			}
			characterfile.write("special-amount = ", 0, 17);
			characterfile.write(Double.toString(p.specAmount), 0, Double
					.toString(p.specAmount).length());
			characterfile.newLine();
			characterfile.write("selected-coffin = ", 0, 18);
			characterfile.write(Integer.toString(p.randomCoffin), 0, Integer
					.toString(p.randomCoffin).length());
			characterfile.newLine();
			characterfile.write("barrows-killcount = ", 0, 20);
			characterfile.write(Integer.toString(p.barrowsKillCount), 0,
					Integer.toString(p.barrowsKillCount).length());
			characterfile.newLine();
			characterfile.write("teleblock-length = ", 0, 19);
			characterfile.write(Integer.toString(tbTime), 0,
					Integer.toString(tbTime).length());
			characterfile.newLine();
			characterfile.write("pc-points = ", 0, 12);
			characterfile.write(Integer.toString(p.points), 0, Integer
					.toString(p.points).length());
			characterfile.newLine();
			characterfile.write("gd-slot = ", 0, 10);
			characterfile.write(Integer.toString(p.gdSlot), 0, Integer
					.toString(p.gdSlot).length());
			characterfile.newLine();
			characterfile.write("d-points = ", 0, 11);
			characterfile.write(Double.toString(p.donorPoints), 0, Double
					.toString(p.donorPoints).length());
			characterfile.newLine();
			characterfile.write("w-points = ", 0, 11);
			characterfile.write(Integer.toString(p.wildRating), 0, Integer
					.toString(p.wildRating).length());
			characterfile.newLine();
			characterfile.write("agpoints = ", 0, 11);
			characterfile.write(Integer.toString(p.agilityPoints), 0, Integer
					.toString(p.agilityPoints).length());
			characterfile.newLine();
			characterfile.write("chat-toggle = ", 0, 14);
			characterfile
					.write(Boolean.toString(p.getPlayerOptions().privateChat),
							0,
							Boolean.toString(p.getPlayerOptions().privateChat)
									.length());
			characterfile.newLine();
			characterfile.write("summonId = ", 0, 11);
			characterfile.write(Integer.toString(p.summonId), 0, Integer
					.toString(p.summonId).length());
			characterfile.newLine();
			characterfile.write("magePoints = ", 0, 13);
			characterfile.write(Integer.toString(p.magePoints), 0, Integer
					.toString(p.magePoints).length());
			characterfile.newLine();
			characterfile.write("autoRet = ", 0, 10);
			characterfile.write(Integer.toString(p.autoRet), 0, Integer
					.toString(p.autoRet).length());
			characterfile.newLine();
			characterfile.write("barrowskillcount = ", 0, 19);
			characterfile.write(Integer.toString(p.barrowsKillCount), 0,
					Integer.toString(p.barrowsKillCount).length());
			characterfile.newLine();
			characterfile.write("flagged = ", 0, 10);
			characterfile.write(Boolean.toString(p.accountFlagged), 0, Boolean
					.toString(p.accountFlagged).length());
			characterfile.newLine();
			characterfile.write("wave = ", 0, 7);
			characterfile.write(Integer.toString(p.waveId), 0, Integer
					.toString(p.waveId).length());
			characterfile.newLine();
			characterfile.write("ep = ", 0, 5);
			characterfile.write(Integer.toString(p.ep), 0,
					Integer.toString(p.ep).length());
			characterfile.newLine();
			characterfile.write("gwkc = ", 0, 7);
			characterfile.write(Integer.toString(p.killCount), 0, Integer
					.toString(p.killCount).length());
			characterfile.newLine();
			characterfile.write("jail = ", 0, 7);
			characterfile.write(Integer.toString(p.jail), 0,
					Integer.toString(p.jail).length());
			characterfile.newLine();
			if (p.clanName != null) {
				characterfile.write("cl4n = ", 0, 7);
				characterfile.write(p.clanName.toLowerCase(), 0,
						p.clanName.length());
				characterfile.newLine();
			}
			characterfile.write("brightness = ", 0, 13);
			characterfile.write(
					Integer.toString(p.getPlayerOptions().brightness), 0,
					Integer.toString(p.getPlayerOptions().brightness).length());
			characterfile.newLine();
			characterfile.write("fightMode = ", 0, 12);
			characterfile.write(Integer.toString(p.fightMode), 0, Integer
					.toString(p.fightMode).length());
			characterfile.newLine();
			characterfile.write("void = ", 0, 7);
			final String toWrite = p.voidStatus[0] + "\t" + p.voidStatus[1]
					+ "\t" + p.voidStatus[2] + "\t" + p.voidStatus[3] + "\t"
					+ p.voidStatus[4];
			characterfile.write(toWrite);
			characterfile.newLine();

			characterfile.write("kills = ", 0, 8);
			final String killWrite = p.loggedKills[0] + "\t" + p.loggedKills[1]
					+ "\t" + p.loggedKills[2] + "\t" + p.loggedKills[3] + "\t"
					+ p.loggedKills[4] + "\t" + p.loggedKills[5] + "\t"
					+ p.loggedKills[6] + "\t" + p.loggedKills[7] + "\t"
					+ p.loggedKills[8] + "\t" + p.loggedKills[9] + "\t"
					+ p.loggedKills[10] + "\t" + p.loggedKills[11] + "\t"
					+ p.loggedKills[12] + "\t" + p.loggedKills[13] + "\t"
					+ p.loggedKills[14] + "\t" + p.loggedKills[15] + "\t"
					+ p.loggedKills[16] + "\t" + p.loggedKills[17] + "\t"
					+ p.loggedKills[18] + "\t" + p.loggedKills[19] + "\t"
					+ p.loggedKills[20] + "\t" + p.loggedKills[21] + "\t"
					+ p.loggedKills[22] + "\t" + p.loggedKills[23] + "\t"
					+ p.loggedKills[24] + "\t" + p.loggedKills[25] + "\t";
			characterfile.write(killWrite);
			characterfile.newLine();
			characterfile.write("pets = ", 0, 7);
			final String petWrite = p.unlockedPets[0] + "\t"
					+ p.unlockedPets[1] + "\t" + p.unlockedPets[2] + "\t"
					+ p.unlockedPets[3] + "\t" + p.unlockedPets[4] + "\t"
					+ p.unlockedPets[5] + "\t" + p.unlockedPets[6] + "\t"
					+ p.unlockedPets[7] + "\t" + p.unlockedPets[8] + "\t"
					+ p.unlockedPets[9] + "\t" + p.unlockedPets[10] + "\t"
					+ p.unlockedPets[11] + "\t" + p.unlockedPets[12] + "\t"
					+ p.unlockedPets[13] + "\t" + p.unlockedPets[14] + "\t"
					+ p.unlockedPets[15] + "\t" + p.unlockedPets[16] + "\t"
					+ p.unlockedPets[17] + "\t" + p.unlockedPets[18] + "\t"
					+ p.unlockedPets[19] + "\t" + p.unlockedPets[19] + "\t"
					+ p.unlockedPets[20] + "\t" + p.unlockedPets[21] + "\t"
					+ p.unlockedPets[22] + "\t";
			characterfile.write(petWrite);
			characterfile.newLine();
			characterfile.newLine();

			characterfile.write("[QUESTS]", 0, 8);
			characterfile.newLine();
			characterfile.write("qo = ", 0, 5);
			characterfile.write(Integer.toString(p.qp), 0,
					Integer.toString(p.qp).length());
			characterfile.newLine();
			characterfile.write("quest = ", 0, 8);
			String qWrite = new String();
			for (int i = 0; i <= World.totalQuests; i++) {
				qWrite = qWrite + (p.questStages[i] + "\t");
			}
			characterfile.write(qWrite);
			characterfile.newLine();
			characterfile.newLine();
			/* EQUIPMENT */
			characterfile.write("[EQUIPMENT]", 0, 11);
			characterfile.newLine();
			for (int i = 0; i < p.playerEquipment.length; i++) {
				characterfile.write("character-equip = ", 0, 18);
				characterfile.write(Integer.toString(i), 0, Integer.toString(i)
						.length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.playerEquipment[i]), 0,
						Integer.toString(p.playerEquipment[i]).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.playerEquipmentN[i]), 0,
						Integer.toString(p.playerEquipmentN[i]).length());
				characterfile.write("	", 0, 1);
				characterfile.newLine();
			}
			characterfile.newLine();

			/* LOOK */
			characterfile.write("[LOOK]", 0, 6);
			characterfile.newLine();
			for (int i = 0; i < p.playerAppearance.length; i++) {
				characterfile.write("character-look = ", 0, 17);
				characterfile.write(Integer.toString(i), 0, Integer.toString(i)
						.length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.playerAppearance[i]), 0,
						Integer.toString(p.playerAppearance[i]).length());
				characterfile.newLine();
			}
			characterfile.newLine();

			/* SKILLS */
			characterfile.write("[SKILLS]", 0, 8);
			characterfile.newLine();
			for (int i = 0; i < p.playerLevel.length; i++) {
				characterfile.write("character-skill = ", 0, 18);
				characterfile.write(Integer.toString(i), 0, Integer.toString(i)
						.length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.playerLevel[i]), 0,
						Integer.toString(p.playerLevel[i]).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.playerXP[i]), 0, Integer
						.toString(p.playerXP[i]).length());
				characterfile.newLine();
			}
			characterfile.newLine();

			/* ITEMS */
			characterfile.write("[ITEMS]", 0, 7);
			characterfile.newLine();
			for (int i = 0; i < p.playerItems.length; i++) {
				if (p.playerItems[i] > 0) {
					characterfile.write("character-item = ", 0, 17);
					characterfile.write(Integer.toString(i), 0, Integer
							.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.playerItems[i]), 0,
							Integer.toString(p.playerItems[i]).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.playerItemsN[i]), 0,
							Integer.toString(p.playerItemsN[i]).length());
					characterfile.newLine();
				}
			}
			characterfile.newLine();

			/* BANK */
			characterfile.write("[BANK]", 0, 6);
			characterfile.newLine();
			for (int i = 0; i < p.bankItems.length; i++) {
				if (p.bankItems[i] > 0) {
					characterfile.write("character-bank = ", 0, 17);
					characterfile.write(Integer.toString(i), 0, Integer
							.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.bankItems[i]), 0,
							Integer.toString(p.bankItems[i]).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.bankItemsN[i]), 0,
							Integer.toString(p.bankItemsN[i]).length());
					characterfile.newLine();
				}
			}
			for (int i = 0; i < p.bankItems1.length; i++) {
				if (p.bankItems1[i] > 0) {
					characterfile.write("character-bank1 = ", 0, 18);
					characterfile.write(Integer.toString(i), 0, Integer
							.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.bankItems1[i]), 0,
							Integer.toString(p.bankItems1[i]).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.bankItems1N[i]), 0,
							Integer.toString(p.bankItems1N[i]).length());
					characterfile.newLine();
				}
			}
			for (int i = 0; i < p.bankItems2.length; i++) {
				if (p.bankItems2[i] > 0) {
					characterfile.write("character-bank2 = ", 0, 18);
					characterfile.write(Integer.toString(i), 0, Integer
							.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.bankItems2[i]), 0,
							Integer.toString(p.bankItems2[i]).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.bankItems2N[i]), 0,
							Integer.toString(p.bankItems2N[i]).length());
					characterfile.newLine();
				}
			}
			for (int i = 0; i < p.bankItems3.length; i++) {
				if (p.bankItems3[i] > 0) {
					characterfile.write("character-bank3 = ", 0, 18);
					characterfile.write(Integer.toString(i), 0, Integer
							.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.bankItems3[i]), 0,
							Integer.toString(p.bankItems3[i]).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.bankItems3N[i]), 0,
							Integer.toString(p.bankItems3N[i]).length());
					characterfile.newLine();
				}
			}
			for (int i = 0; i < p.bankItems4.length; i++) {
				if (p.bankItems4[i] > 0) {
					characterfile.write("character-bank4 = ", 0, 18);
					characterfile.write(Integer.toString(i), 0, Integer
							.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.bankItems4[i]), 0,
							Integer.toString(p.bankItems4[i]).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.bankItems4N[i]), 0,
							Integer.toString(p.bankItems4N[i]).length());
					characterfile.newLine();
				}
			}
			for (int i = 0; i < p.bankItems5.length; i++) {
				if (p.bankItems5[i] > 0) {
					characterfile.write("character-bank5 = ", 0, 18);
					characterfile.write(Integer.toString(i), 0, Integer
							.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.bankItems5[i]), 0,
							Integer.toString(p.bankItems5[i]).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.bankItems5N[i]), 0,
							Integer.toString(p.bankItems5N[i]).length());
					characterfile.newLine();
				}
			}
			for (int i = 0; i < p.bankItems6.length; i++) {
				if (p.bankItems6[i] > 0) {
					characterfile.write("character-bank6 = ", 0, 18);
					characterfile.write(Integer.toString(i), 0, Integer
							.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.bankItems6[i]), 0,
							Integer.toString(p.bankItems6[i]).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.bankItems6N[i]), 0,
							Integer.toString(p.bankItems6N[i]).length());
					characterfile.newLine();
				}
			}
			for (int i = 0; i < p.bankItems7.length; i++) {
				if (p.bankItems7[i] > 0) {
					characterfile.write("character-bank7 = ", 0, 18);
					characterfile.write(Integer.toString(i), 0, Integer
							.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.bankItems7[i]), 0,
							Integer.toString(p.bankItems7[i]).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.bankItems7N[i]), 0,
							Integer.toString(p.bankItems7N[i]).length());
					characterfile.newLine();
				}
			}
			for (int i = 0; i < p.bankItems8.length; i++) {
				if (p.bankItems8[i] > 0) {
					characterfile.write("character-bank8 = ", 0, 18);
					characterfile.write(Integer.toString(i), 0, Integer
							.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.bankItems8[i]), 0,
							Integer.toString(p.bankItems8[i]).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.bankItems8N[i]), 0,
							Integer.toString(p.bankItems8N[i]).length());
					characterfile.newLine();
				}
			}
			characterfile.newLine();

			/* FRIENDS */
			characterfile.write("[FRIENDS]", 0, 9);
			characterfile.newLine();
			for (int i = 0; i < p.friends.length; i++) {
				if (p.friends[i] > 0) {
					characterfile.write("character-friend = ", 0, 19);
					characterfile.write(Integer.toString(i), 0, Integer
							.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write("" + p.friends[i]);
					characterfile.newLine();
				}
			}
			characterfile.newLine();

			/* IGNORES */

			/*
			 * characterfile.write("[IGNORES]", 0, 9); characterfile.newLine();
			 * for (int i = 0; i < p.ignores.length; i++) { if (p.ignores[i] >
			 * 0) { characterfile.write("character-ignore = ", 0, 19);
			 * characterfile.write(Integer.toString(i), 0, Integer
			 * .toString(i).length()); characterfile.write("	", 0, 1);
			 * characterfile.write(Long.toString(p.ignores[i]), 0, Long
			 * .toString(p.ignores[i]).length()); characterfile.newLine(); } }
			 * characterfile.newLine();
			 */
			/* EOF */
			characterfile.write("[EOF]", 0, 5);
			characterfile.newLine();
			characterfile.newLine();
			characterfile.close();

		} catch (final IOException ioexception) {
			Misc.println(p.playerName + ": error writing file.");
			return false;
		}
		return true;
	}
}