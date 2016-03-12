package com.ownxile.rs2.world.shops;

import com.ownxile.config.GameConfig;
import com.ownxile.config.WildConfig;
import com.ownxile.rs2.items.ItemAssistant;
import com.ownxile.rs2.player.Client;

public class Shopping {

	public static boolean buyItem(Client player, int itemId, int slot,
			int amount) {
		if (player.getItems().freeSlots() == 0) {
			player.sendMessage("You need some free inventory space to purchase items.");
			return false;
		}
		if (System.currentTimeMillis() - player.lastAction < 600)
			return false;
		if (player.shopId == 28 || player.shopId == 29 || player.shopId == 40
				|| player.shopId == 18 || player.shopId == 38
				|| player.shopId == 66 || player.shopId == 988) {
			handleSpecialShop(player, itemId, amount);
			return false;
		}
		ShopItem item = player.getCurrentShop().getShopItems().get(slot);
		if (player.getItems().freeSlots() < amount && !item.stackable)
			amount = player.getItems().freeSlots();
		if (player.getCurrentShop().hasItem(itemId, slot, amount)) {
			int cost = getCost(itemId, amount);

			if (item.amount < 1) {
				player.sendMessage("That item is currently is out of stock.");
				return false;
			}
			if (player.hasItem(995, cost)) {
				if (item.amount >= amount) {
					player.lastAction = System.currentTimeMillis();
					if (item.stackable) {
						updateShopItem(player, itemId, slot, amount);
						player.addItem(itemId, amount);
						player.deleteItem(995, cost);
						player.getItems().resetItems(3823);
						player.getCurrentShop().needsUpdate = true;
						return true;
					} else if (!item.stackable) {
						updateShopItem(player, itemId, slot, amount);
						player.addItem(itemId, amount);
						player.deleteItem(995, cost);
						player.getItems().resetItems(3823);
						player.getCurrentShop().needsUpdate = true;
						return true;
					}
				} else {
					player.sendMessage("There aren't enough of that item for you to buy.");
				}
			} else {
				player.sendMessage("You don't have enough money to purchase this item.");
			}
		}
		return false;
	}

	public static boolean canUseShop(Client player) {
		return player.playerRights < 2 && !player.inTrade;
	}

	public static int getCost(int itemId, int amount) {
		int value = ItemAssistant.getItemValue(itemId) * amount;
		return value;
	}

	public Shop getCurrentShop(Client player) {
		return ShopBuilder.getShop(player.shopId);
	}

	public static void handleSpecialShop(Client player, int itemID, int amount) {
		if (amount == 0) {
			return;
		}
		switch (player.shopId) {
		case 18:
			if (player.getPoints() >= GameConfig.getSpecialItemValue(itemID)
					* amount) {
				if (player.getItems().freeSlots() > amount) {
					player.points -= GameConfig.getSpecialItemValue(itemID)
							* amount;
					player.addItem(itemID, amount);
					player.getItems().resetItems(3823);
					player.getCurrentShop().needsUpdate = true;
				}
			} else {
				player.sendMessage("You do not have enough points to buy this item.");
			}
			break;
		case 40:
			if (player.donorPoints >= GameConfig.getDonorItemValue(itemID)
					* amount) {
				if (player.getItems().freeSlots() > amount) {
					player.donorPoints -= GameConfig.getDonorItemValue(itemID)
							* amount;
					player.addItem(itemID, amount);
					player.getItems().resetItems(3823);
					player.getItems().resetItems(3823);
					player.getCurrentShop().needsUpdate = true;
				}
			} else {
				player.sendMessage("You do not have enough money to buy this item.");
			}
			break;
		case 28:
			if (player.agilityPoints >= GameConfig.getSpecialItemValue(itemID)
					* amount) {
				if (player.getItems().freeSlots() > amount) {
					player.agilityPoints -= GameConfig
							.getSpecialItemValue(itemID) * amount;
					player.addItem(itemID, amount);
					player.getItems().resetItems(3823);
					player.getCurrentShop().needsUpdate = true;
				}
			} else {
				player.sendMessage("You do not have enough Agility points to buy this item.");
			}
			break;
		case 66:
			if (player.magePoints >= GameConfig.getSpecialItemValue(itemID)
					* amount) {
				if (player.getItems().freeSlots() > amount) {
					player.magePoints -= GameConfig.getSpecialItemValue(itemID)
							* amount;
					player.addItem(itemID, amount);
					player.getItems().resetItems(3823);
					player.getCurrentShop().needsUpdate = true;
				}
			} else {
				player.sendMessage("You do not have enough Mage arena points to buy this item.");
			}
			break;
		case 38:
			if (player.getItems().getItemCount(4067) >= GameConfig
					.getSpecialItemValue(itemID) * amount) {
				if (player.getItems().freeSlots() > amount) {
					player.getItems().deleteItem2(4067,
							GameConfig.getSpecialItemValue(itemID) * amount);
					player.addItem(itemID, amount);
					player.getItems().resetItems(3823);
					player.getCurrentShop().needsUpdate = true;
				}
			} else {
				player.sendMessage("You do not have enough Castle wars tickets to buy this item.");
			}
			break;
		case 988:
			if (player.wildRating >= WildConfig.getItemRatingCost(itemID)
					* amount) {
				if (player.getItems().freeSlots() > amount) {
					player.wildRating -= WildConfig.getItemRatingCost(itemID)
							* amount;
					player.addItem(itemID, amount);
					player.getItems().resetItems(3823);
					player.getCurrentShop().needsUpdate = true;
					player.sendMessage("@grd@You purchase "
							+ (amount > 1 ? amount : "a") + " "
							+ ItemAssistant.getItemName(itemID)
							+ ", lowering your rating by "
							+ WildConfig.getItemRatingCost(itemID) * amount
							+ ".");

					player.sendMessage("You now have a wilderness rating of @dre@"
							+ player.wildRating + ".");
				}
			} else {
				player.sendMessage("You have too low a rating to aquire this item.");
			}
			break;
		case 29:
			if (player.getItems().getItemCount(6529) >= GameConfig
					.getSpecialItemValue(itemID) * amount) {
				if (player.getItems().freeSlots() > amount) {
					player.getItems().deleteItem2(6529,
							GameConfig.getSpecialItemValue(itemID) * amount);
					player.addItem(itemID, amount);
					player.getItems().resetItems(3823);
					player.getCurrentShop().needsUpdate = true;
				}
			} else {
				player.sendMessage("You do not have enough Tokkul to buy this item.");
			}
			break;
		}
	}

	public static void openShop(Client player, int id) {
		Shop shop = ShopBuilder.getShop(id);
		player.setCurrentShop(shop);
		if (shop != null) {
			ShopBuilder.sendShopInterface(player, shop);
			player.getFunction().sendFrame248(3824, 3822);
			player.getFunction().sendFrame126(shop.getName(), 3901);
			player.isShopping = true;
			player.shopId = id;
		}
	}

	public static boolean sellItem(Client player, int itemID, int fromSlot,
			int amount) {
		if (!canUseShop(player))
			return false;
		if (!player.hasItem(itemID, amount))
			return false;
		Shop shop = player.getCurrentShop();
		if (shop.getId() == 18 || shop.getId() == 28 || shop.getId() == 40
				|| shop.getId() == 988) {
			player.sendMessage("You cannot sell item to this shop.");
			return false;
		}
		if (!player.getItems().tradeable(itemID)) {
			player.sendMessage("You cannot sell that item to this shop.");
			return false;
		}
		for (final int i : GameConfig.ITEM_SELLABLE) {
			if (i == itemID) {
				player.sendMessage("You can't sell "
						+ ItemAssistant.getItemName(itemID).toLowerCase() + ".");
				return false;
			}
		}
		if (!shop.hasItem2(itemID)) {
			player.sendMessage("You can't sell "
					+ ItemAssistant.getItemName(itemID).toLowerCase()
					+ " to this store.");
			return false;
		}
		if (amount > 0) {
			int has = player.getItems().getItemAmount(itemID);
			if (amount > has)
				amount = has;
			if (shop.hasItem2(itemID)) {
				for (ShopItem s : shop.getShopItems()) {
					if (s.id == itemID) {
						player.getItems().deleteItem2(itemID, amount);
						player.addItem(995,
								ShopBuilder.getItemSellPrice(itemID) * amount);
						s.amount += amount;
						player.getItems().resetItems(3823);
						player.getCurrentShop().needsUpdate = true;
						return true;
					}
				}
			} else {
				int slot = player.getCurrentShop().getFreeSlot();
				if (slot > -1) {
					player.getItems().deleteItem2(itemID, amount);
					player.addItem(995, ShopBuilder.getItemSellPrice(itemID)
							* amount);
					ShopItem g = new ShopItem(itemID, amount);
					g.shopSlot = slot;
					g.isTemporary = true;
					player.getCurrentShop().addItem(g);
					player.getItems().resetItems(3823);
					player.getCurrentShop().needsUpdate = true;
					return true;
				} else {
					player.sendMessage("The shop has run out of free space.");
				}
			}
		} else {
			player.sendMessage("You don't have enough of that item to sell.");
		}
		return false;
	}

	private static void updateShopItem(Client player, int itemId, int slot,
			int amount) {
		ShopItem s = player.getCurrentShop().getShopItems().get(slot);
		if (s.isTemporary && s.amount - amount < 1) {
			player.getCurrentShop().removeItem(s);
		} else {
			s.amount -= amount;
		}
	}

	public static void valueInventoryItem(Client player, int itemId,
			int removeSlot) {
		for (final int i : GameConfig.ITEM_SELLABLE) {
			if (i == itemId) {
				player.getItems();
				player.sendMessage("You can't sell "
						+ ItemAssistant.getItemName(itemId).toLowerCase()
						+ " to this shop.");
				return;
			}
		}
		Shop shop = player.getCurrentShop();

		if (!shop.hasItem2(itemId) && shop.sell == 2 || player.shopId == 28
				|| player.shopId == 40 || player.shopId == 18
				|| player.shopId == 988) {
			player.getItems();
			player.sendMessage("You can't sell "
					+ ItemAssistant.getItemName(itemId).toLowerCase()
					+ " to this store.");
		} else {
			int shopValue = ShopBuilder.getItemSellPrice(itemId);
			player.getItems();
			player.sendMessage(ItemAssistant.getItemName(itemId)
					+ ": shop will buy for " + shopValue + " coins");
		}
	}

	public static void valueShopItem(Client player, int itemId, int itemSlot) {
		switch (player.shopId) {
		case 18:
			player.sendMessage(ItemAssistant.getItemName(itemId)
					+ ": currently costs "
					+ GameConfig.getSpecialItemValue(itemId)
					+ " points, you have " + player.getPoints() + " points.");
			return;
		case 28:
			player.sendMessage(ItemAssistant.getItemName(itemId)
					+ ": currently costs "
					+ GameConfig.getSpecialItemValue(itemId)
					+ " Agility points, you have " + player.agilityPoints
					+ " points.");
			return;
		case 29:
			player.sendMessage(ItemAssistant.getItemName(itemId)
					+ ": currently costs "
					+ GameConfig.getSpecialItemValue(itemId) + " Tokkul.");
			return;
		case 38:
			player.sendMessage(ItemAssistant.getItemName(itemId)
					+ ": currently costs "
					+ GameConfig.getSpecialItemValue(itemId) + " tickets.");
			return;
		case 40:
			player.sendMessage(ItemAssistant.getItemName(itemId)
					+ ": currently costs $"
					+ GameConfig.getDonorItemValue(itemId) + "0, you have $"
					+ player.donorPoints + "0.");
			return;
		case 66:
			player.sendMessage(ItemAssistant.getItemName(itemId)
					+ ": currently costs "
					+ GameConfig.getDonorItemValue(itemId)
					+ " Magic arena points, you have " + player.magePoints
					+ " points.");
			return;
		case 988:
			player.sendMessage(ItemAssistant.getItemName(itemId)
					+ ": will cost a wilderness rating of "
					+ WildConfig.getItemRatingCost(itemId) + ".");
			player.sendMessage("You currently have a rating of @dre@"
					+ player.wildRating + "@bla@.");
			return;
		default:
			player.sendMessage(ItemAssistant.getItemName(itemId)
					+ ": currently costs "
					+ ShopBuilder.getItemBuyPrice(itemId) + " coins.");
			break;
		}
	}

}
