package com.ownxile.rs2.packets.clicking;

import com.ownxile.core.World;
import com.ownxile.rs2.content.click.ObjectClickReaction;
import com.ownxile.rs2.packets.Packet;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.skills.agility.Agility;
import com.ownxile.rs2.skills.runecrafting.Runecrafting;
import com.ownxile.rs2.world.games.CastleWars;

public class ClickObject implements Packet {

	public static final int FIRST_CLICK = 132, SECOND_CLICK = 252,
			THIRD_CLICK = 70;

	@Override
	public void processPacket(final Client player, final int packetType,
			int packetSize) {
		player.clickObjectType = player.objectX = player.objectId = player.objectY = 0;
		player.objectYOffset = player.objectXOffset = 0;
		player.getFunction().resetFollow();
		player.faceUpdate(0);
		if (System.currentTimeMillis() - player.lastClick < 600) {
			return;
		}
		player.lastClick = System.currentTimeMillis();
		if (player.isDead) {
			return;
		}
		switch (packetType) {
		case FIRST_CLICK:
			player.objectX = player.getInStream().readSignedWordBigEndianA();
			player.objectId = player.getInStream().readUnsignedWord();
			player.objectY = player.getInStream().readUnsignedWordA();
			player.objectDistance = 1;
			if (Math.abs(player.getX() - player.objectX) > 25
					|| Math.abs(player.getY() - player.objectY) > 25) {
				player.resetWalkingQueue();
				break;
			}
			for (int element : Runecrafting.altarID) {
				if (player.objectId == element) {
					if (Runecrafting.craftRunes(player, player.objectId)) {
						return;
					}
				}
			}
			if (Agility.checkObstacle(player)) {
				return;
			}
			switch (player.objectId) {
			case 6461:
				int height = 6 + (player.getId() * 4);
				player.getFunction().movePlayer(2851, 3809, height);
				World.getNpcHandler().spawnNpc(player, 795, 2845, 3809, height,
						1, 150, 20, 200, 200, true, true);
				return;
			case 1733:
				player.objectYOffset = 2;
				break;
			case 2781:
			case 3044:
			case 2617:
				player.objectYOffset = 5;
				break;
			case 12165:
				player.objectYOffset = 4;
				break;
				
			case 11729:
			case 11730:
			case 11731:
			case 11732:
			case 11733:
				player.getThieving().stealFromStall(player.objectId);
				break;

			/*case 24875:
				player.getThieving().stealFromStall(player.objectId);**/
				
			case 4615:
			case 4616:
			case 3192:
				player.objectDistance = 2;
				break;
			case 4387:
				if (player.getX() == 2437 || player.getX() == 2438) {
					CastleWars.addToWaitRoom(player, 1); // saradomin
				}
				break;
			case 4388:
				if (player.getX() == 2437 || player.getX() == 2438) {
					CastleWars.addToWaitRoom(player, 2); // zamorak
				}
				break;
			case 4408:
				if (player.getX() == 2437 || player.getX() == 2438) {
					CastleWars.addToWaitRoom(player, 3); // guthix
				}
				break;

			case 4389: // sara
			case 4390: // zammy waiting room portal
				CastleWars.leaveWaitingRoom(player);
				break;

			case 1568:
				player.getFunction().movePlayer(player.absX,
						player.getY() + 6400, 0);
				break;
			case 4419:
				if (player.getX() == 2417 && player.getY() == 3077) {
					player.getFunction().movePlayer(2416, 3074, 0);
				}
				if (player.getX() == 2416 && player.getY() == 3074) {
					player.getFunction().movePlayer(2417, 3077, 0);
				}
				break;

			case 4465:
				if (player.getY() == 3073) {
					if (player.getX() == 2415) {
						player.getFunction()
								.movePlayer(2414, 3073, player.absZ);
					} else if (player.getX() == 2414) {
						player.getFunction()
								.movePlayer(2415, 3073, player.absZ);
					}
				}
				break;
			case 2491:
			case 3998:
			case 3999:
			case 3881:
				player.objectDistance = 5;
				break;
			case 1740:
			case 1739:
			case 1738:
				player.objectDistance = 2;
				player.objectYOffset = 1;
				break;
			case 245:
				player.objectYOffset = -1;
				player.objectDistance = 0;
				break;
			case 4625:
				player.objectYOffset = 2;
				break;

			case 272:
				player.objectYOffset = 1;
				player.objectDistance = 0;
				break;

			case 273:
				player.objectYOffset = 1;
				player.objectDistance = 0;
				break;

			case 246:
				player.objectYOffset = 1;
				player.objectDistance = 0;
				break;

			case 4493:
			case 4494:
			case 4496:
			case 4495:
				player.objectDistance = 5;
				break;
			case 10229:
			case 6522:
			case 6:
				player.objectDistance = 2;
				break;
			case 8959:
				player.objectYOffset = 1;
				break;
			case 4417:
				if (player.objectX == 2425 && player.objectY == 3074) {
					player.objectYOffset = 2;
				}
				break;
			case 4420:
				if (player.getX() >= 2383 && player.getX() <= 2385) {
					player.objectYOffset = 1;
				} else {
					player.objectYOffset = -2;
				}
			case 6552:
			case 409:
			case 17955:
				player.objectDistance = 4;
				break;
			case 2879:
			case 2878:
				player.objectDistance = 3;
				break;
			case 4568:
			case 4569:
			case 4570:
				player.objectDistance = 2;
				break;
			case 2558:
				player.objectDistance = 0;
				if (player.absX > player.objectX && player.objectX == 3044) {
					player.objectXOffset = 1;
				}
				if (player.absY > player.objectY) {
					player.objectYOffset = 1;
				}
				if (player.absX < player.objectX && player.objectX == 3038) {
					player.objectXOffset = -1;
				}
				break;
			case 9356:
				player.objectDistance = 2;
				break;
			case 5959:
			case 1815:
			case 5960:
			case 1816:
				player.objectDistance = 0;
				break;

			case 9293:
				player.objectDistance = 2;
				break;
			case 4418:
				if (player.objectX == 2374 && player.objectY == 3131) {
					player.objectYOffset = -2;
				} else if (player.objectX == 2369 && player.objectY == 3126) {
					player.objectXOffset = 2;
				} else if (player.objectX == 2380 && player.objectY == 3127) {
					player.objectYOffset = 2;
				} else if (player.objectX == 2369 && player.objectY == 3126) {
					player.objectXOffset = 2;
				} else if (player.objectX == 2374 && player.objectY == 3131) {
					player.objectYOffset = -2;
				}
				break;
			case 9706:
				player.objectDistance = 0;
				player.objectXOffset = 1;
				break;
			case 9707:
				player.objectDistance = 0;
				player.objectYOffset = -1;
				break;
			case 6707: // verac
				player.objectYOffset = 3;
				break;
			case 6823:
				player.objectDistance = 2;
				player.objectYOffset = 1;
				break;

			case 6706: // torag
				player.objectXOffset = 2;
				break;
			case 6772:
				player.objectDistance = 2;
				player.objectYOffset = 1;
				break;

			case 6705: // karils
				player.objectYOffset = -1;
				break;
			case 6822:
				player.objectDistance = 2;
				player.objectYOffset = 1;
				break;

			case 6704: // guthan stairs
				player.objectYOffset = -1;
				break;
			case 6773:
				player.objectDistance = 2;
				player.objectXOffset = 1;
				player.objectYOffset = 1;
				break;

			case 6703: // dharok stairs
				player.objectXOffset = -1;
				break;
			case 6771:
				player.objectDistance = 2;
				player.objectXOffset = 1;
				player.objectYOffset = 1;
				break;

			case 6702: // ahrim stairs
				player.objectXOffset = -1;
				break;
			case 6821:
				player.objectDistance = 2;
				player.objectXOffset = 1;
				player.objectYOffset = 1;
				break;
			case 1276:
			case 1278:// trees
			case 1281: // oak
			case 1308: // willow
			case 1307: // maple
			case 1309: // yew
			case 1306: // yew
				player.objectDistance = 3;
				break;
			default:
				player.objectDistance = 1;
				player.objectXOffset = 0;
				player.objectYOffset = 0;
				break;
			}
			if (player.goodDistance(player.objectX + player.objectXOffset,
					player.objectY + player.objectYOffset, player.getX(),
					player.getY(), player.objectDistance)) {
				player.turnToObject();
				ObjectClickReaction.firstClick(player, player.objectId,
						player.objectX, player.objectY);
			} else {
				player.clickObjectType = 1;
			}
			break;

		case SECOND_CLICK:
			player.objectId = player.getInStream().readUnsignedWordBigEndianA();
			player.objectY = player.getInStream().readSignedWordBigEndian();
			player.objectX = player.getInStream().readUnsignedWordA();
			player.objectDistance = 1;
			switch (player.objectId) {
			case 6163:
			case 6165:
			case 6166:
			case 6164:
			case 6162:
				player.objectDistance = 2;
				break;

			case 2491:
				player.objectDistance = 5;
				break;
			case 6:
				player.objectDistance = 2;
				break;
			case 4569:
				player.objectDistance = 2;
				break;
			case 7053:
			case 2560:
			case 2561:
			case 2562:
			case 2563:
			case 2564:
				player.objectDistance = 2;
				player.objectYOffset = 1;
				break;
			default:
				player.objectDistance = 1;
				player.objectXOffset = 0;
				player.objectYOffset = 0;
				break;
			}
			if (player.goodDistance(player.objectX + player.objectXOffset,
					player.objectY + player.objectYOffset, player.getX(),
					player.getY(), player.objectDistance)) {
				player.turnToObject();
				ObjectClickReaction.secondClick(player, player.objectId,
						player.objectX, player.objectY);
			} else {
				player.clickObjectType = 2;
			}
			break;

		case THIRD_CLICK:
			player.objectX = player.getInStream().readSignedWordBigEndian();
			player.objectY = player.getInStream().readUnsignedWord();
			player.objectId = player.getInStream().readUnsignedWordBigEndianA();

			switch (player.objectId) {
			case 4569:
				player.objectDistance = 2;
				break;
			default:
				player.objectDistance = 1;
				player.objectXOffset = 0;
				player.objectYOffset = 0;
				break;
			}
			if (player.goodDistance(player.objectX + player.objectXOffset,
					player.objectY + player.objectYOffset, player.getX(),
					player.getY(), player.objectDistance)) {
				player.turnToObject();
				ObjectClickReaction.thirdClick(player, player.objectId,
						player.objectX, player.objectY);
			} else {
				player.clickObjectType = 3;
			}
			break;
		}

	}

}
