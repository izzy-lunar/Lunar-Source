package com.ownxile.rs2.content.item;

import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.world.transport.KaramjaCart;

public class Rope {

	public static void onObject(Client c, int objectId) {
		switch (objectId) {
		case 23609:
			KaramjaCart.travel(c, 3507, 9494);
			break;
		}

	}

}
