package com.ownxile.core.task.impl;

import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.npcs.NPC;
import com.ownxile.util.Misc;

public class PenanceBoss extends Task {

	public PenanceBoss() {
		super(6000);
	}

	protected void execute() {
		int randomIndex = Misc.random(zone.length - 1);
		penanceQueen.teleportNpc(xPos[randomIndex], yPos[randomIndex], 0);
		World.sendMessage("@dre@The Penance queen has spawned "
				+ zone[randomIndex] + "!");
	}

	public static NPC penanceQueen = World.getNpcHandler().spawnNormalNpc(5247,
			100, 100, 0, 0, 600, 99, 500, 500);

	private final int[] xPos = { 3251, 3169, 3050, 3048, 3235, 2951 };
	private final int[] yPos = { 3880, 3640, 3745, 3803, 3924, 3931 };

	private final String[] zone = { "west of the Demonic Ruins",
			"south of the Graveyard of Shadows", "north of the Bandit Camp",
			"south-west of the Lava Maze", "south of the Scorpion Pit",
			"on the Frozen Plateau" };

	public static NPC getNpc() {
		return penanceQueen;
	}

	public static void setNpc(NPC npc) {
		penanceQueen = npc;
	}
}
