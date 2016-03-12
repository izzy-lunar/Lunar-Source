package com.ownxile.rs2;

public enum Location {

	DONOR_ISLAND(3080, 3480, 3100, 3500, 0), VARROCK(0, 0, 0, 0, 0), DESERT(
			3204, 3450, 2700, 3111, 0), EDGEVILLE(3081, 3468, 3200, 3600, 0);

	public final int x, y, x1, y1, z;

	private Location(int x, int x1, int y, int y1, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.x1 = x1;
		this.y1 = y1;
	}

	public final boolean isInLocation(Entity entity) {
		return entity.absX >= x && entity.absX <= x1 && entity.absY >= y
				&& entity.absY <= y1;
	}
}