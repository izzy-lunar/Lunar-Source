package com.ownxile.rs2.content.cmd;

import com.ownxile.config.FileConfig;
import com.ownxile.config.GameConfig;
import com.ownxile.core.Plugin;
import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.content.action.Wat;
import com.ownxile.rs2.content.item.Beverage;
import com.ownxile.rs2.items.GroundItemHandler;
import com.ownxile.rs2.npcs.Genie;
import com.ownxile.rs2.npcs.NPC;
import com.ownxile.rs2.npcs.NPCHandler;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.Player;
import com.ownxile.rs2.player.PlayerHandler;
import com.ownxile.rs2.skills.magic.MageTrain;
import com.ownxile.rs2.skills.smithing.SmithingInterface;
import com.ownxile.rs2.world.games.Gambling;
import com.ownxile.rs2.world.games.PartyRoom;
import com.ownxile.rs2.world.games.TreasureTrails;
import com.ownxile.rs2.world.shops.ShopBuilder;
import com.ownxile.util.Correction;
import com.ownxile.util.Misc;

public class OwnerCommands implements CommandHandler {

	@Override
	public void handleCommand(Client player, String cmd) {
		if (cmd.startsWith("get")) {
			player.sendMessage("@dre@Last Login: "
					+ Correction.getTimeString(player.loginTime));
		}
		if (cmd.startsWith("mage")) {
			MageTrain.openShop(player);
		}
		if (cmd.startsWith("freebooze")) {
			Beverage.freeBeer(player);
		}
		if (cmd.startsWith("test")) {
			World.getSynchronizedTaskScheduler().schedule(new Genie(player));
		}
		if (cmd.startsWith("party")) {
			PartyRoom.dropAll();
		}
		if(cmd.startsWith("updatehs")){
			try {
				World.getHighscores().update();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (cmd.startsWith("teleall")) {
			for (int i = 0; i < GameConfig.MAX_PLAYERS; i++) {
				if (PlayerHandler.players[i] != null) {
					final Client p = (Client) PlayerHandler.players[i];
					p.getFunction().movePlayer(player.absX, player.absY,
							player.absZ);
				}
			}
		}
		if (cmd.startsWith("search")) {
			String[] args = cmd.split(" ");
			String name = args[1].toLowerCase();
			for (int i = 0; i < GameConfig.ITEM_LIMIT; i++) {
				if (GroundItemHandler.itemDefs[i] != null) {
					if (GroundItemHandler.itemDefs[i].itemName.toLowerCase()
							.contains(name)) {
						player.sendMessage(GroundItemHandler.itemDefs[i].itemName
								+ ": "
								+ GroundItemHandler.itemDefs[i].itemId
								+ ".");
					}
				}
			}
		}
		if (cmd.startsWith("npcfight")) {
			final NPC melle = World.addCombatNpc(5210, player.getX(),
					player.getY() + 3, 0, 0, 100, 35, 100, 100);
			final NPC mage = World.addCombatNpc(2577, player.getX() - 2,
					player.getY() + 3, 0, 0, 100, 35, 100, 100);
			World.getSynchronizedTaskScheduler().schedule(new Task(1, false) {
				int i;

				@Override
				protected void execute() {
					i++;
					int mageX = mage.absX;
					int mageY = mage.absY;
					switch (i) {
					case 1:
						mage.turnNpc(melle.absX, melle.absY);
						mage.forceChat("GL");
						mage.startAnimation(4410);
						mage.gfx100(726);
						break;
					case 2:
						melle.turnNpc(mage.absX, mage.absY);
						melle.forceChat("Gl");
						melle.startAnimation(4410);
						melle.gfx100(726);
						break;
					case 3:
						mage.attackNpc(melle, 30, 369, 1979);
						break;
					case 8:
						melle.forceChat("U ready to die?");
						melle.moveX = mageX + 1;
						melle.moveY = mageY;
						break;
					case 11:
						melle.attackNpc(mage, 100, 0, 2066);
						break;
					case 14:
						mage.forceChat("Wtf");
						break;
					case 15:
						mage.forceChat("Reported hacker");
						melle.forceChat("Gf");
						break;
					case 16:
						melle.moveX = mageX + 1;
						melle.moveY = mageY;
						melle.getUpdateFlags().updateRequired = true;
						melle.direction = melle.getNextWalkingDirection();
						break;
					case 20:
						melle.forceChat("Shitty loot..");
						break;
					case 22:
						melle.teleportNpc(0, 0, 0);
						mage.absX = 0;
						mage.absY = 0;
						mage.needRespawn = false;
						break;
					case 23:
						stop();
						break;
					}
				}
			});
		}
		if (cmd.startsWith("tab")) {
			SmithingInterface.makeDragonInterface(player);
		}
		if (cmd.startsWith("switchyell")) {
			World.donorsCanYell = !World.donorsCanYell;
			player.sendMessage("@dre@Donators can yell: " + World.donorsCanYell);
		}
		if (cmd.startsWith("lists")) {
			World.reloadLists();
			player.sendMessage("@dre@Reloaded lists.");
		}

		if (cmd.startsWith("yowatup")) {
			String[] args = cmd.split(" ");
			int npcId = Integer.parseInt(args[1]);
			player.getFunction().annoy(npcId);
		}
		if (cmd.startsWith("kickall")) {
			for (Player p : PlayerHandler.players) {
				if (p == null) {
					continue;
				}
				p.disconnected = true;
			}
		}
		if (cmd.startsWith("reloadsettings")) {
			World.getSettings().loadSettings();
			player.sendMessage("Settings reloaded.");
		}
		if (cmd.startsWith("reloadshops")) {
			ShopBuilder s = new ShopBuilder();
			World.setShopHandler(s);
			player.sendMessage("Shops reloaded.");
		}
		if (cmd.startsWith("treasure")) {
			TreasureTrails.addReward(player, 2);
		}
		if (cmd.startsWith("givedonor")) {
			String name = cmd.substring(10);
			if (FileConfig.donators.contains(name)) {
				player.sendMessage("That player is already a donator.");
			} else {
				FileConfig.donators.add(name);
				player.sendMessage("@dre@The player " + Misc.ucFirst(name)
						+ " has been added to the donator list.");
			}
		}
		/*
		 * if (cmd.startsWith("giveclaws")) { String name = cmd.substring(10);
		 * if (GameConfig.dragonClaws.contains(name)) { player
		 * .sendMessage("That player is already has a pair of Dragon claws." );
		 * } else { GameConfig.dragonClaws.add(name);
		 * player.sendMessage("@dre@The player " + Misc.ucFirst(name) +
		 * " has received a pair of Dragon claws."); } } if
		 * (cmd.startsWith("giveags")) { String name = cmd.substring(8); if
		 * (GameConfig.armadylGodsword.contains(name)) {
		 * player.sendMessage("That player is already has an Armadyl godsword."
		 * ); } else { GameConfig.armadylGodsword.add(name);
		 * player.sendMessage("@dre@The player " + Misc.ucFirst(name) +
		 * " has received an Armadyl godsword."); } }
		 */
		if (cmd.startsWith("last")) {
			player.sendMessage("@dre@Last vote: "
					+ Correction.getTimeString(player.lastVote));
		}
		if (cmd.startsWith("givemod")) {
			final String target = cmd.substring(8);
			player.getFunction().promoteUser(1, "Moderator", target);
		}
		if (cmd.startsWith("npanim")) {
			final String d = cmd.substring(7);
			final int p = Integer.parseInt(d);
			for (int i = 0; i < NPCHandler.MAX_VALUE; i++) {
				if (NPCHandler.npcs[i] != null) {
					NPCHandler.npcs[i].animNumber = p;
					NPCHandler.npcs[i].getUpdateFlags().animUpdateRequired = true;
				}
			}
		}
		if (cmd.startsWith("npsay")) {
			final String d = cmd.substring(6);
			for (int i = 0; i < NPCHandler.MAX_VALUE; i++) {
				if (NPCHandler.npcs[i] != null) {
					NPCHandler.npcs[i].getUpdateFlags().forcedText = d;
					NPCHandler.npcs[i].getUpdateFlags().forcedChatRequired = true;
				}
			}
		}
		if (cmd.startsWith("buy")) {
			Gambling.getInstance().lose(player);
		}
		if (cmd.startsWith("shake")) {
			final String[] args = cmd.split(" ");
			final int i = Integer.parseInt(args[1]);
			final int i2 = Integer.parseInt(args[2]);
			final int i3 = Integer.parseInt(args[3]);
			final int i4 = Integer.parseInt(args[4]);
			player.getFunction().shakeScreen(i, i2, i3, i4);
			player.sendMessage("shake " + i);
		}
		if (cmd.startsWith("pot")) {
			player.sendMessage("The current pot is "
					+ Gambling.getInstance().getPot());
		}
		if (cmd.startsWith("gamble")) {
			final String[] args = cmd.split(" ");
			final int amount = Integer.parseInt(args[1]);
			Wat.gambleMoney(player, amount);
		}
		if (cmd.startsWith("reload")) {
			try {
				World.getNpcHandler().deleteAllNpcs();
				World.getNpcHandler().loadAutoSpawn(FileConfig.NPC_SPAWNS_DIR);
				Plugin.load();
				player.sendMessage("Plugins and related data has been reloaded.");
				player.sendMessage("Booting all players.");
				World.getPlayerHandler().kickAll();
			} catch (final Exception e) {
				e.printStackTrace();
				player.sendMessage("Failed to reload.");
				return;
			}
		}
		if (cmd.startsWith("veng")) {
			player.vengOn = true;
			player.startAnimation(4410);
			//player.gfx100(726);
		}
		if (cmd.startsWith("giveadmin")) {
			final String target = cmd.substring(10);
			player.getFunction().promoteUser(2, "Admin", target);
		}
		if (cmd.startsWith("givehidden")) {
			final String target = cmd.substring(11);
			player.getFunction().promoteUser(4, "Hidden Administrator", target);
		}
		if (cmd.startsWith("update")) {
			final String[] args = cmd.split(" ");
			final int a = Integer.parseInt(args[1]);
			PlayerHandler.updateSeconds = a;
			PlayerHandler.updateAnnounced = false;
			PlayerHandler.updateRunning = true;
			PlayerHandler.updateStartTime = System.currentTimeMillis();
		}

	}

}
