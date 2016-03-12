package com.ownxile.config;

/**
 * @author Robbie holds combat constants
 */
public class CombatConfig {

	public static final int[] AUTOCAST_WEAPONS = { 772, 4675, 13867, 6914,
			15486, 1379, 1381, 1383, 1385, 1387, 1389, 1391, 1393, 1395, 1397,
			1399, 1401, 1403, 1405, 1407, 1409, 8286, 8290, 8294, 12928 };

	public static final int getCombatXP(int difficulty) {
		switch (difficulty) {
		case 1:
			return 4000;
		case 2:
			return 800;
		case 3:
			return 400;
		case 4:
		case 9:
		case 10:
		case 24:
		case 25:
			return 75;
		}
		return 4000;
	}

	public static final int getPrayerXP(int difficulty) {
		switch (difficulty) {
		case 1:
			return 100;
		case 2:
			return 50;
		case 3:
			return 25;
		case 9:
		case 10:
		case 24:
		case 25:
		case 4:
			return 15;
		}
		return 100;

	}

	public static String getDifficulty(int playerTitle) {
		switch (playerTitle) {
		case 1:
			return "Regular";
		case 2:
			return "Champion";
		case 3:
			return "Hero";
		case 4:
			return "Legend";
		case 9:
			return "Ironman";
		case 10:
			return "Ironwoman";
		case 24:
			return "Ultimate Ironman";
		case 25:
			return "UltimateIronwoman";
		}
		return "";
	}

	public static boolean isAutocastWeapon(int weaponId) {
		for (int element : AUTOCAST_WEAPONS) {
			if (element == weaponId) {
				return true;
			}
		}
		return false;
	}
}
