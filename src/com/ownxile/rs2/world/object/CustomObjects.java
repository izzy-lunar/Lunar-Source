package com.ownxile.rs2.world.object;

import java.util.LinkedList;
import java.util.List;

import com.ownxile.rs2.Point;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.world.object.type.GameObject;
import com.ownxile.rs2.world.region.RegionManager;

public class CustomObjects {

	private static List<GameObject> customObjects = new LinkedList<GameObject>();

	public static void add(int id, int x, int y, int face, int type) {
		customObjects.add(new GameObject(new Point(x, y, 0), id, type, face));
		RegionManager.addObject(id, x, y, 0, type, face);
	}

	public static void loadCustomObjects() {
		/*
		 * home coins
		 */
		//add(24875, 3084, 3487, 0, 10);
		
		
		add(11730, 1789, 3787, 0, 10);
		add(11732, 1786, 3787, 0, 10);
		add(11729, 1783, 3787, 0, 10);
		add(11733, 1780, 3787, 0, 10);
		add(11731, 1767, 3787, 0, 10);
		
		/*
		 *rabbit hole
		 */
	//	add(23117, 3103, 3492, 0, 10);
		/*
		 * crystal key chest in bank
		 */
		add(172, 3094, 3499, 0, 10);

		/*
		 * wilderness leaderboard
		 */
		//add(3192, 3094, 3515, 3, 10);
		/*
		 * wilderness portal
		 */

		//add(27254, 3092, 3505, 0, 10);

		/*
		 * north home prayer alter
		 */
		//add(13184, 3096, 3506, 0, 10);

		/*
		 * elf camp bank
		 */
		add(2213, 2192, 3248, 2, 10);

		/*
		 * home fairy ring 14097
		 */

		add(1317, 3103, 3516, 0, 10);
		/*
		 * rock crab dungeon entrance
		 */
		add(10321, 2686, 3713, 0, 10);

		/*
		 * christmas tree
		 */

		// add(10660, 3082, 3499, 0, 10);
		/*
		 * teleport lever
		 */

		add(1814, 3090, 3478, 0, 5);
		/*
		 * Home stall
		 */
		//add(2563, 3078, 3483, 3, 10);
		/*
		 * Home ladder
		 */
		// add(8744, 3097, 3478, 3, 10);

		/*
		 * Home prayer alter
		 */
		add(13184, 3086, 3483, 2, 10);
		/*
		 * edge wildy dungeon
		 */
		add(16154, 3112, 3525, 0, 10);

		/*
		 * dwarf remains
		 */
		add(15596, 2568, 3442, 0, 10);

		/*
		 * noob dungeon food stall
		 */
		add(4875, 2779, 10078, 0, 10);

		/*
		 * Delete doors at neitiznot
		 */
		customObjects.add(new GameObject(new Point(2339, 3801, 0), -1, 1, 0));
		customObjects.add(new GameObject(new Point(2328, 3804, 0), -1, 1, 0));
		customObjects.add(new GameObject(new Point(2328, 3805, 0), -1, 1, 0));
		customObjects.add(new GameObject(new Point(2345, 3807, 0), -1, 1, 0));
		customObjects.add(new GameObject(new Point(2347, 3801, 0), -1, 1, 0));
		customObjects.add(new GameObject(new Point(2352, 3801, 0), -1, 1, 0));

	}

	public static void showCustomObjects(Client client) {
		if (client.absZ == 0) {
			for (GameObject gameObject : customObjects) {
				spawnObject(client, gameObject);
			}
		}
	}

	private static void spawnObject(Client client, GameObject gameObject) {
		client.getFunction().addObject(gameObject.id(), gameObject.x(),
				gameObject.y(), gameObject.z(), gameObject.getFace(),
				gameObject.type());
	}
}
