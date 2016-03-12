package com.ownxile.rs2.skills.crafting;

import com.ownxile.config.SkillConfig;
import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.items.ItemAssistant;
import com.ownxile.rs2.player.Client;

public class Spinning {

	private enum Spinnable {
		FLAX(1779, 1777), WOOL(1737, 1759), SINEW(9436, 9438);

		private int originalItem, newItem;

		private Spinnable(int originalItem, int newItem) {
			this.originalItem = originalItem;
			this.newItem = newItem;
		}

		public int getOriginalItem() {
			return originalItem;
		}

		public int getNewItem() {
			return newItem;
		}
	}

	public static boolean isSpinnable(int item) {
		for (Spinnable i : Spinnable.values()) {
			if (item == i.getOriginalItem())
				return true;
		}
		return false;
	}

	public static Spinnable getSpinnableForButton(int actionButton) {
		switch (actionButton) {
		case 34185:
			return Spinnable.WOOL;
		case 34189:
			return Spinnable.FLAX;
		case 34193:
			return Spinnable.SINEW;
		}
		return null;

	}

	public static void startSpinning(final Client c, final int actionButton) {
		final Spinnable spinnable = getSpinnableForButton(actionButton);
		if (spinnable == null)
			return;
		if (c.isDoingSkill)
			return;
		c.originalX = c.absX;
		c.originalY = c.absY;
		c.isDoingSkill = true;
		if (c.getItems().playerHasItem(spinnable.getOriginalItem())) {
			c.getFunction().closeAllWindows();
			World.getSynchronizedTaskScheduler().schedule(new Task(2, true) {
				boolean wat;

				public void execute() {
					if (c.originalX != c.absX || c.originalY != c.absY) {
						stop();
						c.isDoingSkill = false;
					}
					wat = !wat;
					if (wat)
						c.startAnimation(894);
					else if (!craftSpinnable(c, spinnable)) {
						stop();
						c.isDoingSkill = false;
					}
				}
			});
		} else {
			c.boxMessage("You need "
					+ ItemAssistant.getItemName(spinnable.getOriginalItem())
					+ " to make a "
					+ ItemAssistant.getItemName(spinnable.getNewItem()) + ".");
		}
	}

	private static boolean craftSpinnable(Client c, Spinnable spinnable) {
		if (c.getItems().playerHasItem(spinnable.getOriginalItem())) {
			c.deleteItem(spinnable.getOriginalItem());
			c.addItem(spinnable.getNewItem());
			c.sendMessage("You craft the "
					+ ItemAssistant.getItemName(spinnable.getOriginalItem())
					+ " into a "
					+ ItemAssistant.getItemName(spinnable.getNewItem()) + ".");
			c.getFunction()
					.addSkillXP(23 * SkillConfig.CRAFTING_EXPERIENCE, 12);
			return true;
		}
		return false;
	}

	public static void spinInterface(Client c) {
		c.craftingLeather = false;
		c.getFletching().fletching = false;
		c.getFunction().sendFrame164(8880);
		c.getFunction().sendFrame126("What would you like to make?", 8879);
		c.getFunction().sendFrame246(8883, 200, Spinnable.WOOL.getNewItem());
		c.getFunction().sendFrame246(8884, 200, Spinnable.FLAX.getNewItem());
		c.getFunction().sendFrame246(8885, 200, Spinnable.SINEW.getNewItem());
		c.getFunction().sendFrame126("Ball of Wool", 8889);
		c.getFunction().sendFrame126("Bowstring", 8893);
		c.getFunction().sendFrame126("Crossbow string", 8897);
	}

}
