package com.ownxile.rs2.skills.cooking;

import com.ownxile.config.SkillConfig;
import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.items.ItemAssistant;
import com.ownxile.rs2.player.Client;
import com.ownxile.util.Misc;

public class Cooking {

	public static void cookItem(final Client c, final int itemId,
			final int amount, final int objectId) {
		final CookingData item = forId(itemId);
		if (item != null) {

			c.originalX = c.getX();
			c.originalY = c.getY();
			c.originalZ = c.getZ();
			setCooking(c);
			c.getFunction().removeAllWindows();
			c.doAmount = amount;
			if (c.doAmount > c.getItems().getItemAmount(itemId)) {
				c.doAmount = c.getItems().getItemAmount(itemId);
			}
			if (objectId > 0) {
				c.startAnimation(objectId == 2732 ? 897 : 896);
			}
			World.getSynchronizedTaskScheduler().schedule(new Task(4) {
				@Override
				public void execute() {
					// if for whatever reason we are no longer cooking, stop
					// this event
					if (!c.playerIsCooking) {
						resetCooking(c);
						this.stop();
						return;
					}
					if (!c.getItems().playerHasItem(item.getRawItem(), 1)) {
						c.sendMessage("You have run out of " + item.getName()
								+ " to cook.");
						resetCooking(c);
						this.stop();
						return;
					}

					if (c.originalX != c.getX() || c.originalY != c.getY()
							|| c.originalZ != c.getZ()) {
						stop();
						resetCooking(c);
						c.startAnimation(65535);
						return;
					}
					boolean burn = getSuccess(c, 15, item.getLevelReq(),
							item.getStopBurn());
					c.getItems().deleteItem(item.getRawItem(),
							c.getItems().getItemSlot(itemId), 1);
					if (!burn) {
						c.sendMessage("You successfully cook the "
								+ item.getName().toLowerCase() + ".");
						c.getFunction().addSkillXP(
								item.getXp() * SkillConfig.COOKING_EXPERIENCE,
								7);
						c.getItems().addItem(item.getCookedItem(), 1);
					} else {
						c.sendMessage("Oops! You accidentally burnt the "
								+ item.getName().toLowerCase() + "!");
						c.getItems().addItem(item.getBurntItem(), 1);
					}
					c.doAmount--;
					if (c.doAmount > 0) {
						if (objectId > 0) {
							c.startAnimation(objectId == 2732 ? 897 : 896);
						}
					} else if (c.doAmount == 0) {
						resetCooking(c);
						this.stop();
					}
				}
			});
		}
	}

	public static CookingData forId(int itemId) {
		for (CookingData item : CookingData.values()) {
			if (itemId == item.getRawItem()) {
				return item;
			}
		}
		return null;
	}

	private static boolean getSuccess(Client c, int burnBonus, int levelReq,
			int stopBurn) {
		if (c.playerLevel[c.playerCooking] >= stopBurn) {
			return false;
		}
		return Misc.random(5) == 1;
	}

	private static void resetCooking(Client c) {
		c.playerIsCooking = false;
		c.stopPlayerSkill = false;
	}

	private static void setCooking(Client c) {
		c.playerIsCooking = true;
		c.stopPlayerSkill = true;
	}

	public static void startCooking(Client c, int itemId, int objectId) {
		CookingData item = forId(itemId);
		if (item != null) {
			if (c.playerLevel[c.playerCooking] < item.getLevelReq()) {
				c.getFunction().removeAllWindows();
				c.getFunction().state(
						"You need a Cooking level of " + item.getLevelReq()
								+ " to cook this.");
				return;
			}
			if (c.playerIsCooking) {
				c.getFunction().removeAllWindows();
				return;
			}
			// save the id of the item and object for the cooking interface.
			c.cookingItem = itemId;
			c.cookingObject = objectId;
			viewCookInterface(c, item.getRawItem());
		}
	}

	private static void viewCookInterface(Client c, int item) {
		c.getFunction().sendFrame164(1743);
		c.getFunction().sendFrame246(13716, 180, item);
		c.getFunction().sendFrame126(ItemAssistant.getItemName(item) + "",
				13717);
	}
}