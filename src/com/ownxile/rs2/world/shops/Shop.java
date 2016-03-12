package com.ownxile.rs2.world.shops;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a shop containing shop items
 */
public class Shop {

	public int buy, sell = 2;

	private int id;

	private String name;

	public boolean fixedStock = false;

	public boolean needsUpdate = false;

	private List<ShopItem> shopItems = new ArrayList<ShopItem>();

	public Shop() {
		// leave this
	}

	public int getFreeSlot() {
		if (shopItems.size() > 39) {
			return -1;
		}
		return shopItems.size();
	}

	public Shop(String name, int id) {
		this.id = id;
		this.name = name;
		ShopBuilder.shops.add(this);
	}

	public void addItem(ShopItem gameItem) {
		if (shopItems.size() > 39) {
			return;
		}
		gameItem.shopSlot = getFreeSlot();
		shopItems.add(gameItem);
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<ShopItem> getShopItems() {
		return shopItems;
	}

	public boolean hasItem(int itemId, int slot, int amount) {
		for (ShopItem g : shopItems) {
			if (g.id == itemId && slot == g.getSlot() && amount <= g.amount) {
				return true;
			}
		}
		return false;
	}

	public boolean hasItem2(int itemId) {
		for (ShopItem g : shopItems) {
			if (g.id == itemId) {
				return true;
			}
		}
		return false;
	}

	public void out() {
		System.out.println(getId() + ": " + getName());
	}

	public void removeItem(ShopItem shopItem) {
		shopItems.remove(shopItem);
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

}
