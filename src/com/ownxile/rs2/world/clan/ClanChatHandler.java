package com.ownxile.rs2.world.clan;

import com.ownxile.config.GameConfig;
import com.ownxile.config.ItemConfig;
import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.PlayerHandler;
import com.ownxile.util.Correction;
import com.ownxile.util.Misc;

public class ClanChatHandler {

	public Clan[] clans = new Clan[GameConfig.MAX_CLANS];

	private void addToClan(final int playerId, final int clanId) {
		final Client c = (Client) PlayerHandler.players[playerId];
		c.sendMessage("Attempting to join channel...");
		World.getSynchronizedTaskScheduler().schedule(new Task(2, false) {
			@Override
			protected void execute() {
				addToClan2(c, playerId, clanId);
				stop();
			}
		});
	}

	@SuppressWarnings("unused")
	private boolean isBanned(int clanId, String name) {
		Clan clan = clans[clanId];
		for (String s : clan.banned) {
			if (s.equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	private void addToClan2(Client c, int playerId, int clanId) {
		if (clans[clanId] != null) {
			for (int j = 0; j < clans[clanId].members.length; j++) {
				if (clans[clanId].members[j] <= 0) {
					c.getFunction().sendFrame126(
							"Talking in: " + clans[clanId].name, 18139);
					c.getFunction().sendFrame126(
							"Owner: " + clans[clanId].owner, 18140);
					c.sendMessage("Now talking in clan channel "
							+ clans[clanId].name + ".");
					clans[clanId].members[j] = playerId;
					PlayerHandler.players[playerId].clanId = clanId;
					updateClanChat(clanId);
					c.clanName = clans[clanId].name;
					c.sendMessage("To talk, start each line of chat with the / symbol.");
					return;
				} else if (c.clanId != -1) {
					c.sendMessage("You are already in a chat.");
					return;
				}
			}
		}
	}

	public void clanMessage(String message, int clanId) {
		if (clanId < 0) {
			return;
		}
		for (int member : clans[clanId].members) {
			if (member <= 0) {
				continue;
			}
			if (PlayerHandler.players[member] != null) {
				final Client c = (Client) PlayerHandler.players[member];
				c.sendMessage(message);
			}
		}
	}

	private void destructClan(int clanId) {
		if (clanId < 0) {
			return;
		}
		for (int member : clans[clanId].members) {
			if (clanId < 0) {
				continue;
			}
			if (member <= 0) {
				continue;
			}
			if (PlayerHandler.players[member] != null) {
				final Client c = (Client) PlayerHandler.players[member];
				c.clanId = -1;
				c.getFunction().clearClanChat();
			}
		}
		clans[clanId].members = new int[100];
		if (clanId > 2) {
			clans[clanId].owner = "";
		}
		clans[clanId].name = "";
	}

	public Clan[] getClans() {
		return clans;
	}

	public void handleLootShare(Client c, int item, int amount, int npcId) {
		String name = ItemConfig.getItemName(item);
		String npcName = World.getNpcHandler().getNPCName(npcId);
		if (amount == 0 || name == null)
			return;
		if (amount > 1) {
			if (item == 995) {
				String cashS = Correction.getAmountString(amount);
				sendLootShareMessage(c.clanId, c.playerName,
						"received a drop: " + cashS + " coins from " + npcName
								+ ".");

			} else
				sendLootShareMessage(c.clanId, c.playerName,
						"received a drop: " + amount + "x " + name + " from "
								+ npcName + ".");
		} else {
			sendLootShareMessage(c.clanId, c.playerName, "received a drop: "
					+ name + " from " + npcName + ".");
		}
	}

	public void joinClanChat(Client c, String name) {
		if (name.equalsIgnoreCase("staff") && c.playerRights == 0) {
			c.sendMessage("The clan chat '@blu@staff@bla@' is set to private ");
			return;
		}
		for (int j = 0; j < clans.length; j++) {
			if (clans[j] != null) {
				if (clans[j].name.equalsIgnoreCase(name)) {
					addToClan(c.playerId, j);
					return;
				}
			}
		}
		makeClan(c, name);
	}

	public void leaveClan(int playerId, int clanId) {
		if (clanId < 0) {
			final Client c = (Client) PlayerHandler.players[playerId];
			c.sendMessage("You are not in a clan.");
			return;
		}
		if (clans[clanId] != null) {
			PlayerHandler.players[playerId].clanName = "";
			if (PlayerHandler.players[playerId].playerName
					.equalsIgnoreCase(clans[clanId].owner)) {
				messageToClan("The clan chat has been closed by "
						+ clans[clanId].owner + ".", clanId);
				destructClan(PlayerHandler.players[playerId].clanId);
				return;
			}
			for (int j = 0; j < clans[clanId].members.length; j++) {
				if (clans[clanId].members[j] == playerId) {
					clans[clanId].members[j] = -1;
				}
			}
			if (PlayerHandler.players[playerId] != null) {
				final Client c = (Client) PlayerHandler.players[playerId];
				PlayerHandler.players[playerId].clanId = -1;
				c.sendMessage("You have left the clan.");
				c.getFunction().clearClanChat();
			}
			updateClanChat(clanId);
		} else {
			final Client c = (Client) PlayerHandler.players[playerId];
			PlayerHandler.players[playerId].clanId = -1;
			c.sendMessage("You are not in a clan chat.");
		}
	}

	public void loadDefaultClans() {
		clans[0] = new Clan("staff");
		clans[0].owner = "OwnXile";

		clans[1] = new Clan("help");
		clans[1].owner = "OwnXile";

		clans[2] = new Clan("ownxile");
		clans[2].owner = "OwnXile";

		clans[3] = new Clan("trade");
		clans[3].owner = "OwnXile";
	}

	private void makeClan(Client c, String name) {
		if (openClan() >= 0) {
			if (validName(name)) {
				c.clanId = openClan();
				clans[c.clanId] = new Clan(c, name);
				clans[c.clanId].name = Misc.ucFirst(clans[c.clanId].name
						.toLowerCase());
				addToClan(c.playerId, c.clanId);
			} else {
				c.sendMessage("A clan with this name already exists.");
			}
		} else {
			c.sendMessage("Your clan chat request could not be completed.");
		}
	}

	private void messageToClan(String message, int clanId) {
		if (clanId < 0) {
			return;
		}
		for (int member : clans[clanId].members) {
			if (member < 0) {
				continue;
			}
			if (PlayerHandler.players[member] != null) {
				final Client c = (Client) PlayerHandler.players[member];
				c.sendMessage("" + message);
			}
		}
	}

	private int openClan() {
		for (int j = 0; j < clans.length; j++) {
			if (clans[j] == null || clans[j].owner == "") {
				return j;
			}
		}
		return -1;
	}

	public void playerMessageToClan(int playerId, String message, int clanId) {
		if (clanId < 0) {
			return;
		}

		System.out
				.println("["
						+ clans[clanId].name
						+ "]: "
						+ Misc.formatPlayerName(PlayerHandler.players[playerId].playerName)
						+ ": " + message);
		PlayerHandler.players[playerId].lastClanMessage = System
				.currentTimeMillis();
		for (int member : clans[clanId].members) {
			if (member <= 0) {
				continue;
			}
			if (PlayerHandler.players[member] != null) {
				final Client c = (Client) PlayerHandler.players[member];
				// c.sendMessage("["+Server.playerHandler.players[playerId].playerName+"] - "
				// + message");
				c.getFunction().sendClan(
						PlayerHandler.players[playerId].playerName, message,
						clans[clanId].name,
						PlayerHandler.players[playerId].playerRights);
				/*
				 * c.getFunction().sendClanStyleMessage(clans[clanId].name,
				 * PlayerHandler.players[playerId].playerName, message);
				 */
			}
		}
	}

	public void sendLootShareMessage(int clanId, String name, String message) {
		if (clanId >= 0) {
			for (int member : clans[clanId].members) {
				if (member <= 0) {
					continue;
				}
				if (PlayerHandler.players[member] != null) {
					final Client c = (Client) PlayerHandler.players[member];
					c.sendMessage(name.equals(c.playerName) ? "@grd@You have "
							+ message : "@grd@" + name + " has " + message);
				}
			}
		}
	}

	private void updateClanChat(int clanId) {
		for (int member : clans[clanId].members) {
			if (member <= 0) {
				continue;
			}
			if (PlayerHandler.players[member] != null) {
				final Client c = (Client) PlayerHandler.players[member];

				int slotToFill = 18144;
				for (int member2 : clans[clanId].members) {
					if (member2 > 0) {
						if (PlayerHandler.players[member2] != null) {
							c.getFunction().sendFrame126(
									PlayerHandler.players[member2].playerName,
									slotToFill);
							slotToFill++;
						}
					}
				}
				for (int k = slotToFill; k < 18244; k++) {
					c.getFunction().sendFrame126("", k);
				}
			}
		}
	}

	private boolean validName(String name) {
		for (Clan clan : clans) {
			if (clan != null) {
				if (clan.name.equalsIgnoreCase(name)) {
					return false;
				}
			}
		}
		return true;
	}

}