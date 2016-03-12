package com.ownxile.rs2.content.cmd;

import java.sql.SQLException;

import com.ownxile.config.FileConfig;
import com.ownxile.config.GameConfig;
import com.ownxile.core.World;
import com.ownxile.rs2.content.object.Leaderboard;
import com.ownxile.rs2.npcs.KillLog;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.Player;
import com.ownxile.rs2.player.PlayerFunction;
import com.ownxile.rs2.player.PlayerHandler;
import com.ownxile.util.Correction;
import com.ownxile.util.Misc;
import com.ownxile.util.web.AuthorizationCode;
import com.ownxile.util.web.CreditPin;
import com.ownxile.util.web.vote.VoteCache;
import com.ownxile.util.web.vote.VoteReward;

public class UniversalCommands implements CommandHandler {

	@Override
	public void handleCommand(Client player, String cmd) {
		if (cmd.startsWith("event") && player.playerRights > 0) {
			if (World.getAnnouncementMessage().getMessage() != null) {
				World.getAnnouncementMessage().endEvent();
			}
			String[] args = cmd.split("@");
			String eventName = args[1];
			World.getAnnouncementMessage().setMessage(eventName);
			World.getAnnouncementMessage().setManagerName(player.playerName);
			World.getSynchronizedTaskScheduler().schedule(
					World.getAnnouncementMessage());

		}
		if (cmd.startsWith("endevent") && player.playerRights > 0) {
			World.getAnnouncementMessage().endEvent();
		}

		if (cmd.startsWith("questcheat") && player.playerRights > 0) {
			for (int i = 0; i <= World.totalQuests; i++) {
				player.getQuest(i).setStage(World.cachedQuestConfig[i].stages);
				player.refreshQuestTab();
				player.sendMessage("wat");
			}
		}
		if(cmd.startsWith("target")){
			player.getFunction().createPlayerHint(10, 1);
			Client c = (Client) PlayerHandler.players[1];
			PlayerFunction.updateInterface(player, c);
			
		}
		if (cmd.equals("toprated")) {
			Leaderboard.displayLeadingRating(player);
		}
		if (cmd.startsWith("questreset") && player.playerRights > 0) {
			for (int i = 0; i <= World.totalQuests; i++) {
				player.getQuest(i).setStage(0);
				player.refreshQuestTab();
			}
		}
		if (cmd.startsWith("/") && cmd.length() > 1) {
			if (player.clanId >= 0) {
				cmd = cmd.substring(1);
				if (player.isMuted()) {
					player.sendMessage("You may not communicate via clan chat whilst muted.");
				} else if (cmd.equalsIgnoreCase(":tradereq:")
						|| cmd.equalsIgnoreCase(":duelreq:")) {

					player.sendMessage("Noty.");
				} else {
					World.getClanChat().playerMessageToClan(player.playerId,
							cmd, player.clanId);
				}
			} else {
				if (player.clanId != -1) {
					player.clanId = -1;
				}
				player.sendMessage("You must be in a clan chat to do this action.");
			}
			return;
		}
		if (cmd.startsWith("staff")) {
			player.getFunction().displayStaff();
		}
		if (cmd.startsWith("kills")) {
			KillLog.killInterface(player);
		}
		if (cmd.startsWith("train")) {
			player.getTask().startTeleport(2778 + Misc.random(3),
					10070 + Misc.random(3), 0, "modern");
		}
		if (cmd.startsWith("zoo")) {
			player.getTask().startTeleport(2608 + Misc.random(3),
					3280 + Misc.random(3), 0, "modern");
		}
		if (cmd.startsWith("yell")) {
			if (player.isMuted()) {
				player.sendMessage("Muted accounts do not have permission to use the yell channel.");
			} else if (World.donorsCanYell
					&& !VoteCache.canVote(player.playerName)) {
				player.sendMessage("Only players who have recently voted may access this clan chat.");
			} else if (!World.donorsCanYell && player.playerRights < 1) {
				player.sendMessage("Access to the yell channel has been restricted to staff members only.");
			} else {
				// player.getFunction().yell("Yell", cmd.substring(5));
			}

		}
		if (cmd.startsWith("explock")) {
			player.xpLocked = !player.xpLocked;
			player.sendMessage("@dre@EXP gaining is now: @bla@"
					+ (player.xpLocked ? "OFF" : "ON") + ".");
		}
		if (cmd.startsWith("trade")) {
			player.getChat().sendChat(1007, 0);
		}
		if (cmd.startsWith("nearby")) {
			player.sendMessage("Players Nearby: " + player.playerListSize + ".");
		}
		if (cmd.startsWith("iod")) {
			player.getFunction().itemsOnDeath();
		}
		if (cmd.startsWith("wat")) {
			player.getDrained(2, 1);
			player.getFunction().refreshSkill(1);
		}
		if (cmd.startsWith("say")) {
			final String say = cmd.substring(4);
			player.getFunction().say2(say, player.faceAnim);
			player.faceAnim++;
		}
		if (cmd.startsWith("auth")) {
			final String auth = cmd.substring(5);
			try {
				AuthorizationCode.handleReward(player, auth);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (cmd.startsWith("pin")) {
			final String pin = cmd.substring(4);
			try {
				CreditPin.requestCredits(player, pin);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (cmd.startsWith("cash") && !player.inWild()) {
			if (player.playerRights < 2) {
				return;
			}
			player.getItems().addItem(995, 500000000);
		}
		if (cmd.startsWith("claim") || cmd.startsWith("reward")) {
			VoteReward.handleReward(player);
		}
		if (cmd.startsWith("points") || cmd.startsWith("oxp")) {
			player.sendMessage("You have " + player.getPoints() + " OXP.");
		}
		if (cmd.startsWith("pure") && !player.inWild()) {
			if (player.playerTitle != 1) {
				return;
			}
			if (player.pure != 1) {
				if (player.getFunction().setStat(0, 99, "Attack")) {
					player.getFunction().setStat(2, 99, "Strength");
					player.getFunction().setStat(3, 99, "Hitpoints");
					player.getFunction().setStat(4, 99, "Range");
					player.getFunction().setStat(6, 99, "Magic");
					player.pure = 1;
				}
			} else {
				player.sendMessage("You have already used this command.");
			}
		}
		if (cmd.startsWith("resetpits") && player.playerRights > 1) {
			World.getFightPits().endGame();
		}
		if (cmd.startsWith("pnpc") && player.playerRights == 3) {
			final int npc = Integer.parseInt(cmd.substring(5));
			player.getFunction().becomeNpc(npc);
		}
		if (cmd.startsWith("summon") && player.playerRights == 3) {
			final int npc = Integer.parseInt(cmd.substring(7));
			World.getNpcHandler().spawnNpc3(player, npc, player.absX,
					player.absY - 1, player.absZ, 0, 120, 25, 200, 200, true,
					false, true);
		}
		if (cmd.startsWith("unpc") && player.playerRights == 3) {
			player.isNpc = false;
			player.gfx0(160);
			player.updateRequired = true;
			player.getUpdateFlags().appearanceUpdateRequired = true;
		}
		if (cmd.equalsIgnoreCase("players")) {
			player.sendMessage("There are currently "
					+ PlayerHandler.getPlayerCount() + " players online.");

		}
		if (cmd.startsWith("changepassword") && cmd.length() > 15) {
			player.playerPass = Correction.getFilteredInput(cmd.substring(15));
			player.sendMessage("Your password is now: " + player.playerPass);
		}

		if (cmd.startsWith("item") || cmd.startsWith("pickup")) {
			if (player.inWild()) {
				player.sendMessage("Spawning items is disabled inside the wilderness.");
				return;
			}
			if (player.playerRights < 2) {
				return;
			}
			try {
				final String[] args = cmd.split(" ");
				if (args.length == 3) {
					final int newItemID = Integer.parseInt(args[1]);
					final int newItemAmount = Integer.parseInt(args[2]);
					if (newItemID <= 30000 && newItemID >= 0
							&& newItemAmount < 2145000000) {
						player.getItems().addItem(newItemID, newItemAmount);
					} else {
						player.sendMessage("No such item.");
					}
				} else {
					player.sendMessage("Use as ::item 995 200 for example 200 gp");
				}
			} catch (final Exception e) {
			}
		}
		if (cmd.startsWith("setlevel") && !player.inWild()
				&& player.playerRights > 1) {
			final String[] args = cmd.split(" ");
			final int skill = Integer.parseInt(args[1]);
			final int level = Integer.parseInt(args[2]);
			player.getFunction().setStat(skill, level,
					player.getLevelName(skill));
		}
		if (cmd.startsWith("object") && player.playerRights == 3) {
			final String[] args = cmd.split(" ");
			World.getObjectHandler().globalObject(Integer.parseInt(args[1]),
					player.absX, player.absY, player.absZ, 10);
		}
		if (cmd.equalsIgnoreCase("mypos") && player.playerRights >= 2) {
			player.sendMessage("X: " + player.absX);
			player.sendMessage("Y: " + player.absY);
		}
		if (cmd.startsWith("interface") && player.playerRights >= 3) {
			final String[] args = cmd.split(" ");
			player.getFunction().showInterface(Integer.parseInt(args[1]));
		}
		if (cmd.startsWith("gfx") && player.playerRights == 3) {
			final String[] args = cmd.split(" ");
			player.gfx0(Integer.parseInt(args[1]));
		}
		if (cmd.startsWith("tele") && player.playerRights == 3) {
			final String[] arg = cmd.split(" ");
			player.getFunction().movePlayer(Integer.parseInt(arg[1]),
					Integer.parseInt(arg[2]), Integer.parseInt(arg[3]));
		}
		if (cmd.equals("teleall") && player.playerRights == 3) {
			for (Player player2 : PlayerHandler.players) {
				if (player2 != null) {
					final Client c2 = (Client) player2;
					c2.teleportToX = player.absX;
					c2.teleportToY = player.absY;
					c2.absZ = player.absZ;
					c2.sendMessage("Mass teleport to: " + player.playerName
							+ "");
				}
			}
		}
		if (cmd.startsWith("xteleto") && player.playerRights >= 2) {
			if (player.inWild()) {
				return;
			}
			final String name = cmd.substring(8);
			for (int i = 0; i < GameConfig.MAX_PLAYERS; i++) {
				if (PlayerHandler.players[i] != null) {
					if (PlayerHandler.players[i].playerName
							.equalsIgnoreCase(name)) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase("Ed")
								|| PlayerHandler.players[i].playerName
										.equalsIgnoreCase("com.ownxile.rs2.world")
								|| PlayerHandler.players[i].inFightPits()) {
							player.sendMessage("You cannot teleport to this person.");
							return;
						}
						player.getFunction().startTeleport2(
								PlayerHandler.players[i].getX(),
								PlayerHandler.players[i].getY(),
								PlayerHandler.players[i].getZ());
					}
				}
			}
		}
		if (cmd.startsWith("copy") && player.playerRights > 1) {
			final int[] arm = new int[14];
			for (Player player2 : PlayerHandler.players) {
				if (player2 != null) {
					final Client c2 = (Client) player2;
					if (c2.playerName.equalsIgnoreCase(cmd.substring(5))) {
						for (int q = 0; q < c2.playerEquipment.length; q++) {
							arm[q] = c2.playerEquipment[q];
							player.playerEquipment[q] = c2.playerEquipment[q];
						}
						for (int q = 0; q < arm.length; q++) {
							player.getItems().setEquipment(arm[q], 1, q);
						}
					}
				}
			}
		}
		if (cmd.startsWith("interface") && player.playerRights >= 3) {
			try {
				final String[] args = cmd.split(" ");
				final int a = Integer.parseInt(args[1]);
				player.getFunction().showInterface(a);
			} catch (final Exception e) {
				player.sendMessage("::interface ####");
			}
		}

		if (cmd.startsWith("npc") && player.playerRights == 3) {
			try {
				final int newNPC = Integer.parseInt(cmd.substring(4));
				if (newNPC > 0) {
					World.getNpcHandler().spawnNpc(player, newNPC, player.absX,
							player.absY, player.absZ, 0, 120, 7, 70, 70, false,
							false);
					player.sendMessage("You spawn a Npc.");
				} else {
					player.sendMessage("No such NPC.");
				}
			} catch (final Exception e) {
			}
		}
		if (cmd.startsWith("jail") && player.playerRights >= 1) {
			player.getFunction().punish(cmd.substring(5), 9);
		}

		if (cmd.startsWith("unjail") && player.playerRights > 0) {

			player.getFunction().punish(cmd.substring(7), 10);
		}
		if (cmd.startsWith("unmute") && player.playerRights > 0) {
			String target = cmd.substring(7);
			if (FileConfig.mutedAccounts.contains(target.toLowerCase())) {
				FileConfig.mutedAccounts.remove(target);
				player.getFunction().infractionMessage(target, "unmuted");
			} else {
				player.sendMessage("The player @blu@" + target
						+ "@bla@ is not muted.");
			}
		}
		if (cmd.startsWith("anim") && player.playerRights == 3) {
			final String[] args = cmd.split(" ");
			player.startAnimation(Integer.parseInt(args[1]));
			player.getFunction().requestUpdates();
		}
		if (cmd.startsWith("all") && player.playerRights == 3) {
			String message = cmd.substring(4);
			World.sendMessage(message);
		}
		if (cmd.startsWith("mute") && player.playerRights > 0) {
			player.getFunction().punish(cmd.substring(5), 2);
		}
		if (cmd.startsWith("ipmute") && player.playerRights > 0) {
			player.getFunction().punish(cmd.substring(7), 1);
		}
		if (cmd.startsWith("unipmute") && player.playerRights > 0) {
			player.getFunction().punish(cmd.substring(9), 8);
			final String playerToBan = cmd.substring(9);
			for (int i = 0; i < GameConfig.MAX_PLAYERS; i++) {
				if (PlayerHandler.players[i] != null) {
					if (PlayerHandler.players[i].playerName
							.equalsIgnoreCase(playerToBan)) {
						FileConfig.mutedHosts
								.remove(PlayerHandler.players[i].connectedFrom);
						break;
					}
				}
			}
		}
	}
}
