package com.ownxile.rs2.skills.firemaking;

import com.ownxile.core.World;
import com.ownxile.rs2.Location;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.world.object.type.Object;

public class Firemaking {

	private static final int FIRE_CYCLE_TIMER = 200;
	private final Client player;

	public Firemaking(Client player) {
		this.player = player;
	}

	public void finishFire(final int xp, final int specTinderboxId) {
		player.getFunction().closeAllWindows();
		player.originalX = player.getX();
		player.originalY = player.getY();
		player.originalZ = player.getZ();
		final int x = player.getX();
		final int y = player.getY();
		final int z = player.getZ();
		if (player.originalX == player.getX()
				&& player.originalY == player.getY()
				&& player.originalZ == player.getZ()) {
			int objectId = 2732;
			if (specTinderboxId > 0) {
				objectId = getSpecialFire(specTinderboxId);
				player.deleteItem(specTinderboxId);
			}
			final Object fire = new Object(objectId, x, y, z, 0, 10, -1,
					FIRE_CYCLE_TIMER);
			World.getObjectManager().placeObject(fire);
			player.sendMessage("The logs manage to catch fire and begin to burn.");
			player.getFunction().addSkillXP(xp, player.playerFiremaking);
			player.isDoingSkill = false;
			player.canWalk = true;
			player.lastAction = System.currentTimeMillis();
			player.getFunction().clippedStep();
			player.turnPlayerTo(x, y);
		}

	}

	private int getSpecialFire(int lighterId) {
		if (lighterId == 7329) {
			return 11404;// red
		}
		if (lighterId == 7330) {
			return 11405;// green
		}
		if (lighterId == 7331) {
			return 11406;// blue
		}
		if (lighterId == 10327) {
			return 20000;// white
		}
		if (lighterId == 10326) {
			return 20001;// purple
		}
		return 0;
	}

	public void initialize(int itemUsed, int usedWith) {
		if (!player.getItems().playerHasItem(itemUsed)
				|| !player.getItems().playerHasItem(usedWith)) {
			return;
		}
		if (player.inFightPitsWait() || player.inMinigame()) {
			player.sendMessage("You can't light a fire here.");
			return;
		}
		if (player.isDoingSkill) {
			return;
		}
		if (Location.EDGEVILLE.isInLocation(player)) {
			return;
		}
		if (System.currentTimeMillis() - player.lastAction < 1500)
			return;
		for (final FiremakingData data : FiremakingData.values()) {
			if (itemUsed == data.logid && isTinderbox(usedWith)
					|| usedWith == data.logid && isTinderbox(itemUsed)) {
				if (player.playerLevel[player.playerFiremaking] >= data.level) {
					if (isTinderbox(usedWith)) {
						startFire(data.xp, data.logid, usedWith);
					} else {
						startFire(data.xp, data.logid, itemUsed);
					}
				} else {
					player.sendMessage("You need a firemaking level of "
							+ data.level + " to burn these logs.");
				}
			}
		}
	}

	public boolean isTinderbox(int itemId) {
		switch (itemId) {
		case 590:
		case 2946:
		case 7156:
		case 7329:
		case 7330:
		case 7331:
		case 10326:
		case 10327:
			return true;
		}
		return false;
	}

	public void startFire(int xp, int logid, int tinderboxId) {
		if (!player.isDoingSkill) {
			player.canWalk = false;
			player.isDoingSkill = true;
			// player.startAnimation(733);
			player.sendMessage("You attempt to light the logs...");
			player.getItems().deleteItem2(logid, 1);
			player.turnPlayerTo(player.getX(), player.getY() + 1);
			if (getSpecialFire(tinderboxId) > 0) {
				finishFire(xp, tinderboxId);
			} else {
				finishFire(xp, 0);
			}
		}
	}

}