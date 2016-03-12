package com.ownxile.rs2.skills.slayer;

public class SlayerMonsterRequirement {

	public static int monsterRequirement(int npcId) {
		switch (npcId) {
		case 1648:
			return 5;
		case 1612:
			return 15;
		case 1643:// INFERNAL MAGE
			return 45;
		case 1618:
			return 50;
		case 1624:
			return 65;
		case 1610:
			return 75;
		case 6218:
			return 77;
		case 1613:
			return 80;
		case 6221:// spiritual mage
			return 83;
		case 1615:// abbysal demon
			return 85;
		case 5363:// mithril dragon
			return 87;
		case 2783:// dark beast
			return 90;
		case 3068:// skeletal wyvern
			return 93;
		case 2349:
			return 95;
		}
		return 1;

	}

	/**
	 * @param npcId
	 * @return slayer level required
	 */

	public int[][] slayerReqs = { { 1648, 5 }, { 1612, 15 }, { 1643, 45 },
			{ 1618, 50 }, { 1624, 65 }, { 1610, 75 }, { 1613, 80 },
			{ 1615, 85 }, { 2783, 90 } };

}
