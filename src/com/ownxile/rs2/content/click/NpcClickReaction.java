package com.ownxile.rs2.content.click;

import com.ownxile.core.Plugin;
import com.ownxile.core.World;
import com.ownxile.rs2.npcs.PetHandler;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.skills.fishing.Fishing;
import com.ownxile.rs2.world.shops.Shopping;
import com.ownxile.rs2.world.transport.TeleportInterface;

public class NpcClickReaction {

	public static final int FIRST_CLICK = 0, SECOND_CLICK = 1, THIRD_CLICK = 2;

	public static void executeClick(final Client c, final int clickType,
			final int entityId) {

		c.npcClickIndex = 0;
		if (c.lastClickedNpcId != entityId) {// means a new npc has been
												// clicked
			c.lastClickedNpcId = entityId;
			c.resetDialogueOptions();
		}
		switch (clickType) {
		case FIRST_CLICK:
			switch (entityId) {

			case 4000:// bosspets
			case 4001:
			case 4002:
			case 4003:
			case 4004:
			case 4005:
			case 4006:
			case 4007:
			case 4008:
			case 2553:
			case 4009:
			case 4022:
			case 4021:
			case 4020:
			case 4019:
			case 4018:
			case 4017:
			case 4016:
			case 4015:
			case 4014:
			case 4013:
			case 4012:
			case 4011:
			case 4010:
			case 4121:
			case 4122:
			case 4123:
			case 4124:
			case 4125:
			case 4126:
			case 4127:
			case 4128:
			case 4129:
				c.playerChat("I don't think it wants to be picked up.");
				break;

			case 309:
			case 312:
			case 313:
			case 316:
			case 326:
			case 327:
				Fishing.setupFishing2(c, Fishing.forSpot(entityId, false));
				break;
			}
			if (!Plugin.execute("first_click_npc_" + entityId, c)) {
				firstClick(c, entityId);
			}
			break;
		case SECOND_CLICK:
			if (!Plugin.execute("second_click_npc_" + entityId, c)) {
				secondClick(c, entityId);
			}
			break;
		case THIRD_CLICK:
			if (!Plugin.execute("third_click_npc_" + entityId, c)) {
				thirdClick(c, entityId);
			}
			break;
		}
	}

	public static void firstClick(Client client, int npcType) {
		client.clickNpcType = 0;
		switch (npcType) {
		case 4398:
			TeleportInterface.openTraining(client);
			break;
		case 3809:
		case 3810:
		case 3811:
		case 3812:
			client.startChat(1258831998);
			break;
		case 1304:
			client.getChat().sendChat(100, npcType);
			break;
		case 1661:
			client.getChat().sendChat(79, npcType);
			break;
		case 3021:// Leprechaun
			Shopping.openShop(client, 20);
			break;
		case 1526:// 1526
			client.getChat().sendChat(1055, npcType);
			break;
		case 3805:// Postie Pete
			client.getChat().sendChat(1015, npcType);
			break;
		case 945:// RSGuide
			if (client.starter == 0) {
				client.getChat().sendChat(830, npcType);
			}
			if (client.starter == 1) {
				client.getChat().sendChat(840, npcType);
			}
			break;
		case 308:// Fisherman
			Shopping.openShop(client, 22);
			break;
		case 919:
			Shopping.openShop(client, 10);
			break;
		case 541:
			Shopping.openShop(client, 5);
			break;

		case 461:
			Shopping.openShop(client, 2);
			break;

		case 683:
			Shopping.openShop(client, 3);
			break;

		case 549:
			Shopping.openShop(client, 4);
			break;

		case 2538:
			Shopping.openShop(client, 6);
			break;

		case 519:
			Shopping.openShop(client, 8);
			break;
		case 1282:
			Shopping.openShop(client, 7);
			break;
		case 1152:
			client.getChat().sendChat(16, npcType);
			break;
		case 494:
		case 499:
		case 496:
		case 497:
		case 5383:
			client.startChat(1009305985);
			break;
		case 3789:

			client.getChat().sendChat(505, npcType);
			break;
		case 3788:
			// client.getShop().openVoid();
			break;
		case 905:
			client.getChat().sendChat(5, npcType);
			break;
		case 1658:
			client.getChat().sendChat(1960, npcType);
			break;
		case 460:
			client.getChat().sendChat(3, npcType);
			break;
		case 462:
			client.getChat().sendChat(7, npcType);
			break;
		case 1052:
			client.getChat().sendChat(69, npcType);
			break;
		case 522:
		case 523:
			Shopping.openShop(client, 1);
			break;
		case 843:
			client.getChat().sendChat(20, npcType);
			break;
		case 3117:
			client.getChat().sendChat(426, npcType);
			break;
		case 3793:
			client.getChat().sendChat(435, npcType);
			break;
		case 162:
			client.getChat().sendChat(440, npcType);
			break;
		case 668:
			client.getChat().sendChat(1039, npcType);
			break;
		default:
			// client.sendMessage("Nothing interesting happens.");
			if (client.playerRights == 3) {
				client.boxMessage("First Click Npc : " + npcType + " ("
						+ World.getNpcHandler().getNPCName(npcType) + ")");
			}
			break;
		}
	}

	public static void secondClick(Client client, int npcType) {
		client.clickNpcType = 0;
		if (PetHandler.pickupPet(client, npcType)) {
			return;
		}
		if (client.getThieving().stealFromNPC(npcType)) {
			return;
		}
		switch (npcType) {

		case 4000:// bosspets
		case 4001:
		case 4002:
		case 4003:
		case 4004:
		case 4005:
		case 4006:
		case 4007:
		case 4008:
		case 2553:
		case 4009:
		case 4022:
		case 4021:
		case 4020:
		case 4019:
		case 4018:
		case 4017:
		case 4016:
		case 4015:
		case 4014:
		case 4013:
		case 4012:
		case 4011:
		case 4010:
		case 4121:
		case 4122:
		case 4123:
		case 4124:
		case 4125:
		case 4126:
		case 4127:
		case 4128:
		case 4129:
			PetHandler.interactBossPet(client);
			break;
		case 308:// Fisherman
			Shopping.openShop(client, 22);
			break;
		case 309:
		case 312:
		case 313:
		case 316:
		case 326:
			Fishing.setupFishing2(client, Fishing.forSpot(npcType, true));
			break;
		case 3809:
		case 3810:
		case 3811:
		case 3812:
			client.getFunction().showInterface(802);
			break;
		case 3506:
		case 3507:
		case 761:
		case 760:
		case 762:
		case 763:
		case 764:
		case 765:
		case 766:
		case 767:
		case 768:
		case 769:
		case 770:
		case 771:
		case 772:
		case 773:
		case 3505:
			// if (player.xmas == 4) {
			// player.getDH().sendDialogues(390, npcType);
			// } else if (player.xmas != 4) {
			client.getChat().sendChat(354, npcType);
			// }
			break;
		case 3021:// Leprechaun
			Shopping.openShop(client, 20);
			break;
		case 1282:
			Shopping.openShop(client, 7);
			break;
		case 3788:
			// client.getShop().openVoid();
			break;

		case 494:
		case 4519:
		case 5383:
		case 499:
		case 496:
		case 497:
			client.getFunction().openUpBank(0);
			break;
		case 522:
		case 523:
			Shopping.openShop(client, 1);
			break;
		case 541:
			Shopping.openShop(client, 5);
			break;

		case 461:
			Shopping.openShop(client, 2);
			break;

		case 683:
			Shopping.openShop(client, 3);
			break;

		case 549:
			Shopping.openShop(client, 4);
			break;

		case 2538:
			Shopping.openShop(client, 6);
			break;

		case 519:
			Shopping.openShop(client, 8);
			break;
		case 3789:
			Shopping.openShop(client, 18);
			break;
		default:
			// client.sendMessage("Nothing interesting happens.");
			if (client.playerRights == 3) {
				client.boxMessage("Second Click Npc : " + npcType);
			}
			break;

		}
	}

	public static void thirdClick(Client client, int npcType) {
		client.clickNpcType = 0;
		if (PetHandler.isPetNpc(npcType)) {
			PetHandler.interactWith(client, npcType);
			return;
		}
		switch (npcType) {
		case 1599:// slayer
			Shopping.openShop(client, 11);
			break;

		case 1526:// CW shop
			Shopping.openShop(client, 38);
			break;
		default:
			// client.sendMessage("Nothing interesting happens.");
			if (client.playerRights == 3) {
				client.boxMessage("Third Click NPC : " + npcType);
			}
			break;

		}
	}

}
