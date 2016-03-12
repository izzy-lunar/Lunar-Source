package com.ownxile.rs2.items;

import com.ownxile.config.SkillConfig;
import com.ownxile.core.Plugin;
import com.ownxile.core.World;
import com.ownxile.rs2.content.item.BuryBones;
import com.ownxile.rs2.content.item.Rope;
import com.ownxile.rs2.content.object.CrystalChest;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.skills.cooking.Cooking;
import com.ownxile.rs2.skills.crafting.Spinning;
import com.ownxile.rs2.skills.fletching.BoltMaking;
import com.ownxile.rs2.skills.fletching.Stringing;
import com.ownxile.rs2.skills.herblore.Grinding;
import com.ownxile.rs2.world.games.ChampionsChallenge;
import com.ownxile.rs2.world.games.WarriorsGuild;
import com.ownxile.util.Misc;

public class UseItem {

	public static void itemonItem(Client client, int itemUsed, int useWith) {

		if (Plugin.execute("use_item_" + itemUsed + "_on_" + useWith, client)) {
			return;
		}
		BoltMaking.handleBoltCrafting(client, itemUsed, useWith);
		BoltMaking.handleBoltTipCrafting(client, itemUsed, useWith);
		client.getFiremaking().initialize(itemUsed, useWith);
		Grinding.init(client, itemUsed, useWith);
		if (itemUsed == 227 || useWith == 227) {
			client.getHerblore().handlePotMaking(itemUsed, useWith);
		}
		if (itemUsed == 1777 || useWith == 1777) {
			Stringing.stringBow(client, itemUsed, useWith);
		}
		if (ItemAssistant.getItemName(itemUsed).contains("(")
				&& ItemAssistant.getItemName(useWith).contains("(")) {
			client.getPotMixing().mixPotion2(itemUsed, useWith);
		}
		if (itemUsed == 1733 || useWith == 1733) {
			client.getCrafting().handleLeather(itemUsed, useWith);
		}
		if (itemUsed == 1755 || useWith == 1755) {
			client.getCrafting().handleChisel(itemUsed, useWith);
		}
		if (itemUsed == 946 || useWith == 946) {
			client.getFletching().handleLog(itemUsed, useWith);
		}
		if (itemUsed == 53 || useWith == 53 || itemUsed == 52 || useWith == 52) {
			client.getFletching().makeArrows(itemUsed, useWith);
		}
		if (itemUsed == 1540 && useWith == 11286 || itemUsed == 11286
				&& useWith == 1540) {
			if (client.playerLevel[client.playerSmithing] >= 95) {
				client.getItems().deleteItem(1540,
						client.getItems().getItemSlot(1540), 1);
				client.getItems().deleteItem(11286,
						client.getItems().getItemSlot(11286), 1);
				client.getItems().addItem(11284, 1);
				client.sendMessage("You combine the two materials to create a dragonfire shield.");
				client.getFunction().addSkillXP(
						500 * SkillConfig.SMITHING_EXPERIENCE,
						client.playerSmithing);
			} else {
				client.sendMessage("You need a smithing level of 95 to create a dragonfire shield.");
			}
		}
		if (itemUsed == 9142 && useWith == 9190 || itemUsed == 9190
				&& useWith == 9142) {
			if (client.playerLevel[client.playerFletching] >= 58) {
				final int boltsMade = client.getItems().getItemAmount(itemUsed) > client
						.getItems().getItemAmount(useWith) ? client.getItems()
						.getItemAmount(useWith) : client.getItems()
						.getItemAmount(itemUsed);
				client.getItems().deleteItem(useWith,
						client.getItems().getItemSlot(useWith), boltsMade);
				client.getItems().deleteItem(itemUsed,
						client.getItems().getItemSlot(itemUsed), boltsMade);
				client.getItems().addItem(9241, boltsMade);
				client.getFunction().addSkillXP(
						boltsMade * 6 * SkillConfig.FLETCHING_EXPERIENCE,
						client.playerFletching);
			} else {
				client.sendMessage("You need a fletching level of 58 to fletch this item.");
			}
		}
		if (itemUsed == 9143 && useWith == 9191 || itemUsed == 9191
				&& useWith == 9143) {
			if (client.playerLevel[client.playerFletching] >= 63) {
				final int boltsMade = client.getItems().getItemAmount(itemUsed) > client
						.getItems().getItemAmount(useWith) ? client.getItems()
						.getItemAmount(useWith) : client.getItems()
						.getItemAmount(itemUsed);
				client.getItems().deleteItem(useWith,
						client.getItems().getItemSlot(useWith), boltsMade);
				client.getItems().deleteItem(itemUsed,
						client.getItems().getItemSlot(itemUsed), boltsMade);
				client.getItems().addItem(9242, boltsMade);
				client.getFunction().addSkillXP(
						boltsMade * 7 * SkillConfig.FLETCHING_EXPERIENCE,
						client.playerFletching);
			} else {
				client.sendMessage("You need a fletching level of 63 to fletch this item.");
			}
		}
		if (itemUsed == 9143 && useWith == 9192 || itemUsed == 9192
				&& useWith == 9143) {
			if (client.playerLevel[client.playerFletching] >= 65) {
				final int boltsMade = client.getItems().getItemAmount(itemUsed) > client
						.getItems().getItemAmount(useWith) ? client.getItems()
						.getItemAmount(useWith) : client.getItems()
						.getItemAmount(itemUsed);
				client.getItems().deleteItem(useWith,
						client.getItems().getItemSlot(useWith), boltsMade);
				client.getItems().deleteItem(itemUsed,
						client.getItems().getItemSlot(itemUsed), boltsMade);
				client.getItems().addItem(9243, boltsMade);
				client.getFunction().addSkillXP(
						boltsMade * 7 * SkillConfig.FLETCHING_EXPERIENCE,
						client.playerFletching);
			} else {
				client.sendMessage("You need a fletching level of 65 to fletch this item.");
			}
		}
		if (itemUsed == 9144 && useWith == 9193 || itemUsed == 9193
				&& useWith == 9144) {
			if (client.playerLevel[client.playerFletching] >= 71) {
				final int boltsMade = client.getItems().getItemAmount(itemUsed) > client
						.getItems().getItemAmount(useWith) ? client.getItems()
						.getItemAmount(useWith) : client.getItems()
						.getItemAmount(itemUsed);
				client.getItems().deleteItem(useWith,
						client.getItems().getItemSlot(useWith), boltsMade);
				client.getItems().deleteItem(itemUsed,
						client.getItems().getItemSlot(itemUsed), boltsMade);
				client.getItems().addItem(9244, boltsMade);
				client.getFunction().addSkillXP(
						boltsMade * 10 * SkillConfig.FLETCHING_EXPERIENCE,
						client.playerFletching);
			} else {
				client.sendMessage("You need a fletching level of 71 to fletch this item.");
			}
		}
		if (itemUsed == 9144 && useWith == 9194 || itemUsed == 9194
				&& useWith == 9144) {
			if (client.playerLevel[client.playerFletching] >= 58) {
				final int boltsMade = client.getItems().getItemAmount(itemUsed) > client
						.getItems().getItemAmount(useWith) ? client.getItems()
						.getItemAmount(useWith) : client.getItems()
						.getItemAmount(itemUsed);
				client.getItems().deleteItem(useWith,
						client.getItems().getItemSlot(useWith), boltsMade);
				client.getItems().deleteItem(itemUsed,
						client.getItems().getItemSlot(itemUsed), boltsMade);
				client.getItems().addItem(9245, boltsMade);
				client.getFunction().addSkillXP(
						boltsMade * 13 * SkillConfig.FLETCHING_EXPERIENCE,
						client.playerFletching);
			} else {
				client.sendMessage("You need a fletching level of 58 to fletch this item.");
			}
		}
		if (itemUsed == 1601 && useWith == 1755 || itemUsed == 1755
				&& useWith == 1601) {
			if (client.playerLevel[client.playerFletching] >= 63) {
				client.getItems().deleteItem(1601,
						client.getItems().getItemSlot(1601), 1);
				client.getItems().addItem(9192, 15);
				client.getFunction().addSkillXP(
						8 * SkillConfig.FLETCHING_EXPERIENCE,
						client.playerFletching);
			} else {
				client.sendMessage("You need a fletching level of 63 to fletch this item.");
			}
		}
		if (itemUsed == 1607 && useWith == 1755 || itemUsed == 1755
				&& useWith == 1607) {
			if (client.playerLevel[client.playerFletching] >= 65) {
				client.getItems().deleteItem(1607,
						client.getItems().getItemSlot(1607), 1);
				client.getItems().addItem(9189, 15);
				client.getFunction().addSkillXP(
						8 * SkillConfig.FLETCHING_EXPERIENCE,
						client.playerFletching);
			} else {
				client.sendMessage("You need a fletching level of 65 to fletch this item.");
			}
		}
		if (itemUsed == 1605 && useWith == 1755 || itemUsed == 1755
				&& useWith == 1605) {
			if (client.playerLevel[client.playerFletching] >= 71) {
				client.getItems().deleteItem(1605,
						client.getItems().getItemSlot(1605), 1);
				client.getItems().addItem(9190, 15);
				client.getFunction().addSkillXP(
						8 * SkillConfig.FLETCHING_EXPERIENCE,
						client.playerFletching);
			} else {
				client.sendMessage("You need a fletching level of 71 to fletch this item.");
			}
		}
		if (itemUsed == 7573 && useWith == 233 || itemUsed == 233
				&& useWith == 7573) {
			if (client.playerLevel[client.playerHerblore] >= 75) {
				client.getItems().deleteItem(7573,
						client.getItems().getItemSlot(7573), 1);
				client.getItems().addItem(235, 1);
				client.startAnimation(885);
				client.sendMessage("You grind the special monkey nuts into Nut dust.");
				client.getFunction().addSkillXP(15000, client.playerHerblore);
			} else {
				client.sendMessage("You need a herblore level of 75 to produce this potion.");
			}
		}
		if (itemUsed == 235 && useWith == 227 || itemUsed == 227
				&& useWith == 235) {
			if (client.playerLevel[client.playerHerblore] >= 50) {
				client.getItems().deleteItem(235,
						client.getItems().getItemSlot(235), 1);
				client.getItems().deleteItem(227,
						client.getItems().getItemSlot(227), 1);
				client.getItems().addItem(9739, 1);
				client.startAnimation(1652);
				client.getFunction().addSkillXP(
						20 * SkillConfig.HERBLORE_EXPERIENCE,
						client.playerHerblore);
			} else {
				client.sendMessage("You need a herblore level of 50 to produce this potion.");
			}
		}
		if (itemUsed == 1603 && useWith == 1755 || itemUsed == 1755
				&& useWith == 1603) {
			if (client.playerLevel[client.playerFletching] >= 73) {
				client.getItems().deleteItem(1603,
						client.getItems().getItemSlot(1603), 1);
				client.getItems().addItem(9191, 15);
				client.getFunction().addSkillXP(
						8 * SkillConfig.FLETCHING_EXPERIENCE,
						client.playerFletching);
			} else {
				client.sendMessage("You need a fletching level of 73 to fletch this item.");
			}
		}
		if (itemUsed == 1615 && useWith == 1755 || itemUsed == 1755
				&& useWith == 1615) {
			if (client.playerLevel[client.playerFletching] >= 73) {
				client.getItems().deleteItem(1615,
						client.getItems().getItemSlot(1615), 1);
				client.getItems().addItem(9193, 15);
				client.getFunction().addSkillXP(
						8 * SkillConfig.FLETCHING_EXPERIENCE,
						client.playerFletching);
			} else {
				client.sendMessage("You need a fletching level of 73 to fletch this item.");
			}
		}
		if (itemUsed == 2368 && useWith == 2366 || itemUsed == 2366
				&& useWith == 2368) {
			client.getItems().deleteItem(2368,
					client.getItems().getItemSlot(2368), 1);
			client.getItems().deleteItem(2366,
					client.getItems().getItemSlot(2366), 1);
			client.getItems().addItem(1187, 1);
		}
		if (itemUsed == CrystalChest.toothHalf()
				&& useWith == CrystalChest.loopHalf()
				|| itemUsed == CrystalChest.loopHalf()
				&& useWith == CrystalChest.toothHalf()) {
			CrystalChest.makeKey(client);
		}

	}

	public static void ItemonObject(Client client, int objectID, int objectX,
			int objectY, int itemId) {

		client.turnPlayerTo(objectX, objectY);
		if (!client.getItems().playerHasItem(itemId, 1)) {
			return;
		}

		if (Plugin.execute("use_item_" + itemId + "_on_object_" + objectID, client)) {
			return;
		}
		if (itemId == 954) {
			Rope.onObject(client, objectID);
			return;
		}
		switch (objectID) {
		case 2644:
		case 4309:
		case 5707:
			if (Spinning.isSpinnable(itemId))
				Spinning.spinInterface(client);
			else
				client.sendMessage("You can't spin this item.");
			break;
		case 15621:
			WarriorsGuild.armourOnAnimator(client, itemId);
			break;
		case 594:
		case 595:
		case 26959:
			if (client.playerRights < 2) {
				client.startAnimation(832);
				int amount = client.getItems().getItemCount(itemId);
				client.deleteItem(itemId, amount);
				GroundItemHandler.createGroundItem(client, itemId, objectX,
						objectY, client.absZ, amount, client.getId());
			}
			break;
		case 2714:
			if (itemId == 1947) {
				if (client.hasItem(1931)) {
					client.deleteItem(1947);
					client.deleteItem(1931);
					client.addItem(1933);
					client.sendMessage("You use the hopper to fill your pot with flour.");
				} else {
					client.sendMessage("You need a pot to fill with flour.");
				}
			} else if (itemId == 1931) {
				if (client.hasItem(1947)) {
					client.deleteItem(1947);
					client.deleteItem(1931);
					client.addItem(1933);
					client.sendMessage("You use the hopper to fill your pot with flour.");
				} else {
					client.sendMessage("You need a pot to fill with flour.");
				}
			}
			break;
		case 10556:
			if (ChampionsChallenge.getInstance().isChampion(itemId)) {
				ChampionsChallenge.getInstance().spawnChampion(client, itemId);
				return;
			}
			break;
		case 4421:
		case 4437:
			if (itemId == 4045) {
				client.getItems().deleteItem(4045, 1);
				World.getObjectHandler().globalObject(-1, objectX, objectY,
						client.absZ, 10);
				client.getFunction().stillGfx(346, objectX, objectY, 0, 0);
			}
			break;
		case 884:
		case 879:
		case 26945:
		case 21355:
			if (itemId == 229) {
				client.turnPlayerTo(objectX, objectY);
				client.startAnimation(894);
				client.getItems().deleteItem(229, 1);
				client.getItems().addItem(227, 1);
				client.sendMessage("You fill the vial up with water.");
			}
			break;
		case 172:
			if (itemId == CrystalChest.KEY) {
				CrystalChest.searchChest(client, objectID, objectX, objectY);
			}
			break;
		case 14175:
			client.sendMessage("You should try entering the chute instead.");
			break;
		case 2783:
			client.getSmithingInt().showSmithInterface(itemId);
			break;
		/*
		 * Old Farming case 8151: case 8389:
		 * entity.getFarming().checkItemOnObject(itemId); break;
		 */

		case 8151:
		case 8389:
		case 8132:
		case 8174:
		case 8149:
		case 8139:
		case 8140:
		case 8141:
		case 8142:
		case 7848: // /flower patch catherby
			client.getFarming().checkItemOnObject(itemId, objectX, objectY);
			break;
		case 12269:
		case 2732:
		case 114:
		case 2728:
		case 21302:
			Cooking.startCooking(client, itemId, objectID);
			break;
		case 2640:
		case 409:
		case 13184:
			if (BuryBones.isBone(itemId)) {
				BuryBones.bonesOnAltar(client, itemId);
			}
			break;
		case 2479: // Mind
			if (client.getItems().playerHasItem(1448)
					&& client.getItems().playerHasItem(5525)) {
				client.startAnimation(791);
				client.gfx100(186);
				client.getItems().deleteItem(1448, 1);
				client.getItems().deleteItem(5525, 1);
				client.getItems().addItem(5529, 1);
				client.sendMessage("You infuse the Tiara with the Talisman's magical force!");
				client.getFunction().addSkillXP(
						950 * SkillConfig.RUNECRAFTING_EXPERIENCE,
						client.playerRunecrafting);
			}
			break;
		case 7141: // Blood (on Rift)
			if (client.getItems().playerHasItem(1450)
					&& client.getItems().playerHasItem(5525)) {
				client.startAnimation(791);
				client.gfx100(186);
				client.getItems().deleteItem(1450, 1);
				client.getItems().deleteItem(5525, 1);
				client.getItems().addItem(5549, 1);
				client.sendMessage("You infuse the Tiara with the Talisman's magical force!");
				client.getFunction().addSkillXP(
						950 * SkillConfig.RUNECRAFTING_EXPERIENCE,
						client.playerRunecrafting);
			}
			break;
		case 2480: // Water
			if (client.getItems().playerHasItem(1444)
					&& client.getItems().playerHasItem(5525)) {
				client.startAnimation(791);
				client.gfx100(186);
				client.getItems().deleteItem(1444, 1);
				client.getItems().deleteItem(5525, 1);
				client.getItems().addItem(5531, 1);
				client.sendMessage("You infuse the Tiara with the Talisman's magical force!");
				client.getFunction().addSkillXP(
						950 * SkillConfig.RUNECRAFTING_EXPERIENCE,
						client.playerRunecrafting);
			}
			break;
		case 2481: // Earth
			if (client.getItems().playerHasItem(1440)
					&& client.getItems().playerHasItem(5525)) {
				client.startAnimation(791);
				client.gfx100(186);
				client.getItems().deleteItem(1440, 1);
				client.getItems().deleteItem(5525, 1);
				client.getItems().addItem(5535, 1);
				client.sendMessage("You infuse the Tiara with the Talisman's magical force!");
				client.getFunction().addSkillXP(
						950 * SkillConfig.RUNECRAFTING_EXPERIENCE,
						client.playerRunecrafting);
			}
			break;
		case 2482: // Fire
			if (client.getItems().playerHasItem(1442)
					&& client.getItems().playerHasItem(5525)) {
				client.startAnimation(791);
				client.gfx100(186);
				client.getItems().deleteItem(1442, 1);
				client.getItems().deleteItem(5525, 1);
				client.getItems().addItem(5537, 1);
				client.sendMessage("You infuse the Tiara with the Talisman's magical force!");
				client.getFunction().addSkillXP(
						950 * SkillConfig.RUNECRAFTING_EXPERIENCE,
						client.playerRunecrafting);
			}
			break;
		case 2483: // Body
			if (client.getItems().playerHasItem(1446)
					&& client.getItems().playerHasItem(5525)) {
				client.startAnimation(791);
				client.gfx100(186);
				client.getItems().deleteItem(1446, 1);
				client.getItems().deleteItem(5525, 1);
				client.getItems().addItem(5533, 1);
				client.sendMessage("You infuse the Tiara with the Talisman's magical force!");
				client.getFunction().addSkillXP(
						950 * SkillConfig.RUNECRAFTING_EXPERIENCE,
						client.playerRunecrafting);
			}
			break;
		case 2484: // Cosmic
			if (client.getItems().playerHasItem(1454)
					&& client.getItems().playerHasItem(5525)) {
				client.startAnimation(791);
				client.gfx100(186);
				client.getItems().deleteItem(1454, 1);
				client.getItems().deleteItem(5525, 1);
				client.getItems().addItem(5539, 1);
				client.sendMessage("You infuse the Tiara with the Talisman's magical force!");
				client.getFunction().addSkillXP(
						950 * SkillConfig.RUNECRAFTING_EXPERIENCE,
						client.playerRunecrafting);
			}
			break;
		case 2486: // Nature
			if (client.getItems().playerHasItem(1462)
					&& client.getItems().playerHasItem(5525)) {
				client.startAnimation(791);
				client.gfx100(186);
				client.getItems().deleteItem(1462, 1);
				client.getItems().deleteItem(5525, 1);
				client.getItems().addItem(5541, 1);
				client.sendMessage("You infuse the Tiara with the Talisman's magical force!");
				client.getFunction().addSkillXP(
						950 * SkillConfig.RUNECRAFTING_EXPERIENCE,
						client.playerRunecrafting);
			}
			break;
		case 2487: // Chaos
			if (client.getItems().playerHasItem(1452)
					&& client.getItems().playerHasItem(5525)) {
				client.startAnimation(791);
				client.gfx100(186);
				client.getItems().deleteItem(1452, 1);
				client.getItems().deleteItem(5525, 1);
				client.getItems().addItem(5543, 1);
				client.sendMessage("You infuse the Tiara with the Talisman's magical force!");
				client.getFunction().addSkillXP(
						950 * SkillConfig.RUNECRAFTING_EXPERIENCE,
						client.playerRunecrafting);
			}
			break;
		case 2488: // Death
			if (client.getItems().playerHasItem(1456)
					&& client.getItems().playerHasItem(5525)) {
				client.startAnimation(791);
				client.gfx100(186);
				client.getItems().deleteItem(1456, 1);
				client.getItems().deleteItem(5525, 1);
				client.getItems().addItem(5547, 1);
				client.sendMessage("You infuse the Tiara with the Talisman's magical force!");
				client.getFunction().addSkillXP(
						950 * SkillConfig.RUNECRAFTING_EXPERIENCE,
						client.playerRunecrafting);
			}
			break;
		case 2485: // Law
			if (client.getItems().playerHasItem(1458)
					&& client.getItems().playerHasItem(5525)) {
				client.startAnimation(791);
				client.gfx100(186);
				client.getItems().deleteItem(1458, 1);
				client.getItems().deleteItem(5525, 1);
				client.getItems().addItem(5545, 1);
				client.sendMessage("You infuse the Tiara with the Talisman's magical force!");
				client.getFunction().addSkillXP(
						950 * SkillConfig.RUNECRAFTING_EXPERIENCE,
						client.playerRunecrafting);
			}
			break;
		case 2478: // Air
			if (client.getItems().playerHasItem(1438)
					&& client.getItems().playerHasItem(5525)) {
				client.startAnimation(791);
				client.gfx100(186);
				client.getItems().deleteItem(1438, 1);
				client.getItems().deleteItem(5525, 1);
				client.getItems().addItem(5527, 1);
				client.sendMessage("You infuse the Tiara with the Talisman's magical force!");
				client.getFunction().addSkillXP(
						950 * SkillConfig.RUNECRAFTING_EXPERIENCE,
						client.playerRunecrafting);
			}
			break;
		default:
			client.turnPlayerTo(objectX, objectY);
			client.sendMessage("Nothing interesting happens.");
			if (client.playerRights == 3) {
				Misc.println("Player At Object id: " + objectID
						+ " with Item id: " + itemId);
			}
			break;
		}

	}
}
