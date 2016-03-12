package com.ownxile.rs2.world.games;

import com.ownxile.core.Plugin;
import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.player.Client;
import com.ownxile.util.Misc;
import com.ownxile.util.file.FileLog;

public class Gambling {

	private static Gambling INSTANCE = new Gambling();

	public static Gambling getInstance() {
		return INSTANCE;
	}

	private final int DEFAULT_POT_SIZE = 1000000;
	public long lastWin;
	public int lotteryPot = DEFAULT_POT_SIZE;
	private String lotteryWinner = "Nobody";
	private final int POT_LIMIT = 2147000000;
	private final int TICKET_PRICE = 100000;

	public boolean canAfford(Client client, int amount) {
		if (client.getItems().playerHasItem(995, amount)) {
			return true;
		} else {
			client.sendMessage("You can't afford to do this.");
			return false;
		}
	}

	public void submit(final Client client) {
		client.getFunction().closeAllWindows();
		client.getItems().deleteItem2(995, TICKET_PRICE);
		client.isGambling = true;
		World.getSynchronizedTaskScheduler().schedule(new Task(1, true) {
			int i = 5;

			@Override
			protected void execute() {

				if (i == 0) {
					run(client);
					stop();
				} else {
					client.boxMessage(i + "...");
					i--;
				}
			}
		});
	}

	private void gM(String s) {
		Plugin.execute("gambler_announce", s);
	}

	public void lose(Client client) {
		lotteryPot += TICKET_PRICE;
		client.boxMessage("You lose!");
		gM("Unlucky " + client.playerName + ", you lose!");
		client.nextChat = 0;
	}

	public String getLastWinner() {
		return lotteryWinner;
	}

	public String getPot() {
		if (lotteryPot >= 0 && lotteryPot < 10000) {
			return String.valueOf(lotteryPot);
		}
		if (lotteryPot >= 10000 && lotteryPot < 10000000) {
			return lotteryPot / 1000 + "K";
		}
		if (lotteryPot >= 10000000 && lotteryPot < 999999999) {
			return lotteryPot / 1000000 + "M";
		}
		if (lotteryPot >= 999999999) {
			return "*";
		} else {
			return "?";
		}
	}

	public void run(Client client) {
		if (Misc.random(150) == 1 && lotteryPot > DEFAULT_POT_SIZE
				&& client.getFunction().canAfford(TICKET_PRICE)
				&& client.playerRights < 2) {
			winPot(client);
		} else {
			lose(client);
		}
		client.isGambling = false;
	}

	public void winPot(Client client) {
		if (client.getFunction().canAfford(TICKET_PRICE)) {
			if (lotteryPot >= POT_LIMIT) {
				lotteryPot = POT_LIMIT;
			}
			client.getItems().addItem(995, lotteryPot);
			client.sendMessage("You win and receive " + getPot()
					+ " gold coins .");
			gM(client.playerName + " is the winner!");
			World.sendMessage("@grd@" + client.playerName + " received "
					+ getPot() + " from a succesful gamble.");
			FileLog.writeLog("gamble_winner", "The last gambler to win was "
					+ client.playerName + " who won " + getPot() + ".");
			lotteryWinner = client.playerName;
			lastWin = System.currentTimeMillis();
			client.nextChat = 0;
			lotteryPot = DEFAULT_POT_SIZE;
		}
	}
}
