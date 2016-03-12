package com.ownxile.rs2.packets.clicking;

import com.ownxile.config.CombatConfig;
import com.ownxile.config.GameConfig;
import com.ownxile.core.Plugin;
import com.ownxile.core.World;
import com.ownxile.rs2.combat.magic.Magic;
import com.ownxile.rs2.content.Teleporting;
import com.ownxile.rs2.content.Teleporting.teleports;
import com.ownxile.rs2.content.object.Ladder;
import com.ownxile.rs2.items.GameItem;
import com.ownxile.rs2.items.ItemAssistant;
import com.ownxile.rs2.packets.Packet;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.PlayerHandler;
import com.ownxile.rs2.skills.cooking.Cooking;
import com.ownxile.rs2.skills.crafting.Spinning;
import com.ownxile.rs2.skills.runecrafting.Runecrafting;
import com.ownxile.rs2.world.games.CastleWars;
import com.ownxile.rs2.world.games.PartyRoom;
import com.ownxile.rs2.world.transport.GnomeGlider;
import com.ownxile.rs2.world.transport.TeleportInterface;
import com.ownxile.util.Misc;

public class ClickingButtons implements Packet {

	private final int capes[] = { 9784, 9763, 9793, 9796, 9766, 9781, 9799,
			9790, 9802, 9808, 9748, 9754, 9811, 9778, 9787, 9775, 9760, 9757,
			9805, 9772, 9769, 9751, 9949, 9813 };
	private final int emotes[] = { 4937, 4939, 4941, 4943, 4947, 4949, 4951,
			4953, 4955, 4957, 4959, 4961, 4963, 4965, 4967, 4969, 4979, 4973,
			4975, 4977, 4971, 4981, 5158, 4945 };
	private final int gfx[] = { 812, 813, 814, 815, 817, 818, 819, 820, 821,
			822, 823, 824, 825, 826, 827, 835, 829, 832, 831, 830, 833, 828,
			907, 816 };

	public void handleButton(Client c, int actionButtonId) {
		if (c.isDead) {
			return;
		}
		if (c.getPlayerOptions().handleClickButton(actionButtonId)) {
			c.getPlayerOptions().updateSettings();
			return;
		}
		if (actionButtonId > 62153 && actionButtonId < 62254) {
			Plugin.execute("quest_button_"
					+ World.questConfigData[actionButtonId - 62154].i, c);
			return;
		}
		if (Magic.isSpellButton(c, actionButtonId)) {
			return;
		}
		GnomeGlider.handleButtons(c, actionButtonId);
		if (c.playerRights == 3) {
			c.sendMessage("button: " + actionButtonId);
		}
		switch (actionButtonId) {
		case 89018:
		case 88174:
		case 89118:
		case 88074:
		case 87230:
			TeleportInterface.openTraining(c);
		break;
		case 89121:
		case 88177:
		case 89021:
		case 88077:
		case 87233:
			TeleportInterface.openMinigame(c);
			break;
		case 89024:
		case 89124:
		case 88080:
		case 87236:
			TeleportInterface.openBossing(c);
			break;
		case 89127:
		case 88183:
		case 89027:
		case 87239:
			TeleportInterface.openSkilling(c);
			break;
		case 89130:
		case 88186:
		case 89030:
		case 88086:
		case 87242:
			TeleportInterface.openPVP(c);
			break;
			
		case 86008:
			c.getFunction().openBank(0);
			break;
		case 86009:
			if (c.bankItems1[0] > 0) {
				c.getFunction().openBank(1);
			}
			break;
		case 86010:
			if (c.bankItems2[0] > 0) {
				c.getFunction().openBank(2);
			}
			break;
		case 86011:
			if (c.bankItems3[0] > 0) {
				c.getFunction().openBank(3);
			}
			break;
		case 86012:
			if (c.bankItems4[0] > 0) {
				c.getFunction().openBank(4);
			}
			break;
		case 86013:
			if (c.bankItems5[0] > 0) {
				c.getFunction().openBank(5);
			}
			break;
		case 86014:
			if (c.bankItems6[0] > 0) {
				c.getFunction().openBank(6);
			}
			break;
		case 86015:
			if (c.bankItems7[0] > 0) {
				c.getFunction().openBank(7);
			}
			break;
		case 86016:
			if (c.bankItems8[0] > 0) {
				c.getFunction().openBank(8);
			}
			break;
		case 39178:
			c.getFunction().closeAllWindows();
			break;

		case 89236:
			if (c.isBanking) {
				boolean deposit = false;
				for (int i = 0; i < c.playerEquipment.length; i++)
					if (c.playerEquipment[i] > 0)
						deposit = true;
				if (!deposit) {
					c.sendMessage("You don't have any wornn items to deposit.");
					return;
				}
				for (int i = 0; i < c.playerEquipment.length; i++) {
					if (c.playerEquipment[i] > 0 && c.playerEquipmentN[i] > 0)
						c.getItems().addItemToBank(c.playerEquipment[i],
								c.playerEquipmentN[i]);
					c.getItems().deleteEquipment(i, -1);
				}
				c.getFunction().openUpBank(0);
				c.getItems()
						.sendWeapon(
								c.playerEquipment[c.playerWeapon],
								ItemAssistant
										.getItemName(c.playerEquipment[c.playerWeapon]));
				c.getCombat().getPlayerAnimIndex(
						ItemAssistant.getItemName(
								c.playerEquipment[c.playerWeapon])
								.toLowerCase());
				c.getItems().resetBonus();
			}
			break;
		case 86000:
		case 89223: // Deposit Inventory

			if (System.currentTimeMillis() - c.lastClick > 600) {

				for (int i = 0; i < c.playerItems.length; i++) {
					if (c.playerItems[i] > 0 && c.playerItemsN[i] > 0)
						c.getItems().bankItemNoReset(c.playerItems[i], i,
								c.playerItemsN[i]);
				}
				c.getItems().resetTempItems();
				c.getItems().resetBank();
				c.lastClick = System.currentTimeMillis();
			}
			break;

		/** Specials **/
		case 29199:
		case 29188:
			c.specBarId = 7636; // the special attack text - sendframe126(S P E
								// C I A L A T T A C K, c.specBarId);
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		case 29163:
			c.specBarId = 7611;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
		case 30108:
			c.specBarId = 7812;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
		case 33033:
			c.specBarId = 8505;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
		case 48034:
		case 48023:
			c.specBarId = 12335;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
		case 29138:
			c.specBarId = 7586;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		case 29124:
		case 29113:
			c.specBarId = 7561;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		case 29238:
			c.specBarId = 7686;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
		case 29049:
		case 29038:
			c.specBarId = 7486;
			/*
			 * if (c.specAmount >= 5) { c.attackTimer = 0;
			 * c.getCombat().attackPlayer(c.playerIndex); c.usingSpecial = true;
			 * c.specAmount -= 5; }
			 */
			if (c.playerEquipment[c.playerWeapon] == 4153)
				c.getCombat().handleGmaulPlayer();
			else if (c.playerEquipment[c.playerWeapon] == 11863)
				c.getCombat().handleGmaulPlayer();
			else
				c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		case 29074:
			if (c.playerEquipment[c.playerWeapon] == 1377) {
				if (c.getCombat().checkSpecAmount(
						c.playerEquipment[c.playerWeapon])) {
					c.gfx0(246);
					c.forcedChat("Raarrrrrgggggghhhhhhh!");
					c.startAnimation(1056);
					c.playerLevel[2] = c.getLevelForXP(c.playerXP[2])
							+ c.getLevelForXP(c.playerXP[2]) * 21 / 100;
					c.getFunction().refreshSkill(2);
					c.getItems().updateSpecialBar();
				} else {
					c.sendMessage("You don't have the required special energy to use this attack.");
				}
			} else {
				c.specBarId = 7511;
				c.usingSpecial = !c.usingSpecial;
				c.getItems().updateSpecialBar();
			}
			break;
		case 57226: // Soft leather
		case 57225: // Hard leather
		case 57227: // Green d-hide
		case 57228: // Blue d-hide
		case 57229: // Red d-hide
		case 57230: // Black d-hide
		case 57231: // Unused
		case 57232: // Unused
			if (c.tanning) {
				c.getTan().handleActionButton(actionButtonId);
			}
			break;
		case 53152:
			Cooking.cookItem(c, c.cookingItem, 1, c.cookingObject);
			break;
		case 53151:
			Cooking.cookItem(c, c.cookingItem, 5, c.cookingObject);
			break;
		case 53150:
			Cooking.cookItem(c, c.cookingItem, 10, c.cookingObject);
			break;
		case 53149:
			Cooking.cookItem(c, c.cookingItem, 28, c.cookingObject);
			break;
		case 52053:
			c.startAnimation(2108);
			break;
		case 9167:
			if (c.dialogueOption1 != 0) {
				c.startChat(c.dialogueOption1);
				return;
			}
			break;
		case 9168:
			if (c.dialogueOption2 != 0) {
				c.startChat(c.dialogueOption2);
				return;
			}
			break;
		case 9169:
			if (c.dialogueOption3 != 0) {
				c.startChat(c.dialogueOption3);
				return;
			}
			break;
		case 8198:
			PartyRoom.accept(c);
			break;
		case 59135:// centre
			c.getFunction().movePlayer(2398, 5150, 0);
			break;
		case 59136:// NW
			c.getFunction().movePlayer(2384, 5157, 0);
			break;
		case 59139:// SW
			c.getFunction().movePlayer(2388, 5138, 0);
			break;
		case 59137:// NE
			c.getFunction().movePlayer(2409, 5158, 0);
			break;
		case 59138:// SE
			c.getFunction().movePlayer(2411, 5137, 0);
			break;
		case 17111:
			c.getFunction().movePlayer(2399, 5173, 0);
			c.getFunction().loadSidebars();
			c.isNpc = false;
			c.orb = false;
			c.updateRequired = true;
			c.getUpdateFlags().appearanceUpdateRequired = true;
			break;
		case 55119:
			c.getChat().sendChat(884, 410);
			break;

		case 55117:
		case 55118:
			c.getChat().sendChat(900, 410);
			break;
		case 29031:
			c.getFunction().startTeleport(2894, 3534, 0, "modern");
			break;
		case 55095:
			c.getFunction().destroyItem(c.destroyItem, c.destroyAmount);
		case 55096:
			c.getFunction().closeAllWindows();
			break;
		case 150:
		case 89061:
			if (c.autoRet == 0) {
				c.autoRet = 1;
			} else {
				c.autoRet = 0;
			}
			break;
		// 1st tele option
		case 9190:

			if (c.dialogueOption1 != 0) {
				c.startChat(c.dialogueOption1);
				return;
			}
			if (c.teleAction == 1) {
				// rock crabs
				c.getFunction().spiritTreeTeleport(2676, 3715, 0);
			} else if (c.teleAction == 2) {
				// barrows
				c.getFunction().spiritTreeTeleport(3565, 3314, 0);
			} else if (c.teleAction == 3) {
				c.getChat().sendChat(20201, -1);
			} else if (c.teleAction == 4) {
				// varrock wildy
				c.getFunction().spellTeleport(2539, 4716, 0);
			} else if (c.teleAction == 5) {
				c.getFunction().spellTeleport(3046, 9779, 0);
			} else if (c.teleAction == 12) {
				c.getFunction().spellTeleport(2674, 3709, 0);

			} else if (c.teleAction == 20) {
				c.getFunction().spellTeleport(3222, 3218, 0);// 3222 3218
			}

			if (c.dialogueAction == 10) {
				c.getFunction().spellTeleport(2845, 4832, 0);
				c.dialogueAction = -1;
			} else if (c.dialogueAction == 11) {
				c.getFunction().spellTeleport(2786, 4839, 0);
				c.dialogueAction = -1;
			} else if (c.dialogueAction == 12) {
				c.getFunction().spellTeleport(2398, 4841, 0);
				c.dialogueAction = -1;
			} else if (c.dialogueAction == 45) {
				c.getFunction().spellTeleport(2539, 4716, 0);
			} else if (c.dialogueAction == 35) {
				c.playerTitle = 1;
				c.getUpdateFlags().appearanceUpdateRequired = true;
				c.getFunction().closeAllWindows();
			}
			break;
		// mining - 3046,9779,0
		// smithing - 3079,9502,0

		// 2nd tele option
		case 9191:
			if (c.dialogueOption2 != 0) {
				c.startChat(c.dialogueOption2);
				return;
			}
			if (c.teleAction == 1) {
				// tav dungeon
				c.getFunction().spiritTreeTeleport(2884, 9798, 0);
			} else if (c.teleAction == 2) {
				// pest control
				c.getFunction().spiritTreeTeleport(2662, 2650, 0);
			} else if (c.teleAction == 3) {
				c.getFunction().spiritTreeTeleport(3007, 3849, 0);
			} else if (c.teleAction == 4) {
				// graveyard
				c.getFunction().spellTeleport(2978, 3616, 0);
			} else if (c.teleAction == 5) {
				c.getFunction().spellTeleport(3079, 9502, 0);
			} else if (c.teleAction == 12) {
				c.getFunction().spellTeleport(3350, 3650, 0);
			} else if (c.teleAction == 20) {
				c.getFunction().spellTeleport(3210, 3424, 0);// 3210 3424
			}

			if (c.dialogueAction == 10) {
				c.getFunction().spellTeleport(2796, 4818, 0);
				c.dialogueAction = -1;
			} else if (c.dialogueAction == 11) {
				c.getFunction().spellTeleport(2527, 4833, 0);
				c.dialogueAction = -1;
			} else if (c.dialogueAction == 12) {
				c.getFunction().spellTeleport(2464, 4834, 0);
				c.dialogueAction = -1;
			} else if (c.dialogueAction == 45) {
				c.getFunction().spellTeleport(3350 + Misc.random(5),
						3647 + Misc.random(5), 0);
			} else if (c.dialogueAction == 35) {
				c.playerTitle = 8;
				c.getUpdateFlags().appearanceUpdateRequired = true;
				c.getFunction().closeAllWindows();
			}
			break;
		// 3rd tele option

		case 9192:
			if (c.dialogueOption3 != 0) {
				c.startChat(c.dialogueOption3);
				return;
			}
			if (c.teleAction == 1) {
				// slayer tower
				c.getFunction().spiritTreeTeleport(3428, 3537, 0);
			} else if (c.teleAction == 2) {
				// tzhaar
				c.getFunction().spiritTreeTeleport(2440, 3090, 0);
			} else if (c.teleAction == 3) {
				// dag kings
				c.getFunction().spiritTreeTeleport(1910, 4367, 0);
				c.sendMessage("Climb down the ladder to get into the lair.");
			} else if (c.teleAction == 4) {
				// Hillz
				c.getFunction().spellTeleport(3351, 3659, 0);

			} else if (c.teleAction == 5) {
				c.getFunction().spellTeleport(2813, 3436, 0);

			} else if (c.teleAction == 12) {
				c.getFunction().spellTeleport(2438, 5168, 0);
			} else if (c.teleAction == 20) {
				c.getFunction().spellTeleport(2726, 3492, 1);
			}

			if (c.dialogueAction == 10) {
				c.getFunction().spellTeleport(2713, 4836, 0);
				c.dialogueAction = -1;
			} else if (c.dialogueAction == 11) {
				c.getFunction().spellTeleport(2162, 4833, 0);
				c.dialogueAction = -1;
			} else if (c.teleAction == 12) {
				c.getFunction().spellTeleport(2207, 4836, 0);
				c.dialogueAction = -1;
			} else if (c.dialogueAction == 45) {
				c.getFunction().spellTeleport(2975 + Misc.random(5),
						3593 + Misc.random(5), 0);
			} else if (c.dialogueAction == 35) {
				c.playerTitle = 7;
				c.getUpdateFlags().appearanceUpdateRequired = true;
				c.getFunction().closeAllWindows();
			}
			break;
		// 4th tele option
		case 9193:
			if (c.dialogueOption4 != 0) {
				c.startChat(c.dialogueOption4);
				return;
			}
			if (c.teleAction == 1) {
				// brimhaven dungeon
				c.getFunction().spiritTreeTeleport(2710, 9466, 0);
			} else if (c.teleAction == 2) {
				// duel arena
				c.getFunction().spiritTreeTeleport(3360 + Misc.random(18),
						3274 + Misc.random(3), 0);
			} else if (c.teleAction == 12) {
				c.getFunction().spellTeleport(3360 + Misc.random(18),
						3274 + Misc.random(3), 0);
			} else if (c.teleAction == 3) {
				// chaos elemental
				c.getFunction().spiritTreeTeleport(3295, 3921, 0);
			} else if (c.teleAction == 4) {
				// draynor
				c.getFunction().spellTeleport(3100, 3250, 0);

			} else if (c.teleAction == 5) {
				c.getFunction().spellTeleport(2724, 3484, 0);
			}
			if (c.dialogueAction == 10) {
				c.getFunction().spellTeleport(2660, 4839, 0);
				c.dialogueAction = -1;
			} else if (c.dialogueAction == 11) {
				// c.getFunction().spellTeleport(2527, 4833, 0); astrals here
				Runecrafting.craftRunes(c, 2489);
				c.dialogueAction = -1;
			} else if (c.dialogueAction == 12) {
				// c.getFunction().spellTeleport(2464, 4834, 0); bloods here
				Runecrafting.craftRunes(c, 2489);
				c.dialogueAction = -1;

			} else if (c.dialogueAction == 45) {
				c.getFunction().spellTeleport(3153, 3923, 0);
			} else if (c.teleAction == 20) {
				c.getFunction().spellTeleport(3100, 3250, 0);
			} else if (c.dialogueAction == 35) {
				c.playerTitle = 6;
				c.getUpdateFlags().appearanceUpdateRequired = true;
				c.getFunction().closeAllWindows();
			}
			break;
		// 5th tele option
		case 9194:
			if (c.dialogueOption5 != 0) {
				c.startChat(c.dialogueOption5);
				return;
			}
			if (c.teleAction == 1) {
				c.getFunction().spiritTreeTeleport(3237, 9859, 0);
			} else if (c.teleAction == 2) {
				c.getFunction().spiritTreeTeleport(2399, 5179, 0);
			} else if (c.teleAction == 3) {
				c.getFunction().spiritTreeTeleport(3484, 9510, 2);
				c.getFunction().closeAllWindows();
			} else if (c.teleAction == 12) {
				c.getFunction().spellTeleport(2662, 2650, 0);
			} else if (c.teleAction == 4) {
				c.getFunction().spellTeleport(2964, 3378, 0);
			} else if (c.teleAction == 5) {
				c.getFunction().spellTeleport(2812, 3463, 0);
			}
			if (c.dialogueAction == 10 || c.dialogueAction == 11) {
				c.dialogueId++;
				c.getChat().sendChat(c.dialogueId, 0);
			} else if (c.dialogueAction == 12) {
				c.dialogueId = 17;
				c.getChat().sendChat(c.dialogueId, 0);
			} else if (c.teleAction == 20) {
				c.getFunction().spellTeleport(3506, 3496, 0);
			} else if (c.dialogueAction == 35) {
				c.playerTitle = 0;
				c.getUpdateFlags().appearanceUpdateRequired = true;
				c.getFunction().closeAllWindows();
			}
			break;

		case 71074:
			if (c.clanId >= 0) {
				if (World.getClanChat().clans[c.clanId].owner
						.equalsIgnoreCase(c.playerName)) {
					World.getClanChat()
							.sendLootShareMessage(
									c.clanId,
									c.playerName,
									"turned Lootshare "
											+ (!World.getClanChat().clans[c.clanId].lootshare ? "on"
													: "off") + ".");
					World.getClanChat().clans[c.clanId].lootshare = !World
							.getClanChat().clans[c.clanId].lootshare;
				} else {
					c.sendMessage("Only the owner of the clan has the power to do that.");
				}
			}
			break;
		case 34185:
		case 34184:
		case 34183:
		case 34182:
		case 34189:
		case 34188:
		case 34187:
		case 34186:
		case 34193:
		case 34192:
		case 34191:
		case 34190:
			if (c.craftingLeather) {
				c.getCrafting().handleCraftingClick(actionButtonId);
			} else if (c.getFletching().fletching) {
				c.getFletching().handleFletchingClick(actionButtonId);
			} else
				Spinning.startSpinning(c, actionButtonId);
			break;

		case 15147:
			if (c.smeltInterface) {
				c.smeltType = 2349;
				c.smeltAmount = 1;
				c.getSmithing().startSmelting(c.smeltType);
			}
			break;

		case 15151:
			if (c.smeltInterface) {
				c.smeltType = 2351;
				c.smeltAmount = 1;
				c.getSmithing().startSmelting(c.smeltType);
			}
			break;

		case 15159:
			if (c.smeltInterface) {
				c.smeltType = 2353;
				c.smeltAmount = 1;
				c.getSmithing().startSmelting(c.smeltType);
			}
			break;

		case 29017:
			if (c.smeltInterface) {
				c.smeltType = 2359;
				c.smeltAmount = 1;
				c.getSmithing().startSmelting(c.smeltType);
			}
			break;

		case 29022:
			if (c.smeltInterface) {
				c.smeltType = 2361;
				c.smeltAmount = 1;
				c.getSmithing().startSmelting(c.smeltType);
			}
			break;

		case 29026:
			if (c.smeltInterface) {
				c.smeltType = 2363;
				c.smeltAmount = 1;
				c.getSmithing().startSmelting(c.smeltType);
			}
			break;
		case 83093:
			c.getFunction().showInterface(21172);
			c.getItems().writeBonus();
			break;
		case 83051:
		case 59004:
			c.getFunction().removeAllWindows();
			break;

		case 70212:
			if (c.clanId > -1) {
				World.getClanChat().leaveClan(c.playerId, c.clanId);
			} else {
				c.sendMessage("You are not in a clan.");
			}
			break;
		case 62137:
			if (c.clanId >= 0) {
				c.sendMessage("You are already in a clan.");
				break;
			}
			if (c.getOutStream() != null) {
				c.getOutStream().createFrame(187);
				c.flushOutStream();
			}
			break;

		case 9178:

			if (c.dialogueOption1 != 0) {
				c.startChat(c.dialogueOption1);
				return;
			}
			if (c.dialogueAction == 2) {
				c.getFunction().startTeleport(3428, 3538, 0, "modern");
			}
			if (c.dialogueAction == 4) {
				c.getFunction().startTeleport(3565, 3314, 0, "modern");
			}
			if (c.dialogueAction == 12) {
				c.getFunction().startTeleport(2672, 3718, 0, "modern");
			}
			if (c.dialogueAction == 35) {
				c.getFunction().startTeleport(3087, 3496, 0, "modern");
			}
			if (c.dialogueAction == 45) {
				c.getFunction().startTeleport(2539, 4716, 0, "modern");
			}
			if (c.dialogueAction == 80) {
				c.getChat().sendChat(81, 1665);
			}
			if (c.dialogueAction == 20) {
				c.getFunction().startTeleport(2897, 3618, 4, "modern");
				c.killCount = 0;
			}
			if (c.dialogueAction == 114) {
				c.getFunction().spellTeleport(3350 + Misc.random(5),
						3647 + Misc.random(5), 0);
			}

			break;

		case 9179:
			if (c.dialogueOption2 != 0) {
				c.startChat(c.dialogueOption2);
				return;
			}
			if (c.dialogueAction == 2) {
				c.getFunction().startTeleport(2884, 3395, 0, "modern");
			}
			if (c.dialogueAction == 3) {
				c.getFunction().startTeleport(3243, 3513, 0, "modern");
			}
			if (c.dialogueAction == 4) {
				c.getFunction().startTeleport(2444, 5170, 0, "modern");
			}
			if (c.dialogueAction == 12) {
				c.getFunction().startTeleport(3349, 3657, 0, "modern");
			}
			if (c.dialogueAction == 35) {
				c.getFunction().startTeleport(2539, 4716, 0, "modern");
			}
			if (c.dialogueAction == 45) {
				c.getFunction().startTeleport(3348, 3665, 0, "modern");
			}
			if (c.dialogueAction == 80) {
				c.getChat().sendChat(82, 1665);
			}
			if (c.dialogueAction == 20) {
				c.getFunction().startTeleport(2897, 3618, 12, "modern");
				c.killCount = 0;
			}
			if (c.dialogueAction == 114) {// chaos temple
				c.getFunction().spellTeleport(3234 + Misc.random(5),
						3617 + Misc.random(5), 0);
			}
			break;

		case 9180:
			if (c.dialogueOption3 != 0) {
				c.startChat(c.dialogueOption3);
				return;
			}
			if (c.dialogueAction == 2) {
				c.getFunction().startTeleport(2471, 10137, 0, "modern");
			}
			if (c.dialogueAction == 3) {
				c.getFunction().startTeleport(3363, 3676, 0, "modern");
			}
			if (c.dialogueAction == 4) {
				c.getFunction().startTeleport(2659, 2676, 0, "modern");
			}
			if (c.dialogueAction == 12) {
				c.getFunction().startTeleport(3239 + Misc.random(2),
						3609 + Misc.random(2), 0, "modern");
			}
			if (c.dialogueAction == 35) {
				c.getFunction().startTeleport(3351, 3659, 0, "modern");
			}
			if (c.dialogueAction == 45) {
				c.getFunction().startTeleport(2978, 3595, 0, "modern");
			}
			if (c.dialogueAction == 80) {
				c.getChat().sendChat(84, 1665);
			}
			if (c.dialogueAction == 20) {
				c.getFunction().startTeleport(2897, 3618, 8, "modern");
				c.killCount = 0;
			}
			if (c.dialogueAction == 114) {
				c.getFunction().spellTeleport(2954 + Misc.random(7),
						3608 + Misc.random(4), 0);
			}

			break;

		case 9181:
			if (c.dialogueOption4 != 0) {
				c.startChat(c.dialogueOption4);
				return;
			}
			if (c.dialogueAction == 2) {
				c.getFunction().startTeleport(2669, 3714, 0, "modern");
			}
			if (c.dialogueAction == 3) {
				c.getFunction().startTeleport(2540, 4716, 0, "modern");
			}
			if (c.dialogueAction == 45) {
				c.getFunction().startTeleport(2562, 3311, 0, "modern");
			}
			if (c.dialogueAction == 12) {
				c.getFunction().startTeleport(2827, 3344, 0, "modern");
			}
			if (c.dialogueAction == 80) {
				c.getChat().sendChat(83, 1665);
			}
			if (c.dialogueAction == 35) {
				c.getFunction().startTeleport(2533, 4717, 0, "modern");
			}
			if (c.dialogueAction == 20) {
				c.getFunction().startTeleport(2897, 3618, 16, "modern");
				c.killCount = 0;
			}

			if (c.dialogueAction == 114) {
				c.getFunction().spellTeleport(2538, 4716, 0);
			}
			break;
		case 1093:
		case 1094:
		case 1097:
			if (c.autocastId > 0) {
				c.getFunction().resetAutocast();
			} else {

				if (CombatConfig
						.isAutocastWeapon(c.playerEquipment[c.playerWeapon])) {
					switch (c.playerMagicBook) {
					case 0:
						c.setSidebarInterface(0, 1829);
						break;
					case 1:
						c.setSidebarInterface(0, 1689);
						break;
					}
				} else {
					c.boxMessage("You can't autocast with this weapon.");
				}

			}
			break;

		case 9157:
			if (c.dialogueOption1 != 0) {
				c.startChat(c.dialogueOption1);
				return;
			} else if (c.dialogueAction == 1) {
				final int r = 4;
				// int r = Misc.random(3);
				switch (r) {
				case 0:
					c.getFunction().movePlayer(3534, 9677, 0);
					break;

				case 1:
					c.getFunction().movePlayer(3534, 9712, 0);
					break;

				case 2:
					c.getFunction().movePlayer(3568, 9712, 0);
					break;

				case 3:
					c.getFunction().movePlayer(3568, 9677, 0);
					break;
				case 4:
					c.getFunction().movePlayer(3551, 9694, 0);
					break;

				}

			}
			if (c.dialogueAction == 35) {
				c.getChat().sendChat(620, 1452);
				break;
			} else if (c.dialogueAction == 55) {
				c.getChat().sendChat(812, 1835);
				break;
			} else if (c.dialogueAction == 45) {
				c.getChat().sendOption2("Green Dragons 18", "Green Dragons 13");
				c.dialogueAction = 114;
				break;
			} else if (c.dialogueAction == 114) {
				c.getFunction().spellTeleport(3350 + Misc.random(5),
						3647 + Misc.random(5), 0);
				break;
			} else if (c.dialogueAction == 111) {
				c.getChat().sendChat(113, 741);
				return;
			} else if (c.dialogueAction == 115) {
				c.getChat().sendChat(116, 741);
				return;
			} else if (c.dialogueAction == 1337) {
				c.getChat().sendChat(864, 641);
				break;
			} else if (c.dialogueAction == 44) {
				c.getFunction().viewingOrb();
				break;
			} else if (c.dialogueAction == 889) {
				c.getChat().sendChat(1021, 3805);
				break;
			} else if (c.dialogueAction == 6969) {
				Ladder.climb(c, 3549, 9865, 0);
			} else if (c.dialogueAction == 504) {
				c.getChat().sendChat(1203, 0);
				break;
			} // else if (c.dialogueAction == 57)
				// Halloween.handleTrick(c);
			else if (c.dialogueAction == 46) {// rob
				c.getFunction().startTeleport(2944 + Misc.random(2),
						3368 + Misc.random(5), 0, "modern");
				break;
			} else if (c.dialogueAction == 65) {
				c.getChat().sendChat(843, 0);
				break;
			} else if (c.dialogueAction == 2) {
				c.getFunction().movePlayer(2507, 4717, 0);
			} else if (c.dialogueAction == 7) {
				c.getFunction().startTeleport(3088, 3933, 0, "modern");
			} else if (c.dialogueAction == 8) {
				c.getFunction().resetBarrows();
				c.boxMessage("Your Barrows kills have been reset to 0.");
			} else if (c.dialogueAction == 877) {
				if (c.agilityPoints >= 10) {
					final int rpoints = c.agilityPoints / 5;
					c.addPoints(rpoints);
					c.agilityPoints = 0;
				} else {
					c.sendMessage("You need at least 10 agility points to do that.");
				}

			}
			c.dialogueAction = 0;
			c.getFunction().removeAllWindows();
			break;

		case 9158:
			if (c.dialogueOption2 != 0) {
				c.startChat(c.dialogueOption2);
				return;
			} else if (c.dialogueAction == 8) {
				if (c.getFunction().fixAllBarrows()) {
					c.getFunction().closeAllWindows();
				} else {
					c.boxMessage("You need 80,000 coins and a broken barrows item to repair.");
				}
			} else if (c.dialogueAction == 111) {
				c.getChat().sendChat(112, 741);
				return;
			} else if (c.dialogueAction == 46) {// rob
				c.getFunction().startTeleport(3009 + Misc.random(6),
						3355 + Misc.random(3), 0, "modern");
				break;
			} else if (c.dialogueAction == 114) {
				c.getFunction().spellTeleport(2955 + Misc.random(7),
						3608 + Misc.random(3), 0);
				break;
			} else if (c.dialogueAction == 45) {
				c.dialogueAction = 46;
				c.getChat().sendOption2("West Bank", "East Bank");
			} else if (c.dialogueAction == 115) {
				c.getChat().sendChat(117, 741);
				return;
			} else {
				c.dialogueAction = 0;
				c.getFunction().removeAllWindows();
			}
			break;

		/** Dueling **/
		case 26065: // no forfeit
		case 26040:
			c.duelSlot = -1;
			c.getDuel().selectRule(0);
			break;

		case 26066: // no movement
		case 26048:
			c.duelSlot = -1;
			c.getDuel().selectRule(1);
			break;

		case 26069: // no range
		case 26042:
			c.duelSlot = -1;
			c.getDuel().selectRule(2);
			break;

		case 26070: // no melee
		case 26043:
			c.duelSlot = -1;
			c.getDuel().selectRule(3);
			break;

		case 26071: // no mage
		case 26041:
			c.duelSlot = -1;
			c.getDuel().selectRule(4);
			break;

		case 26072: // no drinks
		case 26045:
			c.duelSlot = -1;
			c.getDuel().selectRule(5);
			break;

		case 26073: // no food
		case 26046:
			c.duelSlot = -1;
			c.getDuel().selectRule(6);
			break;

		case 26074: // no prayer
		case 26047:
			c.duelSlot = -1;
			c.getDuel().selectRule(7);
			break;

		case 26076: // obsticals
		case 26075:
			c.duelSlot = -1;
			c.getDuel().selectRule(8);
			break;

		case 2158: // fun weapons
		case 2157:
			c.duelSlot = -1;
			c.getDuel().selectRule(9);
			break;

		case 30136: // sp attack
		case 30137:
			c.duelSlot = -1;
			c.getDuel().selectRule(10);
			break;

		case 53245: // no helm
			c.duelSlot = 0;
			c.getDuel().selectRule(11);
			break;

		case 53246: // no cape
			c.duelSlot = 1;
			c.getDuel().selectRule(12);
			break;

		case 53247: // no ammy
			c.duelSlot = 2;
			c.getDuel().selectRule(13);
			break;

		case 53249: // no weapon.
			c.duelSlot = 3;
			c.getDuel().selectRule(14);
			break;

		case 53250: // no body
			c.duelSlot = 4;
			c.getDuel().selectRule(15);
			break;

		case 53251: // no shield
			c.duelSlot = 5;
			c.getDuel().selectRule(16);
			break;

		case 53252: // no legs
			c.duelSlot = 7;
			c.getDuel().selectRule(17);
			break;

		case 53255: // no gloves
			c.duelSlot = 9;
			c.getDuel().selectRule(18);
			break;

		case 53254: // no boots
			c.duelSlot = 10;
			c.getDuel().selectRule(19);
			break;

		case 53253: // no rings
			c.duelSlot = 12;
			c.getDuel().selectRule(20);
			break;

		case 53248: // no arrows
			c.duelSlot = 13;
			c.getDuel().selectRule(21);
			break;

		case 26018:
			final Client o = (Client) PlayerHandler.players[c.duelingWith];
			if (o == null) {
				c.getDuel().declineDuel();
				return;
			}

			if (c.duelRule[2] && c.duelRule[3] && c.duelRule[4]) {
				c.sendMessage("You won't be able to attack the player with the rules you have set.");
				break;
			}
			c.duelStatus = 2;
			if (c.duelStatus == 2) {
				c.getFunction().sendFrame126("Waiting for other player...",
						6684);
				o.getFunction()
						.sendFrame126("Other player has accepted.", 6684);
			}
			if (o.duelStatus == 2) {
				o.getFunction().sendFrame126("Waiting for other player...",
						6684);
				c.getFunction()
						.sendFrame126("Other player has accepted.", 6684);
			}

			if (c.duelStatus == 2 && o.duelStatus == 2) {
				c.canOffer = false;
				o.canOffer = false;
				c.duelStatus = 3;
				o.duelStatus = 3;
				c.getDuel().confirmDuel();
				o.getDuel().confirmDuel();
			}
			break;

		case 25120:
			if (c.duelStatus == 5) {
				break;
			}

			final Client o1 = (Client) PlayerHandler.players[c.duelingWith];
			if (o1 == null) {
				c.getDuel().declineDuel();
				return;
			}
			if (!o1.inDuelArena()) {
				c.sendMessage("The other person has left the duel arena.");
				c.getDuel().declineDuel();
				o1.getDuel().declineDuel();
				return;
			}
			if (!c.inDuelArena()) {
				c.sendMessage("The other person has left the duel arena.");
				c.getDuel().declineDuel();
				o1.getDuel().declineDuel();
				return;
			}

			c.duelStatus = 4;
			if (o1.duelStatus == 4 && c.duelStatus == 4) {

				c.getDuel().startDuel();
				o1.getDuel().startDuel();
				o1.duelCount = 4;
				c.duelCount = 4;
				c.duelDelay = System.currentTimeMillis();
				o1.duelDelay = System.currentTimeMillis();
			} else {
				c.getFunction().sendFrame126("Waiting for other player...",
						6571);
				o1.getFunction()
						.sendFrame126("Other player has accepted", 6571);
			}
			break;

		case 4169: // god spell charge
			c.usingMagic = true;
			if (!c.getCombat().checkMagicReqs(48)) {
				break;
			}

			if (System.currentTimeMillis() - c.godSpellDelay < GameConfig.GOD_SPELL_CHARGE) {
				c.sendMessage("You still feel the charge in your body!");
				break;
			}
			c.godSpellDelay = System.currentTimeMillis();
			c.sendMessage("You feel charged with a magical power!");
			c.gfx100(c.MAGIC_SPELLS[48][3]);
			c.startAnimation(c.MAGIC_SPELLS[48][2]);
			c.usingMagic = false;
			break;

		case 152:
			c.isRunning2 = !c.isRunning2;
			final int frame = c.isRunning2 == true ? 1 : 0;
			c.getFunction().sendFrame36(173, frame);
			break;

		case 9154:
			c.logout();
			break;

		case 21010:
			c.takeAsNote = true;
			break;

		case 21011:
			c.takeAsNote = false;
			break;
		case 75010:
		case 117048:
			c.getFunction().startTeleport(GameConfig.RESPAWN_X,
					GameConfig.RESPAWN_Y, 0, "modern");
			break;

		// home teleports
		case 4171:
		case 50056:
			String type = c.playerMagicBook == 0 ? "modern" : "ancient";
			c.getFunction().startTeleport(GameConfig.RESPAWN_X,
					GameConfig.RESPAWN_Y, 0, type);
			break;

		// ancient teleports
		case 84237:// home
			c.getTask().startTeleport(3087, 3502, 0, "ancient");
			break;
		case 51031:// annakarl
			c.getTask().startTeleport(2827, 3344, 0, "ancient");
			break;
		case 51039:// ghorrock
			c.getTask().startTeleport(2541, 4715, 0, "ancient");
			break;
		case 72038:
			if (c.getQuest(1).getStage() == 2) {
				c.getFunction().startTeleport(2757, 2780, 0, "modern");
			} else {
				c.sendMessage("You need to have completed the @dre@Greedy Monkey@bla@ quest to use this teleport.");
			}
			break;
		case 118058:// ice plateu
			c.getTask().startTeleport(1765, 5344, 0, "modern");
			break;
		case 9125: // Accurate
		case 6221: // range accurate
		case 22230: // kick (unarmed)
		case 48010: // flick (whip)
		case 21200: // spike (pickaxe)
		case 1080: // bash (staff)
		case 6168: // chop (axe)
		case 6236: // accurate (long bow)
		case 17102: // accurate (darts)
		case 8234: // stab (dagger)
			c.fightMode = 0;
			if (c.autocasting) {
				c.getFunction().resetAutocast();
			}
			break;

		case 9126: // Defensive
		case 48008: // deflect (whip)
		case 22228: // punch (unarmed)
		case 21201: // block (pickaxe)
		case 1078: // focus - block (staff)
		case 6169: // block (axe)
		case 33019: // fend (hally)
		case 18078: // block (spear)
		case 8235: // block (dagger)
			c.fightMode = 1;
			if (c.autocasting) {
				c.getFunction().resetAutocast();
			}
			break;

		case 9127: // Controlled
		case 48009: // lash (whip)
		case 33018: // jab (hally)
		case 6234: // longrange (long bow)
		case 6219: // longrange
		case 18077: // lunge (spear)
		case 18080: // swipe (spear)
		case 18079: // pound (spear)
		case 17100: // longrange (darts)
			c.fightMode = 3;
			if (c.autocasting) {
				c.getFunction().resetAutocast();
			}
			break;

		case 9128: // Aggressive
		case 6220: // range rapid
		case 22229: // block (unarmed)
		case 21203: // impale (pickaxe)
		case 21202: // smash (pickaxe)
		case 1079: // pound (staff)
		case 6171: // hack (axe)
		case 6170: // smash (axe)
		case 33020: // swipe (hally)
		case 6235: // rapid (long bow)
		case 17101: // repid (darts)
		case 8237: // lunge (dagger)
		case 8236: // slash (dagger)
			c.fightMode = 2;
			if (c.autocasting) {
				c.getFunction().resetAutocast();
			}
			break;

		/** Prayers **/
		case 21233: // thick skin
			c.getCombat().activatePrayer(0);
			break;
		case 21234: // burst of str
			c.getCombat().activatePrayer(1);
			break;
		case 21235: // charity of thought
			c.getCombat().activatePrayer(2);
			break;
		case 77100:
		case 70080: // range
			c.getCombat().activatePrayer(3);
			break;
		case 77102:
		case 70082: // mage
			c.getCombat().activatePrayer(4);
			break;
		case 21236: // rockskin
			c.getCombat().activatePrayer(5);
			break;
		case 21237: // super human
			c.getCombat().activatePrayer(6);
			break;
		case 21238: // improved reflexes
			c.getCombat().activatePrayer(7);
			break;
		case 21239: // hawk eye
			c.getCombat().activatePrayer(8);
			break;
		case 21240:
			c.getCombat().activatePrayer(9);
			break;
		case 21241: // protect Item
			c.getCombat().activatePrayer(10);
			break;
		case 77104:
		case 70084: // 26 range
			c.getCombat().activatePrayer(11);
			break;
		case 77106:
		case 70086: // 27 mage
			c.getCombat().activatePrayer(12);
			break;
		case 21242: // steel skin
			c.getCombat().activatePrayer(13);
			break;
		case 21243: // ultimate str
			c.getCombat().activatePrayer(14);
			break;
		case 21244: // incredible reflex
			c.getCombat().activatePrayer(15);
			break;
		case 21245: // protect from magic
			c.getCombat().activatePrayer(16);
			break;
		case 21246: // protect from range
			c.getCombat().activatePrayer(17);
			break;
		case 21247: // protect from melee
			c.getCombat().activatePrayer(18);
			break;
		case 77109:
		case 70088: // 44 range
			c.getCombat().activatePrayer(19);
			break;
		case 77111:
		case 70090: // 45 mystic
			c.getCombat().activatePrayer(20);
			break;
		case 2171: // ss
			c.getCombat().activatePrayer(21);
			break;
		case 2172: // redem
			c.getCombat().activatePrayer(22);
			break;
		case 2173: // smite
			c.getCombat().activatePrayer(23);
			break;
		case 70092: // piety
			c.getCombat().activatePrayer(24);
			break;
		case 70094: // rapid renewal
			c.getCombat().activatePrayer(26);
			break;
		case 70096: // piety
			c.getCombat().activatePrayer(25);
			break;
		case 70098: // soulsplit
			c.getCombat().activatePrayer(27);
			break;
		case 70100: // turm
			c.getCombat().activatePrayer(28);
			break;
		case 13092:
			if (!c.inTrade) {
				return;
			}
			if (System.currentTimeMillis() - c.lastButton < 400) {

				c.lastButton = System.currentTimeMillis();

				break;

			} else {

				c.lastButton = System.currentTimeMillis();

			}
			final Client ot = (Client) PlayerHandler.players[c.tradeWith];
			if (ot == null) {
				c.getTrade().declineTrade();
				c.sendMessage("Trade declined as the other player has disconnected.");
				break;
			}
			c.getFunction().sendFrame126("Waiting for other player...", 3431);
			ot.getFunction().sendFrame126("Other player has accepted", 3431);
			c.goodTrade = true;
			ot.goodTrade = true;

			for (final GameItem item : c.getTrade().offeredItems) {
				if (item.id > 0) {
					if (ot.getItems().freeSlots() < c.getTrade().offeredItems
							.size()) {
						c.sendMessage(ot.playerName
								+ " only has "
								+ ot.getItems().freeSlots()
								+ " free slots, please remove "
								+ (c.getTrade().offeredItems.size() - ot
										.getItems().freeSlots()) + " items.");
						ot.sendMessage(c.playerName
								+ " has to remove "
								+ (c.getTrade().offeredItems.size() - ot
										.getItems().freeSlots())
								+ " items or you could offer them "
								+ (c.getTrade().offeredItems.size() - ot
										.getItems().freeSlots()) + " items.");
						c.goodTrade = false;
						ot.goodTrade = false;
						c.getFunction().sendFrame126(
								"Not enough inventory space...", 3431);
						ot.getFunction().sendFrame126(
								"Not enough inventory space...", 3431);
						break;
					} else {
						c.getFunction().sendFrame126(
								"Waiting for other player...", 3431);
						ot.getFunction().sendFrame126(
								"Other player has accepted", 3431);
						c.goodTrade = true;
						ot.goodTrade = true;
					}
				}
			}
			if (c.inTrade && !c.tradeConfirmed && ot.goodTrade && c.goodTrade) {
				c.tradeConfirmed = true;
				if (ot.tradeConfirmed) {
					c.getTrade().confirmScreen();
					ot.getTrade().confirmScreen();
					break;
				}

			}

			break;

		case 13218:
			if (!c.inTrade) {
				return;
			}
			if (System.currentTimeMillis() - c.lastButton < 400) {

				c.lastButton = System.currentTimeMillis();

				break;

			} else {

				c.lastButton = System.currentTimeMillis();

			}
			c.tradeAccepted = true;
			final Client ot1 = (Client) PlayerHandler.players[c.tradeWith];
			if (ot1 == null) {
				c.getTrade().declineTrade();
				c.sendMessage("Trade declined as the other player has disconnected.");
				break;
			}

			if (c.inTrade && c.tradeConfirmed && ot1.tradeConfirmed
					&& !c.tradeConfirmed2) {
				c.tradeConfirmed2 = true;
				if (ot1.tradeConfirmed2) {
					c.acceptedTrade = true;
					ot1.acceptedTrade = true;
					c.getTrade().giveItems();
					ot1.getTrade().giveItems();
					break;
				}
				ot1.getFunction().sendFrame126("Other player has accepted.",
						3535);
				c.getFunction().sendFrame126("Waiting for other player...",
						3535);
			}

			break;
		/* Rules Interface Buttons */
		case 125011: // Click agree
			if (!c.ruleAgreeButton) {
				c.ruleAgreeButton = true;
				c.getFunction().sendFrame36(701, 1);
			} else {
				c.ruleAgreeButton = false;
				c.getFunction().sendFrame36(701, 0);
			}
			break;
		case 125003:// Accept
			if (c.ruleAgreeButton) {
				c.getFunction().showInterface(3559);
				c.newPlayer = false;
			} else if (!c.ruleAgreeButton) {
				c.sendMessage("You need to click on you agree before you can continue on.");
			}
			break;
		case 125006:// Decline
			c.sendMessage("You have chosen to decline, Client will be disconnected from the server.");
			break;
		/* End Rules Interface Buttons */
		/* Player Options */
		case 74176:
			if (!c.mouseButton) {
				c.mouseButton = true;
				c.getFunction().sendFrame36(500, 1);
				c.getFunction().sendFrame36(170, 1);
			} else if (c.mouseButton) {
				c.mouseButton = false;
				c.getFunction().sendFrame36(500, 0);
				c.getFunction().sendFrame36(170, 0);
			}
			break;
		case 74184:
			if (!c.splitChat) {
				c.splitChat = true;
				c.getFunction().sendFrame36(502, 1);
				c.getFunction().sendFrame36(287, 1);
			} else {
				c.splitChat = false;
				c.getFunction().sendFrame36(502, 0);
				c.getFunction().sendFrame36(287, 0);
			}
			break;
		case 74180:
			if (!c.chatEffects) {
				c.chatEffects = true;
				c.getFunction().sendFrame36(501, 1);
				c.getFunction().sendFrame36(171, 0);
			} else {
				c.chatEffects = false;
				c.getFunction().sendFrame36(501, 0);
				c.getFunction().sendFrame36(171, 1);
			}
			break;
		case 74188:
			if (!c.acceptAid) {
				c.acceptAid = true;
				c.getFunction().sendFrame36(503, 1);
				c.getFunction().sendFrame36(427, 1);
			} else {
				c.acceptAid = false;
				c.getFunction().sendFrame36(503, 0);
				c.getFunction().sendFrame36(427, 0);
			}
			break;
		case 74192:
			if (!c.isRunning2) {
				c.isRunning2 = true;
				c.getFunction().sendFrame36(504, 1);
				c.getFunction().sendFrame36(173, 1);
			} else {
				c.isRunning2 = false;
				c.getFunction().sendFrame36(504, 0);
				c.getFunction().sendFrame36(173, 0);
			}
			break;
		case 74201:// brightness1
			c.getFunction().sendFrame36(505, 1);
			c.getFunction().sendFrame36(506, 0);
			c.getFunction().sendFrame36(507, 0);
			c.getFunction().sendFrame36(508, 0);
			c.getFunction().sendFrame36(166, 1);
			break;
		case 74203:// brightness2
			c.getFunction().sendFrame36(505, 0);
			c.getFunction().sendFrame36(506, 1);
			c.getFunction().sendFrame36(507, 0);
			c.getFunction().sendFrame36(508, 0);
			c.getFunction().sendFrame36(166, 2);
			break;

		case 74204:// brightness3
			c.getFunction().sendFrame36(505, 0);
			c.getFunction().sendFrame36(506, 0);
			c.getFunction().sendFrame36(507, 1);
			c.getFunction().sendFrame36(508, 0);
			c.getFunction().sendFrame36(166, 3);
			break;

		case 74205:// brightness4
			c.getFunction().sendFrame36(505, 0);
			c.getFunction().sendFrame36(506, 0);
			c.getFunction().sendFrame36(507, 0);
			c.getFunction().sendFrame36(508, 1);
			c.getFunction().sendFrame36(166, 4);
			break;
		case 74206:// area1
			c.getFunction().sendFrame36(509, 1);
			c.getFunction().sendFrame36(510, 0);
			c.getFunction().sendFrame36(511, 0);
			c.getFunction().sendFrame36(512, 0);
			break;
		case 74207:// area2
			c.getFunction().sendFrame36(509, 0);
			c.getFunction().sendFrame36(510, 1);
			c.getFunction().sendFrame36(511, 0);
			c.getFunction().sendFrame36(512, 0);
			break;
		case 74208:// area3
			c.getFunction().sendFrame36(509, 0);
			c.getFunction().sendFrame36(510, 0);
			c.getFunction().sendFrame36(511, 1);
			c.getFunction().sendFrame36(512, 0);
			break;
		case 74209:// area4
			c.getFunction().sendFrame36(509, 0);
			c.getFunction().sendFrame36(510, 0);
			c.getFunction().sendFrame36(511, 0);
			c.getFunction().sendFrame36(512, 1);
			break;
		case 168:
			c.startAnimation(855);
			break;
		case 169:
			c.startAnimation(856);
			break;
		case 162:
			c.startAnimation(857);
			break;
		case 164:
			if (c.playerEquipment[7] == 10396) {
				c.startAnimation(5312);
			} else {
				c.startAnimation(858);
			}
			break;
		case 165:
			if (c.playerEquipment[0] == 10396) {
				c.startAnimation(5315);
			} else {
				c.startAnimation(859);
			}
			break;
		case 161:
			c.startAnimation(860);
			break;
		case 170:
			c.startAnimation(861);
			break;
		case 171:
			c.startAnimation(862);
			break;
		case 163:
			c.startAnimation(863);
			break;
		case 167:
			c.startAnimation(864);
			break;
		case 172:
			c.startAnimation(865);
			break;
		case 166:
			if (c.playerEquipment[7] == 10394) {
				c.startAnimation(5316);
			} else {
				c.startAnimation(866);
			}
			break;
		case 52050:
			c.startAnimation(2105);
			break;
		case 52051:
			c.startAnimation(2106);
			break;
		case 52052:
			c.startAnimation(2107);
			break;
		case 52054:
			c.startAnimation(2109);
			break;
		case 52055:
			c.startAnimation(2110);
			break;
		case 52056:
			if (c.playerEquipment[0] == 10398) {
				c.startAnimation(5313);
			} else {
				c.startAnimation(2111);
			}
			break;
		case 52057:
			c.startAnimation(2112);
			break;
		case 52058:
			c.startAnimation(2113);
			break;
		case 43092:
			c.startAnimation(0x558);
			break;
		case 2155:
			c.startAnimation(0x46B);
			break;
		case 25103:
			c.startAnimation(0x46A);
			break;
		case 25106:
			c.startAnimation(0x469);
			break;
		case 2154:
			c.startAnimation(0x468);
			break;
		case 52071:
			c.startAnimation(0x84F);
			break;
		case 52072:
			c.startAnimation(0x850);
			break;
		case 59062:
			c.startAnimation(2836);
			break;
		case 72032:
			c.startAnimation(3544);
			break;
		case 72033:
			c.startAnimation(3543);
			break;
		case 72254:// bunny hop
			c.startAnimation(6111);
			break;
		case 74108:
		case 154:
			c.stopMovement();
			for (int i = 0; i < capes.length; i++) {
				if (c.playerEquipment[c.playerCape] == capes[i]
						|| c.playerEquipment[c.playerCape] == capes[i] - 1) {
					c.startAnimation(emotes[i]);
					c.gfx0(gfx[i]);
					return;
				}
			}
			c.boxMessage("You can't perform a Skillcape emote without ",
					"wearing a Skillcape.");

			break;
		/* END OF EMOTES */
		case 118098:
			c.getFunction().castVeng();
			break;

		case 24017:
			c.getFunction().resetAutocast();
			// c.sendFrame246(329, 200, c.playerEquipment[c.playerWeapon]);
			c.getItems().sendWeapon(
					c.playerEquipment[c.playerWeapon],
					ItemAssistant
							.getItemName(c.playerEquipment[c.playerWeapon]));
			// c.setSidebarInterface(0, 328);
			// c.setSidebarInterface(6, c.playerMagicBook == 0 ? 1151 :
			// c.playerMagicBook == 1 ? 12855 : 1151);
			break;
		default:
			Plugin.execute("click_button_" + actionButtonId, c);
			break;
		}
		if (c.isAutoButton(actionButtonId)) {
			c.assignAutocast(actionButtonId);
		}
	}

	@Override
	public void processPacket(Client c, int packetTypfe, int packetSize) {
		final int actionButtonId = Misc.hexToInt(c.getInStream().buffer, 0,
				packetSize);
		/*f
		 * if (System.currentTimeMillis() - c.lastClick < 600) { return; }
		 * c.lastClick = System.currentTimeMillis();
		 */
		handleButton(c, actionButtonId);
	}

}
