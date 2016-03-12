package com.ownxile.rs2.content;

import com.ownxile.core.World;
import com.ownxile.rs2.npcs.NPC;
import com.ownxile.rs2.player.Client;

public class EvilChicken {

	private static int[][] chicken = { { 3, 10, 2463 }, { 11, 20, 2464 },
			{ 21, 40, 2465 }, { 41, 60, 2466 }, { 61, 90, 2467 },
			{ 91, 138, 2468 } };

	public static void spawnChicken(Client c) {
		NPC npc = null;
		for (int[] element : EvilChicken.chicken) {
			if (c.combatLevel >= element[0] && c.combatLevel <= element[1]) {
				npc = World.getNpcHandler().spawnNpc(c, element[2], c.absX,
						c.absY, c.absZ, 0, element[1], 4, element[1],
						element[1], true, false);
				break;
			}
		}
	}
}