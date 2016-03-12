package com.ownxile.rs2.skills.slayer;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.ownxile.config.SkillConfig;
import com.ownxile.rs2.content.item.Casket;
import com.ownxile.rs2.player.Client;
import com.ownxile.util.Misc;

public class Slayer {

	private static Random r = new Random();

	private Client c;

	private SlayerTask currentTask;

	public Slayer(Client player) {
		this.c = player;
	}

	public String assignTask(int difficulty) {
		if (currentTask != null) {
			return "You already have a task! You need to kill "
					+ c.slayerAmount + " more "
					+ currentTask.toFormattedString() + "s.";
		}
		handleTaskAssignment(difficulty);
		String s = "You have been assigned to kill @dre@" + c.slayerAmount
				+ " " + currentTask.toFormattedString() + "s@bla@.";
		c.sendMessage(s);
		return s;
	}

	public boolean canAttackSlayerNpc(int npcId) {
		int levelRequired = SlayerMonsterRequirement.monsterRequirement(npcId);
		if (c.playerLevel[18] < levelRequired) {
			c.sendMessage("You need a slayer level of " + levelRequired
					+ " to harm this NPC.");
			return false;
		}
		return true;
	}

	/**
	 * Checks weather our player can do a task if it is given to us
	 * 
	 * @param task
	 *            to check
	 * @return
	 */
	public boolean canDoTask(SlayerTask t) {
		return c.playerLevel[c.playerSlayer] >= t.req;
	}

	public void decreaseKillAmount(int combatLevel) {
		if (c.slayerAmount > 0) {
			c.slayerAmount--;
			int xp = 20 + (currentTask.difficulty + 1) * combatLevel * 2;
			c.getFunction().addSkillXP(xp * SkillConfig.SLAYER_EXPERIENCE,
					c.playerSlayer);
		}
		if (c.slayerAmount < 1) {
			c.addPoints(10 + (5 * currentTask.difficulty));
			c.sendMessage("You completed your slayer task, see the slayer master to get a new one.");
			if (1 == Misc.random(3)) {
				c.addItem(Casket.getCasketReward(currentTask.getDifficulty()),
						1);
				c.sendMessage("@dre@Congratulations, you have received a casket for your endeavours in slayer.");
			}
			resetTask();
		}
	}

	public SlayerTask findRandomTask(int difficulty) {
		List<SlayerTask> possibleTasks = new LinkedList<SlayerTask>();
		for (SlayerTask t : SlayerTask.values()) {
			if (t.getDifficulty() == difficulty && canDoTask(t)) {
				possibleTasks.add(t);
			}
		}
		if (possibleTasks.isEmpty()) {
			return SlayerTask.values()[r.nextInt(SlayerTask.values().length)];
		} else {
			return possibleTasks.get(r.nextInt(possibleTasks.size()));
		}
	}

	public SlayerTask getTask() {
		return currentTask;
	}

	private void handleTaskAssignment(int difficulty) {
		while (currentTask == null) {
			currentTask = findRandomTask(difficulty);
		}
		if (c.lastClickedNpcId == 70 || c.lastClickedNpcId == 5939)
			c.slayerAmount = 10 + Misc.random(15);
		else
			c.slayerAmount = 30 + Misc.random(30);
		c.slayerNpc = currentTask.name();
	}

	public boolean isSlayerTask(int npcId) {
		if (playerHasTask()) {
			if (currentTask.isTask(npcId))
				return true;
		}
		return false;
	}

	public void loadTask() {
		for (SlayerTask t : SlayerTask.values()) {
			if (t.name().equals(c.slayerNpc)) {
				setTask(t, c.slayerAmount);
			}
		}
	}

	public boolean playerHasTask() {
		return currentTask != null;
	}

	public void resetTask() {
		currentTask = null;

		c.slayerAmount = -1;
		c.slayerNpc = "";
	}

	public String resetTask(int difficulty) {
		currentTask = findRandomTask(difficulty);// important
		handleTaskAssignment(difficulty);
		return "You have been assigned to kill " + c.slayerAmount + " "
				+ currentTask.toFormattedString() + "s.";
	}

	public void sendCurrentTask() {
		c.boxMessage("Your current task is to kill " + toString() + ".");
	}

	public void setTask(SlayerTask t, int amount) {
		currentTask = t;
		c.slayerNpc = currentTask.name();
		c.slayerAmount = amount;
	}

	public void setTask(String npc, int amount) {
		currentTask = SlayerTask.valueOf(npc);
		c.slayerNpc = currentTask.name();
		c.slayerAmount = amount;
	}

	@Override
	public String toString() {
		if (currentTask != null) {
			if (c.slayerAmount > 1) {
				return c.slayerAmount + " " + currentTask.toFormattedString()
						+ "s";
			}
			return c.slayerAmount + " " + currentTask.toFormattedString();
		}
		return "0 nothings";
	}
}