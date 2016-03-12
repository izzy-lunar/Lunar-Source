package com.ownxile.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.ownxile.rs2.player.Player;

public class Formula {

	private static Calendar calendar = new GregorianCalendar();

	public static int calculateCashAmount(int totalLevel) {
		int twentyFiveMill = (int) (totalLevel / 2 * totalLevel / 0.085);
		return twentyFiveMill / 4 + Misc.random(twentyFiveMill / 4) + 400000;
	}

	public static int calculateRPAmount(int totalLevel) {
		return (totalLevel / 40);
	}

	/**
	 * @param hoursPassed
	 * @return if less than 10 hours have passed, hours minus 10 divide by 7.5
	 *         to the power of 2 plus 1.1
	 */
	public static double expMultiplier(int hoursPassed) {
		if (hoursPassed < 10) {
			return Math.pow((hoursPassed - 10) / 7.5, 2) + 1.1;
		}
		return 1;
	}

	public static boolean isWeekend(Date date) {
		calendar.setTime(date);
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		return day == Calendar.SUNDAY || day == Calendar.SATURDAY;
	}

	public double getCombatLevel(Player player) {
		int attack = player.playerLevel[0], strength = player.playerLevel[1], defence = player.playerLevel[2], hitpoints = player.playerLevel[3], ranged = player.playerLevel[4], prayer = player.playerLevel[5], magic = player.playerLevel[6], summoning = player.playerLevel[21];
		double combatLevel = (defence + hitpoints + Math.floor(prayer / 2) + Math
				.floor(summoning / 2)) * 0.25;
		double warrior = (attack + strength) * 0.325;
		double ranger = ranged * 0.4875;
		double mage = magic * 0.4875;
		return combatLevel + Math.max(warrior, Math.max(ranger, mage));
	}
}
