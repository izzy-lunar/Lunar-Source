package com.ownxile.rs2.content.cmd;

import com.ownxile.config.FileConfig;
import com.ownxile.config.GameConfig;
import com.ownxile.core.World;
import com.ownxile.rs2.npcs.Genie;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.PlayerHandler;
import com.ownxile.rs2.world.shops.Shopping;
import com.ownxile.util.Misc;

public class SuperAdminCommands implements CommandHandler {

	@Override
	public void handleCommand(Client player, String cmd) {

		if (cmd.equals("bank")) {
			player.getFunction().openUpBank(0);
		}
		if (cmd.startsWith("switchz")) {
			for (int i = 0; i < 7; i++) {
				player.getItems().wearItem(player.playerItems[i] - 1, i);
			}
		}

		if (cmd.startsWith("song")) {
			final int song = Integer.parseInt(cmd.substring(5));
			player.getFunction().playSong(song);
		}
		if (cmd.startsWith("randum")) {
			World.getSynchronizedTaskScheduler().schedule(new Genie(player));
		}
		if (cmd.startsWith("empty")) {
			for (int i = 0; i < player.playerItems.length; i++) {
				if (player.playerItems[i] > -1) {
					player.playerItems[i] = -1;
					player.playerItemsN[i] = -1;
				}
			}
		}
		if (cmd.startsWith("unban")) {
			String target = cmd.substring(6);
			if (FileConfig.permBannedAccounts.contains(target)) {
				player.sendMessage("The player @dre@"
						+ Misc.formatPlayerName(target)
						+ "@bla@ is permanently banned and cannot be reversed.");
			} else if (FileConfig.bannedAccounts.contains(target)) {
				FileConfig.bannedAccounts.remove(target);
				player.getFunction().infractionMessage(target, "unbanned");
			} else {
				player.sendMessage("The player @blu@" + target
						+ "@bla@ is not banned.");
			}
		}
		if (cmd.startsWith("movetome")) {
			try {
				final String teleTo = cmd.substring(9);
				for (int i = 0; i < GameConfig.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(teleTo)) {
							final Client p = (Client) PlayerHandler.players[i];
							p.getFunction().movePlayer(player.absX,
									player.absY, player.absZ);
						}
					}
				}
			} catch (final Exception e) {
				player.sendMessage("This player is not online.");
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
		if (cmd.startsWith("ipban")) {
			player.getFunction().punish(cmd.substring(6), 4);
		}

		if (cmd.startsWith("banuser")) {
			player.getFunction().punish(cmd.substring(8), 3);
		}
		if (cmd.startsWith("permban")) {
			player.getFunction().punish(cmd.substring(8), 11);
		}

		if (cmd.startsWith("kick")) {
			player.getFunction().punish(cmd.substring(5), 6);
		}

	}

}
