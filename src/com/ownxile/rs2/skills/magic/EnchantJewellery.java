package com.ownxile.rs2.skills.magic;

import com.ownxile.rs2.player.Client;

public class EnchantJewellery {

	private static final int[][] DATA = { { 8016, 1637, 2550 },
			{ 8016, 1656, 5853 }, { 8016, 1694, 1727 }, { 8017, 1639, 2552 },
			{ 8017, 1658, 5521 }, { 8017, 1696, 1729 }, { 8018, 1641, 2568 },
			{ 8018, 1660, 11195 }, { 8018, 1698, 1725 }, { 8019, 1643, 2570 },
			{ 8019, 1662, 11090 }, { 8019, 1700, 1725 }, { 8021, 1645, 2572 },
			{ 8021, 1664, 11113 }, { 8021, 1702, 1712 }, { 8021, 6575, 6583 },
			{ 8021, 6577, 11128 }, { 8021, 6581, 6585 } };

	private static void enchant(Client c, int tab, int delete, int add) {
		if (c.getItems().playerHasItem(delete, 1)) {
			c.startAnimation(712);
			c.gfx100(114);
			c.getItems().deleteItem2(delete, 1);
			c.getItems().deleteItem2(tab, 1);
			c.getItems().addItem(add, 1);
		}
	}

	public static void handleItem(Client c, int item) {
		for (int[] element : DATA) {
			if (item == element[0]) {
				enchant(c, element[0], element[1], element[2]);
			}
		}
	}
}
