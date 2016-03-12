package com.ownxile.rs2.combat.range;

import com.ownxile.rs2.player.Client;

public class RangeConfiguration {

	public static final int[] ARROWS = { 882, 884, 886, 888, 890, 892, 4740,
			11212, 9140, 9141, 4142, 9143, 9144, 9240, 9241, 9242, 9243, 9244,
			9245, 9706 };

	public static final int[] BOWS = { 9185, 11795, 839, 845, 847, 851, 855,
			859, 841, 843, 849, 853, 857, 861, 4212, 4214, 4215, 11235, 4216,
			4217, 4218, 4219, 4220, 4221, 4222, 4223, 6724, 4734, 4934, 4935,
			4936, 4937, 9705 };

	public static final int[] OTHER_RANGE_WEAPONS = { 863, 864, 865, 866, 867,
			868, 869, 806, 807, 808, 809, 810, 811, 825, 826, 827, 828, 829,
			830, 800, 801, 802, 803, 804, 805, 6522, 11230, 10034, 13879, 13883 };

	public static int correctBowAndArrows(Client c) {
		if (usingBolts(c)) {
			return -1;
		}
		switch (c.playerEquipment[c.playerWeapon]) {

		case 839:
		case 841:
			return 882;
		case 9705:
			return 9706;
		case 843:
		case 845:
			return 884;

		case 847:
		case 849:
			return 886;

		case 851:
		case 853:
			return 888;

		case 855:
		case 857:
			return 890;

		case 859:
		case 861:
			return 892;

		case 4734:
		case 4935:
		case 4936:
		case 4937:
			return 4740;

		case 11235:
			return 11212;
		}
		return -1;
	}

	public static boolean usingBolts(Client player) {
		return player.playerEquipment[player.playerArrows] >= 9130
				&& player.playerEquipment[player.playerArrows] <= 9145
				|| player.playerEquipment[player.playerArrows] >= 9230
				&& player.playerEquipment[player.playerArrows] <= 9245;
	}

	public static boolean usingCrystalBow(Client player) {
		return player.playerEquipment[player.playerWeapon] >= 4212
				&& player.playerEquipment[player.playerWeapon] <= 4223;
	}

	public static boolean usingCrossbow(Client player) {
		return player.playerEquipment[player.playerWeapon] == 9185
				|| player.playerEquipment[player.playerWeapon] == 11795;
	}

	public static boolean usingDarkBow(Client player) {
		return player.playerEquipment[player.playerWeapon] == 11235;
	}
}
