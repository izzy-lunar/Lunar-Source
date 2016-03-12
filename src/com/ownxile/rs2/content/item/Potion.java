package com.ownxile.rs2.content.item;

import com.ownxile.rs2.items.ItemAssistant;
import com.ownxile.rs2.player.Client;

public class Potion {

	private final Client client;

	public Potion(Client client) {
		this.client = client;
	}

	public void curePoison(long delay) {
		client.poisonDamage = 0;
		client.poisonImmune = delay;
		client.lastPoisonSip = System.currentTimeMillis();
	}

	public void doTheBrew(int itemId, int replaceItem, int slot) {
		if (client.duelRule[6]) {
			client.sendMessage("You may not eat in this duel.");
			return;
		}
		client.startAnimation(829);
		client.playerItems[slot] = replaceItem + 1;
		client.getItems().resetItems(3214);
		final int[] toDecrease = { 0, 2, 4, 6 };

		for (final int tD : toDecrease) {
			client.playerLevel[tD] -= getBrewStat(tD, .10);
			if (client.playerLevel[tD] < 0) {
				client.playerLevel[tD] = 1;
			}
			client.getFunction().refreshSkill(tD);
			client.getFunction().setSkillLevel(tD, client.playerLevel[tD],
					client.playerXP[tD]);
		}
		client.playerLevel[1] += getBrewStat(1, .20);
		if (client.playerLevel[1] > client.getLevelForXP(client.playerXP[1]) * 1.2 + 1) {
			client.playerLevel[1] = (int) (client
					.getLevelForXP(client.playerXP[1]) * 1.2);
		}
		client.getFunction().refreshSkill(1);

		client.playerLevel[3] += getBrewStat(3, .15);
		if (client.playerLevel[3] > client.getLevelForXP(client.playerXP[3]) * 1.17 + 1) {
			client.playerLevel[3] = (int) (client
					.getLevelForXP(client.playerXP[3]) * 1.17);
		}
		client.getFunction().refreshSkill(3);
	}

	public void drinkAntiPoison(int itemId, int replaceItem, int slot,
			long delay) {
		client.startAnimation(829);
		client.playerItems[slot] = replaceItem + 1;
		client.getItems().resetItems(3214);
		curePoison(delay);
	}

	public void drinkExtreme() {
		client.sendMessage("You drink the potion.. It's really thick, you struggle to swallow it.");
		extremeStat(0);
		extremeStat(2);
		extremeStat(4);
		extremeStat(6);
		client.startAnimation(829);
	}

	public void drinkPrayerPot(int itemId, int replaceItem, int slot,
			boolean rest) {
		client.startAnimation(829);
		client.playerItems[slot] = replaceItem + 1;
		client.getItems().resetItems(3214);
		client.playerLevel[5] += client.getLevelForXP(client.playerXP[5]) * .33;
		if (rest) {
			client.playerLevel[5] += 1;
		}
		if (client.playerLevel[5] > client.getLevelForXP(client.playerXP[5])) {
			client.playerLevel[5] = client.getLevelForXP(client.playerXP[5]);
		}
		client.getFunction().refreshSkill(5);
		if (rest) {
			restoreStats();
		}
	}

	public void drinkStatPotion(int itemId, int replaceItem, int slot,
			int stat, boolean sup) {
		client.startAnimation(829);
		client.playerItems[slot] = replaceItem + 1;
		client.getItems().resetItems(3214);
		enchanceStat(stat, sup);
	}

	public void enchanceStat(int skillID, boolean sup) {
		client.playerLevel[skillID] += getBoostedStat(skillID, sup);
		client.getFunction().refreshSkill(skillID);
	}

	public void extremeStat(int skillID) {
		client.playerLevel[skillID] += getExtremeStat(skillID);
		client.getFunction().refreshSkill(skillID);
	}

	public int getBoostedStat(int skill, boolean sup) {
		int increaseBy = 0;
		if (sup) {
			increaseBy = (int) (client.getLevelForXP(client.playerXP[skill]) * .20);
		} else {
			increaseBy = (int) (client.getLevelForXP(client.playerXP[skill]) * .13) + 1;
		}
		if (client.playerLevel[skill] + increaseBy > client
				.getLevelForXP(client.playerXP[skill]) + increaseBy + 1) {
			return client.getLevelForXP(client.playerXP[skill]) + increaseBy
					- client.playerLevel[skill];
		}
		return increaseBy;
	}

	public int getBrewStat(int skill, double amount) {
		return (int) (client.getLevelForXP(client.playerXP[skill]) * amount);
	}

	public int getExtremeStat(int skill) {
		int increaseBy = 0;
		increaseBy = (int) (client.getLevelForXP(client.playerXP[skill]) * .27);
		if (client.playerLevel[skill] + increaseBy > client
				.getLevelForXP(client.playerXP[skill]) + increaseBy + 1) {
			return client.getLevelForXP(client.playerXP[skill]) + increaseBy
					- client.playerLevel[skill];
		}
		return increaseBy;
	}

	public void handlePotion(int itemId, int slot) {
		if (client.duelRule[5]) {
			client.sendMessage("You may not drink potions in this duel.");
			return;
		}
		if (System.currentTimeMillis() - client.potDelay >= 1500) {
			client.potDelay = System.currentTimeMillis();
			client.foodDelay = client.potDelay;
			client.getCombat().resetPlayerAttack();
			client.attackTimer++;
			client.getItems();
			String item = ItemAssistant.getItemName(itemId);
			client.sendMessage("You drink some of your " + item + ".");
			if (item.endsWith("(4)")) {
				client.getTask().delayMessage(
						"You have 3 doses of potion left.", 1);
			} else if (item.endsWith("(3)")) {
				client.getTask().delayMessage(
						"You have 2 doses of potion left.", 1);
			} else if (item.endsWith("(2)")) {
				client.getTask().delayMessage(
						"You have 1 dose of potion left.", 1);
			} else if (item.endsWith("(1)")) {
				client.getTask().delayMessage("You have finished your potion.",
						1);
			}
			switch (itemId) {
			case 6685: // brews
				doTheBrew(itemId, 6687, slot);
				break;
			case 6687:
				doTheBrew(itemId, 6689, slot);
				break;
			case 6689:
				doTheBrew(itemId, 6691, slot);
				break;
			case 6691:
				doTheBrew(itemId, 229, slot);
				break;
			case 2436:
				drinkStatPotion(itemId, 145, slot, 0, true); // sup attack
				break;
			case 145:
				drinkStatPotion(itemId, 147, slot, 0, true);
				break;
			case 147:
				drinkStatPotion(itemId, 149, slot, 0, true);
				break;
			case 149:
				drinkStatPotion(itemId, 229, slot, 0, true);
				break;
			case 2440:
				drinkStatPotion(itemId, 157, slot, 2, true); // sup str
				break;
			case 157:
				drinkStatPotion(itemId, 159, slot, 2, true);
				break;
			case 159:
				drinkStatPotion(itemId, 161, slot, 2, true);
				break;
			case 161:
				drinkStatPotion(itemId, 229, slot, 2, true);
				break;
			case 2444:
				drinkStatPotion(itemId, 169, slot, 4, false); // range pot
				break;
			case 169:
				drinkStatPotion(itemId, 171, slot, 4, false);
				break;
			case 171:
				drinkStatPotion(itemId, 173, slot, 4, false);
				break;
			case 173:
				drinkStatPotion(itemId, 229, slot, 4, false);
				break;
			case 2432:
				drinkStatPotion(itemId, 133, slot, 1, false); // def pot
				break;
			case 133:
				drinkStatPotion(itemId, 135, slot, 1, false);
				break;
			case 135:
				drinkStatPotion(itemId, 137, slot, 1, false);
				break;
			case 137:
				drinkStatPotion(itemId, 229, slot, 1, false);
				break;
			case 113:
				drinkStatPotion(itemId, 115, slot, 2, false); // str pot
				break;
			case 115:
				drinkStatPotion(itemId, 117, slot, 2, false);
				break;
			case 117:
				drinkStatPotion(itemId, 119, slot, 2, false);
				break;
			case 119:
				drinkStatPotion(itemId, 229, slot, 2, false);
				break;
			case 2428:
				drinkStatPotion(itemId, 121, slot, 0, false); // attack pot
				break;
			case 121:
				drinkStatPotion(itemId, 123, slot, 0, false);
				break;
			case 123:
				drinkStatPotion(itemId, 125, slot, 0, false);
				break;
			case 125:
				drinkStatPotion(itemId, 229, slot, 0, false);
				break;
			case 2442:
				drinkStatPotion(itemId, 163, slot, 1, true); // super def pot
				break;
			case 163:
				drinkStatPotion(itemId, 165, slot, 1, true);
				break;
			case 165:
				drinkStatPotion(itemId, 167, slot, 1, true);
				break;
			case 167:
				drinkStatPotion(itemId, 229, slot, 1, true);
				break;
			case 3040:
				drinkStatPotion(itemId, 3042, slot, 6, false); // magic pot
				break;
			case 3042:
				drinkStatPotion(itemId, 3044, slot, 6, false);
				break;
			case 3044:
				drinkStatPotion(itemId, 3046, slot, 6, false);
				break;
			case 3046:
				drinkStatPotion(itemId, 229, slot, 6, false);
				break;
			case 3024:
				drinkPrayerPot(itemId, 3026, slot, true); // sup restore
				break;
			case 3026:
				drinkPrayerPot(itemId, 3028, slot, true);
				break;
			case 3028:
				drinkPrayerPot(itemId, 3030, slot, true);
				break;
			case 3030:
				drinkPrayerPot(itemId, 229, slot, true);
				break;
			case 10925:
				drinkPrayerPot(itemId, 10927, slot, true); // sanfew serums
				curePoison(300000);
				break;
			case 10927:
				drinkPrayerPot(itemId, 10929, slot, true);
				curePoison(300000);
				break;
			case 10929:
				drinkPrayerPot(itemId, 10931, slot, true);
				curePoison(300000);
				break;
			case 10931:
				drinkPrayerPot(itemId, 229, slot, true);
				curePoison(300000);
				break;
			case 3032:
				this.drinkStatPotion(itemId, 3034, slot, client.playerAgility,
						false);
				break;
			case 3034:
				this.drinkStatPotion(itemId, 3036, slot, client.playerAgility,
						false);
				break;
			case 3036:
				this.drinkStatPotion(itemId, 3038, slot, client.playerAgility,
						false);
				break;
			case 3038:
				this.drinkStatPotion(itemId, 229, slot, client.playerAgility,
						false);
				break;
			case 2434:
				drinkPrayerPot(itemId, 139, slot, false); // pray pot
				break;
			case 139:
				drinkPrayerPot(itemId, 141, slot, false);
				break;
			case 141:
				drinkPrayerPot(itemId, 143, slot, false);
				break;
			case 143:
				drinkPrayerPot(itemId, 229, slot, false);
				break;
			case 2446:
				drinkAntiPoison(itemId, 175, slot, 30000); // anti poisons
				break;
			case 175:
				drinkAntiPoison(itemId, 177, slot, 30000);
				break;
			case 177:
				drinkAntiPoison(itemId, 179, slot, 30000);
				break;
			case 179:
				drinkAntiPoison(itemId, 229, slot, 30000);
				break;
			case 2448:
				drinkAntiPoison(itemId, 181, slot, 300000); // anti poisons
				break;
			case 181:
				drinkAntiPoison(itemId, 183, slot, 300000);
				break;
			case 183:
				drinkAntiPoison(itemId, 185, slot, 300000);
				break;
			case 185:
				drinkAntiPoison(itemId, 229, slot, 300000);
				break;
			}
		}
	}

	public boolean isPotion(int itemId) {
		client.getItems();
		final String name = ItemAssistant.getItemName(itemId);
		return name.contains("(4)") || name.contains("(3)")
				|| name.contains("(2)") || name.contains("(1)");
	}

	public void restoreStats() {
		for (int j = 0; j <= 6; j++) {
			if (j == 5 || j == 3) {
				continue;
			}
			if (client.playerLevel[j] < client
					.getLevelForXP(client.playerXP[j])) {
				client.playerLevel[j] += client
						.getLevelForXP(client.playerXP[j]) * .33;
				if (client.playerLevel[j] > client
						.getLevelForXP(client.playerXP[j])) {
					client.playerLevel[j] = client
							.getLevelForXP(client.playerXP[j]);
				}
				client.getFunction().refreshSkill(j);
				client.getFunction().setSkillLevel(j, client.playerLevel[j],
						client.playerXP[j]);
			}
		}
	}
}