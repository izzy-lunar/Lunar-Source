package com.ownxile.rs2.items;

import com.ownxile.config.ItemConfig;

public class GameItem {

	public int id, amount;
	public boolean stackable = false;

	public GameItem(int id) {
		if (ItemConfig.itemStackable[id]) {
			stackable = true;
		}
		this.id = id;
		this.amount = 1;
	}

	public GameItem(int id, int amount) {
		if (ItemConfig.itemStackable[id]) {
			stackable = true;
		}
		this.id = id;
		this.amount = amount;
	}
}