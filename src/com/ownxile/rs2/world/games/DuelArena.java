package com.ownxile.rs2.world.games;

import java.util.concurrent.CopyOnWriteArrayList;

import com.ownxile.config.GameConfig;
import com.ownxile.config.ItemConfig;
import com.ownxile.rs2.combat.prayer.Prayer;
import com.ownxile.rs2.items.GameItem;
import com.ownxile.rs2.items.GroundItemHandler;
import com.ownxile.rs2.items.ItemAssistant;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.PlayerHandler;
import com.ownxile.rs2.player.PlayerSave;
import com.ownxile.util.Misc;

public class DuelArena {

	private final Client c;

	public CopyOnWriteArrayList<GameItem> otherStakedItems = new CopyOnWriteArrayList<GameItem>();

	public CopyOnWriteArrayList<GameItem> stakedItems = new CopyOnWriteArrayList<GameItem>();

	public DuelArena(Client client) {
		this.c = client;
	}

	public void requestDuel(int id) {
		try {
			if (id == c.playerId)
				return;

			resetDuel();
			resetDuelItems();
			c.duelingWith = id;
			Client o = (Client) PlayerHandler.players[id];
			if (c.isIronman() || c.isUltimateIronman()) {
				c.sendMessage("You're an Iron Man and cannot duel!");
				return;
			}
			if (o.isIronman() || o.isUltimateIronman()) {
				c.sendMessage("That Player is an Iron Man and cannot duel.");
				return;
			}
			if (System.currentTimeMillis() - o.logoutDelay < 10000) {
				c.sendMessage("Other player is busy at the moment.");
				return;
			}
			if (o.inDuel || c.inDuel || c.duelStatus > 0 || o.duelStatus > 0) {
				c.sendMessage("Other player is busy at the moment.");
				return;
			}
			if (c.disconnected) {
				o.getFunction().closeAllWindows();
				c.getFunction().closeAllWindows();
				o.getTrade().declineTrade();
				c.getTrade().declineTrade();
				o.sendMessage("The trade has been declined as you have disconnected.");
				c.sendMessage("The trade has been declined because the other person disconnected.");
				return;
			}
			if (o == null) {
				return;
			}
			c.duelRequested = true;
			if (c.duelStatus == 0 && o.duelStatus == 0 && c.duelRequested
					&& o.duelRequested && c.duelingWith == o.getId()
					&& o.duelingWith == c.getId()) {
				if (c.goodDistance(c.getX(), c.getY(), o.getX(), o.getY(), 1)) {
					c.getDuel().openDuel();
					o.getDuel().openDuel();
				} else {
					c.sendMessage("You need to get closer to your opponent to start the duel.");
				}

			} else {
				c.sendMessage("Sending duel request...");
				o.sendMessage(c.playerName + ":duelreq:");
			}
		} catch (Exception e) {
			Misc.println("Error requesting duel.");
		}
	}

	public void openDuel() {
		Client o = (Client) PlayerHandler.players[c.duelingWith];
		if (o == null) {
			return;
		}
		if (c.playerRights == 10) {
			c.sendMessage("You're an Iron Man and cannot duel!");
			return;
		}
		if (o.playerRights == 10) {
			c.sendMessage("That Player is an Iron Man and cannot duel.");
			return;
		}
		if (c.duelStatus >= 1 && c.duelStatus <= 4) {
			c.getDuel().declineDuel();
			return;
		}
		c.duelStatus = 1;
		refreshduelRules();
		refreshDuelScreen();
		c.canOffer = true;
		for (int i = 0; i < c.playerEquipment.length; i++) {
			sendDuelEquipment(c.playerEquipment[i], c.playerEquipmentN[i], i);
		}
		c.getFunction().sendFrame126(
				"Dueling with: " + o.playerName + " (level-" + o.combatLevel
						+ ")", 6671);
		c.getFunction().sendFrame126("", 6684);
		c.getFunction().sendFrame248(6575, 3321);
		c.getItems().resetItems(3322);
	}

	public void sendDuelEquipment(int itemId, int amount, int slot) {
		synchronized (c) {
			if (itemId != 0) {
				c.getOutStream().createFrameVarSizeWord(34);
				c.getOutStream().writeWord(13824);
				c.getOutStream().writeByte(slot);
				c.getOutStream().writeWord(itemId + 1);

				if (amount > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord(amount);
				} else {
					c.getOutStream().writeByte(amount);
				}
				c.getOutStream().endFrameVarSizeWord();
				c.flushOutStream();
			}
		}
	}

	public void refreshduelRules() {
		for (int i = 0; i < c.duelRule.length; i++) {
			c.duelRule[i] = false;
		}
		c.getFunction().sendFrame87(286, 0);
		c.duelOption = 0;
	}

	public void refreshDuelScreen() {
		synchronized (c) {
			Client o = (Client) PlayerHandler.players[c.duelingWith];
			if (o == null) {
				return;
			}
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(6669);
			c.getOutStream().writeWord(stakedItems.toArray().length);
			int current = 0;
			for (GameItem item : stakedItems) {
				if (item.amount > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord_v2(item.amount);
				} else {
					c.getOutStream().writeByte(item.amount);
				}
				if (item.id > GameConfig.ITEM_LIMIT || item.id < 0) {
					item.id = GameConfig.ITEM_LIMIT;
				}
				c.getOutStream().writeWordBigEndianA(item.id + 1);

				current++;
			}

			if (current < 27) {
				for (int i = current; i < 28; i++) {
					c.getOutStream().writeByte(1);
					c.getOutStream().writeWordBigEndianA(-1);
				}
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();

			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(6670);
			c.getOutStream()
					.writeWord(o.getDuel().stakedItems.toArray().length);
			current = 0;
			for (GameItem item : o.getDuel().stakedItems) {
				if (item.amount > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord_v2(item.amount);
				} else {
					c.getOutStream().writeByte(item.amount);
				}
				if (item.id > GameConfig.ITEM_LIMIT || item.id < 0) {
					item.id = GameConfig.ITEM_LIMIT;
				}
				c.getOutStream().writeWordBigEndianA(item.id + 1);
				current++;
			}

			if (current < 27) {
				for (int i = current; i < 28; i++) {
					c.getOutStream().writeByte(1);
					c.getOutStream().writeWordBigEndianA(-1);
				}
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}

	public boolean stakeItem(int itemID, int fromSlot, int amount) {

		for (int i : GameConfig.UNTRADEABLE_ITEMS) {
			if (i == itemID) {
				c.sendMessage("You can't stake this item.");
				return false;
			}
		}
		if (itemID != c.playerItems[fromSlot] - 1) {
			return false;
		}
		if (c.playerRights == 2) {
			c.sendMessage("Administrator can't stake items in duel arena.");
			return false;
		}
		if (itemID == 4740 || itemID == 9244 || itemID == 11212
				|| itemID == 892 || itemID == 9194 || itemID == 9243
				|| itemID == 9242 || itemID == 9241 || itemID == 9240
				|| itemID == 9239 || itemID == 882 || itemID == 884
				|| itemID == 886 || itemID == 888 || itemID == 890) {
			c.sendMessage("You can't stake bolts or arrows.");
			return false;
		}
		if (!c.getItems().playerHasItem(itemID, amount))
			return false;
		if (c.playerItems[fromSlot] - 1 != itemID
				&& c.playerItems[fromSlot] != itemID) { // duel dupe fix by
														// Aleksandr
			if (c.playerItems[fromSlot] == 0)
				return false;
			return false;
		}
		if (amount <= 0)
			return false;
		Client o = (Client) PlayerHandler.players[c.duelingWith];
		if (c.playerRights == 10) {
			c.sendMessage("You're an Iron Man and cannot duel!");
			return false;
		}
		if (o.playerRights == 10) {
			c.sendMessage("That Player is an Iron Man and cannot duel.");
			return false;
		}
		if (o == null) {
			declineDuel();
			return false;
		}
		if (o.duelStatus <= 0 || c.duelStatus <= 0) {
			declineDuel();
			o.getDuel().declineDuel();
			return false;
		}
		if (!c.canOffer) {
			return false;
		}
		changeDuelStuff();
		if (!ItemConfig.itemStackable[itemID]) {
			for (int a = 0; a < amount; a++) {
				if (c.getItems().playerHasItem(itemID, 1)) {
					stakedItems.add(new GameItem(itemID, 1));
					c.getItems().deleteItem(itemID,
							c.getItems().getItemSlot(itemID), 1);
				}
			}
			c.getItems().resetItems(3214);
			c.getItems().resetItems(3322);
			o.getItems().resetItems(3214);
			o.getItems().resetItems(3322);
			refreshDuelScreen();
			o.getDuel().refreshDuelScreen();
			c.getFunction().sendFrame126("", 6684);
			o.getFunction().sendFrame126("", 6684);
		}

		if (!c.getItems().playerHasItem(itemID, amount)) {
			return false;
		}
		if (ItemConfig.itemStackable[itemID] || ItemConfig.itemIsNote[itemID]) {
			boolean found = false;
			for (GameItem item : stakedItems) {
				if (item.id == itemID) {
					found = true;
					item.amount += amount;
					c.getItems().deleteItem(itemID, fromSlot, amount);
					break;
				}
			}
			if (!found) {
				c.getItems().deleteItem(itemID, fromSlot, amount);
				stakedItems.add(new GameItem(itemID, amount));
			}
		}

		c.getItems().resetItems(3214);
		c.getItems().resetItems(3322);
		o.getItems().resetItems(3214);
		o.getItems().resetItems(3322);
		refreshDuelScreen();
		o.getDuel().refreshDuelScreen();
		c.getFunction().sendFrame126("", 6684);
		o.getFunction().sendFrame126("", 6684);
		return true;
	}

	public boolean fromDuel(int itemID, int fromSlot, int amount) {
		Client o = (Client) PlayerHandler.players[c.duelingWith];
		if (o == null) {
			declineDuel();
			return false;
		}
		if (!c.getItems().playerHasItem(itemID, amount)) {
			return false;
		}
		if (o.duelStatus <= 0 || c.duelStatus <= 0) {
			declineDuel();
			o.getDuel().declineDuel();
			return false;
		}
		if (ItemConfig.itemStackable[itemID]) {
			if (c.getItems().freeSlots() - 1 < (c.duelSpaceReq)) {
				c.sendMessage("You have too many rules set to remove that item.");
				return false;
			}
		}

		if (!c.canOffer) {
			return false;
		}

		changeDuelStuff();
		boolean goodSpace = true;
		if (!ItemConfig.itemStackable[itemID]) {
			for (int a = 0; a < amount; a++) {
				for (GameItem item : stakedItems) {
					if (item.id == itemID) {
						if (!item.stackable) {
							if (c.getItems().freeSlots() - 1 < (c.duelSpaceReq)) {
								goodSpace = false;
								break;
							}
							stakedItems.remove(item);
							c.getItems().addItem(itemID, 1);
						} else {
							if (c.getItems().freeSlots() - 1 < (c.duelSpaceReq)) {
								goodSpace = false;
								break;
							}
							if (item.amount > amount) {
								item.amount -= amount;
								c.getItems().addItem(itemID, amount);
							} else {
								if (c.getItems().freeSlots() - 1 < (c.duelSpaceReq)) {
									goodSpace = false;
									break;
								}
								amount = item.amount;
								stakedItems.remove(item);
								c.getItems().addItem(itemID, amount);
							}
						}
						break;
					}
					o.duelStatus = 1;
					c.duelStatus = 1;
					c.getItems().resetItems(3214);
					c.getItems().resetItems(3322);
					o.getItems().resetItems(3214);
					o.getItems().resetItems(3322);
					c.getDuel().refreshDuelScreen();
					o.getDuel().refreshDuelScreen();
					o.getFunction().sendFrame126("", 6684);
				}
			}
		}

		for (GameItem item : stakedItems) {
			if (item.id == itemID) {
				if (!item.stackable) {
				} else {
					if (item.amount > amount) {
						item.amount -= amount;
						c.getItems().addItem(itemID, amount);
					} else {
						amount = item.amount;
						stakedItems.remove(item);
						c.getItems().addItem(itemID, amount);
					}
				}
				break;
			}
		}
		o.duelStatus = 1;
		c.duelStatus = 1;
		c.getItems().resetItems(3214);
		c.getItems().resetItems(3322);
		o.getItems().resetItems(3214);
		o.getItems().resetItems(3322);
		c.getDuel().refreshDuelScreen();
		o.getDuel().refreshDuelScreen();
		o.getFunction().sendFrame126("", 6684);
		if (!goodSpace) {
			c.sendMessage("You have too many rules set to remove that item.");
			return true;
		}
		return true;
	}

	public void confirmDuel() {
		Client o = (Client) PlayerHandler.players[c.duelingWith];
		if (o == null) {
			declineDuel();
			return;
		}
		String itemId = "";
		for (GameItem item : stakedItems) {
			if (ItemConfig.itemStackable[item.id]
					|| ItemConfig.itemIsNote[item.id]) {
				c.getItems();
				itemId += ItemAssistant.getItemName(item.id) + " x "
						+ Misc.format(item.amount) + "\\n";
			} else {
				c.getItems();
				itemId += ItemAssistant.getItemName(item.id) + "\\n";
			}
		}
		c.getFunction().sendFrame126(itemId, 6516);
		itemId = "";
		for (GameItem item : o.getDuel().stakedItems) {
			if (ItemConfig.itemStackable[item.id]
					|| ItemConfig.itemIsNote[item.id]) {
				c.getItems();
				itemId += ItemAssistant.getItemName(item.id) + " x "
						+ Misc.format(item.amount) + "\\n";
			} else {
				c.getItems();
				itemId += ItemAssistant.getItemName(item.id) + "\\n";
			}
		}
		c.getFunction().sendFrame126(itemId, 6517);
		c.getFunction().sendFrame126("", 8242);
		for (int i = 8238; i <= 8253; i++) {
			c.getFunction().sendFrame126("", i);
		}
		c.getFunction().sendFrame126("Hitpoints will be restored.", 8250);
		c.getFunction().sendFrame126("Boosted stats will be restored.", 8238);
		if (c.duelRule[8]) {
			c.getFunction().sendFrame126(
					"There will be obstacles in the arena.", 8239);
		}
		c.getFunction().sendFrame126("", 8240);
		c.getFunction().sendFrame126("", 8241);

		String[] rulesOption = { "Players cannot forfeit!",
				"Players cannot move.", "Players cannot use range.",
				"Players cannot use melee.", "Players cannot use magic.",
				"Players cannot drink pots.", "Players cannot eat food.",
				"Players cannot use prayer." };

		int lineNumber = 8242;
		for (int i = 0; i < 8; i++) {
			if (c.duelRule[i]) {
				c.getFunction().sendFrame126("" + rulesOption[i], lineNumber);
				lineNumber++;
			}
		}
		c.getFunction().sendFrame126("", 6571);
		c.getFunction().sendFrame248(6412, 197);
		// c.getFunction().showInterface(6412);
	}

	public void startDuel() {
		c.freezeTimer = 2;
		c.getFunction().resetFollow();
		Client o = (Client) PlayerHandler.players[c.duelingWith];
		if (o.disconnected) {
			duelVictory();
		}
		if (o == null) {
			duelVictory();
		}
		c.headIconHints = 2;
		c.vengOn = false;

		if (c.duelRule[7]) {
			for (int p = 0; p < c.PRAYER.length; p++) { // reset prayer glows
				c.prayerActive[p] = false;
				c.getFunction().sendFrame36(c.PRAYER_GLOW[p], 0);
			}
			c.headIcon = -1;
			c.getFunction().requestUpdates();
		}
		if (c.duelRule[11]) {
			c.getItems().removeItem(c.playerEquipment[0], 0);
		}
		if (c.duelRule[12]) {
			c.getItems().removeItem(c.playerEquipment[1], 1);
		}
		if (c.duelRule[13]) {
			c.getItems().removeItem(c.playerEquipment[2], 2);
		}
		if (c.duelRule[14]) {
			c.getItems().removeItem(c.playerEquipment[3], 3);
		}
		if (c.duelRule[15]) {
			c.getItems().removeItem(c.playerEquipment[4], 4);
		}
		if (c.duelRule[16]) {
			if (c.getItems()
					.is2handed(
							c.getItems().getItemName(
									c.playerEquipment[c.playerWeapon]),
							c.playerEquipment[c.playerWeapon])) {
				c.getItems().removeItem(c.playerEquipment[3], 3);
			}
			c.getItems().removeItem(c.playerEquipment[5], 5);
		}
		if (c.duelRule[17]) {
			c.getItems().removeItem(c.playerEquipment[7], 7);
		}
		if (c.duelRule[18]) {
			c.getItems().removeItem(c.playerEquipment[9], 9);
		}
		if (c.duelRule[19]) {
			c.getItems().removeItem(c.playerEquipment[10], 10);
		}
		if (c.duelRule[20]) {
			c.getItems().removeItem(c.playerEquipment[12], 12);
		}
		if (c.duelRule[21]) {
			c.getItems().removeItem(c.playerEquipment[13], 13);
		}
		c.duelStatus = 5;
		c.getFunction().removeAllWindows();
		c.specAmount = 10;
		c.getItems().addSpecialBar(c.playerEquipment[c.playerWeapon]);

		if (c.duelRule[8]) {
			if (c.duelRule[1]) {
				c.getFunction().movePlayer(c.duelTeleX, c.duelTeleY, 0);
			} else {
				c.getFunction().movePlayer(3366 + Misc.random(12),
						3246 + Misc.random(6), 0);
			}
		} else {
			if (c.duelRule[1]) {
				c.getFunction().movePlayer(c.duelTeleX, c.duelTeleY, 0);
			} else {
				c.getFunction().movePlayer(3335 + Misc.random(12),
						3246 + Misc.random(6), 0);
			}
		}

		c.getFunction().createPlayerHint(10, o.playerId);
		c.getFunction().showOption(3, 0, "Attack");
		for (int i = 0; i < 20; i++) {
			c.playerLevel[i] = c.getFunction().getLevelForXP(c.playerXP[i]);
			c.getFunction().refreshSkill(i);
		}
		for (GameItem item : o.getDuel().stakedItems) {
			otherStakedItems.add(new GameItem(item.id, item.amount));
		}
		c.getFunction().requestUpdates();
	}

	public void duelVictory() {
		Client o = (Client) PlayerHandler.players[c.duelingWith];
		if (o != null) {
			c.getFunction().sendFrame126("" + o.combatLevel, 6839);
			c.getFunction().sendFrame126(o.playerName, 6840);
			o.duelStatus = 0;
			c.freezeTimer = 3;
		} else {
			c.getFunction().sendFrame126("", 6839);
			c.getFunction().sendFrame126("", 6840);
		}
		c.duelStatus = 6;
		Prayer.resetPrayers(c);
		for (int i = 0; i < 20; i++) {
			c.playerLevel[i] = c.getFunction().getLevelForXP(c.playerXP[i]);
			c.getFunction().refreshSkill(i);
		}
		c.getFunction().refreshSkill(3);
		// c.getFunction().refreshSkill(i);
		c.specAmount = 10;
		c.getItems().addSpecialBar(c.playerEquipment[c.playerWeapon]);
		// you mean?
		c.getFunction().movePlayer(
				GameConfig.DUELING_RESPAWN_X
						+ (Misc.random(GameConfig.RANDOM_DUELING_RESPAWN)),
				GameConfig.DUELING_RESPAWN_Y
						+ (Misc.random(GameConfig.RANDOM_DUELING_RESPAWN)), 0);
		o.absX = GameConfig.DUELING_RESPAWN_X
				+ (Misc.random(GameConfig.RANDOM_DUELING_RESPAWN));
		o.absY = GameConfig.DUELING_RESPAWN_Y
				+ (Misc.random(GameConfig.RANDOM_DUELING_RESPAWN));
		c.getFunction().requestUpdates();
		c.getFunction().showOption(3, 0, "Challenge");
		c.getFunction().createPlayerHint(10, -1);
		duelRewardInterface();
		PlayerSave.saveGame(c);
		c.getFunction().showInterface(6733);// /what u change before u said?
											// what do
		c.canOffer = true;
		c.duelSpaceReq = 0;
		c.duelingWith = 0;
		c.freezeTimer = 1;
		c.getFunction().resetFollow();
		c.getCombat().resetPlayerAttack();
		c.duelRequested = false;
	}

	public void duelRewardInterface() {
		synchronized (c) {
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(6822);
			c.getOutStream().writeWord(otherStakedItems.toArray().length);
			for (GameItem item : otherStakedItems) {
				if (item.amount > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord_v2(item.amount);
				} else {
					c.getOutStream().writeByte(item.amount);
				}
				if (item.id > GameConfig.ITEM_LIMIT || item.id < 0) {
					item.id = GameConfig.ITEM_LIMIT;
				}
				c.getOutStream().writeWordBigEndianA(item.id + 1);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}

	public void claimStakedItems() {
		for (GameItem item : otherStakedItems) {
			if (item.id > 0 && item.amount > 0) {
				if (ItemConfig.itemStackable[item.id]) {
					if (!c.getItems().addItem(item.id, item.amount)) {
						GroundItemHandler.createGroundItem(c, item.id,
								c.getX(), c.getY(), c.getZ(), item.amount,
								c.getId());
					}
				} else {
					int amount = item.amount;
					for (int a = 1; a <= amount; a++) {
						if (!c.getItems().addItem(item.id, 1)) {
							GroundItemHandler.createGroundItem(c, item.id,
									c.getX(), c.getY(), c.getZ(), 1, c.getId());
						}
					}
				}
			}
		}
		for (GameItem item : stakedItems) {
			if (item.id > 0 && item.amount > 0) {
				if (ItemConfig.itemStackable[item.id]) {
					if (!c.getItems().addItem(item.id, item.amount)) {
						GroundItemHandler.createGroundItem(c, item.id,
								c.getX(), c.getY(), c.getZ(), item.amount,
								c.getId());
					}
				} else {
					int amount = item.amount;
					for (int a = 1; a <= amount; a++) {
						if (!c.getItems().addItem(item.id, 1)) {
							GroundItemHandler.createGroundItem(c, item.id,
									c.getX(), c.getY(), c.getZ(), 1, c.getId());
						}
					}
				}
			}
		}
		resetDuel();
		resetDuelItems();
		c.duelStatus = 0;
	}

	public void declineDuel() {
		Client o = (Client) PlayerHandler.players[c.duelingWith];
		if (c.duelStatus >= 1 && c.duelStatus <= 4) {
			c.sendMessage("Duel was declined.");
			if (o == null)
				return;
			o.sendMessage("Duel was declined.");
		}
		// c.duelStatus = 0;
		declineDuel(true);
	}

	public void declineDuel(boolean tellOther) {
		c.getFunction().removeAllWindows();
		Client o = (Client) PlayerHandler.players[c.duelingWith];
		if (o == null) {
			return;
		}

		if (tellOther) {
			o.getDuel().declineDuel(false);
			c.getFunction().removeAllWindows();
		}

		c.canOffer = true;
		c.duelStatus = 0;
		c.duelingWith = 0;
		c.duelSpaceReq = 0;
		c.duelRequested = false;
		for (GameItem item : stakedItems) {
			if (item.amount < 1)
				continue;
			if (ItemConfig.itemStackable[item.id]
					|| ItemConfig.itemIsNote[item.id]) {
				c.getItems().addItem(item.id, item.amount);
			} else {
				c.getItems().addItem(item.id, 1);
			}
		}
		stakedItems.clear();
		for (int i = 0; i < c.duelRule.length; i++) {
			c.duelRule[i] = false;
		}
	}

	public void resetDuel() {
		c.getFunction().showOption(3, 0, "Challenge");
		c.headIconHints = 0;
		for (int i = 0; i < c.duelRule.length; i++) {
			c.duelRule[i] = false;
		}
		c.getFunction().createPlayerHint(10, -1);
		c.duelStatus = 0;
		c.canOffer = true;
		c.duelSpaceReq = 0;
		c.duelingWith = 0;
		c.freezeTimer = 1;
		c.getFunction().resetFollow();
		c.getFunction().requestUpdates();
		c.getCombat().resetPlayerAttack();
		c.duelRequested = false;
	}

	public void resetDuelItems() {
		stakedItems.clear();
		otherStakedItems.clear();
	}

	public void changeDuelStuff() {
		Client o = (Client) PlayerHandler.players[c.duelingWith];
		if (o == null) {
			return;
		}
		o.duelStatus = 1;
		c.duelStatus = 1;
		o.getFunction().sendFrame126("", 6684);
		c.getFunction().sendFrame126("", 6684);
	}

	public void selectRule(int i) { // rules
		Client o = (Client) PlayerHandler.players[c.duelingWith];
		if (o == null) {
			return;
		}
		if (!c.canOffer)
			return;
		changeDuelStuff();
		o.duelSlot = c.duelSlot;
		if (i >= 11 && c.duelSlot > -1) {
			if (c.playerEquipment[c.duelSlot] > 0) {
				if (!c.duelRule[i]) {
					c.duelSpaceReq++;
				} else {
					c.duelSpaceReq--;
				}
			}
			if (o.playerEquipment[o.duelSlot] > 0) {
				if (!o.duelRule[i]) {
					o.duelSpaceReq++;
				} else {
					o.duelSpaceReq--;
				}
			}
		}

		if (i >= 11) {
			if (c.getItems().freeSlots() < (c.duelSpaceReq)
					|| o.getItems().freeSlots() < (o.duelSpaceReq)) {
				c.sendMessage("You or your opponent don't have the required space to set this rule.");
				if (c.playerEquipment[c.duelSlot] > 0) {
					c.duelSpaceReq--;
				}
				if (o.playerEquipment[o.duelSlot] > 0) {
					o.duelSpaceReq--;
				}
				return;
			}
		}

		if (!c.duelRule[i]) {
			c.duelRule[i] = true;
			c.duelOption += c.DUEL_RULE_ID[i];
		} else {
			c.duelRule[i] = false;
			c.duelOption -= c.DUEL_RULE_ID[i];
		}

		c.getFunction().sendFrame87(286, c.duelOption);
		o.duelOption = c.duelOption;
		o.duelRule[i] = c.duelRule[i];
		o.getFunction().sendFrame87(286, o.duelOption);

		if (c.duelRule[8]) {
			if (c.duelRule[1]) {
				c.duelTeleX = 3366 + Misc.random(12);
				o.duelTeleX = c.duelTeleX - 1;
				c.duelTeleY = 3246 + Misc.random(6);
				o.duelTeleY = c.duelTeleY;
			}
		} else {
			if (c.duelRule[1]) {
				c.duelTeleX = 3335 + Misc.random(12);
				o.duelTeleX = c.duelTeleX - 1;
				c.duelTeleY = 3246 + Misc.random(6);
				o.duelTeleY = c.duelTeleY;
			}
		}

	}

	public static boolean twoTraders(Client c, Client o) {
		int count = 0;
		for (int i = 0; i < PlayerHandler.players.length; i++) {
			Client temp = (Client) PlayerHandler.players[i];
			if (temp != null) {
				if (temp.tradeWith == c.playerId
						|| temp.tradeWith == o.playerId) {
					count++;
				}
			}
		}
		return count == 2;
	}

	public void bothDeclineDuel() {
		Client c3 = (Client) PlayerHandler.players[c.duelingWith];
		declineDuel();
		c3.getDuel().declineDuel();
		c.sendMessage("@red@The duel has been declined.");
		c3.sendMessage("@red@The duel has been declined.");
	}

	public void bothDeclineTrade(Client client) {
		Client o = (Client) PlayerHandler.players[c.tradeWith];
		if (o == null) {
			System.out.println("Trade Partner Found Null");
			return;
		}
		client.getTrade().declineTrade();
		o.getTrade().declineTrade();
		client.sendMessage("@red@The trade has been declined.");
		o.sendMessage("@red@The trade has been declined.");
		/*
		 * c.getFunction().removeAllWindows();
		 * c3.getFunction().removeAllWindows();
		 */
	}
}