package com.ownxile.core.task.impl;

import com.ownxile.config.GameConfig;
import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.items.ItemAssistant;
import com.ownxile.rs2.player.Client;
import com.ownxile.util.Misc;
import com.ownxile.util.file.FileLog;

public class LotteryUpdating extends Task {

	public LotteryUpdating(int delay) {
		super(delay);
	}

	@Override
	protected void execute() {
		Client player = (Client) World.getPlayerHandler().getRandomPlayer();
		if (player != null) {
			int item = GameConfig.LOTTERY_ITEMS[Misc
					.random(GameConfig.LOTTERY_ITEMS.length - 1)];
			String itemName = ItemAssistant.getItemName(item);

			FileLog.writeLotteryWinner("Latest Bonus Reward: "
					+ player.playerName + "  won " + itemName + ".");

			World.sendMessage("@blu@"
					+ Misc.formatPlayerName(player.playerName) + " has won "
					+ itemName + " as a bonus reward.");
			player.sendMessage(itemName + " has been added to your bank.");
			player.getItems().addBankItem(item, 1);
		}

	}

}
