package com.ownxile.rs2.items;

import java.util.ArrayList;

import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.Point;

public class DefaultGroundItem {

	private static ArrayList<DefaultGroundItem> defaultGroundItems = new ArrayList<DefaultGroundItem>();

	public static void init() {
		World.getSynchronizedTaskScheduler().schedule(new Task(200, true) {
			@Override
			protected void execute() {
				for (DefaultGroundItem item : defaultGroundItems) {
					if (!GroundItemHandler.groundItemExists(item.getItemId(),
							item.getX(), item.getY(), item.getZ(),
							item.getItemAmount())) {
						GroundItemHandler.items.add(new GroundItem(item
								.getItemId(), item.getX(), item.getY(), item
								.getZ(), item.getItemAmount(), -1, 25000, null));
					}
				}
			}
		});
	}

	private GameItem gameItem;

	private Point point;

	public DefaultGroundItem(int itemId, int itemAmount, int x, int y, int z) {
		this.point = new Point(x, y, z);
		this.gameItem = new GameItem(itemId, itemAmount);
		defaultGroundItems.add(this);
	}

	public int getItemAmount() {
		return gameItem.amount;
	}

	public int getItemId() {
		return gameItem.id;
	}

	public int getX() {
		return point.x;
	}

	public int getY() {
		return point.y;
	}

	public int getZ() {
		return point.z;
	}

}
