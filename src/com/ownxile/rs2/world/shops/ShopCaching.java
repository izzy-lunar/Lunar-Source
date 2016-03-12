package com.ownxile.rs2.world.shops;

import java.util.ArrayList;

/**
 * Creates a cached array of @Shop's
 */
public class ShopCaching {

	/**
	 * The @Shop cache
	 */
	public static ArrayList<Shop> cachedShops = null;

	/**
	 * @return whether cache exists
	 */
	public static boolean isCached() {
		return cachedShops != null;
	}

	public static void purgeCache() {
		cachedShops = null;
	}

}
