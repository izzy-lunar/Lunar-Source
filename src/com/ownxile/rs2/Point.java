package com.ownxile.rs2;

import com.ownxile.rs2.player.Client;

public class Point {

	public enum Position {
		DAEMONHEIM_RESPAWN(0, 0, 0), DEMONS_ROOM(3222, 3372, 0), DONOR_ISLAND_SPAWN(
				3676, 2984, 0), EDGEVILLE_BANK(3092, 3492, 0), JAIL_SPAWN(2097,
				4430, 0), LUMBRIDGE_RESPAWN(3222, 3218, 0), NUT_ROOM(3025,
				5457, 0);

		public final int x, y, z;

		private Position(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}

	public final int x, y, z;

	public Point(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
		this.z = 0;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public final void move(Client player, Point point) {
		player.move(point);
	}

}