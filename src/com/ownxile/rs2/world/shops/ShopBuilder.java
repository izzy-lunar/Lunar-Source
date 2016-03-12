package com.ownxile.rs2.world.shops;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ownxile.config.FileConfig;
import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.items.ItemAssistant;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.Player;
import com.ownxile.rs2.player.PlayerHandler;

public class ShopBuilder {

	public static List<Shop> shops = new ArrayList<Shop>();

	public static int getItemBuyPrice(int itemId) {
		return ItemAssistant.getItemValue(itemId);
	}

	public static int getItemSellPrice(int itemId) {
		return ItemAssistant.getItemValue(itemId) / 2;
	}

	public static Shop getShop(int id) {
		for (Shop shop : shops) {
			if (id == shop.getId()) {
				return shop;
			}
		}
		return null;
	}

	public static void init() {
		World.getSynchronizedTaskScheduler().schedule(new Task(50, false) {
			@Override
			protected void execute() {
				restock();
			}
		});
	}

	public static boolean loadShops() {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[(101 * 2)];
		boolean EndOfFile = false;
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(
					FileConfig.SHOP_CONFIG_FILE_DIR));
		} catch (final FileNotFoundException fileNotFoundException) {
			fileNotFoundException.printStackTrace();
			return false;
		}
		try {
			line = bufferedReader.readLine();
		} catch (final IOException ioException) {
			ioException.printStackTrace();
			try {
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
		while (EndOfFile == false && line != null) {
			line = line.trim();
			final int spot = line.indexOf("=");
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
				if (token.equals("shop")) {
					Shop shop = new Shop();
					int slot = 0;
					shop.setId(Integer.parseInt(token3[0]));
					shop.setName(token3[1].replaceAll("_", " "));
					shop.sell = Integer.parseInt(token3[2]);
					shop.buy = Integer.parseInt(token3[3]);
					int itemId, itemAmount;
					for (int i = 0; i < (token3.length - 4) / 2; i++) {
						if (token3[(4 + i * 2)] != null) {
							itemId = Integer.parseInt(token3[(4 + i * 2)]) + 1;
							itemAmount = Integer.parseInt(token3[(5 + i * 2)]);
							ShopItem item = new ShopItem(itemId - 1, itemAmount);
							item.shopSlot = slot;
							shop.addItem(item);
							slot++;
						} else {
							break;
						}
					}
					shops.add(shop);
				}
			} else {
				if (line.equals("[ENDOFSHOPLIST]")) {
					try {
						bufferedReader.close();
					} catch (final IOException ioexception) {
					}
					/*
					 * System.out.println("Loaded a total of " + shops.size() +
					 * " shops.");
					 */
				}
			}
			try {
				line = bufferedReader.readLine();
			} catch (final IOException ioexception1) {
				EndOfFile = true;
			}
		}
		try {
			bufferedReader.close();
		} catch (final IOException ioexception) {
		}
		if (EndOfFile)
			return true;
		return false;
	}

	public static void printShops() {
		for (Shop shop : shops) {
			shop.out();
		}
	}

	private static void restock() {
		for (Shop shop : shops) {
			for (ShopItem shopItem : shop.getShopItems()) {
				if (shopItem.getOriginalAmount() > shopItem.amount) {
					shopItem.amount++;
				}
			}
			update(shop.getId());
		}

	}

	public static void sendShopInterface(Client player, Shop shop) {
		player.getItems().resetItems(3823);
		player.getOutStream().createFrameVarSizeWord(53);
		player.getOutStream().writeWord(3900);
		player.getOutStream().writeWord(shop.getShopItems().size());
		for (ShopItem gameItem : shop.getShopItems()) {
			if (gameItem.amount > 254) {
				player.getOutStream().writeByte(255);
				player.getOutStream().writeDWord_v2(gameItem.amount);
			} else {
				player.getOutStream().writeByte(gameItem.amount);
			}
			player.getOutStream().writeWordBigEndianA(gameItem.id + 1);
		}
		player.getOutStream().endFrameVarSizeWord();
		player.flushOutStream();
	}

	public static void tick() {
		for (Shop shop : shops) {
			if (shop.needsUpdate) {
				shop.needsUpdate = false;
				update(shop.getId());
			}
		}
	}

	public static void update(int id) {
		for (Player player : PlayerHandler.players) {
			if (player == null) {
				continue;
			}
			if (player.isShopping && player.shopId == id) {
				Client client = (Client) player;
				sendShopInterface(client, client.getCurrentShop());
			}
		}
	}

}
