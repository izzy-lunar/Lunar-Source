package com.ownxile.rs2.content.click;

import com.ownxile.rs2.combat.range.CannonHandler;
import com.ownxile.rs2.content.action.DiceRolling;
import com.ownxile.rs2.content.action.Digging;
import com.ownxile.rs2.content.item.Beverage;
import com.ownxile.rs2.content.item.BuryBones;
import com.ownxile.rs2.content.item.Casket;
import com.ownxile.rs2.content.item.Teletab;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.skills.magic.EnchantJewellery;
import com.ownxile.rs2.world.games.ChampionsChallenge;
import com.ownxile.rs2.world.games.TreasureTrails;
import com.ownxile.util.Correction;
import com.ownxile.util.Misc;

public class ItemClickReaction {

	public static final int FIRST_CLICK = 0, SECOND_CLICK = 1, THIRD_CLICK = 2;

	public static void executeClick(Client c, int clickType, int itemId,
			int itemSlot) {
		switch (clickType) {
		case FIRST_CLICK:
			firstClick(c, itemId, itemSlot);
			break;
		case SECOND_CLICK:
			secondClick(c, itemId, itemSlot);
			break;
		case THIRD_CLICK:
			thirdClick(c, itemId, itemSlot);
			break;
		}
	}

	private static void firstClick(Client c, int itemId, int itemSlot) {
		EnchantJewellery.handleItem(c, itemId);
		if (ChampionsChallenge.getInstance().isChampion(itemId)) {
			ChampionsChallenge.getInstance().handleItem(c, itemId);
			return;
		}
		if (Beverage.handleBeverage(c, itemId)) {
			return;
		}
		switch (itemId) {
		case 10835:

			DiceRolling.initiateDiceRoll(c);
			break;
		case 8007:
		case 8008:
		case 8009:
		case 8010:
		case 8011:
		case 8012:
		case 8013:
			Teletab.handleItem(c, itemId);
			break;
		case 4155:
			c.getSlayer().sendCurrentTask();
			break;
		case 2714:
		case 2715:
		case 2717:
			Casket.handleItem(c, itemId, itemSlot);
			break;
		case 3062:
			// if (DateAndTime.getTodaysDate().equals("25.12.2012")) {
			c.getItems().deleteItem2(3062, 1);
			int random = Misc.random(5000000);
			String amount = Correction.getAmountString(random);
			c.getItems().addItem(995, random);
			c.sendMessage("You open the box and find receive " + amount + ".");
			// } else {
			// c.sendMessage("This box must be opened on Christmas day.");
			// }
			break;
		case 2678:
			c.getItems().deleteItem(itemId, 1);
			TreasureTrails.addReward(c, 1);
			break;
		case 6199:
			c.getItems().deleteItem(6199, 1);
			c.getItems().addItem(555, 600);
			c.getItems().addItem(560, 400);
			c.getItems().addItem(565, 200);
			break;
		case 6:
			CannonHandler.setupCannon(c);
			break;
		case 4053:
			if (c.cades < 4) {
				c.getItems().deleteItem(4053, 1);
				c.cades++;
			} else {
				c.sendMessage("You have already setup enough barricades.");
			}
			break;
		case 2679:
			c.getItems().deleteItem(itemId, 1);
			TreasureTrails.addReward(c, 2);
			break;
		case 2376:
			c.getFunction().showInterface(17620);
			c.startAnimation(1350);
			break;
		case 608:
			c.getFunction().listScroll("@dre@Plans",
					"The thug needs to kill the rebellion",
					"behind the lumbridge castle",
					"Execution must be clean, make sure no one sees",
					"Dispose of his body in the swamp, be discreat.");
			break;
		case 4033:
			c.getChat().sendChat(1006, 1452);
			c.nextChat = 0;
			break;
		case 952:
			if (c.getItems().freeSlots() < 1) {
				c.sendMessage("You required space in your inventory before attempting to dig..");
				return;
			}
			c.sendMessage("You dig the ground.");
			c.startAnimation(830);

			if (Digging.handleDigging(c)) {
				return;
			}
			if (c.inArea(3553, 3301, 3561, 3294)) {
				c.teleTimer = 3;
				c.newLocation = 1;
			} else if (c.inArea(3550, 3287, 3557, 3278)) {
				c.teleTimer = 3;
				c.newLocation = 2;
			} else if (c.inArea(3561, 3292, 3568, 3285)) {
				c.teleTimer = 3;
				c.newLocation = 3;
			} else if (c.inArea(3570, 3302, 3579, 3293)) {
				c.teleTimer = 3;
				c.newLocation = 4;
			} else if (c.inArea(3571, 3285, 3582, 3278)) {
				c.teleTimer = 3;
				c.newLocation = 5;
			} else if (c.inArea(3562, 3279, 3569, 3273)) {
				c.teleTimer = 3;
				c.newLocation = 6;
			}
			break;

		}
		if (c.getHerblore().isUnidHerb(itemId)) {
			c.getHerblore().handleHerbClick(itemId);
		}
		if (BuryBones.isBone(itemId)) {
			BuryBones.buryBone(c, itemId, itemSlot);
		}

	}

	private static void secondClick(Client c, int itemId, int itemSlot) {

		switch (itemId) {
		case 4566:
			c.startAnimation(1835);
			return;
		case 11694:
			c.sendMessage("That action is no longer available.");
			break;
		case 11696:
			c.sendMessage("That action is no longer available.");
			break;
		case 11698:
			c.sendMessage("That action is no longer available.");
			break;
		case 11700:
			c.sendMessage("That action is no longer available.");
			break;
		default:
			c.sendMessage("Nothing interesting happens.");
			if (c.playerRights == 3) {
				Misc.println(c.playerName + " - Item2nddOption: " + itemId);
			}
			break;
		}

	}

	private static void thirdClick(Client c, int itemId, int itemSlot) {

		switch (itemId) {
		case 981:
			c.getFunction().spellTeleport(2095, 4430, 0);
			break;
		case 227:
			c.getItems().replaceItem(227, 229, 1, 1);
			break;
		case 1933:
			c.deleteItem(1933);
			c.addItem(1931);
			c.sendMessage("You empty the pot of flour.");
			break;
		case 1927:
			c.deleteItem(1927);
			c.addItem(1925);
			c.sendMessage("You empty the bucket of cow spunk.");
			break;
		default:
			c.sendMessage("Nothing interesting happens.");
			if (c.playerRights == 3) {
				Misc.println(c.playerName + " - Item3rdOption: " + itemId
						+ " : " + itemId + " : " + itemId);
			}
			break;
		}

	}
}
