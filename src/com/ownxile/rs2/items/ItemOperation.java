package com.ownxile.rs2.items;

import com.ownxile.core.Plugin;
import com.ownxile.rs2.player.Client;

public class ItemOperation {

	public static void operateItem(Client player, int itemId) {
		switch (itemId) {
		case 11283:
		case 11284:
			player.getFunction().handleDfs();
			break;
		case 6731:
			player.getCombat().handleSeers();
			break;
		case 6733:
			player.getCombat().handleArcher();
			break;
		case 4566:
			player.startAnimation(1835);
			return;
		default:
			if (!Plugin.execute("third_click_item_" + itemId, player))
				player.sendMessage("Nothing interesting happens.");
			break;

		}
	}

}
