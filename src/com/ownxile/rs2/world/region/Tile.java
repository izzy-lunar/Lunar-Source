package com.ownxile.rs2.world.region;

public class Tile {

	private int[] pointer = new int[3];

	public Tile(int x, int y, int z) {
		this.pointer[0] = x;
		this.pointer[1] = y;
		this.pointer[2] = z;
	}

	public int[] getTile() {
		return pointer;
	}

	public int getTileHeight() {
		return pointer[2];
	}

	public int getTileX() {
		return pointer[0];
	}

	public int getTileY() {
		return pointer[1];
	}

}
