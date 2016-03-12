package com.ownxile.rs2.world.games;

import com.ownxile.rs2.items.ItemAssistant;
import com.ownxile.rs2.player.Client;
import com.ownxile.util.Misc;

public class TreasureTrails {

	private static final int LOW_LEVEL_REWARDS[] = { 1077, 1125, 1165, 1195,
			1297, 1367, 853, 7390, 7392, 7394, 7396, 7386, 7388, 1099, 1135,
			1065, 851, 1079, 1093, 1113, 1127, 1147, 1163, 1185, 1201, 1275,
			1303, 1319, 1333, 1359, 1373, 2491, 2497, 2503, 861, 859, 2651,
			1079, 1093, 3827, 3828, 3829, 3830, 3831, 3832, 3833, 3834, 3835,
			3836, 3837, 3838, 10714, 10715, 10716, 10717, 10718, 8952, 8953,
			8954, 8955, 8956, 8957, 8958, 8959, 8960, 8961, 8962, 8963, 8964,
			8965, 11282, 9185 };

	private static final int MEDIUM_LEVEL_REWARDS[] = { 1073, 1123, 1161, 1199,
			1301, 1371, 857, 2579, 2487, 2493, 2499, 2633, 2635, 2637, 2639,
			2641, 2643, 2645, 2647, 2649, 855, 1113, 1127, 1147, 1163, 1185,
			1201, 1275, 1303, 1319, 1333, 1359, 1373, 2491, 2497, 2503, 861,
			859, 2651, 2653, 2655, 2657, 2659, 2661, 2663, 2665, 2667, 2669,
			2671, 2673, 2675, 2583, 2585, 2587, 2588, 2589, 2591, 2593, 2598,
			2597, 2599, 2601, 2603, 2605, 2607, 2609, 2611, 2613, 2615, 2617,
			2619, 2621, 2623, 2625, 2627, 2629, 3057, 3058, 3059, 3060, 3061,
			3472, 3474, 3476, 3478, 3479, 3480, 10580, 9946, 9944, 10045 };

	private static final int STACKED_REWARD[] = { 995, 380, 561, 886, 374, 561,
			563, 890, 561, 563, 560, 892, 8007, 8008, 8009, 8012, 8013, 8010,
			8011, 10326, 10034, 299, 10476 };

	public static void addReward(Client player, int clueLevel) {
		player.gfx0(199);
		int lowLength = LOW_LEVEL_REWARDS.length;
		int medLength = MEDIUM_LEVEL_REWARDS.length;
		switch (clueLevel) {
		case 0:
			displayReward(player,
					LOW_LEVEL_REWARDS[Misc.random(lowLength - 1)], 1,
					LOW_LEVEL_REWARDS[Misc.random(lowLength - 1)], 1,
					MEDIUM_LEVEL_REWARDS[Misc.random(medLength - 1)], 1,
					STACKED_REWARD[Misc.random(STACKED_REWARD.length - 1)],
					10 + Misc.random(20));
			break;
		case 1:

			displayReward(player,
					MEDIUM_LEVEL_REWARDS[Misc.random(medLength - 1)], 1,
					MEDIUM_LEVEL_REWARDS[Misc.random(lowLength - 1)], 1,

					MEDIUM_LEVEL_REWARDS[Misc.random(medLength - 1)], 1,
					STACKED_REWARD[Misc.random(STACKED_REWARD.length - 1)],
					20 + Misc.random(30));
			break;
		case 2:
			displayReward(player, getHighLevelReward(), 1,
					Misc.random(2) == 2 ? getHighLevelReward()
							: MEDIUM_LEVEL_REWARDS[Misc.random(medLength - 1)],
					1, MEDIUM_LEVEL_REWARDS[Misc.random(medLength - 1)], 1,
					STACKED_REWARD[Misc.random(STACKED_REWARD.length - 1)],
					50 + Misc.random(50));

			break;

		}
	}

	private static void displayReward(Client player, int item, int amount,
			int item2, int amount2, int item3, int amount3, int item4,
			int amount4) {
		final int[] items = { item, item2, item3, item4 };
		final int[] amounts = { amount, amount2, amount3, amount4 };
		player.getItems().addItem(item, amount);
		player.getItems().addItem(item2, amount2);
		player.getItems().addItem(item3, amount3);
		player.getItems().addItem(item4, amount4);
		player.outStream.createFrameVarSizeWord(53);
		player.outStream.writeWord(6963);
		player.outStream.writeWord(items.length);
		for (int i = 0; i < items.length; i++) {
			if (player.playerItemsN[i] > 254) {
				player.outStream.writeByte(255);
				player.outStream.writeDWord_v2(amounts[i]);
			} else {
				player.outStream.writeByte(amounts[i]);
			}
			if (items[i] > 0) {
				player.outStream.writeWordBigEndianA(items[i] + 1);
			} else {
				player.outStream.writeWordBigEndianA(0);
			}
		}
		player.outStream.endFrameVarSizeWord();
		player.flushOutStream();
		player.getFunction().showInterface(6960);
	}

	public static void dumpRewards() {

		System.out.println("#JUNK REWARDS");
		for (int element : STACKED_REWARD) {
			System.out.println(element + ":"
					+ ItemAssistant.getItemName(element));
		}

		System.out.println("#LOW REWARDS");
		for (int element : LOW_LEVEL_REWARDS) {
			System.out.println(element + ":"
					+ ItemAssistant.getItemName(element));
		}

		System.out.println("#MEDIUM REWARDS");
		for (int element : MEDIUM_LEVEL_REWARDS) {
			System.out.println(element + ":"
					+ ItemAssistant.getItemName(element));
		}
		System.out.println("#HIGH REWARDS");

		for (int i = 0; i < 185; i += 2) {
			int id = 10280 + i;
			System.out.println(id + ":" + ItemAssistant.getItemName(id));

		}
	}

	private static int getHighLevelReward() {
		if (Misc.random(4) == 1) {
			return (5140 + Misc.random(97)) * 2;
		}
		return MEDIUM_LEVEL_REWARDS[Misc
				.random(MEDIUM_LEVEL_REWARDS.length - 1)];
	}

}