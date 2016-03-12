package com.ownxile.rs2.items;

public class GroundItem {

	public int hideTicks;
	public int itemAmount;
	public int itemController;
	public int itemHeight;
	public int itemId;
	public int itemX;
	public int itemY;
	public String ownerName;
	public int removeTicks;

	public GroundItem(int id, int x, int y, int height, int amount,
			int controller, int hideTicks, String name) {
		itemId = id;
		itemX = x;
		itemY = y;
		itemHeight = height;
		itemAmount = amount;
		itemController = controller;
		this.hideTicks = hideTicks;
		ownerName = name;
	}

	public int getItemAmount() {
		return itemAmount;
	}

	public int getItemController() {
		return itemController;
	}

	public int getItemHeight() {
		return itemHeight;
	}

	public int getItemId() {
		return itemId;
	}

	public int getItemX() {
		return itemX;
	}

	public int getItemY() {
		return itemY;
	}

	public String getName() {
		return ownerName;
	}

}