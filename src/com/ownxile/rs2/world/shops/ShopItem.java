package com.ownxile.rs2.world.shops;

import com.ownxile.rs2.items.GameItem;

public class ShopItem extends GameItem {

	public boolean isTemporary = false;
	private int originalAmount;

	public ShopItem(int id, int amount) {
		super(id, amount);
		originalAmount = amount;
	}

	public int getOriginalAmount() {
		return originalAmount;
	}

	public int shopSlot;

	public int getSlot() {
		return shopSlot;
	}

}
