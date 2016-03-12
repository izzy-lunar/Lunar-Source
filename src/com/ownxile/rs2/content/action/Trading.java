package com.ownxile.rs2.content.action;

import java.util.concurrent.CopyOnWriteArrayList;

import com.ownxile.config.GameConfig;
import com.ownxile.config.ItemConfig;
import com.ownxile.rs2.items.GameItem;
import com.ownxile.rs2.items.ItemAssistant;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.PlayerHandler;
import com.ownxile.rs2.player.PlayerSave;
import com.ownxile.util.Misc;

public class Trading {

	public CopyOnWriteArrayList<GameItem> offeredItems = new CopyOnWriteArrayList<GameItem>();

	private final Client player;

	public Trading(Client Client) {
		player = Client;
	}

	public void confirmScreen() {
		final Client o = (Client) PlayerHandler.players[player.tradeWith];
		if (o == null) {
			return;
		}
		player.canOffer = false;
		player.getItems().resetItems(3214);
		String SendTrade = "Absolutely nothing!";
		String SendAmount = "";
		int Count = 0;
		for (final GameItem item : offeredItems) {
			if (item.id > 0) {
				if (item.amount >= 1000 && item.amount < 1000000) {
					SendAmount = "@cya@" + item.amount / 1000 + "K @whi@("
							+ Misc.format(item.amount) + ")";
				} else if (item.amount >= 1000000) {
					SendAmount = "@gre@" + item.amount / 1000000
							+ " million @whi@(" + Misc.format(item.amount)
							+ ")";
				} else {
					SendAmount = "" + Misc.format(item.amount);
				}

				if (Count == 0) {
					player.getItems();
					SendTrade = ItemAssistant.getItemName(item.id);
				} else {
					player.getItems();
					SendTrade = SendTrade + "\\n"
							+ ItemAssistant.getItemName(item.id);
				}

				if (item.stackable) {
					SendTrade = SendTrade + " x " + SendAmount;
				}
				Count++;
			}
		}

		player.getFunction().sendFrame126(SendTrade, 3557);
		SendTrade = "Absolutely nothing!";
		SendAmount = "";
		Count = 0;

		for (final GameItem item : o.getTrade().offeredItems) {
			if (item.id > 0) {
				if (item.amount >= 1000 && item.amount < 1000000) {
					SendAmount = "@cya@" + item.amount / 1000 + "K @whi@("
							+ Misc.format(item.amount) + ")";
				} else if (item.amount >= 1000000) {
					SendAmount = "@gre@" + item.amount / 1000000
							+ " million @whi@(" + Misc.format(item.amount)
							+ ")";
				} else {
					SendAmount = "" + Misc.format(item.amount);
				}

				if (Count == 0) {
					player.getItems();
					SendTrade = ItemAssistant.getItemName(item.id);
				} else {
					player.getItems();
					SendTrade = SendTrade + "\\n"
							+ ItemAssistant.getItemName(item.id);
				}
				if (item.stackable) {
					SendTrade = SendTrade + " x " + SendAmount;
				}
				Count++;
			}
		}
		player.getFunction().sendFrame126(SendTrade, 3558);
		// TODO: find out what 197 does eee 3213
		player.getFunction().sendFrame248(3443, 197);
	}

	public void declineTrade() {
		player.tradeStatus = 0;
		declineTrade(true);
	}

	public void declineTrade(boolean tellOther) {
		player.getFunction().removeAllWindows();
		if (System.currentTimeMillis() - player.tradeAction < 1000) {
			return;
		}
		Client o = (Client) PlayerHandler.players[player.tradeWith];
		player.tradeAction = System.currentTimeMillis();
		if (o == null) {
			return;
		}
		o.tradeAction = System.currentTimeMillis();
		if (tellOther) {
			o.sendMessage("Other player declined trade.");
			o.getTrade().declineTrade(false);
			o.getTrade().player.getFunction().removeAllWindows();
		}

		for (final GameItem item : offeredItems) {
			if (item.amount < 1) {
				continue;
			}
			if (item.stackable) {
				player.getItems().addItem(item.id, item.amount);
			} else {
				for (int i = 0; i < item.amount; i++) {
					player.getItems().addItem(item.id, 1);
				}
			}
		}
		player.canOffer = true;
		player.tradeConfirmed = false;
		player.tradeConfirmed2 = false;
		offeredItems.clear();
		player.inTrade = false;
		player.tradeWith = 0;
	}

	public boolean fromTrade(int itemID, int fromSlot, int amount) {
		final Client o = (Client) PlayerHandler.players[player.tradeWith];
		if (o == null) {
			return false;
		}
		try {
			if (!player.inTrade || !player.canOffer) {
				declineTrade();
				return false;
			}
			if (!player.getItems().playerHasItem(itemID, amount)) {
				return false;
			}
			player.tradeConfirmed = false;
			o.tradeConfirmed = false;
			if (!ItemConfig.itemStackable[itemID]) {
				for (int a = 0; a < amount; a++) {
					for (final GameItem item : offeredItems) {
						if (item.id == itemID) {
							if (!item.stackable) {
								offeredItems.remove(item);
								player.getItems().addItem(itemID, 1);
							} else {
								if (item.amount > amount) {
									item.amount -= amount;
									player.getItems().addItem(itemID, amount);
								} else {
									amount = item.amount;
									offeredItems.remove(item);
									player.getItems().addItem(itemID, amount);
								}
							}
							break;
						}
						player.tradeConfirmed = false;
						o.tradeConfirmed = false;
						player.getItems().resetItems(3322);
						resetTItems(3415);
						o.getTrade().resetOTItems(3416);
						player.getFunction().sendFrame126("", 3431);
						o.getFunction().sendFrame126("", 3431);
					}
				}
			}
			for (final GameItem item : offeredItems) {
				if (item.id == itemID) {
					if (!item.stackable) {
					} else {
						if (item.amount > amount) {
							item.amount -= amount;
							player.getItems().addItem(itemID, amount);
						} else {
							amount = item.amount;
							offeredItems.remove(item);
							player.getItems().addItem(itemID, amount);
						}
					}
					break;
				}
			}
			player.tradeConfirmed = false;
			o.tradeConfirmed = false;
			player.getItems().resetItems(3322);
			resetTItems(3415);
			o.getTrade().resetOTItems(3416);
			player.getFunction().sendFrame126("", 3431);
			o.getFunction().sendFrame126("", 3431);
		} catch (final Exception e) {
		}
		return true;
	}

	public void giveItems() {
		final Client o = (Client) PlayerHandler.players[player.tradeWith];
		if (o == null) {
			return;
		}
		if (System.currentTimeMillis() - player.tradeAction < 1000) {
			return;
		}
		player.tradeAction = System.currentTimeMillis();
		if (o.tradeWith != player.playerId || player.tradeWith != o.playerId) {
			player.getTrade().declineTrade();
			return;
		}
		try {
			for (final GameItem item : o.getTrade().offeredItems) {
				if (item.id > 0) {
					player.getItems().addItem(item.id, item.amount);
				}
			}
			player.getFunction().removeAllWindows();
			PlayerSave.saveGame(player);

			player.sendMessage("Accepted trade.");
			player.tradeResetNeeded = true;
		} catch (final Exception e) {
		}
	}

	public void openTrade() {
		final Client o = (Client) PlayerHandler.players[player.tradeWith];

		if (o == null) {
			return;
		}
		if (player.isBanking || o.isBanking) {
			player.getFunction().closeAllWindows();
			o.getFunction().closeAllWindows();
			return;
		}
		if (player.playerRights == 3) {
			return;
		}
		player.inTrade = true;
		player.canOffer = true;
		player.tradeStatus = 1;
		player.tradeRequested = false;
		player.getItems().resetItems(3322);
		resetTItems(3415);
		resetOTItems(3416);
		String out = o.playerName;

		if (o.playerRights == 1) {
			out = "@cr1@" + out;
		} else if (o.playerRights == 2) {
			out = "@cr2@" + out;
		}
		player.getFunction()
				.sendFrame126("Trading with: " + o.playerName, 3417);
		player.getFunction().sendFrame126("", 3431);
		player.getFunction().sendFrame126(
				"Are you sure you want to make this trade?", 3535);
		player.getFunction().sendFrame248(3323, 3321);
	}

	public void requestTrade(int id) {
		try {
			final Client o = (Client) PlayerHandler.players[id];
			player.turnPlayerTo(o.absX, o.absY);

			if (o.playerIsBusy()) {
				player.sendMessage("The other player is busy at the moment.");
				return;
			}
			if (GameConfig.DISABLE_SAME_IP_TRADING
					&& player.connectedFrom.equalsIgnoreCase(o.connectedFrom)) {
				player.sendMessage("You can'trade someone on the same address as you.");
				return;
			}
			if (player.inWild()) {
				player.sendMessage("You can't trade Player vs Player enabled area.");
				return;
			}
			if (player.inTrade) {
				return;
			}
			if (o.isDead) {
				player.sendMessage("You can't trade this person right now.");
				return;
			}
			if (id == player.playerId) {
				return;
			}
			player.tradeWith = id;
			if (id < 1)
				return;
			if (!player.inTrade && o.tradeRequested
					&& o.tradeWith == player.playerId) {

				player.getTrade().openTrade();
				o.getTrade().openTrade();
			} else if (!player.inTrade) {

				player.tradeRequested = true;
				player.sendMessage("Sending trade request...");
				o.sendMessage(player.playerName + ":tradereq:");
			}
		} catch (final Exception e) {
		}
	}

	public void resetOTItems(int WriteFrame) {
		synchronized (player) {
			final Client o = (Client) PlayerHandler.players[player.tradeWith];
			if (o == null) {
				return;
			}
			player.getOutStream().createFrameVarSizeWord(53);
			player.getOutStream().writeWord(WriteFrame);
			final int len = o.getTrade().offeredItems.toArray().length;
			int current = 0;
			player.getOutStream().writeWord(len);
			for (final GameItem item : o.getTrade().offeredItems) {
				if (item.amount > 254) {
					player.getOutStream().writeByte(255); // item's stack count.
															// if
															// over 254, write
															// byte
															// 255
					player.getOutStream().writeDWord_v2(item.amount);
				} else {
					player.getOutStream().writeByte(item.amount);
				}
				player.getOutStream().writeWordBigEndianA(item.id + 1); // item
																		// id
				current++;
			}
			if (current < 27) {
				for (int i = current; i < 28; i++) {
					player.getOutStream().writeByte(1);
					player.getOutStream().writeWordBigEndianA(-1);
				}
			}
			player.getOutStream().endFrameVarSizeWord();
			player.flushOutStream();
		}
	}

	public void resetTItems(int WriteFrame) {
		synchronized (player) {
			player.getOutStream().createFrameVarSizeWord(53);
			player.getOutStream().writeWord(WriteFrame);
			final int len = offeredItems.toArray().length;
			int current = 0;
			player.getOutStream().writeWord(len);
			for (final GameItem item : offeredItems) {
				if (item.amount > 254) {
					player.getOutStream().writeByte(255);
					player.getOutStream().writeDWord_v2(item.amount);
				} else {
					player.getOutStream().writeByte(item.amount);
				}
				player.getOutStream().writeWordBigEndianA(item.id + 1);
				current++;
			}
			if (current < 27) {
				for (int i = current; i < 28; i++) {
					player.getOutStream().writeByte(1);
					player.getOutStream().writeWordBigEndianA(-1);
				}
			}
			player.getOutStream().endFrameVarSizeWord();
			player.flushOutStream();
		}
	}

	public void resetTrade() {
		offeredItems.clear();
		player.inTrade = false;
		player.tradeWith = 0;
		player.canOffer = true;
		player.tradeConfirmed = false;
		player.tradeConfirmed2 = false;
		player.acceptedTrade = false;
		player.getFunction().removeAllWindows();
		player.tradeResetNeeded = false;
		player.getFunction().sendFrame126(
				"Are you sure you want to make this trade?", 3535);
	}

	public boolean tradeItem(int itemID, int fromSlot, int amount) {
		final Client o = (Client) PlayerHandler.players[player.tradeWith];
		if (o == null) {
			return false;
		}

		for (final int i : GameConfig.UNTRADEABLE_ITEMS) {
			if (i == itemID) {
				player.sendMessage("You can't trade this item.");
				return false;
			}
		}
		if (!(player.playerItems[fromSlot] == itemID + 1 && player.playerItemsN[fromSlot] >= amount)) {
			player.sendMessage("You don't have a high enough amount.");
			return false;
		}

		player.tradeConfirmed = false;
		o.tradeConfirmed = false;
		if (itemID != 995 & player.playerItems[fromSlot] == 996) {
			return false;
		}
		if (amount > player.playerItemsN[fromSlot]) {
			return false;
		}
		if (amount > 0 && itemID == player.playerItems[fromSlot] - 1) {
			if (amount > player.playerItemsN[fromSlot]) {
				amount = player.playerItemsN[fromSlot];
			}
			if (!ItemConfig.itemStackable[itemID]
					&& !ItemConfig.itemIsNote[itemID]) {
				for (int a = 0; a < amount; a++) {
					if (player.getItems().playerHasItem(itemID, 1)) {
						offeredItems.add(new GameItem(itemID, 1));
						player.getItems().deleteItem(itemID,
								player.getItems().getItemSlot(itemID), 1);
					}
				}
				player.getItems().resetItems(3322);
				resetTItems(3415);
				o.getTrade().resetOTItems(3416);
				player.getFunction().sendFrame126("", 3431);
				o.getFunction().sendFrame126("", 3431);
			}
			if (player.getItems().getItemCount(itemID) < amount) {
				amount = player.getItems().getItemCount(itemID);
				if (amount == 0) {
					return false;
				}
			}
			if (!player.inTrade || !player.canOffer) {
				declineTrade();
				return false;
			}

			if (ItemConfig.itemStackable[itemID]
					|| ItemConfig.itemIsNote[itemID]) {
				boolean inTrade = false;
				for (final GameItem item : offeredItems) {
					if (item.id == itemID) {
						inTrade = true;
						item.amount += amount;
						player.getItems().deleteItem2(itemID, amount);
						break;
					}
				}

				if (!inTrade) {
					offeredItems.add(new GameItem(itemID, amount));
					player.getItems().deleteItem2(itemID, amount);
				}
			}
		}
		player.getItems().resetItems(3322);
		resetTItems(3415);
		o.getTrade().resetOTItems(3416);
		player.getFunction().sendFrame126("", 3431);
		o.getFunction().sendFrame126("", 3431);
		return true;
	}

}
