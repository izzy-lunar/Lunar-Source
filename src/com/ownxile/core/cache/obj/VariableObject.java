package com.ownxile.core.cache.obj;

public class VariableObject {

	private final int face;
	private final int type;
	private final int x;
	private final int y;
	private final int z;

	public VariableObject(int type, int x, int y, int z, int face) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
		this.face = face;
	}

	public int getFace() {
		return face;
	}

	public int getHeight() {
		return z;
	}

	public int getType() {
		return type;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}
