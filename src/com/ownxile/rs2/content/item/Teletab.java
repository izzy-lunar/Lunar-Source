package com.ownxile.rs2.content.item;

import java.util.HashMap;

import com.ownxile.config.GameConfig;
import com.ownxile.config.WildConfig;
import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.player.Client;

public class Teletab {

	enum TeletabData {

		ARDOUGNE(8011, 2661, 3307, 0), CAMELOT(8010, 2726, 3492, 0), FALADOR(
				8009, 2945, 3371, 0), HOUSE(8013, 3087, 3504, 0), LUMBRIDGE(
				8008, 3222, 3218, 0), VARROCK(8007, 3210, 3424, 0), WATCHTOWER(
				8012, 2770, 10070, 0);

		public static HashMap<Integer, TeletabData> tabs = new HashMap<Integer, TeletabData>();

		static {
			for (final TeletabData i : TeletabData.values()) {
				TeletabData.tabs.put(i.itemId, i);
			}
		}

		public int itemId, posX, posY, height;

		private TeletabData(int itemId, int posX, int posY, int height) {
			this.itemId = itemId;
			this.posX = posX;
			this.posY = posY;
			this.height = height;
		}
	}

	final static int START_ANIM = 4731, GRAPHIC = 678;

	private static void breakTab(final Client player, final TeletabData teletab) {
		if (player.isTeleporting) {
			return;
		}
		if(player.playerLevel[3] < 1)
			return;
		if (!player.getFunction().canTeleport(false)) {
			return;
		}
		if (player.wildLevel > GameConfig.NO_TELEPORT_WILD_LEVEL) {
			player.sendMessage("You can't teleport above level "
					+ GameConfig.NO_TELEPORT_WILD_LEVEL + " in the wilderness.");
			return;
		}
		player.isTeleporting = true;
		player.stopMovement();
		player.getItems().deleteItem2(teletab.itemId, 1);
		player.getTask().closeAllWindows();
		player.startAnimation(4069);

		if (player.inWild()
				&& System.currentTimeMillis() - player.logoutDelay < 4000) {
			player.sendMessage("@red@You suffer a wilderness rating penalty for teleporting in combat.");
			int remove = WildConfig.getRatingToRemove(player.wildRating);
			player.decreaseRating(remove);
		}
		World.getSynchronizedTaskScheduler().schedule(new Task(1, false) {
			@Override
			protected void execute() {
				player.startAnimation(START_ANIM);
				player.gfx0(GRAPHIC);
				stop();

			}
		});
		World.getSynchronizedTaskScheduler().schedule(new Task(5, false) {
			@Override
			protected void execute() {
				player.getFunction().movePlayer(teletab.posX, teletab.posY,
						teletab.height);
				stop();

			}
		});
		World.getSynchronizedTaskScheduler().schedule(new Task(6, false) {
			@Override
			protected void execute() {
				player.getFunction().frame1();
				player.isTeleporting = false;
				stop();

			}
		});
	}

	public static void handleItem(Client c, int itemId) {
		for (final TeletabData i : TeletabData.values()) {
			if (itemId == i.itemId) {
				breakTab(c, i);
			}
		}
	}

}
