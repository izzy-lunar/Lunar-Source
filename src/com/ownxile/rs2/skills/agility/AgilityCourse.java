package com.ownxile.rs2.skills.agility;

import com.ownxile.config.SkillConfig;
import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.Zone;
import com.ownxile.rs2.player.Client;

public abstract class AgilityCourse {

	protected Zone zone = null;

	public AgilityCourse() {
		Agility.agilityCourses.add(this);
		setZone();
	}

	/**
	 * @return the zone
	 */
	public Zone getZone() {
		return zone;
	}

	public abstract void handleObjects(Client player);

	public boolean inCourse(Client player) {
		if (zone != null) {
			return zone.inZone(player);
		}
		return false;
	}

	/**
	 * @param zone
	 *            the zone to set
	 */
	public abstract void setZone();

	public void startMovement(final Client player, final int x, final int y,
			int animation, int xp, String startMessage, final String endMessage) {
		if (System.currentTimeMillis() - player.lastAgility < 1500) {
			return;
		}
		player.isDoingSkill = true;
		player.lastAgility = System.currentTimeMillis();
		player.startAnimation(animation);
		player.sendMessage(startMessage);
		player.turnPlayerTo(x, y);
		xp += player.playerEquipment[player.playerHands] == 1495 ? xp / 10 : 1;
		final int exp = xp;
		World.getSynchronizedTaskScheduler().schedule(new Task(1, false) {
			@Override
			protected void execute() {
				player.sendMessage(endMessage);
				player.isDoingSkill = false;
				player.getFunction().movePlayer(x, y, player.absZ);
				if (endMessage.contains("finish the course")
						&& player.absY > 9896) {
					player.agilityPoints += player.playerEquipment[player.playerHands] == 1495 ? 25
							: 20;
					player.sendMessage("You now have " + player.agilityPoints
							+ " agility points.");
				}
				player.getFunction().addSkillXP(
						exp * SkillConfig.AGILITY_EXPERIENCE,
						player.playerAgility);
				stop();
			}
		});
	}
}
