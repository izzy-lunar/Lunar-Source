package com.ownxile.rs2.skills.agility;

import java.util.ArrayList;
import java.util.List;

import com.ownxile.rs2.player.Client;

public class Agility {

	public static List<AgilityCourse> agilityCourses = new ArrayList<AgilityCourse>();

	public static boolean checkObstacle(Client player) {
		for (AgilityCourse agilityCourse : agilityCourses) {
			if (agilityCourse.inCourse(player)
					&& player.withinDistance(player.objectX, player.objectY, 4)) {
				agilityCourse.handleObjects(player);
				return true;
			}
		}
		return false;
	}

	public static void createCourses() {
		new WerewolfCourse();
		new WildernessCourse();
	}
}
