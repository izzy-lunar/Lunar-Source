package com.ownxile.rs2.combat.melee;

import com.ownxile.rs2.player.Client;

public class MeleeFormula {

	public static int calculateMeleeMaxHit(Client player) {
		double maxHit = 0;
		final int strBonus = player.playerBonus[10];
		int strength = player.playerLevel[2];
		final int lvlForXP = player.getLevelForXP(player.playerXP[2]);
		if (player.prayerActive[1]) {
			strength += (int) (lvlForXP * .05);
		} else if (player.prayerActive[6]) {
			strength += (int) (lvlForXP * .10);
		} else if (player.prayerActive[14]) {
			strength += (int) (lvlForXP * .13);
		} else if (player.prayerActive[24]) {
			strength += (int) (lvlForXP * .16);
		} else if (player.prayerActive[25]) {
			strength += (int) (lvlForXP * .18);
		} else if (player.prayerActive[28]) {
			strength += (int) (lvlForXP * .20);
		}

		if (player.playerEquipment[player.playerHat] == 2526
				&& player.playerEquipment[player.playerChest] == 2520
				&& player.playerEquipment[player.playerLegs] == 2522) {
			maxHit += maxHit * 10 / 100;
		}
		maxHit += 1.05D + strBonus * strength * 0.00175D;
		maxHit += strength * 0.11D;
		if (player.playerEquipment[player.playerWeapon] == 4718
				&& player.playerEquipment[player.playerHat] == 4716
				&& player.playerEquipment[player.playerChest] == 4720
				&& player.playerEquipment[player.playerLegs] == 4722) {
			maxHit += (player.getFunction().getLevelForXP(player.playerXP[3]) - player.playerLevel[3]) / 2;
		}
		if (player.specDamage > 1) {
			maxHit = (int) (maxHit * player.specDamage);
		}
		if (maxHit < 0) {
			maxHit = 1;
		}
		if (player.fullVoidMelee()) {
			maxHit = (int) (maxHit * 1.05);
		}
		if (player.playerTitle == 2) {
			maxHit = (int) (maxHit * 1.01);
		} else if (player.playerTitle == 3) {
			maxHit = (int) (maxHit * 1.02);
		} else if (player.playerTitle == 4) {
			maxHit = (int) (maxHit * 1.04);
		} else if (player.isIronman()) {
			maxHit = (int) (maxHit * 1.10);
		} else if (player.isUltimateIronman()) {
			maxHit = (int) (maxHit * 1.20);
		}
		if (player.playerEquipment[player.playerAmulet] == 11128
				&& player.playerEquipment[player.playerWeapon] == 6528) {
			maxHit *= 1.20;
		}
		// System.out.println(maxHit);
		return (int) Math.floor(maxHit);
	}

}
