package com.ownxile.rs2.combat.prayer;

import com.ownxile.config.GameConfig;
import com.ownxile.rs2.player.Client;
import com.ownxile.util.Misc;

public class Prayer {

	public static void activePrayer(Client player, int i) {
		if (player.playerLevel[5] < 1) {
			player.getFunction().sendFrame36(player.PRAYER_GLOW[i], 0);
			player.sendMessage(GameConfig.NO_PRAYER_MESSAGE);
			return;
		}
		if (player.duelRule[7]) {
			for (int p = 0; p < player.PRAYER.length; p++) { // reset prayer
																// glows
				player.prayerActive[p] = false;
				player.getFunction().sendFrame36(player.PRAYER_GLOW[p], 0);
			}
			player.sendMessage("Prayer has been disabled in this duel!");
			return;
		}
		if (i == 10) {
			player.getFunction().sendFrame36(player.PRAYER_GLOW[i], 0);
			player.getFunction().state(
					"The protect 1 item prayer is currently unavailable.");
			return;
		}
		if (i == 24
				&& player.getFunction().getLevelForXP(player.playerXP[1]) < 60) {
			player.getFunction().sendFrame36(player.PRAYER_GLOW[i], 0);
			player.getFunction()
					.state("You need a @blu@Defence level of 60@bla@ to use Chivalry.");
			return;
		}
		if (i == 25
				&& player.getFunction().getLevelForXP(player.playerXP[1]) < 70) {
			player.getFunction().sendFrame36(player.PRAYER_GLOW[i], 0);
			player.getFunction().state(
					"You need a @blu@Defence level of 70@bla@ to use Piety.");
			return;
		}
		if (i == 28
				&& player.getFunction().getLevelForXP(player.playerXP[1]) < 20) {
			player.getFunction().sendFrame36(player.PRAYER_GLOW[i], 0);
			player.getFunction().state(
					"You need a @blu@Defence level of 20@bla@ to use Turmoil.");
			return;
		}
		/*
		 * if(i > 18 && i < 26 && Config.MEMBERS == 0){
		 * player.getPA().sendFrame36(player.PRAYER_GLOW[i], 0);
		 * player.getPA().state
		 * ("You must login to a members server to use this prayer." ); return;
		 * }
		 */
		final int[] defPray = { 0, 5, 13, 24, 25, 28 };
		final int[] strPray = { 1, 6, 14, 24, 25, 28 };
		final int[] atkPray = { 2, 7, 15, 24, 25, 28 };
		final int[] rangePray = { 3, 11, 19 };
		final int[] magePray = { 4, 12, 20 };

		if (player.getFunction().getLevelForXP(player.playerXP[5]) >= player.PRAYER_LEVEL_REQUIRED[i]
				|| !GameConfig.PRAYER_LEVEL_REQUIRED) {
			boolean headIcon = false;
			switch (i) {
			case 0:
			case 5:
			case 13:
				if (player.prayerActive[i] == false) {
					for (int j = 0; j < defPray.length; j++) {
						if (defPray[j] != i) {
							player.prayerActive[defPray[j]] = false;
							player.getFunction().sendFrame36(
									player.PRAYER_GLOW[defPray[j]], 0);
						}
					}
				}
				break;

			case 1:
			case 6:
			case 14:
				if (player.prayerActive[i] == false) {
					for (int j = 0; j < strPray.length; j++) {
						if (strPray[j] != i) {
							player.prayerActive[strPray[j]] = false;
							player.getFunction().sendFrame36(
									player.PRAYER_GLOW[strPray[j]], 0);
						}
					}
					for (int j = 0; j < rangePray.length; j++) {
						if (rangePray[j] != i) {
							player.prayerActive[rangePray[j]] = false;
							player.getFunction().sendFrame36(
									player.PRAYER_GLOW[rangePray[j]], 0);
						}
					}
					for (int j = 0; j < magePray.length; j++) {
						if (magePray[j] != i) {
							player.prayerActive[magePray[j]] = false;
							player.getFunction().sendFrame36(
									player.PRAYER_GLOW[magePray[j]], 0);
						}
					}
				}
				break;

			case 2:
			case 7:
			case 15:
				if (player.prayerActive[i] == false) {
					for (int j = 0; j < atkPray.length; j++) {
						if (atkPray[j] != i) {
							player.prayerActive[atkPray[j]] = false;
							player.getFunction().sendFrame36(
									player.PRAYER_GLOW[atkPray[j]], 0);
						}
					}
					for (int j = 0; j < rangePray.length; j++) {
						if (rangePray[j] != i) {
							player.prayerActive[rangePray[j]] = false;
							player.getFunction().sendFrame36(
									player.PRAYER_GLOW[rangePray[j]], 0);
						}
					}
					for (int j = 0; j < magePray.length; j++) {
						if (magePray[j] != i) {
							player.prayerActive[magePray[j]] = false;
							player.getFunction().sendFrame36(
									player.PRAYER_GLOW[magePray[j]], 0);
						}
					}
				}
				break;

			case 3:// range prays
			case 11:
			case 19:
				if (player.prayerActive[i] == false) {
					for (int j = 0; j < atkPray.length; j++) {
						if (atkPray[j] != i) {
							player.prayerActive[atkPray[j]] = false;
							player.getFunction().sendFrame36(
									player.PRAYER_GLOW[atkPray[j]], 0);
						}
					}
					for (int j = 0; j < strPray.length; j++) {
						if (strPray[j] != i) {
							player.prayerActive[strPray[j]] = false;
							player.getFunction().sendFrame36(
									player.PRAYER_GLOW[strPray[j]], 0);
						}
					}
					for (int j = 0; j < rangePray.length; j++) {
						if (rangePray[j] != i) {
							player.prayerActive[rangePray[j]] = false;
							player.getFunction().sendFrame36(
									player.PRAYER_GLOW[rangePray[j]], 0);
						}
					}
					for (int j = 0; j < magePray.length; j++) {
						if (magePray[j] != i) {
							player.prayerActive[magePray[j]] = false;
							player.getFunction().sendFrame36(
									player.PRAYER_GLOW[magePray[j]], 0);
						}
					}
				}
				break;
			case 4:
			case 12:
			case 20:
				if (player.prayerActive[i] == false) {
					for (int j = 0; j < atkPray.length; j++) {
						if (atkPray[j] != i) {
							player.prayerActive[atkPray[j]] = false;
							player.getFunction().sendFrame36(
									player.PRAYER_GLOW[atkPray[j]], 0);
						}
					}
					for (int j = 0; j < strPray.length; j++) {
						if (strPray[j] != i) {
							player.prayerActive[strPray[j]] = false;
							player.getFunction().sendFrame36(
									player.PRAYER_GLOW[strPray[j]], 0);
						}
					}
					for (int j = 0; j < rangePray.length; j++) {
						if (rangePray[j] != i) {
							player.prayerActive[rangePray[j]] = false;
							player.getFunction().sendFrame36(
									player.PRAYER_GLOW[rangePray[j]], 0);
						}
					}
					for (int j = 0; j < magePray.length; j++) {
						if (magePray[j] != i) {
							player.prayerActive[magePray[j]] = false;
							player.getFunction().sendFrame36(
									player.PRAYER_GLOW[magePray[j]], 0);
						}
					}
				}
				break;
			case 10:
				player.lastProtItem = System.currentTimeMillis();
				break;

			case 16:
			case 17:
			case 18:
				if (System.currentTimeMillis() - player.stopPrayerDelay < 5000) {
					player.sendMessage("You have been injured and can't use this prayer!");
					player.getFunction().sendFrame36(player.PRAYER_GLOW[16], 0);
					player.getFunction().sendFrame36(player.PRAYER_GLOW[17], 0);
					player.getFunction().sendFrame36(player.PRAYER_GLOW[18], 0);
					return;
				}
				if (i == 16) {
					player.protMageDelay = System.currentTimeMillis();
				} else if (i == 17) {
					player.protRangeDelay = System.currentTimeMillis();
				} else if (i == 18) {
					player.protMeleeDelay = System.currentTimeMillis();
				}
			case 21:
			case 22:
			case 23:
			case 27:
				headIcon = true;
				for (int p = 16; p < 28; p++) {
					if (i != p && p != 19 && p != 20 && p != 24 && p != 25
							&& p != 26) {
						player.prayerActive[p] = false;
						player.getFunction().sendFrame36(player.PRAYER_GLOW[p],
								0);
					}
				}
				break;
			case 24:
				if (player.prayerActive[i] == false) {

					for (int j = 0; j < atkPray.length; j++) {
						if (atkPray[j] != i) {
							player.prayerActive[atkPray[j]] = false;
							player.getFunction().sendFrame36(
									player.PRAYER_GLOW[atkPray[j]], 0);
						}
					}
					for (int j = 0; j < strPray.length; j++) {
						if (strPray[j] != i) {
							player.prayerActive[strPray[j]] = false;
							player.getFunction().sendFrame36(
									player.PRAYER_GLOW[strPray[j]], 0);
						}
					}
					for (int j = 0; j < rangePray.length; j++) {
						if (rangePray[j] != i) {
							player.prayerActive[rangePray[j]] = false;
							player.getFunction().sendFrame36(
									player.PRAYER_GLOW[rangePray[j]], 0);
						}
					}
					for (int j = 0; j < magePray.length; j++) {
						if (magePray[j] != i) {
							player.prayerActive[magePray[j]] = false;
							player.getFunction().sendFrame36(
									player.PRAYER_GLOW[magePray[j]], 0);
						}
					}
					for (int j = 0; j < defPray.length; j++) {
						if (defPray[j] != i) {
							player.prayerActive[defPray[j]] = false;
							player.getFunction().sendFrame36(
									player.PRAYER_GLOW[defPray[j]], 0);
						}
					}
				}
			case 25:
			case 28:
				if (player.prayerActive[i] == false) {
					for (int j = 0; j < atkPray.length; j++) {
						if (atkPray[j] != i) {
							player.prayerActive[atkPray[j]] = false;
							player.getFunction().sendFrame36(
									player.PRAYER_GLOW[atkPray[j]], 0);
						}
					}
					for (int j = 0; j < strPray.length; j++) {
						if (strPray[j] != i) {
							player.prayerActive[strPray[j]] = false;
							player.getFunction().sendFrame36(
									player.PRAYER_GLOW[strPray[j]], 0);
						}
					}
					for (int j = 0; j < rangePray.length; j++) {
						if (rangePray[j] != i) {
							player.prayerActive[rangePray[j]] = false;
							player.getFunction().sendFrame36(
									player.PRAYER_GLOW[rangePray[j]], 0);
						}
					}
					for (int j = 0; j < magePray.length; j++) {
						if (magePray[j] != i) {
							player.prayerActive[magePray[j]] = false;
							player.getFunction().sendFrame36(
									player.PRAYER_GLOW[magePray[j]], 0);
						}
					}
					for (int j = 0; j < defPray.length; j++) {
						if (defPray[j] != i) {
							player.prayerActive[defPray[j]] = false;
							player.getFunction().sendFrame36(
									player.PRAYER_GLOW[defPray[j]], 0);
						}
					}
				}
				break;
			}

			if (!headIcon) {
				if (player.prayerActive[i] == false) {
					player.prayerActive[i] = true;
					player.getFunction().sendFrame36(player.PRAYER_GLOW[i], 1);
				} else {
					player.prayerActive[i] = false;
					player.getFunction().sendFrame36(player.PRAYER_GLOW[i], 0);
				}
			} else {
				if (player.prayerActive[i] == false) {
					player.prayerActive[i] = true;
					player.getFunction().sendFrame36(player.PRAYER_GLOW[i], 1);
					player.headIcon = player.PRAYER_HEAD_ICONS[i];
					player.getFunction().requestUpdates();
				} else {
					player.prayerActive[i] = false;
					player.getFunction().sendFrame36(player.PRAYER_GLOW[i], 0);
					player.headIcon = -1;
					player.getFunction().requestUpdates();
				}
			}
		} else {
			player.getFunction().sendFrame36(player.PRAYER_GLOW[i], 0);
			player.getFunction().sendFrame126(
					"You need a @blu@Prayer level of "
							+ player.PRAYER_LEVEL_REQUIRED[i] + " @bla@to use "
							+ player.PRAYER_NAME[i] + ".", 357);
			player.getFunction().sendFrame126("Click here to continue", 358);
			player.getFunction().sendFrame164(356);
			player.nextChat = 0;
		}

	}

	public static void alterRecharge(Client client) {
		if (client.playerLevel[5] < client.getFunction().getLevelForXP(
				client.playerXP[5])) {
			client.startAnimation(645);
			client.playerLevel[5] = client.getFunction().getLevelForXP(
					client.playerXP[5]);
			client.sendMessage("You recharge your prayer points.");
			client.getFunction().refreshSkill(5);
		} else {
			client.sendMessage("You already have full prayer points.");
		}
	}

	public static void handleRedemption(Client player) {

		if (player.prayerActive[22]) {
			player.playerLevel[3] += (int) (player
					.getLevelForXP(player.playerXP[5]) * .25);
			player.playerLevel[5] = 0;
			player.getTask().refreshSkill(3);
			player.getTask().refreshSkill(5);
			player.gfx0(436);
			resetPrayers(player);
		}
	}

	public static void handleRetribution(Client killer, Client player) {
		if (player.prayerActive[21]) {
			int retDamage = 3 + Misc.random(17);
			player.gfx0(437);
			killer.playerLevel[3] -= retDamage;
			killer.handleHitMask(retDamage);
			killer.getFunction().refreshSkill(3);
		}
	}

	public static void resetPrayers(Client player) {
		for (int i = 0; i < player.prayerActive.length; i++) {
			player.prayerActive[i] = false;
			player.getFunction().sendFrame36(player.PRAYER_GLOW[i], 0);
		}
		player.headIcon = -1;
		player.getFunction().requestUpdates();
	}

}
