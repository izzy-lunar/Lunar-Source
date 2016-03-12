package com.ownxile.rs2.world.games;

import com.ownxile.core.World;
import com.ownxile.rs2.player.Client;
import com.ownxile.util.Misc;

public class Barrows {

	public static final int[][] COFFIN_AND_BROTHERS = { { 6823, 2030 },
			{ 6772, 2029 }, { 6822, 2028 }, { 6773, 2027 }, { 6771, 2026 },
			{ 6821, 2025 }

	};

	/**
	 * Picking the random coffin
	 **/
	public static int getRandomCoffin() {
		return Misc.random(COFFIN_AND_BROTHERS.length - 1);
	}

	public static void openChest(Client player) {
		if (player.barrowsKillCount < 5) {
			player.sendMessage("You haven't killed all the brothers.");
		}
		if (player.barrowsKillCount == 5
				&& player.barrowsNpcs[player.randomCoffin][1] == 1) {
			player.sendMessage("I have already summoned this npc.");
		}
		if (player.barrowsNpcs[player.randomCoffin][1] == 0
				&& player.barrowsKillCount >= 5) {
			World.getNpcHandler().spawnNpc(player,
					player.barrowsNpcs[player.randomCoffin][0], 3551, 9694 - 1,
					0, 0, 120, 30, 200, 200, true, true);
			player.barrowsNpcs[player.randomCoffin][1] = 1;
		}
		if ((player.barrowsKillCount > 5 || player.barrowsNpcs[player.randomCoffin][1] == 2)
				&& player.getItems().freeSlots() >= 4) {
			player.getFunction().resetBarrows();
			player.addPoints(10);
			player.getItems().addItem(player.getFunction().randomBarrows(), 1);
			player.getItems().addItem(player.getFunction().randomRunes(),
					Misc.random(100) + 50);
			player.getItems().addItem(player.getFunction().randomRunes(),
					Misc.random(100) + 50);
			player.getItems().addItem(player.getFunction().randomRunes(),
					Misc.random(100) + 50);
			player.getFunction().startTeleport(3564, 3288, 0, "modern");
			player.barrowsCount += 1;

		} else if (player.barrowsKillCount > 5
				&& player.getItems().freeSlots() <= 3) {
			player.sendMessage("You need at least 4 inventory spaces available.");
		}

	}

	/**
	 * Selects the coffin and shows the interface if coffin id matches random
	 * coffin
	 **/
	public static boolean selectCoffin(Client client, int coffinId) {
		if (client.randomCoffin == 0) {
			client.randomCoffin = getRandomCoffin();
		}

		if (COFFIN_AND_BROTHERS[client.randomCoffin][0] == coffinId) {
			client.getChat().sendChat(1, -1);
			return true;
		}
		return false;
	}

}