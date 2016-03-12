package com.ownxile.rs2.world.object.type;

public class TemporaryObject {

	public int objectFace;
	public int objectHeight;
	public int objectId;
	public int objectTicks;
	public int objectType;
	public int objectX;
	public int objectY;

	public TemporaryObject(int id, int x, int y, int height, int face,
			int type, int ticks) {
		this.objectId = id;
		this.objectX = x;
		this.objectY = y;
		this.objectHeight = height;
		this.objectFace = face;
		this.objectType = type;
		this.objectTicks = ticks;
	}

	public int getObjectFace() {
		return this.objectFace;
	}

	public int getObjectHeight() {
		return this.objectHeight;
	}

	public int getObjectId() {
		return this.objectId;
	}

	public int getObjectType() {
		return this.objectType;
	}

	public int getObjectX() {
		return this.objectX;
	}

	public int getObjectY() {
		return this.objectY;
	}

}