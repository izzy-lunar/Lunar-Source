package com.ownxile.core.cache.obj;

public class StateObject {

	private final int objectFace;
	private final int objectHeight;
	private final int objectStateChange;
	private final int objectType;
	private final int objectVType;
	private final int objectX;
	private final int objectY;

	public StateObject(int objectType, int objectX, int objectY,
			int objectFace, int objectHeight, int objectStateChange,
			int objectVType) {
		this.objectType = objectType;
		this.objectX = objectX;
		this.objectY = objectY;
		this.objectFace = objectFace;
		this.objectHeight = objectHeight;
		this.objectStateChange = objectStateChange;
		this.objectVType = objectVType;
	}

	public int getFace() {
		return objectFace;
	}

	public int getHeight() {
		return objectHeight;
	}

	public int getStatedObject() {
		return objectStateChange;
	}

	public int getType() {
		return objectType;
	}

	public int getVType() {
		return objectVType;
	}

	public int getX() {
		return objectX;
	}

	public int getY() {
		return objectY;
	}

}