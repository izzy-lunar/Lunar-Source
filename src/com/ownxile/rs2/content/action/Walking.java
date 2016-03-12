package com.ownxile.rs2.content.action;

import com.ownxile.rs2.player.Player;

public class Walking {

	public static boolean canWalk(Player c) {
		if (c.orb) {
			return false;
		}
		if (c.isDead) {
			return false;
		}
		if (!c.canWalk) {
			return false;
		}
		if (c.isTeleporting) {
			return false;
		}
		return true;
	}

}
