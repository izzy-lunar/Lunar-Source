package com.ownxile.rs2.content;

import com.ownxile.core.Plugin;
import com.ownxile.rs2.npcs.PetHandler;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.world.games.Gambling;
import com.ownxile.rs2.world.shops.Shopping;
import com.ownxile.util.Misc;

public class ChatDialogues {

	private final Client client;
	private int msg = 0;

	public ChatDialogues(Client c) {
		client = c;
	}

	public void peteDialogue(int msg) {
		switch (msg) {
		case 0:
			sendNpcChat1("*Postie Pete Yawns*", 3805, "Postie Pete");
			client.nextChat = 1016;
			break;
		case 1:
			sendNpcChat1("Come to play again have you?", 3805, "Postie Pete");
			client.nextChat = 1019;
			break;
		case 2:
			sendNpcChat1("Back AGAIN?", 3805, "Postie Pete");
			client.nextChat = 1019;
			break;
		case 3:
			sendNpcChat1("You sure do like to gamble.", 3805, "Postie Pete");
			client.nextChat = 1019;
			break;
		case 4:
			sendNpcChat1("I haven't seen a gambler like you in a while!", 3805,
					"Postie Pete");
			client.nextChat = 1019;
			break;
		case 5:
			sendNpcChat1("Your resilience is impressive.", 3805, "Postie Pete");
			client.nextChat = 1019;
			break;
		case 6:
			sendNpcChat1("You just keep coming don't you?", 3805, "Postie Pete");
			client.nextChat = 1019;
			break;
		default:
			sendNpcChat1("Come to play again have you?", 3805, "Postie Pete");
			client.nextChat = 1019;
			msg = 1;
			break;
		}
	}

	/*
	 * Information Box
	 */

	public void sendChat(int dialogue, int npcId) {
		client.talkingNpc = npcId;
		switch (dialogue) {
		case 0:
			client.talkingNpc = -1;
			client.getFunction().removeAllWindows();
			client.nextChat = 0;
			break;
		case 1:
			sendStatement("You found a hidden tunnel! Do you want to enter it?");
			client.dialogueAction = 1;
			client.nextChat = 2;
			break;
		case 2:
			sendOption2("Yea! I'm fearless!", "No way! That looks scary!");
			client.dialogueAction = 1;
			client.nextChat = 0;
			client.resetDialogueOptions();
			break;
		case 5:
			sendNpcChat4("Hello adventurer...",
					"My name is Kolodion, the master of this mage bank.",
					"Would you like to play a minigame in order ",
					"to earn points towards recieving magic related prizes?",
					client.talkingNpc, "Kolodion");
			client.nextChat = 6;
			break;
		case 6:
			sendNpcChat4("The way the game works is as follows...",
					"You will be teleported to the wilderness,",
					"You must kill mages to recieve points,",
					"redeem points with the chamber guardian.",
					client.talkingNpc, "Kolodion");
			client.nextChat = 15;
			break;
		case 15:
			sendOption2("Yes I would like to play",
					"No, sounds too dangerous for me.");
			client.dialogueAction = 7;
			client.resetDialogueOptions();
			break;
		case 16:
			sendOption2("I would like to reset my barrows brothers.",
					"I would like to fix all my barrows");
			client.dialogueAction = 8;
			client.resetDialogueOptions();
			break;
		case 17:
			sendOption5("Air", "Mind", "Water", "Earth", "More");
			client.dialogueAction = 10;
			client.dialogueId = 17;
			client.teleAction = -1;
			client.resetDialogueOptions();
			break;
		case 18:
			sendOption5("Fire", "Body", "Cosmic", "Astral", "More");
			client.dialogueAction = 11;
			client.dialogueId = 18;
			client.teleAction = -1;
			client.resetDialogueOptions();
			break;
		case 19:
			sendOption5("Nature", "Law", "Death", "Blood", "More");
			client.dialogueAction = 12;
			client.dialogueId = 19;
			client.teleAction = -1;
			client.resetDialogueOptions();
			break;
		case 55:
			client.boxMessage("You find a casket!");
			break;
		case 56:
			client.boxMessage("You find another clue scroll!");
			break;
		case 57:
			sendPlayerChat1("Looks like another clue!");
			break;
		case 58:
			sendPlayerChat1("No thanks.");
			client.endChat();
			break;
		case 79:
			sendNpcChat2("Welcome to our Agility course friend,",
					"how may I help you?", client.talkingNpc, "Werewolf");
			client.nextChat = 80;
			break;
		case 80:
			sendOption4("How many points do I have?",
					"Can I view your shop please",
					"I wish to exchange my Agility points for OX points",
					"Never mind");
			client.dialogueAction = 80;
			client.resetDialogueOptions();
			break;
		case 81:
			sendNpcChat1("You have " + client.agilityPoints
					+ " agility points.", client.talkingNpc, "Werewolf");
			client.nextChat = 80;
			break;
		case 82:
			Shopping.openShop(client, 28);
			client.nextChat = 0;
			break;
		case 83:
			sendPlayerChat1("Never mind.");
			client.nextChat = 0;
			break;
		case 84:
			if (client.agilityPoints >= 10) {
				sendNpcChat2("You can exchange " + client.agilityPoints
						+ " agility points", "in return for "
						+ client.agilityPoints / 5 + " OXP.",
						client.talkingNpc, "Werewolf");
				client.nextChat = 85;
			} else {
				sendNpcChat1("You need at least 10 agility points to do this.",
						client.talkingNpc, "Werewolf");
			}
			break;
		case 85:
			sendOption2("Buy " + client.agilityPoints / 5 + " OX Points",
					"No thanks");
			client.dialogueAction = 877;
			client.nextChat = 0;
			client.resetDialogueOptions();
			break;
		case 86:
			client.dialogueQuestion("Shoo pet?", "Yes it's a nuisance!", 87,
					"No I want to keep my pet.", 0);
			break;
		case 87:
			PetHandler.killPet(client, true);
			client.nextChat = 0;
			break;

		case 505:
			sendPlayerChat1("I currently have " + client.getPoints()
					+ " points.");
			client.nextChat = 0;
			break;
		case 1015:
			peteDialogue(msg);
			msg++;
			break;
		case 1016:
			sendNpcChat2("Hello, my name is Postie Pete.",
					"I run the OwnXile Lottery.", 3805, "Postie Pete");
			client.nextChat = 1017;
			break;
		case 1017:
			sendPlayerChat1("Hey im " + client.playerName
					+ ", it's nice to meet you.");
			client.nextChat = 1019;
			break;
		case 1019:
			sendNpcChat4("The current Jackpot is @blu@"
					+ Gambling.getInstance().getPot(),
					"The last winner was @blu@"
							+ Gambling.getInstance().getLastWinner(),
					"A chance of winning costs @blu@10M",
					"Would you like to play? ", 3805, "Postie Pete");
			client.nextChat = 1020;
			break;
		case 1020:
			sendOption2("Yes", "No");
			client.dialogueAction = 889;
			client.nextChat = 1021;
			client.resetDialogueOptions();
			break;
		case 1021:
			sendPlayerChat1("Sure!");
			client.nextChat = 1022;
			break;
		case 1022:
			if (Misc.random(10) == 1) {
				Gambling.getInstance().winPot(client);
			} else {
				Gambling.getInstance().lose(client);
			}
			break;
		case 1055:
			sendNpcChat1("Sup.", client.talkingNpc, "Lanthus");
			client.nextChat = 0;
			break;
		case 1200:
			client.getChat().sendOption5("Lord", "Wunderkid", "Big Cheese",
					"King", "None");
			client.dialogueAction = 35;
			break;
		case 500345346:
			client.getFunction().movePlayer(client.getX(), client.getY(),
					client.getZ() + 1);
			break;
		case 500345345:
			client.getFunction().movePlayer(client.getX(), client.getY(),
					client.getZ() - 1);
			break;
		default:
			Plugin.execute("chat_" + dialogue, client);
			break;
		}
	}

	/*
	 * Item Dialogues
	 */

	public void sendItem1(String one, String s, int item) {
		client.getFunction().sendFrame246(6210, 200, item);
		client.getFunction().sendFrame126(one, 6207);
		client.getFunction().sendFrame126(s, 6208);
		client.getFunction().sendFrame164(6206);
	}

	/*
	 * Options
	 */

	public void sendNpcChat1(String s, int ChatNpc, String name) {
		client.getFunction().sendFrame200(4883, 588);
		client.getFunction().sendFrame126(name, 4884);
		client.getFunction().sendFrame126(s, 4885);
		client.getFunction().sendFrame75(ChatNpc, 4883);
		client.getFunction().sendFrame164(4882);
	}

	public void sendNpcChat2(String s, String s1, int ChatNpc, String name) {
		client.getFunction().sendFrame200(4888, 589);
		client.getFunction().sendFrame126(name, 4889);
		client.getFunction().sendFrame126(s, 4890);
		client.getFunction().sendFrame126(s1, 4891);
		client.getFunction().sendFrame75(ChatNpc, 4888);
		client.getFunction().sendFrame164(4887);
	}

	public void sendNpcChat3(String s, String s1, String s2, int ChatNpc,
			String name) {
		client.getFunction().sendFrame200(4894, 590);
		client.getFunction().sendFrame126(name, 4895);
		client.getFunction().sendFrame126(s, 4896);
		client.getFunction().sendFrame126(s1, 4897);
		client.getFunction().sendFrame126(s2, 4898);
		client.getFunction().sendFrame75(ChatNpc, 4894);
		client.getFunction().sendFrame164(4893);
	}

	public void sendNpcChat4(String s, String s1, String s2, String s3,
			int ChatNpc, String name) {
		client.getFunction().sendFrame200(4901, 588);
		client.getFunction().sendFrame126(name, 4902);
		client.getFunction().sendFrame126(s, 4903);
		client.getFunction().sendFrame126(s1, 4904);
		client.getFunction().sendFrame126(s2, 4905);
		client.getFunction().sendFrame126(s3, 4906);
		client.getFunction().sendFrame75(ChatNpc, 4901);
		client.getFunction().sendFrame164(4900);
	}

	public void sendNpcChatAnim(String s, int ChatNpc, String name, int emote) {
		client.getFunction().sendFrame200(4883, emote);
		client.getFunction().sendFrame126(name, 4884);
		client.getFunction().sendFrame126(s, 4885);
		client.getFunction().sendFrame75(ChatNpc, 4883);
		client.getFunction().sendFrame164(4882);
	}

	public void sendOption(String s, String s1) {
		client.getFunction().sendFrame126("Select an Option", 2470);
		client.getFunction().sendFrame126(s, 2471);
		client.getFunction().sendFrame126(s1, 2472);
		client.getFunction().sendFrame126("Click here to continue", 2473);
		client.getFunction().sendFrame164(13758);
	}

	/*
	 * Statements
	 */

	public void sendOption2(String s, String s1) {
		client.getFunction().sendFrame126("Select an Option", 2460);
		client.getFunction().sendFrame126(s, 2461);
		client.getFunction().sendFrame126(s1, 2462);
		client.getFunction().sendFrame164(2459);
	}

	/*
	 * Npc Chatting
	 */

	public void sendOption3(String s, String s1, String s2) {
		client.getFunction().sendFrame126("Select an Option", 2470);
		client.getFunction().sendFrame126(s, 2471);
		client.getFunction().sendFrame126(s1, 2472);
		client.getFunction().sendFrame126(s2, 2473);
		client.getFunction().sendFrame164(2469);
	}

	public void sendOption4(String s, String s1, String s2, String s3) {
		client.getFunction().sendFrame126("Select an Option", 2481);
		client.getFunction().sendFrame126(s, 2482);
		client.getFunction().sendFrame126(s1, 2483);
		client.getFunction().sendFrame126(s2, 2484);
		client.getFunction().sendFrame126(s3, 2485);
		client.getFunction().sendFrame164(2480);
	}

	public void sendOption5(String s, String s1, String s2, String s3, String s4) {
		client.getFunction().sendFrame126("Select an Option", 2493);
		client.getFunction().sendFrame126(s, 2494);
		client.getFunction().sendFrame126(s1, 2495);
		client.getFunction().sendFrame126(s2, 2496);
		client.getFunction().sendFrame126(s3, 2497);
		client.getFunction().sendFrame126(s4, 2498);
		client.getFunction().sendFrame164(2492);
	}

	public void sendPlayerChat1(String s) {
		client.getFunction().sendFrame200(969, 588);
		client.getFunction().sendFrame126(client.playerName, 970);
		client.getFunction().sendFrame126(s, 971);
		client.getFunction().sendFrame185(969);
		client.getFunction().sendFrame164(968);
	}

	public void sendPlayerChat2(String s, String s1) {
		client.getFunction().sendFrame200(974, 589);
		client.getFunction().sendFrame126(client.playerName, 975);
		client.getFunction().sendFrame126(s, 976);
		client.getFunction().sendFrame126(s1, 977);
		client.getFunction().sendFrame185(974);
		client.getFunction().sendFrame164(973);
	}

	/*
	 * Player Chating Back
	 */

	public void sendPlayerChat3(String s, String s1, String s2) {
		client.getFunction().sendFrame200(980, 612 + Misc.random(3));
		client.getFunction().sendFrame126(client.playerName, 981);
		client.getFunction().sendFrame126(s, 982);
		client.getFunction().sendFrame126(s1, 983);
		client.getFunction().sendFrame126(s2, 984);
		client.getFunction().sendFrame185(980);
		client.getFunction().sendFrame164(979);
	}

	public void sendPlayerChat4(String s, String s1, String s2, String s3) {
		client.getFunction().sendFrame200(987, 612 + Misc.random(3));
		client.getFunction().sendFrame126(client.playerName, 988);
		client.getFunction().sendFrame126(s, 989);
		client.getFunction().sendFrame126(s1, 990);
		client.getFunction().sendFrame126(s2, 991);
		client.getFunction().sendFrame126(s3, 992);
		client.getFunction().sendFrame185(987);
		client.getFunction().sendFrame164(986);
	}

	public void sendPlayerChatAnim(String s, int anim) {
		client.getFunction().sendFrame200(969, anim);
		client.getFunction().sendFrame126(client.playerName, 970);
		client.getFunction().sendFrame126(s, 971);
		client.getFunction().sendFrame185(969);
		client.getFunction().sendFrame164(968);
	}

	public void sendStartInfo(String text, String text1, String text2,
			String text3, String title) {
		client.getFunction().sendFrame126(title, 6180);
		client.getFunction().sendFrame126(text, 6181);
		client.getFunction().sendFrame126(text1, 6182);
		client.getFunction().sendFrame126(text2, 6183);
		client.getFunction().sendFrame126(text3, 6184);
		client.getFunction().sendFrame164(6179);
	}

	public void sendStatement(String s) { // 1 line click here to continue chat
											// box interface
		client.getFunction().sendFrame126(s, 357);
		client.getFunction().sendFrame126("Click here to continue", 358);
		client.getFunction().sendFrame164(356);
	}

	public void sendTitledOption2(String title, String s, String s1) {
		client.getFunction().sendFrame126(title, 2460);
		client.getFunction().sendFrame126(s, 2461);
		client.getFunction().sendFrame126(s1, 2462);
		client.getFunction().sendFrame164(2459);
	}
}
