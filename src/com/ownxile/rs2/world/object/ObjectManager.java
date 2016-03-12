package com.ownxile.rs2.world.object;

import java.util.ArrayList;

import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.Player;
import com.ownxile.rs2.player.PlayerHandler;
import com.ownxile.rs2.world.object.type.Object;
import com.ownxile.util.Misc;

public class ObjectManager {

	public boolean[] activated = { false, false, false, false, false, false };
	public final int IN_USE_ID = 14825;

	public int[][] obeliskCoords = { { 3154, 3618 }, { 3225, 3665 },
			{ 3033, 3730 }, { 3104, 3792 }, { 2978, 3864 }, { 3305, 3914 } };

	public int[] obeliskIds = { 14829, 14830, 14827, 14828, 14826, 14831 };

	public ArrayList<Object> objects = new ArrayList<Object>();

	private final ArrayList<Object> toRemove = new ArrayList<Object>();

	public void addObject(Object o) {
		if (getObject(o.objectX, o.objectY, o.height) == null) {
			objects.add(o);
			placeObject(o);
		}
	}

	public int getObeliskIndex(int id) {
		for (int j = 0; j < obeliskIds.length; j++) {
			if (obeliskIds[j] == id) {
				return j;
			}
		}
		return -1;
	}

	public Object getObject(int x, int y, int height) {
		for (final Object o : objects) {
			if (o.objectX == x && o.objectY == y && o.height == height) {
				return o;
			}
		}
		return null;
	}

	public boolean isObelisk(int id) {
		for (int obeliskId : obeliskIds) {
			if (obeliskId == id) {
				return true;
			}
		}
		return false;
	}

	public boolean loadForPlayer(Object o, Client c) {
		if (o == null || c == null) {
			return false;
		}
		return c.distanceToPoint(o.objectX, o.objectY) <= 60
				&& c.absZ == o.height;
	}

	public void loadObjects(Client c) {
		if (c == null) {
			return;
		}
		for (final Object o : objects) {
			if (loadForPlayer(o, c)) {
				c.getFunction().object(o.objectId, o.objectX, o.objectY,
						o.height, o.face, o.type);
			}
		}
		CustomObjects.showCustomObjects(c);
		if (c.distanceToPoint(2813, 3463) <= 60) {
			c.getFarming().updateHerbPatch(c.objectX, c.objectY);
		}
	}

	public void placeObject(Object o) {
		for (Player player : PlayerHandler.players) {
			if (player != null) {
				final Client c = (Client) player;
				if (c.distanceToPoint(o.objectX, o.objectY) <= 60) {
					c.getFunction().object(o.objectId, o.objectX, o.objectY,
							o.height, o.face, o.type);
				}
			}
		}
	}

	public void removeObject(int x, int y, int z) {
		for (Player player : PlayerHandler.players) {
			if (player != null) {
				final Client c = (Client) player;
				c.getFunction().object(-1, x, y, z, 0, 10);
			}
		}
	}

	public void startObelisk(int obeliskId) {
		final int index = getObeliskIndex(obeliskId);
		if (index >= 0) {
			if (!activated[index]) {
				activated[index] = true;
				addObject(new Object(14825, obeliskCoords[index][0],
						obeliskCoords[index][1], 0, -1, 10, obeliskId, 16));
				addObject(new Object(14825, obeliskCoords[index][0] + 4,
						obeliskCoords[index][1], 0, -1, 10, obeliskId, 16));
				addObject(new Object(14825, obeliskCoords[index][0],
						obeliskCoords[index][1] + 4, 0, -1, 10, obeliskId, 16));
				addObject(new Object(14825, obeliskCoords[index][0] + 4,
						obeliskCoords[index][1] + 4, 0, -1, 10, obeliskId, 16));
				World.getSynchronizedTaskScheduler().schedule(
						new Task(16, false) {
							@Override
							protected void execute() {
								teleportObelisk(index);
								stop();
							}
						});
			}
		}
	}

	public void teleportObelisk(int port) {
		int random = Misc.random(5);
		while (random == port) {
			random = Misc.random(5);
		}
		for (Player player : PlayerHandler.players) {
			if (player != null) {
				final Client c = (Client) player;
				final int xOffset = c.absX - obeliskCoords[port][0];
				final int yOffset = c.absY - obeliskCoords[port][1];
				if (c.goodDistance(c.getX(), c.getY(),
						obeliskCoords[port][0] + 2, obeliskCoords[port][1] + 2,
						1)) {
					c.getFunction().teleport(
							obeliskCoords[random][0] + xOffset,
							obeliskCoords[random][1] + yOffset, 0);
				}

				if (c.goodDistance(c.getX(), c.getY(),
						obeliskCoords[port][0] + 2, obeliskCoords[port][1] + 2,
						5)) {

					c.getFunction().stillGfx(342, obeliskCoords[port][0] + 1,
							obeliskCoords[port][0] + 1, 0, 0);
					c.getFunction().stillGfx(342, obeliskCoords[port][0] + 1,
							obeliskCoords[port][0] + 2, 0, 0);
					c.getFunction().stillGfx(342, obeliskCoords[port][0] + 1,
							obeliskCoords[port][0] + 3, 0, 0);
					

					c.getFunction().stillGfx(342, obeliskCoords[port][0] + 2,
							obeliskCoords[port][0] + 1, 0, 0);
					c.getFunction().stillGfx(342, obeliskCoords[port][0] + 2,
							obeliskCoords[port][0] + 2, 0, 0);
					c.getFunction().stillGfx(342, obeliskCoords[port][0] + 2,
							obeliskCoords[port][0] + 3, 0, 0);
					

					c.getFunction().stillGfx(342, obeliskCoords[port][0] + 3,
							obeliskCoords[port][0] + 1, 0, 0);
					c.getFunction().stillGfx(342, obeliskCoords[port][0] + 3,
							obeliskCoords[port][0] + 2, 0, 0);
					c.getFunction().stillGfx(342, obeliskCoords[port][0] + 3,
							obeliskCoords[port][0] + 3, 0, 0);

				}

			}
		}
		activated[port] = false;
	}

	public void tick() {
		try {
			for (final Object o : objects) {
				if (o.tick > 0) {
					o.tick--;
				} else {
					updateObject(o);
					toRemove.add(o);
				}
			}
			for (final Object o : toRemove) {
				objects.remove(o);
			}
			toRemove.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateObject(Object o) {
		for (Player player : PlayerHandler.players) {
			if (player != null) {
				final Client c = (Client) player;
				c.getFunction().object(o.newId, o.objectX, o.objectY, o.height,
						o.face, o.type);
			}
		}
	}

}