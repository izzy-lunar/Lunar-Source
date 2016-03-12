package com.ownxile.rs2.content.quest;

import com.ownxile.core.World;
import com.ownxile.rs2.player.Client;

public class QuestHandler {

	public static String setColor(int c) {
		switch (c) {
		case 1:
			return "@red@";
		case 2:
			return "@yel@";
		case 3:
			return "@gre@";
		}
		return null;
	}

	private final Client player;

	public QuestHandler(Client player) {
		this.player = player;
	}

	public void sendQuestInterface(Client client, String questName,
			String... lines) {
		client.getTask().sendFrame126("@dre@" + questName, 8144);

		for (int i = 0; i < lines.length; i++) {
			client.getTask().sendFrame126(lines[i], 8147 + i);
		}
		for (int i = 8147 + lines.length; i < 8195; i++) {
			client.getTask().sendFrame126("", i);
		}
		client.getTask().showInterface(8134);
	}

	public void completeQuest(String questName, QuestReward questReward) {
		player.qp++;
		player.sendMessage("You have completed " + questName + ".");
		player.endChat();
		player.getFunction().sendFrame126(
				"You have completed the " + questName + " Quest!", 12144);
		for (int i = 0; i < questReward.rewards.length; i++) {
			if (questReward.rewards[i] != null) {
				player.getFunction().sendFrame126(questReward.rewards[i],
						12150 + i);
			} else {
				player.getFunction().sendFrame126(" ", 12150 + i);
			}
		}
		player.getFunction().sendFrame126("Quest Points:", 12146);
		player.getFunction().sendFrame126("" + player.qp, 12147);
		player.getFunction().showInterface(12140);
		player.gfx100(199);
		player.getQuestFunction().refreshQuestTab();
		player.getFunction().playTempSong(86);
	}

	public void completeQuest(String questName, QuestReward questReward,
			int itemId) {
		player.qp++;
		player.sendMessage("You have completed " + questName + ".");
		player.endChat();
		player.getFunction().sendFrame126(
				"You have completed " + questName + "!", 12144);
		for (int i = 0; i < questReward.rewards.length; i++) {
			if (questReward.rewards[i] != null) {
				player.getFunction().sendFrame126(questReward.rewards[i],
						12150 + i);
			} else {
				player.getFunction().sendFrame126(" ", 12150 + i);
			}
		}
		player.getFunction().sendFrame126("Quest Points:", 12146);
		player.getFunction().sendFrame126("" + player.qp, 12147);
		player.getFunction().showInterface(12140);
		player.gfx100(199);
		player.getFunction().itemOnInterface(12145, 240, itemId);
		player.getQuestFunction().refreshQuestTab();
		player.getFunction().playTempSong(86);
	}

	public void displayQuest(int id, String name, int questStage, int stages) {
		int c;
		if (questStage == 0) {
			c = 1;
		} else if (questStage == stages) {
			c = 3;
		} else {
			c = 2;
		}
		player.getFunction().sendFrame126(setColor(c) + name, 16026 + id);
		// player.getFunction().sendFrame126(setColor(c) + name,
		// GameConfig.QUEST_ID[id]);
	}

	public boolean hasCompletedAll() {
		for (int i = 0; i <= World.totalQuests; i++) {
			if (World.cachedQuestConfig[i] != null) {
				if (player.getQuest(i).getStage() != World.cachedQuestConfig[i].stages
						&& i != 30 && i != 34)
					return false;
			}
		}
		return true;
	}

	public void refreshQuestTab() {
		for (int i = 0; i <= World.totalQuests; i++) {
			if (World.questConfigData[i] != null
					&& i < World.questConfigData.length) {
				displayQuest(i, World.questConfigData[i].name,
						player.getQuest(World.questConfigData[i].i).getStage(),
						World.questConfigData[i].stages);
			}
		}
		player.getFunction().sendFrame126("Quest Points: " + player.qp, 640);
		player.getFunction().sendFrame126("Quests", 663);
		player.getFunction().sendFrame126("Hard quests", 683);

	}

}