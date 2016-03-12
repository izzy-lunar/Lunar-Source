package com.ownxile.rs2.items;

import java.util.ArrayList;
import java.util.List;

import com.ownxile.config.GameConfig;
import com.ownxile.config.ItemConfig;
import com.ownxile.rs2.npcs.NPCHandler;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.PlayerHandler;
import com.ownxile.rs2.player.PlayerSave;
import com.ownxile.rs2.world.games.CastleWars;
import com.ownxile.util.Misc;

public class ItemAssistant {

	public static final int[][] STARTER_BANK = { { 385, 10999999 },
			{ 2440, 10999999 }, { 2436, 10999999 } };

	public static String getItemName(int ItemID) {
		for (int i = 0; i < GameConfig.ITEM_LIMIT; i++) {
			if (GroundItemHandler.itemDefs[i] != null) {
				if (GroundItemHandler.itemDefs[i].itemId == ItemID) {
					return GroundItemHandler.itemDefs[i].itemName;
				}
			}
		}
		return "Unarmed";
	}

	public static int getItemValue(int itemId) {
		for (int i = 0; i < GameConfig.ITEM_LIMIT; i++) {
			if (GroundItemHandler.itemDefs[i] != null) {
				if (GroundItemHandler.itemDefs[i].itemId == itemId) {
					return (int) GroundItemHandler.itemDefs[i].shopValue;
				}
			}
		}
		return 0;
	}

	public final String[] BONUS_NAMES = { "Stab", "Slash", "Crush", "Magic",
			"Range", "Stab", "Slash", "Crush", "Magic", "Range", "Strength",
			"Prayer" };

	public int[][] brokenBarrows = { { 4708, 4860 }, { 4710, 4866 },
			{ 4712, 4872 }, { 4714, 4878 }, { 4716, 4884 }, { 4720, 4896 },
			{ 4718, 4890 }, { 4720, 4896 }, { 4722, 4902 }, { 4732, 4932 },
			{ 4734, 4938 }, { 4736, 4944 }, { 4738, 4950 }, { 4724, 4908 },
			{ 4726, 4914 }, { 4728, 4920 }, { 4730, 4926 }, { 4745, 4956 },
			{ 4747, 4926 }, { 4749, 4968 }, { 4751, 4794 }, { 4753, 4980 },
			{ 4755, 4986 }, { 4757, 4992 }, { 4759, 4998 } };

	private final Client client;

	public ItemAssistant(Client c) {
		this.client = c;
	}

	public boolean addBankItem(int item, double amount) {
		synchronized (client) {
			if (amount < 1) {
				amount = 1;
			}
			if (item <= 0) {
				return false;
			}
			for (int i = 0; i < client.bankItems.length; i++) {
				if (client.bankItems[i] == item + 1 && client.bankItems[i] > 0
						&& client.bankItemsN[i] < GameConfig.MAXITEM_AMOUNT) {
					client.bankItems[i] = item + 1;
					if (client.bankItemsN[i] + amount < GameConfig.MAXITEM_AMOUNT
							&& client.bankItemsN[i] + amount > -1) {
						client.bankItemsN[i] += amount;
					} else {
						final int old = client.bankItemsN[i];
						client.bankItemsN[i] = GameConfig.MAXITEM_AMOUNT;
						if (old + amount - (long) GameConfig.MAXITEM_AMOUNT > 0) {
							addBankItem(item, old + amount
									- (long) GameConfig.MAXITEM_AMOUNT);
						}
					}
					return true;
				}
			}
			for (int i = 0; i < client.bankItems.length; i++) {
				if (client.bankItems[i] <= 0) {
					client.bankItems[i] = item + 1;
					if (amount < GameConfig.MAXITEM_AMOUNT && amount > -1) {
						client.bankItemsN[i] = 1;
						if (amount > 1) {
							addBankItem(item, amount - 1);
							return true;
						}
					} else {
						final int old = 0;
						client.bankItemsN[i] = GameConfig.MAXITEM_AMOUNT;
						if (old + amount - (long) GameConfig.MAXITEM_AMOUNT > 0) {
							addBankItem(item, old + amount
									- (long) GameConfig.MAXITEM_AMOUNT);
						}
					}
					return true;
				}
			}
			return false;
		}
	}

	public void addItem(int id) {
		addItem(id, 1);
	}

	/**
	 * Add Item
	 **/
	public boolean addItem(int item, int amount) {
		synchronized (client) {
			if (amount < 1) {
				amount = 1;
			}
			if (item < 0) {
				return false;
			}
			if ((freeSlots() >= 1 || playerHasItem(item, 1))
					&& ItemConfig.itemStackable[item] || freeSlots() > 0
					&& !ItemConfig.itemStackable[item]) {
				for (int i = 0; i < client.playerItems.length; i++) {
					if (client.playerItems[i] == item + 1
							&& ItemConfig.itemStackable[item]
							&& client.playerItems[i] > 0) {
						client.playerItems[i] = item + 1;
						if (client.playerItemsN[i] + amount < GameConfig.MAXITEM_AMOUNT
								&& client.playerItemsN[i] + amount > -1) {
							client.playerItemsN[i] += amount;
						} else {
							client.playerItemsN[i] = GameConfig.MAXITEM_AMOUNT;
						}
						if (client.getOutStream() != null && client != null) {
							client.getOutStream().createFrameVarSizeWord(34);
							client.getOutStream().writeWord(3214);
							client.getOutStream().writeByte(i);
							client.getOutStream().writeWord(
									client.playerItems[i]);
							if (client.playerItemsN[i] > 254) {
								client.getOutStream().writeByte(255);
								client.getOutStream().writeDWord(
										client.playerItemsN[i]);
							} else {
								client.getOutStream().writeByte(
										client.playerItemsN[i]);
							}
							client.getOutStream().endFrameVarSizeWord();
							client.flushOutStream();
						}
						i = 30;
						PlayerSave.saveGame(client);
						return true;
					}
				}
				for (int i = 0; i < client.playerItems.length; i++) {
					if (client.playerItems[i] <= 0) {
						client.playerItems[i] = item + 1;
						if (amount < GameConfig.MAXITEM_AMOUNT && amount > -1) {
							client.playerItemsN[i] = 1;
							if (amount > 1) {
								client.getItems().addItem(item, amount - 1);
								return true;
							}
						} else {
							client.playerItemsN[i] = GameConfig.MAXITEM_AMOUNT;
						}
						/*
						 * if(entity.getOutStream() != null && entity != null )
						 * { entity.getOutStream().createFrameVarSizeWord(34);
						 * entity.getOutStream().writeWord(3214);
						 * entity.getOutStream().writeByte(i);
						 * entity.getOutStream
						 * ().writeWord(entity.playerItems[i]); if
						 * (entity.playerItemsN[i] > 254) {
						 * entity.getOutStream().writeByte(255);
						 * entity.getOutStream
						 * ().writeDWord(entity.playerItemsN[i]); } else {
						 * entity
						 * .getOutStream().writeByte(entity.playerItemsN[i]); }
						 * entity.getOutStream().endFrameVarSizeWord();
						 * entity.flushOutStream(); }
						 */
						resetItems(3214);
						PlayerSave.saveGame(client);
						i = 30;
						return true;
					}
				}
				return false;
			} else {
				resetItems(3214);
				client.sendMessage("Not enough space in your inventory.");
				return false;
			}
		}
	}

	public void addItemsToBank(int[][] items) {
		for (int[] item : items) {
			addItemToBank(item[0], item[1]);
		}
	}

	public void addItemToBank(int itemId, int amount) {
		for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
			if (client.bankItems[i] <= 0 || client.bankItems[i] == itemId + 1
					&& client.bankItemsN[i] + amount < Integer.MAX_VALUE) {
				client.bankItems[i] = itemId + 1;
				client.bankItemsN[i] += amount;
				resetBank();
				return;
			}
		}
	}

	// Added by Parrot

	/**
	 * Weapons special bar, adds the spec bars to weapons that require them and
	 * removes the spec bars from weapons which don't require them
	 **/

	public void addSpecialBar(int weapon) {
		switch (weapon) {
		case 4151: // whip
		case 8285:
		case 12006:
			client.getFunction().sendFrame171(0, 12323);
			specialAmount(weapon, client.specAmount, 12335);
			break;

		case 859: // magic bows
		case 861:
		case 11235:
			client.getFunction().sendFrame171(0, 7549);
			specialAmount(weapon, client.specAmount, 7561);
			break;
		case 11777:
			client.getFunction().sendFrame171(0, 7800);
			specialAmount(weapon, client.specAmount, 7812);
			break;

		case 35:
		case 10887:
		case 4587: // dscimmy
			client.getFunction().sendFrame171(0, 7599);
			specialAmount(weapon, client.specAmount, 7611);
			break;

		case 3204: // d hally
			client.getFunction().sendFrame171(0, 8493);
			specialAmount(weapon, client.specAmount, 8505);
			break;
		case 13883:
		case 1377: // d battleaxe
			client.getFunction().sendFrame171(0, 7499);
			specialAmount(weapon, client.specAmount, 7511);
			break;
		case 13902:
		case 11863:
		case 4153: // gmaul
			client.getFunction().sendFrame171(0, 7474);
			specialAmount(weapon, client.specAmount, 7486);
			break;
		case 13905:
		case 1249: // dspear
			client.getFunction().sendFrame171(0, 7674);
			specialAmount(weapon, client.specAmount, 7686);
			break;

		case 1215:// dragon dagger
		case 1231:
		case 5680:
		case 5698:
		case 1305: // dragon long
		case 11694:
		case 11698:
		case 7806:
		case 11700:
		case 11730:
		case 11696:
		case 13899:
			client.getFunction().sendFrame171(0, 7574);
			specialAmount(weapon, client.specAmount, 7586);
			break;

		case 1434: // dragon mace
			client.getFunction().sendFrame171(0, 7624);
			specialAmount(weapon, client.specAmount, 7636);
			break;
		default:
			client.getFunction().sendFrame171(1, 7624); // mace interface
			client.getFunction().sendFrame171(1, 7474); // hammer, gmaul
			client.getFunction().sendFrame171(1, 7499); // axe
			client.getFunction().sendFrame171(1, 7549); // bow interface
			client.getFunction().sendFrame171(1, 7574); // sword interface
			client.getFunction().sendFrame171(1, 7599); // scimmy sword
														// interface,
			// for most
			// swords
			client.getFunction().sendFrame171(1, 8493);
			client.getFunction().sendFrame171(1, 12323); // whip interface
			break;
		}
	}

	public void addStarter() {
		client.getItems().addItemsToBank(STARTER_BANK);
	}

	public void addToVoidList(int itemId) {
		switch (itemId) {
		case 2518:
			client.voidStatus[0]++;
			break;
		case 2520:
			client.voidStatus[1]++;
			break;
		case 2522:
			client.voidStatus[2]++;
			break;
		case 2524:
			client.voidStatus[3]++;
			break;
		case 2526:
			client.voidStatus[4]++;
			break;
		}
	}

	public boolean antiDupe(int itemID, int slot, int amt) {
		itemID++;
		if (client.playerItems[slot] != itemID
				|| client.playerItemsN[slot] < amt) {
			return false;
		}
		return true;
	}

	public boolean bankItem(int itemID, int fromSlot, int amount) {

		if (!client.isBanking) {
			return false;
		}
		if (client.playerItemsN[fromSlot] <= 0) {
			return false;
		}
		client.getTask().openUpBank(getTabforItem(itemID)); // Move to tab item
															// is
		// in
		// before adding
		if (client.inTrade) {
			client.sendMessage("You can't store items while trading!");
			return false;
		}
		if (client.getTask().getBankItems(client.bankingTab) >= 350) {
			client.sendMessage("You can't store any more items in this tab!");
			return false;
		}
		if (client.playerItems[fromSlot] <= 0
				|| client.playerItemsN[fromSlot] <= 0) {
			return false;
		}
		if (!ItemConfig.itemIsNote[client.playerItems[fromSlot] - 1]) {
			if (client.playerItems[fromSlot] <= 0) {
				return false;
			}
			if (ItemConfig.itemStackable[client.playerItems[fromSlot] - 1]
					|| client.playerItemsN[fromSlot] > 1) {
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
					if (client.bankingItems[i] == client.playerItems[fromSlot]) {
						if (client.playerItemsN[fromSlot] < amount)
							amount = client.playerItemsN[fromSlot];
						alreadyInBank = true;
						toBankSlot = i;
						i = GameConfig.BANK_SIZE + 1;
					}
				}

				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
						if (client.bankingItems[i] <= 0) {
							toBankSlot = i;
							i = GameConfig.BANK_SIZE + 1;
						}
					}
					client.bankingItems[toBankSlot] = client.playerItems[fromSlot];
					if (client.playerItemsN[fromSlot] < amount) {
						amount = client.playerItemsN[fromSlot];
					}
					if ((client.bankingItemsN[toBankSlot] + amount) <= GameConfig.MAXITEM_AMOUNT
							&& (client.bankingItemsN[toBankSlot] + amount) > -1) {
						client.bankingItemsN[toBankSlot] += amount;
					} else {
						client.sendMessage("Bank full!");
						return false;
					}
					deleteItem((client.playerItems[fromSlot] - 1), fromSlot,
							amount);
					resetTempItems();
					resetBank();
					return true;
				} else if (alreadyInBank) {
					if ((client.bankingItemsN[toBankSlot] + amount) <= GameConfig.MAXITEM_AMOUNT
							&& (client.bankingItemsN[toBankSlot] + amount) > -1) {
						client.bankingItemsN[toBankSlot] += amount;
					} else {
						client.sendMessage("Bank full!");
						return false;
					}
					deleteItem((client.playerItems[fromSlot] - 1), fromSlot,
							amount);
					resetTempItems();
					resetBank();
					return true;
				} else {
					client.sendMessage("Bank full!");
					return false;
				}
			} else {
				itemID = client.playerItems[fromSlot];
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
					if (client.bankingItems[i] == client.playerItems[fromSlot]) {
						alreadyInBank = true;
						toBankSlot = i;
						i = GameConfig.BANK_SIZE + 1;
					}
				}
				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
						if (client.bankingItems[i] <= 0) {
							toBankSlot = i;
							i = GameConfig.BANK_SIZE + 1;
						}
					}
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < client.playerItems.length; i++) {
							if ((client.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							client.bankingItems[toBankSlot] = client.playerItems[firstPossibleSlot];
							client.bankingItemsN[toBankSlot] += 1;
							deleteItem(
									(client.playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBank();
					return true;
				} else if (alreadyInBank) {
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < client.playerItems.length; i++) {
							if ((client.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							client.bankingItemsN[toBankSlot] += 1;
							deleteItem(
									(client.playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBank();
					return true;
				} else {
					client.sendMessage("Bank full!");
					return false;
				}
			}
		} else if (ItemConfig.itemIsNote[client.playerItems[fromSlot] - 1]
				&& !ItemConfig.itemIsNote[client.playerItems[fromSlot] - 2]) {
			if (client.playerItems[fromSlot] <= 0) {
				return false;
			}
			if (ItemConfig.itemStackable[client.playerItems[fromSlot] - 1]
					|| client.playerItemsN[fromSlot] > 1) {
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
					if (client.bankingItems[i] == (client.playerItems[fromSlot] - 1)) {
						if (client.playerItemsN[fromSlot] < amount)
							amount = client.playerItemsN[fromSlot];
						alreadyInBank = true;
						toBankSlot = i;
						i = GameConfig.BANK_SIZE + 1;
					}
				}

				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
						if (client.bankingItems[i] <= 0) {
							toBankSlot = i;
							i = GameConfig.BANK_SIZE + 1;
						}
					}
					client.bankingItems[toBankSlot] = (client.playerItems[fromSlot] - 1);
					if (client.playerItemsN[fromSlot] < amount) {
						amount = client.playerItemsN[fromSlot];
					}
					if ((client.bankingItemsN[toBankSlot] + amount) <= GameConfig.MAXITEM_AMOUNT
							&& (client.bankingItemsN[toBankSlot] + amount) > -1) {
						client.bankingItemsN[toBankSlot] += amount;
					} else {
						return false;
					}
					deleteItem((client.playerItems[fromSlot] - 1), fromSlot,
							amount);
					resetTempItems();
					resetBank();
					return true;
				} else if (alreadyInBank) {
					if ((client.bankingItemsN[toBankSlot] + amount) <= GameConfig.MAXITEM_AMOUNT
							&& (client.bankingItemsN[toBankSlot] + amount) > -1) {
						client.bankingItemsN[toBankSlot] += amount;
					} else {
						return false;
					}
					deleteItem((client.playerItems[fromSlot] - 1), fromSlot,
							amount);
					resetTempItems();
					resetBank();
					return true;
				} else {
					client.sendMessage("Bank full!");
					return false;
				}
			} else {
				itemID = client.playerItems[fromSlot];
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
					if (client.bankingItems[i] == (client.playerItems[fromSlot] - 1)) {
						alreadyInBank = true;
						toBankSlot = i;
						i = GameConfig.BANK_SIZE + 1;
					}
				}
				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
						if (client.bankingItems[i] <= 0) {
							toBankSlot = i;
							i = GameConfig.BANK_SIZE + 1;
						}
					}
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < client.playerItems.length; i++) {
							if ((client.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							client.bankingItems[toBankSlot] = (client.playerItems[firstPossibleSlot] - 1);
							client.bankingItemsN[toBankSlot] += 1;
							deleteItem(
									(client.playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBank();
					return true;
				} else if (alreadyInBank) {
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < client.playerItems.length; i++) {
							if ((client.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							client.bankingItemsN[toBankSlot] += 1;
							deleteItem(
									(client.playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBank();
					return true;
				} else {
					client.sendMessage("Bank full!");
					return false;
				}
			}
		} else {
			client.sendMessage("Item not supported "
					+ (client.playerItems[fromSlot] - 1));
			return false;
		}
	}

	public int getTabforItem(int itemID) {
		for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
			if (client.bankItems[i] == itemID
					|| client.bankItems[i] == itemID + 1
					|| ItemConfig.itemIsNote[itemID]
					&& client.bankItems[i] == itemID - 1)
				return 0;
			else if (client.bankItems1[i] == itemID
					|| client.bankItems1[i] == itemID + 1
					|| ItemConfig.itemIsNote[itemID]
					&& client.bankItems1[i] == itemID - 1)
				return 1;
			else if (client.bankItems2[i] == itemID
					|| client.bankItems2[i] == itemID + 1
					|| ItemConfig.itemIsNote[itemID]
					&& client.bankItems2[i] == itemID - 1)
				return 2;
			else if (client.bankItems3[i] == itemID
					|| client.bankItems3[i] == itemID + 1
					|| ItemConfig.itemIsNote[itemID]
					&& client.bankItems3[i] == itemID - 1)
				return 3;
			else if (client.bankItems4[i] == itemID
					|| client.bankItems4[i] == itemID + 1
					|| ItemConfig.itemIsNote[itemID]
					&& client.bankItems4[i] == itemID - 1)
				return 4;
			else if (client.bankItems5[i] == itemID
					|| client.bankItems5[i] == itemID + 1
					|| ItemConfig.itemIsNote[itemID]
					&& client.bankItems5[i] == itemID - 1)
				return 5;
			else if (client.bankItems6[i] == itemID
					|| client.bankItems6[i] == itemID + 1
					|| ItemConfig.itemIsNote[itemID]
					&& client.bankItems6[i] == itemID - 1)
				return 6;
			else if (client.bankItems7[i] == itemID
					|| client.bankItems7[i] == itemID + 1
					|| ItemConfig.itemIsNote[itemID]
					&& client.bankItems7[i] == itemID - 1)
				return 7;
			else if (client.bankItems8[i] == itemID
					|| client.bankItems8[i] == itemID + 1
					|| ItemConfig.itemIsNote[itemID]
					&& client.bankItems8[i] == itemID - 1)
				return 8;
		}
		return client.bankingTab; // if not in bank add to current tab
	}

	public boolean bankItemNoReset(int itemID, int fromSlot, int amount) {

		if (!client.isBanking) {
			return false;
		}
		if (client.playerItemsN[fromSlot] <= 0) {
			return false;
		}
		if (!ItemConfig.itemIsNote[client.playerItems[fromSlot] - 1]) {
			if (client.playerItems[fromSlot] <= 0) {
				return false;
			}
			if (ItemConfig.itemStackable[client.playerItems[fromSlot] - 1]
					|| client.playerItemsN[fromSlot] > 1) {
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
					if (client.bankItems[i] == client.playerItems[fromSlot]) {
						if (client.playerItemsN[fromSlot] < amount) {
							amount = client.playerItemsN[fromSlot];
						}
						alreadyInBank = true;
						toBankSlot = i;
						i = GameConfig.BANK_SIZE + 1;
					}
				}

				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
						if (client.bankItems[i] <= 0) {
							toBankSlot = i;
							i = GameConfig.BANK_SIZE + 1;
						}
					}
					client.bankItems[toBankSlot] = client.playerItems[fromSlot];
					if (client.playerItemsN[fromSlot] < amount) {
						amount = client.playerItemsN[fromSlot];
					}
					if (client.bankItemsN[toBankSlot] + amount <= GameConfig.MAXITEM_AMOUNT
							&& client.bankItemsN[toBankSlot] + amount > -1) {
						client.bankItemsN[toBankSlot] += amount;
					} else {
						client.sendMessage("Bank full!");
						return false;
					}
					deleteItem((client.playerItems[fromSlot] - 1), fromSlot,
							amount);
					PlayerSave.saveGame(client);
					return true;
				} else if (alreadyInBank) {
					if (client.bankItemsN[toBankSlot] + amount <= GameConfig.MAXITEM_AMOUNT
							&& client.bankItemsN[toBankSlot] + amount > -1) {
						client.bankItemsN[toBankSlot] += amount;
					} else {
						client.sendMessage("Bank full!");
						return false;
					}
					deleteItem((client.playerItems[fromSlot] - 1), fromSlot,
							amount);
					PlayerSave.saveGame(client);
					return true;
				} else {
					client.sendMessage("Bank full!");
					return false;
				}
			} else {
				itemID = client.playerItems[fromSlot];
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
					if (client.bankItems[i] == client.playerItems[fromSlot]) {
						alreadyInBank = true;
						toBankSlot = i;
						i = GameConfig.BANK_SIZE + 1;
					}
				}
				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
						if (client.bankItems[i] <= 0) {
							toBankSlot = i;
							i = GameConfig.BANK_SIZE + 1;
						}
					}
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < client.playerItems.length; i++) {
							if (client.playerItems[i] == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							client.bankItems[toBankSlot] = client.playerItems[firstPossibleSlot];
							client.bankItemsN[toBankSlot] += 1;
							deleteItem(
									(client.playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}

					PlayerSave.saveGame(client);
					return true;
				} else if (alreadyInBank) {
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < client.playerItems.length; i++) {
							if (client.playerItems[i] == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							client.bankItemsN[toBankSlot] += 1;
							deleteItem(
									(client.playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					PlayerSave.saveGame(client);
					return true;
				} else {
					client.sendMessage("Bank full!");
					return false;
				}
			}
		} else if (ItemConfig.itemIsNote[client.playerItems[fromSlot] - 1]
				&& !ItemConfig.itemIsNote[client.playerItems[fromSlot] - 2]) {
			if (client.playerItems[fromSlot] <= 0) {
				return false;
			}
			if (ItemConfig.itemStackable[client.playerItems[fromSlot] - 1]
					|| client.playerItemsN[fromSlot] > 1) {
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
					if (client.bankItems[i] == client.playerItems[fromSlot] - 1) {
						if (client.playerItemsN[fromSlot] < amount) {
							amount = client.playerItemsN[fromSlot];
						}
						alreadyInBank = true;
						toBankSlot = i;
						i = GameConfig.BANK_SIZE + 1;
					}
				}

				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
						if (client.bankItems[i] <= 0) {
							toBankSlot = i;
							i = GameConfig.BANK_SIZE + 1;
						}
					}
					client.bankItems[toBankSlot] = client.playerItems[fromSlot] - 1;
					if (client.playerItemsN[fromSlot] < amount) {
						amount = client.playerItemsN[fromSlot];
					}
					if (client.bankItemsN[toBankSlot] + amount <= GameConfig.MAXITEM_AMOUNT
							&& client.bankItemsN[toBankSlot] + amount > -1) {
						client.bankItemsN[toBankSlot] += amount;
					} else {
						return false;
					}
					deleteItem((client.playerItems[fromSlot] - 1), fromSlot,
							amount);

					PlayerSave.saveGame(client);
					return true;
				} else if (alreadyInBank) {
					if (client.bankItemsN[toBankSlot] + amount <= GameConfig.MAXITEM_AMOUNT
							&& client.bankItemsN[toBankSlot] + amount > -1) {
						client.bankItemsN[toBankSlot] += amount;
					} else {
						return false;
					}
					deleteItem((client.playerItems[fromSlot] - 1), fromSlot,
							amount);

					PlayerSave.saveGame(client);
					return true;
				} else {
					client.sendMessage("Bank full!");
					return false;
				}
			} else {
				itemID = client.playerItems[fromSlot];
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
					if (client.bankItems[i] == client.playerItems[fromSlot] - 1) {
						alreadyInBank = true;
						toBankSlot = i;
						i = GameConfig.BANK_SIZE + 1;
					}
				}
				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
						if (client.bankItems[i] <= 0) {
							toBankSlot = i;
							i = GameConfig.BANK_SIZE + 1;
						}
					}
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < client.playerItems.length; i++) {
							if (client.playerItems[i] == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							client.bankItems[toBankSlot] = client.playerItems[firstPossibleSlot] - 1;
							client.bankItemsN[toBankSlot] += 1;
							deleteItem(
									(client.playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					PlayerSave.saveGame(client);
					return true;
				} else if (alreadyInBank) {
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < client.playerItems.length; i++) {
							if (client.playerItems[i] == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							client.bankItemsN[toBankSlot] += 1;
							deleteItem(
									(client.playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}

					PlayerSave.saveGame(client);
					return true;
				} else {
					client.sendMessage("Bank full!");
					return false;
				}
			}
		} else {
			client.sendMessage("Item not supported "
					+ (client.playerItems[fromSlot] - 1));
			return false;
		}
	}

	/**
	 * Drop Item
	 **/

	public void createGroundItem(int itemID, int itemX, int itemY, int itemZ,
			int itemAmount) {
		synchronized (client) {
			if (client.absZ == itemZ) {
				client.getOutStream().createFrame(85);
				client.getOutStream().writeByteC(
						(itemY - 8 * client.mapRegionY));
				client.getOutStream().writeByteC(
						(itemX - 8 * client.mapRegionX));
				client.getOutStream().createFrame(44);
				client.getOutStream().writeWordBigEndianA(itemID);
				client.getOutStream().writeWord(itemAmount);
				client.getOutStream().writeByte(0);
				client.flushOutStream();
			}
		}
	}

	/**
	 * delete all items
	 **/

	public void deleteAllItems() {
		for (int i1 = 0; i1 < client.playerEquipment.length; i1++) {
			deleteEquipment(client.playerEquipment[i1], i1);
		}
		for (int i = 0; i < client.playerItems.length; i++) {
			deleteItem(client.playerItems[i] - 1,
					getItemSlot(client.playerItems[i] - 1),
					client.playerItemsN[i]);
		}
		PlayerSave.saveGame(client);
	}

	public void deleteInventory() {
		for (int i = 0; i < client.playerItems.length; i++) {
			client.playerItems[i] = 0;
		}
		for (int i = 0; i < client.playerItemsN.length; i++) {
			client.playerItemsN[i] = 0;
		}
		resetItems(3214);
		PlayerSave.saveGame(client);
	}

	/**
	 * Delete Arrows
	 **/
	public void deleteArrow() {
		synchronized (client) {
			if (client.playerEquipment[client.playerCape] == 10499
					&& Misc.random(5) != 1) {
				return;
			} else if (client.playerEquipment[client.playerCape] == 10498
					&& Misc.random(3) != 1) {
				return;
			}
			if (client.playerEquipmentN[client.playerArrows] == 1) {
				client.getItems().deleteEquipment(
						client.playerEquipment[client.playerArrows],
						client.playerArrows);
			}
			if (client.playerEquipmentN[client.playerArrows] != 0) {
				client.getOutStream().createFrameVarSizeWord(34);
				client.getOutStream().writeWord(1688);
				client.getOutStream().writeByte(client.playerArrows);
				client.getOutStream().writeWord(
						client.playerEquipment[client.playerArrows] + 1);
				if (client.playerEquipmentN[client.playerArrows] - 1 > 254) {
					client.getOutStream().writeByte(255);
					client.getOutStream().writeDWord(
							client.playerEquipmentN[client.playerArrows] - 1);
				} else {
					client.getOutStream().writeByte(
							client.playerEquipmentN[client.playerArrows] - 1);
				}
				client.getOutStream().endFrameVarSizeWord();
				client.flushOutStream();
				client.playerEquipmentN[client.playerArrows] -= 1;
			}
			client.updateRequired = true;
			client.getUpdateFlags().setAppearanceUpdateRequired(true);
		}
	}

	public void deleteEquipment() {
		synchronized (client) {
			if (client.playerEquipment[client.playerCape] == 10499
					&& Misc.random(5) != 1) {
				return;
			} else if (client.playerEquipment[client.playerCape] == 10498
					&& Misc.random(3) != 1) {
				return;
			}
			if (client.playerEquipmentN[client.playerWeapon] == 1) {
				client.getItems().deleteEquipment(
						client.playerEquipment[client.playerWeapon],
						client.playerWeapon);
			}
			if (client.playerEquipmentN[client.playerWeapon] != 0) {
				client.getOutStream().createFrameVarSizeWord(34);
				client.getOutStream().writeWord(1688);
				client.getOutStream().writeByte(client.playerWeapon);
				client.getOutStream().writeWord(
						client.playerEquipment[client.playerWeapon] + 1);
				if (client.playerEquipmentN[client.playerWeapon] - 1 > 254) {
					client.getOutStream().writeByte(255);
					client.getOutStream().writeDWord(
							client.playerEquipmentN[client.playerWeapon] - 1);
				} else {
					client.getOutStream().writeByte(
							client.playerEquipmentN[client.playerWeapon] - 1);
				}
				client.getOutStream().endFrameVarSizeWord();
				client.flushOutStream();
				client.playerEquipmentN[client.playerWeapon] -= 1;
			}
			client.updateRequired = true;
			client.getUpdateFlags().setAppearanceUpdateRequired(true);
		}
	}

	/**
	 * delete Item
	 **/

	public void deleteEquipment(int i, int j) {
		synchronized (client) {
			if (PlayerHandler.players[client.playerId] == null) {
				return;
			}
			if (i < 0) {
				return;
			}

			client.playerEquipment[j] = -1;
			client.playerEquipmentN[j] = client.playerEquipmentN[j] - 1;
			client.getOutStream().createFrame(34);
			client.getOutStream().writeWord(6);
			client.getOutStream().writeWord(1688);
			client.getOutStream().writeByte(j);
			client.getOutStream().writeWord(0);
			client.getOutStream().writeByte(0);
			getBonus();
			if (j == client.playerWeapon) {
				sendWeapon(-1, "Unarmed");
			}
			resetBonus();
			getBonus();
			writeBonus();
			client.updateRequired = true;
			client.getUpdateFlags().setAppearanceUpdateRequired(true);
		}
	}

	public void deleteItem(int id, int amount) {
		if (id <= 0) {
			return;
		}
		for (int j = 0; j < client.playerItems.length; j++) {
			if (amount <= 0) {
				break;
			}
			if (client.playerItems[j] == id + 1) {
				client.playerItems[j] = 0;
				client.playerItemsN[j] = 0;
				amount--;
			}
		}
		resetItems(3214);
		PlayerSave.saveGame(client);
	}

	public void deleteItem(int id, int slot, int amount) {
		if (id < 0 || slot < 0) {
			return;
		}
		if (client.playerItems[slot] == id + 1) {
			if (client.playerItemsN[slot] > amount) {
				client.playerItemsN[slot] -= amount;
			} else {
				client.playerItemsN[slot] = 0;
				client.playerItems[slot] = 0;
			}
			resetItems(3214);
			PlayerSave.saveGame(client);
		}
	}

	public void deleteItem2(int id, int amount) {
		int am = amount;
		for (int i = 0; i < client.playerItems.length; i++) {
			if (am == 0) {
				break;
			}
			if (client.playerItems[i] == id + 1) {
				if (client.playerItemsN[i] > amount) {
					client.playerItemsN[i] -= amount;
					break;
				} else {
					client.playerItems[i] = 0;
					client.playerItemsN[i] = 0;
					am--;
				}
			}
		}
		PlayerSave.saveGame(client);
		resetItems(3214);
	}

	/**
	 * Dropping Arrows
	 **/

	public void dropArrowNpc() {
		if (client.playerEquipment[client.playerCape] == 10499) {
			return;
		}
		final int enemyX = NPCHandler.npcs[client.oldNpcIndex].getX();
		final int enemyY = NPCHandler.npcs[client.oldNpcIndex].getY();
		if (Misc.random(10) >= 4) {
			if (GroundItemHandler.itemAmount(client.rangeItemUsed, enemyX,
					enemyY) == 0) {
				GroundItemHandler.createGroundItem(client,
						client.rangeItemUsed, enemyX, enemyY, client.absZ, 1,
						client.getId());
			} else if (GroundItemHandler.itemAmount(client.rangeItemUsed,
					enemyX, enemyY) != 0) {
				final int amount = GroundItemHandler.itemAmount(
						client.rangeItemUsed, enemyX, enemyY);
				GroundItemHandler.removeGroundItem(client,
						client.rangeItemUsed, enemyX, enemyY, false);
				GroundItemHandler.createGroundItem(client,
						client.rangeItemUsed, enemyX, enemyY, client.absZ,
						amount + 1, client.getId());
			}
		}
	}

	public void dropArrowPlayer() {
		final int enemyX = PlayerHandler.players[client.oldPlayerIndex].getX();
		final int enemyY = PlayerHandler.players[client.oldPlayerIndex].getY();
		if (client.playerEquipment[client.playerCape] == 10499) {
			return;
		}
		if (Misc.random(10) >= 4) {
			if (GroundItemHandler.itemAmount(client.rangeItemUsed, enemyX,
					enemyY) == 0) {
				GroundItemHandler.createGroundItem(client,
						client.rangeItemUsed, enemyX, enemyY, client.absZ, 1,
						client.getId());
			} else if (GroundItemHandler.itemAmount(client.rangeItemUsed,
					enemyX, enemyY) != 0) {
				final int amount = GroundItemHandler.itemAmount(
						client.rangeItemUsed, enemyX, enemyY);
				GroundItemHandler.removeGroundItem(client,
						client.rangeItemUsed, enemyX, enemyY, false);
				GroundItemHandler.createGroundItem(client,
						client.rangeItemUsed, enemyX, enemyY, client.absZ,
						amount + 1, client.getId());
			}
		}
	}

	public int findItem(int id, int[] items, int[] amounts) {
		for (int i = 0; i < client.playerItems.length; i++) {
			if (items[i] - 1 == id && amounts[i] > 0) {
				return i;
			}
		}
		return -1;
	}

	public int freeBankSlots() {
		int freeS = 0;
		for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
			if (client.bankItems[i] <= 0) {
				freeS++;
			}
		}
		return freeS;
	}

	public int freeSlots() {
		int freeS = 0;
		for (int playerItem : client.playerItems) {
			if (playerItem <= 0) {
				freeS++;
			}
		}
		return freeS;
	}

	public void fromBank(int itemID, int fromSlot, int amount) {
		if (!client.isBanking) {
			client.sendMessage("Go to a bank to bank items!");
			client.getTask().closeAllWindows();
			return;
		}
		int tempT = client.bankingTab;
		int collect = amount;
		for (int i = 0; i < client.getTask().tempItems.length; i++) {
			if (client.getTask().tempItems[i] == itemID + 1
					|| client.getTask().tempItems[i] == itemID) {
				int count = Math.min(client.getTask().tempItemsN[i], collect);
				if (collect == -1)
					count = client.getTask().tempItemsN[i];
				client.bankingTab = (client.getTask().tempItemsT[i]);
				if (client.bankingTab == 0) {
					client.bankingItems = client.bankItems;
					client.bankingItemsN = client.bankItemsN;
				}
				if (client.bankingTab == 1) {
					client.bankingItems = client.bankItems1;
					client.bankingItemsN = client.bankItems1N;
				}
				if (client.bankingTab == 2) {
					client.bankingItems = client.bankItems2;
					client.bankingItemsN = client.bankItems2N;
				}
				if (client.bankingTab == 3) {
					client.bankingItems = client.bankItems3;
					client.bankingItemsN = client.bankItems3N;
				}
				if (client.bankingTab == 4) {
					client.bankingItems = client.bankItems4;
					client.bankingItemsN = client.bankItems4N;
				}
				if (client.bankingTab == 5) {
					client.bankingItems = client.bankItems5;
					client.bankingItemsN = client.bankItems5N;
				}
				if (client.bankingTab == 6) {
					client.bankingItems = client.bankItems6;
					client.bankingItemsN = client.bankItems6N;
				}
				if (client.bankingTab == 7) {
					client.bankingItems = client.bankItems7;
					client.bankingItemsN = client.bankItems7N;
				}
				if (client.bankingTab == 8) {
					client.bankingItems = client.bankItems8;
					client.bankingItemsN = client.bankItems8N;
				}
				fromBank(itemID + 1, client.getTask().tempItemsS[i], count);
				collect -= count;
			}
		}
		client.bankingTab = tempT;
		if (amount > 0) {
			if (client.bankingItems[fromSlot] > 0) {
				if (!client.takeAsNote) {
					if (ItemConfig.itemStackable[client.bankingItems[fromSlot] - 1]) {
						System.out.println("Item: " + itemID + " Amount: "
								+ amount + " Bank Slot: "
								+ client.bankingItems[fromSlot]);
						if (client.bankingItemsN[fromSlot] > amount) {
							if (addItem((client.bankingItems[fromSlot] - 1),
									amount)) {
								client.bankingItemsN[fromSlot] -= amount;
								resetBank();
								resetItems(5064);
							}
						} else {
							if (addItem((client.bankingItems[fromSlot] - 1),
									client.bankingItemsN[fromSlot])) {
								client.bankingItems[fromSlot] = 0;
								client.bankingItemsN[fromSlot] = 0;
								resetBank();
								resetItems(5064);
							}
						}
					} else {
						while (amount > 0) {
							if (client.bankingItemsN[fromSlot] > 0) {
								if (addItem(
										(client.bankingItems[fromSlot] - 1), 1)) {
									client.bankingItemsN[fromSlot] += -1;
									amount--;
								} else {
									amount = 0;
								}
							} else {
								amount = 0;
							}
						}
						resetBank();
						resetItems(5064);
					}
				} else if (client.takeAsNote
						&& ItemConfig.itemIsNote[client.bankingItems[fromSlot]]) {
					if (client.bankingItemsN[fromSlot] > amount) {
						if (addItem(client.bankingItems[fromSlot], amount)) {
							client.bankingItemsN[fromSlot] -= amount;
							resetBank();
							resetItems(5064);
						}
					} else {
						if (addItem(client.bankingItems[fromSlot],
								client.bankingItemsN[fromSlot])) {
							client.bankingItems[fromSlot] = 0;
							client.bankingItemsN[fromSlot] = 0;
							resetBank();
							resetItems(5064);
						}
					}
				} else {
					client.sendMessage("This item can't be withdrawn as a note.");
					if (ItemConfig.itemStackable[client.bankingItems[fromSlot] - 1]) {
						if (client.bankingItemsN[fromSlot] > amount) {
							if (addItem((client.bankingItems[fromSlot] - 1),
									amount)) {
								client.bankingItemsN[fromSlot] -= amount;
								resetBank();
								resetItems(5064);
							}
						} else {
							if (addItem((client.bankingItems[fromSlot] - 1),
									client.bankingItemsN[fromSlot])) {
								client.bankingItems[fromSlot] = 0;
								client.bankingItemsN[fromSlot] = 0;
								resetBank();
								resetItems(5064);
							}
						}
					} else {
						while (amount > 0) {
							if (client.bankingItemsN[fromSlot] > 0) {
								if (addItem(
										(client.bankingItems[fromSlot] - 1), 1)) {
									client.bankingItemsN[fromSlot] += -1;
									amount--;
								} else {
									amount = 0;
								}
							} else {
								amount = 0;
							}
						}
						resetBank();
						resetItems(5064);
					}
				}
			}
		}
		client.getTask().openUpBank(client.bankingTab);
		client.getTask().sendTabs();
	}

	public void getBonus() {
		for (int element : client.playerEquipment) {
			if (element > -1) {
				for (int j = 0; j < GameConfig.ITEM_LIMIT; j++) {
					if (GroundItemHandler.itemDefs[j] != null) {
						if (GroundItemHandler.itemDefs[j].itemId == element) {
							for (int k = 0; k < client.playerBonus.length; k++) {
								client.playerBonus[k] += GroundItemHandler.itemDefs[j].bonus[k];
							}
							break;
						}
					}
				}
			}
		}
	}

	public int getItemAmount(int ItemID) {
		int itemCount = 0;
		for (int i = 0; i < client.playerItems.length; i++) {
			if (client.playerItems[i] - 1 == ItemID) {
				itemCount += client.playerItemsN[i];
			}
		}
		return itemCount;
	}

	public int getItemCount(int itemID) {
		int count = 0;
		for (int j = 0; j < client.playerItems.length; j++) {
			if (client.playerItems[j] == itemID + 1) {
				count += client.playerItemsN[j];
			}
		}
		return count;
	}

	public int getItemId(String itemName) {
		for (int i = 0; i < GameConfig.ITEM_LIMIT; i++) {
			if (GroundItemHandler.itemDefs[i] != null) {
				if (GroundItemHandler.itemDefs[i].itemName
						.equalsIgnoreCase(itemName)) {
					return GroundItemHandler.itemDefs[i].itemId;
				}
			}
		}
		return -1;
	}

	public int getItemSlot(int ItemID) {
		for (int i = 0; i < client.playerItems.length; i++) {
			if (client.playerItems[i] - 1 == ItemID) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Weapon Requirements
	 **/

	public void getRequirements(String itemName, int itemId) {
		client.attackLevelReq = client.defenceLevelReq = client.strengthLevelReq = client.rangeLevelReq = client.magicLevelReq = 0;
		if (itemName.contains("mystic") || itemName.contains("nchanted")) {
			if (itemName.contains("staff")) {
				client.magicLevelReq = 20;
				client.attackLevelReq = 40;
			} else {
				client.magicLevelReq = 20;
				client.defenceLevelReq = 20;
			}
		}

		if (itemName.contains("infinity")) {
			client.magicLevelReq = 50;
			client.defenceLevelReq = 25;
		}
		if (itemName.contains("splitbark")) {
			client.magicLevelReq = 40;
			client.defenceLevelReq = 40;
		}
		if (itemName.contains("granite")) {
			client.defenceLevelReq = 50;
		}

		if (itemName.contains("c'bow")) {
			client.rangeLevelReq = 61;
		}
		if (itemName.contains("black d'hide")) {
			client.rangeLevelReq = 70;
		}
		if (itemName.contains("tzhaar-ket-om")) {
			client.strengthLevelReq = 60;
		}
		if (itemName.contains("red d'hide")) {
			client.rangeLevelReq = 60;
		}
		if (itemName.contains("blue d'hide")) {
			client.rangeLevelReq = 50;
		}
		if (itemName.contains("green d'hide")) {
			client.rangeLevelReq = 40;
		}
		if (itemName.contains("initiate")) {
			client.defenceLevelReq = 20;
		}
		if (itemName.contains("bronze")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				client.attackLevelReq = client.defenceLevelReq = 1;
			}
			return;
		}
		if (itemName.contains("iron")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				client.attackLevelReq = client.defenceLevelReq = 1;
			}
			return;
		}
		if (itemName.contains("steel")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				client.attackLevelReq = client.defenceLevelReq = 5;
			}
			return;
		}
		if (itemName.contains("black")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")
					&& !itemName.contains("vamb") && !itemName.contains("chap")) {
				client.attackLevelReq = client.defenceLevelReq = 10;
			}
			return;
		}
		if (itemName.contains("mithril")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				client.attackLevelReq = client.defenceLevelReq = 20;
			}
			return;
		}
		if (itemName.contains("adamant")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				client.attackLevelReq = client.defenceLevelReq = 30;
			}
			return;
		}
		if (itemName.contains("rune") || itemName.startsWith("gilded")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")
					&& !itemName.contains("'bow")) {
				client.attackLevelReq = client.defenceLevelReq = 40;
			}
			return;
		}
		if (itemName.contains("rock-shell")) {
			if (!itemName.contains("knife")) {
				client.defenceLevelReq = 55;
			}
			return;
		}
		if (itemName.contains("rock-shell")) {
			if (!itemName.contains("knife")) {
				client.defenceLevelReq = 55;
			}
			return;
		}
		if (itemName.contains("spined")) {
			if (!itemName.contains("knife")) {
				client.rangeLevelReq = client.defenceLevelReq = 50;
			}
			return;
		}
		if (itemName.contains("skeletal")) {
			if (!itemName.contains("knife")) {
				client.rangeLevelReq = client.defenceLevelReq = 50;
			}
			return;
		}
		if (itemName.contains("granite shield")) {
			if (!itemName.contains("maul")) {
				client.defenceLevelReq = 50;
			}
			return;
		}
		if (itemName.contains("granite maul")) {
			if (!itemName.contains("shield")) {
				client.attackLevelReq = 50;
			}
			return;
		}
		if (itemName.contains("warrior")) {
			if (!itemName.contains("ring")) {
				client.defenceLevelReq = 40;
			}
			return;
		}
		if (itemName.contains("dragonfire")) {

			client.defenceLevelReq = 75;

		}
		if (itemName.contains("dragonfire")) {

			client.defenceLevelReq = 75;

		}
		if (itemName.endsWith("ward")) {
			client.defenceLevelReq = 70;

		}
		if (itemName.contains("d'hide")) {
			if (!itemName.contains("chaps")) {
				client.defenceLevelReq = client.rangeLevelReq = 40;
			}
			return;
		}
		if (itemName.contains("dragon dagger")) {

			client.attackLevelReq = 60;
		}
		if (itemName.contains("drag dagger")) {

			client.attackLevelReq = 60;
		}
		if (itemName.contains("ancient")) {

			client.attackLevelReq = 50;
		}
		if (itemName.contains("hardleather")) {

			client.defenceLevelReq = 10;
		}
		if (itemName.contains("studded")) {

			client.defenceLevelReq = 20;
		}
		if (itemName.contains("spirit")) {
			client.defenceLevelReq = 75;
		}
		if (itemName.contains("divine")) {
			client.defenceLevelReq = 75;
		}
		if (itemName.contains("studded")) {

			client.defenceLevelReq = 20;
		}
		if (itemName.contains("fighter")) {

			client.defenceLevelReq = 45;
		}
		if (itemName.contains("etheral")) {
			client.defenceLevelReq = 65;
		}
		if (itemName.contains("bandos")) {
			if (!itemName.contains("godsword")) {
				client.strengthLevelReq = client.defenceLevelReq = 65;
				return;
			}
		}

		if (itemName.contains("zuriel")) {
			if (!itemName.contains("staff")) {
				client.magicLevelReq = client.defenceLevelReq = 78;
				return;
			} else {
				client.magicLevelReq = 78;
			}
		}
		if (itemName.contains("statius")) {
			if (!itemName.contains("warhammer")) {
				client.attackLevelReq = client.defenceLevelReq = 78;
				return;
			} else {
				client.attackLevelReq = 78;
			}
		}
		if (itemName.contains("dragon")) {
			if (!itemName.contains("nti-") && !itemName.contains("fire")) {
				client.attackLevelReq = client.defenceLevelReq = 60;
				return;
			}
		}
		if (itemName.contains("crystal")) {
			if (itemName.contains("shield")) {
				client.defenceLevelReq = 70;
			} else {
				client.rangeLevelReq = 70;
			}
			return;
		}
		if (itemName.contains("ahrim")) {
			if (itemName.contains("staff")) {
				client.magicLevelReq = 70;
				client.attackLevelReq = 70;
			} else {
				client.magicLevelReq = 70;
				client.defenceLevelReq = 70;
			}
		}
		if (itemName.contains("karil")) {
			if (itemName.contains("crossbow")) {
				client.rangeLevelReq = 70;
			} else {
				client.rangeLevelReq = 70;
				client.defenceLevelReq = 70;
			}
		}
		if (itemName.contains("armadyl")) {
			if (itemName.contains("godsword")) {
				client.attackLevelReq = 75;
				// entity.Donatorreq = 1;
			} else {
				client.rangeLevelReq = client.defenceLevelReq = 65;
			}
		}
		if (itemName.contains("saradomin")) {
			if (itemName.contains("sword")) {
				client.attackLevelReq = 70;
			}
			if (itemName.contains("crozier")) {
				client.attackLevelReq = 1;
				if (itemName.contains("robe")) {
					client.attackLevelReq = 1;

				} else {
					client.defenceLevelReq = 40;

				}
			}
		}
		if (itemName.contains("godsword")) {
			client.attackLevelReq = 75;
		}
		if (itemName.contains("3rd age") && !itemName.contains("amulet")) {
			client.defenceLevelReq = 60;
		}
		if (itemName.contains("vesta") && !itemName.contains("long")) {
			client.defenceLevelReq = 78;
		}
		if (itemName.contains("vesta longsword")) {
			client.attackLevelReq = 78;
		}
		if (itemName.contains("anger")) {
			client.attackLevelReq = 90;
		}
		if (itemName.contains("verac") || itemName.contains("guthan")
				|| itemName.contains("dharok") || itemName.contains("torag")) {

			if (itemName.contains("hammers")) {
				client.attackLevelReq = 70;
				client.strengthLevelReq = 70;
			} else if (itemName.contains("axe")) {
				client.attackLevelReq = 70;
				client.strengthLevelReq = 70;
			} else if (itemName.contains("warspear")) {
				client.attackLevelReq = 70;
				client.strengthLevelReq = 70;
			} else if (itemName.contains("flail")) {
				client.attackLevelReq = 70;
				client.strengthLevelReq = 70;
			} else {
				client.defenceLevelReq = 70;
			}
		}
		if (itemId > 2652 && itemId < 2677)
			client.defenceLevelReq = 40;
		switch (itemId) {
		case 8839:
		case 8840:
		case 8842:
		case 11663:
		case 11664:
		case 11665:
			client.attackLevelReq = 42;
			client.rangeLevelReq = 42;
			client.strengthLevelReq = 42;
			client.magicLevelReq = 42;
			client.defenceLevelReq = 42;
			return;
		case 10551:
		case 2503:
		case 2501:
		case 2499:
		case 1135:
			client.defenceLevelReq = 40;
			return;
		case 11235:
		case 6522:
			client.rangeLevelReq = 60;
			break;
		case 6524:
			client.defenceLevelReq = 60;
			break;
		case 11284:
			client.defenceLevelReq = 75;
			return;
		case 6889:
		case 6914:
			client.magicLevelReq = 60;
			break;
		case 861:
			client.rangeLevelReq = 50;
			break;
		case 10828:
			client.defenceLevelReq = 55;
			break;
		case 11724:
		case 11726:
		case 11728:
			client.defenceLevelReq = 65;
			break;
		case 3751:
		case 3749:
		case 3755:
			client.defenceLevelReq = 40;
			break;

		case 7462:
		case 7461:
			client.defenceLevelReq = 40;
			break;
		case 8846:
			client.defenceLevelReq = 5;
			break;
		case 8847:
			client.defenceLevelReq = 10;
			break;
		case 8848:
			client.defenceLevelReq = 20;
			break;
		case 8849:
			client.defenceLevelReq = 30;
			break;

		case 8850:
			client.defenceLevelReq = 40;

		case 7460:
			client.defenceLevelReq = 20;
			break;

		case 837:
			client.rangeLevelReq = 61;
			break;

		case 4151: // if you don't want to use names
		case 12006:
			client.attackLevelReq = 70;
			return;

		case 6724: // seercull
			client.rangeLevelReq = 60; // idk if that is correct
			return;
		case 11863:
		case 4153:
			client.attackLevelReq = 50;
			client.strengthLevelReq = 50;
			return;
		}
	}

	public int getTotalCount(int itemID) {
		int count = 0;
		for (int j = 0; j < client.playerItems.length; j++) {
			if (ItemConfig.itemIsNote[itemID + 1]) {
				if (itemID + 2 == client.playerItems[j]) {
					count += client.playerItemsN[j];
				}
			}
			if (!ItemConfig.itemIsNote[itemID + 1]) {
				if (itemID + 1 == client.playerItems[j]) {
					count += client.playerItemsN[j];
				}
			}
		}
		for (int j = 0; j < client.bankItems.length; j++) {
			if (client.bankItems[j] == itemID + 1) {
				count += client.bankItemsN[j];
			}
		}
		return count;
	}

	public int getUnnotedItem(int ItemID) {
		int NewID = ItemID - 1;
		String NotedName = "";
		for (int i = 0; i < GameConfig.ITEM_LIMIT; i++) {
			if (GroundItemHandler.itemDefs[i] != null) {
				if (GroundItemHandler.itemDefs[i].itemId == ItemID) {
					NotedName = GroundItemHandler.itemDefs[i].itemName;
				}
			}
		}
		for (int i = 0; i < GameConfig.ITEM_LIMIT; i++) {
			if (GroundItemHandler.itemDefs[i] != null) {
				if (GroundItemHandler.itemDefs[i].itemName == NotedName) {
					if (GroundItemHandler.itemDefs[i].itemDescription
							.startsWith("Swap this note at any bank for a") == false) {
						NewID = GroundItemHandler.itemDefs[i].itemId;
						break;
					}
				}
			}
		}
		return NewID;
	}

	public int getUntradePrice(int item) {
		switch (item) {
		case 2518:
		case 2524:
		case 2526:
			return 100000;
		case 2520:
		case 2522:
			return 150000;
		}
		return 0;
	}

	public boolean hasAllShards() {
		return playerHasItem(11712, 1) && playerHasItem(11712, 1)
				&& playerHasItem(11714, 1);
	}

	public boolean hasItemInBank(int id) {
		for (int bankItem : client.bankItems) {
			if (bankItem == id) {
				return true;
			}
		}
		return false;
	}

	public boolean hasItemInBank(int id, int amount) {
		for (int i = 0; i < client.bankItems.length; i++) {
			if (client.bankItems[i] == id && client.bankItemsN[i] >= amount) {
				return true;
			}
		}
		return false;
	}

	/**
	 * two handed weapon check
	 **/
	public boolean is2handed(String itemName, int itemId) {
		if (itemName.contains("ahrim") || itemName.contains("karil")
				|| itemName.contains("verac") || itemName.contains("guthan")
				|| itemName.contains("dharok") || itemName.contains("torag")) {
			return true;
		}
		if (itemName.contains("longbow") || itemName.contains("shortbow")
				|| itemName.contains("ark bow")) {
			return true;
		}
		if (itemName.contains("crystal")) {
			return true;
		}
		if (itemName.contains("godsword") || itemName.contains("claw")
				|| itemName.contains("aradomin sword")
				|| itemName.contains("2h") || itemName.contains("spear")) {
			return true;
		}
		switch (itemId) {
		case 6724: // seercull
		case 11730:
		case 4153:
		case 11863:
		case 6528:
		case 11777:
		case 10887:
		case 9705:
			return true;
		}
		return false;
	}

	public boolean isHilt(int i) {
		return i >= 11702 && i <= 11708 && i % 2 == 0;
	}

	public boolean isStackable(int itemID) {
		return ItemConfig.itemStackable[itemID];
	}

	public int itemAmount(int itemID) {
		int tempAmount = 0;
		for (int i = 0; i < client.playerItems.length; i++) {
			if (client.playerItems[i] == itemID) {
				tempAmount += client.playerItemsN[i];
			}
		}
		return tempAmount;
	}

	public boolean itemExistsOnAccount(int itemId, int amount, int type) {
		if (client.getItems().hasItemInBank(itemId + 1, amount)) {
			return true;
		}
		if (client.getItems().playerHasItem(itemId, amount)) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("null")
	public boolean itemIsDestroyable(int item) {
		int[] destroyables = null;
		for (int i : destroyables) {
			if (i == item) {
				return true;
			}
		}
		return false;
	}

	public void itemOnInterface(int id, int amount) {
		synchronized (client) {
			client.getOutStream().createFrameVarSizeWord(53);
			client.getOutStream().writeWord(2274);
			client.getOutStream().writeWord(1);
			if (amount > 254) {
				client.getOutStream().writeByte(255);
				client.getOutStream().writeDWord_v2(amount);
			} else {
				client.getOutStream().writeByte(amount);
			}
			client.getOutStream().writeWordBigEndianA(id);
			client.getOutStream().endFrameVarSizeWord();
			client.flushOutStream();
		}
	}

	/**
	 * Handle player item drops upon death and deleting all items
	 **/
	public void itemsUponDeath() {
		if (client.playerRights > 1) {
			return;
		}
		final Client o = (Client) PlayerHandler.players[client.killerId];
		List<Integer> untradeables = new ArrayList<Integer>();
		if (!o.isIronman() && !o.isUltimateIronman()) {
			for (int i = 0; i < client.playerItems.length; i++) {
				if (o != null) {
					if (tradeable(client.playerItems[i] - 1)) {
						GroundItemHandler.createGroundItem(o,
								client.playerItems[i] - 1, client.getX(),
								client.getY(), client.absZ,
								client.playerItemsN[i], client.killerId);
					} else {
						if (GameConfig.KEEP_UNTRADEABLES_IN_INVENTORY) {
							untradeables.add(client.playerItems[i] - 1);
						} else {
							GroundItemHandler.createGroundItem(client,
									client.playerItems[i] - 1, client.getX(),
									client.getY(), client.absZ,
									client.playerItemsN[i], client.playerId);
						}
					}
				} else {
					if (GameConfig.KEEP_UNTRADEABLES_IN_INVENTORY) {
						untradeables.add(client.playerItems[i] - 1);
					} else {
						GroundItemHandler.createGroundItem(client,
								client.playerItems[i] - 1, client.getX(),
								client.getY(), client.absZ,
								client.playerItemsN[i], client.playerId);
					}
				}
			}
			for (int e = 0; e < client.playerEquipment.length; e++) {
				if (o != null) {
					if (tradeable(client.playerEquipment[e])) {
						GroundItemHandler.createGroundItem(o,
								client.playerEquipment[e], client.getX(),
								client.getY(), client.absZ,
								client.playerEquipmentN[e], client.killerId);
					} else {
						if (GameConfig.KEEP_UNTRADEABLES_IN_INVENTORY) {
							untradeables.add(client.playerEquipment[e]);
						} else {
							GroundItemHandler
									.createGroundItem(client,
											client.playerEquipment[e],
											client.getX(), client.getY(),
											client.absZ,
											client.playerEquipmentN[e],
											client.playerId);
						}
					}
				} else {
					if (GameConfig.KEEP_UNTRADEABLES_IN_INVENTORY) {
						untradeables.add(client.playerEquipment[e]);
					} else {
						GroundItemHandler.createGroundItem(client,
								client.playerEquipment[e], client.getX(),
								client.getY(), client.absZ,
								client.playerEquipmentN[e], client.playerId);
					}
				}
			}
		}

		if (o != null) {
			GroundItemHandler.createGroundItem(o, 526, client.getX(),
					client.getY(), client.absZ, 1, client.killerId);
		}
		if (client.isNpc) {
			client.getFunction().becomeNpc(-1);
		}
		deleteAllItems();
		for (int i = 0; i < untradeables.size(); i++) {
			addItem(untradeables.get(i));
		}
		PlayerSave.saveGame(o);
		PlayerSave.saveGame(client);
	}

	public String itemType(int item) {
		for (int cape : ItemConfig.CAPES) {
			if (item == cape) {
				return "cape";
			}
		}
		for (int hat : ItemConfig.HATS) {
			if (item == hat) {
				return "hat";
			}
		}
		for (int boot : ItemConfig.BOOTS) {
			if (item == boot) {
				return "boots";
			}
		}
		for (int glove : ItemConfig.GLOVES) {
			if (item == glove) {
				return "gloves";
			}
		}
		for (int shield : ItemConfig.shields) {
			if (item == shield) {
				return "shield";
			}
		}
		for (int amulet : ItemConfig.AMULETS) {
			if (item == amulet) {
				return "amulet";
			}
		}
		for (int arrow : ItemConfig.ARROWS) {
			if (item == arrow) {
				return "arrows";
			}
		}
		for (int ring : ItemConfig.rings) {
			if (item == ring) {
				return "ring";
			}
		}
		for (int element : ItemConfig.BODY) {
			if (item == element) {
				return "body";
			}
		}
		for (int leg : ItemConfig.legs) {
			if (item == leg) {
				return "legs";
			}
		}
		return "weapon";
	}

	public void keepItem(int keepItem, boolean deleteItem) {
		int value = 0;
		int item = 0;
		int slotId = 0;
		boolean itemInInventory = false;
		for (int i = 0; i < client.playerItems.length; i++) {
			if (client.playerItems[i] - 1 > 0) {
				final int inventoryItemValue = getItemValue(client.playerItems[i] - 1);
				if (inventoryItemValue > value && !client.invSlot[i]) {
					value = inventoryItemValue;
					item = client.playerItems[i] - 1;
					slotId = i;
					itemInInventory = true;
				}
			}
		}
		for (int i1 = 0; i1 < client.playerEquipment.length; i1++) {
			if (client.playerEquipment[i1] > 0) {
				final int equipmentItemValue = getItemValue(client.playerEquipment[i1]);
				if (equipmentItemValue > value && !client.equipSlot[i1]) {
					value = equipmentItemValue;
					item = client.playerEquipment[i1];
					slotId = i1;
					itemInInventory = false;
				}
			}
		}
		if (itemInInventory) {
			client.invSlot[slotId] = true;
			if (deleteItem) {
				deleteItem(client.playerItems[slotId] - 1,
						getItemSlot(client.playerItems[slotId] - 1), 1);
			}
		} else {
			client.equipSlot[slotId] = true;
			if (deleteItem) {
				deleteEquipment(item, slotId);
			}
		}
		client.itemKeptId[keepItem] = item;
	}

	public void makeBlade() {
		deleteItem(11710, 1);
		deleteItem(11712, 1);
		deleteItem(11714, 1);
		addItem(11690, 1);
		client.sendMessage("You combine the shards to make a blade.");
	}

	public void makeGodsword(int i) {
		if (playerHasItem(11690) && playerHasItem(i)) {
			deleteItem(11690, 1);
			deleteItem(i, 1);
			addItem(i - 8, 1);
			client.sendMessage("You combine the hilt and the blade to make a godsword.");
		}
	}

	/**
	 * Move Items
	 **/

	public void moveItems(int from, int to, int moveWindow, byte insert) {
		if (moveWindow == 3214) {
			int tempI;
			int tempN;
			tempI = client.playerItems[from];
			tempN = client.playerItemsN[from];
			client.playerItems[from] = client.playerItems[to];
			client.playerItemsN[from] = client.playerItemsN[to]; // ?login
			client.playerItems[to] = tempI;
			client.playerItemsN[to] = tempN;
		}
		if (moveWindow == 5382 && from >= 0 && to >= 0
				&& from < GameConfig.BANK_SIZE && to < GameConfig.BANK_SIZE
				&& to < GameConfig.BANK_SIZE) {
			if (insert == 0) {
				int tempI;
				int tempN;
				tempI = client.bankingItems[from];
				tempN = client.bankingItemsN[from];
				client.bankingItems[from] = client.bankingItems[to];
				client.bankingItemsN[from] = client.bankingItemsN[to];
				client.bankingItems[to] = tempI;
				client.bankingItemsN[to] = tempN;
				client.getFunction().openUpBank(client.bankingTab);
			} else if (insert == 1) {
				int tempFrom = from;
				for (int tempTo = to; tempFrom != tempTo;)
					if (tempFrom > tempTo) {
						swapBankItem(tempFrom, tempFrom - 1);
						tempFrom--;
					} else if (tempFrom < tempTo) {
						swapBankItem(tempFrom, tempFrom + 1);
						tempFrom++;
					}
				client.getFunction().openUpBank(client.bankingTab);
			}
		}
		if (moveWindow == 5382) // try and do banking now
		{
			resetBank();
		}
		if (moveWindow == 18579 || moveWindow == 5064) {
			int tempI;
			int tempN;
			tempI = client.playerItems[from];
			tempN = client.playerItemsN[from];
			client.playerItems[from] = client.playerItems[to];
			client.playerItemsN[from] = client.playerItemsN[to];
			client.playerItems[to] = tempI;
			client.playerItemsN[to] = tempN;
			resetItems(3214);
		}
		resetTempItems();
		if (moveWindow == 3214) {
			resetItems(3214);
		}
	}
	public void swapBankItem(int from, int to) {
		int tempI = client.bankItems[from];
		int tempN = client.bankItemsN[from];
		client.bankItems[from] = client.bankItems[to];
		client.bankItemsN[from] = client.bankItemsN[to];
		client.bankItems[to] = tempI;
		client.bankItemsN[to] = tempN;
	}

	public boolean ownsCape() {
		if (client.getItems().playerHasItem(2412, 1)
				|| client.getItems().playerHasItem(2413, 1)
				|| client.getItems().playerHasItem(2414, 1)) {
			return true;
		}
		for (int j = 0; j < GameConfig.BANK_SIZE; j++) {
			if (client.bankItems[j] == 2412 || client.bankItems[j] == 2413
					|| client.bankItems[j] == 2414) {
				return true;
			}
		}
		if (client.playerEquipment[client.playerCape] == 2413
				|| client.playerEquipment[client.playerCape] == 2414
				|| client.playerEquipment[client.playerCape] == 2415) {
			return true;
		}
		return false;
	}

	public boolean playerHasEquipped(int itemID) {
		itemID++;
		for (int element : client.playerEquipment) {
			if (element == itemID) {
				return true;
			}
		}
		return false;
	}

	public boolean playerHasItem(int itemID) {
		itemID++;
		for (int playerItem : client.playerItems) {
			if (playerItem == itemID) {
				return true;
			}
		}
		return false;
	}

	public boolean playerHasItem(int itemID, int amt) {
		itemID++;
		int found = 0;
		for (int i = 0; i < client.playerItems.length; i++) {
			if (client.playerItems[i] == itemID) {
				if (client.playerItemsN[i] >= amt) {
					return true;
				} else {
					found++;
				}
			}
		}
		if (found >= amt) {
			return true;
		}
		return false;
	}

	public boolean playerHasItem(int itemID, int amt, int slot) {
		itemID++;
		int found = 0;
		if (client.playerItems[slot] == itemID) {
			for (int i = 0; i < client.playerItems.length; i++) {
				if (client.playerItems[i] == itemID) {
					if (client.playerItemsN[i] >= amt) {
						return true;
					} else {
						found++;
					}
				}
			}
			if (found >= amt) {
				return true;
			}
			return false;
		}
		return false;
	}

	/**
	 * BANK
	 */

	public void rearrangeBank() {
		int highestSlot = 0;
		for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
			if (client.bankingItems[i] != 0) {
				if (highestSlot <= i) {
					highestSlot = i;
				}
			}
		}
		for (int i = 0; i <= highestSlot; i++) {
			if (client.bankingItems[i] == 0) {
				boolean stop = false;

				for (int k = i; k <= highestSlot; k++) {
					if (client.bankingItems[k] != 0 && !stop) {
						int spots = k - i;
						for (int j = k; j <= highestSlot; j++) {
							client.bankingItems[j - spots] = client.bankingItems[j];
							client.bankingItemsN[j - spots] = client.bankingItemsN[j];
							stop = true;
							client.bankingItems[j] = 0;
							client.bankingItemsN[j] = 0;
						}
					}
				}
			}
		}
	}

	public void removeAllItems() {
		for (int i = 0; i < client.playerItems.length; i++) {
			client.playerItems[i] = 0;
		}
		for (int i = 0; i < client.playerItemsN.length; i++) {
			client.playerItemsN[i] = 0;
		}
		resetItems(3214);
	}

	/**
	 * Pickup Item
	 **/

	public void removeGroundItem(int itemID, int itemX, int itemY, int Amount) {
		synchronized (client) {
			client.getOutStream().createFrame(85);
			client.getOutStream().writeByteC((itemY - 8 * client.mapRegionY));
			client.getOutStream().writeByteC((itemX - 8 * client.mapRegionX));
			client.getOutStream().createFrame(156);
			client.getOutStream().writeByteS(0);
			client.getOutStream().writeWord(itemID);
			client.flushOutStream();
		}
	}

	/**
	 * Remove Item
	 **/
	public void removeItem(int wearID, int slot) {
		synchronized (client) {
			if (client.isDead) {
				return;
			}
			if (client.getOutStream() != null && client != null) {
				if (client.playerEquipment[slot] > -1) {
					if ((client.playerEquipment[slot] == CastleWars.SARA_CAPE || client.playerEquipment[slot] == CastleWars.ZAMMY_CAPE)
							&& (CastleWars.isInCw(client) || CastleWars
									.isInCwWait(client))) {
						return;
					}
					if (addItem(client.playerEquipment[slot],
							client.playerEquipmentN[slot])) {
						if (client.playerEquipment[slot] == CastleWars.SARA_BANNER
								|| client.playerEquipment[slot] == CastleWars.ZAMMY_BANNER) {
							CastleWars.dropFlag(client,
									client.playerEquipment[slot]);
							CastleWars.deleteWeapon(client);
							deleteItem(CastleWars.ZAMMY_BANNER, 1);
							deleteItem(CastleWars.SARA_BANNER, 1);
							return;
						}

						if (client.playerEquipment[client.playerWeapon] == 8286)
							client.getFunction().resetAutocast();
						client.playerEquipment[slot] = -1;
						client.playerEquipmentN[slot] = 0;
						sendWeapon(
								client.playerEquipment[client.playerWeapon],
								getItemName(client.playerEquipment[client.playerWeapon]));
						resetBonus();
						getBonus();
						writeBonus();
						client.getCombat()
								.getPlayerAnimIndex(
										ItemAssistant
												.getItemName(
														client.playerEquipment[client.playerWeapon])
												.toLowerCase());
						client.getOutStream().createFrame(34);
						client.getOutStream().writeWord(6);
						client.getOutStream().writeWord(1688);
						client.getOutStream().writeByte(slot);
						client.getOutStream().writeWord(0);
						client.getOutStream().writeByte(0);
						client.flushOutStream();
						client.getFunction().checkForMiscItems();
						client.updateRequired = true;
						client.getUpdateFlags().setAppearanceUpdateRequired(
								true);

						PlayerSave.saveGame(client);
						if (wearID == 4024 || wearID == 4026 || wearID == 4027
								|| wearID == 4029 || wearID == 4025) {
							client.isNpc = false;
							client.gfx0(160);
						}

						if (wearID == 6583) {
							client.isNpc = false;
						}
						if (wearID == 7927) {
							client.isNpc = false;
						}
					}
				}
			}
		}
	}

	// Added by Parrot
	public void replaceItem(int item, int item2, int amount, int amount2) {
		if (client.getItems().playerHasItem(item, amount)) {
			client.getItems().deleteItem(item,
					client.getItems().getItemSlot(item), amount);
			client.getItems().addItem(item2, amount2);
		} else {
			client.sendMessage("You do not have that item!");
		}
	}

	public void resetBank() {
		if (client.getOutStream() != null && client != null) {
			client.getOutStream().createFrameVarSizeWord(53);
			client.getOutStream().writeWord(5382);
			client.getOutStream().writeWord(GameConfig.BANK_SIZE);
			for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
				if (client.bankingItemsN[i] > 254) {
					client.getOutStream().writeByte(255);
					client.getOutStream()
							.writeDWord_v2(client.bankingItemsN[i]);
				} else {
					client.getOutStream().writeByte(client.bankingItemsN[i]);
				}
				if (client.bankingItemsN[i] < 1) {
					client.bankingItems[i] = 0;
				}
				if (client.bankingItems[i] > GameConfig.ITEM_LIMIT
						|| client.bankingItems[i] < 0) {
					client.bankingItems[i] = GameConfig.ITEM_LIMIT;
				}
				client.getOutStream().writeWordBigEndianA(
						client.bankingItems[i]);
			}
			client.getOutStream().endFrameVarSizeWord();
			client.flushOutStream();
			client.getTask().sendFrame126(
					Integer.toString(calculateRemaindingBankSlots()), 22033);
			client.getTask().sendFrame126(
					Integer.toString(GameConfig.BANK_SIZE), 22034);
		}
	}

	public int calculateRemaindingBankSlots() {
		int tab0 = 0;
		int tab1 = 0;
		int tab2 = 0;
		int tab3 = 0;
		int tab4 = 0;
		int tab5 = 0;
		int tab6 = 0;
		int tab7 = 0;
		int tab8 = 0;

		for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
			if (client.bankItems[i] <= 0) {
				tab0++;
			}
		}
		tab0 = GameConfig.BANK_SIZE - tab0;

		for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
			if (client.bankItems1[i] <= 0) {
				tab1++;
			}
		}
		tab1 = GameConfig.BANK_SIZE - tab1;

		for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
			if (client.bankItems2[i] <= 0) {
				tab2++;
			}
		}
		tab2 = GameConfig.BANK_SIZE - tab2;

		for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
			if (client.bankItems3[i] <= 0) {
				tab3++;
			}
		}
		tab3 = GameConfig.BANK_SIZE - tab3;

		for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
			if (client.bankItems4[i] <= 0) {
				tab4++;
			}
		}
		tab4 = GameConfig.BANK_SIZE - tab4;

		for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
			if (client.bankItems5[i] <= 0) {
				tab5++;
			}
		}
		tab5 = GameConfig.BANK_SIZE - tab5;

		for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
			if (client.bankItems6[i] <= 0) {
				tab6++;
			}
		}
		tab6 = GameConfig.BANK_SIZE - tab6;

		for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
			if (client.bankItems7[i] <= 0) {
				tab7++;
			}
		}
		tab7 = GameConfig.BANK_SIZE - tab7;

		for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
			if (client.bankItems8[i] <= 0) {
				tab8++;
			}
		}
		tab8 = GameConfig.BANK_SIZE - tab8;
		return tab0 + tab1 + tab2 + tab3 + tab4 + tab5 + tab6 + tab7 + tab8;
	}

	public void resetBonus() {
		for (int i = 0; i < client.playerBonus.length; i++) {
			client.playerBonus[i] = 0;
		}
	}

	public void resetItems(int WriteFrame) {
		// synchronized(c) {
		if (client.getOutStream() != null && client != null) {
			client.getOutStream().createFrameVarSizeWord(53);
			client.getOutStream().writeWord(WriteFrame);
			client.getOutStream().writeWord(client.playerItems.length);
			for (int i = 0; i < client.playerItems.length; i++) {
				if (client.playerItemsN[i] > 254) {
					client.getOutStream().writeByte(255);
					client.getOutStream().writeDWord_v2(client.playerItemsN[i]);
				} else {
					client.getOutStream().writeByte(client.playerItemsN[i]);
				}
				client.getOutStream()
						.writeWordBigEndianA(client.playerItems[i]);
			}
			client.getOutStream().endFrameVarSizeWord();
			client.flushOutStream();
		}
	}

	/**
	 * Reset items kept on death
	 **/

	public void resetKeepItems() {
		for (int i = 0; i < client.itemKeptId.length; i++) {
			client.itemKeptId[i] = -1;
		}
		for (int i1 = 0; i1 < client.invSlot.length; i1++) {
			client.invSlot[i1] = false;
		}
		for (int i2 = 0; i2 < client.equipSlot.length; i2++) {
			client.equipSlot[i2] = false;
		}
	}

	public void resetTempItems() {
		synchronized (client) {
			int itemCount = 0;
			for (int i = 0; i < client.playerItems.length; i++) {
				if (client.playerItems[i] > -1) {
					itemCount = i;
				}
			}
			client.getOutStream().createFrameVarSizeWord(53);
			client.getOutStream().writeWord(5064);
			client.getOutStream().writeWord(itemCount + 1);
			for (int i = 0; i < itemCount + 1; i++) {
				if (client.playerItemsN[i] > 254) {
					client.getOutStream().writeByte(255);
					client.getOutStream().writeDWord_v2(client.playerItemsN[i]);
				} else {
					client.getOutStream().writeByte(client.playerItemsN[i]);
				}
				if (client.playerItems[i] > GameConfig.ITEM_LIMIT
						|| client.playerItems[i] < 0) {
					client.playerItems[i] = GameConfig.ITEM_LIMIT;
				}
				client.getOutStream()
						.writeWordBigEndianA(client.playerItems[i]);
			}
			client.getOutStream().endFrameVarSizeWord();
			client.flushOutStream();
		}
	}

	public void sendItemsKept() {
		synchronized (client) {
			if (client.getOutStream() != null && client != null) {
				client.getOutStream().createFrameVarSizeWord(53);
				client.getOutStream().writeWord(6963);
				client.getOutStream().writeWord(client.itemKeptId.length);
				for (int i = 0; i < client.itemKeptId.length; i++) {
					if (client.playerItemsN[i] > 254) {
						client.getOutStream().writeByte(255);
						client.getOutStream().writeDWord_v2(1);
					} else {
						client.getOutStream().writeByte(1);
					}
					if (client.itemKeptId[i] > 0) {
						client.getOutStream().writeWordBigEndianA(
								client.itemKeptId[i] + 1);
					} else {
						client.getOutStream().writeWordBigEndianA(0);
					}
				}
				client.getOutStream().endFrameVarSizeWord();
				client.flushOutStream();
			}
		}
	}

	/**
	 * Wear Item
	 **/

	public void sendWeapon(int Weapon, String WeaponName) {
		String WeaponName2 = WeaponName.replaceAll("Bronze", "");
		WeaponName2 = WeaponName2.replaceAll("Iron", "");
		WeaponName2 = WeaponName2.replaceAll("Steel", "");
		WeaponName2 = WeaponName2.replaceAll("Black", "");
		WeaponName2 = WeaponName2.replaceAll("Mithril", "");
		WeaponName2 = WeaponName2.replaceAll("Adamant", "");
		WeaponName2 = WeaponName2.replaceAll("Rune", "");
		WeaponName2 = WeaponName2.replaceAll("Granite", "");
		WeaponName2 = WeaponName2.replaceAll("Dragon", "");
		WeaponName2 = WeaponName2.replaceAll("Drag", "");
		WeaponName2 = WeaponName2.replaceAll("Crystal", "");
		WeaponName2 = WeaponName2.trim();
		if (WeaponName.equals("Unarmed")) {
			client.setSidebarInterface(0, 5855); // punch, kick, block
			client.getFunction().sendFrame126(WeaponName, 5857);
		} else if (WeaponName.startsWith("Abyss")) {
			client.setSidebarInterface(0, 12290); // flick, lash, deflect
			client.getFunction().sendFrame246(12291, 200, Weapon);
			client.getFunction().sendFrame126(WeaponName, 12293);
		} else if (WeaponName.endsWith("bow") || WeaponName.endsWith("10")
				|| WeaponName.endsWith("full")
				|| WeaponName.startsWith("seercull")) {
			client.setSidebarInterface(0, 1764); // accurate, rapid, longrange
			client.getFunction().sendFrame246(1765, 200, Weapon);
			client.getFunction().sendFrame126(WeaponName, 1767);
		} else if (WeaponName.startsWith("Staff")
				|| WeaponName.endsWith("staff") || WeaponName.endsWith("wand")
				|| WeaponName.startsWith("Trident")) {
			client.setSidebarInterface(0, 328); // spike, impale, smash, block
			client.getFunction().sendFrame246(329, 200, Weapon);
			client.getFunction().sendFrame126(WeaponName, 331);
		} else if (WeaponName2.startsWith("dart")
				|| WeaponName2.startsWith("knife")
				|| WeaponName2.contains("javelin")
				|| WeaponName.equalsIgnoreCase("toktz-xil-ul")) {
			client.setSidebarInterface(0, 4446); // accurate, rapid, longrange
			client.getFunction().sendFrame246(4447, 200, Weapon);
			client.getFunction().sendFrame126(WeaponName, 4449);
		} else if (WeaponName2.startsWith("dagger")
				|| WeaponName2.contains("sword")) {
			client.setSidebarInterface(0, 2276); // stab, lunge, slash, block
			client.getFunction().sendFrame246(2277, 200, Weapon);
			client.getFunction().sendFrame126(WeaponName, 2279);
		} else if (WeaponName2.endsWith("claws")) {
			client.setSidebarInterface(0, 7762); // claws
			client.getFunction().sendFrame246(7763, 200, Weapon);
			client.getFunction().sendFrame126(WeaponName, 7765);
		} else if (WeaponName2.startsWith("pickaxe")) {
			client.setSidebarInterface(0, 5570); // spike, impale, smash, block
			client.getFunction().sendFrame246(5571, 200, Weapon);
			client.getFunction().sendFrame126(WeaponName, 5573);
		} else if (WeaponName2.endsWith("axe")) {
			client.setSidebarInterface(0, 1698); // chop, hack, smash, block
			client.getFunction().sendFrame246(1699, 200, Weapon);
			client.getFunction().sendFrame126(WeaponName, 1701);
		} else if (WeaponName2.startsWith("halberd")) {
			client.setSidebarInterface(0, 8460); // jab, swipe, fend
			client.getFunction().sendFrame246(8461, 200, Weapon);
			client.getFunction().sendFrame126(WeaponName, 8463);
		} else if (WeaponName2.startsWith("Scythe")) {
			client.setSidebarInterface(0, 8460); // jab, swipe, fend
			client.getFunction().sendFrame246(8461, 200, Weapon);
			client.getFunction().sendFrame126(WeaponName, 8463);
		} else if (WeaponName2.endsWith("spear")) {
			client.setSidebarInterface(0, 4679); // lunge, swipe, pound, block
			client.getFunction().sendFrame246(4680, 200, Weapon);
			client.getFunction().sendFrame126(WeaponName, 4682);
		} else if (WeaponName2.toLowerCase().contains("mace")) {
			client.setSidebarInterface(0, 3796);
			client.getFunction().sendFrame246(3797, 200, Weapon);
			client.getFunction().sendFrame126(WeaponName, 3799);

		} else if (WeaponName2.toLowerCase().contains("hammer")
				|| WeaponName2.toLowerCase().contains("maul")) {
			client.setSidebarInterface(0, 425); // war hamer equip.
			client.getFunction().sendFrame246(426, 200, Weapon);
			client.getFunction().sendFrame126(WeaponName, 428);
		} else {
			client.setSidebarInterface(0, 2423); // chop, slash, lunge, block
			client.getFunction().sendFrame246(2424, 200, Weapon);
			client.getFunction().sendFrame126(WeaponName, 2426);
		}

	}

	/**
	 * Update Equip tab
	 **/

	public void setEquipment(int wearID, int amount, int targetSlot) {
		synchronized (client) {
			client.getOutStream().createFrameVarSizeWord(34);
			client.getOutStream().writeWord(1688);
			client.getOutStream().writeByte(targetSlot);
			client.getOutStream().writeWord(wearID + 1);
			if (amount > 254) {
				client.getOutStream().writeByte(255);
				client.getOutStream().writeDWord(amount);
			} else {
				client.getOutStream().writeByte(amount);
			}
			client.getOutStream().endFrameVarSizeWord();
			client.flushOutStream();
			client.playerEquipment[targetSlot] = wearID;
			client.playerEquipmentN[targetSlot] = amount;
			client.updateRequired = true;
			client.getUpdateFlags().setAppearanceUpdateRequired(true);
		}
	}

	/**
	 * Specials bar filling amount
	 **/

	public void specialAmount(int weapon, double specAmount, int barId) {
		client.specBarId = barId;
		client.getFunction().sendFrame70(specAmount >= 10 ? 500 : 0, 0,
				(--barId));
		client.getFunction().sendFrame70(specAmount >= 9 ? 500 : 0, 0,
				(--barId));
		client.getFunction().sendFrame70(specAmount >= 8 ? 500 : 0, 0,
				(--barId));
		client.getFunction().sendFrame70(specAmount >= 7 ? 500 : 0, 0,
				(--barId));
		client.getFunction().sendFrame70(specAmount >= 6 ? 500 : 0, 0,
				(--barId));
		client.getFunction().sendFrame70(specAmount >= 5 ? 500 : 0, 0,
				(--barId));
		client.getFunction().sendFrame70(specAmount >= 4 ? 500 : 0, 0,
				(--barId));
		client.getFunction().sendFrame70(specAmount >= 3 ? 500 : 0, 0,
				(--barId));
		client.getFunction().sendFrame70(specAmount >= 2 ? 500 : 0, 0,
				(--barId));
		client.getFunction().sendFrame70(specAmount >= 1 ? 500 : 0, 0,
				(--barId));
		updateSpecialBar();
		sendWeapon(weapon, getItemName(weapon));
	}

	public boolean specialCase(int itemId) {
		return false;
	}

	public boolean tradeable(int itemId) {
		for (int element : GameConfig.UNTRADEABLE_ITEMS) {
			if (itemId == element) {
				return false;
			}
		}
		return true;
	}

	public void updateSlot(int slot) {
		synchronized (client) {
			if (client.getOutStream() != null && client != null) {
				client.getOutStream().createFrameVarSizeWord(34);
				client.getOutStream().writeWord(1688);
				client.getOutStream().writeByte(slot);
				client.getOutStream().writeWord(
						client.playerEquipment[slot] + 1);
				if (client.playerEquipmentN[slot] > 254) {
					client.getOutStream().writeByte(255);
					client.getOutStream().writeDWord(
							client.playerEquipmentN[slot]);
				} else {
					client.getOutStream().writeByte(
							client.playerEquipmentN[slot]);
				}
				client.getOutStream().endFrameVarSizeWord();
				client.flushOutStream();
			}
		}

	}

	/**
	 * Special attack text and what to highlight or blackout
	 **/
	public void updateSpecialBar() {
		String percent = Double.toString(client.specAmount);
		if (percent.contains(".")) {
			percent = percent.replace(".", "");
		}
		if (percent.startsWith("0") && !percent.equals("00")) {
			percent = percent.replace("0", "");
		}
		if (percent.startsWith("0") && percent.equals("00")) {
			percent = percent.replace("00", "0");
		}
		client.getFunction().sendFrame126(
				client.usingSpecial ? "@yel@Special Attack: " + percent + "%"
						: "@bla@Special Attack: " + percent + "%",
				client.specBarId);
	}

	/**
	 * Wear Item
	 **/

	public boolean wearItem(int wearID, int slot) {
		synchronized (client) {
			if (client.isDead) {
				return false;
			}
			if (client.inTrade) {
				client.getTrade().declineTrade();
				return false;
			}
			if (client.inDuel) {
				client.getDuel().declineDuel();
				return false;
			}
			if (!client.getItems().playerHasItem(wearID, 1, slot)) {
				return false;
			}
			for (final int i : GameConfig.BANNED_ITEMS) {
				if (wearID == i) {
					return false;
				}
			}
			if (!client.isDonator()) {
				for (final int i : GameConfig.DONATOR_ITEMS) {
					if (wearID == i) {
						client.getItems();
						client.getFunction().state(
								"You must be donate to equip "
										+ ItemAssistant.getItemName(wearID));
						return false;
					}
				}
			}
			if (wearID == CastleWars.SARA_CAPE && client.CWteam() == 2) {
				client.sendMessage("You cannot wear a Saradomin item on the Zamorak team.");
				return false;
			} else if (wearID == CastleWars.ZAMMY_CAPE && client.CWteam() == 1) {
				client.sendMessage("You cannot wear a Zamorak item on the Saradomin team.");
				return false;
			}
			if (wearID == CastleWars.SARA_CAPE
					|| wearID == CastleWars.ZAMMY_CAPE && client.inCastleWars()) {
				return false;
			}
			if (wearID == CastleWars.SARA_CAPE
					|| wearID == CastleWars.ZAMMY_CAPE
					&& client.inWaitingRoom()) {
				return false;
			}
			if (wearID == 6583)
				client.stopMovement();
			client.isNpc = false;
			client.getFunction().checkForMiscItems();
			int targetSlot = 0;
			boolean canWearItem = true;
			client.getCombat().resetPlayerAttack();
			@SuppressWarnings("unused")
			boolean npc = client.getFunction().handleTransformationItem(wearID);
			PlayerSave.saveGame(client);
			if (client.playerItems[slot] == wearID + 1) {
				getRequirements(getItemName(wearID).toLowerCase(), wearID);
				targetSlot = ItemConfig.targetSlots[wearID];

				/*
				 * Castle wars
				 */
				if (CastleWars.isInCw(client) || CastleWars.isInCwWait(client)) {
					if (targetSlot == 1 || targetSlot == 0) {
						return false;
					}
				}
				if (itemType(wearID).equalsIgnoreCase("cape")) {
					targetSlot = 1;
				} else if (itemType(wearID).equalsIgnoreCase("hat")) {
					targetSlot = 0;
				} else if (itemType(wearID).equalsIgnoreCase("amulet")) {
					targetSlot = 2;
				} else if (itemType(wearID).equalsIgnoreCase("arrows")) {
					targetSlot = 13;
				} else if (itemType(wearID).equalsIgnoreCase("body")) {
					targetSlot = 4;
				} else if (itemType(wearID).equalsIgnoreCase("shield")) {
					targetSlot = 5;
				} else if (itemType(wearID).equalsIgnoreCase("legs")) {
					targetSlot = 7;
				} else if (itemType(wearID).equalsIgnoreCase("gloves")) {
					targetSlot = 9;
				} else if (itemType(wearID).equalsIgnoreCase("boots")) {
					targetSlot = 10;
				} else if (itemType(wearID).equalsIgnoreCase("ring")) {
					targetSlot = 12;
				}

				if (client.duelRule[11] && targetSlot == 0) {
					client.sendMessage("Wearing hats has been disabled in this duel!");
					return false;
				}
				if (client.duelRule[12] && targetSlot == 1) {
					client.sendMessage("Wearing capes has been disabled in this duel!");
					return false;
				}
				if (client.duelRule[13] && targetSlot == 2) {
					client.sendMessage("Wearing amulets has been disabled in this duel!");
					return false;
				}
				if (client.duelRule[14] && targetSlot == 3) {
					client.sendMessage("Wielding weapons has been disabled in this duel!");
					return false;
				}
				if (client.duelRule[15] && targetSlot == 4) {
					client.sendMessage("Wearing bodies has been disabled in this duel!");
					return false;
				}
				if (client.duelRule[16] && targetSlot == 5
						|| client.duelRule[16]
						&& is2handed(getItemName(wearID).toLowerCase(), wearID)) {
					client.sendMessage("Wearing shield has been disabled in this duel!");
					return false;
				}
				if (client.duelRule[17] && targetSlot == 7) {
					client.sendMessage("Wearing legs has been disabled in this duel!");
					return false;
				}
				if (client.duelRule[18] && targetSlot == 9) {
					client.sendMessage("Wearing gloves has been disabled in this duel!");
					return false;
				}
				if (client.duelRule[19] && targetSlot == 10) {
					client.sendMessage("Wearing boots has been disabled in this duel!");
					return false;
				}
				if (client.duelRule[20] && targetSlot == 12) {
					client.sendMessage("Wearing rings has been disabled in this duel!");
					return false;
				}
				if (client.duelRule[21] && targetSlot == 13) {
					client.sendMessage("Wearing arrows has been disabled in this duel!");
					return false;
				}
				if (GameConfig.ITEMS_REQUIRE_STATS) {
					if (targetSlot == 10 || targetSlot == 7 || targetSlot == 5
							|| targetSlot == 4 || targetSlot == 0
							|| targetSlot == 9 || targetSlot == 10) {
						if (client.defenceLevelReq > 0) {
							if (client.getFunction().getLevelForXP(
									client.playerXP[1]) < client.defenceLevelReq) {
								client.sendMessage("You need a defence level of "
										+ client.defenceLevelReq
										+ " to wear this item.");
								canWearItem = false;
							}
						}
						if (client.rangeLevelReq > 0) {
							if (client.getFunction().getLevelForXP(
									client.playerXP[4]) < client.rangeLevelReq) {
								client.sendMessage("You need a range level of "
										+ client.rangeLevelReq
										+ " to wear this item.");
								canWearItem = false;
							}
						}
						if (client.magicLevelReq > 0) {
							if (client.getFunction().getLevelForXP(
									client.playerXP[6]) < client.magicLevelReq) {
								client.sendMessage("You need a magic level of "
										+ client.magicLevelReq
										+ " to wear this item.");
								canWearItem = false;
							}
						}
					}
					if (targetSlot == 3) {
						if (client.attackLevelReq > 0) {
							if (client.getFunction().getLevelForXP(
									client.playerXP[0]) < client.attackLevelReq) {
								client.sendMessage("You need an attack level of "
										+ client.attackLevelReq
										+ " to wield this weapon.");
								canWearItem = false;
							}
						}
						if (client.rangeLevelReq > 0) {
							if (client.getFunction().getLevelForXP(
									client.playerXP[4]) < client.rangeLevelReq) {
								client.sendMessage("You need a range level of "
										+ client.rangeLevelReq
										+ " to wield this weapon.");
								canWearItem = false;
							}
						}

						if (client.magicLevelReq > 0) {
							if (client.getFunction().getLevelForXP(
									client.playerXP[6]) < client.magicLevelReq) {
								client.sendMessage("You need a magic level of "
										+ client.magicLevelReq
										+ " to wield this weapon.");
								canWearItem = false;
							}
						}
					}
				}

				if (!canWearItem) {
					return false;
				}

				final int wearAmount = client.playerItemsN[slot];
				if (wearAmount < 1) {
					return false;
				}

				if (targetSlot == client.playerWeapon) {
					client.getFunction().resetAutocast();
				}
				if (slot >= 0 && wearID >= 0) {
					if (wearID == 12601 || wearID == 12603 || wearID == 12605)
						targetSlot = 12;
					else if (wearID == 11924 || wearID == 11926)
						targetSlot = 5;
					final int toEquip = client.playerItems[slot];
					final int toEquipN = client.playerItemsN[slot];
					int toRemove = client.playerEquipment[targetSlot];
					int toRemoveN = client.playerEquipmentN[targetSlot];
					/*
					 * Castle wars
					 */
					if (CastleWars.SARA_BANNER == toRemove
							|| CastleWars.ZAMMY_BANNER == toRemove) {
						CastleWars.dropFlag(client, toRemove);
						CastleWars.deleteWeapon(client);
						deleteItem(CastleWars.ZAMMY_BANNER, 1);
						deleteItem(CastleWars.SARA_BANNER, 1);
						return false;
					}
					if (toEquip == toRemove + 1
							&& ItemConfig.itemStackable[toRemove]) {
						deleteItem(toRemove, getItemSlot(toRemove), toEquipN);
						client.playerEquipmentN[targetSlot] += toEquipN;
					} else if (targetSlot != 5 && targetSlot != 3) {
						client.playerItems[slot] = toRemove + 1;
						client.playerItemsN[slot] = toRemoveN;
						client.playerEquipment[targetSlot] = toEquip - 1;
						client.playerEquipmentN[targetSlot] = toEquipN;
					} else if (targetSlot == 5) {
						final boolean wearing2h = is2handed(
								getItemName(
										client.playerEquipment[client.playerWeapon])
										.toLowerCase(),
								client.playerEquipment[client.playerWeapon]);
						if (wearing2h) {
							toRemove = client.playerEquipment[client.playerWeapon];
							toRemoveN = client.playerEquipmentN[client.playerWeapon];
							client.playerEquipment[client.playerWeapon] = -1;
							client.playerEquipmentN[client.playerWeapon] = 0;
							updateSlot(client.playerWeapon);
						}
						client.playerItems[slot] = toRemove + 1;
						client.playerItemsN[slot] = toRemoveN;
						client.playerEquipment[targetSlot] = toEquip - 1;
						client.playerEquipmentN[targetSlot] = toEquipN;
					} else if (targetSlot == 3) {
						final boolean is2h = is2handed(getItemName(wearID)
								.toLowerCase(), wearID);
						final boolean wearingShield = client.playerEquipment[client.playerShield] > 0;
						final boolean wearingWeapon = client.playerEquipment[client.playerWeapon] > 0;
						if (is2h) {
							if (wearingShield && wearingWeapon) {
								if (freeSlots() > 0) {
									client.playerItems[slot] = toRemove + 1;
									client.playerItemsN[slot] = toRemoveN;
									client.playerEquipment[targetSlot] = toEquip - 1;
									client.playerEquipmentN[targetSlot] = toEquipN;
									removeItem(
											client.playerEquipment[client.playerShield],
											client.playerShield);
								} else {
									client.sendMessage("You do not have enough inventory space to do this.");
									return false;
								}
							} else if (wearingShield && !wearingWeapon) {
								client.playerItems[slot] = client.playerEquipment[client.playerShield] + 1;
								client.playerItemsN[slot] = client.playerEquipmentN[client.playerShield];
								client.playerEquipment[targetSlot] = toEquip - 1;
								client.playerEquipmentN[targetSlot] = toEquipN;
								client.playerEquipment[client.playerShield] = -1;
								client.playerEquipmentN[client.playerShield] = 0;
								updateSlot(client.playerShield);
							} else {
								client.playerItems[slot] = toRemove + 1;
								client.playerItemsN[slot] = toRemoveN;
								client.playerEquipment[targetSlot] = toEquip - 1;
								client.playerEquipmentN[targetSlot] = toEquipN;
							}
						} else {
							client.playerItems[slot] = toRemove + 1;
							client.playerItemsN[slot] = toRemoveN;
							client.playerEquipment[targetSlot] = toEquip - 1;
							client.playerEquipmentN[targetSlot] = toEquipN;
						}
					}
					resetItems(3214);
				}
				if (targetSlot == 3) {
					client.usingSpecial = false;
					addSpecialBar(wearID);
				}

				if (client.getOutStream() != null && client != null) {
					client.getOutStream().createFrameVarSizeWord(34);
					client.getOutStream().writeWord(1688);
					client.getOutStream().writeByte(targetSlot);
					client.getOutStream().writeWord(wearID + 1);

					if (client.playerEquipmentN[targetSlot] > 254) {
						client.getOutStream().writeByte(255);
						client.getOutStream().writeDWord(
								client.playerEquipmentN[targetSlot]);
					} else {
						client.getOutStream().writeByte(
								client.playerEquipmentN[targetSlot]);
					}

					client.getOutStream().endFrameVarSizeWord();
					client.flushOutStream();

					client.getFunction().requestUpdates();
				}
				sendWeapon(
						client.playerEquipment[client.playerWeapon],
						getItemName(client.playerEquipment[client.playerWeapon]));
				resetBonus();
				getBonus();
				writeBonus();
				client.getItems();
				client.getCombat().getPlayerAnimIndex(
						ItemAssistant.getItemName(
								client.playerEquipment[client.playerWeapon])
								.toLowerCase());
				client.getFunction().requestUpdates();

				PlayerSave.saveGame(client);
				return true;
			} else {
				return false;
			}
		}
	}

	public void wearItem(int wearID, int wearAmount, int targetSlot) {
		synchronized (client) {
			if (client.getOutStream() != null && client != null) {
				client.getOutStream().createFrameVarSizeWord(34);
				client.getOutStream().writeWord(1688);
				client.getOutStream().writeByte(targetSlot);
				client.getOutStream().writeWord(wearID + 1);

				if (wearAmount > 254) {
					client.getOutStream().writeByte(255);
					client.getOutStream().writeDWord(wearAmount);
				} else {
					client.getOutStream().writeByte(wearAmount);
				}
				client.getOutStream().endFrameVarSizeWord();
				client.flushOutStream();
				client.playerEquipment[targetSlot] = wearID;
				client.playerEquipmentN[targetSlot] = wearAmount;
				client.getItems();
				client.getItems()
						.sendWeapon(
								client.playerEquipment[client.playerWeapon],
								ItemAssistant
										.getItemName(client.playerEquipment[client.playerWeapon]));
				client.getItems().resetBonus();
				client.getItems().getBonus();
				client.getItems().writeBonus();
				client.getItems();
				client.getCombat().getPlayerAnimIndex(
						ItemAssistant.getItemName(
								client.playerEquipment[client.playerWeapon])
								.toLowerCase());
				client.updateRequired = true;
				client.getUpdateFlags().setAppearanceUpdateRequired(true);

				PlayerSave.saveGame(client);
			}
		}
	}

	public void writeBonus() {
		int offset = 0;
		String send = "";
		for (int i = 0; i < client.playerBonus.length; i++) {
			if (client.playerBonus[i] >= 0) {
				send = BONUS_NAMES[i] + ": +" + client.playerBonus[i];
			} else {
				send = BONUS_NAMES[i] + ": -"
						+ java.lang.Math.abs(client.playerBonus[i]);
			}

			if (i == 10) {
				offset = 1;
			}
			client.getFunction().sendFrame126(send, (1675 + i + offset));
		}

	}

	public boolean bankItem(int itemID, int fromSlot, int amount, int[] array,
			int[] arrayN) {
		if (client.inTrade) {
			client.sendMessage("You can't store items while trading!");
			return false;
		}
		if (client.playerItems[fromSlot] <= 0
				|| client.playerItemsN[fromSlot] <= 0) {
			return false;
		}
		if (!ItemConfig.itemIsNote[client.playerItems[fromSlot] - 1]) {
			if (client.playerItems[fromSlot] <= 0) {
				return false;
			}
			if (ItemConfig.itemStackable[client.playerItems[fromSlot] - 1]
					|| client.playerItemsN[fromSlot] > 1) {
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
					if (array[i] == client.playerItems[fromSlot]) {
						if (client.playerItemsN[fromSlot] < amount)
							amount = client.playerItemsN[fromSlot];
						alreadyInBank = true;
						toBankSlot = i;
						i = GameConfig.BANK_SIZE + 1;
					}
				}

				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
						if (array[i] <= 0) {
							toBankSlot = i;
							i = GameConfig.BANK_SIZE + 1;
						}
					}
					array[toBankSlot] = client.playerItems[fromSlot];
					if (client.playerItemsN[fromSlot] < amount) {
						amount = client.playerItemsN[fromSlot];
					}
					if ((arrayN[toBankSlot] + amount) <= GameConfig.MAXITEM_AMOUNT
							&& (arrayN[toBankSlot] + amount) > -1) {
						arrayN[toBankSlot] += amount;
					} else {
						client.sendMessage("Bank full!");
						return false;
					}
					deleteItem((client.playerItems[fromSlot] - 1), fromSlot,
							amount);
					resetBank();
					return true;
				} else if (alreadyInBank) {
					if ((arrayN[toBankSlot] + amount) <= GameConfig.MAXITEM_AMOUNT
							&& (arrayN[toBankSlot] + amount) > -1) {
						arrayN[toBankSlot] += amount;
					} else {
						client.sendMessage("Bank full!");
						return false;
					}
					deleteItem((client.playerItems[fromSlot] - 1), fromSlot,
							amount);
					resetBank();
					return true;
				} else {
					client.sendMessage("Bank full!");
					return false;
				}
			} else {
				itemID = client.playerItems[fromSlot];
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
					if (array[i] == client.playerItems[fromSlot]) {
						alreadyInBank = true;
						toBankSlot = i;
						i = GameConfig.BANK_SIZE + 1;
					}
				}
				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
						if (array[i] <= 0) {
							toBankSlot = i;
							i = GameConfig.BANK_SIZE + 1;
						}
					}
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < client.playerItems.length; i++) {
							if ((client.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							array[toBankSlot] = client.playerItems[firstPossibleSlot];
							arrayN[toBankSlot] += 1;
							deleteItem(
									(client.playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetBank();
					return true;
				} else if (alreadyInBank) {
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < client.playerItems.length; i++) {
							if ((client.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							arrayN[toBankSlot] += 1;
							deleteItem(
									(client.playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetBank();
					return true;
				} else {
					client.sendMessage("Bank full!");
					return false;
				}
			}
		} else if (ItemConfig.itemIsNote[client.playerItems[fromSlot] - 1]
				&& !ItemConfig.itemIsNote[client.playerItems[fromSlot] - 2]) {
			if (client.playerItems[fromSlot] <= 0) {
				return false;
			}
			if (ItemConfig.itemStackable[client.playerItems[fromSlot] - 1]
					|| client.playerItemsN[fromSlot] > 1) {
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
					if (array[i] == (client.playerItems[fromSlot] - 1)) {
						if (client.playerItemsN[fromSlot] < amount)
							amount = client.playerItemsN[fromSlot];
						alreadyInBank = true;
						toBankSlot = i;
						i = GameConfig.BANK_SIZE + 1;
					}
				}

				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
						if (array[i] <= 0) {
							toBankSlot = i;
							i = GameConfig.BANK_SIZE + 1;
						}
					}
					array[toBankSlot] = (client.playerItems[fromSlot] - 1);
					if (client.playerItemsN[fromSlot] < amount) {
						amount = client.playerItemsN[fromSlot];
					}
					if ((arrayN[toBankSlot] + amount) <= GameConfig.MAXITEM_AMOUNT
							&& (arrayN[toBankSlot] + amount) > -1) {
						arrayN[toBankSlot] += amount;
					} else {
						return false;
					}
					deleteItem((client.playerItems[fromSlot] - 1), fromSlot,
							amount);
					resetBank();
					return true;
				} else if (alreadyInBank) {
					if ((arrayN[toBankSlot] + amount) <= GameConfig.MAXITEM_AMOUNT
							&& (arrayN[toBankSlot] + amount) > -1) {
						arrayN[toBankSlot] += amount;
					} else {
						return false;
					}
					deleteItem((client.playerItems[fromSlot] - 1), fromSlot,
							amount);
					resetBank();
					return true;
				} else {
					client.sendMessage("Bank full!");
					return false;
				}
			} else {
				itemID = client.playerItems[fromSlot];
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
					if (array[i] == (client.playerItems[fromSlot] - 1)) {
						alreadyInBank = true;
						toBankSlot = i;
						i = GameConfig.BANK_SIZE + 1;
					}
				}
				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
						if (array[i] <= 0) {
							toBankSlot = i;
							i = GameConfig.BANK_SIZE + 1;
						}
					}
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < client.playerItems.length; i++) {
							if ((client.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							array[toBankSlot] = (client.playerItems[firstPossibleSlot] - 1);
							arrayN[toBankSlot] += 1;
							deleteItem(
									(client.playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBank();
					return true;
				} else if (alreadyInBank) {
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < client.playerItems.length; i++) {
							if ((client.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							arrayN[toBankSlot] += 1;
							deleteItem(
									(client.playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBank();
					return true;
				} else {
					client.sendMessage("Bank full!");
					return false;
				}
			}
		} else {
			client.sendMessage("Item not supported "
					+ (client.playerItems[fromSlot] - 1));
			return false;
		}
	}

	public void toTab(int tab, int fromSlot) {
		if (tab == client.bankingTab)
			return;
		if (tab > client.getTask().getTabCount() + 1)
			return;
		if (client.searchTerm != "N/A")
			return;
		if (client.getTask().getBankItems(tab) >= 350) {
			client.sendMessage("You can't store any more items in this tab!");
			return;
		}
		int id = client.bankingItems[fromSlot];
		/*
		 * if(getTotalCount(id) == 0) return;//player doesn't have item!
		 */
		int amount = client.bankingItemsN[fromSlot];
		int[] invItems = new int[28];
		int[] invItemsN = new int[28];
		for (int i = 0; i < client.playerItems.length; i++) {
			invItems[i] = client.playerItems[i];
			invItemsN[i] = client.playerItemsN[i];
			client.playerItems[i] = 0;
			client.playerItemsN[i] = 0;
		}
		client.playerItems[0] = id;
		client.playerItemsN[0] = amount;
		client.bankingItems[fromSlot] = -1;
		client.bankingItemsN[fromSlot] = 0;
		if (tab == 0)
			bankItem(id, 0, amount, client.bankItems, client.bankItemsN);
		else if (tab == 1)
			bankItem(id, 0, amount, client.bankItems1, client.bankItems1N);
		else if (tab == 2)
			bankItem(id, 0, amount, client.bankItems2, client.bankItems2N);
		else if (tab == 3)
			bankItem(id, 0, amount, client.bankItems3, client.bankItems3N);
		else if (tab == 4)
			bankItem(id, 0, amount, client.bankItems4, client.bankItems4N);
		else if (tab == 5)
			bankItem(id, 0, amount, client.bankItems5, client.bankItems5N);
		else if (tab == 6)
			bankItem(id, 0, amount, client.bankItems6, client.bankItems6N);
		else if (tab == 7)
			bankItem(id, 0, amount, client.bankItems7, client.bankItems7N);
		else if (tab == 8)
			bankItem(id, 0, amount, client.bankItems8, client.bankItems8N);
		for (int i = 0; i < invItems.length; i++) {
			client.playerItems[i] = invItems[i];
			client.playerItemsN[i] = invItemsN[i];
		}
		client.getTask().openUpBank(client.bankingTab); // refresh
		client.getTask().openUpBank(client.bankingTab); // refresh twice to ensure
		// update
		updateInventory = true;
		updateInventory();
		client.getTask().sendTabs();

	}
	public boolean updateInventory = false;

	public void updateInventory() {
		updateInventory = false;
		resetItems(3214);
	}

}