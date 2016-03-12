package com.ownxile.rs2.npcs;

import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.world.region.RegionManager;

public abstract class RandomEvent extends Task {

	private int index;

	public NPC getNpc() {
		return NPCHandler.npcs[index];
	}

	private Client c;
	protected int npcId;
	private int count;
	private boolean answered = true;

	public RandomEvent(Client c, int npcId) {
		super(3, false);
		int offsetX = 0;
		int offsetY = 0;
		this.npcId = npcId;
		if (RegionManager.getClipping(c.getX() - 1, c.getY(), c.absZ, -1, 0)) {
			offsetX = -1;
		} else if (RegionManager.getClipping(c.getX() + 1, c.getY(), c.absZ, 1,
				0)) {
			offsetX = 1;
		} else if (RegionManager.getClipping(c.getX(), c.getY() - 1, c.absZ, 0,
				-1)) {
			offsetY = -1;
		} else if (RegionManager.getClipping(c.getX(), c.getY() + 1, c.absZ, 0,
				1)) {
			offsetY = 1;
		}
		index = World.getNpcHandler().spawnNpc3(c, npcId, c.absX + offsetX,
				c.absY + offsetY, c.absZ, 0, 120, 25, 200, 200, true, false,
				true);
		getNpc().startAnimation(863);
	}

	public void answer() {
		getNpc().forceChat("Good bye " + c.playerName + "!");
		getNpc().startAnimation(863);
		World.getSynchronizedTaskScheduler().schedule(new Task(3, false) {
			protected void execute() {
				getNpc().absX = 0;
				getNpc().absY = 0;
			}
		});
	}

	protected void execute() {
		count++;
		System.out.println("wat");
		if (answered) {
			answer();
			return;
		}
		switch (count) {
		case 1:
			getNpc().startAnimation(863);
			getNpc().forceChat("I said hello " + c.playerName + "!");
			break;
		case 2:
			getNpc().forceChat("Excuse me " + c.playerName + "?");
			break;
		case 3:
			getNpc().forceChat("Are you there " + c.playerName + "?");
			break;
		case 4:
			getNpc().forceChat("Don't ignore me " + c.playerName + "!");
			break;
		case 5:
			stop();
			break;
		}
	}

	/**
	 * @param answered
	 *            the answered to set
	 */
	public void setAnswered(boolean answered) {
		this.answered = answered;
	}

	/**
	 * @return the answered
	 */
	public boolean isAnswered() {
		return answered;
	}

}
