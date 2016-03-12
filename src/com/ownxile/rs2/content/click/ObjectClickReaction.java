package com.ownxile.rs2.content.click;

import com.ownxile.core.Plugin;
import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.Point;
import com.ownxile.rs2.combat.prayer.Prayer;
import com.ownxile.rs2.combat.range.CannonHandler;
import com.ownxile.rs2.content.object.CrystalChest;
import com.ownxile.rs2.content.object.Ladder;
import com.ownxile.rs2.content.object.Leaderboard;
import com.ownxile.rs2.items.ItemAssistant;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.skills.crafting.Spinning;
import com.ownxile.rs2.skills.mining.Mining;
import com.ownxile.rs2.skills.runecrafting.Runecrafting;
import com.ownxile.rs2.skills.woodcutting.Woodcutting;
import com.ownxile.rs2.world.games.Barrows;
import com.ownxile.rs2.world.games.CastleWars;
import com.ownxile.rs2.world.games.GameObjects;
import com.ownxile.rs2.world.games.PartyRoom;
import com.ownxile.rs2.world.object.BalloonObject;
import com.ownxile.rs2.world.object.Doors;
import com.ownxile.rs2.world.object.type.Object;
import com.ownxile.rs2.world.transport.FairyRing;
import com.ownxile.rs2.world.transport.Sailing;
import com.ownxile.util.Misc;

public class ObjectClickReaction {

	public static void firstClick(Client client, int objectType, int obX,
			int obY) {
		client.clickObjectType = 0;
		client.turnToObject();
		if (Doors.getSingleton().handleDoor(client.objectId, client.objectX,
				client.objectY, client.absZ)) {

			if (objectType == 2406
					&& client.playerEquipment[client.playerWeapon] == 772) {
				client.getFunction().walkTo(1, 0);
				FairyRing.zanarisTeleport(client, 2452, 4473);
			}
		}

		if (Ladder.checkForPlugin(client, objectType, obX, obY, client.absZ)) {
			return;
		}
		switch (objectType) {
		case 3192:
			if (System.currentTimeMillis() - client.lastAgility > 2000) {
				Leaderboard.displayLeadingRating(client);
				client.lastAgility = System.currentTimeMillis();
			}
			break;
		case 1754:
			Ladder.climb(client, client.absX, client.absY + 6400, 0);
			break;
		case 10817:
			if (client.jail == 1) {
				client.lastClickedNpcId = 3118;
				client.npcChat("You haven't been given permission to leave yet!");
				return;
			}
			client.startAnimation(2140);
			break;
		case 3931:
			if (client.absX > 2198)
				client.getFunction().movePlayer(2196, 3237, 0);
			else
				client.getFunction().movePlayer(2202, 3237, 0);
			break;
		case 3999:
			client.getFunction().movePlayer(client.absX, 3171, 0);
			break;
		case 3998:
			client.getFunction().movePlayer(client.absX, 3162, 0);
			break;
		case 8738:
		case 8739:
			client.getFunction().movePlayer(2556, client.absY, 0);
			break;
		case 6455:
			client.getFunction().movePlayer(2838, 3803, 1);
			break;
		case 2644:
		case 4309:
		case 5707:
			Spinning.spinInterface(client);
			break;
		case 8149:
			client.boxMessage("Use a rake to remove dead herbs.");
			break;
		case 8174:
		case 8132:
		case 8139:
		case 8140:
		case 8141:
		case 8142:
			client.boxMessage(client.getFarming().getInspectMessage(obX, obY));
			break;
		case 172:
			CrystalChest.searchChest(client, 172, obX, obY);
			break;
		case 2296:// log balance
			int moveX = 0;
			if (obX == 2599) {// west side
				moveX = 5;
			} else if (obX == 2602) {
				moveX = -5;
			}
			client.getFunction()
					.movePlayer(client.absX + moveX, client.absY, 0);
			break;

		case 1725:
		case 1738:// staircases
			if (client.absX > 2838 && client.absX < 2845 && client.absY > 3535
					&& client.absY < 3545) {
				if (client.hasItem(8851, 100)) {
					client.getFunction().movePlayer(client.getX(),
							client.getY(), client.getZ() + 2);
					client.deleteItem(8851, 100);
				} else {
					client.boxMessage("You need at least 100 tokens to go upstairs.");
				}

			} else {
				client.getFunction().movePlayer(client.getX(), client.getY(),
						client.getZ() + 1);

			}
			break;

		case 20:
		case 15:
		case 14:// does not need repairing
		case 16:
		case 18:
		case 19:// needs repairing
			client.boxMessage("This railing does not need repairing.");
			break;
		case 5:
			if (client.getQuest(21).getStage() == 5) {
				client.boxMessage("You fix the broken cannon,");
				client.startAnimation(896);
				client.getQuest(21).setStage(6);
			} else {
				client.boxMessage("The cannon does not need repairing.");
			}
			break;
		case 15596:
			if (client.getQuest(21).getStage() == 3) {
				client.getFunction().object(-1, obX, obY, client.absZ, 0, 10);
				client.addItem(0);
				client.boxMessage("You take the remains of a dead dwarf.");
				client.startAnimation(827);
				client.getQuest(21).setStage(4);
			} else {
				client.sendMessage("Ewww why would you want dwarf remains?");
			}
			break;
		case 17:
			if (client.getQuest(21).getStage() == 1) {
				client.boxMessage("You fix the broken railings,");
				client.startAnimation(896);
				client.getQuest(21).setStage(2);
			} else {
				client.boxMessage("The fence does not need repairing.");
			}
			break;
		case 4568:
			if (client.getQuest(20).getStage() == 2) {
				client.getFunction().movePlayer(client.getX(), client.getY(),
						client.getZ() + 1);
			} else {
				client.sendMessage("You must have completed Horror from the Deep to explore the lighthouse.");
			}
			break;
		case 1740:// staircases
		case 1726:
		case 4569:
		case 4570:
			client.getFunction().movePlayer(client.getX(), client.getY(),
					client.getZ() - 1);
			break;
		case 15638:

			client.getFunction().movePlayer(client.getX(), client.getY(),
					client.getZ() - 2);
			break;
		case 4626:
			client.getFunction().movePlayer(client.getX(), client.getY() + 4,
					client.getZ() + 1);
			break;
		case 4625:
			client.getFunction().movePlayer(client.getX(), client.getY() - 4,
					client.getZ() - 1);
			break;

		case 26360:
			client.getFunction().movePlayer(client.getX() + 4,
					client.getY() + 6400, client.getZ());
			break;

		case 1739:
		case 1748:
		case 8745:
		case 2884:
			client.dialogueOption("Climb up", 500345346, "Climb down",
					500345345);
			break;
		case 12094:
			FairyRing.zanarisTeleport(client, 3092, 3492);
			break;

		case 5083:
			client.getFunction().movePlayer(2713, 9564, 0);
			break;
		case 5103:
			if (client.absX == 2691 && client.absY == 9564) {
				client.getFunction().movePlayer(2689, 9564, 0);
			} else if (client.absX == 2689 && client.absY == 9564) {
				client.getFunction().movePlayer(2691, 9564, 0);
			}
			break;
		case 5106:
			if (client.absX == 2674 && client.absY == 9479) {
				client.getFunction().movePlayer(2676, 9479, 0);
			} else if (client.absX == 2676 && client.absY == 9479) {
				client.getFunction().movePlayer(2674, 9479, 0);
			}
			break;

		case 5105:
			if (client.absX == 2672 && client.absY == 9499) {
				client.getFunction().movePlayer(2674, 9499, 0);
			} else if (client.absX == 2674 && client.absY == 9499) {
				client.getFunction().movePlayer(2672, 9499, 0);
			}
			break;

		case 5107:
			if (client.absX == 2693 && client.absY == 9482) {
				client.getFunction().movePlayer(2695, 9482, 0);
			} else if (client.absX == 2695 && client.absY == 9482) {
				client.getFunction().movePlayer(2693, 9482, 0);
			}
			break;

		case 5104:
			if (client.absX == 2683 && client.absY == 9568) {
				client.getFunction().movePlayer(2683, 9570, 0);
			} else if (client.absX == 2683 && client.absY == 9570) {
				client.getFunction().movePlayer(2683, 9568, 0);
			}
			break;

		case 5100:
			if (client.absY <= 9567) {
				client.getFunction().movePlayer(2655, 9573, 0);
			} else if (client.absY >= 9572) {
				client.getFunction().movePlayer(2655, 9566, 0);
			}
			break;

		case 5099:
			if (client.absY <= 9493) {
				client.getFunction().movePlayer(2698, 9500, 0);
			} else if (client.absY >= 9499) {
				client.getFunction().movePlayer(2698, 9492, 0);
			}
			break;

		case 5088:
			if (client.absX <= 2682) {
				client.getFunction().movePlayer(2687, 9506, 0);
			} else if (client.absX >= 2687) {
				client.getFunction().movePlayer(2682, 9506, 0);
			}
			break;

		case 5110:
			client.getFunction().movePlayer(2647, 9557, 0);
			break;

		case 5111:
			client.getFunction().movePlayer(2649, 9562, 0);
			break;

		case 5084:
			client.getFunction().movePlayer(2744, 3151, 0);
			break;
		case 4780:
			Ladder.climb(client, 2764, 9103, 0);
			break;
		case 4781:
			Ladder.climb(client, 2763, 2703, 0);
			break;
		case 2268:
		case 11727:
		case 8744:
			Ladder.climb(client, client.absX, client.absY, client.absZ + 1);
			break;
		case 2269:
		case 24355:
		case 11728:
		case 8746:
			Ladder.climb(client, client.absX, client.absY, client.absZ - 1);
			break;
		case 6:
			CannonHandler.firstClickObject(client);
			break;
		case 9295:
			if (client.absX < 3154) {
				client.move(new Point(3155, 9906, 0));
			} else {
				client.move(new Point(3149, 9906, 0));
			}
			break;
		case 2408:
			Ladder.climb(client, 2828, 9772, 0);
			break;
		case 10721:
			if (client.absY == 3298) {
				client.getFunction().movePlayer(3363, 3300, 0);
			} else if (client.absY == 3300) {
				client.getFunction().movePlayer(3363, 3298, 0);
			}
			break;
		case 12355:
			client.dialogueAction = 12;
			client.getChat().sendOption4("Rock Crabs @blu@[Training]",
					"Varrock Dragons @blu@[Single PK]",
					"Chaos Temple @blu@[Multi PK]",
					"Entrana @blu@[Skilling Area]");
			break;
		case 4772:
			Ladder.climb(client, client.absX, client.absY, 1);
			break;
		case 4778:
			Ladder.climb(client, client.absX, client.absY, 0);
			break;
		case 11211:
			client.getFunction().movePlayer(3684, 2950, 1);
			break;
		case 11212:
			client.getFunction().movePlayer(3684, 2953, 0);
			break;
		case 11309:
			client.getFunction().movePlayer(3677, 2948, 0);
			break;
		case 11308:
			client.getFunction().movePlayer(3677, 2948, 1);
			break;
		case 11289:
			client.getFunction().movePlayer(3687, client.absY, 2);
			break;
		case 11290:
			client.getFunction().movePlayer(3685, client.absY, 1);
			break;
		case 1276:
		case 1278:// regular
		case 1281:// oak
		case 5551:// willow
		case 5552:// willow
		case 5553:// willow
		case 1308:// willow
		case 1307:// maple
		case 1309:
		case 2023:
		case 10041:
		case 1306:// yew
		case 1282:
		case 1283:
		case 1284:
		case 1285:
		case 1286:
		case 1287:
		case 1288:
		case 1289:
		case 1290:
		case 1291:
		case 1383:
		case 1384:
		case 5902:
		case 5903:
		case 5904:
		case 1319:
		case 1318:
		case 1315:
		case 3881:
		case 3879:
			Woodcutting.attemptWoodcutting(client, objectType);
			break;
		case 12003:
			FairyRing.ringTeleport(client, obX, obY);
			break;
		case 2412:
			Sailing.startTravel(client, 1);
			break;
		case 2414:
			Sailing.startTravel(client, 2);
			break;
		case 2083:
			Sailing.startTravel(client, 5);
			break;
		case 2081:
			Sailing.startTravel(client, 6);
			break;
		case 14304:
			Sailing.startTravel(client, 14);
			break;
		case 14306:
			Sailing.startTravel(client, 15);
			break;
		case 882:
			Ladder.climb(client, 3237, 9859, 0);
			break;
		case 7257:
			client.getFunction().movePlayer(3061, 4985, 1);
			break;
		case 7258:
			client.getFunction().movePlayer(2906, 3537, 0);
			break;
		case 26933:
			client.getFunction().movePlayer(client.getX(),
					client.getAbsY() + 6400, 0);
			break;
		case 115:
		case 116:
		case 117:
		case 118:
		case 119:
			if (client.isIronman() || client.isUltimateIronman()) {
				client.sendMessage("Iron accounts cannot receive loot here.");
				break;
			}
			BalloonObject balloon = PartyRoom.getBalloon(obX, obY);
			if (balloon != null) {
				balloon.pop(client, obX, obY);
			} else {
				client.startAnimation(794);
				client.getFunction().walkTo(obX, obY);
				World.getObjectHandler().globalObject(-1, obX, obY, 0, 10);
			}
			break;
		case 8967:
			client.getFunction().movePlayer(3083, 3486, 0);
			break;
		case 1512:
			if (client.getY() == 3098) {
				client.getFunction().movePlayer(client.getX(), 3099, 0);
			} else if (client.getY() == 3099) {
				client.getFunction().movePlayer(client.getX(), 3098, 0);
			}
			break;
		case 9398:
			client.getFunction().sendFrame248(4465, 197);
			client.getItems().resetItems(7423);
			break;
		case 13672:
			client.getTask().pullGambleLever();
			break;
		case 2418:
			PartyRoom.open(client);
			break;
		case 2416:
			client.startAnimation(2140);
			PartyRoom.dropAll();
			break;
		case 4484:
			CastleWars.castleWarsSeason(client);
			break;
		case 2444:
			client.getFunction().movePlayer(2491, 9864, 0);
			break;
		case 2446:
			client.getFunction().movePlayer(2464, 9897, 0);
			break;
		case 1752:
			client.getFunction().movePlayer(2487, 3465, 2);
			break;
		case 9391:
			// client.getChat().sendOption2("View the other fighters",
			// "No thanks");
			// c.dialogueAction = 44;
			break;
		case 5130:
			Ladder.climb(client, 3090, 3491, 0);
			break;
		case 2447:
			Ladder.climb(client, client.getX() + 1, client.getY() + 2,
					client.absZ + 1);
			break;
		case 2448:
			Ladder.climb(client, 2484, 3463, client.absZ + 1);
			break;
		case 1749:
		case 1746:// Monastery Ladder down
			Ladder.climb(client, client.getX(), client.getY(), client.absZ - 1);
			break;
		case 1750:
		case 1747:
			Ladder.climb(client, client.getX(), client.getY(), client.absZ + 1);
			break;
		case 2641:
			Ladder.climb(client, client.getX(), client.getY(), client.absZ + 1);
			break;
		case 11724:
			client.getFunction().movePlayer(2968, 3348, 1);
			break;
		case 11734:
			client.getFunction().movePlayer(2984, 3340, 2);
			break;
		case 11735:
			client.getFunction().movePlayer(2984, 3337, 1);
			break;
		case 2147: // Stairs down
			if (client.objectX == 3104 && client.objectY == 3162) {
				client.getFunction().movePlayer(3104, 9576, 0);
			}
			break;
		case 2148: // Stairs dungeon (up)
			if (client.objectX == 3103 && client.objectY == 9576) {
				client.getFunction().movePlayer(3105, 3162, 0);
			}
			break;
		case 11993: // Doors
			if (client.objectX == 3109 && client.objectY == 3167) {
				if (client.absY == 3167) {
					client.getFunction().walkTo(0, -1);
				} else {
					client.getFunction().walkTo(0, 1);
				}
			} else if (client.objectX == 3107 && client.objectY == 3162) {
				if (client.absY == 3163) {
					client.getFunction().walkTo(0, -2);
				} else {
					client.getFunction().walkTo(0, 2);
				}
			}
			break;

		case 7129: // Fire Riff
			if (client.getItems().playerHasItem(1442, 1)
					|| client.getItems().playerHasItem(5537)
					|| client.playerEquipment[client.playerHat] == 5537) {
				client.getFunction().spellTeleport(2583, 4838, 0);
			} else {
				client.boxMessage("You need a @dre@Fire tiara@bla@ to teleport to the altar.");
			}
			break;
		case 7130: // Earth Riff
			if (client.getItems().playerHasItem(1440, 1)
					|| client.getItems().playerHasItem(5535)
					|| client.playerEquipment[client.playerHat] == 5535) {
				client.getFunction().spellTeleport(2660, 4839, 0);
			} else {
				client.boxMessage("You need a @dre@Earth tiara@bla@ to teleport to the altar.");
			}
			break;
		case 7131: // Body Riff
			if (client.getItems().playerHasItem(1446, 1)
					|| client.getItems().playerHasItem(5533)
					|| client.playerEquipment[client.playerHat] == 5533) {
				client.getFunction().spellTeleport(2527, 4833, 0);
			} else {
				client.boxMessage("You need a @dre@Body tiara@bla@ to teleport to the altar.");
			}
			break;
		case 7132: // Cosmic Riff
			if (client.getItems().playerHasItem(1454, 1)
					|| client.getItems().playerHasItem(5539)
					|| client.playerEquipment[client.playerHat] == 5539) {
				client.getFunction().spellTeleport(2162, 4833, 0);
			} else {
				client.boxMessage("You need a @dre@Cosmic tiara@bla@ to teleport to the altar.");
			}
			break;
		case 7133: // Nature Riff
			if (client.getItems().playerHasItem(1462, 1)
					|| client.getItems().playerHasItem(5541)
					|| client.playerEquipment[client.playerHat] == 5541) {
				client.getFunction().spellTeleport(2398, 4841, 0);
			} else {
				client.boxMessage("You need a @dre@Nature tiara@bla@ to teleport to the altar.");
			}
			break;
		case 7134: // Chaos Riff
			if (client.getItems().playerHasItem(1452, 1)
					|| client.getItems().playerHasItem(5543)
					|| client.playerEquipment[client.playerHat] == 5543) {
				client.getFunction().spellTeleport(2269, 4843, 0);
			} else {
				client.boxMessage("You need a @dre@Chaos tiara@bla@ to teleport to the altar.");
			}
			break;
		case 7135: // Law Riff
			if (client.getItems().playerHasItem(1458, 1)
					|| client.getItems().playerHasItem(5545)
					|| client.playerEquipment[client.playerHat] == 5545) {
				client.getFunction().spellTeleport(2464, 4834, 0);
			} else {
				client.boxMessage("You need a @dre@Law tiara@bla@ to teleport to the altar.");
			}
			break;
		case 7136: // Death Riff
			if (client.getItems().playerHasItem(1456, 1)
					|| client.getItems().playerHasItem(5547)
					|| client.playerEquipment[client.playerHat] == 5547) {
				client.getFunction().spellTeleport(2207, 4836, 0);
			} else {
				client.boxMessage("You need a @dre@Death tiara@bla@ to teleport to the altar.");
			}
			break;
		case 7137: // Water Riff
			if (client.getItems().playerHasItem(1444, 1)
					|| client.getItems().playerHasItem(5531)
					|| client.playerEquipment[client.playerHat] == 5531) {
				client.getFunction().spellTeleport(2713, 4836, 0);
			} else {
				client.boxMessage("You need a @dre@Water tiara@bla@ to teleport to the altar.");
			}
			break;
		case 7138: // Soul Riff
			if (client.getItems().playerHasItem(1438, 1)
					|| client.getItems().playerHasItem(5527)
					|| client.playerEquipment[client.playerHat] == 5551) {
				client.getFunction().spellTeleport(2845, 4832, 0);
			} else {
				client.boxMessage("You need a @dre@Soul tiara@bla@ to teleport to the altar.");
			}
			break;
		case 7139: // Air Riff
			if (client.getItems().playerHasItem(1438, 1)
					|| client.getItems().playerHasItem(5527)
					|| client.playerEquipment[client.playerHat] == 5527) {
				client.getFunction().spellTeleport(2845, 4832, 0);
			} else {
				client.boxMessage("You need a @dre@Air tiara@bla@ to teleport to the altar.");
			}
			break;
		case 7140: // Mind Riff
			if (client.getItems().playerHasItem(1448, 1)
					|| client.getItems().playerHasItem(5529)
					|| client.playerEquipment[client.playerHat] == 5529) {
				client.getFunction().spellTeleport(2788, 4841, 0);
			} else {
				client.boxMessage("You need a @dre@Mind tiara@bla@ to teleport to the altar.");
			}
			break;
		case 7141: // Blood Riff
			if (client.getItems().playerHasItem(1450, 1)
					|| client.getItems().playerHasItem(5549)
					|| client.playerEquipment[client.playerHat] == 5549) {
				Runecrafting.craftRunes(client, 2489);
			} else {
				client.boxMessage("You need a @dre@Blood tiara@bla@ to teleport to the altar.");
			}
			break;

		case 733:
			final Object geez = new Object(-1, obX, obY, client.absZ, 0, 10,
					733, 50);
			client.startAnimation(client.getCombat().getWepAnim(
					ItemAssistant.getItemName(
							client.playerEquipment[client.playerWeapon])
							.toLowerCase()));

			World.getSynchronizedTaskScheduler().schedule(new Task(1, false) {
				@Override
				protected void execute() {
					World.getObjectManager().placeObject(geez);
				}
			});
			break;
		case 3628:
		case 3632:
		case 1558:
		case 1557:
		case 1597:
		case 1596:
		case 2266:
		case 134:
		case 135:
		case 2262:
		case 15653:
		case 1551:
		case 1553:
		case 15644:
		case 15641:
		case 1967:
		case 1968:
		case 2143:
		case 3197:
		case 3198:
		case 2144:
		case 2882:
		case 2883:
		case 1528:
		case 2041:
		case 2039:
			new Object(-1, obX, obY, client.absZ, 0, 0, objectType, 200);
			break;
		case 2616:
			client.getFunction().movePlayer(3077, 9771, 0);
			break;
		case 2617:
			client.getFunction().movePlayer(3116, 3356, 0);
			break;
		case 132:
			Ladder.climb(client, 3092, 3361, 0);
			break;
		case 133:
			Ladder.climb(client, 3117, 9753, 0);
			break;
		case 9299:
			if (client.getY() > 3090) {
				client.getFunction().movePlayer(client.getX(),
						client.getY() - 1, 0);
			} else {
				client.getFunction().movePlayer(client.getX(),
						client.getY() + 1, 0);
			}

			break;
		case 155:
		case 156:
			if (client.getX() > 3097) {
				client.getFunction().movePlayer(client.getX() - 2,
						client.getY(), 0);
			} else {
				client.getFunction().movePlayer(client.getX() + 2,
						client.getY(), 0);
			}

			break;
		case 6434:
			client.getFunction().movePlayer(client.getX(),
					client.getY() + 6400, 0);
			break;
		case 3629:
			client.sendMessage("Wrong way!");
			break;
		case 3630:
			client.sendMessage("Wrong way!");
			break;
		case 3631:
			client.sendMessage("Wrong way!");
			break;
		case 3635:
			client.sendMessage("The chest is empty.");
			break;
		case 5272:
			client.sendMessage("The chest is empty.");
			break;
		case 5269:
			client.getFunction().movePlayer(3599, 3564, 0);
			break;
		case 2465:
			if (client.isDonator()) {
				client.getChat().sendChat(420, 3117);
			} else {
				client.getFunction().movePlayer(2524, 4777, 0);
			}
			break;
		case 2466:
			client.getChat().sendOption4("@blu@Edgeville", "@blu@Mage Bank",
					"@blu@Green Dragons", "@blu@Falador");
			client.dialogueAction = 35;
			break;
		case 12356:
			client.getChat().sendOption4("Armadyl", "Bandos", "Saradomin",
					"Zamorak");
			client.dialogueAction = 20;
			break;
		case 1765:
			Ladder.climb(client, 3069, 10257, 0);
			break;
		case 881:
			client.sendMessage("You open the manhole.");
			client.startAnimation(827);
			client.getFunction().addObject(882, client.objectX, client.objectY,
					client.absZ, 0, 10);
			break;
		case 12616:
			client.getFunction().movePlayer(2753, 2790, 0);
			client.sendMessage("You climb up the rope.");
			break;
		case 10699:
			client.getFunction().movePlayer(3079, 3505, 0);
			client.sendMessage("You climb up the trapdoor steps.");
			break;
		case 10707:
			client.getFunction().movePlayer(2007, 4429, 1);
			break;
		case 10708:
			client.getFunction().movePlayer(2007, 4431, 0);
			break;
		case 4712:
			if (client.getQuest(19).getStage() == 4) {
				client.getFunction().movePlayer(2654, 4563, 1);
				client.startChat(1635409878);
			} else {
				client.sendMessage("The trapdoor is locked.");
			}
			break;
		case 272:
			client.getFunction().movePlayer(client.absX, client.absY, 1);
			break;
		case 12570:
			Ladder.climb(client, 2753, 2742, 2);
			break;
		case 12265:
			client.getFunction().movePlayer(3078, 3493, 0);
			break;
		case 12266:
		case 4005:
			client.getFunction().movePlayer(3077, 9893, 0);
			break;
		case 12576:
			client.sendMessage("You climb the skull slope.");
			client.getFunction().movePlayer(2743, 2741, 0);
			break;
		case 12578:
			client.getFunction().movePlayer(2756, 2731, 0);
			client.sendMessage("You swing across.");
			break;
		case 12180:
			client.sendMessage("Who knows what could be down there?");
			break;
		case 273:

			client.getFunction().movePlayer(client.absX, client.absY, 0);
			break;
		case 245:
			client.getFunction().movePlayer(client.absX, client.absY + 2, 2);
			break;
		case 246:
			client.getFunction().movePlayer(client.absX, client.absY - 2, 1);
			break;
		case 1766:
			Ladder.climb(client, 3016, 3849, 0);
			break;

		case 1816:
			client.getFunction().startTeleport2(2271, 4680, 0);
			break;
		case 1817:
			client.getFunction().startTeleport(3067, 10253, 0, "modern");
			break;
		case 1814:
			client.getTask().pullLever(objectType, 3153, 3923, obX, obY, 0);
			break;
		case 9356:
			client.getFunction().enterCaves();
			break;
		case 1733:
			client.getFunction().movePlayer(client.absX, client.absY + 6393, 0);
			break;

		case 1734:
			client.getFunction().movePlayer(client.absX, client.absY - 6396, 0);
			break;

		case 9357:
			client.getFunction().resetTzhaar();
			break;

		case 8959:
			if (client.getX() == 2490
					&& (client.getY() == 10146 || client.getY() == 10148)) {
				if (client.getFunction().checkForPlayer(2490,
						client.getY() == 10146 ? 10148 : 10146)) {
					new Object(6951, client.objectX, client.objectY,
							client.absZ, 1, 10, 8959, 15);
				}
			}
			break;

		case 2213:
		case 4483:
		case 14367:
		case 11758:
		case 11402:
		case 11338:
		case 26972:
		case 16700:
		case 10517:
		case 21301:
			client.lastClickedNpcId = 494;
			client.startChat(1009305985);
			break;

		case 10177:
			client.getFunction().movePlayer(1890, 4407, 0);
			break;
		case 10230:
			client.getFunction().movePlayer(2900, 4449, 0);
			break;
		case 10229:
			client.getFunction().movePlayer(1912, 4367, 0);
			break;
		case 2623:
			if (client.absX >= client.objectX) {
				client.getFunction().walkTo(-1, 0);
			} else {
				client.getFunction().walkTo(1, 0);
			}
			break;
		// pc boat
		case 14315:
			client.getFunction().movePlayer(2661, 2639, 0);
			break;
		case 14314:
			client.getFunction().movePlayer(2657, 2639, 0);
			break;
		case 14235:
		case 14233:
			if (client.objectX == 2670) {
				if (client.absX <= 2670) {
					client.absX = 2671;
				} else {
					client.absX = 2670;
				}
			}
			if (client.objectX == 2643) {
				if (client.absX >= 2643) {
					client.absX = 2642;
				} else {
					client.absX = 2643;
				}
			}
			if (client.absX <= 2585) {
				client.absY += 1;
			} else {
				client.absY -= 1;
			}
			client.getFunction().movePlayer(client.absX, client.absY, 0);
			break;

		case 14829:
		case 14830:
		case 14827:
		case 14828:
		case 14826:
		case 14831:
			// Tick.objectHandler.startObelisk(objectType);
			World.getObjectManager().startObelisk(objectType);
			break;
		/*
		 * case 4387: Server.CastleWars.joinWait(player, 1); break;
		 * 
		 * case 4388: Server.CastleWars.joinWait(player, 2); break;
		 * 
		 * case 4408: Server.CastleWars.joinWait(player, 3); break;
		 */
		case 9369:
			if (client.getY() > 5175) {
				client.getFunction().movePlayer(2399, 5175, 0);
			} else {
				client.getFunction().movePlayer(2399, 5177, 0);
				client.getFunction().walkableInterface(-1);
				client.getFunction().closeAllWindows();
			}
			break;

		case 9368:
			if (client.getY() < 5169) {
				World.getFightPits().removePlayer(client.getFunction());
				client.getFunction().movePlayer(2399, 5169, 0);
				client.lastMovement = System.currentTimeMillis();
			}
			break;
		// case 1747:
		case 4411:
		case 4415:
		case 4417:
		case 4418:
		case 4419:
		case 4420:
		case 4469:
		case 4470:
		case 4911:
		case 4912:
		case 1757:
		case 4437:
		case 6281:
		case 6280:
		case 4472:
		case 4471:
		case 4406:
		case 4407:
		case 4458:
		case 4902:
		case 4903:
		case 4900:
		case 4901:
		case 4461:
		case 4463:
		case 4464:
		case 4377:
		case 4378:
		case 4467:
		case 4427:
		case 4428:
		case 4423:
		case 4424:
			GameObjects.handleObject(client, objectType, obX, obY);
			break;

		// barrows
		// Chest
		case 10284:
			Barrows.openChest(client);
			break;
		// doors
		case 6749:
			if (obX == 3562 && obY == 9678) {
				client.getFunction().object(3562, 9678, 6749, client.absZ, -3,
						0);
				client.getFunction().object(3562, 9677, 6730, client.absZ, -1,
						0);
			} else if (obX == 3558 && obY == 9677) {
				client.getFunction().object(3558, 9677, 6749, client.absZ, -1,
						0);
				client.getFunction().object(3558, 9678, 6730, client.absZ, -3,
						0);
			}
			break;
		case 6730:
			if (obX == 3558 && obY == 9677) {
				client.getFunction().object(3562, 9678, 6749, client.absZ, -3,
						0);
				client.getFunction().object(3562, 9677, 6730, client.absZ, -1,
						0);
			} else if (obX == 3558 && obY == 9678) {
				client.getFunction().object(3558, 9677, 6749, client.absZ, -1,
						0);
				client.getFunction().object(3558, 9678, 6730, client.absZ, -3,
						0);
			}
			break;
		case 6727:
			if (obX == 3551 && obY == 9684) {
				client.sendMessage("You cant open this door..");
			}
			break;
		case 6746:
			if (obX == 3552 && obY == 9684) {
				client.sendMessage("You cant open this door..");
			}
			break;
		case 6748:
			if (obX == 3545 && obY == 9678) {
				client.getFunction().object(3545, 9678, 6748, client.absZ, -3,
						0);
				client.getFunction().object(3545, 9677, 6729, client.absZ, -1,
						0);
			} else if (obX == 3541 && obY == 9677) {
				client.getFunction().object(3541, 9677, 6748, client.absZ, -1,
						0);
				client.getFunction().object(3541, 9678, 6729, client.absZ, -3,
						0);
			}
			break;
		case 6729:
			if (obX == 3545 && obY == 9677) {
				client.getFunction().object(3545, 9678, 6748, client.absZ, -3,
						0);
				client.getFunction().object(3545, 9677, 6729, client.absZ, -1,
						0);
			} else if (obX == 3541 && obY == 9678) {
				client.getFunction().object(3541, 9677, 6748, client.absZ, -1,
						0);
				client.getFunction().object(3541, 9678, 6729, client.absZ, -3,
						0);
			}
			break;
		case 6726:
			if (obX == 3534 && obY == 9684) {
				client.getFunction().object(3534, 9684, 6726, client.absZ, -4,
						0);
				client.getFunction().object(3535, 9684, 6745, client.absZ, -2,
						0);
			} else if (obX == 3535 && obY == 9688) {
				client.getFunction().object(3535, 9688, 6726, client.absZ, -2,
						0);
				client.getFunction().object(3534, 9688, 6745, client.absZ, -4,
						0);
			}
			break;
		case 6745:
			if (obX == 3535 && obY == 9684) {
				client.getFunction().object(3534, 9684, 6726, client.absZ, -4,
						0);
				client.getFunction().object(3535, 9684, 6745, client.absZ, -2,
						0);
			} else if (obX == 3534 && obY == 9688) {
				client.getFunction().object(3535, 9688, 6726, client.absZ, -2,
						0);
				client.getFunction().object(3534, 9688, 6745, client.absZ, -4,
						0);
			}
			break;
		case 6743:
			if (obX == 3545 && obY == 9695) {
				client.getFunction().object(3545, 9694, 6724, client.absZ, -1,
						0);
				client.getFunction().object(3545, 9695, 6743, client.absZ, -3,
						0);
			} else if (obX == 3541 && obY == 9694) {
				client.getFunction().object(3541, 9694, 6724, client.absZ, -1,
						0);
				client.getFunction().object(3541, 9695, 6743, client.absZ, -3,
						0);
			}
			break;
		case 6724:
			if (obX == 3545 && obY == 9694) {
				client.getFunction().object(3545, 9694, 6724, client.absZ, -1,
						0);
				client.getFunction().object(3545, 9695, 6743, client.absZ, -3,
						0);
			} else if (obX == 3541 && obY == 9695) {
				client.getFunction().object(3541, 9694, 6724, client.absZ, -1,
						0);
				client.getFunction().object(3541, 9695, 6743, client.absZ, -3,
						0);
			}
			break;
		// end doors
		// coffins
		case 6707: // verac
			client.getFunction().movePlayer(3556, 3298, 0);
			break;

		case 6823:
			if (com.ownxile.rs2.world.games.Barrows.selectCoffin(client,
					objectType)) {
				return;
			}
			if (client.barrowsNpcs[0][1] == 0) {
				World.getNpcHandler()
						.spawnNpc(client, 2030, client.getX(),
								client.getY() - 1, -1, 0, 120, 25, 200, 200,
								true, true);
				client.barrowsNpcs[0][1] = 1;
			} else {
				client.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		case 6706: // torag
			client.getFunction().movePlayer(3553, 3283, 0);
			break;

		case 6772:
			if (com.ownxile.rs2.world.games.Barrows.selectCoffin(client,
					objectType)) {
				return;
			}
			if (client.barrowsNpcs[1][1] == 0) {
				World.getNpcHandler().spawnNpc(client, 2029, client.getX() + 1,
						client.getY(), -1, 0, 120, 20, 200, 200, true, true);
				client.barrowsNpcs[1][1] = 1;
			} else {
				client.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		case 6705: // karil stairs
			client.getFunction().movePlayer(3565, 3276, 0);
			break;
		case 6822:
			if (com.ownxile.rs2.world.games.Barrows.selectCoffin(client,
					objectType)) {
				return;
			}
			if (client.barrowsNpcs[2][1] == 0) {
				World.getNpcHandler().spawnNpc(client, 2028, client.getX(),
						client.getY() - 1, -1, 0, 90, 17, 200, 200, true, true);
				client.barrowsNpcs[2][1] = 1;
			} else {
				client.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		case 6704: // guthan stairs
			client.getFunction().movePlayer(3578, 3284, 0);
			break;
		case 6773:
			if (com.ownxile.rs2.world.games.Barrows.selectCoffin(client,
					objectType)) {
				return;
			}
			if (client.barrowsNpcs[3][1] == 0) {
				World.getNpcHandler()
						.spawnNpc(client, 2027, client.getX(),
								client.getY() - 1, -1, 0, 120, 23, 200, 200,
								true, true);
				client.barrowsNpcs[3][1] = 1;
			} else {
				client.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		case 6703: // dharok stairs
			client.getFunction().movePlayer(3574, 3298, 0);
			break;
		case 6771:
			if (com.ownxile.rs2.world.games.Barrows.selectCoffin(client,
					objectType)) {
				return;
			}
			if (client.barrowsNpcs[4][1] == 0) {
				World.getNpcHandler()
						.spawnNpc(client, 2026, client.getX(),
								client.getY() - 1, -1, 0, 120, 45, 250, 250,
								true, true);
				client.barrowsNpcs[4][1] = 1;
			} else {
				client.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		case 6702: // ahrim stairs
			client.getFunction().movePlayer(3565, 3290, 0);
			break;
		case 6821:
			if (com.ownxile.rs2.world.games.Barrows.selectCoffin(client,
					objectType)) {
				return;
			}
			if (client.barrowsNpcs[5][1] == 0) {
				World.getNpcHandler().spawnNpc(client, 2025, client.getX(),
						client.getY() - 1, -1, 0, 90, 19, 200, 200, true, true);
				client.barrowsNpcs[5][1] = 1;
			} else {
				client.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		/*
		 * case 1276: case 1278:// trees //
		 * player.sendMessage("You chop the tree."); /* Woodcutting object
		 * respawning etc. by lmtruck... making my own - look at this for
		 * example Objects stump = new Objects(1343, player.objectX,
		 * player.objectY, 0, -1, 10, 0); Tick.objectHandler.addObject(stump);
		 * Tick.objectHandler.placeObject(stump); Objects tree = new
		 * Objects(player.objectId, player.objectX, player.objectY, 0, -1, 10,
		 * 7); Tick.objectHandler.addObject(tree);
		 */
		// player.treeId = objectType;

		/*
		 * player.woodcut[0] = 1511; player.woodcut[1] = 1; player.woodcut[2] =
		 * 25; player.getWoodcutting().startWoodcutting(player.woodcut[0],
		 * player.woodcut[1], player.woodcut[2]); break;
		 * 
		 * case 1281: // oak player.woodcut[0] = 1521; player.woodcut[1] = 15;
		 * player.woodcut[2] = 37;
		 * player.getWoodcutting().startWoodcutting(player.woodcut[0],
		 * player.woodcut[1], player.woodcut[2]); break;
		 * 
		 * case 1308: // willow player.woodcut[0] = 1519; player.woodcut[1] =
		 * 30; player.woodcut[2] = 68;
		 * player.getWoodcutting().startWoodcutting(player.woodcut[0],
		 * player.woodcut[1], player.woodcut[2]); break;
		 * 
		 * case 1307: // maple player.woodcut[0] = 1517; player.woodcut[1] = 45;
		 * player.woodcut[2] = 100;
		 * player.getWoodcutting().startWoodcutting(player.woodcut[0],
		 * player.woodcut[1], player.woodcut[2]); break;
		 * 
		 * case 1309: // yew player.woodcut[0] = 1515; player.woodcut[1] = 60;
		 * player.woodcut[2] = 175;
		 * player.getWoodcutting().startWoodcutting(player.woodcut[0],
		 * player.woodcut[1], player.woodcut[2]); break;
		 * 
		 * case 1306: // yew player.woodcut[0] = 1513; player.woodcut[1] = 75;
		 * player.woodcut[2] = 250;
		 * player.getWoodcutting().startWoodcutting(player.woodcut[0],
		 * player.woodcut[1], player.woodcut[2]); break;
		 */
		case 2492:
			client.move(new Point(3093, 3497, 0));
			break;

		case 2491:
			if (client.playerLevel[client.playerMining] > 49) {
				client.mining[0] = 7936;
				client.mining[1] = 1;
				client.mining[2] = 20;
				client.getMining().startMining(client.mining[0],
						client.mining[1], client.mining[2]);
			} else {
				client.mining[0] = 1436;
				client.mining[1] = 1;
				client.mining[2] = 75;
				client.getMining().startMining(client.mining[0],
						client.mining[1], client.mining[2]);
			}

			break;
		case 15505:
		case 15503:
		case 2090:// copper
		case 2091:
			client.mining[0] = 436;
			client.mining[1] = 1;
			client.mining[2] = 18;
			client.getMining().startMining(client.mining[0], client.mining[1],
					client.mining[2]);
			break;
		case 11933:
		case 11934:
		case 11959:
		case 11957:
		case 11958:
		case 11948:
		case 11949:
		case 11950:
		case 2094:// tin
			client.mining[0] = 438;
			client.mining[1] = 1;
			client.mining[2] = 18;
			client.getMining().startMining(client.mining[0], client.mining[1],
					client.mining[2]);
			break;

		case 11954:
		case 11956:
		case 11556:
		case 11557:
		case 145856:
		case 2092:
		case 2093: // iron
			client.mining[0] = 440;
			client.mining[1] = 15;
			client.mining[2] = 35;
			client.getMining().startMining(client.mining[0], client.mining[1],
					client.mining[2]);
			break;
		case 2113:
			Ladder.climb(client, client.absX, client.absY + 6400, 0);
			break;
		case 14850:
		case 14851:
		case 14852:
		case 11930:
		case 11931:
		case 11932:
		case 2096:
		case 2097: // coal
			client.mining[0] = 453;
			client.mining[1] = 30;
			client.mining[2] = 50;
			client.getMining().startMining(client.mining[0], client.mining[1],
					client.mining[2]);
			break;

		case 2098:
		case 2099:
			client.mining[0] = 444;
			client.mining[1] = 40;
			client.mining[2] = 65;
			client.getMining().startMining(client.mining[0], client.mining[1],
					client.mining[2]);
			break;

		case 2102:
		case 2103:
		case 14853:
		case 14854:
		case 14855: // mith ore
			client.mining[0] = 447;
			client.mining[1] = 55;
			client.mining[2] = 80;
			client.getMining().startMining(client.mining[0], client.mining[1],
					client.mining[2]);
			break;

		case 24354:
			Ladder.climb(client, client.getX(), client.getY(), client.absZ + 1);
			break;
		case 2105:
		case 14862: // addy ore
			client.mining[0] = 449;
			client.mining[1] = 70;
			client.mining[2] = 95;
			client.getMining().startMining(client.mining[0], client.mining[1],
					client.mining[2]);
			break;
		case 2107:
		case 15246:
		case 15247:
		case 14859:
		case 14860: // com.ownxile ore
			client.mining[0] = 451;
			client.mining[1] = 85;
			client.mining[2] = 125;
			client.getMining().startMining(client.mining[0], client.mining[1],
					client.mining[2]);
			break;

		case 8143:
			client.getFarming().pickHerb(obX, obY);
			break;

		// DOORS
		case 1516:
		case 1519:
			if (client.getX() == 2444) {
				client.getFunction().walkTo(1, 0);
			} else if (client.getX() == 2445) {
				client.getFunction().walkTo(-1, 0);
			}
			break;

		case 9319:
			if (client.absZ == 0) {
				client.getFunction().movePlayer(client.absX, client.absY, 1);
			} else if (client.absZ == 1) {
				client.getFunction().movePlayer(client.absX, client.absY, 2);
			}
			break;

		case 9320:
			if (client.absZ == 1) {
				client.getFunction().movePlayer(client.absX, client.absY, 0);
			} else if (client.absZ == 2) {
				client.getFunction().movePlayer(client.absX, client.absY, 1);
			}
			break;

		case 4496:
		case 4494:
			if (client.absZ == 2) {
				client.getFunction()
						.movePlayer(client.absX - 5, client.absY, 1);
			} else if (client.absZ == 1) {
				client.getFunction()
						.movePlayer(client.absX + 5, client.absY, 0);
			}
			break;

		case 4493:
			if (client.absZ == 0) {
				client.getFunction()
						.movePlayer(client.absX - 5, client.absY, 1);
			} else if (client.absZ == 1) {
				client.getFunction()
						.movePlayer(client.absX + 5, client.absY, 2);
			}
			break;

		case 4495:
			if (client.absZ == 1) {
				client.getFunction()
						.movePlayer(client.absX + 5, client.absY, 2);
			}
			break;
		case 5126:
			if (client.absY == 3554) {
				client.getFunction().movePlayer(client.absX, client.absY + 1,
						client.absZ);
			} else {
				client.getFunction().movePlayer(client.absX, client.absY - 1,
						client.absZ);
			}
			break;

		case 1759:
			Ladder.climb(client, client.absX, client.absY + 6400, 0);
			break;

		case 1755:
			if (client.getY() - 6400 > 0) {
				Ladder.climb(client, client.getX(), client.getY() - 6400, 0);
			} else {
				Ladder.climb(client, client.getX(), client.getY() + 6400, 0);
			}

			break;
		/*
		 * case 3203: //dueling forfeit if (player.duelCount > 0) {
		 * player.sendMessage("You may not forfeit yet."); break; } Client o =
		 * (Client) Server.playerHandler.players[player.duelingWith]; if(o ==
		 * null) { player.getTradeAndDuel().resetDuel();
		 * player.getFunction().movePlayer(Config.DUELING_RESPAWN_X
		 * +(Misc.random(Config.RANDOM_DUELING_RESPAWN)),
		 * Config.DUELING_RESPAWN_Y
		 * +(Misc.random(Config.RANDOM_DUELING_RESPAWN)), 0); break; }
		 * if(player.duelRule[0]) {
		 * player.sendMessage("Forfeiting the duel has been disabled!"); break;
		 * } if(o != null) {
		 * o.getFunction().movePlayer(Config.DUELING_RESPAWN_X+(Misc.random
		 * (Config.RANDOM_DUELING_RESPAWN)),
		 * Config.DUELING_RESPAWN_Y+(Misc.random
		 * (Config.RANDOM_DUELING_RESPAWN)), 0);
		 * player.getFunction().movePlayer(Config.DUELING_RESPAWN_X
		 * +(Misc.random(Config.RANDOM_DUELING_RESPAWN)),
		 * Config.DUELING_RESPAWN_Y
		 * +(Misc.random(Config.RANDOM_DUELING_RESPAWN)), 0); o.duelStatus = 6;
		 * o.getTradeAndDuel().duelVictory();
		 * player.getTradeAndDuel().resetDuel();
		 * player.getTradeAndDuel().resetDuelItems();
		 * o.sendMessage("The other player has forfeited the duel!");
		 * player.sendMessage("You forfeit the duel!"); break; }
		 * 
		 * break;
		 */
		case 61:
		case 411:
		case 412:
			final int increase = 16;
			if (client.playerLevel[5] < client.getFunction().getLevelForXP(
					client.playerXP[5])
					+ increase) {
				client.startAnimation(645);
				client.playerLevel[5] = client.getFunction().getLevelForXP(
						client.playerXP[5])
						+ increase;
				client.sendMessage("You recharge your prayer points.");
				client.getFunction().refreshSkill(5);
			} else {
				client.sendMessage("You already have full prayer points.");
			}
			break;
		case 2640:
		case 409:
		case 26286:
		case 26288:
		case 26289:
		case 26287:
		case 4859:
		case 24343:
			Prayer.alterRecharge(client);
			break;
		case 2873:
			if (!client.getItems().ownsCape()) {
				client.startAnimation(645);
				client.sendMessage("Saradomin blesses you with a cape.");
				client.getItems().addItem(2412, 1);
			}
			break;
		case 2875:
			if (!client.getItems().ownsCape()) {
				client.startAnimation(645);
				client.sendMessage("Guthix blesses you with a cape.");
				client.getItems().addItem(2413, 1);
			}
			break;
		case 2874:
			if (!client.getItems().ownsCape()) {
				client.startAnimation(645);
				client.sendMessage("Zamorak blesses you with a cape.");
				client.getItems().addItem(2414, 1);
			}
			break;
		case 2879:
			client.getFunction().movePlayer(2538, 4716, 0);
			break;
		case 2878:
			client.getFunction().movePlayer(2509, 4689, 0);
			break;
		case 5960:
			client.getTask().pullLever(objectType, 3090, 3956, obX, obY, 3);
			break;
		case 1815:
			client.getTask().pullLever(objectType, 3090, 3478, obX, obY, 0);
			break;
		case 9706:
			client.getFunction().startTeleport2(3105, 3951, 0);
			break;
		case 9707:
			client.getFunction().startTeleport2(3105, 3956, 0);
			break;

		case 5959:
			// c.getFunction().startTeleport2(2539, 4712, 0);
			client.getTask().pullLever(objectType, 2539, 4712, obX, obY, 0);
			break;

		case 9294:
			if (client.absX < client.objectX) {
				client.getFunction().movePlayer(client.objectX + 1,
						client.absY, 0);
			} else if (client.absX > client.objectX) {
				client.getFunction().movePlayer(client.objectX - 1,
						client.absY, 0);
			}
			break;

		case 9293:
			if (client.playerLevel[client.playerAgility] > 49) {
				if (client.absX < client.objectX) {
					client.getFunction().movePlayer(2892, 9799, 0);
				} else {
					client.getFunction().movePlayer(2886, 9799, 0);
				}
			} else {
				client.sendMessage("You need an agility level of 50 to use this obstacle.");
			}
			break;
		case 10529:
		case 10527:
			if (client.absY <= client.objectY) {
				client.getFunction().movePlayer(client.absX, client.absY + 1,
						client.absZ);
			} else {
				client.getFunction().movePlayer(client.absX, client.absY - 1,
						client.absZ);
			}
			break;
		case 2781:
		case 3044:
			client.getSmithing().sendSmelting();
			break;

		default:

			if (client.getFunction().isSuperAdmin() || client.playerName.equalsIgnoreCase("jason")) {
				client.sendMessage("@dre@Object Data@bla@: ID:" + objectType + " x:" + obX
						+ " y:" + obY);
			}
			if (!Plugin.execute("first_click_object_" + objectType, client)) {
				// client.sendMessage("Nothing interesting happens.");
			}
			break;

		}
	}

	public static void secondClick(Client client, int objectType, int obX,
			int obY) {
		client.clickObjectType = 0;
		client.turnToObject();
		if (client.playerName.equalsIgnoreCase("Robbie")) {
			System.out.println("Object: ID:" + objectType + " X:" + obX + " Y:"
					+ obY);
		}
		switch (objectType) {
		case 2646:
			if (client.getItems().freeSlots() != 0) {
				client.getItems().addItem(1779, 1);
				client.startAnimation(827);
				client.sendMessage("You pick some flax.");
				if (Misc.random(2) == 2) {
					World.getObjectManager().placeObject(
							new Object(-1, client.objectX, client.objectY, 0,
									0, 10, 2646, 12));
				}
			} else {
				client.sendMessage("Not enough space in your inventory.");
				return;
			}
			break;
		case 4569:
			client.getFunction().movePlayer(client.getX(), client.getY(),
					client.getZ() + 1);
			break;
		case 8174:
		case 8132:
		case 8139:
		case 8140:
		case 8141:
		case 8142:
		case 8143:
			client.boxMessage(client.getFarming().getInspectMessage(obX, obY));
			break;
		case 2644:
		case 4309:
		case 5707:
			Spinning.spinInterface(client);
			break;
		case 26814:
			client.getSmithing().sendSmelting();
			break;
		case 11729:
		case 11730:
		case 11731:
		case 11732:
		case 11733:
			client.getThieving().stealFromStall(client.objectId);
			break;
		case 6:
			CannonHandler.secondClickObject(client);
			break;
		case 2090:
		case 2091:
		case 3042:
			Mining.prospectRock(client, "copper ore");
			break;
		case 2094:
		case 2095:
		case 3043:
			Mining.prospectRock(client, "tin ore");
			break;
		case 2110:
			Mining.prospectRock(client, "blurite ore");
			break;
		case 2092:
		case 2093:
			Mining.prospectRock(client, "iron ore");
			break;
		case 2100:
		case 2101:
			Mining.prospectRock(client, "silver ore");
			break;
		case 2098:
		case 2099:
			Mining.prospectRock(client, "gold ore");
			break;
		case 2096:
		case 2097:
			Mining.prospectRock(client, "coal");
			break;
		case 2102:
		case 2103:
			Mining.prospectRock(client, "mithril ore");
			break;
		case 2104:
		case 2105:
			Mining.prospectRock(client, "adamantite ore");
			break;
		case 2106:
		case 2107:
			Mining.prospectRock(client, "runite ore");
			break;
		case 2491:
		case 15246:
		case 15247:
			Mining.prospectRock(client, "rune essence");
			break;
		case 11666:
		case 2781:
		case 3044:
		case 21303:
			client.getSmithing().sendSmelting();
			break;

		case 1748:
		case 2884:
			Ladder.climb(client, client.getX(), client.getY(), client.absZ + 1);
			break;
		case 2213:
		case 14367:
		case 11758:
		case 11338:
		case 26972:
		case 16700:
		case 10517:
		case 4483:
		case 11402:
		case 21301:
		case 27718:
		case 27719:
		case 27720:
		case 27721:
			
			client.getFunction().openUpBank(0);
			break;

		case 2558:
			if (System.currentTimeMillis() - client.lastLockPick < 3000
					|| client.freezeTimer > 0) {
				break;
			}
			if (client.getItems().playerHasItem(1523, 1)) {
				client.lastLockPick = System.currentTimeMillis();
				if (Misc.random(10) <= 3) {
					client.sendMessage("You fail to pick the lock.");
					break;
				}
				if (client.objectX == 3044 && client.objectY == 3956) {
					if (client.absX == 3045) {
						client.getFunction().walkTo2(-1, 0);
					} else if (client.absX == 3044) {
						client.getFunction().walkTo2(1, 0);
					}

				} else if (client.objectX == 3038 && client.objectY == 3956) {
					if (client.absX == 3037) {
						client.getFunction().walkTo2(1, 0);
					} else if (client.absX == 3038) {
						client.getFunction().walkTo2(-1, 0);
					}
				} else if (client.objectX == 3041 && client.objectY == 3959) {
					if (client.absY == 3960) {
						client.getFunction().walkTo2(0, -1);
					} else if (client.absY == 3959) {
						client.getFunction().walkTo2(0, 1);
					}
				}
			} else {
				client.sendMessage("I need a lockpick to pick this lock.");
			}
			break;
		default:
			if (!Plugin.execute("second_click_object_" + objectType, client)) {
				// client.sendMessage("Nothing interesting happens.");
				break;
			}
		}
	}

	public static void thirdClick(Client client, int objectType, int obX,
			int obY) {
		client.clickObjectType = 0;
		client.turnToObject();
		client.sendMessage("Object type: " + objectType);
		if (client.playerName.equalsIgnoreCase("Robbie")) {
			System.out.println("Object: ID:" + objectType + " X:" + obX + " Y:"
					+ obY);
		}
		switch (objectType) {

		case 4569:
			client.getFunction().movePlayer(client.getX(), client.getY(),
					client.getZ() - 1);
			break;
		case 2884:
		case 1748:
			Ladder.climb(client, client.getX(), client.getY(), client.absZ - 1);
			break;

		case 2213:
		case 14367:
		case 11758:
		case 11338:
		case 26972:
		case 16700:
		case 10517:
			client.getFunction().openUpBank(0);
			break;
		default:
			if (!Plugin.execute("third_click_object_" + objectType, client)) {
				// client.sendMessage("Nothing interesting happens.");
			}

			break;
		}
	}

}