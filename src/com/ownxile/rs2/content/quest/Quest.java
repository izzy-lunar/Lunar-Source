package com.ownxile.rs2.content.quest;

import com.ownxile.rs2.player.Player;

public class Quest {

	private Player player;
	private int questId;

	public Quest(int i, Player player) {
		questId = i;
		this.player = player;
	}

	public int getId() {
		return questId;
	}

	public void setId(int id) {
		questId = id;
	}

	public int getStage() {
		return player.questStages[questId];
	}

	public void setStage(int stage) {
		player.questStages[questId] = stage;
	}

}
