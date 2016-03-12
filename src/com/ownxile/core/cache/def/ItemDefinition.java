package com.ownxile.core.cache.def;

public class ItemDefinition {

	public int[] bonus = new int[100];
	public double highAlch;
	public String itemDescription;
	public int itemId;
	public String itemName;
	public double lowAlch;
	public double shopValue;

	public ItemDefinition(int itemId) {
		this.itemId = itemId;
	}
}
