package com.ownxile.rs2.world.games;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import com.ownxile.config.GameConfig;
import com.ownxile.config.ItemConfig;
import com.ownxile.core.World;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.Player;
import com.ownxile.rs2.player.PlayerHandler;
import com.ownxile.rs2.world.object.BalloonObject;
import com.ownxile.rs2.world.object.type.TemporaryObject;

public class PartyRoom {

	public static ArrayList<BalloonObject> balloons = new ArrayList<BalloonObject>();
	public static ArrayList<Point> coords = new ArrayList<Point>();
	static Random r = new Random();
	static int[] roomItems = new int[50];
	static int[] roomItemsN = new int[50];

	public static void accept(Client c) {
		for (int x = 0; x < c.party.length; x++) {
			if (c.partyN[x] > 0) {
				if (ItemConfig.itemStackable[c.party[x]]) {
					final int slot = arraySlot(roomItems, c.party[x]);
					if (slot < 0) {
						c.sendMessage("Theres not enought space on party room.");
						break;
					}
					if (roomItems[slot] != c.party[x]) {
						roomItems[slot] = c.party[x];
						roomItemsN[slot] = c.partyN[x];
					} else {
						roomItemsN[slot] += c.partyN[x];
					}
					c.party[x] = -1;
					c.partyN[x] = 0;
				} else {
					final int left = c.partyN[x];
					for (int y = 0; y < left; y++) {
						final int slot = arraySlot(roomItems, -2);
						if (slot < 0) {
							c.sendMessage("Theres not enought space on party room.");
							break;
						}
						roomItems[slot] = c.party[x];
						roomItemsN[slot] = 1;
						c.partyN[x]--;
					}
					if (c.partyN[x] <= 0) {
						c.party[x] = -1;
					}
				}
			}
		}
		updateDeposit(c);
		updateGlobal(c);
	}

	public static int arraySlot(int[] array, int target) {
		int spare = -1;
		for (int x = 0; x < array.length; x++) {
			if (array[x] == target) {
				return x;
			} else if (spare == -1 && array[x] <= 0) {
				spare = x;
			}
		}
		return spare;
	}

	public static void depositItem(Client c, int id, int amount) {

		final int slot = arraySlot(c.party, id);
		for (final int i : GameConfig.UNTRADEABLE_ITEMS) {
			if (i == id) {
				c.sendMessage("You can't add this item.");
				return;
			}
		}
		if (c.getItems().getItemAmount(id) < amount) {
			amount = c.getItems().getItemAmount(id);
		}
		if (!c.getItems().playerHasItem(id, amount)) {
			c.sendMessage("You don't have that many items.");
			return;
		}
		if (slot == -1) {
			c.sendMessage("You cant deposit more than 8 items at once.");
			return;
		}
		c.getItems().deleteItem2(id, amount);
		if (c.party[slot] != id) {
			c.party[slot] = id;
			c.partyN[slot] = amount;
		} else {
			c.party[slot] = id;
			c.partyN[slot] += amount;
		}
		updateDeposit(c);
	}

	public static void dropAll() {
		if (!balloons.isEmpty()) {
			balloons.clear();
		}
		int trys = 0;
		final int amount = getAmount();
		if (amount < 1) {
			return;
		}
		for (int x = 0; x < roomItems.length; x++) {
			if (roomItemsN[x] > 0) {
				BalloonObject b = null;
				do {
					b = BalloonObject.getBalloon(roomItems[x], roomItemsN[x]);
					trys++;
				} while (coords.contains(BalloonObject.getCoords())
						&& trys < 100);
				World.getObjectHandler().addObject(b);
				World.getObjectHandler().placeObject(b);
			}
			if (trys > 100) {
				break;
			}
			roomItems[x] = 0;
			roomItemsN[x] = 0;
		}
		trys = 0;
		for (int x = 0; x < amount * 2; x++) {
			TemporaryObject o;
			do {
				o = BalloonObject.getEmpty();
			} while (coords.contains(new Point(o.objectX, o.objectY))
					&& trys < 100);
			if (trys > 100) {
				break;
			}
			World.getObjectHandler().addObject(o);
			World.getObjectHandler().placeObject(o);
		}
		World.sendMessage("@dre@It's party time in the seers village!");
		coords.clear();
	}

	public static void fix(Client c) {
		for (int x = 0; x < 8; x++) {
			if (c.party[x] < 0) {
				c.partyN[x] = 0;
			} else if (c.partyN[x] <= 0) {
				c.party[x] = 0;
			}
		}
	}

	public static int getAmount() {
		int amount = 0;
		for (int roomItem : roomItems) {
			if (roomItem > 0) {
				amount++;
			}
		}
		return amount;
	}

	public static BalloonObject getBalloon(int obX, int obY) {
		for (BalloonObject b : balloons) {
			if (obX == b.objectX && obY == b.objectY) {
				return b;
			}
		}
		return null;
	}

	public static void itemOnInterface(Client c, int frame, int slot, int id,
			int amount) {
		c.outStream.createFrameVarSizeWord(34);
		c.outStream.writeWord(frame);
		c.outStream.writeByte(slot);
		c.outStream.writeWord(id + 1);
		c.outStream.writeByte(255);
		c.outStream.writeDWord(amount);
		c.outStream.endFrameVarSizeWord();
	}

	public static void open(Client c) {
		updateGlobal(c);
		updateDeposit(c);
		c.getItems().resetItems(5064);
		c.getFunction().sendFrame248(2156, 5063);
	}

	public static void updateAll() {
		for (Player player : PlayerHandler.players) {
			updateGlobal((Client) player);
		}
	}

	public static void updateDeposit(Client c) {
		c.getItems().resetItems(5064);
		for (int x = 0; x < 8; x++) {
			if (c.partyN[x] <= 0) {
				itemOnInterface(c, 2274, x, -1, 0);
			} else {
				itemOnInterface(c, 2274, x, c.party[x], c.partyN[x]);
			}
		}
	}

	public static void updateGlobal(Client c) {
		for (int x = 0; x < roomItems.length; x++) {
			if (roomItemsN[x] <= 0) {
				itemOnInterface(c, 2273, x, -1, 0);
			} else {
				itemOnInterface(c, 2273, x, roomItems[x], roomItemsN[x]);
			}
		}
	}

	public static void withdrawItem(Client c, int slot) {
		if (c.party[slot] >= 0 && c.getItems().freeSlots() > 0) {
			c.getItems().addItem(c.party[slot], c.partyN[slot]);
			c.party[slot] = 0;
			c.partyN[slot] = 0;
		}
		updateDeposit(c);
		updateGlobal(c);
	}

	// 2273 items on chest
	// 2274 personal
}