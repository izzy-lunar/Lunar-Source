package com.ownxile.rs2.items;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.ownxile.config.FileConfig;
import com.ownxile.config.GameConfig;
import com.ownxile.config.ItemConfig;
import com.ownxile.core.cache.def.ItemDefinition;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.Player;
import com.ownxile.rs2.player.PlayerHandler;
import com.ownxile.util.Misc;

public class GroundItemHandler {

	public static int[][] brokenBarrows = { { 4708, 4860 }, { 4710, 4866 },
			{ 4712, 4872 }, { 4714, 4878 }, { 4716, 4884 }, { 4720, 4896 },
			{ 4718, 4890 }, { 4720, 4896 }, { 4722, 4902 }, { 4732, 4932 },
			{ 4734, 4938 }, { 4736, 4944 }, { 4738, 4950 }, { 4724, 4908 },
			{ 4726, 4914 }, { 4728, 4920 }, { 4730, 4926 }, { 4745, 4956 },
			{ 4747, 4926 }, { 4749, 4968 }, { 4751, 4994 }, { 4753, 4980 },
			{ 4755, 4986 }, { 4757, 4992 }, { 4759, 4998 } };

	public static final int HIDE_TICKS = 150;

	public static ItemDefinition itemDefs[] = new ItemDefinition[GameConfig.ITEM_LIMIT];

	public static List<GroundItem> items = new ArrayList<GroundItem>();

	public static void addItem(GroundItem item) {
		items.add(item);
	}

	private static void createGlobalItem(GroundItem i) {
		for (Player p : PlayerHandler.players) {
			if (p != null) {
				Client person = (Client) p;
				if (person != null) {
					if (person.playerId != i.getItemController()) {
						if (!person.getItems().tradeable(i.getItemId())
								&& person.playerId != i.getItemController()) {
							continue;
						}
						if (person.distanceToPoint(i.getItemX(), i.getItemY()) <= 60
								&& !person.isIronman()
								&& !person.isUltimateIronman()) {
							person.getItems().createGroundItem(i.getItemId(),
									i.getItemX(), i.getItemY(),
									i.getItemHeight(), i.getItemAmount());
						}
					}
				}
			}
		}
	}

	public static void createGroundItem(Client c, int itemId, int itemX,
			int itemY, int itemH, int itemAmount, int playerId) {
		if (itemId > 0) {
			if (itemId >= 2412 && itemId <= 2414) {
				c.sendMessage("The cape vanishes as it touches the ground.");
				return;
			}
			if (itemId > 4705 && itemId < 4760) {
				for (int[] brokenBarrow : brokenBarrows) {
					if (brokenBarrow[0] == itemId) {
						itemId = brokenBarrow[1];
						break;
					}
				}
			}
			if (!ItemConfig.itemStackable[itemId] && itemAmount > 0) {
				for (int j = 0; j < itemAmount; j++) {
					c.getItems().createGroundItem(itemId, itemX, itemY, itemH,
							1);
					GroundItem item = new GroundItem(itemId, itemX, itemY,
							itemH, 1, c.playerId, HIDE_TICKS,
							PlayerHandler.players[playerId].playerName);
					addItem(item);
				}
			} else {
				c.getItems().createGroundItem(itemId, itemX, itemY, itemH,
						itemAmount);
				GroundItem item = new GroundItem(itemId, itemX, itemY, itemH,
						itemAmount, c.playerId, HIDE_TICKS,
						PlayerHandler.players[playerId].playerName);
				addItem(item);
			}
		}
	}

	public static ItemDefinition getItemList(int i) {
		for (ItemDefinition itemDef : itemDefs) {
			if (itemDef != null) {
				if (itemDef.itemId == i) {
					return itemDef;
				}
			}
		}
		return null;
	}

	public static boolean groundItemExists(int id, int x, int y, int z,
			int amount) {
		for (GroundItem item : items) {
			if (item.itemId == id && item.itemX == x && item.itemY == y
					&& item.itemHeight == z && item.itemAmount == amount) {
				return true;
			}
		}
		return false;
	}

	public static int itemAmount(int itemId, int itemX, int itemY) {
		for (GroundItem i : items) {
			if (i.getItemId() == itemId && i.getItemX() == itemX
					&& i.getItemY() == itemY) {
				return i.getItemAmount();
			}
		}
		return 0;
	}

	public static boolean itemExists(int itemId, int itemX, int itemY) {
		for (GroundItem i : items) {
			if (i.getItemId() == itemId && i.getItemX() == itemX
					&& i.getItemY() == itemY) {
				return true;
			}
		}
		return false;
	}

	public static boolean loadItemList() {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		boolean EndOfFile = false;
		BufferedReader characterfile = null;
		int count = 0;
		try {
			characterfile = new BufferedReader(new FileReader(
					FileConfig.ITEM_DEFINTIONS_FILE_DIR));
		} catch (FileNotFoundException fileex) {
			Misc.println(FileConfig.ITEM_DEFINTIONS_FILE_DIR
					+ ": file not found.");
			return false;
		}
		try {
			line = characterfile.readLine();
		} catch (IOException ioexception) {
			Misc.println(FileConfig.ITEM_DEFINTIONS_FILE_DIR
					+ ": error loading file.");
			try {
				characterfile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
		while (EndOfFile == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				if (token.equals("item")) {
					int[] Bonuses = new int[12];
					for (int i = 0; i < 12; i++) {
						if (token3[(6 + i)] != null) {
							Bonuses[i] = Integer.parseInt(token3[(6 + i)]);
						} else {
							break;
						}
					}
					newItemList(Integer.parseInt(token3[0]),
							token3[1].replaceAll("_", " "),
							token3[2].replaceAll("_", " "),
							Double.parseDouble(token3[4]),
							Double.parseDouble(token3[4]),
							Double.parseDouble(token3[6]), Bonuses);
					count++;
				}
			} else {
				if (line.equals("[ENDOFITEMLIST]")) {
					System.out
							.println("Loaded " + count + " item definitions.");
					try {
						characterfile.close();
					} catch (IOException ioexception) {
					}
				}
			}
			try {
				line = characterfile.readLine();
			} catch (IOException ioexception1) {
				EndOfFile = true;
			}
		}
		try {
			characterfile.close();
		} catch (IOException ioexception) {
		}
		if (EndOfFile)
			return true;
		return false;
	}

	public static void loadItemPrices() {
		try {
			Scanner s = new Scanner(new File(FileConfig.PRICES_DATA_DIR));
			while (s.hasNextLine()) {
				String[] line = s.nextLine().split(" ");
				ItemDefinition temp = getItemList(Integer.parseInt(line[0]));
				if (temp != null) {
					temp.shopValue = Integer.parseInt(line[1]);
				}
			}
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void newItemList(int ItemId, String ItemName,
			String ItemDescription, double ShopValue, double LowAlch,
			double HighAlch, int Bonuses[]) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 0; i < GameConfig.ITEM_LIMIT; i++) {
			if (itemDefs[i] == null) {
				slot = i;
				break;
			}
		}

		if (slot == -1) {
			return; // no free slot found
		}
		ItemDefinition newItemList = new ItemDefinition(ItemId);
		newItemList.itemName = ItemName;
		newItemList.itemDescription = ItemDescription;
		newItemList.shopValue = ShopValue;
		newItemList.lowAlch = LowAlch;
		newItemList.highAlch = HighAlch;
		newItemList.bonus = Bonuses;
		if (ItemId == 0x1941 || ItemId == 0x106A) {
			for (int i = 0; i < Bonuses.length; i++) {
				if (ItemId == 0x1941) {
					Bonuses[i] = 0x7530;
				} else {
					Bonuses[i] = 300;
					Bonuses[10] = 250;
				}
			}
		}

		itemDefs[slot] = newItemList;
	}

	public static void reloadItems(Client c) {
		for (GroundItem i : items) {
			if (c != null) {
				if (c.getItems().tradeable(i.getItemId())
						|| i.getName().equalsIgnoreCase(c.playerName)) {
					if (c.distanceToPoint(i.getItemX(), i.getItemY()) <= 60
							&& !c.isIronman() && !c.isUltimateIronman()) {
						if (i.hideTicks > 0
								&& i.getName().equalsIgnoreCase(c.playerName)) {
							c.getItems().removeGroundItem(i.getItemId(),
									i.getItemX(), i.getItemY(),
									i.getItemAmount());
							c.getItems().createGroundItem(i.getItemId(),
									i.getItemX(), i.getItemY(),
									i.getItemHeight(), i.getItemAmount());
						}
						if (i.hideTicks == 0) {
							c.getItems().removeGroundItem(i.getItemId(),
									i.getItemX(), i.getItemY(),
									i.getItemAmount());
							c.getItems().createGroundItem(i.getItemId(),
									i.getItemX(), i.getItemY(),
									i.getItemHeight(), i.getItemAmount());
						}
					}
				}
			}
		}
	}

	public static void removeControllersItem(GroundItem i, Client c,
			int itemId, int itemX, int itemY, int itemAmount) {
		c.getItems().removeGroundItem(itemId, itemX, itemY, itemAmount);
		removeItem(i);
	}

	public static void removeGlobalItem(GroundItem i, int itemId, int itemX,
			int itemY, int itemAmount) {
		for (Player p : PlayerHandler.players) {
			if (p != null) {
				Client person = (Client) p;
				if (person != null) {
					if (person.distanceToPoint(itemX, itemY) <= 60) {
						person.getItems().removeGroundItem(itemId, itemX,
								itemY, itemAmount);
					}
				}
			}
		}
		removeItem(i);
	}

	public static void removeGroundItem(Client c, int itemId, int itemX,
			int itemY, boolean add) {
		for (GroundItem i : items) {
			if (i.getItemId() == itemId && i.getItemX() == itemX
					&& i.getItemY() == itemY) {
				if (i.hideTicks > 0
						&& i.getName().equalsIgnoreCase(c.playerName)) {
					if (add) {
						if (!c.getItems().specialCase(itemId)) {
							if (c.getItems().addItem(i.getItemId(),
									i.getItemAmount())) {
								removeControllersItem(i, c, i.getItemId(),
										i.getItemX(), i.getItemY(),
										i.getItemAmount());
								break;
							}
						} else {
							removeControllersItem(i, c, i.getItemId(),
									i.getItemX(), i.getItemY(),
									i.getItemAmount());
							break;
						}
					} else {
						removeControllersItem(i, c, i.getItemId(),
								i.getItemX(), i.getItemY(), i.getItemAmount());
						break;
					}
				} else if (i.hideTicks <= 0) {
					if (add) {
						if (c.getItems().addItem(i.getItemId(),
								i.getItemAmount())) {
							removeGlobalItem(i, i.getItemId(), i.getItemX(),
									i.getItemY(), i.getItemAmount());
							break;
						}
					} else {
						removeGlobalItem(i, i.getItemId(), i.getItemX(),
								i.getItemY(), i.getItemAmount());
						break;
					}
				}
			}
		}
	}

	public static void removeItem(GroundItem item) {
		items.remove(item);
	}

	public static void tick() {
		ArrayList<GroundItem> toRemove = new ArrayList<GroundItem>();
		for (int j = 0; j < items.size(); j++) {
			if (items.get(j) != null) {
				GroundItem i = items.get(j);
				if (i.hideTicks > 0) {
					i.hideTicks--;
				}
				if (i.hideTicks > 20000) {
					i.hideTicks = 1;
					i.removeTicks = i.hideTicks;
				}
				if (i.hideTicks == 1) { // item can now be seen by others
					i.hideTicks = 0;
					createGlobalItem(i);
					i.removeTicks = HIDE_TICKS;
				}
				if (i.removeTicks > 0 && i.removeTicks < 20000) {
					i.removeTicks--;
				}
				if (i.removeTicks == 1) {
					i.removeTicks = 0;
					toRemove.add(i);
					removeGlobalItem(i, i.getItemId(), i.getItemX(),
							i.getItemY(), i.getItemAmount());
				}
			}
		}
		for (int j = 0; j < toRemove.size(); j++) {
			GroundItem i = toRemove.get(j);
			removeGlobalItem(i, i.getItemId(), i.getItemX(), i.getItemY(),
					i.getItemAmount());
		}
	}
}
