package com.ownxile.rs2.skills.fletching;

import com.ownxile.config.SkillConfig;
import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.items.ItemAssistant;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.skills.crafting.StringingData;

public class Stringing {

	private static int BOW_STRING = 1777;

	public static void stringBow(final Client c, final int itemUsed,
			final int usedWith) {
		if (c.isDoingSkill) {
			return;
		}
		final int itemId = (itemUsed == BOW_STRING ? usedWith : itemUsed);
		final StringingData data = getData(itemId);
		if (data == null) {
			return;
		}
		if (itemId == data.unStrung()) {
			if (c.playerLevel[9] < data.getLevel()) {
				c.sendMessage("You need a fletching level of "
						+ data.getLevel() + " to string this bow.");
				return;
			}
			if (!c.getItems().playerHasItem(itemId)) {
				return;
			}
			c.isDoingSkill = true;
			c.startAnimation(data.getAnimation());
			World.getSynchronizedTaskScheduler().schedule(new Task(3) {
				@Override
				public void execute() {
					complete(c, itemId, data.Strung(), data.getXP());
					stop();
				}
			});
		}
	}

	private static StringingData getData(int itemId) {
		for (StringingData s : StringingData.values()) {
			if (itemId == s.unStrung())
				return s;
		}
		return null;
	}

	private static void complete(Client c, int itemId, int strung, double d) {
		c.getItems().deleteItem(itemId, 1);
		c.getItems().deleteItem(BOW_STRING, 1);
		c.getItems().addItem(strung, 1);
		c.getFunction().addSkillXP((int) d * SkillConfig.FLETCHING_EXPERIENCE,
				c.playerFletching);
		c.sendMessage("You attach the bow string to the "
				+ ItemAssistant.getItemName(itemId).toLowerCase() + ".");
		c.isDoingSkill = false;
	}
}
