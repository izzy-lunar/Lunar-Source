package com.ownxile.rs2.content.action;

import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.player.Client;
import com.ownxile.util.Misc;

public class DiceRolling {

	// static final int ANIMATION = 11900, GFX = 2075;

	public static void initiateDiceRoll(Client player) {
		if (player.clanId < 0) {
			rollDice(player, false);
		} else {
			rollDice(player, true);
		}
	}

	public static void rollDice(final Client player, final boolean clan) {
		if (System.currentTimeMillis() - player.lastRoll >= 2000) {
			// player.startAnimation(ANIMATION);
			// player.turnPlayerTo(1, 1);
			// player.gfx100(GFX);
			player.sendMessage("You roll the dice...");
			player.lastRoll = System.currentTimeMillis();
			World.getSynchronizedTaskScheduler().schedule(new Task(3, false) {
				@Override
				protected void execute() {
					int chance = Misc.random(100);
					if (clan) {
						World.getClanChat()
								.clanMessage(
										"Chat channel-mate @dre@"
												+ Misc.formatPlayerName(player.playerName)
												+ "@bla@ rolled @dre@"
												+ chance
												+ "@bla@ on the percentile dice.",
										player.clanId);
					} else {
						player.sendMessage("You rolled @dre@" + chance
								+ "@bla@ on the percentile dice.");
					}
					stop();
				}
			});
		}
	}
}
