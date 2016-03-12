package com.ownxile.rs2.world.games;

import com.ownxile.core.World;
import com.ownxile.rs2.combat.prayer.Prayer;
import com.ownxile.rs2.npcs.NPC;
import com.ownxile.rs2.npcs.NPCHandler;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.Player;
import com.ownxile.rs2.player.PlayerHandler;
import com.ownxile.util.Correction;

public class PestControl {

	public final int GAME_TIMER = 120;
	public int gameTimer = -1;
	public int players;
	public final int PLAYERS_REQUIRED = 3;
	public int properTimer = 0;
	public long timeTaken;
	public int waitTimer = 20;

	public NPC portal1, portal2, portal3, portal4, voidKnight;

	public boolean allPortalsDead() {
		int count = 0;
		if (waitTimer < 10) {
			for (NPC npc : NPCHandler.npcs) {
				if (npc != null) {
					if (npc.npcType >= 6142 && npc.npcType <= 6145) {
						if (npc.needRespawn) {
							count++;
						}
					}
				}
			}
		}
		return count >= 4;
	}

	public void boatInterface2(Client client) {
		client.getFunction().sendFrame126("Next Departure: " + waitTimer + "",
				25120);
		client.getFunction().sendFrame126(
				"Players Ready: " + playersInBoat() + "", 25121);
		client.getFunction().sendFrame126(
				"(Need " + this.PLAYERS_REQUIRED + " to 50 players)", 25122);
		client.getFunction().sendFrame126("Points: " + client.getPoints() + "",
				25123);
	}

	public void endGame(boolean won) {
		gameTimer = -1;
		waitTimer = 20;
		int players = 0;
		String highestName = "";
		int highestAmount = 0;
		for (Player player : PlayerHandler.players) {
			if (player != null) {
				if (player.inPcGame()) {
					final Client client = (Client) player;
					client.getFunction().closeAllWindows();
					client.getFunction().movePlayer(2657, 2639, 0);
					client.lastClickedNpcId = 3785;
					players++;
					if (won && client.pcDamage > 50) {
						if (client.pcDamage > highestAmount) {
							highestName = client.playerName;
							highestAmount = client.pcDamage;
						}
						client.npcChat(
								"Wow you managed to defeat all of the portals.",
								"I am most impressed!");
						int bonus = client.pcDamage / 40;
						if (bonus > 10)
							bonus = 10;
						client.addPoints(5 + bonus);
						client.playerLevel[3] = client
								.getLevelForXP(client.playerXP[3]);
						client.playerLevel[5] = client
								.getLevelForXP(client.playerXP[5]);
						client.specAmount = 10;
						client.getItems().addItem(995,
								client.combatLevel * 2000);
						client.getFunction().refreshSkill(3);
						client.getFunction().refreshSkill(5);
					} else if (won) {
						client.getChat().sendChat(446, 3785);
						client.npcChat("The void knights notice your lack of zeal.");
					} else {
						client.npcChat("You failed to defeat the portals in time.");
					}
					client.pcDamage = 0;
					client.getItems().addSpecialBar(
							client.playerEquipment[client.playerWeapon]);
					Prayer.resetPrayers(client);
				}
			}
		}
		if (won) {
			GameBuilder.announce("Pest control was succesfully defeated in "
					+ Correction.getTimeString2(timeTaken) + " by " + players
					+ " players.");
			GameBuilder.announce("The top player was " + highestName
					+ " who did " + highestAmount + " damage!");

		}
		portal1.isDead = true;
		portal2.isDead = true;
		portal3.isDead = true;
		portal4.isDead = true;
		portal1.needRespawn = true;
		portal2.needRespawn = true;
		portal3.needRespawn = true;
		portal4.needRespawn = true;

		if (portal1.actionTimer != 0)
			portal1.actionTimer = 0;
		if (portal2.actionTimer != 0)
			portal2.actionTimer = 0;
		if (portal3.actionTimer != 0)
			portal3.actionTimer = 0;
		if (portal4.actionTimer != 0)
			portal4.actionTimer = 0;
	}

	public void movePlayer(int index) {
		final Client client = (Client) PlayerHandler.players[index];
		if (client.combatLevel < 40) {
			client.sendMessage("You must be at least 40 to enter this boat.");
			return;
		}
		client.getFunction().movePlayer(2658, 2611, 0);
		client.getFunction().sendFrame126("@red@0", 22116);
		client.lastClickedNpcId = 3785;
		client.npcChat("GO GO GO!");
	}

	public int playersInBoat() {
		int count = 0;
		for (Player player : PlayerHandler.players) {
			if (player != null) {
				if (player.inPcBoat()) {
					count++;
				}
			}
		}
		return count;
	}

	public void sendBlankInterface(Client client) {
		client.getFunction().sendFrame126("Waiting for Players", 25120);
		client.getFunction().sendFrame126("", 25121);
		client.getFunction().sendFrame126("", 25122);
		client.getFunction().sendFrame126("", 25123);
	}

	public void setInterface() {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				if (PlayerHandler.players[j].inPcGame()) {
					final Client client = (Client) PlayerHandler.players[j];
					if (portal1 != null) {
						if (portal1.isDead)
							client.getFunction().sendFrame126("@red@DEAD",
									25111);
						else
							client.getFunction().sendFrame126(
									"" + portal1.HP + "", 25111);
					}
					if (portal2 != null) {
						if (portal2.isDead)
							client.getFunction().sendFrame126("@red@DEAD",
									25112);
						else
							client.getFunction().sendFrame126(
									"" + portal2.HP + "", 25112);
					}
					if (portal3 != null) {
						if (portal3.isDead)
							client.getFunction().sendFrame126("@red@DEAD",
									25113);
						else
							client.getFunction().sendFrame126(
									"" + portal3.HP + "", 25113);
					}
					if (portal4 != null) {
						if (portal4.isDead)
							client.getFunction().sendFrame126("@red@DEAD",
									25114);
						else
							client.getFunction().sendFrame126(
									"" + portal4.HP + "", 25114);
					}
					if (voidKnight != null) {
						client.getFunction().sendFrame126(
								"" + voidKnight.HP + "", 25115);
					}
					client.getFunction().sendFrame126(
							"Time remaining: " + gameTimer + "", 25117);

				}
			}
		}
	}

	public PestControl() {
		portal1 = World.getNpcHandler().spawnNormalNpc(6142, 2628, 2591, 0, 0,
				500, 0, 0, 100);
		portal2 = World.getNpcHandler().spawnNormalNpc(6143, 2680, 2588, 0, 0,
				500, 0, 0, 100);
		portal3 = World.getNpcHandler().spawnNormalNpc(6144, 2669, 2570, 0, 0,
				500, 0, 0, 100);
		portal4 = World.getNpcHandler().spawnNormalNpc(6145, 2645, 2569, 0, 0,
				500, 0, 0, 100);

	}

	public void startGame() {
		portal1.needRespawn = false;
		portal2.needRespawn = false;
		;
		portal3.needRespawn = false;
		;
		portal4.needRespawn = false;
		if (playersInBoat() >= this.PLAYERS_REQUIRED) {
			gameTimer = GAME_TIMER;
			waitTimer = -1;
			timeTaken = System.currentTimeMillis();
			int players = playersInBoat();
			GameBuilder.announce("Pest control has been started by " + players
					+ " players.");
			for (int j = 0; j < PlayerHandler.players.length; j++) {
				if (PlayerHandler.players[j] != null) {
					if (PlayerHandler.players[j].inPcBoat()) {
						movePlayer(j);
					}
				}
			}

		} else {
			waitTimer = 20;
		}
	}

	public void tick() {
		if (waitTimer > 0) {
			waitTimer--;
			for (int j = 0; j < 200; j++) {
				if (PlayerHandler.players[j] == null)
					continue;
				if (PlayerHandler.players[j].inPcBoat()) {
					final Client client = (Client) PlayerHandler.players[j];
					this.boatInterface2(client);
				}
			}
		} else if (waitTimer == 0) {
			startGame();
		}
		if (gameTimer > 0) {
			setInterface();
			gameTimer--;
			if (allPortalsDead()) {
				endGame(true);
			}
		} else if (gameTimer == 0) {
			endGame(false);
		}
	}

}