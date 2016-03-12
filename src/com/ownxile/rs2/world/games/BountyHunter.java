package com.ownxile.rs2.world.games;

import java.util.ArrayList;

import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.Player;
import com.ownxile.rs2.player.PlayerHandler;

public class BountyHunter {

	public final int MCD = 10;

	public int totalTargets = 0;

	public int[] playersInWild = new int[200];
/*
	public void checkAfkTarget(Client c) {
		if (c.playerTarget => 0) {
			Player target = PlayerHandler.players[c.playerTarget];
			if (c.targetTicks < 4) {
				if (!c.playerTarget.inWild() || c.playerTarget.inSafeZone()) {
					c.targetTicks++;
					c.sendMessage("[TARGET]:  <shad=65280>If your target doesn't enter the wilderness soon, you will get a new target");
				}
			} else {
				c.targetTicks = 0;
				c.playerTarget = null;
				c.sendMessage("[TARGET]:  <shad=65280>You no longer have a target.");
				c.getPA().createPlayerHints(10, -1);
			}
		}
	}

	public void process() {
		CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {
			public void execute(CycleEventContainer c) {
				System.out.println("Processed Wilderness targets system.");
				doProcess();
			}

			public void stop() {
			}
		}, 16);
	}

	public void sendTargets() {
		if (getAmountInWild() <= 1) {
			return;
		}
		for (int i = 0; i < inWilds.length; i++) {
			if (inWilds[i] != null && inWilds[i + 1] != null) {
				Client newTarget = inWilds[Misc.random(totalTargets)];
				if (newTarget != null) {
					if (canBeTargets(newTarget, inWilds[i])) {
						if (inWilds[i].playerTarget == null
								&& newTarget.playerTarget == null) {
							applyTargets(inWilds[i], newTarget);
							reset(i);
						}
					}
				}
			}
		}
		reset();
	}

	public void applyTargets(Client c, Client o) {
		if (c == o) {
			return;
		}
		c.sendMessage("[TARGET]:  <shad=ff0000>You've been assigned a target to kill : <shad=65535>"
				+ o.playerName);
		o.sendMessage("[TARGET]:  <shad=ff0000>You've been assigned a target to kill : <shad=65535>"
				+ c.playerName);
		c.playerTarget = o;
		o.playerTarget = c;
		c.getPA().createPlayerHints(10, o.playerId);
		o.getPA().createPlayerHints(10, c.playerId);
	}

	public void doProcess() {
		for (int i = 0; i < Server.playerHandler.players.length; i++) {
			if (Server.playerHandler.players[i] != null) {
				checkAfkTarget((Client) Server.playerHandler.players[i]);
				if (Server.playerHandler.players[i].inWild()) {
					isInWild((Client) Server.playerHandler.players[i]);
				}
			}
		}
		sendTargets();
	}

	public void isInWild(Client c) {
		if (c.playerTarget == null
				&& System.currentTimeMillis() - c.lastTarget >= 59000) {
			inWilds[getSlot()] = c;
			totalTargets++;
		}
	}

	public int getSlot() {
		for (int i = 0; i < inWilds.length; i++) {
			if (inWilds[i] == null) {
				return i;
			}
		}
		return 0;
	}

	public int getAmountInWild() {
		int amount = 0;
		for (int i = 0; i < inWilds.length; i++) {
			if (inWilds[i] != null) {
				amount++;
			}
		}
		return amount;
	}

	public void reset() {
		for (int i = 0; i < inWilds.length; i++) {
			inWilds[i] = null;
		}
		totalTargets = 0;
	}

	public void reset(int j) {
		inWilds[j] = null;
		totalTargets--;
	}

	public static void handleKilled(Client killer, Client victim) {
		if (killer.playerTarget == victim) {
			// killer.sendMessage("[TARGET]:  <shad=65280>You killed your target and you receive 1 bounty point.");
			// killer.bountyPoints ++;
			killer.sendMessage("[TARGET]:  <shad=65280>You killed your target and you receive 2 extra pk points.");
			killer.pkPoints += 2;
			killer.playerTarget = null;
			victim.playerTarget = null;
			killer.lastTarget = System.currentTimeMillis();
			victim.lastTarget = System.currentTimeMillis();
			killer.sendMessage("[TARGET]:  <shad=65280>You will be able to get a new target in 1 minute");
			victim.sendMessage("[TARGET]:  <shad=65280>You will be able to get a new target in 1 minute");
			victim.getPA().createPlayerHints(10, -1);
			killer.getPA().createPlayerHints(10, -1);
		}
	}

	public boolean canBeTargets(Client c, Client o) {
		int ccb = c.combatLevel;
		int ocb = o.combatLevel;
		if (ccb - ocb <= MCD || ocb - ccb <= MCD) {
			return true;
		}
		return false;
	}
*/

}
