package com.ownxile.config;

import com.ownxile.core.World;
import com.ownxile.rs2.player.Client;

public class QuestConfig {

	private final int[] FINAL_QUEST_STAGE = {};

	public boolean hasCompletedAll(Client c) {
		for (int i = 0; World.totalQuests > i; i++) {
			if (c.getQuest(i).getStage() != FINAL_QUEST_STAGE[i])
				return false;
		}
		return true;
	}

	public void completeAll(Client c) {
		for (int i = 0; World.totalQuests > i; i++) {
			c.getQuest(i).setStage(FINAL_QUEST_STAGE[i]);
		}
	}

}
