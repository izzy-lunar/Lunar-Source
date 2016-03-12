package com.ownxile.rs2.world.object;

import java.awt.Point;
import java.util.Random;

import com.ownxile.core.World;
import com.ownxile.rs2.items.GroundItemHandler;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.world.games.PartyRoom;
import com.ownxile.rs2.world.object.type.TemporaryObject;

public class BalloonObject extends TemporaryObject {

	static Random r = new Random();
	public static int x;
	public static int y;

	public static BalloonObject getBalloon(int item, int amount) {

		return new BalloonObject(115 + r.nextInt(5), (2730 + r.nextInt(13)),
				(3462 + r.nextInt(13)), 0, 0, 10, 0, item, amount);

	}

	public static Point getCoords() {
		return new Point(x, y);
	}

	public static BalloonObject getEmpty() {

		return new BalloonObject(115 + r.nextInt(5), (2730 + r.nextInt(13)),
				(3462 + r.nextInt(13)), 0, 0, 10, 0, -1, 0);

	}

	public int amount, item;

	public BalloonObject(int id, int x, int y, int height, int face, int type,
			int ticks, int item, int amount) {
		super(id, x, y, height, face, type, ticks);
		BalloonObject.x = x;
		BalloonObject.y = y;
		this.item = item;
		this.amount = amount;
		PartyRoom.balloons.add(this);
	}

	public void pop(Client c, int objectX, int objectY) {
		c.getFunction().walkTo(objectX, objectY);
		if (item < 12000 && item > 0) {
			GroundItemHandler.createGroundItem(c, item, objectX, objectY, 0,
					amount, c.getId());
		}
		c.startAnimation(794);
		PartyRoom.coords.remove(getCoords());
		World.getObjectHandler().globalObject(-1, objectX, objectY, c.absZ, 10);
		World.getObjectHandler().removeObject(this);
		PartyRoom.balloons.remove(this);

	}

	public BalloonObject remove(int x, int y) {
		return new BalloonObject(-1, x, y, 0, 0, 10, 0, 0, 0);
	}

}
