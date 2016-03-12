package com.ownxile.rs2.world.clan;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.Player;
import com.ownxile.rs2.player.PlayerHandler;
import com.ownxile.util.Misc;

public class Clan {

	public String[] banned = new String[50];

	public boolean lootshare;

	public int[] members = new int[100];
	public String name;
	public String owner;
	private List<Integer> playerList = new ArrayList<Integer>();
	public Random random = new Random();

	public Clan(Client p, String name) {
		if (p != null) {
			owner = p.playerName;
		}
		this.name = Misc.optimizeText(name);
	}

	public Clan(String name) {
		this.owner = "OwnXile";
		this.name = Misc.optimizeText(name);
	}

	public Player getRandomMember() {
		playerList.clear();
		for (int i = 0; i < members.length; i++) {
			if (members[i] < 0) {
				continue;
			}
			playerList.add(i);
		}
		Player player = PlayerHandler.players[playerList.get(random
				.nextInt(playerList.size()))];
		if (player != null) {
			return player;
		}
		return null;
	}
}