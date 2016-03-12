package com.ownxile.rs2.world.object.type;

import com.ownxile.rs2.Point;

public final class GameObject {

	private final int id, type, face;
	private final Point point;

	public GameObject(Point point, int id, int type, int face) {
		this.id = id;
		this.type = type;
		this.point = point;
		this.face = face;
	}

	public int getFace() {
		return face;
	}

	public int id() {
		return id;
	}

	public int type() {
		return type;
	}

	public int x() {
		return point.x;
	}

	public int y() {
		return point.y;
	}

	public int z() {
		return point.z;
	}
}